package com.shuzhi.controller;

import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.ShowRecords;
import com.shuzhi.service.ShowRecordsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


/**
 * @author zgk
 * @date 2019-08-09 14:38:03
 */

@RestController
@RequestMapping(value = "/showRecords")
public class ShowRecordsController {

    private final ShowRecordsService showRecordsService;

    public ShowRecordsController(ShowRecordsService showRecordsService) {
        this.showRecordsService = showRecordsService;
    }

    /**
     * 分页查询
     *
     * @param showRecords 分页参数和设备类型
     * @return 分页结果
     */
    @RequestMapping("/findShowRecords")
    public Wrapper findShowRecords(@RequestBody ShowRecords showRecords) {

        //如果没有分页信息添加默认值
        showRecords.setPageNum(Optional.ofNullable(showRecords.getPageNum()).orElse(1));
        showRecords.setPageSize(Optional.ofNullable(showRecords.getPageSize()).orElse(10));
        return showRecordsService.findShowRecords(showRecords);
    }

    /**
     * 通过节目发布id查询节目详情
     *
     * @param id 节目发布id
     * @return 详情信息
     */
    @RequestMapping("/findShowRecordsById/{id}")
    private Wrapper findShowRecordsById(@PathVariable Integer id){
        return showRecordsService.findShowRecordsById(id);
    }

    /**
     * 删除节目记录
     *
     * @param id 节目发布id
     * @return 删除结果
     */
    @RequestMapping("/removeShowRecordsById/{id}")
    private Wrapper removeShowRecordsById(@PathVariable Integer id){
        return showRecordsService.removeShowRecordsById(id);
    }

    /**
     * 新增节目记录
     *
     * @param showRecords 节目信息
     * @return 新增节目记录
     */
    @RequestMapping("/addShowRecords")
    private Wrapper addShowRecords(@RequestBody ShowRecords showRecords){
        return showRecordsService.addShowRecords(showRecords);
    }

}