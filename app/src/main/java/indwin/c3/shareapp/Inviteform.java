package indwin.c3.shareapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;

public class Inviteform extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private WebView form;
    private ProgressBar spinner;
    private Pattern pattern;
    private Matcher matcher;
    int an=0,ae=0,ac=0,ap=0;
    Intent intform;  private EditText name,email,phone,ref;
    AutoCompleteTextView college;
int a=0;
    String mName,mEmail,mCollege,mPhone,mRef,truth="";static String token="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inviteform);
spinner=(ProgressBar)findViewById(R.id.progressBar1);
        name=(EditText)findViewById(R.id.name);
try{
    a=getIntent().getExtras().getInt("login");
}
catch(Exception e){}
        email=(EditText)findViewById(R.id.email);
       // college=(AutoCompleteTextView)findViewById(R.id.college);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, colleges);
        phone=(EditText)findViewById(R.id.phone);
ref=(EditText)findViewById(R.id.ref);
//        college.setAdapter(adapter);

        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

        }
        catch(Exception e){System.out.println(e.toString()+"digo");}
        TextView lgin=(TextView)findViewById(R.id.lgin);
        lgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Inviteform.this,MainActivity.class);
                finish();
                startActivity(in);
                overridePendingTransition(0, 0);
            }
        });
        TextView invite=(TextView)findViewById(R.id.invite);
                invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try { mName=name.getText().toString();
                     String EMAIL_PATTERN =
                            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";



mRef=ref.getText().toString();
                    mEmail=email.getText().toString();
                    pattern = Pattern.compile(EMAIL_PATTERN);
                    matcher = pattern.matcher(mEmail);
                    Boolean checkemail=matcher.matches();


                    mPhone=phone.getText().toString();

                    SharedPreferences userP = getSharedPreferences("buddyin", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userP.edit();
                    editor.putString("name", mPhone);

                    editor.commit();
                    try {
          //
          //              Intercom.client().reset();
                    }
                    catch (Exception e)
                    {System.out.println(e.toString()+"int inv");}
                    Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
                    Intercom.client().registerIdentifiedUser(
                            new Registration().withUserId(mPhone));
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
                        Map userMap = new HashMap<>();
                        userMap.put("name", mName);
                        userMap.put("email", mEmail);
                        userMap.put("user_id", mPhone);
                        userMap.put("phone", mPhone);
                        Intercom.client().updateUser(userMap);
                        SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedpreferences.edit();
                        editor1.putInt("shareflow",0);
                        editor1.commit();
                    new ItemsByKeyword().execute("");}
                    else
                        if(!checkemail)
                            Toast.makeText(Inviteform.this,"Please enter a valid email-id!",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(Inviteform.this,"Please Enter the correct details!",Toast.LENGTH_LONG).show();


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
    }
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
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
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

                HttpPost httppost = new HttpPost(getApplicationContext().getString(R.string.server)+"authenticate");
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
            spinner.setVisibility(View.GONE);
            if (result.contains("fail")) {

                // populateUserDetails();

                Toast.makeText(getApplicationContext(),
                        "Something's Wrong! Please try again!",
                        Toast.LENGTH_LONG).show();

            } else {

                new sendOtp().execute();

            }

        }
    }
    private class sendOtp extends
            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... data) {

            // String urldisplay = data[0];
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST
                payload.put("name",mName);
                payload.put("email", mEmail);
               // payload.put("college", mCollege);
                if(mRef.trim().length()>0)
                    payload.put("refCode",mRef);
                payload.put("phone", mPhone);
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                //api/login/sendotp
                String url2 = getApplicationContext().getString(R.string.server)+"api/login/signup";
                HttpPost httppost = new HttpPost(url2);

                httppost.setHeader("x-access-token", token);
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
            spinner.setVisibility(View.GONE);
            if(result.equals("success")){
            if(truth.equals("success")){
            Intent inotp=new Intent(Inviteform.this,Otp.class);
                finish();
//                    inotp.putExtra("Name", mName);
//                    inotp.putExtra("Email",email.getText().toString());
//                    inotp.putExtra("College",college.getText().toString());
                    inotp.putExtra("Phone", phone.getText().toString());
                inotp.putExtra("Ref",mRef);


                startActivity(inotp);
                overridePendingTransition(0,0);
}






        }  else{

//

            Toast.makeText(getApplicationContext(),
                    result,
                    Toast.LENGTH_LONG).show();
        }
    }

}}
