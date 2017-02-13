package com.paulliu.zhihudaily.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created on 16/1/5
 *
 * @author LLW
 */
public class NetUtils {

    /**
     * whether a network is available
     *
     * @param context
     * @return boolean
     */
    public static boolean isNetWorkAvailable(Context context) {
        if (context == null)
            return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    /**
     * whether a network is connected
     *
     * @param context
     * @return boolean
     */
    public static boolean isNetWorkConnected(Context context){
        if(context == null)
            return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo[] = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info : networkInfo){
            if(info.getState() == NetworkInfo.State.CONNECTED)
                return true;
        }
        return false;
    }

    /**
     * whether wifi is connected
     *
     * @param context
     * @return boolean
     */
    public static boolean isWifiConnected(Context context){
        if(context == null)
            return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo != null && networkInfo.isAvailable();
    }

    /**
     * whether mobile is connected
     *
     * @param context
     * @return boolean
     */
    public static boolean isMobileConnected(Context context){
        if(context == null)
            return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkInfo != null && networkInfo.isAvailable();
    }
}
