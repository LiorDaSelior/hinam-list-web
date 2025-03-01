package com.hinamlist.hinam_list_web.service.algorithm_converter.translator;

import com.hinamlist.hinam_list_web.model.algorithm_messenger.ControllerUserInput;

import java.util.Map;

public interface ITranslator {
    // Using barcode list get the associated id in store, if exist
    public Map<String, Integer> translateUserInputByStore(int storeNumber, ControllerUserInput controllerUserInput);
}
