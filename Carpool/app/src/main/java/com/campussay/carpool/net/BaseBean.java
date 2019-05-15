package com.campussay.carpool.net;

/**
 * create by zuyuan on 2019/4/5
 */
public class BaseBean<T> {

    /**
     * {
     * "success": true,
     * "code": 0,
     * "message": "SUCCESS",
     * "data": {}
     * }*/

    private boolean success;
    private int code;
    private String message;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
