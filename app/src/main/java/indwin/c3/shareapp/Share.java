package indwin.c3.shareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Share extends AppCompatActivity {
SharedPreferences sh_otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sh_otp = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_otp);
        try{
        TextView referral=(TextView)findViewById(R.id.code);
        referral.setText(sh_otp.getString("rcode",""));}
        catch(Exception e){
            }
        TextView done=(TextView)findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sh_otp.getInt("chshare",0)==1)
                finish();
                else
                {
                Intent in = new Intent(Share.this, Formempty.class);
                finish();
                in.putExtra("Name", getIntent().getExtras().getString("name"));
                in.putExtra("Email", getIntent().getExtras().getString("email"));
                in.putExtra("Form","empty");
                startActivity(in);
                    overridePendingTransition(0,0);}

            }
        });
        TextView share=(TextView)findViewById(R.id.shareNow);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/png");
                Uri uri = Uri.parse("android.resource://indwin.c3.shareapp/" + R.drawable.buddyrefer);

//                shareIntent
                String shareBody = "Hey check out this crazy website!\n" +
                        "Remember to use the code"+sh_otp.getString("rcode","")+" while signing up!";

                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });
    }
    @Override
    public void onBackPressed()
    {  if(sh_otp.getInt("chshare",0)==1)
        finish();
      else  {
        Intent in=new Intent(Share.this,Formempty.class);
        finish();
        in.putExtra("Name", getIntent().getExtras().getString("name"));
        in.putExtra("Email",getIntent().getExtras().getString("email"));
        in.putExtra("Form", "empty");
        startActivity(in);
        overridePendingTransition(0,0);}
        // code here to show dialoguper.onBackPressed();  // optional depending on your needs
    }
}
