package com.example.currencyconverter;

import com.example.currencyconverter.data.ValCurs;

import org.simpleframework.xml.core.Persister;

import java.io.Reader;
import java.io.StringReader;

/**
 * Created by user on 03.05.17.
 */

public class XmlParser {

    private ValCurs mCurrencies;

    public ValCurs getValCurs() { return mCurrencies; }

    public boolean parse(String xml) {
        Reader reader = new StringReader(xml);
        Persister serializer = new Persister();
        try
        {
            mCurrencies = serializer.read(ValCurs.class, reader, false);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
