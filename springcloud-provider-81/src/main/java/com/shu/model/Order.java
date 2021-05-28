package com.shu.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 */
@Data
public class Order implements Serializable {
    /**
     *
     */
    private Long orderId;
    /**
     *
     */
    private Long userId;
    /**
     *
     */
    private Integer orderStatus;
    /**
     *
     */
    private Integer payStatus;
    /**
     *
     */
    private Long goodsId;
    /**
     *
     */
    private BigDecimal goodsPrice;
    /**
     *
     */
    private BigDecimal moneyPaid;
    /**
     *
     */
    private Date addTime;
    /**
     *
     */
    private Date confirmTime;
    /**
     *
     */
    private Date payTime;
    private String file;
    private String fileOperation;
    private String tag;

}