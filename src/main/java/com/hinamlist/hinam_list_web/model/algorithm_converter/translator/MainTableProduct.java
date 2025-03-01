package com.hinamlist.hinam_list_web.model.algorithm_converter.translator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(StoreNumberBarcodeKey.class)
@Table(name = "products")
public class MainTableProduct {
    @Id
    private int storeNumber;
    @Id
    private String barcode;
    private int storeId;

    public MainTableProduct() {}

    public MainTableProduct(int storeNumber, String barcode, int storeId) {
        this.storeNumber = storeNumber;
        this.barcode = barcode;
        this.storeId = storeId;
    }

    public int getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(int storeNumber) {
        this.storeNumber = storeNumber;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
