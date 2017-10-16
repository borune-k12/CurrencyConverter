package com.example.currencyconverter;

import com.example.currencyconverter.data.Valute;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by user on 16.05.17.
 */
public class XmlParserTest {

    private final String mContent = "<ValCurs Date=\"17.05.2017\" name=\"Foreign Currency Market\">\n" +
            "<Valute ID=\"R01010\">\n" +
            "<NumCode>036</NumCode>\n" +
            "<CharCode>AUD</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Австралийский доллар</Name>\n" +
            "<Value>41,7451</Value>\n" +
            "</Valute>\n" +
            "<Valute ID=\"R01020A\">\n" +
            "<NumCode>944</NumCode>\n" +
            "<CharCode>AZN</CharCode>\n" +
            "<Nominal>1</Nominal>\n" +
            "<Name>Азербайджанский манат</Name>\n" +
            "<Value>33,0457</Value>\n" +
            "</Valute>\n" +
            "</ValCurs>";

    @Test
    public void parseTest() {
        XmlParser parser = new XmlParser();

        parser.parse(mContent);

        assertEquals(parser.getValCurs().getValutes().size(),2);

        Valute valute = parser.getValCurs().getValutes().get(0);
        assertEquals(valute.getID(),"R01010");
        assertEquals(valute.getNumCode(),"036");
        assertEquals(valute.getCharCode(),"AUD");;
        assertEquals(valute.getNominal(),1);
        assertEquals(valute.getName(),"Австралийский доллар");
        assertEquals(valute.getValue(),new Double(41.7451));
    }
}
