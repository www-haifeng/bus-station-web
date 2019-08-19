package com.shuzhi.service.serviceimpl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageSerializable;
import com.shuzhi.common.basemapper.BaseServiceImpl;
import com.shuzhi.common.utils.WrapMapper;
import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.*;
import com.shuzhi.entity.vo.JsonVo;
import com.shuzhi.function.Validation;
import com.shuzhi.mapper.*;
import com.shuzhi.service.ShowRecordsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.shuzhi.eum.WebEum.*;


/**
 * @author zgk
 * @date 2019-08-09 14:38:03
 */

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ShowRecordsServiceImpl extends BaseServiceImpl<ShowRecords> implements ShowRecordsService {

    private final ShowRecordsMapper showRecordsMapper;

    private final MaterialRecordsMapper materialRecordsMapper;

    private final GroupMapper groupMapper;

    private final DictionaryDetailsMapper dictionaryDetailsMapper;

    private final DeviceLoopMapper deviceLoopMapper;

    private final MaterialMapper materialMapper;

    public ShowRecordsServiceImpl(ShowRecordsMapper showRecordsMapper, MaterialRecordsMapper materialRecordsMapper, GroupMapper groupMapper, DictionaryDetailsMapper dictionaryDetailsMapper, DeviceLoopMapper deviceLoopMapper, MaterialMapper materialMapper) {
        this.showRecordsMapper = showRecordsMapper;
        this.materialRecordsMapper = materialRecordsMapper;
        this.groupMapper = groupMapper;
        this.dictionaryDetailsMapper = dictionaryDetailsMapper;
        this.deviceLoopMapper = deviceLoopMapper;
        this.materialMapper = materialMapper;
    }

    @Value("${menu.path}")
    private String path;

    @Value("${menu.json-path}")
    private String jsonPath;

    /**
     * 分页查询
     *
     * @param showRecords 分页参数和设备类型
     * @return 分页结果
     */
    @Override
    public Wrapper findShowRecords(ShowRecords showRecords) {

        //id不为空查单条
        if (showRecords.getId() != null){
            return WrapMapper.ok(showRecordsMapper.selectByPrimaryKey(showRecords.getId()));
        }
        //参数验证
        return Optional.ofNullable(validation().check(showRecords)).orElseGet(() -> {
            //分页
            PageHelper.startPage(showRecords.getPageNum(), showRecords.getPageSize());
            return WrapMapper.ok(new PageSerializable<>(showRecordsMapper.select(showRecords)));
        });
    }

    /**
     * 通过节目发布id查询节目详情
     *
     * @param id 节目发布id
     * @return 详情信息
     */
    @Override
    public Wrapper findShowRecordsById(Integer id) {
        return WrapMapper.ok(showRecordsMapper.findShowRecordsById(id));
    }

    /**
     * 删除节目记录
     *
     * @param id 节目发布id
     * @return 删除结果
     */
    @Override
    public Wrapper removeShowRecordsById(Integer id) {
        //先查该资源是否还存在
        if (showRecordsMapper.selectByPrimaryKey(id) != null){
            showRecordsMapper.deleteByPrimaryKey(id);
            //删除中间表的信息
            MaterialRecords materialRecords = new MaterialRecords();
            materialRecords.setShowRecordsId(id);
            materialRecordsMapper.delete(materialRecords);
            //清除分组的记录
            Group groupSelect = new Group();
            groupSelect.setShowRecordsId(id);
            groupMapper.select(groupSelect).forEach(group -> {
                group.setShowRecordsId(null);
                groupMapper.updateByPrimaryKey(group);
            });
        }
        return WrapMapper.ok();
    }

    /**
     * 新增节目记录
     *
     * @param showRecords 节目信息
     * @return 新增节目记录
     */
    @Override
    public Wrapper addShowRecords(ShowRecords showRecords) {
        return Optional.ofNullable(validation().check(showRecords)).orElseGet(() -> {
            if (StringUtils.isBlank(showRecords.getName())) {
                return WrapMapper.wrap(SHOW_RECORDS_ERROR_02.getCode(), SHOW_RECORDS_ERROR_02.getMsg());
            }
            //查重复
            ShowRecords showRecordsSelect = new ShowRecords();
            showRecordsSelect.setName(showRecords.getName());
            if (showRecordsMapper.select(showRecordsSelect).size() != 0) {
                return WrapMapper.wrap(SHOW_RECORDS_ERROR_07.getCode(), SHOW_RECORDS_ERROR_07.getMsg());
            }
            if (showRecords.getType() == null) {
                return WrapMapper.wrap(SHOW_RECORDS_ERROR_03.getCode(), SHOW_RECORDS_ERROR_03.getMsg());
            }
            List<Integer> groupIds = showRecords.getGroupIds();
            if (groupIds == null || groupIds.size() == 0) {
                return WrapMapper.wrap(SHOW_RECORDS_ERROR_05.getCode(), SHOW_RECORDS_ERROR_05.getMsg());
            }
            //保存节目类型名称
            List<DictionaryDetails> detailsList = dictionaryDetailsMapper.select(new DictionaryDetails(3));
            detailsList.forEach(dictionaryDetails -> {
                if (showRecords.getType().equals(dictionaryDetails.getId())) {
                    showRecords.setTypeName(dictionaryDetails.getName());
                }
            });
            if (StringUtils.isBlank(showRecords.getTypeName())){
                return WrapMapper.wrap(SHOW_RECORDS_ERROR_04.getCode(), SHOW_RECORDS_ERROR_04.getMsg());
            }
            showRecords.setCreationTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            StringBuilder stringBuilder = new StringBuilder();
            //获取并组装分组名
            showRecords.getGroupIds().forEach(id -> {
                Group group = groupMapper.selectByPrimaryKey(id);
                stringBuilder.append(group.getName()).append(" ");
                showRecords.setLocation(stringBuilder.toString());
            });
            int i = showRecordsMapper.insertSelective(showRecords);
            //如果是单色屏的文字
            if (StringUtils.isNotBlank(showRecords.getLedText())) {
                showRecords.setTypeName("文字");
                setJson();
                return WrapMapper.handleResult(i);
            }
            if (showRecords.getMaterialIds() == null || showRecords.getMaterialIds().size() == 0) {
                return WrapMapper.wrap(SHOW_RECORDS_ERROR_06.getCode(), SHOW_RECORDS_ERROR_06.getMsg());
            }
            if (showRecords.getGroupIds() == null || showRecords.getGroupIds().size() == 0) {
                return WrapMapper.wrap(SHOW_RECORDS_ERROR_08.getCode(), SHOW_RECORDS_ERROR_08.getMsg());
            }
            //保存设备分组
            //回查主键
            ShowRecords showRecords1 = showRecordsMapper.selectOne(showRecords);
            groupIds.forEach(id -> {
                Group group = new Group();
                group.setId(id);
                group.setShowRecordsId(showRecords1.getId());
                groupMapper.updateByPrimaryKeySelective(group);
            });
            //保存到资源和节目记录中间表
            showRecords.getMaterialIds().forEach(id -> {
                MaterialRecords materialRecords = new MaterialRecords(showRecords1.getId(), id);
                materialRecordsMapper.insertSelective(materialRecords);
            });
            //写入json文件
            setJson();
            return WrapMapper.handleResult(i);
        });
    }

    /**
     * 删除并重新生成json文件
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void setJson() {
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                log.error("创建文件夹失败 : {}", path);
            }
        }
        //删除json文件
        File jsonFile = new File(jsonPath);
        jsonFile.delete();
        //写入新的文件
        //查出所有的文件类型
        List<ShowRecords> showRecords = showRecordsMapper.selectAll();
        DeviceLoop deviceLoopSelect = new DeviceLoop();
        MaterialRecords materialRecordsSelect = new MaterialRecords();
        HashMap<String, JsonVo> jsonVoHashMap = new HashMap<>(16);
        showRecords.forEach(showRecords1 -> {
            Integer type = showRecords1.getType();
            JsonVo jsonVo = new JsonVo();
            Example example = new Example(Group.class);
            example.createCriteria().andIsNotNull("showRecordsId");
            List<Group> groups = groupMapper.selectByExample(example);
            if (groups != null && groups.size() != 0) {
                groups.forEach(group -> {
                    //判断类型是否相同
                    if (showRecordsMapper.selectByPrimaryKey(group.getShowRecordsId()).getType().equals(type)) {
                        deviceLoopSelect.setGroupId(group.getId());
                        List<DeviceLoop> select = deviceLoopMapper.select(deviceLoopSelect);
                        //封装设备id
                        select.forEach(deviceLoop -> jsonVo.getId().add(deviceLoop.getId()));
                        //如果是文字
                        if (type == 5) {
                            jsonVo.getSource().add(showRecords1.getLedText());
                        }else {
                            //封装资源
                            materialRecordsSelect.setShowRecordsId(group.getShowRecordsId());
                            List<MaterialRecords> select1 = materialRecordsMapper.select(materialRecordsSelect);
                            select1.forEach(materialRecords -> jsonVo.getSource().add(materialMapper.selectByPrimaryKey(materialRecords.getMaterialId()).getPath()));
                        }
                    }
                });
            }
            jsonVoHashMap.put(dictionaryDetailsMapper.selectByPrimaryKey(type).getCode(), jsonVo);
        });
        String dataJson = JSON.toJSONString(jsonVoHashMap);
        try {
            FileCopyUtils.copy(dataJson.getBytes(), jsonFile);
        } catch (IOException e) {
            log.error("写入json文件失败 : {}",e.getMessage());
            e.printStackTrace();
        }
    }


    private Validation<ShowRecords> validation() {

        return showRecords -> {
            //设备类型不能为空
            if (showRecords.getDeviceType() == null) {
                return WrapMapper.wrap(SHOW_RECORDS_ERROR_01.getCode(), SHOW_RECORDS_ERROR_01.getMsg());
            }
            return null;
        };
    }
}