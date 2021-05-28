package com.shu.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 */
@Data
public class Goods implements Serializable {
    /**
     *
     */
    private Long goodsId;
    /**
     *
     */
    private String goodsName;
    /**
     *
     */
    private BigDecimal goodsPrice;
    private Integer goodsNumber;
    /**
     *
     */
    private String goodsDesc;
    /**
     *
     */
    private Date addTime;
    private Integer remark;
}