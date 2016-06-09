package indwin.c3.shareapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.activities.ProfileFormStep2;
import indwin.c3.shareapp.adapters.SpinnerHintAdapter;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.HelpTipDialog;
import indwin.c3.shareapp.utils.ValidationUtils;
import io.intercom.com.google.gson.Gson;

/**
 * Created by shubhang on 05/04/16.
 */
public class ProfileFormStep2Fragment2 extends Fragment {
    private SharedPreferences mPrefs;
    private UserModel user;
    private Button saveAndProceed, previous;
    private Gson gson;
    private TextView gotoFragment1, gotoFragment3, gotoFragment2, addFamilyMember;
    private final int top = 16, left = 16, right = 16, bottom = 16;
    ImageView incompleteStep1, incompleteStep2, incompleteStep3, incompleteFamilyDetails, completeFamilyDetails;
    boolean isFamilyMemberAdded = false;
    private EditText phoneFamilyMember1, phoneFamilyMember2;
    private Spinner prefLangFamilyMember1, prefLangFamilyMember2;
    boolean isFamilyMember1Selected = false, isProfessionFamilyMember1Selected = false, isFamilyMember2Selected = false, isProfessionFamilyMember2Selected = false;
    ImageView topImage;
    View view1, view2;
    private ImageButton familyHelptip;
    private Spinner familyMember2spinner, professionFamilyMember2spinner, familyMember1spinner;
    private SpinnerHintAdapter adapter, adapter2, languageAdapter;
    private LinearLayout parentLL1, parentLL2;
    private TextView incorrectPhoneFamily1, incorrectPhoneFamily2;
    private String mobile;
    private String languageOptions[];

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.profile_form_step2_fragment2, container, false);
        RecyclerView rvImages = (RecyclerView) rootView.findViewById(R.id.rvImages);
        mPrefs = getActivity().getSharedPreferences("buddy", Context.MODE_PRIVATE);
        mPrefs.edit().putBoolean("visitedFormStep2Fragment2", true).apply();
        gson = new Gson();
        ProfileFormStep2 profileFormStep2 = (ProfileFormStep2) getActivity();
        user = profileFormStep2.getUser();
        mobile = AppUtils.getFromSelectedSharedPrefs(getActivity(), "phone_number", "cred");
        getAllViews(rootView);
        if (!mPrefs.getBoolean("step2Editable", true)) {
            ProfileFormStep1Fragment1.setViewAndChildrenEnabled(rootView, false);
        }
        setAllHelpTipsEnabled();
        if (mPrefs.getBoolean("visitedFormStep2Fragment2", false)) {
            //gotoFragment2.setAlpha(1);
            //gotoFragment2.setClickable(true);
        }
        if (mPrefs.getBoolean("visitedFormStep2Fragment3", false)) {
            //gotoFragment3.setAlpha(1);
            //gotoFragment3.setClickable(true);
        }


        //if (user.getGender() != null && "girl".equals(user.getGender())) {
        //    Picasso.with(getActivity())
        //            .load(R.mipmap.step2fragment2girl)
        //            .into(topImage);
        //}
        familyHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "Entering details of more family members, especially earning members may increase your chances at getting a higher Credit Limit.";
                String text2 = "";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your College ID", text1, text2, "#eeb85f");
                dialog.show();
            }
        });


        final String familyMemberOptions[] = getResources().getStringArray(R.array.family_member);
        languageOptions = getResources().getStringArray(R.array.language_array);
        adapter = new SpinnerHintAdapter(getActivity(), familyMemberOptions, R.layout.spinner_item_underline);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        familyMember1spinner = (Spinner) rootView.findViewById(R.id.family_member_1);
        familyMember1spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < familyMemberOptions.length - 1) {
                    user.setFamilyMemberType1(familyMemberOptions[position].toLowerCase());
                    user.setUpdateFamilyMemberType1(true);
                    isFamilyMember1Selected = true;
                    populateSpinner2FamilyMember();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setUpdateFamilyMemberType1(false);
            }
        });
        familyMember1spinner.setAdapter(adapter);
        familyMember1spinner.setSelection(adapter.getCount());

        languageAdapter = new SpinnerHintAdapter(getActivity(), languageOptions, R.layout.spinner_item_underline);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        prefLangFamilyMember1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user.setPrefferedLanguageFamilyMemberType1(languageOptions[position]);
                user.setUpdatePreferredLanguageFamilyMemberType1(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setUpdatePreferredLanguageFamilyMemberType1(false);
            }
        });

        prefLangFamilyMember2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user.setPrefferedLanguageFamilyMemberType2(languageOptions[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setUpdatePreferredLanguageFamilyMemberType2(false);
            }
        });
        prefLangFamilyMember1.setAdapter(languageAdapter);
        prefLangFamilyMember2.setAdapter(languageAdapter);
        familyMember2spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < familyMemberOptions.length - 1) {
                    user.setFamilyMemberType2(familyMemberOptions[position].toLowerCase());
                    user.setUpdateFamilyMemberType2(true);
                    isFamilyMember2Selected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setUpdateFamilyMemberType2(false);
            }
        });
        familyMember2spinner.setAdapter(adapter);

        final String professionFamilyMemberOptions[] = getResources().getStringArray(R.array.profession_family_member);
        adapter2 = new SpinnerHintAdapter(getActivity(), professionFamilyMemberOptions, R.layout.spinner_item_underline);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner professionFamilyMember1spinner = (Spinner) rootView.findViewById(R.id.profession_family_member_1);
        professionFamilyMember1spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < professionFamilyMemberOptions.length - 1) {
                    user.setProfessionFamilyMemberType1(professionFamilyMemberOptions[position]);
                    user.setUpdateProfessionFamilyMemberType1(true);
                    isProfessionFamilyMember1Selected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setUpdateProfessionFamilyMemberType1(false);
            }
        });
        professionFamilyMember1spinner.setAdapter(adapter2);
        professionFamilyMember1spinner.setSelection(adapter2.getCount());

        professionFamilyMember2spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < professionFamilyMemberOptions.length - 1) {
                    user.setProfessionFamilyMemberType2(professionFamilyMemberOptions[position]);
                    user.setUpdateProfessionFamilyMemberType2(true);
                    isProfessionFamilyMember2Selected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setUpdateProfessionFamilyMemberType2(false);
            }
        });
        professionFamilyMember2spinner.setAdapter(adapter2);
        professionFamilyMember2spinner.setSelection(adapter2.getCount());
        checkAndPopulateFamilyLayout2();

        if (AppUtils.isNotEmpty(user.getFamilyMemberType1())) {
            for (int i = 0; i < familyMemberOptions.length - 1; i++) {
                if (familyMemberOptions[i].equalsIgnoreCase(user.getFamilyMemberType1())) {
                    familyMember1spinner.setSelection(i);
                    break;
                }
            }
        }
        if (AppUtils.isNotEmpty(user.getProfessionFamilyMemberType1())) {
            for (int i = 0; i < professionFamilyMemberOptions.length - 1; i++) {
                if (professionFamilyMemberOptions[i].equalsIgnoreCase(user.getProfessionFamilyMemberType1())) {
                    professionFamilyMember1spinner.setSelection(i);
                    break;
                }
            }
        }

        if (AppUtils.isNotEmpty(user.getPrefferedLanguageFamilyMemberType1())) {
            for (int i = 0; i < languageOptions.length - 1; i++) {
                if (languageOptions[i].equalsIgnoreCase(user.getPrefferedLanguageFamilyMemberType1())) {
                    prefLangFamilyMember1.setSelection(i);
                    break;
                }
            }
        }
        if (AppUtils.isNotEmpty(user.getPhoneFamilyMemberType1())) {
            phoneFamilyMember1.setText(user.getPhoneFamilyMemberType1());
        }
        if (AppUtils.isNotEmpty(user.getFamilyMemberType1())
                && user.getProfessionFamilyMemberType1() != null && !"".equals(user.getProfessionFamilyMemberType1())
                && user.getPrefferedLanguageFamilyMemberType1() != null &&
                !"".equals(user.getPrefferedLanguageFamilyMemberType1()) &&
                user.getPhoneFamilyMemberType1() != null && !"".equals(user.getPhoneFamilyMemberType1())) {
            completeFamilyDetails.setVisibility(View.VISIBLE);
            user.setIncompleteFamilyDetails(false);
        } else {
            user.setIncompleteFamilyDetails(true);
        }
        if (AppUtils.isNotEmpty(user.getFamilyMemberType2())) {
            for (int i = 0; i < familyMemberOptions.length - 1; i++) {
                if (familyMemberOptions[i].equalsIgnoreCase(user.getFamilyMemberType2())) {
                    familyMember2spinner.setSelection(i);
                    break;
                }
            }
            if (AppUtils.isNotEmpty(user.getProfessionFamilyMemberType2())) {
                for (int i = 0; i < professionFamilyMemberOptions.length - 1; i++) {
                    if (professionFamilyMemberOptions[i].equalsIgnoreCase(user.getProfessionFamilyMemberType2())) {
                        professionFamilyMember2spinner.setSelection(i);
                        break;
                    }
                }
            }
            if (AppUtils.isNotEmpty(user.getPrefferedLanguageFamilyMemberType2())) {
                for (int i = 0; i < languageOptions.length - 1; i++) {
                    if (languageOptions[i].equalsIgnoreCase(user.getPrefferedLanguageFamilyMemberType2())) {
                        prefLangFamilyMember2.setSelection(i);
                        break;
                    }
                }
            }
            if (AppUtils.isNotEmpty(user.getPhoneFamilyMemberType2())) {
                phoneFamilyMember2.setText(user.getPhoneFamilyMemberType2());
            }
        }
        setOnClickListener();
        if (user.isIncompleteDOB() || user.isIncompleteAddressDetails()) {
            //incompleteStep1.setVisibility(View.VISIBLE);
        }

        if (user.isIncompleteRepaymentSetup() || user.isIncompleteClassmateDetails()
                || user.isIncompleteVerificationDate() || user.isIncompleteStudentLoan())

        {
            //incompleteStep3.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void checkAndPopulateFamilyLayout2() {

        if (AppUtils.isNotEmpty(user.getPrefferedLanguageFamilyMemberType2()) || AppUtils.isNotEmpty(user.getProfessionFamilyMemberType2()) || AppUtils.isNotEmpty(user.getFamilyMemberType2()) || AppUtils.isNotEmpty(user.getPhoneFamilyMemberType2())) {
            showFamilyLayout2();
            if (AppUtils.isNotEmpty(user.getFamilyMemberType2())) {
                int spinnerPosition = adapter.getPosition(user.getFamilyMemberType2());
                familyMember2spinner.setSelection(spinnerPosition);
            }
            if (AppUtils.isNotEmpty(user.getProfessionFamilyMemberType2())) {
                int spinnerPosition = adapter2.getPosition(user.getProfessionFamilyMemberType2());
                professionFamilyMember2spinner.setSelection(spinnerPosition);
            }

            if (AppUtils.isNotEmpty(user.getPrefferedLanguageFamilyMemberType2())) {
                for (int i = 0; i < languageOptions.length - 1; i++) {
                    if (languageOptions[i].equalsIgnoreCase(user.getPrefferedLanguageFamilyMemberType2())) {
                        prefLangFamilyMember2.setSelection(i);
                        break;
                    }
                }
            }
            phoneFamilyMember2.setText(user.getPhoneFamilyMemberType2());
            isFamilyMemberAdded = true;
            addFamilyMember.setText("Remove this family member");


        }
    }

    private void showFamilyLayout2() {
        populateSpinner2FamilyMember();
        parentLL2.setVisibility(View.VISIBLE);
    }

    private void getAllViews(View rootView) {
        incorrectPhoneFamily1 = (TextView) rootView.findViewById(R.id.incorrect_phone_family_1);
        incorrectPhoneFamily2 = (TextView) rootView.findViewById(R.id.incorrect_phone_family_2);
        parentLL1 = (LinearLayout) rootView.findViewById(R.id.parent_ll_1);
        parentLL2 = (LinearLayout) rootView.findViewById(R.id.parent_ll_2);
        //saveAndProceed = (Button) rootView.findViewById(R.id.save_and_proceed);
        //previous = (Button) rootView.findViewById(R.id.previous);
        //gotoFragment1 = (TextView) rootView.findViewById(R.id.goto_fragment1);
        //gotoFragment2 = (TextView) rootView.findViewById(R.id.goto_fragment2);
        //gotoFragment3 = (TextView) rootView.findViewById(R.id.goto_fragment3);
        //incompleteStep1 = (ImageView) rootView.findViewById(R.id.incomplete_step_1);
        //incompleteStep2 = (ImageView) rootView.findViewById(R.id.incomplete_step_2);
        //incompleteStep3 = (ImageView) rootView.findViewById(R.id.incomplete_step_3);
        addFamilyMember = (TextView) rootView.findViewById(R.id.add_family_member);
        prefLangFamilyMember1 = (Spinner) rootView.findViewById(R.id.pref_lang_family_member1);
        prefLangFamilyMember2 = (Spinner) rootView.findViewById(R.id.pref_lang_family_member2);
        phoneFamilyMember1 = (EditText) rootView.findViewById(R.id.phone_number_family_member_1);
        phoneFamilyMember2 = (EditText) rootView.findViewById(R.id.phone_number_family_member_2);
        incompleteFamilyDetails = (ImageView) rootView.findViewById(R.id.incomplete_family_details);
        completeFamilyDetails = (ImageView) rootView.findViewById(R.id.complete_family_details);
        //topImage = (ImageView) rootView.findViewById(R.id.verify_image_view2);
        familyHelptip = (ImageButton) rootView.findViewById(R.id.family_helptip);
        view1 = (View) rootView.findViewById(R.id.view_family_member_2);
        view2 = (View) rootView.findViewById(R.id.profession_view_family_member_2);
        familyMember2spinner = (Spinner) rootView.findViewById(R.id.family_member_2);
        professionFamilyMember2spinner = (Spinner) rootView.findViewById(R.id.profession_family_member_2);
    }

    private void setOnClickListener() {


        phoneFamilyMember1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    String mobile = AppUtils.getFromSelectedSharedPrefs(getActivity(), "phone_number", "cred");
                    if (phoneFamilyMember1.getText().toString().equals(mobile)) {
                        incorrectPhoneFamily1.setText("This cant be your own number");
                        incorrectPhoneFamily1.setVisibility(View.VISIBLE);

                    } else if (!ValidationUtils.isValidPhoneNumber(phoneFamilyMember1.getText().toString())) {
                        incorrectPhoneFamily1.setText("Incorrect phone number");
                        incorrectPhoneFamily1.setVisibility(View.VISIBLE);

                    } else {
                        incorrectPhoneFamily1.setVisibility(View.GONE);
                    }
                }
            }
        });


        phoneFamilyMember2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    String mobile = AppUtils.getFromSelectedSharedPrefs(getActivity(), "phone_number", "cred");
                    if (phoneFamilyMember2.getText().toString().equals(mobile)) {
                        incorrectPhoneFamily2.setText("This cant be your own number");
                        incorrectPhoneFamily2.setVisibility(View.VISIBLE);
                    } else if (!ValidationUtils.isValidPhoneNumber(phoneFamilyMember2.getText().toString())) {
                        incorrectPhoneFamily2.setText("Incorrect phone number");
                        incorrectPhoneFamily2.setVisibility(View.VISIBLE);

                    } else {
                        incorrectPhoneFamily2.setVisibility(View.GONE);
                    }
                }
            }
        });


        addFamilyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFamilyMemberAdded) {
                    isFamilyMemberAdded = true;

                    showFamilyLayout2();
                    resetFamilyDetails();
                    addFamilyMember.setText("Remove this family member");
                } else {
                    isFamilyMemberAdded = false;
                    resetFamilyDetails();
                    hideFamilyDetails();
                    user.setUpdatePreferredLanguageFamilyMemberType2(true);
                    user.setPrefferedLanguageFamilyMemberType2("");

                    user.setPhoneFamilyMemberType2("");
                    user.setUpdatePhoneFamilyMemberType2(true);
                    addFamilyMember.setText("Add a family member");
                    user.setProfessionFamilyMemberType2("");
                    user.setFamilyMemberType2("");
                }
            }
        });


        //saveAndProceed.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //
        //
        //        checkIncomplete();
        //        String json = gson.toJson(user);
        //        mPrefs.edit().putString("UserObject", json).apply();
        //        Intent intent = new Intent(getActivity(), CheckInternetAndUploadUserDetails.class);
        //        getContext().sendBroadcast(intent);
        //        replaceFragment3(false);
        //    }
        //});

        //gotoFragment1.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        replaceFragment1(true);
        //    }
        //});
        //gotoFragment3.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        replaceFragment3(true);
        //    }
        //});
        if (user.isIncompleteFamilyDetails()) {
            //incompleteStep2.setVisibility(View.VISIBLE);
            incompleteFamilyDetails.setVisibility(View.VISIBLE);
        }
    }

    private void populateSpinner2FamilyMember() {

        user.setUpdateFamilyMemberType1(true);
        user.setUpdateFamilyMemberType2(true);
        familyMember2spinner.setEnabled(false);
        int position = familyMember1spinner.getSelectedItemPosition();
        String[] familyMember2Options = getActivity().getResources().getStringArray(R.array.family_member);
        List<String> list = new ArrayList<String>(Arrays.asList(familyMember2Options));
        list.remove(position);
        familyMember2Options = list.toArray(new String[0]);

        familyMember2spinner.setAdapter(new SpinnerHintAdapter(getActivity(), familyMember2Options, R.layout.spinner_item_underline));

    }


    private void hideFamilyDetails() {
        parentLL2.setVisibility(View.GONE);
    }


    private void setAllHelpTipsEnabled() {
        familyHelptip.setEnabled(true);
    }

    public void checkIncomplete() {
        //if (AppUtils.isNotEmpty(prefLangFamilyMember1.getText().toString())) {
        //    user.setPrefferedLanguageFamilyMemberType1(prefLangFamilyMember1.getText().toString());
        //    user.setUpdatePreferredLanguageFamilyMemberType1(true);
        //}
        if (AppUtils.isNotEmpty(phoneFamilyMember1.getText().toString()) && ValidationUtils.isValidPhoneNumber(phoneFamilyMember1.getText().toString()) && !phoneFamilyMember1.getText().toString().equals(mobile)) {
            user.setPhoneFamilyMemberType1(phoneFamilyMember1.getText().toString());
            user.setUpdatePhoneFamilyMemberType1(true);
        }
        if (isFamilyMemberAdded) {
            //if (AppUtils.isNotEmpty(prefLangFamilyMember2.getText().toString())) {
            //    user.setPrefferedLanguageFamilyMemberType2(prefLangFamilyMember2.getText().toString());
            //    user.setUpdatePreferredLanguageFamilyMemberType2(true);
            //}
            if (AppUtils.isNotEmpty(phoneFamilyMember2.getText().toString()) && ValidationUtils.isValidPhoneNumber(phoneFamilyMember2.getText().toString()) && !phoneFamilyMember2.getText().toString().equals(mobile)) {
                user.setPhoneFamilyMemberType2(phoneFamilyMember2.getText().toString());
                user.setUpdatePhoneFamilyMemberType2(true);
            }
        }

        if (!isFamilyMember1Selected || !isProfessionFamilyMember1Selected || AppUtils.isEmpty(user.getPrefferedLanguageFamilyMemberType1()) || AppUtils.isEmpty(user.getPhoneFamilyMemberType1())) {
            user.setIncompleteFamilyDetails(true);
            completeFamilyDetails.setVisibility(View.GONE);
            incompleteFamilyDetails.setVisibility(View.INVISIBLE);
        } else {
            user.setIncompleteFamilyDetails(false);
            incompleteFamilyDetails.setVisibility(View.GONE);
            completeFamilyDetails.setVisibility(View.INVISIBLE);
        }
    }

    private void resetFamilyDetails() {
        professionFamilyMember2spinner.setSelection(10);
        user.setPrefferedLanguageFamilyMemberType2("");
        phoneFamilyMember2.setText("");
    }

}