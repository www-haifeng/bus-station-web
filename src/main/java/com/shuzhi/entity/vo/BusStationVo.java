package com.shuzhi.entity.vo;

import com.shuzhi.entity.Station;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import java.util.List;

/**
 * @author zgk
 * @description 公交站列表信息
 * @date 2019-08-20 16:44
 */
@Data
public class BusStationVo{

    private Integer busId;

    /**
     * 第几站
     */
    private Integer location;

    /**
     * 站台名称
     */
    private String name;

    List<Station> stations;

}
