package com.example.currencyconverter.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by borune on 08.10.17.
 */

@Root(name = "ValCurs")
public class ValCurs {

    @Attribute(name = "Date")
    private String mDate;

    @Attribute (name = "name")
    private String mName;

    @ElementList(name = "Valute", inline = true)
    private List<Valute> mValutes;

    public List<Valute> getValutes(){return mValutes;}

}
