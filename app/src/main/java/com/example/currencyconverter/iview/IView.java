package com.example.currencyconverter.iview;

import java.util.List;

/**
 * Created by user on 14.05.17.
 */

public interface IView {

    // publish list of char codes callback
    void onPublishList(List<String> charCodes);

    // convertion result
    void onConvert(String value, String text);

    // error
    void onError(String error);
}
