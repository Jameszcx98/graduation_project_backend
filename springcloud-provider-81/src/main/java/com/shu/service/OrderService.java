package com.shu.service;

import com.github.pagehelper.PageInfo;
import com.shu.constant.Result;
import com.shu.model.Order;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 *
 */
public interface OrderService {

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
     * @param pageNum  页号
     * @param pageSize 每页大小
     * @return {@link Order}
     */
    PageInfo<Order> findByPage(int pageNum, int pageSize,String email);

    /**
     * 新增
     *
     * @param order
     */
    Result insert(String username, Long goodsId, String file,String rate);

    /**
     * 修改
     *
     * @param order
     */
    void update(Order order);

    /**
     * 通过ID删除单个
     *
     * @param id ID
     */
    void deleteById(Long id);

    Result confirmOrder1(Order order);

    Result confirmOrder2(Order order);

    void reduceGoodsNum(Order order);

    void sendOrder(Order order, String topic, String tags, boolean flag) throws InterruptedException, RemotingException, MQClientException, MQBrokerException;
}