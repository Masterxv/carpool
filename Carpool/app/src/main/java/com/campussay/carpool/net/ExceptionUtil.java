package com.campussay.carpool.net;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

/**
 * create by zuyuan on 2019/4/5
 */
public class ExceptionUtil {

    public static final int PARSE_ERROR = 1001;

    public static final int CONNECT_ERROR = 1002;

    public static final int UNKNOWN_ERROR = 1003;

    public static int paresException(Throwable e) {
        if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            return PARSE_ERROR;
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException
                || e instanceof SocketTimeoutException){
            return CONNECT_ERROR;
        } else {
            return UNKNOWN_ERROR;
        }
    }
}
