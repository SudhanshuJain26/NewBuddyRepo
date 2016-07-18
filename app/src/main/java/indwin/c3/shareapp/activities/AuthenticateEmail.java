package indwin.c3.shareapp.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

//import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.Name;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import indwin.c3.shareapp.BuildConfig;
import indwin.c3.shareapp.GetAccessToken;
import indwin.c3.shareapp.GoogleConstants;
import indwin.c3.shareapp.R;
import indwin.c3.shareapp.Share;
import indwin.c3.shareapp.models.Friends;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;


public class AuthenticateEmail extends AppCompatActivity {

    ImageView google;
//    ImageView yahoo;
    final String TAG = getClass().getName();
    ProgressBar progressBar;
    boolean sentFromYahoo = false;



    private Dialog auth_dialog;
    List<Friends> friends1;
    List<Friends> yahooEmailList;

    private String oAuthVerifier;
    String mEmail;


//    CommonsHttpOAuthConsumer mainConsumer;
//    CommonsHttpOAuthProvider mainProvider;
    ImageView back;
    ProgressDialog pDialog;
    String userId;
    AccountManager mAccountManager;
    ProgressDialog pd;

    int pageCode;
    FrameLayout  rl1;
    RelativeLayout rl2;
    public ArrayList<Friends> listfromServerEmail = new ArrayList<>();
    public ArrayList<Friends> isBuddyListEmail = new ArrayList<>();
    public ArrayList<Friends> isInvitedListEmail = new ArrayList<>();
    public ArrayList<Friends> listfromServerEmailYahoo = new ArrayList<>();
    public ArrayList<Friends> isBuddyListEmailYahoo = new ArrayList<>();
    public ArrayList<Friends> isInvitedListEmailYahoo = new ArrayList<>();
    public ArrayList<String> alreadyListed = new ArrayList<>();
  ;int width,height;
    ViewGroup.LayoutParams params;
    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;



//    @Override
//    protected void onNewIntent(Intent intent) {
//
//        Uri uriData = intent.getData();
//        if (uriData != null && uriData.toString().startsWith(YahooConstants.CALLBACK_URL)) {
//            setVerifier(uriData.getQueryParameter("oauth_verifier"));
//        }
//        if(uriData!=null)
//        new OAuthGetAccessTokenTask().execute();
//        super.onNewIntent(intent);
//
//    }


    @Override
    public void onBackPressed() {

        if (pageCode == 0) {
            Intent intent = new Intent(AuthenticateEmail.this, ShowSelectedItems.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(AuthenticateEmail.this, FillEmailContacts.class);
            startActivity(intent);
            finish();
        }
    }


//    @Override
//    protected void onNewIntent(Intent intent) {
//
//        Uri uriData = intent.getData();
//        if (uriData != null && uriData.toString().startsWith(YahooConstants.CALLBACK_URL)) {
//            setVerifier(uriData.getQueryParameter("oauth_verifier"));
//        }
//        if(uriData!=null)
//        new OAuthGetAccessTokenTask().execute();
//        super.onNewIntent(intent);
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_email);
//        this.mainConsumer = new CommonsHttpOAuthConsumer(YahooConstants.CLIENT_ID, YahooConstants.CLIENT_SECRET);
//        this.mainProvider = new CommonsHttpOAuthProvider(YahooConstants.REQUEST_TOKEN_ENDPOINT_URL, YahooConstants.ACCESS_TOKEN_ENDPOINT_URL, YahooConstants.AUTHORIZE_WEBSITE_URL);

        google = (ImageView) findViewById(R.id.google);
       // yahoo = (ImageView) findViewById(R.id.yahoo);
        back = (ImageView) findViewById(R.id.backo);
        rl1 = (FrameLayout)findViewById(R.id.googleframe);

       // rl2 = (RelativeLayout)findViewById(R.id.yahooFrame);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageCode==0){
                    Intent intent = new Intent(AuthenticateEmail.this,ShowSelectedItems.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(AuthenticateEmail.this,FillEmailContacts.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        UserModel userModel = AppUtils.getUserObject(this);
        userId = userModel.getUserId();
        Log.i("TAG","userId="+userId);
        pageCode = getIntent().getIntExtra("pageCode",1);
        SharedPreferences preferences = getSharedPreferences("inviteCalls",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("email_contacts_isBuddy");
        editor.remove("email_contacts_isInvited");
        editor.remove("email_contacts_listfromServer");
        editor.apply();
        SharedPreferences preferences1 = getSharedPreferences("preferencename2",MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences1.edit();
        editor1.clear();
        editor1.apply();
        SharedPreferences preferences2 = getSharedPreferences("CHECKBOX_STATE_EMAIL",MODE_PRIVATE);
        SharedPreferences.Editor editor2 = preferences2.edit();
        editor2.clear();
        editor2.apply();
        isBuddyListEmailYahoo.clear();
        isInvitedListEmailYahoo.clear();
        listfromServerEmailYahoo.clear();
        WindowManager manager = (WindowManager) getSystemService(Activity.WINDOW_SERVICE);




            width = manager.getDefaultDisplay().getWidth();
            height = manager.getDefaultDisplay().getHeight();

        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAuthDialog();
                if(yahooEmailList!=null)
                    yahooEmailList.clear();
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAuthDialog();
                if(yahooEmailList!=null)
                yahooEmailList.clear();
            }
        });

//        yahoo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new OAuthRequestTokenTask(v.getContext(), mainConsumer, mainProvider).execute();
//                sentFromYahoo = true;
//                if(friends1!=null)
//                friends1.clear();
//            }
//        });


    }

    private void setContactList(List<Friends> contacts) {
        Intent intent = new Intent(this, FillEmailContacts.class);
        if (friends1 != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("values", (Serializable) friends1);
            intent.putExtras(bundle);
        }
        Log.i(TAG, "" + friends1.size());
        startActivity(intent);


    }

//    public void setVerifier(String verifier) {
//        this.oAuthVerifier = verifier;
////      this.webview.loadData("verifier = " + this.OAuthVerifier + "<br>", "text/html", null);
//        Log.d("setVerifier", verifier);
//
//        this.showToken();
//    }

//    public void showToken() {
//        //Log.d("SubPlurkV2", "Token = " + mainConsumer.getToken() + " and secret = " + mainConsumer.getTokenSecret());
//        String str =
//                "verifier = " + this.oAuthVerifier + "<br>" +
//                        "Token = " + mainConsumer.getToken() + "<br>" +
//                        "secret = " + mainConsumer.getTokenSecret() + "<br>" +
//                        "oauth_expires_in = " + mainProvider.getResponseParameters().getFirst("oauth_expires_in") + "<br>" +
//                        "oauth_session_handle = " + mainProvider.getResponseParameters().getFirst("oauth_session_handle") + "<br>" +
//                        "oauth_authorization_expires_in = " + mainProvider.getResponseParameters().getFirst("oauth_authorization_expires_in") + "<br>" +
//                        "xoauth_yahoo_guid = " + mainProvider.getResponseParameters().getFirst("xoauth_yahoo_guid") + "<br>";
//        Log.i("YahooScreen", "str : " + str);
//    }

//    public void getContacts() {
//        String guid = mainProvider.getResponseParameters().getFirst("xoauth_yahoo_guid");
//        String url = "https://social.yahooapis.com/v1/user/" + guid + "/contacts?format=json";
//        new GetYahooContacts().execute(url);
//        SharedPreferences preferences1 = getSharedPreferences("disconnect",MODE_PRIVATE);
//        SharedPreferences.Editor editor1 = preferences1.edit();
//        editor1.putBoolean("disconnectemail",false);
//        editor1.commit();
//
//    }

//    private class GetYahooContacts extends AsyncTask<String, Void, Void> {
//        OAuthConsumer consumer = mainConsumer;
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            new SendEmails(AuthenticateEmail.this).execute();
////            Intent intent = new Intent(AuthenticateEmail.this, FillEmailContacts.class);
//            SharedPreferences preferences = getSharedPreferences("inviteCalls",MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            Gson gson = new Gson();
//            String json = gson.toJson(yahooEmailList);
//            editor.putString("yahooEmailList", json);
//            editor.commit();
//
//            super.onPostExecute(aVoid);
//        }
//
//        @Override
//        protected Void doInBackground(String... params) {
//
//            final HttpGet request = new HttpGet(params[0]);
//            Log.i("doGet", "Requesting URL : " + params[0]);
//
//            try {
//                consumer.sign(request);
//                Log.i("YahooScreen", "request url : " + request.getURI());
//
//                DefaultHttpClient httpclient = new DefaultHttpClient();
//                HttpResponse response;
//                try {
//                    response = httpclient.execute((HttpUriRequest) request);
//                    Log.i("doGet", "Statusline : " + response.getStatusLine());
//                    InputStream data = response.getEntity().getContent();
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(data));
//                    String responeLine;
//                    StringBuilder responseBuilder = new StringBuilder();
//                    while ((responeLine = bufferedReader.readLine()) != null) {
//                        responseBuilder.append(responeLine);
//                    }
//                    try {
//                        JSONObject jsonObject = new JSONObject(responseBuilder.toString());
//                        JSONObject jsonObject1 = jsonObject.getJSONObject("contacts");
//                        JSONArray jsonObject2 = jsonObject1.getJSONArray("contact");
//                        yahooEmailList = new ArrayList<Friends>();
//                        for (int i = 0; i < jsonObject2.length(); i++) {
//
//                            JSONObject object = jsonObject2.getJSONObject(i);
//                            JSONArray fields = object.getJSONArray("fields");
//                            JSONObject name = fields.getJSONObject(0);
//                            JSONObject email = fields.getJSONObject(2);
//                            String email_registered = email.getString("value");
//
//                            JSONObject json = name.getJSONObject("value");
//                            String givenName = json.getString("givenName");
//                            String familyName = json.getString("familyName");
//                            Friends friends = new Friends(givenName + " " + familyName, email_registered);
//                            yahooEmailList.add(friends);
//                        }
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    Log.i("doGet", "Response : " + responseBuilder.toString());
//
//                    //return responseBuilder.toString();
//                } catch (ClientProtocolException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            } catch (OAuthExpectationFailedException e) {
//                e.printStackTrace();
//            } catch (OAuthMessageSignerException e) {
//                e.printStackTrace();
//            } catch (OAuthCommunicationException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }

    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    private void launchAuthDialog() {
        final Context context = this;
        sentFromYahoo = false;
        auth_dialog = new Dialog(context);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(auth_dialog.getWindow().getAttributes());
        lp.width = width;
        lp.height = height;
        auth_dialog.getWindow().setAttributes(lp);
        auth_dialog.setTitle("Authentication");
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
                    Log.i("", "ACCESS_DENIED_HERE");
                    authComplete = true;
                    auth_dialog.dismiss();
                }
            }
        });
        auth_dialog.show();
    }


//    public class OAuthGetAccessTokenTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            try {
//                mainProvider.retrieveAccessToken(mainConsumer, oAuthVerifier);
//            } catch (OAuthMessageSignerException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (OAuthNotAuthorizedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (OAuthExpectationFailedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (OAuthCommunicationException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        /* (non-Javadoc)
//         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
////         */
//        @Override
//        protected void onPostExecute(Void result) {
//            // TODO Auto-generated method stub
//            //super.onPostExecute(result);
//            showToken();
//            getContacts();
//
//        }
//
//    }

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
                    new GetGmailAddress().execute(tok);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String readResponse(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = new byte[2048];
        int len = 0;
        while ((len = is.read(data, 0, data.length)) >= 0) {
            bos.write(data, 0, len);
        }
        return new String(bos.toByteArray(), "UTF-8");
    }
    private class GetGmailAddress extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            String accessToken = params[0];
            String url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token="+accessToken;
            try
            {
                URL url1 = new URL(url);
                HttpURLConnection connection = (HttpURLConnection)url1.openConnection();
                int sc =connection.getResponseCode();

                InputStream is = connection.getInputStream();
                String data = readResponse(is);
                JSONObject jsonObject = new JSONObject(data);
                String email = jsonObject.getString("email");
                SharedPreferences prefs = getSharedPreferences("cred",MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("user_loggedin_email",email);
                editor.apply();

                Log.i(TAG,data);
            }
            catch(Exception ex)
            {
                Log.i(TAG,ex.getMessage());
            }



            return null;
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

            contactsService.setHeader("Authorization", "Bearer " + accessToken);
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
            SharedPreferences preferences2 = getSharedPreferences("disconnect",MODE_PRIVATE);
            SharedPreferences.Editor editor2 = preferences2.edit();
            editor2.putBoolean("disconnectemail",false);
            editor2.commit();

            if (null != googleContacts && googleContacts.size() > 0) {
                friends1 = new ArrayList<Friends>();

                for (ContactEntry contactEntry : googleContacts) {
                    String name = "";
                    String email = "";

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
                                                .getYomi() + ")";
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
                    if (friends.getEmail()!=null && friends.getEmail().length()!=0) {
                        if(friends.getName().length()==0){
                            friends.setName(friends.getEmail());
                        }

                        friends1.add(friends);
                    }
                }

//                new SendEmailstoServer(AuthenticateEmail.this).execute();
                new SendEmails(AuthenticateEmail.this).execute();
                SharedPreferences prefs = getSharedPreferences("inviteCalls", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(friends1);

                editor.putString("email_contacts", json);
                editor.commit();

                SharedPreferences prefs1 = getSharedPreferences("inviteCalls", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = prefs1.edit();
                editor1.putBoolean("email_read",true);
                editor1.apply();




                //setContactList(friends1);

            } else {
                Log.e(TAG, "No Contact Found");
                Toast.makeText(context, "No Contact Found", Toast.LENGTH_SHORT)
                        .show();
            }
            pDialog.dismiss();
        }

    }

    public class SendEmails extends AsyncTask<Void, Void, String> {
        Context context;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Fetching your contacts...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);

            pDialog.show();
        }

        public SendEmails(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("win")) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                new GetInvitedData(context).execute();
            }else if(s==null || s.equals("fail")){
                Intent intent = new Intent(AuthenticateEmail.this,FillEmailContacts.class);
                intent.putExtra("message","errorPage");
                startActivity(intent);
            }

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONObject jsonObject = new JSONObject();
            String url = BuildConfig.SERVER_URL + "api/v1/user/contacts";
            SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
            String tok_sp = toks.getString("token_value", "");
            try {
                jsonObject.put("userid", userId);
                JSONArray contacts = new JSONArray();
                if(!sentFromYahoo) {
                    for (int i = 0; i < friends1.size(); i++) {
                        JSONObject json = new JSONObject();
                        json.put("name", friends1.get(i).getName());
                        String phone = friends1.get(i).getPhone_Num();
                        if (phone == null || phone.length() == 0) {
                            phone = "";
                        }

                        json.put("phone", phone);
                        String email = friends1.get(i).getEmail();
                        if (email == null || email.length() == 0) {
                            email = "";
                        }
                        json.put("email", email);
                        contacts.put(json);
                    }
                    jsonObject.put("contacts", contacts);
                }else {
                    if (yahooEmailList != null) {
                        for (int i = 0; i < yahooEmailList.size(); i++) {
                            JSONObject json = new JSONObject();
                            json.put("name", yahooEmailList.get(i).getName());
                            String phone = yahooEmailList.get(i).getPhone_Num();
                            if (phone == null || phone.length() == 0) {
                                phone = "";
                            }

                            json.put("phone", phone);
                            String email = yahooEmailList.get(i).getEmail();
                            if (email == null || email.length() == 0) {
                                email = "";
                            }
                            json.put("email", email);
                            contacts.put(json);
                        }

                        jsonObject.put("contacts", contacts);
                    }else{

                    }
                }

                HttpResponse response = AppUtils.connectToServerPost(url, jsonObject.toString(), tok_sp);
                if (response != null) {
                    HttpEntity ent = response.getEntity();
                    String responseString = EntityUtils.toString(ent, "UTF-8");
                    if (response.getStatusLine().getStatusCode() != 200) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {
                        JSONObject resp = new JSONObject(responseString);

                        if (!resp.getString("status").contains("success")) {

                            Log.e("MeshCommunication", "Server returned code "
                                    + response.getStatusLine().getStatusCode());
                            return resp.getString("msg");
                        } else {

                            return "win";

                        }

                    }
                } else return "fail";

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Exception", e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class GetInvitedData extends AsyncTask<Void, Void, String> {

        String url = BuildConfig.SERVER_URL + "api/v1/user/contacts?userid=" + userId;
        Context context;

        public GetInvitedData(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                HttpResponse response = AppUtils.connectToServerGet(url, tok_sp, null);
                if (response != null) {
                    HttpEntity ent = response.getEntity();
                    String responseString = EntityUtils.toString(ent, "UTF-8");
                    if (response.getStatusLine().getStatusCode() != 200) {


                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {
                        JSONObject jsonObject = new JSONObject(responseString);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Boolean isInvited;
                            String phone;
                            String email;
                            JSONObject json = jsonArray.getJSONObject(i);
                            String name = json.getString("name");
                            try {
                                phone = json.getString("contactPhone");
                            }catch (JSONException e){
                                phone ="";
                            }
                            try{
                                email = json.getString("contactEmail");
                            }catch (JSONException e){
                                email = "";
                            }

                            Boolean isBuddy = json.getBoolean("isBuddy");
                            if(!isBuddy){
                                isInvited = json.getBoolean("isInvited");
                            }
                            else
                                isInvited = false;

                            Friends friends = new Friends(phone, email, isBuddy, isInvited, name);
                            if(email.length()!=0) {
                                if(sentFromYahoo) {
                                    if (isBuddy)
                                        isBuddyListEmailYahoo.add(friends);
                                    if (!isBuddy && isInvited)
                                        isInvitedListEmailYahoo.add(friends);
                                }else{
                                    if (isBuddy)
                                        isBuddyListEmail.add(friends);
                                    if (!isBuddy && isInvited)
                                        isInvitedListEmail.add(friends);
                                }

                            }
                        }


                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= new ProgressDialog(context);
            pd.setMessage("Finding your friends at Buddy...");
            pd.setIndeterminate(false);
            pd.setCancelable(true);
            pd.show();

            isBuddyListEmailYahoo.clear();
            isInvitedListEmailYahoo.clear();
            listfromServerEmailYahoo.clear();
            isBuddyListEmail.clear();
            isInvitedListEmail.clear();
            listfromServerEmail.clear();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            if(!sentFromYahoo) {

                for (int i = 0; i < isBuddyListEmail.size(); i++) {
                    String name = isBuddyListEmail.get(i).getName();
                    alreadyListed.add(name);
                }
                for (int i = 0; i < isInvitedListEmail.size(); i++) {
                    String name = isInvitedListEmail.get(i).getName();
                    alreadyListed.add(name);
                }
                for (int i = 0; i < friends1.size(); i++) {
                    String name = friends1.get(i).getName();
                    if (!alreadyListed.contains(name)) {
                        listfromServerEmail.add(friends1.get(i));
                    }
                }

                Collections.sort(isBuddyListEmail, new Comparator<Friends>() {
                    @Override
                    public int compare(Friends s1, Friends s2) {
                        return s1.getName().compareToIgnoreCase(s2.getName());
                    }
                });

                Collections.sort(isInvitedListEmail, new Comparator<Friends>() {
                    @Override
                    public int compare(Friends s1, Friends s2) {
                        return s1.getName().compareToIgnoreCase(s2.getName());
                    }
                });

                Collections.sort(listfromServerEmail, new Comparator<Friends>() {
                    @Override
                    public int compare(Friends s1, Friends s2) {
                        return s1.getName().compareToIgnoreCase(s2.getName());
                    }
                });
            }else{
                for (int i = 0; i < isBuddyListEmailYahoo.size(); i++) {
                    String name = isBuddyListEmailYahoo.get(i).getName();
                    alreadyListed.add(name);}
                for (int i = 0; i < isInvitedListEmailYahoo.size(); i++) {
                    String name = isInvitedListEmailYahoo.get(i).getName();
                    alreadyListed.add(name);
                }
                if(yahooEmailList!=null) {
                    for (int i = 0; i < yahooEmailList.size(); i++) {
                        String name = yahooEmailList.get(i).getName();
                        if (!alreadyListed.contains(name)) {
                            listfromServerEmailYahoo.add(yahooEmailList.get(i));
                        }
                    }
                }else{

                }

                Collections.sort(isBuddyListEmailYahoo, new Comparator<Friends>() {
                    @Override
                    public int compare(Friends s1, Friends s2) {
                        return s1.getName().compareToIgnoreCase(s2.getName());
                    }
                });

                Collections.sort(isInvitedListEmailYahoo, new Comparator<Friends>() {
                    @Override
                    public int compare(Friends s1, Friends s2) {
                        return s1.getName().compareToIgnoreCase(s2.getName());
                    }
                });

                Collections.sort(listfromServerEmailYahoo, new Comparator<Friends>() {
                    @Override
                    public int compare(Friends s1, Friends s2) {
                        return s1.getName().compareToIgnoreCase(s2.getName());
                    }
                });


            }

            Intent intent = new Intent(AuthenticateEmail.this,FillEmailContacts.class);
            SharedPreferences preferences = getSharedPreferences("inviteCalls",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("DisconnectEmail",false);
            editor.commit();



            editor.commit();
            sentFromYahoo = false;
            if (friends1 != null && friends1.size()!=0) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("invitedList",isInvitedListEmail);
                bundle.putSerializable("buddyList",isBuddyListEmail);
                bundle.putSerializable("userList",listfromServerEmail);
                intent.putExtras(bundle);
                SharedPreferences preferences1 = getSharedPreferences("list2",MODE_PRIVATE);
                SharedPreferences.Editor editor1 = preferences1.edit();
                editor1.putInt("inviteSize",isInvitedListEmail.size());
                editor1.apply();

                saveListinPrefs();
            }
            if(yahooEmailList!=null && yahooEmailList.size()!=0){
                Bundle bundle = new Bundle();
                bundle.putSerializable("invitedList",isInvitedListEmailYahoo);
                bundle.putSerializable("buddyList",isBuddyListEmailYahoo);
                bundle.putSerializable("userList",listfromServerEmailYahoo);
                intent.putExtras(bundle);
                SharedPreferences preferences2 = getSharedPreferences("list2",MODE_PRIVATE);
                SharedPreferences.Editor editor2 = preferences2.edit();
                editor2.putInt("inviteSize",isInvitedListEmailYahoo.size());
                editor2.apply();
            }

            SharedPreferences sharedPreferences = getSharedPreferences("authenticate",MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences.edit();
            editor1.putBoolean("authenticatedone",true);
            editor1.commit();
            context.startActivity(intent);

        }

        public void saveListinPrefs(){
            SharedPreferences preferences = getSharedPreferences("list1", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(listfromServerEmail);
            editor.putString("email_contacts_notSelected", json);
            editor.commit();

            SharedPreferences preferences1 = getSharedPreferences("list1", MODE_PRIVATE);
            SharedPreferences.Editor editor1 = preferences1.edit();
            Gson gson1 = new Gson();
            String json1 = gson1.toJson(isInvitedListEmail);
            editor1.putString("email_contacts_invited", json1);
            editor1.commit();

            SharedPreferences preferences2 = getSharedPreferences("list1", MODE_PRIVATE);
            SharedPreferences.Editor editor2 = preferences2.edit();
            Gson gson2 = new Gson();
            String json2 = gson2.toJson(isBuddyListEmail);
            editor2.putString("email_contacts_buddy", json2);
            editor2.commit();

        }
    }


//        public class OAuthRequestTokenTask extends AsyncTask<Void, Void, String> {
//
//            final String TAG = getClass().getName();
//            private Context context;
//            private OAuthProvider provider;
//            private OAuthConsumer consumer;
//
//            public OAuthRequestTokenTask(Context context, OAuthConsumer consumer, CommonsHttpOAuthProvider provider) {
//                this.context = context;
//                this.consumer = consumer;
//                this.provider = provider;
//            }
//
//            @Override
//            protected String doInBackground(Void... params) {
//
//                try {
//                    Log.i(TAG, "Retrieving request token from Google servers");
//                    final String url = provider.retrieveRequestToken(consumer, YahooConstants.CALLBACK_URL);
//                    Log.i(TAG, "Popping a browser with the authorize URL : " + url);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    context.startActivity(intent);
//
//                    return url;
//                } catch (Exception e) {
//                    Log.e(TAG, "Error during OAUth retrieve request token", e);
//                }
//
//                return null;
//            }
//
//            /* (non-Javadoc)
//             * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
//             */
//            @Override
//            protected void onPostExecute(String result) {
//                Log.i(TAG, "onPostExecute result : " + result);
//                super.onPostExecute(result);
//
//            }
//        }
    }

