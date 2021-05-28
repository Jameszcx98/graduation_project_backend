package com.shu.service.mq;


import com.alibaba.fastjson.JSON;
import com.shu.constant.MQEntity;
import com.shu.constant.ShopCode;
import com.shu.exception.CastException;
import com.shu.model.Order;
import com.shu.service.OrderService;
import com.shu.service.impl.AudioAndVideoImpl;
import com.shu.service.impl.Image;
import com.shu.service.impl.PdfCompress;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.topic2}",consumerGroup = "${mq.order.consumer.group.name2}",messageModel = MessageModel.BROADCASTING )
//MessageExt 有messageId
public class FileMqListener implements RocketMQListener<MessageExt> {

    @Value(value="${mq.order.tag.cancel}")
    private String tag;

    @Value(value="${mq.order.tag.operation}")
    private String tagOperation;


    @Value(value="${mq.order.topic1}")
    private String topic1;

    @Value(value="${mq.order.topic2}")
    private String topic2;


    @Value(value="${file.upload}")
    private String filePrefix;


    @Autowired
    private AudioAndVideoImpl audioAndVideo;


    @Autowired
    private OrderService orderService;
    @Autowired
    private Image image;
    @Autowired
    private PdfCompress pdfCompress;
    @Override
    public void onMessage(MessageExt messageExt) {
        //1.解析消息
        String msgId = messageExt.getMsgId();
        String tags = messageExt.getTags(); //file_operation
        String keys = messageExt.getKeys(); //订单ID
        try {
            String body = new String(messageExt.getBody(), "UTF-8");
            MQEntity mqEntity = JSON.parseObject(body, MQEntity.class);
            int a=mqEntity.getGoodsId().intValue();
            Order order = orderService.findById(Long.valueOf(keys));
            String fileCompressed=order.getFile().substring(0,order.getFile().lastIndexOf("."))+"_operated"+
                    order.getTag()+order.getFile().substring(order.getFile().lastIndexOf("."));
            String originFile=filePrefix+order.getFile();
            String finalFile=filePrefix+fileCompressed;//原后缀
            switch (a)
            {
                case 1:
//                    image;
                    break;
                case 2:
                    log.info("照片开始处理");
                    image.work(originFile,finalFile.substring(0,finalFile.lastIndexOf("."))+".jpg",order.getTag());
                    log.info("照片处理成功");
                    break;
                case 3:
                    log.info("pdf开始处理");
                    pdfCompress.start(originFile,finalFile,order.getTag());
                    log.info("pdf处理成功");
                    break;
                case 4:
                    log.info("开始音频压缩");
                    audioAndVideo.audioCompress(originFile,finalFile.substring(0,finalFile.lastIndexOf("."))+".mp3",order.getTag());
                    log.info("音频压缩成功");
                    break;
                case 5:
                    log.info("开始音频转换");
                    if(order.getTag().equals("mp3")) {
                        audioAndVideo.m4aToMp3(originFile, finalFile.substring(0, finalFile.lastIndexOf(".")) + ".mp3");
                    }else if(order.getTag().equals("wav")){
                        audioAndVideo.m4aToWav(originFile, finalFile.substring(0, finalFile.lastIndexOf(".")) + ".wav");
                    }
                    log.info("音频转换完成");
                    break;
                case 6:
                    log.info("开始视频压缩");
                    audioAndVideo.videoCompress(originFile,finalFile.substring(0,finalFile.lastIndexOf("."))+".mp4",order.getTag());
                    log.info("视频压缩成功");
                    break;
                case 7:
//                    avi mp4 mov flv 3gp
                    log.info("开始视频转换");
                    if(order.getTag().equals("avi")) {
                        audioAndVideo.movTomp4(originFile, finalFile.substring(0, finalFile.lastIndexOf(".")) + ".avi");
                    }else if(order.getTag().equals("mp4")){
                        audioAndVideo.movTomp4(originFile, finalFile.substring(0, finalFile.lastIndexOf(".")) + ".mp4");
                    }else if(order.getTag().equals("mov")){
                        audioAndVideo.movTomp4(originFile, finalFile.substring(0, finalFile.lastIndexOf(".")) + ".mov");
                    }else if(order.getTag().equals("flv")){
                        audioAndVideo.movTomp4(originFile, finalFile.substring(0, finalFile.lastIndexOf(".")) + ".flv");
                    }else if(order.getTag().equals("3gp")){
                        audioAndVideo.movTomp4(originFile, finalFile.substring(0, finalFile.lastIndexOf(".")) + ".3gp");
                    }
                    log.info("音频视频完成");

                    break;
            }
        }catch (NullPointerException e){
            log.info("未找到文件，正在再次发消息");
            try {
                orderService.sendOrder(orderService.findById(Long.valueOf(messageExt.getKeys())), topic1, tag,true);
            }catch (Exception e1) {
                log.info("文件压缩模块发送cancel消息失败");
                e1.printStackTrace();
                CastException.cast(ShopCode.SHOP_MQ_SEND_MESSAGE_FAIL);
            }
        }catch (Exception e){
            log.info("文件操作失败，正在发送cancel消息");
            try {
                orderService.sendOrder(orderService.findById(Long.valueOf(messageExt.getKeys())), topic1, tag,true);
            }catch (Exception e1) {
                log.info("文件压缩模块发送cancel消息失败");
                e1.printStackTrace();
                CastException.cast(ShopCode.SHOP_MQ_SEND_MESSAGE_FAIL);
            }

        }
    }


}
