package com.translator.system.network;

import cz.msebera.android.httpclient.Header;

/**
 * Created by nsity on 18.03.17.
 */

public class ResponseObject {
    private int statusCode;
    private Header[] headers;
    private Throwable error;
    private Object response;

    public ResponseObject(int statusCode, Header[] headers, Object response){
        this.statusCode = statusCode;
        this.headers = headers;
        this.response = response;
    }

    public ResponseObject(int statusCode, Header[] headers, Throwable error, Object response){
        this.statusCode = statusCode;
        this.headers = headers;
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public Throwable getError() {
        return error;
    }

    public Object getResponse() {
        return response;
    }
}
