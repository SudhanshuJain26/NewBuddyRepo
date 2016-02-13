package indwin.c3.shareapp;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;
import io.intercom.com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    String url = "";
    static String token;
    static Activity act;
    public static final String MyPREFERENCES = "buddy" ;
    SharedPreferences sharedpreferences,sharedpreferences2;
    static String userId = "", pass = "";
    int d=0;
    private ProgressBar spinner;
    String Name = "", email = "", formstatus = "",uniqueCode="",creditLimit="",panoradhar="",bankaccount="",collegeid="",verificationdate="",rejectionReason="";
    EditText username, password;
    HashMap<String, String> data11;

    List<SMS> lstSms = new ArrayList<SMS>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        act=this;


       url= getApplicationContext().getString(R.string.server)+"authenticate";
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences2 = getSharedPreferences("buddyin", Context.MODE_PRIVATE);
        if(sharedpreferences.getInt("checklog",0)==1)
        {userId=sharedpreferences2.getString("name",null);
        pass=sharedpreferences2.getString("password",null);
            new ItemsByKeyword().execute(url);
        } else {
            d=1;


            overridePendingTransition(0,0);
            setContentView(R.layout.activity_main);

            spinner=(ProgressBar)findViewById(R.id.progressBar1);
            username = (EditText) findViewById(R.id.phone_number);
            password = (EditText) findViewById(R.id.password);


            TextView notreg = (TextView) findViewById(R.id.signUp);
            notreg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in2 = new Intent(MainActivity.this, Inviteform.class);
                    in2.putExtra("login",1);
                    finish();
                    startActivity(in2);
                    overridePendingTransition(0, 0);
                }
            });
TextView login_otp=(TextView)findViewById(R.id.Login_otp);
            login_otp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent opt=new Intent(MainActivity.this,Login_with_otp.class);
                    finish();
                    startActivity(opt);
                    overridePendingTransition(0, 0);


                }
            });
            TextView login = (TextView) findViewById(R.id.Login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    SharedPreferences.Editor editor2= sharedpreferences2.edit();
                    editor2.putString("name",username.getText().toString());

                    editor2.putString("password",password.getText().toString());
                    editor2.commit();
                    userId = username.getText().toString();
                    pass = password.getText().toString();
                    Map userMap = new HashMap<>();

                    userMap.put("user_id", username.getText().toString());


                    Intercom.client().updateUser(userMap);


                    if ((userId.length() != 0) && (pass.length() != 0))
                        new ItemsByKeyword().execute(url);
                    else if (userId.length() != 0)

                        Toast.makeText(getApplicationContext(),
                                "Please Enter your password",
                                Toast.LENGTH_LONG).show();
                    else if (pass.length() != 0)

                        Toast.makeText(getApplicationContext(),
                                "Please Enter your userid.",
                                Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(),
                                "Fields are empty",
                                Toast.LENGTH_LONG).show();

                }
            });
        }
    }
    @Override
    public void onBackPressed() {
Intent in=new Intent(MainActivity.this,Landing.class);
        finish();
        startActivity(in);
        overridePendingTransition(0,0);
    }

    private class ItemsByKeyword extends
            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {  if(d==1)
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
            if(d==1)
            spinner.setVisibility(View.GONE);
            if (result.contains("fail")) {

                // populateUserDetails();

                Toast.makeText(getApplicationContext(),
                        "Something's Wrong! Please try again!",
                        Toast.LENGTH_LONG).show();

            } else {

                new login().execute(url);

            }

        }
    }

    private class login extends
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
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                String url2 = getApplicationContext().getString(R.string.server)+"api/user/login";
                HttpPost httppost = new HttpPost(url2);
                httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");
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

                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {

            if (!result.contains("win")) {

                // populateUserDetails();
                if (result.contains("Invalid userid"))
                    Toast.makeText(getApplicationContext(),
                            "Please login with correct credentials ",
                            Toast.LENGTH_LONG).show();


                else if (result.contains("Invalid password")) {
                    password.setText("");
                    Toast.makeText(getApplicationContext(),
                            "Please login with correct credentials ",
                            Toast.LENGTH_LONG).show();
                }

            } else {
                int a=1;
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("checklog", a);
                editor.commit();
                //   getAllSms();
                Map userMap = new HashMap<>();

                userMap.put("user_id", userId);

                userMap.put("phoneVerified", true);
                Intercom.client().updateUser(userMap);
                SharedPreferences userP = getSharedPreferences("buddyin", Context.MODE_PRIVATE);
                int back=userP.getInt("back", 0);
                if(back==0)
                Toast.makeText(getApplicationContext(),
                        "Successfully Logged In",
                        Toast.LENGTH_LONG).show();
            //  getAllSms();
                new Thread(new Runnable() {
                    public void run(){
                       getALlContacts();
                        getAllSms();
//                    getALlContacts();
                    }
                }).start();
//                new Thread(new Runnable() {
//                    public void run(){
////                       getALlContacts();
//                       // getAllSms();
//                    }
//                }).start();
            //   getALlContacts();


                new checkuser().execute(url);

            }

        }
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
                String uurl = getApplicationContext().getString(R.string.server)+"api/user/form?phone="+userId;
                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);

                HttpGet httppost = new HttpGet(uurl);
                httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");
                httppost.setHeader("x-access-token", token);
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
                    try {
                        uniqueCode = data.getString("uniqueCode");
                    }
                    catch(Exception e){}
                    try {
                        creditLimit = data.getString("creditLimit");
                    }
                    catch(Exception e){}
                    try {
                        rejectionReason = data.getString("rejectionReason");
                    }
                    catch(Exception e){}
                    try{
                    formstatus = data.getString("formStatus");}
                    catch(Exception e){
                        formstatus="empty";
                    }
                    if(formstatus.equals(""))
                        formstatus="empty";
                    try{
                        panoradhar = data.getString("addressProof");}
                    catch(Exception e){
                        panoradhar="NA";
                    }
                    if(panoradhar.equals(""))
                        panoradhar="NA";
                    try{
                        collegeid = data.getString("collegeId");}
                    catch(Exception e){
                        collegeid="NA";
                    }
                    if(collegeid.equals(""))
                        collegeid="NA";
                    try{
                       bankaccount = data.getString("bankStatement");}
                    catch(Exception e){
                        bankaccount="NA";
                    }
                    if(bankaccount.equals(""))
                        bankaccount="NA";
                    try{
                        verificationdate = data.getString("collegeIdVerificationDate");}
                    catch(Exception e){
                        verificationdate="NA";
                    }
                    if(verificationdate.equals(""))
                        verificationdate="NA";
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

        protected void onPostExecute(String result) {

            if (result.contains("fail")) {

                // populateUserDetails();

                Toast.makeText(getApplicationContext(),
                        "Something's Wrong! Please try again!",
                        Toast.LENGTH_LONG).show();

            } else { try {
           //     Intercom.client().reset();
            }
            catch (Exception e)
            {System.out.println(e.toString()+"int main");}
                Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
                Intercom.client().registerIdentifiedUser(
                        new Registration().withUserId(userId));
                if (formstatus.equals("saved")) {

                    Intent in = new Intent(MainActivity.this, Formempty.class);
                    finish();
                   // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    in.putExtra("Name", Name);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC",uniqueCode);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                }
else
                if (formstatus.equals("declined")) {

                    Intent in = new Intent(MainActivity.this, Formempty.class);
                    finish();
                    // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    in.putExtra("Name", Name);
                    in.putExtra("Rej",rejectionReason);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC",uniqueCode);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                }
                else
                if (formstatus.equals("submitted")) {
   //                 Intent in = new Intent(MainActivity.this, Landing.class);
                    Intent in = new Intent(MainActivity.this, Formstatus.class);

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
                    in.putExtra("Name", Name);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC",uniqueCode);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                }
                if (formstatus.equals("approved")) {

                    Intent in = new Intent(MainActivity.this, Approved.class);
                    // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    finish();
                    in.putExtra("Name", Name);
                    in.putExtra("Email", email);//
                     in.putExtra("Credits",creditLimit);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC",uniqueCode);
                    startActivity(in);
                    overridePendingTransition(0,0);
                }
                else
                if (formstatus.equals("empty")) {
//                    Intent in = new Intent(MainActivity.this, Inviteform    .class);
                    Intent in = new Intent(MainActivity.this, Formempty.class);
                    finish();
                    in.putExtra("Name", Name);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC",uniqueCode);
                    startActivity(in);
                    overridePendingTransition(0,0);
                }


            }

        }
    }
    public   class SendSmsToServer extends AsyncTask<List<SMS>, String, String> {

        @Override
        protected String doInBackground(List<SMS>... sms){
            try {
                System.out.println("sm123");
                JSONObject payload = new JSONObject();
                List<SMS> res = sms[0];
                System.out.println("buddyqwe" + new Gson().toJson(sms[0]));
                String check = new Gson().toJson(sms[0]);

                int length = check.length();


//            }
//            catch (Exception e){}
                String check2 = check.substring(1, length - 1);
                String pay;


                try {
                    HttpParams httpParameters = new BasicHttpParams();

                    HttpClient client = new DefaultHttpClient(httpParameters);
                    String urlsm = getApplicationContext().getString(R.string.server)+"api/content/sms";
                    SharedPreferences pref = act.getSharedPreferences("MyPref", 0);
                    HttpPost httppost = new HttpPost(urlsm);

                    HttpConnectionParams
                            .setConnectionTimeout(httpParameters, 30000);
                    httppost.setHeader("x-access-token", token);
                    httppost.setHeader("Content-Type", "application/json");
                    //     JSONArray mJSONArray = new JSONArray(Arrays.asList(sms));
                    payload.put("userId", userId);
                    payload.put("data", check);



String t=payload.toString();
                    String t1=payload.toString().replace("{\\", "{");
                    String tx=t1.toString().replace(":\\", ":");
                    String ty=tx.toString().replace("\\\":", "\":");
                    String tz=ty.toString().replace(",\\", ",");
                    String tw=tz.toString().replace("\\\"", "\"");
                    String te=tw.toString().replace("\\\\r\\", "");
                    String tr=te.toString().replace("\\\\\"","");
                    String tu=tr.toString().replace("/\\/","");
                    String tq=tu.toString().replace("\\/","");
                    String tl=tq.toString().replace("\\\\","");
               String t2=tl.replace("\"[{", "[{");
                String t3=t2.replace("}]\"", "} ]");
                    String t4=t3.replace("\\n", "");
                    String t5=t4.replace("\"\"","");
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
            }
            catch (Exception e){return "fail";}
        }

        @Override
        protected void onPostExecute(String result) {
//            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            // TODO Auto-generated method stub
            if(result.equals("win")){

                System.out.println(result+"smswin");
            }
            System.out.println(result+"smsloss");
        }
    }
    public  class SendContactToServer extends AsyncTask<List<Contacts>, String, String> {

        @Override
        protected String doInBackground(List<Contacts>... cont){
            try {
                System.out.println("sm123");
                JSONObject payload = new JSONObject();
                List<Contacts> res = cont[0];

               String check="";
                try{ System.out.println("buddyqwe" + new Gson().toJson(cont[0]));
                    check = new Gson().toJson(cont[0]);}
                catch (Exception e)
                {
                    String s=e.toString();
                    System.out.print("regre"+s+"ssio");
                }

//                int length = check.length();


//            }
//            catch (Exception e){}
//                String check2 = check.substring(1, length - 1);
                String pay;


                try {
                    HttpParams httpParameters = new BasicHttpParams();

                    HttpClient client = new DefaultHttpClient(httpParameters);
                    String urlsm = getApplicationContext().getString(R.string.server)+"api/content/contact";
                    SharedPreferences pref = act.getSharedPreferences("MyPref", 0);
                    HttpPost httppost = new HttpPost(urlsm);

                    HttpConnectionParams
                            .setConnectionTimeout(httpParameters, 30000);
                    httppost.setHeader("x-access-token", token);
                    httppost.setHeader("Content-Type", "application/json");
                    //     JSONArray mJSONArray = new JSONArray(Arrays.asList(sms));
                    try{
                    payload.put("userId", userId);
                    payload.put("data",check);}
                    catch(Exception e)
                    {
                        System.out.println("NNeve"+e.toString());
                    }



                    String t1=payload.toString().replace("{\\", "{");
                    String tx=t1.toString().replace(":\\", ":");
                    String ty=tx.toString().replace("\\\":", "\":");
                    String tz=ty.toString().replace(",\\", ",");
                    String tw=tz.toString().replace("\\\"", "\"");
                    String te=tw.toString().replace("\\\\r\\", "");
                    String tr=te.toString().replace("\\\\\"","");
                    String tu=tr.toString().replace("/\\/","");
                    String tq=tu.toString().replace("\\/","");
                    String tl=tq.toString().replace("\\\\","");
                    String t2=tl.replace("\"[", "[");
                    String t3=t2.replace("]\"", "]");
                    String t4=t3.replace("\\n", "");
                    String t5=t4.replace("\"\"","");
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
            }
            catch (Exception e){return "fail";}
        }

        @Override
        protected void onPostExecute(String result) {
//            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            // TODO Auto-generated method stub
            if(result.equals("win")){
                System.out.println(result);
            }
        }
    }
    public void getALlContacts()
    {
        String id="NA",name="NA",ph_no="NA",typ="NA",email="NA",emailType="NA",noteWhere="NA",note="NA",orgWhere="NA",orgName="NA",title="NA",addrWhere="NA",poBox="NA",city="NA",state="NA",postalCode="NA",street="NA",country="NA",type="NA";
        String[] noteWhereParams,orgWhereParams,addrWhereParams,nickwhereparam;
        String nickwhere="NA",nickname="NA";
        ContentResolver cr1=getContentResolver();
        Cursor cu=cr1.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        if(cu.getCount()>0)
        {
            System.out.print("con"+cu.getCount());
            List<Contacts> con=new ArrayList<Contacts>();
            List<Contacts> con2=new ArrayList<Contacts>();
            int batchcount1=0;
            while(cu.moveToNext())
            {try {
                Contacts cObj=new Contacts();

//                String address=cu.getString(cu.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
//
//                String organisation = cu.getString(cu.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
//          String Website = cu.getString(cu.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL));
//////
//              String notes = cu.getString(cu.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
////                String address = cu.getString(cu.getColumnIndex(ContactsContract.CommonDataKinds.SipAddress.SIP_ADDRESS));
//               String phoen = cu.getString(cu.getColumnIndex(ContactsContract.Contacts.PHONETIC_NAME));
//               String nick = cu.getString(cu.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME));
//                String im=cu.getString(cu.getColumnIndex(ContactsContract.CommonDataKinds.In
//                String im=cu.getString(cu.getColumnIndex(ContactsContract.Contacts.1
try {
    id = cu.getString(
            cu.getColumnIndex(ContactsContract.Contacts._ID));
}
catch(Exception e){System.out.println("Error with contact");
id="NA";}
                try{
                 name = cu.getString(
                        cu.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                }
                catch(Exception e){System.out.println("Error with contact");
                name="NA";}
                cObj.setName(name);
                if (Integer.parseInt(cu.getString(
                        cu.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr1.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);

                   List<Phone> ph_ns=new ArrayList<Phone>();
                    while (pCur.moveToNext()) {
                        Phone p=

                                new Phone();
                try{
                  ph_no=pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));}
                catch(Exception e){System.out.println("Error with contact");
                ph_no="NA";}
                        try{
                         typ=pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));}
                        catch(Exception e){System.out.println("Error with contact");
                        typ="NA";}

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
                List<Email> eMail= new ArrayList<Email>();
                while (emailCur.moveToNext()) {
                    Email eObj= new Email();
                    // This would allow you get several email addresses
                    // if the email addresses were stored in an array
                    try{
                     email = emailCur.getString(
                            emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }
                    catch(Exception e){
                        email="NA";
                        System.out.println("Error with contact");}
                    try{
                    emailType = emailCur.getString(
                            emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                    }
                    catch(Exception e){
                        emailType="NA";
                        System.out.println("Error with contact");}
                    eObj.setemail(email);
                    eObj.setemail_type(emailType);
                    eMail.add(eObj);
//                    System.out.println(emailType+"eeee");
                }
                cObj.setEm(eMail);
                emailCur.close();
                try{
                 noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";}
                catch(Exception e){
                    noteWhere="NA";
                    System.out.println("Error with contact");}

                noteWhereParams = new String[]{id,
                        ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};

                Cursor noteCur = cr1.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
                if (noteCur.moveToFirst()) {
                    try{
                     note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));}
                    catch(Exception e){note="NA";}
                    System.out.println("eeee");
                }
                noteCur.close();
                try{
                orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                }
                catch(Exception e){
                    orgWhere="NA";
                    System.out.println("Error with contact");}
                 orgWhereParams = new String[]{id,
                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                Cursor orgCur = cr1.query(ContactsContract.Data.CONTENT_URI,
                        null, orgWhere, orgWhereParams, null);
                if (orgCur.moveToFirst()) {
                    try{
                     orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                    }
                    catch(Exception e){
                        orgName="NA";
                        System.out.println("Error with contact");}
                    try{
                     title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                    }
                    catch(Exception e){
                        title="NA";
                        System.out.println("Error with contact");}
                    cObj.setOrg(orgName);
                }
                orgCur.close();
                try{
                addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                }
                catch(Exception e){
                    addrWhere="NA";
                    System.out.println("Error with contact");}
                 addrWhereParams = new String[]{id,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
                Cursor addrCur = cr1.query(ContactsContract.Data.CONTENT_URI,
                        null, addrWhere, addrWhereParams, null);
                while(addrCur.moveToNext()) {
                    try{
                     poBox = addrCur.getString(
                            addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
                    }
                    catch(Exception e){
                        poBox="NA";
                        System.out.println("Error with contact");}
                    try{
                    street = addrCur.getString(
                            addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                    }
                    catch(Exception e){
                        street="NA";
                        System.out.println("Error with contact");}
                    try{
                    city = addrCur.getString(
                            addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                    }
                    catch(Exception e){
                        city="NA";
                        System.out.println("Error with contact");}
                    try{
                    state = addrCur.getString(
                            addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                    }
                    catch(Exception e){
                        state="NA";
                        System.out.println("Error with contact");}
                    try {
                        postalCode = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                    }
                    catch(Exception e){
                        postalCode="NA";
                        System.out.println("Error with contact");}
                     try{
                    country = addrCur.getString(
                            addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                     }
                     catch(Exception e){
                         country="NA";
                         System.out.println("Error with contact");}
                    try{
                     type = addrCur.getString(
                            addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
                    }
                    catch(Exception e){
                        type="NA";
                        System.out.println("Error with contact");}
             cObj.setAddress(city + " " + state + " " + postalCode + " " + country + " " + type + " " + poBox + " " + street);
                }
                addrCur.close();
try{
                 nickwhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
}
catch(Exception e){
    nickwhere="NA";
    System.out.println("Error with contact");}
                 nickwhereparam = new String[]{id,
                        ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE};
                Cursor nick = cr1.query(ContactsContract.Data.CONTENT_URI,
                        null, nickwhere, nickwhereparam, null);
                if (nick.moveToFirst()) {
                    try{
                     nickname = nick.getString(nick.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.DATA));
                    }
                    catch(Exception e){
                        nickname="NA";
                        System.out.println("Error with contact");}
                    try{
                    System.out.println("swag" + id);
                    }
                    catch(Exception e){System.out.println("Error with contact");}
                    cObj.setNick(nickname);
//                    String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                }
                nick.close();
con.add(cObj);

            }
            catch (Exception e){System.out.println("buddyErro"+e.toString());}

                batchcount1++;

if(batchcount1%500==0)
{ new SendContactToServer().execute(con);
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
            if(!con2.isEmpty()){
                new SendContactToServer().execute(con2);
                con2.clear();}

            System.out.print(con+"dekkh");

        }




    }

    public void getAllSms() {
JSONArray smsJ=new JSONArray();

        SMS objSms = new SMS();


        JSONObject rSms=new JSONObject();
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
                if(batchCount==350){
                    System.out.println("sm12a" + lstSms.get(0).getMsg());
//                    new SendSmsToServer().execute(lstSms);
                    // lstSms.clear();
                  new SendSmsToServer().execute(lstSms);
                    lstSms.clear();
                    batchCount = 0;

                }
                c.moveToNext();
            }
        }

        if(batchCount>0){
            new SendSmsToServer().execute(lstSms);
        }
        SharedPreferences pref = getSharedPreferences("MyPref", 0);
        pref.edit().putBoolean("sms_sent", true).commit();
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();
        //c.close();


    }



}