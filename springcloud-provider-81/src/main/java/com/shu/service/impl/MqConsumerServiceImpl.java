package com.shu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shu.dao.MqConsumerDAO;
import com.shu.model.MqConsumer;
import com.shu.service.MqConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@Transactional(rollbackFor = Exception.class)
public class MqConsumerServiceImpl implements MqConsumerService {

    @Autowired
    private MqConsumerDAO mqConsumerDAO;

    @Transactional(readOnly = true)
    @Override
    public MqConsumer findById(String msgId) {
        return mqConsumerDAO.findById(msgId);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<MqConsumer> findByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return PageInfo.of(mqConsumerDAO.findByPage());
    }

    @Override
    public void insert(MqConsumer mqConsumer) {
        mqConsumerDAO.insert(mqConsumer);
    }

    @Override
    public int update(MqConsumer mqConsumer) {
        return mqConsumerDAO.update(mqConsumer);
    }

    @Override
    public void deleteById(String groupName,String msgTag,String msgKey) {
        mqConsumerDAO.deleteById(groupName,msgTag,msgKey);
    }

}