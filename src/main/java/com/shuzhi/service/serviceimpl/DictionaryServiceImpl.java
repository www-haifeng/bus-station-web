package com.shuzhi.service.serviceimpl;

import com.shuzhi.common.basemapper.BaseServiceImpl;
import org.springframework.stereotype.Service;

import com.shuzhi.entity.Dictionary;
import com.shuzhi.service.DictionaryService;
import org.springframework.transaction.annotation.Transactional;



/**
 * @author zgk
 * @date 2019-08-09 14:38:03
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class DictionaryServiceImpl extends BaseServiceImpl<Dictionary> implements DictionaryService {

}