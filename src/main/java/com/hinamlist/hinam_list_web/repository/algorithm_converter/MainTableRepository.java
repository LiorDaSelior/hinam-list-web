package com.hinamlist.hinam_list_web.repository.algorithm_converter;

import com.hinamlist.hinam_list_web.model.algorithm_converter.MainTableProduct;
import com.hinamlist.hinam_list_web.model.algorithm_converter.StoreNumberBarcodeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MainTableRepository extends JpaRepository<MainTableProduct, StoreNumberBarcodeKey> {
    @Query("SELECT mtp FROM MainTableProduct mtp WHERE (mtp.storeNumber) = (:storeNumber) AND mtp.barcode IN (:barcodeList)")
    public List<MainTableProduct> findProductListByStoreNumberAndBarcodeList(
            @Param("storeNumber") int storeNumber,
            @Param("barcodeList") List<String> barcodeList);
}
