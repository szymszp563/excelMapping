package com.accenture.test.excel2.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelPOIHelper {

    public Map<Integer, List<String>> readExcel(String fileLocation) throws IOException {

        Map<Integer, List<String>> data = new HashMap<>();
        FileInputStream file = new FileInputStream(new File(fileLocation));
        Workbook workbook = new HSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        int i = 0;
        for (Row row : sheet) {
            data.put(i, new ArrayList<String>());
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING:
                        data.get(i)
                                .add(cell.getRichStringCellValue().getString());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            data.get(i)
                                    .add(cell.getDateCellValue() + "");
                        } else {
                            data.get(i)
                                    .add(cell.getNumericCellValue() + "");
                        }
                        break;
                    case BOOLEAN:
                        data.get(i)
                                .add(cell.getBooleanCellValue() + "");
                        break;
                    case FORMULA:
                        data.get(i)
                                .add(cell.getCellFormula() + "");
                        break;
                    default:
                        data.get(i).add("");
                }
            }
            i++;
        }
        if (workbook != null) {
            workbook.close();
        }
        return data;
    }

    public void writeExcelFromList(List<?> objects) throws IOException {

        try (Workbook workbook = new HSSFWorkbook()) {

            Sheet sheet = workbook.createSheet(objects.get(0).getClass().getName());
            Row header = sheet.createRow(0);
            DataFormat dataFormat = workbook.createDataFormat();
            CellStyle headerStyle = getHeaderStyle(workbook);
            CellStyle style = getCellsStyle(workbook);
            final int numberOfFields = objects.get(0).getClass().getDeclaredFields().length;
            final int numberOfObjects = objects.size();

            for (int j = 0; j < objects.size(); j++) {
                sheet.createRow(j + 1);
            }

            for (int i = 0; i < numberOfFields; i++) {
                Cell headerCell = header.createCell(i);
                headerCell.setCellValue(objects.get(0).getClass().getDeclaredFields()[i].getName());
                headerCell.setCellStyle(headerStyle);

                for (int j = 0; j < numberOfObjects; j++){
                    try {
                        Field field = objects.get(j).getClass().getDeclaredFields()[i];
                        field.setAccessible(true);
                        Object object = field.get(objects.get(j));
                        Cell cell = sheet.getRow(j+1).createCell(i);
                        style.setDataFormat(dataFormat.getFormat("General"));
                        if(object == null){
                            cell.setCellValue("");
                        }
                        else if(NumberUtils.isCreatable(object.toString())
                                && !StringUtils.containsIgnoreCase(field.getName(), "id"))//field.getType().isAssignableFrom(Double.class)
                        {
                            style.setDataFormat(dataFormat.getFormat("0.00"));
                            cell.setCellValue(Double.valueOf(object.toString()));
                        }
                        else {
                            cell.setCellValue(object.toString());
                        }

                        cell.setCellStyle(style);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                sheet.autoSizeColumn(i);
            }

            File currDir = new File("XLSXs/.");
            String path = currDir.getAbsolutePath();
            String fileLocation = path.substring(0, path.length() - 1) + "temp.xls";

            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
        }
    }

    private CellStyle getHeaderStyle(Workbook workbook){

        CellStyle headerStyle = workbook.createCellStyle();

        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        setBorders(BorderStyle.THIN, headerStyle);

        setHSSFFont("Arial", (short)8,true, headerStyle, workbook);

        return headerStyle;
    }

    private CellStyle getCellsStyle(Workbook workbook){
        CellStyle style = workbook.createCellStyle();

        style.setWrapText(true);

        setBorders(BorderStyle.THIN, style);

        setHSSFFont("Arial", (short)8, false, style, workbook);

        return  style;
    }

    private void setBorders(BorderStyle borderStyle, CellStyle style){
        style.setBorderBottom(borderStyle);
        style.setBorderTop(borderStyle);
        style.setBorderLeft(borderStyle);
        style.setBorderRight(borderStyle);
    }

    private void setHSSFFont(String fontName, short fontSize, boolean bold, CellStyle style, Workbook workbook){
        HSSFFont font = ((HSSFWorkbook) workbook).createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints(fontSize);
        font.setBold(bold);
        style.setFont(font);
    }
}
