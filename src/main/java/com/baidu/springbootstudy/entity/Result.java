// Copyright 2018 Baidu Inc BDG. All rights reserved.

package com.baidu.springbootstudy.entity;

/**
 * 统一结果格式 返回json序列化
 *
 * @author
 *
 */

public class Result<T> {

    private String msg;
    private String taskId;
    private Integer taskStatus;

    private Integer errorCode;
    private String errorMsg;

    private T data;

    public Result() {
        // default to design a constructor
    }

    public Result(String msg, String taskId, Integer taskStatus, Integer errorCode, String errorMsg, T data) {
        this.msg = msg;
        this.taskId = taskId;
        this.taskStatus = taskStatus;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setSuccessed() {
    }

    public void setFailed() {
        setErrorCode(-1);
    }

    public void setFailed(Exception e) {
        setErrorMsg(e.getMessage());
    }



    @Override
    public String toString() {
        return "Result{"
                + "msg='" + msg + '\''
                + ", taskId='" + taskId + '\''
                + ", taskStatus=" + taskStatus
                + ", errorCode=" + errorCode
                + ", errorMsg='" + errorMsg + '\''
                + ", data=" + data
                + '}';
    }
}
