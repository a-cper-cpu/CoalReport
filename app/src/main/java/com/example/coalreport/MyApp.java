package com.example.coalreport;

import android.app.Application;
import android.content.SharedPreferences;

import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;

import org.xutils.x;

/**
 * @author a-cper-cpu
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
