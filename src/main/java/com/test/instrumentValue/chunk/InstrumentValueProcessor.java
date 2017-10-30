package com.test.instrumentValue.chunk;

import com.test.instrumentValue.dto.InstrumentDto;
import com.test.instrumentValue.jpa.InstrumentPriceStatistics;
import com.test.instrumentValue.service.InstrumentCalculatorService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@StepScope
public class InstrumentValueProcessor implements ItemProcessor<InstrumentDto, InstrumentPriceStatistics> {

	@Autowired
	private InstrumentCalculatorService instrumentCalculatorService;

	@Override
	public InstrumentPriceStatistics process(InstrumentDto instrumentDto) throws Exception {
		return this.instrumentCalculatorService.statisticsCalculation(instrumentDto);
	}
}
