package com.omak.omakhelpers.CallDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.omak.omakhelpers.openIntentUtil;
import com.omak.samplelibrary.R;

public class CallerDialog implements View.OnClickListener {
    Context context;
    String callNumber;
    AlertDialog alert;
    CallerDialogModel callerDialogModel;
    LinearLayout llDialogBoxMain;
    ImageView ivClose;
    TextView tvCallerNameId, tvCallerId, tvViewCall, tvCallTimeAgo, tvStatus;
    Button btn_message, btn_call, btn_whatsapp, btn_edit, btn_save;
    EventListener listener;

    public interface EventListener {
        CallerDialogModel setupCaller(CallerDialogModel callerDialogModel);
        View setCallerDialog(View view);
    }

    public CallerDialog(Context context) {
        this.context = context;
    }

    private CallerDialogModel getCallerDialogModel(String number) {
        callerDialogModel = new CallerDialogModel();
        callerDialogModel.setNumber(number);

        // number = number.replace("+91", "");

        // Check if contact exists
//        if (App.contactExists(context, callNumber)) {
//            callerDialogModel.setContactExists(true);
//        }

//        // Save this to realm if the user is known
//        if (!callerDialogModel.getType().equals("unknown")) {
//            realmHelpers.saveToRealm(callerDialogModel);
//        }

        callerDialogModel = listener.setupCaller(callerDialogModel);

        return  callerDialogModel;
    }

    public void showDialog(String number) {

        this.callNumber = number;
        CallerDialogModel callerDialogModel = getCallerDialogModel(number);

        AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.caller_dialog_popup, null);

        // ALllow apps to override the popup
        dialogView = listener.setCallerDialog(dialogView);

        llDialogBoxMain = dialogView.findViewById(R.id.llDialogBoxMain);
        ivClose = dialogView.findViewById(R.id.ivClose);
        tvCallerId = dialogView.findViewById(R.id.tvCallerId);
        tvCallerNameId = dialogView.findViewById(R.id.tvCallerNameId);
        tvCallTimeAgo = dialogView.findViewById(R.id.tvCallTimeAgo);
        tvStatus = dialogView.findViewById(R.id.tvStatus);
        tvViewCall = dialogView.findViewById(R.id.tvViewCall);

        btn_message = dialogView.findViewById(R.id.btn_message);
        btn_call = dialogView.findViewById(R.id.btn_call);
        btn_whatsapp = dialogView.findViewById(R.id.btn_whatsapp);
        btn_edit = dialogView.findViewById(R.id.btn_edit);
        btn_save = dialogView.findViewById(R.id.btn_save);

        ivClose.setOnClickListener(this);
        tvViewCall.setOnClickListener(this);
        btn_call.setOnClickListener(this);
        btn_message.setOnClickListener(this);
        btn_whatsapp.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        tvCallerId.setText(number);
        tvCallerNameId.setText(callerDialogModel.getName());
        tvViewCall.setText(callerDialogModel.getView_button_text());
        llDialogBoxMain.setBackgroundColor(ContextCompat.getColor(context, callerDialogModel.getBackgroundColor()));

        tvCallTimeAgo.setText(callerDialogModel.getTime());
        tvStatus.setText(callerDialogModel.getStatus());

        // Hide optional elements
        btn_edit.setVisibility(View.GONE);
        tvCallTimeAgo.setVisibility(View.GONE);
        tvStatus.setVisibility(View.GONE);

        if (!callerDialogModel.getTime().isEmpty()) {
            tvCallTimeAgo.setVisibility(View.VISIBLE);
        }
        if (!callerDialogModel.getStatus().isEmpty()) {
            tvStatus.setVisibility(View.VISIBLE);
        }
        if (callerDialogModel.getType().equals("unknown")) {
            tvViewCall.setVisibility(View.GONE);
        }
        if (callerDialogModel.getContactExists()) {
            btn_edit.setVisibility(View.VISIBLE);
            btn_save.setVisibility(View.GONE);
        }
        builder.setView(dialogView);
        alert = builder.create();
        alert.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        alert.setCanceledOnTouchOutside(true);
        alert.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = alert.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setGravity(Gravity.CENTER);
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.horizontalMargin = 10;
        window.setAttributes(lp);
    }



    @Override
    public void onClick(View v) {
        Integer viewId = v.getId();
        if(viewId==R.id.ivClose) {

        }
        else if(viewId==R.id.btn_call) {
            openIntentUtil.openDialer(context, callNumber);
        }
        else if(viewId==R.id.btn_message) {
            openIntentUtil.openSmsApp(context, callNumber, callerDialogModel.getName());
        }
        else if(viewId==R.id.btn_whatsapp) {
            openIntentUtil.openWhatsapp(context, callNumber, callerDialogModel.getName());
        }
        else if(viewId==R.id.btn_save) {
            openIntentUtil.openSaveContact(context, callerDialogModel.getName(), callNumber, "");
        }
        else if(viewId==R.id.btn_edit) {
            openIntentUtil.openContactToEditByNumber(context, callNumber);
        }
        else if(viewId==R.id.tvViewCall) {

        }
    }
}