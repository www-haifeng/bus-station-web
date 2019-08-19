package com.shuzhi.controller;

import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.MaterialMenu;
import com.shuzhi.service.MaterialMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * 素材目录
 *
 * @author zgk
 * @date 2019-08-08 09:56:30
 */

@RestController
@RequestMapping(value = "/materialMenu")
public class MaterialMenuController {

    private final MaterialMenuService materialMenuService;

    public MaterialMenuController(MaterialMenuService materialMenuService) {
        this.materialMenuService = materialMenuService;
    }

    /**
     * 新增素材目录
     *
     * @param materialMenu 素材目录信息
     * @return 保存结果
     */
    @RequestMapping("/addMaterialMenu")
    public Wrapper addMaterialMenu(@RequestBody MaterialMenu materialMenu){
        return materialMenuService.addMaterialMenu(materialMenu);
    }

    /**
     * 更新素材目录
     *
     * @param materialMenu 素材目录信息
     * @return 更新结果
     */
    @RequestMapping("/updateMaterialMenu")
    public Wrapper updateMaterialMenu(@RequestBody MaterialMenu materialMenu){
        return materialMenuService.updateMaterialMenu(materialMenu);
    }

    /**
     * 删除素材目录
     *
     * @param id 素材目录id
     * @return 更新结果
     */
    @RequestMapping("/removeMaterialMenu/{id}")
    public Wrapper removeMaterialMenu(@PathVariable Integer id){
        return materialMenuService.removeMaterialMenu(id);
    }

    /**
     * 查看素材目录
     *
     * @return 更新结果
     */
    @RequestMapping("/findMaterialMenu")
    public Wrapper findMaterialMenu(){
        return materialMenuService.findMaterialMenu();
    }

}