package indwin.c3.shareapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.LogRecord;

import android.os.Handler;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductsPage extends AppCompatActivity {
    private TextView inc, priceChange, status, creditBalance, creditLimit, cashBack, availbal, availbalmsg;
    private EditText hve, queryN;
    private int checkImg = 1, searchPrice,currDay;
    String sellerNme1 = "", productId1 = "";
    private String formstatus, name, fbid, rejectionReason, urlImg, email, uniqueCode, verificationdate, searchTitle, searchBrand, searchCategory, searchSubcategory;
    private String crcode = "", creduserid = "", truth = "", page = "";
    private Button butcheck;
    private RadioButton couCode, appcBack;
    private CircleImageView profile_image;
    private android.os.Handler rep;
    private boolean mAutoIncrement = false;
    private boolean mAutoDecrement = false;
    public int mValue = 0, mValue2 = 0, dee, cb = 0;
    private RelativeLayout overview, det, infoLayout, desLayout, specLayout, retLayout, spiii, plusR;
    private View vow, vde;
    private int checkValidUrl = 0;
    private Double emi = 0.0;
    private int checkValidFromApis = 0;
    private int monthsnow = 0;
    private int t = 100, count = 0;
    private Spinner spinner;
    private String value = "", maxValue = "";
    private int[] myMonths = {1, 2, 3, 6, 9, 12, 15, 18};
    private String selectedText = "", downPayment = "";

    private String title, brand, sellerNme, searchQuery, urlforImage;
    private int sellingPrice, monthsallowed, spInc, spDec, dayToday;
    private TextView brandName, sellingRs, pname;
    private EditText query, dValue, queryNew;
    private TextView emiAmount, titlePro, totalLoan, detInfo, detSpec, detRet, detDes;
    private ImageView seller, spinnArr, plus, productImg;
    private LinearLayout minusR;

    private SharedPreferences st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
        creduserid = cred.getString("phone_number", "");
        st = getSharedPreferences("token", Context.MODE_PRIVATE);
        if (getIntent().getExtras().getString("page").equals("monkey")) {

            wrongUrl();

        } else if (getIntent().getExtras().getString("page").equals("pay")) {
            paytmUrl();
            queryNew.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {

//                        Intent in = new Intent(HomePage.this, ViewForm.class);

                        // paste = (TextView) findViewById(R.id.pasteAg);
                        queryNew.requestFocus();
                        //clickpaste();
                        parse(queryNew.getText().toString().trim());

                    }
                    return false;
                }
            });

        } else
        //  if(getIntent().getExtras().getString("page").equals("api"))
        {
            correctUrl();
            queryNew.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {

//                        Intent in = new Intent(HomePage.this, ViewForm.class);

                        // paste = (TextView) findViewById(R.id.pasteAg);
                        queryNew.requestFocus();
                        //clickpaste();
                        parse(queryNew.getText().toString().trim());

                    }
                    return false;
                }
            });
        }

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

        if (mValue + 100 <= sellingPrice) {
            mValue += 100;
            spInc = sellingPrice - mValue;
            Double emi = calculateEmi(Double.valueOf(spInc), Double.valueOf(sellingPrice), monthsnow);
//            Toast.makeText(ProductsPage.this, String.valueOf(emi), Toast.LENGTH_SHORT).show();
            Double tot = emi * monthsnow + mValue;
            totalLoan.setText(String.valueOf(tot));

            emiAmount.setText(String.valueOf(emi));
            dValue.setText(String.valueOf(Math.floor(mValue)));
        }
    }

    public void decrement() {

        if (mValue - 100 >= sellingPrice * 0.2) {
            mValue -= 100;
            spInc = sellingPrice - mValue;

            Double emi = calculateEmi(Double.valueOf(spInc), Double.valueOf(sellingPrice), monthsnow);
//            Toast.makeText(ProductsPage.this, String.valueOf(emi), Toast.LENGTH_SHORT).show();
            Double tot = emi * monthsnow + mValue;
            totalLoan.setText(String.valueOf(tot));

            emiAmount.setText(String.valueOf(emi));
            dValue.setText(String.valueOf(Math.floor(mValue)));
        }
    }

    public void initText() {
        couCode = (RadioButton) findViewById(R.id.radioCou);
        detInfo = (TextView) findViewById(R.id.detInfo);
        detSpec = (TextView) findViewById(R.id.detSpec);
        availbal = (TextView) findViewById(R.id.availbal);
        spiii = (RelativeLayout) findViewById(R.id.downRupees);

        availbalmsg = (TextView) findViewById(R.id.availbalmsg);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        detRet = (TextView) findViewById(R.id.detRet);
        detDes = (TextView) findViewById(R.id.detDes);
        appcBack = (RadioButton) findViewById(R.id.radioCash);
        priceChange = (TextView) findViewById(R.id.priceChange);
        infoLayout = (RelativeLayout) findViewById(R.id.infoLayout);
        specLayout = (RelativeLayout) findViewById(R.id.specLayout);
        desLayout = (RelativeLayout) findViewById(R.id.desLayout);
        retLayout = (RelativeLayout) findViewById(R.id.retLayout);
        hve = (EditText) findViewById(R.id.hve);
        crcode = hve.getText().toString().trim().toUpperCase();
        spinner = (Spinner) findViewById(R.id.spinnerItem);
        emiAmount = (TextView) findViewById(R.id.calMonPayRs);
        dValue = (EditText) findViewById(R.id.dValue);
        overview = (RelativeLayout) findViewById(R.id.overView);
        det = (RelativeLayout) findViewById(R.id.details);
        vow = findViewById(R.id.vwo);
        vde = findViewById(R.id.vwd);
        plusR = (RelativeLayout) findViewById(R.id.plusR);
        minusR = (LinearLayout) findViewById(R.id.minusR);
        brandName = (TextView) findViewById(R.id.manProduct);
        sellingRs = (TextView) findViewById(R.id.sellerMrpValue);
        titlePro = (TextView) findViewById(R.id.titleProduct);
        query = (EditText) findViewById(R.id.query);
//    querylearFocus();
        plus = (ImageView) findViewById(R.id.plusImg);
        seller = (ImageView) findViewById(R.id.logo);
        spinnArr = (ImageView) findViewById(R.id.spinnArr);
        productImg = (ImageView) findViewById(R.id.productDisplay);
        totalLoan = (TextView) findViewById(R.id.calTotalPay);
        pname = (TextView) findViewById(R.id.pname);
        status = (TextView) findViewById(R.id.status);
        creditBalance = (TextView) findViewById(R.id.cbValue);
        creditLimit = (TextView) findViewById(R.id.clValue);
        cashBack = (TextView) findViewById(R.id.cbckValue);
    }

    public void setText() {
        try {

            String sta = st.getString("approvedBand", null);
            cb = st.getInt("cashBack", 0);
            int cl = st.getInt("creditLimit", 0);
            int cbv = st.getInt("totalBorrowed", 0);
            int fcbv = cl - cbv;
            Double mind = sellingPrice * .2;
            availbal.setText(getApplicationContext().getString(R.string.Rs) + fcbv);

            availbalmsg.setText("Minimum Downpayment for this product: " + getApplicationContext().getString(R.string.Rs) + mind.intValue());

            creditBalance.setText(getApplicationContext().getString(R.string.Rs) + fcbv);
            creditLimit.setText(getApplicationContext().getString(R.string.Rs) + cl);
            cashBack.setText(getApplicationContext().getString(R.string.Rs) + cb);
//            CircleImageView profile=(CircleImageView)findViewById(R.id.profile_image);
            status.setText(sta);
            pname.setText(getIntent().getExtras().getString("name"));
            SharedPreferences p = getSharedPreferences("proid", Context.MODE_PRIVATE);
            String dp = p.getString("dpid", null);
            String url = "http://graph.facebook.com/" + p.getString("dpid", null) + "/picture?type=square";
            Picasso.with(this)
                    .load("https://graph.facebook.com/" + p.getString("dpid", null) + "/picture?type=large")
                    .placeholder(R.drawable.images)
                    .into(profile_image);
        } catch (Exception e) {
            String t = e.toString();
        }
        TextView checkout = (TextView) findViewById(R.id.checkout);
        Picasso.with(this)
                .load(urlforImage)
                .placeholder(R.drawable.emptyimageproducts)
                .into(productImg);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ProductsPage.this, ConfirmOrder.class);
                in.putExtra("title", title);
                in.putExtra("brand", brand);
                in.putExtra("monthforemi", monthsnow);
                in.putExtra("daytoday", dayToday);
                in.putExtra("down", dValue.getText().toString());
                in.putExtra("sellingprice", sellingPrice);
                in.putExtra("seller", sellerNme);
                in.putExtra("image", urlforImage);
                in.putExtra("discount", 0);
                in.putExtra("emi", emiAmount.getText().toString());
                in.putExtra("months", selectedText);
                startActivity(in);
                //     finish();in.putE
            }
        });
        det.setVisibility(View.GONE);
        overview.setVisibility(View.VISIBLE);
        final ImageView t = (ImageView) findViewById(R.id.dropdes);
        t.setImageResource(R.drawable.downar);
        infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (detInfo.getVisibility() == View.VISIBLE)

                {
                    detInfo.setVisibility(View.GONE);
                    t.setRotationX(0);


                } else {

//                    ImageView t = (ImageView) findViewById(R.id.dropdes);
                    t.setRotationX(180);
                    detInfo.setVisibility(View.VISIBLE);
//                    t.set

                }

            }
        });
        //
        desLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detDes.getVisibility() == View.VISIBLE)

                {
                    ImageView t = (ImageView) findViewById(R.id.dropinfo);
                    t.setRotationX(0);
                    detDes.setVisibility(View.GONE);
                } else {

                    ImageView t = (ImageView) findViewById(R.id.dropinfo);
                    t.setRotationX(180);
                    detDes.setVisibility(View.VISIBLE);
                }

            }
        });

        //
        specLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detSpec.getVisibility() == View.VISIBLE)

                {
                    ImageView t = (ImageView) findViewById(R.id.dropspec);
                    t.setRotationX(0);
                    detSpec.setVisibility(View.GONE);
                } else {

                    ImageView t = (ImageView) findViewById(R.id.dropspec);
                    t.setRotationX(180);
                    detSpec.setVisibility(View.VISIBLE);
                }

            }
        });
        retLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detRet.getVisibility() == View.VISIBLE)

                {
                    ImageView t = (ImageView) findViewById(R.id.dropret);
                    t.setRotationX(0);
                    detRet.setVisibility(View.GONE);
                } else {

                    ImageView t = (ImageView) findViewById(R.id.dropret);
                    t.setRotationX(180);
                    detRet.setVisibility(View.VISIBLE);
                }

            }
        });


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
        hve.setFocusable(false);
        hve.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (count == 1) {
                        checkImg = 1;
                        ((ImageView) findViewById(R.id.plus)).setImageResource(R.drawable.back11);
                        ((ImageView) findViewById(R.id.plus)).setRotationY(180);
                        RelativeLayout plusRelative = (RelativeLayout) findViewById(R.id.plusRelative);
                        plusRelative.setVisibility(View.VISIBLE);
                    }
                    if (count == 0) {
                        RelativeLayout plusRelative = (RelativeLayout) findViewById(R.id.plusRelative);
                        plusRelative.setVisibility(View.GONE);
                    }
                    //write your code here
                } catch (NumberFormatException e) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        hve.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//int t=hve.getText().toString().length();
//                Toast.makeText(ProductsPage.this, "" + t, Toast.LENGTH_SHORT).show();
                hve.setFocusableInTouchMode(true);
                RelativeLayout cash = (RelativeLayout) findViewById(R.id.cashback);
                cash.setVisibility(View.VISIBLE);


                return false;
            }
        });

        couCode.setChecked(true);
        dee = 0;
        appcBack.setChecked(false);
        RelativeLayout plus = (RelativeLayout) findViewById(R.id.plusRelative);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }

        });
        couCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkD = 0;
                if (couCode.isChecked()) {


                    // TODO: 4/21/2016 //make api call to use the referral code
                    checkD = 1;
                    appcBack.setChecked(false);
                    if (!appcBack.isChecked()) {
                        sellingPrice = spDec;

                        //setEmi(sellingPrice);
                    }

//                    Toast.makeText(ProductsPage.this, "checked hai", Toast.LENGTH_SHORT).show();
                }


            }
        });
        RelativeLayout enter = ((RelativeLayout) findViewById(R.id.plusRelative));

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ProductsPage.this, "cccc", Toast.LENGTH_SHORT).show();
                crcode = hve.getText().toString().trim().toUpperCase();
                if ((crcode.length() != 0) && (couCode.isChecked())) {
                    new COUPON().execute();
                }
                ImageView pl = (ImageView) findViewById(R.id.plus);
                Drawable.ConstantState d = pl.getDrawable().getConstantState();
                Drawable.ConstantState d1 = getResources().getDrawable(R.drawable.cancel).getConstantState();
                Drawable p = pl.getDrawable();
//                Drawable r=DrR.drawable.cancel;
//                Drawable i=R.drawable.cancel;
//                if (pl.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.cancel).getConstantState())) {
                if ((checkImg == 2) || (checkImg == 3)) {
                    ((RelativeLayout) findViewById(R.id.cashback)).setVisibility(View.GONE);
                    sellingPrice = getIntent().getExtras().getInt("price");
                    setEmi(sellingPrice);
                    appcBack.setChecked(false);
                    couCode.setChecked(true);
                    hve.setCursorVisible(true);
                    hve.setText("");
                }


            }


        });

        appcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkD = 0;
                if (appcBack.isChecked()) {
                    // TODO: 4/21/2016 do something with cashback
                    checkD = 1;

                    sellingPrice = spDec;

                    dee = 1;

                    sellingPrice = sellingPrice - cb;
                    setEmi(sellingPrice);
                    couCode.setChecked(false);
                    hve.setText(getApplicationContext().getString(R.string.Rs) + cb + " Cashback applied!");
                    ((RelativeLayout) findViewById(R.id.plusRelative)).setVisibility(View.VISIBLE);
                    ((RelativeLayout) findViewById(R.id.cashback)).setVisibility(View.GONE);
                    hve.setCursorVisible(false);
                    ((ImageView) findViewById(R.id.plus)).setImageResource(R.drawable.cancel);
                    checkImg = 2;
//                    ((RelativeLayout) findViewById(R.id.cashback)).setVisibility(View.GONE);
//                    if (cb == 0)
//                        Toast.makeText(ProductsPage.this, "zero cashback", Toast.LENGTH_SHORT).show();
                }


            }
        });


        Double loan = monthsallowed * emi;
        loan += sellingPrice * .2;
        totalLoan.setText(String.valueOf(loan));
        Double downValue = sellingPrice * .2;
        mValue = downValue.intValue();
        mValue2 = downValue.intValue();
        dValue.setText(String.valueOf(Math.floor(downValue)));
        emiAmount.setText(String.valueOf(emi));
        priceChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ProductsPage.this);
                alert.setMessage("Sometimes the actual price of the " +
                        "product might vary because of the lag in placing order " +
                        "or additional delivery charges. For price changes within Rs.100," +
                        "we will go ahead and place the order with the seller of your choice." +
                        "The extra amount will be adjusted in your Flexible Payment Plan." +
                        "If the price exceeds Rs.100, we will contact you to confirm which " +
                        "seller you want to buy product from :)");
                alert.create().show();
            }
        });
        if (brand.trim().length() > 0)
            brandName.setText("Manufacturer: " + brand);
        else
            brandName.setText("");
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

                payload.put("phone", creduserid);

                payload.put("code", crcode);
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
                String url2 = getApplicationContext().getString(R.string.server) + "api/promo/coupon";
                HttpPost httppost = new HttpPost(url2);
//                HttpDelete
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
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
                        truth = resp.getString("msg");
                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return resp.getString("msg");

                    } else {
                        JSONObject data1 = new JSONObject(resp.getString("data"));
                        truth = resp.getString("msg");
                        value = data1.getString("value");
                        maxValue = data1.getString("maxValue");

                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {
            if (result.equals("win")) {
                int dis = Integer.parseInt(value);
                ((RelativeLayout) findViewById(R.id.cashback)).setVisibility(View.GONE);
                sellingPrice = sellingPrice - dis;
                setEmi(sellingPrice);
                hve.setText(truth);
//                Toast.makeText(ProductsPage.this, value, Toa/st.LENGTH_SHORT).show();
            } else {
                hve.setText(truth);
                checkImg = 3;
                ((ImageView) findViewById(R.id.plus)).setImageResource(R.drawable.retry);

            }
        }
    }

    public Double calculateEmi(Double principal, Double searchPrice, int months) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int currDay = 0;
        Date courseDate;
        Double diff = 18.0;
        try {

//            Long milli = courseDate.getTime();
            Date date = new Date();
            String curr = df.format(date);
            String currentDay = "";
            for (int j = curr.length() - 2; j < curr.length(); j++) {
                currentDay += curr.charAt(j);
            }
            currDay = Integer.parseInt(currentDay);
        } catch (Exception e) {
        }
        Double emi = 0.0;
        Double rate = 21.0 / 1200.0;
        int d = 0;
        if (searchPrice <= 5000) {
            emi = searchPrice * 0.8 / months;
        } else {
            if (currDay <= 15)
                d = 35 - currDay;
            else
                d = 65 - currDay;

            emi = Math.floor((principal * rate * Math.pow(1 + rate, months - 1) * (1 + rate * d * 12 / 365)) / (Math.pow(1 + rate, months) - 1));
        }
        dayToday = d;
        return emi;
    }

    public void setEmi(int sellingP) {
        // selectedText=parent.getItemAtPosition(position).toString();
        int i = 0;
        String t = "";
        while (selectedText.charAt(i) != ' ') {

            t = t + selectedText.charAt(i);
            i++;
        }
        Double v = Math.floor(sellingPrice * .2);
        mValue = v.intValue();
        mValue2 = v.intValue();
        monthsnow = Integer.parseInt(t);
        dValue.setText(String.valueOf(Math.floor(sellingPrice * .2)));
        emiAmount.setText(String.valueOf(calculateEmi(sellingPrice * 0.8, Double.valueOf(sellingPrice), monthsnow)));
        Double tot = calculateEmi(sellingPrice * 0.8, Double.valueOf(sellingPrice), monthsnow) * monthsnow + sellingPrice * .2;
        totalLoan.setText(String.valueOf(tot));
//        Toast.makeText(ProductsPage.this, selectedText, Toast.LENGTH_SHORT).show();

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

    public class linkSearch extends
            AsyncTask<String, Void, String> {
        @Override
        public void onPreExecute() {
//            spinner.setVisibility(View.VISIBLE);
        }


        @Override
        public String doInBackground(String... data) {

            //  String urldisplay = data[0];
//               HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: get
                String url = getApplicationContext().getString(R.string.server) + "api/product?productId=" + productId1 + "&seller=" + sellerNme1 + "&userid=" + creduserid;

//                String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjY1M2M2YTUwZTQzNzgyNjc0M2YyNjYiLCJuYW1lIjoiYnVkZHkgYXBpIGFkbWluIiwidXNlcm5hbWUiOiJidWRkeWFwaWFkbWluIiwicGFzc3dvcmQiOiJtZW1vbmdvc2gxIiwiZW1haWwiOiJjYXJlQGhlbGxvYnVkZHkuaW4iLCJpYXQiOjE0NTY3MjY1NzMsImV4cCI6MTQ1Njc2MjU3M30.98mQFcYm5Uf3Fd7ZNPD-OwMIfObu7vfoq9zNtCCLfyI";
                // payload.put("action", details.get("action"));


                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                HttpGet httppost = new HttpGet(url);
                httppost.setHeader("x-access-token", st.getString("token_value", null));
                httppost.setHeader("Content-Type", "application/json");


                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");
                if (response.getStatusLine().getStatusCode() != 200) {
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);
                    if (resp.getString("status").equals("success")) {
                        JSONObject data1 = new JSONObject(resp.getString("data"));
                        searchTitle = data1.getString("title");
                        searchBrand = data1.getString("brand");
                        searchCategory = data1.getString("category");
                        searchSubcategory = data1.getString("subCategory");
                        searchPrice = data1.getInt("sellingPrice");
                        JSONObject img = new JSONObject(data1.getString("imgUrls"));
                        urlImg = img.getString("400x400");

                        return "win";


                    }

                }
            } catch (Exception e) {
            }
            return "";
        }

        protected void onPostExecute(String result) {
            if (!result.equals("win")) {
                System.out.println("Error while computing data");
            } else {

                monthsallowed = months(searchSubcategory, searchCategory, searchBrand, searchPrice);
                int monthscheck = 0;
                //digo
                String course = st.getString("course", "");

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
                            monthscheck = months;
                        }


//                        Toast.makeText(HomePage.this, curr, Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (monthsallowed > monthscheck)
                    monthsallowed = monthscheck;

//                Double emi = 0.0;
                Double rate = 21.0 / 1200.0;
                int d = 0;
                if (searchPrice <= 5000) {
                    emi = searchPrice * 0.8 / monthsallowed;
                } else {
                    if (currDay <= 15)
                        d = 35 - currDay;
                    else
                        d = 65 - currDay;

                    emi = Math.floor((searchPrice * 0.8 * rate * Math.pow(1 + rate, monthsallowed - 1) * (1 + rate * d * 12 / 365)) / (Math.pow(1 + rate, monthsallowed) - 1));
                    Intent in = new Intent(ProductsPage.this, ProductsPage.class);
                    in.putExtra("title", searchTitle);
                    in.putExtra("price", searchPrice);
                    in.putExtra("brand", searchBrand);
                    in.putExtra("name", name);
                    in.putExtra("image", urlImg);
                    in.putExtra("emi", emi);
                    in.putExtra("monthsallowed", monthsallowed);
                    in.putExtra("seller", sellerNme1);
                    in.putExtra("query", queryNew.getText().toString());
                    in.putExtra("page", page);

                    startActivity(in);
                }
            }
        }}

        public void parse(String parseString) {
            productId1 = "";
            int pos = -1;
            if (parseString.contains("flipkart")) {
                sellerNme1 = "flipkart";
                pos = parseString.indexOf("pid");
                if (pos != -1) {
                    for (int j = pos + 4; ; j++) {
                        if (parseString.charAt(j) == '&')
                            break;
                        else {
                            productId1 += parseString.charAt(j);
                        }

                    }
                } else {
                    checkValidUrl = 1;
                }
                //       Toast.makeText(HomePage.this, "DADA" + String.valueOf(pos), Toast.LENGTH_SHORT).show();
            }
//snapdeal

            else if (parseString.contains("snapdeal")) {
                sellerNme1 = "snapdeal";
                pos = parseString.lastIndexOf("/");
                if (pos != -1) {
                    for (int j = pos + 1; ; j++) {
                        if (parseString.charAt(j) == '#')
                            break;
                        else {
                            productId1 += parseString.charAt(j);
                        }

                    }
                } else {
                    checkValidUrl = 1;
                }
                //     Toast.makeText(HomePage.this, "DADA" + String.valueOf(pos), Toast.LENGTH_SHORT).show();
            } else if (parseString.contains("myntra")) {
                sellerNme1 = "myntra";
                checkValidFromApis = 1;
            } else if (parseString.contains("shopclues")) {
                sellerNme1 = "shopclues";
                checkValidFromApis = 1;
            } else if (parseString.contains("jabong")) {
                sellerNme1 = "jabong";
                checkValidFromApis = 1;
            } else if (parseString.contains("paytm")) {
                sellerNme1 = "paytm";
                checkValidFromApis = 1;
            }
//amazon
            else if (parseString.contains("amazon")) {
                sellerNme1 = "amazon";
                int w = 0;
                pos = parseString.indexOf("/dp/");
                if (pos != -1) {
                    pos = parseString.indexOf("dp");
                }
                if (pos == -1) {
                    pos = parseString.indexOf("/product/");
                    if (pos != -1)
                        w = 1;
                }
                if (pos == -1) {

                    pos = parseString.indexOf("/d/");
                    if (pos != -1)
                        w = 2;
                }


                if (pos != -1) {
                    int r = 0;
                    if (w == 0)
                        r = pos + 3;
                    if (w == 1)
                        r = pos + 9;
                    if (w == 2)
                        r = pos + 3;
                    for (int j = r; ; j++) {
                        if ((parseString.charAt(j) == '/') || ((parseString.charAt(j) == '?')))
                            break;
                        else {
                            productId1 += parseString.charAt(j);
                        }

                    }
                } else {
                    checkValidUrl = 1;
                }

            } else if (parseString.contains("shopclues"))
                checkValidFromApis = 1;
            else
                checkValidUrl = 1;

            if ((checkValidFromApis == 0) && (checkValidUrl == 0)) {
                page = "api";
                new linkSearch().execute();


                //make api call
            }
            if ((checkValidFromApis == 1)) {
                //not monley page
                Intent in=new Intent(ProductsPage.this,ProductsPage.class);
                query.setText("");
                in.putExtra("seller",sellerNme1);
                in.putExtra("page","pay");
                startActivity(in);
                finish();
                page = "pay";
                paytmUrl();
            }
            if (checkValidUrl == 1) {
                //monkey page
                Intent in=new Intent(ProductsPage.this,ProductsPage.class);
                query.setText("");

                in.putExtra("page","monkey");
                startActivity(in);
                finish();
                page = "monkey";
                wrongUrl();
            }
//        Toast.makeText(HomePage.this, productId, Toast.LENGTH_SHORT).show();


        }

        public void wrongUrl() {

            setContentView(R.layout.wrongurl);
            queryNew = (EditText) findViewById(R.id.query);
            queryNew.setImeOptions(EditorInfo.IME_ACTION_DONE);
            clickUrl();
            backpress();
        }

        public void paytmUrl() {
            setContentView(R.layout.paytmjab);

            queryNew = (EditText) findViewById(R.id.query);
            queryNew.setImeOptions(EditorInfo.IME_ACTION_DONE);
            clickUrl();
            EditText editQ = (EditText) findViewById(R.id.editQ);
            TextView aval = (TextView) findViewById(R.id.avlbalRs);
            TextView enter = (TextView) findViewById(R.id.enter);
            ImageView img = (ImageView) findViewById(R.id.logo);
            String sta = st.getString("approvedBand", null);
            cb = st.getInt("cashBack", 0);
            int cl = st.getInt("creditLimit", 0);
            int cbv = st.getInt("totalBorrowed", 0);
            int fcbv = cl - cbv;
            aval.setText(getApplicationContext().getString(R.string.Rs) + fcbv);
            backpress();
        }

        public void correctUrl() {
            setContentView(R.layout.activity_products_page);
            queryNew = (EditText) findViewById(R.id.query);
            queryNew.setImeOptions(EditorInfo.IME_ACTION_DONE);
            backpress();
            clickUrl();
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
                urlforImage = getIntent().getExtras().getString("image");

                spInc = sellingPrice;
                spDec = sellingPrice;
            } catch (Exception e) {
            }
            initText();
            //emiAmount=(TextView)findViewById(R.id.calMonPayRs);
            setText();

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    selectedText = parent.getItemAtPosition(position).toString();
                    int i = 0;
                    String t = "";
                    while (selectedText.charAt(i) != ' ') {

                        t = t + selectedText.charAt(i);
                        i++;
                    }
                    Double v = Math.floor(sellingPrice * .2);
                    mValue = v.intValue();
                    mValue2 = v.intValue();
                    monthsnow = Integer.parseInt(t);
                    dValue.setText(String.valueOf(Math.floor(sellingPrice * .2)));
                    emiAmount.setText(String.valueOf(calculateEmi(sellingPrice * 0.8, Double.valueOf(sellingPrice), monthsnow)));
                    Double tot = calculateEmi(sellingPrice * 0.8, Double.valueOf(sellingPrice), monthsnow) * monthsnow + sellingPrice * .2;
                    totalLoan.setText(String.valueOf(tot));
//                    Toast.makeText(ProductsPage.this, selectedText, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            // Spinner Drop down elements
            int arrmont[] = new int[8];
            int p = 0;
            List<String> categories = new ArrayList<String>();
            for (int j = 0; j < 8; j++) {
                if (myMonths[j] <= monthsallowed) {

                    p = j;
                    //categories.add(String.valueOf(myMonths[j]) + " months");
                }
            }
            for (int w = p; w >= 0; w--) {
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
                            spiii.setOnClickListener(new View.OnClickListener() {
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

        public void clickUrl() {
            queryNew.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {

//                        Intent in = new Intent(HomePage.this, ViewForm.class);

                        // paste = (TextView) findViewById(R.id.pasteAg);
                        queryNew.requestFocus();
                        //clickpaste();
                        parse(queryNew.getText().toString().trim());

                    }
                    return false;
                }
            });
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

            return m;
        }

}