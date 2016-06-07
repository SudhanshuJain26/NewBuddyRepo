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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.activities.FullScreenActivity;
import indwin.c3.shareapp.models.Image;
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
    private Image image;
    private TextView textView;
    private String type;

    public ImageUploaderRecyclerAdapter(Context context, ArrayList<String> images, String title, boolean disableAddButton) {
        mContext = context;
        this.images = images;
        this.title = title;
        this.disableAddButton = disableAddButton;

    }


    public ImageUploaderRecyclerAdapter(Context context, Image image, String title, boolean disableAddButton, String type) {
        mContext = context;
        this.image = image;
        this.title = title;
        this.disableAddButton = disableAddButton;
        this.type = type;
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
        int validUrlSize = image.getValidImgUrls().size();
        int invalidUrlSize = image.getInvalidImgUrls().size();
        int imgUrlSize = image.getImgUrls().size();
        String imgUrl = "";
        if (Constants.IMAGE_TYPE.ADDRESS_PROOF.toString().equals(type) || Constants.IMAGE_TYPE.COLLEGE_ID.toString().equals(type)) {
            if (position == 0) {
                {
                    if (image.getFront() != null)
                        imgUrl = image.getFront().getImgUrl();
                    viewHolder.textView.setText("Front");
                    viewHolder.textView.setVisibility(View.VISIBLE);
                }
            } else if (position == 1) {
                viewHolder.textView.setText("Back");
                if (image.getBack() != null)
                    imgUrl = image.getBack().getImgUrl();
                viewHolder.textView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.textView.setVisibility(View.GONE);
                if ((position - 1) <= validUrlSize && validUrlSize > 0) {
                    imgUrl = image.getValidImgUrls().get(position - 2);
                } else if ((position - 1) <= (validUrlSize + invalidUrlSize) && invalidUrlSize > 0) {
                    imgUrl = image.getInvalidImgUrls().get(position - 2 - validUrlSize);
                } else if ((position - 1) <= (validUrlSize + invalidUrlSize + imgUrlSize) && imgUrlSize > 0) {
                    imgUrl = image.getImgUrls().get(position - 2 - validUrlSize - invalidUrlSize);
                }
            }
        } else {
            viewHolder.textView.setVisibility(View.GONE);
            if ((position + 1) <= validUrlSize && validUrlSize > 0) {
                imgUrl = image.getValidImgUrls().get(position);
            } else if ((position + 1) <= (validUrlSize + invalidUrlSize) && invalidUrlSize > 0) {
                imgUrl = image.getInvalidImgUrls().get(position - validUrlSize);
            } else if ((position + 1) <= (validUrlSize + invalidUrlSize + imgUrlSize) && imgUrlSize > 0) {
                imgUrl = image.getImgUrls().get(position - validUrlSize - invalidUrlSize);

                if (image.getImgUrls().get(position - validUrlSize - invalidUrlSize).equals("add")) {
                    Picasso.with(mContext).load("add").placeholder(R.drawable.plus_transparent).into(viewHolder.image);
                }
            }
        }
        if (AppUtils.isNotEmpty(imgUrl) && !imgUrl.equals("add")) {
            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, FullScreenActivity.class);
                    intent.putExtra(AppUtils.IMAGE_TYPE, type);
                    intent.putExtra(Constants.DISABLE_ADD, disableAddButton);
                    intent.putExtra(AppUtils.POSITION, position);
                    intent.putExtra(AppUtils.HEADING, title);
                    mContext.startActivity(intent);
                }
            });
        }
        if (imgUrl.contains("http"))
            Picasso.with(mContext).load(imgUrl).fit().placeholder(R.drawable.downloading).into(viewHolder.image);
        else {
            final File imgFile = new File(imgUrl);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                viewHolder.image.setImageBitmap(bitmap);

            }
        }

        // Get the data model based on position

    }

    @Override
    public int getItemCount() {
        int imgUrlSize = 0;
        imgUrlSize = image.getImgUrls().size() + image.getValidImgUrls().size() + image.getInvalidImgUrls().size();

        if (Constants.IMAGE_TYPE.ADDRESS_PROOF.toString().equals(type) || Constants.IMAGE_TYPE.COLLEGE_ID.toString().equals(type)) {
            return 2 + imgUrlSize;
        } else {
            if (disableAddButton) {
                return imgUrlSize;
            }
            return imgUrlSize;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView image;
        public TextView textView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_image);
            image = (ImageView) itemView.findViewById(R.id.addImage);
        }
    }
}