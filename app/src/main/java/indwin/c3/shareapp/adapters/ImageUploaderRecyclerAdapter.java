package indwin.c3.shareapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.activities.FullScreenActivity;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.Constants;

/**
 * Created by shubhang on 19/03/16.
 */
public class ImageUploaderRecyclerAdapter extends
                                          RecyclerView.Adapter<ImageUploaderRecyclerAdapter.ViewHolder> {

    private ArrayList<String> images;
    private Context mContext;
    private String title;
    boolean disableAddButton;

    public ImageUploaderRecyclerAdapter(Context context, ArrayList<String> images, String title, boolean disableAddButton) {
        mContext = context;
        this.images = images;
        this.title = title;
        this.disableAddButton = disableAddButton;
    }

    @Override
    public ImageUploaderRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View recyclerView = inflater.inflate(R.layout.image_uploader_recycler_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(recyclerView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ImageUploaderRecyclerAdapter.ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        if (images.get(position).equals("add")) {
            Picasso.with(mContext).load(images.get(position)).fit().centerInside().placeholder(R.drawable.plus_transparent).into(viewHolder.image);
        } else {
            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, FullScreenActivity.class);
                    intent.putExtra(AppUtils.IMAGE, images);
                    intent.putExtra(Constants.DISABLE_ADD,disableAddButton);
                    intent.putExtra(AppUtils.POSITION, position);
                    intent.putExtra(AppUtils.HEADING, title);
                    mContext.startActivity(intent);
                }
            });
            if (images.get(position).contains("http"))
                Picasso.with(mContext).load(images.get(position)).fit().placeholder(R.drawable.downloading).into(viewHolder.image);
            else {
                final File imgFile = new File(images.get(position));
                if (imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    viewHolder.image.setImageBitmap(bitmap);

                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView image;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.addImage);
        }
    }
}