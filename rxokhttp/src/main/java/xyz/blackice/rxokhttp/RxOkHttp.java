package xyz.blackice.rxokhttp;

import java.io.IOException;
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
    private String url;
    private Class clazz;


    public RxOkHttp(RequestBuilder requestBuilder){

        this.url = requestBuilder.getUrl();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (requestBuilder.getTimeout() != 0){
            builder.connectTimeout(requestBuilder.getTimeout(), TimeUnit.SECONDS);
            builder.readTimeout(requestBuilder.getTimeout(), TimeUnit.SECONDS);
        }
        if (requestBuilder.getClazz() != null){
            this.clazz = requestBuilder.getClazz();
        }
        okHttpClient = builder.build();
    }

    public <T extends Class> Observable<T> get2(final String url, final T clazz){
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
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

            }
        });

    }
    public Observable<String> get(final String url){
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {

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

            }
        });
    }







}
