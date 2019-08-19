package com.shuzhi.entity;

import com.shuzhi.common.basemapper.BaseEntity;
import lombok.Data;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.io.Serializable;


/**
 * @author zgk
 * @date 2019-08-09 14:38:03
 */

@Table(name = "t_dictionary")
@Data
@EqualsAndHashCode(callSuper = true)
public class Dictionary extends BaseEntity implements Serializable{
private static final long serialVersionUID=1L;

    
    @Id
    private Integer id;
        
    /**
     * 字典名
     */
    @Column(name = "name")
    private String name;
        
    /**
     * 创建时间
     */
    @Column(name = "creation_time")
    private Date creationTime;
    
}
