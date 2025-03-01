package com.hinamlist.hinam_list_web.repository.algorithm_converter.translator;

import com.hinamlist.hinam_list_web.model.algorithm_converter.translator.MainTableProduct;
import com.hinamlist.hinam_list_web.model.algorithm_converter.translator.StoreNumberBarcodeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface MainTableRepo extends JpaRepository<MainTableProduct, StoreNumberBarcodeKey> {
    @Query("SELECT mtp FROM MainTableProduct mtp WHERE (mtp.store_number) = (:storeNumber) AND mtp.barcode IN (:barcodeList)")
    public List<MainTableProduct> findProductListByStoreNumberAndBarcodeList(int storeNumber, Set<String> barcodeSet);
}
