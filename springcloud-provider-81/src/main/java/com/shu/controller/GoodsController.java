package com.shu.controller;

import com.github.pagehelper.PageInfo;
import com.shu.model.Goods;
import com.shu.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping("/goodss")
@Api(tags = "")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/{id}")
    @ApiOperation("通过ID查询单个")
    public Goods findById(@ApiParam("ID") @PathVariable("id") Long id) {
        return goodsService.findById(id);
    }



    @GetMapping
    @ApiOperation("分页查询")
    public PageInfo<Goods> findByPage(@ApiParam("页号") @RequestParam(defaultValue = "1") Integer pageNum,
                                      @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return goodsService.findByPage(pageNum, pageSize);
    }

    @PostMapping
    @ApiOperation("新增")
    public void insert(@RequestBody Goods goods) {
        goodsService.insert(goods);
    }

    @PutMapping
    @ApiOperation("修改")
    public void update(@RequestBody Goods goods) {
        goodsService.update(goods);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("通过ID删除单个")
    public void deleteById(@ApiParam("ID") @PathVariable("id") Long id) {
        goodsService.deleteById(id);
    }
}
