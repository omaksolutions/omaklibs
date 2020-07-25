package com.omak.omakhelpers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.omak.samplelibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * App_Theme_Utils class used in MDC-111 application.
 */
public abstract class Utils {

    /**
     * Set pointer to end of text in edittext when user clicks Next on KeyBoard.
     */
    public static View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (b) {
                HelperFunctions.theLogger("EditText", "will end now");
                ((TextInputEditText) view).setSelection(((TextInputEditText) view).getText().length());
            }
        }
    };

    public static List<String> getUniqueList(List<String> stringList) {
        List<String> uniqueStringList = new ArrayList();
        for (String s : stringList) {
            if (!uniqueStringList.contains(s)) {
                uniqueStringList.add(s);
            }
        }

        return uniqueStringList;
    }

    public static <T extends View> List<T> findViewsWithType(View root, Class<T> type) {
        List<T> views = new ArrayList<>();
        findViewsWithType(root, type, views);
        return views;
    }

    private static <T extends View> void findViewsWithType(View view, Class<T> type, List<T> views) {
        if (type.isInstance(view)) {
            views.add(type.cast(view));
        }

        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                findViewsWithType(viewGroup.getChildAt(i), type, views);
            }
        }
    }

    //  Request Focus Method :-
    public static <T extends View> void setOnFocusListener(View root, Class<T> type) {
        List<T> views = new ArrayList<>();
        findViewsWithType(root, type, views);

        for (T textInputEditText : views) {
            textInputEditText.setOnFocusChangeListener(onFocusChangeListener);
        }
    }

    //Cheking Edit Text field Empty or not :-
    public static <T extends View> boolean is_valid_til(View rootView, Class<T> type) {
        final List<TextInputLayout> textInputLayouts = findViewsWithType(rootView, TextInputLayout.class);

        boolean isValid = true;
        for (TextInputLayout textInputLayout : textInputLayouts) {
            String editTextString = textInputLayout.getEditText().getText().toString();
            if (editTextString.isEmpty()) {
                //textInputLayout.setError(ApplicationHelpers.getResourses().getString(R.string.error_empty_field));
                textInputLayout.setError("Field must not be empty.");
                isValid = false;
            } else {
                textInputLayout.setError(null);
            }
        }

        return isValid;
    }

    public static Spinner setSimpleAdapter(Context context, String[] options, Spinner spinner, Integer chosenPosition) {
        ArrayAdapter leadTypesAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, options);
        leadTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(leadTypesAdapter);
        spinner.setSelection(chosenPosition);

        return spinner;
    }

    public static String bracketize(String string) {
        return "(" + string + ")";
    }

    public static String leftPad(String string) {
        return String.format("%02d", Integer.parseInt(string));
    }

    public static String leftPad(Integer integer) {
        return String.format("%02d", integer);
    }

    public static boolean contactExists(Context context, String number) {
        /// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
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

    // Save contact On Contact List
    public static void openSaveContact(Context context, String contactName, String contactNumber, String contactEmail) {
        Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
        contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        contactIntent.putExtra(ContactsContract.Intents.Insert.NAME, contactName);
        contactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, contactNumber);
        contactIntent.putExtra(ContactsContract.Intents.Insert.EMAIL, contactEmail);
        context.startActivity(contactIntent);
    }

    // method for open sms intent with message id and message body
    public static void openSmsIntent(Context context, String number, String message) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("smsto:" + number));
        intent.putExtra("sms_body", message);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static boolean openWhatsapp(Context context, String number, String name, String message) {
        Boolean success = false;
        try {
            String bodyMessageFormal = "Hello";
            bodyMessageFormal += (name == "") ? "" : " " + name;
            bodyMessageFormal += ",\n\n";
            bodyMessageFormal += (message == "") ? "" : message + "\n\n";

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.putExtra(Intent.EXTRA_TEXT, "");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setType("text/rtf");
            intent.setData(Uri.parse("https://wa.me/" + verifyCountryCode(number) + "/?text=" + bodyMessageFormal));
            context.startActivity(intent);
            success = true;
        } catch (Exception e) {
            HelperFunctions.toaster(context, "Message failed: " + e.getMessage());
        }
        return success;
    }

    public static String verifyCountryCode(String number) {

        number = number.trim();

        if (number.startsWith("+")) return number;

        if (number.length() > 10 && number.startsWith("91")) return "+" + number;

        if (number.length() == 10) return "+91" + number;

        return number;
    }

    // open intent for sharing messages
    private void shareMessage(Context context, String messageBody) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/rtf");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Badiya Khelo, Badiya Jeeto: " + context.getString(R.string.app_name));
        share.putExtra(Intent.EXTRA_TEXT, messageBody);
        //context.startActivity(Intent.createChooser(share, "Share link!"));
        Intent chooserIntent = Intent.createChooser(share, "Share link!");
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(chooserIntent);
    }

}
