package indwin.c3.shareapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

import io.intercom.com.squareup.picasso.Picasso;
import io.intercom.com.squareup.picasso.Target;


/**
 * Created by rock on 5/27/16.
 */
public class TargetButton extends Button implements Target {


    public TargetButton(Context context) {
        super(context);
    }

    public TargetButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TargetButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        setBackgroundDrawable(new BitmapDrawable(bitmap));

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        int a = 0;
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        int a = 0;
    }
}
