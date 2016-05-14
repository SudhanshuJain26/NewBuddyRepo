package indwin.c3.shareapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by USER on 2/25/2016.
 */
public class CustomViewHolder extends RecyclerView.ViewHolder {
    protected ImageView imageView;
    protected TextView textView;

    public CustomViewHolder(View view) {
        super(view);
//        this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
        this.textView = (TextView) view.findViewById(R.id.title);
    }
}
