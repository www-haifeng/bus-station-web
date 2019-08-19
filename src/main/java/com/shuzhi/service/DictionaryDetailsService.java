package com.shuzhi.service;

import com.shuzhi.common.basemapper.BaseService;
import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.DictionaryDetails;


/**
 * @author zgk
 * @date 2019-08-09 14:38:03
 */

public interface DictionaryDetailsService extends BaseService<DictionaryDetails> {

    /**
     * 查询节目发布下拉框
     *
     * @return 节目发布下拉框
     */
    Wrapper getRecordsMenu();

    /**
     * 模板选择下拉框
     *
     * @return 模板选择下拉框
     */
    Wrapper getModelMenu();

    /**
     * 素材目录类型选择下拉框
     *
     * @return 素材目录类型选择下拉框
     */
    Wrapper getMaterialMenu();
}