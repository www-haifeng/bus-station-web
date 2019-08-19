package com.shuzhi.mapper;

import com.shuzhi.common.basemapper.MyBaseMapper;
import com.shuzhi.entity.Material;
import com.shuzhi.entity.ShowRecords;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
 * @author zgk
 * @date 2019-08-09 14:38:03
 */

@Repository
public interface ShowRecordsMapper extends MyBaseMapper<ShowRecords> {

    /**
     * 通过节目发布id查询节目详情
     *
     * @param id 节目发布id
     * @return 详情信息
     */
    List<Material> findShowRecordsById(@Param("id") Integer id);

    /**
     * 查出所有的类型
     *
     * @return 所有的类型
     */
    Set<Integer> findType();
}
