package com.shu.dao;

import com.github.pagehelper.Page;
import com.shu.model.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository
public interface PermissionDAO {

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
     * @return {@link Permission}
     */
    Page<Permission> findByPage();

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