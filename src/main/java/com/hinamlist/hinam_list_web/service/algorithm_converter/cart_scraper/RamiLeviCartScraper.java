package com.hinamlist.hinam_list_web.service.algorithm_converter.cart_scraper;

import com.hinamlist.hinam_list_web.service.algorithm_converter.cart_scraper.exception.APIResponseException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class RamiLeviCartScraper extends  AbstractCartScraper {
    public JSONObject getCartObject(Map<Integer, Float> idQuantityMap) throws IOException, InterruptedException, APIResponseException {
        String uriString;
        HttpRequest request;
        String response;

        Map<String, String> headers = new HashMap<>();
        Map<String, Object> bodyMap = new HashMap<>();

        uriString = "https://www.rami-levy.co.il/api/v2/cart";

        JSONObject itemObject = new JSONObject();
        for (var item : idQuantityMap.entrySet()) {
            itemObject.put(String.valueOf(item.getKey()), String.valueOf(item.getValue()));
        }

        bodyMap.put("items", itemObject);
        bodyMap.put("isClub", 0);
        bodyMap.put("store", 331);
        bodyMap.put("supplyAt",
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "T00:00:00.000Z");
        bodyMap.put("meta",
                JSONObject.NULL);
        request = createHttpPostRequest(uriString, headers, bodyMap);
        response = getResponse(request);
        return new JSONObject(response);
    }
}
