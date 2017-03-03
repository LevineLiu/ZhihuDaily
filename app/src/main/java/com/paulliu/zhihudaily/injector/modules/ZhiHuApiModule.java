package com.paulliu.zhihudaily.injector.modules;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.paulliu.zhihudaily.BuildConfig;
import com.paulliu.zhihudaily.ZhiHuDailyApplication;
import com.paulliu.zhihudaily.api.ZhiHuDailyApi;
import com.paulliu.zhihudaily.util.NetUtils;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on 2017/1/11
 *
 * @author LLW
 */
@Module
public class ZhiHuApiModule {

    @Provides
    public Interceptor provideCachedInterceptor(final ZhiHuDailyApplication application) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                //没有网络强制使用缓存
                if (!NetUtils.isNetWorkAvailable(application)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response originalResponse = chain.proceed(request);
                if (NetUtils.isNetWorkAvailable(application)) {
                    //有网络情况下,不读取缓存
                    int maxAge = 0;
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 7; //最大过时时间一周
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale" + maxStale)
                            .build();
                }
            }
        };
    }

    @Provides
    public OkHttpClient provideOkHttpClient(ZhiHuDailyApplication application, Interceptor interceptor) {
        File cacheDirectory = new File(application.getCacheDir(), "responses");
        int cacheSize = 5 * 1024 * 1024; // 5Mib
        Cache cache = new Cache(cacheDirectory, cacheSize);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cache(cache).addInterceptor(interceptor);
        return builder.build();
    }

    @Singleton
    @Provides
    public ZhiHuDailyApi provideZhiHuDailyApi(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BuildConfig.SERVER)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ZhiHuDailyApi.class);
    }
}
