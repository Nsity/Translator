package com.translator.system.network;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.translator.system.CommonFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by nsity on 18.03.17.
 */

public class AsyncHttpResponse {

    private CallBack<ResponseObject> callBack;

    public static final int CALL_JSON_HTTP_RESPONSE = 0;
    public static final int CALL_POST_JSON_HTTP_RESPONSE = 1;
    public static final int CALL_SYNCHRONIZATION_RESPONSE = 2;

    private static final String EXCEPTION_TAG = "Exception";
    private static final String MESSAGE_TAG = "Message";
    public static final String MESSAGE = "timeout";

    public AsyncHttpResponse(Context context, String url, RequestParams params, int callMethod, CallBack<ResponseObject> callBack){
        this.callBack = callBack;


        AsyncHttpClient client = new SyncHttpClient();
        if ((callMethod == CALL_SYNCHRONIZATION_RESPONSE)) {
            client.setTimeout(30000);
            client.addHeader(AsyncHttpClient.HEADER_ACCEPT_ENCODING, AsyncHttpClient.ENCODING_GZIP);
        }

        switch (callMethod) {
            case CALL_JSON_HTTP_RESPONSE:
                callJsonHttpResponse(context, url, Server.getHttpClient());
                break;
            case CALL_POST_JSON_HTTP_RESPONSE:
                callPostJsonHttpResponse(context, url, params, Server.getHttpClient());
                break;
            case CALL_SYNCHRONIZATION_RESPONSE:
                callJsonHttpResponse(context, url, client);
                break;
        }
    }


    public void callJsonHttpResponse(Context context, String url, AsyncHttpClient client){
        client.get(context, url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                response = (response == null) ? new JSONObject() : response;
                callBack.onSuccess(new ResponseObject(statusCode, headers, response));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                response = (response == null) ? new JSONArray() : response;
                callBack.onSuccess(new ResponseObject(statusCode, headers, response));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                response = (CommonFunctions.StringIsNullOrEmpty(response)) ? "" : response;
                callBack.onSuccess(new ResponseObject(statusCode, headers, response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                errorResponse = (errorResponse == null) ? new JSONObject() : errorResponse;

                if (throwable instanceof SocketTimeoutException || throwable instanceof ConnectException) {
                    JSONObject error = new JSONObject();
                    try {
                        JSONObject exception = new JSONObject();
                        exception.put(MESSAGE_TAG, MESSAGE);
                        error.put(EXCEPTION_TAG, exception);
                    } catch (JSONException ignored) {
                    }
                    errorResponse = error;
                }

                callBack.onFailure(new ResponseObject(statusCode, headers, throwable, errorResponse));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                errorResponse = (errorResponse == null) ? new JSONArray() : errorResponse;
                callBack.onFailure(new ResponseObject(statusCode, headers, throwable, errorResponse));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable throwable) {
                errorResponse = (CommonFunctions.StringIsNullOrEmpty(errorResponse)) ? "" : errorResponse;
                callBack.onFailure(new ResponseObject(statusCode, headers, throwable, errorResponse));
            }
        });
    }


    public void callPostJsonHttpResponse(Context context, String url, RequestParams params, AsyncHttpClient client){
        client.post(context, url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                response = (response == null) ? new JSONObject() : response;
                callBack.onSuccess(new ResponseObject(statusCode, headers, response));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                response = (response == null) ? new JSONArray() : response;
                callBack.onSuccess(new ResponseObject(statusCode, headers, response));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                response = (CommonFunctions.StringIsNullOrEmpty(response)) ? "" : response;
                callBack.onSuccess(new ResponseObject(statusCode, headers, response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                errorResponse = (errorResponse == null) ? new JSONObject() : errorResponse;

                if (throwable instanceof SocketTimeoutException) {
                    JSONObject error = new JSONObject();
                    try {
                        JSONObject exception = new JSONObject();
                        exception.put(MESSAGE_TAG, MESSAGE);
                        error.put(EXCEPTION_TAG, exception);
                    } catch (JSONException ignored) {}
                    errorResponse = error;
                }

                callBack.onFailure(new ResponseObject(statusCode, headers, throwable, errorResponse));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                errorResponse = (errorResponse == null) ? new JSONArray() : errorResponse;
                callBack.onFailure(new ResponseObject(statusCode, headers, throwable, errorResponse));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable throwable) {
                errorResponse = (CommonFunctions.StringIsNullOrEmpty(errorResponse)) ? "" : errorResponse;
                callBack.onFailure(new ResponseObject(statusCode, headers, throwable, errorResponse));
            }
        });
    }
}
