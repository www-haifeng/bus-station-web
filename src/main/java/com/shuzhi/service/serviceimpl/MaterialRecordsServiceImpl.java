package com.shuzhi.service.serviceimpl;

import com.shuzhi.common.basemapper.BaseServiceImpl;
import org.springframework.stereotype.Service;

import com.shuzhi.entity.MaterialRecords;
import com.shuzhi.service.MaterialRecordsService;
import org.springframework.transaction.annotation.Transactional;



/**
 * @author zgk
 * @date 2019-08-09 14:38:03
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class MaterialRecordsServiceImpl extends BaseServiceImpl<MaterialRecords> implements MaterialRecordsService {

}