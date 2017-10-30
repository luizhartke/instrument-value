package com.test.instrumentValue.dto;

import java.util.Date;

public class InstrumentDto {

    public static final String INSTRUMENT_1= "INSTRUMENT1";
    public static final String INSTRUMENT_2= "INSTRUMENT2";
    public static final String INSTRUMENT_3= "INSTRUMENT3";

    private String name;
    private Date date;
    private Double value;

    public InstrumentDto() {
    }

    public InstrumentDto(String name, Date date, Double value) {
        this.name = name;
        this.date = date;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
