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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.adapters.SpinnerHintAdapter;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.CheckInternetAndUploadUserDetails;
import indwin.c3.shareapp.utils.HelpTipDialog;
import io.intercom.com.google.gson.Gson;

/**
 * Created by shubhang on 07/04/16.
 */
public class ProfileFormStep3Fragment2 extends Fragment {
    private SharedPreferences mPrefs;
    private UserModel user;
    private Button saveAndProceed, previous;
    private Gson gson;
    private TextView gotoFragment1, gotoFragment3, gotoFragment2;
    private final int top = 16, left = 16, right = 16, bottom = 16;
    ImageView incompleteStep1, incompleteStep2, incompleteStep3;
    boolean isMonthlyExpenditureSelected = false, isVehicleSelected = false, isVehicleTypeSelected = false;
    ImageView incompleteMonthlyExpenditure, completeMonthlyExpenditure, incompleteVehicleDetails, completeVehicleDetails;
    ImageView topImage;
    View viewVehicleType;
    Spinner vehicleSpinnerType;
    private ImageButton expenditureHelptip;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.profile_form_step3_fragment2, container, false);
        RecyclerView rvImages = (RecyclerView) rootView.findViewById(R.id.rvImages);
        mPrefs = getActivity().getSharedPreferences("buddy", Context.MODE_PRIVATE);
        mPrefs.edit().putBoolean("visitedFormStep3Fragment2", true).apply();
        gson = new Gson();
        String json = mPrefs.getString("UserObject", "");
        user = gson.fromJson(json, UserModel.class);

        getAllViews(rootView);
        if (!mPrefs.getBoolean("step3Editable", true)) {
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
                    .load(R.mipmap.step3fragment2girl)
                    .into(topImage);
        }
        expenditureHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "This amount should reflect how much you spend per month on personal expenses (beside  academic expenses)";
                String text2 = "";
                Dialog dialog = new HelpTipDialog(getActivity(), "Upload your College ID", text1, text2, "#f2954e");
                dialog.show();
            }
        });

        final String monthlyExpenditure[] = getResources().getStringArray(R.array.monthly_expenditure);
        final String monthlyExpenditureValues[] = getResources().getStringArray(R.array.monthly_expenditure_values);
        SpinnerHintAdapter adapter = new SpinnerHintAdapter(getActivity(), monthlyExpenditure, R.layout.spinner_item_underline);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner monthlyExpenditureSpinner = (Spinner) rootView.findViewById(R.id.monthly_expenditure);
        monthlyExpenditureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < monthlyExpenditure.length - 1) {
                    user.setMonthlyExpenditure(monthlyExpenditureValues[position]);
                    user.setUpdateMonthlyExpenditure(true);
                    isMonthlyExpenditureSelected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setUpdateMonthlyExpenditure(false);
            }
        });
        monthlyExpenditureSpinner.setAdapter(adapter);
        monthlyExpenditureSpinner.setSelection(adapter.getCount());

        final String vehicleType[] = getResources().getStringArray(R.array.vehicles_type);
        SpinnerHintAdapter vehicleTypeAdapter = new SpinnerHintAdapter(getActivity(), vehicleType, R.layout.spinner_item_underline);
        vehicleTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        vehicleSpinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < vehicleType.length - 1) {
                    user.setVehicleType(vehicleType[position]);
                    user.setUpdateVehicleType(true);
                    isVehicleTypeSelected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setUpdateVehicleType(false);
            }
        });
        vehicleSpinnerType.setAdapter(vehicleTypeAdapter);

        final String vehicle[] = getResources().getStringArray(R.array.scholarship);
        final String vehicleValues[] = getResources().getStringArray(R.array.scholarship_values);
        final Spinner vehicleSpinner = (Spinner) rootView.findViewById(R.id.vehicle);
        SpinnerHintAdapter vehicleAdapter = new SpinnerHintAdapter(getActivity(), vehicle, R.layout.spinner_item_underline);
        vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < vehicle.length - 1) {
                    user.setVehicle(vehicleValues[position]);
                    user.setUpdateVehicle(true);
                    isVehicleSelected = true;
                }
                if (position == 1) {
                    vehicleSpinnerType.setVisibility(View.VISIBLE);
                    viewVehicleType.setVisibility(View.VISIBLE);
                } else {
                    vehicleSpinnerType.setVisibility(View.GONE);
                    viewVehicleType.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setUpdateVehicle(false);
            }
        });
        vehicleSpinner.setAdapter(vehicleAdapter);
        vehicleSpinner.setSelection(vehicleAdapter.getCount());

        if (user.getMonthlyExpenditure() != null && !"".equals(user.getMonthlyExpenditure())) {
            for (int i = 0; i < monthlyExpenditure.length - 1; i++) {
                if (user.getMonthlyExpenditure().equals(monthlyExpenditureValues[i])) {
                    monthlyExpenditureSpinner.setSelection(i);
                    completeMonthlyExpenditure.setVisibility(View.VISIBLE);
                    user.setIncompleteMonthlyExpenditure(false);
                }
            }
        }
        if (user.getVehicle() != null && !"".equals(user.getVehicle())) {
            for (int i = 0; i < vehicle.length - 1; i++) {
                if (user.getVehicle().equals(vehicleValues[i])) {
                    vehicleSpinner.setSelection(i);
                    completeVehicleDetails.setVisibility(View.VISIBLE);
                    user.setIncompleteVehicleDetails(false);
                }
            }
        }
        if (user.getVehicleType() != null && !"".equals(user.getVehicleType())) {
            for (int i = 0; i < vehicleType.length - 1; i++) {
                if (user.getVehicleType().equals(vehicleType[i])) {
                    vehicleSpinnerType.setSelection(i);
                    vehicleSpinnerType.setVisibility(View.VISIBLE);
                    viewVehicleType.setVisibility(View.VISIBLE);
                    completeVehicleDetails.setVisibility(View.VISIBLE);
                    user.setIncompleteVehicleDetails(false);
                }
            }
        }
        setOnClickListener();

        if (user.isIncompleteAnnualFees() || user.isIncompleteScholarship()) {
            incompleteStep1.setVisibility(View.VISIBLE);
        }
        if (user.isIncompleteMonthlyExpenditure() || user.isIncompleteVehicleDetails()) {
            incompleteStep2.setVisibility(View.VISIBLE);
            if (user.isIncompleteMonthlyExpenditure()) {
                incompleteMonthlyExpenditure.setVisibility(View.VISIBLE);
            }
            if (user.isIncompleteVehicleDetails())
                incompleteVehicleDetails.setVisibility(View.VISIBLE);
        }
        if (user.isIncompleteBankStmt()) {
            incompleteStep3.setVisibility(View.VISIBLE);
        }
        if (user.isAppliedFor60k()) {
            previous.setVisibility(View.INVISIBLE);
            saveAndProceed.setVisibility(View.INVISIBLE);
            rootView.findViewById(R.id.details_submitted_tv).setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void getAllViews(View rootView) {
        saveAndProceed = (Button) rootView.findViewById(R.id.save_and_proceed);
        previous = (Button) rootView.findViewById(R.id.previous);
        gotoFragment1 = (TextView) rootView.findViewById(R.id.goto_fragment1);
        gotoFragment2 = (TextView) rootView.findViewById(R.id.goto_fragment2);
        gotoFragment3 = (TextView) rootView.findViewById(R.id.goto_fragment3);
        incompleteStep1 = (ImageView) rootView.findViewById(R.id.incomplete_step_1);
        incompleteStep2 = (ImageView) rootView.findViewById(R.id.incomplete_step_2);
        incompleteStep3 = (ImageView) rootView.findViewById(R.id.incomplete_step_3);
        completeMonthlyExpenditure = (ImageView) rootView.findViewById(R.id.complete_monthly_expenditure);
        incompleteMonthlyExpenditure = (ImageView) rootView.findViewById(R.id.incomplete_monthly_expenditure);
        completeVehicleDetails = (ImageView) rootView.findViewById(R.id.complete_vehicle_details);
        incompleteVehicleDetails = (ImageView) rootView.findViewById(R.id.incomplete_vehicle_details);
        topImage = (ImageView) rootView.findViewById(R.id.verify_image_view2);
        expenditureHelptip = (ImageButton) rootView.findViewById(R.id.expenditure_helptip);
        viewVehicleType = (View) rootView.findViewById(R.id.view_vehicle_type);
        vehicleSpinnerType = (Spinner) rootView.findViewById(R.id.vehicle_type);

    }

    private void setOnClickListener() {

        saveAndProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIncomplete();
                String json = gson.toJson(user);
                mPrefs.edit().putString("UserObject", json).apply();
                Intent intent = new Intent(getActivity(), CheckInternetAndUploadUserDetails.class);
                getContext().sendBroadcast(intent);
                replaceFragment3(false);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment1(true);
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
    }


    private void setAllHelpTipsEnabled() {
        expenditureHelptip.setEnabled(true);

    }

    private void checkIncomplete() {
        if (!isMonthlyExpenditureSelected)
            user.setIncompleteMonthlyExpenditure(true);
        else
            user.setIncompleteMonthlyExpenditure(false);
        if (!isVehicleTypeSelected && vehicleSpinnerType.getVisibility() == View.VISIBLE)
            user.setIncompleteVehicleDetails(true);
        if (!isVehicleSelected)
            user.setIncompleteVehicleDetails(true);
        else
            user.setIncompleteVehicleDetails(false);
    }

    private void replaceFragment1(boolean check) {
        if (check)
            checkIncomplete();
        String json = gson.toJson(user);
        mPrefs.edit().putString("UserObject", json).apply();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment1, new ProfileFormStep3Fragment1(), "Fragment1Tag");
        ft.commit();
    }

    private void replaceFragment3(boolean check) {
        if (check)
            checkIncomplete();
        String json = gson.toJson(user);
        mPrefs.edit().putString("UserObject", json).apply();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment1, new ProfileFormStep3Fragment3(), "Fragment3Tag");
        ft.commit();
    }
}