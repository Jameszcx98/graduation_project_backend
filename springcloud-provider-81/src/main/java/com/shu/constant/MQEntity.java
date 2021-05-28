package com.shu.constant;

import java.math.BigDecimal;


public class MQEntity {

    private Long orderId;
//    private Long couponId;
    private Long userId;
    private BigDecimal userMoney;
    private Long goodsId;
    private String file;
    private String tag;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    private Integer num;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


//    private Integer goodsNum;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

//    public Long getCouponId() {
//        return couponId;
//    }

//    public void setCouponId(Long couponId) {
//        this.couponId = couponId;
//    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(BigDecimal userMoney) {
        this.userMoney = userMoney;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

//    public Integer getGoodsNum() {
//        return goodsNum;
//    }

//    public void setGoodsNum(Integer goodsNum) {
//        this.goodsNum = goodsNum;
//    }
}
