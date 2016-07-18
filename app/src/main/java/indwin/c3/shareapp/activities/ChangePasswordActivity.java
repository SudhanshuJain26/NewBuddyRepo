package indwin.c3.shareapp.activities;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import indwin.c3.shareapp.BuildConfig;
import indwin.c3.shareapp.GPSTracker;
import indwin.c3.shareapp.R;
import indwin.c3.shareapp.Splash;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.FetchNewToken;
import io.intercom.android.sdk.Intercom;
import io.intercom.com.google.gson.Gson;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    EditText oldPassword, newPassword, reEnterPassword;
    TextView passwordMismatch;
    boolean passwordsMatch = false;
    Button setPassword;
    int retryCount = 0;
    SharedPreferences mPrefs;
    Gson gson;
    UserModel user;
    boolean oldPasswordExists = false, newPasswordExists = false, reEnterPasswordExists = false;
    Button forgotPassword;
    String carrierName;
    String osVersion;
    String deviceName;
    LocationManager locationManager;
    Location getLastLocation;
    Double latitude,longitude;
    String IMEINumber,simSerialNumber;
    public int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(checkAndRequestPermissions()) {
            setContentView(R.layout.activity_change_password);
            try {
                locationManager = (LocationManager) getSystemService
                        (Context.LOCATION_SERVICE);
                GPSTracker gps = new GPSTracker(this);
                if (gps.canGetLocation()) { // gps enabled} // return boolean true/false
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                } else {

                    getLastLocation = locationManager.getLastKnownLocation
                            (LocationManager.PASSIVE_PROVIDER);

                    latitude = getLastLocation.getLatitude();
                    longitude = getLastLocation.getLongitude();
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            IMEINumber = telephonyManager.getDeviceId();
            simSerialNumber = telephonyManager.getSimSerialNumber();
            getLastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            latitude = getLastLocation.getLatitude();
            longitude = getLastLocation.getLongitude();
            carrierName = telephonyManager.getNetworkOperatorName();
            deviceName = AppUtils.getDeviceName();
            osVersion = android.os.Build.VERSION.RELEASE;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            TextView headerTitle = (TextView) findViewById(R.id.activity_header);
            headerTitle.setText("Change Password");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        oldPassword = (EditText) findViewById(R.id.old_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        reEnterPassword = (EditText) findViewById(R.id.re_enter_password);
        passwordMismatch = (TextView) findViewById(R.id.password_mismatch);
        setPassword = (Button) findViewById(R.id.set_password);
        forgotPassword = (Button) findViewById(R.id.forgot_password);

        newPassword.setOnFocusChangeListener(this);
        reEnterPassword.setOnFocusChangeListener(this);

        oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oldPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    oldPasswordExists = true;
                } else {
                    oldPasswordExists = false;
                }
                checkAll();
            }
        });
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    newPasswordExists = true;
                } else {
                    newPasswordExists = false;
                }
                checkAll();
            }
        });
        reEnterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    reEnterPasswordExists = true;
                } else {
                    reEnterPasswordExists = false;
                }
                checkAll();
            }
        });
        setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordsMatch) {
                    new UploadNewPasswordToServer(oldPassword.getText().toString(), newPassword.getText().toString()).execute();
                } else if (reEnterPassword.getText().length() > 0 && newPassword.getText().length() > 0) {
                    if (!reEnterPassword.getText().toString().equals(newPassword.getText().toString())) {
                        passwordMismatch.setVisibility(View.VISIBLE);
                    } else if (oldPassword.getVisibility() == View.GONE)
                        new ChangePasswordOnServer(newPassword.getText().toString()).execute();
                    else
                        new UploadNewPasswordToServer(oldPassword.getText().toString(), newPassword.getText().toString()).execute();
                }
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    public void checkAll() {
        if (reEnterPasswordExists && newPasswordExists && (oldPasswordExists || oldPassword.getVisibility() == View.GONE)) {
            setPassword.setEnabled(true);
            setPassword.setAlpha(1);
        } else {
            setPassword.setEnabled(true);
            setPassword.setAlpha(0.5f);
        }
    }



    private class UploadNewPasswordToServer extends AsyncTask<String, String, String> {
        String oldPasswordString, newPassword;

        public UploadNewPasswordToServer(String oldPassword, String newPassword) {
            this.oldPasswordString = oldPassword;
            this.newPassword = newPassword;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                String url = BuildConfig.SERVER_URL + "api/auth/password/change";
                HttpPost postReq = new HttpPost(url);
                JSONObject jsonobj = new JSONObject();
                jsonobj.put("phone", user.getUserId());
                jsonobj.put("oldpassword", oldPasswordString);
                jsonobj.put("newpassword", newPassword);
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
                jsonobj.put("deviceData",jsonObject);
                JSONObject jsonObject1 = new JSONObject();
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("lat",latitude);
                jsonObject2.put("long",longitude);
                jsonObject1.put("location",jsonObject2);
                jsonObject1.put("client","android");
                jsonObject1.put("timestamp", Splash.dateStamp);
                jsonobj.put("sessionData",jsonObject1);
                Log.i("TAG",Splash.dateStamp);


                StringEntity se = new StringEntity(jsonobj.toString());
                postReq.setHeader("Accept", "application/json");
                postReq.setHeader("Content-type", "application/json");
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                postReq.setHeader("x-access-token", tok_sp);
                postReq.setEntity(se);
                HttpResponse httpresponse = client.execute(postReq);
                String responseText = EntityUtils.toString(httpresponse.getEntity());
                JSONObject json = new JSONObject(responseText);
                if (json.get("status").toString().contains("success")) {
                    if (json.get("msg").toString().contains("Password updated")) {
                        return "success";
                    }
                } else {
                    if (json.get("msg").toString().contains("Incorrect userid or old password")) {
                        return "incorrect password";
                    } else if (json.get("msg").toString().contains("Invalid Token")) {
                        return "authFailed";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "fail";
        }

        @Override
        protected void onPostExecute(String result) {
            if (retryCount < 3) {
                retryCount++;
                if (result.equals("success")) {
                    AccountSettingsActivity.changedPassword.setVisibility(View.VISIBLE);
                    finish();
                } else if (result.equals("authFailed")) {
                    new FetchNewToken(getApplicationContext()).execute();
                    new UploadNewPasswordToServer(oldPasswordString, newPassword).execute();
                } else if (result.equals("fail"))
                    new UploadNewPasswordToServer(oldPasswordString, newPassword).execute();
                else if (result.equals("incorrect password")) {
                    oldPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.incomplete_small, 0);
                }
            } else
                retryCount = 0;
        }
    }

    private class ChangePasswordOnServer extends AsyncTask<String, String, String> {
        String newPassword;

        public ChangePasswordOnServer(String newPassword) {
            this.newPassword = newPassword;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                String url = BuildConfig.SERVER_URL+ "api/auth/password/forgot";
                HttpPost postReq = new HttpPost(url);
                JSONObject jsonobj = new JSONObject();
                jsonobj.put("phone", user.getUserId());
                jsonobj.put("password", newPassword);
                StringEntity se = new StringEntity(jsonobj.toString());
                postReq.setHeader("Accept", "application/json");
                postReq.setHeader("Content-type", "application/json");
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                postReq.setHeader("x-access-token", tok_sp);
                postReq.setEntity(se);
                HttpResponse httpresponse = client.execute(postReq);
                String responseText = EntityUtils.toString(httpresponse.getEntity());
                JSONObject json = new JSONObject(responseText);
                if (json.get("status").toString().contains("success")) {
                    if (json.get("msg").toString().contains("Password updated")) {
                        return "success";
                    }
                } else {
                    if (json.get("msg").toString().contains("Incorrect userid or old password")) {
                        return "incorrect password";
                    } else if (json.get("msg").toString().contains("Invalid Token")) {
                        return "authFailed";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "fail";
        }

        @Override
        protected void onPostExecute(String result) {
            if (retryCount < 3) {
                retryCount++;
                if (result.equals("success")) {
                    AccountSettingsActivity.changedPassword.setVisibility(View.VISIBLE);
                    finish();
                } else if (result.equals("authFailed")) {
                    new FetchNewToken(getApplicationContext()).execute();
                    new ChangePasswordOnServer(newPassword).execute();
                } else if (result.equals("fail"))
                    new ChangePasswordOnServer(newPassword).execute();
            } else
                retryCount = 0;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == reEnterPassword) {
            if (!hasFocus) {
                if (reEnterPassword.getText().length() > 0 && newPassword.getText().length() > 0) {
                    if (!reEnterPassword.getText().toString().equals(newPassword.getText().toString())) {
                        passwordMismatch.setVisibility(View.VISIBLE);
                    } else
                        passwordsMatch = true;
                }
            } else {
                passwordMismatch.setVisibility(View.GONE);
                reEnterPassword.setText("");
            }
        }
        if (v == newPassword) {
            if (!hasFocus) {
                if (newPassword.getText().length() > 0 && reEnterPassword.getText().length() > 0) {
                    if (!newPassword.getText().toString().equals(reEnterPassword.getText().toString())) {
                        passwordMismatch.setVisibility(View.VISIBLE);
                    } else
                        passwordsMatch = true;
                }
            } else {
                passwordMismatch.setVisibility(View.GONE);
                newPassword.setText("");
            }
        }
        if (v == oldPassword) {
            if (hasFocus) {
                oldPassword.setText("");
            }
        }
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
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if ("VerifyOTPActivity".equals(intent.getStringExtra("source"))) {
            forgotPassword.setVisibility(View.GONE);
            oldPassword.setVisibility(View.GONE);
        }
    }

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int readSmsPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS);
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
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

}
