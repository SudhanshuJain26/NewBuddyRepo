package indwin.c3.shareapp.activities;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.fragments.ProfileFormStep1Fragment1;
import indwin.c3.shareapp.models.OnBackPressedListener;
import io.intercom.android.sdk.Intercom;

public class ProfileFormStep1 extends AppCompatActivity {

    public static boolean isBackInsideFrag = false;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_form_step1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            TextView headerTitle = (TextView) findViewById(R.id.activity_header);
            headerTitle.setText("Verify your Identity");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment1, new ProfileFormStep1Fragment1(), "Fragment1Tag");
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

//            mPager = (ViewPager) findViewById(R.id.pager);
//            fragments = new ArrayList<>();
//            fragments.add(new ProfileFormStep1Fragment1());
//            fragments.add(new ProfileFormStep1Fragment2());
//            fragments.add(new ProfileFormStep1Fragment3());
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
    public void onBackPressed() {
//        if (mPager.getCurrentItem() == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
//            super.onBackPressed();
//        } else {
//            // Otherwise, select the previous step.
//            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
//        }
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            //TODO: Perform your logic to pass back press here
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof OnBackPressedListener) {
                    ((OnBackPressedListener) fragment).onBackPressed();
                }
            }
        }
        if (!isBackInsideFrag)
            finish();
    }

//    @Override
//    public void selectPage(int page) {
//        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);
//        mPager.setAdapter(mPagerAdapter);
//        mPager.setCurrentItem(page);
//    }
}
