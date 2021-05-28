package com.shu.service;

import com.github.pagehelper.PageInfo;
import com.shu.model.OrderGoodsLog;

import java.util.Date;

/**
 *
 */
public interface OrderGoodsLogService {

    /**
     * 通过ID查询单个
     *
     * @param id ID
     * @return {@link OrderGoodsLog}
     */
    OrderGoodsLog findById(Integer id, Long orderId, Date logTime);

    /**
     * 分页查询
     *
     * @param pageNum  页号
     * @param pageSize 每页大小
     * @return {@link OrderGoodsLog}
     */
    PageInfo<OrderGoodsLog> findByPage(int pageNum, int pageSize);

    /**
     * 新增
     *
     * @param orderGoodsLog
     */
    void insert(OrderGoodsLog orderGoodsLog);

    /**
     * 修改
     *
     * @param orderGoodsLog
     */
    void update(OrderGoodsLog orderGoodsLog);

    /**
     * 通过ID删除单个
     *
     * @param id ID
     */
    void deleteById(Integer id, Long orderId, Date logTime);

}