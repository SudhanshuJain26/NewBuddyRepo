package indwin.c3.shareapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;

public class Landing extends AppCompatActivity {
    int count=0;
    ViewPager dealspager;
    SharedPreferences sh,sh_otp,ss;
    SharedPreferences userP ;
    private ProgressBar spinner;
   int data;
private String phoneNumberCall="";
    Intent inTent;
    private String action="",cashBack="",name = "", email = "",fbid="", formstatus = "",uniqueCode="",creditLimit="",panoradhar="",bankaccount="",collegeid="",verificationdate="",rejectionReason="",referral_code="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sh = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        ss = getSharedPreferences("cred", Context.MODE_PRIVATE);
        userP = getSharedPreferences("token", Context.MODE_PRIVATE);
        try {
            inTent = getIntent();
            action = inTent.getAction();
            data = inTent.getExtras().getInt("cc");
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        if (data == 1) {
//phoneNumberCall=sh_otp.getString("number", "");
        new AuthTokc().execute();
        }
        else{
        sh_otp = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
        if ((sh.getInt("checklog", 0) != 1) && (sh_otp.getString("number", "").equals(""))) {
            setContentView(R.layout.activity_landing);
            TextView login = (TextView) findViewById(R.id.login_pager);
            TextView signup = (TextView) findViewById(R.id.signup_pager);
            final ImageView dot1 = (ImageView) findViewById(R.id.c1);
            final ImageView dot2 = (ImageView) findViewById(R.id.c2);
            final ImageView dot3 = (ImageView) findViewById(R.id.c3);
            final ImageView logo = (ImageView) findViewById(R.id.buddyLogo);
            dot1.setBackgroundResource(R.drawable.circle2);
            try {
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(Landing.this, MainActivity.class);
                        finish();
                        startActivity(in);
                        overridePendingTransition(0, 0);


                    }
                });
                signup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(Landing.this, Inviteform.class);
                        finish();
                        startActivity(in);
                        overridePendingTransition(0, 0);

                    }
                });
            } catch (Exception e) {
            }

            ViewPagerAdapter adapter = new ViewPagerAdapter(Landing.this, 3, getApplicationContext());
            dealspager = (ViewPager) findViewById(R.id.landing);
            adapter.notifyDataSetChanged();
            dealspager.setAdapter(adapter);
            dealspager.setCurrentItem(0);

            dealspager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {


                    if (position == 0) {


                        dot1.setBackgroundResource(R.drawable.circle2);
                        dot2.setBackgroundResource(R.drawable.circle);
                        dot3.setBackgroundResource(R.drawable.circle);
                    } else if (position == 1) {
                        dot2.setBackgroundResource(R.drawable.circle2);
                        dot1.setBackgroundResource(R.drawable.circle);
                        dot3.setBackgroundResource(R.drawable.circle);
                    } else if (position == 2) {
                        dot2.setBackgroundResource(R.drawable.circle);
                        dot1.setBackgroundResource(R.drawable.circle);
                        dot3.setBackgroundResource(R.drawable.circle2);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            // Timer for auto sliding
        }
//       else if(sh.getInt("checklog",0)==1)
//        {setContentView(R.layout.activity_formsaved);
//            Intent in=new Intent(Landing.this,MainActivity.class);
//            finish();
//            startActivity(in);
//            overridePendingTransition(0, 0);
//
//        }
        else if (!ss.getString("phone_number", "").equals("")) {
            setContentView(R.layout.activity_formsaved);
            spinner = (ProgressBar) findViewById(R.id.progressBar1);
            Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
            try {
                Intercom.client().registerIdentifiedUser(
                        new Registration().withUserId(ss.getString("phone_number", "")));
                Intercom.client().openGCMMessage(getIntent());
            } catch (Exception e) {
            }
//
            Long time = Calendar.getInstance().getTimeInMillis() / 1000;
            Long oldtime = userP.getLong("expires", 0);
//        Toast.makeText(FacebookAuth.this, String.valueOf(oldtime), Toast.LENGTH_SHORT).show();
            if (time + 5 < userP.getLong("expires", 0))
                new verifyOtp().execute("");
            else
                new AuthTokc().execute();
        }
        // else
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
                String urll=getApplicationContext().getString(R.string.server) + "authenticate";
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
                        String token1="";

                         userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorP = userP.edit();
                        token1 = resp.getString("token");
                        editorP.putString("token_value",token1);
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

             //   Toast.makeText(Landing.this, "yey", Toast.LENGTH_SHORT).show();
//            next.new FacebookAuth.fblogin().execute();
                // new fblogin().execute();
//            next.fblogin().execute();
//                new forgotpass().execute();
                 new verifyOtp().execute("");

            }}}


    private class verifyOtp extends
            AsyncTask<String, Void, String> {
        @Override
        protected  void onPreExecute()
        {

        }
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
                String url2=getApplicationContext().getString(R.string.server)+"api/user/form?phone="+ss.getString("phone_number","");


                HttpGet httppost = new HttpGet(url2);
                try {

                }
                catch(Exception e){System.out.println("dio "+e.toString());}
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp=toks.getString("token_value","");
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
                    try{

                        referral_code=data1.getString("uniqueCode");
                        SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("rcode", referral_code);
                        editor.commit();
                        name=data1.getString("name");
                        email=data1.getString("email");}
                    catch (Exception e){}
                    try{
                        try {
                            creditLimit = data1.getString("creditLimit");
                        }
                        catch(Exception e){}
                        try{
                            fbid = data1.getString("fbConnected");}
                        catch(Exception e){
                            fbid="empty";
                        }
                        if(fbid.equals("")||(fbid.equals("false")))
                            fbid="empty";
                        try{
                            formstatus = data1.getString("formStatus");}
                        catch(Exception e){
                            formstatus="empty";
                        }

                        int cashBack=0;
                        try{
                            cashBack=data1.getInt("totalCashback");
                        }
                        catch(Exception e)
                        {
                            cashBack=0;
                        }
                        String approvedBand="";
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
                        editorP.putString("approvedBand",approvedBand);
                        editorP.putInt("creditLimit", creditLimit);
                        editorP.putInt("totalBorrowed", totalBorrowed);
                        editorP.putInt("cashBack", cashBack);
                        editorP.commit();
                        try {
                            rejectionReason = data1.getString("rejectionReason");
                        }
                        catch(Exception e){}
                        if(formstatus.equals(""))
                            formstatus="empty";
                        try{
                            panoradhar = data1.getString("addressProofs");}
                        catch(Exception e){
                            panoradhar="NA";
                        }
                        if(panoradhar.equals(""))
                            panoradhar="NA";
                        try{
                            collegeid = data1.getString("collegeIDs");}
                        catch(Exception e){
                            collegeid="NA";
                        }
                        if(collegeid.equals(""))
                            collegeid="NA";
                        try{
                            bankaccount = data1.getString("bankStatement");}
                        catch(Exception e){
                            bankaccount="NA";
                        }
                        if(bankaccount.equals(""))
                            bankaccount="NA";
                        try{
                            verificationdate = data1.getString("collegeIdVerificationDate");}
                        catch(Exception e){
                            verificationdate="NA";
                        }
                        if(verificationdate.equals(""))
                            verificationdate="NA";
                        try{ String dpid=data1.getString("fbUserId");
                            SharedPreferences sf=getSharedPreferences("proid",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor2= sf.edit();
                            editor2.putString("dpid", dpid);

                            //  editor2.putString("password", password.getText().toString());
                            editor2.commit();}
                        catch (Exception e){}

                    }
                    catch(Exception e){}

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
           // spinner.setVisibility(View.INVISIBLE);
//                    inotp.putExtra("Name", mName);
//                    inotp.putExtra("Email",email.getText().toString());
//                    inotp.putExtra("College",college.getText().toString());
//                    inotp.putExtra("Phone",phone.getText().toString());

            if(result.equals("win")) {



                if (formstatus.equals("saved")) {
                    Intent in;
if(data==1)
    in = new Intent(Landing.this, Formempty.class);
                    else
                    in = new Intent(Landing.this, HomePage.class);
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
                    Intent in;
                    bankaccount="somehting";
                    if(data==1)
                        in = new Intent(Landing.this, Formstatus.class);
                    else
                        in = new Intent(Landing.this, HomePage.class);
                    if((panoradhar.equals("NA"))||(bankaccount.equals("NA"))||(collegeid.equals("NA")))
                    {
                        in.putExtra("screen_no",1);
                    }
                    else if((!panoradhar.equals("NA"))&&(!collegeid.equals("NA"))&&(!bankaccount.equals("NA"))&&(verificationdate.equals("NA")))
                    {
                        in.putExtra("screen_no",2);
                    }
                    else if((!panoradhar.equals("NA"))&&(!collegeid.equals("NA"))&&(!bankaccount.equals("NA"))&&(!verificationdate.equals("NA")))
                    {
                        in.putExtra("screen_no",3);
                        in.putExtra("VeriDate",verificationdate);
                    }

                    finish();
                    in.putExtra("Name", name);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("fbid", fbid);
                    in.putExtra("UniC", referral_code);
                    startActivity(in);
                    overridePendingTransition(0,0);
                }
                if(formstatus.equals("declined"))
                {Intent in;
                    if(data==1)
                        in = new Intent(Landing.this, Formempty.class);
                    else
                         in = new Intent(Landing.this, HomePage.class);
                    finish();
                    // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    in.putExtra("Name", name);
                    in.putExtra("Rej",rejectionReason);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("fbid", fbid);
                    in.putExtra("UniC",uniqueCode);
                    startActivity(in);
                    overridePendingTransition(0, 0);

                }
                if (formstatus.equals("approved")||(formstatus.equals("flashApproved"))) {
Intent in;
                    if(data==1)
                        in = new Intent(Landing.this, Approved.class);
                    else           in = new Intent(Landing.this, HomePage.class);
                    finish();
                    // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    in.putExtra("Name", name);
                    in.putExtra("Email", email);//  in.putExtra("Credits",creditLimit);
                    in.putExtra("Form", formstatus);
                    if(formstatus.equals("approved"))
                        in.putExtra("checkflash",0);
                    else
                        in.putExtra("checkflash",1);
                    in.putExtra("fbid", fbid);
                    in.putExtra("Credits", creditLimit);
                    in.putExtra("UniC", referral_code);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                } else if (formstatus.equals("empty")) {
//                    Intent in = new Intent(MainActivity.this, Inviteform    .class);
                    Intent in;
                    if(data==1)
                        in = new Intent(Landing.this, Formempty.class);
                    else
                        in = new Intent(Landing.this, HomePage.class);
                    finish();
                    in.putExtra("Name", name);
                    in.putExtra("Email", email);

                    in.putExtra("fbid", fbid);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC", referral_code);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                }
            }


            else
            {
                Intent in =new Intent(Landing.this,MainActivity.class);
                finish();
                startActivity(in);
                overridePendingTransition(0,0);
//                Toast.makeText(getApplicationContext(),"Please try again!"
//                        ,
//                        Toast.LENGTH_LONG).show();
            }



        }
    }
}
