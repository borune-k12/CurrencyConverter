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
    private SQLiteDatabase db;
    protected static DataBase instance;

    public static DataBase getInstance(Context context) {
        if(instance == null) {
            instance = new DataBase(context);
        }

        return instance;
    }

    protected DataBase(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context,"valutes.db",DB_VERSION);
        db = helper.getWritableDatabase();
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
            db.replace(DatabaseHelper.TABLE_NAME,null,values);
        }
    }

    // find record _id in table
    private int getValuteId(String valuteID) {
        Cursor cur = db.rawQuery("select _id from ValCursTable where ID = ?",new String[] {valuteID});
        if(cur.moveToFirst())
            return cur.getInt(cur.getColumnIndex("_id"));
        else return -1;
    }

    // get list of codes of all currencies
    public List<String> getCurrencies() {
        Cursor cur = db.rawQuery("select CharCode from ValCursTable;",null);
        List<String> names = new ArrayList<>();

        if(cur.moveToFirst()) {
            do {
                names.add(cur.getString(cur.getColumnIndex("CharCode")));
            } while (cur.moveToNext());

            cur.close();
        }

        return names;
    }

    // get translation coefficient
    public double getCoefficient(String srcCur, String dstCur) {
        Cursor srcCursor = db.rawQuery("select Nominal, Value from ValCursTable where CharCode = ?",new String[]{srcCur});
        Cursor dstCursor = db.rawQuery("select Nominal, Value from ValCursTable where CharCode = ?",new String[]{dstCur});

        if(srcCursor.moveToFirst() && dstCursor.moveToFirst()) {
            return srcCursor.getInt(srcCursor.getColumnIndex("Nominal"))*dstCursor.getDouble(dstCursor.getColumnIndex("Value")) /
                    (dstCursor.getInt(dstCursor.getColumnIndex("Nominal"))*srcCursor.getDouble(srcCursor.getColumnIndex("Value")));
        }

        return 0;
    }

    public String getValuteName(String charCode) {
        Cursor cursor = db.rawQuery("select Name from ValCursTable where CharCode = ?",new String[]{charCode});

        if(cursor.moveToFirst())
            return cursor.getString(cursor.getColumnIndex("Name"));

        else return "";
    }
}
