package com.shuzhi.entity;

import com.shuzhi.common.basemapper.BaseEntity;
import lombok.Data;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.io.Serializable;
import java.util.List;


/**
 * @author zgk
 * @date 2019-08-09 14:38:03
 */

@Table(name = "t_show_records")
@Data
@EqualsAndHashCode(callSuper = true)
public class ShowRecords extends BaseEntity implements Serializable{
private static final long serialVersionUID=1L;

    
    @Id
    private Integer id;
        
    /**
     * 节目名称
     */
    @Column(name = "name")
    private String name;
        
    /**
     * 发布时间
     */
    @Column(name = "creation_time")
    private String creationTime;
        
    /**
     * 节目类型
     */
    @Column(name = "type")
    private Integer type;
        
    /**
     * 设备位置
     */
    @Column(name = "location")
    private String location;
        
    /**
     * 设备类型
     */
    @Column(name = "device_type")
    private Integer deviceType;

    /**
     * 节目类型名称
     */
    @Column(name = "type_name")
    private String typeName;

    /**
     * led单色屏字幕
     */
    @Column(name = "led_text")
    private String ledText;

    /**
     * 资源id
     */
    @Transient
    private List<Integer> materialIds;

    /**
     * 组id
     */
    @Transient
    private List<Integer> groupIds;

    
}
