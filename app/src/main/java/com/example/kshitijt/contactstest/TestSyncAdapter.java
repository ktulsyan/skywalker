package com.example.kshitijt.contactstest;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.nfc.cardemulation.CardEmulation;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by kshitij.t on 02/07/15.
 */
public class TestSyncAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver _resolver;

    public TestSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        _resolver = context.getContentResolver();
    }

    public TestSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        _resolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        /*
        try {

            Cursor cursor = provider.query(ContactsContract.RawContacts.CONTENT_URI,
                    null,
                    ContactsContract.RawContacts.ACCOUNT_TYPE + " + = ? AND " + ContactsContract.RawContacts.ACCOUNT_NAME + " = ? ",
                    new String[]{"com.example.kshtitijt.contactsTest", "kt"},
                    null);

            if (cursor.moveToFirst()) {
                //update
                String mRawContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts._ID));

                Cursor dataCursor = provider.query(ContactsContract.Data.CONTENT_URI,
                        null,
                        ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.Phone.TYPE + " = ? ",
                        new String[]{mRawContactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK)},
                        null);
                Log.v("Data", "Found data cursor");
                String dataId;

                if (!dataCursor.moveToFirst()) {
                    throw new IllegalStateException("Data item not found");
                } else {
                    dataId = dataCursor.getString(cursor.getColumnIndex(ContactsContract.Data._ID));
                }


                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

                ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI
                        .buildUpon()
                        .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
                        .build())
                        .withSelection(ContactsContract.Data.RAW_CONTACT_ID + " = ?", new String[]{mRawContactId})
                        .withSelection(ContactsContract.Data._ID + " = ?", new String[]{dataId})
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK))
                        .withValue(ContactsContract.Data.DATA1, "+917893045408")
                        .build());

                provider.applyBatch(ops);
            } else {
                //insert
                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

                ops.add(ContentProviderOperation
                        .newUpdate(ContactsContract.Settings.CONTENT_URI
                                .buildUpon()
                                .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
                                .build())
                        .withSelection(ContactsContract.Settings.ACCOUNT_TYPE, new String[]{ContactsTestAuthenticator.ACCOUNT_TYPE})
                        .withValue(ContactsContract.Settings.UNGROUPED_VISIBLE, "1")
                        .build());

                ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI
                        .buildUpon()
                        .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
                        .build())
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, ContactsTestAuthenticator.ACCOUNT_TYPE)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, "kt")
                        .build());

                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI
                        .buildUpon()
                        .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
                        .build())
                        .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.Data.DATA1, "AAAAAAA Test")
                        .withValue(ContactsContract.Data.IS_READ_ONLY, "1")
                        .build());

                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI
                        .buildUpon()
                        .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
                        .build())
                        .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK)
                        .withValue(ContactsContract.Data.DATA1, "+911234567890")
                        .withValue(ContactsContract.Data.IS_READ_ONLY, "1")
                        .withYieldAllowed(true)
                        .build());

                provider.applyBatch(ops);

            }

        } catch (RemoteException e) {
            Log.e("SyncAdapter", "Error while query:", e);
        }
        catch (OperationApplicationException e) {
            Log.e("SyncAdapter", "Error while insert/update:", e);
        }
        catch (IllegalStateException e)
        {
            Log.e("SyncAdapter", "Error while update:", e);
        }
        finally {
            provider.release();
        }

*/
    }
}
