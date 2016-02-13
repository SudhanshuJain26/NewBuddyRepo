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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.intercom.android.sdk.Intercom;

public class Formstatus extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    Intent intform;
    String verification_date="";
    int screen_no=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String value = getIntent().getExtras().getString("Name");
        try {
            verification_date = getIntent().getExtras().getString("VeriDate");
        }
        catch(Exception e)
        {
            System.out.println("Exception while getting verification date"+e.toString());
        }

        try {
            screen_no = getIntent().getExtras().getInt("screen_no");
        }
        catch(Exception e)
        {
            System.out.println("Exception while getting screen number"+e.toString());
        }
        // TODO: 2/7/2016  change screens according to the nuber of screen_no
        String value1 = getIntent().getExtras().getString("Email");
        String value2 = getIntent().getExtras().getString("Form");

        if(screen_no==3) {
            setContentView(R.layout.verification_date);
            TextView date=(TextView)findViewById(R.id.applymsg3);
            date.setText(verification_date);

        }
        else

        setContentView(R.layout.viewapp);
        TextView linkbutton=(TextView)findViewById(R.id.apply);
      if(screen_no==1)
      {
          //upload docs click listener
      }

        if(screen_no==2)
        {
            TextView line1=(TextView)findViewById(R.id.applymsg);
            TextView line2=(TextView)findViewById(R.id.applymsg2);
            TextView line3=(TextView)findViewById(R.id.applymsg3);

            ImageView im=(ImageView)findViewById(R.id.upl);
            im.setImageResource(R.drawable.cam);
            line1.setText("Hi! Your profiles is complete but you haven't yet");
            line2.setText("scheduled a verification.Kindly schedule a");
            line3.setText("verification to get started.");
            linkbutton.setText("Schedule verification now!");
        }
        FloatingActionButton chat=(FloatingActionButton)findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intercom.client().displayMessageComposer();
            }
        });
        TextView name=(TextView)findViewById(R.id.name);
        name.setText(value);
        TextView viewDetails=(TextView)findViewById(R.id.view);
        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intform = new Intent(Formstatus.this, ViewForm.class);
                finish();
                intform.putExtra("which_page", 7);
                intform.putExtra("url", "http://hellobuddy.in/#/flipkart/products");
                startActivity(intform);
                overridePendingTransition(0, 0);
            }
        });
        TextView wbsite=(TextView)findViewById(R.id.wbsite);
        wbsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intform = new Intent(Formstatus.this, ViewForm.class);
                finish();
                intform.putExtra("which_page", 1);
                intform.putExtra("url","http://hellobuddy.in/#/flipkart/products");
                startActivity(intform);
                overridePendingTransition(0, 0);
            }



        });
        TextView email=(TextView)findViewById(R.id.email);
        email.setText(value1);
        TextView viewchat=(TextView)findViewById(R.id.apply);
        viewchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(screen_no==1)
                {
                    Intent in=new Intent(Formstatus.this, ViewForm.class);
                    finish();
                    in.putExtra("which_page", 6);
                    in.putExtra("url","http://hellobuddy.in/#/");
                    startActivity(in);
                    overridePendingTransition(0, 0);
                }
                    else
                Intercom.client().displayMessageComposer();
            }
        });
        TextView view=(TextView)findViewById(R.id.view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Formstatus.this, ViewForm.class);
                finish();
                in.putExtra("which_page", 6);
                in.putExtra("url","http://hellobuddy.in/#/");
                startActivity(in);
                overridePendingTransition(0, 0);
            }
        });

        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        catch(Exception e){System.out.println(e.toString()+"digo");}navigationView = (NavigationView) findViewById(R.id.navigation_view);
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
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()){
                    case R.id.MyAccount:


                        return true;
                    case R.id.About:

                        intform=new Intent(Formstatus.this, ViewForm.class);
                        finish();
                        intform.putExtra("which_page", 3);
                        intform.putExtra("url","http://hellobuddy.in/#/how-it-works");
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.faq:

                        intform=new Intent(Formstatus.this, ViewForm.class);
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
                        Intent in=new Intent(Formstatus.this,Share.class);

                        startActivity(in);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.app_form:

                        intform=new Intent(Formstatus.this, ViewForm.class);
                        finish();
                        intform.putExtra("which_page", 4);
                        intform.putExtra("url","http://hellobuddy.in/");
                        startActivity(intform);
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
                        {System.out.println(e.toString()+"int sta");}
                        intform=new Intent(Formstatus.this, MainActivity.class);
                        finish();
                        startActivity(intform);
                        overridePendingTransition(0,0);
                        return true;
                    default:return true;
                }
                //Check to see which item was being clicked and perform appropriate action

            }});

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        NavigationView nview=(NavigationView) drawerLayout.findViewById(R.id.navigation_view);
        View headerView=nview.getHeaderView(0);
        ImageView arr=(ImageView)headerView.findViewById(R.id.arrow);
       RelativeLayout header = (RelativeLayout) headerView.findViewById(R.id.head);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //      Toast.makeText(Formstatus.this, "clicked", Toast.LENGTH_SHORT).show();
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
        actionBarDrawerToggle.syncState(); }
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
