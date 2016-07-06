package indwin.c3.shareapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import indwin.c3.shareapp.models.Friends;

/**
 * Created by sudhanshu on 6/7/16.
 */
public class BlinkingFragment extends Fragment {

    GIFView blinking;
    TextView pleaseConnect;

    static FirstPageFragmentListener firstPageListener;

    public BlinkingFragment() {
    }

    public  static BlinkingFragment init1() {
        BlinkingFragment fragment = new BlinkingFragment();

        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.blinking, container, false);
        blinking = (GIFView)rootView.findViewById(R.id.cat);
        blinking.setGIFResource(R.drawable.blinking);
        pleaseConnect = (TextView)rootView.findViewById(R.id.text);
        return rootView;
    }
}
