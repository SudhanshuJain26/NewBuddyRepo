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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.adapters.AlreadyInvitedAdapter;
import indwin.c3.shareapp.models.Friends;

/**
 * Created by sudhanshu on 28/6/16.
 */
public class AlreadyInvited1 extends Fragment {

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
        invitedList = (ArrayList<Friends>)getArguments().getSerializable("list1");

    }


    public  static AlreadyInvited1 init(List<Friends> list1) {
        AlreadyInvited1 fragment = new AlreadyInvited1();
        Bundle args = new Bundle();
        args.putSerializable("list1", (Serializable)list1);
        fragment.setArguments(args);
        return fragment;
    }
}
