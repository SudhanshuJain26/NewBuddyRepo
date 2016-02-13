package indwin.c3.shareapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;

public class Landing extends AppCompatActivity {
int count=0;
    ViewPager dealspager;
    SharedPreferences sh,sh_otp;
    String name = "", email = "", formstatus = "",uniqueCode="",creditLimit="",panoradhar="",bankaccount="",collegeid="",verificationdate="",rejectionReason="",referral_code="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sh = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        sh_otp = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
        Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
      try{
        Intercom.client().registerIdentifiedUser(
                new Registration().withUserId(sh_otp.getString("number", "")));}
      catch (Exception e){}
        if(sh.getInt("checklog",0)==1)
        {
            Intent in=new Intent(Landing.this,MainActivity.class);
            finish();
            startActivity(in);
            overridePendingTransition(0, 0);

        }
else
        if(!sh_otp.getString("number","").equals(""))
        {
new verifyOtp().execute("");
        }
        else
        {setContentView(R.layout.activity_landing);
            TextView login=(TextView)findViewById(R.id.login_pager);
            TextView signup=(TextView)findViewById(R.id.signup_pager);
         final ImageView dot1=(ImageView)findViewById(R.id.c1);
            final ImageView  dot2=(ImageView)findViewById(R.id.c2);
            final ImageView  dot3=(ImageView)findViewById(R.id.c3);
            final ImageView logo=(ImageView)findViewById(R.id.buddyLogo);
            dot1.setBackgroundResource(R.drawable.circle2);
            try {
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent(Landing.this,MainActivity.class);
                        finish();
                        startActivity(in);
                        overridePendingTransition(0, 0);


                    }
                });
                signup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent(Landing.this,Inviteform.class);
                        finish();
                        startActivity(in);
                        overridePendingTransition(0, 0);

                    }
                });
            }
            catch(Exception e){}

        ViewPagerAdapter adapter = new ViewPagerAdapter(Landing.this,3,getApplicationContext());
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


                    if(position==0)
                    {


                        dot1.setBackgroundResource(R.drawable.circle2);
                        dot2.setBackgroundResource(R.drawable.circle);
                        dot3.setBackgroundResource(R.drawable.circle);}

                    else  if(position==1)
                    {   dot2.setBackgroundResource(R.drawable.circle2);
                        dot1.setBackgroundResource(R.drawable.circle);
                        dot3.setBackgroundResource(R.drawable.circle);}
                    else if(position==2)
                    {dot2.setBackgroundResource(R.drawable.circle);
                        dot1.setBackgroundResource(R.drawable.circle);
                        dot3.setBackgroundResource(R.drawable.circle2);}
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        // Timer for auto sliding
       Timer timer  = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count <= 2) {
                            if (count == 0) {
//{logo.startAnimation(animationFadeIn);

                            }
                          //  dealspager.setCurrentItem(count);
                            count++;
                        } else {
                            count = 0;
                          //  dealspager.setCurrentItem(count);
                        }
                    }
                });
            }
        }, 1000, 5000);}
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
                String url2=getApplicationContext().getString(R.string.server)+"api/user/form?phone="+sh_otp.getString("number","");


                HttpGet httppost = new HttpGet(url2);
                try {

                }
                catch(Exception e){System.out.println("dio "+e.toString());}
                    httppost.setHeader("x-access-token", sh_otp.getString("tok",""));


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

                        name=data1.getString("name");
                        email=data1.getString("email");}
                    catch (Exception e){}
                    try{
                        try {
                            creditLimit = data1.getString("creditLimit");
                        }
                        catch(Exception e){}
                        try{
                            formstatus = data1.getString("formStatus");}
                        catch(Exception e){
                            formstatus="empty";
                        }
                        try {
                            rejectionReason = data1.getString("rejectionReason");
                        }
                        catch(Exception e){}
                        if(formstatus.equals(""))
                            formstatus="empty";
                        try{
                            panoradhar = data1.getString("addressProof");}
                        catch(Exception e){
                            panoradhar="NA";
                        }
                        if(panoradhar.equals(""))
                            panoradhar="NA";
                        try{
                            collegeid = data1.getString("collegeId");}
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

//                    inotp.putExtra("Name", mName);
//                    inotp.putExtra("Email",email.getText().toString());
//                    inotp.putExtra("College",college.getText().toString());
//                    inotp.putExtra("Phone",phone.getText().toString());

            if(result.equals("win")) {



                if (formstatus.equals("saved")) {

                    Intent in = new Intent(Landing.this, Formempty.class);
                    // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    finish();
                    in.putExtra("Name", name);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC", referral_code);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                } else if (formstatus.equals("submitted")) {
                    //                 Intent in = new Intent(MainActivity.this, Landing.class);
                    Intent in = new Intent(Landing.this, Formstatus.class);
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
                    in.putExtra("UniC", referral_code);
                    startActivity(in);
                    overridePendingTransition(0,0);
                }
                if(formstatus.equals("declined"))
                {

                    Intent in = new Intent(Landing.this, Formempty.class);
                    finish();
                    // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    in.putExtra("Name", name);
                    in.putExtra("Rej",rejectionReason);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC",uniqueCode);
                    startActivity(in);
                    overridePendingTransition(0, 0);

                }
                if (formstatus.equals("approved")) {

                    Intent in = new Intent(Landing.this, Approved.class);
                    finish();
                    // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    in.putExtra("Name", name);
                    in.putExtra("Email", email);//  in.putExtra("Credits",creditLimit);
                    in.putExtra("Form", formstatus);
                    in.putExtra("Credits", creditLimit);
                    in.putExtra("UniC", referral_code);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                } else if (formstatus.equals("empty")) {
//                    Intent in = new Intent(MainActivity.this, Inviteform    .class);
                    Intent in = new Intent(Landing.this, Formempty.class);
                    finish();
                    in.putExtra("Name", name);
                    in.putExtra("Email", email);
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
