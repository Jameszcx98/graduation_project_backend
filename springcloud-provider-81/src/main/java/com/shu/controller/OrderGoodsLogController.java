package com.shu.controller;

import com.github.pagehelper.PageInfo;
import com.shu.model.OrderGoodsLog;
import com.shu.service.OrderGoodsLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping("/orderGoodsLogs")
@Api(tags = "")
public class OrderGoodsLogController {
    @Autowired
    private OrderGoodsLogService orderGoodsLogService;

//    @GetMapping("/{id}")
//    @ApiOperation("通过ID查询单个")
//    public OrderGoodsLog findById(@ApiParam("ID") @PathVariable("id") Integer id) {
//        return orderGoodsLogService.findById(id,orderId,logTime);
//    }

    @GetMapping
    @ApiOperation("分页查询")
    public PageInfo<OrderGoodsLog> findByPage(@ApiParam("页号") @RequestParam(defaultValue = "1") Integer pageNum,
                                              @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return orderGoodsLogService.findByPage(pageNum, pageSize);
    }

    @PostMapping
    @ApiOperation("新增")
    public void insert(@RequestBody OrderGoodsLog orderGoodsLog) {
        orderGoodsLogService.insert(orderGoodsLog);
    }

    @PutMapping
    @ApiOperation("修改")
    public void update(@RequestBody OrderGoodsLog orderGoodsLog) {
        orderGoodsLogService.update(orderGoodsLog);
    }

//    @DeleteMapping("/{id}")
//    @ApiOperation("通过ID删除单个")
//    public void deleteById(@ApiParam("ID") @PathVariable("id") Integer id) {
//        orderGoodsLogService.deleteById(id,orderId,logTime);
//    }
}
