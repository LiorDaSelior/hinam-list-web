package com.hinamlist.hinam_list_web.service.algorithm_converter.cart_scraper;

import com.hinamlist.hinam_list_web.service.algorithm_converter.cart_scraper.exception.APIResponseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

@Component
public class CarrfourCartScraper extends AbstractCartScraper {
    public JSONObject getCartObject(Map<Integer, Float> idQuantityMap) throws IOException, InterruptedException, APIResponseException {
        String uriString;
        HttpRequest request;
        String response;
        Map<String, String> headers = new HashMap<>();
        Map<String, Object> bodyMap = new HashMap<>();

        uriString = "https://www.carrefour.co.il/v2/retailers/1540/branches/2995/carts?appId=4";

        headers.put("accept", "application/json, text/plain, */*");
        headers.put("accept-language", "en-US,en;q=0.9");
        headers.put("origin", "https://www.carrefour.co.il");
        headers.put("referer", "https://www.carrefour.co.il/");
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36");

        JSONArray array = new JSONArray();
        JSONObject itemObject;

        for (var item : idQuantityMap.entrySet()) {
            itemObject = new JSONObject();
            itemObject.put("quantity", item.getValue());
            itemObject.put("retailerProductId", item.getKey());
            itemObject.put("soldBy", JSONObject.NULL);
            itemObject.put("type", 1);
            array.put(itemObject);
        }

        bodyMap.put("lines", array);

        request = createHttpPostRequest(uriString, headers, bodyMap);
        response = getResponse(request);;
        return new JSONObject(response).getJSONObject("cart");
    }
}

