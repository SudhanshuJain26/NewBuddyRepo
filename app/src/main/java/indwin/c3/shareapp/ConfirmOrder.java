package indwin.c3.shareapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConfirmOrder extends AppCompatActivity {
    private TextView tit, brand, sellerMrpValue, flexVal, checkOutnow, servicepr, downP, sellerpr;
    private ImageView prod, seller;
    View parent;
    private  PopupWindow popup;
    private  int serviceCharge;
    private int downPayment = 0, loanAmt = 0, datToday = 0, w = 0;
    private long newemi=0;
    String sellerName = "";
//    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registerReceiver(broadcastReceiver, new IntentFilter("order"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);


        initi();
        set();
        backpress();
    }

    public void initi() {
        seller = (ImageView) findViewById(R.id.sell);
        checkOutnow = (TextView) findViewById(R.id.checkOutnow);
        tit = (TextView) findViewById(R.id.tit);
        servicepr = (TextView) findViewById(R.id.servicepr);
        brand = (TextView) findViewById(R.id.man);
        downP = (TextView) findViewById(R.id.servicepr);
        sellerpr = (TextView) findViewById(R.id.detailsDis);
        sellerMrpValue = (TextView) findViewById(R.id.sellerMrpValue);
        flexVal = (TextView) findViewById(R.id.sellerMrpValue);
        prod = (ImageView) findViewById(R.id.log);

    }

    public void set() {
        try {
            prod.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(getIntent().getExtras().getString("image"))
                    .placeholder(R.drawable.emptyimageproducts)
                    .into(prod);
        } catch (Exception e) {
            prod.setVisibility(View.INVISIBLE);
        }
        downPayment = (int) (Double.parseDouble(getIntent().getExtras().getString("down")));
        downP.setText(getApplicationContext().getString(R.string.Rs) + downPayment);
        int month = 0;
        int checkday = getIntent().getExtras().getInt("daytodaycheck");
        try {

            java.util.Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            month = cal.get(Calendar.MONTH);
            System.out.print("month" + month + "check" + getIntent().getExtras().getInt("daytoday") + "dad");

//        System.out.println(month+" - "+day+" - "+year);
        } catch (Exception e) {
        }
        if (checkday <= 15) {
            month = (month + 2) % 12;
        } else {
            month = (month + 3) % 12;
        }
        String checkmonths = checkMonths(month);
        TextView setDue = (TextView) findViewById(R.id.flexVal);
        setDue.setText("5 " + checkmonths);

        loanAmt = getIntent().getExtras().getInt("sellingprice") - downPayment;
        tit.setText(getIntent().getExtras().getString("title"));

        if (getIntent().getExtras().getString("brand").trim().length() > 0)

            brand.setText("Manufacturer: " + getIntent().getExtras().getString("brand"));
        else
            brand.setText("");
        sellerName = getIntent().getExtras().getString("seller");
        if (sellerName.equals("amazon")) {
            seller.setImageResource(R.drawable.amazon);
        } else if (sellerName.equals("flipkart")) {
            seller.setImageResource(R.drawable.flipart);
        } else if (sellerName.equals("snapdeal")) {
            seller.setImageResource(R.drawable.snapdeal);
        } else if (sellerName.equals("paytm")) {
            seller.setImageResource(R.drawable.paytm);
        } else if (sellerName.equals("shopclues")) {
            seller.setImageResource(R.drawable.shopclues);
        } else if (sellerName.equals("myntra")) {
            seller.setImageResource(R.drawable.myntra);
        } else if (sellerName.equals("jabong")) {
            seller.setImageResource(R.drawable.jabong);
        }
        serviceCharge = calcServ(sellerName, getIntent().getExtras().getInt("sellingprice"), loanAmt);
//        if (downPayment == 0) {
//            downPayment = serviceCharge;
//        }
        downPayment+=serviceCharge;
        //   sellerMrpValue.setText(getApplicationContext().getString(R.string.Rs)+String.valueOf(getIntent().getExtras().getInt("sellingprice")));
        servicepr.setText(getApplicationContext().getString(R.string.Rs) + " " + downPayment);
        final int dis = getIntent().getExtras().getInt("discount");
//        newemi = emiWithservice(getIntent().getExtras().getInt("sellingprice") - dis - downPayment + serviceCharge, getIntent().getExtras().getInt("sellingprice"), (getIntent().getExtras().getInt("monthforemi")));
//        Toast.makeText(ConfirmOrder.this, newemi, Toast.LENGTH_SHORT).show();
//        if (downPayment == 0)
//            newemi = 0;
        String emifromProd="";
        try{
            emifromProd=getIntent().getExtras().getString("emi");}
        catch(Exception e)
        {}
//        Double em=Double.valueOf(emifromProd);
        newemi=getIntent().getExtras().getLong("emicheck");
        System.out.println(newemi + "may14");
        String t = newemi + " /month" + " x " + getIntent().getExtras().getString("months");
        flexVal.setText(getApplicationContext().getString(R.string.Rs) + newemi+"/month");
        TextView totalPay = (TextView) findViewById(R.id.emimonths);
        long ww=0;
        ww = (getIntent().getExtras().getInt("monthforemi")) * newemi + downPayment;
//        if (downPayment == 0)
//            ww = serviceCharge;
        totalPay.setText(getApplicationContext().getString(R.string.Rs) + ww);
        checkOutnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ConfirmOrder.this, Editaddress.class);
                SharedPreferences cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
                SharedPreferences.Editor et = cred.edit();
                et.putInt("service", serviceCharge);
                et.putInt("emi", (int)newemi);
                et.putInt("downpayment", downPayment-serviceCharge);
                et.putInt("tot", w);
                EditText e = (EditText) findViewById(R.id.edtWant);
                et.putString("usercom", e.getText().toString());
//                et.putString("whichCoupon", whichCoupon);
//                et.putInt("monthtenure", monthsnow);
//                et.putInt("discount", mDis);
//                et.putString("seller",sellerNme);
                et.commit();

                startActivity(in);
//        finish();
            }
        });
        sellerpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout viewGr = (RelativeLayout) (ConfirmOrder.this).findViewById(R.id.pop);
                LayoutInflater inflater = (LayoutInflater) (ConfirmOrder.this).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                parent = inflater.inflate(R.layout.activity_confirm_order, null, false);
                View popUpView = inflater.inflate(R.layout.popup, null, false);

                popup = new PopupWindow(popUpView);
//                        580, true);

                popup.setContentView(popUpView);
                popup.setWidth(ListPopupWindow.WRAP_CONTENT);
                popup.setHeight(ListPopupWindow.WRAP_CONTENT);
                popup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);

                RelativeLayout cover = (RelativeLayout) findViewById(R.id.cover);
//                prod.setTi(Color.parseColor("#CC000000"));
                cover.setVisibility(View.VISIBLE);
                TextView sp = (TextView) popUpView.findViewById(R.id.textPopamt);
                TextView svc = (TextView) popUpView.findViewById(R.id.textPopseramt);
                TextView inter = (TextView) popUpView.findViewById(R.id.textPopserintamt);
                TextView disc = (TextView) popUpView.findViewById(R.id.textPopserdisamt);
                TextView faq = (TextView) popUpView.findViewById(R.id.cbfaq);
                TextView sellerss = (TextView) popUpView.findViewById(R.id.textPopseller);
                sellerss.setText("Charged by "+getIntent().getExtras().getString("seller"));
                faq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(ConfirmOrder.this, ViewForm.class);
                        i.putExtra("which_page", 5);
                        startActivity(i);
                    }
                });
                sp.setText(getApplicationContext().getString(R.string.Rs) + getIntent().getExtras().getInt("sellingprice"));
                svc.setText(getApplicationContext().getString(R.string.Rs) + serviceCharge);
                int interest = 0;
                if (interest < 0)
                    interest = 0;
                int ll=(int)(newemi*getIntent().getExtras().getInt("monthforemi"));
                interest=ll-getIntent().getExtras().getInt("sellingprice")+downPayment-serviceCharge+dis;
                inter.setText(getApplicationContext().getString(R.string.Rs) + interest);
                disc.setText(getApplicationContext().getString(R.string.Rs) + dis);
                TextView sc = (TextView) popUpView.findViewById(R.id.textPop);
                RelativeLayout pop = (RelativeLayout) popUpView.findViewById(R.id.pop);
                pop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (popup.isShowing()) {
                            popup.dismiss();
                            RelativeLayout cover = (RelativeLayout) findViewById(R.id.cover);
//                prod.setTi(Color.parseColor("#CC000000"));
                            cover.setVisibility(View.GONE);
                            // prod.setBackgroundColor(Color.parseColor("#ffffff"));
                        }
                    }
                });
            }
        });

    }

    public int emiWithservice(int principal, int sellingP, int months) {
        Double emi = 0.0;
        Double rate = 21.0 / 1200.0;
        int d = 0;
        if (sellingP <= 5000) {
            emi = principal * 1.0 / months;
        } else {

            d = getIntent().getExtras().getInt("daytoday");
            emi = Math.floor((principal * rate * Math.pow(1 + rate, months - 1) * (1 + rate * d * 12 / 365)) / (Math.pow(1 + rate, months) - 1));
        }

        return emi.intValue();
    }

    public int calcServ(String seller, int sellingPrice, int loanAmt) {
        int serv = 0;

        if (seller.equals("flipkart") || (seller.equals("amazon")) || seller.equals("snapdeal")) {

            if (loanAmt < 1000)
                serv = 29;
            else if (loanAmt < 5000)
                serv = 99;
            else if (loanAmt < 15000)
                serv = 149;
            else if (loanAmt < 20000)
                serv = 199;
            else if (loanAmt < 25000)
                serv = 299;
            else if (loanAmt > 25000)
                serv = 549;


        } else {
            if (sellingPrice < 1000)
                serv = 29;
            else if (sellingPrice < 5000)
                serv = 99;
            else if (sellingPrice < 10000)
                serv = 99;
            else if (sellingPrice < 15000)
                serv = 299;
            else if (sellingPrice < 25000)
                serv = 449;
            else if (sellingPrice > 25000)
                serv = 599;
        }

        return serv;
    }

    public void backpress() {
        ImageView back = (ImageView) findViewById(R.id.backo);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
    }

    public String checkMonths(int months) {
        String tot = "";
        if (months == 1)
            tot = "JAN";
        else if (months == 2)
            tot = "FEB";

        else if (months == 3)
            tot = "MAR";

        else if (months == 4)
            tot = "APR";
        else if (months == 5)
            tot = "MAY";
        else if (months == 6)
            tot = "JUN";
        else if (months == 7)
            tot = "JUL";
        else if (months == 8)
            tot = "AUG";
        else if (months == 9)
            tot = "SEP";
        else if (months == 10)
            tot = "OCT";
        else if (months == 11)
            tot = "NOV";
        else if (months == 13)
            tot = "DEC";


        return tot;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("order"))
                finish();

        }
    };

    public void finish() {
        super.finish();
    }

    @Override
    public void onBackPressed() {
        try{
            if (popup.isShowing()) {
                popup.dismiss();
                RelativeLayout cover = (RelativeLayout) findViewById(R.id.cover);
//                prod.setTi(Color.parseColor("#CC000000"));
                cover.setVisibility(View.GONE);}
            else
            {finish();}}
        catch (Exception e)
        {finish();}
//        super.onBackPressed();
    }
}
