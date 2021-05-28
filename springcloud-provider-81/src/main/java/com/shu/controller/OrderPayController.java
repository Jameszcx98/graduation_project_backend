package com.shu.controller;

import com.github.pagehelper.PageInfo;
import com.shu.model.OrderPay;
import com.shu.service.OrderPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping("/orderPays")
@Api(tags = "")
public class OrderPayController {
    @Autowired
    private OrderPayService orderPayService;

    @GetMapping("/{id}")
    @ApiOperation("通过ID查询单个")
    public OrderPay findById(@ApiParam("ID") @PathVariable("id") Long id) {
        return orderPayService.findById(id);
    }

    @GetMapping
    @ApiOperation("分页查询")
    public PageInfo<OrderPay> findByPage(@ApiParam("页号") @RequestParam(defaultValue = "1") Integer pageNum,
                                         @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return orderPayService.findByPage(pageNum, pageSize);
    }

    @PostMapping
    @ApiOperation("新增")
    public void insert(@RequestBody OrderPay orderPay) {
        orderPayService.insert(orderPay);
    }

    @PutMapping
    @ApiOperation("修改")
    public void update(@RequestBody OrderPay orderPay) {
        orderPayService.update(orderPay);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("通过ID删除单个")
    public void deleteById(@ApiParam("ID") @PathVariable("id") Long id) {
        orderPayService.deleteById(id);
    }
}
