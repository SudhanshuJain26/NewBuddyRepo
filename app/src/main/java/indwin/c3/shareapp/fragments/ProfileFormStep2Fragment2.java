package indwin.c3.shareapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import indwin.c3.shareapp.models.Error;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.HelpTipDialog;
import indwin.c3.shareapp.utils.ValidationUtils;

/**
 * Created by ROCK
 */
public class ProfileFormStep2Fragment2 extends Fragment {
    private SharedPreferences mPrefs;
    private UserModel user;
    private TextView addFamilyMember;
    ImageView incompleteFamilyDetails, completeFamilyDetails;
    boolean isFamilyMemberAdded = false;
    private EditText phoneFamilyMember1, phoneFamilyMember2;
    private Spinner prefLangFamilyMember1, prefLangFamilyMember2;
    boolean isFamilyMember1Selected = false, isProfessionFamilyMember1Selected = false, isFamilyMember2Selected = false, isProfessionFamilyMember2Selected = false;
    private ImageButton familyHelptip;
    private Spinner familyMember2spinner, professionFamilyMember2spinner, familyMember1spinner;
    private SpinnerHintAdapter adapter, adapter2, languageAdapter;
    private LinearLayout parentLL1, parentLL2;
    private TextView incorrectPhoneFamily1, incorrectPhoneFamily2;
    private String mobile;
    private String languageOptions[];
    private String phoneFamily1;
    private boolean phoneFamily1Update = false;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.profile_form_step2_fragment2, container, false);
        RecyclerView rvImages = (RecyclerView) rootView.findViewById(R.id.rvImages);
        mPrefs = getActivity().getSharedPreferences("buddy", Context.MODE_PRIVATE);
        mPrefs.edit().putBoolean("visitedFormStep2Fragment2", true).apply();
        ProfileFormStep2 profileFormStep2 = (ProfileFormStep2) getActivity();
        user = profileFormStep2.getUser();
        mobile = AppUtils.getFromSelectedSharedPrefs(getActivity(), "phone_number", "cred");
        getAllViews(rootView);
        if (user.isAppliedFor7k()) {
            ProfileFormStep1Fragment1.setViewAndChildrenEnabled(rootView, false);
        }
        setAllHelpTipsEnabled();

        familyHelptip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = "Enter your family details correctly to ensure quick approval. We will confirm a few details such as your name, age and college with your Parent/Guardian. We will not disclose any other details or reveal what you intend to buy";
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
                && AppUtils.isNotEmpty(user.getProfessionFamilyMemberType1())
                && AppUtils.isNotEmpty(user.getPrefferedLanguageFamilyMemberType1()) &&
                AppUtils.isNotEmpty(user.getPhoneFamilyMemberType1())) {
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
        addFamilyMember = (TextView) rootView.findViewById(R.id.add_family_member);
        prefLangFamilyMember1 = (Spinner) rootView.findViewById(R.id.pref_lang_family_member1);
        prefLangFamilyMember2 = (Spinner) rootView.findViewById(R.id.pref_lang_family_member2);
        phoneFamilyMember1 = (EditText) rootView.findViewById(R.id.phone_number_family_member_1);
        phoneFamilyMember2 = (EditText) rootView.findViewById(R.id.phone_number_family_member_2);
        incompleteFamilyDetails = (ImageView) rootView.findViewById(R.id.incomplete_family_details);
        completeFamilyDetails = (ImageView) rootView.findViewById(R.id.complete_family_details);
        familyHelptip = (ImageButton) rootView.findViewById(R.id.family_helptip);
        familyMember2spinner = (Spinner) rootView.findViewById(R.id.family_member_2);
        professionFamilyMember2spinner = (Spinner) rootView.findViewById(R.id.profession_family_member_2);
    }

    private void setOnClickListener() {
        phoneFamilyMember1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                incorrectPhoneFamily1.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneFamily1 = s.toString();
                phoneFamily1Update = true;

            }
        });

        phoneFamilyMember2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                incorrectPhoneFamily2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phoneFamilyMember1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    checkAndVaildatePhoneNumber1(phoneFamilyMember1.getText().toString());
                }
            }
        });


        phoneFamilyMember2.setOnFocusChangeListener(new View.OnFocusChangeListener()

                                                    {
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
                                                                    user.setPhoneFamilyMemberType2(phoneFamilyMember2.getText().toString());
                                                                    user.setUpdatePhoneFamilyMemberType2(true);
                                                                    incorrectPhoneFamily2.setVisibility(View.GONE);
                                                                }
                                                            }
                                                        }
                                                    }

        );


        addFamilyMember.setOnClickListener(new View.OnClickListener()

                                           {
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
                                           }

        );

        if (user.isIncompleteFamilyDetails())

        {
            incompleteFamilyDetails.setVisibility(View.VISIBLE);
        }

    }

    private void checkAndVaildatePhoneNumber1(String phoneNumber) {
        String mobile = AppUtils.getFromSelectedSharedPrefs(getActivity(), "phone_number", "cred");
        if (phoneNumber.equals(mobile)) {
            incorrectPhoneFamily1.setText("This cant be your own number");
            incorrectPhoneFamily1.setVisibility(View.VISIBLE);

        } else if (!ValidationUtils.isValidPhoneNumber(phoneNumber)) {
            incorrectPhoneFamily1.setText("Incorrect phone number");
            incorrectPhoneFamily1.setVisibility(View.VISIBLE);

        } else {
            incorrectPhoneFamily1.setVisibility(View.GONE);
            user.setPhoneFamilyMemberType1(phoneNumber);
            user.setUpdatePhoneFamilyMemberType1(true);
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
        if (phoneFamily1Update) {
            checkAndVaildatePhoneNumber1(phoneFamilyMember1.getText().toString());
        }
        if (AppUtils.isNotEmpty(user.getFamilyMemberType1())
                && AppUtils.isNotEmpty(user.getProfessionFamilyMemberType1())
                && AppUtils.isNotEmpty(user.getPrefferedLanguageFamilyMemberType1()) &&
                AppUtils.isNotEmpty(user.getPhoneFamilyMemberType1())) {
            completeFamilyDetails.setVisibility(View.VISIBLE);
            incompleteFamilyDetails.setVisibility(View.GONE);
            user.setIncompleteFamilyDetails(false);
        } else {
            incompleteFamilyDetails.setVisibility(View.VISIBLE);
            completeFamilyDetails.setVisibility(View.GONE);
            user.setIncompleteFamilyDetails(true);
        }
        if (isFamilyMemberAdded) {
            if (AppUtils.isNotEmpty(phoneFamilyMember2.getText().toString()) && ValidationUtils.isValidPhoneNumber(phoneFamilyMember2.getText().toString()) && !phoneFamilyMember2.getText().toString().equals(mobile)) {
                user.setPhoneFamilyMemberType2(phoneFamilyMember2.getText().toString());
                user.setUpdatePhoneFamilyMemberType2(true);
            }
        }
        phoneFamily1Update = false;

    }

    private void resetFamilyDetails() {
        professionFamilyMember2spinner.setSelection(10);
        user.setPrefferedLanguageFamilyMemberType2("");
        phoneFamilyMember2.setText("");
    }

    public void showFamilyMemberError(Error error) {
        if (error.getValue() != null && error.getValue().getRelation().equalsIgnoreCase(familyMember1spinner.getSelectedItem().toString())) {
            incorrectPhoneFamily1.setText(error.getError());
            incorrectPhoneFamily1.setVisibility(View.VISIBLE);
            incompleteFamilyDetails.setVisibility(View.VISIBLE);
            completeFamilyDetails.setVisibility(View.GONE);
            user.setPhoneFamilyMemberType1(null);
            user.setUpdatePhoneFamilyMemberType1(true);
        } else if (error.getValue() != null && error.getValue().getRelation().equalsIgnoreCase(familyMember2spinner.getSelectedItem().toString())) {
            incorrectPhoneFamily2.setText(error.getError());
            incorrectPhoneFamily2.setVisibility(View.VISIBLE);
        }
    }
}