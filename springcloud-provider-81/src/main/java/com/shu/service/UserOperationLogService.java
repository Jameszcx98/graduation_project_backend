package com.shu.service;

import com.github.pagehelper.PageInfo;
import com.shu.constant.Result;
import com.shu.model.UserOperationLog;

/**
 *
 */
public interface UserOperationLogService {

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
     * @param pageNum  页号
     * @param pageSize 每页大小
     * @return {@link UserOperationLog}
     */
    PageInfo<UserOperationLog> findByPage(int pageNum, int pageSize);

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

    Result addMoney(String email, String money);
}