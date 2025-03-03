package com.hinamlist.hinam_list_web.model.algorithm_converter;

import java.io.Serializable;
import java.util.Objects;

public class StoreNumberBarcodeKey implements Serializable {
    private int storeNumber;
    private String barcode;

    public StoreNumberBarcodeKey() {
    }

    public StoreNumberBarcodeKey(int storeNumber, String barcode) {
        this.storeNumber=storeNumber;
        this.barcode = barcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreNumberBarcodeKey that = (StoreNumberBarcodeKey) o;
        return storeNumber == that.storeNumber &&
                Objects.equals(barcode, that.barcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeNumber, barcode);
    }
}
