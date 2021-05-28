package com.shu.constant;

import java.io.Serializable;

/**
 * 结果实体类
 */
public class Result implements Serializable {
    private Integer success;
    private String message;

    public Result() {
    }

    public Result(Integer success, String message) {
        this.success = success;
        this.message = message;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
