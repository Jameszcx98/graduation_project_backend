package com.shu.service;

import com.github.pagehelper.PageInfo;
import com.shu.constant.Result;
import com.shu.model.User;
import com.shu.model.UserOperationLog;

import java.math.BigDecimal;

/**
 *
 */
public interface UserService {

    /**
     * 通过ID查询单个
     *
     * @param id ID
     * @return {@link User}
     */
    User findById(Long id);

    /**
     * 分页查询
     *
     * @param pageNum  页号
     * @param pageSize 每页大小
     * @return {@link User}
     */
    PageInfo<User> findByPage(int pageNum, int pageSize);

    /**
     * 新增
     *
     * @param user
     */
    Result insert(String username,String phone,String password,String email);

    /**
     * 修改
     *
     * @param user
     */
    void update(User user);
    void updateP(User user);


    /**
     * 通过ID删除单个
     *
     * @param id ID
     */
    void deleteById(Long id);

    Result updateUserMoney(UserOperationLog userOperationLog);
    void updateUserMoney(Long userId, BigDecimal money);

    void confirmEmail(String email);

    void activateEmail(String id);
}