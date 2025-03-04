package com.hinamlist.hinam_list_web.service.algorithm_converter.cart_reader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HaziHinamCartReader extends AbstractCartReader {

    public HaziHinamCartReader(@Value("${store.price-not-found-value}") float priceNotFoundValue) {
        super(priceNotFoundValue);
    }

    @Override
    protected JSONArray getProductJsonArray(JSONObject cartJsonObject) {
        return cartJsonObject.getJSONArray("Items");
    }

    @Override
    protected int getProductId(JSONObject productJsonObject) {
        return productJsonObject.getInt("Id");
    }

    @Override
    protected float getProductPrice(JSONObject productJsonObject) {
        return productJsonObject.getInt("Calculated_Price_NET");
    }
}
