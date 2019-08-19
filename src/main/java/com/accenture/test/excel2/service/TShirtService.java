package com.accenture.test.excel2.service;

import com.accenture.test.excel2.model.TShirt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;

public interface TShirtService {

    void printAllTShirtsFromFile(String fileLocation);

    LinkedList<TShirt> mapAllTShirtsToObjectsAndSaveFromFile(String fileLocation);

    String createJsonToSend(String fileLocation);

    LinkedList<TShirt> getTShirts();

    Page<TShirt> getTShirtsPaged(Pageable pageable);

    void createExcelFromJson(String json);

    Page<TShirt>findAllBySizeDiff(String size, Pageable pageable);

}
