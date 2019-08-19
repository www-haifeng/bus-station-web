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

@Table(name = "t_dictionary_details")
@Data
@EqualsAndHashCode(callSuper = true)
public class DictionaryDetails extends BaseEntity implements Serializable{
private static final long serialVersionUID=1L;

    
    @Id
    private Integer id;
        
    /**
     * 名称
     */
    @Column(name = "name")
    private String name;
        
    /**
     * 字典id
     */
    @Column(name = "dictionary_id")
    private Integer dictionaryId;
        
    /**
     * 创建时间
     */
    @Column(name = "creation_time")
    private Date creationTime;

    /**
     * 创建时间
     */
    @Column(name = "code")
    private String code;

    public DictionaryDetails(int dictionaryId) {
        this.dictionaryId = dictionaryId;
    }

    public DictionaryDetails() {

    }
}
