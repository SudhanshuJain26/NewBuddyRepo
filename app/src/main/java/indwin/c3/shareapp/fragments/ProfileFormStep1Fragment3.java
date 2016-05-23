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
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.activities.AgreementActivity;
import indwin.c3.shareapp.activities.ImageHelperActivity;
import indwin.c3.shareapp.activities.PendingFlashApprovalActivity;
import indwin.c3.shareapp.activities.ProfileActivity;
import indwin.c3.shareapp.adapters.ImageUploaderRecyclerAdapter;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.CheckInternetAndUploadUserDetails;
import indwin.c3.shareapp.utils.HelpTipDialog;
import indwin.c3.shareapp.utils.RecyclerItemClickListener;
import indwin.c3.shareapp.utils.VerhoeffAlgorithm;
import io.intercom.com.google.gson.Gson;

/**
 * Created by shubhang on 18/03/16.
 */
public class ProfileFormStep1Fragment3 extends Fragment {
    private ArrayList<String> addressProofs;
    private Map<String, String> newaddressProofs;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    ImageUploaderRecyclerAdapter adapter;
    ArrayList<Uri> imageUris;
    private SharedPreferences mPrefs;
    private Gson gson;
    private UserModel user;
    private EditText editAadharNumber;
    private TextView aadharNuber, aadharPanHeader, editTextHeader, gotoFragment1, gotoFragment2, gotoFragment3;
    private Spinner aadharOrPan;
    String[] arrayAaadharOrPan;
    private CardView editTextCardView;
    private Button saveAndProceed, previous;
    private ImageView incompleteAadhar, completeAadhar, incompleteAddress, completeAddress, incompleteStep1, incompleteStep2, incompleteStep3;
    int currentSelected;
    private final int top = 16, left = 16, right = 16, bottom = 16;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    boolean deniedPermissionForever = false;
    private static final int REQUEST_PERMISSION_SETTING = 99;
    ImageView topImage;
    private ImageButton aadharHelptip, addressHelptip;
    boolean validAadhar = false;
    private LinearLayout incorrectFormat;
    private ImageButton editAadhar;
    private Button saveAadhar;
    private Button agreementBtn;
    public static ImageView incompleteAgreement, completeAgreement;
    private TextView uploadImageMsgTv;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(
                R.layout.profile_form_step1_fragment3, container, false);
        getAllViews(rootView);
        RecyclerView rvImages = (RecyclerView) rootView.findViewById(R.id.rvImages);
        mPrefs = getActivity().getSharedPreferences("buddy", Context.MODE_PRIVATE);
        mPrefs.edit().putBoolean("visitedFormStep1Fragment3", true).apply();
        gson = new Gson();
        String json = mPrefs.getString("UserObject", "");
        user = gson.fromJson(json, UserModel.class);
        newaddressProofs = new HashMap<>();

        try {
            addressProofs = user.getAddressProofs();
            if (addressProofs == null) {
                addressProofs = new ArrayList<>();
            } else {
                completeAddress.setVisibility(View.VISIBLE);
                user.setIncompletePermanentAddress(false);
                mPrefs.edit().putString("UserObject", json).apply();
            }
        } catch (Exception e) {
            addressProofs = new ArrayList<>();
        }
        if (!addressProofs.contains("add") && !user.isAppliedFor1k())
            addressProofs.add("add");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvImages.setLayoutManager(layoutManager);
        if (AppUtils.isNotEmpty(user.getSelfie()) && AppUtils.isNotEmpty(user.getSignature())) {
            completeAgreement.setVisibility(View.VISIBLE);
        }
        rvImages.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (addressProofs.get(position).equals("add")) {
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
        adapter = new ImageUploaderRecyclerAdapter(getActivity(), addressProofs, "Address Proofs", user.isAppliedFor1k());
        rvImages.setAdapter(adapter);

        if (!mPrefs.getBoolean("step1Editable", true)) {
            ProfileFormStep1Fragment1.setViewAndChildrenEnabled(rootView, false, gotoFragment1, gotoFragment2);
        }

        setAllHelpTipsEnabled();
        agreementBtn.setEnabled(true);
        agreementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel userSP = AppUtils.getUserObject(getActivity());

                if (AppUtils.isNotEmpty(userSP.getSignature())) {
                    user.setSignature(userSP.getSignature());
                    user.setUpdateSignature(userSP.isUpdateSignature());

                }

                if (AppUtils.isNotEmpty(userSP.getSelfie())) {
                    user.setSelfie(userSP.getSelfie());
                    user.setUpdateSelfie(userSP.isUpdateSelfie());

                }
                AppUtils.saveUserObject(getActivity(), user);
                Intent intent = new Intent(getActivity(), AgreementActivity.class);
                startActivity(intent);
            }
        });
        if (user.getGender() != null && "girl".equals(user.getGender())) {
            Picasso.with(getActivity())
                    .load(R.mipmap.step1fragment3girl)
                    .into(topImage);
        }

        gotoFragment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment1(true);
            }
        });

        gotoFragment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment2(true);
            }
        });

        arrayAaadharOrPan = getResources().getStringArray(R.array.aadhar_or_pan);
        if (user.getPanOrAadhar() != null && !"".equals(user.getPanOrAadhar())) {
            if ("Aadhar".equals(user.getPanOrAadhar()) && user.getAadharNumber() != null && !"".equals(user.getAadharNumber())) {
                editAadharNumber.setVisibility(View.GONE);
                aadharPanHeader.setText(arrayAaadharOrPan[0]);
                aadharNuber.setText(user.getAadharNumber());
                completeAadhar.setVisibility(View.VISIBLE);
                user.setIncompleteAadhar(false);
                aadharNuber.setVisibility(View.VISIBLE);
                editTextHeader.setVisibility(View.GONE);
                editTextCardView.setVisibility(View.GONE);
                aadharPanHeader.setVisibility(View.VISIBLE);
                editAadhar.setVisibility(View.VISIBLE);
                saveAadhar.setVisibility(View.GONE);
                editAadhar.setVisibility(View.GONE);
            } else if ("PAN".equals(user.getPanOrAadhar()) && user.getPanNumber() != null && !"".equals(user.getPanNumber())) {
                editAadharNumber.setVisibility(View.GONE);
                aadharPanHeader.setText(arrayAaadharOrPan[1]);
                aadharNuber.setText(user.getPanNumber());
                completeAadhar.setVisibility(View.VISIBLE);
                user.setIncompleteAadhar(false);
                aadharNuber.setVisibility(View.VISIBLE);
                editTextHeader.setVisibility(View.GONE);
                editTextCardView.setVisibility(View.GONE);
                aadharPanHeader.setVisibility(View.VISIBLE);
                editAadhar.setVisibility(View.VISIBLE);
                saveAadhar.setVisibility(View.GONE);
                editAadhar.setVisibility(View.GONE);
            }
        }


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.aadhar_or_pan, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        setOnClickListener();
        if (user.isAppliedFor1k()) {
            previous.setVisibility(View.INVISIBLE);
            saveAndProceed.setVisibility(View.INVISIBLE);
            rootView.findViewById(R.id.details_submitted_tv).setVisibility(View.VISIBLE);
        }
        aadharOrPan.setAdapter(adapter);

        if (AppUtils.isEmpty(user.getSignature()) || AppUtils.isEmpty(user.getSelfie())) {

            incompleteAgreement.setVisibility(View.VISIBLE);
        } else if (AppUtils.isNotEmpty(user.getSignature()) && AppUtils.isNotEmpty(user.getSelfie())) {

            completeAgreement.setVisibility(View.VISIBLE);
        }
        if (user.isIncompleteEmail() || user.isIncompleteFb() || user.isIncompleteGender()) {
            incompleteStep1.setVisibility(View.VISIBLE);
        }
        if (user.getCollegeIds() != null && user.getCollegeIds().size() > 0) {
            user.setIncompleteCollegeId(false);
        }
        if (user.isIncompleteCollegeId() || user.isIncompleteCollegeDetails() || user.isIncompleteRollNumber()) {
            incompleteStep2.setVisibility(View.VISIBLE);
        }
        if (user.isIncompleteAadhar() || user.isIncompletePermanentAddress() || (AppUtils.isEmpty(user.getSelfie()) || AppUtils.isEmpty(user.getSignature()))) {
            incompleteStep3.setVisibility(View.VISIBLE);
            if (user.isIncompleteAadhar())
                incompleteAadhar.setVisibility(View.VISIBLE);
            if (user.isIncompletePermanentAddress())
                incompleteAddress.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    private void setOnClickListener() {

        saveAndProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = AppUtils.getUserObject(getActivity());

                UserModel userSP = AppUtils.getUserObject(getActivity());
                if (AppUtils.isNotEmpty(userSP.getSignature())) {
                    user.setSignature(userSP.getSignature());
                    user.setUpdateSignature(userSP.isUpdateSignature());

                }

                if (AppUtils.isNotEmpty(userSP.getSelfie())) {
                    user.setSelfie(userSP.getSelfie());
                    user.setUpdateSelfie(userSP.isUpdateSelfie());

                }
                checkIncomplete();
                if ((user.isIncompleteEmail() || user.isIncompleteFb() || user.isIncompleteGender() || user.isIncompleteRollNumber()
                        || user.isIncompleteAadhar() || user.isIncompleteCollegeDetails()
                        || user.isIncompleteCollegeId() || user.isIncompletePermanentAddress())
                        && !mPrefs.getBoolean("skipIncompleteMessage", false) || !AppUtils.isNotEmpty(user.getSelfie()) || !AppUtils.isNotEmpty(user.getSignature())) {

                    final Dialog dialog1 = new Dialog(getActivity());
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setContentView(R.layout.incomplete_alert_box);

                    Button okay = (Button) dialog1.findViewById(R.id.okay_button);
                    okay.setTextColor(Color.parseColor("#44c2a6"));
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
                }
                String json = gson.toJson(user);
                mPrefs.edit().putBoolean("updatingDB", false).apply();
                mPrefs.edit().putString("UserObject", json).apply();
                Context context = getActivity();
                Intent intent = new Intent(context, CheckInternetAndUploadUserDetails.class);
                getContext().sendBroadcast(intent);

                Intent intent1 = new Intent(getActivity(), PendingFlashApprovalActivity.class);
                startActivity(intent1);

                getActivity().finish();
            }
        });
        aadharHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "We require a photo or scan of a Govt. approved identity proof, either PAN card or Aadhar card to approve a Credit Limit for you.<br><br>" +
                        "Remember to upload both (front and back) sides of the document.<br>"
                        + "If you don’t have your PAN card, "
                        + "<a href=\"https://tin.tin.nsdl.com/pan/\">get one here</a>" + "<br><br>If you don’t have your Aadhar card, " +
                        "<a href=\"https://aadharcarduid.com/aadhaar-card-apply-online\">get one here</a>";
                String text2 = "";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your College ID", text1, text2, "#44c2a6");
                dialog.show();
            }
        });

        addressHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1;

                if (aadharOrPan.getSelectedItemPosition() == 0) {
                    text1 = "Upload photos or scans of your Aadhar Card registered in your name and with the Aadhar number you have provided.";

                } else {
                    text1 = "Upload either your DL, Aadhar Card or Passports’ softcopy. Or, you could upload one of your parents' Permanent Address Proof.<br>You can upload either photos or scanned copies of these documents.";

                }
                String text2 = "Please remember to upload both front and back sides of the card.";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your College ID", text1, text2, "#44c2a6");
                dialog.show();
            }
        });

        aadharOrPan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    uploadImageMsgTv.setText("Upload your Aadhar Proof");
                } else {
                    uploadImageMsgTv.setText("Upload your Permanent Address Proof");
                }
                try {
                    ((TextView) parent.getChildAt(0)).setText(arrayAaadharOrPan[position]);
                    editAadharNumber.setHint(arrayAaadharOrPan[position]);
                    user.setPanOrAadhar(arrayAaadharOrPan[position].split(" ")[0]);
                    currentSelected = position;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentSelected = 99;
                ((TextView) parent.getChildAt(0)).setText(arrayAaadharOrPan[0]);
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment2(true);
            }
        });
        saveAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAadharorPan();
            }
        });
        editAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelected == 0) {
                    editAadharNumber.setHint("Aadhar Number");
                } else {
                    editAadharNumber.setHint("PAN Number");
                }
                aadharNuber.setVisibility(View.GONE);
                editAadhar.setVisibility(View.GONE);
                saveAadhar.setVisibility(View.VISIBLE);
                editAadharNumber.setVisibility(View.VISIBLE);
            }
        });
        editAadharNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                incorrectFormat.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void getAllViews(View rootView) {
        uploadImageMsgTv = (TextView) rootView.findViewById(R.id.address_proof_header);
        completeAgreement = (ImageView) rootView.findViewById(R.id.complete_agreement);
        incompleteAgreement = (ImageView) rootView.findViewById(R.id.incomplete_agreement);
        completeAddress = (ImageView) rootView.findViewById(R.id.complete_address_proof);
        aadharPanHeader = (TextView) rootView.findViewById(R.id.aadhar_pan_header);
        editTextHeader = (TextView) rootView.findViewById(R.id.editext_header);
        editTextCardView = (CardView) rootView.findViewById(R.id.edittext_carview);
        saveAndProceed = (Button) rootView.findViewById(R.id.save_and_proceed);
        editAadharNumber = (EditText) rootView.findViewById(R.id.edit_aadhar_number);
        incompleteAadhar = (ImageView) rootView.findViewById(R.id.incomplete_aadhar);
        completeAadhar = (ImageView) rootView.findViewById(R.id.complete_aadhar);
        incompleteAddress = (ImageView) rootView.findViewById(R.id.incomplete_address_proof);
        gotoFragment1 = (TextView) rootView.findViewById(R.id.goto_fragment1);
        gotoFragment2 = (TextView) rootView.findViewById(R.id.goto_fragment2);
        gotoFragment3 = (TextView) rootView.findViewById(R.id.goto_fragment3);
        agreementBtn = (Button) rootView.findViewById(R.id.agreement_btn);
        currentSelected = 0;
        incompleteStep1 = (ImageView) rootView.findViewById(R.id.incomplete_step_1);
        incompleteStep2 = (ImageView) rootView.findViewById(R.id.incomplete_step_2);
        incompleteStep3 = (ImageView) rootView.findViewById(R.id.incomplete_step_3);
        previous = (Button) rootView.findViewById(R.id.previous);
        topImage = (ImageView) rootView.findViewById(R.id.verify_image_view2);
        aadharHelptip = (ImageButton) rootView.findViewById(R.id.aadhar_helptip);
        addressHelptip = (ImageButton) rootView.findViewById(R.id.address_helptip);
        incorrectFormat = (LinearLayout) rootView.findViewById(R.id.incorrect_format_layout);
        saveAadhar = (Button) rootView.findViewById(R.id.save_user_aadhar);
        editAadhar = (ImageButton) rootView.findViewById(R.id.edit_user_aadhar);
        aadharNuber = (TextView) rootView.findViewById(R.id.aadhar_number);
        aadharOrPan = (Spinner) rootView.findViewById(R.id.aadhar_or_pan_spinner);
    }

    private void setAllHelpTipsEnabled() {

        aadharHelptip.setEnabled(true);
        addressHelptip.setEnabled(true);
    }

    private void saveAadharorPan() {
        String text = editAadharNumber.getText().toString().trim();
        if (!"".equals(text) && text.length() > 0) {
            if (currentSelected == 0) {
                validAadhar = validateAadharNumber(text);
            } else {
                validAadhar = validatePAnNumber(text);
            }
            if (!validAadhar) {
                incorrectFormat.setVisibility(View.VISIBLE);
            } else {
                incompleteAadhar.setVisibility(View.GONE);
                incorrectFormat.setVisibility(View.GONE);
                aadharNuber.setText(text);
                aadharNuber.setVisibility(View.VISIBLE);
                editAadhar.setVisibility(View.VISIBLE);
                editAadharNumber.setVisibility(View.GONE);
                saveAadhar.setVisibility(View.GONE);
                if (currentSelected == 0) {
                    user.setAadharNumber(text);
                    user.setPanOrAadhar("Aadhar");
                    user.setUpdateAadharNumber(true);
                } else {
                    user.setPanNumber(text.toUpperCase());
                    user.setUpdatePanNumber(true);
                    user.setPanOrAadhar("PAN");
                }
                String json = gson.toJson(user);
                mPrefs.edit().putString("UserObject", json).apply();
                Intent intent = new Intent(getContext(), CheckInternetAndUploadUserDetails.class);
                getContext().sendBroadcast(intent);
            }
        } else {
            aadharNuber.setVisibility(View.VISIBLE);
            editAadhar.setVisibility(View.VISIBLE);
            editAadharNumber.setVisibility(View.GONE);
            saveAadhar.setVisibility(View.GONE);
        }
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void checkIncomplete() {
        if (aadharNuber.getVisibility() == View.GONE) {
            user.setIncompleteAadhar(true);
        } else {
            user.setIncompleteAadhar(false);
        }
        if (addressProofs.size() <= 1) {
            incompleteAddress.setVisibility(View.VISIBLE);
            user.setIncompletePermanentAddress(true);
        } else {
            addressProofs.remove(addressProofs.size() - 1);
            user.setAddressProofs(addressProofs);
            user.setIncompletePermanentAddress(false);
        }
    }

    public static boolean validateAadharNumber(String aadharNumber) {
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if (isValidAadhar) {
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }

    public static boolean validatePAnNumber(String pan) {
        Pattern pattern = Pattern.compile("[A-Z]{3}[P][A-Z][0-9]{4}[A-Z]");

        Matcher matcher = pattern.matcher(pan);
        if (matcher.matches()) {
            Log.i("Matching", "Yes");
            return true;
        }
        return false;
    }

    public void replaceFragment1(boolean check) {
        if (check)
            checkIncomplete();
        String json = gson.toJson(user);
        mPrefs.edit().putString("UserObject", json).apply();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment1, new ProfileFormStep1Fragment1(), "Fragment1Tag");
        ft.commit();
    }

    public void replaceFragment2(boolean check) {
        if (check)
            checkIncomplete();
        String json = gson.toJson(user);
        mPrefs.edit().putString("UserObject", json).apply();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment1, new ProfileFormStep1Fragment2(), "Fragment2Tag");
        ft.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK) {
            if (user.getAddressProofs() == null)
                user.setAddressProofs(new ArrayList<String>());
            imageUris = intent.getParcelableArrayListExtra(ImageHelperActivity.EXTRA_IMAGE_URIS);
            for (Uri uri : imageUris) {
                addressProofs.add(0, uri.getPath());
                newaddressProofs.put(uri.getPath(), AppUtils.uploadStatus.OPEN.toString());
                //                user.addAddressProofs(0, uri.getPath(), user.getAddressProofs());
                //                adapter.notifyItemInserted(0);
            }
            adapter.notifyDataSetChanged();
            user.setNewAddressProofs(newaddressProofs);
            user.setUpdateNewAddressProofs(true);
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
            Intent intent = new Intent(getActivity(), ImageHelperActivity.class);
            startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
