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

import java.util.ArrayList;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.adapters.ScreenSlidePagerAdapter;
import indwin.c3.shareapp.application.BuddyApplication;
import indwin.c3.shareapp.fragments.ProfileFormStep2Fragment1;
import indwin.c3.shareapp.fragments.ProfileFormStep2Fragment2;
import indwin.c3.shareapp.fragments.ProfileFormStep2Fragment3;
import indwin.c3.shareapp.fragments.ProfileFormStep2Fragment4;
import indwin.c3.shareapp.models.Error;
import indwin.c3.shareapp.models.ResponseModel;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.CheckInternetAndUploadUserDetails;
import indwin.c3.shareapp.utils.Constants;
import io.intercom.android.sdk.Intercom;

public class ProfileFormStep2 extends AppCompatActivity implements ViewPager.OnPageChangeListener, CheckInternetAndUploadUserDetails.NotifyProgress {
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
        setContentView(R.layout.activity_profile_form_step2);
        getAllViews();
        setCLickListener();
        user = AppUtils.getUserObject(this);
        setImage(0);
        if (user.isAppliedFor7k()) {
            saveAndProceed.setVisibility(View.INVISIBLE);
            previous.setVisibility(View.INVISIBLE);
            findViewById(R.id.details_submitted_tv).setVisibility(View.VISIBLE);
        }
        setAllUpdateFalse();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            TextView headerTitle = (TextView) findViewById(R.id.activity_header);
            headerTitle.setText("Setup Automatic Repayments");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setOnPageChangeListener(this);
        populateFragments();
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(4);
        mPager.setAdapter(mPagerAdapter);
        if (!user.isAppliedFor7k()) {
            showHideIncompleteStep1();
            showHideIncompleteStep2();
            showHideIncompleteStep4();
            showHideIncompleteStep3();
        }
    }

    public void showHideBankStatements(boolean isBankLayoutVisible) {

        ProfileFormStep2Fragment4 profileFormStep2Fragment4 = (ProfileFormStep2Fragment4) mPagerAdapter.getFragment(3);
        profileFormStep2Fragment4.showHideBankStatement(isBankLayoutVisible);


    }

    private void populateFragments() {
        fragments = new ArrayList<>();
        fragments.add(new ProfileFormStep2Fragment1());
        fragments.add(new ProfileFormStep2Fragment2());
        fragments.add(new ProfileFormStep2Fragment3());
        fragments.add(new ProfileFormStep2Fragment4());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    private void previousPage() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
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
                        if (AppUtils.isOnline(ProfileFormStep2.this)) {
                            SharedPreferences mPrefs = getSharedPreferences("buddy", Context.MODE_PRIVATE);
                            mPrefs.edit().putBoolean("updatingDB", false).apply();
                            progressDialog = new ProgressDialog(ProfileFormStep2.this);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage("Submitting your details..");
                            progressDialog.show();
                            uploadDetailsToServer(Constants.YES_7k);
                        } else {
                            Toast.makeText(ProfileFormStep2.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }


    private void openDialogBox() {

        final Dialog dialog1 = new Dialog(ProfileFormStep2.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.incomplete_alert_box);

        Button okay = (Button) dialog1.findViewById(R.id.okay_button);
        okay.setTextColor(Color.parseColor("#44c2a6"));
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDetailsToServer(null);
                dialog1.dismiss();
                Intent intent2 = new Intent(ProfileFormStep2.this, ProfileActivity.class);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setIncomplete();
    }

    private void saveStep1Data() {
        UserModel userModel = AppUtils.getUserObject(this);

        if (AppUtils.isNotEmpty(user.getGpaType()) && user.isGpaTypeUpdate()) {
            userModel.setGpaType(user.getGpaType());
            userModel.setGpaTypeUpdate(true);
            user.setGpaTypeUpdate(false);
        }
        if (AppUtils.isNotEmpty(user.getGpa()) && user.isGpaValueUpdate()) {
            userModel.setGpa(user.getGpa());
            userModel.setGpaValueUpdate(true);
            user.setGpaValueUpdate(false);
        }

        if (AppUtils.isNotEmpty(user.getStudentLoan()) && user.isUpdateStudentLoan()) {
            userModel.setStudentLoan(user.getStudentLoan());
            userModel.setUpdateStudentLoan(true);
        }
        AppUtils.saveUserObject(this, userModel);
        uploadDetailsToServer(null);
    }

    private void saveStep2Data() {
        UserModel userModel = AppUtils.getUserObject(this);
        if (AppUtils.isNotEmpty(user.getFamilyMemberType1()) && user.isUpdateFamilyMemberType1()) {
            userModel.setFamilyMemberType1(user.getFamilyMemberType1());
            userModel.setUpdateFamilyMemberType1(true);
            user.setUpdateFamilyMemberType1(false);
        }
        if (AppUtils.isNotEmpty(user.getProfessionFamilyMemberType1()) && user.isUpdateProfessionFamilyMemberType1()) {
            userModel.setProfessionFamilyMemberType1(user.getProfessionFamilyMemberType1());
            userModel.setUpdateProfessionFamilyMemberType1(true);
            user.setUpdateProfessionFamilyMemberType1(false);
        }
        if (user.isUpdatePhoneFamilyMemberType1()) {
            userModel.setPhoneFamilyMemberType1(user.getPhoneFamilyMemberType1());
            userModel.setUpdatePhoneFamilyMemberType1(true);
            user.setUpdatePhoneFamilyMemberType1(false);
        }
        if (AppUtils.isNotEmpty(user.getPrefferedLanguageFamilyMemberType1()) && user.isUpdatePreferredLanguageFamilyMemberType1()) {
            userModel.setPrefferedLanguageFamilyMemberType1(user.getPrefferedLanguageFamilyMemberType1());
            userModel.setUpdatePreferredLanguageFamilyMemberType1(true);
            user.setUpdatePreferredLanguageFamilyMemberType1(false);
        }

        AppUtils.saveUserObject(this, userModel);
        uploadDetailsToServer(null);

    }

    private void saveStep4Data(String isLastStep) {
        UserModel userModel = AppUtils.getUserObject(this);

        if (user.isUpdateBankAccNum()) {
            userModel.setBankAccNum(user.getBankAccNum());
            userModel.setUpdateBankAccNum(true);
        }
        if (user.isUpdateBankIfsc()) {
            userModel.setBankIfsc(user.getBankIfsc());
            userModel.setUpdateBankIfsc(true);
        }
        userModel.setOptionalNACH(user.isOptionalNACH());

        AppUtils.saveUserObject(this, userModel);
        user.setUpdateBankIfsc(false);
        user.setUpdateBankAccNum(true);
    }


    private void saveStep3Data() {
        UserModel userModel = AppUtils.getUserObject(this);

        if (user.isUpdateRollNumber()) {
            userModel.setRollNumber(user.getRollNumber());
            userModel.setUpdateRollNumber(true);
            user.setUpdateRollNumber(false);

        }

        if (AppUtils.isNotEmpty(user.getClassmateName()) && user.isUpdateClassmateName()) {
            userModel.setClassmateName(user.getClassmateName());
            userModel.setUpdateClassmateName(true);
            user.setUpdateClassmateName(false);

        }
        if (AppUtils.isNotEmpty(user.getClassmatePhone()) && user.isUpdateClassmatePhone()) {
            userModel.setClassmatePhone(user.getClassmatePhone());
            userModel.setUpdateClassmatePhone(true);
            user.setUpdateClassmatePhone(false);

        }

        if (AppUtils.isNotEmpty(user.getVerificationDate()) && user.isUpdateVerificationDate()) {
            userModel.setVerificationDate(user.getVerificationDate());
            userModel.setUpdateVerificationDate(true);
            user.setUpdateVerificationDate(false);
        }
        AppUtils.saveUserObject(this, userModel);
        uploadDetailsToServer(null);
    }

    private void setAllUpdateFalse() {


        user.setUpdateAccommodation(false);
        user.setUpdateCurrentAddress(false);
        user.setUpdatePermanentAddress(false);
        user.setGpaValueUpdate(false);
        user.setGpaTypeUpdate(false);

        user.setUpdateFamilyMemberType1(false);
        user.setUpdateDesignationFamilyMemberType1(false);
        user.setUpdatePhoneFamilyMemberType1(false);
        user.setUpdatePreferredLanguageFamilyMemberType1(false);

        user.setUpdateBankAccNum(false);
        user.setUpdateBankIfsc(false);
        user.setUpdateClassmateName(false);
        user.setUpdateClassmateName(false);
        user.setUpdateVerificationDate(false);
        user.setUpdateStudentLoan(false);
    }


    private boolean checkIncompleteStep1() {
        try {
            ProfileFormStep2Fragment1 profileFormStep2Fragment1 = (ProfileFormStep2Fragment1) mPagerAdapter.getFragment(0);
            profileFormStep2Fragment1.checkIncomplete();
        } catch (Exception e) {
        }

        return showHideIncompleteStep1();
    }

    private boolean showHideIncompleteStep1() {
        if (user.isIncompleteStudentLoan() || user.isIncompleteGpa() || user.isIncompleteMarksheets()) {

            incompleteStep1.setVisibility(View.VISIBLE);
            return true;
        }
        incompleteStep1.setVisibility(View.GONE);
        return false;
    }

    private boolean checkIncompleteStep2() {
        try {
            ProfileFormStep2Fragment2 profileFormStep2Fragment2 = (ProfileFormStep2Fragment2) mPagerAdapter.getFragment(1);
            profileFormStep2Fragment2.checkIncomplete();
        } catch (Exception e) {
        }
        return showHideIncompleteStep2();
    }


    private boolean showHideIncompleteStep2() {
        if (user.isIncompleteFamilyDetails()) {
            incompleteStep2.setVisibility(View.VISIBLE);
            return true;
        }
        incompleteStep2.setVisibility(View.GONE);
        return false;
    }

    private boolean checkIncompleteStep4() {
        try {
            ProfileFormStep2Fragment4 profileFormStep2Fragment4 = (ProfileFormStep2Fragment4) mPagerAdapter.getFragment(3);
            profileFormStep2Fragment4.checkIncomplete();
        } catch (Exception e) {
        }
        return showHideIncompleteStep4();
    }

    private boolean showHideIncompleteStep4() {

        boolean isIncompleteBankStmnt = false;
        try {
            boolean isTakenStudentLoan = Boolean.parseBoolean(user.getStudentLoan());
            if (isTakenStudentLoan) {
                isIncompleteBankStmnt = user.isIncompleteBankStmt();
            }
        } catch (Exception e) {
        }
        if (user.isIncompleteRepaymentSetup() || isIncompleteBankStmnt) {
            incompleteStep4.setVisibility(View.VISIBLE);
            return true;
        }
        incompleteStep4.setVisibility(View.GONE);
        return false;
    }


    private boolean checkIncompleteStep3() {
        try {
            ProfileFormStep2Fragment3 profileFormStep2Fragment3 = (ProfileFormStep2Fragment3) mPagerAdapter.getFragment(2);
            profileFormStep2Fragment3.checkIncomplete();
        } catch (Exception e) {
        }
        return showHideIncompleteStep3();
    }

    private boolean showHideIncompleteStep3() {
        if (user.isIncompleteRollNumber() || user.isIncompleteVerificationDate() || user.isIncompleteClassmateDetails()
                ) {
            incompleteStep3.setVisibility(View.VISIBLE);
            return true;
        }
        incompleteStep3.setVisibility(View.GONE);
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
    }


    private void setIncomplete() {

        UserModel userModel = AppUtils.getUserObject(this);

        userModel.setIncompleteAddressDetails(user.isIncompleteAddressDetails());

        userModel.setIncompleteFamilyDetails(user.isIncompleteFamilyDetails());
        userModel.setIncompleteGpa(user.isIncompleteGpa());
        userModel.setIncompleteRepaymentSetup(user.isIncompleteRepaymentSetup());
        userModel.setIncompleteClassmateDetails(user.isIncompleteClassmateDetails());
        userModel.setIncompleteClassmateDetails(user.isIncompleteClassmateDetails());
        userModel.setIncompleteVerificationDate(user.isIncompleteVerificationDate());
        userModel.setIncompleteStudentLoan(user.isIncompleteStudentLoan());

        userModel.setIncompleteMarksheets(user.isIncompleteMarksheets());
        AppUtils.saveUserObject(this, userModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.sendGoogleAnalytics((BuddyApplication) getApplication());
    }

    @Override
    public void onBackPressed() {
        onPageSelected(mPager.getCurrentItem());
        super.onBackPressed();

        //if (mPager.getCurrentItem() == 0) {
        //    super.onBackPressed();
        //} else {
        //    mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        //}
    }

    private void uploadDetailsToServer(String isLastStep) {
        CheckInternetAndUploadUserDetails ciauo = new CheckInternetAndUploadUserDetails(ProfileFormStep2.this, isLastStep);
    }

    private void hideShowUpArrow(int i) {
        if (!user.isAppliedFor7k())
            previous.setVisibility(View.VISIBLE);
        findViewById(R.id.up_arrow_1).setVisibility(View.GONE);
        findViewById(R.id.up_arrow_2).setVisibility(View.GONE);
        findViewById(R.id.up_arrow_3).setVisibility(View.GONE);
        findViewById(R.id.up_arrow_4).setVisibility(View.GONE);
        setImage(i);
    }

    private void setImage(int i) {
        int image = 0;
        boolean isGirl = user.getGender() != null && "girl".equals(user.getGender());

        if (i == 0) {
            previous.setVisibility(View.GONE);
            findViewById(R.id.up_arrow_1).setVisibility(View.VISIBLE);
            if (isGirl) {
                image = R.drawable.step2fragment1girl;
            } else {
                image = R.drawable.step2fragment1;
            }
        } else if (i == 1) {
            if (isGirl) {
                image = R.drawable.step2fragment2girl;
            } else {
                image = R.drawable.step2fragment2;
            }
            findViewById(R.id.up_arrow_2).setVisibility(View.VISIBLE);
        } else if (i == 2) {
            if (isGirl) {
                image = R.drawable.step2fragment3girl;
            } else {
                image = R.drawable.step2fragment3;
            }
            findViewById(R.id.up_arrow_3).setVisibility(View.VISIBLE);
        } else if (i == 3) {
            if (isGirl) {
                image = R.drawable.step2fragment4girl;
            } else {
                image = R.drawable.step2fragment4;
            }
            findViewById(R.id.up_arrow_4).setVisibility(View.VISIBLE);
        }
        genderImage.setImageDrawable(getResources().getDrawable(image));

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (!user.isAppliedFor7k()) {
            if (previousPosition == 1) {

                checkIncompleteStep2();
                saveStep2Data();
            } else if (previousPosition == 2) {

                checkIncompleteStep3();
                saveStep3Data();
            } else if (previousPosition == 3) {

                checkIncompleteStep4();
                saveStep4Data(null);
                uploadDetailsToServer(null);

            } else if (previousPosition == 0) {

                checkIncompleteStep1();
                saveStep1Data();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void notifyUploadData(final ResponseModel responseModel, final String isLastStep, final Activity activity) {

        //if (isActive) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog != null && progressDialog.isShowing() && Constants.YES_7k.equals(isLastStep)) {
                    progressDialog.hide();
                    if (responseModel != null && responseModel.getData() != null && responseModel.getData().isAppliedFor7k()) {

                        Intent intent1 = new Intent(activity, Pending7kApprovalActivity.class);
                        startActivity(intent1);
                        finish();
                    } else if (responseModel != null) {
                        openDialogBox();

                    }
                }
                if (responseModel != null) {
                    if (responseModel.getErrors() != null && responseModel.getErrors().size() > 0) {
                        UserModel userModel = AppUtils.getUserObject(activity);
                        for (Error error : responseModel.getErrors()) {
                            if (error.getField().equalsIgnoreCase("familyMember")) {
                                ProfileFormStep2Fragment2 profileFormStep2Fragment2 = (ProfileFormStep2Fragment2) mPagerAdapter.getFragment(1);
                                profileFormStep2Fragment2.showFamilyMemberError(error);
                                userModel.setPhoneFamilyMemberType1(null);
                                incompleteStep2.setVisibility(View.VISIBLE);
                            } else if (error.getField().equalsIgnoreCase("bankAccountNumber")) {
                                ProfileFormStep2Fragment4 profileFormStep2Fragment4 = (ProfileFormStep2Fragment4) mPagerAdapter.getFragment(3);
                                profileFormStep2Fragment4.showDuplicateBankAccountNumber(error);
                                userModel.setBankAccNum(null);
                                userModel.setBankIfsc(null);
                                incompleteStep4.setVisibility(View.VISIBLE);
                            } else if (error.getField().equalsIgnoreCase("rollNumber")) {
                                ProfileFormStep2Fragment3 profileFormStep2Fragment3 = (ProfileFormStep2Fragment3) mPagerAdapter.getFragment(2);
                                profileFormStep2Fragment3.showErrorRollNumber(error);
                                userModel.setRollNumber(null);
                                incompleteStep3.setVisibility(View.VISIBLE);
                            }
                        }
                        AppUtils.saveUserObject(activity, userModel);
                    }
                } else {
                    Toast.makeText(ProfileFormStep2.this, "Error connecting to server", Toast.LENGTH_SHORT).show();

                }
            }
        });
        //}

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
}
