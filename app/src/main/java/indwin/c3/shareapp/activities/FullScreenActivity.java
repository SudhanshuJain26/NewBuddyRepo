package indwin.c3.shareapp.activities;

import android.content.Context;
import android.content.Intent;
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
import java.util.List;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.models.Image;
import indwin.c3.shareapp.models.UserModel;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.Constants;

public class FullScreenActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private List<String> images;
    private TextView currentPageCountTv;
    private String type;
    private Image image;
    int size = 0;
    private TextView headerTitle;
    private String title;
    private TextView verificationStatusTv;
    private ImageView imageStatus;
    private boolean statusApplied;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        currentPageCountTv = (TextView) findViewById(R.id.currentPageCountTv);
        verificationStatusTv = (TextView) findViewById(R.id.verificationStatus);
        imageStatus = (ImageView) findViewById(R.id.imageStatus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        type = intent.getStringExtra(AppUtils.IMAGE_TYPE);
        int position = intent.getIntExtra(AppUtils.POSITION, 0);
        headerTitle = (TextView) findViewById(R.id.activity_header);
        title = intent.getStringExtra(AppUtils.HEADING);
        headerTitle.setText(title);
        statusApplied = true;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        UserModel user = AppUtils.getUserObject(this);
        if (Constants.IMAGE_TYPE.COLLEGE_ID.toString().equals(type)) {
            image = user.getCollegeID();
            if (!user.isAppliedFor1k()) {

                if (position == 1 && image.isFrontEmpty()) {
                    position = position - 1;
                }
                if (position > 1) {
                    if (image.isFrontEmpty()) {
                        position = position - 1;
                    }
                    if (image.isBackEmpty()) {
                        position = position - 1;
                    }
                }
            }

        } else if (Constants.IMAGE_TYPE.ADDRESS_PROOF.toString().equals(type)) {
            image = user.getAddressProof();
            if (!user.isAppliedFor1k() && (image.getFront() == null || AppUtils.isEmpty(image.getFront().getImgUrl())) && (image.getBack() != null && AppUtils.isNotEmpty(image.getBack().getImgUrl()))) {
                position = position - 1;

            }

        } else if (Constants.IMAGE_TYPE.BANK_PROOF.toString().equals(type)) {
            image = user.getBankProof();

        } else if (Constants.IMAGE_TYPE.BANK_STMNTS.toString().equals(type)) {
            image = user.getBankStatement();

        } else if (Constants.IMAGE_TYPE.MARKSHEETS.toString().equals(type)) {
            image = user.getGradeSheet();
        }
        if (image == null) {
            image = new Image();
        }
        size = getToTalCount();
        setHeaderTitle(position);
        currentPageCountTv.setText((position + 1) + " of " + (size));
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new CustomPagerAdapter(this));
        viewPager.setCurrentItem(position);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    private void setHeaderTitle(int position) {
        boolean showStatusImage = true;
        Boolean status = null;
        if (Constants.IMAGE_TYPE.COLLEGE_ID.toString().equals(type) || Constants.IMAGE_TYPE.ADDRESS_PROOF.toString().equals(type)) {
            if (image.getFrontBankSize(statusApplied) > 0 && (position + 1) <= image.getFrontBankSize(statusApplied)) {
                if (position == 0 && (!statusApplied || (image.getFront() != null && AppUtils.isNotEmpty(image.getFront().getImgUrl())))) {
                    headerTitle.setText("Front Side " + title);
                    if (image.getFront() != null) {
                        status = image.getFront().isVerified();
                    } else showStatusImage = false;
                } else if ((position == 0 || (position == 1 && image.getFrontBankSize(statusApplied) == 2)) && ((image.getBack() != null && AppUtils.isNotEmpty(image.getBack().getImgUrl()) || !statusApplied))) {

                    headerTitle.setText("Back Side " + title);
                    if (image.getBack() != null)
                        status = image.getBack().isVerified();
                    else showStatusImage = false;
                }
            } else {
                headerTitle.setText(title);
            }
        } else {
            if (position + 1 <= image.getValidImgUrls().size()) {
                status = true;
            } else if (position + 1 <= (image.getValidImgUrls().size() + image.getInvalidImgUrls().size())) {
                status = false;
            } else {
                status = null;
            }
        }
        if (showStatusImage) {
            imageStatus.setVisibility(View.VISIBLE);
            if (status == null) {
                imageStatus.setImageDrawable(getResources().getDrawable(R.drawable.loader));
                verificationStatusTv.setText(Constants.VERIFICATION_STATUS.UNDER_VEIFICATION.toString());
            } else if (status) {
                imageStatus.setImageDrawable(getResources().getDrawable(R.drawable.complete));
                verificationStatusTv.setText(Constants.VERIFICATION_STATUS.VERIFIED.toString());
            } else if (!status) {
                imageStatus.setImageDrawable(getResources().getDrawable(R.drawable.incomplete));
                verificationStatusTv.setText(Constants.VERIFICATION_STATUS.REJECTED.toString());
            }
        } else {
            imageStatus.setVisibility(View.GONE);
            verificationStatusTv.setText("");
        }
    }

    @Override
    public void onPageSelected(int position) {
        setHeaderTitle(position);

        currentPageCountTv.setText((position + 1) + " of " + size);
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
            return getToTalCount();
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.full_screeen_pager_item, container, false);

             ImageView imageView = (ImageView) itemView.findViewById(R.id.fullImage);
            int validUrlSize = image.getValidImgUrls().size();
            int invalidUrlSize = image.getInvalidImgUrls().size();
            int imgUrlSize = image.getImgUrls().size();
            String imgUrl = "";
            if (Constants.IMAGE_TYPE.ADDRESS_PROOF.toString().equals(type) || Constants.IMAGE_TYPE.COLLEGE_ID.toString().equals(type)) {
                if (image.getFrontBankSize(statusApplied) > 0 && (position + 1) <= image.getFrontBankSize(statusApplied)) {
                    if (position == 0 && (!statusApplied || (image.getFront() != null && AppUtils.isNotEmpty(image.getFront().getImgUrl())))) {
                        {
                            if (image.getFront() != null && AppUtils.isNotEmpty(image.getFront().getImgUrl()))
                                imgUrl = image.getFront().getImgUrl();
                            else
                                Picasso.with(mContext).load(R.mipmap.frontside_noimage).fit().placeholder(R.drawable.downloading).into(imageView);

                        }
                    } else if ((position == 0 || (position == 1 && image.getFrontBankSize(statusApplied) == 2)) && ((image.getBack() != null && AppUtils.isNotEmpty(image.getBack().getImgUrl()) || !statusApplied))) {

                        imgUrl = image.getBack().getImgUrl();

                    }
                } else {
                    imgUrl = setNormalImages(position, validUrlSize, invalidUrlSize, imgUrlSize);
                }
            } else {

                imgUrl = setNormalImages(position, validUrlSize, invalidUrlSize, imgUrlSize);
            }

            if (AppUtils.isNotEmpty(imgUrl)) {
                if (imgUrl.contains("http")) {
                    Picasso.with(mContext).load(imgUrl).fit().placeholder(R.drawable.downloading).into(imageView);
                } else {
                    final File imgFile = new File(imgUrl);
                    if (imgFile.exists()) {
                        Picasso.with(mContext).load(imgFile).placeholder(R.drawable.downloading).into(imageView);
                    }
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

    private int getToTalCount() {
        int imgUrlSize = 0;
        imgUrlSize = image.getTotalImageSize();

        if (Constants.IMAGE_TYPE.ADDRESS_PROOF.toString().equals(type) || Constants.IMAGE_TYPE.COLLEGE_ID.toString().equals(type)) {
            return image.getFrontBankSize(statusApplied) + imgUrlSize;
        } else {
            return imgUrlSize;
        }
    }

    private String setNormalImages(int position, int validUrlSize, int invalidUrlSize, int imgUrlSize) {
        String imgUrl = null;
        if ((position + 1 - image.getFrontBankSize(statusApplied)) <= validUrlSize && validUrlSize > 0) {
            imgUrl = image.getValidImgUrls().get(position - image.getFrontBankSize(statusApplied));
        } else if ((position + 1 - image.getFrontBankSize(statusApplied)) <= (validUrlSize + invalidUrlSize) && invalidUrlSize > 0) {
            imgUrl = image.getInvalidImgUrls().get(position - image.getFrontBankSize(statusApplied) - validUrlSize);
        } else if ((position + 1 - image.getFrontBankSize(statusApplied)) <= (validUrlSize + invalidUrlSize + imgUrlSize) && imgUrlSize > 0) {
            imgUrl = image.getImgUrls().get(position - image.getFrontBankSize(statusApplied) - validUrlSize - invalidUrlSize);
        }
        return imgUrl;
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
