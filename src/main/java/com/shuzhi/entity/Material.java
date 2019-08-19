package com.shuzhi.entity;

import com.shuzhi.common.basemapper.BaseEntity;
import lombok.Data;

import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.Serializable;


/**
 * @author shuzhi
 * @date 2019-08-08 09:56:30
 */

@Table(name = "t_material")
@Data
@EqualsAndHashCode(callSuper = true)
public class Material extends BaseEntity implements Serializable{
private static final long serialVersionUID=1L;

    
    @Id
    private Integer id;
        
    /**
     * 名称
     */
    @Column(name = "name")
    private String name;
        
    /**
     * 类型 1视频 2图片 3音频 4文字
     */
    @Column(name = "type")
    private Integer type;
        
    /**
     * 素材目录id
     */
    @Column(name = "material_menu_id")
    private Integer materialMenuId;
        
    /**
     * 创上传时间
     */
    @Column(name = "creation_time")
    private String creationTime;
        
    /**
     * 保存路径
     */
    @Column(name = "path")
    private String path;
        
    /**
     * 文件大小
     */
    @Column(name = "file_size")
    private String  fileSize;

    public void setData(MultipartFile multipartFile, String filePath) {

        DecimalFormat df = new DecimalFormat("0.00MB");

        this.name = multipartFile.getOriginalFilename();
        this.creationTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.fileSize = df.format((double)multipartFile.getSize() / 1024 / 1024);
        this.path = filePath;
    }
}
