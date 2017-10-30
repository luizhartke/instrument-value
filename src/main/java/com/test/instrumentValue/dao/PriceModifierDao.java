package com.test.instrumentValue.dao;

import com.test.instrumentValue.jpa.InstrumentPriceModifier;

public interface PriceModifierDao {

    InstrumentPriceModifier findByInstrumentName(String instrumentName);

}
