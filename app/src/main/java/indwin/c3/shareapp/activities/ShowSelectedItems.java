package indwin.c3.shareapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import indwin.c3.shareapp.BuildConfig;
import indwin.c3.shareapp.R;
import indwin.c3.shareapp.ShareSecond;
import indwin.c3.shareapp.models.Friends;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;

public class ShowSelectedItems extends AppCompatActivity {

    TextView[] names_phone = new TextView[4];
    ImageView[] images_phone = new ImageView[4];

    TextView email;
    TextView phone;

    int size_phone_contacts =0;
    int size_email_contacts = 0;

    int size_selected_email = 0;
    int size_selected_phone = 0;

    TextView no_items_phone;
    TextView no_items_email;

    TextView addMorePhone;
    TextView addMoreEmail;

    List<Friends> selectedPhoneList = new ArrayList<>();
    List<Friends> selectedEmailList = new ArrayList<>();
    List<Friends> totalSelected = new ArrayList<>();
    String userId,name,referralCode,tok_sp;
    FrameLayout frame1;

    SharedPreferences sh_otp;
    SharedPreferences toks;


    ImageView button;

    Button send_invite;
     float alpha_diff;

    TextView[] names_email;
    ImageView[] images_email;

    TextView phoneInvited;
    TextView emailInvited;

    int num_phones_invited;
    int num_emails_invited;

    Boolean page;
    Boolean page1;

    TextView textView1;
    TextView textView2;

    List<Friends> completePhoneList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_selected_items);
        send_invite = (Button)findViewById(R.id.send_invite);
        sh_otp = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
        referralCode = sh_otp.getString("rcode","");
        no_items_phone = (TextView) findViewById(R.id.no_items);
        no_items_email = (TextView) findViewById(R.id.no_items_email);
        frame1 = (FrameLayout) findViewById(R.id.success_frame);
        addMorePhone = (TextView)findViewById(R.id.add);
        addMoreEmail = (TextView)findViewById(R.id.addMore);
        email = (TextView)findViewById(R.id.phone_text_email);
        phone = (TextView)findViewById(R.id.phone_text);
        SharedPreferences preferences4 = getSharedPreferences("list1", MODE_PRIVATE);
        num_phones_invited = preferences4.getInt("inviteSize",0);
        SharedPreferences preferences5 = getSharedPreferences("list2", MODE_PRIVATE);
        num_emails_invited = preferences5.getInt("inviteSize",0);
        phoneInvited = (TextView)findViewById(R.id.phone_invited);
        emailInvited = (TextView)findViewById(R.id.email_invited);

        phoneInvited.setText(num_phones_invited+" Invited");


        emailInvited.setText(num_emails_invited+ " Invited");


        textView1 = (TextView)findViewById(R.id.text);
        textView2 = (TextView)findViewById(R.id.text_email);

        if(num_phones_invited==0) {
            phoneInvited.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
        }
        else {
            phoneInvited.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.VISIBLE);
        }

        if(num_emails_invited==0) {
            emailInvited.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
        }
        else {
            emailInvited.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
        }

        textView1.setText("Earn upto "+ getApplicationContext().getResources().getString(R.string.Rs) + num_phones_invited*190);
        textView2.setText("Earn upto "+ getApplicationContext().getResources().getString(R.string.Rs) + num_emails_invited*190);
        final SharedPreferences sharedPreferences = getSharedPreferences("disconnect",MODE_PRIVATE);
        page = sharedPreferences.getBoolean("disconnectemail",false);
        SharedPreferences sharedPreferences1 = getSharedPreferences("authenticate",MODE_PRIVATE);
        page1 = sharedPreferences1.getBoolean("authenticatedone",false);



        try{
            int status = getIntent().getIntExtra("phone",100);
            if(status==0){
                SharedPreferences preferences = getSharedPreferences("selectedContacts",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("phone_contacts_selected");
                editor.apply();

            }
        }catch (Exception e){

        }

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page1) {
                    Intent intent = new Intent(ShowSelectedItems.this, GetContacts.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(ShowSelectedItems.this, GetContacts.class);
                    startActivity(intent);
                }
//                phoneInvited.setVisibility(View.GONE);
//                emailInvited.setVisibility(View.GONE);
            }
        });



        UserModel userModel = AppUtils.getUserObject(this);
        userId = userModel.getUserId();
        name = userModel.getName();
        button = (ImageView)findViewById(R.id.backo);

        SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
        tok_sp = toks.getString("token_value", "");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowSelectedItems.this, ShareSecond.class);
                startActivity(intent);
                finish();
            }
        });

        send_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendSelectedContactstoServer(ShowSelectedItems.this).execute();
                SharedPreferences preferences = getSharedPreferences("selectedContacts",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor .commit();
                SharedPreferences preferences1 = getSharedPreferences("CHECKBOX_STATE",MODE_PRIVATE);
                SharedPreferences.Editor editor1 = preferences1.edit();
                editor1.clear();
                editor.commit();
                SharedPreferences prefs = getSharedPreferences("preferencename1", 0);
                SharedPreferences.Editor editor2 = prefs.edit();
                editor2.clear();
                editor2.commit();

                addMoreEmail.setVisibility(View.GONE);
                addMorePhone.setVisibility(View.GONE);
                phoneInvited.setVisibility(View.VISIBLE);
                emailInvited.setVisibility(View.VISIBLE);
                no_items_email.setVisibility(View.GONE);
                no_items_phone.setVisibility(View.GONE);
                phoneInvited.setText(num_phones_invited+size_selected_phone+" Invited");
                emailInvited.setText(num_emails_invited+size_selected_email+ " Invited");
                SharedPreferences preferences4 = getSharedPreferences("list1", MODE_PRIVATE);
                num_phones_invited = preferences4.getInt("inviteSize",0);
                SharedPreferences preferences5 = getSharedPreferences("list2", MODE_PRIVATE);
                num_emails_invited = preferences5.getInt("inviteSize",0);
                textView1.setText("Earn upto "+getApplicationContext().getString(R.string.Rs)+170*(num_phones_invited+size_selected_phone));
                textView2.setText("Earn upto "+getApplicationContext().getString(R.string.Rs)+170*(num_emails_invited+size_selected_email));


                for(int i=0;i<names_phone.length;i++){
                    names_phone[i].setVisibility(View.GONE);
                }

                for(int i=0;i<images_phone.length;i++){
                    images_phone[i].setVisibility(View.GONE);
                }


                for(int i=0;i<names_email.length;i++){
                    names_email[i].setVisibility(View.GONE);
                }

                for(int i=0;i<images_email.length;i++){
                    images_email[i].setVisibility(View.GONE);
                }

            }
        });



        SharedPreferences sharedPrefs = getSharedPreferences("selectedContacts",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("phone_contacts_selected", null);
        if(json!=null) {
            Type type = new TypeToken<ArrayList<Friends>>() {
            }.getType();
            selectedPhoneList = gson.fromJson(json, type);
            if (selectedPhoneList != null && selectedPhoneList.size()!=0) {
                size_selected_phone = selectedPhoneList.size();
                no_items_phone.setText("" + size_selected_phone + " Selected");
                if(no_items_phone.getVisibility()==View.VISIBLE)
                phoneInvited.setVisibility(View.GONE);
                if (size_selected_phone >= 4) {
                    size_phone_contacts = 4;
                } else if (size_selected_phone < 4) {
                   size_phone_contacts = size_selected_phone;
                }

            }else {
                size_email_contacts = 0;
                addMoreEmail.setVisibility(View.GONE);
            }
        }



        SharedPreferences sharedPrefs1 = getSharedPreferences("selectedContacts",MODE_PRIVATE);
        Gson gson1 = new Gson();
        String json1 = sharedPrefs1.getString("email_contacts_selected", null);
        if(json1!=null) {
            Type type = new TypeToken<ArrayList<Friends>>() {
            }.getType();
            selectedEmailList = gson1.fromJson(json1, type);
            if (selectedEmailList != null && selectedEmailList.size()!=0) {
                size_selected_email = selectedEmailList.size();
                no_items_email.setText("" + size_selected_email + " Selected");
                if(no_items_email.getVisibility()==View.VISIBLE)
                emailInvited.setVisibility(View.GONE);
                if (size_selected_phone >= 4) {
                    if (size_selected_email > 3) {
                        size_email_contacts = 3;
                    } else {
                        size_email_contacts = size_selected_email;
                    }
                } else if (size_selected_phone < 4) {
                    if (size_selected_email > 3) {
                        size_email_contacts = 7 - size_selected_phone;
                    } else {
                        size_email_contacts = size_selected_email;
                    }
                }
                size_phone_contacts = size_selected_phone;
            }else {
                size_email_contacts = 0;
                addMoreEmail.setVisibility(View.GONE);
            }
        }
        if(selectedEmailList!=null){
            totalSelected.addAll(selectedEmailList);

        }
        if(selectedPhoneList!=null){
            totalSelected.addAll(selectedPhoneList);
        }



        names_phone[0] =(TextView)findViewById(R.id.text1);
        names_phone[1] =(TextView)findViewById(R.id.text2);
        names_phone[2] =(TextView)findViewById(R.id.text3);
        names_phone[3] =(TextView)findViewById(R.id.text4);

        for(int i=0;i<names_phone.length;i++){
            names_phone[i].setVisibility(View.VISIBLE);
        }

        email = (TextView)findViewById(R.id.phone_text_email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences2 = getSharedPreferences("authenticate",MODE_PRIVATE);
                Boolean flag = sharedPreferences2.getBoolean("authenticatedone",false);

                if(!flag) {
                    Intent intent = new Intent(ShowSelectedItems.this, AuthenticateEmail.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(ShowSelectedItems.this, FillEmailContacts.class);
                    startActivity(intent);
                }

            }
        });

        images_phone[0] = (ImageView)findViewById(R.id.image1);
        images_phone[1] = (ImageView)findViewById(R.id.image2);
        images_phone[2] = (ImageView)findViewById(R.id.image3);
        images_phone[3] = (ImageView)findViewById(R.id.image4);

        for(int i=0;i<images_phone.length;i++){
            images_phone[i].setVisibility(View.VISIBLE);
        }

        if(size_phone_contacts<4){
            names_phone[3].setVisibility(View.GONE);
            images_phone[3].setVisibility(View.GONE);
            addMorePhone.setVisibility(View.VISIBLE);
            if(size_phone_contacts<3){
                names_phone[2].setVisibility(View.GONE);
                images_phone[2].setVisibility(View.GONE);
                addMorePhone.setVisibility(View.VISIBLE);
                if(size_phone_contacts<2){
                    names_phone[1].setVisibility(View.GONE);
                    images_phone[1].setVisibility(View.GONE);
                    addMorePhone.setVisibility(View.VISIBLE);
                }if(size_phone_contacts<1){
                    names_phone[0].setVisibility(View.GONE);
                    images_phone[0].setVisibility(View.GONE);
                    addMorePhone.setVisibility(View.GONE);
                }
            }
        }



        names_email = new TextView[7];
        images_email = new ImageView[7];
        names_email[0] = (TextView)findViewById(R.id.text_email1);
        names_email[1] = (TextView)findViewById(R.id.text_email2);
        names_email[2] = (TextView)findViewById(R.id.text_email3);
        names_email[3] = (TextView)findViewById(R.id.text_email4);
        names_email[4] = (TextView)findViewById(R.id.text_email5);
        names_email[5] = (TextView)findViewById(R.id.text_email6);
        names_email[6] = (TextView)findViewById(R.id.text_email7);

        for(int i=0;i<names_email.length;i++){
            names_email[i].setVisibility(View.VISIBLE);
        }

        images_email[0] = (ImageView)findViewById(R.id.image_email1);
        images_email[1] = (ImageView)findViewById(R.id.image_email2);
        images_email[2] = (ImageView)findViewById(R.id.image_email3);
        images_email[3] = (ImageView)findViewById(R.id.image_email4);
        images_email[4] = (ImageView)findViewById(R.id.image_email5);
        images_email[5] = (ImageView)findViewById(R.id.image_email6);
        images_email[6] = (ImageView)findViewById(R.id.image_email7);

        for(int i=0;i<images_email.length;i++){
            images_email[i].setVisibility(View.VISIBLE);
        }

        if(size_email_contacts>=3)
            alpha_diff = 0.2f;
        else
            alpha_diff = 0.3f;


        for(int i=0;i<size_email_contacts;i++){
            names_email[i].setText(selectedEmailList.get(i).getName());
            names_email[i].setAlpha(1-(i*alpha_diff));
            if (i% 3 == 0)
                images_email[i].setImageResource(R.drawable.blueuser1x);
            if (i % 3 == 1)
                images_email[i].setImageResource(R.drawable.greenuser1x);
            if (i % 3 == 2)
                images_email[i].setImageResource(R.drawable.reduser1x);

            images_email[i].setAlpha(1-(i*alpha_diff));


        }

        if(size_email_contacts>0){
            addMoreEmail.setVisibility(View.VISIBLE);
        }else {
            addMoreEmail.setVisibility(View.GONE);
        }
        for(int i=6;i>=size_email_contacts;i--){
            names_email[i].setVisibility(View.GONE);
            images_email[i].setVisibility(View.GONE);

        }




        addMorePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ShowSelectedItems.this,GetContacts.class);
                intent.putExtra("addMorePhone",100);
                startActivity(intent);



            }
        });

        addMoreEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(ShowSelectedItems.this, FillEmailContacts.class);
                    startActivity(intent);


                }

        });



        if(selectedPhoneList.size()>4)
            size_phone_contacts = 4;
        if(selectedPhoneList.size()<=4)
            size_phone_contacts = selectedPhoneList.size();




//        email.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(ShowSelectedItems.this,AuthenticateEmail.class);
//                startActivity(intent1);
//            }
//        });

        for(int i=0;i<size_phone_contacts;i++){
            names_phone[i].setText(selectedPhoneList.get(i).getName());
            if (i% 3 == 0)
                images_phone[i].setImageResource(R.drawable.blueuser1x);
            if (i % 3 == 1)
                images_phone[i].setImageResource(R.drawable.greenuser1x);
            if (i % 3 == 2)
                images_phone[i].setImageResource(R.drawable.reduser1x);


        }

    if(size_email_contacts==0 && size_phone_contacts==0){
        send_invite.setVisibility(View.GONE);
    }else{
        send_invite.setVisibility(View.VISIBLE);
    }


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPrefs = getSharedPreferences("selectedContacts",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("phone_contacts_selected", null);
        if(json!=null) {
            Type type = new TypeToken<ArrayList<Friends>>() {
            }.getType();
            selectedPhoneList = gson.fromJson(json, type);
            if (selectedPhoneList != null && selectedPhoneList.size()!=0) {
                size_selected_phone = selectedPhoneList.size();
                no_items_phone.setText("" + size_selected_phone + " Selected");
                if(no_items_phone.getVisibility()==View.VISIBLE)
                    phoneInvited.setVisibility(View.GONE);
                if (size_selected_phone >= 4) {
                    size_phone_contacts = 4;
                } else if (size_selected_phone < 4) {
                    size_phone_contacts = size_selected_phone;
                }

            }else {
                size_email_contacts = 0;
                addMoreEmail.setVisibility(View.GONE);
            }
        }
    }

    public class SendSelectedContactstoServer extends AsyncTask<Void,Void,String> {
        @Override
        protected void onPostExecute(String s) {
            if(pd.isShowing())
                pd.dismiss();
            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    frame1.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFinish() {
                   frame1.setVisibility(View.GONE);

                }
            }.start();

        }

        @Override
        protected void onPreExecute() {

            pd.setMessage("Sending invites ...");
            pd.setIndeterminate(false);
            pd.setCancelable(true);
            pd.show();
        }

        Context context;
        ProgressDialog pd ;

        public SendSelectedContactstoServer(Context context) {
            this.context = context;
            pd = new ProgressDialog(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONObject jsonObject = new JSONObject();
            String url = BuildConfig.SERVER_URL +"api/v1/user/invite";

            try {
                jsonObject.put("userid", userId);
                jsonObject.put("name",name);
                jsonObject.put("refCode",referralCode);
                JSONArray contacts = new JSONArray();
                for(int i = 0; i<totalSelected.size(); i++){
                    JSONObject json = new JSONObject();
                    json.put("name", totalSelected.get(i).getName());

                    String phone = totalSelected.get(i).getPhone_Num();
                    if(phone == null || phone.length()==0){
                        phone = "";
                    }
                    json.put("phone", phone);
                    String email = totalSelected.get(i).getEmail();
                    if(email==null || email.length()==0){
                        email = "";
                    }
                    json.put("email",email);
                    contacts.put(json);
                }
                jsonObject.put("contacts",contacts);

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
                Log.e("Exception",e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
