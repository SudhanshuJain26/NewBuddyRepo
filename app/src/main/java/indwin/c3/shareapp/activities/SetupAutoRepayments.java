package indwin.c3.shareapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gun0912.tedpicker.ImagePickerActivity;

import java.util.ArrayList;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.adapters.ImageUploaderRecyclerAdapter;
import indwin.c3.shareapp.fragments.ProfileFormStep2Fragment4;
import indwin.c3.shareapp.models.Image;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.Constants;
import indwin.c3.shareapp.utils.HelpTipDialog;
import indwin.c3.shareapp.utils.RecyclerItemClickListener;
import indwin.c3.shareapp.utils.ValidationUtils;
import io.intercom.android.sdk.Intercom;

public class SetupAutoRepayments extends AppCompatActivity implements View.OnFocusChangeListener {

    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    ArrayList<Uri> imageUris;
    UserModel user;
    EditText bankAccNumber, confirmBankAccNumber, ifscCode;
    Button submitRepayments;
    TextView accountNumberMismatch, wrongIfsc;
    boolean hasAccNum = false, hasConfirmAccNum = false, hasIfsc = false;
    ImageUploaderRecyclerAdapter adapter;
    boolean deniedPermissionForever = false;
    public static final int PERMISSION_ALL = 0;
    private static final int REQUEST_PERMISSION_SETTING = 99;
    RecyclerView rvImages;
    private Image bankProof;
    private ImageButton bankIfscHelptip, bankProofHelptip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_auto_repayments);
        getAllViews();
        setClickListener();
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

            user = AppUtils.getUserObject(this);

            rvImages = (RecyclerView) findViewById(R.id.rvImages);
            try {

                if (user.getBankProof() == null) {
                    user.setBankProof(new Image());
                }
            } catch (Exception e) {
            }
            bankProof = user.getBankProof();
            if (!user.isAppliedFor7k())
                bankProof.getImgUrls().add("add");
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rvImages.setLayoutManager(layoutManager);

        } catch (Exception e) {
            e.printStackTrace();
        }
        rvImages.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (bankProof.getImgUrls().get(position - user.getBankProof().getInvalidImgUrls().size() - user.getBankProof().getValidImgUrls().size()).equals("add")) {
                            String[] temp = AppUtils.hasPermissions(SetupAutoRepayments.this, deniedPermissionForever, REQUEST_PERMISSION_SETTING, PERMISSIONS);
                            if (temp != null && temp.length != 0) {
                                deniedPermissionForever = true;
                                PERMISSIONS = temp;
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(PERMISSIONS, PERMISSION_ALL);
                                }
                            } else {
                                Intent intent = new Intent(SetupAutoRepayments.this, ImagePickerActivity.class);
                                startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
                            }
                        }
                    }
                })
        );
        adapter = new ImageUploaderRecyclerAdapter(this, bankProof, "Bank Proofs", user.isAppliedFor7k(), Constants.IMAGE_TYPE.BANK_PROOF.toString());
        rvImages.setAdapter(adapter);

        bankAccNumber = (EditText) findViewById(R.id.bank_acc_number);
        confirmBankAccNumber = (EditText) findViewById(R.id.confirm_bank_acc_number);
        ifscCode = (EditText) findViewById(R.id.ifsc_code);
        submitRepayments = (Button) findViewById(R.id.submit_repayments);
        accountNumberMismatch = (TextView) findViewById(R.id.account_mismatch);
        wrongIfsc = (TextView) findViewById(R.id.wrong_ifsc);
        if (AppUtils.isNotEmpty(user.getBankAccNum())) {

            bankAccNumber.setText(user.getBankAccNum());
            confirmBankAccNumber.setText(user.getBankAccNum());
            hasAccNum = true;
            hasConfirmAccNum = true;
        }
        if (AppUtils.isNotEmpty(user.getBankIfsc())) {
            ifscCode.setText(user.getBankIfsc());
            if (user.getBankIfsc().trim().length() == 11) {
                hasIfsc = true;
            }
        }
        checkSumbit();
        bankAccNumber.setOnFocusChangeListener(this);
        confirmBankAccNumber.setOnFocusChangeListener(this);

        submitRepayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidationUtils.isNotValidIFSCCode(ifscCode.getText())) {
                    wrongIfsc.setVisibility(View.VISIBLE);
                    return;
                }
                //bankProofs.remove(bankProofs.size() - 1);
                //user.setBankProofs(bankProofs);
                //                String encryptedMsg = "";
                //                try {
                //                    String password = "bf5cbe23fd8e60697c8ddc2ef25af796";
                //                    String iv = "bf5cbe23fd8e6069";
                //                    user.setIv(iv);
                //                    int keyLength = 256;
                //                    byte[] keyBytes = new byte[keyLength / 8];
                //                    Arrays.fill(keyBytes, (byte) 0x0);
                //                    byte[] passwordBytes = password.getBytes("UTF-8");
                //                    int length = passwordBytes.length < keyBytes.length ? passwordBytes.length
                //                            : keyBytes.length;
                //                    System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
                //
                //                    byte[] bytes = AESCrypt.encrypt(new SecretKeySpec(keyBytes, "AES"),
                //                            iv.getBytes("UTF-8"), bankAccNumber.getText().toString().getBytes());
                //                    encryptedMsg = new String(bytes,"UTF-8");
                //                } catch (GeneralSecurityException e) {
                //                    e.printStackTrace();
                //                } catch (Exception e) {
                //                    e.printStackTrace();
                //
                //             }
                UserModel user = AppUtils.getUserObject(SetupAutoRepayments.this);
                String mask = bankAccNumber.getText().toString().replaceAll("\\w(?=\\w{4})", "*");
                user.setBankAccNum(bankAccNumber.getText().toString());
                user.setUpdateBankAccNum(true);
                user.setBankIfsc(ifscCode.getText().toString().toUpperCase());
                user.setUpdateBankIfsc(true);
                AppUtils.saveUserObject(SetupAutoRepayments.this, user);
                ProfileFormStep2Fragment4.bankAccNum.setText(mask);
                ProfileFormStep2Fragment4.setupNachLL.setVisibility(View.GONE);
                ProfileFormStep2Fragment4.setupAutoRepayments.setVisibility(View.GONE);
                ProfileFormStep2Fragment4.bankAccNum.setVisibility(View.VISIBLE);
                ProfileFormStep2Fragment4.incompleteSetupRepayments.setVisibility(View.GONE);
                ProfileFormStep2Fragment4.changeAccNum.setVisibility(View.VISIBLE);
                ProfileFormStep2Fragment4.bankErrorTv.setVisibility(View.GONE);
                finish();
            }
        });

        ifscCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                hasIfsc = false;
                wrongIfsc.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 11) {
                    hasIfsc = true;
                    checkSumbit();
                }
            }
        });
    }

    private void getAllViews() {
        bankIfscHelptip = (ImageButton) findViewById(R.id.bank_ifsc_helptip);
        bankProofHelptip = (ImageButton) findViewById(R.id.bank_proof_helptip);
    }

    private void setClickListener() {

        bankIfscHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text1 = "To enable automatic repayments, we require that the bank account is registered in your own name. Please ensure that the IFSC code provided by you corresponds to the same branch in which your account has been opened.";
                String text2 = "";
                Dialog dialog = new HelpTipDialog(SetupAutoRepayments.this, "Upload your College ID", text1, text2, "#44c2a6");
                dialog.show();

            }
        });
        bankProofHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "We require proof of your Bank Account which clearly shows your Name, Account Number and the bank’s IFSC code. You can upload a photo or scan of any of of the following:<br> • First page of Passbook<br>" +
                        "• Blank/Cancelled Cheque Leaf<br>" +
                        "• Screenshot of your NetBanking account page\n";
                String text2 = "";
                Dialog dialog = new HelpTipDialog(SetupAutoRepayments.this, "Upload your College ID", text1, text2, "#44c2a6");
                dialog.show();
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == bankAccNumber) {
            if (!hasFocus)
                if (bankAccNumber.getText().length() > 0) {
                    bankAccNumber.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    hasAccNum = true;
                } else {
                    hasAccNum = false;
                    bankAccNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            else {
                hasAccNum = false;
                String bankAC = bankAccNumber.getText().toString();
                String confirmBankAc = confirmBankAccNumber.getText().toString();
                if (AppUtils.isNotEmpty(bankAC) && AppUtils.isNotEmpty(confirmBankAc)) {

                    hasAccNum = true;
                    hasConfirmAccNum = true;
                }
                //                bankAccNumber.setText("");
                accountNumberMismatch.setVisibility(View.GONE);
            }
        } else if (v == confirmBankAccNumber) {
            if (!hasFocus)
                if (confirmBankAccNumber.getText().length() > 0) {
                    confirmBankAccNumber.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    if (bankAccNumber.getText().length() > 0) {
                        hasConfirmAccNum = false;
                        if (!bankAccNumber.getText().toString().equals(confirmBankAccNumber.getText().toString())) {
                            accountNumberMismatch.setVisibility(View.VISIBLE);
                        } else
                            hasConfirmAccNum = true;
                    } else {
                        accountNumberMismatch.setVisibility(View.VISIBLE);
                        hasConfirmAccNum = false;
                    }
                } else {
                    hasConfirmAccNum = false;
                    confirmBankAccNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            else {
                hasConfirmAccNum = false;
                //                confirmBankAccNumber.setText("");
                accountNumberMismatch.setVisibility(View.GONE);
            }
        } else if (v == ifscCode) {
            if (!hasFocus)
                if (ifscCode.getText().length() > 0)
                    hasIfsc = true;
                else {
                    hasIfsc = false;
                }
            else {
                hasIfsc = false;
                //                ifscCode.setText("");
                wrongIfsc.setVisibility(View.GONE);
            }
        }
        checkSumbit();
    }

    public void checkSumbit() {
        if (hasAccNum && hasConfirmAccNum && hasIfsc) {
            submitRepayments.setAlpha(1);
            submitRepayments.setClickable(true);
            submitRepayments.setEnabled(true);
        } else {
            submitRepayments.setAlpha(0.5f);
            submitRepayments.setClickable(false);
            submitRepayments.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_ALL) {
            if (grantResults.length > 0)
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
            Intent intent = new Intent(this, ImagePickerActivity.class);
            startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK) {
            UserModel userModel = AppUtils.getUserObject(this);
            if (userModel.getBankProof() == null)
                userModel.setBankProof(new Image());
            imageUris = intent.getParcelableArrayListExtra(ImageHelperActivity.EXTRA_IMAGE_URIS);
            Image image = userModel.getBankProof();
            for (Uri uri : imageUris) {
                image.getImgUrls().add(0, uri.getPath());
                image.getNewImgUrls().put(uri.getPath(), AppUtils.uploadStatus.OPEN.toString());
                bankProof.getImgUrls().add(0, uri.getPath());

            }
            AppUtils.saveUserObject(this, userModel);

            adapter.notifyDataSetChanged();
            image.setUpdateNewImgUrls(true);
        } else if (requestCode == REQUEST_PERMISSION_SETTING && resuleCode == Activity.RESULT_OK) {
            AppUtils.hasPermissions(this, deniedPermissionForever, REQUEST_PERMISSION_SETTING, PERMISSIONS);
        }
    }
}
