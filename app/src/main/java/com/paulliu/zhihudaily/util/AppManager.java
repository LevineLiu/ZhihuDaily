package com.paulliu.zhihudaily.util;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created on 16/4/13
 *
 * @author LLW
 *
 * A manager to manage activities
 */
public class AppManager {

    private static volatile AppManager sAppManager;
    private List<Activity> mActivities = new LinkedList<Activity>();

    public static AppManager getInstance(){
        if(sAppManager == null){
            synchronized (AppManager.class){
                if(sAppManager == null)
                    sAppManager = new AppManager();
            }
        }
        return sAppManager;
    }

    public synchronized void addActivity(Activity activity){
        if(activity != null)
            mActivities.add(activity);
    }

    public synchronized void removeActivity(Activity activity){
        if(mActivities.contains(activity))
            mActivities.remove(activity);
    }

    public synchronized void removeAll(){
        for (int i=0; i<mActivities.size(); i++){
            Activity activity = mActivities.get(i);
            removeActivity(activity);
            activity.finish();
        }
    }
}
