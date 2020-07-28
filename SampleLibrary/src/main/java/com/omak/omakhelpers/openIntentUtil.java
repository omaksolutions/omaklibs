package com.omak.omakhelpers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.core.app.ActivityCompat;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class openIntentUtil {

    // open save contact intent
    public static void openSaveContact(Context context, String name, String number, String email) {
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);

        if (!name.isEmpty()) {
            intent.putExtra(ContactsContract.Intents.Insert.NAME, HelperFunctions.upperCaseLetter(name));
        }

        if (!number.isEmpty()) {
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
            intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
        }

        if (!email.isEmpty()) {
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
        }

        context.startActivity(intent);
    }

    // open whatsApp intent
    public static boolean openWhatsApp(Context context, String number, String name) {
        Boolean success = false;
        try {
            String bodyMessageFormal = "Hello";
            bodyMessageFormal += (name == "") ? "" : " " + HelperFunctions.upperCaseLetter(name);
            bodyMessageFormal += ", \n\n"; // Replace with your message.

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.putExtra(Intent.EXTRA_TEXT, "");
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.setType("text/rtf");
            intent.setData(Uri.parse("https://wa.me/" + number + "/?text=" + bodyMessageFormal));
            context.startActivity(intent);
            success = true;
        } catch (Exception e) {
            HelperFunctions.toaster(context, "Message failed: " + e.getMessage());
            //Toast.makeText(context, "Message failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return success;
    }
// openSms app intent
    public static boolean openSmsApp(Context context, String number, String name) {
        Boolean success = false;
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", number);
        smsIntent.putExtra("sms_body", "Hi " + name + ",");
        smsIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(smsIntent);
        success = true;

        return success;
    }
}
