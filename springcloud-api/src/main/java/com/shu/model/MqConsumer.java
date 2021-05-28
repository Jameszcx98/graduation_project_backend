package com.shu.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
@Data
public class MqConsumer implements Serializable {
    /**
     *
     */
    private String msgId;
    /**
     *
     */
    private String groupName;
    /**
     *
     */
    private String msgTag;
    /**
     *
     */
    private String msgKey;
    /**
     *
     */
    private String msgBody;
    /**
     *
     */
    private Integer consumerStatus;
    /**
     *
     */
    private Integer consumerTimes;
    /**
     *
     */
    private Date consumerTime;
    /**
     *
     */
    private Integer remark;
}