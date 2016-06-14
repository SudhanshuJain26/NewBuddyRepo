package indwin.c3.shareapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.cam2.CameraActivity;
import com.commonsware.cwac.cam2.Facing;
import com.commonsware.cwac.cam2.ZoomStyle;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.fragments.ProfileFormStep1Fragment4;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.PicassoTrustAll;
import indwin.c3.shareapp.utils.TargetButton;
import io.intercom.android.sdk.Intercom;

public class AgreementActivity extends AppCompatActivity {

    private WebView termsAndConditionWebView;
    public static TargetButton takeASelfie, addSignature;
    private int REQUEST_TAKE_PHOTO = 13;
    private String mCurrentPhotoPath;
    private UserModel user;
    private ScrollView parentScrollView;
    private Button acceptTerms;
    public static boolean isSelfieAdded = false, isSignatureAdded = false;
    boolean deniedPermissionForever = false;
    private static final int REQUEST_PERMISSION_SETTING = 99;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    public static final int PERMISSION_ALL = 0;
    private Target loadTarget;
    int widthSelfie = 200, heightSelfie = 180;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            TextView headerTitle = (TextView) findViewById(R.id.activity_header);
            headerTitle.setText("Agreement");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            user = AppUtils.getUserObject(this);
            addSignature = (TargetButton) findViewById(R.id.add_signature_button);
            termsAndConditionWebView = (WebView) findViewById(R.id.terms_and_conditions_webview);
            WebSettings settings = termsAndConditionWebView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setUserAgentString(settings.getUserAgentString() + getApplicationContext().getString(R.string.buddyagent));
            termsAndConditionWebView.setWebChromeClient(new WebChromeClient() {
                public boolean onConsoleMessage(ConsoleMessage cm) {

                    Log.d("MyApplication agreement", cm.message() + " -- From line "
                            + cm.lineNumber() + " of "
                            + cm.sourceId());
                    return true;
                }
            });
            termsAndConditionWebView.loadUrl(getApplicationContext().getString(R.string.web) + "termsApp");
            takeASelfie = (TargetButton) findViewById(R.id.take_a_selfie_button);
            acceptTerms = (Button) findViewById(R.id.accept_terms);
            if (AppUtils.isNotEmpty(user.getSignature())) {
                addSignature.setText("");
                String signatureUrl = user.getSignature();
                if (signatureUrl.contains("http")) {

                    if (loadTarget == null)
                        loadTarget = new Target() {
                            @Override
                            public void onBitmapFailed(Drawable arg0) {
                            }

                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 300, 100, false));
                                d.setColorFilter(new
                                        PorterDuffColorFilter(getResources().getColor(R.color.colorSignature), PorterDuff.Mode.MULTIPLY));
                                addSignature.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
                                if (!user.isAppliedFor1k()) {
                                    addSignature.setText("Edit?");
                                }
                                isSignatureAdded = true;
                            }

                            @Override
                            public void onPrepareLoad(Drawable arg0) {

                            }
                        };

                    try {
                        Picasso.with(this).load(signatureUrl).into(loadTarget);
                    } catch (IllegalArgumentException iae) {
                        iae.printStackTrace();
                    }

                } else {
                    final File imgFile = new File(signatureUrl);
                    if (imgFile.exists()) {
                        Drawable d = Drawable.createFromPath(signatureUrl);
                        d.setColorFilter(new
                                PorterDuffColorFilter(getResources().getColor(R.color.colorSignature), PorterDuff.Mode.MULTIPLY));
                        addSignature.setPadding(0, 0, 0, 0);
                        addSignature.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
                        if (!user.isAppliedFor1k()) {
                            addSignature.setText("Edit?");
                        }
                        isSignatureAdded = true;
                    }
                }
            }

            if (AppUtils.isNotEmpty(user.getSelfie())) {

                String path = user.getSelfie();
                if (path.contains("http")) {
                    populateSelfie(path, null);
                } else {
                    final File imgFile = new File(path);
                    if (imgFile.exists()) {
                        populateSelfie(path, imgFile);
                    }
                }
            }

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

            parentScrollView = (ScrollView) findViewById(R.id.parent_scrollview);
            parentScrollView.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    try {
                        findViewById(R.id.terms_and_conditions_webview).getParent().requestDisallowInterceptTouchEvent(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
            termsAndConditionWebView.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });


            if (!user.isAppliedFor1k()) {
                takeASelfie.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] temp = hasPermissions(AgreementActivity.this, PERMISSIONS);
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && temp != null && temp.length != 0) {
                            deniedPermissionForever = true;
                            PERMISSIONS = temp;
                            requestPermissions(PERMISSIONS, PERMISSION_ALL);
                        } else {
                            dispatchTakePictureIntent();
                        }
                    }
                });

                addSignature.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AgreementActivity.this, AddSignatureActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                addSignature.setText("");
                acceptTerms.setVisibility(View.GONE);
            }
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }

        acceptTerms.setOnClickListener(new View.OnClickListener()

                                       {
                                           @Override
                                           public void onClick(View v) {
                                               if (isSelfieAdded && isSignatureAdded) {
                                                   UserModel userModel = AppUtils.getUserObject(AgreementActivity.this);
                                                   userModel.setTncUpdate(true);
                                                   userModel.setTncAccepted(true);
                                                   AppUtils.saveUserObject(AgreementActivity.this, userModel);
                                                   ProfileFormStep1Fragment4.completeAgreement.setVisibility(View.VISIBLE);
                                                   finish();
                                               } else {
                                                   if (!isSelfieAdded)
                                                       Toast.makeText(AgreementActivity.this, "Please add a selfie", Toast.LENGTH_SHORT).show();
                                                   else
                                                       Toast.makeText(AgreementActivity.this, "Please add a signature", Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       }

        );


    }


    private void dispatchTakePictureIntent() {
        try {
            Intent takePictureIntent = new CameraActivity.IntentBuilder(AgreementActivity.this)
                    .skipConfirm()
                    .to(createImageFile()).facing(getCamera())
                    .debug()
                    .zoomStyle(ZoomStyle.SEEKBAR)
                    .updateMediaStore()
                    .build();
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        } catch (Exception e) {


        }
    }

    private Facing getCamera() {

        Camera.CameraInfo ci = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
                return Facing.FRONT;
        }
        return Facing.BACK;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        galleryAddPic();
        return image;
    }


    private void populateSelfie(String path, File file) {
        takeASelfie.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.transparent_cam), null, null);
        if (file != null) {
            PicassoTrustAll.getInstance(this).load(file).placeholder(R.drawable.downloading).into(takeASelfie);

        } else {
            PicassoTrustAll.getInstance(this).load(path).placeholder(R.drawable.downloading).into(takeASelfie);
        }
        takeASelfie.setText("");
        isSelfieAdded = true;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Drawable d = Drawable.createFromPath(mCurrentPhotoPath);
            if (d == null) {
                user.setSelfie("");
                isSelfieAdded = false;
                Toast.makeText(getBaseContext(), "Error while capturing image", Toast.LENGTH_LONG).show();
                return;
            }
            UserModel user = AppUtils.getUserObject(AgreementActivity.this);
            isSelfieAdded = true;
            populateSelfie(mCurrentPhotoPath, new File(mCurrentPhotoPath));
            user.setUpdateSelfie(true);
            user.setSelfie(mCurrentPhotoPath);
            user.setTncAccepted(false);
            user.setTncUpdate(true);

            AppUtils.saveUserObject(this, user);
        } else if (requestCode == REQUEST_PERMISSION_SETTING && resultCode == Activity.RESULT_OK) {
            hasPermissions(this, PERMISSIONS);
        }
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
    public void onBackPressed() {
        super.onBackPressed();
        if (!user.isAppliedFor1k()) {
            UserModel user = AppUtils.getUserObject(this);
            if (AppUtils.isEmpty(user.getSelfie()) || AppUtils.isEmpty(user.getSignature())||!user.isTncAccepted()) {
                user.setInCompleteAgreement(true);
                AppUtils.saveUserObject(this, user);
                ProfileFormStep1Fragment4.incompleteAgreement.setVisibility(View.VISIBLE);
                ProfileFormStep1Fragment4.completeAgreement.setVisibility(View.GONE);
            }
        }
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public String[] hasPermissions(Context context, final String... permissions) {
        ArrayList<String> askPermissions = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    if (!shouldShowRequestPermissionRationale(permission) && deniedPermissionForever) {
                        showMessageOKCancel("You need to allow access to Camera",
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

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
