package com.shu.service.mq;
import com.alibaba.fastjson.JSON;
import com.shu.constant.MQEntity;
import com.shu.constant.ShopCode;
import com.shu.dao.GoodsDAO;
import com.shu.dao.MqConsumerDAO;
import com.shu.model.Goods;
import com.shu.model.MqConsumer;
import com.shu.model.Order;
import com.shu.model.UserOperationLog;
import com.shu.service.impl.OrderGoodsLogServiceImpl;
import com.shu.service.impl.OrderServiceImpl;
import com.shu.service.impl.UserOperationLogServiceImpl;
import com.shu.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.topic1}",consumerGroup = "${mq.order.consumer.group.name1}",messageModel = MessageModel.BROADCASTING )
//MessageExt 有messageId
public class CancelMqListener implements RocketMQListener<MessageExt> {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    UserOperationLogServiceImpl userOperationLogService;
    @Autowired
    OrderGoodsLogServiceImpl orderGoodsLogService;
    @Autowired
    private MqConsumerDAO mqConsumerDAO;
    @Autowired
    private GoodsDAO goodsDAO;
    @Value(value = "${mq.order.consumer.group.name1}")
    private String groupNM;
    @Value(value="${mq.order.tag.cancel}")
    private String tag;

    @Value(value="${mq.order.tag.operation}")
    private String tagOperation;
    @Override
    public void onMessage(MessageExt messageExt) {
        try {
            //1.解析消息
            String msgId = messageExt.getMsgId();
            String tags = messageExt.getTags();//file_cancel
            String keys = messageExt.getKeys();//orderID
            String body = new String(messageExt.getBody(), "UTF-8");
            MQEntity mqEntity = JSON.parseObject(body, MQEntity.class);
            log.info("接收到消息：取消订单");
            //更新订单
            Order order = orderService.findById(Long.valueOf(keys));
            order.setOrderStatus(ShopCode.SHOP_ORDER_CANCEL.getCode());
            orderService.update(order);
            log.info("订单状态设置为取消");
            //记录商品交易日志
            orderService.reduceGoodsNum(order);
            //查询消息记录
            boolean flag=true;
            MqConsumer mqConsumer = mqConsumerDAO.findById(keys);
            if (mqConsumer != null) {
                //判断如果消费过
                //获得消息处理状态
                Integer status = mqConsumer.getConsumerStatus();
                //处理过。。。返回
                if (ShopCode.SHOP_MQ_MESSAGE_STATUS_SUCCESS.getCode().intValue() == status.intValue()) {
                    log.info("消息：" + msgId + "，已经处理过");
                    flag=false;

                }
                //正在处理。。。返回
                if (ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode().intValue() == status.intValue()) {
                    log.info("消息：" + msgId + "，正在处理");
                    flag=false;

                }

                //处理失败
                if (ShopCode.SHOP_MQ_MESSAGE_STATUS_FAIL.getCode().intValue() == status.intValue()) {
                    //获得消息处理次数
                    Integer times = mqConsumer.getConsumerTimes();
//                    if (times > 3) {
//                        log.info("消息：" + msgId + "，消息处理超过三次");
//
//                    }else {
                        mqConsumer.setConsumerStatus(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode());
                        int i = mqConsumerDAO.update(mqConsumer);
                        if (i <= 0) {
                            //修改未成功，其他线程并发修改
                            flag=false;
                            log.info("并发修改，稍后处理");
                        }
//                    }
                }
            } else {
                //判断没有消费过
                MqConsumer mqConsumerLog = new MqConsumer();
                mqConsumerLog.setMsgTag(tags);
                mqConsumerLog.setMsgKey(keys);
                mqConsumerLog.setGroupName(groupNM);
                mqConsumerLog.setConsumerStatus(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode());
                mqConsumerLog.setMsgBody(body);
                mqConsumerLog.setMsgId(msgId);
                mqConsumerLog.setConsumerTimes(0);
                mqConsumerDAO.insert(mqConsumerLog);
            }
            //回退库存 只有当没有消息，或者消息状态为失败再进行回退
            if(flag) {
                Long goodsId = mqEntity.getGoodsId();
                Goods goods = goodsDAO.findById(goodsId);
                goods.setGoodsNumber(goods.getGoodsNumber() - 1);
                int j=goodsDAO.update(goods);
                if (j == 1) log.info("回退库存成功");
                else log.info("并发修改，稍后处理");
                //6. 将消息的处理状态改为成功
                mqConsumer = mqConsumerDAO.findById(keys);
                mqConsumer.setConsumerStatus(ShopCode.SHOP_MQ_MESSAGE_STATUS_SUCCESS.getCode());
                mqConsumer.setConsumerTime(new Date());
                int i = mqConsumerDAO.update(mqConsumer);
                if (i == 1) log.info("消息状态保存成功");
                else log.info("并发修改，稍后处理");
            }
            //调用业务层,进行余额修改
            if(mqEntity.getUserMoney()!=null && mqEntity.getUserMoney().compareTo(BigDecimal.ZERO)>0){
                log.info("发起余额回退");
                UserOperationLog userMoneyLog = new UserOperationLog();
                userMoneyLog.setUseMoney(mqEntity.getUserMoney());
                userMoneyLog.setLogType(ShopCode.SHOP_USER_MONEY_REFUND.getCode());
                userMoneyLog.setUserId(mqEntity.getUserId());
                userMoneyLog.setOrderId(Long.valueOf(keys));
                userService.updateUserMoney(userMoneyLog);
            }
            if(deleteFile(order.getFile().substring(0,order.getFile().lastIndexOf("."))+"_compressed"+order.getTag()+".pdf")){
                log.info("删除文件"+order.getFile().substring(0,order.getFile().lastIndexOf("."))+"_compressed"+order.getTag()+".pdf"+"成功");
            }else {
                log.info("删除文件"+order.getFile().substring(0,order.getFile().lastIndexOf("."))+"_compressed"+order.getTag()+".pdf"+"失败");

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("回退失败");
        }

    }
    public  boolean deleteFile(String df) {
        File dirFile = new File(df);
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }

        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {

            for (File file : dirFile.listFiles()) {
                deleteFile(file.getAbsolutePath());
            }
        }

        return dirFile.delete();
    }
}
