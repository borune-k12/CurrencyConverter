package com.example.currencyconverter.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.currencyconverter.PageLoaderService;
import com.example.currencyconverter.R;
import com.example.currencyconverter.view.ValuteView;
import com.example.currencyconverter.model.CurrencyModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by user on 14.05.17.
 */

// presenter for data
public class CurrencyPresenter {

    public static final String TAG = "CurrencyPresenter";

    private CurrencyModel mModel;
    private ValuteView mValuteView;
    private Context mContext;
    private BroadcastReceiver mLocalReceiver;

    public CurrencyPresenter(Context context) {
        mContext = context;
        mModel = new CurrencyModel(context);
    }

    public void setIView(ValuteView iview) {
        mValuteView = iview;
    }

    public void setModel(CurrencyModel model) {
        mModel = model;
    }

    // start service for receiving data
    public void requestData() {
        Intent serviceIntent = new Intent(mContext,PageLoaderService.class);
        mContext.startService(serviceIntent);
    }

    // convert from srcValute to dstValute
    public void convert(String sum, String srcValute, String dstValute) {
        if(sum.equals("")) {
            if(mValuteView != null)
                mValuteView.onError(mContext.getString(R.string.enter_sum_message));
            return;
        }

        float cnt = Float.valueOf(sum);
        double coef = mModel.getConversionCoefficient(dstValute,srcValute);
        if(coef == 0) {
            if(mValuteView != null)
                mValuteView.onError(mContext.getString(R.string.conversion_error_text));
            return;
        }
        BigDecimal round = new BigDecimal(new Float(cnt*coef).floatValue()).setScale(2,BigDecimal.ROUND_HALF_UP);

        if(mValuteView != null)
            mValuteView.onConvert(round.toPlainString(),new BigDecimal(sum).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString() + " " + mModel.getValuteName(srcValute) +
                " = " + round.toPlainString() + " " + mModel.getValuteName(dstValute));
    }

    // return list of char codes
    public void publish() {
        List<String> codes = mModel.getCurrencyCodes();
        if(codes.size() > 0) {
            if (mValuteView != null)
                mValuteView.onPublishList(codes);
        }
        else {
            if(mValuteView != null)
                mValuteView.onError(mContext.getString(R.string.empty_list_error_text));
        }
    }

    public void registerReceiver() {
        // create receiver for handling intents from PageLoaderService service
        mLocalReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if(action.equalsIgnoreCase(PageLoaderService.ACTION_ERROR)) {
                    Log.e(TAG, intent.getStringExtra(PageLoaderService.ERROR_STRING));
                } else if(action.equalsIgnoreCase(PageLoaderService.ACTION_DATA_READY)) {
                    Log.i(TAG,"data received");
                }
                // publish list after service is finished
                publish();
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PageLoaderService.ACTION_DATA_READY);
        intentFilter.addAction(PageLoaderService.ACTION_ERROR);

        LocalBroadcastManager.getInstance(mContext).registerReceiver(mLocalReceiver, intentFilter);
    }

    public void unregisterReceiver() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mLocalReceiver);
    }
}
