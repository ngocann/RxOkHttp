package xyz.blackice.rxokhttptest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import xyz.blackice.rxokhttp.R;
import xyz.blackice.rxokhttp.RequestBuilder;
import xyz.blackice.rxokhttp.RxOkHttp;
import xyz.blackice.rxokhttptest.FileConfig;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = " MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestBuilder requestBuilder = new RequestBuilder()
                .setUrl("http://kara.itvcom.net:18400/kacms/ota/ota.json?did=d0a552fe10182bf0&model=Q10%20Pro&lv=1.8.3");
        String url = "http://kara.itvcom.net:18400/kacms/ota/ota.json?did=d0a552fe10182bf0&model=Q10%20Pro&lv=1.8.3";
        RxOkHttp rxOkHttp = new RxOkHttp(requestBuilder);
        rxOkHttp.get2(url, FileConfig.class)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileConfig -> {
                    Log.d(TAG, "onCreate " + fileConfig);
                }, error -> error.printStackTrace());

    }
}
