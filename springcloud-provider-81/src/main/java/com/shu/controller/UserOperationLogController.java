package com.shu.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shu.constant.Result;
import com.shu.constant.ShopCode;
import com.shu.model.UserOperationLog;
import com.shu.service.UserOperationLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;

/**
 *
 */
@RestController
@RequestMapping("/userOperationLogs")
@Api(tags = "")
public class UserOperationLogController {
    @Autowired
    private UserOperationLogService userOperationLogService;

    @GetMapping("/{id}")
    @ApiOperation("通过ID查询单个")
    public UserOperationLog findById(@ApiParam("ID") @PathVariable("id") Long id) {
        return userOperationLogService.findById(id);
    }

    @GetMapping
    @ApiOperation("分页查询")
    public PageInfo<UserOperationLog> findByPage(@ApiParam("页号") @RequestParam(defaultValue = "1") Integer pageNum,
                                                 @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return userOperationLogService.findByPage(pageNum, pageSize);
    }

    @PostMapping
    @ApiOperation("新增")
    public Result insert(@RequestBody String s) {
        JSONObject jo = new JSONObject();
        try {
            s = URLDecoder.decode(s,"UTF-8");
            jo= JSON.parseObject(s);

            return userOperationLogService.addMoney(jo.getString("email"), jo.getString("money"));
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return new Result(ShopCode.SHOP_FAIL.getCode(),ShopCode.SHOP_FAIL.getMessage());
        }
    }

    @PutMapping
    @ApiOperation("修改")
    public void update(@RequestBody UserOperationLog userOperationLog) {
        userOperationLogService.update(userOperationLog);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("通过ID删除单个")
    public void deleteById(@ApiParam("ID") @PathVariable("id") Long id) {
        userOperationLogService.deleteById(id);
    }
}
