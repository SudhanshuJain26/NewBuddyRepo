package indwin.c3.shareapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.google.android.gms.common.api.GoogleApiClient;

import io.intercom.android.sdk.Intercom;

public class Formsaved extends AppCompatActivity {
    WebView form;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    Intent intform;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String value = getIntent().getExtras().getString("Name");
        String value1 = getIntent().getExtras().getString("Email");
        String value2 = getIntent().getExtras().getString("Form");
        setContentView(R.layout.apply_emi);
        TextView viewDetails=(TextView)findViewById(R.id.view);
        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intform = new Intent(Formsaved.this, ViewForm.class);
                finish();
                intform.putExtra("which_page", 7);
                intform.putExtra("url", "http://hellobuddy.in/#/flipkart/products");
                startActivity(intform);
            }
        });
        TextView wbsite = (TextView) findViewById(R.id.wbsite);
        wbsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intform = new Intent(Formsaved.this, ViewForm.class);
                finish();
                intform.putExtra("which_page",1);
                intform.putExtra("url", "http://hellobuddy.in/#/flipkart/products");
                startActivity(intform);
            }
        });
        TextView name = (TextView) findViewById(R.id.name);
        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        } catch (Exception e) {
            System.out.println(e.toString() + "digo");
        }
        ImageView inter=(ImageView)findViewById(R.id.interCom);
        inter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intercom.client().displayMessageComposer();
            }
        });
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
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

                        intform = new Intent(Formsaved.this, ViewForm.class);
                        finish();
                        intform.putExtra("which_page",2);
                        intform.putExtra("url", "http://hellobuddy.in/");
                        startActivity(intform);
                        return true;
                    case R.id.About:

                        intform = new Intent(Formsaved.this, ViewForm.class);
                        finish();
                        intform.putExtra("which_page",3);
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        startActivity(intform);
                        return true;
                    case R.id.faq:

                        intform = new Intent(Formsaved.this, ViewForm.class);
                        finish();
                        intform.putExtra("which_page",4);
                        intform.putExtra("url", "http://hellobuddy.in/#/faqs");
                        startActivity(intform);
                        return true;
                    case R.id.Share:

                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("image/png");
                        Uri uri = Uri.parse("android.resource://indwin.c3.shareapp/"+R.drawable.buddyrefer);
                        String shareBody = "Hey check out this crazy website!\n" +
                                "Remember to use the code "+getIntent().getExtras().getString("UniC")+" while signing up!";
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        return true;
                    case R.id.app_form:

                        intform = new Intent(Formsaved.this, ViewForm.class);
                        finish();
                        intform.putExtra("which_page",5);
                        intform.putExtra("url", "http://hellobuddy.in/");
                        startActivity(intform);
                        return true;
                    case R.id.log:
                        Splash.notify = 0;
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

                        intform = new Intent(Formsaved.this, MainActivity.class);
                        finish();
                        startActivity(intform);
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
        arr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(Formsaved.this, "clicked", Toast.LENGTH_SHORT).show();
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
        FloatingActionButton chat = (FloatingActionButton) findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intercom.client().displayMessageComposer();
            }
        });
        name.setText(value);
        TextView email = (TextView) findViewById(R.id.email);
        email.setText(value1);
        TextView view = (TextView) findViewById(R.id.apply);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Formsaved.this, ViewForm.class);
                in.putExtra("which_page", 6);
                in.putExtra("url", "http://hellobuddy.in/#/");

                startActivity(in);
            }
        });



    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }
                    }).setNegativeButton("No", null).show();}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }




}
