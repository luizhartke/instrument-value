package com.test.instrumentValue.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "INSTRUMENT_PRICE_STATISTICS")
public class InstrumentPriceStatistics {

    @Id
    @Column(name = "NAME")
    private String name;

    @Column(name="VALUE")
    private Double value;

    @Column(name="NUMBER_OF_ITEMS")
    private Integer items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getItems() {
        return items;
    }

    public void setItems(Integer items) {
        this.items = items;
    }

    public InstrumentPriceStatistics() {
    }

    public InstrumentPriceStatistics(String name, Double value, Integer items) {
        this.name = name;
        this.value = value;
        this.items = items;
    }
}
