package com.shu.service;

import com.github.pagehelper.PageInfo;
import com.shu.model.MqConsumer;

/**
 *
 */
public interface MqConsumerService {


    MqConsumer findById(String msgId);

    /**
     * 分页查询
     *
     * @param pageNum  页号
     * @param pageSize 每页大小
     * @return {@link MqConsumer}
     */
    PageInfo<MqConsumer> findByPage(int pageNum, int pageSize);

    /**
     * 新增
     *
     * @param mqConsumer
     */
    void insert(MqConsumer mqConsumer);

    /**
     * 修改
     *
     * @param mqConsumer
     */
    int update(MqConsumer mqConsumer);

    /**
     * 通过ID删除单个
     *
     * @param id ID
     */
    void deleteById(String groupName,String msgTag,String msgKey);

}