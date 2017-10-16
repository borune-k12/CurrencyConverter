package com.example.currencyconverter.model;

import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by user on 10.10.17.
 */
public class CurrencyModelTest {

    private CurrencyModel mCurrencyModel;

    @Before
    public void setUp() {
        mCurrencyModel = new CurrencyModel(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void getCurrencyCodes() throws Exception {
        assertTrue(mCurrencyModel.getCurrencyCodes().size() >= 0);
    }

    @Test
    public void getConversionCoefficient() throws Exception {
        assertTrue(mCurrencyModel.getConversionCoefficient("AUD","AZN") > 0);
        assertEquals(mCurrencyModel.getConversionCoefficient("AUD","ZZZ"),0,0.000000001);
    }

    @Test
    public void getValuteName() throws Exception {
        assertEquals(mCurrencyModel.getValuteName("EUR"),"Евро");
        assertEquals(mCurrencyModel.getValuteName(""),"");
    }

}