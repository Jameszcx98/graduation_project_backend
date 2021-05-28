package com.shu.dao;

import com.github.pagehelper.Page;
import com.shu.model.MqConsumer;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface MqConsumerDAO {

    /**
     * 通过ID查询单个
     *
     * @param id ID
     * @return {@link MqConsumer}
     */
    MqConsumer findById(String msgId);

    /**
     * 分页查询
     *
     * @return {@link MqConsumer}
     */
    Page<MqConsumer> findByPage();

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