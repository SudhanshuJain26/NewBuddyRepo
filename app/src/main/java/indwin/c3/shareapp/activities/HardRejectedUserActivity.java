package indwin.c3.shareapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.models.UserModel;
import io.intercom.com.google.gson.Gson;

public class HardRejectedUserActivity extends AppCompatActivity {

    TextView collegeEndingSoon, collegeNotServiceable, userName, underAge;
    SharedPreferences mPrefs;
    Gson gson;
    UserModel user;
    Button iUnderstand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_rejected_user);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        mPrefs = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        gson = new Gson();
        String json = mPrefs.getString("UserObject", "");
        user = gson.fromJson(json, UserModel.class);
        try {
            userName = (TextView) findViewById(R.id.name);
            String userText = "Sorry " + user.getName().split(" ")[0] + ",";
            userName.setText(userText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean isCollegeEndingSoon = extras.getBoolean("isCollegeEndingSoon");
            boolean isCollegeOutside = extras.getBoolean("isCollegeOutside");
            boolean isUnderAge = extras.getBoolean("isUnderAge");

            if (isCollegeEndingSoon) {
                collegeEndingSoon = (TextView) findViewById(R.id.college_end_too_early);
                user.setRejectionReason(collegeEndingSoon.getText().toString());
                collegeEndingSoon.setVisibility(View.VISIBLE);
            }
            if (isCollegeOutside) {
                collegeNotServiceable = (TextView) findViewById(R.id.college_not_servicing);
                user.setRejectionReason(collegeNotServiceable.getText().toString());
                collegeNotServiceable.setVisibility(View.VISIBLE);
            }
            if (isUnderAge) {
                underAge = (TextView) findViewById(R.id.under_age);
                user.setRejectionReason(underAge.getText().toString());
                underAge.setVisibility(View.VISIBLE);
            }
        }
        iUnderstand = (Button) findViewById(R.id.i_understand);
        iUnderstand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setProfileStatus("declined");
                String json = gson.toJson(user);
                mPrefs.edit().putString("UserObject", json).apply();
                Intent intent = new Intent(HardRejectedUserActivity.this, ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
