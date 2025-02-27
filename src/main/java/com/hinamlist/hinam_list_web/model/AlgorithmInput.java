package com.hinamlist.hinam_list_web.model;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AlgorithmInput {
    private int storeNumber;
    private Map<String, Integer> productAmountMap;

    public AlgorithmInput() {}

    public AlgorithmInput(int storeNumber, Map<String, Integer> productAmountMap) {
        this.storeNumber = storeNumber;
        this.productAmountMap = productAmountMap;
    }

    public int getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(int storeNumber) {
        this.storeNumber = storeNumber;
    }

    public Map<String, Integer> getProductAmountMap() {
        return productAmountMap;
    }

    public void setProductAmountMap(Map<String, Integer> productAmountMap) {
        this.productAmountMap = productAmountMap;
    }
}
