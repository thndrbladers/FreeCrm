package com.freecrm.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RootResponse {
    @JsonProperty("response")
    private ResponseData response;
    @JsonProperty("process_time")
    private double processTime;

    public ResponseData getResponse() {
        return response;
    }

    public void setResponse(ResponseData response) {
        this.response = response;
    }

    public double getProcessTime() {
        return processTime;
    }

    public void setProcessTime(double processTime) {
        this.processTime = processTime;
    }

    @Override
    public String toString() {
        return "RootResponse{" +
                "response=" + response + ", " +
                "processTime=" + processTime +
                "}";
    }
}