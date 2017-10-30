package com.test.instrumentValue.chunk;

import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;

import com.test.instrumentValue.dao.InstrumentStatisticsDao;
import com.test.instrumentValue.dto.InstrumentDto;
import com.test.instrumentValue.jpa.InstrumentPriceStatistics;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class InstrumentValueWriter implements ItemStreamWriter<InstrumentPriceStatistics> {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private InstrumentStatisticsDao instrumentStatisticsDao;

    @Override
    public void write(List<? extends InstrumentPriceStatistics> list) throws Exception {
        for (InstrumentPriceStatistics instrumentPriceStatistics : list) {
            entityManager.merge(instrumentPriceStatistics);
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
    }

    @Override
    public void close() throws ItemStreamException {
        InstrumentPriceStatistics instrument1PriceStatistics = entityManager.find(InstrumentPriceStatistics.class, InstrumentDto.INSTRUMENT_1);
        if (!Objects.isNull(instrument1PriceStatistics))
        System.out.println(String.format("The mean of %s is %4.2f", InstrumentDto.INSTRUMENT_1, instrument1PriceStatistics.getValue() / instrument1PriceStatistics.getItems()));
        InstrumentPriceStatistics instrument2PriceStatistics = entityManager.find(InstrumentPriceStatistics.class, InstrumentDto.INSTRUMENT_2);
        if (!Objects.isNull(instrument2PriceStatistics))
        System.out.println(String.format("The mean of %s in November/2014 is %4.2f", InstrumentDto.INSTRUMENT_2, instrument2PriceStatistics.getValue() / instrument2PriceStatistics.getItems()));
        InstrumentPriceStatistics instrument3PriceStatistics = entityManager.find(InstrumentPriceStatistics.class, InstrumentDto.INSTRUMENT_3);
        if (!Objects.isNull(instrument3PriceStatistics))
        System.out.println(String.format("The difference between the highest and the lowest value of %s is %4.2f", InstrumentDto.INSTRUMENT_3, instrument3PriceStatistics.getValue()));
        instrumentStatisticsDao.findAditionalInstruments().forEach(instrumentPriceStatistics -> {
            System.out.println(String.format("The sum of the ten newest values of %s is %4.2f", instrumentPriceStatistics.getName(), instrumentPriceStatistics.getValue()));
        });
    }
}
