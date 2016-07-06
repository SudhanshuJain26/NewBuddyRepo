package indwin.c3.shareapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import indwin.c3.shareapp.BlinkingFragment;
import indwin.c3.shareapp.Buddies;

import indwin.c3.shareapp.FirstPageFragmentListener;
import indwin.c3.shareapp.NewUser1;
import indwin.c3.shareapp.R;
import indwin.c3.shareapp.Share;
import indwin.c3.shareapp.models.Friends;
import indwin.c3.shareapp.models.Image;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;

public class GetContacts extends AppCompatActivity  {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Friends> friendsArrayList = new ArrayList<>();
    ProgressDialog progressDialog;
    public ArrayList<Friends> listfromServer = new ArrayList<>();
    public ArrayList<Friends> isBuddyList = new ArrayList<>();
    public ArrayList<Friends> isInvitedList = new ArrayList<>();
    int size;
    String userId;
    ProgressDialog pDialog;
    ArrayList<String> alreadyListed = new ArrayList<>();
    TextView disconnect;
    ImageView backButton;
    ArrayList<Friends> newFriends = new ArrayList<>();
    ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_contacts);
        UserModel userModel = AppUtils.getUserObject(this);
        userId = userModel.getUserId();
        SharedPreferences prefs1 = getSharedPreferences("CHECKBOX_STATE",MODE_PRIVATE);
        SharedPreferences.Editor editor1 = prefs1.edit();
        editor1.clear();
        editor1.commit();
        //new GetInvitedData(this).execute();
        listfromServer = (ArrayList<Friends>)getIntent().getSerializableExtra("userList");
        isBuddyList = (ArrayList<Friends>)getIntent().getSerializableExtra("buddyList");
        isInvitedList = (ArrayList<Friends>)getIntent().getSerializableExtra("invitedList");
        disconnect = (TextView)findViewById(R.id.disconnect);
        backButton = (ImageView)findViewById(R.id.backo);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(disconnect.getText().equals("Disconnect")) {
                    disconnect.setText("Connect");
                    SharedPreferences preferences = getSharedPreferences("invite_lists", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
//                    isBuddyList.clear();
//                    isInvitedList.clear();
                    listfromServer.clear();
                    adapter.onSwitchToNextFragment();


//                    List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
//
//                    if (fragmentList == null) {
//                        // code that handles no existing fragments
//                    }
//
//                    for (Fragment frag : fragmentList )
//                    {
//                        getSupportFragmentManager().beginTransaction().remove(frag).commit();
//                    }
//                }else if(disconnect.getText().equals("Connect")){
//
//                    Intent intent = new Intent(GetContacts.this,InviteList.class);
//                    startActivity(intent);
                }
            }
        });


        if(listfromServer!=null) {
            SharedPreferences preferences = getSharedPreferences("list1", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(listfromServer);
            editor.putString("phone_contacts_notSelected", json);
            editor.commit();
        }else {
            SharedPreferences sharedPrefs = getSharedPreferences("list1", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("phone_contacts_notSelected", null);
            if (json != null) {
                Type type = new TypeToken<ArrayList<Friends>>() {
                }.getType();
                listfromServer = gson.fromJson(json, type);
            }
        }
        Collections.sort(listfromServer, new Comparator<Friends>() {
            @Override
            public int compare(Friends s1, Friends s2) {
                return s1.getName().compareToIgnoreCase(s2.getName());
            }
        });

        if(isInvitedList!=null) {
            SharedPreferences preferences = getSharedPreferences("list1", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(isInvitedList);
            editor.putString("phone_contacts_isInvited", json);
            editor.commit();
        }else {
            SharedPreferences sharedPrefs = getSharedPreferences("list1", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("phone_contacts_isInvited", null);
            if (json != null) {
                Type type = new TypeToken<ArrayList<Friends>>() {
                }.getType();
                isInvitedList = gson.fromJson(json, type);
            }
        }


        if(isBuddyList!=null) {
            SharedPreferences preferences = getSharedPreferences("list1", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(isBuddyList);
            editor.putString("phone_contacts_isBuddy", json);
            editor.commit();
        }else {
            SharedPreferences sharedPrefs = getSharedPreferences("list1", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("phone_contacts_isBuddy", null);
            if (json != null) {
                Type type = new TypeToken<ArrayList<Friends>>() {
                }.getType();
                 isBuddyList= gson.fromJson(json, type);
            }
        }

        size = friendsArrayList.size();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ContactsAdapter(getSupportFragmentManager());

        adapter.addFragment(new NewUser1(), "New Users"+ "     ( "+getApplicationContext().getResources().getString(R.string.Rs)+listfromServer.size()*170+" )");
        adapter.addFragment(new AlreadyInvited(), "Invited" + "                   ("+isInvitedList.size()+")");
        adapter.addFragment(new Buddies(),"Buddies" + "                    ("+isBuddyList.size()   +")");
        viewPager.setAdapter(adapter);
    }



    class ContactsAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final FragmentManager mFragmentManager;
        public Fragment mFragmentAtPos0;
        private Context context;


        public ContactsAdapter(FragmentManager manager) {
            super(manager);
            mFragmentManager = manager;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if(mFragmentAtPos0==null){
                        mFragmentAtPos0 = NewUser1.init(listfromServer);
                    }
                    return mFragmentAtPos0;
                case 1:
                    return AlreadyInvited.init(isInvitedList);
                case 2:
                    return Buddies.init(isBuddyList);
                default:
                    break;
            }
            return null;
        }

            public void onSwitchToNextFragment() {
                mFragmentManager.beginTransaction().remove(mFragmentAtPos0)
                        .commit();
                if (mFragmentAtPos0 instanceof NewUser1){
                    mFragmentAtPos0 = new BlinkingFragment();
                }
                this.notifyDataSetChanged();
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

//
}
