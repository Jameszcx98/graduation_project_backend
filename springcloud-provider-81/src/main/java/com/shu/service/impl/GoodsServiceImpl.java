package com.shu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shu.dao.GoodsDAO;
import com.shu.model.Goods;
import com.shu.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@Transactional(rollbackFor = Exception.class)
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDAO goodsDAO;

    @Transactional(readOnly = true)
    @Override
    public Goods findById(Long id) {
        return goodsDAO.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<Goods> findByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return PageInfo.of(goodsDAO.findByPage());
    }

    @Override
    public void insert(Goods goods) {
        goodsDAO.insert(goods);
    }

    @Override
    public void update(Goods goods) {
        goodsDAO.update(goods);
    }

    @Override
    public void deleteById(Long id) {
        goodsDAO.deleteById(id);
    }

}