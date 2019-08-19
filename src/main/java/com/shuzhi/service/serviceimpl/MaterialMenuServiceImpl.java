package com.shuzhi.service.serviceimpl;

import com.shuzhi.common.basemapper.BaseServiceImpl;
import com.shuzhi.common.utils.WrapMapper;
import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.MaterialMenu;
import com.shuzhi.function.Validation;
import com.shuzhi.mapper.MaterialMenuMapper;
import com.shuzhi.service.MaterialMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static com.shuzhi.eum.WebEum.*;


/**
 * @author zgk
 * @date 2019-08-08 09:56:30
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class MaterialMenuServiceImpl extends BaseServiceImpl<MaterialMenu> implements MaterialMenuService {

    private final MaterialMenuMapper materialMenuMapper;

    public MaterialMenuServiceImpl(MaterialMenuMapper materialMenuMapper) {
        this.materialMenuMapper = materialMenuMapper;
    }

    @Value("${menu.path}")
    private String path;


    /**
     * 新增素材目录
     *
     * @param materialMenu 素材目录信息
     * @return 保存结果
     */
    @Override
    public Wrapper addMaterialMenu(MaterialMenu materialMenu) {

        //验证参数 并返回保存结果
        return Optional.ofNullable(validation().check(materialMenu)).orElseGet(() -> {
            materialMenu.setCreationTime(new Date());
            materialMenu.setPath(path);
            return WrapMapper.handleResult(materialMenuMapper.insertSelective(materialMenu));
        });
    }

    /**
     * 更新素材目录
     *
     * @param materialMenu 素材目录信息
     * @return 更新结果
     */
    @Override
    public Wrapper updateMaterialMenu(MaterialMenu materialMenu) {
        //验证参数 并返回保存结果
        return Optional.ofNullable(validation().check(materialMenu)).orElseGet(() -> {
            materialMenu.setUpdateTime(new Date());
            return WrapMapper.handleResult(materialMenuMapper.updateByPrimaryKeySelective(materialMenu));
        });
    }

    /**
     * 删除素材目录
     *
     * @param id 素材目录id
     * @return 更新结果
     */
    @Override
    public Wrapper removeMaterialMenu(Integer id) {
        materialMenuMapper.deleteByPrimaryKey(id);
        return WrapMapper.ok();
    }

    /**
     * 查看素材目录
     *
     * @return 查询结果
     */
    @Override
    public Wrapper findMaterialMenu() {
        return WrapMapper.ok(materialMenuMapper.selectMenu());
    }

    private Validation<MaterialMenu> validation() {

        MaterialMenu materialMenuSelect = new MaterialMenu();
        return materialMenu -> {
            //更新
            if (materialMenu.getId() != null && materialMenuMapper.selectByPrimaryKey(materialMenu.getId()) == null){
                return WrapMapper.wrap(MATERIAL_ERROR_4.getCode(), MATERIAL_ERROR_4.getMsg());
            }
            //验证目录名是否重复
            if (StringUtils.isBlank(materialMenu.getName())) {
                return WrapMapper.wrap(MATERIAL_ERROR_1.getCode(), MATERIAL_ERROR_1.getMsg());
            }
            materialMenuSelect.setName(materialMenu.getName());
            if (materialMenuMapper.select(materialMenu).size() != 0) {
                return WrapMapper.wrap(MATERIAL_ERROR_2.getCode(), MATERIAL_ERROR_2.getMsg());
            }
            return null;
        };
    }
}