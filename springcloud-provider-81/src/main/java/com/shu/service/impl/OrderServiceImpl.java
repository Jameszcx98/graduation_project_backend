package com.shu.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shu.constant.MQEntity;
import com.shu.constant.Result;
import com.shu.constant.ShopCode;
import com.shu.dao.OrderDAO;
import com.shu.dao.OrderGoodsLogDAO;
import com.shu.dao.UserDAO;
import com.shu.exception.CastException;
import com.shu.model.*;
import com.shu.service.GoodsService;
import com.shu.service.OrderService;
import com.shu.service.UserService;
import com.shu.utils.IDWorker;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;

@Data
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderGoodsLogDAO orderGoodsLogDAO;
    @Autowired
    private UserDAO userDAO;


    @Autowired
    @Qualifier("getThreadPool")
    private ThreadPoolTaskExecutor thread;


    @Autowired
    private IDWorker idWorker;

    @Value(value="${mq.order.topic1}")
    private String topic1;

    @Value(value="${mq.order.topic2}")
    private String topic2;

    @Value(value="${mq.order.tag.cancel}")
    private String tag;

    @Value(value="${mq.order.tag.operation}")
    private String tagOperation;


    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    @Transactional(readOnly = true)
    @Override
    public Order findById(Long id) {
        return orderDAO.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<Order> findByPage(int pageNum, int pageSize,String email) {
        PageHelper.startPage(pageNum, pageSize);
        User user = userDAO.findByEmail(email);
        return PageInfo.of(orderDAO.findByPage(user.getUserId()));
    }

    @Override
    public Result insert(String email, Long goodsId,String file,String rate) {
        User user = userDAO.findByEmail(email);
        log.info(user.toString());
        Order order = new Order();
        order.setUserId(user.getUserId());
        order.setGoodsId(goodsId);
        order.setGoodsPrice(goodsService.findById(goodsId).getGoodsPrice());
        order.setMoneyPaid(goodsService.findById(goodsId).getGoodsPrice());
        order.setFile(file);
        order.setTag(rate);
        log.info("接收订单参数"+order.toString());
//        orderDAO.insert(order);
        return confirmOrder1(order);
    }

    @Override
    public void update(Order order) {
        orderDAO.update(order);
    }


    @Override
    public void deleteById(Long id) {
        orderDAO.deleteById(id);
    }


    @Override
    public Result confirmOrder1(Order order) {
        //1.校验订单
        Result result = checkOrder(order);
        if(result.getSuccess()==ShopCode.SHOP_ORDER_FINISHED.getCode()){
            return result;
        }else{
            //2.生成预订单
            savePreOrder(order);
            return confirmOrder2(order);
        }

    }
    @Override
    public Result confirmOrder2(Order order){
        Long orderId = order.getOrderId();
        try {
            sendOrder(order,topic2,tagOperation,false);
            //5.使用余额
            reduceMoneyPaid(order);
            //模拟异常抛出
//            CastException.cast(ShopCode.SHOP_FAIL);
            //6.确认订单
            updateOrderStatus(order);
            log.info("订单:["+orderId+"]确认成功");
            reduceGoodsNum(order);
            //7.返回成功状态
            return new Result(ShopCode.SHOP_SUCCESS.getCode(),ShopCode.SHOP_SUCCESS.getMessage());
        } catch (Exception e) {
            log.info("确认订单失败，报错原因"+e.getMessage(),e);
            //确认订单发送失败
            try {
                sendOrder(order, topic1, tag,true);
            }catch (Exception e1) {
                log.info(e1.getMessage(),e1);
                CastException.cast(ShopCode.SHOP_MQ_SEND_MESSAGE_FAIL);
            }
            return new Result(ShopCode.SHOP_FAIL.getCode(), ShopCode.SHOP_FAIL.getMessage());
        }
    }

    /*
     * @Author: jameszhang
     * @Description:发送订单
     * @param order :
     * @param topic :
     * @param tags :
     * @return: void
     **/
    public void sendOrder(Order order,String topic,String tags,boolean flag) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        //1.构造消息
        MQEntity mqEntity = new MQEntity();
        //订单id
        mqEntity.setOrderId(order.getOrderId());
        //用户id
        mqEntity.setUserId(order.getUserId());
        //用户已付金额
        mqEntity.setUserMoney(order.getMoneyPaid());
        //商品id
        mqEntity.setGoodsId(order.getGoodsId());
        mqEntity.setFile(order.getFile());
        mqEntity.setTag(order.getTag());
        //判断Topic是否为空
        if (StringUtils.isEmpty(String.valueOf(mqEntity.getOrderId()))) {
            CastException.cast(ShopCode.SHOP_MQ_MESSAGE_BODY_IS_EMPTY);
        }
        //判断Topic是否为空
        if (StringUtils.isEmpty(topic)) {
            CastException.cast(ShopCode.SHOP_MQ_TOPIC_IS_EMPTY);
        }
        if(flag){
            SendResult result=null;
        try {
            Message message = new Message(topic,tags,mqEntity.getOrderId().toString(),JSON.toJSONString(mqEntity).getBytes());
            result=rocketMQTemplate.getProducer().send(message);
            if(result.getSendStatus().equals(SendStatus.SEND_OK)){
                log.info("成功发送订单失败消息");
            }

        } catch (Exception e1) {
            System.out.println("取消消息错误信息");


        }}else {
            thread.submit(new Runnable() {
                @Override
                public void run() {
                    SendResult result=null;
                    try {
                        Message message = new Message(topic,tags,mqEntity.getOrderId().toString(),JSON.toJSONString(mqEntity).getBytes());
                        result=rocketMQTemplate.getProducer().send(message);
                    }catch (MQClientException e2){

                    }
                    catch (Exception e1) {
                        System.out.println("打印压缩文件消息错误信息");
                        e1.printStackTrace();
                    }
                    if(result.getSendStatus().equals(SendStatus.SEND_OK)){
                        log.info("文件压缩消息发送成功");
                    }

                }
            });


        }
    }



    /**
     * 校验订单
     *
     * @param order
     */
    public Result checkOrder(Order order) {
        //1.校验订单是否存在
        if (order == null) {
            CastException.cast(ShopCode.SHOP_ORDER_INVALID);
        }
        //2.校验订单中的商品是否存在
        Goods goods = goodsService.findById(order.getGoodsId());
        if (goods == null) {
            CastException.cast(ShopCode.SHOP_GOODS_NO_EXIST);
        }
        //3.校验下单用户是否存在
        User user = userService.findById(order.getUserId());
        if (user == null) {
            CastException.cast(ShopCode.SHOP_USER_NO_EXIST);
        }
        //4.校验商品单价是否合法
        if (order.getGoodsPrice().compareTo(goods.getGoodsPrice()) != 0) {
            CastException.cast(ShopCode.SHOP_GOODS_PRICE_INVALID);
        }
        Order order1 = orderDAO.findByFile(order.getFile());
        //之前没有创建过订单
        if(order1==null){
            log.info("校验订单通过");
            return new Result(ShopCode.SHOP_ORDER_CHECK.getCode(),ShopCode.SHOP_ORDER_CHECK.getMessage());
        }else{
            if(order1.getOrderStatus()==ShopCode.SHOP_ORDER_CONFIRM.getCode()){
                log.info("订单已完成");
                return new Result(ShopCode.SHOP_ORDER_FINISHED.getCode(),ShopCode.SHOP_ORDER_FINISHED.getMessage());

            }else{
                log.info("有订单记录，订单再次处理");
                return new Result(ShopCode.SHOP_ORDER_NO_CONFIRM.getCode(),ShopCode.SHOP_ORDER_NO_CONFIRM.getMessage());

            }

        }

    }
    /**
     * 生成预订单
     *
     * @param order
     * @return
     */
    public Long savePreOrder(Order order) {
        log.info("开始生成预订单"+order.toString());
//        1. 设置订单状态为不可见
        order.setOrderStatus(ShopCode.SHOP_ORDER_NO_CONFIRM.getCode());
        //2. 设置订单ID
        Long orderId = idWorker.nextId();
        order.setOrderId(orderId);
        //8.设置下单时间
        order.setAddTime(new Date());
        System.out.println("预订单生成"+order.toString());
        //9.保存订单到数据库
        orderDAO.insert(order);
        //10.返回订单ID
        return orderId;


    }

    /**
     * 确认订单
     * @param order
     */
    public void updateOrderStatus(Order order) {
        order.setFileOperation(setOFile(order));
        order.setOrderStatus(ShopCode.SHOP_ORDER_CONFIRM.getCode());
        order.setPayStatus(ShopCode.SHOP_ORDER_PAY_STATUS_IS_PAY.getCode());
        order.setConfirmTime(new Date());
        int r = orderDAO.update(order);
        if(r<=0){
            CastException.cast(ShopCode.SHOP_ORDER_CONFIRM_FAIL);
        }
        log.info("订单:"+order.getOrderId()+"确认订单成功");
    }

    /**
     * 扣减余额
     * 由于不需要调用第三方支付程序，所以直接扣款，暂不需要order_pay设置订单支付状态
     * @param order
     */
    public void reduceMoneyPaid(Order order) {
//        if(order.getMoneyPaid()!=null && order.getMoneyPaid().compareTo(BigDecimal.ZERO)==1){
            UserOperationLog userMoneyLog = new UserOperationLog();
            userMoneyLog.setOrderId(order.getOrderId());
            userMoneyLog.setUserId(order.getUserId());
            userMoneyLog.setUseMoney(order.getMoneyPaid());
            userMoneyLog.setLogType(ShopCode.SHOP_USER_MONEY_PAID.getCode());
            userMoneyLog.setCreateTime(new Date());
            Result result = userService.updateUserMoney(userMoneyLog);
            if(result.getSuccess().equals(ShopCode.SHOP_FAIL.getSuccess())){
                CastException.cast(ShopCode.SHOP_USER_MONEY_REDUCE_FAIL);
            }
            log.info("订单:"+order.getOrderId()+",扣减余额成功");

    }


    /**
     * 记录订单日志
     * @param order
     */
    public void reduceGoodsNum(Order order) {
        OrderGoodsLog orderGoodsLog = new OrderGoodsLog();
        orderGoodsLog.setOrderId(order.getOrderId());
        orderGoodsLog.setGoodsId(order.getGoodsId());
        orderGoodsLog.setLogTime(new Date());
        orderGoodsLog.setOrderStatus(order.getOrderStatus());
        orderGoodsLogDAO.insert(orderGoodsLog);
        log.info("订单:"+order.getOrderId()+"日志记录成功");
    }
    public String setOFile(Order order) {
        int a=Integer.valueOf(String.valueOf(order.getGoodsId()));
        String file=new String();
        String finalFile=order.getFile().substring(0,order.getFile().lastIndexOf("."))+"_operated"+
                order.getTag()+order.getFile().substring(order.getFile().lastIndexOf("."));
        switch (a) {
            case 2:
                file=finalFile.substring(0, finalFile.lastIndexOf(".")) + ".jpg";
                break;
            case 3:
                file=finalFile;
                break;
            case 4:
                file=finalFile.substring(0, finalFile.lastIndexOf(".")) + ".mp3";
                break;
            case 5:
                if (order.getTag().equals("mp3")) {
                    file=finalFile.substring(0, finalFile.lastIndexOf(".")) + ".mp3";
                } else if (order.getTag().equals("wav")) {
                    file=finalFile.substring(0, finalFile.lastIndexOf(".")) + ".wav";
                }
                break;
            case 6:
               file=finalFile.substring(0, finalFile.lastIndexOf(".")) + ".mp4";
               break;
            case 7:
//                    avi mp4 mov flv 3gp
                if (order.getTag().equals("avi")) {
                    file=finalFile.substring(0, finalFile.lastIndexOf(".")) + ".avi";
                } else if (order.getTag().equals("mp4")) {
                    file=finalFile.substring(0, finalFile.lastIndexOf(".")) + ".mp4";
                } else if (order.getTag().equals("mov")) {
                    file=finalFile.substring(0, finalFile.lastIndexOf(".")) + ".mov";
                } else if (order.getTag().equals("flv")) {
                    file=finalFile.substring(0, finalFile.lastIndexOf(".")) + ".flv";
                } else if (order.getTag().equals("3gp")) {
                    file=finalFile.substring(0, finalFile.lastIndexOf(".")) + ".3gp";
                }
                break;

        }
        return file;
    }


}