package com.shuzhi.controller;

import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.service.DictionaryDetailsService;
import com.shuzhi.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author zgk
 * @date 2019-08-09 14:38:03
 */

@RestController
@RequestMapping(value = "/dictionaryDetails")
public class DictionaryDetailsController {

    private final DictionaryDetailsService dictionaryDetailsService;

    private final GroupService groupService;

    public DictionaryDetailsController(DictionaryDetailsService dictionaryDetailsService, GroupService groupService) {
        this.dictionaryDetailsService = dictionaryDetailsService;
        this.groupService = groupService;
    }

    /**
     * 查询节目发布下拉框
     *
     * @return 节目发布下拉框
     */
    @RequestMapping("/getRecordsMenu")
    public Wrapper getRecordsMenu(){
       return dictionaryDetailsService.getRecordsMenu();
    }


    /**
     * 模板选择下拉框
     *
     * @return 模板选择下拉框
     */
    @RequestMapping("/getModelMenu")
    public Wrapper getModelMenu(){
        return dictionaryDetailsService.getModelMenu();
    }

    /**
     * 素材目录类型选择
     *
     * @return 素材目录类型选择
     */
    @RequestMapping("/getMaterialMenu")
    public Wrapper getMaterialMenu(){
        return dictionaryDetailsService.getMaterialMenu();
    }

    /**
     * 分组名称下拉框
     *
     * @return 分组名称下拉框
     */
    @RequestMapping("/getGroupMenu")
    public Wrapper getGroupMenu(){
        return groupService.getGroupMenu();
    }
}