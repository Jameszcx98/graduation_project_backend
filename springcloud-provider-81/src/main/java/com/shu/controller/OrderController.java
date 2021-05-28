package com.shu.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shu.constant.Result;
import com.shu.constant.ShopCode;
import com.shu.model.Order;
import com.shu.service.OrderService;
import com.shu.service.impl.OrderServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;

/**
 *
 */
@RestController
@RequestMapping("/orders")
@Api(tags = "")
public class OrderController {
    @Autowired
    private OrderServiceImpl orderService;

    @GetMapping("/{id}")
    @ApiOperation("通过ID查询单个")
    public Order findById(@ApiParam("ID") @PathVariable("id") Long id) {
        return orderService.findById(id);
    }

    @GetMapping
    @ApiOperation("分页查询")
    public PageInfo<Order> findByPage(@ApiParam("页号") @RequestParam(defaultValue = "1") Integer pageNum,
                                      @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize,
                                      @RequestParam String email) {
        return orderService.findByPage(pageNum, pageSize,email);
    }

    @PostMapping
    @ApiOperation("下单")
    public Result insert(@RequestBody String s) {
        JSONObject jo = new JSONObject();
        try {
            s = URLDecoder.decode(s,"UTF-8");
            jo= JSON.parseObject(s);
            System.out.println("hhhh");
            return orderService.insert(jo.getString("email"), jo.getLong("goodsId"),jo.getString("file"),jo.getString("rate"));
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return new Result(ShopCode.SHOP_FAIL.getCode(),ShopCode.SHOP_FAIL.getMessage());
        }


    }

    @PutMapping
    @ApiOperation("修改")
    public void update(@RequestBody Order order) {
        orderService.update(order);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("通过ID删除单个")
    public void deleteById(@ApiParam("ID") @PathVariable("id") Long id) {
        orderService.deleteById(id);
    }



}
