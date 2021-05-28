package com.shu.dao;

import com.github.pagehelper.Page;
import com.shu.model.OrderPay;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface OrderPayDAO {

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
     * @return {@link OrderPay}
     */
    Page<OrderPay> findByPage();

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