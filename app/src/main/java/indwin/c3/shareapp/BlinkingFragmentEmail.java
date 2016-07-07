package indwin.c3.shareapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sudhanshu on 7/7/16.
 */
public class BlinkingFragmentEmail extends Fragment {
    GIFView1 blinking;
    TextView pleaseConnect;



    public static BlinkingFragmentEmail init() {
        BlinkingFragmentEmail fragment = new BlinkingFragmentEmail();

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.blinking_email, container, false);
        blinking = (GIFView1)rootView.findViewById(R.id.cat);
        pleaseConnect = (TextView)rootView.findViewById(R.id.text);
        return rootView;
    }
}
