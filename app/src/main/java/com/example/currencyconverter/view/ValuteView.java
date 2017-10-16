package com.example.currencyconverter.view;

import java.util.List;

/**
 * Created by user on 14.05.17.
 */

public interface ValuteView {

    // publish list of char codes callback
    void onPublishList(List<String> charCodes);

    // conversion result
    void onConvert(String value, String text);

    // error
    void onError(String error);
}
