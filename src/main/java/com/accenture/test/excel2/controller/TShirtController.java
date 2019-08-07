package com.accenture.test.excel2.controller;

import com.accenture.test.excel2.model.TShirt;
import com.accenture.test.excel2.service.TShirtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.LinkedList;

@RestController
public class TShirtController {

    @Autowired
    TShirtService tShirtService;

    @GetMapping("/json")
    public ResponseEntity<LinkedList<TShirt>>getAllShirts(){

        return new ResponseEntity<>(tShirtService.getTShirts(), HttpStatus.OK);
    }

    @PostMapping("/json")
    public ResponseEntity<String> createExcelFile(@RequestBody String tShirts){
        tShirtService.createExcelFromJson(tShirts);
        return new ResponseEntity<>("Excel file has been successfully created", HttpStatus.OK);
    }
}
