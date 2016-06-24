package indwin.c3.shareapp.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.activities.AgreementActivity;
import indwin.c3.shareapp.activities.ImageHelperActivity;
import indwin.c3.shareapp.activities.ProfileFormStep1;
import indwin.c3.shareapp.adapters.SpinnerHintAdapter;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.HelpTipDialog;

/**
 * Created by ROCK
 */
public class ProfileFormStep1Fragment4 extends Fragment {
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private UserModel user;
    int currentSelected;
    private ImageButton addressHelptip;
    private Button agreementBtn;
    public static ImageView incompleteAgreement, completeAgreement;
    private EditText cityEt, pinCodeEt, houseNoEt, streetEt;
    RelativeLayout currentAddressLayout;
    private TextView editCurrentAddress, editPermanentAddress;
    boolean selectedPlaceOfStay = false;
    ImageView incompleteAddressDetails, completeAddressDetails;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(
                R.layout.profile_form_step1_fragment4, container, false);
        getAllViews(rootView);
        setOnClickListener();
        ProfileFormStep1 profileFormStep1 = (ProfileFormStep1) getActivity();
        user = profileFormStep1.getUser();
        final String placesToStay[] = getResources().getStringArray(R.array.places_of_stay);
        final String placesToStayValues[] = getResources().getStringArray(R.array.places_of_stay_values);
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.place_of_stay);
        SpinnerHintAdapter adapter = new SpinnerHintAdapter(getActivity(), placesToStay, R.layout.spinner_item_underline);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < placesToStay.length - 1) {
                    selectedPlaceOfStay = true;
                    user.setAccommodation(placesToStayValues[position]);
                    user.setUpdateAccommodation(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setUpdateAccommodation(false);
            }
        });
        editPermanentAddress.setText(user.getFullPermanentAddress());
        editCurrentAddress.setText(user.getFullCurrentAddress());
        if (AppUtils.isNotEmpty(user.getAccommodation()) &&
                AppUtils.isNotEmpty(user.getCurrentAddress()) &&
                AppUtils.isNotEmpty(user.getPermanentAddress())
                ) {
            completeAddressDetails.setVisibility(View.VISIBLE);
            user.setIncompleteAddressDetails(false);
        }

        if (user.isIncompleteAddressDetails() && !user.isAppliedFor1k()) {
            incompleteAddressDetails.setVisibility(View.VISIBLE);
        }

        if (user.getAccommodation() != null && !"".equals(user.getAccommodation())) {
            for (int i = 0; i < placesToStay.length - 1; i++) {
                if (user.getAccommodation().equals(placesToStayValues[i])) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Rect rectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        currentAddressLayout.getLayoutParams().height = displaymetrics.heightPixels - statusBarHeight;
        if (user.isAppliedFor1k()) {
            ProfileFormStep1Fragment1.setViewAndChildrenEnabled(rootView, false);
        }
        setAllHelpTipsEnabled();
        agreementBtn.setEnabled(true);
        agreementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AgreementActivity.class);
                startActivity(intent);
            }
        });

        setOnClickListener();

        if (user.isInCompleteAgreement() && !user.isAppliedFor1k()) {

            incompleteAgreement.setVisibility(View.VISIBLE);
        } else if (AppUtils.isNotEmpty(user.getSelfie()) && AppUtils.isNotEmpty(user.getSignature()) && (user.isAppliedFor1k() || user.isTncAccepted())) {

            completeAgreement.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    private void setOnClickListener() {
        addressHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "Please enter the locality of both, your local address locality (the city where you study) as well as your permanent address locality (as on your permanent address proof)";
                String text2 = "";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your College ID", text1, text2, "#eeb85f");
                dialog.show();
            }
        });
        editCurrentAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddressLayout(0);
            }
        });
        editPermanentAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddressLayout(1);
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
        user.setTncAccepted(userSP.isTncAccepted());
    }

    private void getAllViews(View rootView) {
        completeAgreement = (ImageView) rootView.findViewById(R.id.complete_agreement);
        incompleteAgreement = (ImageView) rootView.findViewById(R.id.incomplete_agreement);
        agreementBtn = (Button) rootView.findViewById(R.id.agreement_btn);
        currentSelected = 0;
        incompleteAddressDetails = (ImageView) rootView.findViewById(R.id.incomplete_address);
        editCurrentAddress = (TextView) rootView.findViewById(R.id.edit_current_address);
        editPermanentAddress = (TextView) rootView.findViewById(R.id.edit_permanent_address);
        addressHelptip = (ImageButton) rootView.findViewById(R.id.address_helptip);

    }

    private void setAllHelpTipsEnabled() {

        addressHelptip.setEnabled(true);
    }


    public void checkIncomplete() {
        saveSelfieAndSignature();
        if (AppUtils.isEmpty(user.getSelfie()) || AppUtils.isEmpty(user.getSignature()) || !user.isTncAccepted()) {
            user.setInCompleteAgreement(true);
            incompleteAgreement.setVisibility(View.VISIBLE);
            completeAgreement.setVisibility(View.GONE);
        } else {
            user.setInCompleteAgreement(false);
            incompleteAgreement.setVisibility(View.GONE);
            completeAgreement.setVisibility(View.VISIBLE);
        }
        if (AppUtils.isEmpty(editPermanentAddress.getText().toString()) || !selectedPlaceOfStay) {
            user.setIncompleteAddressDetails(true);
            incompleteAddressDetails.setVisibility(View.VISIBLE);
            completeAddressDetails.setVisibility(View.GONE);
        } else {
            user.setIncompleteAddressDetails(false);
            completeAddressDetails.setVisibility(View.VISIBLE);
            incompleteAddressDetails.setVisibility(View.GONE);
        }

    }


    private void openAddressLayout(int i) {
        UserModel userModel = AppUtils.getUserObject(getActivity());
        final Dialog dialogView = new Dialog(getActivity(), android.R.style.Theme_Light);
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogView.setContentView(R.layout.address_layout);
        Toolbar toolbar = (Toolbar) dialogView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);


        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_IN);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();
            }

        });
        houseNoEt = (EditText) dialogView.findViewById(R.id.house_no_et);
        streetEt = (EditText) dialogView.findViewById(R.id.street_et);
        cityEt = (EditText) dialogView.findViewById(R.id.city_et);
        pinCodeEt = (EditText) dialogView.findViewById(R.id.pincode_et);
        TextView headerTitle = (TextView) dialogView.findViewById(R.id.activity_header);
        boolean submitEnabled = true;
        if (i == 0) {

            headerTitle.setText("Current Address");
            if (AppUtils.isNotEmpty(userModel.getCurrentAddress()))
                houseNoEt.setText(userModel.getCurrentAddress());
            else submitEnabled = false;
            if (AppUtils.isNotEmpty(userModel.getCurrentAddressLine2()))
                streetEt.setText(userModel.getCurrentAddressLine2());
            else submitEnabled = false;
            if (AppUtils.isNotEmpty(userModel.getCurrentAddressCity()))
                cityEt.setText(userModel.getCurrentAddressCity());
            else submitEnabled = false;
            if (AppUtils.isNotEmpty(userModel.getCurrentAddressPinCode()))
                pinCodeEt.setText(userModel.getCurrentAddressPinCode());
            else submitEnabled = false;

        } else {
            headerTitle.setText("Permanent Address");
            if (AppUtils.isNotEmpty(userModel.getPermanentAddress()))
                houseNoEt.setText(userModel.getPermanentAddress());
            else submitEnabled = false;
            if (AppUtils.isNotEmpty(userModel.getPermanentAddressLine2()))
                streetEt.setText(userModel.getPermanentAddressLine2());
            else submitEnabled = false;
            if (AppUtils.isNotEmpty(userModel.getPermanentAddressCity()))
                cityEt.setText(userModel.getPermanentAddressCity());
            else submitEnabled = false;
            if (AppUtils.isNotEmpty(userModel.getPermanentAddressPinCode()))
                pinCodeEt.setText(userModel.getPermanentAddressPinCode());
            else submitEnabled = false;
        }
        final Button submit = (Button) dialogView.findViewById(R.id.submit_address);
        if (submitEnabled) {
            submit.setAlpha(1);
        } else {
            submit.setAlpha(0.5f);
        }
        submit.setEnabled(submitEnabled);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (AppUtils.isEmpty(houseNoEt.getText().toString()) || AppUtils.isEmpty(streetEt.getText().toString()) || AppUtils.isEmpty(cityEt.getText().toString()) || AppUtils.isEmpty(pinCodeEt.getText().toString())) {
                    submit.setEnabled(false);
                    submit.setAlpha(0.5f);

                } else {
                    submit.setEnabled(true);
                    submit.setAlpha(1);
                }

            }
        };
        houseNoEt.addTextChangedListener(textWatcher);
        streetEt.addTextChangedListener(textWatcher);
        cityEt.addTextChangedListener(textWatcher);
        pinCodeEt.addTextChangedListener(textWatcher);

        dialogView.show();


        submit.setTag(i);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer i = (Integer) v.getTag();
                UserModel userModel = AppUtils.getUserObject(getActivity());

                if (i == 0) {
                    userModel.setCurrentAddressLine2(streetEt.getText().toString());
                    userModel.setCurrentAddressCity(cityEt.getText().toString());
                    userModel.setCurrentAddressPinCode(pinCodeEt.getText().toString());
                    userModel.setCurrentAddress(houseNoEt.getText().toString());
                    userModel.setUpdateCurrentAddress(true);
                    AppUtils.saveUserObject(getActivity(), userModel);
                    editCurrentAddress.setText(houseNoEt.getText().toString() + "," + streetEt.getText().toString() + "," + cityEt.getText().toString() + "," + pinCodeEt.getText().toString());
                } else {

                    userModel.setPermanentAddressLine2(streetEt.getText().toString());
                    userModel.setPermanentAddressCity(cityEt.getText().toString());
                    userModel.setPermanentAddressPinCode(pinCodeEt.getText().toString());
                    userModel.setPermanentAddress(houseNoEt.getText().toString());
                    userModel.setUpdatePermanentAddress(true);
                    AppUtils.saveUserObject(getActivity(), userModel);
                    editPermanentAddress.setText(houseNoEt.getText().toString() + "," + streetEt.getText().toString() + "," + cityEt.getText().toString() + "," + pinCodeEt.getText().toString());
                }
                dialogView.dismiss();
            }
        });
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
