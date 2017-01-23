package com.paulliu.zhihudaily.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Created by liulewen on 2015/8/18.
 */
public abstract class GlobalProgressDialog {
    private static final String PROGRESS_DIALOG_TAG = GlobalProgressDialog.class.getSimpleName();

    private ProgressDialog mProgressDialog;
    private DialogInterface.OnCancelListener mOnCancelListener;
    private DialogInterface.OnDismissListener mOnDismissListener;
    public static final int DIALOG_ID_NONE = -1;
    public static final int DIALOG_INITIAL_ID = 0;
    private int mCurrentDialogId = DIALOG_INITIAL_ID;
    private final AtomicInteger mProgressDialogCount = new AtomicInteger(DIALOG_INITIAL_ID);


    public GlobalProgressDialog(){

    }


    /**
     * create a progress dialog
     */
    public static ProgressDialog createProgressDialog(Context context, CharSequence title, CharSequence message,
                                                      boolean cancelable, boolean indeterminate, int style){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(cancelable);
        progressDialog.setIndeterminate(indeterminate);
        progressDialog.setProgressStyle(style);
        return progressDialog;
    }

    /**
     * get loading message according to the locale of user
     * @return message
     */
    private String getDefaultDialogMessage(){
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        String city = locale.getCountry();
        String message;
        if("zh".equals(language)){
            if("TW".equals(city) || "HK".equals(city))
                message = "加載中...";
            else
                message = "加载中...";
        }else
            message = "Loading";
        return message;
    }


    /**
     * show progress which is not cancelable
     */

    public final int showProgressDialog(){
        return showProgressDialog(false, null);
    }

    /**
     *
     */
    public final int showProgressDialog(boolean cancelable){
        return showProgressDialog(cancelable, null);
    }

    /**
     * show progress dialog
     * @param cancelable
     */
    public final int showProgressDialog(boolean cancelable, CharSequence message){
        Context context = getHostActivity();
        if(context != null)
            return showProgressDialog(context, cancelable, message);
        return DIALOG_ID_NONE;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener){
        mOnCancelListener = onCancelListener;
    }

    /**
     * show progress dialog
     * @param context
     * @param cancelable
     */
    public final int showProgressDialog(Context context, boolean cancelable, CharSequence message){
        if(mOnCancelListener == null){
            mOnCancelListener = new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    onProgressDialogCancel(dialog);
                    mCurrentDialogId = DIALOG_ID_NONE;
                }
            };
        }
        if(mOnDismissListener == null){
            mOnDismissListener = new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    onProgressDialogDismiss(dialog);
                }
            };
        }

        // for outer caller.
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return mCurrentDialogId;
        }
        Activity activity = getHostActivity();

        /*
         * If you don't check whether this activity is in the process of finishing,
         * you may get the exception of android.view.WindowManager$BadTokenException:
         * "Unable to add window -- token android.os.BinderProxy@44f20780 is not valid; is your activity running?"
         *
         * This can occur when you are showing the dialog for a context that no longer exists.
         * A common case - if the 'show dialog' operation is after an asynchronous operation,
         * for example, using Handler.postDelayed(), using Thread and so on,
         * and during that operation the original activity (that is to be the parent of your dialog) is destroyed.
         * In other words what happens is that the Activity will be going through its destruction
         * when an asynchronous operation finishes its work and tries to show a Dialog.
         * It should have recognized the fact that the Activity is in the process of finishing
         * and not even attempted to show the Dialog.
         */
        if(activity != null && !activity.isFinishing()){
            if(TextUtils.isEmpty(message))
                message = getDefaultDialogMessage();
            if(mProgressDialog == null){
                mProgressDialog = createProgressDialog(context, null, message, true, true, ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setOnCancelListener(mOnCancelListener);
                mProgressDialog.setOnDismissListener(mOnDismissListener);
            }
            mProgressDialog.setMessage(message);
            //mProgressDialog.setCancelable(cancelable);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
            mCurrentDialogId = mProgressDialogCount.getAndIncrement();
        }else{
            mCurrentDialogId = DIALOG_ID_NONE;
        }
        return  mCurrentDialogId;
    }

    public final void dismissProgressDialog() {
        Activity activity = getHostActivity();
        if (mProgressDialog == null || !mProgressDialog.isShowing() || activity == null) {
            return;
        }
        try {
            /*
             * If isFinishing(), MAY cause java.lang.IllegalArgumentException: View not attached to window manager.
             * But we still dismiss the dialog even its activity isFinishing(), to avoid memory leak:
             * android.view.WindowLeaked: Activity has leaked window
             * com.android.internal.policy.impl.PhoneWindow$DecorView that was originally added here.
             */
                // Do NOT use cancel(), otherwise you will trigger onCancelListener when dismiss, not cancel.

            mProgressDialog.dismiss();
        } catch (Exception e) {
            Log.w(PROGRESS_DIALOG_TAG, " dismissProgressDialog() failed : " + e);
        }
    }

    /**
     * invoked while canceling progress, can be override
     */
    public void onProgressDialogCancel(DialogInterface dialogInterface){

    }

    public void onProgressDialogCancel(DialogInterface dialogInterface, String requestTag){

    }

    /**
     * invoked while dismissing progress, can be override
     */
    public void onProgressDialogDismiss(DialogInterface dialogInterface){

    }

    /**
     * Call this method on Activity.onDestroy() or Fragment.onDestroyView(), if not, may cause:
     * Activity has leaked window com.android.internal.policy.impl.PhoneWindow$DecorView that was originally added here.
     */
    public final void destroyProgressDialog() {
        try {
            if (mProgressDialog != null) {
                Log.d(PROGRESS_DIALOG_TAG, " destroyProgressDialog().");
                dismissProgressDialog();
                mProgressDialog = null;
            }
        } catch (Exception e) {
            Log.e(PROGRESS_DIALOG_TAG, " destroyProgressDialog() failed.", e);
        }
    }

    public int getCurrentDialogId(){
        return mCurrentDialogId;
    }

    public abstract Activity getHostActivity();

    public boolean isShowing(){
        if(mProgressDialog != null && mProgressDialog.isShowing())
            return true;
        else
            return false;
    }

}
