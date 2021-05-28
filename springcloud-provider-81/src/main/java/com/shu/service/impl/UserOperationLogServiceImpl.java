package com.shu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shu.constant.Result;
import com.shu.constant.ShopCode;
import com.shu.dao.UserDAO;
import com.shu.dao.UserOperationLogDAO;
import com.shu.exception.CastException;
import com.shu.model.UserOperationLog;
import com.shu.service.UserOperationLogService;
import com.shu.utils.IDWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
//@Transactional(rollbackFor = Exception.class)
public class UserOperationLogServiceImpl implements UserOperationLogService {

    @Autowired
    private UserOperationLogDAO userOperationLogDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private IDWorker idWorker;
    @Autowired
    private UserServiceImpl userService;

    @Transactional(readOnly = true)
    @Override
    public UserOperationLog findById(Long id) {
        return userOperationLogDAO.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<UserOperationLog> findByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return PageInfo.of(userOperationLogDAO.findByPage());
    }

    @Override
    public void insert(UserOperationLog userOperationLog) {
        userOperationLogDAO.insert(userOperationLog);
    }

    @Override
    public void update(UserOperationLog userOperationLog) {
        userOperationLogDAO.update(userOperationLog);
    }

    @Override
    public void deleteById(Long id) {
        userOperationLogDAO.deleteById(id);
    }

    public Result addMoney(String email, String money){
        UserOperationLog userOperationLog = new UserOperationLog();
        userOperationLog.setOrderId(idWorker.nextId());
        userOperationLog.setUserId(userDAO.findByEmail(email).getUserId());
        userOperationLog.setUseMoney(BigDecimal.valueOf(Long.valueOf(money)).negate());
        userOperationLog.setLogType(ShopCode.SHOP_USER_MONEY_ADD.getCode());
        userOperationLog.setCreateTime(new Date());
        UserOperationLog userOperationLog1 = userOperationLogDAO.findById(userOperationLog.getOrderId());
        if(userOperationLog1==null){
            userService.updateUserMoney(userOperationLog.getUserId(),userOperationLog.getUseMoney());
            userOperationLogDAO.insert(userOperationLog);
            log.info("订单"+userOperationLog.getOrderId()+"充值成功");
            return new Result(ShopCode.SHOP_USER_MONEY_ADD.getCode(),ShopCode.SHOP_USER_MONEY_ADD.getMessage());
        }else{
            CastException.cast(ShopCode.SHOP_USER_MONEY_ADDR);
            return new Result(ShopCode.SHOP_USER_MONEY_ADDR.getCode(),ShopCode.SHOP_USER_MONEY_ADDR.getMessage());

        }
//        Result result = userService.updateUserMoney(userMoneyLog);
//        if(result.getSuccess().equals(ShopCode.SHOP_FAIL.getSuccess())){
//            CastException.cast(ShopCode.SHOP_USER_MONEY_REDUCE_FAIL);
//        }
//        log.info("订单:"+order.getOrderId()+",扣减余额成功");

    }


}