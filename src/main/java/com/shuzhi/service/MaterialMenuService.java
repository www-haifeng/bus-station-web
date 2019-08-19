package com.shuzhi.service;

import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.MaterialMenu;
import com.shuzhi.common.basemapper.BaseService;


/**
 * @author shuzhi
 * @date 2019-08-08 09:56:30
 */

public interface MaterialMenuService extends BaseService<MaterialMenu> {

    /**
     * 新增素材目录
     *
     * @param materialMenu 素材目录信息
     * @return 保存结果
     */
    Wrapper addMaterialMenu(MaterialMenu materialMenu);

    /**
     * 更新素材目录
     *
     * @param materialMenu 素材目录信息
     * @return 更新结果
     */
    Wrapper updateMaterialMenu(MaterialMenu materialMenu);

    /**
     * 删除素材目录
     *
     * @param id 素材目录id
     * @return 更新结果
     */
    Wrapper removeMaterialMenu(Integer id);

    /**
     * 查看素材目录
     *
     * @return 更新结果
     */
    Wrapper findMaterialMenu();
}