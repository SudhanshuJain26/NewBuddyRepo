package indwin.c3.shareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class ViewForm extends AppCompatActivity {
private WebView form;String userid="",token="",url="";
    private Intent intform;
    private ProgressBar spinner;
    SharedPreferences userP;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    int which_page=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_form);
        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);
//getSupportActionBar().setBackgroundDrawable(R.id.buddylogo_blue);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
        catch(Exception e){System.out.println(e.toString()+"digo");}

        spinner=(ProgressBar)findViewById(R.id.progressBar1);
         userP = getSharedPreferences("buddyin", Context.MODE_PRIVATE);
        userid=userP.getString("name", null);
        token=userP.getString("tok", null);
        SharedPreferences.Editor editornew = userP.edit();
        editornew.putInt("back", 1);
        editornew.commit();
//         url=getIntent().getExtras().getString("url");
        which_page=getIntent().getExtras().getInt("which_page");
        form=(WebView)findViewById(R.id.webView);
        WebSettings settings = form.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);

//        form.addJavascriptInterface(new WebAppInterface(this),Andr);
        settings.setDomStorageEnabled(true);


//        settings.s

        new checkAuth().execute(url);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
if((which_page==4)||(which_page==6)||(which_page==7))
        navigationView.getMenu().getItem(1).setChecked(true);
else
if(which_page==3)
    navigationView.getMenu().getItem(2).setChecked(true);
        else
if(which_page==5)
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
                    case R.id.About:

                        intform = new Intent(ViewForm.this, ViewForm.class);
                        finish();
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        intform.putExtra("which_page", 3);
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.app_form:

                        intform = new Intent(ViewForm.this, ViewForm.class);
                        finish();
                        intform.putExtra("url", "http://hellobuddy.in/");
                        intform.putExtra("which_page", 4);
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
                    case R.id.Share:

                        SharedPreferences sh_otp=getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editornew = sh_otp.edit();
//                        editor.putInt("checklog",a);
                        editornew.putInt("chshare", 1);
                        //editornew.putString("rcode", getIntent().getExtras().getString("UniC"));
                        editornew.commit();
                        Intent in=new Intent(ViewForm.this,Share.class);

                        startActivity(in);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.log:
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
                        intform = new Intent(ViewForm.this, MainActivity.class);
                        finish();
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    default:
                        return true;
                }
                //Check to see which item was being clicked and perform appropriate action

            }});

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
    public void onBackPressed()
    {

        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else
        {
        Intent in=new Intent(ViewForm.this,Landing.class);
            finish();

        startActivity(in);
            overridePendingTransition(0,0);}
        // code here to show dialoguper.onBackPressed();  // optional depending on your needs
    }
    private class checkAuth extends
            AsyncTask<String, Void, String> {
        @Override
        protected  void onPreExecute()
        {
            spinner.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... data) {

            // String urldisplay = data[0];
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST

                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                String url2= getApplicationContext().getString(R.string.server)+"api/user/authtoken?userid="+userid;


                HttpGet httppost = new HttpGet(url2);
                try {

                }
                catch(Exception e){System.out.println("dio "+e.toString());}
                httppost.setHeader("x-access-token", token);


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
//                    JSONObject data1 = new JSONObject(resp.getString("data"));



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

            if(!result.equals("error"))
            {
                switch (which_page)
                {
                    case 1:
                        url="http://hellobuddy.in/?m=no&userid="+userid+"&key="+result+"#/";
                        break;
                    case 2:
                        url="http://hellobuddy.in/?m=no&userid="+userid+"&key="+result+"#/";
                        break;
                    case 3:
                        url="http://hellobuddy.in/?m=no&userid="+userid+"&key="+result+"#/about-us";
                        break;
                    case 4:
                        url="http://hellobuddy.in/?m=no&userid="+userid+"&key="+result+"#/profile";
                        break;
                    case 5:
                        url="http://hellobuddy.in/?m=no&userid="+userid+"&key="+result+"#/faqs";
                        break;
                    case 6:
                        url="http://hellobuddy.in/?m=no&userid="+userid+"&key="+result+"#/profile";
                        break;

                    case 7:
                        url="http://hellobuddy.in/?m=no&userid="+userid+"&key="+result+"#/profile";
                        break;



                }
                System.out.print("aT123"+result);
                form.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

                form.getSettings().setLoadsImagesAutomatically(true);

                form.setWebViewClient(new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.i(TAG, "Processing webview url click...");
                        return super.shouldOverrideUrlLoading(view, url);
                    }

                });
                form.loadUrl(url);
                spinner.setVisibility(View.GONE);
                form.setWebChromeClient(new WebChromeClient() {
                    public boolean onConsoleMessage(ConsoleMessage cm) {

                        Log.d("MyApplication", cm.message() + " -- From line "
                                + cm.lineNumber() + " of "
                                + cm.sourceId());
                        return true;
                    }
                });
            }

        }}

    }
