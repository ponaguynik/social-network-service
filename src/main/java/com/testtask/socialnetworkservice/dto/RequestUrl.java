package com.testtask.socialnetworkservice.dto;

public class RequestUrl {
    private String url;

    public RequestUrl() {
    }

    public RequestUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }
}
