package com.shu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shu.constant.Result;
import com.shu.constant.ShopCode;
import com.shu.dao.*;
import com.shu.exception.CastException;
import com.shu.model.Goods;
import com.shu.model.Permission;
import com.shu.model.User;
import com.shu.model.UserOperationLog;
import com.shu.service.UserService;
import com.shu.service.email.SendMailService;
import com.shu.utils.IDWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
//@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private PermissionDAO permissionDAO;
    @Autowired
    private UserOperationLogDAO userOperationLogDAO;
    @Autowired
    private GoodsDAO goodsDAO;
    @Autowired
    private IDWorker idWorker;
    @Value(value = "${httpUrl}")
    private String httpUrl;

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        return userDAO.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<User> findByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return PageInfo.of(userDAO.findByPage());
    }

    @Override
    public Result insert(String username,String phone,String password,String email) {
        User user = userDAO.findByEmail(email);
        user.setUsername(username);
        user.setUserMobile(phone);
        user.setPassword(password);
        user.setCreateDate(new Date());
        userDAO.updateP(user);
        return new Result(ShopCode.SHOP_SUCCESS.getCode(),ShopCode.SHOP_SUCCESS.getMessage());
    }

    @Override
    public void update(User user) {
        userDAO.update(user);
    }

    @Override
    public void updateP(User user) {
        userDAO.updateP(user);
    }

    @Override
    public void deleteById(Long id) {
        userDAO.deleteById(id);
    }


    @Override
    public void activateEmail(String id) {
        User user = userDAO.findById(Long.valueOf(id));
        if(user==null){
            return;
        }
        user.setStatus("confirmed");
        userDAO.update(user);
    }

    @Override
    public void confirmEmail(String email1) {
        String email=email1.substring(email1.lastIndexOf(":")+2,email1.lastIndexOf(".")+4);
        log.info("获取到确认邮箱--->"+email);
        if(userDAO.findByEmail(email)!=null){
            return;
        }
        String id=String.valueOf(idWorker.nextId());
        User user=new User();
        user.setUserEmail(email);
        user.setUserId(Long.valueOf(id));
        user.setStatus("NotConfirmed");
        userDAO.insert(user);
        StringBuffer s=new StringBuffer();
        s.append("尊敬的用户你好！欢迎您使用我们的网站，你的激活网址为：");
        s.append(httpUrl+"activate");
        s.append("?id="+id);
        log.info("发送内容--->"+s.toString());
        SendMailService sendMailService = new SendMailService();
        sendMailService.send(email,"激活邮箱",s.toString());


    }

    /*
     * @Author: jameszhang
     * @Description:
     * @param s :用户名
     * @return: org.springframework.security.core.userdetails.UserDetails
     **/
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //1.根据该用户名称查询在数据库中是否存在

        User user = userDAO.findByEmail(s);
        if(user == null){
            return null;
        }
        //2.查询所有的权限
        List<Permission> listPermission=permissionDAO.findById(user.getUserId());
        List<GrantedAuthority> authorities=new ArrayList<>();
        listPermission.forEach(lp->{
            authorities.add(new SimpleGrantedAuthority(lp.getPermTag()));

        });
        //3。将该权限添加到security中
        user.setAuthorities(authorities);
        return user;
    }

    @Override
    public Result updateUserMoney(UserOperationLog userOperationLog) {
        //1.校验参数是否合法

        log.info(userOperationLog.toString());
        if (userOperationLog == null ||
                userOperationLog.getUserId() == null ||
                userOperationLog.getOrderId() == null ||
                userOperationLog.getUseMoney() == null ||
                userOperationLog.getUseMoney().compareTo(BigDecimal.ZERO) <= 0) {
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        Long orderId = userOperationLog.getOrderId();
        Long userId = userOperationLog.getUserId();
        BigDecimal money = userOperationLog.getUseMoney();
        //2.查询订单余额使用日志
        UserOperationLog userOperationLog1 = userOperationLogDAO.findById(orderId);
        Goods goods = goodsDAO.findById(orderDAO.findById(orderId).getGoodsId());
        //没有查询到相关日志
        if (userOperationLog1 == null) {
            //用户付款
            if (userOperationLog.getLogType() == ShopCode.SHOP_USER_MONEY_PAID.getCode()) {
                //扣减余额
                updateUserMoney(userId, money);
                Integer t=goods.getGoodsNumber()+1;
                goods.setGoodsNumber(t);
                goodsDAO.update(goods);
            } else if (userOperationLog.getLogType() == ShopCode.SHOP_USER_MONEY_REFUND.getCode()) {
                //用户无付款记录
                CastException.cast(ShopCode.SHOP_USER_MONEY_REFUND_ALREADY);
            }

        }else{
            if (userOperationLog.getLogType() == ShopCode.SHOP_USER_MONEY_PAID.getCode()) {
                CastException.cast(ShopCode.SHOP_ORDER_PAY_STATUS_IS_PAY);
            } else if (userOperationLog.getLogType() == ShopCode.SHOP_USER_MONEY_REFUND.getCode()){
                    updateUserMoney(userId, money.negate());
                    Integer h=goods.getGoodsNumber()-1;
                    goods.setGoodsNumber(h);
                    goodsDAO.update(goods);
                    log.info("商品销售记录-1");
            }
            }
        userOperationLog.setCreateTime(new Date());
        userOperationLogDAO.insert(userOperationLog);
        return new Result(ShopCode.SHOP_SUCCESS.getCode(),ShopCode.SHOP_SUCCESS.getMessage());

    }

    public void updateUserMoney(Long userId,BigDecimal money){
        User user=findById(userId);
        BigDecimal Money=user.getUserMoney().subtract(money);
        if(Money.compareTo(BigDecimal.ZERO)>=0){
            user.setUserMoney(Money);
            update(user);
            log.info("扣减余额成功");
        }else {
            CastException.cast(ShopCode.SHOP_MONEY_PAID_INVALID);
        }
    }
}