package com.shu.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 */
@Data
public class OrderPay implements Serializable {
    /**
     *
     */
    private Long payId;
    /**
     *
     */
    private Long orderId;
    /**
     *
     */
    private BigDecimal payAmount;
    /**
     *
     */
    private boolean paid;
}