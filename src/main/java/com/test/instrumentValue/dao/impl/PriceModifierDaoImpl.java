package com.test.instrumentValue.dao.impl;

import com.test.instrumentValue.dao.PriceModifierDao;
import com.test.instrumentValue.jpa.InstrumentPriceModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Repository
public class PriceModifierDaoImpl implements PriceModifierDao{

    @Autowired
    private EntityManager entityManager;

    @Override
    public InstrumentPriceModifier findByInstrumentName(String instrumentName) {
        TypedQuery<InstrumentPriceModifier> query = entityManager.createQuery("SELECT modifier from INSTRUMENT_PRICE_MODIFIER modifier WHERE modifier.name LIKE :instrumentName ORDER BY modifier.id DESC", InstrumentPriceModifier.class);
        try {
            return query.setParameter("instrumentName", instrumentName).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
