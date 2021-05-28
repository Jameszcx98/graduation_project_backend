package com.shu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shu.dao.OrderGoodsLogDAO;
import com.shu.model.OrderGoodsLog;
import com.shu.service.OrderGoodsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
//@Transactional(rollbackFor = Exception.class)
public class OrderGoodsLogServiceImpl implements OrderGoodsLogService {

    @Autowired
    private OrderGoodsLogDAO orderGoodsLogDAO;

    @Transactional(readOnly = true)
    @Override
    public OrderGoodsLog findById(Integer id, Long orderId, Date logTime) {
        return orderGoodsLogDAO.findById(id,orderId,logTime);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<OrderGoodsLog> findByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return PageInfo.of(orderGoodsLogDAO.findByPage());
    }

    @Override
    public void insert(OrderGoodsLog orderGoodsLog) {
        orderGoodsLogDAO.insert(orderGoodsLog);
    }

    @Override
    public void update(OrderGoodsLog orderGoodsLog) {
        orderGoodsLogDAO.update(orderGoodsLog);
    }

    @Override
    public void deleteById(Integer id, Long orderId, Date logTime) {
        orderGoodsLogDAO.deleteById(id,orderId,logTime);
    }

}