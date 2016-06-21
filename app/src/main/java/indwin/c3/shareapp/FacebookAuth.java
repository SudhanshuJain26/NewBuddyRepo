package indwin.c3.shareapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Set;

public class FacebookAuth extends AppCompatActivity {
    private LoginButton loginbutton;
    private SharedPreferences toks;
    private ProgressBar spinner;
    private TextView text;
    String user;
    static Context con;
    private int now = 0;
    static String email = "", firstName = "", friends = "", gender = "", lastName = "", link = "", name = "", fbuserId = "", verified = "", birthday;
    private static Boolean veri;
    SharedPreferences cred;
    static int firstloginsign = 0;
    private static int rr = 0;
    int w = 0;
    private CallbackManager callbackManager;

    //    private UiLifecycleHelper uiHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toks = getSharedPreferences("token", Context.MODE_PRIVATE);
        con = getApplicationContext();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        setContentView(R.layout.activity_facebook_auth);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "indwin.c3.shareapp", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        loginbutton = (LoginButton) findViewById(R.id.login_button);
        LoginManager.getInstance().logOut();
        loginbutton.setBackgroundResource(R.color.colorwhite);
        loginbutton.setPadding(getResources().getDimensionPixelSize(
                        R.dimen.fabrig),
                getResources().getDimensionPixelSize(
                        R.dimen.fabtop),
                getResources().getDimensionPixelSize(
                        R.dimen.fabrig),
                getResources().getDimensionPixelSize(
                        R.dimen.fabtop));
        loginbutton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email", "user_birthday"));


        loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {


                // Toast.makeText(MainActivity.this, loginResult.getAccessToken().getPermissions() + "hua dost", Toast.LENGTH_LONG).show();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Apploication code
                                try {
//                                    JSONObject objectdata=response.getJSONObject();
                                    //String id=object.getString("id");
                                    try {
                                        email = object.getString("email");
                                    } catch (Exception e) {
                                    }
                                    try {
                                        firstName = object.getString("first_name");
                                    } catch (Exception e) {
                                    }
                                    try {
                                        gender = object.getString("gender");
                                    } catch (Exception e) {
                                    }
                                    try {
                                        lastName = object.getString("last_name");
                                    } catch (Exception e) {
                                    }
                                    try {
                                        link = object.getString("link");
                                    } catch (Exception e) {
                                    }
                                    try {
                                        fbuserId = object.getString("id");
                                        SharedPreferences sf = getSharedPreferences("proid", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor2 = sf.edit();
                                        editor2.putString("dpid", fbuserId);

                                        //  editor2.putString("password", password.getText().toString());
                                        editor2.commit();
                                    } catch (Exception e) {
                                    }
                                    try {
                                        name = object.getString("name");
                                    } catch (Exception e) {
                                    }
                                    try {
                                        gender = object.getString("gender");
                                    } catch (Exception e) {
                                    }
//                                     birthday=object.getString("birthday");
                                    //System.out.print("digo" + email);
                                    try {
                                        veri = object.getBoolean("verified");
                                    } catch (Exception e) {
                                    }
                                    //do something with the data here
                                } catch (Exception e) {
                                    System.out.print("ds");
                                    e.printStackTrace(); //something's seriously wrong here
                                }
                                // new fblogin().execute();
                                Log.e("LoginActivity please", response.toString());
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email,first_name,last_name,location,locale,timezone,verified");
                request.setParameters(parameters);
                request.executeAsync();

//                request.executeAsync(ParseF)
                new GraphRequest(
                        loginResult.getAccessToken(),
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                /* handle the result */
                                try {
                                    JSONObject obj = response.getJSONObject();
                                    JSONObject summ = new JSONObject(obj.getString("summary"));
                                    friends = summ.getString("total_count");
//                                    System.out.println(obj+"noobs11"//);
                                } catch (Exception ex) {
//                                    System.out.print("bro mera pro");
                                }
                                Log.e("Friend in List", "-------------" + response.toString());
                                //   Toast.makeText(getApplicationContext(),email+friends,Toast.LENGTH_LONG).show();
//                                new fblogin().execute();
                                try {
                                    if ((name.trim().length() == 0) || (email.trim().length() == 0) || (friends.trim().length() == 0)) {
                                        Set<String> denied = loginResult.getRecentlyDeniedPermissions();
                                        if (denied.size() > 0) {
                                            Toast.makeText(FacebookAuth.this, "Please grant all permissions to complete your profile!", Toast.LENGTH_SHORT).show();
                                            LoginManager.getInstance().logInWithReadPermissions(FacebookAuth.this, Arrays.asList("user_friends", "email", "user_birthday"));
                                        }
                                        LoginManager.getInstance().logOut();
                                        Toast.makeText(FacebookAuth.this, "Please try again!", Toast.LENGTH_SHORT).show();
                                        //   finish();
                                    } else {
//                                                    {
                                        Long time = Calendar.getInstance().getTimeInMillis() / 1000;
                                        Long oldtime = toks.getLong("expires", 0);
                                        //  Toast.makeText(FacebookAuth.this, String.valueOf(oldtime), Toast.LENGTH_SHORT).show();
                                        if (time + 5 < toks.getLong("expires", 0))
                                            new fblogin().execute();
                                        else
                                            new AuthTokc().execute("facebook");
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                ).executeAsync();


//                btnSignIn.setOnClickListener();
                //  System.out.println("yo nigga"+parameters.getString("fields"));
            }

            @Override
            public void onCancel() {
                finish();
//                Intent in=new Intent(FacebookAuth.this,Landing.class);
//                finish();
//
//                startActivity(in);
//                overridePendingTransition(0, 0);


            }

            @Override
            public void onError(FacebookException e) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if(now==0)
        {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
//        else
//            finish();


    }

    public void cc() {
        new fblogin().execute();
    }

    public class fblogin extends
            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            try {
                spinner = (ProgressBar) findViewById(R.id.progressBar1);
                text = (TextView) findViewById(R.id.loginface);
                loginbutton.setVisibility(View.INVISIBLE);
                text.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                String k = e.toString();
            }
        }

        @Override
        protected String doInBackground(String... data) {

            // String urldisplay = data[0];
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                payload.put("fbUserId", fbuserId);
                payload.put("fbName", name);
                payload.put("fbEmail", email);
                payload.put("fbGender", gender);
                payload.put("fbFirstName", firstName);
                payload.put("fbLastName", lastName);
                payload.put("fbLink", link);
                payload.put("fbVerified", veri);
            } catch (Exception e) {
            }
            try {
                payload.put("fbFriends", friends);
            } catch (Exception e) {
            }
            try {
                // payload.put("action", details.get("action"));
                //  toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                HttpParams httpParameters = new BasicHttpParams();
                try {
                    cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
                    user = cred.getString("phone_number", "");
                } catch (Exception e) {
                    String k = e.toString();
                }
                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                String url2 = BuildConfig.SERVER_URL + "api/user/social?userid=" + user;
                HttpPut httppost = new HttpPut(url2);
                httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");
                httppost.setHeader("x-access-token", toks.getString("token_value", ""));
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

                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {
            if (result.equals("win")) {
                //  Toast.makeText(FacebookAuth.this, "woo", Toast.LENGTH_SHORT).show();


                //  loginbutton.setVisibility(View.INVISIBLE);


                spinner.setVisibility(View.INVISIBLE);

                Intent in = new Intent(FacebookAuth.this, ViewForm.class);
                in.putExtra("which_page", getIntent().getExtras().getInt("which_page"));
                startActivity(in);
                overridePendingTransition(0, 0);
                finish();
                new fbConnected().execute();
            }

        }


    }


    private class fbConnected extends
            AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... data) {

            // String urldisplay = data[0];
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                payload.put("fbUserId", fbuserId);
//                payload.put("fbName", name);
                payload.put("fbConnected", "true");

                // payload.put("action", details.get("action"));
                SharedPreferences sh_otp = getSharedPreferences("token", Context.MODE_PRIVATE);
                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                String url2 = BuildConfig.SERVER_URL + "api/user/form?phone=" + user;
                HttpPut httppost = new HttpPut(url2);
                httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");
                httppost.setHeader("x-access-token", sh_otp.getString("token_value", ""));
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

                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {
            if (!result.equals("fail"))
                firstloginsign = 1;
        }
    }

    @Override
    public void onBackPressed() {

        {
            finish();
//            Intent in=new Intent(FacebookAuth.this,Landing.class);
//            finish();
//
//            startActivity(in);
//            overridePendingTransition(0, 0);

        }
        // code here to show dialoguper.onBackPressed();  // optional depending on your needs
    }

    public class AuthTokc extends
            AsyncTask<String, Void, String> {

        private String apiN = "";

        //        Context context;
//        AuthTok(Context context) {
//            this.context = context;
//        }
        //    Splash obj=new Splash();
        @Override
        protected String doInBackground(String... params) {
            JSONObject payload = new JSONObject();
            String urldisplay = params[0];
            apiN = urldisplay;
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
                String urll = BuildConfig.SERVER_URL + "authenticate";
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

                        SharedPreferences userP = con.getSharedPreferences("token", Context.MODE_PRIVATE);
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

                //     Toast.makeText(FacebookAuth.this, "yey", Toast.LENGTH_SHORT).show();
//            next.new FacebookAuth.fblogin().execute();
                new fblogin().execute();
//            next.fblogin().execute();


            }
        }
    }
}