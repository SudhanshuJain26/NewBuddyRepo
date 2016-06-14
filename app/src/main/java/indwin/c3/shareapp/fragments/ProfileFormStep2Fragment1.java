package indwin.c3.shareapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.gun0912.tedpicker.ImagePickerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.activities.ImageHelperActivity;
import indwin.c3.shareapp.activities.ProfileFormStep2;
import indwin.c3.shareapp.adapters.ImageUploaderRecyclerAdapter;
import indwin.c3.shareapp.adapters.PlaceAutocompleteAdapter;
import indwin.c3.shareapp.adapters.SpinnerHintAdapter;
import indwin.c3.shareapp.models.Image;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.Constants;
import indwin.c3.shareapp.utils.HelpTipDialog;
import indwin.c3.shareapp.utils.RecyclerItemClickListener;
import io.intercom.com.google.gson.Gson;

public class ProfileFormStep2Fragment1 extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    public static final int PERMISSION_ALL = 0;
    private static final int REQUEST_PERMISSION_SETTING = 99;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    SharedPreferences mPrefs;
    ArrayList<Uri> imageUris;
    TextView gotoFragment1, gotoFragment2, gotoFragment3;
    UserModel user;
    Gson gson;
    Button saveAndProceed;
    private TextView spinnerErrorTv;
    private final int top = 16, left = 16, right = 16, bottom = 16;
    ImageView incompleteStep1, incompleteStep2, incompleteStep3;
    private ImageView incompleteAP, completeAP;
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
    ImageView topImage;
    private ImageButton addressHelptip;
    private TextView currentAddressHeading;
    private Button submitAddress;
    private Spinner gpaTypeSp;
    private EditText gpaValueEt;
    private boolean gpaTypeEntered, gpaValueEntered;
    private boolean gpaTypeUpdate, gpaValueUpdate;
    private String[] gpaTypeArray;
    private ImageView incompleteStudentLoan, completeStudentLoan;
    boolean selectedStudentLoan = false;
    private ArrayList<String> marksheets;
    private Map<String, String> newMarksheets;
    private RecyclerView rvImages;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private ImageUploaderRecyclerAdapter adapter;
    boolean deniedPermissionForever = false;
    private ImageView completeMarksheets, incompleteMarksheets;
    private Image marksheet;
    private ImageButton gpaHelptip;
    private ProfileFormStep2 profileFormStep2;
    private ImageButton marksheetHelptip;
    private LinearLayout gradesheetLayout;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View rootView = inflater.inflate(
                R.layout.profile_form_step2_fragment1, container, false);
        String[] gpaArray = getResources().getStringArray(R.array.gpa_values);


        gpaTypeArray = getResources().getStringArray(R.array.gpa_type);
        mPrefs = getActivity().getSharedPreferences("buddy", Context.MODE_PRIVATE);
        getAllViews(rootView);

        setOnClickListerner();
        profileFormStep2 = (ProfileFormStep2) getActivity();
        user = profileFormStep2.getUser();


        try {
            if (user.getGradeSheet() == null) {
                user.setGradeSheet(new Image());
            } else {
                completeMarksheets.setVisibility(View.VISIBLE);
                user.setIncompleteMarksheets(false);
            }
        } catch (Exception e) {
            user.setGradeSheet(new Image());
        }
        if (user.isIncompleteMarksheets()) {
            if (user.isAppliedFor7k()) {
                gradesheetLayout.setVisibility(View.GONE);
            } else
                incompleteMarksheets.setVisibility(View.VISIBLE);
        }
        marksheet = user.getGradeSheet();
        if (!marksheet.getImgUrls().contains("add") && !user.isAppliedFor7k())
            marksheet.getImgUrls().add("add");


        if (!user.isAppliedFor7k()) {
            if (user.isIncompleteGpa()) {
                incompleteAP.setVisibility(View.VISIBLE);
                completeAP.setVisibility(View.GONE);
            } else {

                if (AppUtils.isNotEmpty(user.getGpaType()) && AppUtils.isNotEmpty(user.getGpa())) {
                    incompleteAP.setVisibility(View.GONE);
                    completeAP.setVisibility(View.VISIBLE);

                }
            }
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvImages.setLayoutManager(layoutManager);

        rvImages.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (marksheet.getImgUrls().get(position - marksheet.getInvalidImgUrls().size() - marksheet.getValidImgUrls().size()).equals("add")) {

                            String[] temp = hasPermissions(getActivity(), PERMISSIONS);
                            if (temp != null && temp.length != 0) {
                                deniedPermissionForever = true;
                                PERMISSIONS = temp;
                                requestPermissions(PERMISSIONS, PERMISSION_ALL);
                            } else {
                                Intent intent = new Intent(getActivity(), ImageHelperActivity.class);
                                startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
                            }
                        }
                    }
                })
        );
        newMarksheets = new HashMap<>();
        adapter = new ImageUploaderRecyclerAdapter(getActivity(), user.getGradeSheet(), "Marksheets", user.isAppliedFor7k(), Constants.IMAGE_TYPE.MARKSHEETS.toString());
        rvImages.setAdapter(adapter);

        final String scholarship[] = getResources().getStringArray(R.array.scholarship);
        final String scholarshipValues[] = getResources().getStringArray(R.array.scholarship_values);

        Spinner studentLoanSpinner = (Spinner) rootView.findViewById(R.id.student_loan);
        SpinnerHintAdapter studentLoanAdapter = new SpinnerHintAdapter(getActivity(), scholarship, R.layout.spinner_item_underline);
        studentLoanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        studentLoanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < scholarship.length - 1) {
                    selectedStudentLoan = true;
                    user.setStudentLoan(scholarshipValues[position]);
                    showHideBankStatements();
                    user.setUpdateStudentLoan(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setUpdateStudentLoan(false);
            }
        });
        studentLoanSpinner.setAdapter(studentLoanAdapter);
        studentLoanSpinner.setSelection(studentLoanAdapter.getCount());


        if (user.getStudentLoan() != null || "".equals(user.getStudentLoan())) {
            for (int i = 0; i < scholarship.length - 1; i++) {
                if (user.getStudentLoan().equals(scholarshipValues[i])) {
                    studentLoanSpinner.setSelection(i);
                    completeStudentLoan.setVisibility(View.VISIBLE);
                    user.setIncompleteStudentLoan(false);
                    break;
                }
            }
        }

        if (!user.isAppliedFor7k()) {
            if (user.isIncompleteStudentLoan()) {
                incompleteStudentLoan.setVisibility(View.VISIBLE);
                completeStudentLoan.setVisibility(View.GONE);
            } else {

                if (AppUtils.isNotEmpty(user.getStudentLoan())) {
                    completeStudentLoan.setVisibility(View.VISIBLE);
                    incompleteStudentLoan.setVisibility(View.GONE);

                }
            }
        }
        if (user.isAppliedFor7k()) {
            ProfileFormStep1Fragment1.setViewAndChildrenEnabled(rootView, false);
        }
        setAllHelpTipsEnabled();
        mPrefs.edit().putBoolean("visitedFormStep2Fragment1", true).apply();
        if (mPrefs.getBoolean("visitedFormStep2Fragment2", false)) {
            //gotoFragment2.setAlpha(1);
            //gotoFragment2.setClickable(true);
        }
        if (mPrefs.getBoolean("visitedFormStep2Fragment3", false)) {
            //gotoFragment3.setAlpha(1);
            //gotoFragment3.setClickable(true);
        }

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

        if (user.getGender() != null && "girl".equals(user.getGender())) {
            //Picasso.with(getActivity())
            //        .load(R.mipmap.step2fragment1girl)
            //        .into(topImage);
        }


        if (user.isIncompleteAddressDetails() || !(gpaValueEntered && gpaTypeEntered))

        {
            //incompleteStep1.setVisibility(View.VISIBLE);

            if (user.isIncompleteAddressDetails()) {
                //incompleteAddressDetails.setVisibility(View.VISIBLE);
            }
        }

        if (user.isIncompleteFamilyDetails())

        {
            //incompleteStep2.setVisibility(View.VISIBLE);
        }

        if (user.isIncompleteRepaymentSetup() || user.isIncompleteClassmateDetails()
                || user.isIncompleteVerificationDate() || user.isIncompleteStudentLoan())

        {
            //incompleteStep3.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void showHideBankStatements() {

        boolean b = false;
        try {
            b = Boolean.parseBoolean(user.getStudentLoan());

        } catch (Exception e) {
        }
        profileFormStep2.showHideBankStatements(b);
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
    public void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK) {
            UserModel user = AppUtils.getUserObject(getActivity());
            imageUris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
            if (user.getGradeSheet() == null)
                user.setGradeSheet(new Image());
            Image image = user.getGradeSheet();
            for (Uri uri : imageUris) {
                image.getImgUrls().add(0, uri.getPath());
                image.getNewImgUrls().put(uri.getPath(), AppUtils.uploadStatus.OPEN.toString());
                this.user.getGradeSheet().getImgUrls().add(0, uri.getPath());

            }
            adapter.notifyDataSetChanged();
            image.setUpdateNewImgUrls(true);
            AppUtils.saveUserObject(getActivity(), user);
        } else if (requestCode == REQUEST_PERMISSION_SETTING && resuleCode == Activity.RESULT_OK) {
            hasPermissions(getActivity(), PERMISSIONS);
        }
    }


    private void setOnClickListerner() {
        gpaHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "We want to encourage students who take their studies seriously. Giving us these details will help us better assess your credibility and may result in getting you more Credit Limit! We do not disclose these details to anybody (Not even your parents)";
                String text2 = "";
                Dialog dialog = new HelpTipDialog(getActivity(), "%GPA", text1, text2, "#eeb85f");
                dialog.show();
            }
        });
        marksheetHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = " Upload photos or scans of your Marksheets that you have received in college.<br>" +
                        "Make sure you include all Marksheets upto your current semester.<br>" + " If you are in 1st year and don't have any marksheets issued from college yet, please upload class 12th marksheet in that case.";
                String text2 = "";
                Dialog dialog = new HelpTipDialog(getActivity(), "%GPA", text1, text2, "#eeb85f");
                dialog.show();
            }
        });

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
            }

        });

    }

    private void getAllViews(View rootView) {
        gradesheetLayout = (LinearLayout) rootView.findViewById(R.id.gradesheet_ll);
        marksheetHelptip = (ImageButton) rootView.findViewById(R.id.marksheet_helptip);
        gpaHelptip = (ImageButton) rootView.findViewById(R.id.gpa_helptip);
        rvImages = (RecyclerView) rootView.findViewById(R.id.rvImages);
        gpaTypeSp = (Spinner) rootView.findViewById(R.id.gpa_spinner);
        spinnerErrorTv = (TextView) rootView.findViewById(R.id.spinner_error_msg_tv);
        gpaValueEt = (EditText) rootView.findViewById(R.id.gpa_value);
        incompleteAP = (ImageView) rootView.findViewById(R.id.incomplete_ap);
        completeAP = (ImageView) rootView.findViewById(R.id.complete_ap);
        incompleteStudentLoan = (ImageView) rootView.findViewById(R.id.incomplete_student_loan);
        completeStudentLoan = (ImageView) rootView.findViewById(R.id.complete_student_loan);
        completeMarksheets = (ImageView) rootView.findViewById(R.id.complete_marksheet);
        incompleteMarksheets = (ImageView) rootView.findViewById(R.id.incomplete_marksheet);
    }

    private void setAllHelpTipsEnabled() {
        gpaHelptip.setEnabled(true);
        marksheetHelptip.setEnabled(true);

    }

    public void checkIncomplete() {
        Image image = user.getGradeSheet();
        int totalSize = image.getImgUrls().size() + image.getValidImgUrls().size() + image.getInvalidImgUrls().size();
        if (totalSize == 0) {
            user.setIncompleteMarksheets(true);
        } else if (totalSize == 1) {
            if ("add".equals(image.getImgUrls().get(0))) {
                user.setIncompleteMarksheets(true);
            } else {
                user.setIncompleteMarksheets(false);
            }
        } else {
            user.setIncompleteMarksheets(false);
        }
        if (user.isIncompleteMarksheets()) {
            incompleteMarksheets.setVisibility(View.VISIBLE);
            completeMarksheets.setVisibility(View.GONE);

        } else {
            completeMarksheets.setVisibility(View.VISIBLE);
            incompleteMarksheets.setVisibility(View.GONE);
        }

        if (gpaValueEntered && gpaValueUpdate) {
            user.setGpaValueUpdate(true);
            user.setGpa(gpaValueEt.getText().toString());
        }
        if (gpaTypeEntered && gpaTypeUpdate) {
            user.setGpaType(gpaTypeArray[gpaTypeSp.getSelectedItemPosition()]);
            user.setGpaTypeUpdate(true);
        }

        if (AppUtils.isEmpty(user.getGpa()) || AppUtils.isEmpty(user.getGpaType())) {
            user.setIncompleteGpa(true);
            completeAP.setVisibility(View.GONE);
            incompleteAP.setVisibility(View.VISIBLE);

        } else {
            user.setIncompleteGpa(false);
            incompleteAP.setVisibility(View.GONE);
            completeAP.setVisibility(View.VISIBLE);
        }

        if (!selectedStudentLoan) {
            user.setIncompleteStudentLoan(true);
            incompleteStudentLoan.setVisibility(View.VISIBLE);
            completeStudentLoan.setVisibility(View.GONE);

        } else {
            user.setIncompleteStudentLoan(false);
            completeStudentLoan.setVisibility(View.VISIBLE);
            incompleteStudentLoan.setVisibility(View.GONE);
        }
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


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO(Developer): Check error code and notify the user of error state and resolution.
    }
}