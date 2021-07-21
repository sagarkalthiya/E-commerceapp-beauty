package com.hariomgarments.pink.BackEnd.AndroidNetworking.common;


import com.hariomgarments.pink.BackEnd.AndroidNetworking.error.ANError;

import okhttp3.Response;

public class ANResponse<T> {

    private final T mResult;

    private final ANError mANError;

    private Response response;

    public static <T> ANResponse<T> success(T result) {
        return new ANResponse<>(result);
    }

    public static <T> ANResponse<T> failed(ANError anError) {
        return new ANResponse<>(anError);
    }

    public ANResponse(T result) {
        this.mResult = result;
        this.mANError = null;
    }

    public ANResponse(ANError anError) {
        this.mResult = null;
        this.mANError = anError;
    }

    public T getResult() {
        return mResult;
    }

    public boolean isSuccess() {
        return mANError == null;
    }

    public ANError getError() {
        return mANError;
    }

    public void setOkHttpResponse(Response response) {
        this.response = response;
    }

    public Response getOkHttpResponse() {
        return response;
    }

}
