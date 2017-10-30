package com.test.instrumentValue.service;

import java.util.List;

import com.test.instrumentValue.dto.InstrumentDto;
import com.test.instrumentValue.jpa.InstrumentPriceStatistics;

public interface InstrumentCalculatorService {

    void runStatistics();

    InstrumentPriceStatistics statisticsCalculation(InstrumentDto instrumentDto);

    InstrumentPriceStatistics getInstrument3StatisticsOnTheFly();

    List<InstrumentPriceStatistics> getInstrumentStatistics();
}
