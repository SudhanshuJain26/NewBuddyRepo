package indwin.c3.shareapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.gun0912.tedpicker.ImagePickerActivity;

import java.util.ArrayList;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.adapters.ImageUploaderRecyclerAdapter;
import indwin.c3.shareapp.fragments.ProfileFormStep2Fragment3;
import indwin.c3.shareapp.models.Image;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.CheckInternetAndUploadUserDetails;
import indwin.c3.shareapp.utils.Constants;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_auto_repayments);
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
                            String[] temp = hasPermissions(SetupAutoRepayments.this, PERMISSIONS);
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
                Intent intent = new Intent(SetupAutoRepayments.this, CheckInternetAndUploadUserDetails.class);
                sendBroadcast(intent);
                ProfileFormStep2Fragment3.bankAccNum.setText(mask);
                ProfileFormStep2Fragment3.setupAutoRepayments.setVisibility(View.GONE);
                ProfileFormStep2Fragment3.bankAccNum.setVisibility(View.VISIBLE);
                ProfileFormStep2Fragment3.incompleteSetupRepayments.setVisibility(View.GONE);
                ProfileFormStep2Fragment3.changeAccNum.setVisibility(View.VISIBLE);
                //                ProfileFormStep2Fragment3.completeSetupRepayments.setVisibility(View.GONE);
                //                ProfileFormStep2Fragment3.changeAccNum.setVisibility(View.VISIBLE);
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
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
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
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Settings", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
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
            hasPermissions(this, PERMISSIONS);
        }
    }
}
