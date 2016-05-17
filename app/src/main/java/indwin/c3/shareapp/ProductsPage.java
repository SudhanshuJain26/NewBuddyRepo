package indwin.c3.shareapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.LogRecord;

import android.os.Handler;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ProductsPage extends AppCompatActivity {
    private TextView inc, priceChange;
    private Button butcheck;
    private RadioButton couCode,appcBack;
    private android.os.Handler rep;
    private boolean mAutoIncrement = false;
    private boolean mAutoDecrement = false;
    public int mValue = 0,mValue2=0;
    private RelativeLayout overview,det;
    private View vow,vde;


private int monthsnow=0;
    private int t = 100, count = 0;
    private Spinner spinner;
    private String value="",maxValue="";
    private int[] myMonths = {1, 2, 3, 6, 9, 12, 15, 18};
private String selectedText="",downPayment="";
    private String title, brand, sellerNme, searchQuery;
    private int sellingPrice, monthsallowed,spInc,spDec;
    private TextView brandName, sellingRs,dValue;
    private EditText query;
    private TextView emiAmount, titlePro, totalLoan;
    private ImageView seller, spinnArr,plus;
    private LinearLayout plusR,minusR;
    private Double emi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_page);
        inc = (TextView) findViewById(R.id.check);
        butcheck = (Button) findViewById(R.id.butcheck);
        try {
            monthsallowed = getIntent().getExtras().getInt("monthsallowed");
            title = getIntent().getExtras().getString("title");
            brand = getIntent().getExtras().getString("brand");
            emi = getIntent().getExtras().getDouble("emi");
            searchQuery = getIntent().getExtras().getString("query");
            sellerNme = getIntent().getExtras().getString("seller");
            sellingPrice = getIntent().getExtras().getInt("price");
            spInc=sellingPrice;
            spDec=sellingPrice;
        } catch (Exception e) {
        }
        initText();
        //emiAmount=(TextView)findViewById(R.id.calMonPayRs);
        setText();
        new COUPON().execute();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

selectedText=parent.getItemAtPosition(position).toString();
                int i=0;
                String t="";
                while(selectedText.charAt(i)!=' ')
                {

                    t=t+selectedText.charAt(i);
                    i++;
                }
Double v=Math.floor(sellingPrice * .2);
                mValue=v.intValue();
                mValue2=v.intValue();
                monthsnow=Integer.parseInt(t);
                dValue.setText(String.valueOf(Math.floor(sellingPrice * .2)));
                emiAmount.setText(String.valueOf(calculateEmi(sellingPrice * 0.8, Double.valueOf(sellingPrice), monthsnow)));
                Double tot=calculateEmi(sellingPrice *0.8, Double.valueOf(sellingPrice), monthsnow)*monthsnow+sellingPrice*.2;
                totalLoan.setText(String.valueOf(tot));
                Toast.makeText(ProductsPage.this, selectedText, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements
        int arrmont[]=new int[8];
        int p=0;
        List<String> categories = new ArrayList<String>();
        for (int j = 0; j < 8; j++) {
            if (myMonths[j] <= monthsallowed) {

                p=j;
                //categories.add(String.valueOf(myMonths[j]) + " months");
            }
        }
        for(int w=p;w>=0;w--)
        {
            categories.add(String.valueOf(myMonths[w]) + " months");
        }


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        rep = new Handler();
        new Thread(new Runnable() {
            public void run() {
                // DO NOT ATTEMPT TO DIRECTLY UPDATE THE UI HERE, IT WON'T WORK!
                // YOU MUST POST THE WORK TO THE UI THREAD'S HANDLER
                rep.postDelayed(new Runnable() {
                    public void run() {
                        // Open the Spinner...
                        //s.performClick();
                        spinnArr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                spinner.performClick();
                            }
                        });

                    }
                }, 5000);
            }
        }).start();
//        rep=new Handler() {
//            @Override
//            public void close() {
//
//            }
//
//            @Override
//            public void flush() {
//
//            }
//
//            @Override
//            public void publish(LogRecord record) {
//
//            }
//        };


//        butcheck.set
        minusR.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View arg0) {
                        mAutoDecrement = true;
                        rep.post(new RptUpdater());
                        return false;
                    }
                }
        );
        minusR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrement();

            }
        });
        minusR.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoDecrement) {
                    mAutoDecrement = false;
                }
                return false;
            }
        });
//        spinner.performClick();

        plusR.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View arg0) {
                        mAutoIncrement = true;
                        rep.post(new RptUpdater());
                        return false;
                    }
                }
        );
        plusR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increment();

            }
        });
        plusR.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoIncrement) {
                    mAutoIncrement = false;
                }
                return false;
            }
        });

    }

    class RptUpdater implements Runnable {
        public void run() {
            if (mAutoIncrement) {
                increment();

                t = t - count * 5;
                count++;
                rep.postDelayed(new RptUpdater(), t);
            }

            if (mAutoDecrement) {
                decrement();

                t = t - count * 5;
                count++;
                rep.postDelayed(new RptUpdater(), t);
            }
        }
    }

    public void increment() {

        if(mValue+100<=sellingPrice)
        {   mValue+=100;
            spInc=sellingPrice-mValue;
            Double emi=calculateEmi(Double.valueOf(spInc),Double.valueOf(sellingPrice),monthsnow);
            Toast.makeText(ProductsPage.this, String.valueOf(emi), Toast.LENGTH_SHORT).show();
            Double tot=emi*monthsnow+mValue;
            totalLoan.setText(String.valueOf(tot));

            emiAmount.setText(String.valueOf(emi));
        dValue.setText(String.valueOf(Math.floor(mValue)));
        }
    }
    public void decrement() {

        if(mValue-100>=sellingPrice*0.2)
        {
            mValue-=100;
            spInc=sellingPrice-mValue;

            Double emi=calculateEmi(Double.valueOf(spInc),Double.valueOf(sellingPrice),monthsnow);
            Toast.makeText(ProductsPage.this, String.valueOf(emi), Toast.LENGTH_SHORT).show();
            Double tot=emi*monthsnow+mValue;
            totalLoan.setText(String.valueOf(tot));

            emiAmount.setText(String.valueOf(emi));
            dValue.setText(String.valueOf(Math.floor(mValue)));
        }
    }

    public void initText() {
        couCode=(RadioButton)findViewById(R.id.radioCou);
        appcBack=(RadioButton)findViewById(R.id.radioCash);
        priceChange = (TextView) findViewById(R.id.priceChange);
        spinner = (Spinner) findViewById(R.id.spinnerItem);
        emiAmount = (TextView) findViewById(R.id.calMonPayRs);
        dValue=(TextView)findViewById(R.id.dValue);
        overview=(RelativeLayout)findViewById(R.id.overView);
        det=(RelativeLayout)findViewById(R.id.details);
        vow=findViewById(R.id.vwo);
        vde=findViewById(R.id.vwd);
        plusR=(LinearLayout)findViewById(R.id.plusR);
        minusR=(LinearLayout)findViewById(R.id.minusR);
        brandName = (TextView) findViewById(R.id.manProduct);
        sellingRs = (TextView) findViewById(R.id.sellerMrpValue);
        titlePro = (TextView) findViewById(R.id.titleProduct);
        query = (EditText) findViewById(R.id.query);
//    query.clearFocus();
        plus = (ImageView) findViewById(R.id.plusImg);
        seller = (ImageView) findViewById(R.id.logo);
        spinnArr = (ImageView) findViewById(R.id.spinnArr);
        totalLoan = (TextView) findViewById(R.id.calTotalPay);
    }

    public void setText() {
        det.setVisibility(View.GONE);
        overview.setVisibility(View.VISIBLE);
        vde.setVisibility(View.GONE);
        vow.setVisibility(View.VISIBLE);
        overview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overview.setVisibility(View.GONE);
                det.setVisibility(View.VISIBLE);
                vow.setVisibility(View.GONE);
                vde.setVisibility(View.VISIBLE);
            }
        });
        det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                det.setVisibility(View.GONE);
               overview.setVisibility(View.VISIBLE);
                vde.setVisibility(View.GONE);
                vow.setVisibility(View.VISIBLE);
            }
        });
        couCode.setChecked(false);
        appcBack.setChecked(false);
        couCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkD = 0;
                if (couCode.isChecked()) {

                    // TODO: 4/21/2016 //make api call to use the referral code
                    checkD = 1;
                    appcBack.setChecked(false);
                    Toast.makeText(ProductsPage.this, "checked hai", Toast.LENGTH_SHORT).show();
                }


            }
        });
        appcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkD=0;
                if(appcBack.isChecked()){
                    // TODO: 4/21/2016 do something with cashback
                    checkD=1;
                    couCode.setChecked(false);
                    Toast.makeText(ProductsPage.this, "checked hai", Toast.LENGTH_SHORT).show();}


            }
        });
        Double loan = monthsallowed * emi;
        loan += sellingPrice * .2;
        totalLoan.setText(String.valueOf(loan));
        Double downValue=sellingPrice * .2;
        mValue=downValue.intValue();
        mValue2=downValue.intValue();
        dValue.setText(String.valueOf(Math.floor(downValue)));
        emiAmount.setText(String.valueOf(emi));
        priceChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(ProductsPage.this, priceChange);
                pop.getMenuInflater().inflate(R.menu.drawer, pop.getMenu());
                pop.show();
            }
        });
        brandName.setText(brand);
        if (sellerNme.equals("amazon"))
            seller.setImageResource(R.drawable.amazon);
        else if (sellerNme.equals("flipkart"))
            seller.setImageResource(R.drawable.flipart);
        else if (sellerNme.equals("snapdeal"))

            seller.setImageResource(R.drawable.snapdeal);
        sellingRs.setText(String.valueOf(sellingPrice));
        titlePro.setText(title);
        query.setText(searchQuery);
    }
    private class COUPON extends
            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... data) {

            // String urldisplay = data[0];
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST
                SharedPreferences red = getSharedPreferences("Referral", Context.MODE_PRIVATE);
//                String referralinst=red.getString("referrer","");
                payload.put("phone","7070362045");
                payload.put("code", "STUD2");
//                // payload.put("college", mCollege);
//                if(mRef.trim().length()>0)
//                    payload.put("refCode",mRef);
//                if((mRef.trim().length()==0)&&(referralinst.trim().length()!=0)) {
                    //
                    //
                    //    Toast.makeText(Inviteform.this,referralinst,Toast.LENGTH_LONG).show();
//                    payload.put("refCode", referralinst.trim());
//                }
//                payload.put("phone", mPhone);
//                payload.put("offlineForm",false);
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                //api/login/sendotp
                String url2 = getApplicationContext().getString(R.string.server)+"api/promo/coupon";
                HttpPost httppost = new HttpPost(url2);
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp=toks.getString("token_value","");
                httppost.setHeader("x-access-token", tok_sp);
                httppost.setHeader("Content-Type", "application/json");


                StringEntity entity = new StringEntity(payload.toString());

                httppost.setEntity(entity);
                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");
                if (response.getStatusLine().getStatusCode() != 200) {

                    Log.e("MeshCommunication", "Server returned code "
                            + response.getStatusLine().getStatusCode());
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);

                    if (resp.getString("status").contains("error")) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return resp.getString("msg");
                    } else {
                        JSONObject data1 = new JSONObject(resp.getString("data"));
                        value=data1.getString("value");
                        maxValue=data1.getString("maxValue");
                       return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {
            if(result.equals("win"))
            {
                Toast.makeText(ProductsPage.this, value, Toast.LENGTH_SHORT).show();
            }
        }}
    public Double  calculateEmi(Double principal,Double searchPrice,int months)
    { DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
int currDay=0;
        Date courseDate;
        Double diff=18.0;
        try {

//            Long milli = courseDate.getTime();
            Date date = new Date();
            String curr = df.format(date);
            String currentDay = "";
            for (int j = curr.length() - 2; j < curr.length(); j++) {
                currentDay += curr.charAt(j);
            }
            currDay=Integer.parseInt(currentDay);
        }catch(Exception e)
        {}
        Double emi=0.0;
        Double rate=21.0/1200.0;
        int d=0;
        if(searchPrice<=5000)
        {
            emi=searchPrice*0.8/months;
        }
        else
        {
            if(currDay<=15)
                d=35-currDay;
            else
                d=65-currDay;

            emi=Math.floor((principal*rate*Math.pow(1+rate,months-1)*(1 + rate * d * 12/365))/(Math.pow(1+rate,months)-1));}
return emi;
    }

        }
