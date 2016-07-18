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

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.adapters.AlreadyInvitedAdapter;
import indwin.c3.shareapp.models.Friends;

/**
 * Created by sudhanshu on 24/6/16.
 */
public class AlreadyInvited extends Fragment {

    ArrayList<Friends> invitedList = new ArrayList<>();
    ListView listView;
    AlreadyInvitedAdapter adp;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.already_invited_phone, container, false);
        listView = (ListView)rootView.findViewById(R.id.list);
        adp = new AlreadyInvitedAdapter(getContext(),invitedList);
        listView.setAdapter(adp);
        return  rootView;



    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invitedList = (ArrayList<Friends>)getArguments().getSerializable("list");

    }

    public static AlreadyInvited init(ArrayList<Friends>list) {
        AlreadyInvited truitonFrag = new AlreadyInvited();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putSerializable("list", list);
        truitonFrag.setArguments(args);
        return truitonFrag;
    }



}
