package indwin.c3.shareapp.activities;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import indwin.c3.shareapp.BuildConfig;
import indwin.c3.shareapp.GIFView;
import indwin.c3.shareapp.MainActivity;
import indwin.c3.shareapp.R;
import indwin.c3.shareapp.Splash;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.Constants;
import indwin.c3.shareapp.utils.FetchNewToken;
import io.intercom.android.sdk.Intercom;
import io.intercom.com.google.gson.Gson;

public class DeleteAccountActivity extends AppCompatActivity {
    private LinearLayout incorrectPasswordLayout;
    private EditText deleteReason, password;
    private Button delete;
    private GIFView loading;
    private Gson gson;
    private SharedPreferences mPrefs;
    private UserModel
            user;
    private TextView incorrectPassword;
    int retryCount = 0;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            TextView headerTitle = (TextView) findViewById(R.id.activity_header);
            headerTitle.setText("Delete Account");
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

        delete = (Button) findViewById(R.id.delete_account);
        deleteReason = (EditText) findViewById(R.id.reason_delete);
        password = (EditText) findViewById(R.id.password);
        incorrectPasswordLayout = (LinearLayout) findViewById(R.id.wrong_password_layout);
        incorrectPassword = (TextView) findViewById(R.id.incorrect_password);

        gson = new Gson();
        mPrefs = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        String json = mPrefs.getString("UserObject", "");
        user = gson.fromJson(json, UserModel.class);

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) password.getLayoutParams();
                int px = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        64,
                        getResources().getDisplayMetrics()
                );
                params.setMargins(0, 0, 0, px); //substitute parameters for left, top, right, bottom
                password.setLayoutParams(params);
                incorrectPasswordLayout.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().length() == 0) {
                    showErrorPassword("Please provide your password");
                    return;
                } else if (deleteReason.getText().toString().length() == 0) {
                    showErrorPassword("Please give us a reason");
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteAccountActivity.this);
                View view = DeleteAccountActivity.this.getLayoutInflater().inflate(R.layout.alert_delete_account, null);
                loading = (GIFView) view.findViewById(R.id.loading);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                view.findViewById(R.id.delete_account).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new LoginUser(password.getText().toString()).execute();
                    }
                });
                view.findViewById(R.id.continue_buddy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }
                });

                dialog.show();

            }
        });

    }

    private void showErrorPassword(String text) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) password.getLayoutParams();
        params.setMargins(0, 0, 0, 0); //substitute parameters for left, top, right, bottom
        password.setLayoutParams(params);
        incorrectPassword.setText(text);
        incorrectPasswordLayout.setVisibility(View.VISIBLE);
    }

    private class LoginUser extends AsyncTask<String, String, String> {
        String password;

        LoginUser(String password) {
            this.password = password;
        }

        @Override
        protected void onPreExecute() {

            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONObject payload = new JSONObject();
                payload.put("userid", user.getUserId());
                payload.put("password", password);
                HttpClient client = new DefaultHttpClient();
                String url = BuildConfig.SERVER_URL + "api/user/login";
                HttpPost postReq = new HttpPost(url);
                postReq.setHeader("Accept", "application/json");
                postReq.setHeader("Content-type", "application/json");
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                StringEntity entity = new StringEntity(payload.toString());
                postReq.setEntity(entity);
                postReq.setHeader("x-access-token", tok_sp);
                HttpResponse httpresponse = client.execute(postReq);
                String responseText = EntityUtils.toString(httpresponse.getEntity());
                final JSONObject json = new JSONObject(responseText);
                if (json.get("status").toString().contains("success")) {
                    return "success";
                } else if (json.get("status").toString().contains("error") && (json.get("msg").toString().contains("Invalid Password"))) {
                    return "authFailed";
                } else {
                    DeleteAccountActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toast.makeText(DeleteAccountActivity.this, json.get("msg").toString(), Toast.LENGTH_SHORT).show();
                                showErrorPassword(json.get("msg").toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "fail";
        }

        @Override
        protected void onPostExecute(String result) {
            loading.setVisibility(View.GONE);
            try {
                if (result.equals("authFailed")) {
                    DeleteAccountActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DeleteAccountActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                            showErrorPassword("Invalid password");
                        }
                    });


                } else if ("success".equals(result)) {
                    new DeleteAccount().execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class DeleteAccount extends AsyncTask<String, String, String> {
        String reason;

        @Override
        protected void onPreExecute() {

            reason = deleteReason.getText().toString();

        }


        @Override
        protected String doInBackground(String... params) {
            try {
                JSONObject payload = new JSONObject();
                payload.put("userid", user.getUserId());
                payload.put("deletionReason", reason);
                HttpClient client = new DefaultHttpClient();
                String url = BuildConfig.SERVER_URL + "api/user/account/delete";
                HttpPost postReq = new HttpPost(url);
                postReq.setHeader("Accept", "application/json");
                postReq.setHeader("Content-type", "application/json");
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                StringEntity entity = new StringEntity(payload.toString());
                postReq.setEntity(entity);
                postReq.setHeader("x-access-token", tok_sp);
                HttpResponse httpresponse = client.execute(postReq);
                String responseText = EntityUtils.toString(httpresponse.getEntity());
                JSONObject json = new JSONObject(responseText);
                if (json.get("status").toString().contains("success")) {
                    if (json.get("msg").toString().contains("Successfully deleted")) {
                        return "success";
                    } else
                        return "approvedUser";
                } else {
                    if (json.get("status").toString().contains("error")) {
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

        @Override
        protected void onPostExecute(String result) {
            try {
                if (retryCount < 3) {
                    retryCount++;
                    if (result.equals("authFailed")) {
                        new FetchNewToken(DeleteAccountActivity.this).execute();
                        new DeleteAccount().execute();
                    } else if (result.equals("fail"))
                        new ContactDialog(DeleteAccountActivity.this, true).show();
                    else if ("success".equals(result)) {
                        Splash.notify = 0;
                        SharedPreferences preferences = getSharedPreferences("buddyotp", 0);
                        SharedPreferences.Editor editora = preferences.edit();
                        editora.clear();
                        editora.commit();
                        SharedPreferences preferencesb = getSharedPreferences("buddy", 0);
                        SharedPreferences.Editor editorab = preferencesb.edit();
                        editorab.clear();
                        editorab.commit();
                        SharedPreferences preferencesbc = getSharedPreferences("buddyin", 0);
                        SharedPreferences.Editor editorabc = preferencesbc.edit();
                        editorabc.clear();
                        editorabc.commit();
                        SharedPreferences preferencesbc1 = getSharedPreferences("proid", 0);
                        SharedPreferences.Editor editorabc1 = preferencesbc1.edit();
                        editorabc1.clear();
                        editorabc1.commit();
                        SharedPreferences preferencesbc2 = getSharedPreferences("cred", 0);
                        SharedPreferences.Editor editorabc2 = preferencesbc2.edit();
                        editorabc2.clear();
                        editorabc2.commit();
                        int a = 0;
                        SharedPreferences sharedpreferences = getSharedPreferences("buddy", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt("checklog", a);
                        editor.commit();
                        SharedPreferences sharedpreferences1 = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                        editor1.putString("number", "");
                        editor1.putString("code", "");
                        editor1.commit();
                        try {
                            Intercom.client().reset();
                        } catch (Exception e) {
                            System.out.println(e.toString() + "int empty");
                        }
                        Intent intform = new Intent(DeleteAccountActivity.this, MainActivity.class);
                        intform.putExtra(Constants.ACCOUNT_DELETED, true);
                        intform.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        finish();
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                    } else if ("approvedUser".equals(result)) {
                        new ContactDialog(DeleteAccountActivity.this, false).show();
                    }
                } else
                    retryCount = 0;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ContactDialog extends Dialog {
        boolean error;

        public ContactDialog(Context context, boolean error) {
            super(context, R.style.NewDialog);
            this.error = error;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.delete_contact_dialog);
            Button contactUs = (Button) findViewById(R.id.contact_us);
            Button continueBuddy = (Button) findViewById(R.id.continue_buddy);
            TextView message = (TextView) findViewById(R.id.text1);
            if (error) {
                message.setText("Error");
            }
            contactUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    try {
                        Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
                        Intercom.client().displayMessageComposer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            continueBuddy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    finish();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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
}
