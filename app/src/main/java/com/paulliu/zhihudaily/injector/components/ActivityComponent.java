package com.paulliu.zhihudaily.injector.components;

import com.paulliu.zhihudaily.injector.ActivityScope;
import com.paulliu.zhihudaily.injector.modules.ActivityModule;
import com.paulliu.zhihudaily.ui.BaseAppCompatActivity;
import com.paulliu.zhihudaily.ui.activity.CommentListActivity;
import com.paulliu.zhihudaily.ui.activity.DailyNewsActivity;
import com.paulliu.zhihudaily.ui.activity.EditorListActivity;
import com.paulliu.zhihudaily.ui.activity.MyCollectionActivity;
import com.paulliu.zhihudaily.ui.activity.NewsDetailActivity;
import com.paulliu.zhihudaily.ui.activity.SplashActivity;
import com.paulliu.zhihudaily.ui.fragment.HomeFragment;
import com.paulliu.zhihudaily.ui.fragment.ThemeFragment;

import dagger.Component;

/**
 * Created on 2017/1/11
 *
 * @author LLW
 */
@ActivityScope
@Component(modules = {ActivityModule.class},dependencies = {AppComponent.class})
public interface ActivityComponent {
    void inject(BaseAppCompatActivity activity);
    void inject(SplashActivity activity);
    void inject(NewsDetailActivity activity);
    void inject(DailyNewsActivity activity);
    void inject(CommentListActivity activity);
    void inject(MyCollectionActivity activity);
    void inject(HomeFragment fragment);
    void inject(ThemeFragment fragment);
    void inject(EditorListActivity fragment);
}
