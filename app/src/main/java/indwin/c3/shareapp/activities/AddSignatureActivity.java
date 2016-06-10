package indwin.c3.shareapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.InkView;

public class AddSignatureActivity extends AppCompatActivity {
    Button saveSignature;
    ImageButton clearSignature, changeSize;
    SharedPreferences mPrefs;
    InkView ink;
    String mCurrentSignPath;
    UserModel user;
    Gson gson;
    boolean expanded = false;
    LinearLayout noteLayout;
    boolean deniedPermissionForever = false;
    final static int MY_PERMISSIONS_REQUEST_CAMERA = 13;
    private static final int REQUEST_PERMISSION_SETTING = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_signature);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            TextView headerTitle = (TextView) findViewById(R.id.activity_header);
            headerTitle.setText("Add your Signature");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ink = (InkView) findViewById(R.id.ink);
        ink.setColor(getResources().getColor(android.R.color.black));
        ink.setMinStrokeWidth(1.5f);
        ink.setMaxStrokeWidth(6f);

        gson = new Gson();
        mPrefs = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        String json = mPrefs.getString("UserObject", "");
        user = AppUtils.getUserObject(this);

        saveSignature = (Button) findViewById(R.id.save_signature);
        changeSize = (ImageButton) findViewById(R.id.change_size_signature);
        clearSignature = (ImageButton) findViewById(R.id.clear_signature);
        noteLayout = (LinearLayout) findViewById(R.id.note_layout);
        clearSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ink.clear();
            }
        });
        saveSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ink.getBitmap() == null) {
                    Toast.makeText(AddSignatureActivity.this, "No bitmap", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    hasPermissions();
                    Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(ink.getBitmap(), 300, 180, false));
                    d.setColorFilter(new
                            PorterDuffColorFilter(getResources().getColor(R.color.colorSignature), PorterDuff.Mode.MULTIPLY));
                    AgreementActivity.addSignature.setPadding(0, 0, 0, 0);
                    AgreementActivity.addSignature.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
                    AgreementActivity.addSignature.setText("Edit?");
                    AgreementActivity.isSignatureAdded = true;

                    UserModel user = AppUtils.getUserObject(AddSignatureActivity.this);
                    user.setSignature(mCurrentSignPath);
                    user.setUpdateSignature(true);
                    user.setTncAccepted(false);
                    user.setTncUpdate(true);
                    AppUtils.saveUserObject(AddSignatureActivity.this, user);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        changeSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expanded) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ink.getLayoutParams();
                    int px = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            320,
                            getResources().getDisplayMetrics()
                    );
                    params.height = px;
                    ink.setLayoutParams(params);
                    RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) noteLayout.getLayoutParams();
                    params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    params.bottomMargin = 200;
                    noteLayout.setLayoutParams(params2);
                    changeSize.setImageResource(R.drawable.minimize);
                } else {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ink.getLayoutParams();
                    int px = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            250,
                            getResources().getDisplayMetrics()
                    );
                    params.height = px;
                    ink.setLayoutParams(params);
                    RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) noteLayout.getLayoutParams();
                    params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                    noteLayout.setLayoutParams(params2);
                    changeSize.setImageResource(R.drawable.maximize);
                }
                expanded = !expanded;
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentSignPath = image.getAbsolutePath();
        return image;
    }

    private void storeImage(Bitmap image) {
        try {
            File pictureFile = createImageFile();
            if (pictureFile == null) {
                Log.d("buddyError",
                        "Error creating media file, check storage permissions: ");// e.getMessage());
                return;
            }
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("buddyError", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("buddyError", "Error accessing file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
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
        finish();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Settings", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void hasPermissions() {
        if (ContextCompat.checkSelfPermission(AddSignatureActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(AddSignatureActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) && deniedPermissionForever) {
                showMessageOKCancel("You need to allow access to your media storage",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        });
            } else {
                ActivityCompat.requestPermissions(AddSignatureActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
        } else {
            storeImage(ink.getBitmap());
        }
    }
}
