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

    public void transacted(Consumer<EntityManager> callback) {
        new TransactionTemplate(this.transactionManager).execute(s -> {
            callback.accept(this.entityManager);
            return null;
        });
    }

    public void runInstrumentValue() {
        createInstrument1Modifier();
        createInstrument2Modifier();
        createInstrument5Modifier();
        this.launcher.runMessageProcessor();
    }

    private void createInstrument1Modifier(){
        InstrumentPriceModifier priceModifier = new InstrumentPriceModifier();
        priceModifier.setId(1L);
        priceModifier.setName(InstrumentDto.INSTRUMENT_1);
        priceModifier.setValue(1.02);
        transacted(em -> em.persist(priceModifier));
    }

    private void createInstrument2Modifier(){
        InstrumentPriceModifier priceModifier = new InstrumentPriceModifier();
        priceModifier.setId(2L);
        priceModifier.setName(InstrumentDto.INSTRUMENT_2);
        priceModifier.setValue(1.04);
        transacted(em -> em.persist(priceModifier));
    }

    private void createInstrument5Modifier(){
        InstrumentPriceModifier priceModifier = new InstrumentPriceModifier();
        priceModifier.setId(5L);
        priceModifier.setName("INSTRUMENT5");
        priceModifier.setValue(1.87);
        transacted(em -> em.persist(priceModifier));
    }

    @Primary
    @Bean
    PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
