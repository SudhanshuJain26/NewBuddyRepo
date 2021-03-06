package indwin.c3.shareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.intercom.android.sdk.Intercom;

public class Formempty extends AppCompatActivity {
    WebView form;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private Menu mOptionsMenu;
    Intent intform;
    private String value2 = "", reason = "", fbid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String value = getIntent().getExtras().getString("Name");
        String value1 = getIntent().getExtras().getString("Email");
        try {
            fbid = getIntent().getExtras().getString("fbid");
        } catch (Exception e) {
            fbid = "empty";
        }
        if ((fbid == null) || (fbid.equals("")))
            fbid = "empty";
        if (!fbid.equals("empty")) {
            Splash.checkfbid = 1;

        }
        try {
            value2 = getIntent().getExtras().getString("Form");
        } catch (Exception e) {
            System.out.println("Error occured while reciving form" + e.toString());
        }
        if (value2.equals("declined")) {
            setContentView(R.layout.rejected);
            TextView titlehead = (TextView) findViewById(R.id.titlehead);
            titlehead.setText("Profile");
            ImageView logointool = (ImageView) findViewById(R.id.logointool);
            logointool.setVisibility(View.INVISIBLE);
            try {
                CircleImageView profile = (CircleImageView) findViewById(R.id.profile_image);
                SharedPreferences p = getSharedPreferences("proid", Context.MODE_PRIVATE);
                String dp = p.getString("dpid", null);
                String url = "https://graph.facebook.com/" + p.getString("dpid", null) + "/picture?type=large";
                Picasso.with(this)
                        .load("https://graph.facebook.com/" + p.getString("dpid", null) + "/picture?type=large")
                        .placeholder(R.drawable.images)
                        .into(profile);
            } catch (Exception e) {
                String t = e.toString();
            }
            TextView wbsite = (TextView) findViewById(R.id.wbsite);
            wbsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //                    if(!fbid.equals("empty"))
                    intform = new Intent(Formempty.this, ViewForm.class);
                    //                    else
                    //                        intform=new Intent(Formempty.this, FacebookAuth.class);
                    intform.putExtra("which_page", 1);
                    intform.putExtra("url", "http://hellobuddy.in/#/flipkart/products");
                    finish();
                    startActivity(intform);

                    overridePendingTransition(0, 0);
                }
            });
            try {
                reason = getIntent().getExtras().getString("Rej");
            } catch (Exception e) {
            }
            TextView reasonR = (TextView) findViewById(R.id.applymsg3);
            reasonR.setText(reason);
        } else
            setContentView(R.layout.activity_formempty);
        TextView titlehead = (TextView) findViewById(R.id.titlehead);
        titlehead.setText("Profile");
        ImageView logointool = (ImageView) findViewById(R.id.logointool);
        logointool.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlack));
        }
        TextView wbsite = (TextView) findViewById(R.id.wbsite);
        try {
            CircleImageView profile = (CircleImageView) findViewById(R.id.profile_image);
            SharedPreferences p = getSharedPreferences("proid", Context.MODE_PRIVATE);
            String dp = p.getString("dpid", null);
            String url = "https://graph.facebook.com/" + p.getString("dpid", null) + "/picture?type=large";
            Picasso.with(this)
                    .load("https://graph.facebook.com/" + p.getString("dpid", null) + "/picture?type=large")
                    .placeholder(R.drawable.images)
                    .into(profile);
        } catch (Exception e) {
            String t = e.toString();
        }
        wbsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        if(!fbid.equals("empty"))
                intform = new Intent(Formempty.this, ViewForm.class);
                //                else
                //                    intform=new Intent(Formempty.this, FacebookAuth.class);
                intform.putExtra("which_page", 1);
                intform.putExtra("url", "http://hellobuddy.in/#/flipkart/products");
                finish();
                startActivity(intform);
                overridePendingTransition(0, 0);
            }
        });

        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);
            //getSupportActionBar().setBackgroundDrawable(R.id.buddylogo_blue);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        } catch (Exception e) {
            System.out.println(e.toString() + "digo");
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
        TextView name = (TextView) findViewById(R.id.name);
        FloatingActionButton chat = (FloatingActionButton) findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intercom.client().displayMessageComposer();
                } catch (Exception e) {
                }
            }
        });
        name.setText(value);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.getMenu().getItem(1).setChecked(true);
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
                navigationView.getMenu().getItem(1).setChecked(true);
                //Closing drawer on item click
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.MyAccount:
                        finish();
                        return true;
                    case R.id.work:


                        intform = new Intent(Formempty.this, ViewForm.class);

                        finish();
                        intform.putExtra("which_page", 11);
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.About:
                        //if(!fbid.equals("empty"))
                        intform = new Intent(Formempty.this, ViewForm.class);
                        //                        else
                        //                            intform=new Intent(Formempty.this, FacebookAuth.class);
                        finish();
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        intform.putExtra("which_page", 3);
                        finish();
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.app_form:
                        if (!fbid.equals("empty") || (FacebookAuth.firstloginsign == 1))
                            intform = new Intent(Formempty.this, ViewForm.class);
                        else
                            intform = new Intent(Formempty.this, FacebookAuth.class);
                        finish();
                        intform.putExtra("url", "http://hellobuddy.in/");
                        intform.putExtra("which_page", 4);
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.faq:
                        //                        if(!fbid.equals("empty"))
                        intform = new Intent(Formempty.this, ViewForm.class);
                        //                        else
                        //                            intform=new Intent(Formempty.this, FacebookAuth.class);

                        intform.putExtra("url", "http://hellobuddy.in/#/faqs");
                        intform.putExtra("which_page", 5);
                        finish();
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.Orders:


                        intform = new Intent(Formempty.this, ViewForm.class);

                        finish();
                        intform.putExtra("which_page", 16);
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Repayments:


                        intform = new Intent(Formempty.this, ViewForm.class);

                        finish();
                        intform.putExtra("which_page", 17);
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.security:

                        //                        if(!fbid.equals("empty"))
                        intform = new Intent(Formempty.this, ViewForm.class);
                        finish();
                        intform.putExtra("which_page", 15);
                        intform.putExtra("url", "http://hellobuddy.in/#/faqs");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Share:
                        SharedPreferences sh_otp = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editornew = sh_otp.edit();
                        editornew.putInt("chshare", 1);
                        String sh = getIntent().getExtras().getString("UniC");
                        // editornew.putString("rcode", getIntent().getExtras().getString("UniC"));
                        editornew.commit();
                        Intent in = new Intent(Formempty.this, ShareSecond.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.log:
                        Splash.notify = 0;
                        Intent stop = new Intent("CLOSE_ALL");
                        Formempty.this.sendBroadcast(stop);
                        Splash.checklog = 1;
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
                        intform = new Intent(Formempty.this, MainActivity.class);
                        finish();
                        try {
                            Intercom.client().reset();
                        } catch (Exception e) {
                            System.out.println(e.toString() + "int empty");
                        }
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
        TextView email = (TextView) findViewById(R.id.email);
        email.setText(value1);
        if (value2.equals("declined")) {
            TextView view = (TextView) findViewById(R.id.view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in;
                    if (!fbid.equals("empty"))
                        in = new Intent(Formempty.this, ViewForm.class);
                    else
                        in = new Intent(Formempty.this, FacebookAuth.class);

                    in.putExtra("which_page", 6);
                    in.putExtra("url", "http://hellobuddy.in/#/");
                    finish();
                    startActivity(in);
                    overridePendingTransition(0, 0);
                }
            });
        }
        TextView view = (TextView) findViewById(R.id.apply);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value2.equals("declined")) {
                    try {
                        Intercom.client().displayMessageComposer();
                    } catch (Exception e) {
                    }
                } else {
                    Intent in;
                    if (!fbid.equals("empty") || (FacebookAuth.firstloginsign == 1))
                        in = new Intent(Formempty.this, ViewForm.class);
                    else
                        in = new Intent(Formempty.this, FacebookAuth.class);
                    in.putExtra("which_page", 6);
                    in.putExtra("url", "http://hellobuddy.in/#/");
                    finish();
                    startActivity(in);
                    overridePendingTransition(0, 0);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        mOptionsMenu = menu;
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
