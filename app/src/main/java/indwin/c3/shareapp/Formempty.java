package indwin.c3.shareapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import io.intercom.android.sdk.Intercom;

public class Formempty extends AppCompatActivity {
WebView form;  private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private Menu mOptionsMenu;
    Intent intform;
    String value2 ="",reason="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String value = getIntent().getExtras().getString("Name");
        String value1 = getIntent().getExtras().getString("Email");

        try{value2=getIntent().getExtras().getString("Form");}
        catch(Exception e)
        {
            System.out.println("Error occured while reciving form"+e.toString());
        }
        if(value2.equals("declined"))
        {
            setContentView(R.layout.rejected);

            TextView wbsite=(TextView)findViewById(R.id.wbsite);
            wbsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intform = new Intent(Formempty.this, ViewForm.class);
                    intform.putExtra("which_page", 1);
                    intform.putExtra("url", "http://hellobuddy.in/#/flipkart/products");
                    startActivity(intform);
                    overridePendingTransition(0, 0);
                }
            });
            try{
        reason=getIntent().getExtras().getString("Rej");}
        catch (Exception e){}
        TextView reasonR=(TextView)findViewById(R.id.applymsg3);
            reasonR.setText(reason);
        }
        else
        setContentView(R.layout.activity_formempty);
        if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlack));
        }
        TextView wbsite=(TextView)findViewById(R.id.wbsite);
        wbsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intform=new Intent(Formempty.this, ViewForm.class);
                intform.putExtra("which_page",1);
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
                navigationView.getMenu().getItem(1).setChecked(true);
                //Closing drawer on item click
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()){
                    case R.id.MyAccount:

                        return true;
                    case R.id.About:

                        intform=new Intent(Formempty.this, ViewForm.class);
                        finish();
                        intform.putExtra("url", "http://hellobuddy.in/#/how-it-works");
                        intform.putExtra("which_page",3);
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.app_form:

                        intform=new Intent(Formempty.this, ViewForm.class);
                        finish();
                        intform.putExtra("url", "http://hellobuddy.in/");
                        intform.putExtra("which_page",4);
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.faq:

                        intform=new Intent(Formempty.this, ViewForm.class);
                        finish();
                        intform.putExtra("url", "http://hellobuddy.in/#/faqs");
                        intform.putExtra("which_page",5);
                        startActivity(intform);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Share:
                        SharedPreferences sh_otp=getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editornew = sh_otp.edit();
//                        editor.putInt("checklog",a);

                        editornew.putInt("chshare", 1);


                        editornew.putString("rcode", getIntent().getExtras().getString("UniC"));
                        editornew.commit();
                       Intent in=new Intent(Formempty.this,Share.class);

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
                        intform=new Intent(Formempty.this, MainActivity.class);
                        finish();
                        try {
                            Intercom.client().reset();
                        }
                        catch (Exception e)
                        {System.out.println(e.toString()+"int empty");}
                        startActivity(intform);
                        overridePendingTransition(0,0);
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
           //     Toast.makeText(Formempty.this, "clicked", Toast.LENGTH_SHORT).show();
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
        if(value2.equals("declined"))
        {
            TextView view=(TextView)findViewById(R.id.view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { Intent in=new Intent(Formempty.this, ViewForm.class);
                    finish();
                    in.putExtra("which_page", 6);
                    in.putExtra("url","http://hellobuddy.in/#/");
                    startActivity(in);
                    overridePendingTransition(0, 0);
                }
            });}
        TextView view=(TextView)findViewById(R.id.apply);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value2.equals("declined"))
                {
                    Intercom.client().displayMessageComposer();
                }
                else
                {
                Intent in=new Intent(Formempty.this, ViewForm.class);
                    finish();
                    in.putExtra("which_page",6);
                in.putExtra("url","http://hellobuddy.in/#/");
                    startActivity(in);
                    overridePendingTransition(0,0);}
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
        mOptionsMenu=menu;
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
