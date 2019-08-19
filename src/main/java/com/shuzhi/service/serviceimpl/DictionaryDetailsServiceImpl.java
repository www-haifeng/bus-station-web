package com.shuzhi.service.serviceimpl;

import com.shuzhi.common.basemapper.BaseServiceImpl;
import com.shuzhi.common.utils.WrapMapper;
import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.mapper.DictionaryDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuzhi.entity.DictionaryDetails;
import com.shuzhi.service.DictionaryDetailsService;
import org.springframework.transaction.annotation.Transactional;



/**
 * @author zgk
 * @date 2019-08-09 14:38:03
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class DictionaryDetailsServiceImpl extends BaseServiceImpl<DictionaryDetails> implements DictionaryDetailsService {

    private final DictionaryDetailsMapper dictionaryDetailsMapper;

    public DictionaryDetailsServiceImpl(DictionaryDetailsMapper dictionaryDetailsMapper) {
        this.dictionaryDetailsMapper = dictionaryDetailsMapper;
    }

    /**
     * 查询节目发布下拉框
     *
     * @return 节目发布下拉框
     */
    @Override
    public Wrapper getRecordsMenu() {
        return WrapMapper.ok(dictionaryDetailsMapper.getRecordsMenu());
    }

    /**
     * 模板选择下拉框
     *
     * @return 模板选择下拉框
     */
    @Override
    public Wrapper getModelMenu() {
        return WrapMapper.ok(dictionaryDetailsMapper.getModelMenu());
    }

    /**
     * 素材目录类型选择下拉框
     *
     * @return 素材目录类型选择下拉框
     */
    @Override
    public Wrapper getMaterialMenu() {
        return WrapMapper.ok(dictionaryDetailsMapper.getMaterialMenu());
    }
}