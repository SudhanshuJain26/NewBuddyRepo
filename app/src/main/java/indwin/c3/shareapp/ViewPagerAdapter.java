package indwin.c3.shareapp;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ANiket Verma(Digo) on 25/12/15.
 */
public class ViewPagerAdapter extends PagerAdapter {
    int size;
    Activity act;
    View layout;
    TextView pagenumber1,pagenumber2,pagenumber3,pagenumber4,pagenumber5;
    ImageView pageImage;
    Context context;
    String[] t=new String[500];
    LayoutInflater inflater;
    static int w;

    public ViewPagerAdapter(Landing mainActivity, int noofsize,Context c) {
        // TODO Auto-generated constructor stub
        size = noofsize;
        act = mainActivity;
        context=c;
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return size;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub

        layout = inflater.inflate(R.layout.pages,container,false);

        try {
            View dot1=(View)layout.findViewById(R.id.c1);
            View dot2=(View)layout.findViewById(R.id.c2);
            View dot3=(View)layout.findViewById(R.id.c3);
            TextView start=(TextView)layout.findViewById(R.id.start);
            int pagenumberTxt = position;
            if(position==0){
                dot1.setVisibility(View.GONE);
                dot1.setBackgroundResource(R.drawable.circle2);
                dot2.setVisibility(View.GONE);
                dot3.setVisibility(View.GONE);
                TextView t=(TextView)layout.findViewById(R.id.textland);
                t.setText("Products starting at");
                TextView t1=(TextView)layout.findViewById(R.id.textland2);
                t1.setText("Rs 899/month");
                ImageView img= (ImageView) layout.findViewById(R.id.imageView1);
                img.setImageResource(R.drawable.onboard1);}
            if(position==1){
                dot1.setVisibility(View.GONE);
                dot2.setVisibility(View.GONE);
                dot2.setBackgroundResource(R.drawable.circle2);
                dot3.setVisibility(View.GONE);
                TextView t=(TextView)layout.findViewById(R.id.textland);
                t.setText("Convert Big MRPs into");
                TextView t1=(TextView)layout.findViewById(R.id.textland2);
                t1.setText("Small Monthly Payments");
                ImageView img= (ImageView) layout.findViewById(R.id.imageView1);
                img.setImageResource(R.drawable.onboard2);}
            if(position==2){
                dot1.setVisibility(View.GONE);
                dot2.setVisibility(View.GONE);

                dot3.setVisibility(View.GONE);
                dot3.setBackgroundResource(R.drawable.circle2);
                TextView t=(TextView)layout.findViewById(R.id.textland);
                t.setText("Upgrade your machine");
                TextView t1=(TextView)layout.findViewById(R.id.textland2);
                t1.setText("and your Student Life");

                ImageView img= (ImageView) layout.findViewById(R.id.imageView1);
                img.setImageResource(R.drawable.onboard3);}
//ImageView img=            (ImageView)layout.findViewById(R.id.imageView1);
//            ImageView ww=(ImageView) layout.findViewById(R.id.imageView1);
//            w=ww.get

        }
        catch (Exception e){e.printStackTrace();}
        //pagenumber1.setText("Now your in Page No  " +pagenumberTxt );


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

    // }
}
