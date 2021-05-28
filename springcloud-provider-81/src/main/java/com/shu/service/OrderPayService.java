package com.shu.service;

import com.github.pagehelper.PageInfo;
import com.shu.model.OrderPay;

/**
 *
 */
public interface OrderPayService {

    /**
     * 通过ID查询单个
     *
     * @param id ID
     * @return {@link OrderPay}
     */
    OrderPay findById(Long id);

    /**
     * 分页查询
     *
     * @param pageNum  页号
     * @param pageSize 每页大小
     * @return {@link OrderPay}
     */
    PageInfo<OrderPay> findByPage(int pageNum, int pageSize);

    /**
     * 新增
     *
     * @param orderPay
     */
    void insert(OrderPay orderPay);

    /**
     * 修改
     *
     * @param orderPay
     */
    void update(OrderPay orderPay);

    /**
     * 通过ID删除单个
     *
     * @param id ID
     */
    void deleteById(Long id);

}