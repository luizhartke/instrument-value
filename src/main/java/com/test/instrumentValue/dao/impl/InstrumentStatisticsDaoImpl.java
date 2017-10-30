package com.test.instrumentValue.dao.impl;

import java.util.List;

import com.test.instrumentValue.dao.InstrumentStatisticsDao;
import com.test.instrumentValue.jpa.InstrumentPriceModifier;
import com.test.instrumentValue.jpa.InstrumentPriceStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Repository
public class InstrumentStatisticsDaoImpl implements InstrumentStatisticsDao{

    @Autowired
    private EntityManager entityManager;

    @Override
    public InstrumentPriceStatistics find(String instrument) {
        return entityManager.find(InstrumentPriceStatistics.class, instrument);
    }

    @Override
    public List<InstrumentPriceStatistics> findAll() {
        TypedQuery<InstrumentPriceStatistics> query = entityManager.createQuery("SELECT statistics from INSTRUMENT_PRICE_STATISTICS statistics", InstrumentPriceStatistics.class);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void updateStatistics(InstrumentPriceStatistics instrumentPriceStatistics) {
        entityManager.merge(instrumentPriceStatistics);
    }

    @Override
    public List<InstrumentPriceStatistics> findAditionalInstruments() {
        TypedQuery<InstrumentPriceStatistics> query = entityManager.createQuery("SELECT statistics from INSTRUMENT_PRICE_STATISTICS statistics WHERE statistics.name NOT IN ('INSTRUMENT1', 'INSTRUMENT2','INSTRUMENT3')", InstrumentPriceStatistics.class);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
