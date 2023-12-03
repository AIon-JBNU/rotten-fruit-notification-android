package dev.s0n9.rottenfruitnotification;

import android.app.Application;

public class MyApplication extends Application {
    public static String FCM_TOKEN = "";
    public static FCMTokenReloadListener LISTENER = null;

    static interface FCMTokenReloadListener {
        void onFCMTokenReload();
    }
}
