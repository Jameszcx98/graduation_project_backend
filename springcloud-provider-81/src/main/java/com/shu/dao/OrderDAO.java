package com.shu.dao;

import com.github.pagehelper.Page;
import com.shu.model.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface OrderDAO {

    /**
     * 通过ID查询单个
     *
     * @param id ID
     * @return {@link Order}
     */
    Order findById(Long id);

    /**
     * 分页查询
     *
     * @return {@link Order}
     */
    Page<Order> findByPage(Long userId);

    /**
     * 新增
     *
     * @param order
     */
    void insert(Order order);

    /**
     * 修改
     *
     * @param order
     */
    int update(Order order);

    /**
     * 通过ID删除单个
     *
     * @param id ID
     */
    void deleteById(Long id);

    Order findByFile(String file);

}