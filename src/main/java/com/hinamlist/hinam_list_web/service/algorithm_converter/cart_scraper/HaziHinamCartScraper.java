package com.hinamlist.hinam_list_web.service.algorithm_converter.cart_scraper;

import com.hinamlist.hinam_list_web.service.algorithm_converter.cart_scraper.exception.APIResponseException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

@Component
public class HaziHinamCartScraper extends AbstractCartScraper {
        public  HaziHinamCartScraper() throws IOException, InterruptedException, APIResponseException {
            super();
            String uriString = "https://shop.hazi-hinam.co.il/proxy/init";
            HttpRequest request = createHttpGetRequest(uriString);
            getResponse(request);
        }

        public JSONObject getCartObject(Map<Integer, Float> idQuantityMap) throws IOException, InterruptedException, APIResponseException {
            String uriString;
            HttpRequest request;
            String response;



            for (var item: idQuantityMap.entrySet()) {
                addCartItem(item.getKey(), item.getValue());
            }

            uriString = "https://shop.hazi-hinam.co.il/proxy/api/item/getItemsInCart?SortBy=2&IsDescending=false";
            request = createHttpGetRequest(uriString);
            response = getResponse(request);
            //System.out.println("Res: " + response);
            return  new JSONObject(response).getJSONObject("Results");

        }

    public void addCartItem(int id, float quantity) throws IOException, InterruptedException, APIResponseException {
        String uriString;
        HttpRequest request;
        Map<String, String> headers = new HashMap<>();
        Map<String, Object> bodyMap = new HashMap<>();

        uriString = "https://shop.hazi-hinam.co.il/proxy/api/item/addItemToCart";

        JSONObject itemObject = new JSONObject();
        itemObject.put("Quantity", quantity);
        itemObject.put("ItemId", id);
        itemObject.put("IsCalculateCart", false);
        itemObject.put("Type", (quantity % 1 == 0) ? 1 : 2);

        bodyMap.put("Object", itemObject);

        request = createHttpPostRequest(uriString, headers, bodyMap);
        getResponse(request);

    }
}
