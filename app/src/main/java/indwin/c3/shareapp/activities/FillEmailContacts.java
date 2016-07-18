package indwin.c3.shareapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import indwin.c3.shareapp.AlreadyInvited1;
import indwin.c3.shareapp.BlinkingFragment;
import indwin.c3.shareapp.BlinkingFragmentEmail;
import indwin.c3.shareapp.Buddies;
import indwin.c3.shareapp.BuddiesEmail;
import indwin.c3.shareapp.BuildConfig;
import indwin.c3.shareapp.MyPageScrollListener;
import indwin.c3.shareapp.NewUserEmail1;
import indwin.c3.shareapp.R;
import indwin.c3.shareapp.Share;
import indwin.c3.shareapp.models.Friends;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;

public class FillEmailContacts extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ArrayList<Friends> friendsArrayList = new ArrayList<>();
    private ArrayList<Friends> yahooArrayList = new ArrayList<>();
    String userId;
    ArrayList<Friends> isBuddyList = new ArrayList<>();
    ArrayList<Friends> isInvitedList = new ArrayList<>();
    ArrayList<Friends> listfromServer = new ArrayList<>();
    TextView logged_email;
    TextView disconnect;
    ImageView backButton;
    TextView another_email;
    public static boolean  yahooMalfunction =false;
    boolean flag = true;
    ArrayList<String> alreadyListed = new ArrayList<>();


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FillEmailContacts.this,ShowSelectedItems.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_email_contacts);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        disconnect = (TextView) findViewById(R.id.disconnect);
        logged_email = (TextView) findViewById(R.id.email_text);

        backButton = (ImageView) findViewById(R.id.backo);
        UserModel userModel = AppUtils.getUserObject(this);
        userId = userModel.getUserId();
        if(disconnect.getVisibility()==View.GONE){
            logged_email.setText("+ Connect another email account");
        }

        SharedPreferences prefs10 = getSharedPreferences("inviteCalls", MODE_PRIVATE);

        flag = prefs10.getBoolean("email_read",true);
        if(flag){
            disconnect.setText("Disconnect");
        }else{
            disconnect.setText("Connect");
        }

        try {
            isBuddyList = (ArrayList<Friends>) getIntent().getSerializableExtra("buddyList");
            isInvitedList = (ArrayList<Friends>) getIntent().getSerializableExtra("invitedList");
            listfromServer = (ArrayList<Friends>) getIntent().getSerializableExtra("userList");
        }catch(Exception e){
            e.printStackTrace();
        }
        //new GetInvitedEmail(this).execute();

        if (listfromServer == null) {
            SharedPreferences sharedPrefs = getSharedPreferences("list1", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("email_contacts_notSelected", null);
            if (json != null) {
                Type type = new TypeToken<ArrayList<Friends>>() {
                }.getType();
                listfromServer = gson.fromJson(json, type);
            }
        }

        SharedPreferences prefs2 = getSharedPreferences("disconnect", MODE_PRIVATE);
        if (!prefs2.getBoolean("disconnectemail", false)) {


            SharedPreferences prefs1 = getSharedPreferences("CHECKBOX_STATE_EMAIL", MODE_PRIVATE);
            SharedPreferences.Editor editor1 = prefs1.edit();
            editor1.clear();
            editor1.commit();

            if (logged_email.getText().equals("Connect to your account")) {
                disconnect.setText("Connect");
            }
            new GetInvitedEmail(FillEmailContacts.this).execute();

            SharedPreferences prefs = getSharedPreferences("cred", MODE_PRIVATE);
            String email = prefs.getString("user_loggedin_email", "");
            logged_email.setText(email);


//            setupViewPager(viewPager);
//            tabLayout.setupWithViewPager(viewPager);
//            tabLayout.setOnTabSelectedListener(
//                    new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
//                        @Override
//                        public void onTabSelected(TabLayout.Tab tab) {
//                            super.onTabSelected(tab);
//                            int position = tab.getPosition();
//                            if (viewPager.getCurrentItem() != position) {
//                                viewPager.setCurrentItem(position, true);
//                            }
//                        }
//                    });


        }

        else {

            SharedPreferences sharedPrefs = getSharedPreferences("list2", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("email_contacts_invited", null);
            if (json != null) {
                Type type = new TypeToken<ArrayList<Friends>>() {
                }.getType();
                isInvitedList = gson.fromJson(json, type);
            }

            SharedPreferences sharedPrefs1 = getSharedPreferences("list2", MODE_PRIVATE);
            Gson gson1 = new Gson();
            String json1 = sharedPrefs1.getString("email_contacts_buddy", null);
            if (json1 != null) {
                Type type = new TypeToken<ArrayList<Friends>>() {
                }.getType();
                isBuddyList = gson1.fromJson(json1, type);
            }
//

                tabLayout.addTab(tabLayout.newTab().setText("New Users"), 0);
                tabLayout.addTab(tabLayout.newTab().setText("Invited"+"                           ("+getApplicationContext().getResources().getString(R.string.Rs)+isInvitedList.size()+")"), 1);
                tabLayout.addTab(tabLayout.newTab().setText("Buddies"+"                            ("+isBuddyList.size()+")"), 2);
                viewPager.setOnPageChangeListener(new MyPageScrollListener(tabLayout));
                disconnect.setVisibility(View.GONE);
                logged_email.setText("+ Connect another email account");



                EmailAdapter1 adapter1 = new EmailAdapter1(getSupportFragmentManager(), 3);
                viewPager.setAdapter(adapter1);
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (viewPager.getCurrentItem() != position) {
                    viewPager.setCurrentItem(position, true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        }


//            Collections.sort(listfromServer, new Comparator<Friends>() {
//                @Override
//                public int compare(Friends s1, Friends s2) {
//                    return s1.getName().compareToIgnoreCase(s2.getName());
//                }
//            });

//

            disconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (disconnect.getText().equals("Connect")) {
                        Intent intent = new Intent(FillEmailContacts.this, AuthenticateEmail.class);
                        intent.putExtra("pageCode",1);
                        startActivity(intent);

//                        disconnect.setText("Disconnect");
                        finish();
                    } else {


                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FillEmailContacts.this);
                        alertDialogBuilder.setMessage("Are u sure you want to disconnect your email account");
                        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences preferences1 = getSharedPreferences("disconnect", MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = preferences1.edit();
                                editor1.putBoolean("disconnectemail", true);
                                editor1.commit();
                                disconnect.setVisibility(View.GONE);

                                //disconnect.setText("Connect");
                                SharedPreferences prefs = getSharedPreferences("preferencename2", 0);
                                SharedPreferences.Editor editor2 = prefs.edit();
                                editor2.clear();
                                editor2.apply();

                                logged_email.setText("+ Connect another email account");
                                SharedPreferences preferences = getSharedPreferences("invite_lists", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.apply();
//                        isBuddyList.clear();
//                        isInvitedList.clear();
//                        listfromServer.clear();

                                tabLayout.setTabMode(TabLayout.MODE_FIXED);
                                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                                tabLayout.removeTabAt(0);

                                tabLayout.addTab(tabLayout.newTab().setText("New Users"), 0);


                                EmailAdapter1 adapter1 = new EmailAdapter1(getSupportFragmentManager(), 3);
                                viewPager.setAdapter(adapter1);

                            }
                        });

                        alertDialogBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.show();


                    }
                }
            });

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (disconnect.getVisibility() == View.GONE) {
                        SharedPreferences sharedPrefs1 = getSharedPreferences("selectedContacts", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPrefs1.edit();
                        editor.remove("email_contacts_selected");
                        editor.apply();

                    }
                    Intent intent = new Intent(FillEmailContacts.this, ShowSelectedItems.class);
                    startActivity(intent);
                    finish();
                }
            });


            logged_email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(logged_email.getText().equals("+ Connect another email account")) {

                        Intent intent = new Intent(FillEmailContacts.this, AuthenticateEmail.class);
                        startActivity(intent);
                        SharedPreferences sharedPrefs1 = getSharedPreferences("selectedContacts", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPrefs1.edit();
                        editor.remove("email_contacts_selected");
                        editor.apply();
                        finish();

                        disconnect.setVisibility(View.VISIBLE);
                    }
                }
            });

//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//
//
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//

    }


    private void setupViewPager(ViewPager viewPager) {
        EmailAdapter adapter = new EmailAdapter(getSupportFragmentManager());
        if(listfromServer==null){
            listfromServer = new ArrayList<>();
            yahooMalfunction = true;

        }
        if(isBuddyList==null){
            isBuddyList = new ArrayList<>();
        }
        if(isInvitedList==null){
            isInvitedList = new ArrayList<>();
        }
        adapter.addFragment(new NewUserEmail1(), "New Users"+ "                                    ( "+getApplicationContext().getResources().getString(R.string.Rs)+listfromServer.size()*170+" )");
        adapter.addFragment(new AlreadyInvited1(), "Invited" + "                          ("+getApplicationContext().getResources().getString(R.string.Rs)+ isInvitedList.size()*170+")");
        adapter.addFragment(new BuddiesEmail(),"Buddies" + "                              ("+isBuddyList.size()   +")");
        viewPager.setAdapter(adapter);

    }


    class EmailAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public EmailAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return NewUserEmail1.init(listfromServer);
                case 1:
                    return AlreadyInvited1.init(isInvitedList);
                case 2:
                    return BuddiesEmail.init(isBuddyList);
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public class EmailAdapter1 extends FragmentStatePagerAdapter{

        int tabCount;
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public EmailAdapter1(FragmentManager fm,int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    return BlinkingFragmentEmail.init();
                case 1:

                    return AlreadyInvited1.init(isInvitedList);
                case 2:

                    return BuddiesEmail.init(isBuddyList);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public class GetInvitedEmail extends AsyncTask<Void, Void, String> {

        Context context;

        String url = BuildConfig.SERVER_URL + "api/v1/user/contacts?userid=" + userId;

        public GetInvitedEmail(Context context) {
            this.context = context;
        }


        @Override
        protected String doInBackground(Void... params) {

            try {
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                HttpResponse response = AppUtils.connectToServerGet(url, tok_sp, null);
                if (response != null) {
                    HttpEntity ent = response.getEntity();
                    String responseString = EntityUtils.toString(ent, "UTF-8");
                    if (response.getStatusLine().getStatusCode() != 200) {


                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {
                        JSONObject jsonObject = new JSONObject(responseString);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            Boolean isInvited;
                            String phone;
                            String email;
                            JSONObject json = jsonArray.getJSONObject(i);
                            String name = json.getString("name");
                            try {
                                phone = json.getString("contactPhone");
                            }catch (JSONException e){
                                phone ="";
                            }
                            try{
                                email = json.getString("contactEmail");
                            }catch (JSONException e){
                                email = "";
                            }

                            Boolean isBuddy = json.getBoolean("isBuddy");
                            if(!isBuddy){
                                isInvited = json.getBoolean("isInvited");
                            }
                            else
                                isInvited = false;

                            Friends friends = new Friends(phone, email, isBuddy, isInvited, name);
                            if(email.length()!=0) {

                                    if (isBuddy)
                                        isBuddyList.add(friends);
                                    if (!isBuddy && isInvited)
                                        isInvitedList.add(friends);
                                }

                            }
                        }


                    }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isBuddyList = new ArrayList<>();
            isInvitedList = new ArrayList<>();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            for (int i = 0; i < isBuddyList.size(); i++) {
                String name = isBuddyList.get(i).getName();
                alreadyListed.add(name);
            }
            for (int i = 0; i < isInvitedList.size(); i++) {
                String name = isInvitedList.get(i).getName();
                alreadyListed.add(name);
            }
            for (int i = 0; i <listfromServer.size(); i++) {
                String name = listfromServer.get(i).getName();
                if (alreadyListed.contains(name)) {
                    listfromServer.remove(listfromServer.get(i));
                }
            }

            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            saveListinPrefs();

        }


    }

    public void saveListinPrefs()
    {
        SharedPreferences preferences1 = getSharedPreferences("list2", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences1.edit();
        Gson gson1 = new Gson();
        String json1 = gson1.toJson(isInvitedList);
        editor1.putString("email_contacts_invited", json1);
        editor1.commit();

        SharedPreferences preferences2 = getSharedPreferences("list2", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = preferences2.edit();
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(isBuddyList);
        editor2.putString("email_contacts_buddy", json2);
        editor2.commit();
    }

}
