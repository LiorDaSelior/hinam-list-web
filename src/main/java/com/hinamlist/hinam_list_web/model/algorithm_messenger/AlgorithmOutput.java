package com.hinamlist.hinam_list_web.model.algorithm_messenger;

import java.io.Serializable;
import java.util.Map;

public class AlgorithmOutput implements Serializable {
    Map<String, Integer> barcodeStoreNumberMap;

    public AlgorithmOutput() {}

    public AlgorithmOutput(Map<String, Integer> barcodeStoreNumberMap) {
        this.barcodeStoreNumberMap = barcodeStoreNumberMap;
    }

    public Map<String, Integer> getBarcodeStoreNumberMap() {
        return barcodeStoreNumberMap;
    }

    public void setBarcodeStoreNumberMap(Map<String, Integer> barcodeStoreNumberMap) {
        this.barcodeStoreNumberMap = barcodeStoreNumberMap;
    }
}
