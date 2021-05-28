package com.shu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shu.dao.OrderPayDAO;
import com.shu.model.OrderPay;
import com.shu.service.OrderPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@Transactional(rollbackFor = Exception.class)
public class OrderPayServiceImpl implements OrderPayService {

    @Autowired
    private OrderPayDAO orderPayDAO;

    @Transactional(readOnly = true)
    @Override
    public OrderPay findById(Long id) {
        return orderPayDAO.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<OrderPay> findByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return PageInfo.of(orderPayDAO.findByPage());
    }

    @Override
    public void insert(OrderPay orderPay) {
        orderPayDAO.insert(orderPay);
    }

    @Override
    public void update(OrderPay orderPay) {
        orderPayDAO.update(orderPay);
    }

    @Override
    public void deleteById(Long id) {
        orderPayDAO.deleteById(id);
    }

}