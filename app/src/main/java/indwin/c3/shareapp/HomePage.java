package indwin.c3.shareapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import indwin.c3.shareapp.Views.CustomHorizontalScrollView;
import indwin.c3.shareapp.activities.AccountSettingsActivity;
import indwin.c3.shareapp.activities.FindProduct;
import indwin.c3.shareapp.activities.ProfileActivity;
import indwin.c3.shareapp.activities.SupportedWebsites;
import indwin.c3.shareapp.adapters.HorizontalScrollViewAdapter;
import indwin.c3.shareapp.application.BuddyApplication;
import indwin.c3.shareapp.models.Product;
import indwin.c3.shareapp.models.RecentSearchItems;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.Constants;
import io.intercom.android.sdk.Intercom;
import io.intercom.com.google.gson.Gson;


public class HomePage extends AppCompatActivity {
    private String[] arraySpinner;
    WebView form;
    private Intent intform;
    private NavigationView navigationView;
    private ImageView card1, card2, card3, card4;
    String userId = "";
    Timer timer;
    int page1 = 0;
    Double latitude, longitude;
    HorizontalScrollViewAdapter adapter0;
    HorizontalScrollViewAdapter adapter1;
    HorizontalScrollViewAdapter adapter2;
    HorizontalScrollViewAdapter adapter3;
    HorizontalScrollViewAdapter adapter4;
    HorizontalScrollViewAdapter adapter5;
    HorizontalScrollViewAdapter adapter6;

    private ProgressBar spinner0;
    private ProgressBar spinner1;
    private ProgressBar spinner2;
    private ProgressBar spinner3;
    private ProgressBar spinner4;
    private ProgressBar spinner5;
    private ProgressBar spinner6;


    private String formstatus, name, fbid, rejectionReason, email, uniqueCode, verificationdate, creditLimit, searchTitle, searchBrand, searchCategory, searchSubcategory, description, specification, review, infor;
    SharedPreferences cred;
    int screen_no;
    private int searchPrice = 0;
    private String urlImg = "", page = "";
    public int mValue = 0, mValue2 = 0;

    private RelativeLayout drops, newtren;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private EditText query;
    private RelativeLayout noti;
    private IntentFilter intentFilter;
    private MyRecyclerAdapter adapt;
    private List<FeedItem> feedsList;
    private RecyclerView mRecycler;
    private int checkedit = 0, currDay;
    private BroadcastReceiver broadcastReceiver;
    private TextView but;
    ImageView dot1;
    ImageView dot2;
    ImageView dot3;
    ImageView dot4;
    private android.content.ClipboardManager myClipboard;
    private String spin = "";
    private String productId = "";
    private int checkValidUrl = 0, monthsallowed = 0;
    private Double emi = 0.0;
    CustomHorizontalScrollView horizontal1;
    CustomHorizontalScrollView horizontal0;
    CustomHorizontalScrollView horizontal2;
    CustomHorizontalScrollView horizontal3;
    CustomHorizontalScrollView horizontal4;
    CustomHorizontalScrollView horizontal5;
    CustomHorizontalScrollView horizontal6;


    private int checkValidFromApis = 0;
    String IMEINumber;
    String simSerialNumber;
    public int cb = 0;
    SharedPreferences st;
    private String sellerNme = "";
    private String token = "";
    Location getLastLocation;
    private SharedPreferences userP;
    private SharedPreferences mPrefs;
    private Gson gson;
    private int cuurr, dayToday;
    private UserModel user;
    private Tracker mTracker;
    //    TimerTask mTimerTask;
    public int currentPage = 0;
    public String[] urls;

    private SharedPreferences sh, ss;
    private ArrayList<RecentSearchItems> recentSearchItemsList = new ArrayList<>();
    ViewPager imageSlider;
    SecondViewPagerAdapter adp;
    SharedPreferences sharedpreferences, sharedpreferences2;
    public static final String MyPREFERENCES = "buddy";
    TextView supported;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                this.moveTaskToBack(true);
                return true;
            }
        return true;
        }

    public boolean emailverified = true;
    HashMap<String,ArrayList<Product>> productsMap = new HashMap<String,ArrayList<Product>>();
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = AppUtils.getUserObject(this);
        new GetImageUrls().execute();
        BuddyApplication application = (BuddyApplication) getApplication();
        mTracker = application.getDefaultTracker();
        sh = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        ss = getSharedPreferences("cred", Context.MODE_PRIVATE);
        userP = getSharedPreferences("token", Context.MODE_PRIVATE);
        cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
        sharedpreferences2 = getSharedPreferences("buddyin", Context.MODE_PRIVATE);
        if (sh.getInt("checklog", 0) == 1) {
            userId = sharedpreferences2.getString("name", null);
        }

        token = userP.getString("token_value", null);
        intentFilter = new IntentFilter();
        intentFilter.addAction("CLOSE_ALL");
        locationManager = (LocationManager) getSystemService
                (Context.LOCATION_SERVICE);
        getLastLocation = locationManager.getLastKnownLocation
                (LocationManager.PASSIVE_PROVIDER);


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Splash.checklog == 1)
                    finish();
                Splash.checklog = 0;                // close activity
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        IMEINumber = telephonyManager.getDeviceId();
        simSerialNumber = telephonyManager.getSimSerialNumber();
        if (Splash.checklog == 1)
            finish();
        else {
            setContentView(R.layout.activity_home_page);
            spinner0 = (ProgressBar)findViewById(R.id.progress_bar0);
            spinner0.setVisibility(View.VISIBLE);

            spinner1 = (ProgressBar)findViewById(R.id.progress_bar1);
            spinner1.setVisibility(View.VISIBLE);

            spinner2 = (ProgressBar)findViewById(R.id.progress_bar2);
            spinner2.setVisibility(View.VISIBLE);

            spinner3 = (ProgressBar)findViewById(R.id.progress_bar3);
            spinner3.setVisibility(View.VISIBLE);

            spinner4 = (ProgressBar)findViewById(R.id.progress_bar4);
            spinner4.setVisibility(View.VISIBLE);

            spinner5 = (ProgressBar)findViewById(R.id.progress_bar5);
            spinner5.setVisibility(View.VISIBLE);

            spinner6 = (ProgressBar)findViewById(R.id.progress_bar6);
            spinner6.setVisibility(View.VISIBLE);







            horizontal0 = (CustomHorizontalScrollView)findViewById(R.id.horizontal0);
            horizontal1 = (CustomHorizontalScrollView)findViewById(R.id.horizontal1);
            horizontal2 = (CustomHorizontalScrollView)findViewById(R.id.horizontal2);
            horizontal3 = (CustomHorizontalScrollView)findViewById(R.id.horizontal3);
            horizontal4 = (CustomHorizontalScrollView)findViewById(R.id.horizontal4);
            horizontal5 = (CustomHorizontalScrollView)findViewById(R.id.horizontal5);
            horizontal6 = (CustomHorizontalScrollView)findViewById(R.id.horizontal6);
            new GetTrendingProducts().execute("trending");
            new Trending("Mobiles").execute("Mobiles");

            new Trending("Electronics").execute("Electronics");
            new Trending("Computers&subCategory=Laptops").execute("Computers&subCategory=Laptops");
            new Trending("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle").execute("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle");
            new Trending("Health%20and%20Beauty").execute("Health%20and%20Beauty");
            new Trending("Footwear").execute("Footwear");


            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.editlayout);


            imageSlider = (ViewPager) findViewById(R.id.imageslider);
            //new GetImageBanners().execute();
            dot1 = (ImageView) findViewById(R.id.c1);
            dot2 = (ImageView) findViewById(R.id.c2);
            dot3 = (ImageView) findViewById(R.id.c3);
            dot4 = (ImageView) findViewById(R.id.c4);
            supported = (TextView) findViewById(R.id.supported);
            supported.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomePage.this, SupportedWebsites.class);
                    startActivity(intent);

                }
            });

            dot1.setBackgroundResource(R.drawable.circle2);
            imageSlider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {


                    if (position == 0) {


                        dot1.setBackgroundResource(R.drawable.circle2);
                        dot2.setBackgroundResource(R.drawable.circle);
                        dot3.setBackgroundResource(R.drawable.circle);
                        dot4.setBackgroundResource(R.drawable.circle);
                    } else if (position == 1) {
                        dot2.setBackgroundResource(R.drawable.circle2);
                        dot1.setBackgroundResource(R.drawable.circle);
                        dot3.setBackgroundResource(R.drawable.circle);
                        dot4.setBackgroundResource(R.drawable.circle);
                    } else if (position == 2) {
                        dot2.setBackgroundResource(R.drawable.circle);
                        dot1.setBackgroundResource(R.drawable.circle);
                        dot3.setBackgroundResource(R.drawable.circle2);
                        dot4.setBackgroundResource(R.drawable.circle);
                    } else if (position == 3) {
                        dot4.setBackgroundResource(R.drawable.circle2);
                        dot1.setBackgroundResource(R.drawable.circle);
                        dot3.setBackgroundResource(R.drawable.circle);
                        dot2.setBackgroundResource(R.drawable.circle);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            final Handler handler = new Handler();

            final Runnable update = new Runnable() {
                public void run() {
                    if (currentPage == 5 - 1) {
                        currentPage = 0;
                    }
                    imageSlider.setCurrentItem(currentPage++, true);
                }
            };


            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    handler.post(update);
                }
            }, 1000, 5000);


            if (!isNetworkAvailable()) {
                showMessageOKCancel("Internet is not working. Some features of the app may be disabled.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                @Override
                public void onScrollChanged() {

                    query.clearFocus();
                    hideSoftKeyboard(HomePage.this);                     //for verticalScrollView
                }
            });

            spin = "trending";

            FloatingActionButton intercom = (FloatingActionButton) findViewById(R.id.chat);
            intercom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Intercom.client().displayMessageComposer();
                    } catch (Exception e) {

                    }

                }
            });
            try {
                toolbar = (Toolbar) findViewById(R.id.toolbarc);
                TextView titlebar = (TextView) findViewById(R.id.titlehead);
                titlebar.setVisibility(View.GONE);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            } catch (Exception e) {
                System.out.println(e.toString() + "oscar goes");
            }
            ImageView inter = (ImageView) findViewById(R.id.interCom);
            inter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intercom.client().displayMessageComposer();
                    } catch (Exception e) {

                    }

                }
            });
            get();
            noti = (RelativeLayout) findViewById(R.id.not);

            if (Splash.notify == 1) {
                noti.setVisibility(View.GONE);
            } else if (Splash.notify == 0) {
                noti.setVisibility(View.VISIBLE);
            }

            ImageView cross = (ImageView) findViewById(R.id.close);
            cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noti.setVisibility(View.GONE);
                    Splash.notify = 1;

                }
            });

            noti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    query.clearFocus();
                    Intent intent = new Intent(HomePage.this, ProfileActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    Splash.notify = 1;
                }
            });
            mPrefs = getSharedPreferences("buddy", Context.MODE_PRIVATE);
            mPrefs.edit().putBoolean("visitedFormStep1Fragment3", true).apply();
            gson = new Gson();

            try {
                TextView name1, line1, but;
                name1 = (TextView) findViewById(R.id.nameintr);
                line1 = (TextView) findViewById(R.id.line1);
                but = (TextView) findViewById(R.id.but);
                name1.setText("Hi " + user.getName() + ",");

                String status60K = "";
                if (user.getStatus60K() != null) {
                    status60K = user.getStatus60K();
                }
                String status7K = "";
                if (user.getStatus7K() != null) {
                    status7K = user.getStatus7K();
                }
                String status1K = "";
                if (user.getStatus1K() != null) {
                    status1K = user.getStatus1K();
                }
                but.setVisibility(View.GONE);
                if (Constants.STATUS.DECLINED.toString().equals(user.getProfileStatus()) || Constants.STATUS.WAITLISTED.equals(user.getProfileStatus())) {

                    line1.setText("Your profile has currently been waitlisted for approval. Touch here to find out more.");

                } else if (Constants.STATUS.APPROVED.toString().equals(user.getProfileStatus())) {

                    if (Constants.STATUS.APPROVED.toString().equals(status60K)) {
                        name1.setText("Congrats " + user.getName() + ",");
                        line1.setText("You have been approved for Rs." + user.getCreditLimit() + " credit limit! Happy shopping!");
                    } else if (Constants.STATUS.APPROVED.toString().equals(status7K)) {
                        line1.setText("You have been approved for Rs." + user.getCreditLimit() + " credit limit! Go ahead and shop or complete your profile to apply for a higher credit limit.");
                    } else if (Constants.STATUS.APPROVED.toString().equals(status1K)) {
                        line1.setText("You have been approved for Rs." + user.getCreditLimit() + " FLASH credit limit! Go ahead and shop or complete your profile to apply for a higher credit limit.");
                    } else if (Constants.STATUS.DECLINED.toString().equals(status1K) || "waitlisted".equals(status1K)) {
                        line1.setText("Your profile has currently been waitlisted for Rs.1000 FLASH credit limit! We suggest that you complete your profile to apply for a higher limit.");
                    } else if (Constants.STATUS.APPLIED.toString().equals(status60K)) {
                        line1.setText("You have applied for upto Rs.60,000 credit. We will inform you as soon as it gets approved.");
                    } else if (Constants.STATUS.APPLIED.toString().equals(status7K)) {
                        line1.setText("You have applied for Rs.7000 credit limit. Go ahead and complete your profile to apply for higher credit limit!");
                    } else if (Constants.STATUS.APPLIED.toString().equals(status1K)) {
                        line1.setText("You have ap'plied for Rs.1000 FLASH credit limit. Go ahead and complete your profile to apply for higher credit limit.");
                    }
                } else {
                    if (Constants.STATUS.APPLIED.toString().equals(status60K)) {
                        line1.setText("You have applied for upto Rs.60,000 credit. We will inform you as soon as it gets approved.");
                    } else if (Constants.STATUS.APPLIED.toString().equals(status7K)) {
                        line1.setText("You have applied for Rs.7000 credit limit. Go ahead and complete your profile to apply for higher credit limit!");
                    } else if (Constants.STATUS.APPLIED.toString().equals(status1K)) {
                        line1.setText("You have applied for Rs.1000 FLASH credit limit. Go ahead and complete your profile to apply for higher credit limit.");
                    } else if (user.isEmailVerified()) {
                        line1.setText("Ready to get started? Complete your profile now to get a Borrowing Limit and start shopping");
                        but.setText("Complete it now!");
                        but.setVisibility(View.VISIBLE);
                    } else if(!user.isEmailVerified())
                        line1.setText("Your email Id needs to be verified.");
                        but.setText("verify your email Id now");
                        but.setVisibility(View.VISIBLE);
                        emailverified = false;
                    }

            } catch (Exception e) {
                e.printStackTrace();
            }


            but = (TextView) findViewById(R.id.but);

            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    query.clearFocus();
                    if (emailverified) {
                        Intent intent = new Intent(HomePage.this, ProfileActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        Splash.notify = 1;
                    } else {
                        Intent intent = new Intent(HomePage.this, AccountSettingsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        Splash.notify = 1;
                    }

                }
            });

            navigationView = (NavigationView) findViewById(R.id.navigation_view);

            navigationView.getMenu().getItem(0).setChecked(true);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {

                    drawerLayout.closeDrawers();

                    switch (menuItem.getItemId()) {

                        case R.id.MyAccount:

                            return true;
                        case R.id.work:


                            intform = new Intent(HomePage.this, ViewForm.class);

                            Splash.checkNot = 1;
                            intform.putExtra("which_page", 11);
                            intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                            startActivity(intform);
                            finish();
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.About:
                            Splash.checkNot = 1;
                            intform = new Intent(HomePage.this, ViewForm.class);
                            intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                            intform.putExtra("which_page", 3);
                            startActivity(intform);
                            finish();
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.app_form:
                            Splash.checkNot = 1;
                            intform = new Intent(HomePage.this, ProfileActivity.class);
                            startActivity(intform);
                            finish();

                            return true;
                        case R.id.faq:
                            navigationView.getMenu().getItem(0).setChecked(true);
                            Splash.checkNot = 1;
                            intform = new Intent(HomePage.this, ViewForm.class);
                            intform.putExtra("url", "http://hellobuddy.in/#/faqs");
                            intform.putExtra("which_page", 5);
                            startActivity(intform);
                            overridePendingTransition(0, 0);
                            finish();
                            return true;

                        case R.id.security:
                            Splash.checkNot = 1;
                            intform = new Intent(HomePage.this, ViewForm.class);

                            intform.putExtra("which_page", 15);
                            intform.putExtra("url", "http://hellobuddy.in/#/faqs");
                            startActivity(intform);
                            finish();
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.Orders:
                            intform = new Intent(HomePage.this, ViewForm.class);
                            intform.putExtra("which_page", 16);
                            Splash.checkNot = 1;
                            intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                            startActivity(intform);
                            finish();
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.Repayments:


                            intform = new Intent(HomePage.this, ViewForm.class);
                            Splash.checkNot = 1;
                            intform.putExtra("which_page", 17);
                            intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                            startActivity(intform);
                            overridePendingTransition(0, 0);
                            finish();
                            return true;
                        case R.id.Recharge:


                            intform = new Intent(HomePage.this, ViewForm.class);
                            Splash.checkNot = 1;
                            intform.putExtra("which_page", 999);
                            intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                            startActivity(intform);
                            finish();
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.Share:

                            SharedPreferences sh_otp = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editornew = sh_otp.edit();
                            editornew.putInt("chshare", 1);
                            Splash.checkNot = 1;
                            editornew.commit();
                            Intent in = new Intent(HomePage.this, ShareSecond.class);
                            startActivity(in);
                            overridePendingTransition(0, 0);
                            return true;

                        case R.id.log:

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
                            navigationView.getMenu().getItem(0).setChecked(true);
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
                            intform = new Intent(HomePage.this, MainActivity.class);
                            Splash.checkNot = 1;
                            finish();
                            startActivity(intform);
                            overridePendingTransition(0, 0);

                            return true;
                        default:
                            return true;
                    }
                }
            });

            drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
            NavigationView nview = (NavigationView) drawerLayout.findViewById(R.id.navigation_view);
            View headerView = nview.getHeaderView(0);
            ImageView arr = (ImageView) headerView.findViewById(R.id.arrow);
            RelativeLayout header = (RelativeLayout) headerView.findViewById(R.id.head);
            header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            });
            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {


                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }
            };

            drawerLayout.setDrawerListener(actionBarDrawerToggle);

            actionBarDrawerToggle.syncState();

            query = (EditText) findViewById(R.id.link);

            query.setImeOptions(EditorInfo.IME_ACTION_DONE);
            query.setInputType(InputType.TYPE_NULL);


            query.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                int w = 1;

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count == 0) {
                        w = 0;
                    } else {
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    query.requestFocus();
                }
            });
            DrawerLayout f = (DrawerLayout) findViewById(R.id.drawer);
            f.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    query.clearFocus();
                }
            });
            query.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    int action = 0;
                    if (action == MotionEvent.ACTION_DOWN && checkedit == 0) {
                        query.setInputType(InputType.TYPE_NULL);

                        Intent intent = new Intent(HomePage.this, FindProduct.class);
                        HomePage.this.startActivity(intent);
                        checkedit = 1;

                    }

                    return false;
                }
            });

            relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = 0;
                    if (action == MotionEvent.ACTION_DOWN && checkedit == 0) {
                        query.setInputType(InputType.TYPE_NULL);

                        Intent intent = new Intent(HomePage.this, FindProduct.class);
                        HomePage.this.startActivity(intent);
                        checkedit = 1;
                    }
                    return false;
                }
            });


            myClipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);



            this.arraySpinner = new String[]{
                    "MOBILES", "LAPTOPS", "FASHION", "BEAUTY & PERSONAL CARE", "ENTERTAINMENT", "FOOTWEAR"
            };
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        checkedit = 0;
    }


    @Override
    public void onDestroy() {

        try {
            if (broadcastReceiver != null)
                unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {

        }
        super.onDestroy();

    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void get() {
        try {
            formstatus = getIntent().getExtras().getString("Form");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        try {
            name = getIntent().getExtras().getString("Name");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        try {
            fbid = getIntent().getExtras().getString("fbid");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        try {
            rejectionReason = getIntent().getExtras().getString("Rej");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        try {
            email = getIntent().getExtras().getString("Email");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        try {
            uniqueCode = getIntent().getExtras().getString("UniC");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        try {
            screen_no = getIntent().getExtras().getInt("screen_no");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        try {
            verificationdate = getIntent().getExtras().getString("VeriDate");

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        try {
            creditLimit = getIntent().getExtras().getString("Credits");

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public int months(String subcat, String cat, String brand, int price)

    {
        int m = 18;
        if ((subcat.equals("Fitness " +
                "Equipments")) || ((subcat.equals("Jewellery"))) || ((subcat.equals("Combos and Kit"))) || ((subcat.equals("Speakers"))) || ((subcat.equals("Team Sports"))) || ((subcat.equals("Racquet Sports"))) || ((subcat.equals("Watches"))) || ((subcat.equals("Health and Personal Care"))) || ((subcat.equals("Leather & Travel Accessories"))) || ((cat.equals("Footwear")))) {
            int mn = 6;
            if (mn < m)
                m = mn;
        } else if ((subcat.equals("Cameras")) || ((subcat.equals("Entertainment"))) || ((subcat.equals("Smartwatches"))) || ((subcat.equals("Smart Headphones"))) || ((subcat.equals("Smart Bands"))) || ((subcat.equals("Digital Accessories"))) || ((subcat.equals("Tablets"))) || ((subcat.equals("Kindle")))) {
            int mn = 12;
            if (mn < m)
                m = mn;
        }
        if ((!brand.equals("Apple")) && !(brand.equals("APPLE"))) {
            int mn = 15;
            if (mn < m)
                m = mn;
        }
        if (price < 5000) {
            int mn = 6;
            if (mn < m)
                m = mn;
        } else if (price < 10000) {
            int mn = 9;
            if (mn < m)
                m = mn;
        } else if (price < 20000) {
            int mn = 12;
            if (mn < m)
                m = mn;
        } else if (price < 40000) {
            int mn = 15;
            if (mn < m)
                m = mn;
        }

        String course = userP.getString("course", "");

        if (!course.equals("")) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Date courseDate;
            Double diff = 18.0;
            try {
                courseDate = df.parse(course);
                String newDateString = df.format(courseDate);
                System.out.println(newDateString);
                Long milli = courseDate.getTime();
                Date date = new Date();
                Long currentMilli = date.getTime();
                Double diffDouble = (milli.doubleValue() - currentMilli.doubleValue());
                Double mul = (1000.0 * 60.0 * 60.0 * 24.0 * 365.0);
                diff = diffDouble / mul;
                diff = diff * 12.0;
                diff = Math.floor(diff);
                String curr = df.format(date);
                String currentDay = "";
                for (int j = curr.length() - 2; j < curr.length(); j++) {

                    currentDay += curr.charAt(j);
                }

                currDay = Integer.parseInt(currentDay);
                int months;
                if (currDay > 15)
                    diff -= 1.0;

                if (diff > 0) {
                    months = diff.intValue();
                    if (diff.intValue() == 1)
                        months = 1;
                    else if (diff.intValue() == 2)
                        months = 2;
                    else if (diff.intValue() >= 3 && diff.intValue() <= 5) {
                        months = 3;
                    } else if (diff.intValue() >= 6 && diff.intValue() <= 8) {
                        months = 6;
                    } else if (diff.intValue() >= 9 && diff.intValue() <= 11) {
                        months = 9;
                    } else if (diff.intValue() >= 12 && diff.intValue() <= 14) {
                        months = 12;
                    } else if (diff.intValue() >= 15 && diff.intValue() <= 18) {
                        months = 15;
                    }
                    if (m > months)
                        m = months;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return m;
    }


    public void parse(String parseString) {
        SharedPreferences cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = cred.edit();
        et.putString("urlprod", parseString);
        et.commit();
        productId = "";
        int pos = -1;
        if (parseString.contains("flipkart")) {
            sellerNme = "flipkart";
            pos = parseString.indexOf("pid");
            if (pos != -1) {
                for (int j = pos + 4; j < parseString.length(); j++) {
                    if (parseString.charAt(j) == '&')
                        break;
                    else {
                        productId += parseString.charAt(j);
                    }

                }
            } else {
                checkValidUrl = 1;
            }

        }

        else if (parseString.contains("snapdeal")) {
            sellerNme = "snapdeal";
            pos = parseString.lastIndexOf("/");
            if (pos != -1) {

                for (int j = pos + 1; j < parseString.length(); j++) {
                    if (((parseString.charAt(j)) >= '0') && (parseString.charAt(j) <= '9'))

                        productId += parseString.charAt(j);
                    else break;


                }
            } else {
                checkValidUrl = 1;
            }
        } else if (parseString.contains("myntra")) {
            sellerNme = "myntra";
            checkValidFromApis = 1;
        } else if (parseString.contains("shopclues")) {
            sellerNme = "shopclues";
            checkValidFromApis = 1;
        } else if (parseString.contains("jabong")) {
            sellerNme = "jabong";
            checkValidFromApis = 1;

        } else if (parseString.contains("paytm")) {
            sellerNme = "paytm";
            checkValidFromApis = 1;
        }
        //amazon
        else if (parseString.contains("amazon")) {
            sellerNme = "amazon";
            int w = 0;
            pos = parseString.indexOf("/dp/");
            if (pos != -1) {
                pos = parseString.indexOf("dp");
            }

            if (pos == -1) {
                pos = parseString.indexOf("/product/");
                if (pos != -1)
                    w = 1;
            }
            if (pos == -1) {

                pos = parseString.indexOf("/d/");
                if (pos != -1)
                    w = 2;
            }


            if (pos != -1) {
                int r = 0;
                if (w == 0)
                    r = pos + 3;
                if (w == 1)
                    r = pos + 9;
                if (w == 2)
                    r = pos + 3;
                for (int j = r; ; j++) {
                    if ((parseString.charAt(j) == '/') || ((parseString.charAt(j) == '?')))
                        break;
                    else {
                        productId += parseString.charAt(j);
                    }

                }
            } else {
                checkValidUrl = 1;
            }

        } else if (parseString.contains("shopclues"))
            checkValidFromApis = 1;
        else
            checkValidUrl = 1;

        if ((checkValidFromApis == 0) && (checkValidUrl == 0)) {
            //make api call
        }
        if ((checkValidFromApis == 1)) {
            //not monley page
        }
        if (checkValidUrl == 1) {
            //monkey page
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // pageSwitcher(5);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finish();

        }
    }

    public int setLoanAmt(int sellingPrice) {
            int loanAmt = 0;
            Double value = sellingPrice * .8;
            int loanAmt1 = value.intValue();
            int loanAmt2 = userP.getInt("creditLimit", 0) - userP.getInt("totalBorrowed", 0);
            if (loanAmt1 < loanAmt2) {
                loanAmt = loanAmt1;
            } else {
                loanAmt = loanAmt2;
            }
            return loanAmt;
        }


        public Double show(String subcat, String cat, String brand, int price) {

            int loanPrice = setLoanAmt(price);
            int monthsallowed = months(subcat, cat, brand, price);
            Double rate = 21.0 / 1200.0;
            int d = 0;
            if (price <= 5000) {
                emi = price * 0.8 / monthsallowed;
            } else {

                Date date = new Date();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String curr = df.format(date);
                String currentDay = "";
                for (int j = curr.length() - 2; j < curr.length(); j++) {

                    currentDay += curr.charAt(j);
                }

                currDay = Integer.parseInt(currentDay);
                if (currDay <= 15)
                    d = 35 - currDay;
                else
                    d = 65 - currDay;

                emi = Math.ceil((loanPrice * rate * Math.pow(1 + rate, monthsallowed - 1) * (1 + rate * d * 12 / 365)) / (Math.pow(1 + rate, monthsallowed) - 1));
            }
            return emi;
        }

    public class Trending extends
            AsyncTask<String, Void, String> {

        String productType;

        public Trending(String productType) {
            this.productType = productType;
        }

        @Override
        protected void onPreExecute() {



        }


        @Override
        protected String doInBackground(String... data) {

            String urldisplay = data[0];
            JSONObject payload = new JSONObject();
            try {

                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);

                String url = BuildConfig.SERVER_URL + "api/product/trending?category=" + urldisplay + "&count=10";

                HttpResponse response = AppUtils.connectToServerGet(url, tok_sp, null);
                if (response != null) {
                    HttpEntity ent = response.getEntity();
                    String responseString = EntityUtils.toString(ent, "UTF-8");
                    if (response.getStatusLine().getStatusCode() != 200) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {
                        ArrayList<Product> products = new ArrayList<>();
                        JSONObject resp = new JSONObject(responseString);
                        if (resp.getString("status").equals("success")) {
                            JSONArray data1 = new JSONArray(resp.getString("data"));
                            int lenght = data1.length();

                            for (int j = 0; j < 10; j++) {
                                try {
                                    JSONObject js = data1.getJSONObject(j);
                                    Product newProduct = new Product(data[0]);
                                    String categ = js.getString("category");
                                    newProduct.setCategory(categ);
                                    String subc = js.getString("subCategory");
                                    newProduct.setSubCategory(subc);
                                    String brand1 = js.getString("brand");
                                    newProduct.setBrand(brand1);
                                    String id = js.getString("title");
                                    newProduct.setTitle(id);
                                    //String mrp = js.getString("mrp");
                                    String seller = js.getString("seller");
                                    newProduct.setSeller(seller);
                                    String fkid = js.getString("fkProductId");
                                    newProduct.setFkid(fkid);
                                    String selling_price = js.getString("sellingPrice");
                                    newProduct.setSellingPrice(selling_price);
                                    JSONObject img = new JSONObject(js.getString("imgUrls"));
                                    String imgurl = img.getString("200x200");
                                    newProduct.setImgUrl(imgurl);
                                    products.add(newProduct);
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }

                            }
                            productsMap.put(data[0],products);
                            Log.i("trending", "called");
                            return "win";
                        } else
                            return "fail";


                    }
                } else {
                    return "fail";

                }
            } catch (Exception e) {
                String t = e.toString();
                return "fail";
            }
        }

        protected void onPostExecute(String result) {
            if (result.equals("fail")) {
                System.out.println("Error while computing data");
            } else {
                if(productType.equals("Mobiles")){
                    if(spinner1.getVisibility()==View.VISIBLE)
                        spinner1.setVisibility(View.GONE);

                    adapter1 = new HorizontalScrollViewAdapter(productsMap.get("Mobiles"),HomePage.this);
                    horizontal1.setAdapter(HomePage.this,adapter1);

                }
                if(productType.equals("Electronics")){
                    if(spinner2.getVisibility()==View.VISIBLE)
                        spinner2.setVisibility(View.GONE);
                    adapter2 = new HorizontalScrollViewAdapter(productsMap.get("Electronics"),HomePage.this);
                    horizontal2.setAdapter(HomePage.this,adapter2);
                }
                if(productType.equals("Computers&subCategory=Laptops")){
                    if(spinner3.getVisibility()==View.VISIBLE)
                        spinner3.setVisibility(View.GONE);
                    adapter3 = new HorizontalScrollViewAdapter(productsMap.get("Computers&subCategory=Laptops"),HomePage.this);
                    horizontal3.setAdapter(HomePage.this,adapter3);
                }

                if(productType.equals("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle")){
                    if(spinner4.getVisibility()==View.VISIBLE)
                        spinner4.setVisibility(View.GONE);
                    adapter4 = new HorizontalScrollViewAdapter(productsMap.get("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle"),HomePage.this);
                    horizontal4.setAdapter(HomePage.this,adapter4);
                }

                if(productType.equals("Health%20and%20Beauty")){
                    if(spinner5.getVisibility()==View.VISIBLE)
                        spinner5.setVisibility(View.GONE);
                    adapter5 = new HorizontalScrollViewAdapter(productsMap.get("Health%20and%20Beauty"),HomePage.this);
                    horizontal5.setAdapter(HomePage.this,adapter5);
                }

                if(productType.equals("Footwear")){
                    if(spinner6.getVisibility()==View.VISIBLE)
                        spinner6.setVisibility(View.GONE);
                    adapter6 = new HorizontalScrollViewAdapter(productsMap.get("Footwear"),HomePage.this);
                    horizontal6.setAdapter(HomePage.this,adapter6);
                }


            }
        }
    }


    private class GetTrendingProducts extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String url = BuildConfig.SERVER_URL+"api/product/trending?category=Computers&category=Mobiles&count=10";
            String urldisplay = params[0];
            try {
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                // String tok_sp = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjY1M2M2YTUwZTQzNzgyNjc0M2YyNjYiLCJuYW1lIjoiYnVkZHkgYXBpIGFkbWluIiwidXNlcm5hbWUiOiJidWRkeWFwaWFkbWluIiwicGFzc3dvcmQiOiJtZW1vbmdvc2gxIiwiZW1haWwiOiJjYXJlQGhlbGxvYnVkZHkuaW4iLCJpYXQiOjE0NjU1NDQwMDgsImV4cCI6MTQ2NTU4MDAwOH0.ZpAwCEB0lYSqiYdfaBYjnBJOXfGrqE9qN8USoRzWR8g";
                HttpResponse response = AppUtils.connectToServerGet(url, tok_sp, null);
                if (response != null) {
                    HttpEntity ent = response.getEntity();
                    String responseString = EntityUtils.toString(ent, "UTF-8");
                    if (response.getStatusLine().getStatusCode() != 200) {


                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {

                        JSONObject resp = new JSONObject(responseString);
                        if (resp.getString("status").equals("success")) {
                            JSONArray data1 = new JSONArray(resp.getString("data"));
                            ArrayList<Product> products = new ArrayList<>();
                            for (int j = 0; j < 10; j++) {
                                JSONObject js = data1.getJSONObject(j);
                                Product newProduct = new Product(params[0]);
                                String categ = js.getString("category");
                                newProduct.setCategory(categ);
                                String subc = js.getString("subCategory");
                                newProduct.setSubCategory(subc);
                                String brand1 = js.getString("brand");
                                newProduct.setBrand(brand1);
                                String id = js.getString("title");
                                newProduct.setTitle(id);
                                String seller = js.getString("seller");
                                newProduct.setSeller(seller);
                                String fkid = js.getString("fkProductId");
                                newProduct.setFkid(fkid);
                                String selling_price = js.getString("sellingPrice");
                                newProduct.setSellingPrice(selling_price);
                                JSONObject img = new JSONObject(js.getString("imgUrls"));
                                String imgurl = img.getString("200x200");
                                newProduct.setImgUrl(imgurl);
                                products.add(newProduct);


                            }
                            productsMap.put("trending",products);
                            Log.i("trending", "called");
                            return "win";
                        } else
                            return "fail";


                    }
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(spinner0.getVisibility()==View.VISIBLE)
                spinner0.setVisibility(View.GONE);

            adapter0 = new HorizontalScrollViewAdapter(productsMap.get("trending"),HomePage.this);
            horizontal0.setAdapter(HomePage.this,adapter0);

        }
    }

    private class GetImageUrls extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String url = BuildConfig.SERVER_URL+"api/v1/homepage/images?clientDevice=android";
            try {
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                // String tok_sp = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjY1M2M2YTUwZTQzNzgyNjc0M2YyNjYiLCJuYW1lIjoiYnVkZHkgYXBpIGFkbWluIiwidXNlcm5hbWUiOiJidWRkeWFwaWFkbWluIiwicGFzc3dvcmQiOiJtZW1vbmdvc2gxIiwiZW1haWwiOiJjYXJlQGhlbGxvYnVkZHkuaW4iLCJpYXQiOjE0NjU1NDQwMDgsImV4cCI6MTQ2NTU4MDAwOH0.ZpAwCEB0lYSqiYdfaBYjnBJOXfGrqE9qN8USoRzWR8g";
                HttpResponse response = AppUtils.connectToServerGet(url, tok_sp, null);
                if (response != null) {
                    HttpEntity ent = response.getEntity();
                    String responseString = EntityUtils.toString(ent, "UTF-8");
                    if (response.getStatusLine().getStatusCode() != 200) {


                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {

                        JSONObject resp = new JSONObject(responseString);
                        if (resp.getString("status").equals("success")) {
                            JSONArray data1 = new JSONArray(resp.getString("data"));
                            JSONObject json = data1.getJSONObject(0);
                            JSONArray jsonArray = json.getJSONArray("urls");
                            String[] urls1 = new String[jsonArray.length()];
                            for(int i=0;i<jsonArray.length();i++){
                                urls1[i] = jsonArray.getString(i);
                            }
                            urls = urls1;

                            return "win";
                        } else
                            return "fail";


                    }
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adp = new SecondViewPagerAdapter(getApplicationContext(),urls, HomePage.this);
            imageSlider.setAdapter(adp);
            adp.notifyDataSetChanged();

            imageSlider.setCurrentItem(0);

        }
    }



}

