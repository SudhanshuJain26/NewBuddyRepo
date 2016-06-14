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
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.activities.FullScreenActivity;
import indwin.c3.shareapp.activities.ImageHelperActivity;
import indwin.c3.shareapp.activities.ProfileFormStep1;
import indwin.c3.shareapp.adapters.ImageUploaderRecyclerAdapter;
import indwin.c3.shareapp.models.FrontBackImage;
import indwin.c3.shareapp.models.Image;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.Constants;
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
    String[] arrayAaadharOrPan, addressTypeArray;
    private CardView editTextCardView;
    private Button saveAndProceed, previous;
    private ImageView incompleteAadhar, completeAadhar, completeAadhar1, incompleteAddress, completeAddress, incompleteStep1, incompleteStep2, incompleteStep3;
    int currentSelected;
    private final int top = 16, left = 16, right = 16, bottom = 16;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    boolean deniedPermissionForever = false;
    private static final int REQUEST_PERMISSION_SETTING = 99;
    ImageView topImage;
    private ImageButton aadharHelptip, addressHelptip, addPanHelptip;
    boolean validAadhar = false;
    private LinearLayout incorrectFormat;
    private ImageButton editAadhar;
    private Button saveAadhar;
    private TextView uploadImageMsgTv;
    private Image addressProof;
    private int clickedPosition;
    private Spinner addressTypeSp;
    private ImageView addPanImage;
    private String typeImage;
    private RelativeLayout panRL;
    private LinearLayout panImageLL;
    private CardView addressProofCv;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(
                R.layout.profile_form_step1_fragment3, container, false);
        getAllViews(rootView);
        arrayAaadharOrPan = getResources().getStringArray(R.array.aadhar_or_pan);
        addressTypeArray = getResources().getStringArray(R.array.address_proof_type);
        RecyclerView rvImages = (RecyclerView) rootView.findViewById(R.id.rvImages);
        mPrefs = getActivity().getSharedPreferences("buddy", Context.MODE_PRIVATE);
        mPrefs.edit().putBoolean("visitedFormStep1Fragment3", true).apply();
        gson = new Gson();
        String json = mPrefs.getString("UserObject", "");
        ProfileFormStep1 profileFormStep1 = (ProfileFormStep1) getActivity();
        user = profileFormStep1.getUser();
        newaddressProofs = new HashMap<>();

        try {
            addressProofs = user.getAddressProofs();
            if (user.getAddressProof() == null) {
                user.setAddressProof(new Image());
            } else if (user.getAddressProof().getFront() != null && AppUtils.isNotEmpty(user.getAddressProof().getFront().getImgUrl())) {
                completeAddress.setVisibility(View.VISIBLE);
                user.setIncompletePermanentAddress(false);
            }
        } catch (Exception e) {
            addressProofs = new ArrayList<>();
        }
        addressProof = user.getAddressProof();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvImages.setLayoutManager(layoutManager);
        rvImages.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        typeImage = Constants.IMAGE_TYPE.ADDRESS_PROOF.toString();
                        if (((position == 0 && (addressProof.getFront() == null || AppUtils.isEmpty(addressProof.getFront().getImgUrl()))) && !user.isAppliedFor1k()) || (position == 1 && (addressProof.getBack() == null || AppUtils.isEmpty(addressProof.getBack().getImgUrl()))) && !user.isAppliedFor1k()) {
                            clickedPosition = position;
                            String[] temp = hasPermissions(getActivity(), PERMISSIONS);
                            if (temp != null && temp.length != 0) {
                                deniedPermissionForever = true;
                                PERMISSIONS = temp;
                                requestPermissions(PERMISSIONS, ProfileFormStep1Fragment2.PERMISSION_ALL);
                            } else {
                                Intent intent = new Intent(getActivity(), ImageHelperActivity.class);
                                startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
                            }
                        } else {
                            Intent intent = new Intent(getActivity(), FullScreenActivity.class);

                            intent.putExtra(AppUtils.IMAGE_TYPE, Constants.IMAGE_TYPE.ADDRESS_PROOF.toString());
                            intent.putExtra(Constants.DISABLE_ADD, true);
                            intent.putExtra(AppUtils.POSITION, position);
                            intent.putExtra(AppUtils.HEADING, "Address Proof");
                            getActivity().startActivity(intent);
                        }
                    }
                })
        );
        adapter = new ImageUploaderRecyclerAdapter(getActivity(), addressProof, "Address Proofs", user.isAppliedFor1k(), Constants.IMAGE_TYPE.ADDRESS_PROOF.toString());
        rvImages.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapterAddressType = ArrayAdapter.createFromResource(getActivity(),
                R.array.address_proof_type, R.layout.spinner_item_green);
        adapterAddressType.setDropDownViewResource(R.layout.spinner_dropdown_item);
        addressTypeSp.setAdapter(adapterAddressType);
        if (user.isAppliedFor1k()) {
            ProfileFormStep1Fragment1.setViewAndChildrenEnabled(rootView, false);
        }


        if (user.getAddressProof() != null && AppUtils.isNotEmpty(user.getAddressProof().getType())) {
            for (int i = 0; i < addressTypeArray.length; i++) {
                if (user.getAddressProof().getType().equals(addressTypeArray[i])) {
                    addressTypeSp.setSelection(i);
                    break;
                }

            }

        }
        setAllHelpTipsEnabled();


        if (user.getPanOrAadhar() != null && !"".equals(user.getPanOrAadhar())) {
            if ("Aadhar".equals(user.getPanOrAadhar()) && user.getAadharNumber() != null && !"".equals(user.getAadharNumber())) {
                editAadharNumber.setVisibility(View.GONE);
                addressProofCv.setVisibility(View.GONE);
                aadharOrPan.setTag(0);
                if (user.isAppliedFor1k() && (user.getAddressProof() == null || AppUtils.isEmpty(user.getAddressProof().getType()))) {
                    uploadImageMsgTv.setText("Upload your Permanent Address Proof");
                } else uploadImageMsgTv.setText("Upload your Aadhar Proof");
                aadharPanHeader.setText(arrayAaadharOrPan[0]);
                aadharNuber.setText(user.getAadharNumber());
                completeAadhar1.setVisibility(View.VISIBLE);
                user.setIncompleteAadhar(false);
                aadharNuber.setVisibility(View.VISIBLE);
                editTextHeader.setVisibility(View.GONE);
                editTextCardView.setVisibility(View.GONE);
                aadharPanHeader.setVisibility(View.VISIBLE);
                saveAadhar.setVisibility(View.GONE);
                panImageLL.setVisibility(View.GONE);
            } else if ("PAN".equals(user.getPanOrAadhar()) && user.getPanNumber() != null && !"".equals(user.getPanNumber())) {
                editAadharNumber.setVisibility(View.GONE);
                addressProofCv.setVisibility(View.VISIBLE);
                aadharOrPan.setTag(1);
                aadharPanHeader.setText(arrayAaadharOrPan[1]);
                aadharNuber.setText(user.getPanNumber());
                completeAadhar1.setVisibility(View.VISIBLE);
                user.setIncompleteAadhar(false);
                uploadImageMsgTv.setText("Upload your Permanent Address Proof");
                aadharNuber.setVisibility(View.VISIBLE);
                editTextHeader.setVisibility(View.GONE);
                editTextCardView.setVisibility(View.GONE);
                aadharPanHeader.setVisibility(View.VISIBLE);
                saveAadhar.setVisibility(View.GONE);

                panImageLL.setVisibility(View.VISIBLE);
            }
        }

        if (!user.isAppliedFor1k()) {
            editAadhar.setVisibility(View.VISIBLE);
        } else {
            if (user.getAddressProof() == null || AppUtils.isEmpty(user.getAddressProof().getType())) {
                addressProofCv.setVisibility(View.GONE);
            }
            editAadhar.setVisibility(View.GONE);
        }
        if (user.getPanProof() != null && user.getPanProof().getImgUrl() != null) {
            File file = new File(user.getPanProof().getImgUrl());
            Picasso.with(getActivity()).load(file).fit().placeholder(R.drawable.downloading).into(addPanImage);

        } else {
            if (user.isAppliedFor1k()) {
                panImageLL.setVisibility(View.GONE);
            }
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.aadhar_or_pan, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);


        setOnClickListener();
        aadharOrPan.setAdapter(adapter);
        if (user.isIncompleteAadhar() || user.isIncompletePermanentAddress()) {
            if (user.isIncompleteAadhar() && !user.isAppliedFor1k())
                incompleteAadhar.setVisibility(View.VISIBLE);
            if (user.isIncompletePermanentAddress() && !user.isAppliedFor1k())
                incompleteAddress.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    private void setOnClickListener() {
        addPanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeImage = Constants.IMAGE_TYPE.PAN.toString();
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
        });

        addPanHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "Upload photos or scans of your PAN Card registered in your name and with the PAN number you have provided.";
                String text2 = "";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your Pan Card", text1, text2, "#44c2a6");
                dialog.show();
            }
        });
        aadharHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "Ensure that the PAN/Aadhar number you are submitting belongs to you and is registered in your name. <br><br>" +
                        "If you don’t have your PAN card, "
                        + "<a href=\"https://tin.tin.nsdl.com/pan/\">get one here</a>" + "<br><br>If you don’t have your Aadhar card, " +
                        "<a href=\"https://aadharcarduid.com/aadhaar-card-apply-online\">get one here</a>";
                String text2 = "";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your Aadhar Proof", text1, text2, "#44c2a6");
                dialog.show();
            }
        });

        addressHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1;

                if ((Integer) aadharOrPan.getTag() == 0) {
                    text1 = "Upload photos or scans of your Aadhar Card registered in your name and with the Aadhar number you have provided.";

                } else {
                    text1 = "Upload either your Driving Licence, Aadhar Card, Ration Card, Voter ID or Passports’ softcopy. Or, you could upload one of your parents' Permanent Address Proof. You can upload either photos or scanned copies of these documents. Remember to upload both (front and back) sides of the documents.";
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
                    addressProofCv.setVisibility(View.GONE);
                    uploadImageMsgTv.setText("Upload your Aadhar Proof");
                    addressTypeSp.setSelection(0);
                    panImageLL.setVisibility(View.GONE);
                    addressTypeSp.setEnabled(false);
                    aadharOrPan.setTag(0);
                } else {
                    addressProofCv.setVisibility(View.VISIBLE);
                    uploadImageMsgTv.setText("Upload your Permanent Address Proof");
                    addressTypeSp.setEnabled(true);
                    panImageLL.setVisibility(View.VISIBLE);
                    aadharOrPan.setTag(1);
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

        addressTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.buddy_green));
                if (user.getAddressProof() == null) {
                    user.setAddressProof(new Image());
                }

                user.getAddressProof().setType(addressTypeSp.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
                if ("Aadhar".equals(user.getPanOrAadhar()) && user.getAadharNumber() != null && !"".equals(user.getAadharNumber())) {
                    aadharOrPan.setSelection(0);
                    currentSelected = 0;
                    editAadharNumber.setText(user.getAadharNumber());
                } else if
                        ("PAN".equals(user.getPanOrAadhar()) && AppUtils.isNotEmpty(user.getPanNumber())) {
                    editAadharNumber.setText(user.getPanNumber());
                    aadharOrPan.setSelection(1);
                    currentSelected = 1;
                }
                if (currentSelected == 0) {
                    editAadharNumber.setHint("Aadhar Number");
                } else {
                    editAadharNumber.setHint("PAN Number");
                }
                aadharPanHeader.setVisibility(View.GONE);
                editTextHeader.setVisibility(View.VISIBLE);
                aadharNuber.setVisibility(View.GONE);
                editAadhar.setVisibility(View.GONE);
                completeAadhar1.setVisibility(View.GONE);
                saveAadhar.setVisibility(View.VISIBLE);
                editAadharNumber.setVisibility(View.VISIBLE);
                editTextCardView.setVisibility(View.VISIBLE);
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

    private void saveSelfieAndSignature() {
        UserModel userSP = AppUtils.getUserObject(getActivity());
        if (AppUtils.isNotEmpty(userSP.getSignature())) {
            user.setSignature(userSP.getSignature());
            user.setUpdateSignature(userSP.isUpdateSignature());

        }

        if (AppUtils.isNotEmpty(userSP.getSelfie())) {
            user.setSelfie(userSP.getSelfie());
            user.setUpdateSelfie(userSP.isUpdateSelfie());

        }
    }

    private void getAllViews(View rootView) {
        addressProofCv = (CardView) rootView.findViewById(R.id.address_proof_cv);
        panImageLL = (LinearLayout) rootView.findViewById(R.id.pan_image_ll);
        addPanImage = (ImageView) rootView.findViewById(R.id.addPanImage);
        uploadImageMsgTv = (TextView) rootView.findViewById(R.id.address_proof_header);
        completeAddress = (ImageView) rootView.findViewById(R.id.complete_address_proof);
        aadharPanHeader = (TextView) rootView.findViewById(R.id.aadhar_pan_header);
        editTextHeader = (TextView) rootView.findViewById(R.id.editext_header);
        editTextCardView = (CardView) rootView.findViewById(R.id.edittext_carview);
        previous = (Button) getActivity().findViewById(R.id.previous);
        editAadharNumber = (EditText) rootView.findViewById(R.id.edit_aadhar_number);
        incompleteAadhar = (ImageView) rootView.findViewById(R.id.incomplete_aadhar);
        completeAadhar = (ImageView) rootView.findViewById(R.id.complete_aadhar);
        completeAadhar1 = (ImageView) rootView.findViewById(R.id.complete_aadhar_1);
        incompleteAddress = (ImageView) rootView.findViewById(R.id.incomplete_address_proof);
        currentSelected = 0;
        incompleteStep1 = (ImageView) getActivity().findViewById(R.id.incomplete_step_1);
        incompleteStep2 = (ImageView) getActivity().findViewById(R.id.incomplete_step_2);
        incompleteStep3 = (ImageView) getActivity().findViewById(R.id.incomplete_step_3);
        topImage = (ImageView) rootView.findViewById(R.id.verify_image_view2);
        saveAndProceed = (Button) getActivity().findViewById(R.id.save_and_proceed);
        panRL = (RelativeLayout) rootView.findViewById(R.id.panRL);
        topImage = (ImageView) getActivity().findViewById(R.id.verify_image_view2);
        aadharHelptip = (ImageButton) rootView.findViewById(R.id.aadhar_helptip);
        addressHelptip = (ImageButton) rootView.findViewById(R.id.address_helptip);
        incorrectFormat = (LinearLayout) rootView.findViewById(R.id.incorrect_format_layout);
        saveAadhar = (Button) rootView.findViewById(R.id.save_user_aadhar);
        editAadhar = (ImageButton) rootView.findViewById(R.id.edit_user_aadhar);
        aadharNuber = (TextView) rootView.findViewById(R.id.aadhar_number);
        aadharOrPan = (Spinner) rootView.findViewById(R.id.aadhar_or_pan_spinner);
        addPanHelptip = (ImageButton) rootView.findViewById(R.id.addPanHelptip);
        addressTypeSp = (Spinner) rootView.findViewById(R.id.address_proof_type_spinner);
    }

    private void setAllHelpTipsEnabled() {

        aadharHelptip.setEnabled(true);
        addressHelptip.setEnabled(true);
        addPanHelptip.setEnabled(true);
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
                UserModel userModel = AppUtils.getUserObject(getActivity());
                if (currentSelected == 0) {
                    userModel.setAadharNumber(text);
                    userModel.setPanOrAadhar("Aadhar");
                    aadharPanHeader.setText("Aadhar");
                    userModel.setUpdateAadharNumber(true);
                    AppUtils.saveUserObject(getActivity(), userModel);
                    this.user.setAadharNumber(text);
                } else {
                    userModel.setPanNumber(text.toUpperCase());
                    userModel.setUpdatePanNumber(true);
                    this.user.setAadharNumber(text.toUpperCase());
                    userModel.setPanOrAadhar("PAN");
                    AppUtils.saveUserObject(getActivity(), userModel);
                    aadharPanHeader.setText("PAN");
                }
                aadharPanHeader.setVisibility(View.VISIBLE);
                completeAadhar1.setVisibility(View.VISIBLE);
                editTextHeader.setVisibility(View.GONE);
                editTextCardView.setVisibility(View.GONE);

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

    public void checkIncomplete() {

        if (aadharNuber.getVisibility() == View.GONE) {
            user.setIncompleteAadhar(true);
            incompleteAadhar.setVisibility(View.VISIBLE);
            completeAadhar.setVisibility(View.GONE);
        } else {
            user.setIncompleteAadhar(false);
            incompleteAadhar.setVisibility(View.GONE);
            if (aadharPanHeader.getVisibility() == View.GONE)
                completeAadhar.setVisibility(View.VISIBLE);
        }
        if (addressProof.getFront() == null || AppUtils.isEmpty(addressProof.getFront().getImgUrl())) {
            user.setIncompletePermanentAddress(true);
        } else {
            user.setIncompletePermanentAddress(false);
        }

        if (user.isIncompletePermanentAddress()) {

            incompleteAddress.setVisibility(View.VISIBLE);
            completeAddress.setVisibility(View.GONE);
        } else {
            completeAddress.setVisibility(View.VISIBLE);
            incompleteAddress.setVisibility(View.GONE);

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


    @Override
    public void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK) {
            UserModel userModel = AppUtils.getUserObject(getActivity());

            imageUris = intent.getParcelableArrayListExtra(ImageHelperActivity.EXTRA_IMAGE_URIS);
            if (Constants.IMAGE_TYPE.ADDRESS_PROOF.toString().equals(typeImage)) {
                if (userModel.getAddressProof() == null)
                    userModel.setAddressProof(new Image());
                FrontBackImage frontBackImage = new FrontBackImage();
                if (imageUris != null && imageUris.size() > 0) {
                    frontBackImage.setImgUrl(imageUris.get(0).getPath());
                    Image addressProof = userModel.getAddressProof();
                    if (clickedPosition == 0) {
                        addressProof.setFrontStatus(AppUtils.uploadStatus.OPEN.toString());
                        addressProof.setFront(frontBackImage);
                        addressProof.setUpdateFront(true);
                        this.addressProof.setFront(frontBackImage);
                    } else if (clickedPosition == 1) {
                        addressProof.setBackStatus(AppUtils.uploadStatus.OPEN.toString());
                        addressProof.setUpdateBack(true);
                        addressProof.setBack(frontBackImage);
                        this.addressProof.setBack(frontBackImage);
                    }
                    adapter.notifyDataSetChanged();
                    userModel.setUpdateNewAddressProofs(true);
                }
            } else {
                if (imageUris != null && imageUris.size() > 0) {
                    userModel.setUpdatePanProof(true);
                    userModel.setPanProof(new FrontBackImage());
                    userModel.setPanStatus(AppUtils.uploadStatus.OPEN.toString());
                    userModel.getPanProof().setImgUrl(imageUris.get(0).getPath());
                    File file = new File(imageUris.get(0).getPath());
                    Picasso.with(getActivity()).load(file).fit().placeholder(R.drawable.downloading).into(addPanImage);
                }
            }
            AppUtils.saveUserObject(getActivity(), userModel);

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
