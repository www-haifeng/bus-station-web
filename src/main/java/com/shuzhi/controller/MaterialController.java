package com.shuzhi.controller;

import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.Material;
import com.shuzhi.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.Callable;


/**
 * @author zgk
 * @date 2019-08-08 09:56:30
 */

@RestController
@RequestMapping(value = "/material")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    /**
     * 以多线程的方式上传文件
     *
     * @param multipartFile 上传流
     * @param material 文件信息
     * @return 上传结果
     */
    @RequestMapping("/uploadMaterial")
    public Callable<Wrapper> uploadMaterial(Material material, MultipartFile[] multipartFile){
        return () -> materialService.uploadMaterial(material,multipartFile);
    }

    /**
     * 删除文件
     *
     * @param id 资源id
     * @return 删除结果
     *
     */
    @RequestMapping("/removeMaterial/{id}")
    public Wrapper removeMaterial(@PathVariable Integer id){
        return materialService.removeMaterial(id);
    }

    /**
     * 查询资源 不传id查所有
     *
     * @param material 资源id
     * @return 查询结果
     */
    @RequestMapping("/findMaterial")
    public Wrapper findMaterial(@RequestBody Material material){
        return materialService.findMaterial(material);
    }
}