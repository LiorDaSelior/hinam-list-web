package com.hinamlist.hinam_list_web.service.algorithm_converter.cart_reader;

import org.json.JSONArray;
import org.json.JSONObject;

public class CarrfourCartReader extends AbstractCartReader{
    @Override
    protected JSONArray getProductJsonArray(JSONObject cartJsonObject) {
        return cartJsonObject.getJSONArray("lines");
    }

    @Override
    protected int getProductId(JSONObject productJsonObject) {
        return productJsonObject.getInt("retailerProductId");
    }

    @Override
    protected float getProductPrice(JSONObject productJsonObject) {
        return productJsonObject.getInt("actualPrice");
    }
}
