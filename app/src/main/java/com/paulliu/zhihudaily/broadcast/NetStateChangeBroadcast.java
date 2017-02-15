package com.paulliu.zhihudaily.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.paulliu.zhihudaily.listener.NetStateListener;
import com.paulliu.zhihudaily.util.NetUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created on 16/4/26
 *
 * @author LLW
 *
 * The BroadcastReceiver listening to the network changed
 */
public class NetStateChangeBroadcast extends BroadcastReceiver {
    private final static String ANDROID_NET_CONN_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private static NetStateChangeBroadcast mNetStateChangeReceiver;
    private boolean isNetConnected;
    private static List<NetStateListener> mNetStateListeners;

    public static NetStateChangeBroadcast getInstance() {
        if (mNetStateChangeReceiver == null) {
            synchronized (NetStateChangeBroadcast.class) {
                if (mNetStateChangeReceiver == null)
                    mNetStateChangeReceiver = new NetStateChangeBroadcast();
            }
        }
        return mNetStateChangeReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (ANDROID_NET_CONN_CHANGE.equals(intent.getAction())) {
                isNetConnected = NetUtils.isNetWorkConnected(context);
                notifyNetStateChange();
            }
        }
    }

    /**
     * called when the net state is changed
     */
    private void notifyNetStateChange() {
        if (mNetStateListeners != null) {
            for (NetStateListener netStateListener : mNetStateListeners) {
                if (isNetConnected)
                    netStateListener.onNetConnected();
                else
                    netStateListener.onNetDisconnected();
            }
        }
    }

    /**
     * register a BroadcastReceiver to be run in this context
     *
     * @param context where the BroadcastReceiver run in
     */
    public void registerNetStateChangeReceiver(Context context) {
        if (context != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ANDROID_NET_CONN_CHANGE);
            context.registerReceiver(mNetStateChangeReceiver, intentFilter);
        }
    }

    /**
     * Unregister a previously registered BroadcastReceiver from this context
     *
     * @param context where the BroadcastReceiver unregister from
     */
    public void unRegisterNetStateChangeReceiver(Context context) {
        try {
            if (context != null) {
                context.unregisterReceiver(mNetStateChangeReceiver);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * set the listener to  notify the change of net state
     *
     */
    public static void setNetStateChangeListener(NetStateListener listener) {
        if (mNetStateListeners == null)
            mNetStateListeners = new ArrayList<NetStateListener>();
        if (listener != null)
            mNetStateListeners.add(listener);
    }

    /**
     * remove a previously added listener
     */
    public static void removeNetStateChangeListener(NetStateListener listener) {
        if (mNetStateListeners != null && listener != null) {
            if (mNetStateListeners.contains(listener))
                mNetStateListeners.remove(listener);
        }
    }
}
