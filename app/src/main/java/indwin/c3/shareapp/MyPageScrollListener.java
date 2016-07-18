package indwin.c3.shareapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

/**
 * Created by sudhanshu on 10/7/16.
 */
public class MyPageScrollListener implements ViewPager.OnPageChangeListener {

    private TabLayout mTabLayout;

    public MyPageScrollListener(TabLayout tabLayout) {
        this.mTabLayout = tabLayout;
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(mTabLayout != null) {
            mTabLayout.getTabAt(position).select();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
