package com.hinamlist.hinam_list_web.model.algorithm_messenger;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Component
public class ControllerUserInput implements Serializable {
    private int storeNumber;
    private Map<String, Float> productAmountMap; // <barcode, quantity>

    public ControllerUserInput() {}

    public ControllerUserInput(int storeNumber, Map<String, Float> productAmountMap) {
        this.storeNumber = storeNumber;
        this.productAmountMap = productAmountMap;
    }

    public int getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(int storeNumber) {
        this.storeNumber = storeNumber;
    }

    public Map<String, Float> getProductAmountMap() {
        return productAmountMap;
    }

    public void setProductAmountMap(Map<String, Float> productAmountMap) {
        this.productAmountMap = productAmountMap;
    }
}
