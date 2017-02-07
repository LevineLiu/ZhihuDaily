package com.paulliu.zhihudaily.ui.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.DatePicker;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.ui.BaseAppCompatActivity;
import com.paulliu.zhihudaily.ui.FragmentSwitcher;
import com.paulliu.zhihudaily.ui.fragments.HomeFragment;
import com.paulliu.zhihudaily.ui.fragments.ThemeFragment;

import butterknife.BindView;

public class MainActivity extends BaseAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private final static int FRAGMENT_HOME = 0;
    private final static int FRAGMENT_RECOMMEND = 1;
    private final static int FRAGMENT_COMPANY = 2;
    private final static int FRAGMENT_SECURITY = 3;
    private final static int FRAGMENT_MOVIE = 4;
    private final static int FRAGMENT_SPORTS = 5;
    private final static int FRAGMENT_COMIC = 6;
    private final static int FRAGMENT_GAME = 7;

    private FragmentSwitcher mFragmentSwitcher;
    private int mCurrentFragmentPosition;


    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view) NavigationView mNavigationView;

    @Override
    protected void getBundleExtra(Bundle extra) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateTo(DatePickerActivity.class);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(R.id.nav_home);
        mFragmentSwitcher = new FragmentSwitcher(this, R.id.content_main);
        if(mFragmentSwitcher.getFragment(FRAGMENT_HOME) == null)
            mFragmentSwitcher.addFragment(FRAGMENT_HOME, new HomeFragment(), FragmentSwitcher.OPERATION_SHOW_HIDE);
        mFragmentSwitcher.switchFragment(FRAGMENT_HOME);
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = mFragmentSwitcher.getFragment(mCurrentFragmentPosition);
                if(fragment != null){
                    if(fragment instanceof HomeFragment)
                        ((HomeFragment) fragment).scrollToTop();
                    else
                        ((ThemeFragment) fragment).scrollToTop();
                }
            }
        });
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.nav_home:
                mCurrentFragmentPosition = FRAGMENT_HOME;
                if(mFragmentSwitcher.getFragment(FRAGMENT_HOME) == null)
                    mFragmentSwitcher.addFragment(FRAGMENT_HOME, new HomeFragment(), FragmentSwitcher.OPERATION_SHOW_HIDE);
                mFragmentSwitcher.switchFragment(FRAGMENT_HOME);
                mFab.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_recommend:
                mCurrentFragmentPosition = FRAGMENT_RECOMMEND;
                if(mFragmentSwitcher.getFragment(FRAGMENT_RECOMMEND) == null){
                    ThemeFragment fragment = new ThemeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(ThemeFragment.EXTRA_THEME_ID, 12);
                    fragment.setArguments(bundle);
                    mFragmentSwitcher.addFragment(FRAGMENT_RECOMMEND, fragment, FragmentSwitcher.OPERATION_SHOW_HIDE);
                }
                mFragmentSwitcher.switchFragment(FRAGMENT_RECOMMEND);
                mFab.setVisibility(View.GONE);
                break;
            case R.id.nav_company:
                mCurrentFragmentPosition = FRAGMENT_COMPANY;
                if(mFragmentSwitcher.getFragment(FRAGMENT_COMPANY) == null){
                    ThemeFragment fragment = new ThemeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(ThemeFragment.EXTRA_THEME_ID, 5);
                    fragment.setArguments(bundle);
                    mFragmentSwitcher.addFragment(FRAGMENT_COMPANY, fragment, FragmentSwitcher.OPERATION_SHOW_HIDE);
                }
                mFragmentSwitcher.switchFragment(FRAGMENT_COMPANY);
                mFab.setVisibility(View.GONE);
                break;
            case R.id.nav_internet_security:
                mCurrentFragmentPosition = FRAGMENT_SECURITY;
                if(mFragmentSwitcher.getFragment(FRAGMENT_SECURITY) == null){
                    ThemeFragment fragment = new ThemeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(ThemeFragment.EXTRA_THEME_ID, 10);
                    fragment.setArguments(bundle);
                    mFragmentSwitcher.addFragment(FRAGMENT_SECURITY, fragment, FragmentSwitcher.OPERATION_SHOW_HIDE);
                }
                mFragmentSwitcher.switchFragment(FRAGMENT_SECURITY);
                mFab.setVisibility(View.GONE);
                break;
            case R.id.nav_movie:
                mCurrentFragmentPosition = FRAGMENT_MOVIE;
                if(mFragmentSwitcher.getFragment(FRAGMENT_MOVIE) == null){
                    ThemeFragment fragment = new ThemeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(ThemeFragment.EXTRA_THEME_ID, 3);
                    fragment.setArguments(bundle);
                    mFragmentSwitcher.addFragment(FRAGMENT_MOVIE, fragment, FragmentSwitcher.OPERATION_SHOW_HIDE);
                }

                mFragmentSwitcher.switchFragment(FRAGMENT_MOVIE);
                mFab.setVisibility(View.GONE);
                break;
            case R.id.nav_sports:
                mCurrentFragmentPosition = FRAGMENT_SPORTS;
                if(mFragmentSwitcher.getFragment(FRAGMENT_SPORTS) == null){
                    ThemeFragment fragment = new ThemeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(ThemeFragment.EXTRA_THEME_ID, 8);
                    fragment.setArguments(bundle);
                    mFragmentSwitcher.addFragment(FRAGMENT_SPORTS, fragment, FragmentSwitcher.OPERATION_SHOW_HIDE);
                }
                mFragmentSwitcher.switchFragment(FRAGMENT_SPORTS);
                mFab.setVisibility(View.GONE);
                break;
            case R.id.nav_comic:
                mCurrentFragmentPosition = FRAGMENT_COMIC;
                if(mFragmentSwitcher.getFragment(FRAGMENT_COMIC) == null){
                    ThemeFragment fragment = new ThemeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(ThemeFragment.EXTRA_THEME_ID, 9);
                    fragment.setArguments(bundle);
                    mFragmentSwitcher.addFragment(FRAGMENT_COMIC, fragment, FragmentSwitcher.OPERATION_SHOW_HIDE);
                }
                mFragmentSwitcher.switchFragment(FRAGMENT_COMIC);
                mFab.setVisibility(View.GONE);
                break;
            case R.id.nav_game:
                mCurrentFragmentPosition = FRAGMENT_GAME;
                if(mFragmentSwitcher.getFragment(FRAGMENT_GAME) == null){
                    ThemeFragment fragment = new ThemeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(ThemeFragment.EXTRA_THEME_ID, 2);
                    fragment.setArguments(bundle);
                    mFragmentSwitcher.addFragment(FRAGMENT_GAME, fragment, FragmentSwitcher.OPERATION_SHOW_HIDE);
                }
                mFragmentSwitcher.switchFragment(FRAGMENT_GAME);
                mFab.setVisibility(View.GONE);
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
