package com.example.kshitijt.contactstest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by kshitij.t on 30/06/15.
 */
public class AuthService extends Service {
    
    @Override
    public IBinder onBind(Intent intent) {
        return new ContactsTestAuthenticator(this).getIBinder();
    }
}
