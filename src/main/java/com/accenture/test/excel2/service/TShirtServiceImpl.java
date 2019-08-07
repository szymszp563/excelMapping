package com.accenture.test.excel2.service;

import com.accenture.test.excel2.excel.ExcelPOIHelper;
import com.accenture.test.excel2.model.TShirt;
import com.accenture.test.excel2.repositories.TShirtRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import exception.custom.MistakeInJsonException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TShirtServiceImpl implements TShirtService {

    private final TShirtRepository tShirtRepository;
    private final ExcelPOIHelper excelPOIHelper;

    private enum Collumn {
        SEASON, DEPTCLASSSUBCLASS, COUNTRYGROUPID, SIZEDIFFGROUPID, SIZEDIFF, PERCENT
    }

    public TShirtServiceImpl(TShirtRepository tShirtRepository, ExcelPOIHelper excelPOIHelper) {
        this.tShirtRepository = tShirtRepository;
        this.excelPOIHelper = excelPOIHelper;
    }

    @Override
    public void printAllTShirtsFromFile(String fileLocation) {

        Map<Integer, List<String>> excelFile;
        try {
            excelFile = excelPOIHelper.readExcel(fileLocation);
            excelFile.entrySet().stream()
                    .filter(x -> x.getValue()
                            .stream()
                            .noneMatch(v -> v.length() <= 0))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                    .forEach((k, v) -> {
                        String format = "%-35s";
                        System.out.printf(format, k);
                        v.forEach(item -> System.out.printf(format, item));
                        System.out.println("");
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LinkedList<TShirt> mapAllTShirtsToObjectsAndSaveFromFile(String fileLocation) {

        LinkedList<TShirt> tShirts = new LinkedList<>();

        try {
            Map<Integer, List<String>> excelFile = excelPOIHelper.readExcel(fileLocation);
            Map<Integer, List<String>> excelFileWithoutEmptyRows = excelFile
                    .entrySet().stream()
                    .filter(x -> x.getValue()
                            .stream()
                            .noneMatch(v -> v.length() <= 0))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            excelFileWithoutEmptyRows.remove(0);//removing headers
            excelFileWithoutEmptyRows.forEach((k, v) -> {
                TShirt tShirt = TShirt.builder()
                        .season(v.get(Collumn.SEASON.ordinal()))
                        .deptClassSubclass(v.get(Collumn.DEPTCLASSSUBCLASS.ordinal()))
                        .countryGroupId(v.get(Collumn.COUNTRYGROUPID.ordinal()))
                        .sizeDiffGroupId(v.get(Collumn.SIZEDIFFGROUPID.ordinal()))
                        .sizeDiff(v.get(Collumn.SIZEDIFF.ordinal()))
                        .percent(v.get(Collumn.PERCENT.ordinal()))//(Double.valueOf(v.get(Collumn.PERCENT.ordinal())))
                        .build();
                System.out.println(tShirt);
                tShirts.add(tShirt);
                tShirtRepository.save(tShirt);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tShirts;
    }

    @Override
    public String createJsonToSend(String fileLocation) {

        String jsonToSend;
        LinkedList<TShirt> tShirts = new LinkedList<>();
        tShirtRepository.findAll().iterator().forEachRemaining(tShirts::add);
        jsonToSend = new Gson().toJson(tShirts);

        return jsonToSend;
    }

    @Override
    public LinkedList<TShirt> getTShirts() {
        LinkedList<TShirt> tShirts = new LinkedList<>();
        tShirtRepository.findAll().iterator().forEachRemaining(tShirts::add);
        return tShirts;
    }

    @Override
    public void createExcelFromJson(String json) {
        Type listType = new TypeToken<LinkedList<TShirt>>(){}.getType();

        try{
            JsonReader reader = new JsonReader(new StringReader(json));
            reader.setLenient(true);
            LinkedList<TShirt> tShirts = new Gson().fromJson(reader, listType);
            try {
                excelPOIHelper.writeExcelFromList(tShirts);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (NumberFormatException e){
            throw new MistakeInJsonException(json);
        }

    }

}
