package com.example.currencyconverter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.currencyconverter.XmlParser;
import com.example.currencyconverter.data.Valute;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 05.05.17.
 */

public class DataBase {

    private final static int DB_VERSION = 1;
    private SQLiteDatabase mDB;
    protected static DataBase sInstance;

    public static DataBase getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new DataBase(context);
        }

        return sInstance;
    }

    protected DataBase(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context,"valutes.mDB",DB_VERSION);
        mDB = helper.getWritableDatabase();
    }

    // fill table
    public void writeData(XmlParser parser) {
        for(Valute valute : parser.getValCurs().getValutes())
        {
            ContentValues values = new ContentValues();

            int id = getValuteId(valute.getID());
            if(id > 0)
                values.put("_id",id);
            values.put("ID",valute.getID());
            values.put("NumCode",valute.getNumCode());
            values.put("CharCode",valute.getCharCode());
            values.put("Nominal",valute.getNominal());
            values.put("Value",valute.getValue());
            values.put("Name",valute.getName());
            mDB.replace(DatabaseHelper.TABLE_NAME,null,values);
        }
    }

    // find record _id in table
    private int getValuteId(String valuteID) {
        Cursor cursor = null;
        int valuteId = -1;
        try {
            cursor = mDB.rawQuery("select _id from ValCursTable where ID = ?", new String[]{valuteID});
            if (cursor.moveToFirst())
                valuteId = cursor.getInt(cursor.getColumnIndex("_id"));
        } finally {
            cursor.close();
        }
        return valuteId;
    }

    // get list of codes of all currencies
    public List<String> getCurrencies() {
        Cursor cursor = null;
        List<String> names = new ArrayList<>();
        try {
            cursor = mDB.rawQuery("select CharCode from ValCursTable;", null);

            if (cursor.moveToFirst()) {
                do {
                    names.add(cursor.getString(cursor.getColumnIndex("CharCode")));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return names;
    }

    // get translation coefficient
    public double getCoefficient(String srcCur, String dstCur) {
        Cursor srcCursor = null;
        Cursor dstCursor = null;
        double coefficient = 0;

        try {
            srcCursor = mDB.rawQuery("select Nominal, Value from ValCursTable where CharCode = ?", new String[]{srcCur});
            dstCursor = mDB.rawQuery("select Nominal, Value from ValCursTable where CharCode = ?", new String[]{dstCur});

            if (srcCursor.moveToFirst() && dstCursor.moveToFirst()) {
                coefficient = srcCursor.getInt(srcCursor.getColumnIndex("Nominal")) * dstCursor.getDouble(dstCursor.getColumnIndex("Value")) /
                        (dstCursor.getInt(dstCursor.getColumnIndex("Nominal")) * srcCursor.getDouble(srcCursor.getColumnIndex("Value")));
            }
        } finally {
            srcCursor.close();
            dstCursor.close();
        }

        return coefficient;
    }

    public String getValuteName(String charCode) {
        Cursor cursor = null;
        String valuteName = "";

        try {
            cursor = mDB.rawQuery("select Name from ValCursTable where CharCode = ?", new String[]{charCode});

            if (cursor.moveToFirst())
                valuteName = cursor.getString(cursor.getColumnIndex("Name"));
        } finally {
            cursor.close();
        }

        return valuteName;
    }
}
