package com.example.currencyconverter;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.example.currencyconverter.view.ValuteView;
import com.example.currencyconverter.presenter.CurrencyPresenter;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ValuteView,
        NetworkStateReceiver.OnNetworkStateChanged {

    private static final String TAG = "MainActivity";

    private AppCompatEditText mSumma;
    private AppCompatSpinner mCurrencyFrom;
    private AppCompatSpinner mCurrencyTo;
    private AppCompatTextView mResult;
    private AppCompatTextView mResultText;
    private View mBtnConvert;

    private CurrencyPresenter mPresenter;

    private NetworkStateReceiver mNetworkStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSumma = (AppCompatEditText) findViewById(R.id.et_summa);
        mCurrencyFrom = (AppCompatSpinner) findViewById(R.id.spin_currency_from);
        mCurrencyTo = (AppCompatSpinner) findViewById(R.id.spin_currency_to);
        mResult = ((AppCompatTextView)findViewById(R.id.tv_result));
        mResultText = ((AppCompatTextView)findViewById(R.id.tv_result_text));
        mBtnConvert = findViewById(R.id.btn_convert);
        mBtnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.convert(mSumma.getText().toString(), mCurrencyFrom.getSelectedItem().toString(), mCurrencyTo.getSelectedItem().toString());
            }
        });

        mPresenter = new CurrencyPresenter(this);
        mPresenter.setIView(this);
    }


    @Override
    public void onStop() {
        super.onStop();

        unregisterReceiver(mNetworkStateReceiver);
        mPresenter.unregisterReceiver();
    }

    @Override
    public void onStart() {
        super.onStart();

        mNetworkStateReceiver = new NetworkStateReceiver();

        registerReceiver(mNetworkStateReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        mPresenter.registerReceiver();
    }

    @Override
    public void onPublishList(List<String> charCodes) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.spinner_item, charCodes);
        mCurrencyFrom.setAdapter(adapter);
        mCurrencyTo.setAdapter(adapter);
        mBtnConvert.setEnabled(true);
        mSumma.setEnabled(true);
        mCurrencyFrom.setEnabled(true);
        mCurrencyTo.setEnabled(true);
    }

    @Override
    public void onConvert(String value, String text) {
        mResult.setText(value);
        mResultText.setText(text);
    }

    @Override
    public void onError(String error) {
        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
        mResult.setText("");
        mResultText.setText("");
    }

    @Override
    public void networkConnected() {
        mPresenter.requestData();
    }

    @Override
    public void networkDisconnected() {

    }
}


