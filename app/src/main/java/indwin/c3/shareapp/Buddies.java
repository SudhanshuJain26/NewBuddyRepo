package indwin.c3.shareapp;

/**
 * Created by sudhanshu on 15/7/16.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import indwin.c3.shareapp.adapters.AlreadyInvitedAdapter;
import indwin.c3.shareapp.models.Friends;

/**
 * Created by sudhanshu on 1/7/16.
 */
public class Buddies extends Fragment {

    ArrayList<Friends> buddyList = new ArrayList<>();
    ListView listView;
    AlreadyInvitedAdapter adp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buddyList = (ArrayList<Friends>)getArguments().getSerializable("list");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.already_invited_phone, container, false);
        listView = (ListView)rootView.findViewById(R.id.list);
        adp = new AlreadyInvitedAdapter(getContext(),buddyList);
        listView.setAdapter(adp);
        return  rootView;
    }

    public static Buddies init(ArrayList<Friends> list) {
        Buddies fragment = new Buddies();
        Bundle args = new Bundle();
        args.putSerializable("list", list);
        fragment.setArguments(args);
        return fragment;
    }

}
