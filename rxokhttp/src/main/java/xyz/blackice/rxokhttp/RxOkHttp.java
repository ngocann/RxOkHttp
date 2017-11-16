package xyz.blackice.rxokhttp;

import android.Manifest;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import xyz.blackice.rxokhttp.helper.GrantPermissions;
import xyz.blackice.rxokhttp.helper.TargetUi;

/**
 * Created by Ann on 11/8/17.
 */

public class RxOkHttp {

    private OkHttpClient okHttpClient;

    public RxOkHttp(RequestBuilder requestBuilder) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (requestBuilder.getTimeout() != 0) {
            builder.connectTimeout(requestBuilder.getTimeout(), TimeUnit.SECONDS);
            builder.readTimeout(requestBuilder.getTimeout(), TimeUnit.SECONDS);
        }
        okHttpClient = builder.build();
    }

    private Observable<String> get(Request request) {
        return Observable.create(e -> {
            try {
                Response response = okHttpClient.newCall(request).execute();
                String strResponse = response.body().string();
                response.body().close();
                e.onNext(strResponse);
                e.onComplete();
            } catch (IOException exception) {
                e.onError(exception);
            }

        });
    }

    private Observable<String> get(final String url) {
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
            } catch (IOException exception) {
                e.onError(exception);
            }

        });
    }

    public <T> Observable<T> get(final Request request, final Class<T> dataClass) {
        return get(request)
                .flatMap(strResponse -> {
                    Gson gson = new Gson();
                    return Observable.just(gson.fromJson(strResponse, dataClass));
                });
    }

    public <T> Observable<T> get(final String url, final Class<T> dataClass) {
        return get(url)
                .flatMap(strResponse -> {
                    Gson gson = new Gson();
                    return Observable.just(gson.fromJson(strResponse, dataClass));
                });
    }

    public <T> Observable<List<T>> getList(final String url, final Class<T[]> dataClass) {
        return get(url)
                .flatMap(strResponse -> {
                    Gson gson = new Gson();
                    T[] arrayResult = gson.fromJson(strResponse, dataClass);
                    return Observable.just(Arrays.asList(arrayResult));
                });
    }

    public <T> Observable<List<T>> getList(final Request request, final Class<T[]> dataClass) {
        return get(request)
                .flatMap(strResponse -> {
                    Gson gson = new Gson();
                    T[] arrayResult = gson.fromJson(strResponse, dataClass);
                    return Observable.just(Arrays.asList(arrayResult));
                });
    }

    //Download File
    private File saveFile(String pathFolderSaved, String url) throws IOException {
        String filename = url.substring(url.lastIndexOf("/") + 1, url.length());
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String destination = pathFolderSaved + filename;
        File file = new File(destination);
        if (file.exists()) {
            file.delete();
        }
        BufferedSink sink = Okio.buffer(Okio.sink(file));
        sink.writeAll(response.body().source());
        sink.close();
        return file;
    }

    public Observable<String> saveFile(final TargetUi targetUi, String path, final String url) {
        GrantPermissions grantPermissions = new GrantPermissions(targetUi);
        return grantPermissions.with(permissions())
                .react()
                .flatMap(ignore -> {
                    File file = saveFile(path, url);
                    return Observable.just(file.getAbsolutePath());
                });
    }

    public Single<List<String>> saveFile(final TargetUi targetUi, String path, final List<String> urls) {
        GrantPermissions grantPermissions = new GrantPermissions(targetUi);
        return grantPermissions.with(permissions())
                .react()
                .flatMap(ignore -> Observable.fromIterable(urls)
                        .flatMap(s -> {
                            File file = saveFile(path, s);
                            return Observable.just(file.getAbsolutePath());
                        })
                )
                .toList()
        ;
    }

    private String[] permissions() {
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    //Post Image

    //Send File


}
