package indwin.c3.shareapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.Views.DatePicker;
import indwin.c3.shareapp.adapters.PlaceAutocompleteAdapter;
import indwin.c3.shareapp.adapters.SpinnerHintAdapter;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.CheckInternetAndUploadUserDetails;
import indwin.c3.shareapp.utils.DaysDifferenceFinder;
import indwin.c3.shareapp.utils.HelpTipDialog;
import io.intercom.com.google.gson.Gson;

public class ProfileFormStep2Fragment1 extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    SharedPreferences mPrefs;
    TextView gotoFragment1, gotoFragment2, gotoFragment3;
    UserModel user;
    Gson gson;
    Button saveAndProceed;
    static EditText dobEditText;
    private TextView spinnerErrorTv;
    private final int top = 16, left = 16, right = 16, bottom = 16;
    ImageView incompleteStep1, incompleteStep2, incompleteStep3;
    private static DatePicker datePicker;
    static boolean updateUserDOB = false;
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
    ImageView incompleteDOB, completeDOB, incompleteAddressDetails, completeAddressDetails;
    ImageView topImage;
    private ImageButton addressHelptip;
    private TextView currentAddressHeading;
    private Button submitAddress;
    private Spinner gpaTypeSp;
    private EditText gpaValueEt;
    private boolean gpaTypeEntered, gpaValueEntered;
    private boolean gpaTypeUpdate, gpaValueUpdate;
    private String[] gpaTypeArray;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View rootView = inflater.inflate(
                R.layout.profile_form_step2_fragment1, container, false);
        String[] gpaArray = getResources().getStringArray(R.array.gpa_values);
        final String placesToStay[] = getResources().getStringArray(R.array.places_of_stay);
        final String placesToStayValues[] = getResources().getStringArray(R.array.places_of_stay_values);

        gpaTypeArray = getResources().getStringArray(R.array.gpa_type);
        mPrefs = getActivity().getSharedPreferences("buddy", Context.MODE_PRIVATE);
        getAllViews(rootView);


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
        if (!mPrefs.getBoolean("step2Editable", true)) {
            ProfileFormStep1Fragment1.setViewAndChildrenEnabled(rootView, false, gotoFragment2, gotoFragment3);
        }
        setAllHelpTipsEnabled();
        mPrefs.edit().putBoolean("visitedFormStep2Fragment1", true).apply();
        if (mPrefs.getBoolean("visitedFormStep2Fragment2", false)) {
            gotoFragment2.setAlpha(1);
            gotoFragment2.setClickable(true);
        }
        if (mPrefs.getBoolean("visitedFormStep2Fragment3", false)) {
            gotoFragment3.setAlpha(1);
            gotoFragment3.setClickable(true);
        }
        gson = new Gson();
        String json = mPrefs.getString("UserObject", "");
        user = gson.fromJson(json, UserModel.class);

        if (AppUtils.isNotEmpty(user.getGpa())) {
            if (!user.isAppliedFor7k()) {
                gpaValueEt.setEnabled(true);
            } else {
                gpaValueEt.setEnabled(false);
            }
            gpaValueEt.setText(user.getGpa());
            gpaValueEntered = true;
        }


        if (AppUtils.isNotEmpty(user.getGpaType())) {
            if (!user.isAppliedFor7k()) {
                gpaValueEt.setEnabled(true);
            }
            gpaTypeEntered = true;
            if (gpaArray[1].equalsIgnoreCase(user.getGpaType())) {
                gpaTypeSp.setSelection(1);
            } else if (gpaArray[2].equals(user.getGpaType()) || gpaTypeArray[2].equalsIgnoreCase(user.getGpaType())) {
                gpaTypeSp.setSelection(2);
            } else if (gpaArray[3].equals(user.getGpaType()) || gpaTypeArray[3].equalsIgnoreCase(user.getGpaType())) {
                gpaTypeSp.setSelection(3);
            } else {

                gpaTypeSp.setSelection(0);
            }
        }
        if (user.isAppliedFor7k()) {
            saveAndProceed.setVisibility(View.INVISIBLE);
            rootView.findViewById(R.id.details_submitted_tv).setVisibility(View.VISIBLE);
        }
        if (user.getGender() != null && "girl".equals(user.getGender())) {
            Picasso.with(getActivity())
                    .load(R.mipmap.step2fragment1girl)
                    .into(topImage);
        }
        googleCurrentAddress.setOnItemClickListener(mAutocompleteClickListener);

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

        final Spinner spinner = (Spinner) rootView.findViewById(R.id.place_of_stay);
        SpinnerHintAdapter adapter = new SpinnerHintAdapter(getActivity(), placesToStay, R.layout.spinner_item_underline);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gpaValueEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = validateGpa(gpaValueEt.getText().toString());
                spinnerErrorTv.setText(text);
                if (!AppUtils.isNotEmpty(text)) {
                    gpaTypeUpdate = true;
                    gpaValueUpdate = true;
                }
            }
        });


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
        setOnClickListerner();

        if (user.getDob() != null && !"".equals(user.getDob())) {
            dobEditText.setText(user.getDob());
            completeDOB.setVisibility(View.VISIBLE);
            user.setIncompleteDOB(false);
        }
        if (user.getAccommodation() != null && !"".equals(user.getAccommodation())) {
            for (int i = 0; i < placesToStay.length - 1; i++) {
                if (user.getAccommodation().equals(placesToStayValues[i])) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
        if (user.getCurrentAddress() != null && !"".equals(user.getCurrentAddress())) {
            editCurrentAddress.setText(user.getCurrentAddress());
        }
        if (user.getPermanentAddress() != null && !"".equals(user.getPermanentAddress())) {
            editPermanentAddress.setText(user.getPermanentAddress());
        }
        if (user.getAccommodation() != null && !"".equals(user.getAccommodation()) &&
                user.getCurrentAddress() != null && !"".equals(user.getCurrentAddress()) &&
                user.getPermanentAddress() != null && !"".equals(user.getPermanentAddress())
                ) {
            completeAddressDetails.setVisibility(View.VISIBLE);
            user.setIncompleteAddressDetails(false);
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Rect rectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        currentAddressLayout.getLayoutParams().height = displaymetrics.heightPixels - statusBarHeight;


        datePicker = new

                DatePicker(getActivity(),

                "DOB");
        datePicker.build(new DialogInterface.OnClickListener()

                         {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                             }
                         }

                , null);


        if (user.isIncompleteDOB() || user.isIncompleteAddressDetails() || !(gpaValueEntered && gpaTypeEntered))

        {
            incompleteStep1.setVisibility(View.VISIBLE);
            if (user.isIncompleteDOB()) {
                incompleteDOB.setVisibility(View.VISIBLE);
            }
            if (user.isIncompleteAddressDetails()) {
                incompleteAddressDetails.setVisibility(View.VISIBLE);
            }
        }

        if (user.isIncompleteFamilyDetails())

        {
            incompleteStep2.setVisibility(View.VISIBLE);
        }

        if (user.isIncompleteRepaymentSetup() || user.isIncompleteClassmateDetails()
                || user.isIncompleteVerificationDate() || AppUtils.isEmpty(user.getStudentLoan()))

        {
            incompleteStep3.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void setOnClickListerner() {
        closeCurrentAddressLayout.setOnClickListener(new View.OnClickListener()

                                                     {
                                                         @Override
                                                         public void onClick(View v) {
                                                             hideCurrentAddressLayout();
                                                         }
                                                     }

        );
        gpaTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    gpaValueEt.setEnabled(false);
                    spinnerErrorTv.setText("");
                } else {
                    if (!user.isAppliedFor7k())
                        gpaValueEt.setEnabled(true);
                    spinnerErrorTv.setText(validateGpa(gpaValueEt.getText().toString()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
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
                currentAddressLayout.setVisibility(View.VISIBLE);
                isEditingCurrentAddress = true;
                currentAddressHeading.setText("Enter your Current Address Locality");
                googleCurrentAddress.setHint("Current Address Locality");
                googleCurrentAddress.setText(editCurrentAddress.getText());
                googleCurrentAddress.requestFocus();
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(googleCurrentAddress, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        editPermanentAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAddressLayout.setVisibility(View.VISIBLE);
                isEditingCurrentAddress = false;
                googleCurrentAddress.setHint("Permanent Address Locality");
                googleCurrentAddress.setText(editPermanentAddress.getText());
                currentAddressHeading.setText("Enter your Permanent Address Locality");
                googleCurrentAddress.requestFocus();
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(googleCurrentAddress, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        gotoFragment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment2(true);
            }
        });

        gotoFragment3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment3(true);
            }
        });
        saveAndProceed.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  boolean readyToUpdate = true;
                                                  if (isUserRejected) {
                                                      readyToUpdate = false;
                                                  }
                                                  checkIncomplete();


                                                  if (gpaValueEntered && gpaValueUpdate) {
                                                      user.setGpaValueUpdate(true);
                                                      user.setGpa(gpaValueEt.getText().toString());
                                                  }
                                                  if (gpaTypeEntered && gpaTypeUpdate) {
                                                      user.setGpaType(gpaTypeArray[gpaTypeSp.getSelectedItemPosition()]);
                                                      user.setGpaTypeUpdate(true);
                                                  }
                                                  if (updateUserDOB) {
                                                      try {
                                                          SimpleDateFormat spf = new SimpleDateFormat("dd MMM yyyy");
                                                          Date newDate = spf.parse(dobEditText.getText().toString());
                                                          spf = new SimpleDateFormat("yyyy-MM-dd");
                                                          user.setDob(spf.format(newDate));
                                                          user.setUpdateDOB(true);

                                                          Calendar endCalendar = new GregorianCalendar();
                                                          endCalendar.setTime(new Date());
                                                          Calendar startCalendar = new GregorianCalendar();
                                                          startCalendar.setTime(newDate);

                                                          int age = DaysDifferenceFinder.getDifferenceBetweenDatesInYears(startCalendar, endCalendar);
                                                          if (age < 18) {
                                                              isUnderAge = true;
                                                              readyToUpdate = false;
                                                          }
                                                      } catch (Exception e) {
                                                          e.printStackTrace();
                                                      }
                                                  }
                                                  replaceFragment2(false);
                                                  String json = gson.toJson(user);
                                                  mPrefs.edit().putString("UserObject", json).apply();
                                                  Intent intent = new Intent(getActivity(), CheckInternetAndUploadUserDetails.class);
                                                  getContext().sendBroadcast(intent);
                                              }
                                          }

        );

        dobEditText.setOnClickListener(new View.OnClickListener()

                                       {
                                           @Override
                                           public void onClick(View v) {
                                               datePicker.show();
                                           }
                                       }

        );

    }

    private void getAllViews(View rootView) {
        gpaTypeSp = (Spinner) rootView.findViewById(R.id.gpa_spinner);
        spinnerErrorTv = (TextView) rootView.findViewById(R.id.spinner_error_msg_tv);
        gpaValueEt = (EditText) rootView.findViewById(R.id.gpa_value);
        gotoFragment1 = (TextView) rootView.findViewById(R.id.goto_fragment1);
        gotoFragment2 = (TextView) rootView.findViewById(R.id.goto_fragment2);
        gotoFragment3 = (TextView) rootView.findViewById(R.id.goto_fragment3);
        saveAndProceed = (Button) rootView.findViewById(R.id.save_and_proceed);
        incompleteStep1 = (ImageView) rootView.findViewById(R.id.incomplete_step_1);
        incompleteStep2 = (ImageView) rootView.findViewById(R.id.incomplete_step_2);
        incompleteStep3 = (ImageView) rootView.findViewById(R.id.incomplete_step_3);
        dobEditText = (EditText) rootView.findViewById(R.id.user_dob_edittext);
        incompleteAddressDetails = (ImageView) rootView.findViewById(R.id.incomplete_address);
        completeAddressDetails = (ImageView) rootView.findViewById(R.id.complete_address);
        completeDOB = (ImageView) rootView.findViewById(R.id.complete_dob);
        incompleteDOB = (ImageView) rootView.findViewById(R.id.incomplete_dob);
        googleCurrentAddress = (AutoCompleteTextView) rootView.findViewById(R.id.google_current_address_autocomplete);
        topImage = (ImageView) rootView.findViewById(R.id.verify_image_view2);

        editCurrentAddress = (TextView) rootView.findViewById(R.id.edit_current_address);
        editPermanentAddress = (TextView) rootView.findViewById(R.id.edit_permanent_address);
        currentAddressLayout = (RelativeLayout) rootView.findViewById(R.id.current_address_layout);
        addressHelptip = (ImageButton) rootView.findViewById(R.id.address_helptip);
        currentAddressHeading = (TextView) rootView.findViewById(R.id.current_address_heading);
        submitAddress = (Button) rootView.findViewById(R.id.submit_address);
        closeCurrentAddressLayout = (ImageButton) rootView.findViewById(R.id.close_current_address_layout);
    }

    private void setAllHelpTipsEnabled() {
        addressHelptip.setEnabled(true);
    }

    private void checkIncomplete() {
        if ("".equals(editCurrentAddress.getText().toString()) || "".equals(editPermanentAddress.getText().toString()) || !selectedPlaceOfStay) {
            user.setIncompleteAddressDetails(true);
        } else {
            user.setIncompleteAddressDetails(false);
        }
        if ("".equals(dobEditText.getText().toString())) {
            user.setIncompleteDOB(true);
        } else
            user.setIncompleteDOB(false);
    }


    public void replaceFragment2(boolean check) {
        if (check)
            checkIncomplete();
        String json = gson.toJson(user);
        mPrefs.edit().putString("UserObject", json).apply();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment1, new ProfileFormStep2Fragment2(), "Fragment2Tag");
        ft.commit();
    }

    public void replaceFragment3(boolean check) {
        if (check)
            checkIncomplete();
        String json = gson.toJson(user);
        mPrefs.edit().putString("UserObject", json).apply();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment1, new ProfileFormStep2Fragment3(), "Fragment3Tag");
        ft.commit();
    }

    public String validateGpa(String gpa) {
        gpaTypeEntered = false;
        gpaValueEntered = false;
        if (AppUtils.isNotEmpty(gpa)) {
            try {
                Double gpaDouble = Double.parseDouble(gpa);
                int position = gpaTypeSp.getSelectedItemPosition();
                if (position == 1) {
                    if (gpaDouble > 100)
                        return "Enter valid  value";
                    else {
                        gpaTypeEntered = true;
                        gpaValueEntered = true;
                        return "";
                    }
                } else if (position == 2) {
                    if (gpaDouble > 5)
                        return "Enter valid  value";
                    else {
                        gpaTypeEntered = true;
                        gpaValueEntered = true;
                        return "";
                    }
                } else if (position == 3) {
                    if (gpaDouble > 10)
                        return "Enter valid  value";
                    else {
                        gpaTypeEntered = true;
                        gpaValueEntered = true;
                        return "";
                    }
                } else return "";
            } catch (Exception e) {
                return "Invalid format";
            }

        } else if (AppUtils.isEmpty(gpa)) {
            return "";
        } else {
            return "Invalid format";
        }

    }

    public static void confirmDOB() {
        String date = datePicker.getSelectedDate() + " " + datePicker.getSelectedMonthName() + " " + datePicker.getSelectedYear();
        dobEditText.setText(date);
        updateUserDOB = true;
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
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO(Developer): Check error code and notify the user of error state and resolution.
    }
}