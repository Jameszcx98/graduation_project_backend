package com.shu.dao;

import com.github.pagehelper.Page;
import com.shu.model.User;
import com.shu.model.UserOperationLog;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface UserDAO {

    /**
     * 通过ID查询单个
     *
     * @param id ID
     * @return {@link User}
     */
    User findById(Long id);

    User findByName(String name);

    User findByEmail(String email);


    /**
     * 分页查询
     *
     * @return {@link User}
     */
    Page<User> findByPage();

    /**
     * 新增
     *
     * @param user
     */
    void insert(User user);

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


}