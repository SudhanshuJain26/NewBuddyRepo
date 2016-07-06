package indwin.c3.shareapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import indwin.c3.shareapp.BuildConfig;
import indwin.c3.shareapp.R;
import indwin.c3.shareapp.models.Friends;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;


public class InviteList extends AppCompatActivity {

    TextView addPhone;
    TextView addEmails;
    ImageView backButton;
    TextView num_email;
    TextView num_phone;
    TextView earn170;
    TextView earn1700;

    private static final int PERMISSION_REQUEST_CODE = 1;
    ProgressBar spinner;
    String userId;
    public ArrayList<Friends> listfromServerPhone = new ArrayList<>();
    public ArrayList<Friends> isBuddyListPhone = new ArrayList<>();
    public ArrayList<Friends> isInvitedListPhone = new ArrayList<>();

    ProgressDialog pDialog;
    ArrayList<String> alreadyListed = new ArrayList<>();
    ProgressDialog progressDialog;
    ArrayList<Friends> friends1 = new ArrayList<>();
    ArrayList<Friends> isInvitedListEmail = new ArrayList<>();
    ArrayList<Friends> isBuddyListEmail= new ArrayList<>();
    ArrayList<Friends> listfromServerEmail = new ArrayList<>();
    ArrayList<Friends> friendsArrayList1 = new ArrayList<>();
    ArrayList<Friends> previousArrayList = new ArrayList<>();

    int phone_size_selected = 0;
    int email_size_selected = 0;


    @Override
    protected void onResume() {
        super.onResume();
        isBuddyListPhone = new ArrayList<>();
        isInvitedListPhone = new ArrayList<>();
        listfromServerPhone = new ArrayList<>();
        isBuddyListEmail = new ArrayList<>();
        isInvitedListEmail = new ArrayList<>();
        listfromServerEmail = new ArrayList<>();

        friendsArrayList1.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_list);
        backButton = (ImageView) findViewById(R.id.backo);
        addEmails = (TextView) findViewById(R.id.emailtext);
        addPhone = (TextView) findViewById(R.id.phonetext);
        SharedPreferences preferences = getSharedPreferences("selectedContacts",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        UserModel userModel = AppUtils.getUserObject(this);
        userId = userModel.getUserId();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        num_email = (TextView)findViewById(R.id.num_email);
        num_phone = (TextView)findViewById(R.id.num_phone);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        earn170 = (TextView)findViewById(R.id.earn170);
        earn1700 = (TextView)findViewById(R.id.earn1700);
        earn170.setText("Earn "+getApplicationContext().getResources().getString(R.string.Rs)+"170 per invite");
        earn1700.setText("Earn "+getApplicationContext().getResources().getString(R.string.Rs)+"170 per invite");
        try{
            phone_size_selected = getIntent().getIntExtra("phone_send",0);
            email_size_selected = getIntent().getIntExtra("emails_send",0);
            if(phone_size_selected!=0){
                num_phone.setVisibility(View.VISIBLE);
                num_phone.setText(phone_size_selected+ " invited");
            }else{
                num_phone.setVisibility(View.GONE);
            }
            if(email_size_selected!=0){
                num_email.setVisibility(View.VISIBLE);
            num_email.setText(email_size_selected + " invited");
            }else{
                num_email.setVisibility(View.GONE);
            }

        }catch (Exception e){
            phone_size_selected = 0;
            email_size_selected = 0;
        }


        addPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhone.setEnabled(false);
                checkPermission();

//
            }
        });

        addEmails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmails.setEnabled(false);

                SharedPreferences preferences = getSharedPreferences("inviteCalls",MODE_PRIVATE);
                Boolean email_read = preferences.getBoolean("email_read",false);
                Boolean disconnect = preferences.getBoolean("DisconnectEmail",false);
                SharedPreferences sharedPrefs = getSharedPreferences("inviteCalls", MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPrefs.getString("email_contacts", null);
                if (json != null) {
                    Type type = new TypeToken<ArrayList<Friends>>() {
                    }.getType();
                    friends1 = gson.fromJson(json, type);
                }


                if(email_read && !disconnect){
                    new GetInvitedDataEmail(InviteList.this).execute();
                    SharedPreferences sharedPreferences = getSharedPreferences("preferencename2",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                }else {

                    Intent intent = new Intent(InviteList.this, AuthenticateEmail.class);
                    addEmails.setEnabled(true);
                    addPhone.setEnabled(true);
                    startActivity(intent);
                }

            }
        });

    }

    public class GetInvitedDataEmail extends AsyncTask<Void, Void, String> {
        String url = BuildConfig.SERVER_URL + "api/v1/user/contacts?userid=" + userId;
        Context context;

        public GetInvitedDataEmail(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                HttpResponse response = AppUtils.connectToServerGet(url, tok_sp, null);
                if (response != null) {
                    HttpEntity ent = response.getEntity();
                    String responseString = EntityUtils.toString(ent, "UTF-8");
                    if (response.getStatusLine().getStatusCode() != 200) {


                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {
                        JSONObject jsonObject = new JSONObject(responseString);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Boolean isInvited;
                            String phone;
                            String email;
                            JSONObject json = jsonArray.getJSONObject(i);
                            String name = json.getString("name");
                            try {
                                phone = json.getString("contactPhone");
                            }catch (JSONException e){
                                phone = "";
                            }
                            try{
                                email = json.getString("contactEmail");
                            }catch (JSONException e){
                                email = "";
                            }
                            Boolean isBuddy = json.getBoolean("isBuddy");
                            if(!isBuddy){
                                isInvited = json.getBoolean("isInvited");
                            }
                            else
                                isInvited = false;

                            Friends friends = new Friends(phone, email, isBuddy, isInvited, name);
                            if(email.length()!=0) {
                                if (isBuddy)
                                    isBuddyListEmail.add(friends);
                                if (!isBuddy && isInvited)
                                    isInvitedListEmail.add(friends);

                            }
                        }


                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            for(int i =0;i<isBuddyListEmail.size();i++){
                String name = isBuddyListEmail.get(i).getName();
                alreadyListed.add(name);
            }
            for(int i =0;i<isInvitedListEmail.size();i++){
                String name = isInvitedListEmail.get(i).getName();
                alreadyListed.add(name);
            }
            for(int i= 0;i<friends1.size();i++){
                String name = friends1.get(i).getName();
                if(!alreadyListed.contains(name)){
                    listfromServerEmail.add(friends1.get(i));
                }
            }

            Intent intent = new Intent(InviteList.this,FillEmailContacts.class);
            if (friends1 != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("invitedList",isInvitedListEmail);
                bundle.putSerializable("buddyList",isBuddyListEmail);
                bundle.putSerializable("userList",listfromServerEmail);
                intent.putExtras(bundle);
            }
            addPhone.setEnabled(true);
            addEmails.setEnabled(true);
            context.startActivity(intent);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED) {

            spinner.setVisibility(View.VISIBLE);

            readContacts();
            return true;

        } else {
            requestPermission();
            return false;

        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this
                , Manifest.permission.READ_CALL_LOG)) {

            Toast.makeText(getApplicationContext(), "Reading Contacts permission needed. Please allow in App Settings for sending invites.", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, PERMISSION_REQUEST_CODE);
        }
    }

    public void readContacts() {

        ContentResolver cr = getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);

                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Friends friends = new Friends();
                        if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            System.out.println("name : " + name + ", ID : " + id);
                            friends.setName(name);

                            // get the phone number
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                String phone = pCur.getString(
                                        pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                friends.setPhone_Num(phone);
                                break;
                            }


                            pCur.close();


                            // get email and type

                            Cursor emailCur = cr.query(
                                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            while (emailCur.moveToNext()) {
                                String email = emailCur.getString(
                                        emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                                friends.setEmail(email);
                                break;
                            }

                            emailCur.close();

                        }
                        friends.setType("sms");
                        if (friends.getName() == null || friends.getPhone_Num() == null) {
                        } else

                            friendsArrayList1.add(friends);

                    }



                    spinner.setVisibility(View.GONE);
                }


        SharedPreferences sharedPreferences11 = getSharedPreferences("inviteCalls2",MODE_PRIVATE);
        Boolean sendInvites1 = sharedPreferences11.getBoolean("phone-contacts-taken",false);
        if(!sendInvites1)
            new SendContactstoServer(this).execute();
        else
            new GetInvitedDataPhone(this).execute();

        SharedPreferences sharedPreferences = getSharedPreferences("preferencename1",MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences.edit();
        editor1.clear();
        editor1.commit();

    }

    public class SendContactstoServer extends AsyncTask<Void, Void, String> {
        Context context;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Fetching your contacts...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);

            pDialog.show();
        }

        public SendContactstoServer(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(String s) {
            if (pDialog.isShowing())
                pDialog.dismiss();

            SharedPreferences sharedPreferences = context.getSharedPreferences("inviteCalls2", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("phone-contacts-taken", true);
            editor.apply();

            new GetInvitedDataPhone(context).execute();



            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONObject jsonObject = new JSONObject();
            String url = BuildConfig.SERVER_URL + "api/v1/user/contacts";
            SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
            String tok_sp = toks.getString("token_value", "");
            try {
                jsonObject.put("userid", userId);
                JSONArray contacts = new JSONArray();
                for (int i = 0; i < friendsArrayList1.size(); i++) {
                    JSONObject json = new JSONObject();
                    json.put("name", friendsArrayList1.get(i).getName());
                    String phone = friendsArrayList1.get(i).getPhone_Num();
                    if (phone == null || phone.length() == 0) {
                        phone = "";
                    }
                    json.put("phone", phone);
                    String email = friendsArrayList1.get(i).getEmail();
                    if (email == null || email.length() == 0) {
                        email = "";
                    }
                    json.put("email", email);
                    contacts.put(json);
                }
                jsonObject.put("contacts", contacts);

                HttpResponse response = AppUtils.connectToServerPost(url, jsonObject.toString(), tok_sp);
                if (response != null) {
                    HttpEntity ent = response.getEntity();
                    String responseString = EntityUtils.toString(ent, "UTF-8");
                    if (response.getStatusLine().getStatusCode() != 200) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {
                        JSONObject resp = new JSONObject(responseString);

                        if (!resp.getString("status").contains("success")) {

                            Log.e("MeshCommunication", "Server returned code "
                                    + response.getStatusLine().getStatusCode());
                            return resp.getString("msg");
                        } else {

                            return "win";

                        }

                    }
                } else return "fail";

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Exception", e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


        public class GetInvitedDataPhone extends AsyncTask<Void, Void, String> {
            String url = BuildConfig.SERVER_URL + "api/v1/user/contacts?userid=" + userId;
            Context context;

            public GetInvitedDataPhone(Context context) {
                this.context = context;
            }

            @Override
            protected String doInBackground(Void... params) {

                try {
                    SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                    String tok_sp = toks.getString("token_value", "");
                    HttpResponse response = AppUtils.connectToServerGet(url, tok_sp, null);
                    if (response != null) {
                        HttpEntity ent = response.getEntity();
                        String responseString = EntityUtils.toString(ent, "UTF-8");
                        if (response.getStatusLine().getStatusCode() != 200) {


                            Log.e("MeshCommunication", "Server returned code "
                                    + response.getStatusLine().getStatusCode());
                            return "fail";
                        } else {
                            JSONObject jsonObject = new JSONObject(responseString);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Boolean isInvited;
                                String phone;
                                String email;
                                JSONObject json = jsonArray.getJSONObject(i);
                                String name = json.getString("name");
                                try {
                                    phone = json.getString("contactPhone");
                                }catch (JSONException e){
                                    phone = "";
                                }
                                try {
                                    email = json.getString("contactEmail");
                                }catch (JSONException e){
                                    email = "";
                                }

                                Boolean isBuddy = json.getBoolean("isBuddy");
                                if(!isBuddy){
                                     isInvited = json.getBoolean("isInvited");
                                }
                                else
                                isInvited = false;

                                Friends friends = new Friends(phone, email, isBuddy, isInvited, name);
                                if(phone.length()!=0 && phone!=null){
                                if (isBuddy) {
                                    isBuddyListPhone.add(friends);
                                    Log.i(friends.getName(),friends.getPhone_Num());
                                }
                                if (!isBuddy && isInvited)
                                    isInvitedListPhone.add(friends);

                            }
                            }


                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Finding your friends ...");
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(true);

                progressDialog.show();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                for(int i =0;i<isBuddyListPhone.size();i++){
                    String name = isBuddyListPhone.get(i).getName();
                    alreadyListed.add(name);
                }
                for(int i =0;i<isInvitedListPhone.size();i++){
                    String name = isInvitedListPhone.get(i).getName();
                    alreadyListed.add(name);
                }
                for(int i= 0;i<friendsArrayList1.size();i++){
                    String name = friendsArrayList1.get(i).getName();
                    if(!alreadyListed.contains(name)){
                        listfromServerPhone.add(friendsArrayList1.get(i));
                    }
                }

                progressDialog.dismiss();

                Intent intent = new Intent(InviteList.this, GetContacts.class);
                if (friendsArrayList1 != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("invitedList",isInvitedListPhone);
                    bundle.putSerializable("buddyList",isBuddyListPhone);
                    bundle.putSerializable("userList",listfromServerPhone);
                    intent.putExtras(bundle);
                }
                context.startActivity(intent);
            }
        }

    }

