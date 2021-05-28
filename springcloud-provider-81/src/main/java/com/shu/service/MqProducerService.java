package com.shu.service;

import com.github.pagehelper.PageInfo;
import com.shu.model.MqProducer;

/**
 *
 */
public interface MqProducerService {

    /**
     * 通过ID查询单个
     *
     * @param id ID
     * @return {@link MqProducer}
     */
    MqProducer findById(String id);

    /**
     * 分页查询
     *
     * @param pageNum  页号
     * @param pageSize 每页大小
     * @return {@link MqProducer}
     */
    PageInfo<MqProducer> findByPage(int pageNum, int pageSize);

    /**
     * 新增
     *
     * @param mqProducer
     */
    void insert(MqProducer mqProducer);

    /**
     * 修改
     *
     * @param mqProducer
     */
    void update(MqProducer mqProducer);

    /**
     * 通过ID删除单个
     *
     * @param id ID
     */
    void deleteById(String id);

}