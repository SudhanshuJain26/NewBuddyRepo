 package indwin.c3.shareapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import indwin.c3.shareapp.models.TrendingMapWrapper;
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
    //ImageView paste;
    ImageView dot1;
    ImageView dot2;
    ImageView dot3;
    ImageView dot4;
    //    Map<String,Map<int,V>> map;

    //    HashMap<String, HashMap<String,String>> image;
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
    public int cb = 0;
    SharedPreferences st;
    private String sellerNme = "";
    private String token = "";
    private SharedPreferences userP;
    private SharedPreferences mPrefs;
    private Gson gson;
    private int cuurr, dayToday;
    private UserModel user;
    private Tracker mTracker;
    //    TimerTask mTimerTask;
    public int currentPage = 0;

    private SharedPreferences sh, ss;
    private ArrayList<RecentSearchItems> recentSearchItemsList = new ArrayList<>();
    ViewPager imageSlider;
    SecondViewPagerAdapter adp;
    SharedPreferences sharedpreferences, sharedpreferences2;
    public static final String MyPREFERENCES = "buddy";
    TextView supported;
    public boolean emailverified = true;
    HashMap<String,ArrayList<Product>> productsMap = new HashMap<String,ArrayList<Product>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = AppUtils.getUserObject(this);
        BuddyApplication application = (BuddyApplication) getApplication();
        mTracker = application.getDefaultTracker();
        sh = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        ss = getSharedPreferences("cred", Context.MODE_PRIVATE);
        userP = getSharedPreferences("token", Context.MODE_PRIVATE);
        cred = getSharedPreferences("cred", Context.MODE_PRIVATE);

//        new Trending().execute("Computers&subCategory=Laptops");
//        new Trending().execute("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle");
//        new Trending().execute("Health%20and%20Beauty");
//        new Trending().execute("Electronics");
//        new Trending().execute("Footwear");

        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences2 = getSharedPreferences("buddyin", Context.MODE_PRIVATE);
        if (sh.getInt("checklog", 0) == 1) {
            userId = sharedpreferences2.getString("name", null);
        }

        token = userP.getString("token_value", null);
        intentFilter = new IntentFilter();
        intentFilter.addAction("CLOSE_ALL");


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Splash.checklog == 1)
                    finish();
                Splash.checklog = 0;                // close activity
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
        if (Splash.checklog == 1)
            finish();
        else {
            setContentView(R.layout.activity_home_page);
            spinner0 = (ProgressBar)findViewById(R.id.progress_bar0);
            spinner0.setVisibility(View.VISIBLE);

            spinner1 = (ProgressBar)findViewById(R.id.progress_bar);
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
            //new Trending("trending").execute("trending");
            new Trending("Mobiles").execute("Mobiles");
            new Trending("Electronics").execute("Electronics");
            new Trending("Computers&subCategory=Laptops").execute("Computers&subCategory=Laptops");
            new Trending("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle").execute("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle");
            new Trending("Health%20and%20Beauty").execute("Health%20and%20Beauty");
            new Trending("Footwear").execute("Footwear");

//            ImageView image1 = (ImageView) findViewById(R.id.img11);
//            ImageView image2 = (ImageView) findViewById(R.id.img12);
//            ImageView image3 = (ImageView) findViewById(R.id.img13);
//            ImageView image4 = (ImageView) findViewById(R.id.img14);
//            ImageView image5 = (ImageView) findViewById(R.id.img21);
//            ImageView image6 = (ImageView) findViewById(R.id.img22);
//            ImageView image7 = (ImageView) findViewById(R.id.img23);
//            ImageView image8 = (ImageView) findViewById(R.id.img24);
//            ImageView image9 = (ImageView) findViewById(R.id.img31);
//            ImageView image10 = (ImageView) findViewById(R.id.img32);
//            ImageView image11 = (ImageView) findViewById(R.id.img33);
//            ImageView image12 = (ImageView) findViewById(R.id.img34);
//            ImageView image13 = (ImageView) findViewById(R.id.img41);
//            ImageView image14 = (ImageView) findViewById(R.id.img42);
//            ImageView image15 = (ImageView) findViewById(R.id.img43);
//            ImageView image16 = (ImageView) findViewById(R.id.img44);
//            ImageView image17 = (ImageView) findViewById(R.id.img51);
//            ImageView image18 = (ImageView) findViewById(R.id.img52);
//            ImageView image19 = (ImageView) findViewById(R.id.img53);
//            ImageView image20 = (ImageView) findViewById(R.id.img54);
//            ImageView image21 = (ImageView) findViewById(R.id.img61);
//            ImageView image22 = (ImageView) findViewById(R.id.img62);
//            ImageView image23 = (ImageView) findViewById(R.id.img63);
//            ImageView image24 = (ImageView) findViewById(R.id.img64);
//            ImageView image25 = (ImageView) findViewById(R.id.img01);
//            ImageView image26 = (ImageView) findViewById(R.id.img02);
//            ImageView image27 = (ImageView) findViewById(R.id.img03);
//            ImageView image28 = (ImageView) findViewById(R.id.img04);
//            ImageView image29 = (ImageView) findViewById(R.id.img05);
//            ImageView image30 = (ImageView) findViewById(R.id.img06);
//            ImageView image31 = (ImageView) findViewById(R.id.img07);
//            ImageView image32 = (ImageView) findViewById(R.id.img08);
//            ImageView image33 = (ImageView) findViewById(R.id.img09);
//            ImageView image34 = (ImageView) findViewById(R.id.img10);

//            ImageView image70 = (ImageView) findViewById(R.id.img15);
//            ImageView image35 = (ImageView) findViewById(R.id.img16);
//            ImageView image36 = (ImageView) findViewById(R.id.img17);
//            ImageView image37 = (ImageView) findViewById(R.id.img18);
//            ImageView image38 = (ImageView) findViewById(R.id.img19);
//            ImageView image39 = (ImageView) findViewById(R.id.img20);
//            ImageView image40 = (ImageView) findViewById(R.id.img25);
//            ImageView image41 = (ImageView) findViewById(R.id.img26);
//            ImageView image42 = (ImageView) findViewById(R.id.img27);
//            ImageView image43 = (ImageView) findViewById(R.id.img28);
//            ImageView image44 = (ImageView) findViewById(R.id.img29);
//            ImageView image45 = (ImageView) findViewById(R.id.img30);
//            ImageView image46 = (ImageView) findViewById(R.id.img35);
//            ImageView image47 = (ImageView) findViewById(R.id.img36);
//            ImageView image48 = (ImageView) findViewById(R.id.img37);
//            ImageView image49 = (ImageView) findViewById(R.id.img38);
//            ImageView image50 = (ImageView) findViewById(R.id.img39);
//            ImageView image51 = (ImageView) findViewById(R.id.img40);
//            ImageView image52 = (ImageView) findViewById(R.id.img45);
//            ImageView image53 = (ImageView) findViewById(R.id.img46);
//            ImageView image54 = (ImageView) findViewById(R.id.img47);
//            ImageView image55 = (ImageView) findViewById(R.id.img48);
//            ImageView image56 = (ImageView) findViewById(R.id.img49);
//            ImageView image57 = (ImageView) findViewById(R.id.img50);
//            ImageView image58 = (ImageView) findViewById(R.id.img55);
//            ImageView image59 = (ImageView) findViewById(R.id.img56);
//            ImageView image60 = (ImageView) findViewById(R.id.img57);
//            ImageView image61 = (ImageView) findViewById(R.id.img58);
//            ImageView image62 = (ImageView) findViewById(R.id.img59);
//            ImageView image63 = (ImageView) findViewById(R.id.img60);
//            ImageView image64 = (ImageView) findViewById(R.id.img65);
//            ImageView image65 = (ImageView) findViewById(R.id.img66);
//            ImageView image66 = (ImageView) findViewById(R.id.img67);
//            ImageView image67 = (ImageView) findViewById(R.id.img68);
//            ImageView image68 = (ImageView) findViewById(R.id.img69);
//            ImageView image69 = (ImageView) findViewById(R.id.img70);


//            image1.setOnClickListener(this);
//            image2.setOnClickListener(this);
//            image3.setOnClickListener(this);
//            image4.setOnClickListener(this);
//            image5.setOnClickListener(this);
//            image6.setOnClickListener(this);
//            image7.setOnClickListener(this);
//            image8.setOnClickListener(this);
//            image9.setOnClickListener(this);
//            image10.setOnClickListener(this);
//            image11.setOnClickListener(this);
//            image12.setOnClickListener(this);
//            image13.setOnClickListener(this);
//            image14.setOnClickListener(this);
//            image15.setOnClickListener(this);
//            image16.setOnClickListener(this);
//            image17.setOnClickListener(this);
//            image18.setOnClickListener(this);
//            image19.setOnClickListener(this);
//            image20.setOnClickListener(this);
//            image21.setOnClickListener(this);
//            image22.setOnClickListener(this);
//            image23.setOnClickListener(this);
//            image24.setOnClickListener(this);
//            image25.setOnClickListener(this);
//            image26.setOnClickListener(this);
//            image27.setOnClickListener(this);
//            image28.setOnClickListener(this);
//            image29.setOnClickListener(this);
//            image30.setOnClickListener(this);
//            image31.setOnClickListener(this);
//            image32.setOnClickListener(this);
//            image33.setOnClickListener(this);
//            image34.setOnClickListener(this);
//            image35.setOnClickListener(this);
//            image36.setOnClickListener(this);
//            image37.setOnClickListener(this);
//            image38.setOnClickListener(this);
//            image39.setOnClickListener(this);
//            image40.setOnClickListener(this);
//            image41.setOnClickListener(this);
//            image42.setOnClickListener(this);
//            image43.setOnClickListener(this);
//            image44.setOnClickListener(this);
//            image45.setOnClickListener(this);
//            image46.setOnClickListener(this);
//            image47.setOnClickListener(this);
//            image48.setOnClickListener(this);
//            image49.setOnClickListener(this);
//            image50.setOnClickListener(this);
//            image51.setOnClickListener(this);
//            image52.setOnClickListener(this);
//            image53.setOnClickListener(this);
//            image54.setOnClickListener(this);
//            image55.setOnClickListener(this);
//            image56.setOnClickListener(this);
//            image57.setOnClickListener(this);
//            image58.setOnClickListener(this);
//            image59.setOnClickListener(this);
//            image60.setOnClickListener(this);
//            image61.setOnClickListener(this);
//            image62.setOnClickListener(this);
//            image63.setOnClickListener(this);
//            image64.setOnClickListener(this);
//            image65.setOnClickListener(this);
//            image66.setOnClickListener(this);
//            image67.setOnClickListener(this);
//            image68.setOnClickListener(this);
//            image69.setOnClickListener(this);
//            image70.setOnClickListener(this);


            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.editlayout);


            imageSlider = (ViewPager) findViewById(R.id.imageslider);
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


            // paste = (ImageView) findViewById(R.id.pasteAg);
            adp = new SecondViewPagerAdapter(getApplicationContext(), 4, HomePage.this);
            imageSlider.setAdapter(adp);
            //AsynchTaskTimer();
//            doTimerTask();
            adp.notifyDataSetChanged();

            imageSlider.setCurrentItem(0);
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

//            ImageView img1 = (ImageView) findViewById(R.id.img1);
//            img1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                }
//            });


            if (!isNetworkAvailable()) {
                showMessageOKCancel("Internet is not working. Some features of the app may be disabled.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }

//
            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                @Override
                public void onScrollChanged() {

                    query.clearFocus();
                    hideSoftKeyboard(HomePage.this);                     //for verticalScrollView
                    //DO SOMETHING WITH THE SCROLL COORDINATES

                }
            });

//

            spin = "trending";

            try {
               // populateTrendingRow();
            } catch (Exception e) {
            }
            try {
                //  cardclick();
            } catch (Exception e) {
            }

            try {
                spin = "Mobiles";
                //populateFirstRow();
                //  cardclick();
            } catch (Exception e) {
            }

            spin = "Electronics";
            try {

//                populateSecondRow();
            } catch (Exception e) {

                System.out.println(e.getMessage());
            }
            spin = "Computers&subCategory=Laptops";
            try {

                //populateThirdRow();
            } catch (Exception e) {

                System.out.println(e.getMessage());
            }
            spin = "Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle";
            try {

               // populateFouthRow();
            } catch (Exception e) {

                System.out.println(e.getMessage());
            }
            spin = "Health%20and%20Beauty";
            try {
            //    populateFifthRow();
            } catch (Exception e) {

                System.out.println(e.getMessage());
            }
            spin = "Footwear";
            try {
             //   populateSixthRow();
            } catch (Exception e) {

                System.out.println(e.getMessage());
            }


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

                    // clickpaste();

                }
            });

            //pageSwitcher(5);


            noti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    query.clearFocus();
                    //                    send();
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
                TextView name1, line1, line2, but;
                name1 = (TextView) findViewById(R.id.nameintr);
                line1 = (TextView) findViewById(R.id.line1);
                line2 = (TextView) findViewById(R.id.line2);
                TextView line3 = (TextView) findViewById(R.id.line3);
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
                    } else if ("Check".equals(AccountSettingsActivity.verifyEmail.getText().toString())) {
                        line1.setText("Ready to get started? Complete your profile now to get a Borrowing Limit and start shopping");
                        but.setText("Complete it now!");
                        but.setVisibility(View.VISIBLE);
                    } else {
                        line1.setText("Your email Id needs to be verified. Do it quickly to get your profile approved.");
                        but.setText("verify your email Id now");
                        but.setVisibility(View.VISIBLE);
                        emailverified = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            but = (TextView) findViewById(R.id.but);

            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    query.clearFocus();

                    //                    send();
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

            //        navigationView.getMenu().getItem(0).setChecked(true);
            //        GridView gridviewshow=(GridView)findViewById(R.id.grid1a);
            //       gridviewshow.setAdapter(new ImageAdapter(this));


            navigationView.getMenu().getItem(0).setChecked(true);
            //Setting Navigation View Item Selected Listener
            // to handle the item click of the navigation menu
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                // This method will trigger on item Click of navigation menu
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {


                    //Checking if the item is in
                    //Closing drawer on item click
                    drawerLayout.closeDrawers();

                    switch (menuItem.getItemId()) {

                        case R.id.MyAccount:

                            return true;
                        case R.id.work:


                            intform = new Intent(HomePage.this, ViewForm.class);

                            Splash.checkNot = 1;
                            //clickpaste();
                            intform.putExtra("which_page", 11);
                            intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                            startActivity(intform);
                            finish();
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.About:
                            Splash.checkNot = 1;

                            // clickpaste();
                            intform = new Intent(HomePage.this, ViewForm.class);
                            //                        else
                            //                            intform=new Intent(Formempty.this, FacebookAuth.class);

                            // finish();
                            intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                            intform.putExtra("which_page", 3);
                            //  finish();
                            startActivity(intform);
                            finish();
                            overridePendingTransition(0, 0);
                            //                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                            //                        intform.putExtra("which_page",3);
                            //                        finish();
                            //                        startActivity(intform);
                            //                        overridePendingTransition(0, 0);
                            return true;
                        case R.id.app_form:
                            Splash.checkNot = 1;

                            // clickpaste();
                            intform = new Intent(HomePage.this, ProfileActivity.class);
                            startActivity(intform);
                            finish();

                            return true;
                        case R.id.faq:
                            navigationView.getMenu().getItem(0).setChecked(true);
                            // Intent in//
                            Splash.checkNot = 1;

                            // clickpaste();
                            intform = new Intent(HomePage.this, ViewForm.class);
                            //                        else
                            //                            intform=new Intent(Formempty.this, FacebookAuth.class);


                            intform.putExtra("url", "http://hellobuddy.in/#/faqs");
                            intform.putExtra("which_page", 5);
                            //     finish();
                            startActivity(intform);
                            overridePendingTransition(0, 0);
                            finish();
                            return true;

                        case R.id.security:
                            Splash.checkNot = 1;


                            //clickpaste();

                            //                        if(!fbid.equals("empty"))

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

                            //  clickpaste();
                            intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                            startActivity(intform);
                            finish();
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.Repayments:


                            intform = new Intent(HomePage.this, ViewForm.class);
                            Splash.checkNot = 1;

                            //clickpaste();

                            intform.putExtra("which_page", 17);
                            intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                            startActivity(intform);
                            overridePendingTransition(0, 0);
                            finish();
                            return true;
                        case R.id.Recharge:


                            intform = new Intent(HomePage.this, ViewForm.class);
                            Splash.checkNot = 1;

                            // clickpaste();

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
                            // finish();
                            Splash.checkNot = 1;

                            //clickpaste();
                            //   editornew.putString("rcode", getIntent().getExtras().getString("UniC"));
                            editornew.commit();
                            Intent in = new Intent(HomePage.this, Share.class);
                            startActivity(in);
                            finish();
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

                            //  clickpaste();
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
            NavigationView nview = (NavigationView) drawerLayout.findViewById(R.id.navigation_view);
            View headerView = nview.getHeaderView(0);
            ImageView arr = (ImageView) headerView.findViewById(R.id.arrow);
            RelativeLayout header = (RelativeLayout) headerView.findViewById(R.id.head);
            //RelativeLayout header = (RelativeLayout) headerView.findViewById(R.id.head);
            header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //     Toast.makeText(Formempty.this, "clicked", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            });
            //dddddd
            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {


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


            // paste.setVisibility(View.GONE);
            //        mRecycler = (RecyclerView) findViewById(R.id.recycler_view);

            query = (EditText) findViewById(R.id.link);

            // query.setCompoundDrawablesWithIntrinsicBounds(50,50,50,50);
            query.setImeOptions(EditorInfo.IME_ACTION_DONE);
            query.setInputType(InputType.TYPE_NULL);

//            query.setOnFocusChangeListener(new View.OnFocusChangeListener() {  // done by sudhanshu
//
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (!hasFocus) {
//                        paste.setVisibility(View.GONE);
//
//
//                        // code to execute when EditText loses focus
//                    }if(hasFocus){
//                        paste.setVisibility(View.VISIBLE);
//
//
//                    }
//                }
//            });


            query.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // hideSoftKeyboard(HomePage.this);
                    // paste.setVisibility(View.GONE);
                }

                int w = 1;

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count == 0) {
                        w = 0;
//                        ImageView home = (ImageView) findViewById(R.id.ho);
//                        home.setBackgroundResource(R.drawable.list_grad);
                        //  Toast.makeText(HomePage.this, "2", Toast.LENGTH_LONG).show();
                        // paste.setVisibility(View.VISIBLE);
                    } else {
                        //paste.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    query.requestFocus();

                    //if(w==0)
                    //    paste.setVisibility(View.VISIBLE);
                }
            });
            //


//            query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                    if (actionId == EditorInfo.IME_ACTION_DONE) {
//
//
//                        //                        Intent in = new Intent(HomePage.this, ViewForm.class);
//                        Splash.checkNot = 1;
//                        paste = (ImageView) findViewById(R.id.pasteAg);
//                        query.requestFocus();
//                        clickpaste();
//                        parse(query.getText().toString().trim());
//
//                        if ((checkValidUrl == 0) && (checkValidFromApis == 0)) {
//                            Long time = Calendar.getInstance().getTimeInMillis() / 1000;
//                            Intent in = new Intent(HomePage.this, ProductsPage.class);
//                            in.putExtra("seller", sellerNme);
//                            in.putExtra("product", productId);
//                            in.putExtra("query", query.getText().toString());
//                            query.setText("");
//                            in.putExtra("page", "api");
//                            checkValidFromApis = 0;
//                            checkValidUrl = 0;
//                            startActivity(in);
//                            //                            if (time + 5 < userP.getLong("expires", 0))
//                            ////                                new checkAuth().execute(url);//
//                            //                            {
//                            //                                new linkSearch().execute();
//                            //                            } else
//                            //                                //   new checkAuth().execute(url);
//                            //                                new AuthTokc().execute("cc");
//
//                        } else if (checkValidUrl == 1) {
//                            //monkey page
//                            Intent in = new Intent(HomePage.this, ProductsPage.class);
//                            query.setText("");
//                            in.putExtra("query", query.getText().toString());
//                            in.putExtra("page", "monkey");
//                            startActivity(in);
//                            checkValidFromApis = 0;
//                            checkValidUrl = 0;
//                            //                            finish();
//                            page = "monkey";
//                        } else if ((checkValidFromApis == 1)) {
//                            //not monley page
//                            query.setText("");
//                            Intent in = new Intent(HomePage.this, ProductsPage.class);
//                            in.putExtra("query", query.getText().toString());
//                            in.putExtra("seller", sellerNme);
//                            in.putExtra("page", "pay");
//                            startActivity(in);
//                            checkValidFromApis = 0;
//                            checkValidUrl = 0;
//                            //                            finish();
//                            page = "pay";
//                        }
//                        //                        in.putExtra("url", query.getText().toString());
//                        //                        in.putExtra("which_page", 9);
//                        //                        startActivity(in);
//                        //  Toast.makeText(HomePage.this,"oscar goes to caprio",Toast.LENGTH_LONG).show();
//                        //TODO: do something
//                    }
//                    return false;
//                }
//            });
//            ImageView home = (ImageView) findViewById(R.id.ho);
//            home.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    query.clearFocus();
//                    hideSoftKeyboard(HomePage.this);
//                }
//            });
            DrawerLayout f = (DrawerLayout) findViewById(R.id.drawer);
            f.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    query.clearFocus();
                    // hideSoftKeyboard(HomePage.this);
                }
            });
            query.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    int action = 0;
                    // if (checkedit == 1) {
                    //       Toast.makeText(HomePage.this,"check",Toast.LENGTH_LONG).show();
                    if (action == MotionEvent.ACTION_DOWN && checkedit == 0) {
                        //paste.setVisibility(View.VISIBLE);
                        query.setInputType(InputType.TYPE_NULL);

                        Intent intent = new Intent(HomePage.this, FindProduct.class);
//                        //intent.putExtra("searchlist",recentSearchItemsList);
//                        Log.i("jbabc","2345");
                        HomePage.this.startActivity(intent);
                        // new FindRecentProductLinks(HomePage.this).execute("https://ssl.hellobuddy.in/api/user/product/recent?userid="+userId +"&count=5");
                        checkedit = 1;
                        // ImageView home = (ImageView) findViewById(R.id.ho);

                    }
//                    }
//                    checkedit = 1;

                    return false;
                }
            });

            relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = 0;
                    // if (checkedit == 1) {
                    //       Toast.makeText(HomePage.this,"check",Toast.LENGTH_LONG).show();
                    if (action == MotionEvent.ACTION_DOWN && checkedit == 0) {
                        //paste.setVisibility(View.VISIBLE);
                        query.setInputType(InputType.TYPE_NULL);

                        Intent intent = new Intent(HomePage.this, FindProduct.class);
//                        //intent.putExtra("searchlist",recentSearchItemsList);
//                        Log.i("jbabc","2345");
                        HomePage.this.startActivity(intent);
//                        new FindRecentProductLinks(HomePage.this).execute("https://ssl.hellobuddy.in/api/user/product/recent?userid=8971923656&count=5");
                        checkedit = 1;
                        // ImageView home = (ImageView) findViewById(R.id.ho);
                    }
//                    }
                    return false;
                }
            });

//            query.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    if (checkedit == 1) {
//                        //       Toast.makeText(HomePage.this,"check",Toast.LENGTH_LONG).show();
//                        paste.setVisibility(View.VISIBLE);
//                        query.setInputType(InputType.TYPE_NULL);
////                        Log.i("Tension","called twice");
////                        Intent intent = new Intent(HomePage.this, FindProduct.class);
////                        //intent.putExtra("searchlist",recentSearchItemsList);
////                        Log.i("jbabc","2345");
////                        HomePage.this.startActivity(intent);
//                        new FindRecentProductLinks(HomePage.this).execute("https://ssl.hellobuddy.in/api/user/product/recent?userid=8971923656&count=5");
//
//                        ImageView home = (ImageView) findViewById(R.id.ho);
//
////                    }
////                    checkedit=1;
//                }
//            });

            myClipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            //clickpaste();


            this.arraySpinner = new String[]{
                    "MOBILES", "LAPTOPS", "FASHION", "BEAUTY & PERSONAL CARE", "ENTERTAINMENT", "FOOTWEAR"
            };
//
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        checkedit = 0;
    }

//


    private void setNewIdsNull(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.optJSONArray("newAddressProofs") != null) {
                jsonObject.put("newAddressProofs", null);
            }
            if (jsonObject.optJSONArray("newBankProofs") != null) {
                jsonObject.put("newBankProofs", null);
            }
            if (jsonObject.optJSONArray("newBankStmts") != null) {
                jsonObject.put("newBankStmts", null);
            }
            if (jsonObject.optJSONArray("newCollegeIds") != null) {
                jsonObject.put("newCollegeIds", null);
            }


        } catch (Exception e) {


        }
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

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

    //
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
//
//
//    public void switchtoProductPage() {
//        Intent in = new Intent(HomePage.this, ProductsPage.class);
//        in.putExtra("seller", sellerNme);
//        in.putExtra("product", productId);
//        in.putExtra("page", "api");
//        startActivity(in);
//    }
//
//    @Override
//    public void onClick(View v) {
//        Long time = Calendar.getInstance().getTimeInMillis() / 1000;
//
//        switch (v.getId()) {
////            case R.id.img11:
////                spin = "Mobiles";
////
////                productId = Splash.fkid1.get(spin).get("0");
////                sellerNme = Splash.sellers.get(spin).get("0");
////                switchtoProductPage();
////
////
////                break;
////
////            case R.id.img12:
////                spin = "Mobiles";
////                productId = Splash.fkid1.get(spin).get("1");
////                sellerNme = Splash.sellers.get(spin).get("1");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img13:
////                spin = "Mobiles";
////                productId = Splash.fkid1.get(spin).get("2");
////                sellerNme = Splash.sellers.get(spin).get("2");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img14:
////                spin = "Mobiles";
////
////                productId = Splash.fkid1.get(spin).get("3");
////                sellerNme = Splash.sellers.get(spin).get("3");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img15:
////                spin = "Mobiles";
////
////                productId = Splash.fkid1.get(spin).get("4");
////                sellerNme = Splash.sellers.get(spin).get("4");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img16:
////                spin = "Mobiles";
////
////                productId = Splash.fkid1.get(spin).get("5");
////                sellerNme = Splash.sellers.get(spin).get("5");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img17:
////                spin = "Mobiles";
////
////                productId = Splash.fkid1.get(spin).get("6");
////                sellerNme = Splash.sellers.get(spin).get("6");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img18:
////                spin = "Mobiles";
////
////                productId = Splash.fkid1.get(spin).get("7");
////                sellerNme = Splash.sellers.get(spin).get("7");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img19:
////                spin = "Mobiles";
////
////                productId = Splash.fkid1.get(spin).get("8");
////                sellerNme = Splash.sellers.get(spin).get("8");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img20:
////                spin = "Mobiles";
////
////                productId = Splash.fkid1.get(spin).get("9");
////                sellerNme = Splash.sellers.get(spin).get("9");
////                switchtoProductPage();
////
////                break;
//
////            case R.id.img21:
////                spin = "Electronics";
////
////                productId = Splash.fkid1.get(spin).get("0");
////                sellerNme = Splash.sellers.get(spin).get("0");
////                switchtoProductPage();
////
////
////                break;
////
////            case R.id.img22:
////                spin = "Electronics";
////                productId = Splash.fkid1.get(spin).get("1");
////                sellerNme = Splash.sellers.get(spin).get("1");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img23:
////                spin = "Electronics";
////                productId = Splash.fkid1.get(spin).get("2");
////                sellerNme = Splash.sellers.get(spin).get("2");
////                switchtoProductPage();
////
////
////                break;
////
////            case R.id.img24:
////                spin = "Electronics";
////
////                productId = Splash.fkid1.get(spin).get("3");
////                sellerNme = Splash.sellers.get(spin).get("3");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img25:
////                spin = "Electronics";
////
////                productId = Splash.fkid1.get(spin).get("4");
////                sellerNme = Splash.sellers.get(spin).get("4");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img26:
////                spin = "Electronics";
////
////                productId = Splash.fkid1.get(spin).get("5");
////                sellerNme = Splash.sellers.get(spin).get("5");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img27:
////                spin = "Electronics";
////
////                productId = Splash.fkid1.get(spin).get("6");
////                sellerNme = Splash.sellers.get(spin).get("6");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img28:
////                spin = "Electronics";
////
////                productId = Splash.fkid1.get(spin).get("7");
////                sellerNme = Splash.sellers.get(spin).get("7");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img29:
////                spin = "Electronics";
////
////                productId = Splash.fkid1.get(spin).get("8");
////                sellerNme = Splash.sellers.get(spin).get("8");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img30:
////                spin = "Electronics";
////
////                productId = Splash.fkid1.get(spin).get("9");
////                sellerNme = Splash.sellers.get(spin).get("9");
////                switchtoProductPage();
////
////                break;
//
//
////            case R.id.img31:
////                spin = "Computers&subCategory=Laptops";
////
////                productId = Splash.fkid1.get(spin).get("0");
////                sellerNme = Splash.sellers.get(spin).get("0");
////                switchtoProductPage();
////
////                break;
////            case R.id.img32:
////                spin = "Computers&subCategory=Laptops";
////
////                productId = Splash.fkid1.get(spin).get("1");
////                sellerNme = Splash.sellers.get(spin).get("1");
////                switchtoProductPage();
////
////                break;
////            case R.id.img33:
////                spin = "Computers&subCategory=Laptops";
////
////                productId = Splash.fkid1.get(spin).get("2");
////                sellerNme = Splash.sellers.get(spin).get("2");
////                switchtoProductPage();
////
////                break;
////            case R.id.img34:
////                spin = "Computers&subCategory=Laptops";
////
////                productId = Splash.fkid1.get(spin).get("3");
////                sellerNme = Splash.sellers.get(spin).get("3");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img35:
////                spin = "Computers&subCategory=Laptops";
////
////                productId = Splash.fkid1.get(spin).get("4");
////                sellerNme = Splash.sellers.get(spin).get("4");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img36:
////                spin = "Computers&subCategory=Laptops";
////
////                productId = Splash.fkid1.get(spin).get("5");
////                sellerNme = Splash.sellers.get(spin).get("5");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img37:
////                spin = "Computers&subCategory=Laptops";
////
////                productId = Splash.fkid1.get(spin).get("6");
////                sellerNme = Splash.sellers.get(spin).get("6");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img38:
////                spin = "Computers&subCategory=Laptops";
////
////                productId = Splash.fkid1.get(spin).get("7");
////                sellerNme = Splash.sellers.get(spin).get("7");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img39:
////                spin = "Computers&subCategory=Laptops";
////
////                productId = Splash.fkid1.get(spin).get("8");
////                sellerNme = Splash.sellers.get(spin).get("8");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img40:
////                spin = "Computers&subCategory=Laptops";
////
////                productId = Splash.fkid1.get(spin).get("9");
////                sellerNme = Splash.sellers.get(spin).get("9");
////                switchtoProductPage();
////
////                break;
//
////            case R.id.img41:
////                spin = "Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle";
////
////                productId = Splash.fkid1.get(spin).get("0");
////                sellerNme = Splash.sellers.get(spin).get("0");
////                switchtoProductPage();
////
////                break;
////            case R.id.img42:
////                spin = "Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle";
////
////                productId = Splash.fkid1.get(spin).get("1");
////                sellerNme = Splash.sellers.get(spin).get("1");
////                switchtoProductPage();
////
////                break;
////            case R.id.img43:
////                spin = "Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle";
////
////                productId = Splash.fkid1.get(spin).get("2");
////                sellerNme = Splash.sellers.get(spin).get("2");
////                switchtoProductPage();
////
////                break;
////            case R.id.img44:
////                spin = "Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle";
////
////                productId = Splash.fkid1.get(spin).get("3");
////                sellerNme = Splash.sellers.get(spin).get("3");
////                switchtoProductPage();
////
////                break;
////
////
////            case R.id.img45:
////                spin = "Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle";
////
////                productId = Splash.fkid1.get(spin).get("4");
////                sellerNme = Splash.sellers.get(spin).get("4");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img46:
////                spin = "Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle";
////
////                productId = Splash.fkid1.get(spin).get("5");
////                sellerNme = Splash.sellers.get(spin).get("5");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img47:
////                spin = "Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle";
////
////                productId = Splash.fkid1.get(spin).get("6");
////                sellerNme = Splash.sellers.get(spin).get("6");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img48:
////                spin = "Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle";
////
////                productId = Splash.fkid1.get(spin).get("7");
////                sellerNme = Splash.sellers.get(spin).get("7");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img49:
////                spin = "Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle";
////
////                productId = Splash.fkid1.get(spin).get("8");
////                sellerNme = Splash.sellers.get(spin).get("8");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img50:
////                spin = "Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle";
////
////                productId = Splash.fkid1.get(spin).get("9");
////                sellerNme = Splash.sellers.get(spin).get("9");
////                switchtoProductPage();
////
////                break;
////            case R.id.img51:
////                spin = "Health%20and%20Beauty";
////
////                productId = Splash.fkid1.get(spin).get("0");
////                sellerNme = Splash.sellers.get(spin).get("0");
////                switchtoProductPage();
////
////                break;
////            case R.id.img52:
////                spin = "Health%20and%20Beauty";
////
////                productId = Splash.fkid1.get(spin).get("1");
////                sellerNme = Splash.sellers.get(spin).get("1");
////                switchtoProductPage();
////
////                break;
////            case R.id.img53:
////                spin = "Health%20and%20Beauty";
////
////                productId = Splash.fkid1.get(spin).get("2");
////                sellerNme = Splash.sellers.get(spin).get("2");
////                switchtoProductPage();
////
////                break;
////            case R.id.img54:
////                spin = "Health%20and%20Beauty";
////
////                productId = Splash.fkid1.get(spin).get("3");
////                sellerNme = Splash.sellers.get(spin).get("3");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img55:
////                spin = "Health%20and%20Beauty";
////
////                productId = Splash.fkid1.get(spin).get("4");
////                sellerNme = Splash.sellers.get(spin).get("4");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img56:
////                spin = "Health%20and%20Beauty";
////
////                productId = Splash.fkid1.get(spin).get("5");
////                sellerNme = Splash.sellers.get(spin).get("5");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img57:
////                spin = "Health%20and%20Beauty";
////
////                productId = Splash.fkid1.get(spin).get("6");
////                sellerNme = Splash.sellers.get(spin).get("6");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img58:
////                spin = "Health%20and%20Beauty";
////
////                productId = Splash.fkid1.get(spin).get("7");
////                sellerNme = Splash.sellers.get(spin).get("7");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img59:
////                spin = "Health%20and%20Beauty";
////
////                productId = Splash.fkid1.get(spin).get("8");
////                sellerNme = Splash.sellers.get(spin).get("8");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img60:
////                spin = "Health%20and%20Beauty";
////
////                productId = Splash.fkid1.get(spin).get("9");
////                sellerNme = Splash.sellers.get(spin).get("9");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img61:
////                spin = "Footwear";
////
////                productId = Splash.fkid1.get(spin).get("0");
////                sellerNme = Splash.sellers.get(spin).get("0");
////                switchtoProductPage();
////
////                break;
////            case R.id.img62:
////                spin = "Footwear";
////
////                productId = Splash.fkid1.get(spin).get("1");
////                sellerNme = Splash.sellers.get(spin).get("1");
////                switchtoProductPage();
////
////                break;
////            case R.id.img63:
////                spin = "Footwear";
////
////                productId = Splash.fkid1.get(spin).get("2");
////                sellerNme = Splash.sellers.get(spin).get("2");
////                switchtoProductPage();
////
////                break;
////            case R.id.img64:
////                spin = "Footwear";
////
////                productId = Splash.fkid1.get(spin).get("3");
////                sellerNme = Splash.sellers.get(spin).get("3");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img65:
////                spin = "Footwear";
////
////                productId = Splash.fkid1.get(spin).get("4");
////                sellerNme = Splash.sellers.get(spin).get("4");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img66:
////                spin = "Footwear";
////
////                productId = Splash.fkid1.get(spin).get("5");
////                sellerNme = Splash.sellers.get(spin).get("5");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img67:
////                spin = "Footwear";
////
////                productId = Splash.fkid1.get(spin).get("6");
////                sellerNme = Splash.sellers.get(spin).get("6");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img68:
////                spin = "Footwear";
////
////                productId = Splash.fkid1.get(spin).get("7");
////                sellerNme = Splash.sellers.get(spin).get("7");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img69:
////                spin = "Footwear";
////
////                productId = Splash.fkid1.get(spin).get("8");
////                sellerNme = Splash.sellers.get(spin).get("8");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img70:
////                spin = "Footwear";
////
////                productId = Splash.fkid1.get(spin).get("9");
////                sellerNme = Splash.sellers.get(spin).get("9");
////                switchtoProductPage();
////
////                break;
//
////            case R.id.img01:
////                spin = "trending";
////
////                productId = Splash.fkid1.get(spin).get("0");
////                sellerNme = Splash.sellers.get(spin).get("0");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img02:
////                spin = "trending";
////
////                productId = Splash.fkid1.get(spin).get("1");
////                sellerNme = Splash.sellers.get(spin).get("1");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img03:
////                spin = "trending";
////
////                productId = Splash.fkid1.get(spin).get("2");
////                sellerNme = Splash.sellers.get(spin).get("2");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img04:
////                spin = "trending";
////
////                productId = Splash.fkid1.get(spin).get("3");
////                sellerNme = Splash.sellers.get(spin).get("3");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img05:
////                spin = "trending";
////
////                productId = Splash.fkid1.get(spin).get("4");
////                sellerNme = Splash.sellers.get(spin).get("4");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img06:
////                spin = "trending";
////
////                productId = Splash.fkid1.get(spin).get("5");
////                sellerNme = Splash.sellers.get(spin).get("5");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img07:
////                spin = "trending";
////
////                productId = Splash.fkid1.get(spin).get("6");
////                sellerNme = Splash.sellers.get(spin).get("6");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img08:
////                spin = "trending";
////
////                productId = Splash.fkid1.get(spin).get("7");
////                sellerNme = Splash.sellers.get(spin).get("7");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img09:
////                spin = "trending";
////
////                productId = Splash.fkid1.get(spin).get("8");
////                sellerNme = Splash.sellers.get(spin).get("8");
////                switchtoProductPage();
////
////                break;
////
////            case R.id.img10:
////                spin = "trending";
////
////                productId = Splash.fkid1.get(spin).get("9");
////                sellerNme = Splash.sellers.get(spin).get("9");
////                switchtoProductPage();
////
////                break;
//
//
//            default:
//                break;
//
//        }
//
//    }


    private class trending extends
            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            //            spinner.setVisibility(View.VISIBLE);

        }


        @Override
        protected String doInBackground(String... data) {

            String urldisplay = data[0];

            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: get
                String url = "http://54.255.147.43:80/api/product/trending?category=" + urldisplay;

                String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjY1M2M2YTUwZTQzNzgyNjc0M2YyNjYiLCJuYW1lIjoiYnVkZHkgYXBpIGFkbWluIiwidXNlcm5hbWUiOiJidWRkeWFwaWFkbWluIiwicGFzc3dvcmQiOiJtZW1vbmdvc2gxIiwiZW1haWwiOiJjYXJlQGhlbGxvYnVkZHkuaW4iLCJpYXQiOjE0NTY3MjY1NzMsImV4cCI6MTQ1Njc2MjU3M30.98mQFcYm5Uf3Fd7ZNPD-OwMIfObu7vfoq9zNtCCLfyI";
                // payload.put("action", details.get("action"));


                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);

                // url2=getApplicationContext().getString(R.string.server)+"api/login/verifyotp";
                if (urldisplay.equals("Computers&subCategory=Laptops"))
                    urldisplay = "Laptops";

                else if (urldisplay.equals("Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle"))
                    urldisplay = "apparels";
                else if (urldisplay.equals("Health%20and%20Beauty"))
                    urldisplay = "homeandbeauty";
                HttpGet httppost = new HttpGet(url);
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
                    if (resp.getString("status").equals("success")) {
                        JSONArray data1 = new JSONArray(resp.getString("data"));
                        int lenght = data1.length();
                        for (int j = 0; j < lenght; j++) {
                            JSONObject js = data1.getJSONObject(j);
                            String id = js.getString("title");
                            String mrp = js.getString("mrp");
                            String seller = js.getString("seller");
                            String fkid = js.getString("fkProductId");
                            String selling_price = js.getString("sellingPrice");
                            JSONObject img = new JSONObject(js.getString("imgUrls"));
                            String imgurl = img.getString("200x200");

                            //                           image.get(urldisplay).put(String.valueOf(j), imgurl);
                            //                            mrp1.get(urldisplay).put(String.valueOf(j), mrp);
                            //                            title.get(urldisplay).put(String.valueOf(j), id);
                            //                            fkid1.get(urldisplay).put(String.valueOf(j), fkid);
                            //                            selling.get(urldisplay).put(String.valueOf(j),selling_price);
                            //                            sellers.get(urldisplay).put(String.valueOf(j), seller);

                            String p = id;
                        }
                        return "win";
                        //versioncode=data1.getString("version_code");
                        //return versioncode;
                    } else
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
            }
            //else

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void get() {
        Intent incoming;
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
        //  send();
    }

    private void send() {

        {

            if (formstatus.equals("declined")) {

                Intent in = new Intent(HomePage.this, Formempty.class);

                // Intent in = new Intent(MainActivity.this, Inviteform.class);
                in.putExtra("Name", name);
                in.putExtra("fbid", fbid);
                in.putExtra("Rej", rejectionReason);
                in.putExtra("Email", email);
                in.putExtra("Form", formstatus);
                in.putExtra("UniC", uniqueCode);
                Splash.checkNot = 1;
                // clickpaste();
                startActivity(in);
                overridePendingTransition(0, 0);

            }


            if (formstatus.equals("saved")) {

                Intent in = new Intent(HomePage.this, Formempty.class);
                // Intent in = new Intent(MainActivity.this, Inviteform.class);
                //    finish();
                in.putExtra("Name", name);
                in.putExtra("fbid", fbid);
                in.putExtra("Email", email);
                in.putExtra("Form", formstatus);
                in.putExtra("UniC", uniqueCode);
                Splash.checkNot = 1;

                // clickpaste();
                startActivity(in);
                overridePendingTransition(0, 0);
            } else if (formstatus.equals("submitted")) {
                Splash.checkNot = 1;
                //clickpaste();
                Intent in = new Intent(HomePage.this, Formstatus.class);
                in.putExtra("screen_no", screen_no);
                try {
                    in.putExtra("VeriDate", verificationdate);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }


                //  finish();
                in.putExtra("Name", name);
                in.putExtra("fbid", fbid);
                in.putExtra("Email", email);
                in.putExtra("Form", formstatus);
                in.putExtra("UniC", uniqueCode);
                startActivity(in);
                overridePendingTransition(0, 0);
            }
            if (formstatus.equals("flashApproved")) {
                Splash.checkNot = 1;

                // clickpaste();
                Intent in = new Intent(HomePage.this, Approved.class);
                //   finish();
                // Intent in = new Intent(MainActivity.this, Inviteform.class);
                in.putExtra("Name", name);
                in.putExtra("fbid", fbid);
                in.putExtra("Email", email);//  in.putExtra("Credits",creditLimit);
                in.putExtra("Form", formstatus);
                in.putExtra("Credits", creditLimit);
                in.putExtra("UniC", uniqueCode);
                startActivity(in);
                overridePendingTransition(0, 0);
            }
            if (formstatus.equals("approved")) {
                Splash.checkNot = 1;
                //  clickpaste();
                Intent in = new Intent(HomePage.this, Approved.class);
                //   finish();
                // Intent in = new Intent(MainActivity.this, Inviteform.class);
                in.putExtra("Name", name);
                in.putExtra("fbid", fbid);
                in.putExtra("Email", email);//  in.putExtra("Credits",creditLimit);
                in.putExtra("Form", formstatus);
                in.putExtra("Credits", creditLimit);
                in.putExtra("UniC", uniqueCode);
                startActivity(in);
                overridePendingTransition(0, 0);
            } else if (formstatus.equals("empty")) {
                Splash.checkNot = 1;


                // clickpaste();
                //                    Intent in = new Intent(MainActivity.this, Inviteform    .class);

                Intent in = new Intent(HomePage.this, Formempty.class);
                //    finish();
                in.putExtra("Name", name);
                in.putExtra("Email", email);
                in.putExtra("fbid", fbid);
                in.putExtra("Form", formstatus);
                in.putExtra("UniC", uniqueCode);
                startActivity(in);
                overridePendingTransition(0, 0);
            }
        }


        noti.setVisibility(View.GONE);
    }

//

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

//    private void populateTrendingRow() {
//
//        TextView price1 = (TextView) findViewById(R.id.title01);
//        TextView price2 = (TextView) findViewById(R.id.title02);
//        TextView price3 = (TextView) findViewById(R.id.title03);
//        TextView price4 = (TextView) findViewById(R.id.title04);
//        TextView price5 = (TextView) findViewById(R.id.title05);
//        TextView price6 = (TextView) findViewById(R.id.title06);
//        TextView price7 = (TextView) findViewById(R.id.title07);
//        TextView price8 = (TextView) findViewById(R.id.title08);
//        TextView price9 = (TextView) findViewById(R.id.title09);
//        TextView price10 = (TextView) findViewById(R.id.title10);
//
//        TextView title1 = (TextView) findViewById(R.id.titlexxx01);
//        TextView title2 = (TextView) findViewById(R.id.titlexxx02);
//        TextView title3 = (TextView) findViewById(R.id.titlexxx03);
//        TextView title4 = (TextView) findViewById(R.id.titlexxx04);
//        TextView title5 = (TextView) findViewById(R.id.titlexxx05);
//        TextView title6 = (TextView) findViewById(R.id.titlexxx06);
//        TextView title7 = (TextView) findViewById(R.id.titlexxx07);
//        TextView title8 = (TextView) findViewById(R.id.titlexxx08);
//        TextView title9 = (TextView) findViewById(R.id.titlexxx09);
//        TextView title10 = (TextView) findViewById(R.id.titlexxx10);
//        //TextView title4 = (TextView) findViewById(R.id.titlexxx04);
//
//
//        ImageView img1 = (ImageView) findViewById(R.id.img01);
//        ImageView img2 = (ImageView) findViewById(R.id.img02);
//        ImageView img3 = (ImageView) findViewById(R.id.img03);
//        ImageView img4 = (ImageView) findViewById(R.id.img04);
//        ImageView img5 = (ImageView) findViewById(R.id.img05);
//        ImageView img6 = (ImageView) findViewById(R.id.img06);
//        ImageView img7 = (ImageView) findViewById(R.id.img07);
//        ImageView img8 = (ImageView) findViewById(R.id.img08);
//        ImageView img9 = (ImageView) findViewById(R.id.img09);
//        ImageView img10 = (ImageView) findViewById(R.id.img10);
//
//
//        ImageView brand1 = (ImageView) findViewById(R.id.brand01);
//        ImageView brand2 = (ImageView) findViewById(R.id.brand02);
//        ImageView brand3 = (ImageView) findViewById(R.id.brand03);
//        ImageView brand4 = (ImageView) findViewById(R.id.brand04);
//        ImageView brand5 = (ImageView) findViewById(R.id.brand05);
//        ImageView brand6 = (ImageView) findViewById(R.id.brand06);
//        ImageView brand7 = (ImageView) findViewById(R.id.brand07);
//        ImageView brand8 = (ImageView) findViewById(R.id.brand08);
//        ImageView brand9 = (ImageView) findViewById(R.id.brand09);
//        ImageView brand10 = (ImageView) findViewById(R.id.brand10);
//        //ImageView brand4 = (ImageView) findViewById(R.id.brand04);
//
//
//        if (Splash.sellers.get(spin).get("0").equals("flipkart"))
//            brand1.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("amazon"))
//            brand1.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("paytm"))
//            brand1.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("snapdeal"))
//            brand1.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("flipkart"))
//            brand2.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("amazon"))
//            brand2.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("paytm"))
//            brand2.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("snapdeal"))
//            brand2.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("flipkart"))
//            brand3.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("amazon"))
//            brand3.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("paytm"))
//            brand3.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("snapdeal"))
//            brand3.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("flipkart"))
//            brand4.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("amazon"))
//            brand4.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("paytm"))
//            brand4.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("snapdeal"))
//            brand4.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("flipkart"))
//            brand5.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("amazon"))
//            brand5.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("paytm"))
//            brand5.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("snapdeal"))
//            brand5.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("flipkart"))
//            brand6.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("amazon"))
//            brand6.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("paytm"))
//            brand6.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("snapdeal"))
//            brand6.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("flipkart"))
//            brand7.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("amazon"))
//            brand7.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("paytm"))
//            brand7.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("snapdeal"))
//            brand7.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("flipkart"))
//            brand8.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("amazon"))
//            brand8.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("paytm"))
//            brand8.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("snapdeal"))
//            brand8.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("flipkart"))
//            brand9.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("amazon"))
//            brand9.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("paytm"))
//            brand9.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("snapdeal"))
//            brand9.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("flipkart"))
//            brand10.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("amazon"))
//            brand10.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("paytm"))
//            brand10.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("snapdeal"))
//            brand10.setImageResource(R.drawable.sdeal_fav1x);
//
//
////            card1 = (ImageView) findViewById(R.id.img1);
////            card2 = (ImageView) findViewById(R.id.imgx);
////            card3 = (ImageView) findViewById(R.id.img2);
////            card4 = (ImageView) findViewById(R.id.img2x);
//
//        title1.setText(Splash.title.get(spin).get("0"));
//        title2.setText(Splash.title.get(spin).get("1"));
//        title3.setText(Splash.title.get(spin).get("2"));
//        title4.setText(Splash.title.get(spin).get("3"));
//        title5.setText(Splash.title.get(spin).get("4"));
//        title6.setText(Splash.title.get(spin).get("5"));
//        title7.setText(Splash.title.get(spin).get("6"));
//        title8.setText(Splash.title.get(spin).get("7"));
//        title9.setText(Splash.title.get(spin).get("8"));
//        title10.setText(Splash.title.get(spin).get("9"));
//
//
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("0"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img1);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("1"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img2);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("2"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img3);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("3"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img4);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("4"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img5);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("5"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img6);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("6"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img7);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("7"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img8);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("8"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img9);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("9"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img10);
//
//
//        Double princ1 = Double.parseDouble(Splash.selling.get(spin).get("0"));
//
//        int price01 = princ1.intValue();
//        Double emi01 = show(Splash.subCategory.get(spin).get("0"), Splash.category.get(spin).get("0"), Splash.brand.get(spin).get("0"), price01);
//        if (emi01.intValue() < 200)
//            emi01 = 200.0;
//        price1.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi01.intValue()) + " per month");
//        //emi2
//
//
//        Double princ2 = Double.parseDouble(Splash.selling.get(spin).get("1"));
//
//        int price02 = princ2.intValue();
//        Double emi02 = show(Splash.subCategory.get(spin).get("1"), Splash.category.get(spin).get("1"), Splash.brand.get(spin).get("1"), price02);
//        if (emi02.intValue() < 200)
//            emi02 = 200.0;
//        price2.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi02.intValue()) + " per month");
//
//
//        Double princ3 = Double.parseDouble(Splash.selling.get(spin).get("2"));
//        int price03 = princ3.intValue();
//        Double emi03 = show(Splash.subCategory.get(spin).get("2"), Splash.category.get(spin).get("2"), Splash.brand.get(spin).get("2"), price03);
//        if (emi03.intValue() < 200)
//            emi03 = 200.0;
//        price3.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi03.intValue()) + " per month");
//
//
//        //emi4
//
//        Double princ4 = Double.parseDouble(Splash.selling.get(spin).get("3"));
//        int price04 = princ4.intValue();
//        Double emi04 = show(Splash.subCategory.get(spin).get("3"), Splash.category.get(spin).get("3"), Splash.brand.get(spin).get("3"), price04);
//        if (emi04.intValue() < 200)
//            emi04 = 200.0;
//        price4.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi04.intValue()) + " per month");
//
//
//        Double princ5 = Double.parseDouble(Splash.selling.get(spin).get("4"));
//        int price05 = princ5.intValue();
//        Double emi05 = show(Splash.subCategory.get(spin).get("4"), Splash.category.get(spin).get("4"), Splash.brand.get(spin).get("4"), price05);
//        if (emi05.intValue() < 200)
//            emi05 = 200.0;
//        price5.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi05.intValue()) + " per month");
//
//
//        Double princ6 = Double.parseDouble(Splash.selling.get(spin).get("5"));
//        int price06 = princ6.intValue();
//        Double emi06 = show(Splash.subCategory.get(spin).get("5"), Splash.category.get(spin).get("5"), Splash.brand.get(spin).get("5"), price06);
//        if (emi06.intValue() < 200)
//            emi06 = 200.0;
//
//        price6.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi06.intValue()) + " per month");
//
//
//        Double princ7 = Double.parseDouble(Splash.selling.get(spin).get("6"));
//        int price07 = princ7.intValue();
//        Double emi07 = show(Splash.subCategory.get(spin).get("6"), Splash.category.get(spin).get("6"), Splash.brand.get(spin).get("6"), price07);
//        if (emi07.intValue() < 200)
//            emi07 = 200.0;
//        price7.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi07.intValue()) + " per month");
//
//
//        Double princ8 = Double.parseDouble(Splash.selling.get(spin).get("7"));
//        int price08 = princ8.intValue();
//        Double emi08 = show(Splash.subCategory.get(spin).get("7"), Splash.category.get(spin).get("7"), Splash.brand.get(spin).get("7"), price08);
//        if (emi08.intValue() < 200)
//            emi08 = 200.0;
//        price8.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi08.intValue()) + " per month");
//
//
//        Double princ9 = Double.parseDouble(Splash.selling.get(spin).get("8"));
//        int price09 = princ9.intValue();
//        Double emi09 = show(Splash.subCategory.get(spin).get("8"), Splash.category.get(spin).get("8"), Splash.brand.get(spin).get("8"), price09);
//        if (emi09.intValue() < 200)
//            emi09 = 200.0;
//        price9.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi09.intValue()) + " per month");
//
//
//        Double princ10 = Double.parseDouble(Splash.selling.get(spin).get("9"));
//        int price00 = princ10.intValue();
//        Double emi10 = show(Splash.subCategory.get(spin).get("9"), Splash.category.get(spin).get("9"), Splash.brand.get(spin).get("9"), price00);
//        if (emi10.intValue() < 200)
//            emi10 = 200.0;
//        price10.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi10.intValue()) + " per month");
//
//
//    }


//    private void populateFirstRow() {
//
//
//        TextView price1 = (TextView) findViewById(R.id.title11);
//        TextView price2 = (TextView) findViewById(R.id.title12);
//        TextView price3 = (TextView) findViewById(R.id.title13);
//        TextView price4 = (TextView) findViewById(R.id.title14);
//        TextView price5 = (TextView) findViewById(R.id.title15);
//        TextView price6 = (TextView) findViewById(R.id.title16);
//        TextView price7 = (TextView) findViewById(R.id.title17);
//        TextView price8 = (TextView) findViewById(R.id.title18);
//        TextView price9 = (TextView) findViewById(R.id.title19);
//        TextView price10 = (TextView) findViewById(R.id.title20);
//
//        TextView title1 = (TextView) findViewById(R.id.titlexxx11);
//        TextView title2 = (TextView) findViewById(R.id.titlexxx12);
//        TextView title3 = (TextView) findViewById(R.id.titlexxx13);
//        TextView title4 = (TextView) findViewById(R.id.titlexxx14);
//        TextView title5 = (TextView) findViewById(R.id.titlexxx15);
//        TextView title6 = (TextView) findViewById(R.id.titlexxx16);
//        TextView title7 = (TextView) findViewById(R.id.titlexxx17);
//        TextView title8 = (TextView) findViewById(R.id.titlexxx18);
//        TextView title9 = (TextView) findViewById(R.id.titlexxx19);
//        TextView title10 = (TextView) findViewById(R.id.titlexxx20);
//
//
//        ImageView img1 = (ImageView) findViewById(R.id.img11);
//        ImageView img2 = (ImageView) findViewById(R.id.img12);
//        ImageView img3 = (ImageView) findViewById(R.id.img13);
//        ImageView img4 = (ImageView) findViewById(R.id.img14);
//        ImageView img5 = (ImageView) findViewById(R.id.img15);
//        ImageView img6 = (ImageView) findViewById(R.id.img16);
//        ImageView img7 = (ImageView) findViewById(R.id.img17);
//        ImageView img8 = (ImageView) findViewById(R.id.img18);
//        ImageView img9 = (ImageView) findViewById(R.id.img19);
//        ImageView img10 = (ImageView) findViewById(R.id.img20);
//
//        ImageView brand1 = (ImageView) findViewById(R.id.brand11);
//        ImageView brand2 = (ImageView) findViewById(R.id.brand12);
//        ImageView brand3 = (ImageView) findViewById(R.id.brand13);
//        ImageView brand4 = (ImageView) findViewById(R.id.brand14);
//        ImageView brand5 = (ImageView) findViewById(R.id.brand15);
//        ImageView brand6 = (ImageView) findViewById(R.id.brand16);
//        ImageView brand7 = (ImageView) findViewById(R.id.brand17);
//        ImageView brand8 = (ImageView) findViewById(R.id.brand18);
//        ImageView brand9 = (ImageView) findViewById(R.id.brand19);
//        ImageView brand10 = (ImageView) findViewById(R.id.brand20);
//        if (Splash.sellers.get(spin).get("0").equals("flipkart"))
//            brand1.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("amazon"))
//            brand1.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("paytm"))
//            brand1.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("snapdeal"))
//            brand1.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("flipkart"))
//            brand2.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("amazon"))
//            brand2.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("paytm"))
//            brand2.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("snapdeal"))
//            brand2.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("flipkart"))
//            brand3.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("amazon"))
//            brand3.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("paytm"))
//            brand3.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("snapdeal"))
//            brand3.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("flipkart"))
//            brand4.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("amazon"))
//            brand4.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("paytm"))
//            brand4.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("snapdeal"))
//            brand4.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("flipkart"))
//            brand5.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("amazon"))
//            brand5.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("paytm"))
//            brand5.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("snapdeal"))
//            brand5.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("flipkart"))
//            brand6.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("amazon"))
//            brand6.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("paytm"))
//            brand6.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("snapdeal"))
//            brand6.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("flipkart"))
//            brand7.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("amazon"))
//            brand7.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("paytm"))
//            brand7.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("snapdeal"))
//            brand7.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("flipkart"))
//            brand8.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("amazon"))
//            brand8.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("paytm"))
//            brand8.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("snapdeal"))
//            brand8.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("flipkart"))
//            brand9.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("amazon"))
//            brand9.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("paytm"))
//            brand9.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("snapdeal"))
//            brand9.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("flipkart"))
//            brand10.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("amazon"))
//            brand10.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("paytm"))
//            brand10.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("snapdeal"))
//            brand10.setImageResource(R.drawable.sdeal_fav1x);
//
//
////            card1 = (ImageView) findViewById(R.id.img1);
////            card2 = (ImageView) findViewById(R.id.imgx);
////            card3 = (ImageView) findViewById(R.id.img2);
////            card4 = (ImageView) findViewById(R.id.img2x);
//
//        title1.setText(Splash.title.get(spin).get("0"));
//        title2.setText(Splash.title.get(spin).get("1"));
//        title3.setText(Splash.title.get(spin).get("2"));
//        title4.setText(Splash.title.get(spin).get("3"));
//        title5.setText(Splash.title.get(spin).get("4"));
//        title6.setText(Splash.title.get(spin).get("5"));
//        title7.setText(Splash.title.get(spin).get("6"));
//        title8.setText(Splash.title.get(spin).get("7"));
//        title9.setText(Splash.title.get(spin).get("8"));
//        title10.setText(Splash.title.get(spin).get("9"));
//
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("0"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img1);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("1"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img2);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("2"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img3);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("3"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img4);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("4"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img5);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("5"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img6);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("6"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img7);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("7"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img8);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("8"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img9);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("9"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img10);
//
//
//        Double princ1 = Double.parseDouble(Splash.selling.get(spin).get("0"));
//
//        int price01 = princ1.intValue();
//        Double emi01 = show(Splash.subCategory.get(spin).get("0"), Splash.category.get(spin).get("0"), Splash.brand.get(spin).get("0"), price01);
//        if (emi01.intValue() < 200)
//            emi01 = 200.0;
//        price1.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi01.intValue()) + " per month");
//        //emi2
//
//
//        Double princ2 = Double.parseDouble(Splash.selling.get(spin).get("1"));
//
//        int price02 = princ2.intValue();
//        Double emi02 = show(Splash.subCategory.get(spin).get("1"), Splash.category.get(spin).get("1"), Splash.brand.get(spin).get("1"), price02);
//        if (emi02.intValue() < 200)
//            emi02 = 200.0;
//        price2.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi02.intValue()) + " per month");
//
//
//        Double princ3 = Double.parseDouble(Splash.selling.get(spin).get("2"));
//        int price03 = princ3.intValue();
//        Double emi03 = show(Splash.subCategory.get(spin).get("2"), Splash.category.get(spin).get("2"), Splash.brand.get(spin).get("2"), price03);
//        if (emi03.intValue() < 200)
//            emi03 = 200.0;
//        price3.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi03.intValue()) + " per month");
////
//
//        //emi4
//
//        Double princ4 = Double.parseDouble(Splash.selling.get(spin).get("3"));
//        int price04 = princ4.intValue();
//        Double emi04 = show(Splash.subCategory.get(spin).get("3"), Splash.category.get(spin).get("3"), Splash.brand.get(spin).get("3"), price04);
//        if (emi04.intValue() < 200)
//            emi04 = 200.0;
//        price4.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi04.intValue()) + " per month");
//
//
//        Double princ5 = Double.parseDouble(Splash.selling.get(spin).get("4"));
//        int price05 = princ5.intValue();
//        Double emi05 = show(Splash.subCategory.get(spin).get("4"), Splash.category.get(spin).get("4"), Splash.brand.get(spin).get("4"), price05);
//        if (emi05.intValue() < 200)
//            emi05 = 200.0;
//        price5.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi05.intValue()) + " per month");
//
//
//        Double princ6 = Double.parseDouble(Splash.selling.get(spin).get("5"));
//        int price06 = princ6.intValue();
//        Double emi06 = show(Splash.subCategory.get(spin).get("5"), Splash.category.get(spin).get("5"), Splash.brand.get(spin).get("5"), price06);
//        if (emi06.intValue() < 200)
//            emi06 = 200.0;
//        price6.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi06.intValue()) + " per month");
//
//
//        Double princ7 = Double.parseDouble(Splash.selling.get(spin).get("6"));
//        int price07 = princ7.intValue();
//        Double emi07 = show(Splash.subCategory.get(spin).get("6"), Splash.category.get(spin).get("6"), Splash.brand.get(spin).get("6"), price07);
//        if (emi07.intValue() < 200)
//            emi07 = 200.0;
//        price7.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi07.intValue()) + " per month");
//
//
//        Double princ8 = Double.parseDouble(Splash.selling.get(spin).get("7"));
//        int price08 = princ8.intValue();
//        Double emi08 = show(Splash.subCategory.get(spin).get("7"), Splash.category.get(spin).get("7"), Splash.brand.get(spin).get("7"), price08);
//        if (emi08.intValue() < 200)
//            emi08 = 200.0;
//        price8.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi08.intValue()) + " per month");
//
//
//        Double princ9 = Double.parseDouble(Splash.selling.get(spin).get("8"));
//        int price09 = princ9.intValue();
//        Double emi09 = show(Splash.subCategory.get(spin).get("8"), Splash.category.get(spin).get("8"), Splash.brand.get(spin).get("8"), price09);
//        if (emi09.intValue() < 200)
//            emi09 = 200.0;
//        price9.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi09.intValue()) + " per month");
//
//
//        Double princ10 = Double.parseDouble(Splash.selling.get(spin).get("9"));
//        int price00 = princ10.intValue();
//        Double emi10 = show(Splash.subCategory.get(spin).get("9"), Splash.category.get(spin).get("9"), Splash.brand.get(spin).get("9"), price00);
//        if (emi10.intValue() < 200)
//            emi10 = 200.0;
//        price10.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi10.intValue()) + " per month");
//
//
//    }

//    private void populateSecondRow() {
//
//
//        TextView price1 = (TextView) findViewById(R.id.title21);
//        TextView price2 = (TextView) findViewById(R.id.title22);
//        TextView price3 = (TextView) findViewById(R.id.title23);
//        TextView price4 = (TextView) findViewById(R.id.title24);
//        TextView price5 = (TextView) findViewById(R.id.title25);
//        TextView price6 = (TextView) findViewById(R.id.title26);
//        TextView price7 = (TextView) findViewById(R.id.title27);
//        TextView price8 = (TextView) findViewById(R.id.title28);
//        TextView price9 = (TextView) findViewById(R.id.title29);
//        TextView price10 = (TextView) findViewById(R.id.title30);
//
//        TextView title1 = (TextView) findViewById(R.id.titlexxx21);
//        TextView title2 = (TextView) findViewById(R.id.titlexxx22);
//        TextView title3 = (TextView) findViewById(R.id.titlexxx23);
//        TextView title4 = (TextView) findViewById(R.id.titlexxx24);
//        TextView title5 = (TextView) findViewById(R.id.titlexxx25);
//        TextView title6 = (TextView) findViewById(R.id.titlexxx26);
//        TextView title7 = (TextView) findViewById(R.id.titlexxx27);
//        TextView title8 = (TextView) findViewById(R.id.titlexxx28);
//        TextView title9 = (TextView) findViewById(R.id.titlexxx29);
//        TextView title10 = (TextView) findViewById(R.id.titlexxx30);
//
//
//        ImageView img1 = (ImageView) findViewById(R.id.img21);
//        ImageView img2 = (ImageView) findViewById(R.id.img22);
//        ImageView img3 = (ImageView) findViewById(R.id.img23);
//        ImageView img4 = (ImageView) findViewById(R.id.img24);
//        ImageView img5 = (ImageView) findViewById(R.id.img25);
//        ImageView img6 = (ImageView) findViewById(R.id.img26);
//        ImageView img7 = (ImageView) findViewById(R.id.img27);
//        ImageView img8 = (ImageView) findViewById(R.id.img28);
//        ImageView img9 = (ImageView) findViewById(R.id.img29);
//        ImageView img10 = (ImageView) findViewById(R.id.img30);
//
//        ImageView brand1 = (ImageView) findViewById(R.id.brand21);
//        ImageView brand2 = (ImageView) findViewById(R.id.brand22);
//        ImageView brand3 = (ImageView) findViewById(R.id.brand23);
//        ImageView brand4 = (ImageView) findViewById(R.id.brand24);
//        ImageView brand5 = (ImageView) findViewById(R.id.brand25);
//        ImageView brand6 = (ImageView) findViewById(R.id.brand26);
//        ImageView brand7 = (ImageView) findViewById(R.id.brand27);
//        ImageView brand8 = (ImageView) findViewById(R.id.brand28);
//        ImageView brand9 = (ImageView) findViewById(R.id.brand29);
//        ImageView brand10 = (ImageView) findViewById(R.id.brand30);
//        if (Splash.sellers.get(spin).get("0").equals("flipkart"))
//            brand1.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("amazon"))
//            brand1.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("paytm"))
//            brand1.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("snapdeal"))
//            brand1.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("flipkart"))
//            brand2.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("amazon"))
//            brand2.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("paytm"))
//            brand2.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("snapdeal"))
//            brand2.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("flipkart"))
//            brand3.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("amazon"))
//            brand3.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("paytm"))
//            brand3.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("snapdeal"))
//            brand3.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("flipkart"))
//            brand4.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("amazon"))
//            brand4.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("paytm"))
//            brand4.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("snapdeal"))
//            brand4.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("flipkart"))
//            brand5.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("amazon"))
//            brand5.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("paytm"))
//            brand5.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("snapdeal"))
//            brand5.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("flipkart"))
//            brand6.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("amazon"))
//            brand6.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("paytm"))
//            brand6.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("snapdeal"))
//            brand6.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("flipkart"))
//            brand7.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("amazon"))
//            brand7.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("paytm"))
//            brand7.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("snapdeal"))
//            brand7.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("flipkart"))
//            brand8.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("amazon"))
//            brand8.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("paytm"))
//            brand8.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("snapdeal"))
//            brand8.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("flipkart"))
//            brand9.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("amazon"))
//            brand9.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("paytm"))
//            brand9.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("snapdeal"))
//            brand9.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("flipkart"))
//            brand10.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("amazon"))
//            brand10.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("paytm"))
//            brand10.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("snapdeal"))
//            brand10.setImageResource(R.drawable.sdeal_fav1x);
//
//
////            card1 = (ImageView) findViewById(R.id.img1);
////            card2 = (ImageView) findViewById(R.id.imgx);
////            card3 = (ImageView) findViewById(R.id.img2);
////            card4 = (ImageView) findViewById(R.id.img2x);
//
//        title1.setText(Splash.title.get(spin).get("0"));
//        title2.setText(Splash.title.get(spin).get("1"));
//        title3.setText(Splash.title.get(spin).get("2"));
//        title4.setText(Splash.title.get(spin).get("3"));
//        title5.setText(Splash.title.get(spin).get("4"));
//        title6.setText(Splash.title.get(spin).get("5"));
//        title7.setText(Splash.title.get(spin).get("6"));
//        title8.setText(Splash.title.get(spin).get("7"));
//        title9.setText(Splash.title.get(spin).get("8"));
//        title10.setText(Splash.title.get(spin).get("9"));
//
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("0"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img1);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("1"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img2);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("2"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img3);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("3"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img4);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("4"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img5);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("5"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img6);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("6"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img7);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("7"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img8);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("8"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img9);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("9"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img10);
//
//        Double princ1 = Double.parseDouble(Splash.selling.get(spin).get("0"));
//
//        int price01 = princ1.intValue();
//        Double emi01 = show(Splash.subCategory.get(spin).get("0"), Splash.category.get(spin).get("0"), Splash.brand.get(spin).get("0"), price01);
//        if (emi01.intValue() < 200)
//            emi01 = 200.0;
//        price1.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi01.intValue()) + " per month");
//        //emi2
//
//
//        Double princ2 = Double.parseDouble(Splash.selling.get(spin).get("1"));
//
//        int price02 = princ2.intValue();
//        Double emi02 = show(Splash.subCategory.get(spin).get("1"), Splash.category.get(spin).get("1"), Splash.brand.get(spin).get("1"), price02);
//        if (emi02.intValue() < 200)
//            emi02 = 200.0;
//        price2.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi02.intValue()) + " per month");
//
//
//        Double princ3 = Double.parseDouble(Splash.selling.get(spin).get("2"));
//        int price03 = princ3.intValue();
//        Double emi03 = show(Splash.subCategory.get(spin).get("2"), Splash.category.get(spin).get("2"), Splash.brand.get(spin).get("2"), price03);
//        if (emi03.intValue() < 200)
//            emi03 = 200.0;
//        price3.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi03.intValue()) + " per month");
//
//
//        //emi4
//
//        Double princ4 = Double.parseDouble(Splash.selling.get(spin).get("3"));
//        int price04 = princ4.intValue();
//        Double emi04 = show(Splash.subCategory.get(spin).get("3"), Splash.category.get(spin).get("3"), Splash.brand.get(spin).get("3"), price04);
//        if (emi04.intValue() < 200)
//            emi04 = 200.0;
//        price4.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi04.intValue()) + " per month");
//
//
//        Double princ5 = Double.parseDouble(Splash.selling.get(spin).get("4"));
//        int price05 = princ5.intValue();
//        Double emi05 = show(Splash.subCategory.get(spin).get("4"), Splash.category.get(spin).get("4"), Splash.brand.get(spin).get("4"), price05);
//        // price5.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi05.intValue()) + " per month");
//        if (emi05.intValue() < 200)
//            emi05 = 200.0;
//        price5.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi05.intValue()) + " per month");
//
//
//        Double princ6 = Double.parseDouble(Splash.selling.get(spin).get("5"));
//        int price06 = princ6.intValue();
//        Double emi06 = show(Splash.subCategory.get(spin).get("5"), Splash.category.get(spin).get("5"), Splash.brand.get(spin).get("5"), price06);
//        //price6.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi06.intValue()) + " per month");
//        if (emi06.intValue() < 200)
//            emi06 = 200.0;
//        price6.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi06.intValue()) + " per month");
//
//
//        Double princ7 = Double.parseDouble(Splash.selling.get(spin).get("6"));
//        int price07 = princ7.intValue();
//        Double emi07 = show(Splash.subCategory.get(spin).get("6"), Splash.category.get(spin).get("6"), Splash.brand.get(spin).get("6"), price07);
//        if (emi07.intValue() < 200)
//            emi07 = 200.0;
//        price7.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi07.intValue()) + " per month");
//
//
//        Double princ8 = Double.parseDouble(Splash.selling.get(spin).get("7"));
//        int price08 = princ8.intValue();
//        Double emi08 = show(Splash.subCategory.get(spin).get("7"), Splash.category.get(spin).get("7"), Splash.brand.get(spin).get("7"), price08);
//        if (emi08.intValue() < 200)
//            emi08 = 200.0;
//        price8.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi08.intValue()) + " per month");
//
//
//        Double princ9 = Double.parseDouble(Splash.selling.get(spin).get("8"));
//        int price09 = princ9.intValue();
//        Double emi09 = show(Splash.subCategory.get(spin).get("8"), Splash.category.get(spin).get("8"), Splash.brand.get(spin).get("8"), price09);
//        if (emi09.intValue() < 200)
//            emi09 = 200.0;
//        price9.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi09.intValue()) + " per month");
//
//
//        Double princ10 = Double.parseDouble(Splash.selling.get(spin).get("9"));
//        int price00 = princ10.intValue();
//        Double emi10 = show(Splash.subCategory.get(spin).get("9"), Splash.category.get(spin).get("9"), Splash.brand.get(spin).get("9"), price00);
//        if (emi10.intValue() < 200)
//            emi10 = 200.0;
//        price10.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi10.intValue()) + " per month");
//
//
//    }

//    private void populateThirdRow() {
//        TextView price1 = (TextView) findViewById(R.id.title31);
//        TextView price2 = (TextView) findViewById(R.id.title32);
//        TextView price3 = (TextView) findViewById(R.id.title33);
//        TextView price4 = (TextView) findViewById(R.id.title34);
//        TextView price5 = (TextView) findViewById(R.id.title35);
//        TextView price6 = (TextView) findViewById(R.id.title36);
//        TextView price7 = (TextView) findViewById(R.id.title37);
//        TextView price8 = (TextView) findViewById(R.id.title38);
//        TextView price9 = (TextView) findViewById(R.id.title39);
//        TextView price10 = (TextView) findViewById(R.id.title40);
//
//        TextView title1 = (TextView) findViewById(R.id.titlexxx31);
//        TextView title2 = (TextView) findViewById(R.id.titlexxx32);
//        TextView title3 = (TextView) findViewById(R.id.titlexxx33);
//        TextView title4 = (TextView) findViewById(R.id.titlexxx34);
//        TextView title5 = (TextView) findViewById(R.id.titlexxx35);
//        TextView title6 = (TextView) findViewById(R.id.titlexxx36);
//        TextView title7 = (TextView) findViewById(R.id.titlexxx37);
//        TextView title8 = (TextView) findViewById(R.id.titlexxx38);
//        TextView title9 = (TextView) findViewById(R.id.titlexxx39);
//        TextView title10 = (TextView) findViewById(R.id.titlexxx40);
//
//
//        ImageView img1 = (ImageView) findViewById(R.id.img31);
//        ImageView img2 = (ImageView) findViewById(R.id.img32);
//        ImageView img3 = (ImageView) findViewById(R.id.img33);
//        ImageView img4 = (ImageView) findViewById(R.id.img34);
//        ImageView img5 = (ImageView) findViewById(R.id.img35);
//        ImageView img6 = (ImageView) findViewById(R.id.img36);
//        ImageView img7 = (ImageView) findViewById(R.id.img37);
//        ImageView img8 = (ImageView) findViewById(R.id.img38);
//        ImageView img9 = (ImageView) findViewById(R.id.img39);
//        ImageView img10 = (ImageView) findViewById(R.id.img40);
//
//        ImageView brand1 = (ImageView) findViewById(R.id.brand31);
//        ImageView brand2 = (ImageView) findViewById(R.id.brand32);
//        ImageView brand3 = (ImageView) findViewById(R.id.brand33);
//        ImageView brand4 = (ImageView) findViewById(R.id.brand34);
//        ImageView brand5 = (ImageView) findViewById(R.id.brand35);
//        ImageView brand6 = (ImageView) findViewById(R.id.brand36);
//        ImageView brand7 = (ImageView) findViewById(R.id.brand37);
//        ImageView brand8 = (ImageView) findViewById(R.id.brand38);
//        ImageView brand9 = (ImageView) findViewById(R.id.brand39);
//        ImageView brand10 = (ImageView) findViewById(R.id.brand40);
//        if (Splash.sellers.get(spin).get("0").equals("flipkart"))
//            brand1.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("amazon"))
//            brand1.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("paytm"))
//            brand1.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("snapdeal"))
//            brand1.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("flipkart"))
//            brand2.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("amazon"))
//            brand2.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("paytm"))
//            brand2.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("snapdeal"))
//            brand2.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("flipkart"))
//            brand3.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("amazon"))
//            brand3.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("paytm"))
//            brand3.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("snapdeal"))
//            brand3.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("flipkart"))
//            brand4.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("amazon"))
//            brand4.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("paytm"))
//            brand4.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("snapdeal"))
//            brand4.setImageResource(R.drawable.sdeal_fav1x);
//
//        if (Splash.sellers.get(spin).get("4").equals("flipkart"))
//            brand5.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("amazon"))
//            brand5.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("paytm"))
//            brand5.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("snapdeal"))
//            brand5.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("flipkart"))
//            brand6.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("amazon"))
//            brand6.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("paytm"))
//            brand6.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("snapdeal"))
//            brand6.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("flipkart"))
//            brand7.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("amazon"))
//            brand7.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("paytm"))
//            brand7.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("snapdeal"))
//            brand7.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("flipkart"))
//            brand8.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("amazon"))
//            brand8.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("paytm"))
//            brand8.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("snapdeal"))
//            brand8.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("flipkart"))
//            brand9.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("amazon"))
//            brand9.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("paytm"))
//            brand9.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("snapdeal"))
//            brand9.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("flipkart"))
//            brand10.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("amazon"))
//            brand10.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("paytm"))
//            brand10.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("snapdeal"))
//            brand10.setImageResource(R.drawable.sdeal_fav1x);
//
//
////            card1 = (ImageView) findViewById(R.id.img1);
////            card2 = (ImageView) findViewById(R.id.imgx);
////            card3 = (ImageView) findViewById(R.id.img2);
////            card4 = (ImageView) findViewById(R.id.img2x);
//
//        title1.setText(Splash.title.get(spin).get("0"));
//        title2.setText(Splash.title.get(spin).get("1"));
//        title3.setText(Splash.title.get(spin).get("2"));
//        title4.setText(Splash.title.get(spin).get("3"));
//        title5.setText(Splash.title.get(spin).get("4"));
//        title6.setText(Splash.title.get(spin).get("5"));
//        title7.setText(Splash.title.get(spin).get("6"));
//        title8.setText(Splash.title.get(spin).get("7"));
//        title9.setText(Splash.title.get(spin).get("8"));
//        title10.setText(Splash.title.get(spin).get("9"));
//
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("0"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img1);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("1"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img2);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("2"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img3);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("3"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img4);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("4"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img5);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("5"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img6);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("6"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img7);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("7"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img8);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("8"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img9);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("9"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img10);
//
//        Double princ1 = Double.parseDouble(Splash.selling.get(spin).get("0"));
//
//        int price01 = princ1.intValue();
//        Double emi01 = show(Splash.subCategory.get(spin).get("0"), Splash.category.get(spin).get("0"), Splash.brand.get(spin).get("0"), price01);
//        if (emi01.intValue() < 200)
//            emi01 = 200.0;
//        price1.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi01.intValue()) + " per month");
//        //emi2
//
//
//        Double princ2 = Double.parseDouble(Splash.selling.get(spin).get("1"));
//
//        int price02 = princ2.intValue();
//        Double emi02 = show(Splash.subCategory.get(spin).get("1"), Splash.category.get(spin).get("1"), Splash.brand.get(spin).get("1"), price02);
//        if (emi02.intValue() < 200)
//            emi02 = 200.0;
//        price2.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi02.intValue()) + " per month");
//
//
//        Double princ3 = Double.parseDouble(Splash.selling.get(spin).get("2"));
//        int price03 = princ3.intValue();
//        Double emi03 = show(Splash.subCategory.get(spin).get("2"), Splash.category.get(spin).get("2"), Splash.brand.get(spin).get("2"), price03);
//        if (emi03.intValue() < 200)
//            emi03 = 200.0;
//        price3.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi03.intValue()) + " per month");
//
//
//        //emi4
//
//        Double princ4 = Double.parseDouble(Splash.selling.get(spin).get("3"));
//        int price04 = princ4.intValue();
//        Double emi04 = show(Splash.subCategory.get(spin).get("3"), Splash.category.get(spin).get("3"), Splash.brand.get(spin).get("3"), price04);
//        if (emi04.intValue() < 200)
//            emi04 = 200.0;
//        price4.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi04.intValue()) + " per month");
//
//
//        Double princ5 = Double.parseDouble(Splash.selling.get(spin).get("4"));
//        int price05 = princ5.intValue();
//        Double emi05 = show(Splash.subCategory.get(spin).get("4"), Splash.category.get(spin).get("4"), Splash.brand.get(spin).get("4"), price05);
//        //price5.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi05.intValue()) + " per month");
//        if (emi05.intValue() < 200)
//            emi05 = 200.0;
//        price5.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi05.intValue()) + " per month");
//
//
//        Double princ6 = Double.parseDouble(Splash.selling.get(spin).get("5"));
//        int price06 = princ6.intValue();
//        Double emi06 = show(Splash.subCategory.get(spin).get("5"), Splash.category.get(spin).get("5"), Splash.brand.get(spin).get("5"), price06);
//        //price6.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi06.intValue()) + " per month");
//        if (emi06.intValue() < 200)
//            emi06 = 200.0;
//        price6.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi06.intValue()) + " per month");
//
//
//        Double princ7 = Double.parseDouble(Splash.selling.get(spin).get("6"));
//        int price07 = princ7.intValue();
//        Double emi07 = show(Splash.subCategory.get(spin).get("6"), Splash.category.get(spin).get("6"), Splash.brand.get(spin).get("6"), price07);
//        if (emi07.intValue() < 200)
//            emi07 = 200.0;
//        price7.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi07.intValue()) + " per month");
//
//
//        Double princ8 = Double.parseDouble(Splash.selling.get(spin).get("7"));
//        int price08 = princ8.intValue();
//        Double emi08 = show(Splash.subCategory.get(spin).get("7"), Splash.category.get(spin).get("7"), Splash.brand.get(spin).get("7"), price08);
//        if (emi08.intValue() < 200)
//            emi08 = 200.0;
//        price8.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi08.intValue()) + " per month");
//
//
//        Double princ9 = Double.parseDouble(Splash.selling.get(spin).get("8"));
//        int price09 = princ9.intValue();
//        Double emi09 = show(Splash.subCategory.get(spin).get("8"), Splash.category.get(spin).get("8"), Splash.brand.get(spin).get("8"), price09);
//        if (emi09.intValue() < 200)
//            emi09 = 200.0;
//        price9.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi09.intValue()) + " per month");
//
//
//        Double princ10 = Double.parseDouble(Splash.selling.get(spin).get("9"));
//        int price00 = princ10.intValue();
//        Double emi10 = show(Splash.subCategory.get(spin).get("9"), Splash.category.get(spin).get("9"), Splash.brand.get(spin).get("9"), price00);
//        if (emi10.intValue() < 200)
//            emi10 = 200.0;
//        price10.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi10.intValue()) + " per month");
//
//
//    }
//
//    private void populateFouthRow() {
//        TextView price1 = (TextView) findViewById(R.id.title41);
//        TextView price2 = (TextView) findViewById(R.id.title42);
//        TextView price3 = (TextView) findViewById(R.id.title43);
//        TextView price4 = (TextView) findViewById(R.id.title44);
//        TextView price5 = (TextView) findViewById(R.id.title45);
//        TextView price6 = (TextView) findViewById(R.id.title46);
//        TextView price7 = (TextView) findViewById(R.id.title47);
//        TextView price8 = (TextView) findViewById(R.id.title48);
//        TextView price9 = (TextView) findViewById(R.id.title49);
//        TextView price10 = (TextView) findViewById(R.id.title50);
//
//        TextView title1 = (TextView) findViewById(R.id.titlexxx41);
//        TextView title2 = (TextView) findViewById(R.id.titlexxx42);
//        TextView title3 = (TextView) findViewById(R.id.titlexxx43);
//        TextView title4 = (TextView) findViewById(R.id.titlexxx44);
//        TextView title5 = (TextView) findViewById(R.id.titlexxx45);
//        TextView title6 = (TextView) findViewById(R.id.titlexxx46);
//        TextView title7 = (TextView) findViewById(R.id.titlexxx47);
//        TextView title8 = (TextView) findViewById(R.id.titlexxx48);
//        TextView title9 = (TextView) findViewById(R.id.titlexxx49);
//        TextView title10 = (TextView) findViewById(R.id.titlexxx50);
//
//
//        ImageView img1 = (ImageView) findViewById(R.id.img41);
//        ImageView img2 = (ImageView) findViewById(R.id.img42);
//        ImageView img3 = (ImageView) findViewById(R.id.img43);
//        ImageView img4 = (ImageView) findViewById(R.id.img44);
//        ImageView img5 = (ImageView) findViewById(R.id.img45);
//        ImageView img6 = (ImageView) findViewById(R.id.img46);
//        ImageView img7 = (ImageView) findViewById(R.id.img47);
//        ImageView img8 = (ImageView) findViewById(R.id.img48);
//        ImageView img9 = (ImageView) findViewById(R.id.img49);
//        ImageView img10 = (ImageView) findViewById(R.id.img50);
//
//        ImageView brand1 = (ImageView) findViewById(R.id.brand41);
//        ImageView brand2 = (ImageView) findViewById(R.id.brand42);
//        ImageView brand3 = (ImageView) findViewById(R.id.brand43);
//        ImageView brand4 = (ImageView) findViewById(R.id.brand44);
//        ImageView brand5 = (ImageView) findViewById(R.id.brand45);
//        ImageView brand6 = (ImageView) findViewById(R.id.brand46);
//        ImageView brand7 = (ImageView) findViewById(R.id.brand47);
//        ImageView brand8 = (ImageView) findViewById(R.id.brand48);
//        ImageView brand9 = (ImageView) findViewById(R.id.brand49);
//        ImageView brand10 = (ImageView) findViewById(R.id.brand50);
//        if (Splash.sellers.get(spin).get("0").equals("flipkart"))
//            brand1.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("amazon"))
//            brand1.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("paytm"))
//            brand1.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("snapdeal"))
//            brand1.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("flipkart"))
//            brand2.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("amazon"))
//            brand2.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("paytm"))
//            brand2.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("snapdeal"))
//            brand2.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("flipkart"))
//            brand3.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("amazon"))
//            brand3.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("paytm"))
//            brand3.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("snapdeal"))
//            brand3.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("flipkart"))
//            brand4.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("amazon"))
//            brand4.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("paytm"))
//            brand4.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("snapdeal"))
//            brand4.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("flipkart"))
//            brand5.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("amazon"))
//            brand5.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("paytm"))
//            brand5.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("snapdeal"))
//            brand5.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("flipkart"))
//            brand6.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("amazon"))
//            brand6.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("paytm"))
//            brand6.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("snapdeal"))
//            brand6.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("flipkart"))
//            brand7.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("amazon"))
//            brand7.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("paytm"))
//            brand7.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("snapdeal"))
//            brand7.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("flipkart"))
//            brand8.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("amazon"))
//            brand8.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("paytm"))
//            brand8.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("snapdeal"))
//            brand8.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("flipkart"))
//            brand9.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("amazon"))
//            brand9.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("paytm"))
//            brand9.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("snapdeal"))
//            brand9.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("flipkart"))
//            brand10.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("amazon"))
//            brand10.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("paytm"))
//            brand10.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("snapdeal"))
//            brand10.setImageResource(R.drawable.sdeal_fav1x);
//
//
////            card1 = (ImageView) findViewById(R.id.img1);
////            card2 = (ImageView) findViewById(R.id.imgx);
////            card3 = (ImageView) findViewById(R.id.img2);
////            card4 = (ImageView) findViewById(R.id.img2x);
//
//        title1.setText(Splash.title.get(spin).get("0"));
//        title2.setText(Splash.title.get(spin).get("1"));
//        title3.setText(Splash.title.get(spin).get("2"));
//        title4.setText(Splash.title.get(spin).get("3"));
//        title5.setText(Splash.title.get(spin).get("4"));
//        title6.setText(Splash.title.get(spin).get("5"));
//        title7.setText(Splash.title.get(spin).get("6"));
//        title8.setText(Splash.title.get(spin).get("7"));
//        title9.setText(Splash.title.get(spin).get("8"));
//        title10.setText(Splash.title.get(spin).get("9"));
//
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("0"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img1);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("1"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img2);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("2"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img3);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("3"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img4);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("4"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img5);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("5"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img6);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("6"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img7);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("7"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img8);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("8"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img9);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("9"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img10);
//
//
//        Double princ1 = Double.parseDouble(Splash.selling.get(spin).get("0"));
//
//        int price01 = princ1.intValue();
//        Double emi01 = show(Splash.subCategory.get(spin).get("0"), Splash.category.get(spin).get("0"), Splash.brand.get(spin).get("0"), price01);
//        if (emi01.intValue() < 200)
//            emi01 = 200.0;
//        price1.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi01.intValue()) + " per month");
//        //emi2
//
//
//        Double princ2 = Double.parseDouble(Splash.selling.get(spin).get("1"));
//
//        int price02 = princ2.intValue();
//        Double emi02 = show(Splash.subCategory.get(spin).get("1"), Splash.category.get(spin).get("1"), Splash.brand.get(spin).get("1"), price02);
//        if (emi02.intValue() < 200)
//            emi02 = 200.0;
//        price2.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi02.intValue()) + " per month");
//
//
//        Double princ3 = Double.parseDouble(Splash.selling.get(spin).get("2"));
//        int price03 = princ3.intValue();
//        Double emi03 = show(Splash.subCategory.get(spin).get("2"), Splash.category.get(spin).get("2"), Splash.brand.get(spin).get("2"), price03);
//        if (emi03.intValue() < 200)
//            emi03 = 200.0;
//        price3.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi03.intValue()) + " per month");
//
//
//        //emi4
//
//        Double princ4 = Double.parseDouble(Splash.selling.get(spin).get("3"));
//        int price04 = princ4.intValue();
//        Double emi04 = show(Splash.subCategory.get(spin).get("3"), Splash.category.get(spin).get("3"), Splash.brand.get(spin).get("3"), price04);
//        if (emi04.intValue() < 200)
//            emi04 = 200.0;
//        price4.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi04.intValue()) + " per month");
//
//
//        Double princ5 = Double.parseDouble(Splash.selling.get(spin).get("4"));
//        int price05 = princ5.intValue();
//        Double emi05 = show(Splash.subCategory.get(spin).get("4"), Splash.category.get(spin).get("4"), Splash.brand.get(spin).get("4"), price05);
//        //price5.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi05.intValue()) + " per month");
//        if (emi05.intValue() < 200)
//            emi05 = 200.0;
//        price5.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi05.intValue()) + " per month");
//
//
//        Double princ6 = Double.parseDouble(Splash.selling.get(spin).get("5"));
//        int price06 = princ6.intValue();
//        Double emi06 = show(Splash.subCategory.get(spin).get("5"), Splash.category.get(spin).get("5"), Splash.brand.get(spin).get("5"), price06);
//        //price6.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi06.intValue()) + " per month");
//        if (emi06.intValue() < 200)
//            emi06 = 200.0;
//        price6.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi06.intValue()) + " per month");
//
//
//        Double princ7 = Double.parseDouble(Splash.selling.get(spin).get("6"));
//        int price07 = princ7.intValue();
//        Double emi07 = show(Splash.subCategory.get(spin).get("6"), Splash.category.get(spin).get("6"), Splash.brand.get(spin).get("6"), price07);
//        if (emi07.intValue() < 200)
//            emi07 = 200.0;
//        price7.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi07.intValue()) + " per month");
//
//
//        Double princ8 = Double.parseDouble(Splash.selling.get(spin).get("7"));
//        int price08 = princ8.intValue();
//        Double emi08 = show(Splash.subCategory.get(spin).get("7"), Splash.category.get(spin).get("7"), Splash.brand.get(spin).get("7"), price08);
//        if (emi08.intValue() < 200)
//            emi08 = 200.0;
//        price8.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi08.intValue()) + " per month");
//
//
//        Double princ9 = Double.parseDouble(Splash.selling.get(spin).get("8"));
//        int price09 = princ9.intValue();
//        Double emi09 = show(Splash.subCategory.get(spin).get("8"), Splash.category.get(spin).get("8"), Splash.brand.get(spin).get("8"), price09);
//        if (emi09.intValue() < 200)
//            emi09 = 200.0;
//        price9.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi09.intValue()) + " per month");
//
//
//        Double princ10 = Double.parseDouble(Splash.selling.get(spin).get("9"));
//        int price00 = princ10.intValue();
//        Double emi10 = show(Splash.subCategory.get(spin).get("9"), Splash.category.get(spin).get("9"), Splash.brand.get(spin).get("9"), price00);
//        if (emi10.intValue() < 200)
//            emi10 = 200.0;
//        price10.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi10.intValue()) + " per month");
//
//    }
//
//    private void populateFifthRow() {
//        TextView price1 = (TextView) findViewById(R.id.title51);
//        TextView price2 = (TextView) findViewById(R.id.title52);
//        TextView price3 = (TextView) findViewById(R.id.title53);
//        TextView price4 = (TextView) findViewById(R.id.title54);
//        TextView price5 = (TextView) findViewById(R.id.title55);
//        TextView price6 = (TextView) findViewById(R.id.title56);
//        TextView price7 = (TextView) findViewById(R.id.title57);
//        TextView price8 = (TextView) findViewById(R.id.title58);
//        TextView price9 = (TextView) findViewById(R.id.title59);
//        TextView price10 = (TextView) findViewById(R.id.title60);
//
//        TextView title1 = (TextView) findViewById(R.id.titlexxx51);
//        TextView title2 = (TextView) findViewById(R.id.titlexxx52);
//        TextView title3 = (TextView) findViewById(R.id.titlexxx53);
//        TextView title4 = (TextView) findViewById(R.id.titlexxx54);
//        TextView title5 = (TextView) findViewById(R.id.titlexxx55);
//        TextView title6 = (TextView) findViewById(R.id.titlexxx56);
//        TextView title7 = (TextView) findViewById(R.id.titlexxx57);
//        TextView title8 = (TextView) findViewById(R.id.titlexxx58);
//        TextView title9 = (TextView) findViewById(R.id.titlexxx59);
//        TextView title10 = (TextView) findViewById(R.id.titlexxx60);
//
//
//        ImageView img1 = (ImageView) findViewById(R.id.img51);
//        ImageView img2 = (ImageView) findViewById(R.id.img52);
//        ImageView img3 = (ImageView) findViewById(R.id.img53);
//        ImageView img4 = (ImageView) findViewById(R.id.img54);
//        ImageView img5 = (ImageView) findViewById(R.id.img55);
//        ImageView img6 = (ImageView) findViewById(R.id.img56);
//        ImageView img7 = (ImageView) findViewById(R.id.img57);
//        ImageView img8 = (ImageView) findViewById(R.id.img58);
//        ImageView img9 = (ImageView) findViewById(R.id.img59);
//        ImageView img10 = (ImageView) findViewById(R.id.img60);
//
//        ImageView brand1 = (ImageView) findViewById(R.id.brand51);
//        ImageView brand2 = (ImageView) findViewById(R.id.brand52);
//        ImageView brand3 = (ImageView) findViewById(R.id.brand53);
//        ImageView brand4 = (ImageView) findViewById(R.id.brand54);
//        ImageView brand5 = (ImageView) findViewById(R.id.brand55);
//        ImageView brand6 = (ImageView) findViewById(R.id.brand56);
//        ImageView brand7 = (ImageView) findViewById(R.id.brand57);
//        ImageView brand8 = (ImageView) findViewById(R.id.brand58);
//        ImageView brand9 = (ImageView) findViewById(R.id.brand59);
//        ImageView brand10 = (ImageView) findViewById(R.id.brand60);
//        if (Splash.sellers.get(spin).get("0").equals("flipkart"))
//            brand1.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("amazon"))
//            brand1.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("paytm"))
//            brand1.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("snapdeal"))
//            brand1.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("flipkart"))
//            brand2.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("amazon"))
//            brand2.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("paytm"))
//            brand2.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("snapdeal"))
//            brand2.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("flipkart"))
//            brand3.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("amazon"))
//            brand3.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("paytm"))
//            brand3.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("snapdeal"))
//            brand3.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("flipkart"))
//            brand4.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("amazon"))
//            brand4.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("paytm"))
//            brand4.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("snapdeal"))
//            brand4.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("flipkart"))
//            brand5.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("amazon"))
//            brand5.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("paytm"))
//            brand5.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("snapdeal"))
//            brand5.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("flipkart"))
//            brand6.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("amazon"))
//            brand6.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("paytm"))
//            brand6.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("snapdeal"))
//            brand6.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("flipkart"))
//            brand7.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("amazon"))
//            brand7.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("paytm"))
//            brand7.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("snapdeal"))
//            brand7.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("flipkart"))
//            brand8.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("amazon"))
//            brand8.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("paytm"))
//            brand8.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("snapdeal"))
//            brand8.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("flipkart"))
//            brand9.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("amazon"))
//            brand9.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("paytm"))
//            brand9.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("snapdeal"))
//            brand9.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("flipkart"))
//            brand10.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("amazon"))
//            brand10.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("paytm"))
//            brand10.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("snapdeal"))
//            brand10.setImageResource(R.drawable.sdeal_fav1x);
//
//
////            card1 = (ImageView) findViewById(R.id.img1);
////            card2 = (ImageView) findViewById(R.id.imgx);
////            card3 = (ImageView) findViewById(R.id.img2);
////            card4 = (ImageView) findViewById(R.id.img2x);
//
//        title1.setText(Splash.title.get(spin).get("0"));
//        title2.setText(Splash.title.get(spin).get("1"));
//        title3.setText(Splash.title.get(spin).get("2"));
//        title4.setText(Splash.title.get(spin).get("3"));
//        title5.setText(Splash.title.get(spin).get("4"));
//        title6.setText(Splash.title.get(spin).get("5"));
//        title7.setText(Splash.title.get(spin).get("6"));
//        title8.setText(Splash.title.get(spin).get("7"));
//        title9.setText(Splash.title.get(spin).get("8"));
//        title10.setText(Splash.title.get(spin).get("9"));
//
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("0"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img1);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("1"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img2);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("2"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img3);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("3"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img4);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("4"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img5);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("5"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img6);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("6"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img7);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("7"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img8);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("8"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img9);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("9"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img10);
//
//
//        Double princ1 = Double.parseDouble(Splash.selling.get(spin).get("0"));
//
//        int price01 = princ1.intValue();
//        Double emi01 = show(Splash.subCategory.get(spin).get("0"), Splash.category.get(spin).get("0"), Splash.brand.get(spin).get("0"), price01);
//        if (emi01.intValue() < 200)
//            emi01 = 200.0;
//        price1.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi01.intValue()) + " per month");
//        //emi2
//
//
//        Double princ2 = Double.parseDouble(Splash.selling.get(spin).get("1"));
//
//        int price02 = princ2.intValue();
//        Double emi02 = show(Splash.subCategory.get(spin).get("1"), Splash.category.get(spin).get("1"), Splash.brand.get(spin).get("1"), price02);
//        if (emi02.intValue() < 200)
//            emi02 = 200.0;
//        price2.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi02.intValue()) + " per month");
//
//
//        Double princ3 = Double.parseDouble(Splash.selling.get(spin).get("2"));
//        int price03 = princ3.intValue();
//        Double emi03 = show(Splash.subCategory.get(spin).get("2"), Splash.category.get(spin).get("2"), Splash.brand.get(spin).get("2"), price03);
//        if (emi03.intValue() < 200)
//            emi03 = 200.0;
//        price3.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi03.intValue()) + " per month");
//
//
//        //emi4
//
//        Double princ4 = Double.parseDouble(Splash.selling.get(spin).get("3"));
//        int price04 = princ4.intValue();
//        Double emi04 = show(Splash.subCategory.get(spin).get("3"), Splash.category.get(spin).get("3"), Splash.brand.get(spin).get("3"), price04);
//        if (emi04.intValue() < 200)
//            emi04 = 200.0;
//        price4.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi04.intValue()) + " per month");
//
//
//        Double princ5 = Double.parseDouble(Splash.selling.get(spin).get("4"));
//        int price05 = princ5.intValue();
//        Double emi05 = show(Splash.subCategory.get(spin).get("4"), Splash.category.get(spin).get("4"), Splash.brand.get(spin).get("4"), price05);
//        // price5.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi05.intValue()) + " per month");
//        if (emi05.intValue() < 200)
//            emi05 = 200.0;
//        price5.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi05.intValue()) + " per month");
//
//
//        Double princ6 = Double.parseDouble(Splash.selling.get(spin).get("5"));
//        int price06 = princ6.intValue();
//        Double emi06 = show(Splash.subCategory.get(spin).get("5"), Splash.category.get(spin).get("5"), Splash.brand.get(spin).get("5"), price06);
//        //price6.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi06.intValue()) + " per month");
//        if (emi06.intValue() < 200)
//            emi06 = 200.0;
//        price6.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi06.intValue()) + " per month");
//
//
//        Double princ7 = Double.parseDouble(Splash.selling.get(spin).get("6"));
//        int price07 = princ7.intValue();
//        Double emi07 = show(Splash.subCategory.get(spin).get("6"), Splash.category.get(spin).get("6"), Splash.brand.get(spin).get("6"), price07);
//        if (emi07.intValue() < 200)
//            emi07 = 200.0;
//        price7.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi07.intValue()) + " per month");
//
//
//        Double princ8 = Double.parseDouble(Splash.selling.get(spin).get("7"));
//        int price08 = princ8.intValue();
//        Double emi08 = show(Splash.subCategory.get(spin).get("7"), Splash.category.get(spin).get("7"), Splash.brand.get(spin).get("7"), price08);
//        if (emi08.intValue() < 200)
//            emi08 = 200.0;
//        price8.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi08.intValue()) + " per month");
//
//
//        Double princ9 = Double.parseDouble(Splash.selling.get(spin).get("8"));
//        int price09 = princ9.intValue();
//        Double emi09 = show(Splash.subCategory.get(spin).get("8"), Splash.category.get(spin).get("8"), Splash.brand.get(spin).get("8"), price09);
//        if (emi09.intValue() < 200)
//            emi09 = 200.0;
//        price9.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi09.intValue()) + " per month");
//
//
//        Double princ10 = Double.parseDouble(Splash.selling.get(spin).get("9"));
//        int price00 = princ10.intValue();
//        Double emi10 = show(Splash.subCategory.get(spin).get("9"), Splash.category.get(spin).get("9"), Splash.brand.get(spin).get("9"), price00);
//        if (emi10.intValue() < 200)
//            emi10 = 200.0;
//        price10.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi10.intValue()) + " per month");
//
//
//    }
//
//
//    private void populateSixthRow() {
//        TextView price1 = (TextView) findViewById(R.id.title61);
//        TextView price2 = (TextView) findViewById(R.id.title62);
//        TextView price3 = (TextView) findViewById(R.id.title63);
//        TextView price4 = (TextView) findViewById(R.id.title64);
//        TextView price5 = (TextView) findViewById(R.id.title65);
//        TextView price6 = (TextView) findViewById(R.id.title66);
//        TextView price7 = (TextView) findViewById(R.id.title67);
//        TextView price8 = (TextView) findViewById(R.id.title68);
//        TextView price9 = (TextView) findViewById(R.id.title69);
//        TextView price10 = (TextView) findViewById(R.id.title70);
//
//        TextView title1 = (TextView) findViewById(R.id.titlexxx61);
//        TextView title2 = (TextView) findViewById(R.id.titlexxx62);
//        TextView title3 = (TextView) findViewById(R.id.titlexxx63);
//        TextView title4 = (TextView) findViewById(R.id.titlexxx64);
//        TextView title5 = (TextView) findViewById(R.id.titlexxx65);
//        TextView title6 = (TextView) findViewById(R.id.titlexxx66);
//        TextView title7 = (TextView) findViewById(R.id.titlexxx67);
//        TextView title8 = (TextView) findViewById(R.id.titlexxx68);
//        TextView title9 = (TextView) findViewById(R.id.titlexxx69);
//        TextView title10 = (TextView) findViewById(R.id.titlexxx70);
//
//
//        ImageView img1 = (ImageView) findViewById(R.id.img61);
//        ImageView img2 = (ImageView) findViewById(R.id.img62);
//        ImageView img3 = (ImageView) findViewById(R.id.img63);
//        ImageView img4 = (ImageView) findViewById(R.id.img64);
//        ImageView img5 = (ImageView) findViewById(R.id.img65);
//        ImageView img6 = (ImageView) findViewById(R.id.img66);
//        ImageView img7 = (ImageView) findViewById(R.id.img67);
//        ImageView img8 = (ImageView) findViewById(R.id.img68);
//        ImageView img9 = (ImageView) findViewById(R.id.img69);
//        ImageView img10 = (ImageView) findViewById(R.id.img70);
//
//        ImageView brand1 = (ImageView) findViewById(R.id.brand61);
//        ImageView brand2 = (ImageView) findViewById(R.id.brand62);
//        ImageView brand3 = (ImageView) findViewById(R.id.brand63);
//        ImageView brand4 = (ImageView) findViewById(R.id.brand64);
//        ImageView brand5 = (ImageView) findViewById(R.id.brand65);
//        ImageView brand6 = (ImageView) findViewById(R.id.brand66);
//        ImageView brand7 = (ImageView) findViewById(R.id.brand67);
//        ImageView brand8 = (ImageView) findViewById(R.id.brand68);
//        ImageView brand9 = (ImageView) findViewById(R.id.brand69);
//        ImageView brand10 = (ImageView) findViewById(R.id.brand70);
//        if (Splash.sellers.get(spin).get("0").equals("flipkart"))
//            brand1.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("amazon"))
//            brand1.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("paytm"))
//            brand1.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("0").equals("snapdeal"))
//            brand1.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("flipkart"))
//            brand2.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("amazon"))
//            brand2.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("paytm"))
//            brand2.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("1").equals("snapdeal"))
//            brand2.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("flipkart"))
//            brand3.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("amazon"))
//            brand3.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("paytm"))
//            brand3.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("2").equals("snapdeal"))
//            brand3.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("flipkart"))
//            brand4.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("amazon"))
//            brand4.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("paytm"))
//            brand4.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("3").equals("snapdeal"))
//            brand4.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("flipkart"))
//            brand5.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("amazon"))
//            brand5.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("paytm"))
//            brand5.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("4").equals("snapdeal"))
//            brand5.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("flipkart"))
//            brand6.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("amazon"))
//            brand6.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("paytm"))
//            brand6.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("5").equals("snapdeal"))
//            brand6.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("flipkart"))
//            brand7.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("amazon"))
//            brand7.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("paytm"))
//            brand7.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("6").equals("snapdeal"))
//            brand7.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("flipkart"))
//            brand8.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("amazon"))
//            brand8.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("paytm"))
//            brand8.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("7").equals("snapdeal"))
//            brand8.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("flipkart"))
//            brand9.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("amazon"))
//            brand9.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("paytm"))
//            brand9.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("8").equals("snapdeal"))
//            brand9.setImageResource(R.drawable.sdeal_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("flipkart"))
//            brand10.setImageResource(R.drawable.fk_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("amazon"))
//            brand10.setImageResource(R.drawable.amazon_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("paytm"))
//            brand10.setImageResource(R.drawable.paytm_fav1x);
//        if (Splash.sellers.get(spin).get("9").equals("snapdeal"))
//            brand10.setImageResource(R.drawable.sdeal_fav1x);
//
//
////            card1 = (ImageView) findViewById(R.id.img1);
////            card2 = (ImageView) findViewById(R.id.imgx);
////            card3 = (ImageView) findViewById(R.id.img2);
////            card4 = (ImageView) findViewById(R.id.img2x);
//
//        title1.setText(Splash.title.get(spin).get("0"));
//        title2.setText(Splash.title.get(spin).get("1"));
//        title3.setText(Splash.title.get(spin).get("2"));
//        title4.setText(Splash.title.get(spin).get("3"));
//        title5.setText(Splash.title.get(spin).get("4"));
//        title6.setText(Splash.title.get(spin).get("5"));
//        title7.setText(Splash.title.get(spin).get("6"));
//        title8.setText(Splash.title.get(spin).get("7"));
//        title9.setText(Splash.title.get(spin).get("8"));
//        title10.setText(Splash.title.get(spin).get("9"));
//
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("0"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img1);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("1"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img2);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("2"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img3);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("3"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img4);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("4"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img5);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("5"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img6);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("6"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img7);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("7"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img8);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("8"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img9);
//        Picasso.with(this)
//                .load(Splash.image.get(spin).get("9"))
//                .placeholder(R.drawable.emptyimageproducts)
//                .into(img10);
//
//
//        Double princ1 = Double.parseDouble(Splash.selling.get(spin).get("0"));
//
//        int price01 = princ1.intValue();
//        Double emi01 = show(Splash.subCategory.get(spin).get("0"), Splash.category.get(spin).get("0"), Splash.brand.get(spin).get("0"), price01);
//        if (emi01.intValue() < 200)
//            emi01 = 200.0;
//        price1.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi01.intValue()) + " per month");
//        //emi2
//
//
//        Double princ2 = Double.parseDouble(Splash.selling.get(spin).get("1"));
//
//        int price02 = princ2.intValue();
//        Double emi02 = show(Splash.subCategory.get(spin).get("1"), Splash.category.get(spin).get("1"), Splash.brand.get(spin).get("1"), price02);
//        if (emi02.intValue() < 200)
//            emi02 = 200.0;
//        price2.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi02.intValue()) + " per month");
//
//
//        Double princ3 = Double.parseDouble(Splash.selling.get(spin).get("2"));
//        int price03 = princ3.intValue();
//        Double emi03 = show(Splash.subCategory.get(spin).get("2"), Splash.category.get(spin).get("2"), Splash.brand.get(spin).get("2"), price03);
//        if (emi03.intValue() < 200)
//            emi03 = 200.0;
//        price3.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi03.intValue()) + " per month");
//
//
//        //emi4
//
//        Double princ4 = Double.parseDouble(Splash.selling.get(spin).get("3"));
//        int price04 = princ4.intValue();
//        Double emi04 = show(Splash.subCategory.get(spin).get("3"), Splash.category.get(spin).get("3"), Splash.brand.get(spin).get("3"), price04);
//        if (emi04.intValue() < 200)
//            emi04 = 200.0;
//        price4.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi04.intValue()) + " per month");
//
//
//        Double princ5 = Double.parseDouble(Splash.selling.get(spin).get("4"));
//        int price05 = princ5.intValue();
//        Double emi05 = show(Splash.subCategory.get(spin).get("4"), Splash.category.get(spin).get("4"), Splash.brand.get(spin).get("4"), price05);
//        //price5.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi05.intValue()) + " per month");
//        if (emi05.intValue() < 200)
//            emi05 = 200.0;
//        price5.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi05.intValue()) + " per month");
//
//
//        Double princ6 = Double.parseDouble(Splash.selling.get(spin).get("5"));
//        int price06 = princ6.intValue();
//        Double emi06 = show(Splash.subCategory.get(spin).get("5"), Splash.category.get(spin).get("5"), Splash.brand.get(spin).get("5"), price06);
//        //price6.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi06.intValue()) + " per month");
//        if (emi06.intValue() < 200)
//            emi06 = 200.0;
//        price6.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi06.intValue()) + " per month");
//
//
//        Double princ7 = Double.parseDouble(Splash.selling.get(spin).get("6"));
//        int price07 = princ7.intValue();
//        Double emi07 = show(Splash.subCategory.get(spin).get("6"), Splash.category.get(spin).get("6"), Splash.brand.get(spin).get("6"), price07);
//        if (emi07.intValue() < 200)
//            emi07 = 200.0;
//        price7.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi07.intValue()) + " per month");
//
//
//        Double princ8 = Double.parseDouble(Splash.selling.get(spin).get("7"));
//        int price08 = princ8.intValue();
//        Double emi08 = show(Splash.subCategory.get(spin).get("7"), Splash.category.get(spin).get("7"), Splash.brand.get(spin).get("7"), price08);
//        if (emi08.intValue() < 200)
//            emi08 = 200.0;
//        price8.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi08.intValue()) + " per month");
//
//
//        Double princ9 = Double.parseDouble(Splash.selling.get(spin).get("8"));
//        int price09 = princ9.intValue();
//        Double emi09 = show(Splash.subCategory.get(spin).get("8"), Splash.category.get(spin).get("8"), Splash.brand.get(spin).get("8"), price09);
//        if (emi09.intValue() < 200)
//            emi09 = 200.0;
//        price9.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi09.intValue()) + " per month");
//
//
//        Double princ10 = Double.parseDouble(Splash.selling.get(spin).get("9"));
//        int price00 = princ10.intValue();
//        Double emi10 = show(Splash.subCategory.get(spin).get("9"), Splash.category.get(spin).get("9"), Splash.brand.get(spin).get("9"), price00);
//        if (emi10.intValue() < 200)
//            emi10 = 200.0;
//        price10.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi10.intValue()) + " per month");
//
//
//    }


    //    public Double calculateEmi( int monthsallowed ,int sellingPrice){
//        Double rate = 21.0 / 1200.0;
//        int d = 0;
//        if (sellingPrice <= 5000) {
//            emi = sellingPrice * 0.8 / monthsallowed;
//        } else {
//            if (currDay <= 15)
//                d = 35 - currDay;
//            else
//                d = 65 - currDay;
//
//            emi = Math.ceil((sellingPrice * 0.8 * rate * Math.pow(1 + rate, monthsallowed - 1) * (1 + rate * d * 12 / 365)) / (Math.pow(1 + rate, monthsallowed) - 1));
//        }
//        return emi;
//    }
//
//
//
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

        int monthscheck = 0;
        //digo
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


                //                        Toast.makeText(HomePage.this, curr, Toast.LENGTH_SHORT).show();
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
        //snapdeal


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
        //
        //       Toast.makeText(HomePage.this, productId, Toast.LENGTH_SHORT).show();

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
//
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

                String url = getApplicationContext().getString(R.string.server) + "api/product/trending?category=" + urldisplay + "&count=10";

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
                            //versioncode=data1.getString("version_code");
                            //return versioncode;
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
                if(productType.equals("trending")){
                    if(spinner0.getVisibility()==View.VISIBLE)
                        spinner0.setVisibility(View.INVISIBLE);

                    adapter0 = new HorizontalScrollViewAdapter(productsMap.get("trending"),HomePage.this);
                    horizontal0.setAdapter(HomePage.this,adapter0);

                }
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





//                TrendingMapWrapper mapWrapper = new TrendingMapWrapper();
//                mapWrapper.setCategory(category);
//                mapWrapper.setBrand(brand);
//                mapWrapper.setFkid1(fkid1);
//                mapWrapper.setImage(image);
//                mapWrapper.setMrp1(mrp1);
//                mapWrapper.setSellers(sellers);
//                mapWrapper.setSelling(selling);
//                mapWrapper.setSubCategory(subCategory);
//                mapWrapper.setTitle(title);
//                String json = gson.toJson(mapWrapper);
//                sh.edit().putString("TrendingProductsSerialized", json).apply();

            }
        }
    }
    }





