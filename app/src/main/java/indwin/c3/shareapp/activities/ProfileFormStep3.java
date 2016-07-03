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
import indwin.c3.shareapp.fragments.ProfileFormStep3Fragment1;
import indwin.c3.shareapp.fragments.ProfileFormStep3Fragment2;
import indwin.c3.shareapp.fragments.ProfileFormStep3Fragment3;
import indwin.c3.shareapp.models.ResponseModel;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.CheckInternetAndUploadUserDetails;
import indwin.c3.shareapp.utils.Constants;
import io.intercom.android.sdk.Intercom;

public class ProfileFormStep3 extends AppCompatActivity implements ViewPager.OnPageChangeListener, CheckInternetAndUploadUserDetails.NotifyProgress {
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private ArrayList<Fragment> fragments;
    private TextView gotoFragment1, gotoFragment2, gotoFragment3;
    private Button saveAndProceed, previous;
    private UserModel user;
    private ImageView genderImage;
    private ImageView incompleteStep1, incompleteStep2, incompleteStep3;
    int previousPosition;
    boolean takenStudentLoan;
    private ProgressDialog progressDialog;
    private static boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form_step3);
        getAllViews();
        setCLickListener();

        findViewById(R.id.fragment4).setVisibility(View.GONE);
        user = AppUtils.getUserObject(this);
        setImage(0);
        if (user.isAppliedFor60k()) {
            saveAndProceed.setVisibility(View.INVISIBLE);
            previous.setVisibility(View.INVISIBLE);
            findViewById(R.id.details_submitted_tv).setVisibility(View.VISIBLE);
        }
        setAllUpdateFalse();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView headerTitle = (TextView) findViewById(R.id.activity_header);
        headerTitle.setText("Apply for more Credit");
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
        if (!user.isAppliedFor60k()) {
            showHideIncompleteStep1();
            showHideIncompleteStep2();
            showHideIncompleteStep3();
        }
    }

    private void populateFragments() {
        fragments = new ArrayList<>();
        fragments.add(new ProfileFormStep3Fragment1());
        fragments.add(new ProfileFormStep3Fragment2());
        takenStudentLoan = false;

        try {
            takenStudentLoan = Boolean.parseBoolean(user.getStudentLoan());
        } catch (Exception e) {
        }
        if (!takenStudentLoan) {
            fragments.add(new ProfileFormStep3Fragment3());
        } else {
            gotoFragment3.setVisibility(View.GONE);
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
        saveAndProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPage = mPager.getCurrentItem();

                if (currentPage != (fragments.size() - 1)) {
                    mPager.setCurrentItem(currentPage + 1);
                    uploadDetailsToServer();
                } else {
                    boolean incompleteStep3 = false;
                    boolean incompleteStep2 = false;
                    if (!takenStudentLoan) {
                        incompleteStep2 = checkIncompleteStep2();
                        incompleteStep3 = checkIncompleteStep3();
                        saveStep3Data();
                    } else {
                        incompleteStep2 = checkIncompleteStep2();
                        saveStep2Data();
                    }
                    if (checkIncompleteStep1() || incompleteStep2 || incompleteStep3) {
                        openDialogBox();
                        return;
                    } else {
                        SharedPreferences mPrefs = getSharedPreferences("buddy", Context.MODE_PRIVATE);
                        mPrefs.edit().putBoolean("updatingDB", false).apply();
                        CheckInternetAndUploadUserDetails ciauo = new CheckInternetAndUploadUserDetails(ProfileFormStep3.this, Constants.YES_60k);
                        progressDialog = new ProgressDialog(ProfileFormStep3.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Submitting your details..");
                        progressDialog.show();
                    }
                }
            }
        });
    }

    private void openDialogBox() {

        final Dialog dialog1 = new Dialog(ProfileFormStep3.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.incomplete_alert_box);

        Button okay = (Button) dialog1.findViewById(R.id.okay_button);
        okay.setTextColor(Color.parseColor("#44c2a6"));
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDetailsToServer();
                dialog1.dismiss();
                Intent intent2 = new Intent(ProfileFormStep3.this, ProfileActivity.class);
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

    private void uploadDetailsToServer() {
        CheckInternetAndUploadUserDetails ciauo = new CheckInternetAndUploadUserDetails(ProfileFormStep3.this, null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        setIncomplete();

    }

    private void saveStep1Data() {
        UserModel userModel = AppUtils.getUserObject(this);
        if (AppUtils.isNotEmpty(user.getAnnualFees()) && user.isUpdateAnnualFees()) {
            userModel.setAnnualFees(user.getAnnualFees());
            userModel.setUpdateAnnualFees(true);
            user.setUpdateAnnualFees(false);
        }


        if (AppUtils.isNotEmpty(user.getScholarship()) && user.isUpdateScholarship()) {
            userModel.setScholarship(user.getScholarship());
            userModel.setUpdateScholarship(true);
            user.setUpdateScholarship(false);
        }
        if (AppUtils.isNotEmpty(user.getScholarshipType()) && user.isUpdateScholarshipType()) {
            userModel.setScholarshipType(user.getScholarshipType());
            userModel.setUpdateScholarshipType(true);
            user.setUpdateScholarshipType(false);
        }

        if (AppUtils.isNotEmpty(user.getScholarshipAmount()) && user.isUpdateScholarshipAmount()) {
            userModel.setScholarshipAmount(user.getScholarshipAmount());
            userModel.setUpdateScholarshipAmount(true);
            user.setUpdateScholarshipAmount(false);
        }


        AppUtils.saveUserObject(this, userModel);
        uploadDetailsToServer();
    }

    private void saveStep2Data() {
        UserModel userModel = AppUtils.getUserObject(this);
        if (AppUtils.isNotEmpty(user.getMonthlyExpenditure()) && user.isUpdateMonthlyExpenditure()) {
            userModel.setMonthlyExpenditure(user.getMonthlyExpenditure());
            userModel.setUpdateMonthlyExpenditure(true);
            user.setUpdateMonthlyExpenditure(false);
        }

        if (AppUtils.isNotEmpty(user.getVehicle()) && user.isUpdateVehicle()) {
            userModel.setVehicle(user.getVehicle());
            userModel.setUpdateVehicle(true);
            user.setUpdateVehicle(false);
        }
        if (AppUtils.isNotEmpty(user.getVehicleType()) && user.isUpdateVehicleType()) {
            userModel.setVehicleType(user.getVehicleType());
            userModel.setUpdateVehicleType(true);
            user.setUpdateVehicleType(false);
        }

        AppUtils.saveUserObject(this, userModel);
        uploadDetailsToServer();

    }

    private void saveStep3Data() {
        uploadDetailsToServer();

    }

    private void setAllUpdateFalse() {
        user.setUpdateAnnualFees(false);
        user.setUpdateScholarship(false);
        user.setUpdateScholarshipType(false);
        user.setUpdateScholarshipAmount(false);

        user.setUpdateMonthlyExpenditure(false);
        user.setUpdateVehicle(false);
        user.setUpdateVehicleType(false);

        user.setUpdateNewBankStmts(false);

    }


    private boolean checkIncompleteStep1() {
        try {
            ProfileFormStep3Fragment1 profileFormStep3Fragment1 = (ProfileFormStep3Fragment1) mPagerAdapter.getFragment(0);
            profileFormStep3Fragment1.checkIncomplete();
        } catch (Exception e) {
        }

        return showHideIncompleteStep1();
    }

    private boolean showHideIncompleteStep1() {
        if (user.isIncompleteAnnualFees() || user.isIncompleteScholarship()) {

            incompleteStep1.setVisibility(View.VISIBLE);
            return true;
        }
        incompleteStep1.setVisibility(View.GONE);
        return false;
    }

    private boolean checkIncompleteStep2() {
        try {
            ProfileFormStep3Fragment2 profileFormStep3Fragment2 = (ProfileFormStep3Fragment2) mPagerAdapter.getFragment(1);
            profileFormStep3Fragment2.checkIncomplete();
        } catch (Exception e) {
        }
        return showHideIncompleteStep2();
    }


    private boolean showHideIncompleteStep2() {
        if (user.isIncompleteMonthlyExpenditure() || user.isIncompleteVehicleDetails()) {
            incompleteStep2.setVisibility(View.VISIBLE);
            return true;
        }
        incompleteStep2.setVisibility(View.GONE);
        return false;
    }

    private boolean checkIncompleteStep3() {
        try {
            ProfileFormStep3Fragment3 profileFormStep3Fragment3 = (ProfileFormStep3Fragment3) mPagerAdapter.getFragment(2);
            profileFormStep3Fragment3.checkIncomplete();
        } catch (Exception e) {
        }
        return showHideIncompleteStep3();
    }

    private boolean showHideIncompleteStep3() {
        if (user.isIncompleteBankStmt()) {
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
        userModel.setIncompleteAnnualFees(user.isIncompleteAnnualFees());
        userModel.setIncompleteScholarship(user.isIncompleteScholarship());
        userModel.setIncompleteMonthlyExpenditure(user.isIncompleteMonthlyExpenditure());
        userModel.setIncompleteVehicleDetails(user.isIncompleteVehicleDetails());
        userModel.setIncompleteBankStmt(user.isIncompleteBankStmt());
        AppUtils.saveUserObject(this, userModel);

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.sendGoogleAnalytics((BuddyApplication) getApplication());
    }


    private void previousPage() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onBackPressed() {
        onPageSelected(mPager.getCurrentItem());
        super.onBackPressed();
    }

    private void hideShowUpArrow(int i) {
        if (!user.isAppliedFor60k())
            previous.setVisibility(View.VISIBLE);
        findViewById(R.id.up_arrow_1).setVisibility(View.GONE);
        findViewById(R.id.up_arrow_2).setVisibility(View.GONE);
        findViewById(R.id.up_arrow_3).setVisibility(View.GONE);
        setImage(i);
    }

    private void setImage(int i) {

        int image = 0;
        boolean isGirl = user.getGender() != null && "girl".equals(user.getGender());

        if (i == 0) {
            previous.setVisibility(View.GONE);
            findViewById(R.id.up_arrow_1).setVisibility(View.VISIBLE);
            if (isGirl) {
                image = R.drawable.step3fragment1girl;
            } else {
                image = R.drawable.step3fragment1;
            }
        } else if (i == 1) {
            if (isGirl) {
                image = R.drawable.step3fragment2girl;
            } else {
                image = R.drawable.step3fragment2;
            }
            findViewById(R.id.up_arrow_2).setVisibility(View.VISIBLE);
        } else if (i == 2) {
            if (isGirl) {
                image = R.drawable.step3fragment3girl;
            } else {
                image = R.drawable.step3fragment3;
            }
            findViewById(R.id.up_arrow_3).setVisibility(View.VISIBLE);
        }
        genderImage.setImageDrawable(getResources().getDrawable(image));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (!user.isAppliedFor60k()) {
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


    @Override
    public void notifyUploadData(final ResponseModel responseModel, final String isLastStep, final Activity activity) {
        if (isActive) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (progressDialog != null && progressDialog.isShowing() && Constants.YES_60k.equals(isLastStep)) {
                        progressDialog.hide();
                        if (responseModel != null && responseModel.getData() != null && responseModel.getData().isAppliedFor60k()) {
                            Intent intent1 = new Intent(activity, Pending60kApprovalActivity.class);
                            startActivity(intent1);
                            finish();
                        } else if (responseModel != null) {
                            openDialogBox();
                        } else {
                            Toast.makeText(ProfileFormStep3.this, "Error connecting to server", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });
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
}
