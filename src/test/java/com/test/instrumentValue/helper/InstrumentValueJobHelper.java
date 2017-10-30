package com.test.instrumentValue.helper;

import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.test.instrumentValue.batch.InstrumentValueLauncher;
import com.test.instrumentValue.dto.InstrumentDto;
import com.test.instrumentValue.jpa.InstrumentPriceModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@EntityScan(basePackageClasses = { InstrumentPriceModifier.class })
public class InstrumentValueJobHelper {

    @Autowired
    private InstrumentValueLauncher launcher;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public void runInstrumentValue() {
        this.launcher.runMessageProcessor();
    }

    @Primary
    @Bean
    PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
