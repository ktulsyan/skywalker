package com.example.kshitijt.contactstest;

import android.app.Application;

/**
 * Created by kshitij.t on 06/07/15.
 */
public class ContactsTestApplication extends Application {

    private static TestSyncAdapter syncAdapter;
    private static Object syncAdapterLock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (syncAdapterLock)
        {
            if(syncAdapter== null)
            {
                syncAdapter = new TestSyncAdapter(this, true);
            }
        }
    }

    public static TestSyncAdapter getSyncAdapter() {
        return syncAdapter;
    }


}
