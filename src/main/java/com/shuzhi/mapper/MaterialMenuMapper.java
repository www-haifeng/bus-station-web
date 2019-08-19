package com.shuzhi.mapper;

import com.shuzhi.entity.MaterialMenu;
import org.springframework.stereotype.Repository;
import com.shuzhi.common.basemapper.MyBaseMapper;

import java.util.List;


/**
 * @author shuzhi
 * @date 2019-08-08 09:56:30
 */

@Repository
public interface MaterialMenuMapper extends MyBaseMapper<MaterialMenu> {

    /**
     * 查看素材目录
     *
     * @return 查询结果
     */
    List<MaterialMenu> selectMenu();
}
