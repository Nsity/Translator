package com.translator.system.network;

/**
 * Created by nsity on 18.03.17.
 */

public class CallBack<T> {
    public void onSuccess() {
    }

    public void onSuccess(T result) {
    }

    public void onFail(String message) {

    }

    public void onFailure(T result) {

    }
}
