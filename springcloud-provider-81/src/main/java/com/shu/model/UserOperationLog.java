package com.shu.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 */
@Data
public class UserOperationLog implements Serializable {
    /**
     *
     */
    private Long userId;
    /**
     *
     */
    private Long orderId;
    /**
     *
     */
    private BigDecimal useMoney;
    /**
     *
     */
    private Date createTime;
    private Integer logType;
}