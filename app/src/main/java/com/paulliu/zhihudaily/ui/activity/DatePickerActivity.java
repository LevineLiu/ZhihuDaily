package com.paulliu.zhihudaily.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.ui.BaseAppCompatActivity;

import java.util.Calendar;

import butterknife.BindView;

/**
 * Created on 2017/2/6
 *
 * @author LLW
 */

public class DatePickerActivity extends BaseAppCompatActivity{

    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDay;

    @BindView(R.id.calendar_view)
    CalendarView mCalendarView;

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
        Calendar calendar = Calendar.getInstance();
        mCalendarView.setMaxDate(calendar.getTime().getTime());
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                mSelectedYear = year;
                mSelectedMonth = month;
                mSelectedDay = dayOfMonth;
            }
        });
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.NONE, Menu.NONE, getString(R.string.confirm));
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case Menu.NONE:
                Calendar calendar = Calendar.getInstance();
                if(mSelectedYear != 0){
                    calendar.set(Calendar.YEAR, mSelectedYear);
                    calendar.set(Calendar.MONTH, mSelectedMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, mSelectedDay);
                }
                Bundle bundle = new Bundle();
                bundle.putLong(DailyNewsActivity.EXTRA_SELECTED_DATE, calendar.getTimeInMillis());
                navigateTo(DailyNewsActivity.class, bundle);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
