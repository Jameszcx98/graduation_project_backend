package com.shu.service.mq;


import com.alibaba.fastjson.JSON;
import com.shu.constant.MQEntity;
import com.shu.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.topic2}",consumerGroup = "${mq.order.consumer.group.name2}",messageModel = MessageModel.BROADCASTING )
//MessageExt 有messageId
public class FileMqListener implements RocketMQListener<MessageExt> {



    private static final String REST_URL_PREFIX = "http://springcloud-provider-dept";

    @Override
    public void onMessage(MessageExt messageExt) {

            //1.解析消息
            String msgId = messageExt.getMsgId();
            String tags = messageExt.getTags(); //file_operation
            String keys = messageExt.getKeys(); //订单ID
        try{
            String body = new String(messageExt.getBody(), "UTF-8");
            MQEntity mqEntity = JSON.parseObject(body, MQEntity.class);
            int a=mqEntity.getGoodsId().intValue();
            log.info("接收到文件处理消息，文件为为："+mqEntity.getFile()+mqEntity.getTag());
            switch (a)
            {
                case 4:
                    log.info("开始音频压缩");
                    break;
                case 5:
//                    image;
                    break;
                case 6:
                    log.info("开始视频压缩");
                    break;
                case 7:
//                    pdfCompress;
                    break;
            }




            log.info("接收到文件处理消息");
        }catch (Exception e){

        }
    }
}
