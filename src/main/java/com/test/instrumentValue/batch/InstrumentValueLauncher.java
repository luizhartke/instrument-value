package com.test.instrumentValue.batch;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class InstrumentValueLauncher extends SimpleJobLauncher {

	@Autowired
	@Qualifier("instrumentValueJob")
	private Job instrumentValueJob;

	private JobExecution lastJobExecution;

	public JobExecution runMessageProcessor() {
		try {
			return this.lastJobExecution = super.run(this.instrumentValueJob, createJobParameters());
		} catch (RuntimeException | JobExecutionException e) {
			Throwable effectiveException = Optional.ofNullable(ExceptionUtils.getRootCause(e)).orElse(e);
			effectiveException.printStackTrace();
			return null;
		}
	}

	private JobParameters createJobParameters() {
		JobParametersBuilder builder = new JobParametersBuilder();
		builder.addLong("time", System.currentTimeMillis());
		return builder.toJobParameters();
	}

	@Autowired
	@Override
	public void setJobRepository(JobRepository jobRepository) {
		super.setJobRepository(jobRepository);
	}
}
