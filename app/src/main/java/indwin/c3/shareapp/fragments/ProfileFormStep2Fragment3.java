package indwin.c3.shareapp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.Views.DatePicker;
import indwin.c3.shareapp.activities.ProfileFormStep2;
import indwin.c3.shareapp.models.Error;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.HelpTipDialog;
import indwin.c3.shareapp.utils.ValidationUtils;

/**
 * Created by ROCK
 */
public class ProfileFormStep2Fragment3 extends Fragment implements View.OnFocusChangeListener {
    private UserModel user;
    static EditText verificationDateEditText, classmateName, classmatePhone;
    private static DatePicker datePicker;
    public static boolean updateUserVerificationDate = false;
    private ImageView incompleteClassmate, completeClassmate,
            incompleteVerificationDate, completeVerifcationDate;
    private TextView incorrectPhone;
    private EditText addRollNumberEt;
    private ImageView incompleteRollNumber, completeRollNumber;
    private ImageButton classmateHelptip, verificationHelptip;
    private TextView errorRollTv;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.profile_form_step2_fragment3, container, false);
        ProfileFormStep2 profileFormStep2 = (ProfileFormStep2) getActivity();
        user = profileFormStep2.getUser();
        getAllViews(rootView);
        if (user.isAppliedFor7k()) {
            ProfileFormStep1Fragment1.setViewAndChildrenEnabled(rootView, false);
        }
        setAllHelpTipsEnabled();
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

        classmatePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                incompleteClassmate.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                user.setUpdateClassmatePhone(true);

            }
        });
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


        addRollNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                errorRollTv.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (AppUtils.isNotEmpty(addRollNumberEt.getText().toString())) {

                    user.setRollNumber(addRollNumberEt.getText().toString());
                    user.setUpdateRollNumber(true);
                }

            }
        });


        if (user.isIncompleteRollNumber()) {
            if (!user.isAppliedFor7k())
                incompleteRollNumber.setVisibility(View.VISIBLE);
            completeRollNumber.setVisibility(View.GONE);
        } else {
            if (AppUtils.isNotEmpty(user.getRollNumber())) {

                addRollNumberEt.setText(user.getRollNumber());
                completeRollNumber.setVisibility(View.VISIBLE);
                incompleteRollNumber.setVisibility(View.GONE);

            }

        }
        setOnClickListener();

        if (user.isIncompleteClassmateDetails()
                || user.isIncompleteVerificationDate() || user.isIncompleteStudentLoan()) {
            if (user.isIncompleteClassmateDetails() && !user.isAppliedFor7k()) {
                incompleteClassmate.setVisibility(View.VISIBLE);
            }
            if (user.isIncompleteVerificationDate() && !user.isAppliedFor7k()) {
                incompleteVerificationDate.setVisibility(View.VISIBLE);
            }


        }
        return rootView;
    }

    private void setOnClickListener() {

        addRollNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                user.setUpdateRollNumber(true);
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

        classmatePhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    showPhoneValidationError();
                }
            }
        });
    }

    private void showPhoneValidationError() {
        String mobile = AppUtils.getFromSelectedSharedPrefs(getActivity(), "phone_number", "cred");
        if (classmatePhone.getText().toString().equals(mobile)) {
            if (classmatePhone.getText().toString().equals(user.getClassmatePhone())) {
                user.setClassmatePhone(null);
                UserModel userDB = AppUtils.getUserObject(getActivity());
                userDB.setClassmatePhone(null);
            }
            incorrectPhone.setText("This cant be your own number");
            incorrectPhone.setVisibility(View.VISIBLE);

        } else if (!ValidationUtils.isValidPhoneNumber(classmatePhone.getText().toString())) {
            incorrectPhone.setText("Incorrect phone number");
            incorrectPhone.setVisibility(View.VISIBLE);

        } else {
            incorrectPhone.setVisibility(View.GONE);
            user.setClassmatePhone(classmatePhone.getText().toString());
            user.setUpdateClassmatePhone(true);
        }
    }


    private void getAllViews(View rootView) {
        addRollNumberEt = (EditText) rootView.findViewById(R.id.roll_number_et);
        errorRollTv = (TextView) rootView.findViewById(R.id.error_roll_number);
        verificationDateEditText = (EditText) rootView.findViewById(R.id.verification_date);
        completeRollNumber = (ImageView) rootView.findViewById(R.id.complete_roll_number);
        incompleteRollNumber = (ImageView) rootView.findViewById(R.id.incomplete_roll_number);
        classmateName = (EditText) rootView.findViewById(R.id.classmate_name);
        classmatePhone = (EditText) rootView.findViewById(R.id.classmate_phone);
        completeClassmate = (ImageView) rootView.findViewById(R.id.complete_classmate);
        incompleteClassmate = (ImageView) rootView.findViewById(R.id.incomplete_classmate);
        completeVerifcationDate = (ImageView) rootView.findViewById(R.id.complete_verification_date);
        incompleteVerificationDate = (ImageView) rootView.findViewById(R.id.incomplete_verification_date);
        incorrectPhone = (TextView) rootView.findViewById(R.id.incorrect_phone);
        classmateHelptip = (ImageButton) rootView.findViewById(R.id.classmate_helptip);
        verificationHelptip = (ImageButton) rootView.findViewById(R.id.verification_helptip);
    }

    private void setAllHelpTipsEnabled() {
        classmateHelptip.setEnabled(true);
        verificationHelptip.setEnabled(true);
    }

    public void checkIncomplete() {

        showPhoneValidationError();


        if (AppUtils.isEmpty(user.getRollNumber())) {

            user.setIncompleteRollNumber(true);
            completeRollNumber.setVisibility(View.GONE);
            incompleteRollNumber.setVisibility(View.VISIBLE);
        } else {
            user.setIncompleteRollNumber(false);
            incompleteRollNumber.setVisibility(View.GONE);
            completeRollNumber.setVisibility(View.VISIBLE);

        }

        if (AppUtils.isEmpty(classmateName.getText().toString()) || AppUtils.isEmpty(user.getClassmatePhone())) {
            user.setIncompleteClassmateDetails(true);
            completeClassmate.setVisibility(View.GONE);
            incompleteClassmate.setVisibility(View.VISIBLE);
        } else {
            user.setIncompleteClassmateDetails(false);
            user.setClassmateName(classmateName.getText().toString());
            user.setUpdateClassmateName(true);
            incompleteClassmate.setVisibility(View.GONE);
            completeClassmate.setVisibility(View.VISIBLE);
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


        if (user.isIncompleteVerificationDate()) {
            incompleteVerificationDate.setVisibility(View.VISIBLE);
            completeVerifcationDate.setVisibility(View.GONE);
        } else {

            completeVerifcationDate.setVisibility(View.VISIBLE);
            incompleteVerificationDate.setVisibility(View.GONE);

        }
    }


    public static void confirmVerificationDate() {
        String date = datePicker.getSelectedDate() + " " + datePicker.getSelectedMonthName() + " " + datePicker.getSelectedYear();
        verificationDateEditText.setText(date);
        updateUserVerificationDate = true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void showErrorRollNumber(Error error) {

        user.setRollNumber(null);
        user.setUpdateRollNumber(true);
        incompleteRollNumber.setVisibility(View.VISIBLE);
        completeRollNumber.setVisibility(View.GONE);
        errorRollTv.setText(error.getError());
        errorRollTv.setVisibility(View.VISIBLE);
    }
}
