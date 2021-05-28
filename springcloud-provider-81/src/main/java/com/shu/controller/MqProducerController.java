package com.shu.controller;

import com.github.pagehelper.PageInfo;
import com.shu.model.MqProducer;
import com.shu.service.MqProducerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping("/mqProducers")
@Api(tags = "")
public class MqProducerController {
    @Autowired
    private MqProducerService mqProducerService;

    @GetMapping("/{id}")
    @ApiOperation("通过ID查询单个")
    public MqProducer findById(@ApiParam("ID") @PathVariable("id") String id) {
        return mqProducerService.findById(id);
    }

    @GetMapping
    @ApiOperation("分页查询")
    public PageInfo<MqProducer> findByPage(@ApiParam("页号") @RequestParam(defaultValue = "1") Integer pageNum,
                                           @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return mqProducerService.findByPage(pageNum, pageSize);
    }

    @PostMapping
    @ApiOperation("新增")
    public void insert(@RequestBody MqProducer mqProducer) {
        mqProducerService.insert(mqProducer);
    }

    @PutMapping
    @ApiOperation("修改")
    public void update(@RequestBody MqProducer mqProducer) {
        mqProducerService.update(mqProducer);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("通过ID删除单个")
    public void deleteById(@ApiParam("ID") @PathVariable("id") String id) {
        mqProducerService.deleteById(id);
    }
}
