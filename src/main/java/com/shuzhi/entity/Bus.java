package com.shuzhi.entity;

import com.shuzhi.common.basemapper.BaseEntity;
import lombok.Data;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.io.Serializable;


/**
 * @author shuzhi
 * @date 2019-08-20 15:32:51
 */

@Table(name = "t_bus")
@Data
@EqualsAndHashCode(callSuper = true)
public class Bus extends BaseEntity implements Serializable{
private static final long serialVersionUID=1L;

    
    @Id
    private Integer busId;
        
    /**
     * 站台名称
     */
    @Column(name = "name")
    private String name;
        
    /**
     * 第几站
     */
    @Column(name = "location")
    private Integer location;
        
    /**
     * 站台说明   
     */
    @Column(name = "describe")
    private String describe;
    
}
