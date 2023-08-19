package com.xtracover.xcqcmh.Utilities;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiNetworkClient {

    private static final String Base_Url = "https://store.xtracover.com/api/StoreApi/";
    private static final String Base_UrlQutrust = "http://api.qutrust.com/clientdata/";
    private static final String Base_UrlWithPinCode = "https://channel.xtracover.com/api/StoreApi/";
    private static Retrofit retrofit = null;
    private static Retrofit retrofitQutrust = null;
    private static Retrofit retrofitWithPinCode = null;

    public static Retrofit getStoreApiRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptorLogin = new HttpLoggingInterceptor();
            interceptorLogin.setLevel(HttpLoggingInterceptor.Level.BODY);

            CookieHandler cookieHandlerLogin = new CookieManager();
            OkHttpClient clientLogin = new OkHttpClient.Builder().addNetworkInterceptor(interceptorLogin)
                    .cookieJar(new JavaNetCookieJar(cookieHandlerLogin))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_Url)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(clientLogin)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitQutrust() {
        if (retrofitQutrust == null) {
            HttpLoggingInterceptor interceptorLogin = new HttpLoggingInterceptor();
            interceptorLogin.setLevel(HttpLoggingInterceptor.Level.BODY);

            CookieHandler cookieHandlerLogin = new CookieManager();
            OkHttpClient clientLogin = new OkHttpClient.Builder().addNetworkInterceptor(interceptorLogin)
                    .cookieJar(new JavaNetCookieJar(cookieHandlerLogin))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofitQutrust = new Retrofit.Builder()
                    .baseUrl(Base_UrlQutrust)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(clientLogin)
                    .build();
        }
        return retrofitQutrust;
    }

    public static Retrofit getRetrofitWithPinCode() {
        if (retrofitWithPinCode == null) {
            HttpLoggingInterceptor interceptorLogin = new HttpLoggingInterceptor();
            interceptorLogin.setLevel(HttpLoggingInterceptor.Level.BODY);

            CookieHandler cookieHandlerLogin = new CookieManager();
            OkHttpClient clientLogin = new OkHttpClient.Builder().addNetworkInterceptor(interceptorLogin)
                    .cookieJar(new JavaNetCookieJar(cookieHandlerLogin))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofitWithPinCode = new Retrofit.Builder()
                    .baseUrl(Base_UrlWithPinCode)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(clientLogin)
                    .build();
        }
        return retrofitWithPinCode;
    }
}
