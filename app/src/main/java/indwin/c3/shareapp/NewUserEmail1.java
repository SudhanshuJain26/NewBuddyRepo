package indwin.c3.shareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import indwin.c3.shareapp.activities.FillEmailContacts;
import indwin.c3.shareapp.activities.ShowSelectedItems;
import indwin.c3.shareapp.models.Friends;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;

/**
 * Created by sudhanshu on 28/6/16.
 */
public class NewUserEmail1 extends ListFragment {

    private ListView mListView;
    public int number =0;
    private static boolean isNotAdded = true;
    private CheckBox checkBox_header;
    CustomAdapter adapter;
    List<Friends> friendsArrayList = new ArrayList<>();
    List<Friends> selectedFriendlist = new ArrayList<>();
    public boolean master =true;
    String userId;
    String tok_sp;
    SharedPreferences sh_otp;
    String name;
    String referralCode;
    boolean check1;
    View headerView;

    Button done;



    SparseBooleanArray mChecked = new SparseBooleanArray();


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        headerView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                checkBox_header.setChecked(!checkBox_header.isChecked());

                if(checkBox_header.isChecked()) {
                    done.setVisibility(View.VISIBLE);
                    master =true;
                }
                else {
                    done.setVisibility(View.GONE);
                    selectedFriendlist.clear();
                    master = false;
                }

                    /*
                     * Set all the checkbox to True/False
                     */


                for (int i = 0; i < friendsArrayList.size(); i++) {
                    mChecked.put(i, checkBox_header.isChecked());
                }

                    /*
                     * Update View
                     */
                adapter.notifyDataSetChanged();

            }
        });

        checkBox_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox_header.isChecked()) {
                    done.setVisibility(View.VISIBLE);
                    master =true;
                }
                else {
                    done.setVisibility(View.GONE);
                    selectedFriendlist.clear();
                    master = false;
                }

                for (int i = 0; i < friendsArrayList.size(); i++) {
                    mChecked.put(i, checkBox_header.isChecked());
                }

                    /*
                     * Update View
                     */
                adapter.notifyDataSetChanged();
            }
        });



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = getContext().getSharedPreferences("preferencename2", 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("mChecked1_size", mChecked.size());

                for(int i=0;i<mChecked.size();i++)
                    editor.putBoolean( "mChecked2_" + i, mChecked.get(i));

                editor.apply();

                for(int i =0;i<mChecked.size();i++){
                    if(mChecked.get(i)){
                        selectedFriendlist.add(friendsArrayList.get(i));
                    }
                }

                Intent intent = new Intent(getContext(), ShowSelectedItems.class);
                intent.putExtra("email",2);

                if(selectedFriendlist!=null || selectedFriendlist.size()!=0){


                    Set<Friends> hs = new HashSet<>();
                    hs.addAll(selectedFriendlist);
                    selectedFriendlist.clear();
                    selectedFriendlist.addAll(hs);



                    SharedPreferences preferences = getContext().getSharedPreferences("selectedContacts",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = preferences.edit();
                    Gson gson = new Gson();

                    String json = gson.toJson(selectedFriendlist);

                    editor1.putString("email_contacts_selected", json);
                    editor1.commit();

                }
                getContext().startActivity(intent);
                getActivity().finish();

            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserModel userModel = AppUtils.getUserObject(getContext());
        userId = userModel.getUserId();
        name = userModel.getName();
        friendsArrayList = (List<Friends>) getArguments().getSerializable("list");
        mChecked = getArray();

        SharedPreferences toks = getContext().getSharedPreferences("token", Context.MODE_PRIVATE);
        tok_sp = toks.getString("token_value", "");
        sh_otp = getContext().getSharedPreferences("buddyotp",Context.MODE_PRIVATE);
        referralCode = sh_otp.getString("rcode","");

//        SharedPreferences sharedPrefs = getContext().getSharedPreferences("cred",Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPrefs.getString("phone_contacts", null);
//        if(json!=null) {
//            Type type = new TypeToken<ArrayList<Friends>>() {
//            }.getType();
//            retrievedFriendsList = gson.fromJson(json, type);
//        }
        //new SendContactstoServer(getContext()).execute();
    }

    public  static NewUserEmail1 init(ArrayList<Friends> list) {
        NewUserEmail1 fragment = new NewUserEmail1();
        Bundle args = new Bundle();
        args.putSerializable("list", list);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.email_user_list, container, false);
        mListView = (ListView) rootView.findViewById(android.R.id.list);
        headerView = inflater.inflate(R.layout.header1,
                mListView, false);
        final FrameLayout frameLayout = (FrameLayout)rootView.findViewById(R.id.frame2);
        if(FillEmailContacts.yahooMalfunction) {

            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    frameLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFinish() {
                    frameLayout.setVisibility(View.GONE);

                }
            }.start();

            FillEmailContacts.yahooMalfunction=false;
        }
        checkBox_header = (CheckBox) headerView.findViewById(R.id.checkbox);
        adapter = new CustomAdapter(getContext(),friendsArrayList);
        done = (Button) rootView.findViewById(R.id.done);
        mListView.addHeaderView(headerView);
        mListView.setAdapter(adapter);
        return rootView;
    }

    public class CustomAdapter extends BaseAdapter {

        Context context;
        List<Friends> friendsList;
        SharedPreferences prefs;


        public CustomAdapter(final Context context, List<Friends> friendsList) {
            this.context = context;
            this.friendsList = friendsList;
            prefs = context.getSharedPreferences("CHECKBOX_STATE_EMAIL",Context.MODE_PRIVATE);

        }

        @Override
        public int getCount() {

            /*nhp
             * Length of our listView
             */

            return friendsList.size();
        }

        @Override
        public Object getItem(int position) {

            /*
             * Current Item
             */
            return friendsList.get(position);
        }

        @Override
        public long getItemId(int position) {

            /*
             * Current Item's ID
             */
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View mView = convertView;

            final LayoutInflater sInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);

            mView = sInflater.inflate(R.layout.contact_item, null, false);

            final Friends rowItem = (Friends) getItem(position);



            final TextView name = (TextView) mView.findViewById(R.id.contact_name);
            final ImageView imageView = (ImageView) mView.findViewById(R.id.contact_image);
            final CheckBox checkBox = (CheckBox) mView.findViewById(R.id.checkBox);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.setChecked(!checkBox.isChecked());
                }
            });

            if(position%3==0)
                imageView.setImageResource(R.drawable.blueuser1x);
            if(position%3==1)
                imageView.setImageResource(R.drawable.reduser1x);
            if(position%3==2)
                imageView.setImageResource(R.drawable.greenuser1x);
            if(mChecked.size()==0){
                mChecked = getArray();
            }


            name.setText(rowItem.getName());
            if(prefs.contains(rowItem.getName()))
                checkBox.setChecked(prefs.getBoolean(rowItem.getName(), false));
//            if(prefs.getBoolean(rowItem.getName(), false)){
//                if(!selectedFriendlist.contains(rowItem)) {
//                    selectedFriendlist.add(rowItem);
//                    number++;
//                }
//            }else{
//                if(selectedFriendlist.contains(rowItem)){
//                    selectedFriendlist.remove(rowItem);
//                    number--;
//                }
//            }
//            if(selectedFriendlist.size()>0){
//                done.setVisibility(View.VISIBLE);
//            }else{
//                done.setVisibility(View.GONE);
//            }
            String TAG = String.valueOf(prefs.getBoolean(rowItem.getName(),false));
            Log.i("TAG",TAG + position);
            checkBox.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            if(prefs.getBoolean(rowItem.getName(),false)){
//
//                            }else {
                            prefs.edit().putBoolean(rowItem.getName(), isChecked).apply();
                            done.setVisibility(View.VISIBLE);
//                            }
//                            String TAGs = String.valueOf(prefs.getBoolean(rowItem.getName(),false));
//                            Log.i("TAGs",TAGs + position);
//
//
                            if (isChecked) {

                                number++;

                                mChecked.put(position, isChecked);
//                                    if (!selectedFriendlist.contains(getItem(position))) {
//                                        selectedFriendlist.add((Friends)getItem(position));
//
//                                    }


                                if (isAllValuesChecked()) {
                                    checkBox_header.setChecked(isChecked);
                                }


                            } else {

                                mChecked.put(position,isChecked);


//                                    if (selectedFriendlist.contains(getItem(position))) {
//                                        selectedFriendlist.remove(getItem(position));
//
//                                    }
//
//
                                number--;

                                checkBox_header.setChecked(isChecked);

                            }
//
                            if(master) {

                                if (number == 0)
                                    done.setVisibility(View.GONE);
                                if (!checkBox_header.isChecked() && number != 0)
                                    done.setVisibility(View.VISIBLE);
                            }else{
                                if(number!=0)
                                    done.setVisibility(View.VISIBLE);
                                else
                                    done.setVisibility(View.GONE);
                            }
//
                        }
                    });

            checkBox.setChecked((mChecked.get(position) == true ? true : false));
//

            /* **************ADDING CONTENTS**************** */

            /*
             * Return View here
             */
            return mView;
        }

        /*
         * Find if all value
         * s are checked.
         */
        protected boolean isAllValuesChecked() {


            for (int i = 0; i < friendsList.size(); i++) {
                if (!mChecked.get(i)) {
                    return false;
                }
            }

            return true;
        }

    }

    public SparseBooleanArray getArray(){
        SharedPreferences prefs = getContext().getSharedPreferences("preferencename2", 0);
        SparseBooleanArray array = new SparseBooleanArray();
        for(int i=0;i<friendsArrayList.size();i++)
            array.put(i,prefs.getBoolean("mChecked2_"+i,false));

        return array;
    }
}


