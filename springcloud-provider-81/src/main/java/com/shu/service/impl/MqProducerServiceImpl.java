package com.shu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shu.dao.MqProducerDAO;
import com.shu.model.MqProducer;
import com.shu.service.MqProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@Transactional(rollbackFor = Exception.class)
public class MqProducerServiceImpl implements MqProducerService {

    @Autowired
    private MqProducerDAO mqProducerDAO;

    @Transactional(readOnly = true)
    @Override
    public MqProducer findById(String id) {
        return mqProducerDAO.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<MqProducer> findByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return PageInfo.of(mqProducerDAO.findByPage());
    }

    @Override
    public void insert(MqProducer mqProducer) {
        mqProducerDAO.insert(mqProducer);
    }

    @Override
    public void update(MqProducer mqProducer) {
        mqProducerDAO.update(mqProducer);
    }

    @Override
    public void deleteById(String id) {
        mqProducerDAO.deleteById(id);
    }

}