package com.campussay.carpool.net.app_net;

/**
 * create by WenJinG on 2019/4/21
 */
public class RequestErrorException extends RuntimeException{
    private RequestErrorBean requestErrorBean;
    private String errorCode;


    RequestErrorException(String errorCode, RequestErrorBean requestErrorBean){
        super(errorCode);
        this.requestErrorBean = requestErrorBean;
        this.errorCode = errorCode;
    }

    public RequestErrorBean getRequestErrorBean() {
        return requestErrorBean;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
