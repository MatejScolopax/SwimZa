package scolopax.sk.swimza.util;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Method;

import scolopax.sk.swimza.data.ParseHtmlTask;
import scolopax.sk.swimza.ui.SettingsDialog;


/**
 * Created by scolopax on 5/22/2016.
 */
public class br_ConnectivityChange extends BroadcastReceiver {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (getConnectivityStatus(context) != TYPE_NOT_CONNECTED) {
            // device is online
            Log.v("Service_KeepOnline", "Device connected to internet");
            if (SettingsDialog.isDownloadAutomatic(context)){
                new DownloadTask(context).execute();
            }


        } else {
            Log.v("Service_KeepOnline", "stop br_connectivityChange");
        }
    }

    public static boolean isConnected(Context context) {
        if (getConnectivityStatus(context) != TYPE_NOT_CONNECTED)
            return true;
        else
            return false;
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static boolean isWifiEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return (wifiManager.isWifiEnabled());
    }

    public static boolean isDataEnabled(Context context) {
        boolean mobileDataEnabled = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true);
            mobileDataEnabled = (Boolean) method.invoke(cm);
        } catch (Exception e) {
            Log.v("br_timeChange", "mobile data isAvailable err");
        }
        return mobileDataEnabled;
    }

    private class DownloadTask extends ParseHtmlTask {

        public DownloadTask(Context context) {
            super(context);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPreExecute();
        }
    }

}
