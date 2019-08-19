package com.shuzhi.entity;

import com.shuzhi.common.basemapper.BaseEntity;
import lombok.Data;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * @author shuzhi
 * @date 2019-08-11 11:14:57
 */

@Table(name = "t_group")
@Data
@EqualsAndHashCode(callSuper = true)
public class Group extends BaseEntity implements Serializable{
private static final long serialVersionUID=1L;

    
    @Id
    private Integer id;
        
    /**
     * 分组类型
     */
    @Column(name = "name")
    private String name;
        
    /**
     * 设备名称
     */
    @Column(name = "device_name")
    private String deviceName;
        
    /**
     * 创建时间
     */
    @Column(name = "creation_time")
    private Date creationTime;
        
    /**
     * 设备类型
     */
    @Column(name = "device_type")
    private Integer deviceType;
        
    /**
     * 分组id
     */
    @Column(name = "show_records_id")
    private Integer showRecordsId;

    /**
     * 设备id
     */
    @Transient
    private Set<Integer> deviceIds;

    /**
     * 设备
     */
    @Transient
    private List<DeviceLoop> deviceLoops;


    /**
     * 设备信息
     */
    @Transient
    private List<DeviceLoop> devices;
    
}
