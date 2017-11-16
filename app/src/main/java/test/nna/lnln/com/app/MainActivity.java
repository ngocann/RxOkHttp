package test.nna.lnln.com.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import xyz.blackice.rxokhttp.RequestBuilder;
import xyz.blackice.rxokhttp.RxOkHttp;
import xyz.blackice.rxokhttp.helper.TargetUi;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "nna - MainActivity";
    private final String url = "http://kara.itvcom.net:18400/kacms/ota/ota.json";
    private final String urlList= "https://s3-ap-northeast-1.amazonaws.com/dev-lnlnqa/lunababy/android/info.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxOkHttp rxOkHttp = new RxOkHttp(new RequestBuilder());
        rxOkHttp.get(url, FileConfig.class)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileConfig -> Log.d(TAG, "onCreate " + fileConfig)
                        , error -> error.printStackTrace());

//        rxOkHttp.getList(urlList, SystemMessage[].class)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(fileConfig -> Log.d(TAG, "onCreate " + fileConfig)
//                        , error -> error.printStackTrace());

        rxOkHttp.saveFile(new TargetUi(this), "/mnt/sdcard/", Arrays.asList(url, urlList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileConfig -> Log.d(TAG, "onCreate " + fileConfig)
                        , error -> error.printStackTrace());

    }
}
