package com.example.currencyconverter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by user on 10.10.17.
 */

public class NetworkStateReceiver extends BroadcastReceiver {

    public NetworkStateReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
                if(action.equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION)) {

                    final NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

                    OnNetworkStateChanged listener = null;
                    if(context instanceof OnNetworkStateChanged)
                        listener = (OnNetworkStateChanged)context;

                    if(listener != null) {
                        if (netInfo != null && netInfo.isConnectedOrConnecting())
                                listener.networkConnected();
                         else listener.networkDisconnected();
                    }
                }
    }

    public interface OnNetworkStateChanged {
        void networkConnected();
        void networkDisconnected();
    }
}
