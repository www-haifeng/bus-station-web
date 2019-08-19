package com.shuzhi.service;

import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.Material;
import com.shuzhi.common.basemapper.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.Callable;


/**
 * @author zgk
 * @date 2019-08-08 09:56:30
 */

public interface MaterialService extends BaseService<Material> {

    /**
     * 上传文件
     *
     * @param multipartFile 上传流
     * @param material 文件目录路径
     * @return 上传结果
     */
    Wrapper uploadMaterial(Material material, MultipartFile[] multipartFile);

    /**
     * 以多线程的方式上传文件
     *
     * @param id 资源id
     * @return 上传结果
     */
    Wrapper removeMaterial(Integer id);

    /**
     * 查询资源 不传id查所有
     *
     * @param material 资源id
     * @return 上传结果
     */
    Wrapper findMaterial(Material material);
}