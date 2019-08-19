package com.shuzhi.entity;

import com.shuzhi.common.basemapper.BaseEntity;
import lombok.Data;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.io.Serializable;


/**
 * @author shuzhi
 * @date 2019-08-08 09:56:30
 */

@Table(name = "t_material_menu")
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialMenu extends BaseEntity implements Serializable{
private static final long serialVersionUID=1L;

    
    @Id
    private Integer id;
        
    /**
     * 名称
     */
    @Column(name = "name")
    private String name;
        
    /**
     * 保存路径
     */
    @Column(name = "path")
    private String path;
        
    /**
     * 创建时间
     */
    @Column(name = "creation_time")
    private Date creationTime;
        
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

}
