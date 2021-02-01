package com.youkeda.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @date 2020/6/6, 周六
 */

public class Result<T> {
    @JsonProperty("success")
    protected Boolean success = false;
    @JsonProperty("code")
    protected String code = null;
    @JsonProperty("message")
    protected String message = null;
    @JsonProperty("timestamp")
    protected Long timestamp = null;
    protected T data = null;

    public Result() {
    }

    public Result isSuccess(Boolean isSuccess) {
        this.success = isSuccess;
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Result code(String code) {
        this.code = code;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Result message(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result<T> data(T data) {
        this.setData(data);
        return this;
    }

    public Long getTimeStamp() {
        return this.timestamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timestamp = timeStamp;
    }
}

