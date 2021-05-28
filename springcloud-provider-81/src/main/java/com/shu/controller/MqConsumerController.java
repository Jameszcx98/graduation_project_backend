package com.shu.controller;

import com.github.pagehelper.PageInfo;
import com.shu.model.MqConsumer;
import com.shu.service.MqConsumerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping("/mqConsumers")
@Api(tags = "")
public class MqConsumerController {
    @Autowired
    private MqConsumerService mqConsumerService;

//    @GetMapping("/{id}")
//    @ApiOperation("通过ID查询单个")
//    public MqConsumer findById(@ApiParam("ID") @PathVariable("id") String id) {
//        return mqConsumerService.findById(groupName,msgTag,msgKey);
//    }

    @GetMapping
    @ApiOperation("分页查询")
    public PageInfo<MqConsumer> findByPage(@ApiParam("页号") @RequestParam(defaultValue = "1") Integer pageNum,
                                           @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return mqConsumerService.findByPage(pageNum, pageSize);
    }

    @PostMapping
    @ApiOperation("新增")
    public void insert(@RequestBody MqConsumer mqConsumer) {
        mqConsumerService.insert(mqConsumer);
    }

    @PutMapping
    @ApiOperation("修改")
    public void update(@RequestBody MqConsumer mqConsumer) {
        mqConsumerService.update(mqConsumer);
    }

//    @DeleteMapping("/{id}")
//    @ApiOperation("通过ID删除单个")
//    public void deleteById(@ApiParam("ID") @PathVariable("id") String id) {
//        mqConsumerService.deleteById(groupName,msgTag,msgKey);
//    }
}
