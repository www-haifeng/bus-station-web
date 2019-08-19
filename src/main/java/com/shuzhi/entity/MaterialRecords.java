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

@Table(name = "t_material_records")
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialRecords extends BaseEntity implements Serializable{
private static final long serialVersionUID=1L;

    
    @Id
    private Integer id;
        
    /**
     * 资源id
     */
    @Column(name = "material_id")
    private Integer materialId;
        
    /**
     * 节目记录id
     */
    @Column(name = "show_records_id")
    private Integer showRecordsId;
        
    /**
     * 创建时间
     */
    @Column(name = "creation_time")
    private Date creationTime;

    public MaterialRecords(Integer showRecordId, Integer materialId) {
        this.materialId = materialId;
        this.showRecordsId = showRecordId;
        this.creationTime = new Date();
    }

    public MaterialRecords() {

    }
}
