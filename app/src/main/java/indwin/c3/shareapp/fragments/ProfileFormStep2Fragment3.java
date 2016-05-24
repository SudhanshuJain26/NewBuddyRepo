package indwin.c3.shareapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.Views.DatePicker;
import indwin.c3.shareapp.activities.Pending7kApprovalActivity;
import indwin.c3.shareapp.activities.ProfileActivity;
import indwin.c3.shareapp.activities.SetupAutoRepayments;
import indwin.c3.shareapp.adapters.SpinnerHintAdapter;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.CheckInternetAndUploadUserDetails;
import indwin.c3.shareapp.utils.HelpTipDialog;
import indwin.c3.shareapp.utils.ValidationUtils;
import io.intercom.com.google.gson.Gson;

/**
 * Created by shubhang on 05/04/16.
 */
public class ProfileFormStep2Fragment3 extends Fragment implements View.OnFocusChangeListener {
    private SharedPreferences mPrefs;
    private UserModel user;
    private Button saveAndProceed, previous;
    private Gson gson;
    private TextView gotoFragment1, gotoFragment3, gotoFragment2;
    private final int top = 16, left = 16, right = 16, bottom = 16;
    ImageView incompleteStep1, incompleteStep2, incompleteStep3;
    static EditText verificationDateEditText, classmateName, classmatePhone;
    private static DatePicker datePicker;
    public static boolean updateUserVerificationDate = false;
    public static Button setupAutoRepayments;
    public static TextView bankAccNum;
    private ImageView incompleteClassmate, completeClassmate,
            incompleteVerificationDate, completeVerifcationDate;
    public static ImageButton changeAccNum;
    private TextView incorrectPhone;
    public static ImageView incompleteSetupRepayments, completeSetupRepayments;
    ImageView topImage;
    private ImageView incompleteStudentLoan, completeStudentLoan;
    boolean selectedStudentLoan = false;
    private ImageButton autoRepayHelptip, classmateHelptip, verificationHelptip;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.profile_form_step2_fragment3, container, false);
        RecyclerView rvImages = (RecyclerView) rootView.findViewById(R.id.rvImages);
        mPrefs = getActivity().getSharedPreferences("buddy", Context.MODE_PRIVATE);
        mPrefs.edit().putBoolean("visitedFormStep2Fragment3", true).apply();
        gson = new Gson();
        user = AppUtils.getUserObject(getActivity());

        getAllViews(rootView);

        if (!mPrefs.getBoolean("step2Editable", true)) {
            ProfileFormStep1Fragment1.setViewAndChildrenEnabled(rootView, false, gotoFragment1, gotoFragment2);
        }
        setAllHelpTipsEnabled();
        if (!user.isAppliedFor7k() && AppUtils.isNotEmpty(user.getBankAccNum()) && AppUtils.isNotEmpty(user.getBankIfsc())) {
            changeAccNum.setVisibility(View.VISIBLE);
        } else {
            changeAccNum.setVisibility(View.GONE);
        }
        if (mPrefs.getBoolean("visitedFormStep2Fragment2", false)) {
            gotoFragment2.setAlpha(1);
            gotoFragment2.setClickable(true);
        }
        if (mPrefs.getBoolean("visitedFormStep2Fragment1", false)) {
            gotoFragment3.setAlpha(1);
            gotoFragment3.setClickable(true);
        }
        if (user.getGender() != null && "girl".equals(user.getGender())) {
            Picasso.with(getActivity())
                    .load(R.mipmap.step2fragment3girl)
                    .into(topImage);
        }


        if (AppUtils.isEmpty(user.getStudentLoan())) {
            incompleteStudentLoan.setVisibility(View.VISIBLE);
            completeStudentLoan.setVisibility(View.GONE);
        } else {
            completeStudentLoan.setVisibility(View.VISIBLE);
            incompleteStudentLoan.setVisibility(View.GONE);
        }
        final String scholarship[] = getResources().getStringArray(R.array.scholarship);
        final String scholarshipValues[] = getResources().getStringArray(R.array.scholarship_values);

        Spinner studentLoanSpinner = (Spinner) rootView.findViewById(R.id.student_loan);
        SpinnerHintAdapter studentLoanAdapter = new SpinnerHintAdapter(getActivity(), scholarship, R.layout.spinner_item_underline);
        studentLoanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        studentLoanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < scholarship.length - 1) {
                    selectedStudentLoan = true;
                    user.setStudentLoan(scholarshipValues[position]);
                    user.setUpdateStudentLoan(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setUpdateStudentLoan(false);
            }
        });
        studentLoanSpinner.setAdapter(studentLoanAdapter);
        studentLoanSpinner.setSelection(studentLoanAdapter.getCount());
        if (user.isAppliedFor7k()) {
            previous.setVisibility(View.INVISIBLE);
            saveAndProceed.setVisibility(View.INVISIBLE);
            rootView.findViewById(R.id.details_submitted_tv).setVisibility(View.VISIBLE);
        }


        if (user.getStudentLoan() != null || "".equals(user.getStudentLoan())) {
            for (int i = 0; i < scholarship.length - 1; i++) {
                if (user.getStudentLoan().equals(scholarshipValues[i])) {
                    studentLoanSpinner.setSelection(i);
                    completeStudentLoan.setVisibility(View.VISIBLE);
                    user.setIncompleteStudentLoan(false);
                    break;
                }
            }
        }
        if (user.getBankAccNum() != null && !"".equals(user.getBankAccNum())) {
            try {
                //                String password = "bf5cbe23fd8e60697c8ddc2ef25af796";
                //                String iv = "bf5cbe23fd8e6069";
                //                int keyLength = 256;
                //                byte[] keyBytes = new byte[keyLength / 8];
                //                Arrays.fill(keyBytes, (byte) 0x0);
                //                byte[] passwordBytes = password.getBytes("UTF-8");
                //                int length = passwordBytes.length < keyBytes.length ? passwordBytes.length
                //                        : keyBytes.length;
                //                System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
                //                String message = new String(AESCrypt.decrypt(new SecretKeySpec(keyBytes, "AES"), iv.getBytes("UTF-8"),
                //                        user.getBankAccNum().getBytes()), "UTF-8");
                String mask = user.getBankAccNum().replaceAll("\\w(?=\\w{4})", "*");
                setupAutoRepayments.setVisibility(View.GONE);
                bankAccNum.setText(mask);
                bankAccNum.setVisibility(View.VISIBLE);
                //                changeAccNum.setVisibility(View.VISIBLE);
                completeSetupRepayments.setVisibility(View.VISIBLE);
                user.setIncompleteRepaymentSetup(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (user.getClassmateName() != null && !"".equals(user.getClassmateName())
                && user.getClassmatePhone() != null && !"".equals(user.getClassmatePhone())) {
            completeClassmate.setVisibility(View.VISIBLE);
            classmateName.setText(user.getClassmateName());
            classmatePhone.setText(user.getClassmatePhone());
            user.setIncompleteClassmateDetails(false);
        } else {
            if (!"".equals(user.getClassmateName())) {
                classmateName.setText(user.getClassmateName());
            } else if (!"".equals(user.getClassmatePhone())) {
                classmatePhone.setText(user.getClassmatePhone());
            }
        }
        if (AppUtils.isNotEmpty(user.getVerificationDate())) {


            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = null;
            try {
                newDate = spf.parse(user.getVerificationDate());
                spf = new SimpleDateFormat("dd MMM yyyy");

                verificationDateEditText.setText(spf.format(newDate));
                completeVerifcationDate.setVisibility(View.VISIBLE);
                user.setIncompleteVerificationDate(false);
            } catch (ParseException e) {
                e.printStackTrace();
                try {
                    spf = new SimpleDateFormat("dd MMM yyyy");
                    Date verificationDate = spf.parse(user.getVerificationDate());
                    verificationDateEditText.setText(spf.format(verificationDate));
                    completeVerifcationDate.setVisibility(View.VISIBLE);
                    spf = new SimpleDateFormat("yyyy-MM-dd");
                    user.setUpdateVerificationDate(true);
                    user.setVerificationDate(spf.format(verificationDate));
                    user.setIncompleteVerificationDate(false);

                } catch (Exception e1) {
                    e1.printStackTrace();
                    user.setVerificationDate("");
                    user.setIncompleteVerificationDate(true);
                }

            }

        }
        datePicker = new DatePicker(getActivity(), "VerificationDate");
        datePicker.build(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }, null);
        verificationDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classmatePhone.getText().length() > 0)
                    if (!ValidationUtils.isValidPhoneNumber(classmatePhone.getText().toString())) {
                        incorrectPhone.setVisibility(View.VISIBLE);
                        return;
                    }
                datePicker.show();
            }
        });


        //        changeAccNum.setOnClickListener(listener);
        setOnClickListener();

        if (user.isIncompleteFamilyDetails()) {
            incompleteStep2.setVisibility(View.VISIBLE);
        }
        if (user.isIncompleteDOB() || user.isIncompleteAddressDetails()) {
            incompleteStep1.setVisibility(View.VISIBLE);
        }
        if (user.isIncompleteRepaymentSetup() || user.isIncompleteClassmateDetails()
                || user.isIncompleteVerificationDate() || AppUtils.isEmpty(user.getStudentLoan())) {
            incompleteStep3.setVisibility(View.VISIBLE);
            if (user.isIncompleteRepaymentSetup()) {
                incompleteSetupRepayments.setVisibility(View.VISIBLE);
            }
            if (user.isIncompleteClassmateDetails()) {
                incompleteClassmate.setVisibility(View.VISIBLE);
            }
            if (user.isIncompleteVerificationDate()) {
                incompleteVerificationDate.setVisibility(View.VISIBLE);
            }
        }
        return rootView;
    }

    private void setOnClickListener() {
        autoRepayHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "Upload your identity card(with photo) that has been provided to you by your college or institution.";
                String text2 = "Please remember to upload both front and back sides of the card.";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your College ID", text1, text2, "#eeb85f");
                dialog.show();
            }
        });
        classmateHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "We need to speak to a classmate who can vouch for your authenticity. Let them know in advance!";
                String text2 = "";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your College ID", text1, text2, "#eeb85f");
                dialog.show();
            }
        });
        verificationHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "We will come to your college, verify your college ID and get your signature. Tell us a convenient time when we can come and visit!";
                String text2 = "";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your College ID", text1, text2, "#eeb85f");
                dialog.show();
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                AppUtils.saveUserObject(getActivity(), user);
                if (classmatePhone.getText().length() > 0)
                    if (!ValidationUtils.isValidPhoneNumber(classmatePhone.getText().toString())) {
                        incorrectPhone.setVisibility(View.VISIBLE);
                        return;
                    }
                Intent intent = new Intent(getActivity(), SetupAutoRepayments.class);
                startActivity(intent);
            }
        };
        setupAutoRepayments.setOnClickListener(listener);
        changeAccNum.setOnClickListener(listener);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment2(true);
            }
        });

        saveAndProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                if (!ValidationUtils.isValidPhoneNumber(classmatePhone.getText().toString())) {
                    incorrectPhone.setVisibility(View.VISIBLE);
                }
                checkIncomplete();
                if ((user.isIncompleteDOB() || user.isIncompleteAddressDetails() || user.isIncompleteFamilyDetails() || AppUtils.isEmpty(user.getStudentLoan())
                        || user.isIncompleteRepaymentSetup() || user.isIncompleteClassmateDetails() || user.isIncompleteVerificationDate())
                        && !mPrefs.getBoolean("skipIncompleteMessage", false)) {
                    final Dialog dialog1 = new Dialog(getActivity());
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setContentView(R.layout.incomplete_alert_box);

                    Button okay = (Button) dialog1.findViewById(R.id.okay_button);
                    okay.setTextColor(Color.parseColor("#eeb85f"));
                    okay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String json = gson.toJson(user);
                            mPrefs.edit().putString("UserObject", json).apply();
                            Context context = getActivity();
                            Intent intent = new Intent(context, CheckInternetAndUploadUserDetails.class);
                            getContext().sendBroadcast(intent);
                            dialog1.dismiss();
                            Intent intent2 = new Intent(getActivity(), ProfileActivity.class);
                            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent2);
                            getActivity().finish();
                        }
                    });

                    CheckBox stopMessage = (CheckBox) dialog1.findViewById(R.id.check_message);
                    stopMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((CheckBox) v).isChecked()) {
                                mPrefs.edit().putBoolean("skipIncompleteMessage", true).apply();
                            } else {
                                mPrefs.edit().putBoolean("skipIncompleteMessage", false).apply();
                            }
                        }
                    });
                    dialog1.show();
                    return;
                }
                String classmateNameSt = classmateName.getText().toString();
                if (AppUtils.isNotEmpty(classmateNameSt)) {
                    user.setUpdateClassmateName(true);
                    user.setClassmateName(classmateNameSt);
                }
                if (ValidationUtils.isValidPhoneNumber(classmatePhone.getText().toString())) {
                    user.setUpdateClassmatePhone(true);
                    user.setClassmatePhone(classmatePhone.getText().toString());
                }

                if (AppUtils.isNotEmpty(user.getGpa())) {
                    user.setGpaValueUpdate(true);
                }
                if (AppUtils.isNotEmpty(user.getGpaType())) {
                    user.setGpaTypeUpdate(true);
                }
                AppUtils.saveUserObject(getActivity(), user);
                mPrefs.edit().putBoolean("updatingDB", false).apply();
                Context context = getActivity();
                Intent intent = new Intent(context, CheckInternetAndUploadUserDetails.class);
                getContext().sendBroadcast(intent);
                Intent intent2 = new Intent(context, Pending7kApprovalActivity.class);
                startActivity(intent2);
                getActivity().finish();
            }
        });

        classmatePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                incorrectPhone.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        classmatePhone.setOnFocusChangeListener(this);

        gotoFragment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment1(true);
            }
        });
        gotoFragment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment2(true);
            }
        });
    }

    private void saveData() {

        UserModel userSaved = AppUtils.getUserObject(getActivity());

        if (AppUtils.isNotEmpty(userSaved.getBankIfsc())) {
            user.setUpdateBankIfsc(userSaved.isUpdateBankIfsc());
            user.setBankIfsc(userSaved.getBankIfsc());
        }
        if (AppUtils.isNotEmpty(userSaved.getBankAccNum())) {
            user.setUpdateBankAccNum(userSaved.isUpdateBankAccNum());
            user.setBankAccNum(userSaved.getBankAccNum());
        }
    }

    private void getAllViews(View rootView) {
        incompleteStudentLoan = (ImageView) rootView.findViewById(R.id.incomplete_student_loan);
        completeStudentLoan = (ImageView) rootView.findViewById(R.id.complete_student_loan);

        saveAndProceed = (Button) rootView.findViewById(R.id.save_and_proceed);
        previous = (Button) rootView.findViewById(R.id.previous);
        gotoFragment1 = (TextView) rootView.findViewById(R.id.goto_fragment1);
        gotoFragment2 = (TextView) rootView.findViewById(R.id.goto_fragment2);
        gotoFragment3 = (TextView) rootView.findViewById(R.id.goto_fragment3);
        incompleteStep1 = (ImageView) rootView.findViewById(R.id.incomplete_step_1);
        incompleteStep2 = (ImageView) rootView.findViewById(R.id.incomplete_step_2);
        incompleteStep3 = (ImageView) rootView.findViewById(R.id.incomplete_step_3);
        verificationDateEditText = (EditText) rootView.findViewById(R.id.verification_date);
        setupAutoRepayments = (Button) rootView.findViewById(R.id.setup_repayments);
        bankAccNum = (TextView) rootView.findViewById(R.id.bank_acc_number);
        classmateName = (EditText) rootView.findViewById(R.id.classmate_name);
        classmatePhone = (EditText) rootView.findViewById(R.id.classmate_phone);
        completeSetupRepayments = (ImageView) rootView.findViewById(R.id.complete_repayments);
        incompleteSetupRepayments = (ImageView) rootView.findViewById(R.id.incomplete_repayments);
        completeClassmate = (ImageView) rootView.findViewById(R.id.complete_classmate);
        incompleteClassmate = (ImageView) rootView.findViewById(R.id.incomplete_classmate);
        completeVerifcationDate = (ImageView) rootView.findViewById(R.id.complete_verification_date);
        incompleteVerificationDate = (ImageView) rootView.findViewById(R.id.incomplete_verification_date);
        changeAccNum = (ImageButton) rootView.findViewById(R.id.edit_bank_acc);
        incorrectPhone = (TextView) rootView.findViewById(R.id.incorrect_phone);
        topImage = (ImageView) rootView.findViewById(R.id.verify_image_view2);
        autoRepayHelptip = (ImageButton) rootView.findViewById(R.id.auto_repayment_helptip);
        classmateHelptip = (ImageButton) rootView.findViewById(R.id.classmate_helptip);
        verificationHelptip = (ImageButton) rootView.findViewById(R.id.verification_helptip);
    }

    private void setAllHelpTipsEnabled() {
        autoRepayHelptip.setEnabled(true);
        classmateHelptip.setEnabled(true);
        verificationHelptip.setEnabled(true);
    }

    private void checkIncomplete() {
        if (bankAccNum.getText().length() <= 0) {
            user.setIncompleteRepaymentSetup(true);
        } else {
            user.setIncompleteRepaymentSetup(false);
        }
        if (AppUtils.isEmpty(classmateName.getText().toString()) || AppUtils.isEmpty(classmatePhone.getText().toString())) {
            user.setIncompleteClassmateDetails(true);
        } else {
            user.setIncompleteClassmateDetails(false);
            if (AppUtils.isNotEmpty(classmateName.getText().toString())) {
                user.setClassmateName(classmateName.getText().toString());
                user.setUpdateClassmateName(true);
            }
            if (AppUtils.isNotEmpty(classmatePhone.getText().toString())) {
                user.setClassmatePhone(classmatePhone.getText().toString());
                user.setUpdateClassmatePhone(true);
            }
        }
        if (updateUserVerificationDate) {
            user.setIncompleteVerificationDate(false);
            try {
                SimpleDateFormat spf = new SimpleDateFormat("dd MMM yyyy");
                Date newDate = spf.parse(verificationDateEditText.getText().toString());
                spf = new SimpleDateFormat("yyyy-MM-dd");
                user.setVerificationDate(spf.format(newDate));
                user.setUpdateVerificationDate(true);
            } catch (Exception e) {
                user.setIncompleteVerificationDate(true);
                e.printStackTrace();
            }
        } else if (AppUtils.isEmpty(verificationDateEditText.getText().toString())) {
            user.setIncompleteVerificationDate(true);
        }
    }

    private void replaceFragment1(boolean check) {
        if (check) checkIncomplete();
        String json = gson.toJson(user);
        mPrefs.edit().putString("UserObject", json).apply();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment1, new ProfileFormStep2Fragment1(), "Fragment1Tag");
        ft.commit();
    }

    private void replaceFragment2(boolean check) {
        if (check)
            checkIncomplete();
        String json = gson.toJson(user);
        mPrefs.edit().putString("UserObject", json).apply();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment1, new ProfileFormStep2Fragment2(), "Fragment2Tag");
        ft.commit();
    }

    public static void confirmVerificationDate() {
        String date = datePicker.getSelectedDate() + " " + datePicker.getSelectedMonthName() + " " + datePicker.getSelectedYear();
        verificationDateEditText.setText(date);
        updateUserVerificationDate = true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == classmatePhone) {
            if (!hasFocus) {
                if (!ValidationUtils.isValidPhoneNumber(classmatePhone.getText().toString())) {
                    incorrectPhone.setVisibility(View.VISIBLE);
                    return;
                }
                if (user.getUserId().equals(classmatePhone.getText().toString())) {
                    incorrectPhone.setText("Not your number!");
                    incorrectPhone.setVisibility(View.VISIBLE);
                }
            }
            incorrectPhone.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String json = mPrefs.getString("UserObject", "");
        user = gson.fromJson(json, UserModel.class);
    }
}