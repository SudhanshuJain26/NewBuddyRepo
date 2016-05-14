package indwin.c3.shareapp;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;

public class Splash extends AppCompatActivity {
    int count = 0;
    ViewPager dealspager;
    static int checkfbid = 0, checklog = 0;
    static String urlauth = "";
    static HashMap<String, HashMap<String, String>> image;
    static HashMap<String, HashMap<String, String>> mrp1;
    static HashMap<String, HashMap<String, String>> fkid1;
    static HashMap<String, HashMap<String, String>> title;
    static HashMap<String, HashMap<String, String>> sellers;
    static HashMap<String, HashMap<String, String>> selling;
    static HashMap<String, HashMap<String, String>> category;
    static HashMap<String, HashMap<String, String>> subCategory;
    static HashMap<String, HashMap<String, String>> brand;
    private RelativeLayout sp;
    static int checkNot = 0;
    SharedPreferences sh, sh_otp;
    static int notify = 0;
    public static final String MyPREFERENCES = "buddy";
    SharedPreferences sharedpreferences, sharedpreferences2;
    String url2 = "";
    Intent intent;
    Uri data;
    private String cashBack,approvedBand="";

    String action = "", name = "", fbid = "", email = "", formstatus = "", uniqueCode = "", creditLimit = "", panoradhar = "", bankaccount = "", collegeid = "", verificationdate = "", rejectionReason = "", referral_code = "", token = "";
    //    String name="",email="",formstatus="",creditLimit="",referral_code="",token="";
    static String userId = "", pass = "", token1 = "";
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notify = 0;

        init();
        sh = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        sh_otp = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_splash);
        try {
            intent = getIntent();
            action = intent.getAction();
            data = intent.getData();
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        if (data != null) {
            String s=action;
            String t=data.toString();
           // Toast.makeText(Splash.this,""+data, Toast.LENGTH_LONG).show();
            Intent in=null;
            if(t.charAt(t.length()-2)=='r'){
                in=new Intent(Splash.this,Share.class);
            in.putExtra("cc", 2);
                finish();
                startActivity(in);}
            if (t.charAt(t.length()-2)=='l') {
                in = new Intent(Splash.this, Landing.class);
                in.putExtra("cc", 1);
                finish();
                startActivity(in);
            }



         //   Toast.makeText(Splash.this, action+""+data, Toast.LENGTH_LONG).show();
        }
//        Toast.makeText(Splash.this, "oncreate", Toast.LENGTH_LONG).show();

        sp = (RelativeLayout) findViewById(R.id.splash);
        ImageView logo = (ImageView) findViewById(R.id.buddyLogo);
        final Animation animationFadeIn = new AlphaAnimation(0, 1);
        animationFadeIn.setInterpolator(new AccelerateInterpolator());
        animationFadeIn.setStartOffset(500); // Start fading out after 500 milli seconds
        animationFadeIn.setDuration(1500);
//        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        logo.startAnimation(animationFadeIn);
        animationFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

//                Toast.makeText(Splash.this, "ccc1", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                url = getApplicationContext().getString(R.string.server) + "authenticate";
//                if(isNetworkAvailable())
                if (isNetworkAvailable())
                    new ItemsByKeyword().execute("");
                else {
                    Toast.makeText(Splash.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
//                new ItemsByKeyword().execute("");
//                else {
//
//                System.exit(0);
//                }
                // callother();


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private int checresume = 1, checresume1 = 1;

    @Override
    public void onResume() {

        super.onResume();
//        Toast.makeText(Splash.this, "onresume", Toast.LENGTH_LONG).show();
        if (checresume == 0) {
//            Toast.makeText(Splash.this, "onresume1", Toast.LENGTH_LONG).show();
            sp = (RelativeLayout) findViewById(R.id.splash);
            ImageView logo = (ImageView) findViewById(R.id.buddyLogo);
            final Animation animationFadeIn = new AlphaAnimation(0, 1);
            animationFadeIn.setInterpolator(new AccelerateInterpolator());
            animationFadeIn.setStartOffset(500); // Start fading out after 500 milli seconds
            animationFadeIn.setDuration(1500);
//        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
            logo.startAnimation(animationFadeIn);
            animationFadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

//                    Toast.makeText(Splash.this, "ccc", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    url = getApplicationContext().getString(R.string.server) + "authenticate";
                    urlauth = url;
                    if (isNetworkAvailable())
                        new ItemsByKeyword().execute("");
                    else {
                        Toast.makeText(Splash.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                    }
                    // callother();


                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        checresume = 0;
    }

    private class ItemsByKeyword extends
            AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
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
            if (result.equals("win")) {
                new CheckVersion().execute("");
                new trending().execute("Mobiles");
                new trending().execute("Computers&subCategory=Laptops");
                new trending().execute("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle");
                new trending().execute("Health%20and%20Beauty");
                new trending().execute("Electronics");
                new trending().execute("Footwear");


            }

        }
    }

    public class trending extends
            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
//            spinner.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... data) {

            String urldisplay = data[0];
//               HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: get
                String url = getApplicationContext().getString(R.string.server) + "api/product/trending?category=" + urldisplay;
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                // httppost.setHeader("x-access-token", tok_sp);
                // String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjY1M2M2YTUwZTQzNzgyNjc0M2YyNjYiLCJuYW1lIjoiYnVkZHkgYXBpIGFkbWluIiwidXNlcm5hbWUiOiJidWRkeWFwaWFkbWluIiwicGFzc3dvcmQiOiJtZW1vbmdvc2gxIiwiZW1haWwiOiJjYXJlQGhlbGxvYnVkZHkuaW4iLCJpYXQiOjE0NTY3MjY1NzMsImV4cCI6MTQ1Njc2MjU3M30.98mQFcYm5Uf3Fd7ZNPD-OwMIfObu7vfoq9zNtCCLfyI";
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);

                // url2=getApplicationContext().getString(R.string.server)+"api/login/verifyotp";
                if (urldisplay.equals("Computers&subCategory=Laptops"))
                    urldisplay = "Laptops";

                else if (urldisplay.equals("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle"))
                    urldisplay = "apparels";
                else if (urldisplay.equals("Health%20and%20Beauty"))
                    urldisplay = "homeandbeauty";
                HttpGet httppost = new HttpGet(url);
                httppost.setHeader("x-access-token", tok_sp);
                httppost.setHeader("Content-Type", "application/json");


                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");
                if (response.getStatusLine().getStatusCode() != 200) {

                    Log.e("MeshCommunication", "Server returned code "
                            + response.getStatusLine().getStatusCode());
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);
                    if (resp.getString("status").equals("success")) {
                        JSONArray data1 = new JSONArray(resp.getString("data"));
                        int lenght = data1.length();
                        for (int j = 0; j < lenght; j++) {
                            JSONObject js = data1.getJSONObject(j);
                            String categ = js.getString("category");
                            String subc = js.getString("subCategory");
                            String brand1 = js.getString("brand");
                            String id = js.getString("title");
                            String mrp = js.getString("mrp");
                            String seller = js.getString("seller");
                            String fkid = js.getString("fkProductId");
                            String selling_price = js.getString("sellingPrice");
                            JSONObject img = new JSONObject(js.getString("imgUrls"));
                            String imgurl = img.getString("200x200");
                            category.get(urldisplay).put(String.valueOf(j), categ);
                            subCategory.get(urldisplay).put(String.valueOf(j), subc);
                            brand.get(urldisplay).put(String.valueOf(j), brand1);
                            image.get(urldisplay).put(String.valueOf(j), imgurl);
                            mrp1.get(urldisplay).put(String.valueOf(j), mrp);
                            title.get(urldisplay).put(String.valueOf(j), id);
                            fkid1.get(urldisplay).put(String.valueOf(j), fkid);
                            selling.get(urldisplay).put(String.valueOf(j), selling_price);
                            sellers.get(urldisplay).put(String.valueOf(j), seller);
                            String p = id;
                        }
                        return "win";
                        //versioncode=data1.getString("version_code");
                        //return versioncode;
                    } else
                        return "fail";


                }
            } catch (Exception e) {
                String t = e.toString();
                return "fail";
            }
        }

        protected void onPostExecute(String result) {
            if (result.equals("fail")) {
                System.out.println("Error while computing data");
            } else {
            }
        }
    }

    private class CheckVersion extends
            AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST

                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
//                String url2="http://54.255.147.43:80/api/user/form?phone="+sh_otp.getString("number","");
                String url3 = getApplicationContext().getString(R.string.server) + "api/app/version?platform=android";

                HttpGet httppost = new HttpGet(url3);
                try {

                } catch (Exception e) {
                    System.out.println("dio " + e.toString());
                }
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                httppost.setHeader("x-access-token", tok_sp);


                httppost.setHeader("Content-Type", "application/json");


                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");
                String versioncode = "";
                if (response.getStatusLine().getStatusCode() != 200) {

                    Log.e("MeshCommunication", "Server returned code "
                            + response.getStatusLine().getStatusCode());
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);
                    if (resp.getString("status").equals("success")) {
                        JSONObject data1 = new JSONObject(resp.getString("data"));
                        versioncode = data1.getString("version_code");
                        return versioncode;
                    } else
                        return "fail";


                }
            } catch (Exception e) {
                return "fail";
            }
        }

        protected void onPostExecute(String result) {
            if (!result.equals("fail")) {
                PackageInfo pInfo = null;
                try {
                    pInfo = getPackageManager().getPackageInfo(
                            getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                int versionCode = pInfo.versionCode;
                if (Integer.parseInt(result) > versionCode) {
                    new AlertDialog.Builder(
                            Splash.this,
                            AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                            .setTitle("Update App Version")
                            .setCancelable(false)
                            .setMessage(
                                    "Looks like you are currently using an older version of our application. Please update your app and come back to check out our latest products and features.")
                            .setNegativeButton(
                                    "Exit",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // continue with
                                            // delete
                                            Intent intent = new Intent(
                                                    Intent.ACTION_MAIN);
                                            intent.addCategory(Intent.CATEGORY_HOME);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// ***Change
                                            // Here***
                                            startActivity(intent);
                                            finish();
                                            System.exit(0);
                                        }
                                    })
                            .setPositiveButton(
                                    "Update",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // continue with
                                            // delete
                                            Uri uri = Uri
                                                    .parse("https://play.google.com/store/apps/details?id=indwin.c3.shareapp&hl=en"); // missing
                                            // 'http://'
                                            //geuse
                                            // crashed
                                            Intent intent = new Intent(
                                                    Intent.ACTION_VIEW,
                                                    uri);
                                            startActivity(intent);
                                        }
                                    }).show();

                } else {
                    String tok1 = "";
                    SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorP = userP.edit();
                    token = userP.getString("tok", "");
                    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    sharedpreferences2 = getSharedPreferences("buddyin", Context.MODE_PRIVATE);
                    if (sharedpreferences.getInt("checklog", 0) == 1) {
                        userId = sharedpreferences2.getString("name", null);
                        pass = sharedpreferences2.getString("password", null);


                        tok1 = token;
                        try {
                            Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
                            Intercom.client().registerIdentifiedUser(
                                    new Registration().withUserId(userId));
                            //  Intercom.client().openGCMMessage(getIntent());

                        } catch (Exception e) {
                            System.out.println("Intercom eight" + e.toString());
                        }
                        // token=MainActivity.token;
                        // token=MainActivity.token;
                        url2 = getApplicationContext().getString(R.string.server) + "api/user/form?phone=" + userId;
                        new verifyOtp().execute("");
                    } else if (!sh_otp.getString("number", "").equals("")) {
                        token = tok1;
                        try {
                            Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
                            Intercom.client().registerIdentifiedUser(
                                    new Registration().withUserId(sh_otp.getString("number", "")));
                            Intercom.client().openGCMMessage(getIntent());
                        } catch (Exception e) {
                            System.out.println("Intercom nine" + e.toString());
                        }
                        url2 = getApplicationContext().getString(R.string.server) + "api/user/form?phone=" + sh_otp.getString("number", "");
                        new verifyOtp().execute("");
                    } else {
                        Intent in = new Intent(Splash.this, Landing.class);
                        finish();
                        startActivity(in);
                        overridePendingTransition(0, 0);

                    }
                }
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class verifyOtp extends
            AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... data) {

            // String urldisplay = data[0];
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST

                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
//                String url2="http://54.255.147.43:80/api/user/form?phone="+sh_otp.getString("number","");


                HttpGet httppost = new HttpGet(url2);
                try {

                } catch (Exception e) {
                    System.out.println("dio " + e.toString());
                }
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                httppost.setHeader("x-access-token", tok_sp);


                httppost.setHeader("Content-Type", "application/json");


                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");

                if (response.getStatusLine().getStatusCode() != 200) {

                    Log.e("MeshCommunication", "Server returned code "
                            + response.getStatusLine().getStatusCode());
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);
                    JSONObject data1 = new JSONObject(resp.getString("data"));
                    try {

                        referral_code = data1.getString("uniqueCode");
                        SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("rcode", referral_code);
                        editor.commit();
                        name = data1.getString("name");
                        email = data1.getString("email");
                    } catch (Exception e) {
                    }
                    try {
                        try {
                            creditLimit = data1.getString("creditLimit");
                        } catch (Exception e) {
                        }
                        try {
                            fbid = data1.getString("fbConnected");
                        } catch (Exception e) {
                            fbid = "empty";
                        }
                        if (fbid.equals("") || (fbid.equals("false")))
                            fbid = "empty";
                        try {
                            formstatus = data1.getString("formStatus");
                        } catch (Exception e) {
                            formstatus = "empty";
                        }
int cashBack=0;
                        try{
                            cashBack=data1.getInt("totalCashback");
                        }
                        catch(Exception e)
                        {
                            cashBack=0;
                        }
                        try{
                            approvedBand=data1.getString("approvedBand");
                        }
                        catch(Exception e)
                        {
                            approvedBand="";
                        }
                        int creditLimit=0;
                        try{
                            creditLimit=data1.getInt("creditLimit");
                        }
                        catch(Exception e)
                        {
                            creditLimit=0;
                        }
                        int totalBorrowed=0;
                        try{
                            totalBorrowed=data1.getInt("totalBorrowed");
                        }
                        catch(Exception e)
                        {
                            totalBorrowed=0;
                        }
                        SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorP = userP.edit();

                        editorP.putInt("creditLimit", creditLimit);
                        editorP.putInt("totalBorrowed", totalBorrowed);
                        editorP.putInt("cashBack", cashBack);
                        editorP.putString("approvedBand",approvedBand);
                        editorP.commit();
                        try {
                            rejectionReason = data1.getString("rejectionReason");
                        } catch (Exception e) {
                        }
                        if (formstatus.equals(""))
                            formstatus = "empty";
                        try {
                            panoradhar = data1.getString("addressProofs");
                        } catch (Exception e) {
                            panoradhar = "NA";
                        }
                        if (panoradhar.equals(""))
                            panoradhar = "NA";
                        try {
                            collegeid = data1.getString("collegeIDs");
                        } catch (Exception e) {
                            collegeid = "NA";
                        }
                        if (collegeid.equals(""))
                            collegeid = "NA";
                        try {
                            bankaccount = data1.getString("bankStatement");
                        } catch (Exception e) {
                            bankaccount = "NA";
                        }
                        if (bankaccount.equals(""))
                            bankaccount = "NA";
                        try {
                            verificationdate = data1.getString("collegeIdVerificationDate");
                        } catch (Exception e) {
                            verificationdate = "NA";
                        }
                        if (verificationdate.equals(""))
                            verificationdate = "NA";

                        try {
                            String dpid = data1.getString("fbUserId");
                            SharedPreferences sf = getSharedPreferences("proid", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor2 = sf.edit();
                            editor2.putString("dpid", dpid);

                            //  editor2.putString("password", password.getText().toString());
                            editor2.commit();
                        } catch (Exception e) {
                        }

                    } catch (Exception e) {
                                          }

                    if (resp.getString("msg").contains("error")) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return resp.getString("msg");
                    } else {

                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {

//                    inotp.putExtra("Name", mName);
//                    inotp.putExtra("Email",email.getText().toString());
//                    inotp.putExtra("College",college.getText().toString());
//                    inotp.putExtra("Phone",phone.getText().toString());

            if (result.equals("win")) {

                if (formstatus.equals("declined")) {

                    Intent in = new Intent(Splash.this, HomePage.class);
                    finish();
                    // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    in.putExtra("Name", name);
                    in.putExtra("fbid", fbid);
                    in.putExtra("Rej", rejectionReason);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC", uniqueCode);
                    startActivity(in);
                    overridePendingTransition(0, 0);

                }


                if (formstatus.equals("saved")) {

                    Intent in = new Intent(Splash.this, HomePage.class);
                    // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    finish();
                    in.putExtra("Name", name);
                    in.putExtra("fbid", fbid);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC", referral_code);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                } else if (formstatus.equals("submitted")) {
                    //                 Intent in = new Intent(MainActivity.this, Landing.class);
                    bankaccount = "some";
                    Intent in = new Intent(Splash.this, HomePage.class);
                    if ((panoradhar.equals("NA")) || (bankaccount.equals("NA")) || (collegeid.equals("NA"))) {
                        in.putExtra("screen_no", 1);
                    } else if ((!panoradhar.equals("NA")) && (!collegeid.equals("NA")) && (!bankaccount.equals("NA")) && (verificationdate.equals("NA"))) {
                        in.putExtra("screen_no", 2);
                    } else if ((!panoradhar.equals("NA")) && (!collegeid.equals("NA")) && (!bankaccount.equals("NA")) && (!verificationdate.equals("NA"))) {
                        in.putExtra("screen_no", 3);
                        in.putExtra("VeriDate", verificationdate);
                    }

                    finish();
                    in.putExtra("Name", name);
                    in.putExtra("fbid", fbid);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC", referral_code);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                }
                if (formstatus.equals("approved") || (formstatus.equals("flashApproved"))) {

                    Intent in = new Intent(Splash.this, HomePage.class);
                    finish();
                    // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    in.putExtra("Name", name);
                    in.putExtra("fbid", fbid);
                    in.putExtra("Email", email);//  in.putExtra("Credits",creditLimit);
                    in.putExtra("Form", formstatus);
                    in.putExtra("Credits", creditLimit);
                    in.putExtra("UniC", referral_code);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                } else if (formstatus.equals("empty")) {
//                    Intent in = new Intent(MainActivity.this, Inviteform    .class);
                    Intent in = new Intent(Splash.this, HomePage.class);
                    finish();
                    in.putExtra("Name", name);
                    in.putExtra("Email", email);
                    in.putExtra("fbid", fbid);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC", referral_code);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                }
            } else {
                Intent in = new Intent(Splash.this, MainActivity.class);
                finish();
                startActivity(in);
                overridePendingTransition(0, 0);
            }


        }
    }

    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        return resultCode == ConnectionResult.SUCCESS;
    }

    private void init() {

        selling = new HashMap<String, HashMap<String, String>>();
        selling.put("Mobiles", new HashMap<String, String>());
        selling.put("Laptops", new HashMap<String, String>());
        selling.put("apparels", new HashMap<String, String>());
        selling.put("Electronics", new HashMap<String, String>());
        selling.put("Footwear", new HashMap<String, String>());
        selling.put("homeandbeauty", new HashMap<String, String>());
        subCategory = new HashMap<String, HashMap<String, String>>();
        subCategory.put("Mobiles", new HashMap<String, String>());
        subCategory.put("Laptops", new HashMap<String, String>());
        subCategory.put("apparels", new HashMap<String, String>());
        subCategory.put("Electronics", new HashMap<String, String>());

        subCategory.put("Footwear", new HashMap<String, String>());
        subCategory.put("homeandbeauty", new HashMap<String, String>());

        image = new HashMap<String, HashMap<String, String>>();
        image.put("Mobiles", new HashMap<String, String>());
        image.put("Laptops", new HashMap<String, String>());
        image.put("apparels", new HashMap<String, String>());
        image.put("Electronics", new HashMap<String, String>());
        image.put("Footwear", new HashMap<String, String>());
        image.put("homeandbeauty", new HashMap<String, String>());
        brand = new HashMap<String, HashMap<String, String>>();
        brand.put("Mobiles", new HashMap<String, String>());
        brand.put("Laptops", new HashMap<String, String>());
        brand.put("apparels", new HashMap<String, String>());
        brand.put("Electronics", new HashMap<String, String>());

        brand.put("Footwear", new HashMap<String, String>());
        brand.put("homeandbeauty", new HashMap<String, String>());
        category = new HashMap<String, HashMap<String, String>>();
        category.put("Mobiles", new HashMap<String, String>());
        category.put("Laptops", new HashMap<String, String>());
        category.put("apparels", new HashMap<String, String>());
        category.put("Electronics", new HashMap<String, String>());

        category.put("Footwear", new HashMap<String, String>());
        category.put("homeandbeauty", new HashMap<String, String>());
        sellers = new HashMap<String, HashMap<String, String>>();
        sellers.put("Mobiles", new HashMap<String, String>());
        sellers.put("Laptops", new HashMap<String, String>());
        sellers.put("apparels", new HashMap<String, String>());
        sellers.put("Electronics", new HashMap<String, String>());
        sellers.put("Footwear", new HashMap<String, String>());
        sellers.put("homeandbeauty", new HashMap<String, String>());


        mrp1 = new HashMap<String, HashMap<String, String>>();
        mrp1.put("Mobiles", new HashMap<String, String>());
        mrp1.put("Laptops", new HashMap<String, String>());
        mrp1.put("apparels", new HashMap<String, String>());
        mrp1.put("Electronics", new HashMap<String, String>());
        mrp1.put("Footwear", new HashMap<String, String>());
        mrp1.put("homeandbeauty", new HashMap<String, String>());
        fkid1 = new HashMap<String, HashMap<String, String>>();
        fkid1.put("Mobiles", new HashMap<String, String>());
        fkid1.put("Laptops", new HashMap<String, String>());
        fkid1.put("apparels", new HashMap<String, String>());
        fkid1.put("Electronics", new HashMap<String, String>());
        fkid1.put("Footwear", new HashMap<String, String>());
        fkid1.put("homeandbeauty", new HashMap<String, String>());
        title = new HashMap<String, HashMap<String, String>>();
        title.put("Mobiles", new HashMap<String, String>());
        title.put("Laptops", new HashMap<String, String>());
        title.put("apparels", new HashMap<String, String>());
        title.put("Electronics", new HashMap<String, String>());
        title.put("Footwear", new HashMap<String, String>());
        title.put("homeandbeauty", new HashMap<String, String>());
    }
}
