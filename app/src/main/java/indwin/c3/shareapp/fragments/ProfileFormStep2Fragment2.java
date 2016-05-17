package indwin.c3.shareapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.adapters.SpinnerHintAdapter;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.CheckInternetAndUploadUserDetails;
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
    private EditText designationFamilyMember1, designationFamilyMember2, phoneFamilyMember1, phoneFamilyMember2;
    boolean isFamilyMember1Selected = false, isProfessionFamilyMember1Selected = false, isFamilyMember2Selected = false, isProfessionFamilyMember2Selected = false;
    ImageView topImage;
    View view1, view2;
    private ImageButton familyHelptip;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.profile_form_step2_fragment2, container, false);
        RecyclerView rvImages = (RecyclerView) rootView.findViewById(R.id.rvImages);
        mPrefs = getActivity().getSharedPreferences("buddy", Context.MODE_PRIVATE);
        mPrefs.edit().putBoolean("visitedFormStep2Fragment2", true).apply();
        gson = new Gson();
        String json = mPrefs.getString("UserObject", "");
        user = gson.fromJson(json, UserModel.class);

        saveAndProceed = (Button) rootView.findViewById(R.id.save_and_proceed);
        previous = (Button) rootView.findViewById(R.id.previous);
        gotoFragment1 = (TextView) rootView.findViewById(R.id.goto_fragment1);
        gotoFragment2 = (TextView) rootView.findViewById(R.id.goto_fragment2);
        gotoFragment3 = (TextView) rootView.findViewById(R.id.goto_fragment3);
        incompleteStep1 = (ImageView) rootView.findViewById(R.id.incomplete_step_1);
        incompleteStep2 = (ImageView) rootView.findViewById(R.id.incomplete_step_2);
        incompleteStep3 = (ImageView) rootView.findViewById(R.id.incomplete_step_3);
        addFamilyMember = (TextView) rootView.findViewById(R.id.add_family_member);
        designationFamilyMember1 = (EditText) rootView.findViewById(R.id.designation_family_member_1);
        designationFamilyMember2 = (EditText) rootView.findViewById(R.id.designation_family_member_2);
        phoneFamilyMember1 = (EditText) rootView.findViewById(R.id.phone_number_family_member_1);
        phoneFamilyMember2 = (EditText) rootView.findViewById(R.id.phone_number_family_member_2);
        incompleteFamilyDetails = (ImageView) rootView.findViewById(R.id.incomplete_family_details);
        completeFamilyDetails = (ImageView) rootView.findViewById(R.id.complete_family_details);
        topImage = (ImageView) rootView.findViewById(R.id.verify_image_view2);
        familyHelptip = (ImageButton) rootView.findViewById(R.id.family_helptip);
        view1 = (View) rootView.findViewById(R.id.view_family_member_2);
        view2 = (View) rootView.findViewById(R.id.profession_view_family_member_2);

        if (!mPrefs.getBoolean("step2Editable", true)) {
            ProfileFormStep1Fragment1.setViewAndChildrenEnabled(rootView, false, gotoFragment1, gotoFragment3);
        }
        setAllHelpTipsEnabled();
        if (mPrefs.getBoolean("visitedFormStep2Fragment2", false)) {
            gotoFragment2.setAlpha(1);
            gotoFragment2.setClickable(true);
        }
        if (mPrefs.getBoolean("visitedFormStep2Fragment3", false)) {
            gotoFragment3.setAlpha(1);
            gotoFragment3.setClickable(true);
        }

        if (user.getGender() != null && "girl".equals(user.getGender())) {
            Picasso.with(getActivity())
                    .load(R.mipmap.step2fragment2girl)
                    .into(topImage);
        }
        familyHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "Entering details of more family members, especially earning members may increase your chances at getting a higher Credit Limit.";
                String text2 = "";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your College ID", text1, text2, "#eeb85f");
                dialog.show();
            }
        });

        if (user.isAppliedFor7k()) {
            previous.setVisibility(View.INVISIBLE);
            saveAndProceed.setVisibility(View.INVISIBLE);
            rootView.findViewById(R.id.details_submitted_tv).setVisibility(View.VISIBLE);
        }

        final String familyMemberOptions[] = getResources().getStringArray(R.array.family_member);
        SpinnerHintAdapter adapter = new SpinnerHintAdapter(getActivity(), familyMemberOptions, R.layout.spinner_item_underline);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner familyMember1spinner = (Spinner) rootView.findViewById(R.id.family_member_1);
        familyMember1spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < familyMemberOptions.length - 1) {
                    user.setFamilyMemberType1(familyMemberOptions[position].toLowerCase());
                    user.setUpdateFamilyMemberType1(true);
                    isFamilyMember1Selected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setUpdateFamilyMemberType1(false);
            }
        });
        familyMember1spinner.setAdapter(adapter);
        familyMember1spinner.setSelection(adapter.getCount());

        final Spinner familyMember2spinner = (Spinner) rootView.findViewById(R.id.family_member_2);
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
        familyMember2spinner.setSelection(adapter.getCount());

        final String professionFamilyMemberOptions[] = getResources().getStringArray(R.array.profession_family_member);
        SpinnerHintAdapter adapter2 = new SpinnerHintAdapter(getActivity(), professionFamilyMemberOptions, R.layout.spinner_item_underline);
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

        final Spinner professionFamilyMember2spinner = (Spinner) rootView.findViewById(R.id.profession_family_member_2);
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

        if (!"".equals(user.getFamilyMemberType1())) {
            for (int i = 0; i < familyMemberOptions.length - 1; i++) {
                if (familyMemberOptions[i].equalsIgnoreCase(user.getFamilyMemberType1())) {
                    familyMember1spinner.setSelection(i);
                    break;
                }
            }
        }
        if (!"".equals(user.getProfessionFamilyMemberType1())) {
            for (int i = 0; i < professionFamilyMemberOptions.length - 1; i++) {
                if (professionFamilyMemberOptions[i].equalsIgnoreCase(user.getProfessionFamilyMemberType1())) {
                    professionFamilyMember1spinner.setSelection(i);
                    break;
                }
            }
        }
        if (!"".equals(user.getDesignationFamilyMemberType1())) {
            designationFamilyMember1.setText(user.getDesignationFamilyMemberType1());
        }
        if (!"".equals(user.getPhoneFamilyMemberType1())) {
            phoneFamilyMember1.setText(user.getPhoneFamilyMemberType1());
        }
        if (AppUtils.isNotEmpty(user.getFamilyMemberType1())
                && user.getProfessionFamilyMemberType1() != null && !"".equals(user.getProfessionFamilyMemberType1())
                && user.getDesignationFamilyMemberType1() != null &&
                !"".equals(user.getDesignationFamilyMemberType1()) &&
                user.getPhoneFamilyMemberType1() != null && !"".equals(user.getPhoneFamilyMemberType1())) {
            completeFamilyDetails.setVisibility(View.VISIBLE);
            user.setIncompleteFamilyDetails(false);
        }
        if (AppUtils.isNotEmpty(user.getFamilyMemberType2())) {
            for (int i = 0; i < familyMemberOptions.length - 1; i++) {
                if (familyMemberOptions[i].equalsIgnoreCase(user.getFamilyMemberType2())) {
                    familyMember2spinner.setSelection(i);
                    break;
                }
            }
            if (!"".equals(user.getProfessionFamilyMemberType2())) {
                for (int i = 0; i < professionFamilyMemberOptions.length - 1; i++) {
                    if (professionFamilyMemberOptions[i].equalsIgnoreCase(user.getProfessionFamilyMemberType2())) {
                        professionFamilyMember2spinner.setSelection(i);
                        break;
                    }
                }
            }
            if (!"".equals(user.getDesignationFamilyMemberType2())) {
                designationFamilyMember2.setText(user.getDesignationFamilyMemberType2());
            }
            if (!"".equals(user.getPhoneFamilyMemberType2())) {
                phoneFamilyMember2.setText(user.getPhoneFamilyMemberType2());
            }
        }
        addFamilyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFamilyMemberAdded) {
                    isFamilyMemberAdded = true;
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                    familyMember2spinner.setVisibility(View.VISIBLE);
                    professionFamilyMember2spinner.setVisibility(View.VISIBLE);
                    designationFamilyMember2.setVisibility(View.VISIBLE);
                    phoneFamilyMember2.setVisibility(View.VISIBLE);
                    addFamilyMember.setText("Remove this family member");
                } else {
                    isFamilyMemberAdded = false;
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    familyMember2spinner.setVisibility(View.GONE);
                    professionFamilyMember2spinner.setVisibility(View.GONE);
                    designationFamilyMember2.setVisibility(View.GONE);
                    phoneFamilyMember2.setVisibility(View.GONE);
                    addFamilyMember.setText("Add a family member");
                }
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment1(true);
            }
        });

        saveAndProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIncomplete();
                if (!"".equals(designationFamilyMember1.getText().toString())) {
                    user.setDesignationFamilyMemberType1(designationFamilyMember1.getText().toString());
                    user.setUpdateDesignationFamilyMemberType1(true);
                }
                if (!"".equals(phoneFamilyMember1.getText().toString()) && ValidationUtils.isValidPhoneNumber(phoneFamilyMember1.getText().toString())) {
                    user.setPhoneFamilyMemberType1(phoneFamilyMember1.getText().toString());
                    user.setUpdatePhoneFamilyMemberType1(true);
                }
                if (isFamilyMemberAdded) {
                    if (!"".equals(designationFamilyMember2.getText().toString())) {
                        user.setDesignationFamilyMemberType2(designationFamilyMember2.getText().toString());
                        user.setUpdateDesignationFamilyMemberType2(true);
                    }
                    if (!"".equals(phoneFamilyMember2.getText().toString()) && ValidationUtils.isValidPhoneNumber(phoneFamilyMember2.getText().toString())) {
                        user.setPhoneFamilyMemberType2(phoneFamilyMember2.getText().toString());
                        user.setUpdatePhoneFamilyMemberType2(true);
                    }
                }
                String json = gson.toJson(user);
                mPrefs.edit().putString("UserObject", json).apply();
                Intent intent = new Intent(getActivity(), CheckInternetAndUploadUserDetails.class);
                getContext().sendBroadcast(intent);
                replaceFragment3(false);
            }
        });

        gotoFragment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment1(true);
            }
        });
        gotoFragment3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment3(true);
            }
        });
        if (user.isIncompleteFamilyDetails()) {
            incompleteStep2.setVisibility(View.VISIBLE);
            incompleteFamilyDetails.setVisibility(View.VISIBLE);
        }
        if (user.isIncompleteDOB() || user.isIncompleteAddressDetails()) {
            incompleteStep1.setVisibility(View.VISIBLE);
        }
        if (user.isIncompleteRepaymentSetup() || user.isIncompleteClassmateDetails() || user.isIncompleteVerificationDate()) {
            incompleteStep3.setVisibility(View.VISIBLE);
        }
        return rootView;
    }


    private void setAllHelpTipsEnabled() {
        familyHelptip.setEnabled(true);
    }

    private void checkIncomplete() {
        if (!isFamilyMember1Selected || !isProfessionFamilyMember1Selected) {
            user.setIncompleteFamilyDetails(true);
        } else {
            user.setIncompleteFamilyDetails(false);
        }
    }

    private void replaceFragment1(boolean check) {
        if (check)
            checkIncomplete();
        String json = gson.toJson(user);
        mPrefs.edit().putString("UserObject", json).apply();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment1, new ProfileFormStep2Fragment1(), "Fragment1Tag");
        ft.commit();
    }

    private void replaceFragment3(boolean check) {
        if (check)
            checkIncomplete();
        String json = gson.toJson(user);
        mPrefs.edit().putString("UserObject", json).apply();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment1, new ProfileFormStep2Fragment3(), "Fragment3Tag");
        ft.commit();
    }
}