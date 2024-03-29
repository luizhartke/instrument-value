package com.test.instrumentValue.dao;

import java.util.List;

import com.test.instrumentValue.jpa.InstrumentPriceStatistics;

public interface InstrumentStatisticsDao {

    InstrumentPriceStatistics find(String instrument);

    List<InstrumentPriceStatistics> findAll();

    void updateStatistics(InstrumentPriceStatistics instrumentPriceStatistics);

    List<InstrumentPriceStatistics> findAditionalInstruments();
}
