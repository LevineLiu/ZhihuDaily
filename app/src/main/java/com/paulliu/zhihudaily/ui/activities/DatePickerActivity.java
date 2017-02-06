package com.paulliu.zhihudaily.ui.activities;

import android.os.Bundle;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.ui.BaseAppCompatActivity;

/**
 * Created on 2017/2/6
 *
 * @author LLW
 */

public class DatePickerActivity extends BaseAppCompatActivity{
    @Override
    protected void getBundleExtra(Bundle extra) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_date_picker;
    }

    @Override
    protected void initView() {
        setDisplayHomeAsUp(null);
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }
}
