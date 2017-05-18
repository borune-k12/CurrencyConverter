package com.example.currencyconverter;

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

    ValCurs mCurrencies;

    public ValCurs getValCurs() { return mCurrencies; }

    @Root (name = "Valute")
    static public class Valute {
        @Attribute (name = "ID", required = false)
        private String ID;
        public String getID() { return ID;}

        @Element (name="NumCode", required = false)
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

    @Root (name = "ValCurs")
    static public class ValCurs {
        @Attribute (name = "Date")
        private String date;

        @Attribute (name = "name")
        private String name;

        @ElementList(name = "Valute", inline = true)
        private List<Valute> valutes;

        public List<Valute> getValutes(){return valutes;}
    }

    public boolean parse(String xml){
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
