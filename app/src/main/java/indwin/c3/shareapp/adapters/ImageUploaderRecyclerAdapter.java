package indwin.c3.shareapp.adapters;

import android.content.Context;
import android.content.Intent;
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
            if (image.getFrontBankSize(disableAddButton) > 0 && (position + 1) <= image.getFrontBankSize(disableAddButton)) {
                if (position == 0 && (!disableAddButton || (image.getFront() != null && AppUtils.isNotEmpty(image.getFront().getImgUrl())))) {
                    {
                        if (image.getFront() != null && AppUtils.isNotEmpty(image.getFront().getImgUrl())) {
                            imgUrl = image.getFront().getImgUrl();
                            viewHolder.textView.setText("Front");
                            viewHolder.textView.setVisibility(View.VISIBLE);
                        }
                        viewHolder.textView.setText("Front");
                        viewHolder.textView.setVisibility(View.VISIBLE);
                    }
                } else if ((position == 0 || (position == 1 && image.getFrontBankSize(disableAddButton) == 2)) && ((image.getBack() != null && AppUtils.isNotEmpty(image.getBack().getImgUrl()) || !disableAddButton))) {
                    viewHolder.textView.setText("Back");
                    if (image.getBack() != null) {
                        imgUrl = image.getBack().getImgUrl();
                    } else {
                        if (disableAddButton) {
                            Picasso.with(mContext).load(R.mipmap.backside_noimage).fit().placeholder(R.drawable.downloading).into(viewHolder.image);
                        }
                    }
                    viewHolder.textView.setVisibility(View.VISIBLE);
                }
            } else {
                imgUrl = setNormalImages(viewHolder, position, validUrlSize, invalidUrlSize, imgUrlSize);
            }
        } else {
            imgUrl = setNormalImages(viewHolder, position, validUrlSize, invalidUrlSize, imgUrlSize);
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
        if (imgUrl != null) {
            if (imgUrl.contains("http"))
                Picasso.with(mContext).load(imgUrl).fit().placeholder(R.drawable.downloading).into(viewHolder.image);
            else {
                final File imgFile = new File(imgUrl);
                if (imgFile.exists()) {
                    Picasso.with(mContext).load(imgFile).fit().placeholder(R.drawable.downloading).into(viewHolder.image);
                }
            }
        }

        // Get the data model based on position

    }


    private String setNormalImages(ViewHolder viewHolder, int position, int validUrlSize, int invalidUrlSize, int imgUrlSize) {
        String imgUrl = null;
        viewHolder.textView.setVisibility(View.GONE);
        if ((position + 1 - image.getFrontBankSize(disableAddButton)) <= validUrlSize && validUrlSize > 0) {
            imgUrl = image.getValidImgUrls().get(position - image.getFrontBankSize(disableAddButton));
        } else if ((position + 1 - image.getFrontBankSize(disableAddButton)) <= (validUrlSize + invalidUrlSize) && invalidUrlSize > 0) {
            imgUrl = image.getInvalidImgUrls().get(position - image.getFrontBankSize(disableAddButton) - validUrlSize);
        } else if ((position + 1 - image.getFrontBankSize(disableAddButton)) <= (validUrlSize + invalidUrlSize + imgUrlSize) && imgUrlSize > 0) {
            int frontBackSize = 0;
            if (Constants.IMAGE_TYPE.ADDRESS_PROOF.toString().equals(type) || Constants.IMAGE_TYPE.COLLEGE_ID.toString().equals(type)) {
                frontBackSize = image.getFrontBankSize(disableAddButton);
            }
            imgUrl = image.getImgUrls().get(position - frontBackSize - validUrlSize - invalidUrlSize);
        }
        return imgUrl;
    }

    @Override
    public int getItemCount() {
        int imgUrlSize = 0;
        imgUrlSize = image.getTotalImageSize();

        if (Constants.IMAGE_TYPE.ADDRESS_PROOF.toString().equals(type) || Constants.IMAGE_TYPE.COLLEGE_ID.toString().equals(type)) {
            return image.getFrontBankSize(disableAddButton) + imgUrlSize;
        } else {
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