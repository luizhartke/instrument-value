package com.test.instrumentValue.chunk;

import com.test.instrumentValue.dto.InstrumentDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@StepScope
public class InstrumentValueReader extends FlatFileItemReader<InstrumentDto> {

	@Value("${instrument.file.path}")
	private String filePath;

	private LineMapper<InstrumentDto> createInstrumentLineMapper() {
		DefaultLineMapper<InstrumentDto> instrumentLineMapper = new DefaultLineMapper<>();

		LineTokenizer instrumentLineTokenizer = createStudentLineTokenizer();
		instrumentLineMapper.setLineTokenizer(instrumentLineTokenizer);

		FieldSetMapper<InstrumentDto> instrumentMapper = createInstrumentMapper();
		instrumentLineMapper.setFieldSetMapper(instrumentMapper);

		return instrumentLineMapper;
	}

	private LineTokenizer createStudentLineTokenizer() {
		DelimitedLineTokenizer instrumentLineTokenizer = new DelimitedLineTokenizer();
		instrumentLineTokenizer.setDelimiter(",");
		instrumentLineTokenizer.setNames(new String[]{"name", "date", "value"});
		return instrumentLineTokenizer;
	}

	private FieldSetMapper<InstrumentDto> createInstrumentMapper() {
		BeanWrapperFieldSetMapper<InstrumentDto> instrumentMapper = new BeanWrapperFieldSetMapper<>();
		instrumentMapper.setTargetType(InstrumentDto.class);
		return instrumentMapper;
	}

	@PostConstruct
	void setInstrumentValueReader() {
		setLineMapper(createInstrumentLineMapper());
		setResource(new ClassPathResource(this.filePath));
	}

}
