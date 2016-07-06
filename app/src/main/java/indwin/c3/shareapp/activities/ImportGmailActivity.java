package indwin.c3.shareapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.Name;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import indwin.c3.shareapp.GetAccessToken;
import indwin.c3.shareapp.GoogleConstants;
import indwin.c3.shareapp.R;
import indwin.c3.shareapp.models.Friends;

public class ImportGmailActivity extends AppCompatActivity {

    final String TAG = getClass().getName();

    private Dialog auth_dialog;
    public ArrayList<Friends> friendsArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_gmail);
        launchAuthDialog();
    }

    private void launchAuthDialog() {
        final Context context = this;
        auth_dialog = new Dialog(context);
        auth_dialog.setTitle("Auth dialog box");
        auth_dialog.setCancelable(true);
        auth_dialog.setContentView(R.layout.auth_dialog);

        auth_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });

        WebView web = (WebView) auth_dialog.findViewById(R.id.web);
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(GoogleConstants.OAUTH_URL + "?redirect_uri="
                + GoogleConstants.REDIRECT_URI
                + "&response_type=code&client_id=" + GoogleConstants.CLIENT_ID
                + "&scope=" + GoogleConstants.OAUTH_SCOPE_CONTACTS+ " "+GoogleConstants.OAUTH_SCOPE_PROFILE);
        web.setWebViewClient(new WebViewClient() {
            boolean authComplete = false;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("?code=") && authComplete != true) {
                    Uri uri = Uri.parse(url);
                    String authCode = uri.getQueryParameter("code");
                    authComplete = true;
                    auth_dialog.dismiss();
                    new GoogleAuthToken(context).execute(authCode);
                } else if (url.contains("error=access_denied")) {
                    Log.i("","ACCESS_DENIED_HERE");
                    authComplete = true;
                    auth_dialog.dismiss();
                }
            }
        });
        auth_dialog.show();
    }

    private class GoogleAuthToken extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        private Context context;

        public GoogleAuthToken(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Contacting Google ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            String authCode = args[0];
            GetAccessToken jParser = new GetAccessToken();
            JSONObject json = jParser.gettoken(GoogleConstants.TOKEN_URL,
                    authCode, GoogleConstants.CLIENT_ID,
                    GoogleConstants.CLIENT_SECRET,
                    GoogleConstants.REDIRECT_URI, GoogleConstants.GRANT_TYPE);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            if (json != null) {
                try {
                    String tok = json.getString("access_token");
                    String expire = json.getString("expires_in");
                    String refresh = json.getString("refresh_token");
                    new GetGoogleContacts(context).execute(tok);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class GetGoogleContacts extends
            AsyncTask<String, String, List<ContactEntry>> {

        private ProgressDialog pDialog;
        private Context context;

        public GetGoogleContacts(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Authenticated. Getting Google Contacts ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            pDialog.show();
        }

        @Override
        protected List<ContactEntry> doInBackground(String... args) {
            String accessToken = args[0];
            ContactsService contactsService = new ContactsService(
                    GoogleConstants.APP);
            contactsService.setHeader("Authorization","Bearer " + accessToken);
            contactsService.setHeader("GData-Version", "3.0");
            List<ContactEntry> contactEntries = null;
            try {
                URL feedUrl = new URL(GoogleConstants.CONTACTS_URL);
                ContactFeed resultFeed = contactsService.getFeed(feedUrl,
                        ContactFeed.class);
                contactEntries = resultFeed.getEntries();
            } catch (Exception e) {
                pDialog.dismiss();
                Toast.makeText(context, "Failed to get Contacts",
                        Toast.LENGTH_SHORT).show();
            }
            return contactEntries;
        }

        @Override
        protected void onPostExecute(List<ContactEntry> googleContacts) {
            if (null != googleContacts && googleContacts.size() > 0) {
                 friendsArrayList = new ArrayList<>();

                for (ContactEntry contactEntry : googleContacts) {
                    String name = "";
                    String email ="";

                    if (contactEntry.hasName()) {
                        Name tmpName = contactEntry.getName();
                        if (tmpName.hasFullName()) {
                            name = tmpName.getFullName().getValue();
                        } else {
                            if (tmpName.hasGivenName()) {
                                name = tmpName.getGivenName().getValue();
                                if (tmpName.getGivenName().hasYomi()) {
                                    name += " ("
                                            + tmpName.getGivenName().getYomi()
                                            + ")";
                                }
                                if (tmpName.hasFamilyName()) {
                                    name += tmpName.getFamilyName().getValue();
                                    if (tmpName.getFamilyName().hasYomi()) {
                                        name += " ("
                                                + tmpName.getFamilyName()
                                                .getYomi() +")";
                                    }
                                }
                            }
                        }
                    }
                    List<Email> emails = contactEntry.getEmailAddresses();
                    if (null != emails && emails.size() > 0) {
                        Email tempEmail = (Email) emails.get(0);
                        email = tempEmail.getAddress();
                    }
                    Friends friends = new Friends(name, email);
                    friends.setType("email");


                    friendsArrayList.add(friends);
                }


            } else {
                Log.e(TAG, "No Contact Found");
                Toast.makeText(context, "No Contact Found", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.dismiss();
            Intent intent = new Intent(ImportGmailActivity.this,FillEmailContacts.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("contacts",(Serializable) friendsArrayList);

            startActivity(intent);
            finish();
        }

    }
}
