package indwin.c3.shareapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.models.UserModel;
import io.intercom.com.google.gson.Gson;

/**
 * Created by shubhang on 14/04/16.
 */
public class FlashRejectedActivity extends AppCompatActivity {

    ImageView userPic;
    SharedPreferences mPrefs;
    Gson gson;
    UserModel user;
    TextView userName,flashRejectedText,flashRejectedText1,meanwhileText;
    Button unlockMoreCredit, searchProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_approved);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mPrefs = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        gson = new Gson();
        String json = mPrefs.getString("UserObject", "");
        user = gson.fromJson(json, UserModel.class);

        userPic = (ImageView) findViewById(R.id.status_image);
        userPic.setBackgroundResource(R.drawable.verifygraphics);
        userName = (TextView) findViewById(R.id.name);
        String text = "Sorry " + user.getName() + ",";
        userName.setText(text);

        unlockMoreCredit = (Button) findViewById(R.id.unlock_more_credit);
        searchProduct = (Button) findViewById(R.id.choose_product);
        flashRejectedText = (TextView) findViewById(R.id.flash_credit_text);
        flashRejectedText1 = (TextView) findViewById(R.id.flash_credit_text2);
        meanwhileText = (TextView) findViewById(R.id.meanwhile_text);

        meanwhileText.setText("But worry not...");
        flashRejectedText.setText("We are unable to approve");
        flashRejectedText1.setText("Rs.1000 FLASH Credit for you.");
        searchProduct.setVisibility(View.GONE);
        unlockMoreCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlashRejectedActivity.this, ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FlashRejectedActivity.this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
