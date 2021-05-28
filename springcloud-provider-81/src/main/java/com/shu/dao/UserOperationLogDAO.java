package com.shu.dao;

import com.github.pagehelper.Page;
import com.shu.model.UserOperationLog;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface UserOperationLogDAO {

    /**
     * 通过ID查询单个
     *
     * @param id ID
     * @return {@link UserOperationLog}
     */
    UserOperationLog findById(Long id);

    /**
     * 分页查询
     *
     * @return {@link UserOperationLog}
     */
    Page<UserOperationLog> findByPage();

    /**
     * 新增
     *
     * @param userOperationLog
     */
    void insert(UserOperationLog userOperationLog);

    /**
     * 修改
     *
     * @param userOperationLog
     */
    void update(UserOperationLog userOperationLog);

    /**
     * 通过ID删除单个
     *
     * @param id ID
     */
    void deleteById(Long id);

}