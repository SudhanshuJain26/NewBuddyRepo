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
import android.widget.CheckBox;
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
import indwin.c3.shareapp.models.Error;
import indwin.c3.shareapp.models.Image;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.Constants;
import indwin.c3.shareapp.utils.HelpTipDialog;
import indwin.c3.shareapp.utils.RecyclerItemClickListener;
import io.intercom.com.google.gson.Gson;

/**
 * Created by ROCK
 */
public class ProfileFormStep2Fragment4 extends Fragment {
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
    public static TextView bankErrorTv;

    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    ImageUploaderRecyclerAdapter adapter;
    ArrayList<Uri> imageUris;
    ImageView completeBankStmt, incompleteBankStmt;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    boolean deniedPermissionForever = false;
    private static final int REQUEST_PERMISSION_SETTING = 99;
    public static LinearLayout setupNachLL;
    private CheckBox setupNachCb;
    private TextView setupNachTv;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.profile_form_step2_fragment4, container, false);
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
        if (AppUtils.isNotEmpty(user.getBankAccNum())) {
            setupNachLL.setVisibility(View.GONE);
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
        if (!bankStmnt.getImgUrls().contains("add") && !user.isAppliedFor7k())
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
        adapter = new ImageUploaderRecyclerAdapter(getActivity(), bankStmnt, "Bank Statements", user.isAppliedFor7k(), Constants.IMAGE_TYPE.BANK_STMNTS.toString());
        rvImages.setAdapter(adapter);


        if (user.isAppliedFor7k()) {
            ProfileFormStep1Fragment1.setViewAndChildrenEnabled(rootView, false);
        }
        setAllHelpTipsEnabled();
        if (user.isOptionalNACH() != null && user.isOptionalNACH()) {
            setupNachCb.setBackgroundColor(getResources().getColor(R.color.colorwhite));
            setupNachCb.setChecked(user.isOptionalNACH());
        }
        if (!user.isAppliedFor7k() && AppUtils.isNotEmpty(user.getBankAccNum()) && AppUtils.isNotEmpty(user.getBankIfsc())) {
            changeAccNum.setVisibility(View.VISIBLE);
        } else {
            changeAccNum.setVisibility(View.GONE);
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
        setOnClickListener();

        if (user.isIncompleteRepaymentSetup() && !user.isAppliedFor7k()) {
            incompleteSetupRepayments.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    public void showHideBankStatement(boolean isBankLayoutVisible) {
        if (isBankLayoutVisible)
            bankStmntLayout.setVisibility(View.VISIBLE);
        else bankStmntLayout.setVisibility(View.GONE);

    }

    private void setOnClickListener() {
        setupNachTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNACHDialogBox();
            }
        });
        setupNachCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setupNachCb.isChecked()) {
                    setupNachCb.setChecked(false);
                    openNACHDialogBox();
                } else {
                    setupNachCb.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                    user.setOptionalNACH(false);
                }
            }
        });
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

    private void openNACHDialogBox() {
        final Dialog dialogView = new Dialog(getActivity(), R.style.nach_dialog);
        dialogView.setContentView(R.layout.setup_nach_later_layout);
        dialogView.show();
        Button setupNACH = (Button) dialogView.findViewById(R.id.setup_later);
        setupNACH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupNachCb.setChecked(true);
                user.setOptionalNACH(true);
                dialogView.hide();
            }
        });
    }


    private void getAllViews(View rootView) {
        setupNachLL = (LinearLayout) rootView.findViewById(R.id.setupNachLL);
        setupNachCb = (CheckBox) rootView.findViewById(R.id.setupNachCB);
        setupNachTv = (TextView) rootView.findViewById(R.id.setupNachTv);
        bankErrorTv = (TextView) rootView.findViewById(R.id.bankErrorTv);
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

        UserModel userDB = AppUtils.getUserObject(getActivity());
        if (AppUtils.isEmpty(userDB.getBankAccNum()) && ((user.isOptionalNACH() == null) || !(user.isOptionalNACH()))) {
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

    public void showDuplicateBankAccountNumber(Error error) {
        bankErrorTv.setVisibility(View.VISIBLE);
        bankErrorTv.setText(error.getError());
        incompleteSetupRepayments.setVisibility(View.VISIBLE);
        completeSetupRepayments.setVisibility(View.GONE);
        user.setIncompleteRepaymentSetup(true);
        user.setBankAccNum(null);
        user.setUpdateBankAccNum(true);
        user.setBankIfsc(null);
        user.setUpdateBankIfsc(true);
        setupNachLL.setVisibility(View.VISIBLE);

    }
}