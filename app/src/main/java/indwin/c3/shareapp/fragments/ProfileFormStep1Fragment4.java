package indwin.c3.shareapp.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.activities.AgreementActivity;
import indwin.c3.shareapp.activities.ImageHelperActivity;
import indwin.c3.shareapp.activities.ProfileFormStep1;
import indwin.c3.shareapp.adapters.ImageUploaderRecyclerAdapter;
import indwin.c3.shareapp.adapters.PlaceAutocompleteAdapter;
import indwin.c3.shareapp.adapters.SpinnerHintAdapter;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.HelpTipDialog;
import io.intercom.com.google.gson.Gson;

/**
 * Created by shubhang on 18/03/16.
 */
public class ProfileFormStep1Fragment4 extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
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
    private EditText cityEt, pinCodeEt, houseNoEt, streetEt;

    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mGooglePlaceAdapter;
    private static final LatLngBounds BOUNDS_GREATER_BANGALORE = new LatLngBounds(
            new LatLng(12.89201, 77.58905), new LatLng(12.97232, 77.59480));
    private static final Location BANGALORE_CENTER = new Location("Bangalore");
    private AutoCompleteTextView googleCurrentAddress;
    boolean isUserRejected = false;
    RelativeLayout currentAddressLayout;
    private TextView editCurrentAddress, editPermanentAddress;
    boolean isEditingCurrentAddress = false;
    private ImageButton closeCurrentAddressLayout;
    boolean selectedPlaceOfStay = false;
    boolean isUnderAge = false;
    ImageView incompleteAddressDetails, completeAddressDetails;
    private TextView currentAddressHeading;
    private Button submitAddress;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(
                R.layout.profile_form_step1_fragment4, container, false);
        getAllViews(rootView);
        setOnClickListener();
        mPrefs = getActivity().getSharedPreferences("buddy", Context.MODE_PRIVATE);
        mPrefs.edit().putBoolean("visitedFormStep1Fragment4", true).apply();
        ProfileFormStep1 profileFormStep1 = (ProfileFormStep1) getActivity();
        user = profileFormStep1.getUser();
        final String placesToStay[] = getResources().getStringArray(R.array.places_of_stay);
        final String placesToStayValues[] = getResources().getStringArray(R.array.places_of_stay_values);

        BANGALORE_CENTER.setLatitude(12.97232);
        BANGALORE_CENTER.setLongitude(77.59480);
        if (mGoogleApiClient == null)
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(Places.GEO_DATA_API)
                    .addOnConnectionFailedListener(this)
                    .build();
        mGooglePlaceAdapter = new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient, BOUNDS_GREATER_BANGALORE,
                null);
        googleCurrentAddress.setAdapter(mGooglePlaceAdapter);

        googleCurrentAddress.setOnItemClickListener(mAutocompleteClickListener);


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


        if (AppUtils.isNotEmpty(user.getSelfie()) && AppUtils.isNotEmpty(user.getSignature())) {
            completeAgreement.setVisibility(View.VISIBLE);
        }

        if (!mPrefs.getBoolean("step1Editable", true)) {
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

        if (user.isInCompleteAgreement()) {

            incompleteAgreement.setVisibility(View.VISIBLE);
        } else if (AppUtils.isNotEmpty(user.getSelfie()) && AppUtils.isNotEmpty(user.getSignature())) {

            completeAgreement.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void setOnClickListener() {


        addressHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "Please enter the locality of both, your local address locality (the city where you study) " +
                        "as well as your permanent address locality (as on your permanent address proof)";
                String text2 = "";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your College ID", text1, text2, "#eeb85f");
                dialog.show();
            }
        });

        closeCurrentAddressLayout.setOnClickListener(new View.OnClickListener()

                                                     {
                                                         @Override
                                                         public void onClick(View v) {
                                                             hideCurrentAddressLayout();
                                                         }
                                                     }

        );

        submitAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place = googleCurrentAddress.getText().toString();
                if (isEditingCurrentAddress) {
                    user.setCurrentAddress(place);
                    user.setUpdateCurrentAddress(true);
                    editCurrentAddress.setText(place);
                    editCurrentAddress.setFocusable(true);
                    editCurrentAddress.setFocusableInTouchMode(true);
                } else {
                    editPermanentAddress.setText(place);
                    editPermanentAddress.setFocusable(true);
                    editPermanentAddress.setFocusableInTouchMode(true);
                    user.setPermanentAddress(place);
                    user.setUpdatePermanentAddress(true);
                }
                hideCurrentAddressLayout();
            }
        });
        editCurrentAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddressLayout(0);


                //currentAddressLayout.setVisibility(View.VISIBLE);
                //isEditingCurrentAddress = true;
                //currentAddressHeading.setText("Enter your Current Address Locality");
                //googleCurrentAddress.setHint("Current Address Locality");
                //googleCurrentAddress.setText(editCurrentAddress.getText());
                //googleCurrentAddress.requestFocus();
                //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                //InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.showSoftInput(googleCurrentAddress, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        editPermanentAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddressLayout(1);
                //currentAddressLayout.setVisibility(View.VISIBLE);
                //isEditingCurrentAddress = false;
                //googleCurrentAddress.setHint("Permanent Address Locality");
                //googleCurrentAddress.setText(editPermanentAddress.getText());
                //currentAddressHeading.setText("Enter your Permanent Address Locality");
                //googleCurrentAddress.requestFocus();
                //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                //InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.showSoftInput(googleCurrentAddress, InputMethodManager.SHOW_IMPLICIT);
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
        completeAgreement = (ImageView) rootView.findViewById(R.id.complete_agreement);
        incompleteAgreement = (ImageView) rootView.findViewById(R.id.incomplete_agreement);
        completeAddress = (ImageView) rootView.findViewById(R.id.complete_address_proof);
        editTextHeader = (TextView) rootView.findViewById(R.id.editext_header);
        editTextCardView = (CardView) rootView.findViewById(R.id.edittext_carview);
        previous = (Button) getActivity().findViewById(R.id.previous);
        incompleteAddress = (ImageView) rootView.findViewById(R.id.incomplete_address_proof);
        agreementBtn = (Button) rootView.findViewById(R.id.agreement_btn);
        currentSelected = 0;
        incompleteAddressDetails = (ImageView) rootView.findViewById(R.id.incomplete_address);
        completeAddressDetails = (ImageView) rootView.findViewById(R.id.complete_address);

        googleCurrentAddress = (AutoCompleteTextView) rootView.findViewById(R.id.google_current_address_autocomplete);

        editCurrentAddress = (TextView) rootView.findViewById(R.id.edit_current_address);
        editPermanentAddress = (TextView) rootView.findViewById(R.id.edit_permanent_address);
        currentAddressLayout = (RelativeLayout) rootView.findViewById(R.id.current_address_layout);
        addressHelptip = (ImageButton) rootView.findViewById(R.id.address_helptip);
        currentAddressHeading = (TextView) rootView.findViewById(R.id.current_address_heading);
        submitAddress = (Button) rootView.findViewById(R.id.submit_address);
        closeCurrentAddressLayout = (ImageButton) rootView.findViewById(R.id.close_current_address_layout);
        addressHelptip = (ImageButton) rootView.findViewById(R.id.address_helptip);
    }

    private void setAllHelpTipsEnabled() {

        addressHelptip.setEnabled(true);
    }


    public void checkIncomplete() {
        saveSelfieAndSignature();
        if (AppUtils.isEmpty(user.getSelfie()) || AppUtils.isEmpty(user.getSignature())) {
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
                if (houseNoEt.getText().toString().isEmpty() || streetEt.getText().toString().isEmpty() || cityEt.getText().toString().isEmpty() || pinCodeEt.getText().toString().isEmpty()) {
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
                    editCurrentAddress.setText(houseNoEt.getText().toString() + "," + streetEt.getText().toString() + "," + cityEt.getText().toString() + "," + pinCodeEt.getText().toString());
                } else {

                    userModel.setPermanentAddressLine2(streetEt.getText().toString());
                    userModel.setPermanentAddressCity(cityEt.getText().toString());
                    userModel.setPermanentAddressPinCode(pinCodeEt.getText().toString());
                    userModel.setPermanentAddress(houseNoEt.getText().toString());
                    userModel.setUpdatePermanentAddress(true);
                    editPermanentAddress.setText(houseNoEt.getText().toString() + "," + streetEt.getText().toString() + "," + cityEt.getText().toString() + "," + pinCodeEt.getText().toString());

                }
                AppUtils.saveUserObject(getActivity(), userModel);
                dialogView.dismiss();


            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final AutocompletePrediction item = mGooglePlaceAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private void hideCurrentAddressLayout() {
        try {
            hideKeyboard();
            googleCurrentAddress.setText("");
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            currentAddressLayout.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                places.release();
                return;
            }
            final Place place = places.get(0);

            if (isEditingCurrentAddress) {
                Location location = new Location("home");
                location.setLatitude(place.getLatLng().latitude);
                location.setLongitude(place.getLatLng().longitude);
                float distance = location.distanceTo(BANGALORE_CENTER) / 1000;
                if (distance > 100) {
                    isUserRejected = true;
                } else {
                    isUserRejected = false;
                    try {
                        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                        user.setCurrentAddressCity(addresses.get(0).getLocality());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                user.setCurrentAddress(place.getAddress().toString());
                user.setUpdateCurrentAddress(true);
                editCurrentAddress.setText(place.getAddress().toString());
                editCurrentAddress.setFocusable(true);
                editCurrentAddress.setFocusableInTouchMode(true);
            } else {
                editPermanentAddress.setText(place.getAddress().toString());
                editPermanentAddress.setFocusable(true);
                editPermanentAddress.setFocusableInTouchMode(true);
                user.setPermanentAddress(place.getAddress().toString());
                user.setUpdatePermanentAddress(true);
            }
            hideCurrentAddressLayout();
            places.release();
        }
    };

    private void hideKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO(Developer): Check error code and notify the user of error state and resolution.
    }
}
