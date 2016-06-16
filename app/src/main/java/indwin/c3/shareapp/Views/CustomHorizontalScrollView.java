package indwin.c3.shareapp.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import indwin.c3.shareapp.ZeroChildException;
import indwin.c3.shareapp.adapters.HorizontalScrollViewAdapter;

/**
 * Created by sudhanshu on 16/6/16.
 */
public class CustomHorizontalScrollView extends HorizontalScrollView {

    Context context;

    public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setSmoothScrollingEnabled(true);

    }

    public void setAdapter(Context context, HorizontalScrollViewAdapter mAdapter) {

        try {
            fillViewWithAdapter(mAdapter);
        } catch (ZeroChildException e) {

            e.printStackTrace();
        }
    }

    private void fillViewWithAdapter(HorizontalScrollViewAdapter mAdapter)
            throws ZeroChildException {
        if (getChildCount() == 0) {
            throw new ZeroChildException(
                    "CenterLockHorizontalScrollView must have one child");
        }
        if (getChildCount() == 0 || mAdapter == null)
            return;

        ViewGroup parent = (ViewGroup) getChildAt(0);

        parent.removeAllViews();

        for (int i = 0; i < mAdapter.getCount(); i++) {
            parent.addView(mAdapter.getView(i, null, parent));
        }
    }
}
