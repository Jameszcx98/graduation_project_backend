package com.shu.dao;

import com.github.pagehelper.Page;
import com.shu.model.OrderGoodsLog;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 *
 */
@Repository
public interface OrderGoodsLogDAO {

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
     * @return {@link OrderGoodsLog}
     */
    Page<OrderGoodsLog> findByPage();

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