package com.omak.omakhelpers.contactUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class ExportDatabaseCSVTask extends AsyncTask<String, String, String> {
    Context context;
    public final ProgressDialog dialog = new ProgressDialog(context);

    @Override
    public void onPreExecute() {
        this.dialog.setMessage("Exporting database...");
        this.dialog.show();
    }

    public String doInBackground(final String... args) {
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }


        File file = new File(exportDir, "ExcelFile.csv");
        try {

            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

            //data
            ArrayList<String> listdata = new ArrayList<String>();
            listdata.add("Aniket");
            listdata.add("Shinde");
            listdata.add("pune");
            listdata.add("anything@anything");
            listdata.add("7508608223");
            //Headers
            String[] arrStr1 = {"First Name", "Last Name", "Address", "Email", "phone"};
            csvWrite.writeNext(arrStr1);

            String[] arrStr = {listdata.get(0), listdata.get(1), listdata.get(2), listdata.get(3), listdata.get(4)};
            csvWrite.writeNext(arrStr);

            csvWrite.close();
            return "";
        } catch (IOException e) {
            Log.e("MainActivity", e.getMessage(), e);
            return "";
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onPostExecute(final String success) {

        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
        if (success.isEmpty()) {
            Toast.makeText(context, "Export successful!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Export failed!", Toast.LENGTH_SHORT).show();
        }
    }
}
