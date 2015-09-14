/*
 * Copyright (C) 2012 Graeme Woodhouse
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.kshitijt.contactstest;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsIntegrationActivity extends Activity {

    // Request code for the contact picker activity
    private static final int PICK_CONTACT_REQUEST = 1;
    private static final String CALLOUT_MIME_TYPE = "vnd.android.cursor.item/vnd.com.example.kshitijt.contactstest.android.callout.profile";
    private static final String CALLBACK_MIME_TYPE = "vnd.android.cursor.item/vnd.com.example.kshitijt.contactstest.android.callback.profile";

    private TextView mResult;
    private String mEmail;

    private String mContactId;
    private String mContactNameRawContactId;
    private String mRawContactId;
    private String mDataId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mResult = (TextView) findViewById(R.id.result);
        final EditText mEmailEditText = (EditText) findViewById(R.id.email);
        Button mAttach = (Button) findViewById(R.id.attach);

        mAttach.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                mEmail = mEmailEditText.getText().toString();
                startActivityForResult(new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI), PICK_CONTACT_REQUEST);
            }
        });
    }

    /**
     * Invoked when the contact picker activity is finished. The {@code contactUri} parameter
     * will contain a reference to the contact selected by the user. We will treat it as
     * an opaque URI and allow the SDK-specific ContactAccessor to handle the URI accordingly.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            loadContactInfo(data.getData());
        }
    }

    /**
     * Load contact information on a background thread.
     */
    private void loadContactInfo(Uri contactUri) {

        /*
         * We should always run database queries on a background thread. The database may be
         * locked by some process for a long time.  If we locked up the UI thread while waiting
         * for the query to come back, we might get an "Application Not Responding" dialog.
         */
        AsyncTask<Uri, Void, Boolean> task = new AsyncTask<Uri, Void, Boolean>() {


            @Override
            protected Boolean doInBackground(Uri... uris) {
                Log.v("Retreived ContactURI", uris[0].toString());

                return hasTestRawContact(uris[0]);
            }

            @Override
            protected void onPostExecute(Boolean exists) {
                if (exists) {
                    Log.v("", "Updating...");
                    updateTestContact();
                } else {
                    Log.v("", "Inserting...");
                    insertTestContact();
                }
            }
        };

        task.execute(contactUri);
    }

    private Boolean hasTestRawContact(Uri contactUri) {
        boolean returnValue = false;
        Cursor mContactCursor = getContentResolver().query(contactUri,
                null,
                null,
                null,
                null);
        Log.v("Contact", "Got Contact Cursor");

        try {
            if (mContactCursor.moveToFirst()) {

                Log.v("Cursor", mContactCursor.toString());

                mContactId = getCursorString(mContactCursor,
                        Contacts._ID);
                mContactNameRawContactId = getCursorString(mContactCursor, Contacts.NAME_RAW_CONTACT_ID);

                Cursor mRawContactCursor = getContentResolver().query(
                        RawContacts.CONTENT_URI,
                        null,
                        Data.CONTACT_ID + " = ?",
                        new String[]{mContactId},
                        null);

                Log.v("RawContact", "Got RawContact Cursor");

                try {
                    while (mRawContactCursor.moveToNext()) {
                        String rawId = getCursorString(mRawContactCursor, RawContacts._ID);
                        String data;
                        data = getCursorString(mRawContactCursor, RawContacts.ACCOUNT_TYPE);
                        Log.v("RawContact AccountType:", data == null ? "\n" : data);

                        if (data.equals(ContactsTestAuthenticator.ACCOUNT_TYPE)) {
                            mRawContactId = rawId;
                            returnValue = true;
                        }
                    }
                } finally {
                    mRawContactCursor.close();
                }
            }
        } catch (Exception e) {
            Log.w("UpdateContact", e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                Log.w("UpdateContact", "\t" + ste.toString());
            }
            throw new RuntimeException();
        } finally {
            mContactCursor.close();
        }

        return returnValue;
    }


    private static String getCursorString(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index != -1) return cursor.getString(index);
        return null;
    }

    public void insertTestContact() {
        try {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            String fname = "";
            String lname = "";

            Cursor rawContactCursor = getContentResolver().query(RawContacts.CONTENT_URI,
                    null,
                    RawContacts._ID + " = ?",
                    new String[]{mContactNameRawContactId},
                    null);

            if (rawContactCursor.moveToFirst()) {
                String[] displayName = getCursorString(rawContactCursor, RawContacts.DISPLAY_NAME_PRIMARY).split(" ");
                if (displayName.length > 1) {
                    fname = displayName[0];
                    lname = displayName[displayName.length - 1];
                } else {
                    fname = displayName[0];
                    lname = "";
                }
            }

            ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                    .withValue(RawContacts.ACCOUNT_TYPE, ContactsTestAuthenticator.ACCOUNT_TYPE)
                    .withValue(RawContacts.ACCOUNT_NAME, "kt")
                    .build());

            ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                    .withValueBackReference(RawContacts.Data.RAW_CONTACT_ID, 0)
                    .withValue(Data.MIMETYPE, CALLOUT_MIME_TYPE)
                    .withValue(Data.DATA3, "+911234567890")
                    .withValue(Data.DATA4, "CALLOUT Detail")
                    .build());

            ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                    .withValueBackReference(RawContacts.Data.RAW_CONTACT_ID, 0)
                    .withValue(Data.MIMETYPE, CALLBACK_MIME_TYPE)
                    .withValue(Data.DATA3, "+911234567891")
                    .withValue(Data.DATA4, "CALLBACK Detail")
                    .withYieldAllowed(true)
                    .build());

            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            mResult.setText("Inserted");
        } catch (Exception e) {
            // Display warning
            Log.w("UpdateContact", e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                Log.w("UpdateContact", "\t" + ste.toString());
            }
            Context ctx = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(ctx, "Insert failed", duration);
            toast.show();
        }
    }

    public void updateTestContact() {
        try {
            Cursor dataCursor = getContentResolver().query(Data.CONTENT_URI,
                    null,
                    Data.RAW_CONTACT_ID + " = ? AND (" + Data.MIMETYPE + " = ? " + "OR " + Data.MIMETYPE + " = ? )",
                    new String[]{mRawContactId, CALLOUT_MIME_TYPE, CALLBACK_MIME_TYPE},
                    null);
            Log.v("Data", "Found data cursor");
            String dataId;
            String mimeType;

            if (!dataCursor.moveToFirst()) {
                throw new Exception("Data item no found");
            } else {
                dataId = getCursorString(dataCursor, Data._ID);
                mimeType = getCursorString(dataCursor, Data.MIMETYPE);

            }


            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

            ops.add(ContentProviderOperation.newUpdate(Data.CONTENT_URI)
                    .withSelection(Data.RAW_CONTACT_ID + " = ?", new String[]{mRawContactId})
                    .withSelection(Data._ID + " = ?", new String[]{dataId})
                    .withValue(Data.MIMETYPE, mimeType)
                    .build());
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            mResult.setText("Deleted");
        } catch (Exception e) {
            // Display warning
            Log.w("UpdateContact", e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                Log.w("UpdateContact", "\t" + ste.toString());
            }
            Context ctx = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(ctx, "Update failed", duration);
            toast.show();
        }
    }
}