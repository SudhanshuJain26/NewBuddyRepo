package indwin.c3.shareapp.activities;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.application.BuddyApplication;
import indwin.c3.shareapp.fragments.ProfileFormStep2Fragment1;
import indwin.c3.shareapp.utils.AppUtils;
import io.intercom.android.sdk.Intercom;

public class ProfileFormStep2 extends AppCompatActivity {
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form_step2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            TextView headerTitle = (TextView) findViewById(R.id.activity_header);
            headerTitle.setText("Setup Automatic Repayments");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment1, new ProfileFormStep2Fragment1(), "Fragment1Tag");
            ft.commit();
            ImageView inter = (ImageView) findViewById(R.id.interCom);
            inter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
                        Intercom.client().displayMessageComposer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            //            ArrayList<Fragment> fragments = new ArrayList<>();
            //            fragments.add(new ProfileFormStep2Fragment1());
            //            fragments.add(new ProfileFormStep2Fragment2());
            //            fragments.add(new ProfileFormStep2Fragment3());
            //            mPager = (ViewPager) findViewById(R.id.pager);
            //            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);
            //            mPager.setAdapter(mPagerAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.sendGoogleAnalytics((BuddyApplication) getApplication());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //    @Override
    //    public void selectPage(int page) {
    //        mPager.setCurrentItem(page);
    //    }
}
