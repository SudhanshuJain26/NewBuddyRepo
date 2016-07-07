package indwin.c3.shareapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import indwin.c3.shareapp.application.BuddyApplication;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.Constants;
import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODEC = 2;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODEL = 3;
    private static final int PERMISSION_REQUEST_CODER = 4;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    TextView notreg, login_otp;
    private PendingIntent pendingIntent;
    String url = "";
    static String token;

    private int cashBack = 0;

    static Activity act;
    public static final String MyPREFERENCES = "buddy";
    SharedPreferences sharedpreferences, sharedpreferences2;
    static String userId = "", pass = "";
    int d = 0;
    private ProgressBar spinner;

    private String Name = "", email = "", fbid = "", courseCompletiondate = "", formstatus = "", uniqueCode = "", creditLimit = "", panoradhar = "", bankaccount = "", collegeid = "", verificationdate = "", rejectionReason = "";
    private EditText username, password;
    HashMap<String, String> data11;
    private TextView login;
    private RelativeLayout rerr;
    private TextView error;
    private int pL, pT, pR, pB;
    private int checkpass = 0, checkuserid = 0;
    List<SMS> lstSms = new ArrayList<SMS>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    Double latitude, longitude;
    String IMEINumber;
    String simSerialNumber;
    Location getLastLocation;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(checkAndRequestPermissions()){

        }

        boolean accountDeleted = getIntent().getBooleanExtra(Constants.ACCOUNT_DELETED, false);
        if (accountDeleted) {

            showAccountDeletedPopup();
        }
        act = this;

        url = BuildConfig.SERVER_URL + "authenticate";
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences2 = getSharedPreferences("buddyin", Context.MODE_PRIVATE);

        d = 1;
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_main);

        rerr = (RelativeLayout) findViewById(R.id.error);
        error = (TextView) findViewById(R.id.msg);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        username = (EditText) findViewById(R.id.phone_number);
        password = (EditText) findViewById(R.id.password);

        locationManager = (LocationManager) getSystemService
                (Context.LOCATION_SERVICE);



//        checkPermission();
//        checkPermissionPHONEState();

        getLastLocation = locationManager.getLastKnownLocation
                (LocationManager.PASSIVE_PROVIDER);

        getLastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        latitude = getLastLocation.getLatitude();
        longitude  = getLastLocation.getLongitude();
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    username.setHint(R.string.h2);
                    //  Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                } else {
                    username.setHint("Phone Number");
                    // Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    password.setHint(R.string.h3);
                    //  Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                } else {
                    password.setHint("Password");
                    // Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });
        pL = username.getPaddingLeft();
        pT = username.getPaddingTop();
        pR = username.getPaddingRight();
        pB = username.getPaddingBottom();
        login = (TextView) findViewById(R.id.Login);
        login.setEnabled(false);
        final TextWatcher mTextEditorWatcher = new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                username.setBackgroundResource(R.drawable.texted);
                username.setPadding(pL, pT, pR, pB);
                password.setBackgroundResource(R.drawable.texted);
                password.setPadding(pL, pT, pR, pB);
                //
                //  textview.setText(String.valueOf(s.length());
                //                    int padding_in_dp = 16;  // 6 dps
                //                    final float scale = getResources().getDisplayMetrics().density;
                //                    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
                //                  //  final float scale = getResources().getDisplayMetrics().density;
                //                    int padding_in_px1 = (int) (48 * scale + 0.5f);
                //                    //final float scale = getResources().getDisplayMetrics().density;
                //                    int padding_in_px2 = (int) (5 * scale + 0.5f);
                //                    username.setPadding(0,padding_in_px,0,0);
                //                    password.setPadding(0,padding_in_px,0,0);
                //                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)username.getLayoutParams();
                //                    params.setMargins(padding_in_px1, padding_in_px, padding_in_px1, padding_in_px2);
                //                    username.setLayoutParams(params);
                //                    RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)password.getLayoutParams();
                //                    params.setMargins(padding_in_px1, padding_in_px, padding_in_px1, padding_in_px2);
                //                    password.setLayoutParams(params1);
                //
                //  Toast.makeText(getApplicationContext(),
                //                                "Please Enter your password",
                //                                Toast.LENGTH_LONG).show();
                //                    username.setBackgroundResource(R.drawable.texted);
                //                    password.setBackgroundResource(R.drawable.texted);
                rerr.setVisibility(View.INVISIBLE);
                //                    error.setText("Please login with correct userid.")
                if (s.length() == 10)
                    checkuserid = 1;
                else
                    checkuserid = 0;
                if ((checkuserid == 1) && (checkpass == 1)) {
                    login.setEnabled(true);
                    login.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    login.setEnabled(false);
                    login.setTextColor(Color.parseColor("#66ffffff"));
                }

            }

            public void afterTextChanged(Editable s) {
            }
        };
        username.addTextChangedListener(mTextEditorWatcher);

        final TextWatcher mTextEditorWatcher1 = new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length

                username.setBackgroundResource(R.drawable.texted);
                username.setPadding(pL, pT, pR, pB);
                password.setBackgroundResource(R.drawable.texted);
                password.setPadding(pL, pT, pR, pB);
                //                    int padding_in_dp = 16;  // 6 dps
                //                    final float scale = getResources().getDisplayMetrics().density;
                //                    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
                //                    //  final float scale = getResources().getDisplayMetrics().density;
                //                    int padding_in_px1 = (int) (48 * scale + 0.5f);
                //                    //final float scale = getResources().getDisplayMetrics().density;
                //                    int padding_in_px2 = (int) (5 * scale + 0.5f);
                //                    username.setPadding(0,padding_in_px,0,0);
                //                    password.setPadding(0,padding_in_px,0,0);
                //                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)username.getLayoutParams();
                //                    params.setMargins(padding_in_px1, padding_in_px, padding_in_px1, padding_in_px2);
                //                    username.setLayoutParams(params);
                //                    RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)password.getLayoutParams();
                //                    params.setMargins(padding_in_px1, padding_in_px, padding_in_px1, padding_in_px2);
                //                    password.setLayoutParams(params1);
                ////
                ////  Toast.makeText(getApplicationContext(),
                ////                                "Please Enter your password",
                ////                                Toast.LENGTH_LONG).show();
                //                    username.setBackgroundResource(R.drawable.texted);
                //                    password.setBackgroundResource(R.drawable.texted);
                rerr.setVisibility(View.INVISIBLE);

                //  textview.setText(String.valueOf(s.length());
                if (s.length() > 0)
                    checkpass = 1;
                else
                    checkpass = 0;
                if ((checkuserid == 1) && (checkpass == 1)) {
                    login.setEnabled(true);
                    login.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    login.setEnabled(false);
                    login.setTextColor(Color.parseColor("#66ffffff"));
                }

            }

            public void afterTextChanged(Editable s) {
            }
        };
        password.addTextChangedListener(mTextEditorWatcher1);

        notreg = (TextView) findViewById(R.id.signUp);
        notreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(MainActivity.this, Inviteform.class);
                in2.putExtra("login", 1);
                finish();
                startActivity(in2);
                overridePendingTransition(0, 0);
            }
        });
        login_otp = (TextView) findViewById(R.id.Login_otp);
        login_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent opt = new Intent(MainActivity.this, Login_with_otp.class);
                finish();
                startActivity(opt);
                overridePendingTransition(0, 0);


            }
        });

        login.setTextColor(Color.parseColor("#66ffffff"));
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setTextColor(Color.parseColor("#ffffff"));
                login.setEnabled(false);

                SharedPreferences.Editor editor2 = sharedpreferences2.edit();
                editor2.putString("name", username.getText().toString());
                editor2.putString("password", password.getText().toString());
                editor2.commit();
                userId = username.getText().toString();
                pass = password.getText().toString();
                SharedPreferences cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
                SharedPreferences.Editor edc = cred.edit();
                edc.putString("phone_number", userId);
                edc.commit();
                SharedPreferences mPrefs = getSharedPreferences("buddy", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                UserModel user = new UserModel();
                user.setUserId(userId);
                AppUtils.saveUserObject(MainActivity.this, user);
                //                    String tok_sp=toks.getString("token_value","");

                if ((userId.length() != 0) && (pass.length() != 0)) {
                    new ItemsByKeyword().execute(url);
                    login_otp.setEnabled(false);
                    notreg.setEnabled(false);
                } else if (userId.length() != 0) {
                    login.setTextColor(Color.parseColor("#66ffffff"));
                    //                        int padding_in_dp = 16;  // 6 dps
                    //                        final float scale = getResources().getDisplayMetrics().density;
                    //                        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
                    //                        //  final float scale = getResources().getDisplayMetrics().density;
                    //                        int padding_in_px1 = (int) (48 * scale + 0.5f);
                    //                        //final float scale = getResources().getDisplayMetrics().density;
                    //                        int padding_in_px2 = (int) (5 * scale + 0.5f);
                    //                        username.setPadding(0,padding_in_px,0,0);
                    //                        password.setPadding(0,padding_in_px,0,0);
                    //                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)username.getLayoutParams();
                    //                        params.setMargins(padding_in_px1, padding_in_px, padding_in_px1, padding_in_px2);
                    //                        username.setLayoutParams(params);
                    //                        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)password.getLayoutParams();
                    //                        params.setMargins(padding_in_px1, padding_in_px, padding_in_px1, padding_in_px2);
                    //                        password.setLayoutParams(params1);
                    ////
                    ////  Toast.makeText(getApplicationContext(),
                    ////                                "Please Enter your password",
                    ////                                Toast.LENGTH_LONG).show();
                    ////                      username.setBackgroundResource(R.drawable.texted);
                    //                        password.setBackgroundResource(R.drawable.texted2);

                    password.setBackgroundResource(R.drawable.texted2);
                    password.setPadding(pL, pT, pR, pB);
                    rerr.setVisibility(View.VISIBLE);
                    error.setText("Please Enter your password");
                    login.setEnabled(true);

                } else if (pass.length() != 0) {
                    login.setTextColor(Color.parseColor("#66ffffff"));
                    login.setEnabled(true);
                    //                        Toast.makeText(getApplicationContext(),
                    //                                "Please Enter your userid.",
                    //                                Toast.LENGTH_LONG).show();
                    rerr.setVisibility(View.VISIBLE);
                    //                        username.setBackgroundResource(R.drawable.texted2);
                    //                        int padding_in_dp = 16;  // 6 dps
                    //                        final float scale = getResources().getDisplayMetrics().density;
                    //                        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
                    //                        //  final float scale = getResources().getDisplayMetrics().density;
                    //                        int padding_in_px1 = (int) (48 * scale + 0.5f);
                    //                        //final float scale = getResources().getDisplayMetrics().density;
                    //                        int padding_in_px2 = (int) (5 * scale + 0.5f);
                    //                        username.setPadding(0,padding_in_px,0,0);
                    //                        password.setPadding(0,padding_in_px,0,0);
                    //                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)username.getLayoutParams();
                    //                        params.setMargins(padding_in_px1, padding_in_px, padding_in_px1, padding_in_px2);
                    //                        username.setLayoutParams(params);
                    //                        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)password.getLayoutParams();
                    //                        params.setMargins(padding_in_px1, padding_in_px, padding_in_px1, padding_in_px2);
                    //                        password.setLayoutParams(params1);
                    ////  Toast.makeText(getApplicationContext(),
                    ////                                "Please Enter your password",
                    ////                                Toast.LENGTH_LONG).show();
                    //                        username.setBackgroundResource(R.drawable.texted2);
                    //                        password.setBackgroundResource(R.drawable.texted2);
                    //    password.setBackgroundResource(R.drawable.texted);

                    username.setBackgroundResource(R.drawable.texted2);
                    username.setPadding(pL, pT, pR, pB);
                    error.setText("Please Enter your userid.");
                } else {
                    login.setEnabled(true);
                    login.setTextColor(Color.parseColor("#66ffffff"));
                    //                        Toast.makeText(getApplicationContext(),

                    //                                "Fields are empty",
                    ////                                Toast.LENGTH_LONG).show();
                    //                        int padding_in_dp = 16;  // 6 dps
                    //                        final float scale = getResources().getDisplayMetrics().density;
                    //                        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
                    //                        //  final float scale = getResources().getDisplayMetrics().density;
                    //                        int padding_in_px1 = (int) (48 * scale + 0.5f);
                    //                        //final float scale = getResources().getDisplayMetrics().density;
                    //                        int padding_in_px2 = (int) (5 * scale + 0.5f);
                    //                        username.setPadding(0,padding_in_px,0,0);
                    //                        password.setPadding(0,padding_in_px,0,0);
                    //                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)username.getLayoutParams();
                    //                        params.setMargins(padding_in_px1, padding_in_px, padding_in_px1, padding_in_px2);
                    //                        username.setLayoutParams(params);
                    //                        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)password.getLayoutParams();
                    //                        params.setMargins(padding_in_px1, padding_in_px, padding_in_px1, padding_in_px2);
                    //                        password.setLayoutParams(params1);
                    ////
                    ////  Toast.makeText(getApplicationContext(),
                    ////                                "Please Enter your password",
                    ////                                Toast.LENGTH_LONG).show();
                    ////                        username.setBackgroundResource(R.drawable.texted);
                    ////                        password.setBackgroundResource(R.drawable.texted2);
                    //                        username.setBackgroundResource(R.drawable.texted2);
                    //                        password.setBackgroundResource(R.drawable.texted2);
                    rerr.setVisibility(View.VISIBLE);

                    username.setBackgroundResource(R.drawable.texted2);
                    username.setPadding(pL, pT, pR, pB);

                    password.setBackgroundResource(R.drawable.texted2);
                    password.setPadding(pL, pT, pR, pB);
                    error.setText("Fields are empty");
                }
            }
        });
        try {
            Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
            Intercom.client().registerIdentifiedUser(
                    new Registration().withUserId(userId));
        } catch (Exception e) {
        }
        try {
            Intercom.client().openGCMMessage(getIntent());
        } catch (Exception e) {
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void showAccountDeletedPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = MainActivity.this.getLayoutInflater().inflate(R.layout.alert_delete_account_message, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();

        dialog.show();

    }


    @Override
    public void onBackPressed() {
        Intent in = new Intent(MainActivity.this, Landing.class);
        finish();
        startActivity(in);
        overridePendingTransition(0, 0);


    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://indwin.c3.shareapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://indwin.c3.shareapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class ItemsByKeyword extends
                                 AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            if (d == 1)
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

                HttpPost httppost = new HttpPost(url);
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
                        SharedPreferences userP = getSharedPreferences("buddyin", Context.MODE_PRIVATE);
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

            if (result.contains("fail")) {

                // populateUserDetails();

                Toast.makeText(getApplicationContext(),
                        "Something's Wrong! Please try again!",
                        Toast.LENGTH_LONG).show();

            } else {


                new Login().execute(url);


            }

        }
    }


    private class Login extends
                        AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... data) {

            // String urldisplay = data[0];
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST

                payload.put("userid", userId);
                payload.put("password", pass);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("imei",IMEINumber);
                jsonObject.put("simNumber",simSerialNumber);
                JSONObject location = new JSONObject();
                location.put("latitude",latitude);
                location.put("longitude",longitude);
                jsonObject.put("location",location);
                payload.put("deviceDetails",jsonObject);
                payload.put("clientDevice","android");

                // payload.put("action", details.get("action"));


                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                String url2 =BuildConfig.SERVER_URL + "api/user/login";

                HttpResponse response = AppUtils.connectToServerPost(url2, payload.toString(), tok_sp);

                if (response != null) {
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

                            return "win";

                        }

                    }
                } else return "fail";


            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {

            if (d == 1)
                spinner.setVisibility(View.GONE);
            if (!result.contains("win")) {
                login.setTextColor(Color.parseColor("#66ffffff"));
                // populateUserDetails();
                if (result.contains("Invalid userid")) {
                    login.setEnabled(true);

                    notreg.setEnabled(true);
                    login_otp.setEnabled(true);
                    username.setBackgroundResource(R.drawable.texted2);

                    //  password.setBackgroundResource(R.drawable.texted);
                    username.setPadding(pL, pT, pR, pB);
                    rerr.setVisibility(View.VISIBLE);
                    error.setText("Please login with correct userid.");
                } else if (result.contains("Invalid password")) {
                    password.setText("");
                    login.setEnabled(true);
                    rerr.setVisibility(View.VISIBLE);
                    notreg.setEnabled(true);
                    login_otp.setEnabled(true);
                    password.setBackgroundResource(R.drawable.texted2);

                    //password.setBackgroundResource(R.drawable.texted);
                    password.setPadding(pL, pT, pR, pB);
                    error.setText("Please login with correct password.");
                }

            } else {
                int a = 1;
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("checklog", a);
                editor.commit();
                setUpPush();


                //   gestAllSms();
                try {
                    Map userMap = new HashMap<>();


                    userMap.put("user_id", userId);
                    userMap.put("logged_in", true);
                    userMap.put("phoneVerified", true);
                    Intercom.client().updateUser(userMap);
                } catch (Exception e) {
                    System.out.println("Intercom three" + e.toString());
                }

                //  getAllSms();

                Intent alarmIntent = new Intent(MainActivity.this, AndroidReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);
                setAlarm();
                int results = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS);
                int w = 0;
                int resultscon = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS);
                if (resultscon == PackageManager.PERMISSION_GRANTED) {
                    w = 1;

                    new Thread(new Runnable() {
                        public void run() {

                        }

                    }).start();

                } else {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CODEC);

                }


                if (results == PackageManager.PERMISSION_GRANTED) {


                    new Thread(new Runnable() {
                        public void run() {

                            //  getALlContacts()
                        }

                    }).start();

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, PERMISSION_REQUEST_CODE);

                }

                //                new Thread(new Runnable() {
                //                    public void run(){
                ////                       getALlContacts();
                //                       // getAllSms();
                //                    }
                //                }).start();

                //   getALlContacts();
                SharedPreferences sh = getSharedPreferences("buddy", Context.MODE_PRIVATE);
                boolean isUpdatingDB = sh.getBoolean("updatingDB", false);
                if (!isUpdatingDB) {

                    new checkuser().execute(url);
                } else {
                    Runnable myRunnable = new Runnable() {

                        public void run() {
                            checkForDBUpdate();
                        }


                    };
                    Thread thread = new Thread(myRunnable);
                    thread.start();

                }


            }

        }
    }

    private void checkForDBUpdate() {
        try {
            Thread.sleep(10000);

        } catch (Exception e) {

        }
        SharedPreferences sh = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        boolean isUpdatingDB = sh.getBoolean("updatingDB", false);
        if (isUpdatingDB)
            checkForDBUpdate();
        else
            new checkuser().execute(url);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new Thread(new Runnable() {
                        public void run() {

                            //  getALlContacts();
                            //  getALlContacts();

                            //                            getAllSms();

                            //                    getALlContacts();
                        }

                    }).start();
                    //                    Snackbar.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();

                } else {

                    //                    Snackbar.make(view,"Permission Denied, You cannot access location data.",Snackbar.LENGTH_LONG).show();

                }
                break;
            case PERMISSION_REQUEST_CODEC:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new Thread(new Runnable() {
                        public void run() {


                            //                            getALlContacts();
                            //                            getAllSms(

                            //                    getALlContacts();
                        }

                    }).start();
                    //                    Snackbar.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();

                } else {

                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.sendGoogleAnalytics((BuddyApplication) getApplication());
    }

    private class checkuser extends
                            AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            // String urldisplay = data[0];
            // HashMap<String, String> details = data[0];

            try {
                // userid=12&productid=23&action=add
                // TYPE: POST
                //      payload.put("userid", details.get("userid"));
                // payload.put("productid", details.get("productid"));
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();
                String uurl = BuildConfig.SERVER_URL + "api/user/form?phone=" + userId;
                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                HttpGet httppost = new HttpGet(uurl);
                httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");
                httppost.setHeader("x-access-token", tok_sp);
                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");
                if (response.getStatusLine().getStatusCode() != 200) {

                    Log.e("MeshCommunication", "Server returned code "
                            + response.getStatusLine().getStatusCode());
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);
                    JSONObject data = new JSONObject(resp.getString("data"));

                    //profile url to be included later
                    email = data.getString("email");
                    Name = data.getString("name");
                    SharedPreferences sharedpreferences111 = getSharedPreferences("token", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor111 = sharedpreferences111.edit();

                    editor111.putString("productdpname", Name);
                    editor111.commit();
                    SharedPreferences sharedpreferences11 = getSharedPreferences("cred", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor11 = sharedpreferences11.edit();
                    editor11.putString("n1", Name);
                    editor11.putString("e1", email);
                    editor11.commit();

                    Gson gson = new Gson();
                    String json = sharedpreferences.getString("UserObject", "");
                    UserModel user = AppUtils.getUserObject(MainActivity.this);
                    if (user == null)
                        user = new UserModel();
                    user.setName(Name);
                    user.setEmail(email);
                    if (data.opt("offlineForm") != null && !data.getBoolean("offlineForm"))
                        checkDataForNormalUser(user, gson, data);
                    else
                        checkDataForOfflineUser(user, gson, data);
                    json = gson.toJson(user);

                    json = gson.toJson(user);
                    sharedpreferences.edit().putString("UserObject", json).apply();

                    try {
                        uniqueCode = data.getString("uniqueCode");
                        SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("rcode", uniqueCode);
                        editor.commit();

                    } catch (Exception e) {
                    }
                    try {
                        creditLimit = data.getString("creditLimit");
                    } catch (Exception e) {
                    }
                    try {
                        rejectionReason = data.getString("rejectionReason");
                    } catch (Exception e) {
                    }

                    try {

                        SharedPreferences.Editor editor2 = toks.edit();
                        editor2.putString("course", courseCompletiondate);

                        //  editor2.putString("password", password.getText().toString());
                        editor2.commit();

                        formstatus = data.getString("formStatus");
                    } catch (Exception e) {
                        formstatus = "empty";
                    }


                    try {
                        cashBack = data.getInt("totalCashback");
                    } catch (Exception e) {
                        cashBack = 0;
                    }
                    String approvedBand = "";
                    try {
                        approvedBand = data.getString("approvedBand");
                    } catch (Exception e) {
                        approvedBand = "";
                    }
                    int creditLimit = 0;
                    try {
                        creditLimit = data.getInt("creditLimit");
                    } catch (Exception e) {
                        creditLimit = 0;
                    }
                    int totalBorrowed = 0;
                    try {
                        totalBorrowed = data.getInt("totalBorrowed");
                    } catch (Exception e) {
                        totalBorrowed = 0;
                    }
                    String profileStatus = "";
                    try {
                        profileStatus = data.getString("profileStatus");
                    } catch (Exception e) {
                        profileStatus = "";
                    }
                    String courseend = "";

                    try {
                        courseend = data.getString("courseCompletionDate");
                    } catch (Exception e) {
                        courseend = "";
                    }
                    String status1k = "";
                    try {
                        status1k = data.getString("status1K");
                    } catch (Exception e) {
                        status1k = "";
                    }
                    String status7k = "";
                    try {
                        status7k = data.getString("status7K");
                    } catch (Exception e) {
                        status7k = "";
                    }

                    String nameadd = "";
                    try {
                        nameadd = data.getString("college");
                    } catch (Exception e) {
                    }
                    SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorP = userP.edit();
                    editorP.putString("approvedBand", approvedBand);
                    editorP.putString("status1K", status1k);
                    editorP.putString("status7K", status7k);
                    editorP.putString("profileStatus", profileStatus);
                    editorP.putString("course", courseend);
                    editorP.putString("formStatus", formstatus);
                    editorP.putString("productdpname", Name);
                    editorP.putInt("creditLimit", creditLimit);
                    editorP.putInt("totalBorrowed", totalBorrowed);
                    editorP.putInt("cashBack", cashBack);
                    editorP.putString("nameadd", nameadd);

                    editorP.commit();

                    if (formstatus.equals(""))
                        formstatus = "empty";
                    try {
                        String dpid = data.getString("fbUserId");
                        SharedPreferences sf = getSharedPreferences("proid", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sf.edit();
                        editor2.putString("dpid", dpid);

                        //  editor2.putString("password", password.getText().toString());
                        editor2.commit();
                    } catch (Exception e) {
                    }
                    try {
                        fbid = data.getString("fbConnected");
                    } catch (Exception e) {
                        fbid = "empty";
                    }
                    if ((fbid.equals("") || (fbid.equals("false"))))
                        fbid = "empty";
                    try {
                        panoradhar = data.getString("addressProofs");
                    } catch (Exception e) {
                        panoradhar = "NA";
                    }
                    if (panoradhar.equals(""))
                        panoradhar = "NA";
                    try {
                        collegeid = data.getString("collegeIDs");
                    } catch (Exception e) {
                        collegeid = "NA";
                    }
                    if (collegeid.equals(""))
                        collegeid = "NA";
                    try {
                        bankaccount = data.getString("bankStatement");
                    } catch (Exception e) {
                        bankaccount = "NA";
                    }
                    if (bankaccount.equals(""))
                        bankaccount = "NA";
                    try {
                        verificationdate = data.getString("collegeIdVerificationDate");
                    } catch (Exception e) {
                        verificationdate = "NA";
                    }
                    if (verificationdate.equals(""))
                        verificationdate = "NA";

                    // TODO: 2/7/2016  add college id field and check

                    if (resp.getString("status").contains("fail")) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {
                        return "win";
                    }

                }

            } catch (Exception e) {
                Log.e("mesherror111", e.getMessage());
                return "fail";

            }
        }

        private void checkDataForNormalUser(UserModel user, Gson gson, JSONObject data1) {
            AppUtils.checkDataForNormalUser(user, gson, data1, MainActivity.this);
            try {
                Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
                Intercom.client().registerIdentifiedUser(
                        new Registration().withUserId(user.getUserId()));
                Map userMap = new HashMap<>();
                userMap.put("name", user.getName());
                userMap.put("email", user.getEmail());
                userMap.put("phone", user.getUserId());
                Intercom.client().updateUser(userMap);
                //user.setEmailSent(false);
                //if (data1.opt("gender") != null)
                //    user.setGender(data1.getString("gender"));
                //if (data1.opt("approvedBand") != null)
                //    user.setApprovedBand(data1.getString("approvedBand"));
                //if (data1.opt("formStatus") != null)
                //    user.setFormStatus(data1.getString("formStatus"));
                //Map userMap = new HashMap<>();
                //if (data1.opt("profileStatus") != null) {
                //    user.setProfileStatus(data1.getString("profileStatus"));
                //    userMap.put("profileStatus", user.getProfileStatus());
                //}
                //if (data1.opt("status1K") != null) {
                //    String status1K = data1.getString("status1K");
                //    user.setStatus1K(status1K);
                //    userMap.put("status1K", status1K);
                //    if (Constants.STATUS.DECLINED.toString().equals(status1K) || Constants.STATUS.APPLIED.toString().equals(status1K) || Constants.STATUS.APPROVED.toString().equals(status1K))
                //        user.setAppliedFor1k(true);
                //    else user.setAppliedFor1k(false);
                //}
                //if (data1.opt("status7K") != null) {
                //    String status7K = data1.getString("status7K");
                //    user.setStatus7K(status7K);
                //    userMap.put("status7K", status7K);
                //    if (Constants.STATUS.DECLINED.toString().equals(status7K) || Constants.STATUS.APPLIED.toString().equals(status7K) || Constants.STATUS.APPROVED.toString().equals(status7K))
                //        user.setAppliedFor7k(true);
                //    else user.setAppliedFor7k(false);
                //}
                //if (data1.opt("status60K") != null) {
                //    String status60K = data1.getString("status60K");
                //    user.setStatus60K(status60K);
                //    userMap.put("status60K", status60K);
                //    if (Constants.STATUS.DECLINED.toString().equals(status60K) || Constants.STATUS.APPLIED.toString().equals(status60K) || Constants.STATUS.APPROVED.toString().equals(status60K))
                //        user.setAppliedFor60k(true);
                //    else user.setAppliedFor60k(false);
                //}
                //Intercom.client().updateUser(userMap);
                //if (data1.opt("creditLimit") != null)
                //    user.setCreditLimit(data1.getInt("creditLimit"));
                //if (data1.opt("totalBorrowed") != null) {
                //    if (user.getCreditLimit() == 0) {
                //        user.setAvailableCredit(0);
                //    } else {
                //        int available = user.getCreditLimit() - data1.getInt("totalBorrowed");
                //        if (available > 0) {
                //            user.setAvailableCredit(available);
                //        } else {
                //            user.setAvailableCredit(0);
                //        }
                //    }
                //} else {
                //    user.setAvailableCredit(user.getCreditLimit());
                //}
                //if (data1.opt("totalCashback") != null)
                //    user.setCashBack(data1.getInt("totalCashback"));
                //if (data1.opt("emailVerified") != null)
                //    user.setEmailVerified(data1.getBoolean("emailVerified"));
                //if (data1.opt("fbConnected") != null)
                //    user.setIsFbConnected(Boolean.parseBoolean(data1.getString("fbConnected")));
                //if (data1.opt("college") != null)
                //    user.setCollegeName(data1.getString("college"));
                //if (data1.opt("course") != null)
                //    user.setCourseName(data1.getString("course"));
                //if (data1.opt("courseCompletionDate") != null)
                //    user.setCourseEndDate(data1.getString("courseCompletionDate"));
                //if (data1.opt("gpa") != null) {
                //
                //    user.setGpa(data1.getString("gpa"));
                //}
                //if (data1.opt("gpaType") != null) {
                //
                //    user.setGpaType(data1.getString("gpaType"));
                //}
                //if (data1.opt("collegeIDs") != null)
                //    user.setCollegeIds(gson.fromJson(data1.getString("collegeIDs"), ArrayList.class));
                //if (data1.opt("addressProofs") != null)
                //    user.setAddressProofs(gson.fromJson(data1.getString("addressProofs"), ArrayList.class));
                //if (data1.opt("panOrAadhar") != null) {
                //    user.setPanOrAadhar(data1.getString("panOrAadhar"));
                //    if ("PAN".equals(user.getPanOrAadhar()))
                //        user.setPanNumber(data1.getString("pan"));
                //    else
                //        user.setAadharNumber(data1.getString("aadhar"));
                //}
                //if (data1.opt("dob") != null)
                //    user.setDob(data1.getString("dob"));
                //if (data1.opt("accomodation") != null)
                //    user.setAccommodation(data1.getString("accomodation"));
                //if (data1.opt("currentAddress") != null)
                //    user.setCurrentAddress(data1.getJSONObject("currentAddress").getString("line1"));
                //if (data1.opt("permanentAddress") != null)
                //    user.setPermanentAddress(data1.getJSONObject("permanentAddress").getString("line1"));
                //if (data1.opt("rollNumber") != null)
                //    user.setRollNumber(data1.getString("rollNumber"));
                //if (data1.opt("rejectionReason") != null)
                //    user.setRejectionReason(data1.getString("rejectionReason"));
                //if (data1.optJSONArray("familyMember") != null) {
                //    JSONArray familyMembers = data1.getJSONArray("familyMember");
                //    for (int i = 0; i < familyMembers.length(); i++) {
                //        JSONObject familyJson = familyMembers.getJSONObject(i);
                //        if (i == 1) {
                //            if (familyJson.getString("relation") != null) {
                //                user.setFamilyMemberType2(familyJson.getString("relation"));
                //                user.setProfessionFamilyMemberType2(familyJson.getString("occupation"));
                //                user.setPhoneFamilyMemberType2(familyJson.getString("phone"));
                //                if (familyJson.opt("preferredLanguage") != null)
                //                    user.setPrefferedLanguageFamilyMemberType2(familyJson.getString("preferredLanguage"));
                //            }
                //        } else {
                //            if (familyJson.getString("relation") != null) {
                //                user.setFamilyMemberType1(familyJson.getString("relation"));
                //                user.setProfessionFamilyMemberType1(familyJson.getString("occupation"));
                //                user.setPhoneFamilyMemberType1(familyJson.getString("phone"));
                //                if (familyJson.opt("preferredLanguage") != null)
                //                    user.setPrefferedLanguageFamilyMemberType1(familyJson.getString("preferredLanguage"));
                //            }
                //        }
                //    }
                //}
                //if (data1.opt("bankAccountNumber") != null)
                //    user.setBankAccNum(data1.getString("bankAccountNumber"));
                //if (data1.opt("bankIFSC") != null)
                //    user.setBankIfsc(data1.getString("bankIFSC"));
                //if (data1.opt("friendName") != null)
                //    user.setClassmateName(data1.getString("friendName"));
                //if (data1.opt("friendNumber") != null)
                //    user.setClassmatePhone(data1.getString("friendNumber"));
                //if (data1.opt("collegeIdVerificationDate") != null)
                //    user.setVerificationDate(data1.getString("collegeIdVerificationDate"));
                //if (data1.opt("annualFees") != null)
                //    user.setAnnualFees(data1.getString("annualFees"));
                //if (data1.opt("scholarship") != null)
                //    user.setScholarship(String.valueOf(data1.getString("scholarship")));
                //if (data1.opt("scholarshipProgram") != null)
                //    user.setScholarshipType(data1.getString("scholarshipProgram"));
                //if (data1.opt("scholarshipAmount") != null)
                //    user.setScholarshipAmount(data1.getString("scholarshipAmount"));
                //if (data1.opt("takenLoan") != null)
                //    user.setStudentLoan(data1.getString("takenLoan"));
                //if (data1.opt("monthlyExpense") != null)
                //    user.setMonthlyExpenditure(data1.getString("monthlyExpense"));
                //if (data1.opt("ownVehicle") != null)
                //    user.setVehicle(String.valueOf(data1.getString("ownVehicle")));
                //if (data1.opt("vehicleType") != null)
                //    user.setVehicleType(data1.getString("vehicleType"));
                //if (data1.opt("bankStatements") != null)
                //    user.setBankStmts(gson.fromJson(data1.getString("bankStatements"), ArrayList.class));
                //if (data1.opt("bankProofs") != null)
                //    user.setBankProofs(gson.fromJson(data1.getString("bankProofs"), ArrayList.class));
                //if (data1.opt("selfie") != null) {
                //    JSONArray array = data1.getJSONArray("selfie");
                //    user.setSelfie((String) array.get(0));
                //} else {
                //    user.setSelfie("");
                //}
                //if (data1.opt("signature") != null) {
                //    JSONArray array = data1.getJSONArray("signature");
                //    user.setSignature((String) array.get(0));
                //} else {
                //    user.setSignature("");
                //}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void checkDataForOfflineUser(UserModel user, Gson gson, JSONObject data1) {
            try {
                sharedpreferences.edit().putBoolean("visitedFormStep1Fragment1", true).apply();
                sharedpreferences.edit().putBoolean("visitedFormStep1Fragment2", true).apply();
                sharedpreferences.edit().putBoolean("visitedFormStep1Fragment3", true).apply();
                checkDataForNormalUser(user, gson, data1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected void onPostExecute(String result) {
            if (d == 1) {
                spinner.setVisibility(View.GONE);
                notreg.setEnabled(true);
                login_otp.setEnabled(true);
            }
            if (result.contains("fail")) {
                // populateUserDetails();

                Toast.makeText(getApplicationContext(),
                        "Something's Wrong! Please try again!",
                        Toast.LENGTH_LONG).show();

            } else {
                try {


                    if (d == 1)

                        Toast.makeText(getApplicationContext(),
                                "Successfully Logged In",
                                Toast.LENGTH_LONG).show();
                    //     Intercom.client().reset();
                } catch (Exception e) {
                    System.out.println(e.toString() + "int main");
                }
                try {


                    try {
                        Map userMap = new HashMap<>();
                        System.out.println("Intercom data 1" + userId);

                        userMap.put("user_id", userId);


                        Intercom.client().updateUser(userMap);
                    } catch (Exception e) {
                        System.out.println("Intercom five" + e.toString());
                    }
                } catch (Exception e) {
                    System.out.println("Intercom four" + e.toString());
                }
                if (formstatus.equals("saved")) {

                    Intent in = new Intent(MainActivity.this, HomePage.class);
                    finish();
                    // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    in.putExtra("Name", Name);
                    in.putExtra("fbid", fbid);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC", uniqueCode);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                } else if (formstatus.equals("declined")) {

                    Intent in = new Intent(MainActivity.this, HomePage.class);
                    finish();
                    // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    in.putExtra("Name", Name);
                    in.putExtra("fbid", fbid);
                    in.putExtra("Rej", rejectionReason);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC", uniqueCode);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                } else if (formstatus.equals("submitted")) {
                    //                 Intent in = new Intent(MainActivity.this, Landing.class);
                    Intent in = new Intent(MainActivity.this, HomePage.class);
                    bankaccount = "something";
                    if ((panoradhar.equals("NA")) || (bankaccount.equals("NA")) || (collegeid.equals("NA"))) {
                        in.putExtra("screen_no", 1);
                    } else if ((!panoradhar.equals("NA")) && (!collegeid.equals("NA")) && (!bankaccount.equals("NA")) && (verificationdate.equals("NA"))) {
                        in.putExtra("screen_no", 2);
                    } else if ((!panoradhar.equals("NA")) && (!collegeid.equals("NA")) && (!bankaccount.equals("NA")) && (!verificationdate.equals("NA"))) {
                        in.putExtra("screen_no", 3);
                        in.putExtra("VeriDate", verificationdate);
                    }

                    finish();
                    in.putExtra("Name", Name);
                    in.putExtra("Email", email);
                    in.putExtra("fbid", fbid);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC", uniqueCode);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                }

                if (formstatus.equals("approved") || (formstatus.equals("flashApproved"))) {

                    Intent in = new Intent(MainActivity.this, HomePage.class);
                    // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    finish();
                    in.putExtra("Name", Name);

                    if (formstatus.equals("approved"))
                        in.putExtra("checkflash", 0);
                    else
                        in.putExtra("checkflash", 1);


                    in.putExtra("fbid", fbid);
                    in.putExtra("Email", email);//
                    in.putExtra("Credits", creditLimit);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC", uniqueCode);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                } else if (formstatus.equals("empty")) {

                    Intent in = new Intent(MainActivity.this, HomePage.class);
                    finish();
                    in.putExtra("Name", Name);
                    in.putExtra("fbid", fbid);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC", uniqueCode);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                }


            }

        }
    }

    public class SendSmsToServer extends AsyncTask<List<SMS>, String, String> {

        @Override
        protected String doInBackground(List<SMS>... sms) {
            try {
                System.out.println("sm123");
                JSONObject payload = new JSONObject();
                List<SMS> res = sms[0];
                System.out.println("buddyqwe" + new Gson().toJson(sms[0]));
                String check = new Gson().toJson(sms[0]);


                int length = check.length();


                String check2 = check.substring(1, length - 1);
                String pay;


                try {
                    HttpParams httpParameters = new BasicHttpParams();
                    SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                    String tok_sp = toks.getString("token_value", "");
                    HttpClient client = new DefaultHttpClient(httpParameters);
                    String urlsm = BuildConfig.SERVER_URL + "api/content/sms";
                    SharedPreferences pref = act.getSharedPreferences("MyPref", 0);
                    HttpPost httppost = new HttpPost(urlsm);

                    HttpConnectionParams
                            .setConnectionTimeout(httpParameters, 30000);
                    httppost.setHeader("x-access-token", tok_sp);
                    httppost.setHeader("Content-Type", "application/json");
                    //     JSONArray mJSONArray = new JSONArray(Arrays.asList(sms));
                    payload.put("userId", userId);
                    payload.put("data", check);


                    String t = payload.toString();
                    String t1 = payload.toString().replace("{\\", "{");
                    String tx = t1.toString().replace(":\\", ":");
                    String ty = tx.toString().replace("\\\":", "\":");
                    String tz = ty.toString().replace(",\\", ",");
                    String tw = tz.toString().replace("\\\"", "\"");
                    String te = tw.toString().replace("\\\\r\\", "");
                    String tr = te.toString().replace("\\\\\"", "");
                    String tu = tr.toString().replace("/\\/", "");
                    String tq = tu.toString().replace("\\/", "");
                    String tl = tq.toString().replace("\\\\", "");
                    String t2 = tl.replace("\"[{", "[{");
                    String t3 = t2.replace("}]\"", "} ]");
                    String t4 = t3.replace("\\n", "");
                    String t5 = t4.replace("\"\"", "");
                    StringEntity entity = new StringEntity(t5);
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

                        if (resp.getString("status").contains("fail")) {

                            Log.e("MeshCommunication", "Server returned code "
                                    + resp.getString("message"));

                            return "fail";
                        } else {

                            return "win";

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return "fail";
                }
            } catch (Exception e) {
                return "fail";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            // TODO Auto-generated method stub
            if (result.equals("win")) {

                System.out.println(result + "smswin");
            }
            System.out.println(result + "smsloss");
        }
    }

    public class SendContactToServer extends AsyncTask<List<Contacts>, String, String> {

        @Override
        protected String doInBackground(List<Contacts>... cont) {
            try {
                System.out.println("sm123");
                JSONObject payload = new JSONObject();
                List<Contacts> res = cont[0];

                String check = "";
                try {
                    System.out.println("buddyqwe" + new Gson().toJson(cont[0]));
                    check = new Gson().toJson(cont[0]);
                } catch (Exception e) {
                    String s = e.toString();
                    System.out.print("regre" + s + "ssio");
                }

                String pay;


                try {
                    HttpParams httpParameters = new BasicHttpParams();
                    SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                    String tok_sp = toks.getString("token_value", "");
                    HttpClient client = new DefaultHttpClient(httpParameters);
                    String urlsm = BuildConfig.SERVER_URL+ "api/content/contact";
                    SharedPreferences pref = act.getSharedPreferences("MyPref", 0);
                    HttpPost httppost = new HttpPost(urlsm);

                    HttpConnectionParams
                            .setConnectionTimeout(httpParameters, 30000);
                    httppost.setHeader("x-access-token", tok_sp);
                    httppost.setHeader("Content-Type", "application/json");
                    //     JSONArray mJSONArray = new JSONArray(Arrays.asList(sms));
                    try {
                        payload.put("userId", userId);
                        payload.put("data", check);
                    } catch (Exception e) {
                        System.out.println("NNeve" + e.toString());
                    }


                    String t1 = payload.toString().replace("{\\", "{");
                    String tx = t1.toString().replace(":\\", ":");
                    String ty = tx.toString().replace("\\\":", "\":");
                    String tz = ty.toString().replace(",\\", ",");
                    String tw = tz.toString().replace("\\\"", "\"");
                    String te = tw.toString().replace("\\\\r\\", "");
                    String tr = te.toString().replace("\\\\\"", "");
                    String tu = tr.toString().replace("/\\/", "");
                    String tq = tu.toString().replace("\\/", "");
                    String tl = tq.toString().replace("\\\\", "");
                    String t2 = tl.replace("\"[", "[");
                    String t3 = t2.replace("]\"", "]");
                    String t4 = t3.replace("\\n", "");
                    String t5 = t4.replace("\"\"", "");
                    StringEntity entity = new StringEntity(t5);
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

                        if (resp.getString("status").contains("fail")) {

                            Log.e("MeshCommunication", "Server returned code "
                                    + resp.getString("message"));

                            return "fail";
                        } else {

                            return "win";

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return "fail";
                }
            } catch (Exception e) {
                return "fail";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            // TODO Auto-generated method stub
            if (result.equals("win")) {
                System.out.println(result);
            }
        }
    }

    public void getALlContacts() {
        String id = "NA", name = "NA", ph_no = "NA", typ = "NA", email = "NA", emailType = "NA", noteWhere = "NA", note = "NA", orgWhere = "NA", orgName = "NA", title = "NA", addrWhere = "NA", poBox = "NA", city = "NA", state = "NA", postalCode = "NA", street = "NA", country = "NA", type = "NA";
        String[] noteWhereParams, orgWhereParams, addrWhereParams, nickwhereparam;
        String nickwhere = "NA", nickname = "NA";
        ContentResolver cr1 = getContentResolver();
        Cursor cu = cr1.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cu.getCount() > 0) {
            System.out.print("con" + cu.getCount());
            List<Contacts> con = new ArrayList<Contacts>();
            List<Contacts> con2 = new ArrayList<Contacts>();
            int batchcount1 = 0;
            while (cu.moveToNext()) {
                try {
                    Contacts cObj = new Contacts();

                    try {
                        id = cu.getString(
                                cu.getColumnIndex(ContactsContract.Contacts._ID));
                    } catch (Exception e) {
                        System.out.println("Error with contact");
                        id = "NA";
                    }
                    try {
                        name = cu.getString(
                                cu.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    } catch (Exception e) {
                        System.out.println("Error with contact");
                        name = "NA";
                    }
                    cObj.setName(name);
                    if (Integer.parseInt(cu.getString(
                            cu.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = cr1.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);

                        List<Phone> ph_ns = new ArrayList<Phone>();
                        while (pCur.moveToNext()) {
                            Phone p =

                                    new Phone();
                            try {
                                ph_no = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            } catch (Exception e) {
                                System.out.println("Error with contact");
                                ph_no = "NA";
                            }
                            try {
                                typ = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                            } catch (Exception e) {
                                System.out.println("Error with contact");
                                typ = "NA";
                            }

                            //System.out.println("eefee"+typ);
                            p.setPhone(ph_no);
                            p.setPhone_type(typ);
                            ph_ns.add(p);

                            // Do something with phones
                        }
                        pCur.close();
                        cObj.setPh(ph_ns);

                    }

                    Cursor emailCur = cr1.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    List<Email> eMail = new ArrayList<Email>();
                    while (emailCur.moveToNext()) {
                        Email eObj = new Email();
                        // This would allow you get several email addresses
                        // if the email addresses were stored in an array
                        try {
                            email = emailCur.getString(
                                    emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        } catch (Exception e) {
                            email = "NA";
                            System.out.println("Error with contact");
                        }
                        try {
                            emailType = emailCur.getString(
                                    emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        } catch (Exception e) {
                            emailType = "NA";
                            System.out.println("Error with contact");
                        }
                        eObj.setemail(email);
                        eObj.setemail_type(emailType);
                        eMail.add(eObj);
                    }
                    cObj.setEm(eMail);
                    emailCur.close();
                    try {
                        noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    } catch (Exception e) {
                        noteWhere = "NA";
                        System.out.println("Error with contact");
                    }

                    noteWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};

                    Cursor noteCur = cr1.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
                    if (noteCur.moveToFirst()) {
                        try {
                            note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
                        } catch (Exception e) {
                            note = "NA";
                        }
                        System.out.println("eeee");
                    }
                    noteCur.close();
                    try {
                        orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    } catch (Exception e) {
                        orgWhere = "NA";
                        System.out.println("Error with contact");
                    }
                    orgWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                    Cursor orgCur = cr1.query(ContactsContract.Data.CONTENT_URI,
                            null, orgWhere, orgWhereParams, null);
                    if (orgCur.moveToFirst()) {
                        try {
                            orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                        } catch (Exception e) {
                            orgName = "NA";
                            System.out.println("Error with contact");
                        }
                        try {
                            title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                        } catch (Exception e) {
                            title = "NA";
                            System.out.println("Error with contact");
                        }
                        cObj.setOrg(orgName);
                    }
                    orgCur.close();
                    try {
                        addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    } catch (Exception e) {
                        addrWhere = "NA";
                        System.out.println("Error with contact");
                    }
                    addrWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
                    Cursor addrCur = cr1.query(ContactsContract.Data.CONTENT_URI,
                            null, addrWhere, addrWhereParams, null);
                    while (addrCur.moveToNext()) {
                        try {
                            poBox = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
                        } catch (Exception e) {
                            poBox = "NA";
                            System.out.println("Error with contact");
                        }
                        try {
                            street = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                        } catch (Exception e) {
                            street = "NA";
                            System.out.println("Error with contact");
                        }
                        try {
                            city = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                        } catch (Exception e) {
                            city = "NA";
                            System.out.println("Error with contact");
                        }
                        try {
                            state = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                        } catch (Exception e) {
                            state = "NA";
                            System.out.println("Error with contact");
                        }
                        try {
                            postalCode = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                        } catch (Exception e) {
                            postalCode = "NA";
                            System.out.println("Error with contact");
                        }
                        try {
                            country = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                        } catch (Exception e) {
                            country = "NA";
                            System.out.println("Error with contact");
                        }
                        try {
                            type = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
                        } catch (Exception e) {
                            type = "NA";
                            System.out.println("Error with contact");
                        }
                        cObj.setAddress(city + " " + state + " " + postalCode + " " + country + " " + type + " " + poBox + " " + street);
                    }
                    addrCur.close();
                    try {
                        nickwhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    } catch (Exception e) {
                        nickwhere = "NA";
                        System.out.println("Error with contact");
                    }
                    nickwhereparam = new String[]{id,
                            ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE};
                    Cursor nick = cr1.query(ContactsContract.Data.CONTENT_URI,
                            null, nickwhere, nickwhereparam, null);
                    if (nick.moveToFirst()) {
                        try {
                            nickname = nick.getString(nick.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.DATA));
                        } catch (Exception e) {
                            nickname = "NA";
                            System.out.println("Error with contact");
                        }
                        try {
                            System.out.println("swag" + id);
                        } catch (Exception e) {
                            System.out.println("Error with contact");
                        }
                        cObj.setNick(nickname);

                    }
                    nick.close();
                    con.add(cObj);

                } catch (Exception e) {
                    System.out.println("buddyErro" + e.toString());
                }

                batchcount1++;

                if (batchcount1 % 500 == 0) {
                    new SendContactToServer().execute(con);
                    lstSms.clear();
                    batchcount1 = 0;
                    break;
                }

            }
            //            int w=0;
            //            for(Contacts item:con)
            //            {
            //                if(w++%500==0)
            //                {
            //                    con2.add(item);
            //                    new SendContactToServer().execute(con2);
            //                }
            //            }
            new SendContactToServer().execute(con);
            //for(int i=0;i<batchcount1;i++)
            //{
            //    con2.add(con.get(i));
            //    if(i%500==0)
            //    {
            //        new SendContactToServer().execute(con2);
            //
            //        con2.clear();
            //
            //    }
            //}

            if (!con2.isEmpty()) {
                new SendContactToServer().execute(con2);
                con2.clear();
            }

            System.out.print(con + "dekkh");

        }


    }


    public void setAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 5000;
        long timeToset = 1459871688 + 21600;
        long curr = System.currentTimeMillis() / 1000;
        int w = 1;
        while (w == 1) {
            if (timeToset < curr) {
                timeToset += 24 * 60 * 60;
            } else
                w = 0;
        }
        ;//+21600000;
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeToset * 1000, 24 * 60 * 60 * 1000, pendingIntent);
        //Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void getAllSms() {
        JSONArray smsJ = new JSONArray();

        SMS objSms = new SMS();


        JSONObject rSms = new JSONObject();
        Uri message = Uri.parse("content://sms/inbox");
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        int totalSMS = c.getCount();
        int batchCount = 0;
        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new SMS();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                // objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }

                // String json=gson.toJson(objSms);
                lstSms.add(objSms);

                batchCount++;
                if (batchCount == 350) {
                    System.out.println("sm12a" + lstSms.get(0).getMsg());

                    lstSms.clear();
                    batchCount = 0;

                }
                c.moveToNext();
            }
        }

        if (batchCount > 0) {
        }
        SharedPreferences pref = getSharedPreferences("MyPref", 0);
        pref.edit().putBoolean("sms_sent", true).commit();
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();
        //c.close();


    }


    private void setUpPush() {
        //make sure we have google play services available,
        //without it we can't receive push notifications
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        return resultCode == ConnectionResult.SUCCESS;
    }
//    private void requestPermission() {
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this
//                , Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//            Toast.makeText(getApplicationContext(), "Accessing Location permission needed. Please allow in App Settings for better experience.", Toast.LENGTH_LONG).show();
//
//        } else {
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODEL);
//        }
//    }
//
//    private void requestPermissionPhoneState() {
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this
//                , Manifest.permission.READ_PHONE_STATE)) {
//
//            Toast.makeText(getApplicationContext(), "Mobile information needed. Please allow in App Settings for better experience.", Toast.LENGTH_LONG).show();
//
//        } else {
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODER);
//        }
//    }
    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int readSmsPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS);
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

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


//    private boolean checkPermission() {
//        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
//        if (result == PackageManager.PERMISSION_GRANTED) {
//
//            return true;
//
//        } else {
//            requestPermission();
//            return false;
//
//        }
//    }
//
//    private boolean checkPermissionPHONEState() {
//        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);
//        if (result == PackageManager.PERMISSION_GRANTED) {
//            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//            IMEINumber = telephonyManager.getDeviceId();
//            simSerialNumber = telephonyManager.getSimSerialNumber();
//            return true;
//
//        } else {
//            requestPermissionPhoneState();
//            return false;
//
//        }
//    }

}