package com.omak.omakhelpers.contactUtil;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.omak.readmin.aallHelpers.Alerts;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class ContactsHelper {

    // Constants
    public static final Contact UNKNOWN = new Contact("Unknown", "", null);
    public static final Contact VOICEMAIL = new Contact("Voicemail", "", null);
    public static final Contact ERROR = new Contact("Error", "", null);

    /**
     * Returns a contact by a given phone number
     *
     * @param context
     * @param phoneNumber
     * @return Contact
     */
    public static Contact getContactByPhoneNumber(@NonNull Context context, @NonNull String phoneNumber) {
        // if no number, return null
        if (phoneNumber.isEmpty()) return null;
        // check for permission to read contacts

        // get contacts cursor
        Cursor cursor = new ContactsCursorLoader(context, phoneNumber, null).loadInBackground();
        // if cursor null, there is no contact, return only with number
        if (cursor == null) return new Contact(null, phoneNumber, null);
        Alerts.log("is", "Contacts");
        // there is a match, return the first one
        cursor.moveToFirst();
        return new Contact(cursor);
    }

    /**
     * Returns a contact by a given name
     * (almost identical to the previous method (above) but filters the name instead of
     * the phone number)
     *
     * @param context
     * @param name
     * @return Contact
     */
    public static Contact getContactByName(@NonNull Context context, @NonNull String name) {
        // if no name return null
        if (name.isEmpty()) return null;
        // check for permission to read contacts
//        if (Utilities.checkPermissionGranted(context, READ_CONTACTS)) return null;
        // get contacts cursor
        Cursor cursor = new ContactsCursorLoader(context, null, name).loadInBackground();
        // no results, return only with a name
        if (cursor == null) return new Contact(name, null);
        // there is a match, return the first one
        cursor.moveToFirst();
        return new Contact(cursor);
    }

    /**
     * Opens 'Add Contact' dialog from default os
     *
     * @param number
     */
    public static void addContactIntent(Activity activity, String number) {
        Intent addContactIntent = new Intent(Intent.ACTION_INSERT); // initiate intent
        addContactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE); // add type
        addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, number); // add number
        int PICK_CONTACT = 100; // Unique number to return when done with intent
        activity.startActivityForResult(addContactIntent, PICK_CONTACT); // start intent
    }

    /**
     * Opens a contact in the default contacts app by a given number
     *
     * @param activity
     * @param number
     */
    public static void openContactByNumber(Activity activity, String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = new String[]{ContactsContract.PhoneLookup._ID};
        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);

        // if cursor isn't empty, take the first result
        if (cursor != null && cursor.moveToNext()) {
            Long id = cursor.getLong(0);
            openContactById(activity, id);
        }
        cursor.close(); // close the mf cursor
    }

    /**
     * Open contact in default contacts app by the contact's id
     *
     * @param activity
     * @param contactId
     */
    public static void openContactById(Activity activity, long contactId) {
        try {
            // new intent to view contact in contacts
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactId));
            intent.setData(uri);
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "Oops there was a problem trying to open the contact :(", Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * Open contact edit page in default contacts app by contact's id
     *
     * @param context
     * @param number
     */
    public static void openContactToEditByNumber(Context context, String number) {
        try {
            long contactId = ContactsHelper.getContactByPhoneNumber(context, number).getContactId();
            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);

            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setDataAndType(uri, ContactsContract.Contacts.CONTENT_ITEM_TYPE);
            intent.putExtra("finishActivityOnSaveCompleted", true);

            intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Activity activity = AppFlowHelpers.getActivity(context);
            //activity.startActivityForResult(intent, 1);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Oops there was a problem trying to open the contact :(" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Open contact edit page in default contacts app by contact's id
     *
     * @param activity
     * @param contactId
     */
    public static void openContactToEditById(Activity activity, long contactId) {
        try {
            Intent intent = new Intent(Intent.ACTION_EDIT, ContactsContract.Contacts.CONTENT_URI);
            intent.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId));
            intent.putExtra("finishActivityOnSaveCompleted", true);
            //add the below line?
            intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivityForResult(intent, 1);
        } catch (Exception e) {
            Toast.makeText(activity, "Oops there was a problem trying to open the contact :(", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Deletes contact by id
     *
     * @param activity
     * @param contactId
     */
    public static void deleteContactById(Activity activity, long contactId) {
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, Long.toString(contactId));
        activity.getContentResolver().delete(uri, null, null);
        Toast.makeText(activity, "Contact Deleted", Toast.LENGTH_LONG).show();
    }

}