package indwin.c3.shareapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import indwin.c3.shareapp.application.BuddyApplication;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.Constants;
import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;

public class Landing extends AppCompatActivity {
    int count = 0;
    ViewPager dealspager;
    SharedPreferences sh, sh_otp, ss;
    SharedPreferences userP;
    private ProgressBar spinner;
    int data;
    private String phoneNumberCall = "";
    Intent inTent;
    private String action = "", cashBack = "", name = "", email = "", fbid = "", formstatus = "", uniqueCode = "", creditLimit = "", panoradhar = "", bankaccount = "", collegeid = "", verificationdate = "", rejectionReason = "", referral_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sh = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        ss = getSharedPreferences("cred", Context.MODE_PRIVATE);
        userP = getSharedPreferences("token", Context.MODE_PRIVATE);
        try {
            inTent = getIntent();
            action = inTent.getAction();
            data = inTent.getExtras().getInt("cc");
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        if (data == 1) {
            //phoneNumberCall=sh_otp.getString("number", "");
            new AuthTokc().execute();
        } else {
            sh_otp = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
            if ((sh.getInt("checklog", 0) != 1) && (sh_otp.getString("number", "").equals(""))) {
                setContentView(R.layout.activity_landing);
                TextView login = (TextView) findViewById(R.id.login_pager);
                TextView signup = (TextView) findViewById(R.id.signup_pager);
                final ImageView dot1 = (ImageView) findViewById(R.id.c1);
                final ImageView dot2 = (ImageView) findViewById(R.id.c2);
                final ImageView dot3 = (ImageView) findViewById(R.id.c3);
                final ImageView logo = (ImageView) findViewById(R.id.buddyLogo);
                dot1.setBackgroundResource(R.drawable.circle2);
                try {
                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent in = new Intent(Landing.this, MainActivity.class);
                            finish();
                            startActivity(in);
                            overridePendingTransition(0, 0);


                        }
                    });
                    signup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent in = new Intent(Landing.this, Inviteform.class);
                            finish();
                            startActivity(in);
                            overridePendingTransition(0, 0);

                        }
                    });
                } catch (Exception e) {
                }

                ViewPagerAdapter adapter = new ViewPagerAdapter(Landing.this, 3, getApplicationContext());
                dealspager = (ViewPager) findViewById(R.id.landing);
                adapter.notifyDataSetChanged();
                dealspager.setAdapter(adapter);
                dealspager.setCurrentItem(0);

                dealspager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {


                        if (position == 0) {


                            dot1.setBackgroundResource(R.drawable.circle2);
                            dot2.setBackgroundResource(R.drawable.circle);
                            dot3.setBackgroundResource(R.drawable.circle);
                        } else if (position == 1) {
                            dot2.setBackgroundResource(R.drawable.circle2);
                            dot1.setBackgroundResource(R.drawable.circle);
                            dot3.setBackgroundResource(R.drawable.circle);
                        } else if (position == 2) {
                            dot2.setBackgroundResource(R.drawable.circle);
                            dot1.setBackgroundResource(R.drawable.circle);
                            dot3.setBackgroundResource(R.drawable.circle2);
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                // Timer for auto sliding
            }
            //       else if(sh.getInt("checklog",0)==1)
            //        {setContentView(R.layout.activity_formsaved);
            //            Intent in=new Intent(Landing.this,MainActivity.class);
            //            finish();
            //            startActivity(in);
            //            overridePendingTransition(0, 0);
            //
            //        }
            else if (!ss.getString("phone_number", "").equals("")) {
                setContentView(R.layout.activity_formsaved);
                spinner = (ProgressBar) findViewById(R.id.progressBar1);
                Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
                try {
                    Intercom.client().registerIdentifiedUser(
                            new Registration().withUserId(ss.getString("phone_number", "")));
                    Intercom.client().openGCMMessage(getIntent());
                } catch (Exception e) {
                }

                //
                Long time = Calendar.getInstance().getTimeInMillis() / 1000;
                Long oldtime = userP.getLong("expires", 0);
                //        Toast.makeText(FacebookAuth.this, String.valueOf(oldtime), Toast.LENGTH_SHORT).show();
                if (!isNetworkAvailable()) {
                    Gson gson = new Gson();
                    String json = sh.getString("UserObject", "");
                    UserModel user = gson.fromJson(json, UserModel.class);
                    Intent in = new Intent(Landing.this, HomePage.class);
                    finish();
                    // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    if (user.getName() != null && !"".equals(user.getName()))
                        in.putExtra("Name", user.getName());
                    if (user.getEmail() != null && !"".equals(user.getEmail()))
                        in.putExtra("Email", user.getEmail());
                    if (user.getFormStatus() != null && !"".equals(user.getFormStatus()))
                        in.putExtra("Form", user.getFormStatus());
                    else
                        in.putExtra("Form", "empty");
                    if (user.getFbUserId() != null && !"".equals(user.getFbUserId()))
                        in.putExtra("fbid", user.getFbUserId());
                    startActivity(in);
                    overridePendingTransition(0, 0);
                    return;
                }
                if (time + 5 < userP.getLong("expires", 0))
                    new ValidateForm().execute("");
                else
                    new AuthTokc().execute();
            }
            // else
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class AuthTokc extends
                          AsyncTask<String, Void, String> {

        private String apiN = "";

        //        Context context;
        //        AuthTok(Context context) {
        //            this.context = context;
        //        }

        //    Splash obj=new Splash();
        @Override
        protected String doInBackground(String... params) {
            JSONObject payload = new JSONObject();

            try {

                // userid=12&productid=23&action=add
                // TYPE: POST
                //      payload.put("userid", details.get("userid"));
                // payload.put("productid", details.get("productid"));
                // payload.put("action", details.get("action"));


                String urll = getApplicationContext().getString(R.string.server) + "authenticate";


                HttpResponse response = AppUtils.connectToServerPost(urll, null, null);
                if (response != null) {


                    HttpEntity ent = response.getEntity();
                    String responseString = EntityUtils.toString(ent, "UTF-8");
                    if (response.getStatusLine().getStatusCode() != 200) {


                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {

                        JSONObject resp = new JSONObject(responseString);

                        if (resp.getString("status").contains("fail")) {

                            Log.e("MeshCommunication", "Server returned code "
                                    + response.getStatusLine().getStatusCode());
                            return "fail";
                        } else {
                            String token1 = "";

                            userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorP = userP.edit();
                            token1 = resp.getString("token");
                            editorP.putString("token_value", token1);
                            editorP.putLong("expires", resp.getLong("expiresAt"));
                            editorP.commit();
                            return "win";

                        }

                    }
                } else {
                    return "fail";

                }

            } catch (Exception e) {
                Log.e("mesherror111", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {
            if (result.equals("win")) {

                //   Toast.makeText(Landing.this, "yey", Toast.LENGTH_SHORT).show();
                //            next.new FacebookAuth.fblogin().execute();
                // new fblogin().execute();
                //            next.fblogin().execute();
                //                new forgotpass().execute();
                new ValidateForm().execute("");

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.sendGoogleAnalytics((BuddyApplication) getApplication());
    }

    private class ValidateForm extends
                               AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {

        }


        @Override
        protected String doInBackground(String... data) {

            // String urldisplay = data[0];
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST

                // payload.put("action", details.get("action"));


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

                            referral_code = data1.getString("uniqueCode");
                            SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("rcode", referral_code);
                            editor.commit();

                            Gson gson = new Gson();
                            String json = sh.getString("UserObject", "");
                            UserModel user = gson.fromJson(json, UserModel.class);
                            if (user == null)
                                user = new UserModel();
                            name = data1.getString("name");
                            email = data1.getString("email");
                            user.setName(name);
                            user.setEmail(email);
                            if (!data1.getBoolean("offlineForm"))
                                checkDataForNormalUser(user, gson, data1);
                            else
                                checkDataForOfflineUser(user, gson, data1);
                            json = gson.toJson(user);
                            sh.edit().putString("UserObject", json).apply();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            try {
                                creditLimit = data1.getString("creditLimit");
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
                            SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorP = userP.edit();

                            editorP.putString("profileStatus", profileStatus);

                            editorP.putInt("creditLimit", creditLimit);
                            editorP.putInt("totalBorrowed", totalBorrowed);
                            editorP.putInt("cashBack", cashBack);
                            editorP.putString("formStatus", formstatus);
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
                                panoradhar = data1.getString("addressProofs");
                            } catch (Exception e) {
                                panoradhar = "NA";
                            }
                            if (panoradhar.equals(""))
                                panoradhar = "NA";
                            try {
                                collegeid = data1.getString("collegeIDs");
                            } catch (Exception e) {
                                collegeid = "NA";
                            }
                            if (collegeid.equals(""))
                                collegeid = "NA";
                            try {
                                bankaccount = data1.getString("bankStatement");
                            } catch (Exception e) {
                                bankaccount = "NA";
                            }
                            if (bankaccount.equals(""))
                                bankaccount = "NA";
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

        private void checkDataForNormalUser(UserModel user, Gson gson, JSONObject data1) {
            try {
                user.setEmailSent(false);
                if (data1.opt("gender") != null)
                    user.setGender(data1.getString("gender"));
                if (data1.opt("approvedBand") != null)
                    user.setApprovedBand(data1.getString("approvedBand"));
                if (data1.opt("creditLimit") != null)
                    user.setCreditLimit(data1.getInt("creditLimit"));
                if (data1.opt("totalBorrowed") != null) {
                    if (user.getCreditLimit() == 0) {
                        user.setAvailableCredit(0);
                    } else {
                        int available = user.getCreditLimit() - data1.getInt("totalBorrowed");
                        if (available > 0) {
                            user.setAvailableCredit(available);
                        } else {
                            user.setAvailableCredit(0);
                        }
                    }
                } else {
                    user.setAvailableCredit(user.getCreditLimit());
                }
                if (data1.opt("totalCashback") != null)
                    user.setCashBack(data1.getInt("totalCashback"));
                if (data1.opt("emailVerified") != null)
                    user.setEmailVerified(data1.getBoolean("emailVerified"));
                if (data1.opt("formStatus") != null)
                    user.setFormStatus(data1.getString("formStatus"));
                Map userMap = new HashMap<>();
                if (data1.opt("profileStatus") != null) {
                    user.setProfileStatus(data1.getString("profileStatus"));
                    userMap.put("profileStatus", user.getProfileStatus());
                }
                if (data1.opt("status1K") != null) {
                    String status1K = data1.getString("status1K");
                    user.setStatus1K(status1K);
                    userMap.put("status1K", status1K);
                    if (Constants.STATUS.DECLINED.toString().equals(status1K) || Constants.STATUS.APPLIED.toString().equals(status1K) || Constants.STATUS.APPROVED.toString().equals(status1K))
                        user.setAppliedFor1k(true);
                    else user.setAppliedFor1k(false);
                }
                if (data1.opt("status7K") != null) {
                    String status7K = data1.getString("status7K");
                    user.setStatus7K(status7K);
                    userMap.put("status7K", status7K);
                    if (Constants.STATUS.DECLINED.toString().equals(status7K) || Constants.STATUS.APPLIED.toString().equals(status7K) || Constants.STATUS.APPROVED.toString().equals(status7K))
                        user.setAppliedFor7k(true);
                    else user.setAppliedFor7k(false);
                }
                if (data1.opt("status60K") != null) {
                    String status60K = data1.getString("status60K");
                    user.setStatus60K(status60K);
                    userMap.put("status60K", status60K);
                    if (Constants.STATUS.DECLINED.toString().equals(status60K) || Constants.STATUS.APPLIED.toString().equals(status60K) || Constants.STATUS.APPROVED.toString().equals(status60K))
                        user.setAppliedFor60k(true);
                    else user.setAppliedFor60k(false);
                }
                Intercom.client().updateUser(userMap);


                if (data1.opt("gpa") != null) {

                    user.setGpa(data1.getString("gpa"));
                }
                if (data1.opt("gpaType") != null) {

                    user.setGpaType(data1.getString("gpaType"));
                }
                if (data1.opt("fbConnected") != null)
                    user.setIsFbConnected(Boolean.parseBoolean(data1.getString("fbConnected")));
                if (data1.opt("college") != null)
                    user.setCollegeName(data1.getString("college"));
                if (data1.opt("course") != null)
                    user.setCourseName(data1.getString("course"));
                if (data1.opt("courseCompletionDate") != null)
                    user.setCourseEndDate(data1.getString("courseCompletionDate"));
                if (data1.opt("collegeIDs") != null)
                    user.setCollegeIds(gson.fromJson(data1.getString("collegeIDs"), ArrayList.class));


                if (data1.opt("addressProofs") != null)
                    user.setAddressProofs(gson.fromJson(data1.getString("addressProofs"), ArrayList.class));
                if (data1.opt("panOrAadhar") != null) {
                    user.setPanOrAadhar(data1.getString("panOrAadhar"));
                    if ("PAN".equals(user.getPanOrAadhar()))
                        user.setPanNumber(data1.getString("pan"));
                    else
                        user.setAadharNumber(data1.getString("aadhar"));
                }
                if (data1.opt("dob") != null)
                    user.setDob(data1.getString("dob"));
                if (data1.opt("accomodation") != null)
                    user.setAccommodation(data1.getString("accomodation"));
                if (data1.opt("currentAddress") != null)
                    user.setCurrentAddress(data1.getJSONObject("currentAddress").getString("line1"));
                if (data1.opt("permanentAddress") != null)
                    user.setPermanentAddress(data1.getJSONObject("permanentAddress").getString("line1"));
                if (data1.opt("rollNumber") != null)
                    user.setRollNumber(data1.getString("rollNumber"));

                if (data1.opt("rejectionReason") != null)
                    user.setRejectionReason(data1.getString("rejectionReason"));
                if (data1.optJSONArray("familyMember") != null) {
                    JSONArray familyMembers = data1.getJSONArray("familyMember");
                    for (int i = 0; i < familyMembers.length(); i++) {
                        JSONObject familyJson = familyMembers.getJSONObject(i);
                        if (i == 1) {
                            if (familyJson.getString("relation") != null) {
                                user.setFamilyMemberType2(familyJson.getString("relation"));
                                user.setProfessionFamilyMemberType2(familyJson.getString("occupation"));
                                user.setPhoneFamilyMemberType2(familyJson.getString("phone"));
                                user.setPrefferedLanguageFamilyMemberType2(familyJson.getString("preferredLanguage"));
                            }
                        } else {
                            if (familyJson.getString("relation") != null) {
                                user.setFamilyMemberType1(familyJson.getString("relation"));
                                user.setProfessionFamilyMemberType1(familyJson.getString("occupation"));
                                user.setPhoneFamilyMemberType1(familyJson.getString("phone"));
                                user.setPrefferedLanguageFamilyMemberType1(familyJson.getString("preferredLanguage"));
                            }
                        }
                    }
                }

                if (data1.opt("bankAccountNumber") != null)
                    user.setBankAccNum(data1.getString("bankAccountNumber"));
                if (data1.opt("bankIFSC") != null)
                    user.setBankIfsc(data1.getString("bankIFSC"));
                if (data1.opt("friendName") != null)
                    user.setClassmateName(data1.getString("friendName"));
                if (data1.opt("friendNumber") != null)
                    user.setClassmatePhone(data1.getString("friendNumber"));
                if (data1.opt("collegeIdVerificationDate") != null)
                    user.setVerificationDate(data1.getString("collegeIdVerificationDate"));
                if (data1.opt("annualFees") != null)
                    user.setAnnualFees(data1.getString("annualFees"));
                if (data1.opt("scholarship") != null)
                    user.setScholarship(String.valueOf(data1.getString("scholarship")));
                if (data1.opt("scholarshipProgram") != null)
                    user.setScholarshipType(data1.getString("scholarshipProgram"));
                if (data1.opt("scholarshipAmount") != null)
                    user.setScholarshipAmount(data1.getString("scholarshipAmount"));
                if (data1.opt("takenLoan") != null)
                    user.setStudentLoan(data1.getString("takenLoan"));
                if (data1.opt("monthlyExpense") != null)
                    user.setMonthlyExpenditure(data1.getString("monthlyExpense"));
                if (data1.opt("ownVehicle") != null)
                    user.setVehicle(String.valueOf(data1.getString("ownVehicle")));
                if (data1.opt("vehicleType") != null)
                    user.setVehicleType(data1.getString("vehicleType"));
                if (data1.opt("bankStatements") != null)
                    user.setBankStmts(gson.fromJson(data1.getString("bankStatements"), ArrayList.class));
                if (data1.opt("bankProofs") != null)
                    user.setBankProofs(gson.fromJson(data1.getString("bankProofs"), ArrayList.class));
                if (data1.opt("selfie") != null) {
                    JSONArray array = data1.getJSONArray("selfie");
                    user.setSelfie((String) array.get(0));
                } else {
                    user.setSelfie("");
                }
                if (data1.opt("signature") != null) {
                    JSONArray array = data1.getJSONArray("signature");
                    user.setSignature((String) array.get(0));
                } else {
                    user.setSignature("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void checkDataForOfflineUser(UserModel user, Gson gson, JSONObject data1) {
            try {
                sh.edit().putBoolean("visitedFormStep1Fragment1", true).apply();
                sh.edit().putBoolean("visitedFormStep1Fragment2", true).apply();
                sh.edit().putBoolean("visitedFormStep1Fragment3", true).apply();
                checkDataForNormalUser(user, gson, data1);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        protected void onPostExecute(String result) {
            // spinner.setVisibility(View.INVISIBLE);
            //                    inotp.putExtra("Name", mName);
            //                    inotp.putExtra("Email",email.getText().toString());
            //                    inotp.putExtra("College",college.getText().toString());
            //                    inotp.putExtra("Phone",phone.getText().toString());

            if (result.equals("win")) {


                if (formstatus.equals("saved")) {
                    Intent in;

                    if (data == 1)
                        in = new Intent(Landing.this, Formempty.class);
                    else
                        in = new Intent(Landing.this, HomePage.class);

                    // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    finish();
                    in.putExtra("Name", name);
                    in.putExtra("fbid", fbid);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC", referral_code);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                } else if (formstatus.equals("submitted")) {
                    //                 Intent in = new Intent(MainActivity.this, Landing.class);


                    Intent in;
                    bankaccount = "somehting";
                    if (data == 1)
                        in = new Intent(Landing.this, Formstatus.class);
                    else
                        in = new Intent(Landing.this, HomePage.class);
                    if ((panoradhar.equals("NA")) || (bankaccount.equals("NA")) || (collegeid.equals("NA"))) {
                        in.putExtra("screen_no", 1);
                    } else if ((!panoradhar.equals("NA")) && (!collegeid.equals("NA")) && (!bankaccount.equals("NA")) && (verificationdate.equals("NA"))) {
                        in.putExtra("screen_no", 2);
                    } else if ((!panoradhar.equals("NA")) && (!collegeid.equals("NA")) && (!bankaccount.equals("NA")) && (!verificationdate.equals("NA"))) {
                        in.putExtra("screen_no", 3);
                        in.putExtra("VeriDate", verificationdate);

                    }

                    finish();
                    in.putExtra("Name", name);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("fbid", fbid);
                    in.putExtra("UniC", referral_code);
                    startActivity(in);

                    overridePendingTransition(0, 0);
                }

                if (formstatus.equals("declined")) {
                    Intent in;
                    if (data == 1)
                        in = new Intent(Landing.this, Formempty.class);
                    else
                        in = new Intent(Landing.this, HomePage.class);
                    finish();
                    // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    in.putExtra("Name", name);
                    in.putExtra("Rej", rejectionReason);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("fbid", fbid);
                    in.putExtra("UniC", uniqueCode);

                    startActivity(in);
                    overridePendingTransition(0, 0);

                }

                if (formstatus.equals("approved") || (formstatus.equals("flashApproved"))) {
                    Intent in;
                    if (data == 1)
                        in = new Intent(Landing.this, Approved.class);
                    else in = new Intent(Landing.this, HomePage.class);

                    finish();
                    // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    in.putExtra("Name", name);
                    in.putExtra("Email", email);//  in.putExtra("Credits",creditLimit);
                    in.putExtra("Form", formstatus);

                    if (formstatus.equals("approved"))
                        in.putExtra("checkflash", 0);
                    else
                        in.putExtra("checkflash", 1);

                    in.putExtra("fbid", fbid);
                    in.putExtra("Credits", creditLimit);
                    in.putExtra("UniC", referral_code);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                } else if (formstatus.equals("empty")) {

                    //                    Intent in = new Intent(MainActivity.this, Inviteform    .class);
                    Intent in;
                    if (data == 1)
                        in = new Intent(Landing.this, Formempty.class);
                    else
                        in = new Intent(Landing.this, HomePage.class);
                    finish();
                    in.putExtra("Name", name);
                    in.putExtra("Email", email);

                    in.putExtra("fbid", fbid);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC", referral_code);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                }

            } else {
                Intent in = new Intent(Landing.this, MainActivity.class);
                finish();
                startActivity(in);
                overridePendingTransition(0, 0);
                //                Toast.makeText(getApplicationContext(),"Please try again!"
                //                        ,
                //                        Toast.LENGTH_LONG).show();
            }


        }
    }
}
