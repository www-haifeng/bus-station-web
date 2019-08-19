package com.shuzhi.mapper;

import com.shuzhi.common.basemapper.MyBaseMapper;
import com.shuzhi.entity.DictionaryDetails;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author zgk
 * @date 2019-08-09 14:38:03
 */

@Repository
public interface DictionaryDetailsMapper extends MyBaseMapper<DictionaryDetails> {

    /**
     * 查询节目发布下拉框
     *
     * @return 节目发布下拉框
     */
    List<DictionaryDetails> getRecordsMenu();

    /**
     * 模板选择下拉框
     *
     * @return 模板选择下拉框
     */
    List<DictionaryDetails> getModelMenu();

    /**
     * 素材目录类型选择下拉框
     *
     * @return 素材目录类型选择下拉框
     */
    List<DictionaryDetails> getMaterialMenu();
}
