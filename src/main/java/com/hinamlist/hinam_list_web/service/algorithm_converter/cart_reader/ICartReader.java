package com.hinamlist.hinam_list_web.service.algorithm_converter.cart_reader;

import org.json.JSONObject;

import java.util.Map;

public interface ICartReader {
    Map<Integer, Float> getProductIdPriceMap(JSONObject cartJsonObject);
}
