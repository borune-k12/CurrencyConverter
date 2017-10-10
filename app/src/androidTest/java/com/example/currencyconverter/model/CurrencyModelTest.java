package com.example.currencyconverter.model;

import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by user on 10.10.17.
 */
public class CurrencyModelTest {

    CurrencyModel currencyModel;

    @Before
    public void setUp() {
        currencyModel = new CurrencyModel(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void getCurrencyCodes() throws Exception {
        assertTrue(currencyModel.getCurrencyCodes().size() >= 0);
    }

    @Test
    public void getConversionCoefficient() throws Exception {
        assertTrue(currencyModel.getConversionCoefficient("AUD","AZN") > 0);
        assertEquals(currencyModel.getConversionCoefficient("AUD","ZZZ"),0,0.000000001);
    }

    @Test
    public void getValuteName() throws Exception {
        assertEquals(currencyModel.getValuteName("EUR"),"Евро");
        assertEquals(currencyModel.getValuteName(""),"");
    }

}