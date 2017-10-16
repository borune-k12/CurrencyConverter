package com.example.currencyconverter.model;

import android.content.Context;

import com.example.currencyconverter.db.DataBase;

import java.util.List;

/**
 * Created by user on 14.05.17.
 */

// model for representating currency data
public class CurrencyModel {

    private Context mContext;
    private DataBase mDB;

    public CurrencyModel(Context context) {
        mContext = context;
        mDB = DataBase.getInstance(mContext);
    }

    public List<String> getCurrencyCodes() {
        return mDB.getCurrencies();
    }

    public double getConversionCoefficient(String src, String dst) {
        return mDB.getCoefficient(src,dst);
    }

    public String getValuteName(String charCode) {
        return mDB.getValuteName(charCode);
    }

}
