package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import com.otpless.dto.OtplessResponse;
import com.otpless.main.OtplessManager;
import com.otpless.main.OtplessView;
import com.otpless.utils.Utility;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    OtplessView otplessView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        otplessView = OtplessManager.getInstance().getOtplessView(this);
        final JSONObject extras = new JSONObject();
        try {
            extras.put("method", "get");
            final JSONObject params = new JSONObject();
            params.put("cid", "HRIRBIIKXMKEOTDDA8VV4HP2V24454X8");
            extras.put("params", params);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        otplessView.setCallback(this::onOtplessCallback, extras);
        otplessView.showOtplessLoginPage(extras, this::onOtplessCallback);
        otplessView.verifyIntent(getIntent());

        // If you are using WHATSAPP login, it's required to add this code to hide the OTPless functionality

        if (Utility.isWhatsAppInstalled(this)) {
            Toast.makeText(this, "WhatsApp is installed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "WhatsApp is not installed", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (otplessView != null) {
            otplessView.verifyIntent(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // make sure you call this code before super.onBackPressed();
        if (otplessView.onBackPressed()) return;
    }

    private void onOtplessCallback(OtplessResponse response) {
        if (response.getErrorMessage() != null) {
// todo error handing
        } else {
            final String token = response.getData().optString("token");
// todo token verification with api
            Log.d("Otpless", "token: " + token);
        }
    }
}