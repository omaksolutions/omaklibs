package com.omak.omakhelpers;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.omak.omakhelpers.contactUtil.ContactsHelper;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class openIntentUtil {

    //open Dialer
    public static void openDialer(Context context, String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        callIntent.setAction("android.intent.action.DIAL"); // but this line is an optional one
        callIntent.setData(Uri.parse("tel:" + number));
        context.startActivity(callIntent);
    }

    // find the contact number exiting or not
    public static boolean contactExists(Context context, String number) {
        /// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.DISPLAY_NAME};

        Cursor cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection,
                null, null, null);
        try {
            if (cur.moveToFirst()) {
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }

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

    // update exiting contact
    public static void openContactToEditByNumber(Context context, String number) {
        try {
            long contactId = ContactsHelper.getContactByPhoneNumber(context, number).getContactId();
            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);

            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setDataAndType(uri, ContactsContract.Contacts.CONTENT_ITEM_TYPE);
            intent.putExtra("finishActivityOnSaveCompleted", true);

            intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Oops there was a problem trying to open the contact :(" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // open whatsApp intent
    public static boolean openWhatsapp(Context context, String number, String name) {
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
