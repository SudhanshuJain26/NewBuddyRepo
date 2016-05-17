package indwin.c3.shareapp.activities;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.SMSreceiver;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.SendOTP;
import indwin.c3.shareapp.utils.VerifyOTP;
import io.intercom.android.sdk.Intercom;
import io.intercom.com.google.gson.Gson;

public class VerifyOTPActivity extends AppCompatActivity {

    TextView phone;
    SharedPreferences mPrefs;
    Gson gson;
    UserModel user;
    BroadcastReceiver smsRecievedListener;
    EditText otp;
    Button verifyOtp;
    TextView timer;
    public static CountDownTimer time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            TextView headerTitle = (TextView) findViewById(R.id.activity_header);
            headerTitle.setText("Forgot Password");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            ImageView inter = (ImageView) findViewById(R.id.interCom);
            inter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
                        Intercom.client().displayMessageComposer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPrefs = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        gson = new Gson();
        String json = mPrefs.getString("UserObject", "");
        user = gson.fromJson(json, UserModel.class);

        phone = (TextView) findViewById(R.id.phone);
        phone.append(user.getUserId());
        otp = (EditText) findViewById(R.id.enter_otp);
        verifyOtp = (Button) findViewById(R.id.verify_otp);

        smsRecievedListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                String code = intent.getStringExtra(SMSreceiver.CODE);
                if (code.length() == 4) {
                    otp.setText(code);
                    timer.setVisibility(View.GONE);
                    new VerifyOTP(VerifyOTPActivity.this, user.getUserId(), otp.getText().toString()).execute();
                } else {
                    ActivityCompat.requestPermissions(VerifyOTPActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
                }
            }
        };

        timer = (TextView) findViewById(R.id.timer);
        time = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished / 1000));
                timer.setEnabled(false);
            }

            public void onFinish() {
                timer.setText("Resend OTP");
                timer.setEnabled(true);
            }

        }.start();
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendOTP(VerifyOTPActivity.this, user.getUserId(), true).execute();
            }
        });

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new VerifyOTP(VerifyOTPActivity.this, user.getUserId(), otp.getText().toString()).execute();
            }
        });
        otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    verifyOtp.setEnabled(true);
                    verifyOtp.setAlpha(1);
                } else {
                    verifyOtp.setEnabled(false);
                    verifyOtp.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(
                SMSreceiver.INTENT_INFORM_MESSAGE_RECIEVED);
        registerReceiver(smsRecievedListener, filter);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(smsRecievedListener, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(smsRecievedListener);
        } catch (Exception e) {
            Log.e("digo", e.getMessage());
        }
    }

}
