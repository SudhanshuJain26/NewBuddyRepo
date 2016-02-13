package indwin.c3.shareapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import io.intercom.android.sdk.Intercom;

/**
 * Created by Aniket Verma(Digo) on 2/4/2016.
 */
public class Approved extends AppCompatActivity {
    WebView form;  private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ProgressBar spinner;

    Intent intform;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String value = getIntent().getExtras().getString("Name");
        String value1 = getIntent().getExtras().getString("Email");
        String value2 = getIntent().getExtras().getString("Credits");
        setContentView(R.layout.credits_given_profile);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        TextView credit=(TextView)findViewById(R.id.applymsg3);
        credit.setText("Rs." + value2);
        TextView wbsite=(TextView)findViewById(R.id.wbsite);
        wbsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intform=new Intent(Approved.this, ViewForm.class);
                intform.putExtra("which_page",1);
                intform.putExtra("url", "http://hellobuddy.in/#/flipkart/products");

                startActivity(intform);
                overridePendingTransition(0, 0);
            }
        });
        TextView viewDetails=(TextView)findViewById(R.id.view);
        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intform=new Intent(Approved.this, ViewForm.class);
                finish();
                intform.putExtra("which_page", 7);

                intform.putExtra("url","http://hellobuddy.in/#/flipkart/products");
                startActivity(intform);
                overridePendingTransition(0, 0);
            }
        });
        wbsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intform=new Intent(Approved.this, ViewForm.class);
                finish();
                intform.putExtra("which_page", 1);
                intform.putExtra("url","http://hellobuddy.in/#/flipkart/products");
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

        }
        catch(Exception e){System.out.println(e.toString()+"digo");}

        TextView name=(TextView)findViewById(R.id.name);
        FloatingActionButton chat=(FloatingActionButton)findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intercom.client().displayMessageComposer();
            }
        });
        name.setText(value);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.getMenu().getItem(0).setChecked(true);
//        GridView gridviewshow=(GridView)findViewById(R.id.grid1a);
//       gridviewshow.setAdapter(new ImageAdapter(this));


        //Setting Navigation View Item Selected Listener
        // to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();


                switch (menuItem.getItemId()){
                    case R.id.MyAccount:


                        return true;
                    case R.id.About:

                        intform=new Intent(Approved.this, ViewForm.class);
                        finish();
                        intform.putExtra("which_page", 3);
                        intform.putExtra("url","http://hellobuddy.in/#/how-it-works");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.app_form:

                        intform=new Intent(Approved.this, ViewForm.class);
                        finish();
                        intform.putExtra("which_page", 4);
                        intform.putExtra("url","http://hellobuddy.in/");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.faq:

                        intform=new Intent(Approved.this, ViewForm.class);
                        finish();
                        intform.putExtra("which_page", 5);
                        intform.putExtra("url","http://hellobuddy.in/#/faqs");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Share:

                        SharedPreferences sh_otp=getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editornew = sh_otp.edit();
//                        editor.putInt("checklog",a);
                        editornew.putInt("chshare",1);
                        editornew.putString("rcode", getIntent().getExtras().getString("UniC"));
                        editornew.commit();
                        Intent in=new Intent(Approved.this,Share.class);

                        startActivity(in);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.log:
                        int a=0;
                        SharedPreferences sharedpreferences = getSharedPreferences("buddy", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt("checklog",a);
                        editor.commit();
                        SharedPreferences sharedpreferences1 = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                        editor1.putString("number", "");
                        editor1.putString("code", "");
                        editor1.commit();
                        try {
                            Intercom.client().reset();
                        }
                        catch (Exception e)
                        {System.out.println(e.toString() + "int appr");}

                        intform=new Intent(Approved.this, MainActivity.class);
                        finish();
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    default:return true;
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
           //     Toast.makeText(Approved.this, "clicked", Toast.LENGTH_SHORT).show();
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
        TextView email=(TextView)findViewById(R.id.email);
        email.setText(value1);
        TextView view=(TextView)findViewById(R.id.apply);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent in=new Intent(Approved.this, ViewForm.class);
                finish();
                in.putExtra("which_page", 6);
                in.putExtra("url", "http://hellobuddy.in/#/");

                startActivity(in);
                overridePendingTransition(0,0);
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

