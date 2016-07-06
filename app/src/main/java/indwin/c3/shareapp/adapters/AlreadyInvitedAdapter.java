package indwin.c3.shareapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.models.Friends;


/**
 * Created by sudhanshu on 2/7/16.
 */
public class AlreadyInvitedAdapter extends BaseAdapter {
    ArrayList<Friends> friendsList;
    Context context;
    LayoutInflater inflater;

    public AlreadyInvitedAdapter(Context context, ArrayList<Friends> friendsList) {
        this.context = context;
        this.friendsList = friendsList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return friendsList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;             //trying to reuse a recycled view

        ViewHolder holder = null;

        if (vi == null) {



            vi = inflater.inflate(R.layout.already_invited_item, parent, false);

            holder = new ViewHolder();

            holder.title = (TextView) vi.findViewById(R.id.text);
            holder.userImage = (ImageView)vi.findViewById(R.id.user_image);




            vi.setTag(holder);

        } else {



            // no need to inflate

            // no need to findViews by id

            holder = (ViewHolder) vi.getTag();

        }
        String name = friendsList.get(position).getName();


        holder.title.setText(name);
        if(position%3==0)
            holder.userImage.setImageResource(R.drawable.blueuser1x);
        if(position%3==1)
            holder.userImage.setImageResource(R.drawable.reduser1x);
        if(position%3==2)
            holder.userImage.setImageResource(R.drawable.greenuser1x);
        //holder.price.setText(recentSearchItems.getPrice());


        return vi;
    }

    static class ViewHolder {

        public TextView title;
        public ImageView userImage;
        //public TextView price;



    }

}
