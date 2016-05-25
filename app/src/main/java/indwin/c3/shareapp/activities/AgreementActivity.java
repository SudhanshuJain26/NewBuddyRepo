package indwin.c3.shareapp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import indwin.c3.shareapp.fragments.ProfileFormStep1Fragment3;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import io.intercom.com.google.gson.Gson;

public class AgreementActivity extends AppCompatActivity {

    private WebView termsAndConditionWebView;
    public static Button addSignature, takeASelfie;
    public static ImageView signature;
    private int REQUEST_TAKE_PHOTO = 13;
    String mCurrentPhotoPath;
    SharedPreferences mPrefs;
    Gson gson;
    UserModel user;
    private ScrollView parentScrollView;
    private Button acceptTerms;
    public static boolean isSelfieAdded = false, isSignatureAdded = false;
    boolean deniedPermissionForever = false;
    final static int MY_PERMISSIONS_REQUEST_CAMERA = 13;
    private static final int REQUEST_PERMISSION_SETTING = 99;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    public static final int PERMISSION_ALL = 0;
    private Target loadTarget, loadTargetSelfie;
    int widthSelfie = 200, heightSelfie = 180;
    private ColorDrawable cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        cd = new ColorDrawable(0xFFFFFF);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            TextView headerTitle = (TextView) findViewById(R.id.activity_header);
            headerTitle.setText("Agreement");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            mPrefs = getSharedPreferences("buddy", Context.MODE_PRIVATE);
            gson = new Gson();
            String json = mPrefs.getString("UserObject", "");
            user = gson.fromJson(json, UserModel.class);
            addSignature = (Button) findViewById(R.id.add_signature_button);
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
            //termsAndConditionWebView.loadUrl(AppUtils.TnC_URL);
            termsAndConditionWebView.loadUrl(getApplicationContext().getString(R.string.web) + "termsApp");
            takeASelfie = (Button) findViewById(R.id.take_a_selfie_button);

            acceptTerms = (Button) findViewById(R.id.accept_terms);
            if (AppUtils.isNotEmpty(user.getSignature())) {
                //addSignature.setBackgroundDrawable(getResources().getDrawable(R.drawable.downloading));
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
                                //addSignature.setPadding(0, 0, 0, 0);
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
                        Picasso.with(this).load(signatureUrl).placeholder(R.drawable.downloading).into(loadTarget);
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
                Drawable d = getResources().getDrawable(R.drawable.downloading);


                populateSelfie(null, d);
                String path = user.getSelfie();
                if (path.contains("http")) {

                    if (loadTargetSelfie == null)
                        loadTargetSelfie = new Target() {
                            @Override
                            public void onBitmapFailed(Drawable arg0) {
                            }

                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                handleLoadedBitmap(bitmap);
                            }

                            @Override
                            public void onPrepareLoad(Drawable arg0) {

                            }
                        };

                    try {
                        Picasso.with(this).load(path).into(loadTargetSelfie);
                    } catch (IllegalArgumentException iae) {
                        iae.printStackTrace();
                    }

                } else {
                    final File imgFile = new File(path);
                    if (imgFile.exists()) {
                        Drawable drawable = Drawable.createFromPath(user.getSelfie());


                        populateSelfie(null, drawable);


                    }
                }
            }


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
        } catch (Exception e) {
            e.printStackTrace();
        }
        acceptTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel userSaved = AppUtils.getUserObject(AgreementActivity.this);
                if (AppUtils.isNotEmpty(userSaved.getSignature())) {

                    user.setSignature(userSaved.getSignature());
                    user.setUpdateSignature(userSaved.isUpdateSignature());
                }
                if (AppUtils.isNotEmpty(user.getSelfie()) && AppUtils.isNotEmpty(user.getSignature()) && ProfileFormStep1Fragment3.completeAgreement != null) {

                    ProfileFormStep1Fragment3.completeAgreement.setVisibility(View.VISIBLE);
                    finish();
                } else {
                    if (AppUtils.isEmpty(user.getSelfie()))
                        Toast.makeText(AgreementActivity.this, "Please add a selfie", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(AgreementActivity.this, "Please add a signature", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void dispatchTakePictureIntent() {
        try {
            Intent takePictureIntent = new CameraActivity.IntentBuilder(AgreementActivity.this)
                    .skipConfirm()
                    .to(createImageFile()).facing(Facing.FRONT)
                    .debug()
                    .zoomStyle(ZoomStyle.SEEKBAR)
                    .updateMediaStore()
                    .build();
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        } catch (Exception e) {


        }
        //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //// Ensure that there's a camera activity to handle the intent
        //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        //    // Create the File where the photo should go
        //    File photoFile = null;
        //    try {
        //        photoFile = createImageFile();
        //    } catch (IOException ex) {
        //        // Error occurred while creating the File
        //    }
        //    // Continue only if the File was successfully created
        //    if (photoFile != null) {
        //        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
        //                Uri.fromFile(photoFile));
        //        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        //    }
        //}
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

    public void handleLoadedBitmap(Bitmap b) {
        populateSelfie(b, null);
    }

    private void populateSelfie(Bitmap bitmap, Drawable d) {
        if (bitmap != null) {
            d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, widthSelfie, heightSelfie, false));
        }
        takeASelfie.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.transparent_cam), null, null);
        takeASelfie.setBackgroundDrawable(d);
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
                Toast.makeText(getBaseContext(),
                        "Error while capturing image", Toast.LENGTH_LONG)

                        .show();

                return;
            }


            populateSelfie(null, d);
            user.setUpdateSelfie(true);
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

        if (AppUtils.isEmpty(user.getSelfie()) || AppUtils.isEmpty(user.getSignature())) {
            user.setInCompleteAgreement(true);
            ProfileFormStep1Fragment3.incompleteAgreement.setVisibility(View.VISIBLE);
            ProfileFormStep1Fragment3.completeAgreement.setVisibility(View.GONE);
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
