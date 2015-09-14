package com.example.kshitijt.contactstest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import junit.framework.Test;

/**
 * Created by kshitij.t on 03/07/15.
 */
public class TestSyncService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return ContactsTestApplication.getSyncAdapter().getSyncAdapterBinder();
    }
}
