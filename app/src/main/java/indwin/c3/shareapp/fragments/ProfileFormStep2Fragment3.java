package indwin.c3.shareapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gun0912.tedpicker.ImagePickerActivity;

import java.util.ArrayList;
import java.util.Map;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.Views.DatePicker;
import indwin.c3.shareapp.activities.ImageHelperActivity;
import indwin.c3.shareapp.activities.ProfileFormStep2;
import indwin.c3.shareapp.activities.SetupAutoRepayments;
import indwin.c3.shareapp.adapters.ImageUploaderRecyclerAdapter;
import indwin.c3.shareapp.models.Image;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.Constants;
import indwin.c3.shareapp.utils.HelpTipDialog;
import indwin.c3.shareapp.utils.RecyclerItemClickListener;
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
    private LinearLayout bankStmntLayout;
    private ImageButton bankHelptip;
    private ArrayList<String> bankStmts;
    private Map<String, String> newBankStmts;
    private Image bankStmnt;


    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    ImageUploaderRecyclerAdapter adapter;
    ArrayList<Uri> imageUris;
    ImageView completeBankStmt, incompleteBankStmt;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    boolean deniedPermissionForever = false;
    private static final int REQUEST_PERMISSION_SETTING = 99;

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
        boolean b = false;
        try {
            b = Boolean.parseBoolean(user.getStudentLoan());

        } catch (Exception e) {
        }
        showHideBankStatement(b);
        try {
            bankStmts = user.getBankStmts();
            if (user.getBankStatement() == null) {
                user.setBankStatement(new Image());
            } else {
                completeBankStmt.setVisibility(View.VISIBLE);
                user.setIncompleteBankStmt(false);
            }
        } catch (Exception e) {
            bankStmts = new ArrayList<>();
        }
        bankStmnt = user.getBankStatement();
        if (!bankStmnt.getImgUrls().contains("add") && !user.isAppliedFor60k())
            bankStmnt.getImgUrls().add("add");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvImages.setLayoutManager(layoutManager);

        rvImages.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (bankStmnt.getImgUrls().get(position - bankStmnt.getInvalidImgUrls().size() - bankStmnt.getValidImgUrls().size()).equals("add")) {

                            String[] temp = hasPermissions(getActivity(), PERMISSIONS);
                            if (temp != null && temp.length != 0) {
                                deniedPermissionForever = true;
                                PERMISSIONS = temp;
                                requestPermissions(PERMISSIONS, ProfileFormStep1Fragment2.PERMISSION_ALL);
                            } else {
                                Intent intent = new Intent(getActivity(), ImageHelperActivity.class);
                                startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
                            }
                        }
                    }
                })
        );
        adapter = new ImageUploaderRecyclerAdapter(getActivity(), bankStmnt, "Bank Statements", user.isAppliedFor60k(), Constants.IMAGE_TYPE.BANK_STMNTS.toString());
        rvImages.setAdapter(adapter);


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

    public void showHideBankStatement(boolean isBankLayoutVisible) {
        if (isBankLayoutVisible)
            bankStmntLayout.setVisibility(View.VISIBLE);
        else bankStmntLayout.setVisibility(View.GONE);

    }

    private void setOnClickListener() {

        bankHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "We require your last 3 months financial history to be able to provide you with a higher borrowing limit. " +
                        "You can either take photos/scans of your passbook pages or download/take screenshots from your " +
                        "netbanking account";
                String text2 = "Please remember to upload both front and back sides of the card.";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your College ID", text1, text2, "#f2954e");
                dialog.show();
            }
        });
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

        bankStmntLayout = (LinearLayout) rootView.findViewById(R.id.bank_statement_layout);

        completeBankStmt = (ImageView) rootView.findViewById(R.id.complete_bank_stmt);
        incompleteBankStmt = (ImageView) rootView.findViewById(R.id.incomplete_bank_stmt);
        bankHelptip = (ImageButton) rootView.findViewById(R.id.bank_helptip);
    }

    private void setAllHelpTipsEnabled() {
        autoRepayHelptip.setEnabled(true);
        bankHelptip.setEnabled(true);
    }


    @Override
    public void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK) {
            UserModel user = AppUtils.getUserObject(getActivity());
            if (user.getBankStmts() == null)
                user.setBankStmts(new ArrayList<String>());
            imageUris = intent.getParcelableArrayListExtra(ImageHelperActivity.EXTRA_IMAGE_URIS);
            if (user.getBankStatement() == null)
                user.setBankStatement(new Image());
            Image image = user.getBankStatement();
            for (Uri uri : imageUris) {
                image.getImgUrls().add(0, uri.getPath());
                image.getNewImgUrls().put(uri.getPath(), AppUtils.uploadStatus.OPEN.toString());
                this.user.getBankStatement().getImgUrls().add(0, uri.getPath());

            }
            adapter.notifyDataSetChanged();
            image.setUpdateNewImgUrls(true);
            AppUtils.saveUserObject(getActivity(), user);
        }
    }

    public String[] hasPermissions(Context context, final String... permissions) {
        ArrayList<String> askPermissions = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    if (!shouldShowRequestPermissionRationale(permission) && deniedPermissionForever) {
                        showMessageOKCancel("You need to allow access to Images",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                    }
                                });
                    }
                    askPermissions.add(permission);
                }
            }
        }
        return askPermissions.toArray(new String[0]);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("Settings", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == ProfileFormStep1Fragment2.PERMISSION_ALL) {
            if (grantResults.length > 0)
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
            Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
            startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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

        try {
            boolean isTakenStudentLoan = Boolean.parseBoolean(user.getStudentLoan());
            if (isTakenStudentLoan) {

                Image image = user.getBankStatement();
                int totalSize = image.getImgUrls().size() + image.getValidImgUrls().size() + image.getInvalidImgUrls().size();
                if (totalSize == 0) {
                    user.setIncompleteBankStmt(true);
                } else if (totalSize == 1) {
                    if ("add".equals(image.getImgUrls().get(0))) {
                        user.setIncompleteBankStmt(true);
                    } else {
                        user.setIncompleteBankStmt(false);
                    }
                } else {
                    user.setIncompleteBankStmt(false);
                }

                if (user.isIncompleteBankStmt()) {
                    incompleteBankStmt.setVisibility(View.VISIBLE);
                    completeBankStmt.setVisibility(View.GONE);
                } else {
                    completeBankStmt.setVisibility(View.VISIBLE);
                    incompleteBankStmt.setVisibility(View.GONE);
                }
            }

        } catch (Exception e) {
        }


    }


    @Override
    public void onResume() {
        super.onResume();
    }
}