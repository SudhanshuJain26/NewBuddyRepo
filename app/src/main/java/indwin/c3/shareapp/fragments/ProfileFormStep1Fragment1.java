package indwin.c3.shareapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import indwin.c3.shareapp.BuildConfig;
import indwin.c3.shareapp.R;
import indwin.c3.shareapp.Views.DatePicker;
import indwin.c3.shareapp.activities.ProfileFormStep1;
import indwin.c3.shareapp.adapters.SpinnerHintAdapter;
import indwin.c3.shareapp.models.Error;
import indwin.c3.shareapp.models.FBUserModel;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.DaysDifferenceFinder;
import indwin.c3.shareapp.utils.FetchNewToken;
import indwin.c3.shareapp.utils.HelpTipDialog;
import io.intercom.com.google.gson.Gson;

/**
 * Created by ROCK
 */
public class ProfileFormStep1Fragment1 extends Fragment {
    static EditText dobEditText;
    private static DatePicker datePicker;
    Button connectSocialAccountFb, connectSocialAccountInsta;
    CallbackManager callbackManager;
    private String email, firstName, friends, gender, lastName, link, name, fbuserId;
    private Boolean veri;
    int w = 0;
    LoginManager loginManager;
    int retryCount = 0;
    UserModel user;
    public static ImageView completeEmail, incompleteEmail;
    ImageView completeFb, incompleteFb;
    ImageView completeGender, incompleteGender;
    boolean isGenderSelected = false;
    ImageView topImage;
    public static Button verifyEmail;
    private ImageButton socialHelptip;
    private Spinner genderSpinner;
    private ImageView incompleteDOB, completeDOB;
    static boolean updateUserDOB = false;
    private TextView fbErrorTv;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View rootView = inflater.inflate(
                R.layout.profile_form_step1_fragment1, container, false);

        FacebookSdk.sdkInitialize(getActivity());
        getAllViews(rootView);
        setAllClickListener();

        ProfileFormStep1 profileFormStep1 = (ProfileFormStep1) getActivity();
        user = profileFormStep1.getUser();
        if (user.isAppliedFor1k()) {
            setViewAndChildrenEnabled(rootView, false);
        }
        setAllHelpTipsEnabled();

        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        registerFbCallback();
        isFbLoggedIn();
        if (AppUtils.isNotEmpty(user.getDob())) {
            dobEditText.setText(user.getDob());
            completeDOB.setVisibility(View.VISIBLE);
            user.setIncompleteDOB(false);
        }
        datePicker = new

                DatePicker(getActivity(),

                "DOB");
        datePicker.build(new DialogInterface.OnClickListener()

                         {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                             }
                         }

                , null);


        final String genderOptions[] = getResources().getStringArray(R.array.gender);
        final SpinnerHintAdapter adapter2 = new SpinnerHintAdapter(getActivity(), genderOptions, R.layout.spinner_item_underline);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner = (Spinner) rootView.findViewById(R.id.gender_spinner);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < genderOptions.length - 1) {
                    saveGender();
                }
                if (position == 1) {
                    topImage.setImageDrawable(getResources().getDrawable(R.drawable.step1fragment1girl));

                } else {
                    topImage.setImageDrawable(getResources().getDrawable(R.drawable.step1fragment1));

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
                                .load(R.drawable.step1fragment1girl)
                                .into(topImage);
                    }
                    genderSpinner.setSelection(i);
                    completeGender.setVisibility(View.VISIBLE);
                    user.setIncompleteGender(false);
                    break;
                }
            }
        }


        if (user.isIncompleteEmail() || user.isIncompleteFb() || user.isIncompleteGender()) {
            if (user.isIncompleteFb() && !user.isAppliedFor1k()) {
                incompleteFb.setVisibility(View.VISIBLE);
            }
            if (user.isIncompleteGender() && !user.isAppliedFor1k()) {
                incompleteGender.setVisibility(View.VISIBLE);
            }
        }
        if (user.getCollegeIds() != null && user.getCollegeIds().size() > 0) {
            user.setIncompleteCollegeId(false);
        }

        return rootView;
    }

    private void registerFbCallback() {

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
                                    firstName = fbUserModel.getFirst_name();
                                    gender = fbUserModel.getGender();
                                    lastName = fbUserModel.getLast_name();
                                    link = fbUserModel.getLink();
                                    fbuserId = fbUserModel.getId();
                                    email = fbUserModel.getEmail();
                                    //UserModel user = AppUtils.getUserObject(getActivity());
                                    //user.setFbUserId(fbuserId);
                                    //AppUtils.saveUserObject(getActivity(), user);
                                    //SharedPreferences sf = getActivity().getSharedPreferences("proid", Context.MODE_PRIVATE);
                                    //SharedPreferences.Editor editor2 = sf.edit();
                                    //editor2.putString("dpid", fbuserId);
                                    //editor2.commit();
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
                                if (AppUtils.isEmpty(name) || AppUtils.isEmpty(friends)) {
                                    Set<String> denied = loginResult.getRecentlyDeniedPermissions();
                                    if (denied.size() > 0 && !denied.contains("email")) {
                                        Toast.makeText(getActivity(), "Please grant all permissions to complete your profile!", Toast.LENGTH_SHORT).show();
                                        LoginManager.getInstance().logInWithReadPermissions(ProfileFormStep1Fragment1.this, Arrays.asList("user_friends", "user_birthday"));
                                    }
                                    Toast.makeText(getActivity(), "Please try again!", Toast.LENGTH_SHORT).show();
                                } else {
                                    new FBLogin().execute();
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
    }


    private void getAllViews(View rootView) {
        dobEditText = (EditText) rootView.findViewById(R.id.user_dob_edittext);
        completeDOB = (ImageView) rootView.findViewById(R.id.complete_dob);
        incompleteDOB = (ImageView) rootView.findViewById(R.id.incomplete_dob);
        verifyEmail = (Button) rootView.findViewById(R.id.verify_user_email);
        connectSocialAccountFb = (Button) rootView.findViewById(R.id.connect_social_account_fb);
        connectSocialAccountInsta = (Button) rootView.findViewById(R.id.connect_social_account_insta);
        //        dontHaveFb = (Button) rootView.findViewById(R.id.dont_have_fb);
        completeEmail = (ImageView) rootView.findViewById(R.id.complete_email);
        completeFb = (ImageView) rootView.findViewById(R.id.complete_fb);
        incompleteEmail = (ImageView) rootView.findViewById(R.id.incomplete_email);
        incompleteFb = (ImageView) rootView.findViewById(R.id.incomplete_fb);
        completeGender = (ImageView) rootView.findViewById(R.id.complete_gender);
        incompleteGender = (ImageView) rootView.findViewById(R.id.incomplete_gender);
        topImage = (ImageView) getActivity().findViewById(R.id.verify_image_view2);
        socialHelptip = (ImageButton) rootView.findViewById(R.id.social_helptip);
        fbErrorTv = (TextView) rootView.findViewById(R.id.fb_error_tv);

    }


    private void setAllClickListener() {
        dobEditText.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });
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
                fbErrorTv.setText("");
                fbErrorTv.setVisibility(View.GONE);
                loginManager.logOut();
                loginManager.logInWithReadPermissions(ProfileFormStep1Fragment1.this, Arrays.asList("public_profile", "user_friends", "email", "user_birthday"));

            }
        });


    }

    private void setAllHelpTipsEnabled() { socialHelptip.setEnabled(true);}


    public static void confirmDOB() {
        String date = datePicker.getSelectedDate() + " " + datePicker.getSelectedMonthName() + " " + datePicker.getSelectedYear();
        dobEditText.setText(date);
        updateUserDOB = true;
    }

    public void checkIncomplete() {

        if (updateUserDOB) {
            try {
                SimpleDateFormat spf = new SimpleDateFormat("dd MMM yyyy");
                Date newDate = spf.parse(dobEditText.getText().toString());
                spf = new SimpleDateFormat("yyyy-MM-dd");
                user.setDob(spf.format(newDate));
                user.setUpdateDOB(true);

                Calendar endCalendar = new GregorianCalendar();
                endCalendar.setTime(new Date());
                Calendar startCalendar = new GregorianCalendar();
                startCalendar.setTime(newDate);

                int age = DaysDifferenceFinder.getDifferenceBetweenDatesInYears(startCalendar, endCalendar);
                if (age < 18) {
                    //isUnderAge = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!user.isFbConnected()) {
            incompleteFb.setVisibility(View.VISIBLE);
            user.setIncompleteFb(true);
        } else {
            user.setIncompleteFb(false);
        }
        if (!isGenderSelected) {
            user.setIncompleteGender(true);
            completeGender.setVisibility(View.GONE);
            incompleteGender.setVisibility(View.VISIBLE);
        } else {
            user.setIncompleteGender(false);
            incompleteGender.setVisibility(View.GONE);
            completeGender.setVisibility(View.VISIBLE);
        }
        if ("".equals(dobEditText.getText().toString())) {
            user.setIncompleteDOB(true);
            completeDOB.setVisibility(View.GONE);
            incompleteDOB.setVisibility(View.VISIBLE);
        } else {
            user.setIncompleteDOB(false);
            incompleteDOB.setVisibility(View.GONE);
            completeDOB.setVisibility(View.VISIBLE);
        }
    }

    private void saveGender() {
        isGenderSelected = true;
        user.setGender(genderSpinner.getSelectedItem().toString());
        user.setUpdateGender(true);
    }

    private void saveFb() {
        user.setFbUserId(fbuserId);
        user.setIsFbConnected(true);
        user.setUpdateFbConnected(true);
        UserModel user = AppUtils.getUserObject(getActivity());
        user.setFbUserId(fbuserId);
        user.setIsFbConnected(true);
        user.setUpdateFbConnected(true);
        AppUtils.saveUserObject(getActivity(), user);
        SharedPreferences sf = getActivity().getSharedPreferences("proid", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sf.edit();
        editor2.putString("dpid", fbuserId);
        editor2.commit();
    }


    public static void setViewAndChildrenEnabled(View view, boolean enabled) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                child.setEnabled(enabled);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void showErrorResponse(Error responseModel) {


    }

    private class FBLogin extends
                          AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            connectSocialAccountFb.setText("");
            connectSocialAccountFb.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.loader, 0, 0);
        }

        @Override
        protected String doInBackground(String... data) {
            SharedPreferences cred = getActivity().getSharedPreferences("cred", Context.MODE_PRIVATE);
            String phone = cred.getString("phone_number", "");
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
                payload.put("userid", phone);

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                String url2 = BuildConfig.SERVER_URL + "api/v1.01/user/social?userid=" + phone;
                HttpPut httppost = new HttpPut(url2);
                httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");
                httppost.setHeader("x-access-token", AppUtils.getFromSharedPrefs(getActivity(), "token_value"));
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
                fbErrorTv.setVisibility(View.GONE);
                user.setFbConnected(true);
                isFbLoggedIn();
                return;
            }
            if (result.equals("authFail")) {
                new FetchNewToken(getActivity()).execute();
                new FBLogin().execute();
            } else {
                showFbErrorMessage(result);
                connectSocialAccountFb.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fb, 0, 0, 0);
                connectSocialAccountFb.setText("Try connecting again!");
            }
        }
    }

    private void showFbErrorMessage(String message) {
        try {
            fbErrorTv.setText(message);
            fbErrorTv.setVisibility(View.VISIBLE);

        } catch (Exception e) {
        }
    }

    public void isFbLoggedIn() {
        try {
            if (user.isFbConnected()) {
                completeFb.setVisibility(View.VISIBLE);
                user.setIncompleteFb(false);
                incompleteFb.setVisibility(View.GONE);
                connectSocialAccountFb.setClickable(false);
                connectSocialAccountFb.setEnabled(false);
                connectSocialAccountFb.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fb_white, 0, 0, 0);
                connectSocialAccountFb.setTextColor(getActivity().getResources().getColor(R.color.colorwhite));
                connectSocialAccountFb.setText("Account Connected!");
                connectSocialAccountFb.setBackgroundResource(R.drawable.border_button_green);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}