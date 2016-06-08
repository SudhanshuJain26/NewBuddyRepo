package indwin.c3.shareapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import indwin.c3.shareapp.HomePage;
import indwin.c3.shareapp.R;
import indwin.c3.shareapp.Views.RoundedTransformation;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import io.intercom.com.google.gson.Gson;

public class Pending60kApprovalActivity extends AppCompatActivity {
    ImageView userPic;
    SharedPreferences mPrefs;
    Gson gson;
    UserModel user;
    TextView userName;
    Button unlockMoreCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending60k_approval);
        try {
            //            final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.helptip);
            //            upArrow.setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_ATOP);
            //            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            //            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //            getSupportActionBar().setDisplayShowTitleEnabled(false);

            mPrefs = getSharedPreferences("buddy", Context.MODE_PRIVATE);
            user = AppUtils.getUserObject(this);

            userPic = (ImageView) findViewById(R.id.user_image);
            SharedPreferences sf = getSharedPreferences("proid", Context.MODE_PRIVATE);
            Picasso.with(this).load("https://graph.facebook.com/" + sf.getString("dpid", "") + "/picture?type=large").transform(new RoundedTransformation(5, 0)).into(userPic);
            userName = (TextView) findViewById(R.id.name);
            String userText = "Hi " + user.getName() + ",";
            userName.setText(userText);

            user.setAppliedFor60k(true);
            AppUtils.saveUserObject(this, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        unlockMoreCredit = (Button) findViewById(R.id.unlock_more_credit);
        unlockMoreCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pending60kApprovalActivity.this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Pending60kApprovalActivity.this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
