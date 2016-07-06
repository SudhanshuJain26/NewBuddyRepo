package indwin.c3.shareapp.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by shubhang on 07/05/16.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> fragments;
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();

    public ScreenSlidePagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        //registeredFragments.put(position, fragment);
        instantiatedFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //registeredFragments.remove(position);
        instantiatedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public Fragment getFragment(final int position) {
        final WeakReference<Fragment> wr = instantiatedFragments.get(position);
        if (wr != null) {
            return wr.get();
        } else {
            return null;
        }
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}