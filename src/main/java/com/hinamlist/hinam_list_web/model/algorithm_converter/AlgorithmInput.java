package com.hinamlist.hinam_list_web.model.algorithm_converter;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class AlgorithmInput implements Serializable {

    private List<String> barcodeList;
    private Map<Integer, List<Float>> storeNumberPriceListMap;
    private Map<Integer, Float> storeNumberLowerLimitMap;
    private Map<Integer, Float> storeNumberOrderAddonMap;

    public AlgorithmInput() {}

    public AlgorithmInput(List<String> barcodeList,
                          Map<Integer, List<Float>> storeNumberPriceListMap,
                          Map<Integer, Float> storeNumberLowerLimitMap,
                          Map<Integer, Float> storeNumberOrderAddonMap) {
        this.barcodeList = barcodeList;
        this.storeNumberPriceListMap = storeNumberPriceListMap;
        this.storeNumberLowerLimitMap = storeNumberLowerLimitMap;
        this.storeNumberOrderAddonMap = storeNumberOrderAddonMap;
    }

    public List<String> getBarcodeList() {
        return barcodeList;
    }

    public void setBarcodeList(List<String> barcodeList) {
        this.barcodeList = barcodeList;
    }

    public Map<Integer, List<Float>> getStoreNumberPriceListMap() {
        return storeNumberPriceListMap;
    }

    public void setStoreNumberPriceListMap(Map<Integer, List<Float>> storeNumberPriceListMap) {
        this.storeNumberPriceListMap = storeNumberPriceListMap;
    }

    public Map<Integer, Float> getStoreNumberLowerLimitMap() {
        return storeNumberLowerLimitMap;
    }

    public void setStoreNumberLowerLimitMap(Map<Integer, Float> storeNumberLowerLimitMap) {
        this.storeNumberLowerLimitMap = storeNumberLowerLimitMap;
    }

    public Map<Integer, Float> getStoreNumberOrderAddonMap() {
        return storeNumberOrderAddonMap;
    }

    public void setStoreNumberOrderAddonMap(Map<Integer, Float> storeNumberOrderAddonMap) {
        this.storeNumberOrderAddonMap = storeNumberOrderAddonMap;
    }
}
