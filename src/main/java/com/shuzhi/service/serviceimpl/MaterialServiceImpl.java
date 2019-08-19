package com.shuzhi.service.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageSerializable;
import com.shuzhi.common.basemapper.BaseServiceImpl;
import com.shuzhi.common.utils.WrapMapper;
import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.Material;
import com.shuzhi.function.Validation;
import com.shuzhi.mapper.MaterialMapper;
import com.shuzhi.mapper.MaterialMenuMapper;
import com.shuzhi.service.MaterialService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.shuzhi.eum.WebEum.*;


/**
 * @author zgk
 * @date 2019-08-08 09:56:30
 */

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MaterialServiceImpl extends BaseServiceImpl<Material> implements MaterialService {

    private final MaterialMapper materialMapper;

    private final MaterialMenuMapper materialMenuMapper;

    public MaterialServiceImpl(MaterialMapper materialMapper, MaterialMenuMapper materialMenuMapper) {
        this.materialMapper = materialMapper;
        this.materialMenuMapper = materialMenuMapper;
    }

    @Value("${menu.http-path}")
    private String httpPath;

    /**
     * 上传文件
     *
     * @param multipartFiles 上传流
     * @param material      文件类型
     * @return 上传结果
     */
    @Override
    public Wrapper uploadMaterial(Material material, MultipartFile[] multipartFiles) {

        //C:\Users\HASEE\Desktop\新建文件夹
        return Optional.ofNullable(validation().check(material)).orElseGet(() -> {
            String path = materialMenuMapper.selectByPrimaryKey(material.getMaterialMenuId()).getPath();
            for (MultipartFile multipartFile : multipartFiles) {
                //检查文件名
                Material materialSelect = new Material();
                String originalFilename = multipartFile.getOriginalFilename();
                materialSelect.setName(originalFilename);
                //检查重复
                if (materialMapper.selectOne(materialSelect) != null) {
                    return WrapMapper.wrap(MATERIAL_ERROR_9.getCode(), MATERIAL_ERROR_9.getMsg());
                }
                //判断文件类型
                String saveHttpPath;
                if (material.getType() == 6) {
                    path = path + "/video/";
                    saveHttpPath = httpPath + "/video/";
                } else if (material.getType() == 3){
                    path = path + "/img/";
                    saveHttpPath = httpPath + "/img/";
                }else {
                    return WrapMapper.wrap(MATERIAL_ERROR_14.getCode(), MATERIAL_ERROR_14.getMsg());
                }
                String filePath = path + originalFilename;
                saveHttpPath = saveHttpPath + originalFilename;
                File file = new File(filePath);
                File file1 = new File(path);
                try {
                    //如果当前路径不存在 就新建一个文件夹
                    if (!file1.exists()) {
                        if (!file1.mkdirs()) {
                            return WrapMapper.wrap(MATERIAL_ERROR_8.getCode(), MATERIAL_ERROR_8.getMsg());
                        }
                    }
                    multipartFile.transferTo(file);
                    //入库 保存文件信息
                    material.setData(multipartFile, saveHttpPath);
                    materialMapper.insertSelective(material);
                } catch (IOException e) {
                    log.error("文件上传失败 ：{} {}", multipartFile.getName(), e.getMessage());
                    return WrapMapper.wrap(MATERIAL_ERROR_7.getCode(), MATERIAL_ERROR_7.getMsg());
                }
            }
            return WrapMapper.handleResult(multipartFiles.length);
        });
    }

    /**
     * 删除资源
     *
     * @param id 资源id
     * @return 删除结果
     */
    @Override
    public Wrapper removeMaterial(Integer id) {

        //查询出资源路径并删除
        Material material = materialMapper.selectByPrimaryKey(id);
        File file = new File(material.getPath());
        if (!file.delete()){
            //查看文件还在不在
            if (file.exists()){
                return WrapMapper.wrap(MATERIAL_ERROR_10.getCode(), MATERIAL_ERROR_10.getMsg());
            }
        }
        materialMapper.deleteByPrimaryKey(id);
        return WrapMapper.ok();
    }

    /**
     * 查询资源 不传id查所有
     *
     * @param material 资源id
     * @return 上传结果
     */
    @Override
    public Wrapper findMaterial(Material material) {
        //id不为空就通过id查
        if (material.getId() != null){
            return WrapMapper.handleResult(materialMapper.selectByPrimaryKey(material.getId()));
        }

        if (material.getType() == null) {
            return WrapMapper.wrap(MATERIAL_ERROR_12.getCode(), MATERIAL_ERROR_12.getMsg());
        }

        //如果没有分页信息添加默认值
        material.setPageNum(Optional.ofNullable(material.getPageNum()).orElse(1));
        material.setPageSize(Optional.ofNullable(material.getPageSize()).orElse(10));
        //名称不为空就以名称模糊查
        if (StringUtils.isNotBlank(material.getName())){
            PageHelper.startPage(material.getPageNum(), material.getPageSize());
            Example example = new Example(Material.class);
            example.createCriteria().andLike("name", "%"+material.getName()+"%").andEqualTo("type", material.getType());
            List<Material> materials = materialMapper.selectByExample(example);
            return WrapMapper.ok(new PageSerializable<>(materials));
        }
        if (material.getMaterialMenuId() == null){
            return WrapMapper.wrap(MATERIAL_ERROR_11.getCode(), MATERIAL_ERROR_11.getMsg());
        }

        Material material1Select = new Material();
        material1Select.setMaterialMenuId(material.getMaterialMenuId());
        material1Select.setType(material.getType());
        //分页
        PageHelper.startPage(material.getPageNum(), material.getPageSize());
        List<Material> select = materialMapper.select(material1Select);
        return WrapMapper.ok(new PageSerializable<>(select));
    }


    private Validation<Material> validation() {

        return material -> {
            if (material == null) {
                return WrapMapper.wrap(MATERIAL_ERROR_5.getCode(), MATERIAL_ERROR_5.getMsg());
            }
            return null;
        };
    }
}