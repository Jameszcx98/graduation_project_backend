package com.shu.service.springSecurity;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shu.dao.PermissionDAO;
import com.shu.model.Permission;
import com.shu.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDAO permissionDAO;

    @Transactional(readOnly = true)
    @Override
    public List<Permission> findById(Long id) {
        return permissionDAO.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<Permission> findByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return PageInfo.of(permissionDAO.findByPage());
    }

    @Override
    public void insert(Permission permission) {
        permissionDAO.insert(permission);
    }

    @Override
    public void update(Permission permission) {
        permissionDAO.update(permission);
    }

    @Override
    public void deleteById(Long id) {
        permissionDAO.deleteById(id);
    }

}