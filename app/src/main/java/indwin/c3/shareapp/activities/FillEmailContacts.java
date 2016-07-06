package indwin.c3.shareapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import indwin.c3.shareapp.BuddiesEmail;
import indwin.c3.shareapp.NewUserEmail1;
import indwin.c3.shareapp.R;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_email_contacts);
        UserModel userModel = AppUtils.getUserObject(this);
        userId = userModel.getUserId();
        logged_email = (TextView)findViewById(R.id.email_text);
        disconnect = (TextView)findViewById(R.id.disconnect);
        backButton =(ImageView)findViewById(R.id.backo);
        SharedPreferences prefs1 = getSharedPreferences("CHECKBOX_STATE_EMAIL",MODE_PRIVATE);
        SharedPreferences.Editor editor1 = prefs1.edit();
        editor1.clear();
        editor1.commit();
        if(logged_email.getText().equals("Connect to your account")){
            disconnect.setText("Connect");
        }

//        try{
//            String message = getIntent().getStringExtra("message");
//            isBuddyList = new ArrayList<>();
//            isInvitedList = new ArrayList<>();
//            listfromServer = new ArrayList<>();
//            Toast.makeText(getApplicationContext(),"Error in yahoo server",Toast.LENGTH_SHORT).show();
//        }catch (Exception e){
//            String message = "";
//
//
//        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        SharedPreferences prefs = getSharedPreferences("cred",MODE_PRIVATE);
        String email = prefs.getString("user_loggedin_email","");
        logged_email.setText(email);
        isBuddyList = (ArrayList<Friends>) getIntent().getSerializableExtra("buddyList");
        isInvitedList = (ArrayList<Friends>) getIntent().getSerializableExtra("invitedList");
        listfromServer = (ArrayList<Friends>)getIntent().getSerializableExtra("userList");
        //new GetInvitedEmail(this).execute();
        if (isBuddyList != null) {
            SharedPreferences preferences = getSharedPreferences("inviteCalls", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();

            String json = gson.toJson(isBuddyList);

            editor.putString("email_contacts_isBuddy", json);
            editor.commit();
        } else {
            SharedPreferences sharedPrefs = getSharedPreferences("inviteCalls", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("email_contacts_isBuddy", null);
            if (json != null) {
                Type type = new TypeToken<ArrayList<Friends>>() {
                }.getType();
                isBuddyList = gson.fromJson(json, type);
            }
        }

        if (isInvitedList != null) {
            SharedPreferences preferences = getSharedPreferences("inviteCalls", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();

            String json = gson.toJson(isInvitedList);

            editor.putString("email_contacts_isInvited", json);
            editor.commit();
        } else {
            SharedPreferences sharedPrefs = getSharedPreferences("inviteCalls", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("email_contacts_isInvited", null);
            if (json != null) {
                Type type = new TypeToken<ArrayList<Friends>>() {
                }.getType();
                isInvitedList = gson.fromJson(json, type);
            }
        }

        if (listfromServer!= null) {
            SharedPreferences preferences = getSharedPreferences("inviteCalls", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();

            String json = gson.toJson(listfromServer);

            editor.putString("email_contacts_listfromServer", json);
            editor.commit();
        } else {
            SharedPreferences sharedPrefs = getSharedPreferences("inviteCalls", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("email_contacts_listfromServer", null);
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

            disconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(disconnect.getText().equals("Disconnect")) {

                        SharedPreferences preferences1 = getSharedPreferences("inviteCalls",MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = preferences1.edit();
                        editor1.putBoolean("DisconnectEmail",true);
                        editor1.commit();

                        disconnect.setText("Connect");
                        logged_email.setText("Connect to your account");
                        SharedPreferences preferences = getSharedPreferences("invite_lists", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        isBuddyList.clear();
                        isInvitedList.clear();
                        listfromServer.clear();

                        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

                        if (fragmentList == null) {
                            // code that handles no existing fragments
                        }

                        for (Fragment frag : fragmentList )
                        {
                            getSupportFragmentManager().beginTransaction().remove(frag).commit();
                        }
                    }else if(disconnect.getText().equals("Connect")){

                        Intent intent = new Intent(FillEmailContacts.this,AuthenticateEmail.class);
                        startActivity(intent);
                    }
                }
            });



//        if (yahooArrayList != null) {
//            SharedPreferences preferences = getSharedPreferences("cred", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            Gson gson = new Gson();
//
//            String json = gson.toJson(yahooArrayList);
//
//            editor.putString("email_contacts_notSelectedYahoo", json);
//            editor.commit();
//        } else {
//            SharedPreferences sharedPrefs = getSharedPreferences("cred", MODE_PRIVATE);
//            Gson gson = new Gson();
//            String json = sharedPrefs.getString("email_contacts_notSelectedYahoo", null);
//            if (json != null) {
//                Type type = new TypeToken<ArrayList<Friends>>() {
//                }.getType();
//                yahooArrayList = gson.fromJson(json, type);
//            }
//        }


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        EmailAdapter adapter = new EmailAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewUserEmail1(), "New Users"+ "     ( "+getApplicationContext().getResources().getString(R.string.Rs)+listfromServer.size()*170+" )");
        adapter.addFragment(new AlreadyInvited1(), "Invited" + "                   ("+isInvitedList.size()+")");
        adapter.addFragment(new BuddiesEmail(),"Buddies" + "                    ("+isBuddyList.size()   +")");
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

//    public class GetInvitedEmail extends AsyncTask<Void, Void, String> {
//
//        Context context;
//
//        String url = BuildConfig.SERVER_URL + "api/v1/user/contacts?userid=" + userId;
//
//        public GetInvitedEmail(Context context) {
//            this.context = context;
//        }
//
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            try {
//                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
//                String tok_sp = toks.getString("token_value", "");
//                HttpResponse response = AppUtils.connectToServerGet(url, tok_sp, null);
//                if (response != null) {
//                    HttpEntity ent = response.getEntity();
//                    String responseString = EntityUtils.toString(ent, "UTF-8");
//                    if (response.getStatusLine().getStatusCode() != 200) {
//
//
//                        Log.e("MeshCommunication", "Server returned code "
//                                + response.getStatusLine().getStatusCode());
//                        return "fail";
//                    } else {
//                        JSONObject jsonObject = new JSONObject(responseString);
//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//
//                            JSONObject json = jsonArray.getJSONObject(i);
//                            String name = json.getString("name");
//                            String phone = json.getString("phone");
//                            String email = json.getString("email");
//                            Boolean isBuddy = json.getBoolean("isBuddy");
//                            Boolean isInvited = json.getBoolean("isInvited");
//                            Friends friends = new Friends(phone, email, isBuddy, isInvited, name);
//                            if (isBuddy)
//                                isBuddyList.add(friends);
//                            if (!isBuddy && isInvited)
//                                isInvitedList.add(friends);
//                            if (!isBuddy && !isInvited)
//                                listfromServer.add(friends);
//                        }
//
//
//                    }
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//        }
//
//
//    }
}
