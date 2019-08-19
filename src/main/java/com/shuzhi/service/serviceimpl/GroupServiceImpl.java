package com.shuzhi.service.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageSerializable;
import com.shuzhi.common.utils.WrapMapper;
import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.DeviceLoop;
import com.shuzhi.function.Validation;
import com.shuzhi.mapper.DeviceLoopMapper;
import com.shuzhi.mapper.GroupMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.shuzhi.entity.Group;
import com.shuzhi.service.GroupService;
import com.shuzhi.common.basemapper.BaseServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.shuzhi.eum.WebEum.*;


/**
 * @author shuzhi
 * @date 2019-08-11 11:14:57
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class GroupServiceImpl extends BaseServiceImpl<Group> implements GroupService {

    private final GroupMapper groupMapper;

    private final DeviceLoopMapper deviceLoopMapper;

    public GroupServiceImpl(GroupMapper groupMapper, DeviceLoopMapper deviceLoopMapper) {
        this.groupMapper = groupMapper;
        this.deviceLoopMapper = deviceLoopMapper;
    }

    /**
     * 查询分组信息 传id查单个 不传id查所有
     *
     * @param group 分页信息和设备类型
     * @return 查询结果
     */
    @Override
    public Wrapper findGroup(Group group) {
        //有id就查单条信息
        if (group.getId() != null) {
            return WrapMapper.ok(groupMapper.findById(group.getId(), group.getDeviceType()));
        }
        //设备类型不能为空
        if (group.getDeviceType() == null) {
            return WrapMapper.wrap(SHOW_RECORDS_ERROR_01.getCode(), SHOW_RECORDS_ERROR_01.getMsg());
        }
        //没id就查所有
        group.setPageNum(Optional.ofNullable(group.getPageNum()).orElse(1));
        group.setPageSize(Optional.ofNullable(group.getPageSize()).orElse(10));
        PageHelper.startPage(group.getPageNum(), group.getPageSize());
        Group groupSelect = new Group();
        groupSelect.setDeviceType(group.getDeviceType());
        List<Group> select = groupMapper.select(groupSelect);

        return WrapMapper.ok(new PageSerializable<>(select));
    }

    /**
     * 删除组id
     *
     * @param id 组id
     * @return 删除结果
     */
    @Override
    public Wrapper removeGroup(Integer id) {
        //查询要删除的信息是否还存在
        if (groupMapper.selectByPrimaryKey(id) != null){
            //删除组
            groupMapper.deleteByPrimaryKey(id);
            //将设备的组剔除
            DeviceLoop deviceLoopSelect = new DeviceLoop();
            deviceLoopSelect.setGroupId(id);
            List<DeviceLoop> select1 = deviceLoopMapper.select(deviceLoopSelect);
            if (select1 != null && select1.size() != 0) {
                select1.forEach(deviceLoop -> {
                    deviceLoop.setGroupId(null);
                    deviceLoopMapper.updateByPrimaryKey(deviceLoop);
                });
            }
        }
        return WrapMapper.ok();
    }

    /**
     * 增加组
     *
     * @param group 组信息
     * @return 增加结果
     */
    @Override
    public Wrapper addGroup(Group group) {

        return Optional.ofNullable(validation().check(group)).orElseGet(() -> {

            int save = 0;

            group.setCreationTime(new Date());
            //保存到设备表
            if (group.getDeviceIds() != null) {
                //拼接设备名称
                StringBuilder stringBuilder = new StringBuilder();
                group.getDeviceIds().forEach(id -> stringBuilder.append(deviceLoopMapper.selectByPrimaryKey(id).getDeviceName()).append(" "));
                group.setDeviceName(stringBuilder.toString());
                save = groupMapper.insertSelective(group);
                //保存到设备表
                group.getDeviceIds().forEach(id -> {
                    DeviceLoop deviceLoopSelect = new DeviceLoop();
                    deviceLoopSelect.setGroupId(groupMapper.selectOne(group).getId());
                    deviceLoopSelect.setId(id);
                    deviceLoopMapper.updateByPrimaryKeySelective(deviceLoopSelect);
                });
            }
            //保存
            return WrapMapper.ok(save);
        });
    }

    /**
     * 更新组
     *
     * @param group 组信息
     * @return 更新结果
     */
    @Override
    public Wrapper updateGroup(Group group) {
        //验证信息
        if (group.getId() == null ){
           return WrapMapper.wrap(GROUP_ERROR_06.getCode(), GROUP_ERROR_06.getMsg());
        }
        if (groupMapper.selectByPrimaryKey(group.getId()) != null) {
            //删除组
            removeGroup(group.getId());
            //添加组
            group.setId(null);
            addGroup(group);
            return WrapMapper.ok();
        }
        return WrapMapper.wrap(GROUP_ERROR_03.getCode(), GROUP_ERROR_03.getMsg());
    }

    /**
     * 返回设备列表
     *
     * @param id 设备类型
     * @return 设备列表
     */
    @Override
    public Wrapper getDeviceMenu(Integer id) {
        if (id == null){
            WrapMapper.wrap(GROUP_ERROR_01.getCode(), GROUP_ERROR_01.getMsg());
        }
        //查出没有绑定的设备并返回
        return WrapMapper.ok(deviceLoopMapper.getDeviceMenu(id));
    }

    /**
     * 分组名称下拉框
     *
     * @return 分组名称下拉框
     */
    @Override
    public Wrapper getGroupMenu() {
        return WrapMapper.ok(groupMapper.getGroupMenu());
    }


    private Validation<Group> validation() {

        return group -> {
            Group groupSelect = new Group();
            //设备类型不能为空
            if (group.getDeviceType() == null) {
                return WrapMapper.wrap(SHOW_RECORDS_ERROR_01.getCode(), SHOW_RECORDS_ERROR_01.getMsg());
            }
            //检查分组名称
            if (StringUtils.isBlank(group.getName())) {
                return WrapMapper.wrap(GROUP_ERROR_01.getCode(), GROUP_ERROR_01.getMsg());
            }
            groupSelect.setName(group.getName());
            List<Group> select = groupMapper.select(groupSelect);
            if (select.size() != 0) {
                return WrapMapper.wrap(GROUP_ERROR_02.getCode(), GROUP_ERROR_02.getMsg());
            }
            if(group.getDeviceIds() == null){
                return WrapMapper.wrap(GROUP_ERROR_05.getCode(), GROUP_ERROR_05.getMsg());
            }
            return null;
        };
    }
}