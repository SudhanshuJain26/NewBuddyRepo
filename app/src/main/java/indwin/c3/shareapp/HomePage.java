package indwin.c3.shareapp;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.intercom.android.sdk.Intercom;

public class HomePage extends AppCompatActivity {
    private String[] arraySpinner;
    WebView form;
    private Intent intform;
    private NavigationView navigationView;
    private ImageView card1, card2, card3, card4;
    private String formstatus, name, fbid, rejectionReason, email, uniqueCode, verificationdate, creditLimit, searchTitle, searchBrand, searchCategory, searchSubcategory;
    SharedPreferences cred;
    int screen_no;
    private int searchPrice = 0;
    private String urlImg = "",page="";

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
    private TextView paste, tren, products, lappy, fashion, beau, ent, foot, but;
    //    Map<String,Map<int,V>> map;
//    HashMap<String, HashMap<String,String>> image;
    private android.content.ClipboardManager myClipboard;
    private String spin = "";
    private String productId = "";
    private int checkValidUrl = 0,monthsallowed=0;
    private Double emi = 0.0;
    private int checkValidFromApis = 0;
    private String sellerNme = "";
    private String token = "";
    private SharedPreferences userP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userP = getSharedPreferences("token", Context.MODE_PRIVATE);
        cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
        //userid=ss.getString("phone_number", "");
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

            ImageView img1 = (ImageView) findViewById(R.id.img1);
            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(HomePage
                            .this, "clicked", Toast.LENGTH_SHORT).show();

                }
            });
            newtren = (RelativeLayout) findViewById(R.id.newtr);
            tren = (TextView) findViewById(R.id.trending);
            newtren.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    drops = (RelativeLayout) findViewById(R.id.drops);
                    newtren.setVisibility(View.GONE);
                    View viewDrops = findViewById(R.id.drops);
                    Animation animShow = AnimationUtils.loadAnimation(HomePage.this, R.anim.show); //- See more at: http://findnerd.com/list/view/HideShow-a-View-with-slide-updown-animation-in-Android/2537/#sthash.0WR4fLwr.dpuf

                    drops.setVisibility(View.VISIBLE);
                    // drops.setAnimation(animShow);
                    query.clearFocus();
                    hideSoftKeyboard(HomePage.this);
                    //   drops.setVisibility(View.VISIBLE);


                }
            });
            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                @Override
                public void onScrollChanged() {

                    query.clearFocus();
                    hideSoftKeyboard(HomePage.this);                     //for verticalScrollView
                    //DO SOMETHING WITH THE SCROLL COORDINATES

                }
            });

            products = (TextView) findViewById(R.id.products);
            products.setTypeface(Typeface.DEFAULT_BOLD);
            lappy = (TextView) findViewById(R.id.lappy);
            fashion = (TextView) findViewById(R.id.fashion);
            beau = (TextView) findViewById(R.id.beau);
            ent = (TextView) findViewById(R.id.ent);
            foot = (TextView) findViewById(R.id.foot);
            spin = "Mobiles";
            try {
                populate();
            } catch (Exception e) {
            }
            try {
                cardclick();
            } catch (Exception e) {
            }
            products.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tren.setText("ALL PRODUCTS");
                    drops.setVisibility(View.GONE);
                    products.setTypeface(Typeface.DEFAULT_BOLD);
                    lappy.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    beau.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    ent.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    foot.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    fashion.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

                    newtren.setVisibility(View.VISIBLE);
                    tren.setText("MOBILES");
                    spin = "Mobiles";
                    try {
                        populate();
                        cardclick();
                    } catch (Exception e) {
                    }


                }
            });
            lappy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tren.setText("LAPTOPS");
                    drops.setVisibility(View.GONE);
                    newtren.setVisibility(View.VISIBLE);
                    lappy.setTypeface(Typeface.DEFAULT_BOLD);
                    spin = "Laptops";
                    products.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    beau.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    ent.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    foot.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    fashion.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    try {
                        populate();
                        cardclick();
                    } catch (Exception e) {
                    }

                }
            });
            fashion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tren.setText("FASHION");

                    drops.setVisibility(View.GONE);
                    newtren.setVisibility(View.VISIBLE);
                    fashion.setTypeface(Typeface.DEFAULT_BOLD);
                    spin = "apparels";
                    lappy.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    beau.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    ent.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    foot.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    products.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    try {
                        populate();
                        cardclick();
                    } catch (Exception e) {
                    }
                }
            });
            beau.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tren.setText("BEAUTY AND PERSONAL CARE");
                    drops.setVisibility(View.GONE);
                    newtren.setVisibility(View.VISIBLE);
                    beau.setTypeface(Typeface.DEFAULT_BOLD);
                    spin = "homeandbeauty";
                    lappy.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    products.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    ent.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    foot.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    fashion.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    try {
                        populate();
                        cardclick();
                    } catch (Exception e) {
                    }
                }
            });
            ent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tren.setText("ENTERTAINMENT");
                    drops.setVisibility(View.GONE);
                    ent.setTypeface(Typeface.DEFAULT_BOLD);
                    newtren.setVisibility(View.VISIBLE);
                    spin = "Electronics";
                    lappy.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    beau.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    products.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    foot.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    fashion.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    try {
                        populate();
                        cardclick();
                    } catch (Exception e) {
                    }
                }
            });
            foot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tren.setText("FOOTWEAR");
                    drops.setVisibility(View.GONE);
                    newtren.setVisibility(View.VISIBLE);
                    foot.setTypeface(Typeface.DEFAULT_BOLD);
                    lappy.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    beau.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    ent.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    products.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    fashion.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    spin = "Footwear";
                    try {
                        populate();
                        cardclick();
                    } catch (Exception e) {
                    }
                }
            });


            FloatingActionButton intercom = (FloatingActionButton) findViewById(R.id.chat);
            intercom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intercom.client().displayMessageComposer();
                }
            });
            try {
                toolbar = (Toolbar) findViewById(R.id.toolbarc);

                setSupportActionBar(toolbar);
//getSupportActionBar().setBackgroundDrawable(R.id.buddylogo_blue);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            } catch (Exception e) {
                System.out.println(e.toString() + "oscar goes");
            }
            ImageView inter = (ImageView) findViewById(R.id.interCom);
            inter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intercom.client().displayMessageComposer();
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
                    paste = (TextView) findViewById(R.id.pasteAg);
                    clickpaste();

                }
            });
            TextView name1, line1, line2, but;
            name1 = (TextView) findViewById(R.id.nameintr);
            line1 = (TextView) findViewById(R.id.line1);
            line2 = (TextView) findViewById(R.id.line2);
            TextView line3 = (TextView) findViewById(R.id.line3);
            but = (TextView) findViewById(R.id.but);
            name1.setText("Hi " + name + ",");
            if (formstatus.equals("empty")) {
                line1.setText("Ready to get started? Complete your profile now to get a Borrowing Limit and start shopping now!");
                //line2.setText("");
                line3.setVisibility(View.VISIBLE);
                line3.setText("");
                but.setText("Complete it now!");
            } else if (formstatus.equals("saved")) {
                line1.setText("Ready to get started? Complete your profile now to get a Borrowing Limit and start shopping now!");
                line2.setText("");
                line3.setVisibility(View.VISIBLE);
                line3.setText("");
                but.setText("Complete it now!");
            } else if (formstatus.equals("flashApproved")) {
                line1.setText("Congrats! You have been approved for Rs.1000 flash Credit Limit! ");
                line2.setText("");
                line3.setVisibility(View.VISIBLE);
                line3.setText("");
                but.setText("Increase it further!");
            } else if (formstatus.equals("approved")) {

                line1.setText("You have been approved for a Borrowing Limit of Rs. " + creditLimit + ". Go Crazy");
//                line2.setText("");
                but.setVisibility(View.GONE);
                ImageView i = (ImageView) findViewById(R.id.app);
                i.setVisibility(View.VISIBLE);

            } else if (formstatus.equals("declined")) {
                line1.setText("Sorry, but we are unable to approve a Borrowing Limit for you! Find out why!");
//                line2.setText("");
                but.setText("");
            } else if (formstatus.equals("submitted")) {
                if (screen_no == 1) {
//upload
                    line1.setText("We have received your details but your documents are not yet uploaded.");
//                    line2.setText("");

                    but.setText("Upload 'em now");

                } else if (screen_no == 2) {
                    //setupverif
                    line1.setText("Your profile is almost complete, but you still haven't scheduled your college id verification date.");
//                    line2.setText("");
//                    line3.setVisibility(View.VISIBLE);
//                    line3.setText("");
                    but.setText("Schedule it now!");

                } else if (screen_no == 3) {
                    line1.setText("Your verification has been scheduled on:");
                    line2.setText(verificationdate);
                    ;
                    but.setText("Okie Dokie");
                    //verif

                }

            }

            but = (TextView) findViewById(R.id.but);
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    query.clearFocus();
                    send();

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


                    //Checking if the item is in checked state or not, if not make it in checked state
//                if(menuItem.isChecked()) menuItem.setChecked(false);
//                else menuItem.setChecked(true);
//                navigationView.getMenu().getItem(1).setChecked(true);
                    //Closing drawer on item click
                    drawerLayout.closeDrawers();

                    switch (menuItem.getItemId()) {

                        case R.id.MyAccount:
                            intform = new Intent(HomePage.this, ProductsPage.class);
//                            Splash.checkNot=1;
//                            paste = (TextView) findViewById(R.id.pasteAg);
//                            clickpaste();
                            startActivity(intform);
                            return true;
                        case R.id.work:


                            intform = new Intent(HomePage.this, ViewForm.class);

                            Splash.checkNot = 1;
                            paste = (TextView) findViewById(R.id.pasteAg);
                            clickpaste();
                            intform.putExtra("which_page", 11);
                            intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                            startActivity(intform);
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.About:
                            Splash.checkNot = 1;
                            paste = (TextView) findViewById(R.id.pasteAg);
                            clickpaste();
                            intform = new Intent(HomePage.this, ViewForm.class);
//                        else
//                            intform=new Intent(Formempty.this, FacebookAuth.class);
                            // finish();
                            intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                            intform.putExtra("which_page", 3);
                            //  finish();
                            startActivity(intform);
                            overridePendingTransition(0, 0);

//                        //if(!fbid.equals("empty"))
//                        intform=new Intent(Formempty.this, ViewForm.class);
////                        else
////                            intform=new Intent(Formempty.this, FacebookAuth.class);
//                        finish();
//                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
//                        intform.putExtra("which_page",3);
//                        finish();
//                        startActivity(intform);
//                        overridePendingTransition(0, 0);
                            return true;
                        case R.id.app_form:
                            Splash.checkNot = 1;
                            paste = (TextView) findViewById(R.id.pasteAg);
                            clickpaste();
                            send();

                            return true;
                        case R.id.faq:
                            navigationView.getMenu().getItem(0).setChecked(true);
                            // Intent in//
                            Splash.checkNot = 1;
                            paste = (TextView) findViewById(R.id.pasteAg);
                            clickpaste();
                            intform = new Intent(HomePage.this, ViewForm.class);
//                        else
//                            intform=new Intent(Formempty.this, FacebookAuth.class);

                            intform.putExtra("url", "http://hellobuddy.in/#/faqs");
                            intform.putExtra("which_page", 5);
                            //     finish();
                            startActivity(intform);
                            overridePendingTransition(0, 0);
                            return true;

                        case R.id.security:
                            Splash.checkNot = 1;
                            paste = (TextView) findViewById(R.id.pasteAg);
                            clickpaste();

//                        if(!fbid.equals("empty"))
                            intform = new Intent(HomePage.this, ViewForm.class);

                            intform.putExtra("which_page", 15);
                            intform.putExtra("url", "http://hellobuddy.in/#/faqs");
                            startActivity(intform);
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.Orders:
                            intform = new Intent(HomePage.this, ViewForm.class);
                            intform.putExtra("which_page", 16);
                            Splash.checkNot = 1;
                            paste = (TextView) findViewById(R.id.pasteAg);
                            clickpaste();
                            intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                            startActivity(intform);
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.Repayments:


                            intform = new Intent(HomePage.this, ViewForm.class);
                            Splash.checkNot = 1;
                            paste = (TextView) findViewById(R.id.pasteAg);
                            clickpaste();

                            intform.putExtra("which_page", 17);
                            intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                            startActivity(intform);
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.Share:

                            SharedPreferences sh_otp = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editornew = sh_otp.edit();
                            editornew.putInt("chshare", 1);
                            // finish();
                            Splash.checkNot = 1;
                            paste = (TextView) findViewById(R.id.pasteAg);
                            clickpaste();
                            //   editornew.putString("rcode", getIntent().getExtras().getString("UniC"));
                            editornew.commit();
                            Intent in = new Intent(HomePage.this, Share.class);
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
                            paste = (TextView) findViewById(R.id.pasteAg);
                            clickpaste();
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


            paste = (TextView) findViewById(R.id.paste);


            paste.setVisibility(View.GONE);
//        mRecycler = (RecyclerView) findViewById(R.id.recycler_view);
            query = (EditText) findViewById(R.id.link);

            // query.setCompoundDrawablesWithIntrinsicBounds(50,50,50,50);
            query.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        query
            query.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        paste.setVisibility(View.GONE);
//                        hideSoftKeyboard(HomePage.this);

                        // code to execute when EditText loses focus
                    }
                }
            });


            query.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // hideSoftKeyboard(HomePage.this);
                    paste.setVisibility(View.GONE);
                }

                int w = 1;

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count == 0) {
                        w = 0;
                        ImageView home = (ImageView) findViewById(R.id.ho);
                        home.setBackgroundResource(R.drawable.list_grad);
                        //  Toast.makeText(HomePage.this, "2", Toast.LENGTH_LONG).show();
                        paste.setVisibility(View.VISIBLE);
                    } else {
                        paste.setVisibility(View.GONE);
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
            query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {

//                        Intent in = new Intent(HomePage.this, ViewForm.class);
                        Splash.checkNot = 1;
                        paste = (TextView) findViewById(R.id.pasteAg);
                        query.requestFocus();
                        clickpaste();
                        parse(query.getText().toString().trim());

                        if ((checkValidUrl == 0) && (checkValidFromApis == 0)) {
                            Long time = Calendar.getInstance().getTimeInMillis() / 1000;
                            if (time + 5 < userP.getLong("expires", 0))
//                                new checkAuth().execute(url);//
                            {
                                new linkSearch().execute();
                            } else
                                //   new checkAuth().execute(url);
                                new AuthTokc().execute("cc");

                        }
                        else if (checkValidUrl == 1) {
                            //monkey page
                            Intent in=new Intent(HomePage.this,ProductsPage.class);
                            query.setText("");

                            in.putExtra("page","monkey");
                            startActivity(in);
                            finish();
                            page="monkey";
                        }
                        else
                        if ((checkValidFromApis == 1)) {
                            //not monley page
                            query.setText("");
                            Intent in=new Intent(HomePage.this,ProductsPage.class);
                            in.putExtra("seller",sellerNme);
                            in.putExtra("page","pay");
                            startActivity(in);
                            finish();
                            page="pay";
                        }
//                        in.putExtra("url", query.getText().toString());
//                        in.putExtra("which_page", 9);
//                        startActivity(in);
                        //  Toast.makeText(HomePage.this,"oscar goes to caprio",Toast.LENGTH_LONG).show();
                        //TODO: do something
                    }
                    return false;
                }
            });
            ImageView home = (ImageView) findViewById(R.id.ho);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    query.clearFocus();
                    hideSoftKeyboard(HomePage.this);
                }
            });
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
                    if (checkedit == 1) {
                        //       Toast.makeText(HomePage.this,"check",Toast.LENGTH_LONG).show();
                        paste.setVisibility(View.VISIBLE);
                        ImageView home = (ImageView) findViewById(R.id.ho);

                    }
                    checkedit = 1;

                    return false;
                }
            });

            myClipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clickpaste();


//        CustomLinearLayoutManager customLayoutManager = new CustomLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
//        mRecycler.setLayoutManager(customLayoutManager);
//        mRecycler.setNestedScrollingEnabled(false);
//        feedsList=new ArrayList<FeedItem>();
            this.arraySpinner = new String[]{
                    "MOBILES", "LAPTOPS", "FASHION", "BEAUTY & PERSONAL CARE", "ENTERTAINMENT", "FOOTWEAR"
            };
            Spinner s = (Spinner) findViewById(R.id.spinner);
            Drawable spinnerDrawable = s.getBackground().getConstantState().newDrawable();

            spinnerDrawable.setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_ATOP);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                s.setBackground(spinnerDrawable);
            } else {
                s.setBackgroundDrawable(spinnerDrawable);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.colorspin, arraySpinner);
            adapter.setDropDownViewResource(R.layout.dropdown);
            s.setAdapter(adapter);

            try {
                s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        if (position == 0)
                            spin = "Mobiles";
                        if (position == 1)
                            spin = "Laptops";
                        if (position == 2)
                            spin = "apparels";
                        if (position == 4)
                            spin = "Electronics";
                        if (position == 5)
                            spin = "Footwear";
                        if (position == 3)
                            spin = "homeandbeauty";
                        try {
                            populate();
                        } catch (Exception e) {
                            new trending().execute("dd");
                        }
                        cardclick();
                        //final Intent send1=new Intent(HomePage.this,ViewForm.class);
//                        card1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                query.clearFocus();
//                                //     Toast.makeText(HomePage.this,"checknow",Toast.LENGTH_LONG).show();
//                                Intent send1 = new Intent(HomePage.this, ViewForm.class);
//                                send1.putExtra("prodid", Splash.fkid1.get(spin).get("0"));
//                                send1.putExtra("ecom", Splash.sellers.get(spin).get("0"));
//                                send1.putExtra("which_page", 10);
//                                Splash.checkNot = 1;
//                                paste = (TextView) findViewById(R.id.pasteAg);
//                                clickpaste();
//                                startActivity(send1);
//                            }
//                        });
//                        card2.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                query.clearFocus();
//                                Intent send1 = new Intent(HomePage.this, ViewForm.class);
//                                send1.putExtra("prodid", Splash.fkid1.get(spin).get("1"));
//                                send1.putExtra("ecom", Splash.sellers.get(spin).get("1"));
//                                send1.putExtra("which_page", 10);
//                                Splash.checkNot = 1;
//                                paste = (TextView) findViewById(R.id.pasteAg);
//                                clickpaste();
//                                startActivity(send1);
//
//                            }
//                        });
//                        card3.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                query.clearFocus();
//                                Intent send1 = new Intent(HomePage.this, ViewForm.class);
//
//                                send1.putExtra("prodid", Splash.fkid1.get(spin).get("2"));
//                                send1.putExtra("ecom", Splash.sellers.get(spin).get("2"));
//                                send1.putExtra("which_page", 10);
//                                Splash.checkNot = 1;
//                                paste = (TextView) findViewById(R.id.pasteAg);
//                                clickpaste();
//                                startActivity(send1);
//                            }
//                        });
//                        card4.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                query.clearFocus();
//                                Intent send1 = new Intent(HomePage.this, ViewForm.class);
//                                send1.putExtra("prodid", Splash.fkid1.get(spin).get("3"));
//                                send1.putExtra("ecom", Splash.sellers.get(spin).get("3"));
//                                send1.putExtra("which_page", 10);
//                                Splash.checkNot = 1;
//                                paste = (TextView) findViewById(R.id.pasteAg);
//                                clickpaste();
//                                startActivity(send1);
//                            }
//                        });
//                    if(position==3)
//                        Toast.makeText(HomePage.this,"checknow",Toast.LENGTH_LONG).show();
                        // your code here
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }

                });

            } catch (Exception e) {
                System.out.println("Error with spinner" + e.toString());
            }
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

    private class trending extends
            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
//            spinner.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... data) {

            String urldisplay = data[0];
//               HashMap<String, String> details = data[0];
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
//                finish();
                // Intent in = new Intent(MainActivity.this, Inviteform.class);
                in.putExtra("Name", name);
                in.putExtra("fbid", fbid);
                in.putExtra("Rej", rejectionReason);
                in.putExtra("Email", email);
                in.putExtra("Form", formstatus);
                in.putExtra("UniC", uniqueCode);
                Splash.checkNot = 1;
                paste = (TextView) findViewById(R.id.pasteAg);
                clickpaste();
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
                paste = (TextView) findViewById(R.id.pasteAg);
                clickpaste();
                startActivity(in);
                overridePendingTransition(0, 0);
            } else if (formstatus.equals("submitted")) {
                Splash.checkNot = 1;
                paste = (TextView) findViewById(R.id.pasteAg);
                clickpaste();
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
                paste = (TextView) findViewById(R.id.pasteAg);
                clickpaste();
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
                paste = (TextView) findViewById(R.id.pasteAg);
                clickpaste();
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
                paste = (TextView) findViewById(R.id.pasteAg);
                clickpaste();
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

    private void cardclick() {
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.clearFocus();
                //     Toast.makeText(HomePage.this,"checknow",Toast.LENGTH_LONG).show();
              ///  Intent send1 = new Intent(HomePage.this, ViewForm.class);
                Long time = Calendar.getInstance().getTimeInMillis() / 1000;
                productId=Splash.fkid1.get(spin).get("0");
                sellerNme=Splash.sellers.get(spin).get("0");
                if (time + 5 < userP.getLong("expires", 0))
//                                new checkAuth().execute(url);//
                {
                    new linkSearch().execute();
                } else
                    //   new checkAuth().execute(url);
                    new AuthTokc().execute("cc");

                Splash.checkNot = 1;
                paste = (TextView) findViewById(R.id.pasteAg);
                clickpaste();
//                send1.putExtra("prodid", Splash.fkid1.get(spin).get("0"));
//                send1.putExtra("ecom", Splash.sellers.get(spin).get("0"));
//                send1.putExtra("which_page", 10);
               //
               // sestartActivity(send1);
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.clearFocus();
//                Intent send1 = new Intent(HomePage.this, ViewForm.class);
//                send1.putExtra("prodid", Splash.fkid1.get(spin).get("1"));
                Splash.checkNot = 1;
                paste = (TextView) findViewById(R.id.pasteAg);
                clickpaste();
                Long time = Calendar.getInstance().getTimeInMillis() / 1000;
                productId=Splash.fkid1.get(spin).get("1");
                sellerNme=Splash.sellers.get(spin).get("1");
                if (time + 5 < userP.getLong("expires", 0))
//                                new checkAuth().execute(url);//
                {
                    new linkSearch().execute();
                } else
                    //   new checkAuth().execute(url);
                    new AuthTokc().execute("cc");

//                send1.putExtra("ecom", Splash.sellers.get(spin).get("1"));
//                send1.putExtra("which_page", 10);
//                startActivity(send1);

            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.clearFocus();
//                Intent send1 = new Intent(HomePage.this, ViewForm.class);
                Splash.checkNot = 1;
                Long time = Calendar.getInstance().getTimeInMillis() / 1000;
                productId=Splash.fkid1.get(spin).get("2");
                sellerNme=Splash.sellers.get(spin).get("2");
                if (time + 5 < userP.getLong("expires", 0))
//                                new checkAuth().execute(url);//
                {
                    new linkSearch().execute();
                } else
                    //   new checkAuth().execute(url);
                    new AuthTokc().execute("cc");

                paste = (TextView) findViewById(R.id.pasteAg);
                clickpaste();
//                send1.putExtra("prodid", Splash.fkid1.get(spin).get("2"));
//                send1.putExtra("ecom", Splash.sellers.get(spin).get("2"));
//                send1.putExtra("which_page", 10);
//                send1.putExtra("which_page", 10);
//                startActivity(send1);
            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.clearFocus();
                Splash.checkNot = 1;
                paste = (TextView) findViewById(R.id.pasteAg);
                clickpaste();
                Long time = Calendar.getInstance().getTimeInMillis() / 1000;
                productId = Splash.fkid1.get(spin).get("3");
                sellerNme = Splash.sellers.get(spin).get("3");
                if (time + 5 < userP.getLong("expires", 0))
//                                new checkAuth().execute(url);//
                {
                    new linkSearch().execute();
                } else
                    //   new checkAuth().execute(url);
                    new AuthTokc().execute("cc");

//                Intent send1 = new Intent(HomePage.this, ViewForm.class);
//                send1.putExtra("prodid", Splash.fkid1.get(spin).get("3"));
//                send1.putExtra("ecom", Splash.sellers.get(spin).get("3"));
//                send1.putExtra("which_page", 10);
//                startActivity(send1);
            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void populate() {
        {
//            TextView mrp11=(TextView)findViewById(R.id.mrp1);
//            TextView mrp2=(TextView)findViewById(R.id.mrp2);
//            TextView mrp3=(TextView)findViewById(R.id.mrp3);
//            TextView mrp4=(TextView)findViewById(R.id.mrp4);
            TextView price1 = (TextView) findViewById(R.id.titlexxx);
            TextView price2 = (TextView) findViewById(R.id.title1);
            TextView price3 = (TextView) findViewById(R.id.titlex2);
            TextView price4 = (TextView) findViewById(R.id.title2);
            TextView title1 = (TextView) findViewById(R.id.titlexxxd);
            TextView title2 = (TextView) findViewById(R.id.mrp1);
            TextView title3 = (TextView) findViewById(R.id.titlenew2);
            TextView title4 = (TextView) findViewById(R.id.mrp2);
            ImageView img1 = (ImageView) findViewById(R.id.img1);
            ImageView img2 = (ImageView) findViewById(R.id.imgx);
            ImageView img3 = (ImageView) findViewById(R.id.img2);
            ImageView img4 = (ImageView) findViewById(R.id.img2x);
            ImageView brand1 = (ImageView) findViewById(R.id.brand1);
            ImageView brand2 = (ImageView) findViewById(R.id.brand2);
            ImageView brand3 = (ImageView) findViewById(R.id.brand3);
            ImageView brand4 = (ImageView) findViewById(R.id.brand4);
            if (Splash.sellers.get(spin).get("0").equals("flipkart"))
                brand1.setImageResource(R.drawable.flipart);
            if (Splash.sellers.get(spin).get("0").equals("amazon"))
                brand1.setImageResource(R.drawable.amazon);
            if (Splash.sellers.get(spin).get("0").equals("paytm"))
                brand1.setImageResource(R.drawable.paytm);
            if (Splash.sellers.get(spin).get("0").equals("snapdeal"))
                brand1.setImageResource(R.drawable.snapdeal);
            if (Splash.sellers.get(spin).get("1").equals("flipkart"))
                brand2.setImageResource(R.drawable.flipart);
            if (Splash.sellers.get(spin).get("1").equals("amazon"))
                brand2.setImageResource(R.drawable.amazon);
            if (Splash.sellers.get(spin).get("1").equals("paytm"))
                brand2.setImageResource(R.drawable.paytm);
            if (Splash.sellers.get(spin).get("1").equals("snapdeal"))
                brand2.setImageResource(R.drawable.snapdeal);
            if (Splash.sellers.get(spin).get("2").equals("flipkart"))
                brand3.setImageResource(R.drawable.flipart);
            if (Splash.sellers.get(spin).get("2").equals("amazon"))
                brand3.setImageResource(R.drawable.amazon);
            if (Splash.sellers.get(spin).get("2").equals("paytm"))
                brand3.setImageResource(R.drawable.paytm);
            if (Splash.sellers.get(spin).get("2").equals("snapdeal"))
                brand3.setImageResource(R.drawable.snapdeal);
            if (Splash.sellers.get(spin).get("3").equals("flipkart"))
                brand4.setImageResource(R.drawable.flipart);
            if (Splash.sellers.get(spin).get("3").equals("amazon"))
                brand4.setImageResource(R.drawable.amazon);
            if (Splash.sellers.get(spin).get("3").equals("paytm"))
                brand4.setImageResource(R.drawable.paytm);
            if (Splash.sellers.get(spin).get("3").equals("snapdeal"))
                brand4.setImageResource(R.drawable.snapdeal);


            card1 = (ImageView) findViewById(R.id.img1);
            card2 = (ImageView) findViewById(R.id.imgx);
            card3 = (ImageView) findViewById(R.id.img2);
            card4 = (ImageView) findViewById(R.id.img2x);
//            mrp11.setText("MRP "+getApplicationContext().getString(R.string.Rs)+" "+ Splash.selling.get(spin).get("0"));
//            mrp2.setText("MRP "+getApplicationContext().getString(R.string.Rs)+" "+ Splash.selling.get(spin).get("1"));
//            mrp3.setText("MRP "+getApplicationContext().getString(R.string.Rs)+" "+ Splash.selling.get(spin).get("2"));
//            mrp4.setText("MRP "+getApplicationContext().getString(R.string.Rs)+" "+ Splash.selling.get(spin).get("3"));
            title1.setText(Splash.title.get(spin).get("0"));
            title2.setText(Splash.title.get(spin).get("1"));
            title3.setText(Splash.title.get(spin).get("2"));
            title4.setText(Splash.title.get(spin).get("3"));
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
//emi3

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

        cardclick();
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

        return m;
    }

    //    @Override
//    public void onResume() {
//        paste = (TextView) findViewById(R.id.pasteAg);
//        clickpaste();
//       // Toast.makeText(HomePage.this, "check", Toast.LENGTH_SHORT).show();
//        super.onResume();}
    public void parse(String parseString) {
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
            Toast.makeText(HomePage.this, "DADA" + String.valueOf(pos), Toast.LENGTH_SHORT).show();
        }
//snapdeal

        else if (parseString.contains("snapdeal")) {
            sellerNme = "snapdeal";
            pos = parseString.lastIndexOf("/");
            if (pos != -1) {
                for (int j = pos + 1;j<parseString.length(); j++) {
               //     if(((parseString.charAt(j))>='0')&&(parseString.charAt(j)<='9'))
                        productId += parseString.charAt(j);





                }
            } else {
                checkValidUrl = 1;
            }
            Toast.makeText(HomePage.this, "DADA" + String.valueOf(pos), Toast.LENGTH_SHORT).show();
        } else if (parseString.contains("myntra")) {
            sellerNme = "myntra";
            checkValidFromApis = 1;
        } else if (parseString.contains("shopclues")) {
            sellerNme = "shopclues";
            checkValidFromApis = 1;
        } else if (parseString.contains("jabong")) {
            sellerNme = "jabong";
            checkValidFromApis = 1;
        }
        else if (parseString.contains("paytm")) {
            sellerNme = "paytm";
            checkValidFromApis = 1;
        }
//amazon
        else if (parseString.contains("amazon")) {
            sellerNme = "amazon";
            int w = 0;
            pos = parseString.indexOf("/dp/");
            if(pos!=-1)
            {pos=parseString.indexOf("dp");}
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
            page="api";
            //make api call
        }
        if ((checkValidFromApis == 1)) {
            //not monley page
            page="pay";
        }
        if (checkValidUrl == 1) {
            //monkey page
            page="monkey";
        }
        Toast.makeText(HomePage.this, productId, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finish();

//            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
//                    .setMessage("Are you sure you want to exit?")
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//
//                        }
//                    }).setNegativeButton("No", null).show();
        }
    }

    public void clickpaste() {
        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    query.requestFocus();
                    ClipData abc = myClipboard.getPrimaryClip();
                    ClipData.Item item = abc.getItemAt(0);
                    String text = item.getText().toString();

                    query.setText( "   "+text);
                    paste.setVisibility(View.GONE);

                } catch (Exception e) {
                    Toast.makeText(HomePage.this, "Please copy a URL", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class AuthTokc extends
            AsyncTask<String, Void, String> {

        private String apiN = "";

        //        Context context;
//        AuthTok(Context context) {
//            this.context = context;
//        }
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


    public class linkSearch extends
            AsyncTask<String, Void, String> {
        @Override
        public void onPreExecute() {
//            spinner.setVisibility(View.VISIBLE);
        }


        @Override
        public String doInBackground(String... data) {

            //  String urldisplay = data[0];
//               HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: get
                String url = getApplicationContext().getString(R.string.server) + "api/product?productId=" + productId + "&seller=" + sellerNme + "&userid=" + cred.getString("phone_number", "");

//                String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjY1M2M2YTUwZTQzNzgyNjc0M2YyNjYiLCJuYW1lIjoiYnVkZHkgYXBpIGFkbWluIiwidXNlcm5hbWUiOiJidWRkeWFwaWFkbWluIiwicGFzc3dvcmQiOiJtZW1vbmdvc2gxIiwiZW1haWwiOiJjYXJlQGhlbGxvYnVkZHkuaW4iLCJpYXQiOjE0NTY3MjY1NzMsImV4cCI6MTQ1Njc2MjU3M30.98mQFcYm5Uf3Fd7ZNPD-OwMIfObu7vfoq9zNtCCLfyI";
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


                        Toast.makeText(HomePage.this, curr, Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (monthsallowed > monthscheck)
                    monthsallowed = monthscheck;

//                Double emi = 0.0;
                Double rate = 21.0 / 1200.0;
                int d = 0;
                if (searchPrice <= 5000) {
                    emi = searchPrice * 0.8 / monthsallowed;
                } else {
                    if (currDay <= 15)
                        d = 35 - currDay;
                    else
                        d = 65 - currDay;

                    emi = Math.floor((searchPrice * 0.8 * rate * Math.pow(1 + rate, monthsallowed - 1) * (1 + rate * d * 12 / 365)) / (Math.pow(1 + rate, monthsallowed) - 1));}
                    String q=query.getText().toString();
                    Intent in = new Intent(HomePage.this, ProductsPage.class);
                    in.putExtra("title", searchTitle);
                    in.putExtra("price", searchPrice);
                    in.putExtra("brand", searchBrand);
                    in.putExtra("name",name);
                    in.putExtra("image", urlImg);
                    in.putExtra("emi", emi);
                    in.putExtra("monthsallowed", monthsallowed);
                    in.putExtra("seller", sellerNme);
                    in.putExtra("query", q);
                    in.putExtra("page",page);

                    startActivity(in);
                    query.setText("");

                    Toast.makeText(HomePage.this, String.valueOf(emi), Toast.LENGTH_SHORT).show();


            }
            //else


        }
    }
}

