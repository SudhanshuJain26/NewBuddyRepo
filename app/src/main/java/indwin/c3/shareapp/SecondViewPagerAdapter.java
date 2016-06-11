package indwin.c3.shareapp;


import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sudhanshu on 6/6/16.
 */
public class SecondViewPagerAdapter extends PagerAdapter {

    int size;
    Activity act;
    View layout;
    Context context;
    LayoutInflater inflater;

    public SecondViewPagerAdapter(Context context, int size, HomePage act) {
        this.context = context;
        this.size = size;
        this.act = act;
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layout = inflater.inflate(R.layout.pages2, container, false);
        try {
            View dot1 = (View) layout.findViewById(R.id.c1);
            View dot2 = (View) layout.findViewById(R.id.c2);
            View dot3 = (View) layout.findViewById(R.id.c3);
            View dot4 = (View) layout.findViewById(R.id.c4);

            if (position == 0) {
                dot1.setVisibility(View.GONE);
                dot1.setBackgroundResource(R.drawable.circle2);
                dot2.setVisibility(View.GONE);
                dot3.setVisibility(View.GONE);
                dot4.setVisibility(View.GONE);
                ImageView img = (ImageView) layout.findViewById(R.id.imageView1);
                img.setImageResource(R.drawable.first_image);

            }
            if (position == 1) {
                dot1.setVisibility(View.GONE);
                dot2.setVisibility(View.GONE);
                dot2.setBackgroundResource(R.drawable.circle2);
                dot3.setVisibility(View.GONE);
                dot4.setVisibility(View.GONE);
                ImageView img = (ImageView) layout.findViewById(R.id.imageView1);
                img.setImageResource(R.drawable.second_image);
            }
            if (position == 2) {
                dot1.setVisibility(View.GONE);
                dot2.setVisibility(View.GONE);

                dot3.setVisibility(View.GONE);
                dot3.setBackgroundResource(R.drawable.circle2);
                dot4.setVisibility(View.GONE);

                ImageView img = (ImageView) layout.findViewById(R.id.imageView1);
                img.setImageResource(R.drawable.third_image);

            }
            if(position==3){
                dot1.setVisibility(View.GONE);
                dot2.setVisibility(View.GONE);
                dot3.setVisibility(View.GONE);
                dot4.setVisibility(View.GONE);
                dot4.setBackgroundResource(R.drawable.circle2);
                ImageView img = (ImageView) layout.findViewById(R.id.imageView1);
                img.setImageResource(R.drawable.forth_image);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        ( container).addView(layout);
        return layout;


    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }
    @Override
    public Parcelable saveState() {
        return null;
    }




    @Override
    public int getCount() {
        return size;
    }


}
