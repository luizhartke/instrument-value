package com.test.instrumentValue.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import com.test.instrumentValue.batch.InstrumentValueLauncher;
import com.test.instrumentValue.dao.InstrumentStatisticsDao;
import com.test.instrumentValue.dao.PriceModifierDao;
import com.test.instrumentValue.dto.InstrumentDto;
import com.test.instrumentValue.jpa.InstrumentPriceStatistics;
import com.test.instrumentValue.service.InstrumentCalculatorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstrumentCalculatorServiceImpl implements InstrumentCalculatorService {

    @Autowired
    private PriceModifierDao priceModifierDao;

    @Autowired
    private InstrumentStatisticsDao instrumentStatisticsDao;

    @Autowired
    private InstrumentValueLauncher launcher;

    /**
     * Desirable caches so we can compare most of the processed values
     */
    private Map<String, Double> modifierCache = new HashMap<>();
    private Map<String, List<InstrumentDto>> newestTenValuesForInstrument = new HashMap<>();
    private InstrumentPriceStatistics highestInstrument3PriceStatistics = new InstrumentPriceStatistics(InstrumentDto.INSTRUMENT_3, 0D, 0);
    private InstrumentPriceStatistics lowestInstrument3PriceStatistics = new InstrumentPriceStatistics(InstrumentDto.INSTRUMENT_3, 0D, 0);

    private Double find(String key, Map<String, Double> cache, Supplier<Double> finder) {
        if(cache.containsKey(key)) {
            return cache.get(key);
        }
        Double value = finder.get();
        cache.put(key, value);
        return value;
    }

    @Override
    public void runStatistics() {
        launcher.runMessageProcessor();
    }

    @Override
    public List<InstrumentPriceStatistics> getInstrumentStatistics() {
        return instrumentStatisticsDao.findAll();
    }

    @Override
    public InstrumentPriceStatistics getInstrument3StatisticsOnTheFly(){
        return instrumentStatisticsDao.find(InstrumentDto.INSTRUMENT_3);
    }

    @Override
    public InstrumentPriceStatistics statisticsCalculation(InstrumentDto instrumentDto){
        Double modifier = find(instrumentDto.getName(), this.modifierCache, () -> priceModifierDao.findByInstrumentName(instrumentDto.getName()) == null ? null : priceModifierDao.findByInstrumentName(instrumentDto.getName()).getValue());
        InstrumentPriceStatistics statistics;
        if (!Objects.isNull(modifier)) {
            instrumentDto.setValue(instrumentDto.getValue() * modifier);
        }
        statistics = instrumentStatisticsDao.find(instrumentDto.getName());
        if(Objects.isNull(statistics)){
            statistics = new InstrumentPriceStatistics();
            statistics.setName(instrumentDto.getName());
        }
        if (StringUtils.equals(instrumentDto.getName(), InstrumentDto.INSTRUMENT_1)) {
            instrument1StatisticCalc(instrumentDto, statistics);
        } else if (StringUtils.equals(instrumentDto.getName(), InstrumentDto.INSTRUMENT_2)) {
            instrument2StatisticCalc(instrumentDto, statistics);
        } else if (StringUtils.equals(instrumentDto.getName(), InstrumentDto.INSTRUMENT_3)) {
            instrument3StatisticCalc(instrumentDto, statistics);
        } else {
            instrumentXStatisticsnstrumentCalc(instrumentDto, statistics);
        }
        return statistics;
    }

    private void instrument1StatisticCalc(InstrumentDto instrumentDto, InstrumentPriceStatistics statistics) {
        statistics.setItems(Optional.ofNullable(statistics.getItems()).orElse(0) + 1);
        statistics.setValue(Optional.ofNullable(statistics.getValue()).orElse(0D) + instrumentDto.getValue());
    }

    private void instrument2StatisticCalc(InstrumentDto instrumentDto, InstrumentPriceStatistics statistics) {
        if (instrumentDto.getDate().getMonth() == Calendar.NOVEMBER) {
            instrument1StatisticCalc(instrumentDto, statistics);
        }
    }

    /**
     * For this statistic calculation I choose to calculate the difference between the highest and the lowest price for the INSTRUMENT_3
     * @param instrumentDto
     * @param statistics
     */
    private void instrument3StatisticCalc(InstrumentDto instrumentDto, InstrumentPriceStatistics statistics) {
        if (highestInstrument3PriceStatistics.getValue() < instrumentDto.getValue()) {
            highestInstrument3PriceStatistics.setValue(instrumentDto.getValue());
        }
        if (!lowestInstrument3PriceStatistics.equals(0D) || lowestInstrument3PriceStatistics.getValue() > instrumentDto.getValue()) {
            lowestInstrument3PriceStatistics.setValue(instrumentDto.getValue());
        }
        statistics.setItems(Optional.ofNullable(statistics.getItems()).orElse(0) + 1);
        statistics.setValue(highestInstrument3PriceStatistics.getValue()-lowestInstrument3PriceStatistics.getValue());
    }

    private void instrumentXStatisticsnstrumentCalc(InstrumentDto instrumentDto, InstrumentPriceStatistics statistics) {
        List<InstrumentDto> values = newestTenValuesForInstrument.get(statistics.getName());
        if (Objects.isNull(values)) {
            newestTenValuesForInstrument.put(statistics.getName(), new ArrayList<>());
        }
        List<InstrumentDto> instrumentDtos = newestTenValuesForInstrument.get(statistics.getName());
        if (instrumentDtos.size() == 10) {
            Collections.sort(values, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
            if (instrumentDtos.get(0).getDate().compareTo(instrumentDto.getDate()) < 1) {
                instrumentDtos.set(0, instrumentDto);
            }
        } else {
            instrumentDtos.add(instrumentDto);
        }
        Double statisticFinalValue = 0D;
        for (InstrumentDto instrument: instrumentDtos) {
            statisticFinalValue += instrument.getValue();
        }
        statistics.setValue(statisticFinalValue);
        statistics.setItems(Optional.ofNullable(statistics.getItems()).orElse(0) + 1);
    }

}
