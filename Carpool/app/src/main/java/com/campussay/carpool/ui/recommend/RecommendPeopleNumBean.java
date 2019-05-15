package com.campussay.carpool.ui.recommend;

import com.campussay.carpool.net.BaseBean;

/**
 * creat by teng on 2019/4/17
 */
public class RecommendPeopleNumBean {

    private boolean success;
    private int code;
    private String message;
    private String data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
