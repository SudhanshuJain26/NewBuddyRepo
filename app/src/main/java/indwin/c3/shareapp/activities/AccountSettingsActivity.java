package indwin.c3.shareapp.activities;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import indwin.c3.shareapp.BuildConfig;
import indwin.c3.shareapp.R;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.CheckInternetAndUploadUserDetails;
import indwin.c3.shareapp.utils.FetchLatestUserDetails;
import indwin.c3.shareapp.utils.FetchNewToken;
import indwin.c3.shareapp.utils.ValidationUtils;
import io.intercom.android.sdk.Intercom;
import io.intercom.com.google.gson.Gson;

public class AccountSettingsActivity extends AppCompatActivity {

    TextView userName, userEmail;
    SharedPreferences mPrefs;
    Gson gson;
    UserModel user;
    ImageButton editName,
            editEmail;
    TextView saveName,
            saveEmail;
    public static Button verifyEmail;
    public static TextView changedPassword;
    EditText editTextName, editTextEmail;
    Button changePassword, deleteAccount;
    View nameUnderline,
            emailUnderline;
    LinearLayout changePasswordLayout, deleteAccountLayout;
    private TextView incorrectEmail;
    private String errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        verifyEmail = (Button) findViewById(R.id.verify_user_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setupClickToolbarBackButton(toolbar);
        try {
            TextView headerTitle = (TextView) findViewById(R.id.activity_header);
            headerTitle.setText("Account Settings");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            ImageView inter = (ImageView) findViewById(R.id.interCom);
            inter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
                        Intercom.client().displayMessageComposer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        incorrectEmail = (TextView) findViewById(R.id.incorrect_email);
        userName = (TextView) findViewById(R.id.user_name);
        userEmail = (TextView) findViewById(R.id.user_email);
        editName = (ImageButton) findViewById(R.id.edit_name);
        editEmail = (ImageButton) findViewById(R.id.edit_email);
        saveName = (TextView) findViewById(R.id.save_name);
        saveEmail = (TextView) findViewById(R.id.save_email);
        editTextName = (EditText) findViewById(R.id.user_name_edittext);
        editTextEmail = (EditText) findViewById(R.id.user_email_edittext);
        changePassword = (Button) findViewById(R.id.change_password);
        deleteAccount = (Button) findViewById(R.id.delete_account);
        changePasswordLayout = (LinearLayout) findViewById(R.id.change_password_layout);
        deleteAccountLayout = (LinearLayout) findViewById(R.id.delete_account_layout);
        nameUnderline = findViewById(R.id.name_view);
        emailUnderline = findViewById(R.id.email_underline);
        changedPassword = (TextView) findViewById(R.id.changed_password);

        mPrefs = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        gson = new Gson();
        String json = mPrefs.getString("UserObject", "");
        user = gson.fromJson(json, UserModel.class);

        if (user.isEmailVerified()) {
            verifyEmail.setText("Verified!");
            verifyEmail.setTextColor(Color.GRAY);
            verifyEmail.setClickable(false);
            verifyEmail.setEnabled(false);
            user.setIncompleteEmail(false);
        } else if (user.isEmailSent()) {
            verifyEmail.setText("Check");
            verifyEmail.setTextColor(Color.parseColor("#7c6a94"));
            verifyEmail.setClickable(true);
            verifyEmail.setEnabled(true);
            verifyEmail.setVisibility(View.VISIBLE);
        }

        userName.setText(user.getName());
        userEmail.setText(user.getEmail());
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName.setVisibility(View.GONE);
                editTextName.setText(userName.getText());
                editTextName.setVisibility(View.VISIBLE);
                editName.setVisibility(View.GONE);
                saveName.setVisibility(View.VISIBLE);
                LayoutParams params = (LinearLayout.LayoutParams) nameUnderline.getLayoutParams();
                params.setMargins(2, 0, 2, 0);
                nameUnderline.setLayoutParams(params);
            }
        });

        saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName.setText(editTextName.getText());
                editTextName.setVisibility(View.GONE);
                userName.setVisibility(View.VISIBLE);
                saveName.setVisibility(View.GONE);
                editName.setVisibility(View.VISIBLE);
                LayoutParams params = (LinearLayout.LayoutParams) nameUnderline.getLayoutParams();
                params.setMargins(2, 32, 2, 0);
                nameUnderline.setLayoutParams(params);
                user.setName(userName.getText().toString());
                user.setUpdateName(true);
                String json = gson.toJson(user);
                mPrefs.edit().putString("UserObject", json).apply();
                Intent intent = new Intent(AccountSettingsActivity.this, CheckInternetAndUploadUserDetails.class);
                sendBroadcast(intent);
            }
        });

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    saveName.setEnabled(false);

                    saveName.setAlpha(0.5f);
                } else {
                    saveName.setEnabled(true);

                    saveName.setAlpha(1);
                }
            }
        });
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                incorrectEmail.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        saveEmail.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             if (ValidationUtils.isValidEmail(editTextEmail.getText().toString())) {
                                                 if (!editTextEmail.getText().toString().equals(user.getEmail())) {
                                                     user.setEmail(editTextEmail.getText().toString());
                                                     user.setUpdateEmail(true);
                                                     user.setEmailVerified(false);
                                                     UserModel user = AppUtils.getUserObject(AccountSettingsActivity.this);
                                                     userEmail.setText(editTextEmail.getText().toString());
                                                     user.setEmail(editTextEmail.getText().toString());
                                                     user.setUpdateEmail(true);
                                                     user.setEmailVerified(false);
                                                     verifyEmail.setText("Verify");
                                                     AppUtils.saveUserObject(AccountSettingsActivity.this, user);
                                                 }
                                                 editTextEmail.setVisibility(View.GONE);
                                                 userEmail.setVisibility(View.VISIBLE);
                                                 verifyEmail.setVisibility(View.VISIBLE);
                                                 editEmail.setVisibility(View.VISIBLE);
                                                 saveEmail.setVisibility(View.GONE);
                                                 LayoutParams params = (LinearLayout.LayoutParams) emailUnderline.getLayoutParams();
                                                 params.setMargins(2, 32, 2, 0);
                                                 emailUnderline.setLayoutParams(params);
                                             } else {
                                                 incorrectEmail.setVisibility(View.VISIBLE);
                                             }
                                         }

                                     }
        );
        editEmail.setOnClickListener(new View.OnClickListener()

                                     {
                                         @Override
                                         public void onClick(View v) {
                                             userEmail.setVisibility(View.GONE);
                                             editTextEmail.setText(userEmail.getText());
                                             editTextEmail.requestFocus();
                                             editTextEmail.setVisibility(View.VISIBLE);
                                             editEmail.setVisibility(View.GONE);
                                             saveEmail.setVisibility(View.VISIBLE);
                                             verifyEmail.setTextColor(Color.parseColor("#7c6a94"));
                                             verifyEmail.setClickable(true);
                                             verifyEmail.setEnabled(true);
                                             verifyEmail.setVisibility(View.GONE);
                                             InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                             imm.showSoftInput(editTextEmail, InputMethodManager.SHOW_IMPLICIT);
                                             LayoutParams params = (LinearLayout.LayoutParams) emailUnderline.getLayoutParams();
                                             params.setMargins(2, 0, 2, 0);
                                             emailUnderline.setLayoutParams(params);
                                         }
                                     }
        );
        verifyEmail.setOnClickListener(new View.OnClickListener()

                                       {
                                           @Override
                                           public void onClick(View v) {
                                               if (!"".equals(userEmail.getText().toString()))
                                                   if ("Check".equals(verifyEmail.getText().toString()))
                                                       new FetchLatestUserDetails(AccountSettingsActivity.this, user.getUserId(), user).execute();
                                                   else {
                                                       new VerifyEmail(AccountSettingsActivity.this, user.getUserId(), userEmail.getText().toString()).execute();
                                                       user.setEmailSent(true);
                                                   }
                                           }
                                       }
        );

        changePassword.setOnClickListener(new View.OnClickListener()

                                          {
                                              @Override
                                              public void onClick(View v) {
                                                  Intent intent = new Intent(AccountSettingsActivity.this, ChangePasswordActivity.class);
                                                  startActivity(intent);
                                              }
                                          }
        );
        changePasswordLayout.setOnClickListener(new View.OnClickListener()

                                                {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(AccountSettingsActivity.this, ChangePasswordActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
        );
        deleteAccountLayout.setOnClickListener(new View.OnClickListener()

                                               {
                                                   @Override
                                                   public void onClick(View v) {
                                                       Intent intent = new Intent(AccountSettingsActivity.this, DeleteAccountActivity.class);
                                                       startActivity(intent);
                                                   }
                                               }
        );
        deleteAccount.setOnClickListener(new View.OnClickListener()

                                         {
                                             @Override
                                             public void onClick(View v) {
                                                 Intent intent = new Intent(AccountSettingsActivity.this, DeleteAccountActivity.class);
                                                 startActivity(intent);
                                             }
                                         }
        );
    }


    private void setupClickToolbarBackButton(Toolbar toolbar) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyboard(AccountSettingsActivity.this);
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AppUtils.hideKeyboard(this);
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public class VerifyEmail extends
                             AsyncTask<String, Void, String> {

        Context context;
        int retryCount = 0;
        String phone;
        String email;

        public VerifyEmail(Context context, String phone, String email) {
            this.context = context;
            this.phone = phone;
            this.email = email;
        }

        @Override
        protected String doInBackground(String... data) {
            try {
                HttpClient client = new DefaultHttpClient();
                String url = BuildConfig.SERVER_URL + "api/auth/verify/email?phone=" + phone + "&email=" + email;
                HttpGet postReq = new HttpGet(url);
                postReq.setHeader("Accept", "application/json");
                postReq.setHeader("Content-type", "application/json");
                SharedPreferences toks = context.getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                postReq.setHeader("x-access-token", tok_sp);
                HttpResponse httpresponse = client.execute(postReq);
                String responseText = EntityUtils.toString(httpresponse.getEntity());
                JSONObject json = new JSONObject(responseText);
                if (json.get("status").toString().contains("success")) {
                    if (json.get("msg").toString().contains("sent")) {
                        return "success";
                    }
                } else {
                    if (json.get("status").toString().contains("error")) {
                        try {
                            errorMsg = json.getString("msg");
                        } catch (Exception e) {
                        }
                        Log.d("Error", json.get("msg").toString());
                        return "fail";
                    } else if (json.get("msg").toString().contains("Invalid Token")) {
                        return "authFailed";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "fail";
        }

        protected void onPostExecute(String result) {
            try {
                if (retryCount < 3) {
                    retryCount++;
                    if (result.equals("success")) {
                        Toast.makeText(context, context.getResources().getString(R.string.confirmation_email_link), Toast.LENGTH_SHORT).show();
                        AccountSettingsActivity.verifyEmail.setText("Check");
                    } else if (result.equals("authFailed")) {
                        new FetchNewToken(context).execute();
                        new VerifyEmail(context, phone, email).execute();
                    } else if (result.equals("fail"))
                        try {
                            showErrorEmailMsg(errorMsg);
                        } catch (Exception e) {
                        }
                } else {
                    retryCount = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showErrorEmailMsg(String msg) {
        if (AppUtils.isNotEmpty(msg)) {
            incorrectEmail.setText(msg);
            incorrectEmail.setVisibility(View.VISIBLE);
        }
    }
}
