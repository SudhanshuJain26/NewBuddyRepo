package indwin.c3.shareapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.activities.ProfileFormStep3;
import indwin.c3.shareapp.adapters.SpinnerHintAdapter;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.HelpTipDialog;
import io.intercom.com.google.gson.Gson;

/**
 * Created by shubhang on 07/04/16.
 */
public class ProfileFormStep3Fragment1 extends Fragment {
    private boolean selectedAnnualFees = false, selectedScholarship = false,
            selectedScholarshipType = false, selectedStudentLoan = false;
    SharedPreferences mPrefs;
    Gson gson;
    TextView gotoFragment1, gotoFragment2, gotoFragment3;
    UserModel user;
    ImageView incompleteStep1, incompleteStep2, incompleteStep3;
    private final int top = 16, left = 16, right = 16, bottom = 16;
    Button saveAndProceed;
    ImageView incompleteAnnualFees, completeAnnualFees, incompleteScholarshipDetails, completeScholarshipDetails;
    ImageView topImage;
    EditText scholarshipAmount;
    String[] scholarshipType;
    Spinner scholarshipTypeSpinner;
    View viewScholarship;
    private ImageButton feesHelptip, scholarshipHelptip;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View rootView = inflater.inflate(
                R.layout.profile_form_step3_fragment1, container, false);
        getAllViews(rootView);

        ProfileFormStep3 profileFormStep3 = (ProfileFormStep3) getActivity();
        user = profileFormStep3.getUser();
        mPrefs = getActivity().getSharedPreferences("buddy", Context.MODE_PRIVATE);
        mPrefs.edit().putBoolean("visitedFormStep3Fragment2", true).apply();
        if (user.isAppliedFor60k()) {
            ProfileFormStep1Fragment1.setViewAndChildrenEnabled(rootView, false);
        }
        //mPrefs.edit().putBoolean("visitedFormStep3Fragment1", true).apply();
        //if (mPrefs.getBoolean("visitedFormStep3Fragment2", false)) {
        //    gotoFragment2.setAlpha(1);
        //    gotoFragment2.setClickable(true);
        //}
        //if (mPrefs.getBoolean("visitedFormStep3Fragment3", false)) {
        //    gotoFragment3.setAlpha(1);
        //    gotoFragment3.setClickable(true);
        //}


        setOnclickListener();
        final String annualFeesOptions[] = getResources().getStringArray(R.array.annual_fees);
        final String annualFeesValues[] = getResources().getStringArray(R.array.annual_fees_values);
        Spinner spinner = (Spinner) rootView.findViewById(R.id.annual_fees);
        SpinnerHintAdapter adapter = new SpinnerHintAdapter(getActivity(), annualFeesOptions, R.layout.spinner_item_underline);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < annualFeesOptions.length - 1) {
                    user.setAnnualFees(annualFeesValues[position]);
                    user.setUpdateAnnualFees(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setUpdateAnnualFees(false);
            }
        });
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());

        scholarshipType = getResources().getStringArray(R.array.scholarship_type);

        SpinnerHintAdapter scholarshipTypeAdapter = new SpinnerHintAdapter(getActivity(), scholarshipType, R.layout.spinner_item_underline);
        scholarshipTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scholarshipTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < scholarshipType.length - 1) {
                    selectedScholarshipType = true;
                    user.setScholarshipType(scholarshipType[position]);
                    user.setUpdateScholarshipType(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setUpdateScholarshipType(false);
            }
        });
        scholarshipTypeSpinner.setAdapter(scholarshipTypeAdapter);
        scholarshipTypeSpinner.setSelection(scholarshipTypeAdapter.getCount());

        scholarshipAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v == scholarshipAmount && !hasFocus) {
                    scholarshipAmount.clearFocus();
                    if (getActivity().getCurrentFocus() != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                }
            }
        });


        final String scholarship[] = getResources().getStringArray(R.array.scholarship);
        final String scholarshipValues[] = getResources().getStringArray(R.array.scholarship_values);
        Spinner scholarshipSpinner = (Spinner) rootView.findViewById(R.id.scholarship);
        SpinnerHintAdapter scholarshipAdapter = new SpinnerHintAdapter(getActivity(), scholarship, R.layout.spinner_item_underline);
        scholarshipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scholarshipSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < scholarship.length - 1) {
                    selectedScholarship = true;
                    user.setScholarship(scholarshipValues[position]);
                    user.setUpdateScholarship(true);
                }
                if (position == 1) {
                    scholarshipTypeSpinner.setVisibility(View.VISIBLE);
                    scholarshipAmount.setVisibility(View.VISIBLE);
                    viewScholarship.setVisibility(View.VISIBLE);
                } else {
                    scholarshipTypeSpinner.setVisibility(View.GONE);
                    scholarshipAmount.setVisibility(View.GONE);
                    viewScholarship.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setUpdateScholarship(false);
            }
        });
        scholarshipSpinner.setAdapter(scholarshipAdapter);
        scholarshipSpinner.setSelection(scholarshipAdapter.getCount());


        if (user.getAnnualFees() != null && !"".equals(user.getAnnualFees())) {
            for (int i = 0; i < annualFeesOptions.length - 1; i++) {
                if (user.getAnnualFees().equals(annualFeesValues[i])) {
                    spinner.setSelection(i);
                    completeAnnualFees.setVisibility(View.VISIBLE);
                    user.setIncompleteAnnualFees(false);
                    break;
                }
            }
        }
        if (user.getScholarship() != null && !"".equals(user.getScholarship())) {
            for (int i = 0; i < scholarship.length - 1; i++) {
                if (user.getScholarship().equals(scholarshipValues[i])) {
                    if (i == 1) {
                        checkTypeandAmount();
                    }
                    scholarshipSpinner.setSelection(i);
                    completeScholarshipDetails.setVisibility(View.VISIBLE);
                    user.setIncompleteScholarship(false);
                    checkForIncompleteScholarship();
                    break;
                }
            }
        }
        if ("Yes".equalsIgnoreCase(user.getScholarship()) || "true".equalsIgnoreCase(user.getScholarship()) && (AppUtils.isEmpty(user.getScholarshipType()) || AppUtils.isEmpty(user.getScholarshipAmount()))) {
            user.setIncompleteScholarship(true);
        }

        if (user.isIncompleteAnnualFees() || user.isIncompleteScholarship()) {
            if (user.isIncompleteAnnualFees())
                incompleteAnnualFees.setVisibility(View.VISIBLE);
            if (user.isIncompleteScholarship())
                incompleteScholarshipDetails.setVisibility(View.VISIBLE);

        }


        return rootView;
    }


    private void checkForIncompleteScholarship() {
        if ("Yes".equalsIgnoreCase(user.getScholarship()) || "true".equalsIgnoreCase(user.getScholarship()) && (AppUtils.isEmpty(user.getScholarshipType()) || AppUtils.isEmpty(user.getScholarshipAmount()))) {
            user.setIncompleteScholarship(true);
            incompleteScholarshipDetails.setVisibility(View.VISIBLE);
            completeScholarshipDetails.setVisibility(View.GONE);
        }
    }

    private void getAllViews(View rootView) {
        incompleteAnnualFees = (ImageView) rootView.findViewById(R.id.incomplete_annual_fees);
        completeAnnualFees = (ImageView) rootView.findViewById(R.id.complete_annual_fees);
        incompleteScholarshipDetails = (ImageView) rootView.findViewById(R.id.incomplete_scholarship_details);
        completeScholarshipDetails = (ImageView) rootView.findViewById(R.id.complete_scholarship_details);
        feesHelptip = (ImageButton) rootView.findViewById(R.id.fees_helptip);
        scholarshipHelptip = (ImageButton) rootView.findViewById(R.id.scholarship_helptip);
        scholarshipAmount = (EditText) rootView.findViewById(R.id.edit_scholarship_amount);
        viewScholarship = (View) rootView.findViewById(R.id.view_scholarship_type);
        saveAndProceed = (Button) rootView.findViewById(R.id.save_and_proceed);
        scholarshipTypeSpinner = (Spinner) rootView.findViewById(R.id.scholarship_type);
    }

    private void setOnclickListener() {

        feesHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "Upload your identity card(with photo) that has been provided to you by your college or institution.";
                String text2 = "Please remember to upload both front and back sides of the card.";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your College ID", text1, text2, "#44c2a6");
                dialog.show();
            }
        });
        scholarshipHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "Upload your identity card(with photo) that has been provided to you by your college or institution.";
                String text2 = "Please remember to upload both front and back sides of the card.";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your College ID", text1, text2, "#44c2a6");
                dialog.show();
            }
        });
    }

    public void checkIncomplete() {

        if (AppUtils.isEmpty(user.getAnnualFees())) {
            user.setIncompleteAnnualFees(true);
            completeAnnualFees.setVisibility(View.GONE);
            incompleteAnnualFees.setVisibility(View.VISIBLE);
        } else {
            user.setIncompleteAnnualFees(false);
            incompleteAnnualFees.setVisibility(View.GONE);
            completeAnnualFees.setVisibility(View.VISIBLE);
        }
        if ("".equals(scholarshipAmount.getText().toString().trim()) && scholarshipAmount.getVisibility() == View.VISIBLE)
            user.setIncompleteScholarship(true);
        else if (!"".equals(scholarshipAmount.getText().toString().trim())) {
            user.setScholarshipAmount(scholarshipAmount.getText().toString().trim());
            user.setUpdateScholarshipAmount(true);
        }
        if (!selectedScholarshipType && scholarshipTypeSpinner.getVisibility() == View.VISIBLE)
            user.setIncompleteScholarship(true);
        if (!selectedScholarship)
            user.setIncompleteScholarship(true);
        else
            user.setIncompleteScholarship(false);
        if ("Yes".equalsIgnoreCase(user.getScholarship()) || "true".equalsIgnoreCase(user.getScholarship()) && (AppUtils.isEmpty(user.getScholarshipType()) || AppUtils.isEmpty(user.getScholarshipAmount()))) {

            user.setIncompleteScholarship(true);
        }

        if (user.isIncompleteScholarship()) {
            completeScholarshipDetails.setVisibility(View.GONE);
            incompleteScholarshipDetails.setVisibility(View.VISIBLE);
        } else {
            incompleteScholarshipDetails.setVisibility(View.GONE);
            completeScholarshipDetails.setVisibility(View.VISIBLE);
        }

    }

    private void checkTypeandAmount() {
        if (user.getScholarshipType() != null && !"".equals(user.getScholarshipType())) {
            for (int i = 0; i < scholarshipType.length - 1; i++) {
                if (user.getScholarshipType().equals(scholarshipType[i])) {
                    scholarshipTypeSpinner.setSelection(i);
                    scholarshipTypeSpinner.setVisibility(View.VISIBLE);
                    viewScholarship.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
        if (user.getScholarshipAmount() != null && !"".equals(user.getScholarshipAmount())) {
            scholarshipAmount.setText(user.getScholarshipAmount());
            scholarshipAmount.setVisibility(View.VISIBLE);
        }
    }

}