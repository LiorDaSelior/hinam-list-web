package com.hinamlist.hinam_list_web.service.algorithm_converter.cart_scraper;

import com.hinamlist.hinam_list_web.service.algorithm_converter.cart_scraper.exception.APIResponseException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Component
public abstract class AbstractCartScraper implements ICartScraper {
    protected HttpClient client;
    protected CookieManager cookieManager;

    public AbstractCartScraper() {
        this.cookieManager = new CookieManager();
        this.cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        this.client = HttpClient.newBuilder().cookieHandler(this.cookieManager).followRedirects(HttpClient.Redirect.NEVER).build();
    }

    protected HttpRequest.Builder createHttpBuilder(String uriString) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        try {
            requestBuilder.uri(new URI(uriString));
        } catch (URISyntaxException uriExp) { // uri string is incorrect
            throw (new RuntimeException("Target URI is not in the correct format", uriExp));
        }
        return requestBuilder;
    }

    protected HttpRequest createHttpGetRequest(String uriString) {
        var builder = createHttpBuilder(uriString);
        builder.GET();
        return builder.build();
    }

    protected HttpRequest createHttpPostRequest(String uriString, Map<String, String> headerMap, Map<String,Object> propertyMap) {
        var builder = createHttpBuilder(uriString);
        builder.header("Content-Type", "application/json; charset=utf-8");

        for (var header : headerMap.entrySet()) {
            builder.header(header.getKey(), header.getValue());
        }

        JSONObject obj = new JSONObject(propertyMap);
        builder.POST(HttpRequest.BodyPublishers.ofString(obj.toString()));
        return builder.build();
    }

    protected String getResponse(HttpRequest request) throws IOException, InterruptedException, APIResponseException {

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //System.out.println("Response Headers: " + response.headers());

        int responseCode = response.statusCode();
        if (responseCode >= 400) {
            throw (new APIResponseException(responseCode));
        }
        return response.body();
    }
}
