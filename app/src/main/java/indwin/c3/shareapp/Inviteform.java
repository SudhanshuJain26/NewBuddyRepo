package indwin.c3.shareapp;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;

public class Inviteform extends AppCompatActivity {

    private Toolbar toolbar;
    private WebView form;
    private ProgressBar spinner;
    String carrierName;
    String osVersion;
    String deviceName;
    private TextView invite,openref;
    private Pattern pattern;
    private Matcher matcher;
    private int pT,pR,pL,pB;
    private int checkname=0,checkemail=0,checkphone=0;
    int an=0,ae=0,ac=0,ap=0;
    private RelativeLayout error;
    private TextView lgin;
    private TextView msg;
    public boolean successAchieved =true;
    Intent intform;  private EditText name,email,phone,ref;
    AutoCompleteTextView college;
int a=0;
    String mName,mEmail,mCollege,mPhone,mRef="",truth="";static String token="";
    Double latitude, longitude;
    String IMEINumber;
    String simSerialNumber;
    Location getLastLocation;
    LocationManager locationManager;
    String status;
    int pageCode;
    String[] messages;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    boolean otpsend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inviteform);
        if(checkAndRequestPermissions()) {
//            try {
//                locationManager = (LocationManager) getSystemService
//                        (Context.LOCATION_SERVICE);
//                GPSTracker gps = new GPSTracker(this);
//                if(gps.canGetLocation()){ // gps enabled} // return boolean true/false
//                    latitude = gps.getLatitude();
//                    longitude = gps.getLongitude();
//
//                }else{
//
//                    getLastLocation = locationManager.getLastKnownLocation
//                            (LocationManager.PASSIVE_PROVIDER);
//
//                    latitude = getLastLocation.getLatitude();
//                    longitude = getLastLocation.getLongitude();
//                }
//            }
//            catch (Exception e)
//            {
//                System.out.println(e.toString());
//            }
//            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//            IMEINumber = telephonyManager.getDeviceId();
//            simSerialNumber = telephonyManager.getSimSerialNumber();
//            carrierName = telephonyManager.getNetworkOperatorName();
//            deviceName = AppUtils.getDeviceName();
//            osVersion = android.os.Build.VERSION.RELEASE;
        }

        error=(RelativeLayout)findViewById(R.id.error);
        msg=(TextView)findViewById(R.id.msg);
        spinner=(ProgressBar)findViewById(R.id.progressBar1);
        name=(EditText)findViewById(R.id.name);
        name.setImeOptions(EditorInfo.IME_ACTION_NEXT);
try{
    a=getIntent().getExtras().getInt("login");
}
catch(Exception e){}
        email=(EditText)findViewById(R.id.email);
        email.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        ref=(EditText)findViewById(R.id.ref);
       // college=(AutoCompleteTextView)findViewById(R.id.college);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, colleges);
        phone=(EditText)findViewById(R.id.phone);

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    name.setHint(R.string.h4);
                    //  Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                } else {
                    name.setHint("Name");
                    // Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    email.setHint(R.string.h5);
                    //  Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                } else {
                    email.setHint("Email");
                    // Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    phone.setHint(R.string.h2);
                    //  Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                } else {
                    phone.setHint("Phone Number");
                    // Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });
        ref.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ref.setHint(R.string.h7);
                    //  Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                } else {
                    ref.setHint("Referral Code");
                    // Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });



        pL=name.getPaddingLeft();
        pR=name.getPaddingRight();
         pT = name.getPaddingTop();
         pB = name.getPaddingBottom();
//        phone.setImeOptions(EditorInfo.IME_ACTION_);
        openref=(TextView)findViewById(R.id.have);
        invite=(TextView)findViewById(R.id.invite);
        invite.setEnabled(false);
       // username.addTextChangedListener(mTextEditorWatcher);


        final TextWatcher mTextEditorWatcher1 = new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                error.setVisibility(View.INVISIBLE);
                name.setBackgroundResource(R.drawable.texted);
                name.setPadding(pL, pT, pR, pB);
                phone.setBackgroundResource(R.drawable.texted);
                phone.setPadding(pL, pT, pR, pB);
                email.setBackgroundResource(R.drawable.texted);
                email.setPadding(pL, pT, pR, pB);

               ref.setBackgroundResource(R.drawable.texted);
                ref.setPadding(pL, pT, pR, pB);
                //  textview.setText(String.valueOf(s.length());
                if(s.length()>0)
                    checkname=1;
                else
                    checkname=0;
                if((checkname==1)&&(checkemail==1)&&(checkphone==1)){
                    invite.setEnabled(true);
                    invite.setTextColor(Color.parseColor("#ffffff"));}
                else{
                    invite.setTextColor(Color.parseColor("#66ffffff"));
                    invite.setEnabled(false);}


            }

            public void afterTextChanged(Editable s) {
            }
        };
        name.addTextChangedListener(mTextEditorWatcher1);


        final TextWatcher mTextEditorWatcher2 = new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                //  textview.setText(String.valueOf(s.length());
                error.setVisibility(View.INVISIBLE);
                name.setBackgroundResource(R.drawable.texted);
                name.setPadding(pL, pT, pR, pB);
                phone.setBackgroundResource(R.drawable.texted);
                phone.setPadding(pL, pT, pR, pB);
                email.setBackgroundResource(R.drawable.texted);
                email.setPadding(pL, pT, pR, pB);
                ref.setBackgroundResource(R.drawable.texted);
                ref.setPadding(pL, pT, pR, pB);

                if(s.length()>0)
                    checkemail=1;
                else
                    checkemail=0;
                if((checkname==1)&&(checkemail==1)&&(checkphone==1))
                {invite.setEnabled(true);
                    invite.setTextColor(Color.parseColor("#ffffff"));}
                else{
                    invite.setTextColor(Color.parseColor("#66ffffff"));
                    invite.setEnabled(false);}


            }

            public void afterTextChanged(Editable s) {
            }
        };
        email.addTextChangedListener(mTextEditorWatcher2);


        final TextWatcher mTextEditorWatcher3 = new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                //  textview.setText(String.valueOf(s.length());
                error.setVisibility(View.INVISIBLE);
                name.setBackgroundResource(R.drawable.texted);
                name.setPadding(pL, pT, pR, pB);
                phone.setBackgroundResource(R.drawable.texted);
                phone.setPadding(pL, pT, pR, pB);
                email.setBackgroundResource(R.drawable.texted);
                email.setPadding(pL, pT, pR, pB);
                ref.setBackgroundResource(R.drawable.texted);
                ref.setPadding(pL, pT, pR, pB);

                if(s.length()==10)
                    checkphone=1;
                else
                    checkphone=0;
                if((checkname==1)&&(checkemail==1)&&(checkphone==1))
                {
                    invite.setEnabled(true);
                    invite.setTextColor(Color.parseColor("#ffffff"));}
                else{
                    invite.setTextColor(Color.parseColor("#66ffffff"));
        invite.setEnabled(false);}

            }

            public void afterTextChanged(Editable s) {
            }
        };
        phone.addTextChangedListener(mTextEditorWatcher3);

        openref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openref.setVisibility(View.INVISIBLE);
                ref.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout rPage=(RelativeLayout)findViewById(R.id.relative);
        rPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ref.getText().toString().trim().length()==0)
                {ref.setVisibility(View.INVISIBLE);
                openref.setVisibility(View.VISIBLE);}

            }
        });


        //ref.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//        college.setAdapter(adapter);

        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);


        } catch (Exception e) {
            System.out.println(e.toString() + "digo");
        }
        lgin = (TextView) findViewById(R.id.lgin);


        lgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(Inviteform.this, MainActivity.class);
                startActivity(in);
                overridePendingTransition(0, 0);
                finish();
            }
        });

//        int res1 = ContextCompat.checkSelfPermission(Inviteform.this,Manifest.permission.ACCESS_FINE_LOCATION);
//        if(res1 ==PackageManager.PERMISSION_GRANTED){
//            locationManager = (LocationManager) getSystemService
//                    (Context.LOCATION_SERVICE);
//            getLastLocation = locationManager.getLastKnownLocation
//                    (LocationManager.PASSIVE_PROVIDER);
//            latitude = getLastLocation.getLatitude();
//            longitude  = getLastLocation.getLongitude();
//
//        }else {
//            ActivityCompat.requestPermissions(Inviteform.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
//        }



        int res= ContextCompat.checkSelfPermission(Inviteform.this, Manifest.permission.RECEIVE_SMS);

        if(res== PackageManager.PERMISSION_GRANTED){
                invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                invite.setTextColor(Color.parseColor("#ffffff"));
                try { mName=name.getText().toString().trim();
                     String EMAIL_PATTERN =
                            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";



                    mRef=ref.getText().toString().trim();
                    mEmail=email.getText().toString().trim();
                    pattern = Pattern.compile(EMAIL_PATTERN);
                    matcher = pattern.matcher(mEmail);
                    Boolean checkemail=matcher.matches();


                    mPhone=phone.getText().toString().trim();

                    SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                    SharedPreferences userid = getSharedPreferences("cred", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userid.edit();
                    editor.putString("phone_number", mPhone);

                    editor.commit();
                    try {
          //
          //              Intercom.client().reset();
                    }
                    catch (Exception e)
                    {System.out.println(e.toString()+"int inv");}
                    try {
                        Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
                        Intercom.client().registerIdentifiedUser(
                                new Registration().withUserId(mPhone));
                    }
                    catch (Exception e)
                    {
                        System.out.println("Intercom one"+e.toString());
                    }
                    int chckphn=0;
                    for(int j=1;j<mPhone.length();j++)
                    {
                        if((mPhone.charAt(j)>='0')&&(mPhone.charAt(j)<='9'))
                            chckphn=1;
                        else {
                         chckphn=0;
                            break;
                        }
                    }
                    if((chckphn==1)&&(mPhone.charAt(0)>='1')&&(mPhone.charAt(0)<='9'))
                        chckphn=1;
                    else
                    chckphn=0;


                    if(mName.length()!=0)
                        an=1;
                    if((mEmail.length()!=0)&&(checkemail))
                        ae=1;
                    if((mPhone.length()==10)&&(chckphn)==1)
                        ap=1;
                        ac=1;
                    if((an==1)&&(ae==1)&&(ap==1)&&(ac==1))

                    {
                        try{
                        Map userMap = new HashMap<>();
                        userMap.put("name", mName);
                        userMap.put("email", mEmail);
                        if(mRef.trim().length()>0)
                        userMap.put("referralCode",mRef.trim());
                        userMap.put("user_id", mPhone);
                        userMap.put("phone", mPhone);
                            System.out.println("Intercom data 4" + mPhone);
                        Intercom.client().updateUser(userMap);}
                        catch (Exception e)
                        {
                            System.out.println("Intercom two"+e.toString());
                        }
                        SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedpreferences.edit();
                        editor1.putInt("shareflow",0);
                        editor1.commit();
                        Long time= Calendar.getInstance().getTimeInMillis()/1000;
                        Long oldtime=userP.getLong("expires",0);
//        Toast.makeText(FacebookAuth.this, String.valueOf(oldtime), Toast.LENGTH_SHORT).show();
                        if(time+5<userP.getLong("expires",0)) {
                            if (!otpsend) {
                                new sendOtp().execute();

                            }
                        }
                        else
                            new AuthTokc().execute();
//                        new sendOtp().execute();
                        openref.setEnabled(false);
                        lgin.setEnabled(false);}
                    else
                        if(!checkemail)
                        {
                            error.setVisibility(View.VISIBLE);
//                            name.setBackgroundResource(R.drawable.texted);
//                            name.setPadding(pL, pT, pR, pB);
//                            phone.setBackgroundResource(R.drawable.texted);
//                            phone.setPadding(pL, pT, pR, pB);
                            email.setBackgroundResource(R.drawable.texted2);
                            email.setPadding(pL, pT, pR, pB);
                            invite.setEnabled(true);
                            msg.setText("Please enter a valid email-id!");
                            invite.setTextColor(Color.parseColor("#66ffffff"));
//                            Toast.makeText(Inviteform.this,"Please enter a valid email-id!",Toast.LENGTH_LONG).show();

                        }
                    else {
                            invite.setEnabled(true);
                            phone.setBackgroundResource(R.drawable.texted2);
                            phone.setPadding(pL, pT, pR, pB);
                            spinner.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);
                            msg.setText("Please Enter the correct details!");
                            invite.setTextColor(Color.parseColor("#66ffffff"));
                        }

//

                }
                catch(Exception e) {
                    //           Toast.makeText(Inviteform.this,e.toString(),Toast.LENGTH_LONG).show();
                    // }
                    invite.setEnabled(true);
                    System.out.println("Error"+e.toString());
                }



                //                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      startActivity(intent);
            }
        });}
        else
        {
            ActivityCompat.requestPermissions(Inviteform.this, new String[]{Manifest.permission.RECEIVE_SMS,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE}, 1);
        }


    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case 1:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    invite.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            invite.setTextColor(Color.parseColor("#ffffff"));
//                            try {
//                                mName = name.getText().toString().trim();
//
//                                String EMAIL_PATTERN =
//                                        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
//                                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//
//
//
//
//                                mRef=ref.getText().toString().trim();
//                                mEmail=email.getText().toString().trim();
//                                pattern = Pattern.compile(EMAIL_PATTERN);
//                                matcher = pattern.matcher(mEmail);
//                                Boolean checkemail=matcher.matches();
//
//
//                                mPhone=phone.getText().toString().trim();
//
//
//                                SharedPreferences userP = getSharedPreferences("buddyin", Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = userP.edit();
//                                editor.putString("name", mPhone);
//
//                                editor.commit();
//                                try {
//                                    //
//                                    //              Intercom.client().reset();
//
//                                } catch (Exception e) {
//                                    System.out.println(e.toString() + "int inv");
//                                }
//
//                                try {
//                                    Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
//                                    Intercom.client().registerIdentifiedUser(
//                                            new Registration().withUserId(mPhone));
//
//                                } catch (Exception e) {
//                                    System.out.println("Intercom one" + e.toString());
//                                }
//                                int chckphn = 0;
//                                for (int j = 1; j < mPhone.length(); j++) {
//                                    if ((mPhone.charAt(j) >= '0') && (mPhone.charAt(j) <= '9'))
//                                        chckphn = 1;
//                                    else {
//                                        chckphn = 0;
//                                        break;
//                                    }
//                                }
//                                if ((chckphn == 1) && (mPhone.charAt(0) >= '1') && (mPhone.charAt(0) <= '9'))
//                                    chckphn = 1;
//                                else
//                                    chckphn = 0;
//
//
//                                if (mName.length() != 0)
//                                    an = 1;
//                                if ((mEmail.length() != 0) && (checkemail))
//                                    ae = 1;
//                                if ((mPhone.length() == 10) && (chckphn) == 1)
//                                    ap = 1;
//                                ac = 1;
//                                if ((an == 1) && (ae == 1) && (ap == 1) && (ac == 1))
//
//                                {
//                                    try {
//
//                                        Map userMap = new HashMap<>();
//                                        userMap.put("name", mName);
//                                        userMap.put("email", mEmail);
//                                        userMap.put("user_id", mPhone);
//                                        userMap.put("phone", mPhone);
//                                        System.out.println("Intercom data 4" + mPhone);
//
//                                        Intercom.client().updateUser(userMap);
//                                    } catch (Exception e) {
//                                        System.out.println("Intercom two" + e.toString());
//
//                                    }
//                                    SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor1 = sharedpreferences.edit();
//                                    editor1.putInt("shareflow", 0);
//                                    editor1.commit();
////<<<<<<< HEAD
//                               //     new sendOtp().execute();
//                                    Long time= Calendar.getInstance().getTimeInMillis()/1000;
//                                    Long oldtime=userP.getLong("expires",0);
////        Toast.makeText(FacebookAuth.this, String.valueOf(oldtime), Toast.LENGTH_SHORT).show();
//                                    if(time+5<userP.getLong("expires",0))
//
//                                        new sendOtp().execute();
//                                    else
//                                        new AuthTokc().execute();
//                                    openref.setEnabled(false);
//
//                                    lgin.setEnabled(false);}
//                                else
//                                if(!checkemail) {
//
//                                    error.setVisibility(View.VISIBLE);
//                                    invite.setEnabled(true);
//                                    spinner.setVisibility(View.GONE);
////                                    name.setBackgroundResource(R.drawable.texted);
////                                    name.setPadding(pL, pT, pR, pB);
////                                    phone.setBackgroundResource(R.drawable.texted);
////                                    phone.setPadding(pL, pT, pR, pB);
//                                    email.setBackgroundResource(R.drawable.texted2);
//                                    email.setPadding(pL, pT, pR, pB);
//                                    msg.setText("Please enter a valid email-id!");
//
//                                   // Toast.makeText(Inviteform.this, "Please enter a valid email-id!", Toast.LENGTH_LONG).show();
//                                    invite.setTextColor(Color.parseColor("#66ffffff"));
//                                }
//                                else{
//                                    invite.setEnabled(true);
////                                    name.setBackgroundResource(R.drawable.texted);
////                                    name.setPadding(pL, pT, pR, pB);
//                                    phone.setBackgroundResource(R.drawable.texted2);
//                                    phone.setPadding(pL, pT, pR, pB);
////                                    email.setBackgroundResource(R.drawable.texted);
////                                    email.setPadding(pL, pT, pR, pB);
//                                    error.setVisibility(View.VISIBLE);
//                                    msg.setText("Please Enter the correct details!");
////                                    Toast.makeText(Inviteform.this,"Please Enter the correct details!",Toast.LENGTH_LONG).show();
//
//                                    invite.setTextColor(Color.parseColor("#66ffffff"));}
//
////
//                            }
//                            catch(Exception e) {
//                                //           Toast.makeText(Inviteform.this,e.toString(),Toast.LENGTH_LONG).show();
//                                // }
//                                System.out.println("Error"+e.toString());
//                            }
//
//
//
//                            //                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      startActivity(intent);
//                        }
//                    });
//                }
//                else
//                {
//                    invite.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        invite.setTextColor(Color.parseColor("#ffffff"));
//                        try { mName=name.getText().toString().trim();
//                            String EMAIL_PATTERN =
//                                    "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
//                                            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//
//
//
//                            mRef=ref.getText().toString().trim();
//                            mEmail=email.getText().toString().trim();
//                            pattern = Pattern.compile(EMAIL_PATTERN);
//                            matcher = pattern.matcher(mEmail);
//                            Boolean checkemail=matcher.matches();
//
//
//                            mPhone=phone.getText().toString().trim();
//
//                            SharedPreferences userP = getSharedPreferences("buddyin", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = userP.edit();
//                            editor.putString("name", mPhone);
//
//                            editor.commit();
//                            try {
//                                //
//                                //              Intercom.client().reset();
//                            }
//                            catch (Exception e)
//                            {System.out.println(e.toString()+"int inv");}
//                            try {
//                                Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
//                                Intercom.client().registerIdentifiedUser(
//                                        new Registration().withUserId(mPhone));
//                            }
//                            catch (Exception e)
//                            {
//                                System.out.println("Intercom one"+e.toString());
//                            }
//                            int chckphn=0;
//                            for(int j=1;j<mPhone.length();j++)
//                            {
//                                if((mPhone.charAt(j)>='0')&&(mPhone.charAt(j)<='9'))
//                                    chckphn=1;
//                                else {
//                                    chckphn=0;
//                                    break;
//                                }
//                            }
//                            if((chckphn==1)&&(mPhone.charAt(0)>='1')&&(mPhone.charAt(0)<='9'))
//                                chckphn=1;
//                            else
//                                chckphn=0;
//
//
//                            if(mName.length()!=0)
//                                an=1;
//                            if((mEmail.length()!=0)&&(checkemail))
//                                ae=1;
//                            if((mPhone.length()==10)&&(chckphn)==1)
//                                ap=1;
//                            ac=1;
//                            if((an==1)&&(ae==1)&&(ap==1)&&(ac==1))
//
//                            {
//                                try{
//                                    Map userMap = new HashMap<>();
//                                    userMap.put("name", mName);
//                                    userMap.put("email", mEmail);
//                                    userMap.put("user_id", mPhone);
//                                    userMap.put("phone", mPhone);
//                                    System.out.println("Intercom data 4" + mPhone);
//                                    Intercom.client().updateUser(userMap);}
//                                catch (Exception e)
//                                {
//                                    System.out.println("Intercom two"+e.toString());
//                                }
//                                SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor1 = sharedpreferences.edit();
//                                editor1.putInt("shareflow",0);
//                                editor1.commit();
//                                Long time= Calendar.getInstance().getTimeInMillis()/1000;
//                                Long oldtime=userP.getLong("expires",0);
////        Toast.makeText(FacebookAuth.this, String.valueOf(oldtime), Toast.LENGTH_SHORT).show();
//                                if(time+5<userP.getLong("expires",0))
//                                    new sendOtp().execute();
//                                else
//                                    new AuthTokc().execute();
//                                openref.setEnabled(false);
//                                lgin.setEnabled(false);}
//                            else
//                            if(!checkemail)
//                            {
//                                invite.setEnabled(true);
//                                error.setVisibility(View.VISIBLE);
////                                name.setBackgroundResource(R.drawable.texted);
////                                name.setPadding(pL, pT, pR, pB);
////                                phone.setBackgroundResource(R.drawable.texted);
////                                phone.setPadding(pL, pT, pR, pB);
//
//                                email.setBackgroundResource(R.drawable.texted2);
//                                email.setPadding(pL, pT, pR, pB);
//                                msg.setText("Please enter a valid email-id!");
//                                invite.setTextColor(Color.parseColor("#66ffffff"));
////                                Toast.makeText(Inviteform.this,"Please enter a valid email-id!",Toast.LENGTH_LONG).show();
//                            }
//                            else{invite.setTextColor(Color.parseColor("#66ffffff"));
//                                error.setVisibility(View.VISIBLE);
//                                invite.setEnabled(true);
//                                msg.setText("Please Enter the correct details!");
////                                name.setBackgroundResource(R.drawable.texted);
////                                name.setPadding(pL, pT, pR, pB);
//                                phone.setBackgroundResource(R.drawable.texted2);
//                                phone.setPadding(pL, pT, pR, pB);
////                                email.setBackgroundResource(R.drawable.texted);
////                                email.setPadding(pL, pT, pR, pB);
//                                invite.setTextColor(Color.parseColor("#66ffffff"));
////                                Toast.makeText(Inviteform.this,"Please Enter the correct details!",Toast.LENGTH_LONG).show();
//                            }
//
//
////
//                        }
//                        catch(Exception e) {
//                            invite.setEnabled(true);
//                            //           Toast.makeText(Inviteform.this,e.toString(),Toast.LENGTH_LONG).show();
//                            // }
//                            System.out.println("Error"+e.toString());
//                        }
//
//
//
//                        //                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      startActivity(intent);
//                    }
//                });}
//                break;
//
//
//        }}

                    @Override
    public void onBackPressed() {
        if(a==1)
        {
            Intent in=new Intent(Inviteform.this,Landing.class);
            finish();
            startActivity(in);
            overridePendingTransition(0, 0);
        }
else{
            Intent in=new Intent(Inviteform.this,Landing.class);
            finish();
            startActivity(in);
            overridePendingTransition(0, 0);}

    }

    private static final String[] colleges=new String[]{};

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(Inviteform.this,
                Manifest.permission.READ_PHONE_STATE);
        int locationPermission = ContextCompat.checkSelfPermission(Inviteform.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int readSmsPermission = ContextCompat.checkSelfPermission(Inviteform.this,Manifest.permission.READ_SMS);
//        int contactsPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (readSmsPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }

//        if (readSmsPermission != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
//        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(Inviteform.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("TAG", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE,PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);


                    // Check for both permissions
                    if (perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_PHONE_STATE)==PackageManager.PERMISSION_GRANTED) {
                        Log.d("TAG", "sms & location services permission granted");
                        invite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                invite.setEnabled(false);
                                invite.setTextColor(Color.parseColor("#ffffff"));
                                try {
                                    mName = name.getText().toString().trim();

                                    String EMAIL_PATTERN =
                                            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";




                                    mRef=ref.getText().toString().trim();
                                    mEmail=email.getText().toString().trim();
                                    pattern = Pattern.compile(EMAIL_PATTERN);
                                    matcher = pattern.matcher(mEmail);
                                    Boolean checkemail=matcher.matches();


                                    mPhone=phone.getText().toString().trim();


                                    SharedPreferences userP = getSharedPreferences("buddyin", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = userP.edit();
                                    editor.putString("name", mPhone);

                                    editor.commit();
                                    try {
                                        //
                                        //              Intercom.client().reset();

                                    } catch (Exception e) {
                                        System.out.println(e.toString() + "int inv");
                                    }

                                    try {
                                        Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
                                        Intercom.client().registerIdentifiedUser(
                                                new Registration().withUserId(mPhone));

                                    } catch (Exception e) {
                                        System.out.println("Intercom one" + e.toString());
                                    }
                                    int chckphn = 0;
                                    for (int j = 1; j < mPhone.length(); j++) {
                                        if ((mPhone.charAt(j) >= '0') && (mPhone.charAt(j) <= '9'))
                                            chckphn = 1;
                                        else {
                                            chckphn = 0;
                                            break;
                                        }
                                    }
                                    if ((chckphn == 1) && (mPhone.charAt(0) >= '1') && (mPhone.charAt(0) <= '9'))
                                        chckphn = 1;
                                    else
                                        chckphn = 0;


                                    if (mName.length() != 0)
                                        an = 1;
                                    if ((mEmail.length() != 0) && (checkemail))
                                        ae = 1;
                                    if ((mPhone.length() == 10) && (chckphn) == 1)
                                        ap = 1;
                                    ac = 1;
                                    if ((an == 1) && (ae == 1) && (ap == 1) && (ac == 1))

                                    {
                                        try {

                                            Map userMap = new HashMap<>();
                                            userMap.put("name", mName);
                                            userMap.put("email", mEmail);
                                            userMap.put("user_id", mPhone);
                                            userMap.put("phone", mPhone);
                                            System.out.println("Intercom data 4" + mPhone);

                                            Intercom.client().updateUser(userMap);
                                        } catch (Exception e) {
                                            System.out.println("Intercom two" + e.toString());

                                        }
                                        SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor1 = sharedpreferences.edit();
                                        editor1.putInt("shareflow", 0);
                                        editor1.commit();
//<<<<<<< HEAD
                                        //     new sendOtp().execute();
                                        Long time= Calendar.getInstance().getTimeInMillis()/1000;
                                        Long oldtime=userP.getLong("expires",0);
//        Toast.makeText(FacebookAuth.this, String.valueOf(oldtime), Toast.LENGTH_SHORT).show();
                                        if(time+5<userP.getLong("expires",0))

                                            new sendOtp().execute();
                                        else
                                            new AuthTokc().execute();
                                        openref.setEnabled(false);

                                        lgin.setEnabled(false);}
                                    else
                                    if(!checkemail) {

                                        error.setVisibility(View.VISIBLE);
                                        invite.setEnabled(true);
                                        spinner.setVisibility(View.GONE);
//                                    name.setBackgroundResource(R.drawable.texted);
//                                    name.setPadding(pL, pT, pR, pB);
//                                    phone.setBackgroundResource(R.drawable.texted);
//                                    phone.setPadding(pL, pT, pR, pB);
                                        email.setBackgroundResource(R.drawable.texted2);
                                        email.setPadding(pL, pT, pR, pB);
                                        msg.setText("Please enter a valid email-id!");

                                        // Toast.makeText(Inviteform.this, "Please enter a valid email-id!", Toast.LENGTH_LONG).show();
                                        invite.setTextColor(Color.parseColor("#66ffffff"));
                                    }
                                    else{
                                        invite.setEnabled(true);
//                                    name.setBackgroundResource(R.drawable.texted);
//                                    name.setPadding(pL, pT, pR, pB);
                                        phone.setBackgroundResource(R.drawable.texted2);
                                        phone.setPadding(pL, pT, pR, pB);
//                                    email.setBackgroundResource(R.drawable.texted);
//                                    email.setPadding(pL, pT, pR, pB);
                                        error.setVisibility(View.VISIBLE);
                                        msg.setText("Please Enter the correct details!");
//                                    Toast.makeText(Inviteform.this,"Please Enter the correct details!",Toast.LENGTH_LONG).show();

                                        invite.setTextColor(Color.parseColor("#66ffffff"));}

//
                                }
                                catch(Exception e) {
                                    //           Toast.makeText(Inviteform.this,e.toString(),Toast.LENGTH_LONG).show();
                                    // }
                                    System.out.println("Error"+e.toString());
                                }



                                //                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      startActivity(intent);
                            }
                        });
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("TAG", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.

                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false

                    }
                }
            }
        }
    }

    private class ItemsByKeyword extends
            AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {


            // String urldisplay = data[0];
            // HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST
                //      payload.put("userid", details.get("userid"));
                // payload.put("productid", details.get("productid"));
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);


                HttpPost httppost = new HttpPost(BuildConfig.SERVER_URL+"authenticate");

                httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");

                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");
                if (response.getStatusLine().getStatusCode() != 200) {

                    Log.e("MeshCommunication", "Server returned code "
                            + response.getStatusLine().getStatusCode());
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);

                    if (resp.getString("status").contains("fail")) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {
                        token = resp.getString("token");
                        SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userP.edit();
                        editor.putString("tok", token);

                        editor.commit();
                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror111", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {
            invite.setEnabled(true);
            spinner.setVisibility(View.GONE);
            if (result.contains("fail")) {

                // populateUserDetails();

                Toast.makeText(getApplicationContext(),
                        "Something's Wrong! Please try again!",
                        Toast.LENGTH_LONG).show();

            } else {



            }

        }
    }


    public  class AuthTokc extends
            AsyncTask<String, Void, String> {

        private String apiN="";

        //        Context context;
//        AuthTok(Context context) {
//            this.context = context;
//        }
        //    Splash obj=new Splash();
        @Override
        protected String doInBackground(String... params) {
            JSONObject payload = new JSONObject();
//            String urldisplay = params[0];
//            apiN=urldisplay;
            try {

                // userid=12&productid=23&action=add
                // TYPE: POST
                //      payload.put("userid", details.get("userid"));
                // payload.put("productid", details.get("productid"));
                // payload.put("action", details.get("action"));


                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);

                String urll=BuildConfig.SERVER_URL + "authenticate";

                HttpPost httppost = new HttpPost(urll);
                httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");

                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");
                if (response.getStatusLine().getStatusCode() != 200) {

                    Log.e("MeshCommunication", "Server returned code "
                            + response.getStatusLine().getStatusCode());
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);

                    if (resp.getString("status").contains("fail")) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {

                        String token1 = "";


                        SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorP = userP.edit();
                        token1 = resp.getString("token");

                        editorP.putString("token_value", token1);

                        editorP.putLong("expires", resp.getLong("expiresAt"));
                        editorP.commit();
                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror111", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {
            if(result.equals("win")){

               // Toast.makeText(Inviteform.this, "yey", Toast.LENGTH_SHORT).show();
//            next.new FacebookAuth.fblogin().execute();
                // new fblogin().execute();
                new sendOtp().execute();
//            next.fblogin().execute();




            }}}
    private class sendOtp extends
            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
            otpsend = true;
        }

        @Override
        protected String doInBackground(String... data) {

            // String urldisplay = data[0];
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST
                SharedPreferences red = getSharedPreferences("Referral", Context.MODE_PRIVATE);

                String referralinst=red.getString("referrer","");
                payload.put("name",mName);
                payload.put("email", mEmail);
               // payload.put("college", mCollege);
                if(mRef.trim().length()>0)
                    payload.put("refCode",mRef);
                payload.put("phone", mPhone);
                payload.put("offlineForm",false);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceID",IMEINumber);
                jsonObject.put("SIM",simSerialNumber);
                jsonObject.put("connectionType","mobile");
                jsonObject.put("deviceCarrier",carrierName);
                jsonObject.put("osVersion", osVersion);
                jsonObject.put("deviceModel",deviceName);
                jsonObject.put("userAgent","android");
                String versionCode = BuildConfig.VERSION_NAME;
                jsonObject.put("appVersion",versionCode);

                payload.put("deviceData",jsonObject);

                JSONObject jsonObject1 = new JSONObject();
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("lat",latitude);
                jsonObject2.put("long",longitude);
                jsonObject1.put("location",jsonObject2);
                jsonObject1.put("client","android");
                jsonObject1.put("timestamp",Splash.dateStamp);
                payload.put("sessionData",jsonObject1);


                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                //api/login/sendotp

                String url2 = BuildConfig.SERVER_URL+"api/login/signup";
                HttpPost httppost = new HttpPost(url2);
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp=toks.getString("token_value","");

                httppost.setHeader("x-access-token", tok_sp);
                httppost.setHeader("Content-Type", "application/json");


                StringEntity entity = new StringEntity(payload.toString());

                httppost.setEntity(entity);
                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");
                if (response.getStatusLine().getStatusCode() != 200) {

                    Log.e("MeshCommunication", "Server returned code "
                            + response.getStatusLine().getStatusCode());
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);

                    if (resp.getString("status").contains("error")) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return resp.getString("msg");
                    } else {

                        truth=resp.getString("status");

                        return truth;

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";


            }
        }

        protected void onPostExecute(String result) {
            invite.setEnabled(true);
            openref.setEnabled(true);
            lgin.setEnabled(true);

            spinner.setVisibility(View.GONE);

            if (result.equals("success")) {

                invite.setEnabled(false);
                SharedPreferences cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
                SharedPreferences.Editor edc = cred.edit();
                edc.putString("phone_number", mPhone);
                edc.commit();
                new GetMessage().execute(BuildConfig.SERVER_URL+"api/v1/user/messages?userid="+mPhone);
                SharedPreferences mPrefs = getSharedPreferences("buddy", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                UserModel user = new UserModel();
                user.setUserId(mPhone);
                user.setName(mName);
                user.setEmail(mEmail);
                String json = gson.toJson(user);
                mPrefs.edit().putString("UserObject", json).apply();
                if (truth.equals("success") && successAchieved) {
                    Intent inotp = new Intent(Inviteform.this, Otp.class);
//                    finish();

//                    inotp.putExtra("Name", mName);
//                    inotp.putExtra("Email",email.getText().toString());
//                    inotp.putExtra("College",college.getText().toString());
                    inotp.putExtra("Phone", phone.getText().toString().trim());

                inotp.putExtra("Ref",mRef);
                successAchieved = false;

                startActivity(inotp);
                overridePendingTransition(0,0);
}






        }  else{


                invite.setEnabled(true);
                spinner.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
                msg.setText(result);

                if(result.contains("Phone"))
//                    name.setBackgroundResource(R.drawable.texted);
//                name.setPadding(pL, pT, pR, pB);
                {phone.setBackgroundResource(R.drawable.texted2);
                phone.setPadding(pL, pT, pR, pB);}
                else
                if(result.contains("Email"))
//                    name.setBackgroundResource(R.drawable.texted);
//                name.setPadding(pL, pT, pR, pB);
                {email.setBackgroundResource(R.drawable.texted2);
                    email.setPadding(pL, pT, pR, pB);}

                else
                {ref.setBackgroundResource(R.drawable.texted2);
                    ref.setPadding(pL, pT, pR, pB);}




//                email.setBackgroundResource(R.drawable.texted);
//                email.setPadding(pL, pT, pR, pB);

//            Toast.makeText(getApplicationContext(),
//                    result,
//                    Toast.LENGTH_LONG).show();

            }
        }

        public class GetMessage extends AsyncTask<String,Void,String>{

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                String comma = ",";
                String stop = ".";
                messages= new String [2];
                if(s!=null) {
                    messages[0] = s.substring(s.indexOf(comma) + 1, s.lastIndexOf(stop));
                    messages[1] = s.substring(s.lastIndexOf(stop)+1);
                    SharedPreferences preferences = getSharedPreferences("message",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("message0",messages[0]);
                    editor.putString("message1",messages[1]);
                    editor.apply();

                    if (messages[1].equals("Start Now") || messages[1].equals("Verify Now") || messages[1].equals("Complete it now") || messages[1].equals("Apply Now") || messages[1].equals("Find out more")) {
                        pageCode = 1;
                    } else if (messages[1].equals("Okay")) {
                        pageCode = 2;
                    } else if (messages[1].equals("Repay Now")) {
                        pageCode = 3;
                    } else if (messages[1].equals("Talk to us")) {
                        pageCode = 4;
                    }
                    editor.putInt("pageCode",pageCode);
                    editor.putString("status",status);
                    editor.apply();
                }else{
                    messages[0] = "";
                    messages[1] = "";
                }
            }

            @Override
            protected String doInBackground(String... params) {
                String url = params[0];

                try {
                    SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                    String tok_sp = toks.getString("token_value", "");
                    // String tok_sp = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjY1M2M2YTUwZTQzNzgyNjc0M2YyNjYiLCJuYW1lIjoiYnVkZHkgYXBpIGFkbWluIiwidXNlcm5hbWUiOiJidWRkeWFwaWFkbWluIiwicGFzc3dvcmQiOiJtZW1vbmdvc2gxIiwiZW1haWwiOiJjYXJlQGhlbGxvYnVkZHkuaW4iLCJpYXQiOjE0NjU1NDQwMDgsImV4cCI6MTQ2NTU4MDAwOH0.ZpAwCEB0lYSqiYdfaBYjnBJOXfGrqE9qN8USoRzWR8g";
                    HttpResponse response = AppUtils.connectToServerGet(url, tok_sp, null);
                    if (response != null) {
                        HttpEntity ent = response.getEntity();
                        String responseString = EntityUtils.toString(ent, "UTF-8");
                        if (response.getStatusLine().getStatusCode() != 200) {


                            Log.e("MeshCommunication", "Server returned code "
                                    + response.getStatusLine().getStatusCode());
                            return "fail";
                        } else {

                            JSONObject resp = new JSONObject(responseString);
                            if (resp.getString("status").equals("success")) {
                                JSONObject message = resp.getJSONObject("msg");
                                status = message.getString("status");
                                String lines = message.getString("message");
                                if(lines.length()!=0)
                                return lines;
                                else
                                    return null;
                            } else
                                return "";


                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();

                }
                return null;

            }


        }


    }
}

