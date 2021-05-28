package com.shu.service;

import com.github.pagehelper.PageInfo;
import com.shu.model.Permission;

import java.util.List;

/**
 *
 */
public interface PermissionService {

    /**
     * 通过ID查询单个
     *
     * @param id ID
     * @return {@link Permission}
     */
    List<Permission> findById(Long id);

    /**
     * 分页查询
     *
     * @param pageNum  页号
     * @param pageSize 每页大小
     * @return {@link Permission}
     */
    PageInfo<Permission> findByPage(int pageNum, int pageSize);

    /**
     * 新增
     *
     * @param permission
     */
    void insert(Permission permission);

    /**
     * 修改
     *
     * @param permission
     */
    void update(Permission permission);

    /**
     * 通过ID删除单个
     *
     * @param id ID
     */
    void deleteById(Long id);

}