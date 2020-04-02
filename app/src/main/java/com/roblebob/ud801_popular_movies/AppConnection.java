package com.roblebob.ud801_popular_movies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import androidx.lifecycle.LiveData;


public class AppConnection extends LiveData<Boolean> {

    private Context context;
    private ConnectivityManager.NetworkCallback networkCallback = null;
    private NetworkReceiver networkReceiver;
    private ConnectivityManager connectivityManager;

    public AppConnection(Context context) {
        this.context = context;
        this.connectivityManager = ((ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE));
        this.networkCallback = new NetworkCallBack(this);
    }

    @Override protected void onActive() {
        super.onActive();
        updateConnection();
        this.connectivityManager.registerDefaultNetworkCallback( this.networkCallback);
    }

    @Override protected void onInactive() {
        super.onInactive();
        this.connectivityManager.unregisterNetworkCallback( this.networkCallback);
    }


    class NetworkCallBack extends ConnectivityManager.NetworkCallback {
        private AppConnection appConnection;
        public NetworkCallBack(AppConnection appConnection) { this.appConnection = appConnection; }

        @Override public void onAvailable( Network network) { if (network != null)  appConnection.postValue(true); }
        @Override public void onLost(      Network network) {                       appConnection.postValue(false);}
    }


    private void updateConnection() {
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null  &&  activeNetwork.isConnectedOrConnecting()) { postValue(true); }
            else                                                                    { postValue(false); }
        }
    }


    class NetworkReceiver extends BroadcastReceiver {

        @Override public void onReceive(Context context, Intent intent) {
            if (intent.getAction() .equals( ConnectivityManager.CONNECTIVITY_ACTION))  updateConnection();
        }
    }
}
