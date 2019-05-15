package com.campussay.carpool.net.app_net;

import java.util.List;

/**
 * create by WenJinG on 2019/4/21
 */
public class RequestErrorBean {
    /**
     * errorCode : SERVICE_ERROR
     * errorDetails : []
     * data : {}
     */

    private String errorCode;
    private DataBean data;
    private List<?> errorDetails;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public List<?> getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(List<?> errorDetails) {
        this.errorDetails = errorDetails;
    }

    public static class DataBean {
    }
}
