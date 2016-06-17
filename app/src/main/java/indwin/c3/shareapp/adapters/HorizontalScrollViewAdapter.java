package indwin.c3.shareapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import indwin.c3.shareapp.ProductsPage;
import indwin.c3.shareapp.R;
import indwin.c3.shareapp.Splash;
import indwin.c3.shareapp.models.Product;

/**
 * Created by sudhanshu on 16/6/16.
 */
public class HorizontalScrollViewAdapter extends BaseAdapter {

    ArrayList<Product> productsList;
    Context context;
    SharedPreferences userP;
    private Double emi = 0.0;
    private int currDay;

    public HorizontalScrollViewAdapter(ArrayList<Product> productsList, Context context) {
        this.productsList = productsList;
        this.context = context;
        userP = context.getSharedPreferences("token", Context.MODE_PRIVATE);
    }




    @Override
    public int getCount() {
        return productsList.size();
    }

    @Override
    public Object getItem(int position) {
        return productsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        RelativeLayout layout;
        Holder holder;

        if (convertView == null) {

            layout = (RelativeLayout) View.inflate(context,R.layout.product_tile, null);

            holder = new Holder();

            holder.title = (TextView) layout.findViewById(R.id.title);
            holder.price = (TextView)layout.findViewById(R.id.price);
            holder.brand = (ImageView)layout.findViewById(R.id.brand_image);
            holder.item = (ImageView)layout.findViewById(R.id.product_image);
            layout.setTag(holder);

        } else {
            layout = (RelativeLayout) convertView;

            holder = (Holder) layout.getTag();
        }
        final Product product = productsList.get(position);
        holder.title.setText(product.getTitle());
//        holder.price.setText(getApplicationContext(). String.valueOf(setEmi(product)));
        holder.price.setText(context.getApplicationContext().getResources().getString(R.string.Rs) + " " + String.valueOf(setEmi(product))+ " per month ");
//        price2.setText(getApplicationContext().getString(R.string.Rs) + " " + String.valueOf(emi02.intValue()) + " per month");
        Picasso.with(context)
                .load(product.getImgUrl())
                .placeholder(R.drawable.emptyimageproducts)
                .into(holder.item);
        if (product.getSeller().equals("flipkart"))
            holder.brand.setImageResource(R.drawable.fk_fav1x);
        if (product.getSeller().equals("amazon"))
            holder.brand.setImageResource(R.drawable.amazon_fav1x);
        if (product.getSeller().equals("paytm"))
            holder.brand.setImageResource(R.drawable.paytm_fav1x);
        if (product.getSeller().equals("snapdeal"))
            holder.brand.setImageResource(R.drawable.sdeal_fav1x);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductsPage.class);
                intent.putExtra("seller",product.getSeller());
                intent.putExtra("product", product.getFkid());
                intent.putExtra("page", "api");
                context.startActivity(intent);
            }
        });

//        holder.title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("CustomAdapter","My name is Anthony");
//            }
//        });


        return layout;
    }

    public int setEmi(Product product){

        Double emi = show(product.getSubCategory(),product.getCategory(),product.getBrand(),Integer.parseInt(product.getSellingPrice()));
        return emi.intValue();


    }

    public Double show(String subcat, String cat, String brand, int price) {

        int loanPrice = setLoanAmt(price);
        int monthsallowed = months(subcat, cat, brand, price);
//
        Double rate = 21.0 / 1200.0;
        int d = 0;
        if (price <= 5000) {
            emi = price * 0.8 / monthsallowed;
        } else {

            Date date = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String curr = df.format(date);
            String currentDay = "";
            for (int j = curr.length() - 2; j < curr.length(); j++) {

                currentDay += curr.charAt(j);
            }

            currDay = Integer.parseInt(currentDay);
            if (currDay <= 15)
                d = 35 - currDay;
            else
                d = 65 - currDay;

            emi = Math.ceil((loanPrice * rate * Math.pow(1 + rate, monthsallowed - 1) * (1 + rate * d * 12 / 365)) / (Math.pow(1 + rate, monthsallowed) - 1));
        }
        return emi;
    }

    public int months(String subcat, String cat, String brand, int price)

    {
        int m = 18;
        if ((subcat.equals("Fitness " +
                "Equipments")) || ((subcat.equals("Jewellery"))) || ((subcat.equals("Combos and Kit"))) || ((subcat.equals("Speakers"))) || ((subcat.equals("Team Sports"))) || ((subcat.equals("Racquet Sports"))) || ((subcat.equals("Watches"))) || ((subcat.equals("Health and Personal Care"))) || ((subcat.equals("Leather & Travel Accessories"))) || ((cat.equals("Footwear")))) {
            int mn = 6;
            if (mn < m)
                m = mn;
        } else if ((subcat.equals("Cameras")) || ((subcat.equals("Entertainment"))) || ((subcat.equals("Smartwatches"))) || ((subcat.equals("Smart Headphones"))) || ((subcat.equals("Smart Bands"))) || ((subcat.equals("Digital Accessories"))) || ((subcat.equals("Tablets"))) || ((subcat.equals("Kindle")))) {
            int mn = 12;
            if (mn < m)
                m = mn;
        }
        if ((!brand.equals("Apple")) && !(brand.equals("APPLE"))) {
            int mn = 15;
            if (mn < m)
                m = mn;
        }
        if (price < 5000) {
            int mn = 6;
            if (mn < m)
                m = mn;
        } else if (price < 10000) {
            int mn = 9;
            if (mn < m)
                m = mn;
        } else if (price < 20000) {
            int mn = 12;
            if (mn < m)
                m = mn;
        } else if (price < 40000) {
            int mn = 15;
            if (mn < m)
                m = mn;
        }

        int monthscheck = 0;
        //digo
        String course = userP.getString("course", "");

        if (!course.equals("")) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Date courseDate;
            Double diff = 18.0;
            try {
                courseDate = df.parse(course);
                String newDateString = df.format(courseDate);
                System.out.println(newDateString);
                Long milli = courseDate.getTime();
                Date date = new Date();
                Long currentMilli = date.getTime();
                Double diffDouble = (milli.doubleValue() - currentMilli.doubleValue());
                Double mul = (1000.0 * 60.0 * 60.0 * 24.0 * 365.0);
                diff = diffDouble / mul;
                diff = diff * 12.0;
                diff = Math.floor(diff);
                String curr = df.format(date);
                String currentDay = "";
                for (int j = curr.length() - 2; j < curr.length(); j++) {

                    currentDay += curr.charAt(j);
                }

                currDay = Integer.parseInt(currentDay);
                int months;
                if (currDay > 15)
                    diff -= 1.0;

                if (diff > 0) {
                    months = diff.intValue();
                    if (diff.intValue() == 1)
                        months = 1;
                    else if (diff.intValue() == 2)
                        months = 2;
                    else if (diff.intValue() >= 3 && diff.intValue() <= 5) {
                        months = 3;
                    } else if (diff.intValue() >= 6 && diff.intValue() <= 8) {
                        months = 6;
                    } else if (diff.intValue() >= 9 && diff.intValue() <= 11) {
                        months = 9;
                    } else if (diff.intValue() >= 12 && diff.intValue() <= 14) {
                        months = 12;
                    } else if (diff.intValue() >= 15 && diff.intValue() <= 18) {
                        months = 15;
                    }
                    if (m > months)
                        m = months;
                }


                //                        Toast.makeText(HomePage.this, curr, Toast.LENGTH_SHORT).show();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return m;
    }

    public int setLoanAmt(int sellingPrice) {
        int loanAmt = 0;
        Double value = sellingPrice * .8;
        int loanAmt1 = value.intValue();
        int loanAmt2 = userP.getInt("creditLimit", 0) - userP.getInt("totalBorrowed", 0);
        if (loanAmt1 < loanAmt2) {
            loanAmt = loanAmt1;
        } else {
            loanAmt = loanAmt2;
        }
        return loanAmt;
    }

    private class Holder{
        TextView title;
        TextView price;
        ImageView item;
        ImageView brand;
    }
}
