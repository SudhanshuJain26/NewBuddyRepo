package indwin.c3.shareapp.activities;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.adapters.ScreenSlidePagerAdapter;
import indwin.c3.shareapp.application.BuddyApplication;
import indwin.c3.shareapp.fragments.ProfileFormStep1Fragment1;
import indwin.c3.shareapp.fragments.ProfileFormStep1Fragment2;
import indwin.c3.shareapp.fragments.ProfileFormStep1Fragment3;
import indwin.c3.shareapp.fragments.ProfileFormStep1Fragment4;
import indwin.c3.shareapp.models.Error;
import indwin.c3.shareapp.models.Image;
import indwin.c3.shareapp.models.ResponseModel;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.CheckInternetAndUploadUserDetails;
import indwin.c3.shareapp.utils.Constants;
import io.intercom.android.sdk.Intercom;

public class ProfileFormStep1 extends AppCompatActivity implements ViewPager.OnPageChangeListener, CheckInternetAndUploadUserDetails.NotifyProgress {

    public static boolean isBackInsideFrag = false;
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private ArrayList<Fragment> fragments;
    private TextView gotoFragment1, gotoFragment2, gotoFragment3, gotoFragment4;
    private Button saveAndProceed, previous;
    private UserModel user;
    private ImageView genderImage;
    private ImageView incompleteStep1, incompleteStep2, incompleteStep3, incompleteStep4;
    int previousPosition;
    private ProgressDialog progressDialog;
    private static boolean isActive = false;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_form_step1);
        user = AppUtils.getUserObject(this);

        setAllUpdateFalse();
        getAllViews();
        setCLickListener();

        SharedPreferences mPrefs = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        if (mPrefs.getBoolean("visitedFormStep1Fragment2", false)) {
            gotoFragment2.setAlpha(1);
            gotoFragment2.setClickable(true);
        }
        if (mPrefs.getBoolean("visitedFormStep1Fragment3", false)) {
            gotoFragment3.setAlpha(1);
            gotoFragment3.setClickable(true);
        }

        UserModel user = AppUtils.getUserObject(this);
        if (user.isAppliedFor1k()) {
            saveAndProceed.setVisibility(View.GONE);
            previous.setVisibility(View.GONE);
            findViewById(R.id.details_submitted_tv).setVisibility(View.VISIBLE);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            TextView headerTitle = (TextView) findViewById(R.id.activity_header);
            headerTitle.setText("Verify your Identity");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

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

            mPager = (ViewPager) findViewById(R.id.pager);
            mPager.setOnPageChangeListener(this);
            populateFragments();
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);
            mPager.setAdapter(mPagerAdapter);
            mPager.setOffscreenPageLimit(4);
            mPager.setAdapter(mPagerAdapter);
            if (!user.isAppliedFor1k()) {
                showHideIncompleteStep1();
                showHideIncompleteStep2();
                showHideIncompleteStep3();
                showHideIncompleteStep4();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    private void populateFragments() {
        fragments = new ArrayList<>();
        fragments.add(new ProfileFormStep1Fragment1());
        fragments.add(new ProfileFormStep1Fragment2());
        fragments.add(new ProfileFormStep1Fragment3());
        fragments.add(new ProfileFormStep1Fragment4());
    }

    private void previousPage() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    private void setCLickListener() {
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousPage();

            }
        });
        gotoFragment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(0);
            }
        });
        gotoFragment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(1);
            }
        });

        gotoFragment3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(2);
            }
        });
        gotoFragment4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(3);
            }
        });
        saveAndProceed.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  int currentPage = mPager.getCurrentItem();

                                                  if (currentPage != (fragments.size() - 1)) {
                                                      mPager.setCurrentItem(currentPage + 1);
                                                      uploadDetailsToServer(null);


                                                  } else {
                                                      boolean incompleteStep4 = checkIncompleteStep4();
                                                      saveStep4Data(null);
                                                      if (checkIncompleteStep1() || checkIncompleteStep2() || checkIncompleteStep3() || incompleteStep4) {
                                                          openDialogBox();
                                                          uploadDetailsToServer(null);
                                                          return;
                                                      } else {
                                                          SharedPreferences mPrefs = getSharedPreferences("buddy", Context.MODE_PRIVATE);
                                                          mPrefs.edit().putBoolean("updatingDB", false).apply();

                                                          progressDialog = new ProgressDialog(ProfileFormStep1.this);
                                                          progressDialog.setCancelable(false);
                                                          progressDialog.setMessage("Submitting your details..");
                                                          progressDialog.show();
                                                          uploadDetailsToServer(Constants.YES_1k);


                                                      }
                                                  }
                                              }
                                          }

        );
    }

    private void openDialogBox() {
        final Dialog dialog1 = new Dialog(ProfileFormStep1.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.incomplete_alert_box);

        Button okay = (Button) dialog1.findViewById(R.id.okay_button);
        okay.setTextColor(Color.parseColor("#44c2a6"));
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDetailsToServer(null);

                dialog1.dismiss();
                Intent intent2 = new Intent(ProfileFormStep1.this, ProfileActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                finish();
            }
        });

        CheckBox stopMessage = (CheckBox) dialog1.findViewById(R.id.check_message);
        stopMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mPrefs = getSharedPreferences(AppUtils.APP_NAME, Context.MODE_PRIVATE);
                if (((CheckBox) v).isChecked()) {
                    mPrefs.edit().putBoolean("skipIncompleteMessage", true).apply();
                } else {
                    mPrefs.edit().putBoolean("skipIncompleteMessage", false).apply();
                }
            }
        });
        dialog1.show();
    }

    private void saveStep1Data() {
        UserModel userModel = AppUtils.getUserObject(this);
        if (AppUtils.isNotEmpty(user.getDob()) && user.isUpdateDOB()) {
            userModel.setDob(user.getDob());
            userModel.setUpdateDOB(true);
            user.setUpdateDOB(false);
        }
        if (AppUtils.isNotEmpty(user.getGender()) && user.isUpdateGender()) {

            userModel.setGender(user.getGender());
            userModel.setUpdateGender(true);

            user.setUpdateGender(false);
        }
        AppUtils.saveUserObject(this, userModel);
        uploadDetailsToServer(null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setIncomplete();
    }

    private void saveStep2Data() {
        UserModel userModel = AppUtils.getUserObject(this);


        if (AppUtils.isNotEmpty(user.getRollNumber()) && user.isUpdateRollNumber()) {
            userModel.setRollNumber(user.getRollNumber());
            userModel.setUpdateRollNumber(true);

        }
        if (AppUtils.isNotEmpty(user.getCollegeName()) && user.isUpdateCollegeName()) {
            userModel.setCollegeName(user.getCollegeName());
            userModel.setUpdateCollegeName(true);

        }
        if (AppUtils.isNotEmpty(user.getCourseName()) && user.isUpdateCourseName()) {
            userModel.setCourseName(user.getCourseName());
            userModel.setUpdateCourseName(true);

        }
        if (user.isUpdateCourseEndDate()) {
            try {
                SimpleDateFormat spf = new SimpleDateFormat("MMM yyyy");
                Date newDate = spf.parse(user.getCourseEndDate());
                spf = new SimpleDateFormat("yyyy-MM-dd");
                userModel.setCourseEndDate(spf.format(newDate));
                userModel.setUpdateCourseEndDate(true);
                Calendar startCalendar = new GregorianCalendar();
                startCalendar.setTime(new Date());
                Calendar endCalendar = new GregorianCalendar();
                endCalendar.setTime(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        AppUtils.saveUserObject(this, userModel);
        user.setUpdateCourseEndDate(false);
        user.setUpdateRollNumber(false);
        user.setUpdateCollegeName(false);
        user.setUpdateCourseName(false);
        uploadDetailsToServer(null);

    }

    private void saveStep3Data() {
        if (user.getAddressProof() != null) {
            UserModel userModel = AppUtils.getUserObject(this);
            if (userModel.getAddressProof() == null) {
                userModel.setAddressProof(new Image());
            }
            userModel.getAddressProof().setType(this.user.getAddressProof().getType());
            AppUtils.saveUserObject(this, userModel);
        }
        uploadDetailsToServer(null);
    }

    private void saveStep4Data(String isLastStep) {

        UserModel userModel = AppUtils.getUserObject(this);
        if (AppUtils.isNotEmpty(user.getAccommodation()) && user.isUpdateAccommodation()) {
            userModel.setAccommodation(user.getAccommodation());
            userModel.setUpdateAccommodation(true);
            user.setUpdateAccommodation(false);
        }

        AppUtils.saveUserObject(this, userModel);

    }

    private boolean checkIncompleteStep4() {
        try {
            ProfileFormStep1Fragment4 profileFormStep1Fragment4 = (ProfileFormStep1Fragment4) mPagerAdapter.getFragment(3);
            profileFormStep1Fragment4.checkIncomplete();
        } catch (Exception e) {
        }
        return showHideIncompleteStep4();
    }


    private void setAllUpdateFalse() {
        user.setUpdateDOB(false);
        user.setUpdateGender(false);

        user.setUpdateRollNumber(false);
        user.setUpdateCollegeName(false);
        user.setUpdateCourseName(false);
    }


    private boolean checkIncompleteStep1() {
        try {
            ProfileFormStep1Fragment1 profileFormStep1Fragment1 = (ProfileFormStep1Fragment1) mPagerAdapter.getFragment(0);
            profileFormStep1Fragment1.checkIncomplete();
        } catch (Exception e) {
        }
        return showHideIncompleteStep1();
    }

    private boolean showHideIncompleteStep1() {
        if (user.isIncompleteDOB() || user.isIncompleteGender() || user.isIncompleteFb()) {
            incompleteStep1.setVisibility(View.VISIBLE);
            return true;
        }
        incompleteStep1.setVisibility(View.GONE);
        return false;
    }

    private boolean checkIncompleteStep2() {
        //ProfileFormStep1Fragment2 profileFormStep1Fragment2 = (ProfileFormStep1Fragment2) getSupportFragmentManager().getFragments().get(1);
        try {
            ProfileFormStep1Fragment2 profileFormStep1Fragment2 = (ProfileFormStep1Fragment2) mPagerAdapter.getFragment(1);
            profileFormStep1Fragment2.checkIncomplete();
        } catch (Exception e) {
        }
        return showHideIncompleteStep2();
    }

    private boolean showHideIncompleteStep2() {
        if (user.isIncompleteCollegeId() || user.isIncompleteRollNumber() || user.isIncompleteCollegeDetails()) {
            incompleteStep2.setVisibility(View.VISIBLE);
            return true;
        }
        incompleteStep2.setVisibility(View.GONE);
        return false;
    }

    private boolean checkIncompleteStep3() {
        try {
            ProfileFormStep1Fragment3 profileFormStep1Fragment3 = (ProfileFormStep1Fragment3) mPagerAdapter.getFragment(2);
            profileFormStep1Fragment3.checkIncomplete();
        } catch (Exception e) {
        }
        return showHideIncompleteStep3();
    }

    private boolean showHideIncompleteStep3() {
        if (user.isIncompleteAadhar() || user.isIncompletePermanentAddress()) {
            incompleteStep3.setVisibility(View.VISIBLE);
            return true;
        }
        incompleteStep3.setVisibility(View.GONE);
        return false;
    }

    private boolean showHideIncompleteStep4() {
        if (user.isIncompleteAddressDetails() || user.isInCompleteAgreement()) {
            incompleteStep4.setVisibility(View.VISIBLE);
            return true;
        }
        incompleteStep4.setVisibility(View.GONE);
        return false;
    }


    private void getAllViews() {
        gotoFragment1 = (TextView) findViewById(R.id.goto_fragment1);
        gotoFragment2 = (TextView) findViewById(R.id.goto_fragment2);
        gotoFragment3 = (TextView) findViewById(R.id.goto_fragment3);
        gotoFragment4 = (TextView) findViewById(R.id.goto_fragment4);
        incompleteStep1 = (ImageView) findViewById(R.id.incomplete_step_1);
        incompleteStep2 = (ImageView) findViewById(R.id.incomplete_step_2);
        incompleteStep3 = (ImageView) findViewById(R.id.incomplete_step_3);
        incompleteStep4 = (ImageView) findViewById(R.id.incomplete_step_4);
        saveAndProceed = (Button) findViewById(R.id.save_and_proceed);
        previous = (Button) findViewById(R.id.previous);
        genderImage = (ImageView) findViewById(R.id.verify_image_view2);
        //CircleProgressView cpv = (CircleProgressView) findViewById(R.id.circleView);
        //cpv.bringToFront();
    }

    private void setIncomplete() {
        UserModel userModel = AppUtils.getUserObject(this);
        userModel.setIncompleteDOB(user.isIncompleteDOB());
        userModel.setIncompleteGender(user.isIncompleteGender());
        userModel.setIncompleteFb(user.isIncompleteFb());

        userModel.setIncompleteCollegeId(user.isIncompleteCollegeId());
        userModel.setIncompleteRollNumber(user.isIncompleteRollNumber());
        userModel.setIncompleteCollegeDetails(user.isIncompleteCollegeDetails());

        userModel.setIncompleteAadhar(user.isIncompleteAadhar());
        userModel.setIncompletePermanentAddress(user.isIncompletePermanentAddress());
        userModel.setInCompleteAgreement(user.isInCompleteAgreement());

        AppUtils.saveUserObject(this, userModel);

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.sendGoogleAnalytics((BuddyApplication) getApplication());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void uploadDetailsToServer(String isLastStep) {
        CheckInternetAndUploadUserDetails ciauo = new CheckInternetAndUploadUserDetails(ProfileFormStep1.this, isLastStep);

    }

    @Override
    public void onBackPressed() {
        onPageSelected(mPager.getCurrentItem());
        super.onBackPressed();
    }

    private void hideShowUpArrow(int i) {
        if (!user.isAppliedFor1k())
            previous.setVisibility(View.VISIBLE);
        findViewById(R.id.up_arrow_1).setVisibility(View.GONE);
        findViewById(R.id.up_arrow_2).setVisibility(View.GONE);
        findViewById(R.id.up_arrow_3).setVisibility(View.GONE);
        findViewById(R.id.up_arrow_4).setVisibility(View.GONE);
        int image = 0;
        boolean isGirl = user.getGender() != null && "girl".equals(user.getGender());

        if (i == 0) {
            previous.setVisibility(View.GONE);
            findViewById(R.id.up_arrow_1).setVisibility(View.VISIBLE);
            if (isGirl) {
                image = R.drawable.step1fragment1girl;
            } else {
                image = R.drawable.step1fragment1;
            }

        } else if (i == 1) {
            if (isGirl) {
                image = R.drawable.step1fragment2girl;
            } else {
                image = R.drawable.step1fragment2;
            }
            findViewById(R.id.up_arrow_2).setVisibility(View.VISIBLE);
        } else if (i == 2) {
            if (isGirl) {
                image = R.drawable.step1fragment3girl;
            } else {
                image = R.drawable.step1fragment3;
            }
            findViewById(R.id.up_arrow_3).setVisibility(View.VISIBLE);
        } else if (i == 3) {
            if (isGirl) {
                image = R.drawable.step1fragment4girl;
            } else {
                image = R.drawable.step1fragment4;
            }
            findViewById(R.id.up_arrow_4).setVisibility(View.VISIBLE);
        }
        genderImage.setImageDrawable(getResources().getDrawable(image));
        //ProgressPieView ppv=(ProgressPieView)findViewById(R.id.progressPieViewXml);
        //ppv.setProgress(60);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (!user.isAppliedFor1k()) {
            if (previousPosition == 1) {

                checkIncompleteStep2();
                saveStep2Data();
            } else if (previousPosition == 2) {

                checkIncompleteStep3();
                saveStep3Data();
            } else if (previousPosition == 0) {

                checkIncompleteStep1();
                saveStep1Data();
            } else if (previousPosition == 3) {

                checkIncompleteStep4();
                saveStep4Data(null);
                uploadDetailsToServer(null);

            }
        }
        previousPosition = position;
        hideShowUpArrow(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    @Override
    public void notifyUploadData(final ResponseModel responseModel, final String isLastStep, final Activity activity) {
        //if (isActive) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog != null && progressDialog.isShowing() && Constants.YES_1k.equals(isLastStep)) {
                    progressDialog.hide();
                    if (responseModel != null && responseModel.getData() != null && responseModel.getData().isAppliedFor1k()) {
                        Intent intent1 = new Intent(activity, PendingFlashApprovalActivity.class);
                        startActivity(intent1);
                        finish();
                    } else if (responseModel != null) {
                        openDialogBox();
                    }
                }

                if (responseModel != null) {
                    for (Error error : responseModel.getErrors()) {
                        if (error.getField().equalsIgnoreCase("fbUserId")) {
                            ProfileFormStep1Fragment1 profileFormStep1Fragment1 = (ProfileFormStep1Fragment1) mPagerAdapter.getFragment(0);
                            profileFormStep1Fragment1.showErrorResponse(error);
                        } else if (error.getField().equalsIgnoreCase("pan")) {
                            UserModel userDB = AppUtils.getUserObject(activity);
                            userDB.setPanNumber(null);
                            userDB.setUpdatePanNumber(true);
                            AppUtils.saveUserObject(activity, userDB);

                            user.setPanNumber(null);
                            user.setPanDuplicate(true);
                            user.setUpdatePanNumber(true);
                            ProfileFormStep1Fragment3 profileFormStep1Fragment3 = (ProfileFormStep1Fragment3) mPagerAdapter.getFragment(2);
                            profileFormStep1Fragment3.showErrorpanAadhaar(error);
                            incompleteStep3.setVisibility(View.VISIBLE);
                        } else if (error.getField().equalsIgnoreCase("aadhar")) {
                            UserModel userDB = AppUtils.getUserObject(activity);
                            userDB.setAadharNumber(null);
                            userDB.setUpdateAadharNumber(true);
                            AppUtils.saveUserObject(activity, userDB);
                            user.setAadharNumber(null);
                            user.setAadhaarDuplicate(true);
                            user.setUpdateAadharNumber(true);
                            ProfileFormStep1Fragment3 profileFormStep1Fragment3 = (ProfileFormStep1Fragment3) mPagerAdapter.getFragment(2);
                            profileFormStep1Fragment3.showErrorpanAadhaar(error);
                            incompleteStep3.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Toast.makeText(ProfileFormStep1.this, "Error connecting to server", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //}
    }

}
