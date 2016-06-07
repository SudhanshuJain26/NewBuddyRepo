package indwin.c3.shareapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.Views.DatePicker;
import indwin.c3.shareapp.activities.ProfileFormStep2;
import indwin.c3.shareapp.activities.SetupAutoRepayments;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.HelpTipDialog;
import io.intercom.com.google.gson.Gson;

/**
 * Created by shubhang on 05/04/16.
 */
public class ProfileFormStep2Fragment3 extends Fragment {
    private SharedPreferences mPrefs;
    private UserModel user;
    private Gson gson;
    private final int top = 16, left = 16, right = 16, bottom = 16;
    static EditText verificationDateEditText, classmateName, classmatePhone;
    private static DatePicker datePicker;
    public static boolean updateUserVerificationDate = false;
    public static Button setupAutoRepayments;
    public static TextView bankAccNum;
    public static ImageButton changeAccNum;
    private TextView incorrectPhone;
    public static ImageView incompleteSetupRepayments, completeSetupRepayments;

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
        ProfileFormStep2 profileFormStep2 = (ProfileFormStep2) getActivity();
        user = profileFormStep2.getUser();

        getAllViews(rootView);

        if (!mPrefs.getBoolean("step2Editable", true)) {
            ProfileFormStep1Fragment1.setViewAndChildrenEnabled(rootView, false);
        }
        setAllHelpTipsEnabled();
        if (!user.isAppliedFor7k() && AppUtils.isNotEmpty(user.getBankAccNum()) && AppUtils.isNotEmpty(user.getBankIfsc())) {
            changeAccNum.setVisibility(View.VISIBLE);
        } else {
            changeAccNum.setVisibility(View.GONE);
        }
        if (mPrefs.getBoolean("visitedFormStep2Fragment2", false)) {
            //gotoFragment2.setAlpha(1);
            //gotoFragment2.setClickable(true);
        }
        if (mPrefs.getBoolean("visitedFormStep2Fragment1", false)) {
            //gotoFragment3.setAlpha(1);
            //gotoFragment3.setClickable(true);
        }
        if (user.getGender() != null && "girl".equals(user.getGender())) {
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


        datePicker = new DatePicker(getActivity(), "VerificationDate");
        datePicker.build(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }, null);


        //        changeAccNum.setOnClickListener(listener);
        setOnClickListener();

        if (user.isIncompleteFamilyDetails()) {
            //incompleteStep2.setVisibility(View.VISIBLE);
        }
        if (user.isIncompleteDOB() || user.isIncompleteAddressDetails()) {
            //incompleteStep1.setVisibility(View.VISIBLE);
        }
        if (user.isIncompleteRepaymentSetup() || user.isIncompleteClassmateDetails()
                || user.isIncompleteVerificationDate() || user.isIncompleteStudentLoan()) {
            //incompleteStep3.setVisibility(View.VISIBLE);
            if (user.isIncompleteRepaymentSetup()) {
                incompleteSetupRepayments.setVisibility(View.VISIBLE);
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

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetupAutoRepayments.class);
                startActivity(intent);
            }
        };
        setupAutoRepayments.setOnClickListener(listener);
        changeAccNum.setOnClickListener(listener);


    }


    private void getAllViews(View rootView) {


        setupAutoRepayments = (Button) rootView.findViewById(R.id.setup_repayments);
        bankAccNum = (TextView) rootView.findViewById(R.id.bank_acc_number);
        completeSetupRepayments = (ImageView) rootView.findViewById(R.id.complete_repayments);
        incompleteSetupRepayments = (ImageView) rootView.findViewById(R.id.incomplete_repayments);
        changeAccNum = (ImageButton) rootView.findViewById(R.id.edit_bank_acc);
        autoRepayHelptip = (ImageButton) rootView.findViewById(R.id.auto_repayment_helptip);
    }

    private void setAllHelpTipsEnabled() {
        autoRepayHelptip.setEnabled(true);
    }

    public void checkIncomplete() {


        if (bankAccNum.getText().length() <= 0) {
            user.setIncompleteRepaymentSetup(true);
            completeSetupRepayments.setVisibility(View.GONE);
            incompleteSetupRepayments.setVisibility(View.VISIBLE);
        } else {
            user.setIncompleteRepaymentSetup(false);
            incompleteSetupRepayments.setVisibility(View.GONE);
            completeSetupRepayments.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public void onResume() {
        super.onResume();
    }
}