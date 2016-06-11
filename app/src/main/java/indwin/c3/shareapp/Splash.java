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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import indwin.c3.shareapp.activities.ProfileActivity;
import indwin.c3.shareapp.application.BuddyApplication;
import indwin.c3.shareapp.models.Image;
import indwin.c3.shareapp.models.TrendingMapWrapper;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.CheckInternetAndUploadUserDetails;
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
    public static int checkNot = 0;
    SharedPreferences sh, sh_otp;

    public static int notify = 0;

    public static final String MyPREFERENCES = "buddy";
    SharedPreferences sharedpreferences, sharedpreferences2;
    String url2 = "";
    Intent intent;
    Uri data;

    private String cashBack, approvedBand = "";
    Gson gson;


    String action = "", name = "", fbid = "", email = "", formstatus = "", uniqueCode = "", creditLimit = "", panoradhar = "", bankaccount = "", collegeid = "", verificationdate = "", rejectionReason = "", referral_code = "", token = "";
    //    String name="",email="",formstatus="",creditLimit="",referral_code="",token="";
    static String userId = "", pass = "", token1 = "";
    private String url;

    private Tracker mTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notify = 0;

        SharedPreferences t1 = getSharedPreferences("cred", Context.MODE_PRIVATE);
        SharedPreferences.Editor e1 = t1.edit();
        e1.putInt("add", 0);
        e1.commit();
        BuddyApplication application = (BuddyApplication) getApplication();
        mTracker = application.getDefaultTracker();
        gson = new Gson();
        init();


        sh = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        String json = sh.getString("UserObject", "");
        setNewIdsNull(json);
        migrateImages();
        json = sh.getString("UserObject", "");
        boolean isUpdatingDB = sh.getBoolean("updatingDB", false);
        if (isUpdatingDB) {
            sh.edit().putBoolean("updatingDB", false).commit();
            Intent intent = new Intent(this, CheckInternetAndUploadUserDetails.class);
            sendBroadcast(intent);
        }

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

            String s = action;
            String t = data.toString();
            // Toast.makeText(Splash.this,""+data, Toast.LENGTH_LONG).show();
            Intent in = null;
            if (t.charAt(t.length() - 2) == 'r') {
                in = new Intent(Splash.this, Share.class);
                in.putExtra("cc", 2);
                finish();
                startActivity(in);
            }
            if (t.contains("profile")) {

                in = new Intent(Splash.this, ProfileActivity.class);
                in.putExtra("cc", 1);
                finish();
                startActivity(in);
            }


        }

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

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                url = getApplicationContext().getString(R.string.server) + "authenticate";

                //                if(isNetworkAvailable())
                if (isNetworkAvailable())
                    new ItemsByKeyword().execute("");
                else {
                    String wrapperString = sh.getString("TrendingProductsSerialized", "");
                    if (!"".equals(wrapperString)) {
                        TrendingMapWrapper wrapper = gson.fromJson(wrapperString, TrendingMapWrapper.class);
                        category = wrapper.getCategory();
                        subCategory = wrapper.getSubCategory();
                        brand = wrapper.getBrand();
                        mrp1 = wrapper.getMrp1();
                        fkid1 = wrapper.getFkid1();
                        sellers = wrapper.getSellers();
                        selling = wrapper.getSelling();
                        title = wrapper.getTitle();
                        image = wrapper.getImage();
                        Intent in = new Intent(Splash.this, Landing.class);
                        finish();
                        startActivity(in);
                        overridePendingTransition(0, 0);
                    }
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


    private void setNewIdsNull(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.optJSONArray("newAddressProofs") != null) {
                jsonObject.put("newAddressProofs", null);
            }
            if (jsonObject.optJSONArray("newBankProofs") != null) {
                jsonObject.put("newBankProofs", null);
            }
            if (jsonObject.optJSONArray("newBankStmts") != null) {
                jsonObject.put("newBankStmts", null);
            }
            if (jsonObject.optJSONArray("newCollegeIds") != null) {
                jsonObject.put("newCollegeIds", null);
            }

            AppUtils.saveInSharedPrefs(this, AppUtils.USER_OBJECT, jsonObject.toString());


        } catch (Exception e) {


        }
    }


    private void migrateImages() {
        UserModel userModel = AppUtils.getUserObject(this);
        if (userModel != null) {
            if (userModel.getCollegeIds() != null && userModel.getCollegeIds().size() > 0) {
                if (userModel.getCollegeID() == null) {
                    Image image = new Image();
                    image.setImgUrls(userModel.getCollegeIds());
                    userModel.setCollegeID(image);
                } else {
                    userModel.getCollegeID().setImgUrls(userModel.getCollegeIds());
                }
            }

            if (userModel.getBankStmts() != null && userModel.getBankStmts().size() > 0) {
                if (userModel.getBankStatement() == null) {
                    Image image = new Image();
                    image.setImgUrls(userModel.getBankStmts());
                    userModel.setBankStatement(image);
                } else {
                    userModel.getBankStatement().setImgUrls(userModel.getBankStmts());
                }
            }
            if (userModel.getBankProofs() != null && userModel.getBankProofs().size() > 0) {
                if (userModel.getBankProof() == null) {
                    Image image = new Image();
                    image.setImgUrls(userModel.getBankProofs());
                    userModel.setBankProof(image);
                } else {
                    userModel.getBankProof().setImgUrls(userModel.getBankProofs());
                }
            }
            if (userModel.getAddressProofs() != null && userModel.getAddressProofs().size() > 0) {
                if (userModel.getAddressProof() == null) {
                    Image image = new Image();
                    image.setImgUrls(userModel.getAddressProofs());
                    userModel.setAddressProof(image);
                } else {
                    userModel.getAddressProof().setImgUrls(userModel.getAddressProofs());
                }
            }

            userModel.setBankStmts(new ArrayList<String>());
            userModel.setAddressProofs(new ArrayList<String>());
            userModel.setCollegeIds(new ArrayList<String>());
            userModel.setBankProofs(new ArrayList<String>());
            AppUtils.saveUserObject(this, userModel);
        }
    }

    @Override
    public void onResume() {

        super.onResume();


        AppUtils.sendGoogleAnalytics((BuddyApplication) getApplication());

        //        Toast.makeText(Splash.this, "onresume", Toast.LENGTH_LONG).show();
        if (checresume == 0) {
            //            Toast.makeText(Splash.this, "onresume1", Toast.LENGTH_LONG).show();

            sp = (RelativeLayout) findViewById(R.id.splash);
            ImageView logo = (ImageView) findViewById(R.id.buddyLogo);
            final Animation animationFadeIn = new AlphaAnimation(0, 1);
            animationFadeIn.setInterpolator(new AccelerateInterpolator());
            animationFadeIn.setStartOffset(500); // Start fading out after 500 milli seconds
            animationFadeIn.setDuration(1500);

            logo.startAnimation(animationFadeIn);
            animationFadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {


                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    url = getApplicationContext().getString(R.string.server) + "authenticate";
                    urlauth = url;
                    if (isNetworkAvailable())
                        new ItemsByKeyword().execute("");
                    else {

                        String wrapperString = sh.getString("TrendingProductsSerialized", "");
                        if (!"".equals(wrapperString)) {
                            TrendingMapWrapper wrapper = gson.fromJson(wrapperString, TrendingMapWrapper.class);
                            category = wrapper.getCategory();
                            subCategory = wrapper.getSubCategory();
                            brand = wrapper.getBrand();
                            mrp1 = wrapper.getMrp1();
                            fkid1 = wrapper.getFkid1();
                            sellers = wrapper.getSellers();
                            selling = wrapper.getSelling();
                            title = wrapper.getTitle();
                            image = wrapper.getImage();
                            Intent in = new Intent(Splash.this, Landing.class);
                            finish();
                            startActivity(in);
                            overridePendingTransition(0, 0);
                        }

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


                HttpResponse response = AppUtils.connectToServerPost(url, null, null);
                if (response != null) {
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
                }
                {
                    return "fail";

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
                new GetTrendingProducts().execute("trending");


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
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: get
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
//                if (urldisplay.equals("Computers&subCategory=Laptops"))
//                    urldisplay = "Laptops";
//
//                else if (urldisplay.equals("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle"))
//                    urldisplay = "apparels";
//                else if (urldisplay.equals("Health%20and%20Beauty"))
//                    urldisplay = "homeandbeauty";
                String url = getApplicationContext().getString(R.string.server) + "api/product/trending?category=" + urldisplay;

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


                            }
                            Log.i("trending", "called");
                            return "win";
                            //versioncode=data1.getString("version_code");
                            //return versioncode;
                        } else
                            return "fail";


                    }
                } else {
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

                TrendingMapWrapper mapWrapper = new TrendingMapWrapper();
                mapWrapper.setCategory(category);
                mapWrapper.setBrand(brand);
                mapWrapper.setFkid1(fkid1);
                mapWrapper.setImage(image);
                mapWrapper.setMrp1(mrp1);
                mapWrapper.setSellers(sellers);
                mapWrapper.setSelling(selling);
                mapWrapper.setSubCategory(subCategory);
                mapWrapper.setTitle(title);
                String json = gson.toJson(mapWrapper);
                sh.edit().putString("TrendingProductsSerialized", json).apply();

            }
        }
    }

    private class GetTrendingProducts extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String url = getApplicationContext().getResources().getString(R.string.server)+"api/product/trending?category=Computers&category=Mobiles&count=10";
            String urldisplay = params[0];
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
                            JSONArray data1 = new JSONArray(resp.getString("data"));
                            int lenght = data1.length();
                            for (int j = 0; j < 10; j++) {
                                JSONObject js = data1.getJSONObject(j);
                                String categ = js.getString("category");
                                String subc = js.getString("subCategory");
                                String brand1 = js.getString("brand");
                                String id = js.getString("title");
                                //String mrp = js.getString("mrp");
                                String seller = js.getString("seller");
                                String fkid = js.getString("fkProductId");
                                String selling_price = js.getString("sellingPrice");
                                JSONObject img = new JSONObject(js.getString("imgUrls"));
                                String imgurl = img.getString("200x200");
                                category.get(urldisplay).put(String.valueOf(j), categ);
                                subCategory.get(urldisplay).put(String.valueOf(j), subc);
                                brand.get(urldisplay).put(String.valueOf(j), brand1);
                                image.get(urldisplay).put(String.valueOf(j), imgurl);
                                //mrp1.get(urldisplay).put(String.valueOf(j), mrp);
                                title.get(urldisplay).put(String.valueOf(j), id);
                                fkid1.get(urldisplay).put(String.valueOf(j), fkid);
                                selling.get(urldisplay).put(String.valueOf(j), selling_price);
                                sellers.get(urldisplay).put(String.valueOf(j), seller);


                            }
                            Log.i("trending", "called");
                            return "win";
                            //versioncode=data1.getString("version_code");
                            //return versioncode;
                        } else
                            return "fail";


                    }
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }
    }


    public class CheckVersion extends AsyncTask<String, Void, String> {


            @Override
            protected String doInBackground(String... params) {
                try {

                    String url3 = getApplicationContext().getString(R.string.server) + "api/app/version?platform=android";
                    SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                    String tok_sp = toks.getString("token_value", "");


                    HttpResponse response = AppUtils.connectToServerGet(url3, tok_sp, null);
                    if (response != null) {
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
                    } else {
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

                            new ValidateForm().execute("");

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

                            new ValidateForm().execute("");

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

        public boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }


        private class ValidateForm extends
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

                    SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                    String tok_sp = toks.getString("token_value", "");


                    HttpResponse response = AppUtils.connectToServerGet(url2, tok_sp, null);
                    if (response != null) {
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
                                Gson gson = new Gson();
                                String json = sh.getString("UserObject", "");
                                UserModel user = gson.fromJson(json, UserModel.class);
                                if (user == null)
                                    user = new UserModel();
                                name = data1.getString("name");
                                email = data1.getString("email");
                                user.setName(name);
                                user.setEmail(email);
                                if (!data1.getBoolean("offlineForm"))
                                    checkDataForNormalUser(user, gson, data1);
                                else
                                    checkDataForOfflineUser(user, gson, data1);
                                json = gson.toJson(user);
                                sh.edit().putString("UserObject", json).apply();
                                name = data1.getString("name");
                                email = data1.getString("email");
                                SharedPreferences sharedpreferences11 = getSharedPreferences("cred", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor11 = sharedpreferences11.edit();
                                editor11.putString("n1", name);
                                editor11.putString("e1", email);
                                editor11.commit();
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
                                int totalCashBack = 0;
                                try {
                                    totalCashBack = data1.getInt("totalCashback");
                                } catch (Exception e) {
                                    totalCashBack = 0;
                                }
                                try {
                                    approvedBand = data1.getString("approvedBand");
                                } catch (Exception e) {
                                    approvedBand = "";
                                }
                                int creditLimit = 0;
                                try {
                                    creditLimit = data1.getInt("creditLimit");
                                } catch (Exception e) {
                                    creditLimit = 0;
                                }
                                int totalBorrowed = 0;
                                try {
                                    totalBorrowed = data1.getInt("totalBorrowed");
                                } catch (Exception e) {
                                    totalBorrowed = 0;
                                }
                                String nameadd = "";
                                String courseend = "";

                                try {
                                    courseend = data1.getString("courseCompletionDate");
                                } catch (Exception e) {
                                    courseend = "";
                                }

                                try {
                                    nameadd = data1.getString("college");
                                } catch (Exception e) {
                                }
                                String profileStatus = "";
                                try {
                                    profileStatus = data1.getString("profileStatus");
                                } catch (Exception e) {
                                    profileStatus = "";
                                }
                                SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editorP = userP.edit();

                                editorP.putInt("creditLimit", creditLimit);
                                editorP.putString("profileStatus", profileStatus);
                                editorP.putInt("totalBorrowed", totalBorrowed);
                                editorP.putString("course", courseend);
                                editorP.putInt("cashBack", totalCashBack);
                                editorP.putString("nameadd", nameadd);
                                editorP.putString("formStatus", formstatus);
                                editorP.putString("approvedBand", approvedBand);
                                editorP.putString("productdpname", name);
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
                    } else {
                        return "fail";

                    }

                } catch (Exception e) {
                    Log.e("mesherror", e.getMessage());
                    return "fail";

                }
            }

            protected void onPostExecute(String result) {


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
                    } else {

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

    private void checkDataForNormalUser(UserModel user, Gson gson, JSONObject data1) {
        try {
            Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
            Intercom.client().registerIdentifiedUser(
                    new Registration().withUserId(user.getUserId()));


        } catch (Exception e) {
        }
        AppUtils.checkDataForNormalUser(user, gson, data1);
        //    user.setEmailSent(false);
        //    if (data1.opt("gender") != null)
        //        user.setGender(data1.getString("gender"));
        //    if (data1.opt("approvedBand") != null)
        //        user.setApprovedBand(data1.getString("approvedBand"));
        //    if (data1.opt("creditLimit") != null)
        //        user.setCreditLimit(data1.getInt("creditLimit"));
        //    if (data1.opt("totalBorrowed") != null) {
        //        if (user.getCreditLimit() == 0) {
        //            user.setAvailableCredit(0);
        //        } else {
        //            int available = user.getCreditLimit() - data1.getInt("totalBorrowed");
        //            if (available > 0) {
        //                user.setAvailableCredit(available);
        //            } else {
        //                user.setAvailableCredit(0);
        //            }
        //        }
        //    } else {
        //        user.setAvailableCredit(user.getCreditLimit());
        //    }
        //    if (data1.opt("totalCashback") != null)
        //        user.setCashBack(data1.getInt("totalCashback"));
        //    if (data1.opt("emailVerified") != null)
        //        user.setEmailVerified(data1.getBoolean("emailVerified"));
        //    if (data1.opt("formStatus") != null)
        //        user.setFormStatus(data1.getString("formStatus"));
        //    Map userMap = new HashMap<>();
        //    if (data1.opt("profileStatus") != null) {
        //        user.setProfileStatus(data1.getString("profileStatus"));
        //        userMap.put("profileStatus", user.getProfileStatus());
        //    }
        //    if (data1.opt("status1K") != null) {
        //        String status1K = data1.getString("status1K");
        //        user.setStatus1K(status1K);
        //        userMap.put("status1K", status1K);
        //        if (Constants.STATUS.DECLINED.toString().equals(status1K) || Constants.STATUS.APPLIED.toString().equals(status1K) || Constants.STATUS.APPROVED.toString().equals(status1K))
        //            user.setAppliedFor1k(true);
        //        else user.setAppliedFor1k(false);
        //    }
        //    if (data1.opt("status7K") != null) {
        //        String status7K = data1.getString("status7K");
        //        user.setStatus7K(status7K);
        //        userMap.put("status7K", status7K);
        //        if (Constants.STATUS.DECLINED.toString().equals(status7K) || Constants.STATUS.APPLIED.toString().equals(status7K) || Constants.STATUS.APPROVED.toString().equals(status7K))
        //            user.setAppliedFor7k(true);
        //        else user.setAppliedFor7k(false);
        //    }
        //    if (data1.opt("status60K") != null) {
        //        String status60K = data1.getString("status60K");
        //        user.setStatus60K(status60K);
        //        userMap.put("status60K", status60K);
        //        if (Constants.STATUS.DECLINED.toString().equals(status60K) || Constants.STATUS.APPLIED.toString().equals(status60K) || Constants.STATUS.APPROVED.toString().equals(status60K))
        //            user.setAppliedFor60k(true);
        //        else user.setAppliedFor60k(false);
        //    }
        //    Intercom.client().updateUser(userMap);
        //    if (data1.opt("fbConnected") != null)
        //        user.setIsFbConnected(Boolean.parseBoolean(data1.getString("fbConnected")));
        //    if (data1.opt("college") != null)
        //        user.setCollegeName(data1.getString("college"));
        //    if (data1.opt("course") != null)
        //        user.setCourseName(data1.getString("course"));
        //    if (data1.opt("courseCompletionDate") != null)
        //        user.setCourseEndDate(data1.getString("courseCompletionDate"));
        //    if (data1.opt("collegeIDs") != null)
        //        user.setCollegeIds(gson.fromJson(data1.getString("collegeIDs"), ArrayList.class));
        //    if (data1.opt("addressProofs") != null)
        //        user.setAddressProofs(gson.fromJson(data1.getString("addressProofs"), ArrayList.class));
        //
        //    if (data1.opt("dob") != null)
        //        user.setDob(data1.getString("dob"));
        //    if (data1.opt("accomodation") != null)
        //        user.setAccommodation(data1.getString("accomodation"));
        //
        //
        //    if (data1.opt("panOrAadhar") != null) {
        //        user.setPanOrAadhar(data1.getString("panOrAadhar"));
        //        if ("PAN".equals(user.getPanOrAadhar()))
        //            user.setPanNumber(data1.getString("pan"));
        //        else
        //            user.setAadharNumber(data1.getString("aadhar"));
        //    }
        //    if (data1.opt("currentAddress") != null)
        //        user.setCurrentAddress(data1.getJSONObject("currentAddress").getString("line1"));
        //    if (data1.opt("permanentAddress") != null)
        //        user.setPermanentAddress(data1.getJSONObject("permanentAddress").getString("line1"));
        //    if (data1.opt("bankAccountNumber") != null)
        //        user.setBankAccNum(data1.getString("bankAccountNumber"));
        //    if (data1.opt("bankIFSC") != null)
        //        user.setBankIfsc(data1.getString("bankIFSC"));
        //    if (data1.opt("rollNumber") != null)
        //        user.setRollNumber(data1.getString("rollNumber"));
        //    if (data1.opt("rejectionReason") != null)
        //        user.setRejectionReason(data1.getString("rejectionReason"));
        //    if (data1.optJSONArray("familyMember") != null) {
        //        JSONArray familyMembers = data1.getJSONArray("familyMember");
        //        for (int i = 0; i < familyMembers.length(); i++) {
        //            JSONObject familyJson = familyMembers.getJSONObject(i);
        //            if (i == 1) {
        //                if (familyJson.getString("relation") != null) {
        //                    user.setFamilyMemberType2(familyJson.getString("relation"));
        //                    user.setProfessionFamilyMemberType2(familyJson.getString("occupation"));
        //                    user.setPhoneFamilyMemberType2(familyJson.getString("phone"));
        //                    if (familyJson.opt("preferredLanguage") != null)
        //                        user.setPrefferedLanguageFamilyMemberType2(familyJson.getString("preferredLanguage"));
        //                }
        //            } else {
        //                if (familyJson.getString("relation") != null) {
        //                    user.setFamilyMemberType1(familyJson.getString("relation"));
        //                    user.setProfessionFamilyMemberType1(familyJson.getString("occupation"));
        //                    user.setPhoneFamilyMemberType1(familyJson.getString("phone"));
        //                    if (familyJson.opt("preferredLanguage") != null)
        //                        user.setPrefferedLanguageFamilyMemberType1(familyJson.getString("preferredLanguage"));
        //                }
        //            }
        //        }
        //    }
        //
        //
        //    if (data1.opt("friendName") != null)
        //        user.setClassmateName(data1.getString("friendName"));
        //    if (data1.opt("friendNumber") != null)
        //        user.setClassmatePhone(data1.getString("friendNumber"));
        //    if (data1.opt("collegeIdVerificationDate") != null)
        //        user.setVerificationDate(data1.getString("collegeIdVerificationDate"));
        //    if (data1.opt("annualFees") != null)
        //        user.setAnnualFees(data1.getString("annualFees"));
        //    if (data1.opt("scholarship") != null)
        //        user.setScholarship(String.valueOf(data1.getString("scholarship")));
        //    if (data1.opt("scholarshipProgram") != null)
        //        user.setScholarshipType(data1.getString("scholarshipProgram"));
        //    if (data1.opt("scholarshipAmount") != null)
        //        user.setScholarshipAmount(data1.getString("scholarshipAmount"));
        //    if (data1.opt("takenLoan") != null)
        //        user.setStudentLoan(data1.getString("takenLoan"));
        //    if (data1.opt("monthlyExpense") != null)
        //        user.setMonthlyExpenditure(data1.getString("monthlyExpense"));
        //    if (data1.opt("ownVehicle") != null)
        //        user.setVehicle(String.valueOf(data1.getString("ownVehicle")));
        //    if (data1.opt("vehicleType") != null)
        //        user.setVehicleType(data1.getString("vehicleType"));
        //    if (data1.opt("gpa") != null) {
        //
        //        user.setGpa(data1.getString("gpa"));
        //    }
        //    if (data1.opt("gpaType") != null) {
        //
        //        user.setGpaType(data1.getString("gpaType"));
        //    }
        //    if (data1.opt("bankStatements") != null)
        //        user.setBankStmts(gson.fromJson(data1.getString("bankStatements"), ArrayList.class));
        //    if (data1.opt("bankProofs") != null)
        //        user.setBankProofs(gson.fromJson(data1.getString("bankProofs"), ArrayList.class));
        //    if (data1.opt("selfie") != null) {
        //        JSONArray array = data1.getJSONArray("selfie");
        //        user.setSelfie((String) array.get(0));
        //    } else {
        //        user.setSelfie("");
        //    }
        //    if (data1.opt("signature") != null) {
        //        JSONArray array = data1.getJSONArray("signature");
        //        user.setSignature((String) array.get(0));
        //    } else {
        //        user.setSignature("");
        //    }
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
    }


        private void checkDataForOfflineUser(UserModel user, Gson gson, JSONObject data1) {
            try {
                sh.edit().putBoolean("visitedFormStep1Fragment1", true).apply();
                sh.edit().putBoolean("visitedFormStep1Fragment2", true).apply();
                sh.edit().putBoolean("visitedFormStep1Fragment3", true).apply();
                checkDataForNormalUser(user, gson, data1);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        public boolean checkPlayServices() {
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            return resultCode == ConnectionResult.SUCCESS;
        }

        public void init() {

            selling = new HashMap<String, HashMap<String, String>>();
            selling.put("Mobiles", new HashMap<String, String>());
            selling.put("Computers&subCategory=Laptops", new HashMap<String, String>());
            selling.put("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle", new HashMap<String, String>());
            selling.put("Electronics", new HashMap<String, String>());
            selling.put("Footwear", new HashMap<String, String>());
            selling.put("Health%20and%20Beauty", new HashMap<String, String>());
            selling.put("trending", new HashMap<String, String>());
            subCategory = new HashMap<String, HashMap<String, String>>();
            subCategory.put("Mobiles", new HashMap<String, String>());
            subCategory.put("Computers&subCategory=Laptops", new HashMap<String, String>());
            subCategory.put("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle", new HashMap<String, String>());
            subCategory.put("Electronics", new HashMap<String, String>());

            subCategory.put("Footwear", new HashMap<String, String>());
            subCategory.put("Health%20and%20Beauty", new HashMap<String, String>());
            subCategory.put("trending", new HashMap<String, String>());

            image = new HashMap<String, HashMap<String, String>>();
            image.put("Mobiles", new HashMap<String, String>());
            image.put("Computers&subCategory=Laptops", new HashMap<String, String>());
            image.put("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle", new HashMap<String, String>());
            image.put("Electronics", new HashMap<String, String>());
            image.put("Footwear", new HashMap<String, String>());
            image.put("Health%20and%20Beauty", new HashMap<String, String>());
            image.put("trending", new HashMap<String, String>());
            brand = new HashMap<String, HashMap<String, String>>();
            brand.put("Mobiles", new HashMap<String, String>());
            brand.put("Computers&subCategory=Laptops", new HashMap<String, String>());
            brand.put("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle", new HashMap<String, String>());
            brand.put("Electronics", new HashMap<String, String>());

            brand.put("Footwear", new HashMap<String, String>());
            brand.put("Health%20and%20Beauty", new HashMap<String, String>());
            brand.put("trending", new HashMap<String, String>());
            category = new HashMap<String, HashMap<String, String>>();
            category.put("Mobiles", new HashMap<String, String>());
            category.put("Computers&subCategory=Laptops", new HashMap<String, String>());
            category.put("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle", new HashMap<String, String>());
            category.put("Electronics", new HashMap<String, String>());
            category.put("trending", new HashMap<String, String>());
            category.put("Footwear", new HashMap<String, String>());
            category.put("Health%20and%20Beauty", new HashMap<String, String>());
            sellers = new HashMap<String, HashMap<String, String>>();
            sellers.put("Mobiles", new HashMap<String, String>());
            sellers.put("Computers&subCategory=Laptops", new HashMap<String, String>());
            sellers.put("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle", new HashMap<String, String>());
            sellers.put("Electronics", new HashMap<String, String>());
            sellers.put("Footwear", new HashMap<String, String>());
            sellers.put("Health%20and%20Beauty", new HashMap<String, String>());
            sellers.put("trending", new HashMap<String, String>());


            mrp1 = new HashMap<String, HashMap<String, String>>();
            mrp1.put("Mobiles", new HashMap<String, String>());
            mrp1.put("Computers&subCategory=Laptops", new HashMap<String, String>());
            mrp1.put("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle", new HashMap<String, String>());
            mrp1.put("Electronics", new HashMap<String, String>());
            mrp1.put("Footwear", new HashMap<String, String>());
            mrp1.put("Health%20and%20Beauty", new HashMap<String, String>());
            mrp1.put("trending", new HashMap<String, String>());
            fkid1 = new HashMap<String, HashMap<String, String>>();
            fkid1.put("Mobiles", new HashMap<String, String>());
            fkid1.put("Computers&subCategory=Laptops", new HashMap<String, String>());
            fkid1.put("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle", new HashMap<String, String>());
            fkid1.put("Electronics", new HashMap<String, String>());
            fkid1.put("Footwear", new HashMap<String, String>());
            fkid1.put("Health%20and%20Beauty", new HashMap<String, String>());
            fkid1.put("trending", new HashMap<String, String>());
            title = new HashMap<String, HashMap<String, String>>();
            title.put("Mobiles", new HashMap<String, String>());
            title.put("Computers&subCategory=Laptops", new HashMap<String, String>());
            title.put("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle", new HashMap<String, String>());
            title.put("Electronics", new HashMap<String, String>());
            title.put("Footwear", new HashMap<String, String>());
            title.put("Health%20and%20Beauty", new HashMap<String, String>());
            title.put("trending", new HashMap<String, String>());
        }
    }


