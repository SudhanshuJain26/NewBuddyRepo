package indwin.c3.shareapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ConfirmOrder extends AppCompatActivity {
    private TextView tit,brand,sellerMrpValue,flexVal,checkOutnow,servicepr,downP,sellerpr;
    private ImageView prod,seller;
    View parent;
private    int downPayment=0,loanAmt=0,datToday=0;
    String sellerName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);


        initi();
        set();
        backpress();
    }
    public void initi()
    {seller=(ImageView)findViewById(R.id.sell);
        checkOutnow=(TextView)findViewById(R.id.checkOutnow);
        tit=(TextView)findViewById(R.id.tit);
        servicepr=(TextView)findViewById(R.id.servicepr);
        brand=(TextView)findViewById(R.id.man);
        downP=(TextView)findViewById(R.id.downV);
        sellerpr=(TextView)findViewById(R.id.sellerpr);
        sellerMrpValue=(TextView)findViewById(R.id.sellerMrpValue);
        flexVal=(TextView)findViewById(R.id.emimonths);
        prod=(ImageView)findViewById(R.id.log);

    }
    public void set()
    { Picasso.with(this)
            .load(getIntent().getExtras().getString("image"))
            .placeholder(R.drawable.emptyimageproducts)
            .into(prod);
        downPayment=(int)(Double.parseDouble(getIntent().getExtras().getString("down")));
        downP.setText(getApplicationContext().getString(R.string.Rs) + " " + downPayment);
        loanAmt=getIntent().getExtras().getInt("sellingprice")-downPayment;
        tit.setText(getIntent().getExtras().getString("title"));
        sellerpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout viewGr=(RelativeLayout)(ConfirmOrder.this).findViewById(R.id.pop);
                LayoutInflater inflater = (LayoutInflater) (ConfirmOrder.this).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                parent=inflater.inflate(R.layout.activity_confirm_order, null, false);
                View popUpView = inflater.inflate(R.layout.popup, null, false);
                final PopupWindow popup = new PopupWindow(popUpView, 400,
                        580, true);
                popup.setContentView(popUpView);
                popup.showAtLocation(popUpView, Gravity.CENTER_HORIZONTAL, 10, 10);
                ScrollView sc=(ScrollView)findViewById(R.id.scrollConfirm);
                sc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(popup.isShowing())
                            popup.dismiss();
                    }
                });
            }
        });
        if(getIntent().getExtras().getString("brand").trim().length()>0)

        brand.setText("Manufacturer: "+getIntent().getExtras().getString("brand"));
        else
        brand.setText("");
        sellerName=getIntent().getExtras().getString("seller");
if(sellerName.equals("amazon"))
{
    seller.setImageResource(R.drawable.amazon);
}
        else
if(sellerName.equals("flipkart"))
{
    seller.setImageResource(R.drawable.flipart);
}
        else
if(sellerName.equals("snapdeal"))
{
    seller.setImageResource(R.drawable.snapdeal);
}

        int serviceCharge=calcServ(sellerName,getIntent().getExtras().getInt("sellingprice"),loanAmt);
        sellerMrpValue.setText(getApplicationContext().getString(R.string.Rs)+String.valueOf(getIntent().getExtras().getInt("sellingprice")));
        servicepr.setText("+     "+getApplicationContext().getString(R.string.Rs)+" "+serviceCharge);
        int dis=getIntent().getExtras().getInt("discount");
        int newemi=emiWithservice(getIntent().getExtras().getInt("sellingprice")-dis-downPayment+serviceCharge,getIntent().getExtras().getInt("sellingprice"),(getIntent().getExtras().getInt("monthforemi")));
        String t=newemi+ " /month"+" x "+getIntent().getExtras().getString("months");
        flexVal.setText(t);
checkOutnow.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent in=new Intent(ConfirmOrder.this,Editaddress.class);
        startActivity(in);
//        finish();
    }
});


    }
    public int emiWithservice(int principal,int sellingP,int months)
    {
        Double emi = 0.0;
        Double rate = 21.0 / 1200.0;
        int d = 0;
        if (sellingP <= 5000) {
            emi = principal * 0.8 / months;
        } else {

d=getIntent().getExtras().getInt("daytoday");
            emi = Math.floor((principal * rate * Math.pow(1 + rate, months - 1) * (1 + rate * d * 12 / 365)) / (Math.pow(1 + rate, months) - 1));
        }

        return emi.intValue();
    }
    public int calcServ(String seller,int sellingPrice,int loanAmt)
    {
        int serv=0;
        if(sellingPrice<5000)
        {
            if(seller.equals("flipkart")||(seller.equals("amazon")))
                serv=99;
            else
                serv=199;
        }
        if(sellingPrice>=5000)
        {
            if(seller.equals("flipkart")||(seller.equals("amazon")))
                serv=99;
            else {
                if(sellingPrice<10000)
                    serv=299;
                else
                    if(sellingPrice<15000)
                        serv=449;
                else
                        if(sellingPrice<25000)
                            serv=549;
                else
                            serv=749;
            }


        }
        if(seller.equals("flipkart")||(seller.equals("amazon")))
        {
            if(loanAmt>10000)
                serv=199;
            if(loanAmt>15000)
                serv=299;
            if(loanAmt>20000)
                serv=399;
            if(loanAmt>25000)
                serv=549;

        }

        return serv;
    }
    public void backpress()
    {
        ImageView back=(ImageView)findViewById(R.id.backo);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
    }
}
