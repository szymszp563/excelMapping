package com.accenture.test.excel2.service;

import com.accenture.test.excel2.model.TShirt;

import java.util.LinkedList;

public interface TShirtService {

    void printAllTShirtsFromFile(String fileLocation);

    LinkedList<TShirt> mapAllTShirtsToObjectsAndSaveFromFile(String fileLocation);

    String createJsonToSend(String fileLocation);

    LinkedList<TShirt> getTShirts();

    void createExcelFromJson(String json);
}
