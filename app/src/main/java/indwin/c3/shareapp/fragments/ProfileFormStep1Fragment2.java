package indwin.c3.shareapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
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
import com.gun0912.tedpicker.ImagePickerActivity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.Views.MonthYearPicker;
import indwin.c3.shareapp.activities.ImageHelperActivity;
import indwin.c3.shareapp.activities.ProfileFormStep1;
import indwin.c3.shareapp.adapters.AutoCompleteAdapter;
import indwin.c3.shareapp.adapters.ImageUploaderRecyclerAdapter;
import indwin.c3.shareapp.adapters.PlaceAutocompleteAdapter;
import indwin.c3.shareapp.models.OnBackPressedListener;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.CheckInternetAndUploadUserDetails;
import indwin.c3.shareapp.utils.DaysDifferenceFinder;
import indwin.c3.shareapp.utils.HelpTipDialog;
import indwin.c3.shareapp.utils.RecyclerItemClickListener;
import io.intercom.com.google.gson.Gson;

/**
 * Created by shubhang on 18/03/16.
 */
public class ProfileFormStep1Fragment2 extends Fragment implements GoogleApiClient.OnConnectionFailedListener, OnBackPressedListener {
    public static final String TAG = "buddy";
    public static final int PERMISSION_ALL = 0;
    private static final int REQUEST_PERMISSION_SETTING = 99;
    private SharedPreferences mPrefs;
    private UserModel user;
    private ArrayList<String> collegeIds;
    private Map<String, String> newCollegeIds;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    ImageUploaderRecyclerAdapter adapter;
    ArrayList<Uri> imageUris;
    private TextView editCollegeName, editCourseName, collegeNameHeading;
    //    collegeNameMap;
    private LinearLayout collegeNameLayout;
    //    private RelativeLayout collegeNameMapLayout;
    private AutoCompleteTextView collegeName, googleCollegeName, courseName;
    private ImageButton closeCollegeNameLayout, closeAddCollegeLayout;
    //    , closeCollegeNameMapLayout;
    ArrayList<String> collegeArrayList, courseArrayList;
    //    private Button acceptCollege;
    private static MonthYearPicker monthYearPicker;
    private static TextView editCollegeEndDate;
    private Button saveAndProceed, previous, cantFindCollege;
    private Gson gson;
    private static boolean updateCourseEndDate = false;
    private TextView gotoFragment1, gotoFragment3, gotoFragment2;
    private RelativeLayout addCollegeLayout, addCourseLayout;
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mGooglePlaceAdapter;
    private static final LatLngBounds BOUNDS_GREATER_BANGALORE = new LatLngBounds(
            new LatLng(12.89201, 77.58905), new LatLng(12.97232, 77.59480));
    private static final Location BANGALORE_CENTER = new Location("Bangalore");
    boolean isUserRejected = false;
    private ImageView completeCollegeId, incompleteCollegeId, completeCollegeDetails, incompleteCollegeDetails, incompleteStep1, incompleteStep2, incompleteStep3;
    private final int top = 16, left = 16, right = 16, bottom = 16;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    boolean deniedPermissionForever = false;
    ImageView topImage;
    private ImageButton socialHelptip;
    private Button addCourse, addCollege;
    private EditText addCourseEt;
    private EditText addRollNumberEt;
    private ImageView incompleteRollNumber, completeRollNumber;
    private boolean isRollNUmberUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.profile_form_step1_fragment2, container, false);
        getAllViews(rootView);
        setOnCLickListener();
        RecyclerView rvImages = (RecyclerView) rootView.findViewById(R.id.rvImages);
        mPrefs = getActivity().getSharedPreferences("buddy", Context.MODE_PRIVATE);
        mPrefs.edit().putBoolean("visitedFormStep1Fragment2", true).apply();
        gson = new Gson();
        String json = mPrefs.getString("UserObject", "");
        user = gson.fromJson(json, UserModel.class);

        try {
            collegeIds = user.getCollegeIds();
            if (collegeIds == null) {
                collegeIds = new ArrayList<>();
            } else {
                completeCollegeId.setVisibility(View.VISIBLE);
                user.setIncompleteCollegeId(false);
                mPrefs.edit().putString("UserObject", json).apply();
            }
        } catch (Exception e) {
            collegeIds = new ArrayList<>();
        }
        if (!collegeIds.contains("add") && !user.isAppliedFor1k())
            collegeIds.add("add");
        BANGALORE_CENTER.setLatitude(12.97232);
        BANGALORE_CENTER.setLongitude(77.59480);
        if (mGoogleApiClient == null)
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(Places.GEO_DATA_API)
                    .build();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvImages.setLayoutManager(layoutManager);

        rvImages.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (collegeIds.get(position).equals("add")) {

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
        newCollegeIds = new HashMap<>();
        adapter = new ImageUploaderRecyclerAdapter(getActivity(), collegeIds, "College Ids", user.isAppliedFor1k());
        rvImages.setAdapter(adapter);

        googleCollegeName.setOnItemClickListener(mAutocompleteClickListener);
        mGooglePlaceAdapter = new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient, BOUNDS_GREATER_BANGALORE, null);
        googleCollegeName.setAdapter(mGooglePlaceAdapter);

        if (!mPrefs.getBoolean("step1Editable", true)) {
            ProfileFormStep1Fragment1.setViewAndChildrenEnabled(rootView, false, gotoFragment1, gotoFragment3);
        }
        setAllHelpTipsEnabled();

        if (mPrefs.getBoolean("visitedFormStep1Fragment2", false)) {
            gotoFragment2.setAlpha(1);
            gotoFragment2.setClickable(true);
        }

        if (mPrefs.getBoolean("visitedFormStep1Fragment3", false)) {
            gotoFragment3.setAlpha(1);
            gotoFragment3.setClickable(true);
        }

        if (user.getGender() != null && "girl".equals(user.getGender())) {
            Picasso.with(getActivity())
                    .load(R.mipmap.step1fragment2girl)
                    .into(topImage);
        }
        if (AppUtils.isNotEmpty(user.getCollegeName())) {
            editCollegeName.setText(user.getCollegeName());
        }

        if (AppUtils.isNotEmpty(user.getCourseName())) {
            editCourseName.setText(user.getCourseName());
        }

        if (AppUtils.isNotEmpty(user.getCourseEndDate())) {
            editCollegeEndDate.setText(user.getCourseEndDate());
        }

        if (AppUtils.isNotEmpty(user.getCourseName()) && AppUtils.isNotEmpty(user.getCourseEndDate())) {
            completeCollegeDetails.setVisibility(View.VISIBLE);
            user.setIncompleteCollegeDetails(false);
        }


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        Rect rectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().

                getWindowVisibleDisplayFrame(rectangle);

        int statusBarHeight = rectangle.top;
        int height = displaymetrics.heightPixels - statusBarHeight;
        collegeNameLayout.getLayoutParams().height = height;
        addCollegeLayout.getLayoutParams().height = height;
        //        collegeNameMapLayout.getLayoutParams().height = height;

        if (AppUtils.isNotEmpty(user.getRollNumber())) {
            addRollNumberEt.setText(user.getRollNumber());
            completeRollNumber.setVisibility(View.VISIBLE);
        }
        if (user.isIncompleteCollegeId() || user.isIncompleteCollegeDetails() || user.isIncompleteRollNumber())

        {
            incompleteStep2.setVisibility(View.VISIBLE);
            if (user.isIncompleteCollegeId())
                incompleteCollegeId.setVisibility(View.VISIBLE);
            if (user.isIncompleteCollegeDetails())
                incompleteCollegeDetails.setVisibility(View.VISIBLE);
            if (user.isIncompleteRollNumber()) {
                incompleteRollNumber.setVisibility(View.VISIBLE);
            } else {
                completeRollNumber.setVisibility(View.VISIBLE);

            }
        } else

        {
            incompleteStep2.setVisibility(View.GONE);
        }

        if (user.isIncompleteEmail() || user.isIncompleteFb() || user.isIncompleteGender())

        {
            incompleteStep1.setVisibility(View.VISIBLE);
        } else
            incompleteStep1.setVisibility(View.GONE);


        if (user.getCollegeIds() != null && user.getCollegeIds().size() > 0) {
            user.setIncompleteCollegeId(false);
        }
        if (user.isIncompleteAadhar() || user.isIncompleteAddressDetails() || user.isInCompleteAgreement())

        {
            incompleteStep3.setVisibility(View.VISIBLE);
        } else
            incompleteStep3.setVisibility(View.GONE);


        if (user.isAppliedFor1k()) {
            previous.setVisibility(View.INVISIBLE);
            saveAndProceed.setVisibility(View.INVISIBLE);
            rootView.findViewById(R.id.details_submitted_tv).setVisibility(View.VISIBLE);
        }


        return rootView;
    }


    private void setOnCLickListener() {
        addRollNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isRollNUmberUpdate = true;
                user.setUpdateRollNumber(true);
            }
        });
        gotoFragment1.setOnClickListener(new View.OnClickListener()

                                         {
                                             @Override
                                             public void onClick(View v) {
                                                 replaceFragment1(true);
                                             }
                                         }

        );
        gotoFragment3.setOnClickListener(new View.OnClickListener()

                                         {
                                             @Override
                                             public void onClick(View v) {
                                                 replaceFragment3(true);
                                             }
                                         }

        );
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourseLayout.setVisibility(View.GONE);
                editCourseName.setText(addCourseEt.getText().toString());
                user.setCourseName(editCourseName.getText().toString());
                user.setUpdateCourseName(true);
            }
        });
        addCollege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCollegeLayout.setVisibility(View.GONE);
                editCollegeName.setText(googleCollegeName.getText().toString());
                user.setCollegeName(editCollegeName.getText().toString());
                user.setUpdateCollegeName(true);
            }
        });


        socialHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "Upload photos or scans of your photo identity card that has been provided by your college or institution.";
                String text2 = "Please remember to upload both (front and back) sides of the card.";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your College ID", text1, text2, "#44c2a6");
                dialog.show();
            }
        });
        editCollegeName.setOnClickListener
                (new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         try {
                             collegeNameHeading.setText("Enter your College Name");
                             cantFindCollege.setText("Can't find your college");
                             cantFindCollege.setVisibility(View.VISIBLE);
                             cantFindCollege.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     collegeNameLayout.setVisibility(View.GONE);
                                     addCollegeLayout.setVisibility(View.VISIBLE);
                                 }
                             });
                             AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(getActivity(),
                                     R.layout.autocomplete_item, R.id.automcomplete_text_item, collegeArrayList);
                             collegeName.setDropDownVerticalOffset(120);
                             collegeName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                 @Override
                                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                     String selection = (String) parent.getItemAtPosition(position);
                                     if (selection.length() > 0) {
                                         editCollegeName.setText(selection);
                                         editCollegeName.setFocusable(true);
                                         editCollegeName.setFocusableInTouchMode(true);
                                         user.setCollegeName(selection);
                                         user.setUpdateCollegeName(true);
                                         collegeNameLayout.setVisibility(View.GONE);
                                         hideCollegeNameLayout();
                                         if (getActivity().getCurrentFocus() != null) {
                                             InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                             inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                                         }
                                     }
                                 }
                             });
                             collegeName.setAdapter(autoCompleteAdapter);
                             ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                             collegeNameLayout.setVisibility(View.VISIBLE);
                             collegeName.requestFocus();
                             InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                             imm.showSoftInput(collegeName, InputMethodManager.SHOW_IMPLICIT);
                         } catch (Exception e) {
                             e.printStackTrace();
                         }
                     }
                 }
                );
        editCourseName.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  try {
                                                      collegeNameHeading.setText("Enter your Course Name");
                                                      AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(getActivity(),
                                                              R.layout.autocomplete_item, R.id.automcomplete_text_item, courseArrayList);
                                                      cantFindCollege.setText("Can't find your course");
                                                      cantFindCollege.setVisibility(View.VISIBLE);
                                                      cantFindCollege.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              collegeNameLayout.setVisibility(View.GONE);
                                                              addCourseLayout.setVisibility(View.VISIBLE);
                                                          }
                                                      });

                                                      collegeName.setDropDownVerticalOffset(16);
                                                      collegeName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                          @Override
                                                          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                              String selection = (String) parent.getItemAtPosition(position);
                                                              if (selection.length() > 0) {
                                                                  editCourseName.setText(selection);
                                                                  editCourseName.setFocusable(true);
                                                                  editCourseName.setFocusableInTouchMode(true);
                                                                  user.setCourseName(editCourseName.getText().toString());
                                                                  user.setUpdateCourseName(true);
                                                                  hideCollegeNameLayout();
                                                              }
                                                          }
                                                      });
                                                      collegeName.setAdapter(autoCompleteAdapter);
                                                      ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                                                      collegeNameLayout.setVisibility(View.VISIBLE);
                                                      collegeName.requestFocus();
                                                      InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                                      imm.showSoftInput(collegeName, InputMethodManager.SHOW_IMPLICIT);
                                                  } catch (Exception e) {
                                                      e.printStackTrace();
                                                  }
                                              }
                                          }

        );
        editCollegeEndDate.setOnClickListener(new View.OnClickListener()

                                              {
                                                  @Override
                                                  public void onClick(View v) {
                                                      monthYearPicker.show();
                                                  }
                                              }

        );
        closeCollegeNameLayout.setOnClickListener(new View.OnClickListener()

                                                  {
                                                      @Override
                                                      public void onClick(View v) {
                                                          hideCollegeNameLayout();
                                                      }
                                                  }

        );
        closeAddCollegeLayout.setOnClickListener(new View.OnClickListener()

                                                 {
                                                     @Override
                                                     public void onClick(View v) {
                                                         hideAddCollegeLayout();
                                                     }
                                                 }

        );
        //        closeCollegeNameMapLayout.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                collegeNameMapLayout.setVisibility(View.GONE);
        //                collegeNameMap.setText("");
        //            }
        //        });
        //
        //        acceptCollege.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                editCollegeName.setText(collegeNameMap.getText());
        //                user.setCollegeName(editCollegeName.getText().toString());
        //                user.setUpdateCollegeName(true);
        //                collegeNameMapLayout.setVisibility(View.GONE);
        //                collegeNameMap.setText("");
        //                hideCollegeNameLayout();
        //            }
        //        });
        //        map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
        //                .getMap();
        //        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        monthYearPicker = new MonthYearPicker(getActivity());
        monthYearPicker.build(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }, null);

        monthYearPicker.setYearValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //                if (newVal == currentYear) {
                //                    monthYearPicker.setMinMonth(currentMonth);
                //                } else {
                //                   monthYearPicker.setMinMonth(10);
                //                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                replaceFragment1(true);
            }
        });
        saveAndProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean readyToUpdate = true;
                boolean isCollegeEndingSoon = false;

                if (isUserRejected) {
                    readyToUpdate = false;
                }
                checkIncomplete();
                if (isRollNUmberUpdate && AppUtils.isNotEmpty(addRollNumberEt.getText().toString())) {
                    user.setIncompleteRollNumber(false);
                    user.setRollNumber(addRollNumberEt.getText().toString());
                }
                if (updateCourseEndDate) {
                    try {
                        SimpleDateFormat spf = new SimpleDateFormat("MMM yyyy");
                        Date newDate = spf.parse(editCollegeEndDate.getText().toString());
                        spf = new SimpleDateFormat("yyyy-MM-dd");
                        user.setCourseEndDate(spf.format(newDate));
                        user.setUpdateCourseEndDate(true);
                        Calendar startCalendar = new GregorianCalendar();
                        startCalendar.setTime(new Date());
                        Calendar endCalendar = new GregorianCalendar();
                        endCalendar.setTime(newDate);

                        int diffMonth = DaysDifferenceFinder.getDifferenceBetweenDatesInMonths(startCalendar, endCalendar);
                        if (startCalendar.get(Calendar.DAY_OF_MONTH) > 15) {
                            diffMonth -= 1;
                        }
                        if (diffMonth <= 0) {
                            isCollegeEndingSoon = true;
                            readyToUpdate = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String json = gson.toJson(user);
                mPrefs.edit().putString("UserObject", json).apply();
                Intent intent = new Intent(getActivity(), CheckInternetAndUploadUserDetails.class);
                getContext().sendBroadcast(intent);
                replaceFragment3(false);
            }
        });
    }

    private void getAllViews(View rootView) {
        completeCollegeId = (ImageView) rootView.findViewById(R.id.complete_college_id);
        addCourse = (Button) rootView.findViewById(R.id.add_course);
        addRollNumberEt = (EditText) rootView.findViewById(R.id.roll_number_et);

        completeRollNumber = (ImageView) rootView.findViewById(R.id.complete_roll_number);
        incompleteRollNumber = (ImageView) rootView.findViewById(R.id.incomplete_roll_number);
        addCollege = (Button) rootView.findViewById(R.id.add_college);
        editCollegeName = (TextView) rootView.findViewById(R.id.edit_college_name);
        editCourseName = (TextView) rootView.findViewById(R.id.edit_course_name);
        editCollegeEndDate = (TextView) rootView.findViewById(R.id.edit_college_end_date);
        editCollegeEndDate = (TextView) rootView.findViewById(R.id.edit_college_end_date);
        collegeNameLayout = (LinearLayout) rootView.findViewById(R.id.college_name_layout);
        collegeName = (AutoCompleteTextView) rootView.findViewById(R.id.college_name);
        addCourseEt = (EditText) rootView.findViewById(R.id.course_name_et);
        closeCollegeNameLayout = (ImageButton) rootView.findViewById(R.id.close_college_name_layout);
        collegeArrayList = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.colleges)));
        courseArrayList = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.courses)));
        collegeNameHeading = (TextView) rootView.findViewById(R.id.college_name_heading);
        //        collegeNameMapLayout = (RelativeLayout) rootView.findViewById(R.id.college_name_map_layout);
        //        collegeNameMap = (TextView) rootView.findViewById(R.id.college_name_map);
        //        closeCollegeNameMapLayout = (ImageButton) rootView.findViewById(R.id.close_college_name_map_layout);
        //        acceptCollege = (Button) rootView.findViewById(R.id.accept_college);
        saveAndProceed = (Button) rootView.findViewById(R.id.save_and_proceed);

        previous = (Button) rootView.findViewById(R.id.previous);
        gotoFragment1 = (TextView) rootView.findViewById(R.id.goto_fragment1);
        gotoFragment2 = (TextView) rootView.findViewById(R.id.goto_fragment2);
        gotoFragment3 = (TextView) rootView.findViewById(R.id.goto_fragment3);
        addCollegeLayout = (RelativeLayout) rootView.findViewById(R.id.add_college_layout);
        addCourseLayout = (RelativeLayout) rootView.findViewById(R.id.add_course_layout);

        cantFindCollege = (Button) rootView.findViewById(R.id.cant_find_college_button);
        closeAddCollegeLayout = (ImageButton) rootView.findViewById(R.id.close_add_college_name_layout);
        incompleteCollegeId = (ImageView) rootView.findViewById(R.id.incomplete_college_id);
        completeCollegeDetails = (ImageView) rootView.findViewById(R.id.complete_college_details);
        incompleteCollegeDetails = (ImageView) rootView.findViewById(R.id.incomplete_college_details);
        incompleteStep1 = (ImageView) rootView.findViewById(R.id.incomplete_step_1);
        incompleteStep2 = (ImageView) rootView.findViewById(R.id.incomplete_step_2);
        incompleteStep3 = (ImageView) rootView.findViewById(R.id.incomplete_step_3);
        topImage = (ImageView) rootView.findViewById(R.id.verify_image_view2);
        socialHelptip = (ImageButton) rootView.findViewById(R.id.social_helptip);

        googleCollegeName = (AutoCompleteTextView) rootView.findViewById(R.id.google_college_autocomplete);

    }


    private void setAllHelpTipsEnabled() { socialHelptip.setEnabled(true);}

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

    private void checkIncomplete() {
        if (collegeIds.size() <= 1) {
            incompleteCollegeId.setVisibility(View.VISIBLE);
            user.setIncompleteCollegeId(true);
        } else {
            collegeIds.remove(collegeIds.size() - 1);
            user.setCollegeIds(collegeIds);
            user.setIncompleteCollegeId(false);
        }
        if (AppUtils.isEmpty(addRollNumberEt.getText().toString())) {

            user.setIncompleteRollNumber(true);
        } else {
            user.setIncompleteRollNumber(false);
        }
        if ("".equals(editCollegeName.getText().toString()) || "".equals(editCourseName.getText().toString())
                || "".equals(editCollegeEndDate.getText().toString()) || AppUtils.isEmpty(addRollNumberEt.getText().toString())) {
            incompleteCollegeDetails.setVisibility(View.VISIBLE);
            user.setIncompleteCollegeDetails(true);
        } else
            user.setIncompleteCollegeDetails(false);
    }

    private void replaceFragment1(boolean check) {
        if (check)
            checkIncomplete();
        String json = gson.toJson(user);
        mPrefs.edit().putString("UserObject", json).apply();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment1, new ProfileFormStep1Fragment1(), "Fragment2Tag");
        ft.commit();
    }

    private void replaceFragment3(boolean check) {
        if (check)
            checkIncomplete();
        String json = gson.toJson(user);
        mPrefs.edit().putString("UserObject", json).apply();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment1, new ProfileFormStep1Fragment3(), "Fragment2Tag");
        ft.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK) {
            imageUris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
            if (user.getCollegeIds() == null)
                user.setCollegeIds(new ArrayList<String>());
            for (Uri uri : imageUris) {
                collegeIds.add(0, uri.getPath());
                newCollegeIds.put(uri.getPath(), AppUtils.uploadStatus.OPEN.toString());
                //                user.addCollegeId(0, uri.getPath(), user.getCollegeIds());
            }
            adapter.notifyDataSetChanged();
            user.setNewCollegeIds(newCollegeIds);
            user.setUpdateNewCollegeIds(true);
        } else if (requestCode == REQUEST_PERMISSION_SETTING && resuleCode == Activity.RESULT_OK) {
            hasPermissions(getActivity(), PERMISSIONS);
        }
    }

    private void hideCollegeNameLayout() {
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            collegeNameLayout.setVisibility(View.GONE);
            collegeName.setText("");
            if (getActivity().getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideAddCollegeLayout() {
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            addCollegeLayout.setVisibility(View.GONE);
            collegeName.setText("");
            googleCollegeName.setText("");
            if (getActivity().getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void confirmCollegeEndDate() {
        String date = monthYearPicker.getSelectedMonthName() + " " + monthYearPicker.getSelectedYear();
        editCollegeEndDate.setText(date);
        editCollegeEndDate.setFocusable(true);
        editCollegeEndDate.setFocusableInTouchMode(true);
        updateCourseEndDate = true;
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mGooglePlaceAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);
            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                //                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            Location location = new Location("college");
            location.setLatitude(place.getLatLng().latitude);
            location.setLongitude(place.getLatLng().longitude);
            float distance = location.distanceTo(BANGALORE_CENTER) / 1000;
            if (distance > 100) {
                isUserRejected = true;
            } else
                isUserRejected = false;
            // Format details of the place for display and show it in a TextView.
            editCollegeName.setText(place.getAddress());
            editCollegeName.setFocusable(true);
            editCollegeName.setFocusableInTouchMode(true);
            user.setCollegeName(place.getAddress().toString());
            user.setUpdateCollegeName(true);
            hideAddCollegeLayout();
            places.release();
        }
    };

    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        //        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
        //                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        //        Toast.makeText(getActivity(),
        //                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
        //                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (collegeNameLayout.getVisibility() == View.VISIBLE) {
            collegeNameLayout.setVisibility(View.GONE);
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            ProfileFormStep1.isBackInsideFrag = true;
        } else if (addCollegeLayout.getVisibility() == View.VISIBLE) {
            addCollegeLayout.setVisibility(View.GONE);
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            ProfileFormStep1.isBackInsideFrag = true;
        } else {
            ProfileFormStep1.isBackInsideFrag = false;
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
        if (requestCode == PERMISSION_ALL) {
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