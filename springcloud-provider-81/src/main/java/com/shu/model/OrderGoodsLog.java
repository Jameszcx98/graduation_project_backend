package com.shu.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
@Data
public class OrderGoodsLog implements Serializable {
    /**
     *
     */
    private Long goodsId;
    /**
     *
     */
    private Long orderId;
    /**
     *
     */
    private Date logTime;
    private Integer orderStatus;
}