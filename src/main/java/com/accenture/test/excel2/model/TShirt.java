package com.accenture.test.excel2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class TShirt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String season;
    private String deptClassSubclass;
    private String countryGroupId;
    private String sizeDiffGroupId;
    private String sizeDiff;
    private String percent;
}
