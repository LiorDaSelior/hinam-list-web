package com.hinamlist.hinam_list_web.service.algorithm_converter.cart_reader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RamiLeviCartReader extends AbstractCartReader{

    public RamiLeviCartReader(@Value("${store.price-not-found-value}") float priceNotFoundValue) {
        super(priceNotFoundValue);
    }

    @Override
    protected JSONArray getProductJsonArray(JSONObject cartJsonObject) {
        return cartJsonObject.getJSONArray("items");
    }

    @Override
    protected int getProductId(JSONObject productJsonObject) {
        return productJsonObject.getInt("id");
    }

    @Override
    protected float getProductPrice(JSONObject productJsonObject) {
        var salesObject = productJsonObject.getJSONObject("countUsesSale");
        float num;
        if (salesObject.isEmpty())
            num =  productJsonObject.getFloat("FormatedTotalPriceWithoutDiscount");
        else
            num = productJsonObject.getFloat("FormatedTotalPriceWallet");
        return (float) (Math.round(num * 100.0) / 100.0);
    }
}
