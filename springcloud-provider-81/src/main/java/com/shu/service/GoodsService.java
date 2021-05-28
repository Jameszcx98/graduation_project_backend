package com.shu.service;

import com.github.pagehelper.PageInfo;
import com.shu.model.Goods;

/**
 *
 */
public interface GoodsService {

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
     * @param pageNum  页号
     * @param pageSize 每页大小
     * @return {@link Goods}
     */
    PageInfo<Goods> findByPage(int pageNum, int pageSize);

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
    void update(Goods goods);

    /**
     * 通过ID删除单个
     *
     * @param id ID
     */
    void deleteById(Long id);

}