package indwin.c3.shareapp.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by shubhang on 04/04/16.
 */
public class SpinnerHintAdapter extends ArrayAdapter<String> {

    public SpinnerHintAdapter(Context theContext, String[] objects, int theLayoutResId) {
        super(theContext, theLayoutResId, objects);
    }

    @Override
    public int getCount() {
        // don't display last item. It is used as hint.
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }
}