package indwin.c3.shareapp.activities;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import indwin.c3.shareapp.Landing;
import indwin.c3.shareapp.MainActivity;
import indwin.c3.shareapp.R;
import indwin.c3.shareapp.Share;
import indwin.c3.shareapp.Splash;
import indwin.c3.shareapp.ViewForm;
import indwin.c3.shareapp.Views.MLRoundedImageView;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.Constants;
import io.intercom.android.sdk.Intercom;

/**
 * Created by shubhang on 17/03/16.
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Intent intform;
    private Toolbar toolbar;
    private MLRoundedImageView profilePic;
    private TextView userName;
    private CardView verifyIdentityCardView, autoRepayCardView, moreCreditCardView, declinedCardView;
    private TextView creditLimit, creditAvailable, cashBack;
    private ImageView verifyIdentity;
    private ImageView verifyIdentityProgressBar, autoRepaymentsProgressBar, moreCreditProgressBar;
    private TextView verifyIdentityText, autoRepaymentText, moreCreditText, declinedReason;
    private ImageView verifyIdentityLock, autoRepaymentLock, moreCreditLock;
    SharedPreferences mPrefs;
    private Button iUnderstand;
    private TextView hiName;
    private LinearLayout layout, middleLayout;
    UserModel user;
    private String formstatus, name, fbid, rejectionReason, email, uniqueCode, verificationdate, searchTitle, searchBrand, searchCategory, searchSubcategory, description, specification, review, infor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = AppUtils.getUserObject(this);
        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            TextView headerTitle = (TextView) findViewById(R.id.activity_header);
            headerTitle.setText("Profile");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ImageView inter = (ImageView) findViewById(R.id.interCom);
            inter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
                        Intercom.client().displayMessageComposer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences sh = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        boolean isUpdatingDB = sh.getBoolean("updatingDB", false);
        if (!isUpdatingDB) {

            new ValidateForm().execute();
        } else {
            Runnable myRunnable = new Runnable() {

                public void run() {
                    try {
                        Thread.sleep(10000);

                    } catch (Exception e) {

                    }
                    new ValidateForm().execute();
                }


            };
            Thread thread = new Thread(myRunnable);
            thread.start();

        }

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.getMenu().getItem(1).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                navigationView.getMenu().getItem(1).setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.MyAccount:
                        intform = new Intent(ProfileActivity.this, Landing.class);
                        finish();
                        break;
                    case R.id.work:
                        intform = new Intent(ProfileActivity.this, ViewForm.class);
                        intform.putExtra("which_page", 11);
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        break;
                    case R.id.About:
                        intform = new Intent(ProfileActivity.this, ViewForm.class);
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        intform.putExtra("which_page", 3);
                        break;
                    case R.id.app_form:
                        intform = new Intent(ProfileActivity.this, ProfileActivity.class);
                        break;
                    case R.id.faq:
                        intform = new Intent(ProfileActivity.this, ViewForm.class);
                        intform.putExtra("url", "http://hellobuddy.in/#/faqs");
                        intform.putExtra("which_page", 5);
                        break;
                    case R.id.security:
                        intform = new Intent(ProfileActivity.this, ViewForm.class);
                        intform.putExtra("which_page", 15);
                        intform.putExtra("url", "http://hellobuddy.in/#/faqs");
                        break;
                    case R.id.Orders:
                        intform = new Intent(ProfileActivity.this, ViewForm.class);
                        intform.putExtra("which_page", 16);
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        break;
                    case R.id.Repayments:
                        intform = new Intent(ProfileActivity.this, ViewForm.class);
                        intform.putExtra("which_page", 17);
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        break;
                    case R.id.Share:
                        SharedPreferences sh_otp = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editornew = sh_otp.edit();
                        editornew.putInt("chshare", 1);
                        editornew.commit();
                        intform = new Intent(ProfileActivity.this, Share.class);
                        break;
                    case R.id.Recharge:


                        intform = new Intent(ProfileActivity.this, ViewForm.class);


                        //clickpaste();

                        intform.putExtra("which_page", 999);
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.log:
                        Splash.notify = 0;
                        SharedPreferences preferences = getSharedPreferences("buddyotp", 0);
                        SharedPreferences.Editor editora = preferences.edit();
                        editora.clear();
                        editora.commit();
                        SharedPreferences preferencesb = getSharedPreferences("buddy", 0);
                        SharedPreferences.Editor editorab = preferencesb.edit();
                        editorab.clear();
                        editorab.commit();
                        SharedPreferences preferencesbc = getSharedPreferences("buddyin", 0);
                        SharedPreferences.Editor editorabc = preferencesbc.edit();
                        editorabc.clear();
                        editorabc.commit();
                        SharedPreferences preferencesbc1 = getSharedPreferences("proid", 0);
                        SharedPreferences.Editor editorabc1 = preferencesbc1.edit();
                        editorabc1.clear();
                        editorabc1.commit();
                        SharedPreferences preferencesbc2 = getSharedPreferences("cred", 0);
                        SharedPreferences.Editor editorabc2 = preferencesbc2.edit();
                        editorabc2.clear();
                        editorabc2.commit();
                        int a = 0;
                        SharedPreferences sharedpreferences = getSharedPreferences("buddy", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt("checklog", a);
                        editor.commit();
                        SharedPreferences sharedpreferences1 = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                        editor1.putString("number", "");
                        editor1.putString("code", "");
                        editor1.commit();
                        try {
                            Intercom.client().reset();
                        } catch (Exception e) {
                            System.out.println(e.toString() + "int empty");
                        }
                        intform = new Intent(ProfileActivity.this, MainActivity.class);
                        intform.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                        break;
                    default:
                        break;
                }
                startActivity(intform);
                overridePendingTransition(0, 0);
                return true;
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        NavigationView nview = (NavigationView) drawerLayout.findViewById(R.id.navigation_view);
        View headerView = nview.getHeaderView(0);
        ImageView arr = (ImageView) headerView.findViewById(R.id.arrow);
        RelativeLayout header = (RelativeLayout) headerView.findViewById(R.id.head);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        mPrefs = getSharedPreferences("buddy", Context.MODE_PRIVATE);

        profilePic = (MLRoundedImageView) findViewById(R.id.profile_pic);
        SharedPreferences sf = getSharedPreferences("proid", Context.MODE_PRIVATE);
        String id = sf.getString("dpid", "");
        if (!"".equals(id))
            Picasso.with(this).load("https://graph.facebook.com/" + sf.getString("dpid", "") + "/picture?type=large")
                    .placeholder(R.drawable.profile_placeholder).into(profilePic);
        userName = (TextView) findViewById(R.id.user_name);
        userName.setText(user.getName());

        creditLimit = (TextView) findViewById(R.id.credit_limit);
        creditAvailable = (TextView) findViewById(R.id.credit_balance);
        cashBack = (TextView) findViewById(R.id.cash_back);
        verifyIdentityLock = (ImageView) findViewById(R.id.verify_identity_lock);
        autoRepaymentLock = (ImageView) findViewById(R.id.auto_repayment_lock);
        moreCreditLock = (ImageView) findViewById(R.id.more_credit_lock);
        String creditLimitText = "Credit Limit \n₹" + user.getCreditLimit();
        creditLimit.setText(creditLimitText);
        String creditBalanceText = "Credit Balance \n₹" + user.getAvailableCredit();
        creditAvailable.setText(creditBalanceText);
        String cashBackText = "Cashback \n₹" + user.getCashBack();
        cashBack.setText(cashBackText);

        //        verifyIdentity = (ImageView) findViewById(R.id.verify_identity);
        //        Sharp.loadResource(getResources(), R.raw.verify)
        //                .into(verifyIdentity);

        verifyIdentityCardView = (CardView) findViewById(R.id.profile_verify_identity_card_view);
        verifyIdentityCardView.setOnClickListener(this);
        autoRepayCardView = (CardView) findViewById(R.id.profile_auto_repayments_card_view);
        autoRepayCardView.setOnClickListener(this);
        moreCreditCardView = (CardView) findViewById(R.id.profile_more_credit_card_view);
        moreCreditCardView.setOnClickListener(this);

        verifyIdentityProgressBar = (ImageView) findViewById(R.id.verify_progress_bar);
        autoRepaymentsProgressBar = (ImageView) findViewById(R.id.auto_repayment_progress_bar);
        moreCreditProgressBar = (ImageView) findViewById(R.id.more_credit_progress_bar);

        verifyIdentityText = (TextView) findViewById(R.id.verify_identity_text);
        autoRepaymentText = (TextView) findViewById(R.id.auto_repayment_text);
        moreCreditText = (TextView) findViewById(R.id.more_credit_text);

        if (user.isAppliedFor60k()) {
            applied60k();
            if (!user.isAppliedFor7k() || !user.isAppliedFor1k())
                moreCreditText.setText("Please complete previous steps!");
        } else {
            incomplete60k();
        }
        if (user.isAppliedFor7k()) {
            applied7k();
            if (!user.isAppliedFor1k())
                autoRepaymentText.setText("Please complete above step!");

        } else {
            incomplete7k();
        }
        if (user.isAppliedFor1k()) {
            applied1k();
        } else {
            incomplete1k();
        }

        if (Constants.STATUS.DECLINED.toString().equals(user.getProfileStatus())) {
            declinedUser();
            return;
        }
        //        String formStatus = user.getFormStatus();
        //        if ("appliedFor1K".equals(formStatus)) {
        //            applied1k();
        //        } else if ("declinedFor1K".equals(formStatus)) {
        //            verifyIdentityProgressBar.setImageResource(R.drawable.complete);
        //            verifyIdentityProgressBar.setVisibility(View.VISIBLE);
        //            verifyIdentityLock.setVisibility(View.GONE);
        //            verifyIdentityText.setText("Waitlisted. Proceed to next step!");
        //            mPrefs.edit().putBoolean("step1Editable", false).apply();
        //        } else if ("approvedFor1K".equals(formStatus)) {
        //            approved1K();
        //        } else if ("appliedFor7K".equals(formStatus)) {
        //            applied7k();
        //        } else if ("approvedFor7K".equals(formStatus)) {
        //            approved7k();
        //        } else if ("appliedFor60K".equals(formStatus)) {
        //            applied60k();
        //        } else if ("approvedFor60k".equals(formStatus)) {
        //            applied60k();
        //        } else if ("declined".equals(formStatus)) {

        //        }
        //        if (user.getApprovedBand() != null && !"".equals(user.getApprovedBand())) {
        //            String approvedBand = user.getApprovedBand();
        //            if ("Palladium".equals(approvedBand)) {
        //                approved60k();
        //                approved7k();
        //            } else if ("Oxygen".equals(approvedBand)) {
        //                approved7k();
        //            }
        //            approved1K();
        //        }

        String status1K = user.getStatus1K();
        if ("declined".equals(status1K)) {
            waitList1K();
        } else if ("waitlisted".equals(status1K)) {
            waitList1K();
        } else if ("approved".equals(status1K)) {
            approved1K();
        } else if ("applied".equals(status1K)) {
            user.setAppliedFor1k(true);
            applied1k();
        } else if (!user.isAppliedFor1k()) {
            mPrefs.edit().putBoolean("visitedFormStep1Fragment1", true).apply();
            incomplete1k();
        }

        String status7K = user.getStatus7K();
        if ("declined".equals(status7K)) {
            waitList7K();
        } else if ("waitlisted".equals(status7K)) {
            waitList7K();
        } else if ("approved".equals(status7K)) {
            approved7k();
        } else if ("applied".equals(status7K)) {
            user.setAppliedFor7k(true);
            applied7k();
            if (status1K == null || "appStart".equals(status1K))
                autoRepaymentText.setText("Please complete above step!");
        } else if (!user.isAppliedFor7k()) {
            mPrefs.edit().putBoolean("visitedFormStep1Fragment2", true).apply();
            incomplete7k();
        }

        String status60K = user.getStatus60K();
        if ("declined".equals(status60K)) {
            waitList60K();
        } else if ("waitlisted".equals(status60K)) {
            waitList60K();
        } else if ("approved".equals(status60K)) {
            approved60k();
        } else if ("applied".equals(status60K)) {
            user.setAppliedFor60k(true);
            applied60k();
            if (status1K == null || "appStart".equals(status1K) || status7K == null || "appStart".equals(status7K))
                moreCreditText.setText("Please complete previous steps!");
        } else if (!user.isAppliedFor60k()) {
            mPrefs.edit().putBoolean("visitedFormStep1Fragment3", true).apply();
            incomplete60k();
        }

        AppUtils.saveUserObject(this, user);
    }

    private void declinedUser() {
        declinedCardView = (CardView) findViewById(R.id.declined_card_view);
        declinedReason = (TextView) findViewById(R.id.decline_reason);
        iUnderstand = (Button) findViewById(R.id.i_understand);
        hiName = (TextView) findViewById(R.id.hi_name);
        layout = (LinearLayout) findViewById(R.id.rl1);
        middleLayout = (LinearLayout) findViewById(R.id.middle_layout);
        verifyIdentityCardView.setVisibility(View.VISIBLE);
        autoRepayCardView.setVisibility(View.VISIBLE);
        moreCreditCardView.setVisibility(View.VISIBLE);
        declinedCardView.setVisibility(View.VISIBLE);
        layout.setBackgroundColor(Color.parseColor("#c67876"));
        middleLayout.setBackgroundColor(Color.parseColor("#d99897"));
        toolbar.setBackgroundColor(Color.parseColor("#c67876"));
        //        iUnderstand.setVisibility(View.VISIBLE);
        String name = "Hi " + user.getName() + ",";
        hiName.setText(name);
        String text;
        if (AppUtils.isNotEmpty(user.getRejectionReason())) {
            text = "You have currently been waitlisted " + user.getRejectionReason() + "\nThank you for applying!";
        } else {
            text = "You have currently been waitlisted. Please contact us to know more.\nThank you for applying!";

        }
        declinedReason.setText(text);
        iUnderstand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private class ValidateForm extends
                               AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {

        }


        @Override
        protected String doInBackground(String... data) {
            SharedPreferences ss = getSharedPreferences("cred", Context.MODE_PRIVATE);
            JSONObject payload = new JSONObject();
            try {
                String url2 = getApplicationContext().getString(R.string.server) + "api/user/form?phone=" + ss.getString("phone_number", "");
                try {

                } catch (Exception e) {
                    System.out.println("dio " + e.toString());
                }
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");


                HttpResponse response = AppUtils.connectToServerGet(url2, tok_sp, null);
                if (response != null) {
                    HttpEntity ent = response.getEntity();
                    String responseString = EntityUtils.toString(ent, "UTF-8");

                    if (response.getStatusLine().getStatusCode() != 200) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {
                        JSONObject resp = new JSONObject(responseString);
                        JSONObject data1 = new JSONObject(resp.getString("data"));
                        try {


                            Gson gson = new Gson();
                            UserModel user = AppUtils.getUserObject(ProfileActivity.this);
                            if (user == null)
                                user = new UserModel();
                            name = data1.getString("name");
                            email = data1.getString("email");
                            user.setName(name);
                            user.setEmail(email);
                            if (!data1.getBoolean("offlineForm"))
                                AppUtils.checkDataForNormalUser(user, gson, data1);
                            else
                                checkDataForOfflineUser(user, gson, data1);

                            AppUtils.saveUserObject(ProfileActivity.this, user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            try {
                                String creditLimit = data1.getString("creditLimit");
                            } catch (Exception e) {
                            }
                            try {
                                fbid = data1.getString("fbConnected");
                            } catch (Exception e) {
                                fbid = "empty";
                            }
                            if (fbid.equals("") || (fbid.equals("false")))
                                fbid = "empty";
                            try {
                                formstatus = data1.getString("formStatus");
                            } catch (Exception e) {
                                formstatus = "empty";
                            }
                            int cashBack = 0;
                            try {
                                cashBack = data1.getInt("totalCashback");
                            } catch (Exception e) {
                                cashBack = 0;
                            }
                            String approvedBand = "";
                            try {
                                approvedBand = data1.getString("approvedBand");
                            } catch (Exception e) {
                                approvedBand = "";
                            }
                            int creditLimit = 0;
                            try {
                                creditLimit = data1.getInt("creditLimit");
                            } catch (Exception e) {
                                creditLimit = 0;
                            }
                            int totalBorrowed = 0;
                            try {
                                totalBorrowed = data1.getInt("totalBorrowed");
                            } catch (Exception e) {
                                totalBorrowed = 0;
                            }
                            String nameadd = "";
                            try {
                                nameadd = data1.getString("college");
                            } catch (Exception e) {
                            }
                            String profileStatus = "";
                            try {
                                profileStatus = data1.getString("profileStatus");
                            } catch (Exception e) {
                                profileStatus = "";
                            }
                            String courseend = "";

                            try {
                                courseend = data1.getString("courseCompletionDate");
                            } catch (Exception e) {
                                courseend = "";
                            }

                            SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorP = userP.edit();

                            editorP.putString("profileStatus", profileStatus);

                            editorP.putInt("creditLimit", creditLimit);
                            editorP.putInt("totalBorrowed", totalBorrowed);
                            editorP.putInt("cashBack", cashBack);
                            editorP.putString("formStatus", formstatus);
                            editorP.putString("course", courseend);
                            editorP.putString("nameadd", nameadd);
                            editorP.putString("approvedBand", approvedBand);
                            editorP.putString("productdpname", name);
                            editorP.commit();


                            try {
                                rejectionReason = data1.getString("rejectionReason");
                            } catch (Exception e) {
                            }
                            if (formstatus.equals(""))
                                formstatus = "empty";

                            try {
                                verificationdate = data1.getString("collegeIdVerificationDate");
                            } catch (Exception e) {
                                verificationdate = "NA";
                            }
                            if (verificationdate.equals(""))
                                verificationdate = "NA";
                            try {
                                String dpid = data1.getString("fbUserId");
                                SharedPreferences sf = getSharedPreferences("proid", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor2 = sf.edit();
                                editor2.putString("dpid", dpid);

                                //  editor2.putString("password", password.getText().toString());
                                editor2.commit();
                            } catch (Exception e) {
                            }

                        } catch (Exception e) {
                        }
                        if (resp.getString("msg").contains("error")) {
                            Log.e("MeshCommunication", "Server returned code "
                                    + response.getStatusLine().getStatusCode());
                            return resp.getString("msg");
                        } else {
                            return "win";
                        }

                    }
                }
                return "fail";

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }
        }
    }


    private void checkDataForOfflineUser(UserModel user, Gson gson, JSONObject data1) {
        try {
            SharedPreferences sh = getSharedPreferences("buddy", Context.MODE_PRIVATE);
            sh.edit().putBoolean("visitedFormStep1Fragment1", true).apply();
            sh.edit().putBoolean("visitedFormStep1Fragment2", true).apply();
            sh.edit().putBoolean("visitedFormStep1Fragment3", true).apply();
            AppUtils.checkDataForNormalUser(user, gson, data1);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void incomplete1k() {
        if (mPrefs.getBoolean("visitedFormStep1Fragment1", false)) {
            verifyIdentityProgressBar.setImageResource(R.drawable.incomplete);
            verifyIdentityProgressBar.setVisibility(View.VISIBLE);
            verifyIdentityText.setText("Details Incomplete");
            verifyIdentityLock.setVisibility(View.GONE);
            mPrefs.edit().putBoolean("step1Editable", true).apply();
        }
    }

    private void waitList1K() {
        verifyIdentityProgressBar.setImageResource(R.drawable.complete);
        verifyIdentityProgressBar.setVisibility(View.VISIBLE);
        verifyIdentityText.setText("Waitlisted. Proceed to next step!");
        verifyIdentityLock.setVisibility(View.GONE);
        mPrefs.edit().putBoolean("step1Editable", false).apply();
    }

    private void approved1K() {
        verifyIdentityProgressBar.setImageResource(R.drawable.complete);
        verifyIdentityProgressBar.setVisibility(View.VISIBLE);
        verifyIdentityText.setText("Approved for Flash");
        verifyIdentityLock.setImageResource(R.drawable.view);
        verifyIdentityLock.setVisibility(View.VISIBLE);
        mPrefs.edit().putBoolean("step1Editable", false).apply();
    }

    private void applied1k() {
        verifyIdentityProgressBar.setImageResource(R.drawable.loader);
        verifyIdentityProgressBar.setVisibility(View.VISIBLE);
        verifyIdentityText.setText("Under verification...");
        verifyIdentityLock.setVisibility(View.GONE);
        mPrefs.edit().putBoolean("step1Editable", false).apply();
    }

    private void incomplete7k() {
        if (mPrefs.getBoolean("visitedFormStep2Fragment1", false)) {
            autoRepaymentsProgressBar.setImageResource(R.drawable.incomplete);
            autoRepaymentsProgressBar.setVisibility(View.VISIBLE);
            autoRepaymentText.setText("Details Incomplete");
            autoRepaymentLock.setVisibility(View.GONE);
            mPrefs.edit().putBoolean("step2Editable", true).apply();
        }
    }

    private void waitList7K() {
        autoRepaymentsProgressBar.setImageResource(R.drawable.complete);
        autoRepaymentsProgressBar.setVisibility(View.VISIBLE);
        autoRepaymentText.setText("Waitlisted. Proceed to next step!");
        autoRepaymentLock.setVisibility(View.GONE);
        mPrefs.edit().putBoolean("step2Editable", false).apply();
    }

    private void approved7k() {
        autoRepaymentsProgressBar.setImageResource(R.drawable.complete);
        autoRepaymentsProgressBar.setVisibility(View.VISIBLE);
        autoRepaymentText.setText("Approved for Oxygen!");
        autoRepaymentLock.setImageResource(R.drawable.view);
        autoRepaymentLock.setVisibility(View.VISIBLE);
        mPrefs.edit().putBoolean("step2Editable", false).apply();
    }

    private void applied7k() {
        autoRepaymentsProgressBar.setImageResource(R.drawable.loader);
        autoRepaymentsProgressBar.setVisibility(View.VISIBLE);
        autoRepaymentText.setText("Under verification...");
        autoRepaymentLock.setVisibility(View.GONE);
        mPrefs.edit().putBoolean("step2Editable", false).apply();
    }

    private void incomplete60k() {
        if (mPrefs.getBoolean("visitedFormStep3Fragment1", false)) {
            moreCreditProgressBar.setImageResource(R.drawable.incomplete);
            moreCreditProgressBar.setVisibility(View.VISIBLE);
            moreCreditText.setText("Details Incomplete");
            moreCreditLock.setVisibility(View.GONE);
            mPrefs.edit().putBoolean("step3Editable", true).apply();
        }
    }

    private void waitList60K() {
        moreCreditProgressBar.setImageResource(R.drawable.complete);
        moreCreditProgressBar.setVisibility(View.VISIBLE);
        moreCreditText.setText("Waitlisted.");
        moreCreditLock.setVisibility(View.GONE);
        mPrefs.edit().putBoolean("step2Editable", false).apply();
    }

    private void approved60k() {
        moreCreditProgressBar.setImageResource(R.drawable.complete);
        moreCreditProgressBar.setVisibility(View.VISIBLE);
        moreCreditText.setText("Approved for " + user.getApprovedBand());
        moreCreditLock.setImageResource(R.drawable.view);
        moreCreditLock.setVisibility(View.VISIBLE);
        mPrefs.edit().putBoolean("step3Editable", false).apply();
    }

    private void applied60k() {
        moreCreditProgressBar.setImageResource(R.drawable.loader);
        moreCreditProgressBar.setVisibility(View.VISIBLE);
        moreCreditText.setText("Under verification...");
        moreCreditLock.setVisibility(View.GONE);
        mPrefs.edit().putBoolean("step3Editable", false).apply();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.profile_verify_identity_card_view:
                intent = new Intent(ProfileActivity.this, ProfileFormStep1.class);
                break;
            case R.id.profile_auto_repayments_card_view:
                intent = new Intent(ProfileActivity.this, ProfileFormStep2.class);
                break;
            case R.id.profile_more_credit_card_view:
                intent = new Intent(ProfileActivity.this, ProfileFormStep3.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
    //    @Override
    //    protected void attachBaseContext(Context newBase) {
    //        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    //    }

    public void goToAccSettings(View view) {
        Intent intent = new Intent(ProfileActivity.this, AccountSettingsActivity.class);
        startActivity(intent);
    }
}
