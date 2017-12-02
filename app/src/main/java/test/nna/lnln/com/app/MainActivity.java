package test.nna.lnln.com.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import xyz.blackice.rxokhttp.model.ImplSaveFile;
import xyz.blackice.rxokhttp.RequestBuilder;
import xyz.blackice.rxokhttp.RxOkHttp;
import xyz.blackice.rxokhttp.helper.TargetUi;
import xyz.blackice.rxokhttp.model.SimpleSaveFile;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "nna - MainActivity";
    private final String url = "http://kara.itvcom.net:18400/kacms/ota/ota.json";
    private final String urlList= "https://s3-ap-northeast-1.amazonaws.com/dev-lnlnqa/lunababy/android/info.json";
    private final String urlVideo ="http://kara.itvcom.net:18400/kacms/background/video/20171113090231_karaoke_background_640_480.mp4";
    private final String urlVideo2 = "http://kara.itvcom.net:18400/kacms/background/video/20171113084523_videoplayback_2_cabien.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxOkHttp rxOkHttp = new RxOkHttp(new RequestBuilder());
//        rxOkHttp.get(url, FileConfig.class)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(fileConfig -> Log.d(TAG, "onCreate " + fileConfig)
//                        , error -> error.printStackTrace());
//        rxOkHttp.getList(urlList, SystemMessage[].class)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(fileConfig -> Log.d(TAG, "onCreate " + fileConfig)
//                        , error -> error.printStackTrace());

//        rxOkHttp.saveFile(new TargetUi(this), "/mnt/sdcard/", Arrays.asList(url, urlList))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(fileConfig -> Log.d(TAG, "onCreate " + fileConfig)
//                        , error -> error.printStackTrace());
//        rxOkHttp.get(url, FileConfig.class)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(fileConfig -> Log.d(TAG, "onCreate " + fileConfig)
//                        , error -> error.printStackTrace());

        rxOkHttp.saveListFileProgress(new TargetUi(this), "/mnt/sdcard/", Arrays.asList(new SimpleSaveFile(urlVideo), new SimpleSaveFile(urlVideo2)))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> {
                    Log.d(TAG, "onCreate " + file);
                }, err -> {
                    err.printStackTrace();
                }, () -> {
                    Log.d(TAG, "onCreate complete");

                });

    }
}
