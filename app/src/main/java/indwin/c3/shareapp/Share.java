package indwin.c3.shareapp;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.Permission;

public class Share extends AppCompatActivity {
SharedPreferences sh_otp;
    private int cc;
    private int checksharefromweb=0;
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
        setContentView(R.layout.activity_otp);
        try{
        TextView referral=(TextView)findViewById(R.id.shareNow);
            String sharecode=sh_otp.getString("rcode", "");
        referral.setText(sh_otp.getString("rcode", ""));}
        catch(Exception e){
            }
        TextView done=(TextView)findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((sh_otp.getInt("chshare", 0) == 1))
                    finish();
                else
                if(cc==2)
                    finish();
                else {
                    Intent in = new Intent(Share.this, HomePage.class);
                    finish();
                    in.putExtra("Name", getIntent().getExtras().getString("name"));
                    in.putExtra("Email", getIntent().getExtras().getString("email"));
                    in.putExtra("Form", "empty");
                    startActivity(in);
                    overridePendingTransition(0, 0);
                }

            }
        });
        RelativeLayout share=(RelativeLayout)findViewById(R.id.rlCode);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int results = ContextCompat.checkSelfPermission(Share.this, Manifest.permission.READ_EXTERNAL_STORAGE);

if(results== PackageManager.PERMISSION_GRANTED)
{
                Uri uri = Uri.parse("android.resource://indwin.c3.shareapp/" + R.drawable.buddyrefer);
                Uri imageUri = null;
                try {
                    imageUri = Uri.parse(MediaStore.Images.Media.insertImage(Share.this.getContentResolver(),
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
                ActivityCompat.requestPermissions(Share.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Uri uri = Uri.parse("android.resource://indwin.c3.shareapp/" + R.drawable.buddyrefer2);
                Uri imageUri = null;
                try {
                    imageUri = Uri.parse(MediaStore.Images.Media.insertImage(Share.this.getContentResolver(),
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
                startActivity(Intent.createChooser(sharingIntent, "Share via"));}}}
    @Override
    public void onBackPressed()
    {  if ((sh_otp.getInt("chshare", 0) == 1))

        finish();
    else
        if(cc==2)
           finish();
      else  {
        Intent in=new Intent(Share.this,HomePage.class);
        finish();
        in.putExtra("Name", getIntent().getExtras().getString("name"));
        in.putExtra("Email",getIntent().getExtras().getString("email"));
        in.putExtra("Form", "empty");
        startActivity(in);
        overridePendingTransition(0,0);}
        // code here to show dialoguper.onBackPressed();  // optional depending on your needs
    }
}
