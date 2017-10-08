package com.example.currencyconverter.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by borune on 08.10.17.
 */

@Root(name = "ValCurs")
public class ValCursModel {

    @Attribute(name = "Date")
    private String date;

    @Attribute (name = "name")
    private String name;

    @ElementList(name = "Valute", inline = true)
    private List<ValuteModel> valutes;

    public List<ValuteModel> getValutes(){return valutes;}

}
