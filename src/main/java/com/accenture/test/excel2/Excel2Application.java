package com.accenture.test.excel2;

import com.accenture.test.excel2.excel.ExcelPOIHelper;
import com.accenture.test.excel2.model.TShirt;
import com.accenture.test.excel2.service.TShirtService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.util.LinkedList;

@SpringBootApplication
public class Excel2Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Excel2Application.class, args);

        ExcelPOIHelper excelPOIHelper = new ExcelPOIHelper();
//        try {
//            excelPOIHelper.writeExcelFromList();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        File currDir = new File("XLSXs/.");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "mens RES t-shirts 2011-08-23 v1.xls";


        TShirtService tShirtService = (TShirtService)ctx.getBean(TShirtService.class);

        LinkedList<TShirt> tShirts = tShirtService.mapAllTShirtsToObjectsAndSaveFromFile(fileLocation);

        //String jsonToSend = tShirtService.createJsonToSend(fileLocation);

        System.out.println("====================================");
        String jsonToSend = tShirtService.createJsonToSend(fileLocation);

        String jsonToSend3 = "[" +
                "{" +
                "\"id\":\"\"," +
                "\"percent\":0.2," +
                "\"season\":\"AW 2011\"," +
                "\"sizeDiffGroupId\":\"S-XXL-5-N\"," +
                "\"sizeDiff\":\"S\"," +
                "\"countryGroupId\":\"UE\"," +
                "\"deptClassSubclass\":\"men's RES;t-shirts;t-shirts_s_s\"}," +
                "{" +
                "\"id\":\"2\"," +
                "\"percent\":0.2666," +
                "\"season\":\"AW 2011\"," +
                "\"sizeDiffGroupId\":\"S-XXL-5-N\"," +
                "\"sizeDiff\":\"M\"," +
                "\"countryGroupId\":\"UE\"," +
                "\"deptClassSubclass\":\"men's RES;t-shirts;t-shirts_s_s\"}]";

        System.out.println(jsonToSend3);
        tShirtService.createExcelFromJson(jsonToSend3);


    }

}
