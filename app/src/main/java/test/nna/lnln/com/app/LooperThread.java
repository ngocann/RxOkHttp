package test.nna.lnln.com.app;

import android.os.Looper;

public class LooperThread extends Thread {

    @Override
    public void run() {
        super.run();
        Looper.prepare();
    }
}
