package indwin.c3.shareapp.activities;

import android.app.Application;
import android.app.Dialog;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.adapters.ScreenSlidePagerAdapter;
import indwin.c3.shareapp.application.BuddyApplication;
import indwin.c3.shareapp.fragments.ProfileFormStep2Fragment1;
import indwin.c3.shareapp.fragments.ProfileFormStep2Fragment2;
import indwin.c3.shareapp.fragments.ProfileFormStep2Fragment3;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.CheckInternetAndUploadUserDetails;
import io.intercom.android.sdk.Intercom;

public class ProfileFormStep2 extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private ArrayList<Fragment> fragments;
    private TextView gotoFragment1, gotoFragment2, gotoFragment3;
    private Button saveAndProceed, previous;
    private UserModel user;
    private ImageView genderImage;
    private ImageView incompleteStep1, incompleteStep2, incompleteStep3;
    int previousPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form_step2);
        getAllViews();
        setCLickListener();
        user = AppUtils.getUserObject(this);
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

        showHideIncompleteStep1();
        showHideIncompleteStep2();
        showHideIncompleteStep3();
    }


    private void populateFragments() {
        fragments = new ArrayList<>();
        fragments.add(new ProfileFormStep2Fragment1());
        fragments.add(new ProfileFormStep2Fragment2());
        fragments.add(new ProfileFormStep2Fragment3());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    private void setCLickListener() {
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

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
        saveAndProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPage = mPager.getCurrentItem();
                //if (currentPage == 0) {
                //    saveStep1Data();
                //} else if (currentPage == 1) {
                //    saveStep2Data();
                //}
                //if (currentPage == 2) {
                //    saveStep3Data();
                //}
                if (currentPage != (fragments.size() - 1)) {
                    Intent intent = new Intent(ProfileFormStep2.this, CheckInternetAndUploadUserDetails.class);
                    sendBroadcast(intent);

                    mPager.setCurrentItem(currentPage + 1);
                } else {
                    boolean incompleteStep3 = checkIncompleteStep3();
                    saveStep3Data();
                    if (checkIncompleteStep1() || checkIncompleteStep2() || incompleteStep3) {
                        final Dialog dialog1 = new Dialog(ProfileFormStep2.this);
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.setContentView(R.layout.incomplete_alert_box);

                        Button okay = (Button) dialog1.findViewById(R.id.okay_button);
                        okay.setTextColor(Color.parseColor("#44c2a6"));
                        okay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ProfileFormStep2.this, CheckInternetAndUploadUserDetails.class);
                                sendBroadcast(intent);
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
                        return;
                    } else {
                        Intent intentDataUpload = new Intent(ProfileFormStep2.this, CheckInternetAndUploadUserDetails.class);
                        sendBroadcast(intentDataUpload);
                        Intent intent1 = new Intent(ProfileFormStep2.this, Pending7kApprovalActivity.class);
                        startActivity(intent1);
                        finish();
                    }
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        setIncomplete();
    }

    private void saveStep1Data() {
        checkIncompleteStep1();
        UserModel userModel = AppUtils.getUserObject(this);
        if (AppUtils.isNotEmpty(user.getDob()) && user.isUpdateDOB()) {
            userModel.setDob(user.getDob());
            userModel.setUpdateDOB(true);
            user.setUpdateDOB(false);
        }

        if (AppUtils.isNotEmpty(user.getAccommodation()) && user.isUpdateAccommodation()) {
            userModel.setAccommodation(user.getAccommodation());
            userModel.setUpdateAccommodation(true);
            user.setUpdateAccommodation(false);
        }
        if (AppUtils.isNotEmpty(user.getCurrentAddress()) && user.isUpdateCurrentAddress()) {
            userModel.setCurrentAddress(user.getCurrentAddress());
            userModel.setUpdateCurrentAddress(true);
            user.setUpdateCurrentAddress(false);
        }
        if (AppUtils.isNotEmpty(user.getPermanentAddress()) && user.isUpdatePermanentAddress()) {
            userModel.setPermanentAddress(user.getPermanentAddress());
            userModel.setUpdatePermanentAddress(true);
            user.setUpdatePermanentAddress(false);
        }

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


        AppUtils.saveUserObject(this, userModel);
    }

    private void saveStep2Data() {
        UserModel userModel = AppUtils.getUserObject(this);
        if (AppUtils.isNotEmpty(user.getFamilyMemberType1()) && user.isUpdateFamilyMemberType1()) {
            userModel.setFamilyMemberType1(user.getFamilyMemberType1());
            userModel.setUpdateFamilyMemberType1(true);
        }
        if (AppUtils.isNotEmpty(user.getProfessionFamilyMemberType1()) && user.isUpdateProfessionFamilyMemberType1()) {
            userModel.setProfessionFamilyMemberType1(user.getProfessionFamilyMemberType1());
            userModel.setUpdateProfessionFamilyMemberType1(true);
        }
        if (AppUtils.isNotEmpty(user.getPhoneFamilyMemberType1()) && user.isUpdatePhoneFamilyMemberType1()) {
            userModel.setPhoneFamilyMemberType1(user.getPhoneFamilyMemberType1());
            userModel.setUpdatePhoneFamilyMemberType1(true);
        }
        if (AppUtils.isNotEmpty(user.getPrefferedLanguageFamilyMemberType1()) && user.isUpdatePreferredLanguageFamilyMemberType1()) {
            userModel.setPrefferedLanguageFamilyMemberType1(user.getPrefferedLanguageFamilyMemberType1());
            userModel.setUpdatePreferredLanguageFamilyMemberType1(true);
        }
        AppUtils.saveUserObject(this, userModel);

    }

    private void saveStep3Data() {
        UserModel userModel = AppUtils.getUserObject(this);

        if (AppUtils.isNotEmpty(user.getBankAccNum()) && user.isUpdateBankAccNum()) {
            userModel.setBankAccNum(user.getBankAccNum());
            userModel.setUpdateBankAccNum(true);
        }
        if (AppUtils.isNotEmpty(user.getBankIfsc()) && user.isUpdateBankIfsc()) {
            userModel.setBankIfsc(user.getBankIfsc());
            userModel.setUpdateBankIfsc(true);
        }
        if (AppUtils.isNotEmpty(user.getClassmateName()) && user.isUpdateClassmateName()) {
            userModel.setClassmateName(user.getClassmateName());
            userModel.setUpdateClassmateName(true);
        }
        if (AppUtils.isNotEmpty(user.getClassmatePhone()) && user.isUpdateClassmatePhone()) {
            userModel.setClassmatePhone(user.getClassmatePhone());
            userModel.setUpdateClassmatePhone(true);
        }
        if (AppUtils.isNotEmpty(user.getStudentLoan()) && user.isUpdateStudentLoan()) {
            userModel.setStudentLoan(user.getStudentLoan());
            userModel.setUpdateStudentLoan(true);
        }
        if (AppUtils.isNotEmpty(user.getVerificationDate()) && user.isUpdateVerificationDate()) {
            userModel.setVerificationDate(user.getVerificationDate());
            userModel.setUpdateVerificationDate(true);
        }
        AppUtils.saveUserObject(this, userModel);
    }

    private void setAllUpdateFalse() {

        user.setUpdateDOB(false);
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
        ProfileFormStep2Fragment1 profileFormStep2Fragment1 = (ProfileFormStep2Fragment1) mPagerAdapter.getRegisteredFragment(0);
        profileFormStep2Fragment1.checkIncomplete();

        return showHideIncompleteStep1();
    }

    private boolean showHideIncompleteStep1() {
        if (user.isIncompleteDOB() || user.isIncompleteAddressDetails()) {

            incompleteStep1.setVisibility(View.VISIBLE);
            return true;
        }
        incompleteStep1.setVisibility(View.GONE);
        return false;
    }

    private boolean checkIncompleteStep2() {
        ProfileFormStep2Fragment2 profileFormStep2Fragment2 = (ProfileFormStep2Fragment2) mPagerAdapter.getRegisteredFragment(1);
        profileFormStep2Fragment2.checkIncomplete();
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

    private boolean checkIncompleteStep3() {
        ProfileFormStep2Fragment3 profileFormStep2Fragment3 = (ProfileFormStep2Fragment3) mPagerAdapter.getRegisteredFragment(2);
        profileFormStep2Fragment3.checkIncomplete();
        return showHideIncompleteStep3();
    }

    private boolean showHideIncompleteStep3() {
        if (user.isIncompleteRepaymentSetup() || user.isIncompleteClassmateDetails()
                || user.isIncompleteVerificationDate() || user.isIncompleteStudentLoan()) {
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
        incompleteStep1 = (ImageView) findViewById(R.id.incomplete_step_1);
        incompleteStep2 = (ImageView) findViewById(R.id.incomplete_step_2);
        incompleteStep3 = (ImageView) findViewById(R.id.incomplete_step_3);

        saveAndProceed = (Button) findViewById(R.id.save_and_proceed);
        previous = (Button) findViewById(R.id.previous);
        genderImage = (ImageView) findViewById(R.id.verify_image_view2);
    }


    private void setIncomplete() {

        UserModel userModel = AppUtils.getUserObject(this);

        userModel.setIncompleteAddressDetails(user.isIncompleteAddressDetails());
        userModel.setIncompleteDOB(user.isIncompleteDOB());

        userModel.setIncompleteFamilyDetails(user.isIncompleteFamilyDetails());

        userModel.setIncompleteRepaymentSetup(user.isIncompleteRepaymentSetup());
        userModel.setIncompleteClassmateDetails(user.isIncompleteClassmateDetails());
        userModel.setIncompleteClassmateDetails(user.isIncompleteClassmateDetails());
        userModel.setIncompleteVerificationDate(user.isIncompleteVerificationDate());
        userModel.setIncompleteStudentLoan(user.isIncompleteStudentLoan());

        AppUtils.saveUserObject(this, userModel);
        //step2


        //step3

        userModel.setIncompleteAnnualFees(user.isIncompleteAnnualFees());
        userModel.setIncompleteScholarship(user.isIncompleteScholarship());

        userModel.setIncompleteMonthlyExpenditure(user.isIncompleteMonthlyExpenditure());
        userModel.setIncompleteVehicleDetails(user.isIncompleteVehicleDetails());

        userModel.setIncompleteBankStmt(user.isIncompleteBankStmt());


    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.sendGoogleAnalytics((BuddyApplication) getApplication());
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private void hideShowUpArrow(int i) {
        if (!user.isAppliedFor7k())
            previous.setVisibility(View.VISIBLE);
        findViewById(R.id.up_arrow_1).setVisibility(View.GONE);
        findViewById(R.id.up_arrow_2).setVisibility(View.GONE);
        findViewById(R.id.up_arrow_3).setVisibility(View.GONE);
        int image = 0;
        boolean isGirl = user.getGender() != null && "girl".equals(user.getGender());

        if (i == 0) {
            previous.setVisibility(View.GONE);
            findViewById(R.id.up_arrow_1).setVisibility(View.VISIBLE);
            if (isGirl) {
                image = R.mipmap.step2fragment1girl;
            } else {
                image = R.mipmap.step2fragment1;
            }
        } else if (i == 1) {
            if (isGirl) {
                image = R.mipmap.step2fragment2girl;
            } else {
                image = R.mipmap.step2fragment2;
            }
            findViewById(R.id.up_arrow_2).setVisibility(View.VISIBLE);
        } else if (i == 2) {
            if (isGirl) {
                image = R.mipmap.step2fragment3girl;
            } else {
                image = R.mipmap.step2fragment3;
            }
            findViewById(R.id.up_arrow_3).setVisibility(View.VISIBLE);
        }
        Picasso.with(this).load(image).into(genderImage);
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


}
