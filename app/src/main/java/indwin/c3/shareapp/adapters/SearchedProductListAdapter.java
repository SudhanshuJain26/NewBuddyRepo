package indwin.c3.shareapp.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import indwin.c3.shareapp.R;
import indwin.c3.shareapp.models.RecentSearchItems;

/**
 * Created by sudhanshu on 3/6/16.
 */
public class SearchedProductListAdapter extends BaseAdapter{

    private List<RecentSearchItems> recentSearchItemses;

    LayoutInflater inflater;



    public SearchedProductListAdapter(List<RecentSearchItems> recentSearchItemses, Context context) {
        this.recentSearchItemses = recentSearchItemses;
        inflater = LayoutInflater.from(context);
    }

    public void addObject(RecentSearchItems item){
        recentSearchItemses.add(0,item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return recentSearchItemses.size();
    }

    @Override
    public Object getItem(int position) {
        return recentSearchItemses.get(position);
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

            //The view is not a recycled one: we have to inflate

            vi = inflater.inflate(R.layout.searchlist_items, parent, false);

            holder = new ViewHolder();

            holder.title = (TextView) vi.findViewById(R.id.title);

           // holder.price = (TextView) vi.findViewById(R.id.price);
            holder.seller = (ImageView)vi.findViewById(R.id.seller);

            vi.setTag(holder);

        } else {

            // View recycled !

            // no need to inflate

            // no need to findViews by id

            holder = (ViewHolder) vi.getTag();

        }
        RecentSearchItems recentSearchItems = recentSearchItemses.get(position);

        holder.title.setText(recentSearchItems.getTitle() + " - Rs. "+recentSearchItems.getPrice() );
        //holder.price.setText(recentSearchItems.getPrice());
        switch (recentSearchItems.getSeller()) {
            case "flipkart":
                holder.seller.setImageResource(R.drawable.fk_fav1x);
                break;
            case "amazon":
                holder.seller.setImageResource(R.drawable.amazon_fav1x);
                break;
            case "snapdeal":
                holder.seller.setImageResource(R.drawable.sdeal_fav1x);
                break;
            case "paytm":
                holder.seller.setImageResource(R.drawable.paytm_fav1x);
                break;
            default:
                holder.seller.setImageResource(R.drawable.empty);
        }

        return vi;
    }


     static class ViewHolder {

        public TextView title;
        public ImageView seller;
        //public TextView price;



    }

}

