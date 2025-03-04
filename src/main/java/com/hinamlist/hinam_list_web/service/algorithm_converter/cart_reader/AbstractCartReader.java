package com.hinamlist.hinam_list_web.service.algorithm_converter.cart_reader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractCartReader implements ICartReader{
    public final float priceNotFoundValue;
    protected abstract JSONArray getProductJsonArray(JSONObject cartJsonObject);
    protected abstract int getProductId(JSONObject productJsonObject);
    protected abstract float getProductPrice(JSONObject productJsonObject);

    public AbstractCartReader(float priceNotFoundValue) {
        this.priceNotFoundValue = priceNotFoundValue;
    }

    private Stream<JSONObject> getStreamFromJsonArray(JSONArray jsonArray) {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObjectList.add(jsonArray.getJSONObject(i));
        }
        return jsonObjectList.stream();
    }

    @Override
    public Map<Integer, Float> getProductIdPriceMap(JSONObject cartJsonObject) {
        JSONArray jsonProductArray = getProductJsonArray(cartJsonObject);
        return getStreamFromJsonArray(jsonProductArray).collect(Collectors.toMap(
                this::getProductId,
                this::getProductPrice
                ));
    }
}
