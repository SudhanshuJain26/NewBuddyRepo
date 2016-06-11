package indwin.c3.shareapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

import indwin.c3.shareapp.activities.FindProduct;
import indwin.c3.shareapp.activities.ProfileActivity;
import indwin.c3.shareapp.activities.SupportedWebsites;
import indwin.c3.shareapp.application.BuddyApplication;
import indwin.c3.shareapp.models.RecentSearchItems;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.Constants;
import io.intercom.android.sdk.Intercom;
import io.intercom.com.google.gson.Gson;


public class HomePage extends AppCompatActivity implements View.OnClickListener {
    private String[] arraySpinner;
    WebView form;
    private Intent intform;
    private NavigationView navigationView;
    private ImageView card1, card2, card3, card4;
    String userId = "";
    Timer timer;
    int page1 = 0;

    private String formstatus, name, fbid, rejectionReason, email, uniqueCode, verificationdate, creditLimit, searchTitle, searchBrand, searchCategory, searchSubcategory, description, specification, review, infor;
    SharedPreferences cred;
    int screen_no;
    private int searchPrice = 0;
    private String urlImg = "", page = "";

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
    ImageView dot1 ;
    ImageView dot2 ;
    ImageView dot3;
    ImageView dot4;
    //    Map<String,Map<int,V>> map;

    //    HashMap<String, HashMap<String,String>> image;
    private android.content.ClipboardManager myClipboard;
    private String spin = "";
    private String productId = "";
    private int checkValidUrl = 0, monthsallowed = 0;
    private Double emi = 0.0;

    private int checkValidFromApis = 0;
    private String sellerNme = "";
    private String token = "";
    private SharedPreferences userP;
    private SharedPreferences mPrefs;
    private Gson gson;
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
//    final Handler handler = new Handler();
//    Timer t = new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BuddyApplication application = (BuddyApplication) getApplication();
        mTracker = application.getDefaultTracker();
        sh = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        ss = getSharedPreferences("cred", Context.MODE_PRIVATE);
        userP = getSharedPreferences("token", Context.MODE_PRIVATE);
        cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences2 = getSharedPreferences("buddyin", Context.MODE_PRIVATE);
        if (sh.getInt("checklog", 0) == 1) {
            userId = sharedpreferences2.getString("name", null);
        }

//        imageSlider = (ViewPager)findViewById(R.id.imageslider);
//        dot1 = (ImageView) findViewById(R.id.c1);
//        dot2 = (ImageView) findViewById(R.id.c2);
//        dot3 = (ImageView) findViewById(R.id.c3);
        //userid=ss.getString("phone_number", "");
        token = userP.getString("token_value", null);
        intentFilter = new IntentFilter();
        intentFilter.addAction("CLOSE_ALL");
        //adp = new SecondViewPagerAdapter(getApplicationContext(),3,HomePage.this);
        //imageSlider.setAdapter(adp);
//        adp.notifyDataSetChanged();
//        imageSlider.setCurrentItem(0);
//        imageSlider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//            @Override
//            public void onPageSelected(int position) {
//
//
//                if (position == 0) {
//
//
//                    dot1.setBackgroundResource(R.drawable.circle2);
//                    dot2.setBackgroundResource(R.drawable.circle);
//                    dot3.setBackgroundResource(R.drawable.circle);
//                } else if (position == 1) {
//                    dot2.setBackgroundResource(R.drawable.circle2);
//                    dot1.setBackgroundResource(R.drawable.circle);
//                    dot3.setBackgroundResource(R.drawable.circle);
//                } else if (position == 2) {
//                    dot2.setBackgroundResource(R.drawable.circle);
//                    dot1.setBackgroundResource(R.drawable.circle);
//                    dot3.setBackgroundResource(R.drawable.circle2);
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        // Timer for auto sliding



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

            ImageView image1 = (ImageView)findViewById(R.id.img11);
            ImageView image2 = (ImageView)findViewById(R.id.img12);
            ImageView image3 = (ImageView)findViewById(R.id.img13);
            ImageView image4 = (ImageView)findViewById(R.id.img14);
            ImageView image5 = (ImageView)findViewById(R.id.img21);
            ImageView image6 = (ImageView)findViewById(R.id.img22);
            ImageView image7 = (ImageView)findViewById(R.id.img23);
            ImageView image8 = (ImageView)findViewById(R.id.img24);
            ImageView image9 = (ImageView)findViewById(R.id.img31);
            ImageView image10 = (ImageView)findViewById(R.id.img32);
            ImageView image11 = (ImageView)findViewById(R.id.img33);
            ImageView image12 = (ImageView)findViewById(R.id.img34);
            ImageView image13 = (ImageView)findViewById(R.id.img41);
            ImageView image14 = (ImageView)findViewById(R.id.img42);
            ImageView image15= (ImageView)findViewById(R.id.img43);
            ImageView image16 = (ImageView)findViewById(R.id.img44);
            ImageView image17= (ImageView)findViewById(R.id.img51);
            ImageView image18 = (ImageView)findViewById(R.id.img52);
            ImageView image19= (ImageView)findViewById(R.id.img53);
            ImageView image20 = (ImageView)findViewById(R.id.img54);
            ImageView image21 = (ImageView)findViewById(R.id.img61);
            ImageView image22 = (ImageView)findViewById(R.id.img62);
            ImageView image23 = (ImageView)findViewById(R.id.img63);
            ImageView image24 = (ImageView)findViewById(R.id.img64);
            ImageView image25 = (ImageView)findViewById(R.id.img01);
            ImageView image26 = (ImageView)findViewById(R.id.img02);
            ImageView image27 = (ImageView)findViewById(R.id.img03);
            ImageView image28 = (ImageView)findViewById(R.id.img04);
            ImageView image29 = (ImageView)findViewById(R.id.img05);
            ImageView image30 = (ImageView)findViewById(R.id.img06);
            ImageView image31 = (ImageView)findViewById(R.id.img07);
            ImageView image32 = (ImageView)findViewById(R.id.img08);
            ImageView image33 = (ImageView)findViewById(R.id.img09);
            ImageView image34 = (ImageView)findViewById(R.id.img10);






            image1.setOnClickListener(this);
            image2.setOnClickListener(this);
            image3.setOnClickListener(this);
            image4.setOnClickListener(this);
            image5.setOnClickListener(this);
            image6.setOnClickListener(this);
            image7.setOnClickListener(this);
            image8.setOnClickListener(this);
            image9.setOnClickListener(this);
            image10.setOnClickListener(this);
            image11.setOnClickListener(this);
            image12.setOnClickListener(this);
            image13.setOnClickListener(this);
            image14.setOnClickListener(this);
            image15.setOnClickListener(this);
            image16.setOnClickListener(this);
            image17.setOnClickListener(this);
            image18.setOnClickListener(this);
            image19.setOnClickListener(this);
            image20.setOnClickListener(this);
            image21.setOnClickListener(this);
            image22.setOnClickListener(this);
            image23.setOnClickListener(this);
            image24.setOnClickListener(this);
            image25.setOnClickListener(this);
            image26.setOnClickListener(this);
            image27.setOnClickListener(this);
            image28.setOnClickListener(this);
            image29.setOnClickListener(this);
            image30.setOnClickListener(this);
            image31.setOnClickListener(this);
            image32.setOnClickListener(this);
            image33.setOnClickListener(this);
            image34.setOnClickListener(this);



            RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.editlayout);


            imageSlider = (ViewPager)findViewById(R.id.imageslider);
            dot1 = (ImageView) findViewById(R.id.c1);
            dot2 = (ImageView) findViewById(R.id.c2);
            dot3 = (ImageView) findViewById(R.id.c3);
            dot4 = (ImageView) findViewById(R.id.c4);
            supported = (TextView)findViewById(R.id.supported);
            supported.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomePage.this, SupportedWebsites.class);
                    startActivity(intent);

                }
            });




           // paste = (ImageView) findViewById(R.id.pasteAg);
            adp = new SecondViewPagerAdapter(getApplicationContext(),4,HomePage.this);
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
                    }else if (position==3){
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

            final  Handler handler = new Handler();

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

//            newtren = (RelativeLayout) findViewById(R.id.newtr);
//            tren = (TextView) findViewById(R.id.trending);
//            newtren.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    drops = (RelativeLayout) findViewById(R.id.drops);
//                    newtren.setVisibility(View.GONE);
//                    View viewDrops = findViewById(R.id.drops);
//                    Animation animShow = AnimationUtils.loadAnimation(HomePage.this, R.anim.show); //- See more at: http://findnerd.com/list/view/HideShow-a-View-with-slide-updown-animation-in-Android/2537/#sthash.0WR4fLwr.dpuf
//
//                    drops.setVisibility(View.VISIBLE);
//                    // drops.setAnimation(animShow);
//                    query.clearFocus();
//                    hideSoftKeyboard(HomePage.this);
//                    //   drops.setVisibility(View.VISIBLE);
//
//
//                }
//            });
            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                @Override
                public void onScrollChanged() {

                    query.clearFocus();
                    hideSoftKeyboard(HomePage.this);                     //for verticalScrollView
                    //DO SOMETHING WITH THE SCROLL COORDINATES

                }
            });

//            products = (TextView) findViewById(R.id.products);
//            products.setTypeface(Typeface.DEFAULT_BOLD);
//            lappy = (TextView) findViewById(R.id.lappy);
//            fashion = (TextView) findViewById(R.id.fashion);
//            beau = (TextView) findViewById(R.id.beau);
//            ent = (TextView) findViewById(R.id.ent);
//            foot = (TextView) findViewById(R.id.foot);
            spin = "Mobiles";
            try {
                populate();
            } catch (Exception e) {
            }
            spin ="trending";

            try {
                populateTrendingRow();
            } catch (Exception e) {
            } try {
                //  cardclick();
            } catch (Exception e) {
            }

            try {
              //  cardclick();
            } catch (Exception e) {
            }

            spin = "Electronics";
            try{

                populateSecondRow();
            }catch (Exception e){

                System.out.println(e.getMessage());
            }
            spin = "Computers&subCategory=Laptops";
            try{

                populateThirdRow();
            }catch (Exception e){

                System.out.println(e.getMessage());
            }
            spin = "Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle";
            try{

                populateFouthRow();
            }catch (Exception e){

                System.out.println(e.getMessage());
            }
            spin = "Health%20and%20Beauty";
            try{
                populateFifthRow();
            }catch (Exception e){

                System.out.println(e.getMessage());
            }
            spin = "Footwear";
            try{
                populateSixthRow();
            }catch (Exception e){

                System.out.println(e.getMessage());
            }


//            products.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    tren.setText("ALL PRODUCTS");
//                    drops.setVisibility(View.GONE);
//                    products.setTypeface(Typeface.DEFAULT_BOLD);
//                    lappy.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    beau.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    ent.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    foot.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    fashion.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//
//                    newtren.setVisibility(View.VISIBLE);
//                    tren.setText("MOBILES");
//                    spin = "Mobiles";
//                    try {
//                        populate();
//                        cardclick();
//                    } catch (Exception e) {
//                    }
//
//
//                }
//            });
//            lappy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    tren.setText("LAPTOPS");
//                    drops.setVisibility(View.GONE);
//                    newtren.setVisibility(View.VISIBLE);
//                    lappy.setTypeface(Typeface.DEFAULT_BOLD);
//                    spin = "Laptops";
//                    products.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    beau.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    ent.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    foot.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    fashion.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    try {
//                        populate();
//                        cardclick();
//                    } catch (Exception e) {
//                    }
//
//                }
//            });
//            fashion.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    tren.setText("FASHION");
//
//                    drops.setVisibility(View.GONE);
//                    newtren.setVisibility(View.VISIBLE);
//                    fashion.setTypeface(Typeface.DEFAULT_BOLD);
//                    spin = "apparels";
//                    lappy.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    beau.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    ent.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    foot.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    products.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    try {
//                        populate();
//                        cardclick();
//                    } catch (Exception e) {
//                    }
//                }
//            });
//            beau.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    tren.setText("BEAUTY AND PERSONAL CARE");
//                    drops.setVisibility(View.GONE);
//                    newtren.setVisibility(View.VISIBLE);
//                    beau.setTypeface(Typeface.DEFAULT_BOLD);
//                    spin = "homeandbeauty";
//                    lappy.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    products.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    ent.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    foot.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    fashion.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    try {
//                        populate();
//                        cardclick();
//                    } catch (Exception e) {
//                    }
//                }
//            });
//            ent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    tren.setText("ENTERTAINMENT");
//                    drops.setVisibility(View.GONE);
//                    ent.setTypeface(Typeface.DEFAULT_BOLD);
//                    newtren.setVisibility(View.VISIBLE);
//                    spin = "Electronics";
//                    lappy.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    beau.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    products.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    foot.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    fashion.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    try {
//                        populate();
//                        cardclick();
//                    } catch (Exception e) {
//                    }
//                }
//            });
//            foot.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    tren.setText("FOOTWEAR");
//                    drops.setVisibility(View.GONE);
//                    newtren.setVisibility(View.VISIBLE);
//                    foot.setTypeface(Typeface.DEFAULT_BOLD);
//                    lappy.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    beau.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    ent.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    products.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    fashion.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//                    spin = "Footwear";
//                    try {
//                        populate();
//                        cardclick();
//                    } catch (Exception e) {
//                    }
//                }
//            });


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
                TextView titlebar = (TextView)findViewById(R.id.titlehead);
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
            String json = mPrefs.getString("UserObject", "");
            setNewIdsNull(json);
            json = mPrefs.getString("UserObject", "");
            user = gson.fromJson(json, UserModel.class);

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
                    } else {
                        line1.setText("Your email ID needs to be verified. Do it quickly to get your profile approved");
                        but.setText("Verify your email ID now");
                        but.setVisibility(View.VISIBLE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //            if (user.get) {
            //
            //            } else if (formstatus.equals("saved")) {
            //                line1.setText("Ready to get started? Complete your profile now to get a Borrowing Limit and start shopping now!");
            //                line2.setText("");
            //                line3.setVisibility(View.VISIBLE);
            //                line3.setText("");
            //                but.setText("Complete it now!");
            //            } else if (formstatus.equals("flashApproved")) {
            //                line1.setText("Congrats! You have been approved for Rs.1000 flash Credit Limit! ");
            //                line2.setText("");
            //                line3.setVisibility(View.VISIBLE);
            //                line3.setText("");
            //                but.setText("Increase it further!");
            //            } else if (formstatus.equals("approved")) {
            //
            //                line1.setText("You have been approved for a Borrowing Limit of Rs. " + creditLimit + ". Go Crazy");
            ////                line2.setText("");
            //                but.setVisibility(View.GONE);
            //                ImageView i = (ImageView) findViewById(R.id.app);
            //                i.setVisibility(View.VISIBLE);
            //
            //            } else if (formstatus.equals("declined")) {
            //                line1.setText("Sorry, but we are unable to approve a Borrowing Limit for you! Find out why!");
            ////                line2.setText("");
            //                but.setText("");
            //            } else if (formstatus.equals("submitted")) {
            //                if (screen_no == 1) {
            ////upload
            //                    line1.setText("We have received your details but your documents are not yet uploaded.");
            ////                    line2.setText("");
            //
            //                    but.setText("Upload 'em now");
            //
            //                } else if (screen_no == 2) {
            //                    //setupverif
            //                    line1.setText("Your profile is almost complete, but you still haven't scheduled your college_id verification date.");
            ////                    line2.setText("");
            ////                    line3.setVisibility(View.VISIBLE);
            ////                    line3.setText("");
            //                    but.setText("Schedule it now!");
            //
            //                } else if (screen_no == 3) {
            //                    line1.setText("Your verification has been scheduled on:");
            //                    line2.setText(verificationdate);
            //                    ;
            //                    but.setText("Okie Dokie");
            //                    //verif
            //
            //                }
            //
            //            }


            but = (TextView) findViewById(R.id.but);
            but.setOnClickListener(new View.OnClickListener() {
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
                    int action =0;
                   // if (checkedit == 1) {
                    //       Toast.makeText(HomePage.this,"check",Toast.LENGTH_LONG).show();
                    if(action==MotionEvent.ACTION_DOWN && checkedit==0) {
                        //paste.setVisibility(View.VISIBLE);
                        query.setInputType(InputType.TYPE_NULL);

                        Intent intent = new Intent(HomePage.this, FindProduct.class);
//                        //intent.putExtra("searchlist",recentSearchItemsList);
//                        Log.i("jbabc","2345");
                        HomePage.this.startActivity(intent);
                       // new FindRecentProductLinks(HomePage.this).execute("https://ssl.hellobuddy.in/api/user/product/recent?userid="+userId +"&count=5");
                        checkedit=1;
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
                    int action =0;
                    // if (checkedit == 1) {
                    //       Toast.makeText(HomePage.this,"check",Toast.LENGTH_LONG).show();
                    if(action==MotionEvent.ACTION_DOWN && checkedit==0) {
                        //paste.setVisibility(View.VISIBLE);
                        query.setInputType(InputType.TYPE_NULL);

                        Intent intent = new Intent(HomePage.this, FindProduct.class);
//                        //intent.putExtra("searchlist",recentSearchItemsList);
//                        Log.i("jbabc","2345");
                        HomePage.this.startActivity(intent);
//                        new FindRecentProductLinks(HomePage.this).execute("https://ssl.hellobuddy.in/api/user/product/recent?userid=8971923656&count=5");
                        checkedit=1;
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
//            Spinner s = (Spinner) findViewById(R.id.spinner);
//            Drawable spinnerDrawable = s.getBackground().getConstantState().newDrawable();
//
//            spinnerDrawable.setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_ATOP);
//
//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                s.setBackground(spinnerDrawable);
//            } else {
//                s.setBackgroundDrawable(spinnerDrawable);
//            }
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                    R.layout.colorspin, arraySpinner);
//            adapter.setDropDownViewResource(R.layout.dropdown);
//            s.setAdapter(adapter);

//            try {
//                s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                        if (position == 0)
//                            spin = "Mobiles";
//                        if (position == 1)
//                            spin = "Laptops";
//                        if (position == 2)
//                            spin = "apparels";
//                        if (position == 4)
//                            spin = "Electronics";
//                        if (position == 5)
//                            spin = "Footwear";
//                        if (position == 3)
//                            spin = "homeandbeauty";
//                        try {
//                            populate();
//                        } catch (Exception e) {
//
//                            //    new trending().execute("dd");
//                        }
//                        cardclick();
//                        //final Intent send1=new Intent(HomePage.this,ViewForm.class);
//                        //                        card1.setOnClickListener(new View.OnClickListener() {
//                        //                            @Override
//                        //                            public void onClick(View v) {
//                        //                                query.clearFocus();
//                        //                                //     Toast.makeText(HomePage.this,"checknow",Toast.LENGTH_LONG).show();
//                        //                                Intent send1 = new Intent(HomePage.this, ViewForm.class);
//                        //                                send1.putExtra("prodid", Splash.fkid1.get(spin).get("0"));
//                        //                                send1.putExtra("ecom", Splash.sellers.get(spin).get("0"));
//                        //                                send1.putExtra("which_page", 10);
//                        //                                Splash.checkNot = 1;
//                        //                                paste = (TextView) findViewById(R.id.pasteAg);
//                        //                                clickpaste();
//                        //                                startActivity(send1);
//                        //                            }
//                        //                        });
//                        //                        card2.setOnClickListener(new View.OnClickListener() {
//                        //                            @Override
//                        //                            public void onClick(View v) {
//                        //                                query.clearFocus();
//                        //                                Intent send1 = new Intent(HomePage.this, ViewForm.class);
//                        //                                send1.putExtra("prodid", Splash.fkid1.get(spin).get("1"));
//                        //                                send1.putExtra("ecom", Splash.sellers.get(spin).get("1"));
//                        //                                send1.putExtra("which_page", 10);
//                        //                                Splash.checkNot = 1;
//                        //                                paste = (TextView) findViewById(R.id.pasteAg);
//                        //                                clickpaste();
//                        //                                startActivity(send1);
//                        //
//                        //                            }
//                        //                        });
//                        //                        card3.setOnClickListener(new View.OnClickListener() {
//                        //                            @Override
//                        //                            public void onClick(View v) {
//                        //                                query.clearFocus();
//                        //                                Intent send1 = new Intent(HomePage.this, ViewForm.class);
//                        //
//                        //                                send1.putExtra("prodid", Splash.fkid1.get(spin).get("2"));
//                        //                                send1.putExtra("ecom", Splash.sellers.get(spin).get("2"));
//                        //                                send1.putExtra("which_page", 10);
//                        //                                Splash.checkNot = 1;
//                        //                                paste = (TextView) findViewById(R.id.pasteAg);
//                        //                                clickpaste();
//                        //                                startActivity(send1);
//                        //                            }
//                        //                        });
//                        //                        card4.setOnClickListener(new View.OnClickListener() {
//                        //                            @Override
//                        //                            public void onClick(View v) {
//                        //                                query.clearFocus();
//                        //                                Intent send1 = new Intent(HomePage.this, ViewForm.class);
//                        //                                send1.putExtra("prodid", Splash.fkid1.get(spin).get("3"));
//                        //                                send1.putExtra("ecom", Splash.sellers.get(spin).get("3"));
//                        //                                send1.putExtra("which_page", 10);
//                        //                                Splash.checkNot = 1;
//                        //                                paste = (TextView) findViewById(R.id.pasteAg);
//                        //                                clickpaste();
//                        //                                startActivity(send1);
//                        //                            }
//                        //                        });
//                        //                    if(position==3)
//                        //                        Toast.makeText(HomePage.this,"checknow",Toast.LENGTH_LONG).show();
//                        // your code here
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parentView) {
//                        // your code here
//                    }
//
//                });
//
//            } catch (Exception e) {
//                System.out.println("Error with spinner" + e.toString());
//            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        checkedit=0;
    }

//    public void doTimerTask(){
//
//        mTimerTask = new TimerTask() {
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//
//                                if (page1 > 3) { // In my case the number of pages are 5
//
//                                    imageSlider.setCurrentItem(0);
//
//
//
//                                    // Showing a toast for just testing purpose
//                                    //Toast.makeText(getApplicationContext(), "Timer stoped",
//                                    // Toast.LENGTH_LONG).show();
//                                } else {
//                                    imageSlider.setCurrentItem(page1++);
//                                }
//                            }
//                        });
//
//                    }};
//
//
//
//        // public void schedule (TimerTask task, long delay, long period)
//        t.schedule(mTimerTask, 500, 30000);  //
//
//    }


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

            AppUtils.saveInSharedPrefs(this, AppUtils.USER_OBJECT, jsonObject.toString());


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

//    public void pageSwitcher(int seconds) {
//        timer = new Timer(); // At this line a new Thread will be created
//        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
//
//        // in
//        // milliseconds
//    }

//    public void AsynchTaskTimer() {
//        final Handler handler = new Handler();
//
//        TimerTask timertask = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//                        try {
////                            if (page1 > 3) { // In my case the number of pages are 5
//////
////                        imageSlider.setCurrentItem(0);
////
////
////
////                        // Showing a toast for just testing purpose
////                        //Toast.makeText(getApplicationContext(), "Timer stoped",
////                        // Toast.LENGTH_LONG).show();
////                    } else {
////                        imageSlider.setCurrentItem(page1++);
////                    }
//                            new RemindTask();
//
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                        }
//                    }
//                });
//            }
//        };
//        timer = new Timer(); //This is new
//        timer.schedule(timertask, 0, 15000); // execute in every 15sec
//    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
//    public void createNewTimer(){
//        timer = new Timer(); // At this line a new Thread will be created
//        timer.scheduleAtFixedRate(new RemindTask(), 5000, 5 * 1000);
//        Log.i("timer","djjdjd");
//    }

    public void switchtoProductPage(){
        Intent in = new Intent(HomePage.this, ProductsPage.class);
        in.putExtra("seller", sellerNme);
        in.putExtra("product", productId);
        in.putExtra("page", "api");
        startActivity(in);
    }

    @Override
    public void onClick(View v) {
        Long time = Calendar.getInstance().getTimeInMillis() / 1000;

        switch(v.getId()){
            case R.id.img11:
                spin = "Mobiles";

                productId = Splash.fkid1.get(spin).get("0");
                sellerNme = Splash.sellers.get(spin).get("0");
                switchtoProductPage();


                break;

            case R.id.img12:
                spin = "Mobiles";
                productId = Splash.fkid1.get(spin).get("1");
                sellerNme = Splash.sellers.get(spin).get("1");
                switchtoProductPage();

                break;

            case R.id.img13:
                spin = "Mobiles";
                productId = Splash.fkid1.get(spin).get("2");
                sellerNme = Splash.sellers.get(spin).get("2");
                switchtoProductPage();

                break;

            case R.id.img14:
                spin = "Mobiles";

                productId = Splash.fkid1.get(spin).get("3");
                sellerNme = Splash.sellers.get(spin).get("3");
                switchtoProductPage();

                break;

            case R.id.img21:
                spin = "Electronics";

                productId = Splash.fkid1.get(spin).get("0");
                sellerNme = Splash.sellers.get(spin).get("0");
                switchtoProductPage();


                break;

            case R.id.img22:
                spin = "Electronics";
                productId = Splash.fkid1.get(spin).get("1");
                sellerNme = Splash.sellers.get(spin).get("1");
                switchtoProductPage();

                break;

            case R.id.img23:
                spin = "Electronics";
                productId = Splash.fkid1.get(spin).get("2");
                sellerNme = Splash.sellers.get(spin).get("2");
                switchtoProductPage();



                break;

            case R.id.img24:
                spin = "Electronics";

                productId = Splash.fkid1.get(spin).get("3");
                sellerNme = Splash.sellers.get(spin).get("3");
                switchtoProductPage();

                break;

            case R.id.img31:
                spin = "Computers&subCategory=Laptops";

                productId = Splash.fkid1.get(spin).get("0");
                sellerNme = Splash.sellers.get(spin).get("0");
                switchtoProductPage();

                break;
            case R.id.img32:
                spin = "Computers&subCategory=Laptops";

                productId = Splash.fkid1.get(spin).get("1");
                sellerNme = Splash.sellers.get(spin).get("1");
                switchtoProductPage();

                break;
            case R.id.img33:
                spin = "Computers&subCategory=Laptops";

                productId = Splash.fkid1.get(spin).get("2");
                sellerNme = Splash.sellers.get(spin).get("2");
                switchtoProductPage();

                break;
            case R.id.img34:
                spin = "Computers&subCategory=Laptops";

                productId = Splash.fkid1.get(spin).get("3");
                sellerNme = Splash.sellers.get(spin).get("3");
                switchtoProductPage();

                break;

            case R.id.img41:
                spin = "Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle";

                productId = Splash.fkid1.get(spin).get("0");
                sellerNme = Splash.sellers.get(spin).get("0");
                switchtoProductPage();

                break;
            case R.id.img42:
                spin = "Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle";

                productId = Splash.fkid1.get(spin).get("1");
                sellerNme = Splash.sellers.get(spin).get("1");
                switchtoProductPage();

                break;
            case R.id.img43:
                spin = "Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle";

                productId = Splash.fkid1.get(spin).get("2");
                sellerNme = Splash.sellers.get(spin).get("2");
                switchtoProductPage();

                break;
            case R.id.img44:
                spin = "Apparels&category=Wearable%20Smart%20Devices&category=Lifestyle";

                productId = Splash.fkid1.get(spin).get("3");
                sellerNme = Splash.sellers.get(spin).get("3");
                switchtoProductPage();

                break;
            case R.id.img51:
                spin = "Health%20and%20Beauty";

                productId = Splash.fkid1.get(spin).get("0");
                sellerNme = Splash.sellers.get(spin).get("0");
                switchtoProductPage();

                break;
            case R.id.img52:
                spin = "Health%20and%20Beauty";

                productId = Splash.fkid1.get(spin).get("1");
                sellerNme = Splash.sellers.get(spin).get("1");
                switchtoProductPage();

                break;
            case R.id.img53:
                spin = "Health%20and%20Beauty";

                productId = Splash.fkid1.get(spin).get("2");
                sellerNme = Splash.sellers.get(spin).get("2");
                switchtoProductPage();

                break;
            case R.id.img54:
                spin = "Health%20and%20Beauty";

                productId = Splash.fkid1.get(spin).get("3");
                sellerNme = Splash.sellers.get(spin).get("3");
                switchtoProductPage();

                break;

            case R.id.img61:
                spin = "Footwear";

                productId = Splash.fkid1.get(spin).get("0");
                sellerNme = Splash.sellers.get(spin).get("0");
                switchtoProductPage();

                break;
            case R.id.img62:
                spin = "Footwear";

                productId = Splash.fkid1.get(spin).get("1");
                sellerNme = Splash.sellers.get(spin).get("1");
                switchtoProductPage();

                break;
            case R.id.img63:
                spin = "Footwear";

                productId = Splash.fkid1.get(spin).get("2");
                sellerNme = Splash.sellers.get(spin).get("2");
                switchtoProductPage();

                break;
            case R.id.img64:
                spin = "Footwear";

                productId = Splash.fkid1.get(spin).get("3");
                sellerNme = Splash.sellers.get(spin).get("3");
                switchtoProductPage();

                break;

            case R.id.img01:
                spin = "trending";

                productId = Splash.fkid1.get(spin).get("0");
                sellerNme = Splash.sellers.get(spin).get("0");
                switchtoProductPage();

                break;

            case R.id.img02:
                spin = "trending";

                productId = Splash.fkid1.get(spin).get("1");
                sellerNme = Splash.sellers.get(spin).get("1");
                switchtoProductPage();

                break;

            case R.id.img03:
                spin = "trending";

                productId = Splash.fkid1.get(spin).get("2");
                sellerNme = Splash.sellers.get(spin).get("2");
                switchtoProductPage();

                break;

            case R.id.img04:
                spin = "trending";

                productId = Splash.fkid1.get(spin).get("3");
                sellerNme = Splash.sellers.get(spin).get("3");
                switchtoProductPage();

                break;

            case R.id.img05:
                spin = "trending";

                productId = Splash.fkid1.get(spin).get("4");
                sellerNme = Splash.sellers.get(spin).get("4");
                switchtoProductPage();

                break;

            case R.id.img06:
                spin = "trending";

                productId = Splash.fkid1.get(spin).get("5");
                sellerNme = Splash.sellers.get(spin).get("5");
                switchtoProductPage();

                break;

            case R.id.img07:
                spin = "trending";

                productId = Splash.fkid1.get(spin).get("6");
                sellerNme = Splash.sellers.get(spin).get("6");
                switchtoProductPage();

                break;

            case R.id.img08:
                spin = "trending";

                productId = Splash.fkid1.get(spin).get("7");
                sellerNme = Splash.sellers.get(spin).get("7");
                switchtoProductPage();

                break;

            case R.id.img09:
                spin = "trending";

                productId = Splash.fkid1.get(spin).get("8");
                sellerNme = Splash.sellers.get(spin).get("8");
                switchtoProductPage();

                break;

            case R.id.img10:
                spin = "trending";

                productId = Splash.fkid1.get(spin).get("9");
                sellerNme = Splash.sellers.get(spin).get("9");
                switchtoProductPage();

                break;



            default:
                break;

        }

    }


//    @Override
//    public void onClick(View v) {
//        Long time = Calendar.getInstance().getTimeInMillis() / 1000;
//        Log.i("Clicked","which one");
//        switch(v.getId()){
//            case R.id.rl11:
//                spin = "Mobiles";
//
//                productId = Splash.fkid1.get(spin).get("0");
//                sellerNme = Splash.sellers.get(spin).get("0");
//                switchtoProductPage();
//
//
//                break;
//
//            case R.id.rl12:
//                spin = "Mobiles";
//                productId = Splash.fkid1.get(spin).get("1");
//                sellerNme = Splash.sellers.get(spin).get("1");
//                switchtoProductPage();
//
//                break;
//
//            case R.id.rl13:
//                spin = "Mobiles";
//                productId = Splash.fkid1.get(spin).get("2");
//                sellerNme = Splash.sellers.get(spin).get("2");
//                switchtoProductPage();
//
//                break;
//
//            case R.id.rl14:
//                spin = "Mobiles";
//
//                productId = Splash.fkid1.get(spin).get("3");
//                sellerNme = Splash.sellers.get(spin).get("3");
//                switchtoProductPage();
//
//                break;
//
//            case R.id.rl21:
//                spin = "Electronics";
//
//                productId = Splash.fkid1.get(spin).get("0");
//                sellerNme = Splash.sellers.get(spin).get("0");
//                switchtoProductPage();
//
//
//                break;
//
//            case R.id.rl22:
//                spin = "Electronics";
//                productId = Splash.fkid1.get(spin).get("1");
//                sellerNme = Splash.sellers.get(spin).get("1");
//                switchtoProductPage();
//
//                break;
//
//            case R.id.rl23:
//                spin = "Electronics";
//                productId = Splash.fkid1.get(spin).get("2");
//                sellerNme = Splash.sellers.get(spin).get("2");
//                switchtoProductPage();
//
//                break;
//
//            case R.id.rl24:
//                spin = "Electronics";
//
//                productId = Splash.fkid1.get(spin).get("3");
//                sellerNme = Splash.sellers.get(spin).get("3");
//                switchtoProductPage();
//
//                break;
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

//    private void cardclick() {
//        card1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                query.clearFocus();
//                //     Toast.makeText(HomePage.this,"checknow",Toast.LENGTH_LONG).show();
//
//                ///  Intent send1 = new Intent(HomePage.this, ViewForm.class);
//                Long time = Calendar.getInstance().getTimeInMillis() / 1000;
//                productId = Splash.fkid1.get(spin).get("0");
//                sellerNme = Splash.sellers.get(spin).get("0");
//                //                Long time = Calendar.getInstance().getTimeInMillis() / 1000;
//                Intent in = new Intent(HomePage.this, ProductsPage.class);
//                in.putExtra("seller", sellerNme);
//                in.putExtra("product", productId);
//                //                in.putExtra("query",query.getText().toString());
//                in.putExtra("page", "api");
//                startActivity(in);
//                if (time + 5 < userP.getLong("expires", 0))
//                //                                new checkAuth().execute(url);//
//                {
//                    //   new linkSearch().execute();
//                } else {
//                }
//                //   new checkAuth().execute(url);
//                //                    new AuthTokc().execute("cc");
//
//                Splash.checkNot = 1;
//
//                paste = (ImageView) findViewById(R.id.pasteAg);
//                //                clickpaste();
//                //                send1.putExtra("prodid", Splash.fkid1.get(spin).get("0"));
//                //                send1.putExtra("ecom", Splash.sellers.get(spin).get("0"));
//                //                send1.putExtra("which_page", 10);
//
//                //
//                // sestartActivity(send1);
//
//            }
//        });
//        card2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                query.clearFocus();
//
//                //                Intent send1 = new Intent(HomePage.this, ViewForm.class);
//                //                send1.putExtra("prodid", Splash.fkid1.get(spin).get("1"));
//                Splash.checkNot = 1;
//                paste = (ImageView) findViewById(R.id.pasteAg);
                //clickpaste();
//                Long time = Calendar.getInstance().getTimeInMillis() / 1000;
//                productId = Splash.fkid1.get(spin).get("1");
//                sellerNme = Splash.sellers.get(spin).get("1");
//                //                Long time = Calendar.getInstance().getTimeInMillis() / 1000;
//                Intent in = new Intent(HomePage.this, ProductsPage.class);
//                in.putExtra("seller", sellerNme);
//                in.putExtra("product", productId);
//                in.putExtra("page", "api");
//                startActivity(in);
//                if (time + 5 < userP.getLong("expires", 0))
//                //                                new checkAuth().execute(url);//
//                {
//                    // new linkSearch().execute();
//                } else
//                //   new checkAuth().execute(url);
//                // new AuthTokc().execute("cc");
//                {
//                }
//                //                send1.putExtra("ecom", Splash.sellers.get(spin).get("1"));
//                //                send1.putExtra("which_page", 10);
//                //                startActivity(send1);
//
//            }
//        });
//        card3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                query.clearFocus();
//
//                //                Intent send1 = new Intent(HomePage.this, ViewForm.class);
//                Splash.checkNot = 1;
//                Long time = Calendar.getInstance().getTimeInMillis() / 1000;
//                productId = Splash.fkid1.get(spin).get("2");
//                sellerNme = Splash.sellers.get(spin).get("2");
//                //                Long time = Calendar.getInstance().getTimeInMillis() / 1000;
//                Intent in = new Intent(HomePage.this, ProductsPage.class);
//                in.putExtra("seller", sellerNme);
//                in.putExtra("product", productId);
//                in.putExtra("page", "api");
//                startActivity(in);
//                //                if (time + 5 < userP.getLong("expires", 0))
//                ////                                new checkAuth().execute(url);//
//                //                {
//                //                    new linkSearch().execute();
//                //                } else
//                //                    //   new checkAuth().execute(url);
//                //                    new AuthTokc().execute("cc");
//
//
//                paste = (ImageView) findViewById(R.id.pasteAg);
//                //     clickpaste();
//                //                send1.putExtra("prodid", Splash.fkid1.get(spin).get("2"));
//                //                send1.putExtra("ecom", Splash.sellers.get(spin).get("2"));
//                //                send1.putExtra("which_page", 10);
//                //                send1.putExtra("which_page", 10);
//                //                startActivity(send1);
//
//            }
//        });
//        card4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                query.clearFocus();
//                Splash.checkNot = 1;
//
//                //                paste = (TextView) findViewById(R.id.pasteAg);
//                clickpaste();
//                Long time = Calendar.getInstance().getTimeInMillis() / 1000;
//                productId = Splash.fkid1.get(spin).get("3");
//                sellerNme = Splash.sellers.get(spin).get("3");
//                //                Long time = Calendar.getInstance().getTimeInMillis() / 1000;
//                Intent in = new Intent(HomePage.this, ProductsPage.class);
//                in.putExtra("seller", sellerNme);
//                in.putExtra("product", productId);
//                in.putExtra("page", "api");
//                startActivity(in);
//                //                if (time + 5 < userP.getLong("expires", 0))
//                ////                                new checkAuth().execute(url);//
//                //                {
//                //                    new linkSearch().execute();
//                //                } else
//                //                    //   new checkAuth().execute(url);
//                //                    new AuthTokc().execute("cc");
//
//                //                Intent send1 = new Intent(HomePage.this, ViewForm.class);
//                //                send1.putExtra("prodid", Splash.fkid1.get(spin).get("3"));
//                //                send1.putExtra("ecom", Splash.sellers.get(spin).get("3"));
//                //                send1.putExtra("which_page", 10);
//                //                startActivity(send1);
//            }
//        });
//    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void populateTrendingRow(){

        TextView price1 = (TextView) findViewById(R.id.title01);
        TextView price2 = (TextView) findViewById(R.id.title02);
        TextView price3 = (TextView) findViewById(R.id.title03);
        TextView price4 = (TextView) findViewById(R.id.title04);
        TextView price5 = (TextView) findViewById(R.id.title05);
        TextView price6 = (TextView) findViewById(R.id.title06);
        TextView price7 = (TextView) findViewById(R.id.title07);
        TextView price8 = (TextView) findViewById(R.id.title08);
        TextView price9 = (TextView) findViewById(R.id.title09);
        TextView price10 = (TextView) findViewById(R.id.title10);

        TextView title1 = (TextView) findViewById(R.id.titlexxx01);
        TextView title2 = (TextView) findViewById(R.id.titlexxx02);
        TextView title3 = (TextView) findViewById(R.id.titlexxx03);
        TextView title4 = (TextView) findViewById(R.id.titlexxx04);
        TextView title5 = (TextView) findViewById(R.id.titlexxx05);
        TextView title6 = (TextView) findViewById(R.id.titlexxx06);
        TextView title7 = (TextView) findViewById(R.id.titlexxx07);
        TextView title8 = (TextView) findViewById(R.id.titlexxx08);
        TextView title9 = (TextView) findViewById(R.id.titlexxx09);
        TextView title10 = (TextView) findViewById(R.id.titlexxx10);
        //TextView title4 = (TextView) findViewById(R.id.titlexxx04);



        ImageView img1 = (ImageView) findViewById(R.id.img01);
        ImageView img2 = (ImageView) findViewById(R.id.img02);
        ImageView img3 = (ImageView) findViewById(R.id.img03);
        ImageView img4 = (ImageView) findViewById(R.id.img04);
        ImageView img5 = (ImageView) findViewById(R.id.img05);
        ImageView img6 = (ImageView) findViewById(R.id.img06);
        ImageView img7 = (ImageView) findViewById(R.id.img07);
        ImageView img8 = (ImageView) findViewById(R.id.img08);
        ImageView img9 = (ImageView) findViewById(R.id.img09);
        ImageView img10 = (ImageView) findViewById(R.id.img10);



        ImageView brand1 = (ImageView) findViewById(R.id.brand01);
        ImageView brand2 = (ImageView) findViewById(R.id.brand02);
        ImageView brand3 = (ImageView) findViewById(R.id.brand03);
        ImageView brand4 = (ImageView) findViewById(R.id.brand04);
        ImageView brand5 = (ImageView) findViewById(R.id.brand05);
        ImageView brand6 = (ImageView) findViewById(R.id.brand06);
        ImageView brand7 = (ImageView) findViewById(R.id.brand07);
        ImageView brand8 = (ImageView) findViewById(R.id.brand08);
        ImageView brand9 = (ImageView) findViewById(R.id.brand09);
        ImageView brand10 = (ImageView) findViewById(R.id.brand10);
        //ImageView brand4 = (ImageView) findViewById(R.id.brand04);


        if (Splash.sellers.get(spin).get("0").equals("flipkart"))
            brand1.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("amazon"))
            brand1.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("paytm"))
            brand1.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("snapdeal"))
            brand1.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("flipkart"))
            brand2.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("amazon"))
            brand2.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("paytm"))
            brand2.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("snapdeal"))
            brand2.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("flipkart"))
            brand3.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("amazon"))
            brand3.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("paytm"))
            brand3.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("snapdeal"))
            brand3.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("flipkart"))
            brand4.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("amazon"))
            brand4.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("paytm"))
            brand4.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("snapdeal"))
            brand4.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("4").equals("flipkart"))
            brand4.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("4").equals("amazon"))
            brand4.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("4").equals("paytm"))
            brand4.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("4").equals("snapdeal"))
            brand4.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("5").equals("flipkart"))
            brand4.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("5").equals("amazon"))
            brand4.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("5").equals("paytm"))
            brand4.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("5").equals("snapdeal"))
            brand4.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("6").equals("flipkart"))
            brand4.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("6").equals("amazon"))
            brand4.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("6").equals("paytm"))
            brand4.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("6").equals("snapdeal"))
            brand4.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("7").equals("flipkart"))
            brand4.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("7").equals("amazon"))
            brand4.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("7").equals("paytm"))
            brand4.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("7").equals("snapdeal"))
            brand4.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("8").equals("flipkart"))
            brand4.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("8").equals("amazon"))
            brand4.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("8").equals("paytm"))
            brand4.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("8").equals("snapdeal"))
            brand4.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("9").equals("flipkart"))
            brand4.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("9").equals("amazon"))
            brand4.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("9").equals("paytm"))
            brand4.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("9").equals("snapdeal"))
            brand4.setImageResource(R.drawable.sdeal_fav1x);



//            card1 = (ImageView) findViewById(R.id.img1);
//            card2 = (ImageView) findViewById(R.id.imgx);
//            card3 = (ImageView) findViewById(R.id.img2);
//            card4 = (ImageView) findViewById(R.id.img2x);

        title1.setText(Splash.title.get(spin).get("0"));
        title2.setText(Splash.title.get(spin).get("1"));
        title3.setText(Splash.title.get(spin).get("2"));
        title4.setText(Splash.title.get(spin).get("3"));
        title5.setText(Splash.title.get(spin).get("4"));
        title6.setText(Splash.title.get(spin).get("5"));
        title7.setText(Splash.title.get(spin).get("6"));
        title8.setText(Splash.title.get(spin).get("7"));
        title9.setText(Splash.title.get(spin).get("8"));
        title10.setText(Splash.title.get(spin).get("9"));


        Picasso.with(this)
                .load(Splash.image.get(spin).get("0"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img1);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("1"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img2);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("2"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img3);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("3"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img4);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("4"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img5);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("5"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img6);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("6"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img7);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("7"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img8);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("8"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img9);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("9"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img10);



        Double princ1 = Double.parseDouble(Splash.selling.get(spin).get("0"));
        int monthscam = 18;
        String cat1 = Splash.category.get(spin).get("0");
        String subcat1 = Splash.subCategory.get(spin).get("0");
        int a = 2;
        monthscam = months(Splash.subCategory.get(spin).get("0"), Splash.category.get(spin).get("0"), Splash.brand.get(spin).get("0"), princ1.intValue());


        princ1 = princ1 * 0.8;
        Double rate = 0.21 / 12;

        Double emi1 = Math.floor((princ1 * rate * Math.pow(1 + rate, monthscam)) / ((Math.pow(1 + rate, monthscam)) - 1));
        if ((princ1 * 10 / 8) < 5000.0)
            emi1 = princ1 / monthscam;
        price1.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi1.intValue()) + " per month");
        //emi2
        Double princ2 = Double.parseDouble(Splash.selling.get(spin).get("1"));
        int monthscam2 = 18;
        monthscam2 = months(Splash.subCategory.get(spin).get("1"), Splash.category.get(spin).get("1"), Splash.brand.get(spin).get("1"), princ2.intValue());

        princ2 = princ2 * 0.8;
        // Double rate=0.2;
        Double emi2 = Math.floor((princ2 * rate * Math.pow(1 + rate, monthscam2)) / ((Math.pow(1 + rate, monthscam2)) - 1));
        if ((princ2 * 10 / 8) < 5000.0)
            emi2 = princ2 / monthscam2;
        price2.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi2.intValue()) + " per month");


        Double princ3 = Double.parseDouble(Splash.selling.get(spin).get("2"));
        int monthscam3 = 18;
        monthscam3 = months(Splash.subCategory.get(spin).get("2"), Splash.category.get(spin).get("2"), Splash.brand.get(spin).get("2"), princ3.intValue());
        princ3 = princ3 * 0.8;
        // Double rate=0.2;
        Double emi3 = Math.floor((princ3 * rate * Math.pow(1 + rate, monthscam3)) / ((Math.pow(1 + rate, monthscam3)) - 1));
        if ((princ3 * 10 / 8) < 5000.0)
            emi3 = princ3 / monthscam3;
        price3.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi3.intValue()) + " per month");

        //emi4

        Double princ4 = Double.parseDouble(Splash.selling.get(spin).get("3"));
        int monthscam4 = 18;
        monthscam4 = months(Splash.subCategory.get(spin).get("3"), Splash.category.get(spin).get("3"), Splash.brand.get(spin).get("3"), princ4.intValue());


        princ4 = princ4 * 0.8;
        // Double rate=0.2;
        Double emi4 = Math.floor((princ4 * rate * Math.pow(1 + rate, monthscam4)) / ((Math.pow(1 + rate, monthscam4)) - 1));
        if ((princ4 * 10 / 8) < 5000.0)
            emi4 = princ4 / monthscam4;
        price4.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi4.intValue()) + " per month");





        Double princ5 = Double.parseDouble(Splash.selling.get(spin).get("4"));
        int monthscam5 = 18;
        monthscam5 = months(Splash.subCategory.get(spin).get("4"), Splash.category.get(spin).get("4"), Splash.brand.get(spin).get("4"), princ5.intValue());


        princ5 = princ5 * 0.8;
        // Double rate=0.2;
        Double emi5 = Math.floor((princ5 * rate * Math.pow(1 + rate, monthscam5)) / ((Math.pow(1 + rate, monthscam5)) - 1));
        if ((princ5 * 10 / 8) < 5000.0)
            emi5 = princ5 / monthscam5;
        price5.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi5.intValue()) + " per month");


        Double princ6 = Double.parseDouble(Splash.selling.get(spin).get("5"));
        int monthscam6 = 18;
        monthscam6 = months(Splash.subCategory.get(spin).get("5"), Splash.category.get(spin).get("5"), Splash.brand.get(spin).get("5"), princ6.intValue());


        princ6 = princ6 * 0.8;
        // Double rate=0.2;
        Double emi6 = Math.floor((princ6 * rate * Math.pow(1 + rate, monthscam6)) / ((Math.pow(1 + rate, monthscam6)) - 1));
        if ((princ6 * 10 / 8) < 5000.0)
            emi6 = princ6 / monthscam6;
        price6.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi6.intValue()) + " per month");



        Double princ7 = Double.parseDouble(Splash.selling.get(spin).get("6"));
        int monthscam7 = 18;
        monthscam7 = months(Splash.subCategory.get(spin).get("6"), Splash.category.get(spin).get("6"), Splash.brand.get(spin).get("6"), princ7.intValue());


        princ7 = princ7 * 0.8;
        // Double rate=0.2;
        Double emi7 = Math.floor((princ7 * rate * Math.pow(1 + rate, monthscam7)) / ((Math.pow(1 + rate, monthscam7)) - 1));
        if ((princ7 * 10 / 8) < 5000.0)
            emi7 = princ7 / monthscam7;
        price7.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi7.intValue()) + " per month");



        Double princ8 = Double.parseDouble(Splash.selling.get(spin).get("7"));
        int monthscam8 = 18;
        monthscam8 = months(Splash.subCategory.get(spin).get("7"), Splash.category.get(spin).get("7"), Splash.brand.get(spin).get("7"), princ8.intValue());


        princ8 = princ8 * 0.8;
        // Double rate=0.2;
        Double emi8 = Math.floor((princ8 * rate * Math.pow(1 + rate, monthscam8)) / ((Math.pow(1 + rate, monthscam8)) - 1));
        if ((princ8 * 10 / 8) < 5000.0)
            emi8 = princ8 / monthscam8;
        price8.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi8.intValue()) + " per month");


        Double princ9 = Double.parseDouble(Splash.selling.get(spin).get("8"));
        int monthscam9 = 18;
        monthscam9 = months(Splash.subCategory.get(spin).get("8"), Splash.category.get(spin).get("8"), Splash.brand.get(spin).get("8"), princ9.intValue());


        princ9 = princ9 * 0.8;
        // Double rate=0.2;
        Double emi9 = Math.floor((princ9 * rate * Math.pow(1 + rate, monthscam9)) / ((Math.pow(1 + rate, monthscam9)) - 1));
        if ((princ9 * 10 / 8) < 5000.0)
            emi9 = princ9/ monthscam9;
        price9.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi9.intValue()) + " per month");



        Double princ10 = Double.parseDouble(Splash.selling.get(spin).get("9"));
        int monthscam10 = 18;
        monthscam10 = months(Splash.subCategory.get(spin).get("9"), Splash.category.get(spin).get("9"), Splash.brand.get(spin).get("9"), princ10.intValue());


        princ10 = princ10 * 0.8;
        // Double rate=0.2;
        Double emi10 = Math.floor((princ10 * rate * Math.pow(1 + rate, monthscam10)) / ((Math.pow(1 + rate, monthscam10)) - 1));
        if ((princ10 * 10 / 8) < 5000.0)
            emi10 = princ10/ monthscam10;
        price10.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi10.intValue()) + " per month");




    }

    private void populate() {
        {

            TextView price1 = (TextView) findViewById(R.id.title11);
            TextView price2 = (TextView) findViewById(R.id.title12);
            TextView price3 = (TextView) findViewById(R.id.title13);
            TextView price4 = (TextView) findViewById(R.id.title14);
            TextView title1 = (TextView) findViewById(R.id.titlexxx11);
            TextView title2 = (TextView) findViewById(R.id.titlexxx12);
            TextView title3 = (TextView) findViewById(R.id.titlexxx13);
            TextView title4 = (TextView) findViewById(R.id.titlexxx14);
            ImageView img1 = (ImageView) findViewById(R.id.img11);
            ImageView img2 = (ImageView) findViewById(R.id.img12);
            ImageView img3 = (ImageView) findViewById(R.id.img13);
            ImageView img4 = (ImageView) findViewById(R.id.img14);
            ImageView brand1 = (ImageView) findViewById(R.id.brand11);
            ImageView brand2 = (ImageView) findViewById(R.id.brand12);
            ImageView brand3 = (ImageView) findViewById(R.id.brand13);
            ImageView brand4 = (ImageView) findViewById(R.id.brand14);



            if (Splash.sellers.get(spin).get("0").equals("flipkart"))
                brand1.setImageResource(R.drawable.fk_fav1x);
            if (Splash.sellers.get(spin).get("0").equals("amazon"))
                brand1.setImageResource(R.drawable.amazon_fav1x);
            if (Splash.sellers.get(spin).get("0").equals("paytm"))
                brand1.setImageResource(R.drawable.paytm_fav1x);
            if (Splash.sellers.get(spin).get("0").equals("snapdeal"))
                brand1.setImageResource(R.drawable.sdeal_fav1x);
            if (Splash.sellers.get(spin).get("1").equals("flipkart"))
                brand2.setImageResource(R.drawable.fk_fav1x);
            if (Splash.sellers.get(spin).get("1").equals("amazon"))
                brand2.setImageResource(R.drawable.amazon_fav1x);
            if (Splash.sellers.get(spin).get("1").equals("paytm"))
                brand2.setImageResource(R.drawable.paytm_fav1x);
            if (Splash.sellers.get(spin).get("1").equals("snapdeal"))
                brand2.setImageResource(R.drawable.sdeal_fav1x);
            if (Splash.sellers.get(spin).get("2").equals("flipkart"))
                brand3.setImageResource(R.drawable.fk_fav1x);
            if (Splash.sellers.get(spin).get("2").equals("amazon"))
                brand3.setImageResource(R.drawable.amazon_fav1x);
            if (Splash.sellers.get(spin).get("2").equals("paytm"))
                brand3.setImageResource(R.drawable.paytm_fav1x);
            if (Splash.sellers.get(spin).get("2").equals("snapdeal"))
                brand3.setImageResource(R.drawable.sdeal_fav1x);
            if (Splash.sellers.get(spin).get("3").equals("flipkart"))
                brand4.setImageResource(R.drawable.fk_fav1x);
            if (Splash.sellers.get(spin).get("3").equals("amazon"))
                brand4.setImageResource(R.drawable.amazon_fav1x);
            if (Splash.sellers.get(spin).get("3").equals("paytm"))
                brand4.setImageResource(R.drawable.paytm_fav1x);
            if (Splash.sellers.get(spin).get("3").equals("snapdeal"))
                brand4.setImageResource(R.drawable.sdeal_fav1x);


//            card1 = (ImageView) findViewById(R.id.img1);
//            card2 = (ImageView) findViewById(R.id.imgx);
//            card3 = (ImageView) findViewById(R.id.img2);
//            card4 = (ImageView) findViewById(R.id.img2x);

            title1.setText(Splash.title.get(spin).get("0"));
            title2.setText(Splash.title.get(spin).get("1"));
            title3.setText(Splash.title.get(spin).get("2"));
            title4.setText(Splash.title.get(spin).get("3"));

            Picasso.with(this)
                    .load(Splash.image.get(spin).get("0"))
                    .placeholder(R.drawable.emptyimageproducts)
                    .into(img1);
            Picasso.with(this)
                    .load(Splash.image.get(spin).get("1"))
                    .placeholder(R.drawable.emptyimageproducts)
                    .into(img2);
            Picasso.with(this)
                    .load(Splash.image.get(spin).get("2"))
                    .placeholder(R.drawable.emptyimageproducts)
                    .into(img3);
            Picasso.with(this)
                    .load(Splash.image.get(spin).get("3"))
                    .placeholder(R.drawable.emptyimageproducts)
                    .into(img4);
            Double princ1 = Double.parseDouble(Splash.selling.get(spin).get("0"));
            int monthscam = 18;
            String cat1 = Splash.category.get(spin).get("0");
            String subcat1 = Splash.subCategory.get(spin).get("0");
            int a = 2;
            monthscam = months(Splash.subCategory.get(spin).get("0"), Splash.category.get(spin).get("0"), Splash.brand.get(spin).get("0"), princ1.intValue());


            princ1 = princ1 * 0.8;
            Double rate = 0.21 / 12;

            Double emi1 = Math.floor((princ1 * rate * Math.pow(1 + rate, monthscam)) / ((Math.pow(1 + rate, monthscam)) - 1));
            if ((princ1 * 10 / 8) < 5000.0)
                emi1 = princ1 / monthscam;
            price1.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi1.intValue()) + " per month");
            //emi2
            Double princ2 = Double.parseDouble(Splash.selling.get(spin).get("1"));
            int monthscam2 = 18;
            monthscam2 = months(Splash.subCategory.get(spin).get("1"), Splash.category.get(spin).get("1"), Splash.brand.get(spin).get("1"), princ2.intValue());

            princ2 = princ2 * 0.8;
            // Double rate=0.2;
            Double emi2 = Math.floor((princ2 * rate * Math.pow(1 + rate, monthscam2)) / ((Math.pow(1 + rate, monthscam2)) - 1));
            if ((princ2 * 10 / 8) < 5000.0)
                emi2 = princ2 / monthscam2;
            price2.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi2.intValue()) + " per month");


            Double princ3 = Double.parseDouble(Splash.selling.get(spin).get("2"));
            int monthscam3 = 18;
            monthscam3 = months(Splash.subCategory.get(spin).get("2"), Splash.category.get(spin).get("2"), Splash.brand.get(spin).get("2"), princ3.intValue());
            princ3 = princ3 * 0.8;
            // Double rate=0.2;
            Double emi3 = Math.floor((princ3 * rate * Math.pow(1 + rate, monthscam3)) / ((Math.pow(1 + rate, monthscam3)) - 1));
            if ((princ3 * 10 / 8) < 5000.0)
                emi3 = princ3 / monthscam3;
            price3.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi3.intValue()) + " per month");

            //emi4

            Double princ4 = Double.parseDouble(Splash.selling.get(spin).get("3"));
            int monthscam4 = 18;
            monthscam4 = months(Splash.subCategory.get(spin).get("3"), Splash.category.get(spin).get("3"), Splash.brand.get(spin).get("3"), princ3.intValue());


            princ4 = princ4 * 0.8;
            // Double rate=0.2;
            Double emi4 = Math.floor((princ4 * rate * Math.pow(1 + rate, monthscam4)) / ((Math.pow(1 + rate, monthscam4)) - 1));
            if ((princ4 * 10 / 8) < 5000.0)
                emi4 = princ4 / monthscam4;
            price4.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi4.intValue()) + " per month");
        }
////
    //cardclick();
    }

    private void populateSecondRow(){


        TextView price1 = (TextView) findViewById(R.id.title21);
        TextView price2 = (TextView) findViewById(R.id.title22);
        TextView price3 = (TextView) findViewById(R.id.title23);
        TextView price4 = (TextView) findViewById(R.id.title24);
        TextView title1 = (TextView) findViewById(R.id.titlexxx21);
        TextView title2 = (TextView) findViewById(R.id.titlexxx22);
        TextView title3 = (TextView) findViewById(R.id.titlexxx23);
        TextView title4 = (TextView) findViewById(R.id.titlexxx24);
        ImageView img1 = (ImageView) findViewById(R.id.img21);
        ImageView img2 = (ImageView) findViewById(R.id.img22);
        ImageView img3 = (ImageView) findViewById(R.id.img23);
        ImageView img4 = (ImageView) findViewById(R.id.img24);
        ImageView brand1 = (ImageView) findViewById(R.id.brand21);
        ImageView brand2 = (ImageView) findViewById(R.id.brand22);
        ImageView brand3 = (ImageView) findViewById(R.id.brand23);
        ImageView brand4 = (ImageView) findViewById(R.id.brand24);
        if (Splash.sellers.get(spin).get("0").equals("flipkart"))
            brand1.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("amazon"))
            brand1.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("paytm"))
            brand1.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("snapdeal"))
            brand1.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("flipkart"))
            brand2.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("amazon"))
            brand2.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("paytm"))
            brand2.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("snapdeal"))
            brand2.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("flipkart"))
            brand3.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("amazon"))
            brand3.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("paytm"))
            brand3.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("snapdeal"))
            brand3.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("flipkart"))
            brand4.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("amazon"))
            brand4.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("paytm"))
            brand4.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("snapdeal"))
            brand4.setImageResource(R.drawable.sdeal_fav1x);


//            card1 = (ImageView) findViewById(R.id.img1);
//            card2 = (ImageView) findViewById(R.id.imgx);
//            card3 = (ImageView) findViewById(R.id.img2);
//            card4 = (ImageView) findViewById(R.id.img2x);

        title1.setText(Splash.title.get(spin).get("0"));
        title2.setText(Splash.title.get(spin).get("1"));
        title3.setText(Splash.title.get(spin).get("2"));
        title4.setText(Splash.title.get(spin).get("3"));

        Picasso.with(this)
                .load(Splash.image.get(spin).get("0"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img1);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("1"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img2);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("2"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img3);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("3"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img4);
        Double princ1 = Double.parseDouble(Splash.selling.get(spin).get("0"));
        int monthscam = 18;
        String cat1 = Splash.category.get(spin).get("0");
        String subcat1 = Splash.subCategory.get(spin).get("0");
        int a = 2;
        monthscam = months(Splash.subCategory.get(spin).get("0"), Splash.category.get(spin).get("0"), Splash.brand.get(spin).get("0"), princ1.intValue());


        princ1 = princ1 * 0.8;
        Double rate = 0.21 / 12;

        Double emi1 = Math.floor((princ1 * rate * Math.pow(1 + rate, monthscam)) / ((Math.pow(1 + rate, monthscam)) - 1));
        if ((princ1 * 10 / 8) < 5000.0)
            emi1 = princ1 / monthscam;
        price1.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi1.intValue()) + " per month");
        //emi2
        Double princ2 = Double.parseDouble(Splash.selling.get(spin).get("1"));
        int monthscam2 = 18;
        monthscam2 = months(Splash.subCategory.get(spin).get("1"), Splash.category.get(spin).get("1"), Splash.brand.get(spin).get("1"), princ2.intValue());

        princ2 = princ2 * 0.8;
        // Double rate=0.2;
        Double emi2 = Math.floor((princ2 * rate * Math.pow(1 + rate, monthscam2)) / ((Math.pow(1 + rate, monthscam2)) - 1));
        if ((princ2 * 10 / 8) < 5000.0)
            emi2 = princ2 / monthscam2;
        price2.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi2.intValue()) + " per month");


        Double princ3 = Double.parseDouble(Splash.selling.get(spin).get("2"));
        int monthscam3 = 18;
        monthscam3 = months(Splash.subCategory.get(spin).get("2"), Splash.category.get(spin).get("2"), Splash.brand.get(spin).get("2"), princ3.intValue());
        princ3 = princ3 * 0.8;
        // Double rate=0.2;
        Double emi3 = Math.floor((princ3 * rate * Math.pow(1 + rate, monthscam3)) / ((Math.pow(1 + rate, monthscam3)) - 1));
        if ((princ3 * 10 / 8) < 5000.0)
            emi3 = princ3 / monthscam3;
        price3.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi3.intValue()) + " per month");

        //emi4

        Double princ4 = Double.parseDouble(Splash.selling.get(spin).get("3"));
        int monthscam4 = 18;
        monthscam4 = months(Splash.subCategory.get(spin).get("3"), Splash.category.get(spin).get("3"), Splash.brand.get(spin).get("3"), princ3.intValue());


        princ4 = princ4 * 0.8;
        // Double rate=0.2;
        Double emi4 = Math.floor((princ4 * rate * Math.pow(1 + rate, monthscam4)) / ((Math.pow(1 + rate, monthscam4)) - 1));
        if ((princ4 * 10 / 8) < 5000.0)
            emi4 = princ4 / monthscam4;
        price4.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi4.intValue()) + " per month");
    }

    private void populateThirdRow(){
        TextView price1 = (TextView) findViewById(R.id.title31);
        TextView price2 = (TextView) findViewById(R.id.title32);
        TextView price3 = (TextView) findViewById(R.id.title33);
        TextView price4 = (TextView) findViewById(R.id.title34);
        TextView title1 = (TextView) findViewById(R.id.titlexxx31);
        TextView title2 = (TextView) findViewById(R.id.titlexxx32);
        TextView title3 = (TextView) findViewById(R.id.titlexxx33);
        TextView title4 = (TextView) findViewById(R.id.titlexxx34);
        ImageView img1 = (ImageView) findViewById(R.id.img31);
        ImageView img2 = (ImageView) findViewById(R.id.img32);
        ImageView img3 = (ImageView) findViewById(R.id.img33);
        ImageView img4 = (ImageView) findViewById(R.id.img34);
        ImageView brand1 = (ImageView) findViewById(R.id.brand31);
        ImageView brand2 = (ImageView) findViewById(R.id.brand32);
        ImageView brand3 = (ImageView) findViewById(R.id.brand33);
        ImageView brand4 = (ImageView) findViewById(R.id.brand34);
        if (Splash.sellers.get(spin).get("0").equals("flipkart"))
            brand1.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("amazon"))
            brand1.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("paytm"))
            brand1.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("snapdeal"))
            brand1.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("flipkart"))
            brand2.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("amazon"))
            brand2.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("paytm"))
            brand2.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("snapdeal"))
            brand2.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("flipkart"))
            brand3.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("amazon"))
            brand3.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("paytm"))
            brand3.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("snapdeal"))
            brand3.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("flipkart"))
            brand4.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("amazon"))
            brand4.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("paytm"))
            brand4.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("snapdeal"))
            brand4.setImageResource(R.drawable.sdeal_fav1x);


//            card1 = (ImageView) findViewById(R.id.img1);
//            card2 = (ImageView) findViewById(R.id.imgx);
//            card3 = (ImageView) findViewById(R.id.img2);
//            card4 = (ImageView) findViewById(R.id.img2x);

        title1.setText(Splash.title.get(spin).get("0"));
        title2.setText(Splash.title.get(spin).get("1"));
        title3.setText(Splash.title.get(spin).get("2"));
        title4.setText(Splash.title.get(spin).get("3"));

        Picasso.with(this)
                .load(Splash.image.get(spin).get("0"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img1);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("1"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img2);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("2"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img3);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("3"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img4);
        Double princ1 = Double.parseDouble(Splash.selling.get(spin).get("0"));
        int monthscam = 18;
        String cat1 = Splash.category.get(spin).get("0");
        String subcat1 = Splash.subCategory.get(spin).get("0");
        int a = 2;
        monthscam = months(Splash.subCategory.get(spin).get("0"), Splash.category.get(spin).get("0"), Splash.brand.get(spin).get("0"), princ1.intValue());


        princ1 = princ1 * 0.8;
        Double rate = 0.21 / 12;

        Double emi1 = Math.floor((princ1 * rate * Math.pow(1 + rate, monthscam)) / ((Math.pow(1 + rate, monthscam)) - 1));
        if ((princ1 * 10 / 8) < 5000.0)
            emi1 = princ1 / monthscam;
        price1.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi1.intValue()) + " per month");
        //emi2
        Double princ2 = Double.parseDouble(Splash.selling.get(spin).get("1"));
        int monthscam2 = 18;
        monthscam2 = months(Splash.subCategory.get(spin).get("1"), Splash.category.get(spin).get("1"), Splash.brand.get(spin).get("1"), princ2.intValue());

        princ2 = princ2 * 0.8;
        // Double rate=0.2;
        Double emi2 = Math.floor((princ2 * rate * Math.pow(1 + rate, monthscam2)) / ((Math.pow(1 + rate, monthscam2)) - 1));
        if ((princ2 * 10 / 8) < 5000.0)
            emi2 = princ2 / monthscam2;
        price2.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi2.intValue()) + " per month");


        Double princ3 = Double.parseDouble(Splash.selling.get(spin).get("2"));
        int monthscam3 = 18;
        monthscam3 = months(Splash.subCategory.get(spin).get("2"), Splash.category.get(spin).get("2"), Splash.brand.get(spin).get("2"), princ3.intValue());
        princ3 = princ3 * 0.8;
        // Double rate=0.2;
        Double emi3 = Math.floor((princ3 * rate * Math.pow(1 + rate, monthscam3)) / ((Math.pow(1 + rate, monthscam3)) - 1));
        if ((princ3 * 10 / 8) < 5000.0)
            emi3 = princ3 / monthscam3;
        price3.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi3.intValue()) + " per month");

        //emi4

        Double princ4 = Double.parseDouble(Splash.selling.get(spin).get("3"));
        int monthscam4 = 18;
        monthscam4 = months(Splash.subCategory.get(spin).get("3"), Splash.category.get(spin).get("3"), Splash.brand.get(spin).get("3"), princ3.intValue());


        princ4 = princ4 * 0.8;
        // Double rate=0.2;
        Double emi4 = Math.floor((princ4 * rate * Math.pow(1 + rate, monthscam4)) / ((Math.pow(1 + rate, monthscam4)) - 1));
        if ((princ4 * 10 / 8) < 5000.0)
            emi4 = princ4 / monthscam4;
        price4.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi4.intValue()) + " per month");

    }

    private void populateFouthRow(){
        TextView price1 = (TextView) findViewById(R.id.title41);
        TextView price2 = (TextView) findViewById(R.id.title42);
        TextView price3 = (TextView) findViewById(R.id.title43);
        TextView price4 = (TextView) findViewById(R.id.title44);
        TextView title1 = (TextView) findViewById(R.id.titlexxx41);
        TextView title2 = (TextView) findViewById(R.id.titlexxx42);
        TextView title3 = (TextView) findViewById(R.id.titlexxx43);
        TextView title4 = (TextView) findViewById(R.id.titlexxx44);
        ImageView img1 = (ImageView) findViewById(R.id.img41);
        ImageView img2 = (ImageView) findViewById(R.id.img42);
        ImageView img3 = (ImageView) findViewById(R.id.img43);
        ImageView img4 = (ImageView) findViewById(R.id.img44);
        ImageView brand1 = (ImageView) findViewById(R.id.brand41);
        ImageView brand2 = (ImageView) findViewById(R.id.brand42);
        ImageView brand3 = (ImageView) findViewById(R.id.brand43);
        ImageView brand4 = (ImageView) findViewById(R.id.brand44);
        if (Splash.sellers.get(spin).get("0").equals("flipkart"))
            brand1.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("amazon"))
            brand1.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("paytm"))
            brand1.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("snapdeal"))
            brand1.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("flipkart"))
            brand2.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("amazon"))
            brand2.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("paytm"))
            brand2.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("snapdeal"))
            brand2.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("flipkart"))
            brand3.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("amazon"))
            brand3.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("paytm"))
            brand3.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("snapdeal"))
            brand3.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("flipkart"))
            brand4.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("amazon"))
            brand4.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("paytm"))
            brand4.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("snapdeal"))
            brand4.setImageResource(R.drawable.sdeal_fav1x);


//            card1 = (ImageView) findViewById(R.id.img1);
//            card2 = (ImageView) findViewById(R.id.imgx);
//            card3 = (ImageView) findViewById(R.id.img2);
//            card4 = (ImageView) findViewById(R.id.img2x);

        title1.setText(Splash.title.get(spin).get("0"));
        title2.setText(Splash.title.get(spin).get("1"));
        title3.setText(Splash.title.get(spin).get("2"));
        title4.setText(Splash.title.get(spin).get("3"));

        Picasso.with(this)
                .load(Splash.image.get(spin).get("0"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img1);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("1"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img2);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("2"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img3);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("3"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img4);
        Double princ1 = Double.parseDouble(Splash.selling.get(spin).get("0"));
        int monthscam = 18;
        String cat1 = Splash.category.get(spin).get("0");
        String subcat1 = Splash.subCategory.get(spin).get("0");
        int a = 2;
        monthscam = months(Splash.subCategory.get(spin).get("0"), Splash.category.get(spin).get("0"), Splash.brand.get(spin).get("0"), princ1.intValue());


        princ1 = princ1 * 0.8;
        Double rate = 0.21 / 12;

        Double emi1 = Math.floor((princ1 * rate * Math.pow(1 + rate, monthscam)) / ((Math.pow(1 + rate, monthscam)) - 1));
        if ((princ1 * 10 / 8) < 5000.0)
            emi1 = princ1 / monthscam;
        price1.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi1.intValue()) + " per month");
        //emi2
        Double princ2 = Double.parseDouble(Splash.selling.get(spin).get("1"));
        int monthscam2 = 18;
        monthscam2 = months(Splash.subCategory.get(spin).get("1"), Splash.category.get(spin).get("1"), Splash.brand.get(spin).get("1"), princ2.intValue());

        princ2 = princ2 * 0.8;
        // Double rate=0.2;
        Double emi2 = Math.floor((princ2 * rate * Math.pow(1 + rate, monthscam2)) / ((Math.pow(1 + rate, monthscam2)) - 1));
        if ((princ2 * 10 / 8) < 5000.0)
            emi2 = princ2 / monthscam2;
        price2.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi2.intValue()) + " per month");


        Double princ3 = Double.parseDouble(Splash.selling.get(spin).get("2"));
        int monthscam3 = 18;
        monthscam3 = months(Splash.subCategory.get(spin).get("2"), Splash.category.get(spin).get("2"), Splash.brand.get(spin).get("2"), princ3.intValue());
        princ3 = princ3 * 0.8;
        // Double rate=0.2;
        Double emi3 = Math.floor((princ3 * rate * Math.pow(1 + rate, monthscam3)) / ((Math.pow(1 + rate, monthscam3)) - 1));
        if ((princ3 * 10 / 8) < 5000.0)
            emi3 = princ3 / monthscam3;
        price3.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi3.intValue()) + " per month");

        //emi4

        Double princ4 = Double.parseDouble(Splash.selling.get(spin).get("3"));
        int monthscam4 = 18;
        monthscam4 = months(Splash.subCategory.get(spin).get("3"), Splash.category.get(spin).get("3"), Splash.brand.get(spin).get("3"), princ3.intValue());


        princ4 = princ4 * 0.8;
        // Double rate=0.2;
        Double emi4 = Math.floor((princ4 * rate * Math.pow(1 + rate, monthscam4)) / ((Math.pow(1 + rate, monthscam4)) - 1));
        if ((princ4 * 10 / 8) < 5000.0)
            emi4 = princ4 / monthscam4;
        price4.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi4.intValue()) + " per month");
    }


    private void populateFifthRow(){
        TextView price1 = (TextView) findViewById(R.id.title51);
        TextView price2 = (TextView) findViewById(R.id.title52);
        TextView price3 = (TextView) findViewById(R.id.title53);
        TextView price4 = (TextView) findViewById(R.id.title54);
        TextView title1 = (TextView) findViewById(R.id.titlexxx51);
        TextView title2 = (TextView) findViewById(R.id.titlexxx52);
        TextView title3 = (TextView) findViewById(R.id.titlexxx53);
        TextView title4 = (TextView) findViewById(R.id.titlexxx54);
        ImageView img1 = (ImageView) findViewById(R.id.img51);
        ImageView img2 = (ImageView) findViewById(R.id.img52);
        ImageView img3 = (ImageView) findViewById(R.id.img53);
        ImageView img4 = (ImageView) findViewById(R.id.img54);
        ImageView brand1 = (ImageView) findViewById(R.id.brand51);
        ImageView brand2 = (ImageView) findViewById(R.id.brand52);
        ImageView brand3 = (ImageView) findViewById(R.id.brand53);
        ImageView brand4 = (ImageView) findViewById(R.id.brand54);
        if (Splash.sellers.get(spin).get("0").equals("flipkart"))
            brand1.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("amazon"))
            brand1.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("paytm"))
            brand1.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("snapdeal"))
            brand1.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("flipkart"))
            brand2.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("amazon"))
            brand2.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("paytm"))
            brand2.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("snapdeal"))
            brand2.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("flipkart"))
            brand3.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("amazon"))
            brand3.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("paytm"))
            brand3.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("snapdeal"))
            brand3.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("flipkart"))
            brand4.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("amazon"))
            brand4.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("paytm"))
            brand4.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("snapdeal"))
            brand4.setImageResource(R.drawable.sdeal_fav1x);


//            card1 = (ImageView) findViewById(R.id.img1);
//            card2 = (ImageView) findViewById(R.id.imgx);
//            card3 = (ImageView) findViewById(R.id.img2);
//            card4 = (ImageView) findViewById(R.id.img2x);

        title1.setText(Splash.title.get(spin).get("0"));
        title2.setText(Splash.title.get(spin).get("1"));
        title3.setText(Splash.title.get(spin).get("2"));
        title4.setText(Splash.title.get(spin).get("3"));

        Picasso.with(this)
                .load(Splash.image.get(spin).get("0"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img1);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("1"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img2);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("2"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img3);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("3"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img4);
        Double princ1 = Double.parseDouble(Splash.selling.get(spin).get("0"));
        int monthscam = 18;
        String cat1 = Splash.category.get(spin).get("0");
        String subcat1 = Splash.subCategory.get(spin).get("0");
        int a = 2;
        monthscam = months(Splash.subCategory.get(spin).get("0"), Splash.category.get(spin).get("0"), Splash.brand.get(spin).get("0"), princ1.intValue());


        princ1 = princ1 * 0.8;
        Double rate = 0.21 / 12;

        Double emi1 = Math.floor((princ1 * rate * Math.pow(1 + rate, monthscam)) / ((Math.pow(1 + rate, monthscam)) - 1));
        if ((princ1 * 10 / 8) < 5000.0)
            emi1 = princ1 / monthscam;
        price1.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi1.intValue()) + " per month");
        //emi2
        Double princ2 = Double.parseDouble(Splash.selling.get(spin).get("1"));
        int monthscam2 = 18;
        monthscam2 = months(Splash.subCategory.get(spin).get("1"), Splash.category.get(spin).get("1"), Splash.brand.get(spin).get("1"), princ2.intValue());

        princ2 = princ2 * 0.8;
        // Double rate=0.2;
        Double emi2 = Math.floor((princ2 * rate * Math.pow(1 + rate, monthscam2)) / ((Math.pow(1 + rate, monthscam2)) - 1));
        if ((princ2 * 10 / 8) < 5000.0)
            emi2 = princ2 / monthscam2;
        price2.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi2.intValue()) + " per month");


        Double princ3 = Double.parseDouble(Splash.selling.get(spin).get("2"));
        int monthscam3 = 18;
        monthscam3 = months(Splash.subCategory.get(spin).get("2"), Splash.category.get(spin).get("2"), Splash.brand.get(spin).get("2"), princ3.intValue());
        princ3 = princ3 * 0.8;
        // Double rate=0.2;
        Double emi3 = Math.floor((princ3 * rate * Math.pow(1 + rate, monthscam3)) / ((Math.pow(1 + rate, monthscam3)) - 1));
        if ((princ3 * 10 / 8) < 5000.0)
            emi3 = princ3 / monthscam3;
        price3.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi3.intValue()) + " per month");

        //emi4

        Double princ4 = Double.parseDouble(Splash.selling.get(spin).get("3"));
        int monthscam4 = 18;
        monthscam4 = months(Splash.subCategory.get(spin).get("3"), Splash.category.get(spin).get("3"), Splash.brand.get(spin).get("3"), princ3.intValue());


        princ4 = princ4 * 0.8;
        // Double rate=0.2;
        Double emi4 = Math.floor((princ4 * rate * Math.pow(1 + rate, monthscam4)) / ((Math.pow(1 + rate, monthscam4)) - 1));
        if ((princ4 * 10 / 8) < 5000.0)
            emi4 = princ4 / monthscam4;
        price4.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi4.intValue()) + " per month");
    }

    private void populateSixthRow(){
        TextView price1 = (TextView) findViewById(R.id.title61);
        TextView price2 = (TextView) findViewById(R.id.title62);
        TextView price3 = (TextView) findViewById(R.id.title63);
        TextView price4 = (TextView) findViewById(R.id.title64);
        TextView title1 = (TextView) findViewById(R.id.titlexxx61);
        TextView title2 = (TextView) findViewById(R.id.titlexxx62);
        TextView title3 = (TextView) findViewById(R.id.titlexxx63);
        TextView title4 = (TextView) findViewById(R.id.titlexxx64);
        ImageView img1 = (ImageView) findViewById(R.id.img61);
        ImageView img2 = (ImageView) findViewById(R.id.img62);
        ImageView img3 = (ImageView) findViewById(R.id.img63);
        ImageView img4 = (ImageView) findViewById(R.id.img64);
        ImageView brand1 = (ImageView) findViewById(R.id.brand61);
        ImageView brand2 = (ImageView) findViewById(R.id.brand62);
        ImageView brand3 = (ImageView) findViewById(R.id.brand63);
        ImageView brand4 = (ImageView) findViewById(R.id.brand64);
        if (Splash.sellers.get(spin).get("0").equals("flipkart"))
            brand1.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("amazon"))
            brand1.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("paytm"))
            brand1.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("0").equals("snapdeal"))
            brand1.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("flipkart"))
            brand2.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("amazon"))
            brand2.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("paytm"))
            brand2.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("1").equals("snapdeal"))
            brand2.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("flipkart"))
            brand3.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("amazon"))
            brand3.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("paytm"))
            brand3.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("2").equals("snapdeal"))
            brand3.setImageResource(R.drawable.sdeal_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("flipkart"))
            brand4.setImageResource(R.drawable.fk_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("amazon"))
            brand4.setImageResource(R.drawable.amazon_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("paytm"))
            brand4.setImageResource(R.drawable.paytm_fav1x);
        if (Splash.sellers.get(spin).get("3").equals("snapdeal"))
            brand4.setImageResource(R.drawable.sdeal_fav1x);


//            card1 = (ImageView) findViewById(R.id.img1);
//            card2 = (ImageView) findViewById(R.id.imgx);
//            card3 = (ImageView) findViewById(R.id.img2);
//            card4 = (ImageView) findViewById(R.id.img2x);

        title1.setText(Splash.title.get(spin).get("0"));
        title2.setText(Splash.title.get(spin).get("1"));
        title3.setText(Splash.title.get(spin).get("2"));
        title4.setText(Splash.title.get(spin).get("3"));

        Picasso.with(this)
                .load(Splash.image.get(spin).get("0"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img1);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("1"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img2);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("2"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img3);
        Picasso.with(this)
                .load(Splash.image.get(spin).get("3"))
                .placeholder(R.drawable.emptyimageproducts)
                .into(img4);
        Double princ1 = Double.parseDouble(Splash.selling.get(spin).get("0"));
        int monthscam = 18;
        String cat1 = Splash.category.get(spin).get("0");
        String subcat1 = Splash.subCategory.get(spin).get("0");
        int a = 2;
        monthscam = months(Splash.subCategory.get(spin).get("0"), Splash.category.get(spin).get("0"), Splash.brand.get(spin).get("0"), princ1.intValue());


        princ1 = princ1 * 0.8;
        Double rate = 0.21 / 12;

        Double emi1 = Math.floor((princ1 * rate * Math.pow(1 + rate, monthscam)) / ((Math.pow(1 + rate, monthscam)) - 1));
        if ((princ1 * 10 / 8) < 5000.0)
            emi1 = princ1 / monthscam;
        price1.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi1.intValue()) + " per month");
        //emi2
        Double princ2 = Double.parseDouble(Splash.selling.get(spin).get("1"));
        int monthscam2 = 18;
        monthscam2 = months(Splash.subCategory.get(spin).get("1"), Splash.category.get(spin).get("1"), Splash.brand.get(spin).get("1"), princ2.intValue());

        princ2 = princ2 * 0.8;
        // Double rate=0.2;
        Double emi2 = Math.floor((princ2 * rate * Math.pow(1 + rate, monthscam2)) / ((Math.pow(1 + rate, monthscam2)) - 1));
        if ((princ2 * 10 / 8) < 5000.0)
            emi2 = princ2 / monthscam2;
        price2.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi2.intValue()) + " per month");


        Double princ3 = Double.parseDouble(Splash.selling.get(spin).get("2"));
        int monthscam3 = 18;
        monthscam3 = months(Splash.subCategory.get(spin).get("2"), Splash.category.get(spin).get("2"), Splash.brand.get(spin).get("2"), princ3.intValue());
        princ3 = princ3 * 0.8;
        // Double rate=0.2;
        Double emi3 = Math.floor((princ3 * rate * Math.pow(1 + rate, monthscam3)) / ((Math.pow(1 + rate, monthscam3)) - 1));
        if ((princ3 * 10 / 8) < 5000.0)
            emi3 = princ3 / monthscam3;
        price3.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi3.intValue()) + " per month");

        //emi4

        Double princ4 = Double.parseDouble(Splash.selling.get(spin).get("3"));
        int monthscam4 = 18;
        monthscam4 = months(Splash.subCategory.get(spin).get("3"), Splash.category.get(spin).get("3"), Splash.brand.get(spin).get("3"), princ3.intValue());


        princ4 = princ4 * 0.8;
        // Double rate=0.2;
        Double emi4 = Math.floor((princ4 * rate * Math.pow(1 + rate, monthscam4)) / ((Math.pow(1 + rate, monthscam4)) - 1));
        if ((princ4 * 10 / 8) < 5000.0)
            emi4 = princ4 / monthscam4;
        price4.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi4.intValue()) + " per month");
    }


////



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

        return m;
    }

    public void parse(String parseString) {
        SharedPreferences cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = cred.edit();
        et.putString("urlprod",parseString);
        et.commit();
        productId = "";
        int pos = -1;
        if (parseString.contains("flipkart")) {
            sellerNme = "flipkart";
            pos = parseString.indexOf("pid");
            if (pos != -1) {
                for (int j = pos + 4; ; j++) {
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

//    public void clickpaste() {
//        paste.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//
//                    query.requestFocus();
//                    query.setText("");
//                    ClipData abc = myClipboard.getPrimaryClip();
//                    ClipData.Item item = abc.getItemAt(0);
//                    String text = item.getText().toString();
//
//
//                    query.setText("   " + text);
//
//                    paste.setVisibility(View.GONE);
//
//                } catch (Exception e) {
//                    Toast.makeText(HomePage.this, "Please copy a URL", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

//    private class GetTrendingProducts extends AsyncTask<String, Void, String> {
//
//
//        @Override
//        protected String doInBackground(String... params) {
//            String url = "https://ssl.hellobuddy.in/api/product/trending?category=Computers&category=Mobiles&count=4";
//            String urldisplay = params[0];
//            try {
//                String tok_sp = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjY1M2M2YTUwZTQzNzgyNjc0M2YyNjYiLCJuYW1lIjoiYnVkZHkgYXBpIGFkbWluIiwidXNlcm5hbWUiOiJidWRkeWFwaWFkbWluIiwicGFzc3dvcmQiOiJtZW1vbmdvc2gxIiwiZW1haWwiOiJjYXJlQGhlbGxvYnVkZHkuaW4iLCJpYXQiOjE0NjU0MTMwMjksImV4cCI6MTQ2NTQ0OTAyOX0.H8903Jzuu4cLhMgtHBCvHCAhjWc1le2deEEI87qIKsk";
//
//                HttpResponse response = AppUtils.connectToServerGet(url, tok_sp, null);
//                if (response != null) {
//                    HttpEntity ent = response.getEntity();
//                    String responseString = EntityUtils.toString(ent, "UTF-8");
//                    if (response.getStatusLine().getStatusCode() != 200) {
//
//
//                        Log.e("MeshCommunication", "Server returned code "
//                                + response.getStatusLine().getStatusCode());
//                        return "fail";
//                    } else {
//
//                        JSONObject resp = new JSONObject(responseString);
//                        if (resp.getString("status").equals("success")) {
//                            JSONArray data1 = new JSONArray(resp.getString("data"));
//                            int lenght = data1.length();
//                            for (int j = 0; j < lenght; j++) {
//                                JSONObject js = data1.getJSONObject(j);
//                                TrendingProducts trendingProducts = new TrendingProducts();
//                                String categ = js.getString("category");
//                                String subc = js.getString("subCategory");
//                                String brand1 = js.getString("brand");
//                                String id = js.getString("title");
//                                String mrp = js.getString("mrp");
//                                String seller = js.getString("seller");
//                                String fkid = js.getString("fkProductId");
//                                String selling_price = js.getString("sellingPrice");
//                                JSONObject img = new JSONObject(js.getString("imgUrls"));
//                                String imgurl = img.getString("200x200");
////                                trendingProducts.setCategory(categ);
////                                trendingProducts.setSubcategory(subc);
////                                trendingProducts.setBrand(brand1);
////                                trendingProducts.setMrp1(mrp);
////                                trendingProducts.setTitle(id);
////                                trendingProducts.setFkid1(fkid);
////                                trendingProducts.setSelling(selling_price);
//                                category.get(urldisplay).put(String.valueOf(j), categ);
//                                subCategory.get(urldisplay).put(String.valueOf(j), subc);
//                                brand.get(urldisplay).put(String.valueOf(j), brand1);
//                                image.get(urldisplay).put(String.valueOf(j), imgurl);
//                                mrp1.get(urldisplay).put(String.valueOf(j), mrp);
//                                title.get(urldisplay).put(String.valueOf(j), id);
//                                fkid1.get(urldisplay).put(String.valueOf(j), fkid);
//                                selling.get(urldisplay).put(String.valueOf(j), selling_price);
//                                sellers.get(urldisplay).put(String.valueOf(j), seller);
//
//
//                            }
//                            Log.i("trending", "called");
//                            return "win";
//                            //versioncode=data1.getString("version_code");
//                            //return versioncode;
//                        } else
//                            return "fail";
//
//
//                    }
//                }
//
//            } catch (JSONException e1) {
//                e1.printStackTrace();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            return null;
//        }
//    }

    public class AuthTokc extends

                          AsyncTask<String, Void, String> {


//        public void AsynchTaskTimer() {
//            final Handler handler = new Handler();
//
//            TimerTask timertask = new TimerTask() {
//                @Override
//                public void run() {
//                    handler.post(new Runnable() {
//                        public void run() {
//                            try{
//                                if (page1 > 3) { // In my case the number of pages are 5
//                                    timer.cancel();
//                                    imageSlider.setCurrentItem(0);
//
//                                } else {
//                                    imageSlider.setCurrentItem(page1++);
//                                }
//                            } catch (Exception e) {
//                                // TODO Auto-generated catch block
//                            }
//                        }
//                    });
//                }
//            };
//            timer = new Timer(); //This is new
//            timer.schedule(timertask, 0, 5000); // execute in every 15sec
//        }

        private String apiN = "";

        //        Context context;
        //    Splash obj=new Splash();
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
                        String token1 = "";

                        SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorP = userP.edit();
                        token1 = resp.getString("token");
                        editorP.putString("token_value", token1);
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
            if (result.equals("win")) {


            }
        }

    }

//    class RemindTask extends TimerTask {
//        @Override
//        public void run() {
//
//            // As the TimerTask run on a seprate thread from UI thread we have
//            // to call runOnUiThread to do work on UI thread.
//            runOnUiThread(new Runnable() {
//                public void run() {
//
//                    if (page1 > 3) { // In my case the number of pages are 5
//
//                        imageSlider.setCurrentItem(0);
//
//
//
//                        // Showing a toast for just testing purpose
//                        //Toast.makeText(getApplicationContext(), "Timer stoped",
//                        // Toast.LENGTH_LONG).show();
//                    } else {
//                        imageSlider.setCurrentItem(page1++);
//                    }
//                }
//            });
//
//        }
//
//    }



    public class linkSearch extends
                            AsyncTask<String, Void, String> {
        @Override
        public void onPreExecute() {
            //            spinner.setVisibility(View.VISIBLE);

        }


        @Override

        public String doInBackground(String... data) {

            //  String urldisplay = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: get
                String url = getApplicationContext().getString(R.string.server) + "api/product?productId=" + productId + "&seller=" + sellerNme + "&userid=" + cred.getString("phone_number", "");

                // payload.put("action", details.get("action"));


                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                HttpGet httppost = new HttpGet(url);
                httppost.setHeader("x-access-token", token);
                httppost.setHeader("Content-Type", "application/json");


                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");
                if (response.getStatusLine().getStatusCode() != 200) {
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);
                    if (resp.getString("status").equals("success")) {
                        JSONObject data1 = new JSONObject(resp.getString("data"));
                        searchTitle = data1.getString("title");
                        searchBrand = data1.getString("brand");
                        searchCategory = data1.getString("category");
                        searchSubcategory = data1.getString("subCategory");
                        searchPrice = data1.getInt("sellingPrice");

                        JSONObject img = new JSONObject(data1.getString("imgUrls"));
                        urlImg = img.getString("400x400");
                        //                        infor=data1.getString("")
                        try {
                            specification = data1.getString("specificaiton");
                        } catch (Exception e) {
                            specification = "";
                        }
                        try {
                            description = data1.getString("description");
                        } catch (Exception e) {
                            description = "";
                        }
                        try {
                            review = data1.getString("fkProductUrl");
                        } catch (Exception e) {
                            review = "";
                        }
                        infor = "The minimum downpayment is 20% of the product price and also depends on the payment band (Oxygen/Silicon/Palladium/Krypton) you lie in, which you will get to know after your college ID verification.";


                        return "win";


                    }

                }
            } catch (Exception e) {
            }
            return "";
        }

        protected void onPostExecute(String result) {
            if (!result.equals("win")) {
                System.out.println("Error while computing data");
            } else {

                monthsallowed = months(searchSubcategory, searchCategory, searchBrand, searchPrice);
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
                            monthscheck = months;
                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (monthsallowed > monthscheck)
                    monthsallowed = monthscheck;

                Double rate = 21.0 / 1200.0;
                int d = 0;
                if (searchPrice <= 5000) {
                    emi = searchPrice * 0.8 / monthsallowed;
                } else {
                    if (currDay <= 15)
                        d = 35 - currDay;
                    else
                        d = 65 - currDay;

                    emi = Math.floor((searchPrice * 0.8 * rate * Math.pow(1 + rate, monthsallowed - 1) * (1 + rate * d * 12 / 365)) / (Math.pow(1 + rate, monthsallowed) - 1));

                }
                String q = query.getText().toString();
                Intent in = new Intent(HomePage.this, ProductsPage.class);
                in.putExtra("title", searchTitle);
                try {
                    Map userMap = new HashMap<>();
                    userMap.put("PRODUCT_TITLE", searchTitle);
                    //                    userMap.put("email", mEmail);
                    //                    userMap.put("user_id", mPhone);
                    //                    userMap.put("phone", mPhone);
                    //                    System.out.println("Intercom data 4" + mPhone);
                    Intercom.client().updateUser(userMap);
                } catch (Exception e) {


                    System.out.println("Intercom two" + e.toString());
                }
                in.putExtra("price", searchPrice);
                in.putExtra("brand", searchBrand);
                in.putExtra("name", name);
                in.putExtra("image", urlImg);
                in.putExtra("emi", emi);
                in.putExtra("desc", "");


                in.putExtra("monthsallowed", monthsallowed);
                in.putExtra("seller", sellerNme);
                in.putExtra("query", q);
                in.putExtra("page", page);

                startActivity(in);
                query.setText("");

                //                    Toast.makeText(HomePage.this, String.valueOf(emi), Toast.LENGTH_SHORT).show();


            }
            //else


        }
    }
}

