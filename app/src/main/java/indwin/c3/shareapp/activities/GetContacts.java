package indwin.c3.shareapp.activities;

//import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
//import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.FrameLayout;
import android.widget.ImageView;
//import android.widget.LinearLayout;
import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
//import java.lang.reflect.Type;
import java.lang.reflect.Type;
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import indwin.c3.shareapp.AlreadyInvited;
import indwin.c3.shareapp.BlinkingFragment;
import indwin.c3.shareapp.Buddies;

import indwin.c3.shareapp.BuildConfig;
//import indwin.c3.shareapp.FirstPageFragmentListener;
import indwin.c3.shareapp.MyPageScrollListener;
import indwin.c3.shareapp.NewUser1;
import indwin.c3.shareapp.R;

import indwin.c3.shareapp.Share;
import indwin.c3.shareapp.models.Friends;

import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;

public class GetContacts extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Friends> friendsArrayList = new ArrayList<>();
    ProgressDialog progressDialog;
    ProgressDialog getProgressDialog;
    public ArrayList<Friends> listfromServerEmail = new ArrayList<>();
    public ArrayList<Friends> isBuddyListEmail = new ArrayList<>();
    public ArrayList<Friends> isInvitedListEmail = new ArrayList<>();
    public ArrayList<Friends> listfromServerPhone = new ArrayList<>();
    public ArrayList<Friends> isBuddyListPhone = new ArrayList<>();
    public ArrayList<Friends> isInvitedListPhone = new ArrayList<>();
    int size;
    String userId;
    ArrayList<String> alreadyListed = new ArrayList<>();
    TextView disconnect;
    ImageView backButton;
    ArrayList<Friends> newFriends = new ArrayList<>();
    ContactsAdapter adapter;
    Boolean blinking = false;
    ContactAdapter1 adapter1;
    ImageView refresh;


    ProgressBar spinner;
    List<Friends> completePhoneList = new ArrayList<>();
    ProgressDialog pDialog;
    int intentResult;

    int intentResult1;
    Boolean isdisconnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_contacts);
        disconnect = (TextView) findViewById(R.id.disconnect);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        backButton = (ImageView) findViewById(R.id.backo);
        refresh = (ImageView)findViewById(R.id.refresh);
        SharedPreferences preferences = getSharedPreferences("disconnect", MODE_PRIVATE);
        isdisconnect = preferences.getBoolean("disconnectphone", false);
        SharedPreferences sharedPrefs = getSharedPreferences("list1", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("phone_contacts_notSelected", null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<Friends>>() {
            }.getType();
            listfromServerPhone = gson.fromJson(json, type);
        }

        SharedPreferences sharedPrefs1 = getSharedPreferences("list1", MODE_PRIVATE);
        Gson gson1 = new Gson();
        String json1 = sharedPrefs1.getString("phone_contacts_buddy", null);
        if (json1 != null) {
            Type type = new TypeToken<ArrayList<Friends>>() {
            }.getType();
            isBuddyListPhone = gson1.fromJson(json1, type);
        }

        SharedPreferences sharedPrefs2 = getSharedPreferences("list1", MODE_PRIVATE);
        Gson gson2 = new Gson();
        String json2 = sharedPrefs2.getString("phone_contacts_invited", null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<Friends>>() {
            }.getType();
            isInvitedListPhone = gson2.fromJson(json2, type);
        }
        if (!isdisconnect) {
            spinner = (ProgressBar) findViewById(R.id.spinner);
            disconnect.setText("Disconnect");
            UserModel userModel = AppUtils.getUserObject(this);
            userId = userModel.getUserId();
            try {
                intentResult = getIntent().getIntExtra("addMorePhone", 1);
            } catch (Exception e) {

            }


            if (intentResult == 100) {


                setupViewPager(viewPager);


                tabLayout.setupWithViewPager(viewPager);
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("list1",MODE_PRIVATE);
                Boolean flag = sharedPreferences.getBoolean("saveList",false);
                if(!flag)
                new GetContactListFromPhone().execute();
                else
                   new GetInvitedDataPhone(this).execute();



            }
            UserModel userModel1 = AppUtils.getUserObject(this);
            userId = userModel1.getUserId();
            SharedPreferences prefs1 = getSharedPreferences("CHECKBOX_STATE", MODE_PRIVATE);
            SharedPreferences.Editor editor1 = prefs1.edit();
            editor1.clear();
            editor1.commit();

        } else {
            disconnect.setText("Connect");


            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//            tabLayout.removeTabAt(0);
//
            tabLayout.addTab(tabLayout.newTab().setText("New Users"), 0);
            tabLayout.addTab(tabLayout.newTab().setText("Invited" +"                            ("+isInvitedListPhone.size()+")"), 1);
            tabLayout.addTab(tabLayout.newTab().setText("Buddies"+"                             ("+isBuddyListPhone.size()+")"), 2);
            viewPager.setOnPageChangeListener(new MyPageScrollListener(tabLayout));
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


            ContactAdapter1 adapter1 = new ContactAdapter1(getSupportFragmentManager(), 3);
            viewPager.setAdapter(adapter1);
        }


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(disconnect.getText().equals("Disconnect"))
                onBackPressed();
                else{
                    Intent intent = new Intent(GetContacts.this,ShowSelectedItems.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (disconnect.getText().equals("Disconnect")) {
                    disconnect.setText("Connect");
                    SharedPreferences preferences = getSharedPreferences("invite_lists", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    SharedPreferences sharedPrefs1 = getSharedPreferences("selectedContacts", MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = sharedPrefs1.edit();
                    editor3.remove("phone_contacts_selected");
                    editor3.apply();
                    SharedPreferences preferences1 = getSharedPreferences("disconnect", MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = preferences1.edit();
                    editor2.putBoolean("disconnectphone", true);
                    editor2.apply();
                    SharedPreferences prefs = getSharedPreferences("preferencename1", 0);
                    SharedPreferences.Editor editor1 = prefs.edit();
                    editor1.clear();
                    editor1.apply();
                    tabLayout.removeAllTabs();

                    tabLayout.setTabMode(TabLayout.MODE_FIXED);
                    tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                    tabLayout.addTab(tabLayout.newTab().setText("New Users"), 0);
                    tabLayout.addTab(tabLayout.newTab().setText("Invited"+"                              ("+getApplicationContext().getResources().getString(R.string.Rs)+isInvitedListPhone.size()+")"), 1);
                    tabLayout.addTab(tabLayout.newTab().setText("Buddies"+"                               ("+isBuddyListPhone.size()+")"), 2);
                    viewPager.setOnPageChangeListener(new MyPageScrollListener(tabLayout));
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
                    ContactAdapter1 adapter1 = new ContactAdapter1(getSupportFragmentManager(), 3);
                    viewPager.setAdapter(adapter1);
//
                } else {
                    SharedPreferences preferences2 = getSharedPreferences("disconnect", MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = preferences2.edit();
                    editor3.putBoolean("disconnectphone", false);
                    editor3.apply();
                    disconnect.setText("Disconnect");
                    UserModel userModel = AppUtils.getUserObject(GetContacts.this);
                    userId = userModel.getUserId();
                    try {
                        intentResult = getIntent().getIntExtra("addMorePhone", 1);
                    } catch (Exception e) {

                    }


                        setupViewPager(viewPager);


                        tabLayout.setupWithViewPager(viewPager);


                }
            }
        });

       refresh.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               new GetContactListFromPhone().execute();
           }
       });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GetContacts.this,ShowSelectedItems.class);
        startActivity(intent);
        finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ContactsAdapter(getSupportFragmentManager(),3);

        adapter.addFragment(new NewUser1(), "New Users"+ "                              ( "+getApplicationContext().getResources().getString(R.string.Rs)+listfromServerPhone.size()*170+" )");
        adapter.addFragment(new AlreadyInvited(), "Invited" + "                              ("+getApplicationContext().getResources().getString(R.string.Rs)+ isInvitedListPhone.size()*170+")");
        adapter.addFragment(new Buddies(),"Buddies" + "                      ("+isBuddyListPhone.size()   +")");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPager1(ViewPager viewPager) {
        adapter1 = new ContactAdapter1(getSupportFragmentManager(),3);

        adapter1.addFragment(new BlinkingFragment(), "New Users"+ "     ( "+0+")");
        adapter1.addFragment(new AlreadyInvited(), "Invited" + "                   ("+getApplicationContext().getResources().getString(R.string.Rs)+isInvitedListPhone.size()*170+")");
        adapter1.addFragment(new Buddies(),"Buddies" + "                    ("+isBuddyListPhone.size()   +")");
        viewPager.setAdapter(adapter);
    }

    class ContactsAdapter extends FragmentStatePagerAdapter {
        int tabCount;
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final FragmentManager mFragmentManager;
        public Fragment mFragmentAtPos0;
        private Context context;


        public ContactsAdapter(FragmentManager manager,int tabCount) {
            super(manager);
            mFragmentManager = manager;
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    return NewUser1.init(listfromServerPhone);

                case 1:
                    return AlreadyInvited.init(isInvitedListPhone);
                case 2:
                    return Buddies.init(isBuddyListPhone);
                default:
                    break;
            }
            return null;
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

    public class ContactAdapter1 extends FragmentStatePagerAdapter {
        int tabCount;
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ContactAdapter1(FragmentManager fm , int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    return BlinkingFragment.init();
                case 1:

                    return AlreadyInvited.init(isInvitedListPhone);
                case 2:

                    return Buddies.init(isBuddyListPhone);
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

    public void readContacts() {

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Friends friends = new Friends();
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    System.out.println("name : " + name + ", ID : " + id);
                    friends.setName(name);

                    // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        friends.setPhone_Num(phone);
                        break;
                    }


                    pCur.close();


                    // get email and type

                    Cursor emailCur = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCur.moveToNext()) {
                        String email = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                        friends.setEmail(email);
                        break;
                    }

                    emailCur.close();

                }
                friends.setType("sms");
                if (friends.getName() == null || friends.getPhone_Num() == null) {
                } else

                    completePhoneList.add(friends);

            }





        }

        SharedPreferences preferences = getSharedPreferences("list1", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(completePhoneList);
        editor.putString("completephonelist", json);
        editor.commit();





    }

    public class SendContactstoServer extends AsyncTask<Void, Void, String> {
        Context context;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Fetching your contacts...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);

            pDialog.show();
        }

        public SendContactstoServer(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(String s) {
            if (pDialog.isShowing())
                pDialog.dismiss();

            SharedPreferences sharedPreferences = context.getSharedPreferences("inviteCalls2", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("phone-contacts-taken", true);
            editor.apply();

            new GetInvitedDataPhone(context).execute();



            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONObject jsonObject = new JSONObject();
            String url = BuildConfig.SERVER_URL + "api/v1/user/contacts";
            SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
            String tok_sp = toks.getString("token_value", "");
            try {
                jsonObject.put("userid", userId);
                JSONArray contacts = new JSONArray();
                for (int i = 0; i <completePhoneList.size(); i++) {
                    JSONObject json = new JSONObject();
                    json.put("name", completePhoneList.get(i).getName());
                    String phone = completePhoneList.get(i).getPhone_Num();
                    if (phone == null || phone.length() == 0) {
                        phone = "";
                    }
                    json.put("phone", phone);
                    String email = completePhoneList.get(i).getEmail();
                    if (email == null || email.length() == 0) {
                        email = "";
                    }
                    json.put("email", email);
                    contacts.put(json);
                }
                jsonObject.put("contacts", contacts);

                HttpResponse response = AppUtils.connectToServerPost(url, jsonObject.toString(), tok_sp);
                if (response != null) {
                    HttpEntity ent = response.getEntity();
                    String responseString = EntityUtils.toString(ent, "UTF-8");
                    if (response.getStatusLine().getStatusCode() != 200) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {
                        JSONObject resp = new JSONObject(responseString);

                        if (!resp.getString("status").contains("success")) {

                            Log.e("MeshCommunication", "Server returned code "
                                    + response.getStatusLine().getStatusCode());
                            return resp.getString("msg");
                        } else {

                            return "win";

                        }

                    }
                } else return "fail";

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Exception", e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    public class GetInvitedDataPhone extends AsyncTask<Void, Void, String> {
        String url = BuildConfig.SERVER_URL + "api/v1/user/contacts?userid=" + userId;
        Context context;

        public GetInvitedDataPhone(Context context) {
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
                                phone = "";
                            }
                            try {
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
                            if(phone.length()!=0 && phone!=null && name!=null){
                                if (isBuddy) {
                                    isBuddyListPhone.add(friends);
                                    Log.i(friends.getName(),friends.getPhone_Num());
                                }
                                if (!isBuddy && isInvited)
                                    isInvitedListPhone.add(friends);

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
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Finding your friends ...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            isBuddyListPhone.clear();
            isInvitedListPhone.clear();
            listfromServerPhone.clear();
//            isInvitedListEmail.clear();
//            isInvitedListEmail.clear();

            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            for(int i =0;i<isBuddyListPhone.size();i++){
                String name = isBuddyListPhone.get(i).getName();
                alreadyListed.add(name);
            }
            for(int i =0;i<isInvitedListPhone.size();i++){
                String name = isInvitedListPhone.get(i).getName();
                alreadyListed.add(name);
            }

            SharedPreferences sharedPrefs = getSharedPreferences("list1", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("completephonelist", null);
            if (json != null) {
                Type type = new TypeToken<ArrayList<Friends>>() {
                }.getType();
                completePhoneList = gson.fromJson(json, type);
            }


            for(int i= 0;i<completePhoneList.size();i++){
                String name = completePhoneList.get(i).getName();
                if(!alreadyListed.contains(name)){
                    listfromServerPhone.add(completePhoneList.get(i));
                }
            }

            Collections.sort(isBuddyListPhone, new Comparator<Friends>() {
            @Override
            public int compare(Friends s1, Friends s2) {
                return s1.getName().compareToIgnoreCase(s2.getName());
            }
        });

            Collections.sort(isInvitedListPhone, new Comparator<Friends>() {
                @Override
                public int compare(Friends s1, Friends s2) {
                    return s1.getName().compareToIgnoreCase(s2.getName());
                }
            });

            Collections.sort(listfromServerPhone, new Comparator<Friends>() {
                @Override
                public int compare(Friends s1, Friends s2) {
                    return s1.getName().compareToIgnoreCase(s2.getName());
                }
            });

            progressDialog.dismiss();

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            saveListinPrefs();
        }
    }

    public void saveListinPrefs(){
        SharedPreferences preferences = getSharedPreferences("list1", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listfromServerPhone);
        editor.putString("phone_contacts_notSelected", json);
        editor.commit();

        SharedPreferences preferences1 = getSharedPreferences("list1", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences1.edit();
        Gson gson1 = new Gson();
        String json1 = gson1.toJson(isInvitedListPhone);
        editor1.putString("phone_contacts_invited", json1);
        editor1.commit();

        SharedPreferences preferences2 = getSharedPreferences("list1", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = preferences2.edit();
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(isBuddyListPhone);
        editor2.putString("phone_contacts_buddy", json2);
        editor2.commit();

        SharedPreferences preferences3 = getSharedPreferences("list1", MODE_PRIVATE);
        SharedPreferences.Editor editor3 = preferences3.edit();
        editor3.putBoolean("saveList",true);
        editor3.commit();

        SharedPreferences preferences4 = getSharedPreferences("list1", MODE_PRIVATE);
        SharedPreferences.Editor editor4 = preferences4.edit();
        editor4.putInt("inviteSize",isInvitedListPhone.size());
        editor4.commit();
    }

    public class GetContactListFromPhone extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            completePhoneList.clear();
            readContacts();
            return null;
        }

        @Override
        protected void onPreExecute() {
            getProgressDialog = new ProgressDialog(GetContacts.this);
            getProgressDialog = new ProgressDialog(GetContacts.this);
            getProgressDialog.setMessage("Working on ur contacts...");
            getProgressDialog.setIndeterminate(false);
            getProgressDialog.setCancelable(true);
            getProgressDialog.show();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(getProgressDialog.isShowing()){
                getProgressDialog.dismiss();
            }

            SharedPreferences sharedPreferences11 = getSharedPreferences("inviteCalls2",MODE_PRIVATE);
            Boolean sendInvites1 = sharedPreferences11.getBoolean("phone-contacts-taken",false);
            SharedPreferences sharedPreferences = getSharedPreferences("list1",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("saveList",true);
            new SendContactstoServer(GetContacts.this).execute();

        }
    }


//
}
