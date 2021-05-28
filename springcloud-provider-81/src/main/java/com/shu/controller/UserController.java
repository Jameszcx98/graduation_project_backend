package com.shu.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.shu.constant.Result;
import com.shu.constant.ShopCode;
import com.shu.dao.UserDAO;
import com.shu.model.User;
import com.shu.service.UserService;
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
@RequestMapping(produces = { "text/html;charset=UTF-8;", "application/json;charset=UTF-8;" })
@Api(tags = "")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDAO userDAO;

    @PostMapping("/toLogin")
    @ApiOperation("用户登录")
    public String login() {
        return "login";

    }


    @PostMapping("/userInfo")
    @ApiOperation("通过ID查询单个")
    public User findById(@RequestBody String email) {
        String email1=email.substring(email.lastIndexOf(":")+2,email.lastIndexOf(".")+4);
        return userDAO.findByEmail(email1);
    }
    @PutMapping("/editUserInfo")
    @ApiOperation("通过ID查询单个")
    public Result Update(@RequestBody String s) {
        JSONObject jo = new JSONObject();
        try {
            s = URLDecoder.decode(s,"UTF-8");
            jo= JSON.parseObject(s);
            User user = userDAO.findByEmail(jo.getString("email"));
            user.setPassword(jo.getString("password"));
            user.setUserMobile(jo.getString("phone"));
            user.setUsername(jo.getString("username"));

            userService.updateP(user);
            return new Result(ShopCode.SHOP_SUCCESS.getCode(),ShopCode.SHOP_SUCCESS.getMessage());

        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return new Result(ShopCode.SHOP_FAIL.getCode(),ShopCode.SHOP_FAIL.getMessage());
        }

    }



    @GetMapping
    @ApiOperation("分页查询")
    public PageInfo<User> findByPage(@ApiParam("页号") @RequestParam(defaultValue = "1") Integer pageNum,
                                     @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return userService.findByPage(pageNum, pageSize);
    }

    @PostMapping
    @ApiOperation("新增")
    public Result insert(@RequestBody String s) {
        JSONObject jo = new JSONObject();
        try {
            s = URLDecoder.decode(s,"UTF-8");
            jo= JSON.parseObject(s);

            return userService.insert(jo.getString("name"), jo.getString("phone"),jo.getString("password"),jo.getString("email"));
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return new Result(ShopCode.SHOP_FAIL.getCode(),ShopCode.SHOP_FAIL.getMessage());
        }


    }

    @PostMapping("/email")
    @ApiOperation("邮箱确认")
    public void confirmEmail(@RequestBody String email) {
        userService.confirmEmail(email);
    }

    @GetMapping("/activate")
    @ApiOperation("邮箱激活")
    public String activateEmail(@RequestParam String id) {
        userService.activateEmail(id);
        return "激活成功！";

    }

    @PutMapping
    @ApiOperation("修改")
    public void update(@RequestBody User user) {
        userService.update(user);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("通过ID删除单个")
    public void deleteById(@ApiParam("ID") @PathVariable("id") Long id) {
        userService.deleteById(id);
    }
}
