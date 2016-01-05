package com.example.buddy.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import io.intercom.android.sdk.Intercom;

public class Formempty extends AppCompatActivity {
WebView form;  private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    Intent intform;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String value = getIntent().getExtras().getString("Name");
        String value1 = getIntent().getExtras().getString("Email");
        String value2 = getIntent().getExtras().getString("Form");
        setContentView(R.layout.activity_formempty);
        TextView wbsite=(TextView)findViewById(R.id.wbsite);
        wbsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intform=new Intent(Formempty.this, ViewForm.class);
                intform.putExtra("url","http://hellobuddy.in/#/flipkart/products");
                startActivity(intform);
            }
        });
        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);
//getSupportActionBar().setBackgroundDrawable(R.id.buddylogo_blue);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setIcon(R.drawable.buddylogo_blue);
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

                        intform=new Intent(Formempty.this, ViewForm.class);
                        intform.putExtra("url", "http://hellobuddy.in/");
                        startActivity(intform);
                        return true;
                    case R.id.About:

                        intform=new Intent(Formempty.this, ViewForm.class);
                        intform.putExtra("url","http://hellobuddy.in/#/how-it-works");
                        startActivity(intform);
                        return true;
                    case R.id.faq:

                        intform=new Intent(Formempty.this, ViewForm.class);
                        intform.putExtra("url","http://hellobuddy.in/#/faqs");
                        startActivity(intform);
                        return true;
                    case R.id.Share:

                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("image/png");
                        Uri uri = Uri.parse("android.resource://com.example.buddy.myapplication/"+R.drawable.buddyshare);
                        final File photoFile = new File(getFilesDir(), "buddyshare.png");
                        String shareBody = "Hi, checkout this amazing startup which lets college students buy products from flipkart through small payments. http://hellobuddy.in/#/me?referralCode="+getIntent().getExtras().getString("UniC");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        return true;
                    case R.id.log:
                        int a=0;
                        SharedPreferences sharedpreferences = getSharedPreferences("buddy", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt("checklog",a);
                        editor.commit();

                        intform=new Intent(Formempty.this, MainActivity.class);
                        startActivity(intform);
                        return true;
                    default:return true;
                }
                //Check to see which item was being clicked and perform appropriate action

            }});

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        NavigationView nview=(NavigationView) drawerLayout.findViewById(R.id.navigation_view);
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
            public void onClick(View v) { Intent in=new Intent(Formempty.this, ViewForm.class);
                in.putExtra("url","http://hellobuddy.in/#/");
                startActivity(in);
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
