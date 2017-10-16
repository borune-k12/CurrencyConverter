package com.example.currencyconverter.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by borune on 08.10.17.
 */

@Root(name = "Valute")
public class Valute {

    @Attribute(name = "ID", required = false)
    private String mID;
    public String getID() { return mID;}

    @Element(name="NumCode", required = false)
    private String mNumCode;
    public String getNumCode() { return mNumCode;}

    @Element (name="CharCode", required = false)
    private String mCharCode;
    public String getCharCode() { return mCharCode;}

    @Element (name="Nominal")
    private int mNominal;
    public int getNominal() { return mNominal;}

    @Element (name="Name")
    private String mName;
    public String getName() { return mName;}

    @Element (name="Value")
    private String mValue;
    public Double getValue() { return Double.valueOf(mValue.replace(",","."));}

}

