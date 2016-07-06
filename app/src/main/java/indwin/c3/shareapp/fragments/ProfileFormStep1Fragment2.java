package indwin.c3.shareapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.gun0912.tedpicker.ImagePickerActivity;

import java.util.ArrayList;
import java.util.Arrays;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.Views.MonthYearPicker;
import indwin.c3.shareapp.activities.FullScreenActivity;
import indwin.c3.shareapp.activities.ImageHelperActivity;
import indwin.c3.shareapp.activities.ProfileFormStep1;
import indwin.c3.shareapp.adapters.AutoCompleteAdapter;
import indwin.c3.shareapp.adapters.ImageUploaderRecyclerAdapter;
import indwin.c3.shareapp.models.FrontBackImage;
import indwin.c3.shareapp.models.Image;
import indwin.c3.shareapp.models.OnBackPressedListener;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.Constants;
import indwin.c3.shareapp.utils.HelpTipDialog;
import indwin.c3.shareapp.utils.RecyclerItemClickListener;

/**
 * Created by ROCK
 */
public class ProfileFormStep1Fragment2 extends Fragment implements OnBackPressedListener {
    public static final int PERMISSION_ALL = 0;
    private static final int REQUEST_PERMISSION_SETTING = 99;
    private static UserModel user;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    ImageUploaderRecyclerAdapter adapter;
    ArrayList<Uri> imageUris;
    private TextView editCollegeName, editCourseName, collegeNameHeading;
    private LinearLayout collegeNameLayout;
    private AutoCompleteTextView collegeName, googleCollegeName;
    private ImageButton closeCollegeNameLayout, closeAddCollegeLayout;
    ArrayList<String> collegeArrayList, courseArrayList;
    private static MonthYearPicker monthYearPicker;
    private static TextView editCollegeEndDate;
    private Button cantFindCollege;
    private RelativeLayout addCollegeLayout, addCourseLayout;
    private ImageView completeCollegeId, incompleteCollegeId, completeCollegeDetails, incompleteCollegeDetails;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    boolean deniedPermissionForever = false;
    private ImageButton socialHelptip;
    private Button addCourse, addCollege;
    private EditText addCourseEt;
    private Image collegeIDs;
    private int clickedPosition;
    private ImageButton closeAddCourseLayout;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.profile_form_step1_fragment2, container, false);
        getAllViews(rootView);
        setOnCLickListener();
        RecyclerView rvImages = (RecyclerView) rootView.findViewById(R.id.rvImages);
        ProfileFormStep1 profileFormStep1 = (ProfileFormStep1) getActivity();
        user = profileFormStep1.getUser();
        try {
            if (user.getCollegeID() == null) {
                user.setCollegeID(new Image());
            } else if (user.getCollegeID().getFront() != null && AppUtils.isNotEmpty(user.getCollegeID().getFront().getImgUrl())) {
                completeCollegeId.setVisibility(View.VISIBLE);
                user.setIncompleteCollegeId(false);
            }
        } catch (Exception e) {
        }
        collegeIDs = user.getCollegeID();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvImages.setLayoutManager(layoutManager);

        rvImages.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if (
                            //((position == 0 && (collegeIDs.getFront() == null || AppUtils.isEmpty(collegeIDs.getFront().getImgUrl()))) &&
                                !user.isAppliedFor1k()
                            //)

                            //|| (position == 1 && (collegeIDs.getBack() == null || AppUtils.isEmpty(collegeIDs.getBack().getImgUrl()))) && !user.isAppliedFor1k()
                                ) {

                            String[] temp = AppUtils.hasPermissions(getActivity(), deniedPermissionForever, REQUEST_PERMISSION_SETTING, PERMISSIONS);

                            if (temp != null && temp.length != 0) {
                                deniedPermissionForever = true;
                                PERMISSIONS = temp;
                                requestPermissions(PERMISSIONS, PERMISSION_ALL);
                            } else {
                                clickedPosition = position;
                                Intent intent = new Intent(getActivity(), ImageHelperActivity.class);
                                startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
                            }
                        } else {
                            Intent intent = new Intent(getActivity(), FullScreenActivity.class);

                            intent.putExtra(AppUtils.IMAGE_TYPE, Constants.IMAGE_TYPE.COLLEGE_ID.toString());
                            intent.putExtra(Constants.DISABLE_ADD, true);
                            intent.putExtra(AppUtils.POSITION, position);
                            intent.putExtra(AppUtils.HEADING, "College Ids");
                            getActivity().startActivity(intent);
                        }
                    }
                })
        );
        adapter = new ImageUploaderRecyclerAdapter(getActivity(), collegeIDs, "College Ids", user.isAppliedFor1k(), Constants.IMAGE_TYPE.COLLEGE_ID.toString());
        rvImages.setAdapter(adapter);


        if (user.isAppliedFor1k()) {
            ProfileFormStep1Fragment1.setViewAndChildrenEnabled(rootView, false);
        }
        setAllHelpTipsEnabled();


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
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);


        int statusBarHeight = rectangle.top;
        int height = displaymetrics.heightPixels - statusBarHeight;
        collegeNameLayout.getLayoutParams().height = height;
        addCollegeLayout.getLayoutParams().height = height;

        if (user.isIncompleteCollegeId() || user.isIncompleteCollegeDetails() || user.isIncompleteRollNumber())

        {
            if (user.isIncompleteCollegeId() && !user.isAppliedFor1k())
                incompleteCollegeId.setVisibility(View.VISIBLE);
            if (user.isIncompleteCollegeDetails() && !user.isAppliedFor1k())
                incompleteCollegeDetails.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    private void setOnCLickListener() {
        closeAddCourseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAddCourseLayout();
            }
        });
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

        monthYearPicker = new MonthYearPicker(getActivity());
        monthYearPicker.build(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }, null);

        monthYearPicker.setYearValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            }
        });
    }

    private void getAllViews(View rootView) {
        completeCollegeId = (ImageView) rootView.findViewById(R.id.complete_college_id);
        addCourse = (Button) getActivity().findViewById(R.id.add_course);

        addCollege = (Button) getActivity().findViewById(R.id.add_college);
        editCollegeName = (TextView) rootView.findViewById(R.id.edit_college_name);
        editCourseName = (TextView) rootView.findViewById(R.id.edit_course_name);
        editCollegeEndDate = (TextView) rootView.findViewById(R.id.edit_college_end_date);
        editCollegeEndDate = (TextView) rootView.findViewById(R.id.edit_college_end_date);
        collegeNameLayout = (LinearLayout) getActivity().findViewById(R.id.college_name_layout);
        collegeName = (AutoCompleteTextView) getActivity().findViewById(R.id.college_name);


        addCourseEt = (EditText) getActivity().findViewById(R.id.course_name_et);
        closeCollegeNameLayout = (ImageButton) getActivity().findViewById(R.id.close_college_name_layout);
        collegeArrayList = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.colleges)));
        courseArrayList = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.courses)));
        collegeNameHeading = (TextView) getActivity().findViewById(R.id.college_name_heading);
        addCollegeLayout = (RelativeLayout) getActivity().findViewById(R.id.add_college_layout);
        addCourseLayout = (RelativeLayout) getActivity().findViewById(R.id.add_course_layout);

        cantFindCollege = (Button) getActivity().findViewById(R.id.cant_find_college_button);
        closeAddCollegeLayout = (ImageButton) getActivity().findViewById(R.id.close_add_college_name_layout);
        incompleteCollegeId = (ImageView) rootView.findViewById(R.id.incomplete_college_id);
        completeCollegeDetails = (ImageView) rootView.findViewById(R.id.complete_college_details);
        incompleteCollegeDetails = (ImageView) rootView.findViewById(R.id.incomplete_college_details);
        socialHelptip = (ImageButton) rootView.findViewById(R.id.social_helptip);

        googleCollegeName = (AutoCompleteTextView) getActivity().findViewById(R.id.google_college_autocomplete);
        closeAddCourseLayout = (ImageButton) getActivity().findViewById(R.id.close_add_course_name_layout);
    }


    private void setAllHelpTipsEnabled() {
        socialHelptip.setEnabled(true);
        socialHelptip.setClickable(true);
    }


    public void checkIncomplete() {
        if (collegeIDs.getFront() == null || AppUtils.isEmpty(collegeIDs.getFront().getImgUrl())) {
            incompleteCollegeId.setVisibility(View.VISIBLE);
            user.setIncompleteCollegeId(true);
        } else {
            user.setIncompleteCollegeId(false);

        }
        if (user.isIncompleteCollegeId()) {
            incompleteCollegeId.setVisibility(View.VISIBLE);
            completeCollegeId.setVisibility(View.GONE);

        } else {
            completeCollegeId.setVisibility(View.VISIBLE);
            incompleteCollegeId.setVisibility(View.GONE);
        }
        if ("".equals(editCollegeName.getText().toString()) || "".equals(editCourseName.getText().toString())
                || "".equals(editCollegeEndDate.getText().toString())) {
            incompleteCollegeDetails.setVisibility(View.VISIBLE);
            completeCollegeDetails.setVisibility(View.GONE);
            user.setIncompleteCollegeDetails(true);
        } else {
            user.setIncompleteCollegeDetails(false);
            incompleteCollegeDetails.setVisibility(View.GONE);
            completeCollegeDetails.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK) {
            UserModel user = AppUtils.getUserObject(getActivity());
            imageUris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
            if (user.getCollegeID() == null)
                user.setCollegeID(new Image());
            FrontBackImage frontBackImage = new FrontBackImage();
            if (imageUris != null && imageUris.size() > 0) {
                Image collegeId = user.getCollegeID();
                frontBackImage.setImgUrl(imageUris.get(0).getPath());
                if (clickedPosition == 0) {
                    collegeId.setFrontStatus(AppUtils.uploadStatus.OPEN.toString());
                    collegeId.setFront(frontBackImage);
                    collegeId.setUpdateFront(true);
                    collegeIDs.setFront(frontBackImage);
                } else if (clickedPosition == 1) {
                    collegeId.setUpdateBack(true);
                    collegeId.setBack(frontBackImage);
                    collegeIDs.setBack(frontBackImage);
                    collegeId.setBackStatus(AppUtils.uploadStatus.OPEN.toString());
                }
                adapter.notifyDataSetChanged();
                AppUtils.saveUserObject(getActivity(), user);
            }
        } else if (requestCode == REQUEST_PERMISSION_SETTING && resuleCode == Activity.RESULT_OK) {
            AppUtils.hasPermissions(getActivity(), deniedPermissionForever, REQUEST_PERMISSION_SETTING, PERMISSIONS);
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

    private void hideAddCourseLayout() {
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            addCourseLayout.setVisibility(View.GONE);
            collegeName.setText("");
            googleCollegeName.setText("");
            AppUtils.hideKeyboard(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void confirmCollegeEndDate() {
        String date = monthYearPicker.getSelectedMonthName() + " " + monthYearPicker.getSelectedYear();
        editCollegeEndDate.setText(date);
        editCollegeEndDate.setFocusable(true);
        editCollegeEndDate.setFocusableInTouchMode(true);
        user.setCourseEndDate(date);
        user.setUpdateCourseEndDate(true);
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