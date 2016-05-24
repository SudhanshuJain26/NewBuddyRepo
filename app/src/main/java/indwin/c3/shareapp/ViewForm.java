package indwin.c3.shareapp;

import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;

import android.webkit.JavascriptInterface;
import android.webkit.JsResult;


import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import java.io.File;


import java.net.URLEncoder;
import java.util.Calendar;

import im.delight.android.webview.AdvancedWebView;

import indwin.c3.shareapp.activities.ProfileActivity;
import io.intercom.android.sdk.Intercom;

public class ViewForm extends AppCompatActivity implements AdvancedWebView.Listener {
    private AdvancedWebView form;
    String userid = "", token = "", url = "";
    private Intent intform;
    private String res;
    private Uri mCapturedImageURI;
    private int FILECHOOSER_RESULTCODE = 1;

    private ValueCallback<Uri> mUploadMessage;
    private ProgressBar spinner;
    SharedPreferences userP;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private String reviewUrl="";
    private String seller,prodid;
    int which_page=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userP = getSharedPreferences("token", Context.MODE_PRIVATE);

        try{
            which_page=getIntent().getExtras().getInt("which_page");}
        catch (Exception e)
        {}
        try{
            reviewUrl=getIntent().getExtras().getString("reviewUrl");
        }
        catch (Exception e)
        {}
        setContentView(R.layout.activity_view_form);
        setTheme(R.style.DrawerArrowStyle2);https://github.com/aniket29081992/buddyapp
        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);



            setSupportActionBar(toolbar);
//getSupportActionBar().setBackgroundDrawable(R.id);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
        catch(Exception e){System.out.println(e.toString()+"digo");}
        TextView test=(TextView)findViewById(R.id.texthead);
        ImageView inter=(ImageView)findViewById(R.id.interCom);
        inter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intercom.client().displayMessageComposer();
            }
        });
        if((which_page==3))
            test.setText("About Us");
        else
        if((which_page==3))
            test.setText("About Us");
else
        if((which_page==5))
            test.setText("FAQs");
        else
        if((which_page==4)||(which_page==6)||(which_page==7)||(which_page==8))
            test.setText("Profile Details");
        else
        if(which_page==9)
            test.setText("Products");
        else
        if(which_page==119)
            test.setText("Products");
        else
        if(which_page==10)
            test.setText("Products");
        else
        if(which_page==11)
            test.setText("How it works");
        else
        if(which_page==16)
            test.setText("Orders");
        else
        if(which_page==17)
            test.setText("Repayments");
        else
        if(which_page==14)
        test.setText("Why use Buddy");
        else
        if(which_page==15)
            test.setText("Safety and Security");

        else
        if(which_page==999)
            test.setText("Recharge Paytm Wallet");

        spinner=(ProgressBar)findViewById(R.id.progressBar1);

        SharedPreferences ss = getSharedPreferences("cred", Context.MODE_PRIVATE);
        userid=ss.getString("phone_number", "");
        token=userP.getString("tok", null);
        SharedPreferences.Editor editornew = userP.edit();
        editornew.putInt("back", 1);
        editornew.commit();
//         url=getIntent().getExtras().getString("url");

        try
        {res=getIntent().getExtras().getString("url");}
        catch (Exception e){}
        try
        {seller=getIntent().getExtras().getString("ecom");}
        catch (Exception e){}
        try
        {prodid=getIntent().getExtras().getString("prodid");}
        catch (Exception e){}
        form=(AdvancedWebView)findViewById(R.id.webView);
        form.setListener(this, this);
        this.form.getSettings().setUserAgentString(this.form.getSettings().getUserAgentString()

                      +" "+R.string.buddyagent);
        int t=R.string.buddyagent;

//String userA=R.string.buddyagent;
//        form.addJavascriptInterface(new WebAppInterface(this),Andr);
//        settings.setDomStorageEnabled(true);


//        settings.s
// TODO: 3/21/2016 check this and then remove comment
        Long time= Calendar.getInstance().getTimeInMillis()/1000;
        Long oldtime=userP.getLong("expires",0);
//        Toast.makeText(FacebookAuth.this, String.valueOf(oldtime), Toast.LENGTH_SHORT).show();
        if(time+5<userP.getLong("expires",0))
            new checkAuth().execute(url);
        else
     //   new checkAuth().execute(url);
new AuthTokc().execute("cc");
      //  new indwin.c3.shareapp.AuthTok(getApplicationContext()).execute();
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if((which_page==4)||(which_page==6)||(which_page==7))
            navigationView.getMenu().getItem(1).setChecked(true);
        else
        if(which_page==3)
            navigationView.getMenu().getItem(4).setChecked(true);
        else
        if(which_page==5)
            navigationView.getMenu().getItem(6).setChecked(true);
else
        if(which_page==11)
            navigationView.getMenu().getItem(5).setChecked(true);
else
        if(which_page==14)
            navigationView.getMenu().getItem(7).setChecked(true);
        else
        if(which_page==15)
            navigationView.getMenu().getItem(7).setChecked(true);
        else
        if(which_page==999)
            navigationView.getMenu().getItem(8).setChecked(true);
        else if(which_page==16)
            navigationView.getMenu().getItem(2).setChecked(true);
       else if(which_page==17)
            navigationView.getMenu().getItem(3).setChecked(true);


//        GridView gridviewshow=(GridView)findViewById(R.id.grid1a);
//       gridviewshow.setAdapter(new ImageAdapter(this));


        //Setting Navigation View Item Selected Listener
        // to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {

                    case R.id.MyAccount:
                        Intent home=new Intent(ViewForm.this,Landing.class);
                        finish();
                        startActivity(home);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.work:


                        intform=new Intent(ViewForm.this, ViewForm.class);

                        finish();
                        intform.putExtra("which_page", 11);
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.About:
                        intform = new Intent(ViewForm.this, ViewForm.class);
                        finish();
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        intform.putExtra("which_page", 3);
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.app_form:
                        Intent intent = new Intent(ViewForm.this, ProfileActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.security:
                        //                        if(!fbid.equals("empty"))
                        intform = new Intent(ViewForm.this, ViewForm.class);
                        finish();
                        intform.putExtra("which_page", 15);
                        intform.putExtra("url", "http://hellobuddy.in/#/faqs");

                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.faq:
                        intform = new Intent(ViewForm.this, ViewForm.class);
                        finish();
                        intform.putExtra("url", "http://hellobuddy.in/#/faqs");
                        intform.putExtra("which_page", 5);
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Orders:

                        intform = new Intent(ViewForm.this, ViewForm.class);
                        finish();
                        intform.putExtra("which_page", 16);
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");

                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Repayments:

                        intform = new Intent(ViewForm.this, ViewForm.class);
                        finish();
                        intform.putExtra("which_page", 17);
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");

                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Share:


                        SharedPreferences sh_otp=getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editornew = sh_otp.edit();
//                        editor.putInt("checklog",a);
                        editornew.putInt("chshare", 1);
                        //editornew.putString("rcode", getIntent().getExtras().getString("UniC"));
                        editornew.commit();
                        Intent in=new Intent(ViewForm.this,Share.class);
in.putExtra("checksharefromweb",1);

                        startActivity(in);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Recharge:


                        intform = new Intent(ViewForm.this, ViewForm.class);
                        Splash.checkNot = 1;

//                        clickpaste();

                        intform.putExtra("which_page", 999);
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.log:
                        try {


                            Intercom.client().reset();
                        } catch (Exception e) {
                        }
                        Splash.checklog = 1;
                        Splash.notify = 0;

                        Intent stop = new Intent("CLOSE_ALL");
                        ViewForm.this.sendBroadcast(stop);
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
                        intform = new Intent(ViewForm.this, MainActivity.class);
                        finish();
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    default:
                        return true;
                }
                //Check to see which item was being clicked and perform appropriate action

            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        //   ImageView arrowclose=(ImageView)findViewById(R.id.arrow);


        NavigationView nview=(NavigationView) drawerLayout.findViewById(R.id.navigation_view);
        View headerView=nview.getHeaderView(0);
        ImageView arr=(ImageView)headerView.findViewById(R.id.arrow);

        RelativeLayout header = (RelativeLayout) headerView.findViewById(R.id.head);
        //RelativeLayout header = (RelativeLayout) headerView.findViewById(R.id.head);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        //dddddd

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){


            @Override
            public void onDrawerClosed(View drawerView) {

                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionb
        // arToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onBackPressed() {

        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (form.canGoBack())
                form.goBack();
            else {
                if (!isNetworkAvailable()) {
                    Intent in = new Intent(ViewForm.this, Landing.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                    finish();
                    return;
                }
                Intent in = new Intent(ViewForm.this, HomePage.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void cc2() {
        new checkAuth().execute();
    }

    private class checkAuth extends
                            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... data) {
            JSONObject payload = new JSONObject();
            try {
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);
                HttpClient client = new DefaultHttpClient(httpParameters);
                String url2 = getApplicationContext().getString(R.string.server) + "api/user/authtoken?userid=" + userid;
                HttpGet httppost = new HttpGet(url2);
                try {

                } catch (Exception e) {
                    System.out.println("dio " + e.toString());
                }
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                httppost.setHeader("x-access-token", tok_sp);
                httppost.setHeader("Content-Type", "application/json");
                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");

                if (response.getStatusLine().getStatusCode() != 200) {

                    Log.e("MeshCommunication", "Server returned code "
                            + response.getStatusLine().getStatusCode());
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);

                    if (resp.getString("status").contains("error")) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return resp.getString("status");
                    } else {

                        return resp.getString("authToken");

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "error";

            }
        }

        protected void onPostExecute(String result) {


            if(!result.equals("fail"))
            {//url="http://www.convert-jpg-to-pdf.net/";
                switch (which_page)
                {
                    case 1:
                        url=getApplicationContext().getString(R.string.web)+"/?m=no&userid="+userid+"&key="+result;
                        break;
                    case 2:
                        url=getApplicationContext().getString(R.string.web)+"/?m=no&userid="+userid+"&key="+result;
                        break;
                    case 3:
                        url=getApplicationContext().getString(R.string.web)+"/about-us?m=no&userid="+userid+"&key="+result;
                        break;
                    case 4:
//                        url="http://abhimanyutak.github.io/buddyapp/?m=no&userid="+userid+"&key="+result+"#/profile";
                        url=getApplicationContext().getString(R.string.web)+"/profile?m=no&userid="+userid+"&key="+result;
//                        url="http://hellobuddy.in/#/profile";
                        break;
                    case 5:
                        url=getApplicationContext().getString(R.string.web)+"/faqs?m=no&userid="+userid+"&key="+result;
                        break;
                    case 6:
//                        url="http://hellobuddy.in/#/profile";
                        url=getApplicationContext().getString(R.string.web)+"/profile?m=no&userid="+userid+"&key="+result;;
//                        url="http://abhimanyutak.github.io/buddyapp/?m=no&userid="+userid+"&key="+result+"#/profile";
                        break;

                    case 7:
//                        url="http://abhimanyutak.github.io/buddyapp/?m=no&userid="+userid+"&key="+result+"#/profile";
                        url=getApplicationContext().getString(R.string.web)+"/profile?m=no&userid="+userid+"&key="+result;
//                        url="http://hellobuddy.in/#/profile";
                        break;

                    case 8:
//                        url="http://abhimanyutak.github.io/buddyapp/?m=no&userid="+userid+"&key="+result+"#/profile";
                        url=getApplicationContext().getString(R.string.web)+"/profile?m=no&userid="+userid+"&key="+result;
//                        url="http://hellobuddy.in/#/profile";
                        break;
                    case 9:
                        try {
                            url = getApplicationContext().getString(R.string.web)+"/search/products/?flipkartURL=" + URLEncoder.encode(res, "UTF-8") + "&m=no&userid=" + userid + "&key=" + result;
                        }
                        catch (Exception e){}
                            break;
                    case 10:
                        url=getApplicationContext().getString(R.string.web)+"/search/products/"+prodid+"?seller="+seller+"&m=no&userid=" + userid + "&key=" + result;
                                                        break;
                    case 11: url=getApplicationContext().getString(R.string.web)+"/how-it-works?m=no&userid="+userid+"&key="+result;
                        break;
                    case 14:
                        url=getApplicationContext().getString(R.string.web)+"/whyBuddy?m=no&userid="+userid+"&key="+result;
                        break;
                    case 15:
                        url=getApplicationContext().getString(R.string.web)+"/security" +
                                "?m=no&userid="+userid+"&key="+result;
                        break;
                    case 99:
                        url=getApplicationContext().getString(R.string.web)+"/security" +
                                "?m=no&userid="+userid+"&key="+result;
                        break;

                    case 16:
                        url=getApplicationContext().getString(R.string.web)+"/orders" +
                                "?m=no&userid="+userid+"&key="+result;
                        break;
                    case 17:
                        url=getApplicationContext().getString(R.string.web)+"/repayments" +
                                "?m=no&userid="+userid+"&key="+result;
                        break;
                    case 119:
                        try {
                            url = reviewUrl;
                        }
                        catch (Exception e){}
                        break;

                }
                int apiversion= Build.VERSION.SDK_INT;
                System.out.print("aT123" + result);
                if((((apiversion==19)||(apiversion==20))&&((which_page==8)))||((which_page==40)))
                {

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    finish();
                    startActivity(i);

                }
                else
                {
                    form.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

                    form.getSettings().setLoadsImagesAutomatically(true);
                   form.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    form.getSettings().setAppCacheEnabled(false);
                  form.clearCache(true);


//

form.setWebViewClient(new myWebClient());


                    form.loadUrl(url);


                    form.setWebChromeClient(new WebChromeClient() {
                        public boolean onConsoleMessage(ConsoleMessage cm) {

                            Log.d("MyApplication", cm.message() + " -- From line "
                                    + cm.lineNumber() + " of "
                                    + cm.sourceId());
                            return true;
                        }
                    });
                }
            }

        }
    }

    public class AuthTokc extends
                          AsyncTask<String, Void, String> {

        private String apiN = "";


        @Override
        protected String doInBackground(String... params) {
            JSONObject payload = new JSONObject();
            String urldisplay = params[0];

            apiN = urldisplay;
            try {

                // userid=12&productid=23&action=add
                // TYPE: POST
                //      payload.put("userid", details.get("userid"));
                // payload.put("productid", details.get("productid"));
                // payload.put("action", details.get("action"));
                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                String urll = getApplicationContext().getString(R.string.server) + "authenticate";
                HttpPost httppost = new HttpPost(urll);
                httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");

                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");
                if (response.getStatusLine().getStatusCode() != 200) {
                    Log.e("MeshCommunication", "Server returned code "
                            + response.getStatusLine().getStatusCode());
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);
                    if (resp.getString("status").contains("fail")) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {

                        String token1="";

                        SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorP = userP.edit();
                        token1 = resp.getString("token");
                        editorP.putString("token_value",token1);
                        editorP.putLong("expires", resp.getLong("expiresAt"));
                        editorP.commit();
                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror111", e.getMessage());
                return "fail";

            }
        }
        protected void onPostExecute(String result) {
            if(result.equals("win")){

              //  Toast.makeText(ViewForm.this, "yey", Toast.LENGTH_SHORT).show();
//            next.new FacebookAuth.fblogin().execute();
               // new fblogin().execute();
//            next.fblogin().execute();
                new checkAuth().execute(url);


            }}}


    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        form.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        form.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        form.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        form.onActivityResult(requestCode, resultCode, intent);
        // ...
    }



    public class myWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            if(url.equals("buddy://profile"))
            {
                Toast.makeText(ViewForm.this, "gopi", Toast.LENGTH_SHORT).show();
            }
            else

            view.loadUrl(url);
            return true;

        }
    }


    @Override
    public void onPageStarted(String url, Bitmap favicon) {  spinner.setVisibility(View.INVISIBLE);}


    @Override
    public void onPageFinished(String url) {
    }

    @Override

    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) { }

    @Override
    public void onExternalPageRequest(String url) { }


}

