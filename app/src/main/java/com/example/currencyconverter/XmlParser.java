package com.example.currencyconverter;

import com.example.currencyconverter.model.ValCursModel;
import com.example.currencyconverter.model.ValuteModel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

/**
 * Created by user on 03.05.17.
 */

public class XmlParser {

    ValCursModel mCurrencies;

    public ValCursModel getValCurs() { return mCurrencies; }

    public boolean parse(String xml){
        Reader reader = new StringReader(xml);
        Persister serializer = new Persister();
        try
        {
            mCurrencies = serializer.read(ValCursModel.class, reader, false);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
