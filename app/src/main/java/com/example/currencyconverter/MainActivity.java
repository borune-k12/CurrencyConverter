package com.example.currencyconverter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.example.currencyconverter.iview.IView;
import com.example.currencyconverter.presenter.CurrencyPresenter;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IView {

    private static final String TAG = "MainActivity";

    private AppCompatEditText sum;
    private AppCompatSpinner srcCurrency;
    private AppCompatSpinner dstCurrency;
    private AppCompatTextView result;
    private AppCompatTextView resultText;
    private CurrencyPresenter mPresenter;
    private View trBtn;

    private boolean isConnected = false;

    private BroadcastReceiver mNetworkStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sum = (AppCompatEditText) findViewById(R.id.sum);
        srcCurrency = (AppCompatSpinner) findViewById(R.id.srcCurrency);
        dstCurrency = (AppCompatSpinner) findViewById(R.id.dstCurrency);
        result = ((AppCompatTextView)findViewById(R.id.result));
        resultText = ((AppCompatTextView)findViewById(R.id.resultText));
        trBtn = findViewById(R.id.trBtn);
        trBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.convert(sum.getText().toString(),srcCurrency.getSelectedItem().toString(),dstCurrency.getSelectedItem().toString());
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

        mNetworkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(action.equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    if(!isConnected)
                        mPresenter.requestData();

                    final NetworkInfo netInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

                    if(netInfo != null && netInfo.isConnected()) {
                        isConnected = true;
                    } else isConnected = false;
                }
            }
        };

        registerReceiver(mNetworkStateReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        mPresenter.registerReceiver();
    }

    @Override
    public void onPublishList(List<String> charCodes) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.spinner_item, charCodes);
        srcCurrency.setAdapter(adapter);
        dstCurrency.setAdapter(adapter);
        trBtn.setEnabled(true);
        sum.setEnabled(true);
        srcCurrency.setEnabled(true);
        dstCurrency.setEnabled(true);
    }

    @Override
    public void onConvert(String value, String text) {
        result.setText(value);
        resultText.setText(text);
    }

    @Override
    public void onError(String error) {
        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
        result.setText("");
        resultText.setText("");
    }
}


