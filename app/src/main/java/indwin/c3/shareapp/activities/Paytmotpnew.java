package indwin.c3.shareapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.SMSreceiver;

public class Paytmotpnew extends AppCompatActivity {
    String name, email, college, fbid = "", phone, truth = "", code;
    BroadcastReceiver smsRecievedListener;
    int w = 0;
    int d = 1;
    private RelativeLayout rwarn;
    private ProgressBar spinner;
    EditText otp1, otp2, otp3, otp4;
    int check = 0, ot_check = 0;
    private int pT, pR, pL, pB;
    private String ref = "";
    private TextView timer, verify;
    SharedPreferences toks;
    String referral_code = "", creditLimit = "", formstatus = "", panoradhar = "", bankaccount = "", collegeid = "", verificationdate = "", rejectionReason = "";
    int l_otp = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paytmotp);
        TextView t = (TextView) findViewById(R.id.buddyLogo);
        SharedPreferences get = getSharedPreferences("cred", Context.MODE_PRIVATE);
        t.setText("To change your password, please verify with OTP first.Your OTP will be sent to +91-" + get.getString("phone_number", ""));
        timer = (TextView) findViewById(R.id.textLook);

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished / 1000));
                timer.setEnabled(false);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {

                timer.setText("Resend Otp");
                timer.setEnabled(true);
                //mTextField.setText("done!");

            }

        }.start();
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   Toast.makeText(Otp.this,"Cool",Toast.LENGTH_LONG).show();
            }
        });
        //countDownTimer = new MalibuCountDownTimer(30000, 1000);
        try {
            spinner = (ProgressBar) findViewById(R.id.progressBar1);
            spinner.setVisibility(View.INVISIBLE);
        }
        catch (Exception e)
        {

        }
        otp1 = (EditText) findViewById(R.id.otp1);
        otp1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    otp1.setHint(R.string.hint1);
                    //  Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                } else {
                    otp1.setHint("Entet OTP");
                    // Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });
        pL = otp1.getPaddingLeft();
        pT = otp1.getPaddingTop();
        pR = otp1.getPaddingRight();
        pB = otp1.getPaddingBottom();


        otp2 = (EditText) findViewById(R.id.otp2);
        otp3 = (EditText) findViewById(R.id.otp3);
        otp4 = (EditText) findViewById(R.id.otp4);
        //        otp1.addTextChangedListener;'
        rwarn = (RelativeLayout) findViewById(R.id.error);
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                otp1.setBackgroundResource(R.drawable.texted);
                otp1.setPadding(pL, pT, pR, pB);
                if (otp1.getText().toString().length() == 1)
                    otp2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otp2.getText().toString().length() == 1)
                    otp3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otp3.getText().toString().length() == 1)
                    otp4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        TextView message = (TextView) findViewById(R.id.buddyLogo);
        smsRecievedListener = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub


                code = intent.getStringExtra(SMSreceiver.CODE);
                System.out.println("got the code information");
                if (code.length() == 4) {
                    try {

                        otp1.setText(String.valueOf(code).charAt(0));
                        otp2.setText(String.valueOf(code.charAt(1)));
                        otp3.setText(String.valueOf(code.charAt(2)));
                        otp4.setText(String.valueOf(code.charAt(3)));
                        SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                        w = sharedpreferences.getInt("shareflow", 1);
                    }
                    catch (Exception e)
                    {}}}};

                    }
}
