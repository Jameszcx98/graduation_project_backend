package com.shu.dao;

import com.github.pagehelper.Page;
import com.shu.model.Goods;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface GoodsDAO {

    /**
     * 通过ID查询单个
     *
     * @param id ID
     * @return {@link Goods}
     */
    Goods findById(Long id);

    /**
     * 分页查询
     *
     * @return {@link Goods}
     */
    Page<Goods> findByPage();

    /**
     * 新增
     *
     * @param goods
     */
    void insert(Goods goods);

    /**
     * 修改
     *
     * @param goods
     */
    int update(Goods goods);

    /**
     * 通过ID删除单个
     *
     * @param id ID
     */
    void deleteById(Long id);

}