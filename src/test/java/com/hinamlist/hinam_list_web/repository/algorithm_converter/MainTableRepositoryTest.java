package com.hinamlist.hinam_list_web.repository.algorithm_converter;

import com.hinamlist.hinam_list_web.model.algorithm_converter.translator.MainTableProduct;
import com.hinamlist.hinam_list_web.model.algorithm_messenger.ControllerUserInput;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // By default, overrides data source with Embedded db
public class MainTableRepositoryTest {
    @Autowired
    private MainTableRepository repo;

    public static Stream<Arguments> getTestCases() {
        return Stream.of(
                Arguments.of(
                        2,
                        Arrays.asList("7290011877095", "7290011877088", "7290017941912"),
                        Set.of(27176, 14319, 4131265)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getTestCases")
    public void testFindProductListByStoreNumberAndBarcodeListQuery(int storeNumber, List<String> barcodeList, Set<Integer> expectedIdSet) {
        var result = repo.findProductListByStoreNumberAndBarcodeList(storeNumber, barcodeList);
        var resultProductIdSet = result.stream().map(MainTableProduct::getStoreId).collect(Collectors.toSet());
        var missingKeySet = new HashSet<>(expectedIdSet);
        missingKeySet.removeAll(resultProductIdSet);
        var extraKeySet = new HashSet<>(resultProductIdSet);
        extraKeySet.removeAll(expectedIdSet);
        assertTrue(missingKeySet.isEmpty() && extraKeySet.isEmpty(),
                "Key mismatch:\nMissing keys from expected result: " + missingKeySet + "\n"
                        + "Extra keys from expected result: " + extraKeySet + "\n"
        );
    }
}
