package indwin.c3.shareapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.Constants;

public class FullScreenActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private List<String> images;
    private TextView currentPageCountTv;

    private boolean addDisabled;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        currentPageCountTv = (TextView) findViewById(R.id.currentPageCountTv);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        images = (ArrayList<String>) intent.getSerializableExtra(AppUtils.IMAGE);
        int position = intent.getIntExtra(AppUtils.POSITION, 0);
        addDisabled = intent.getBooleanExtra(Constants.DISABLE_ADD, false);
        if (addDisabled) {
            size = images.size();
        } else {
            size = images.size() - 1;
        }
        TextView headerTitle = (TextView) findViewById(R.id.activity_header);
        headerTitle.setText(intent.getStringExtra(AppUtils.HEADING));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new CustomPagerAdapter(this));
        viewPager.setCurrentItem(position);
        viewPager.setOnPageChangeListener(this);
        currentPageCountTv.setText((position + 1) + " of " + (size));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        currentPageCountTv.setText((position + 1) + " of " + (size));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.full_screeen_pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.fullImage);
            String imagePath = images.get(position);

            if (imagePath.contains("http"))
                Picasso.with(FullScreenActivity.this).load(imagePath).fit().placeholder(R.drawable.downloading).into(imageView);
            else {
                final File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);

                }
            }

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
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


}
