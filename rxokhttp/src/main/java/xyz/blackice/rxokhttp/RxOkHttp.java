package xyz.blackice.rxokhttp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Ann on 11/8/17.
 */

public class RxOkHttp {

    private OkHttpClient okHttpClient;

    public RxOkHttp(RequestBuilder requestBuilder){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (requestBuilder.getTimeout() != 0){
            builder.connectTimeout(requestBuilder.getTimeout(), TimeUnit.SECONDS);
            builder.readTimeout(requestBuilder.getTimeout(), TimeUnit.SECONDS);
        }
        okHttpClient = builder.build();
    }

    public Observable<String> get(final String url){
        return Observable.create(e -> {
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = okHttpClient.newCall(request).execute();
                String strResponse = response.body().string();
                response.body().close();
                e.onNext(strResponse);
                e.onComplete();
            }catch (IOException exception){
                e.onError(exception);
            }

        });
    }

    public <T> Observable<T> get(final String url, final Class<T> dataClass){
        return get(url)
                .flatMap( strResponse -> {
                    Gson gson = new Gson();
                    return Observable.just(gson.fromJson(strResponse, dataClass));
                });
    }

    public <T> Observable<List<T>> getList(final String url, final Class<T[]> dataClass){
        return get(url)
                .flatMap( strResponse -> {
                    Gson gson = new Gson();
                    T[] arrayResult = gson.fromJson(strResponse, dataClass);
                    return Observable.just(Arrays.asList(arrayResult));
                });
    }

    //Download File

    //Post Image

    //Send File




}
