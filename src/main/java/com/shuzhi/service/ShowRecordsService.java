package com.shuzhi.service;

import com.shuzhi.common.basemapper.BaseService;
import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.ShowRecords;


/**
 * @author zgk
 * @date 2019-08-09 14:38:03
 */

public interface ShowRecordsService extends BaseService<ShowRecords> {

    /**
     * 分页查询
     *
     * @param showRecords 分页参数和设备类型
     * @return 分页结果
     */
    Wrapper findShowRecords(ShowRecords showRecords);

    /**
     * 通过节目发布id查询节目详情
     *
     * @param id 节目发布id
     * @return 详情信息
     */
    Wrapper findShowRecordsById(Integer id);

    /**
     * 删除节目记录
     *
     * @param id 节目发布id
     * @return 删除结果
     */
    Wrapper removeShowRecordsById(Integer id);

    /**
     * 新增节目记录
     *
     * @param showRecords 节目信息
     * @return 新增节目记录
     */
    Wrapper addShowRecords(ShowRecords showRecords);
}