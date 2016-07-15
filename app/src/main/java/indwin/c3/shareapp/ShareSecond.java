package indwin.c3.shareapp;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import indwin.c3.shareapp.activities.InviteList;
import indwin.c3.shareapp.activities.ShowSelectedItems;
import indwin.c3.shareapp.models.Image;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;

public class ShareSecond extends AppCompatActivity {
    SharedPreferences sh_otp;
    private int cc;
    private int checksharefromweb=0;
    TextView referral;
    TextView line1,line2,line3,line4;
    String[] amount = new String[3];
    TextView blue1,blue2;

    @Override
    public void onBackPressed() {
        if ((sh_otp.getInt("chshare", 0) == 1))

            finish();
        else
        if(cc==2)
            finish();
        else  {
            Intent in=new Intent(ShareSecond.this,HomePage.class);
            finish();
            in.putExtra("Name", getIntent().getExtras().getString("name"));
            in.putExtra("Email",getIntent().getExtras().getString("email"));
            in.putExtra("Form", "empty");
            startActivity(in);
            overridePendingTransition(0,0);}
        // code here to show dialoguper.onBackPressed();  // optional depending on your needs
    }


    ImageView email;
    ImageView backButton;
    RelativeLayout couponCode;


    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
        case 1:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Uri uri = Uri.parse("android.resource://indwin.c3.shareapp/" + R.drawable.buddyrefer2);
            Uri imageUri = null;
            try {
                imageUri = Uri.parse(MediaStore.Images.Media.insertImage(ShareSecond.this.getContentResolver(),
                        BitmapFactory.decodeResource(getResources(), R.drawable.buddyrefer2), null, null));
            } catch (NullPointerException e) {
            }
//                String shareBody="https://play.google.com/store/apps/details?id=indwin.c3.shareapp&referrer="+sh_otp.getString("rcode","");
//                shareIntent
//                String shareBody = "Hey check out this crazy website!\n" +
//                        "Remember to use the code "+sh_otp.getString("rcode","")+" while signing up!";
            String shareBody="Buy mobiles, laptops, fashion accessories and more on easy monthly installments! Remember to use "+ sh_otp.getString("rcode","") +" while signing up! www.hellobuddy.in";
            Intent sharingIntent = new Intent();
            sharingIntent.setAction(Intent.ACTION_SEND);
            sharingIntent.setType("image/*");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("preferencename1", 0);
        SharedPreferences.Editor editor2 = prefs.edit();
        editor2.clear();
        editor2.commit();

        SharedPreferences preferences = getSharedPreferences("selectedContacts",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            cc=getIntent().getExtras().getInt("cc");}
        catch(Exception e)
        {}

        try{
            checksharefromweb=getIntent().getExtras().getInt("checksharefromweb");
        }
        catch (Exception e){}
        sh_otp = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_share_second);
        line1 = (TextView)findViewById(R.id.line1);
        line2 = (TextView)findViewById(R.id.line2);
        line3 = (TextView)findViewById(R.id.line3);
        line4 = (TextView)findViewById(R.id.verify);
        blue1 = (TextView)findViewById(R.id.blue1);
        blue2 = (TextView)findViewById(R.id.blue2);


        blue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareSecond.this,ShowSelectedItems.class);
                startActivity(intent);
            }
        });

        blue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int results = ContextCompat.checkSelfPermission(ShareSecond.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

                if(results== PackageManager.PERMISSION_GRANTED)
                {
                    Uri uri = Uri.parse("android.resource://indwin.c3.shareapp/" + R.drawable.buddyrefer);
                    Uri imageUri = null;
                    try {
                        imageUri = Uri.parse(MediaStore.Images.Media.insertImage(ShareSecond.this.getContentResolver(),
                                BitmapFactory.decodeResource(getResources(), R.drawable.buddyrefer), null, null));
                    } catch (NullPointerException e) {
                    }
//                String shareBody="https://play.google.com/store/apps/details?id=indwin.c3.shareapp&referrer="+sh_otp.getString("rcode","");
//                shareIntent
//                String shareBody = "Hey check out this crazy website!\n" +
//                        "Remember to use the code "+sh_otp.getString("rcode","")+" while signing up!";
                    String shareBody="Buy mobiles,laptops,fashion accessories of student friendly payment plans. Only on Buddy! Signup using my special link https://hellobuddy.in/me?rc="+ sh_otp.getString("rcode","") +"  and get Rs. 150 cashback as soon as you sign up.";
//                String shareBody="Buy mobiles, laptops, fashion accessories and more on easy monthly installments! Remember to use "+ sh_otp.getString("rcode","") +" while signing up! www.hellobuddy.in";
                    Intent sharingIntent = new Intent();
                    sharingIntent.setAction(Intent.ACTION_SEND);
                    sharingIntent.setType("image/*");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));}
                else
                    ActivityCompat.requestPermissions(ShareSecond.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        });


//        line1.setText(Html.fromHtml("Invite your friends to Buddy and <b>earn " +getApplicationContext().getResources().getString(R.string.Rs)+"150 Cashback</b> for every friend that signs up with your code"));
//        line2.setText(Html.fromHtml("There more! <b>Earn " + getApplicationContext().getResources().getString(R.string.Rs)+"20 in Paytm Cash</b> everytime an invited friend submits their profile for verification!"));
//        line3.setText(Html.fromHtml("<b>Get upto " + getApplicationContext().getResources().getString(R.string.Rs)+"3000 Credit Limit Boost</b> by referring your friends after applying for a Credit Limit."));
       new GetAmount().execute("http://ssl.hellobuddy.in/api/v1/share/about");
        backButton = (ImageView)findViewById(R.id.backo);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareSecond.this,HomePage.class);
                startActivity(intent);
            }
        });
        couponCode = (RelativeLayout)findViewById(R.id.coupon_code);
        email = (ImageView)findViewById(R.id.email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareSecond.this, ShowSelectedItems.class);
                startActivity(intent);
            }
        });
        try{
            referral=(TextView)findViewById(R.id.inside);
            String sharecode=sh_otp.getString("rcode", "");
            referral.setText(sharecode);
        }

        catch(Exception e){
        }

        line4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareSecond.this,ViewForm.class);
                intent.putExtra("url","https://hellobuddy.in/refer-&-earn");
                intent.putExtra("which_page",26);
                startActivity(intent);
            }
        });



        couponCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int results = ContextCompat.checkSelfPermission(ShareSecond.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

                if(results== PackageManager.PERMISSION_GRANTED)
                {
                    Uri uri = Uri.parse("android.resource://indwin.c3.shareapp/" + R.drawable.buddyrefer);
                    Uri imageUri = null;
                    try {
                        imageUri = Uri.parse(MediaStore.Images.Media.insertImage(ShareSecond.this.getContentResolver(),
                                BitmapFactory.decodeResource(getResources(), R.drawable.buddyrefer), null, null));
                    } catch (NullPointerException e) {
                    }
//                String shareBody="https://play.google.com/store/apps/details?id=indwin.c3.shareapp&referrer="+sh_otp.getString("rcode","");
//                shareIntent
//                String shareBody = "Hey check out this crazy website!\n" +
//                        "Remember to use the code "+sh_otp.getString("rcode","")+" while signing up!";
                    String shareBody="Buy mobiles,laptops,fashion accessories of student friendly payment plans. Only on Buddy! Signup using my special link https://hellobuddy.in/me?rc="+ sh_otp.getString("rcode","") +"  and get Rs. 150 cashback as soon as you sign up.";
//                String shareBody="Buy mobiles, laptops, fashion accessories and more on easy monthly installments! Remember to use "+ sh_otp.getString("rcode","") +" while signing up! www.hellobuddy.in";
                    Intent sharingIntent = new Intent();
                    sharingIntent.setAction(Intent.ACTION_SEND);
                    sharingIntent.setType("image/*");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));}
                else
                    ActivityCompat.requestPermissions(ShareSecond.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        });


    }

    public class GetAmount extends AsyncTask<String,Void,String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            line1.setText(Html.fromHtml("Invite your friends to Buddy and <b>earn " +getApplicationContext().getResources().getString(R.string.Rs)+ amount[0] +" Cashback</b> for every friend that signs up with your code"));
            line2.setText(Html.fromHtml("There more! <b>Earn " + getApplicationContext().getResources().getString(R.string.Rs)+amount[1]+" in Paytm Cash</b> everytime an invited friend submits their profile for verification!"));
            line3.setText(Html.fromHtml("<b>Get upto " + getApplicationContext().getResources().getString(R.string.Rs)+"3000 Credit Limit Boost</b> by referring your friends after applying for a Credit Limit."));

        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];

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
                            JSONArray jsonArray = resp.getJSONArray("msg");
                            for(int i =0;i<3;i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                String amount1 = object.getString("award");
                                amount[i] = amount1;
                            }

                            return "win";
                        } else
                            return "";


                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }
            return null;

        }
    }


}
