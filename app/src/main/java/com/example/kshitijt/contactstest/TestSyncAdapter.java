package com.example.kshitijt.contactstest;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Created by kshitij.t on 02/07/15.
 */
public class TestSyncAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver _resolver;

    public TestSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        _resolver = context.getContentResolver();
    }

    public TestSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs)
    {
        super(context, autoInitialize, allowParallelSyncs);
        _resolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
    }
}
