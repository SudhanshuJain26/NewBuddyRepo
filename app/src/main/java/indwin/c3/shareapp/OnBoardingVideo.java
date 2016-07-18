package indwin.c3.shareapp;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

public class OnBoardingVideo extends AppCompatActivity {

    private VideoView mVideoView;
    ViewPagerAdapter adapter;
    private int previousPosition = 0;

    ImageView dot1;
    ImageView dot2;
    ImageView dot3;
    TextView skip;
    ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_video);
        dot1 = (ImageView) findViewById(R.id.c1small);
        dot2 = (ImageView) findViewById(R.id.c2small);
        dot3 = (ImageView) findViewById(R.id.c3small);

        skip = (TextView)findViewById(R.id.skip);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);

        ArrayList<Fragment> videoFragments = new ArrayList<Fragment>();
        videoFragments.add(new VideoFragment(0, viewPager));
        videoFragments.add(new VideoFragment(1, viewPager));
        videoFragments.add(new VideoFragment(2, viewPager));
        videoFragments.add(new SignUpFragment());
       // videoFragments.add(new SignUpFragment());
        final ViewPagerAdapterVideo adapter = new ViewPagerAdapterVideo(this, getSupportFragmentManager(), videoFragments);
        viewPager.setAdapter(adapter);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3,true);
            }
        });

        dot1.setBackgroundResource(R.drawable.circle2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            if(position<3) {
                dot1.setVisibility(View.VISIBLE);
                dot2.setVisibility(View.VISIBLE);
                dot3.setVisibility(View.VISIBLE);
                skip.setVisibility(View.VISIBLE);
                skip.setText("skip");
                VideoFragment previousFragment = (VideoFragment) adapter.getFragment(previousPosition);
                previousFragment.stopVideo();
                VideoFragment videoFragment = (VideoFragment) adapter.getFragment(position);
                videoFragment.startVideo(position);
                previousPosition = position;
//
                btnAction(position);
                if (position == 1) {
                    skip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewPager.setCurrentItem(3, true);
                        }
                    });
                }

                if (position == 2) {
                    skip.setText("start");
                    skip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewPager.setCurrentItem(3, true);
                        }
                    });
                }
            }else{
                dot1.setVisibility(View.GONE);
                dot2.setVisibility(View.GONE);
                dot3.setVisibility(View.GONE);
                skip.setVisibility(View.GONE);

            }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void btnAction(int action) {

        switch (action) {
            case 0:
                dot1.setBackgroundResource(R.drawable.circle2);
                dot2.setBackgroundResource(R.drawable.circle);
                dot3.setBackgroundResource(R.drawable.circle);

                break;
            case 1:

                dot2.setBackgroundResource(R.drawable.circle2);
                dot1.setBackgroundResource(R.drawable.circle);
                dot3.setBackgroundResource(R.drawable.circle);
                break;
            case 2:
                dot1.setBackgroundResource(R.drawable.circle);
                dot2.setBackgroundResource(R.drawable.circle);
                dot3.setBackgroundResource(R.drawable.circle2);
                break;
        }
    }



}
