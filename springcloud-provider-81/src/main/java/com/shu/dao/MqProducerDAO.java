package com.shu.dao;

import com.github.pagehelper.Page;
import com.shu.model.MqProducer;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface MqProducerDAO {

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
     * @return {@link MqProducer}
     */
    Page<MqProducer> findByPage();

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