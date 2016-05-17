package indwin.c3.shareapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gun0912.tedpicker.ImagePickerActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.activities.ImageHelperActivity;
import indwin.c3.shareapp.activities.Pending60kApprovalActivity;
import indwin.c3.shareapp.activities.ProfileActivity;
import indwin.c3.shareapp.adapters.ImageUploaderRecyclerAdapter;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.CheckInternetAndUploadUserDetails;
import indwin.c3.shareapp.utils.HelpTipDialog;
import indwin.c3.shareapp.utils.RecyclerItemClickListener;
import io.intercom.com.google.gson.Gson;

/**
 * Created by shubhang on 07/04/16.
 */
public class ProfileFormStep3Fragment3 extends Fragment {
    private SharedPreferences mPrefs;
    private UserModel user;
    private Button saveAndProceed, previous;
    private Gson gson;
    private TextView gotoFragment1, gotoFragment3, gotoFragment2;
    private final int top = 16, left = 16, right = 16, bottom = 16;
    ImageView incompleteStep1, incompleteStep2, incompleteStep3;
    private ArrayList<String> bankStmts;
    private Map<String, String> newBankStmts;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    ImageUploaderRecyclerAdapter adapter;
    ArrayList<Uri> imageUris;
    ImageView completeBankStmt, incompleteBankStmt;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    boolean deniedPermissionForever = false;
    private static final int REQUEST_PERMISSION_SETTING = 99;
    ImageView topImage;
    private ImageButton bankHelptip;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.profile_form_step3_fragment3, container, false);
        RecyclerView rvImages = (RecyclerView) rootView.findViewById(R.id.rvImages);
        mPrefs = getActivity().getSharedPreferences("buddy", Context.MODE_PRIVATE);
        mPrefs.edit().putBoolean("visitedFormStep3Fragment3", true).apply();
        gson = new Gson();
        String json = mPrefs.getString("UserObject", "");
        user = gson.fromJson(json, UserModel.class);
        newBankStmts = new HashMap<>();
        completeBankStmt = (ImageView) rootView.findViewById(R.id.complete_bank_stmt);
        incompleteBankStmt = (ImageView) rootView.findViewById(R.id.incomplete_bank_stmt);
        try {
            bankStmts = user.getBankStmts();
            if (bankStmts == null) {
                bankStmts = new ArrayList<>();
            } else {
                completeBankStmt.setVisibility(View.VISIBLE);
                user.setIncompleteBankStmt(false);
                mPrefs.edit().putString("UserObject", json).apply();
            }
        } catch (Exception e) {
            bankStmts = new ArrayList<>();
        }
        if (!bankStmts.contains("add") && !user.isAppliedFor60k())
            bankStmts.add("add");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvImages.setLayoutManager(layoutManager);

        rvImages.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (bankStmts.get(position).equals("add")) {
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
        adapter = new ImageUploaderRecyclerAdapter(getActivity(), bankStmts, "Bank Statements", user.isAppliedFor60k());
        rvImages.setAdapter(adapter);

        saveAndProceed = (Button) rootView.findViewById(R.id.unlock);
        previous = (Button) rootView.findViewById(R.id.previous);
        gotoFragment1 = (TextView) rootView.findViewById(R.id.goto_fragment1);
        gotoFragment2 = (TextView) rootView.findViewById(R.id.goto_fragment2);
        gotoFragment3 = (TextView) rootView.findViewById(R.id.goto_fragment3);
        incompleteStep1 = (ImageView) rootView.findViewById(R.id.incomplete_step_1);
        incompleteStep2 = (ImageView) rootView.findViewById(R.id.incomplete_step_2);
        incompleteStep3 = (ImageView) rootView.findViewById(R.id.incomplete_step_3);
        topImage = (ImageView) rootView.findViewById(R.id.verify_image_view2);
        bankHelptip = (ImageButton) rootView.findViewById(R.id.bank_helptip);

        if (!mPrefs.getBoolean("step3Editable", true)) {
            ProfileFormStep1Fragment1.setViewAndChildrenEnabled(rootView, false, gotoFragment1, gotoFragment2);
        }
        setAllHelpTipsEnabled();
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
                    .load(R.mipmap.step3fragment3girl)
                    .into(topImage);
        }
        if (user.isIncompleteAnnualFees() || user.isIncompleteScholarship() || user.isIncompleteStudentLoan()) {
            incompleteStep1.setVisibility(View.VISIBLE);
        }
        if (user.isIncompleteMonthlyExpenditure() || user.isIncompleteVehicleDetails()) {
            incompleteStep2.setVisibility(View.VISIBLE);
        }
        if (user.isIncompleteBankStmt()) {
            incompleteStep3.setVisibility(View.VISIBLE);
        }
        bankHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "We require your last 3 months financial history to be able to provide you with a higher borrowing limit. " +
                        "You can either take photos of your passbook pages or download/take screenshots from your " +
                        "netbanking account for the last 3 months.";
                String text2 = "Please remember to upload both front and back sides of the card.";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your College ID", text1, text2, "#f2954e");
                dialog.show();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment2();
            }
        });
        saveAndProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIncomplete();
                if ((user.isIncompleteAnnualFees() || user.isIncompleteScholarship()
                        || user.isIncompleteStudentLoan() || user.isIncompleteMonthlyExpenditure() ||
                        user.isIncompleteVehicleDetails() || user.isIncompleteBankStmt())
                        && !mPrefs.getBoolean("skipIncompleteMessage", false)) {
                    final Dialog dialog1 = new Dialog(getActivity());
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setContentView(R.layout.incomplete_alert_box);

                    Button okay = (Button) dialog1.findViewById(R.id.okay_button);
                    okay.setTextColor(Color.parseColor("#f2954e"));
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
                } else {
                    String json = gson.toJson(user);
                    mPrefs.edit().putBoolean("updatingDB", false).apply();
                    mPrefs.edit().putString("UserObject", json).apply();
                    Context context = getActivity();
                    Intent intent = new Intent(context, CheckInternetAndUploadUserDetails.class);
                    getContext().sendBroadcast(intent);
                    Intent intent2 = new Intent(context, Pending60kApprovalActivity.class);
                    startActivity(intent2);
                    getActivity().finish();
                }
            }
        });
        if (user.isAppliedFor60k()) {
            previous.setVisibility(View.INVISIBLE);
            saveAndProceed.setVisibility(View.INVISIBLE);
            rootView.findViewById(R.id.details_submitted_tv).setVisibility(View.VISIBLE);
        }
        gotoFragment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment1();
            }
        });
        gotoFragment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment2();
            }
        });
        return rootView;
    }

    private void setAllHelpTipsEnabled() {

        bankHelptip.setEnabled(true);

    }

    private void checkIncomplete() {
        if (bankStmts.size() == 1) {
            user.setIncompleteBankStmt(true);
        } else {
            bankStmts.remove(bankStmts.size() - 1);
            user.setBankStmts(bankStmts);
            user.setIncompleteBankStmt(false);
        }
    }

    private void replaceFragment1() {
        checkIncomplete();
        String json = gson.toJson(user);
        mPrefs.edit().putString("UserObject", json).apply();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment1, new ProfileFormStep3Fragment1(), "Fragment1Tag");
        ft.commit();
    }

    private void replaceFragment2() {
        checkIncomplete();
        String json = gson.toJson(user);
        mPrefs.edit().putString("UserObject", json).apply();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment1, new ProfileFormStep3Fragment2(), "Fragment2Tag");
        ft.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK) {
            if (user.getBankStmts() == null)
                user.setBankStmts(new ArrayList<String>());
            imageUris = intent.getParcelableArrayListExtra(ImageHelperActivity.EXTRA_IMAGE_URIS);
            for (Uri uri : imageUris) {
                bankStmts.add(0, uri.getPath());
                newBankStmts.put(uri.getPath(), AppUtils.uploadStatus.OPEN.toString());
                //                user.addBankStmts(0, uri.getPath(), user.getBankStmts());
                //                adapter.notifyItemInserted(0);
            }
            adapter.notifyDataSetChanged();
            user.setNewBankStmts(newBankStmts);
            user.setUpdateNewBankStmts(true);
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
}