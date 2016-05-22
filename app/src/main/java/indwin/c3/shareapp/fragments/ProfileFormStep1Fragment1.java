package indwin.c3.shareapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Set;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.adapters.SpinnerHintAdapter;
import indwin.c3.shareapp.models.FBUserModel;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.CheckInternetAndUploadUserDetails;
import indwin.c3.shareapp.utils.FetchLatestUserDetails;
import indwin.c3.shareapp.utils.FetchNewToken;
import indwin.c3.shareapp.utils.HelpTipDialog;
import indwin.c3.shareapp.utils.VerifyEmail;
import io.intercom.com.google.gson.Gson;

/**
 * Created by shubhang on 18/03/16.
 */
public class ProfileFormStep1Fragment1 extends Fragment {
    TextView userEmail, gotoFragment1, gotoFragment2, gotoFragment3;
    ImageButton editEmail;
    EditText userEmailEditText;
    Button saveEmail, connectSocialAccountFb, connectSocialAccountInsta, saveAndProceed;
    CallbackManager callbackManager;
    private String email, firstName, friends, gender, lastName, link, name, fbuserId;
    private Boolean veri;
    static int firstloginsign = 0;
    int w = 0;
    LoginManager loginManager;
    int retryCount = 0;
    SharedPreferences mPrefs;
    UserModel user;
    public static ImageView completeEmail, incompleteEmail;
    ImageView completeFb, incompleteFb, incompleteStep1, incompleteStep2, incompleteStep3;
    Gson gson;
    ImageView completeGender, incompleteGender;
    private final int top = 16, left = 16, right = 16, bottom = 16;
    boolean isGenderSelected = false;
    ImageView topImage;
    public static Button verifyEmail;
    boolean runningAsync = false;
    private ImageButton socialHelptip;
    private TextView incorrectEmail;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View rootView = inflater.inflate(
                R.layout.profile_form_step1_fragment1, container, false);
        FacebookSdk.sdkInitialize(getActivity());
        getAllViews(rootView);
        setAllClickListener();
        mPrefs = getActivity().getSharedPreferences("buddy", Context.MODE_PRIVATE);
        mPrefs.edit().putBoolean("visitedFormStep1Fragment1", true).apply();
        if (!mPrefs.getBoolean("step1Editable", true)) {
            setViewAndChildrenEnabled(rootView, false, gotoFragment2, gotoFragment3);
        }
        setAllHelpTipsEnabled();


        if (mPrefs.getBoolean("visitedFormStep1Fragment2", false)) {
            gotoFragment2.setAlpha(1);

            gotoFragment2.setClickable(true);
        }
        if (mPrefs.getBoolean("visitedFormStep1Fragment3", false)) {
            gotoFragment3.setAlpha(1);
            gotoFragment3.setClickable(true);
        }


        gson = new Gson();
        user = AppUtils.getUserObject(getActivity());
        if (user.isAppliedFor1k()) {

            saveAndProceed.setVisibility(View.INVISIBLE);
            rootView.findViewById(R.id.details_submitted_tv).setVisibility(View.VISIBLE);
        }


        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                final GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    FBUserModel fbUserModel = new Gson().fromJson(object.toString(), FBUserModel.class);
                                    email = fbUserModel.getEmail();
                                    firstName = fbUserModel.getFirst_name();
                                    gender = fbUserModel.getGender();
                                    lastName = fbUserModel.getLast_name();
                                    link = fbUserModel.getLink();
                                    fbuserId = fbUserModel.getId();
                                    user.setFbUserId(fbuserId);
                                    SharedPreferences sf = getActivity().getSharedPreferences("proid", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor2 = sf.edit();
                                    editor2.putString("dpid", fbuserId);
                                    editor2.commit();
                                    name = fbUserModel.getName();

                                    veri = fbUserModel.isVerified();


                                } catch (Exception e) {
                                    System.out.print("ds");
                                    e.printStackTrace(); //something's seriously wrong here
                                }
                                Log.e("LoginActivity please", response.toString());
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email,first_name,last_name,location,locale,timezone,verified");
                request.setParameters(parameters);
                request.executeAsync();
                new GraphRequest(
                        loginResult.getAccessToken(),
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                try {
                                    JSONObject obj = response.getJSONObject();
                                    JSONObject summ = new JSONObject(obj.getString("summary"));
                                    friends = summ.getString("total_count");
                                } catch (Exception ex) {
                                }


                                if (AppUtils.isEmpty(name) || AppUtils.isEmpty(email) || AppUtils.isEmpty(friends)) {
                                    Set<String> denied = loginResult.getRecentlyDeniedPermissions();
                                    if (denied.size() > 0) {
                                        Toast.makeText(getActivity(), "Please grant all permissions to complete your profile!", Toast.LENGTH_SHORT).show();
                                        LoginManager.getInstance().logInWithReadPermissions(ProfileFormStep1Fragment1.this, Arrays.asList("user_friends", "email", "user_birthday"));
                                    }
                                    LoginManager.getInstance().logOut();
                                    Toast.makeText(getActivity(), "Please try again!", Toast.LENGTH_SHORT).show();

                                    //   finish();
                                } else {
                                    new fblogin().execute();
                                }

                            }
                        }
                ).executeAsync();

            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        });
        isFbLoggedIn();

        if (AppUtils.isNotEmpty(user.getEmail())) {
            userEmail.setText(user.getEmail());
        }
        if (user.isEmailVerified()) {
            verifyEmail.setText("Verified!");
            verifyEmail.setTextColor(Color.GRAY);
            verifyEmail.setClickable(false);
            verifyEmail.setEnabled(false);
            completeEmail.setVisibility(View.VISIBLE);
            user.setIncompleteEmail(false);
        } else if (user.isEmailSent()) {
            verifyEmail.setText("Check");
            verifyEmail.setTextColor(Color.parseColor("#44c2a6"));
            verifyEmail.setClickable(true);
            verifyEmail.setEnabled(true);
            verifyEmail.setVisibility(View.VISIBLE);
            completeEmail.setVisibility(View.GONE);
        }


        final String genderOptions[] = getResources().getStringArray(R.array.gender);
        final SpinnerHintAdapter adapter2 = new SpinnerHintAdapter(getActivity(), genderOptions, R.layout.spinner_item_underline);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner genderSpinner = (Spinner) rootView.findViewById(R.id.gender_spinner);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < genderOptions.length - 1) {
                    user.setUpdateGender(true);
                    isGenderSelected = true;
                    user.setGender(genderOptions[position]);
                }
                if (position == 1) {
                    Picasso.with(getActivity())
                            .load(R.mipmap.step1fragment1girl)
                            .into(topImage);
                } else {
                    Picasso.with(getActivity())
                            .load(R.mipmap.step1fragment1)
                            .into(topImage);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setUpdateGender(false);
            }
        });
        genderSpinner.setAdapter(adapter2);
        genderSpinner.setSelection(adapter2.getCount());

        if (AppUtils.isNotEmpty(user.getGender())) {
            for (int i = 0; i < genderOptions.length - 1; i++) {
                if (genderOptions[i].equals(user.getGender())) {
                    if (i == 1) {
                        Picasso.with(getActivity())
                                .load(R.mipmap.step1fragment1girl)
                                .into(topImage);
                    }
                    genderSpinner.setSelection(i);
                    completeGender.setVisibility(View.VISIBLE);
                    user.setIncompleteGender(false);
                    break;
                }
            }
        }

        gotoFragment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment2(true);
            }
        });

        gotoFragment3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment3(true);
            }
        });
        saveAndProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("Verified!".equals(verifyEmail.getText().toString())) {
                    user.setEmailVerified(true);
                }
                checkIncomplete();
                String json = gson.toJson(user);
                mPrefs.edit().putString("UserObject", json).apply();
                Intent intent = new Intent(getActivity(), CheckInternetAndUploadUserDetails.class);
                getContext().sendBroadcast(intent);
                replaceFragment2(false);
            }
        });

        if (user.isIncompleteEmail() || user.isIncompleteFb() || user.isIncompleteGender()) {
            incompleteStep1.setVisibility(View.VISIBLE);
            if (user.isIncompleteFb()) {
                incompleteFb.setVisibility(View.VISIBLE);
            }
            if (user.isIncompleteEmail()) {
                incompleteEmail.setVisibility(View.VISIBLE);
            }
            if (user.isIncompleteGender()) {
                incompleteGender.setVisibility(View.VISIBLE);
            }
        }
        if (user.getCollegeIds() != null && user.getCollegeIds().size() > 0) {
            user.setIncompleteCollegeId(false);
        }
        if (user.isIncompleteCollegeId() || user.isIncompleteCollegeDetails() || user.isIncompleteRollNumber()) {
            incompleteStep2.setVisibility(View.VISIBLE);
        }

        if (user.isIncompleteAadhar() || !(user.getAddressProofs() != null && user.getAddressProofs().size() > 0) || (AppUtils.isEmpty(user.getSelfie()) || AppUtils.isEmpty(user.getSignature()))) {
            incompleteStep3.setVisibility(View.VISIBLE);
        }

        return rootView;
    }


    private void getAllViews(View rootView) {

        userEmail = (TextView) rootView.findViewById(R.id.user_email);
        userEmailEditText = (EditText) rootView.findViewById(R.id.user_email_edittext);
        editEmail = (ImageButton) rootView.findViewById(R.id.edit_user_email);
        verifyEmail = (Button) rootView.findViewById(R.id.verify_user_email);
        saveEmail = (Button) rootView.findViewById(R.id.save_user_email);
        connectSocialAccountFb = (Button) rootView.findViewById(R.id.connect_social_account_fb);
        connectSocialAccountInsta = (Button) rootView.findViewById(R.id.connect_social_account_insta);
        //        dontHaveFb = (Button) rootView.findViewById(R.id.dont_have_fb);
        gotoFragment1 = (TextView) rootView.findViewById(R.id.goto_fragment1);
        gotoFragment2 = (TextView) rootView.findViewById(R.id.goto_fragment2);
        gotoFragment3 = (TextView) rootView.findViewById(R.id.goto_fragment3);
        saveAndProceed = (Button) rootView.findViewById(R.id.save_and_proceed);
        completeEmail = (ImageView) rootView.findViewById(R.id.complete_email);
        completeFb = (ImageView) rootView.findViewById(R.id.complete_fb);
        incompleteEmail = (ImageView) rootView.findViewById(R.id.incomplete_email);
        incompleteFb = (ImageView) rootView.findViewById(R.id.incomplete_fb);
        incompleteStep1 = (ImageView) rootView.findViewById(R.id.incomplete_step_1);
        incompleteStep2 = (ImageView) rootView.findViewById(R.id.incomplete_step_2);
        incompleteStep3 = (ImageView) rootView.findViewById(R.id.incomplete_step_3);
        completeGender = (ImageView) rootView.findViewById(R.id.complete_gender);
        incompleteGender = (ImageView) rootView.findViewById(R.id.incomplete_gender);
        topImage = (ImageView) rootView.findViewById(R.id.verify_image_view2);
        socialHelptip = (ImageButton) rootView.findViewById(R.id.social_helptip);
        incorrectEmail = (TextView) rootView.findViewById(R.id.incorrect_email);


    }


    private void setAllClickListener() {
        socialHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "We require access to your facebook account to help build your social score. This step is very important to getting approved for a credit limit.";
                String text2 = "";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your College ID", text1, text2, "#44c2a6");
                dialog.show();
            }
        });
        connectSocialAccountFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginManager.logOut();
                loginManager.logInWithReadPermissions(ProfileFormStep1Fragment1.this, Arrays.asList("public_profile", "user_friends", "email", "user_birthday"));

            }
        });
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail.setVisibility(View.GONE);
                userEmailEditText.setVisibility(View.VISIBLE);
                userEmailEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(userEmailEditText, InputMethodManager.SHOW_IMPLICIT);
                editEmail.setVisibility(View.GONE);
                verifyEmail.setTextColor(Color.parseColor("#44c2a6"));
                verifyEmail.setText("Verify");
                verifyEmail.setClickable(true);
                verifyEmail.setEnabled(true);
                verifyEmail.setVisibility(View.GONE);
                saveEmail.setVisibility(View.VISIBLE);
                completeEmail.setVisibility(View.GONE);
            }
        });
        saveEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (userEmailEditText.getText().length() > 0 && !"".equals(userEmailEditText.getText().toString().trim()))
                //    saveEmail();
                //else
                if (AppUtils.isEmpty(userEmailEditText.getText().toString())) {
                    return;
                }
                if (isValidEmail(userEmailEditText.getText())) {
                    incorrectEmail.setVisibility(View.GONE);
                    user.setEmail(userEmailEditText.getText().toString());
                    //userEmail.setText(user.getEmail());
                    editEmail.setVisibility(View.VISIBLE);
                    verifyEmail.setText("Verify");
                    verifyEmail.setVisibility(View.VISIBLE);
                    saveEmail.setVisibility(View.GONE);
                    userEmailEditText.setVisibility(View.GONE);
                    userEmail.setVisibility(View.VISIBLE);
                } else {
                    incorrectEmail.setVisibility(View.VISIBLE);
                }
                hideKeyboard();
            }
        });
        userEmailEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (userEmailEditText.getText().length() > 0 &&
                            !"".equals(userEmailEditText.getText().toString().trim()))
                        saveEmail();
                    else {
                        incorrectEmail.setVisibility(View.VISIBLE);
                    }
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });
        userEmailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v == userEmailEditText && !hasFocus) {
                    incorrectEmail.setVisibility(View.GONE);
                    userEmail.setText(user.getEmail());
                    editEmail.setVisibility(View.VISIBLE);
                    if (user.isEmailVerified()) {
                        verifyEmail.setText("Verified!");
                    }
                    verifyEmail.setVisibility(View.VISIBLE);
                    saveEmail.setVisibility(View.GONE);
                    userEmailEditText.setVisibility(View.GONE);
                    userEmail.setVisibility(View.VISIBLE);
                    hideKeyboard();
                }
            }
        });
        userEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                incorrectEmail.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //        dontHaveFb.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                if (connectSocialAccountInsta.getVisibility() == View.GONE) {
        //                    connectSocialAccountFb.setVisibility(View.GONE);
        //                    connectSocialAccountInsta.setVisibility(View.VISIBLE);
        //                    dontHaveFb.setText("or connect with Facebook instead");
        //                } else {
        //                    connectSocialAccountInsta.setVisibility(View.GONE);
        //                    connectSocialAccountFb.setVisibility(View.VISIBLE);
        //                    dontHaveFb.setText("Don't have a Facebook Account?");
        //                }
        //            }
        //        });

        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(userEmail.getText().toString()))
                    if ("Check".equals(verifyEmail.getText().toString()))
                        new FetchLatestUserDetails(getActivity(), user.getUserId(), user).execute();
                    else {
                        new VerifyEmail(getActivity(), user.getUserId(), userEmail.getText().toString()).execute();
                        user.setEmailSent(true);
                    }
            }
        });


    }

    private void setAllHelpTipsEnabled() { socialHelptip.setEnabled(true);}

    private void hideKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void checkIncomplete() {
        if (!user.isEmailVerified()) {
            incompleteEmail.setVisibility(View.VISIBLE);
            user.setIncompleteEmail(true);
        } else {
            user.setIncompleteEmail(false);
        }
        if (!user.isFbConnected()) {
            incompleteFb.setVisibility(View.VISIBLE);
            user.setIncompleteFb(true);
        } else {
            user.setIncompleteFb(false);
        }
        if (!isGenderSelected) {
            user.setIncompleteGender(true);
        } else {
            user.setIncompleteGender(false);
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void replaceFragment2(boolean check) {
        if (check) checkIncomplete();
        String json = gson.toJson(user);
        mPrefs.edit().putString("UserObject", json).apply();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment1, new ProfileFormStep1Fragment2(), "Fragment2Tag");
        ft.commit();
    }

    public void replaceFragment3(boolean check) {
        if (check)
            checkIncomplete();
        String json = gson.toJson(user);
        mPrefs.edit().putString("UserObject", json).apply();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment1, new ProfileFormStep1Fragment3(), "Fragment3Tag");
        ft.commit();
    }

    private void saveEmail() {
        userEmail.setText(userEmailEditText.getText().toString());
        user.setEmail(userEmail.getText().toString());
        user.setUpdateEmail(true);
        editEmail.setVisibility(View.VISIBLE);
        verifyEmail.setVisibility(View.VISIBLE);
        saveEmail.setVisibility(View.GONE);
        userEmailEditText.setVisibility(View.GONE);
        userEmail.setVisibility(View.VISIBLE);
        user.setEmailVerified(false);
        String json = gson.toJson(user);
        mPrefs.edit().putString("UserObject", json).apply();
    }

    public static void setViewAndChildrenEnabled(View view, boolean enabled, View except1, View except2) {
        if (view != except1 && view != except2) {
            view.setEnabled(enabled);
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled, except1, except2);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private class fblogin extends
                          AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            connectSocialAccountFb.setText("");
            connectSocialAccountFb.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.loader, 0, 0);
        }

        @Override
        protected String doInBackground(String... data) {

            // String urldisplay = data[0];
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                payload.put("fbUserId", fbuserId);
                payload.put("fbName", name);
                payload.put("fbEmail", email);
                payload.put("fbGender", gender);
                payload.put("fbFirstName", firstName);
                payload.put("fbLastName", lastName);
                payload.put("fbLink", link);
                payload.put("fbVerified", veri);
                payload.put("fbFriends", friends);
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();
                SharedPreferences cred = getActivity().getSharedPreferences("cred", Context.MODE_PRIVATE);
                String phone = cred.getString("phone_number", "");
                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                String url2 = getActivity().getString(R.string.server) + "api/user/social?userid=" + phone;
                HttpPut httppost = new HttpPut(url2);
                httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");
                httppost.setHeader("x-access-token", mPrefs.getString("token_value", ""));
                httppost.setHeader("Content-Type", "application/json");
                StringEntity entity = new StringEntity(payload.toString());
                httppost.setEntity(entity);
                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");
                if (response.getStatusLine().getStatusCode() != 200) {
                    Log.e("BuddyError", "Server returned code "
                            + response.getStatusLine().getStatusCode());
                    return "authFail";
                } else {
                    JSONObject resp = new JSONObject(responseString);
                    if (resp.getString("status").contains("error")) {
                        Log.e("BuddyError", resp.getString("msg"));
                        if (resp.getString("msg").contains("Invalid Token"))
                            return "authFail";
                        return resp.getString("msg");
                    } else {
                        return "win";
                    }
                }
            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";
            }
        }

        protected void onPostExecute(String result) {
            if (result.equals("win")) {
                w++;
                if (w == 1) {
                    user.setFbUserId(fbuserId);
                    user.setIsFbConnected(true);
                    user.setUpdateFbConnected(true);
                    String json = gson.toJson(user);
                    mPrefs.edit().putString("UserObject", json).apply();
                    isFbLoggedIn();
                    return;
                }
            }
            if (retryCount < 3) {
                retryCount++;
                if (result.equals("authFail")) {
                    new FetchNewToken(getActivity()).execute();
                    new fblogin().execute();
                } else if (result.equals("fail")) {
                    new fblogin().execute();
                }
            } else {
                connectSocialAccountFb.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fb, 0, 0, 0);
                connectSocialAccountFb.setText("Try connecting again!");
            }
        }
    }

    public void isFbLoggedIn() {
        try {
            if (user.isFbConnected()) {
                completeFb.setVisibility(View.VISIBLE);
                user.setIncompleteFb(false);
                //                dontHaveFb.setVisibility(View.INVISIBLE);
                incompleteFb.setVisibility(View.GONE);
                connectSocialAccountFb.setClickable(false);
                connectSocialAccountFb.setEnabled(false);
                connectSocialAccountFb.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fb_white, 0, 0, 0);
                connectSocialAccountFb.setTextColor(getActivity().getResources().getColor(R.color.colorwhite));
                connectSocialAccountFb.setText("Account Connected!");
                connectSocialAccountFb.setBackgroundResource(R.drawable.border_button_green);
                //                connectSocialAccountFb.setBackgroundColor(Color.parseColor("#44c2a6"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}