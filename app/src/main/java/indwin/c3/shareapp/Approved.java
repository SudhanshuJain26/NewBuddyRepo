package indwin.c3.shareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.intercom.android.sdk.Intercom;

/**
 * Created by Aniket Verma(Digo) on 2/4/2016.
 */
public class Approved extends AppCompatActivity {
    WebView form;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ProgressBar spinner;
    private String fbid = "", formstatus = "";
    Intent intform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String value = getIntent().getExtras().getString("Name");
        String value1 = getIntent().getExtras().getString("Email");
        String value2 = getIntent().getExtras().getString("Credits");
        try {
            fbid = getIntent().getExtras().getString("fbid");
            if (!fbid.equals("empty"))
                Splash.checkfbid = 1;
            //comment tet
        } catch (Exception e) {
        }
        try {
            formstatus = getIntent().getExtras().getString("Form");

        } catch (Exception e) {
            formstatus = "";
        }
        setContentView(R.layout.credits_given_profile);
        TextView titlehead = (TextView) findViewById(R.id.titlehead);
        titlehead.setText("Profile");
        ImageView logointool = (ImageView) findViewById(R.id.logointool);
        logointool.setVisibility(View.INVISIBLE);


        try {
            CircleImageView profile = (CircleImageView) findViewById(R.id.profile_image);
            SharedPreferences p = getSharedPreferences("proid", Context.MODE_PRIVATE);
            String dp = p.getString("dpid", null);
            String url = "http://graph.facebook.com/" + p.getString("dpid", null) + "/picture?type=square";
            Picasso.with(this)
                    .load("https://graph.facebook.com/" + p.getString("dpid", null) + "/picture?type=large")
                    .placeholder(R.drawable.images)
                    .into(profile);
        } catch (Exception e) {
            String t = e.toString();
        }
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        if (formstatus.equals("approved")) {
            TextView credit = (TextView) findViewById(R.id.applymsg3);
            credit.setText("Rs." + value2);
        }
        TextView wbsite = (TextView) findViewById(R.id.wbsite);
        wbsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fbid.equals("empty"))
                    intform = new Intent(Approved.this, ViewForm.class);
                else
                    intform = new Intent(Approved.this, FacebookAuth.class);
                intform.putExtra("which_page", 1);
                intform.putExtra("url", "http://hellobuddy.in/#/flipkart/products");
                finish();
                startActivity(intform);
                overridePendingTransition(0, 0);
            }
        });
        TextView viewDetails = (TextView) findViewById(R.id.view);
        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fbid.equals("empty"))
                    intform = new Intent(Approved.this, ViewForm.class);
                else
                    intform = new Intent(Approved.this, FacebookAuth.class);
                finish();
                intform.putExtra("which_page", 7);

                intform.putExtra("url", "http://hellobuddy.in/#/flipkart/products");
                startActivity(intform);
                overridePendingTransition(0, 0);
            }
        });
        wbsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fbid.equals("empty"))
                    intform = new Intent(Approved.this, ViewForm.class);
                else
                    intform = new Intent(Approved.this, FacebookAuth.class);
                finish();
                intform.putExtra("which_page", 1);
                intform.putExtra("url", "http://hellobuddy.in/#/flipkart/products");
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

                //Closing drawer on item click
                drawerLayout.closeDrawers();


                switch (menuItem.getItemId()) {
                    case R.id.MyAccount:

                        finish();
                        return true;
                    case R.id.About:


                        intform = new Intent(Approved.this, ViewForm.class);

                        finish();
                        intform.putExtra("which_page", 3);
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Orders:


                        intform = new Intent(Approved.this, ViewForm.class);

                        finish();
                        intform.putExtra("which_page", 16);
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Repayments:


                        intform = new Intent(Approved.this, ViewForm.class);

                        finish();
                        intform.putExtra("which_page", 17);
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.work:


                        intform = new Intent(Approved.this, ViewForm.class);

                        finish();
                        intform.putExtra("which_page", 11);
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.app_form:

                        if (!fbid.equals("empty"))
                            intform = new Intent(Approved.this, ViewForm.class);
                        else
                            intform = new Intent(Approved.this, FacebookAuth.class);
                        finish();
                        intform.putExtra("which_page", 4);
                        intform.putExtra("url", "http://hellobuddy.in/");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.faq:

                        //                        if(!fbid.equals("empty"))
                        intform = new Intent(Approved.this, ViewForm.class);
                        finish();
                        intform.putExtra("which_page", 5);
                        intform.putExtra("url", "http://hellobuddy.in/#/faqs");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.security:

                        //                        if(!fbid.equals("empty"))
                        intform = new Intent(Approved.this, ViewForm.class);
                        finish();
                        intform.putExtra("which_page", 15);
                        intform.putExtra("url", "http://hellobuddy.in/#/faqs");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Share:

                        SharedPreferences sh_otp = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editornew = sh_otp.edit();
                        //                        editor.putInt("checklog",a);
                        editornew.putInt("chshare", 1);
                        //                        editornew.putString("rcode", getIntent().getExtras().getString("UniC"));
                        editornew.commit();
                        Intent in = new Intent(Approved.this, ShareSecond.class);

                        startActivity(in);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.log:
                        Splash.notify = 0;
                        Intent stop = new Intent("CLOSE_ALL");
                        Approved.this.sendBroadcast(stop);
                        Splash.checklog = 1;
                        int a = 0;
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
                            System.out.println(e.toString() + "int appr");
                        }

                        intform = new Intent(Approved.this, MainActivity.class);
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

        NavigationView nview = (NavigationView) drawerLayout.findViewById(R.id.navigation_view);
        View headerView = nview.getHeaderView(0);
        ImageView arr = (ImageView) headerView.findViewById(R.id.arrow);
        RelativeLayout header = (RelativeLayout) headerView.findViewById(R.id.head);
        //RelativeLayout header = (RelativeLayout) headerView.findViewById(R.id.head);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     Toast.makeText(Approved.this, "clicked", Toast.LENGTH_SHORT).show();
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
        TextView view = (TextView) findViewById(R.id.apply);
        if (formstatus.equals("flashApproved")) {
            TextView con = (TextView) findViewById(R.id.applymsg);
            con.setText("CONGRATS!");
            TextView firms = (TextView) findViewById(R.id.applymsg2);

            TextView secms = (TextView) findViewById(R.id.applymsg3);
            firms.setText("You have been approved for a flash Credit Limit of Rs.1000, which will be credited to your Buddy account in a short while.");
            secms.setText("In the meanwhile, complete your profile to increase your credit limit further!");
            view.setText("Complete your Profile!");
            ImageView fla = (ImageView) findViewById(R.id.rej);
            fla.setImageResource(R.drawable.flash);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in;
                //                if(!fbid.equals("empty"))
                in = new Intent(Approved.this, ViewForm.class);
                //                else
                //                    in=new Intent(Approved.this, FacebookAuth.class);
                finish();
                in.putExtra("which_page", 1);
                in.putExtra("url", "http://hellobuddy.in/#/");

                startActivity(in);
                overridePendingTransition(0, 0);
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
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }
}

