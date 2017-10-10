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
    private String ID;
    public String getID() { return ID;}

    @Element(name="NumCode", required = false)
    private String NumCode;
    public String getNumCode() { return NumCode;}

    @Element (name="CharCode", required = false)
    private String CharCode;
    public String getCharCode() { return CharCode;}

    @Element (name="Nominal")
    private int Nominal;
    public int getNominal() { return Nominal;}

    @Element (name="Name")
    private String Name;
    public String getName() { return Name;}

    @Element (name="Value")
    private String Value;
    public Double getValue() { return Double.valueOf(Value.replace(",","."));}

}

