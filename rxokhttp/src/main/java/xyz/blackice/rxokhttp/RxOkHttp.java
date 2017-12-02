package xyz.blackice.rxokhttp;

import android.Manifest;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import xyz.blackice.rxokhttp.helper.GrantPermissions;
import xyz.blackice.rxokhttp.helper.TargetUi;
import xyz.blackice.rxokhttp.model.ImplSaveFile;

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
        file.mkdirs();
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

    interface ProgressListener {
        void update(long bytesRead, long contentLength, boolean done);
    }
    private static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override public long contentLength() {
            return responseBody.contentLength();
        }

        @Override public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }
    public <T extends ImplSaveFile> Observable<T> downloadFile(String path, T t) {
        return Observable.create(e -> {
            try {
                String url = t.getUrlFile();
                String filename = url.substring(url.lastIndexOf("/") + 1, url.length());
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                OkHttpClient client = new OkHttpClient.Builder()
                        .addNetworkInterceptor(chain -> {
                            Response originalResponse = chain.proceed(chain.request());
                            return originalResponse.newBuilder()
                                    .body(new ProgressResponseBody(originalResponse.body(), (bytesRead, contentLength, done) -> {
                                        t.progress((int) (100*bytesRead / contentLength));
                                        t.isCompleted(done);
                                        e.onNext(t);
                                        if (done) {
                                            e.onComplete();
                                        }
                                    }))
                                    .build();
                        })
                        .build();
                Response response = client.newCall(request).execute();
                String destination = path + filename;
                File file = new File(destination);
                file.mkdirs();
                if (file.exists()) {
                    file.delete();
                }
                BufferedSink sink = Okio.buffer(Okio.sink(file));
                sink.writeAll(response.body().source());
                sink.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                e.onError(ex);
            }
        });
    }

    public <T extends ImplSaveFile> Observable<T> saveListFileProgress(final TargetUi targetUi, String path, List<T> t) {
        GrantPermissions grantPermissions = new GrantPermissions(targetUi);
        return grantPermissions.with(permissions())
                .react()
                .flatMap(ignore -> Observable.fromIterable(t)
                        .concatMap(implSaveFile -> saveFileProgress(targetUi, path, implSaveFile))
                );
    }

    public <T extends ImplSaveFile> Observable<T> saveFileProgress(final TargetUi targetUi, String path, T t) {
        GrantPermissions grantPermissions = new GrantPermissions(targetUi);
        return grantPermissions.with(permissions())
                .react()
                .flatMap(ignore -> downloadFile(path, t));
    }

    public Single<? extends List<? extends ImplSaveFile>> saveFileFromList(final TargetUi targetUi, String path, final List<? extends ImplSaveFile> urls) {
        GrantPermissions grantPermissions = new GrantPermissions(targetUi);
        return grantPermissions.with(permissions())
                .react()
                .flatMap(ignore -> Observable.fromIterable(urls)
                        .flatMap(implSaveFile -> {
                            File file = saveFile(path, implSaveFile.getUrlFile());
                            return Observable.just(implSaveFile);
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
