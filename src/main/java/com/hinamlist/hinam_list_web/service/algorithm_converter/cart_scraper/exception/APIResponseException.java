package com.hinamlist.hinam_list_web.service.algorithm_converter.cart_scraper.exception;

public class APIResponseException extends Exception {
    private int statusCode;
    public APIResponseException(int statusCode) {
        super(String.format("Unexpected API Response - Response code is not 200: %d", statusCode));
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
