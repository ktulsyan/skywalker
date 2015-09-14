package com.example.kshitijt.contactstest;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kshitij.t on 01/07/15.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.login);
    }

    public void onCancelClick(View v) {
        finish();
    }

    public void onSaveClick(View v) {
        TextView tvUsername = (TextView) findViewById(R.id.uc_txt_username);
        TextView tvPassword = (TextView) findViewById(R.id.uc_txt_password);
        String username = tvUsername.getText().toString();
        String password = tvPassword.getText().toString();
        boolean hasErrors = false;

        if (!username.toUpperCase().equals("KT")) {
            hasErrors = true;
        }
        if (!password.toUpperCase().equals("KT")) {
            hasErrors = true;
        }

        if (hasErrors) {
            return;
        } else {
            try {
                String accountType = ContactsTestAuthenticator.ACCOUNT_TYPE;

                AccountManager accountManager = AccountManager.get(this);

                // This is the magic that addes the account to the Android Account Manager
                final Account account = new Account(username, accountType);
                accountManager.addAccountExplicitly(account, password, null);

                // Now we tell our caller, could be the Android Account Manager
                // or even our own application that the process was successful
                final Intent intent = new Intent();
                intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
                intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                setAccountAuthenticatorResult(intent.getExtras());
                setResult(RESULT_OK, intent);
                finish();
            }
            catch (Exception e)
            {
                Log.e("Update table","error while adding account",e);
            }
        }

    }
}
