package com.test.instrumentValue.batch;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

import com.test.instrumentValue.dto.InstrumentDto;
import com.test.instrumentValue.jpa.InstrumentPriceStatistics;
import javafx.util.Pair;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InstrumentValueBatch extends DefaultBatchConfigurer {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	Job instrumentValueJob(Step pushMessageProcessorStep) {
		return this.jobBuilderFactory.get("instrumentValueJob")
			.preventRestart()
			.start(pushMessageProcessorStep)
			.build();
	}

	@Bean
	Step instrumentValueStep(FlatFileItemReader<InstrumentDto> instrumentValueReader,
		ItemProcessor<InstrumentDto, InstrumentPriceStatistics> instrumentValueProcessor,
		ItemWriter<InstrumentPriceStatistics> instrumentValueWriter) {
		return this.stepBuilderFactory.get("instrumentValueStep")
			.<InstrumentDto, InstrumentPriceStatistics>chunk(1)
			.reader(instrumentValueReader)
			.processor(instrumentValueProcessor)
			.writer(instrumentValueWriter).build();
	}

	private DataSource dataSource;
	@Override
	public void initialize() {
		super.initialize();
		super.setDataSource(this.dataSource);
	}

	@Autowired
	@Override
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
