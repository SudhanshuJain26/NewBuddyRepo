package indwin.c3.shareapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import indwin.c3.shareapp.activities.FindProduct;
import indwin.c3.shareapp.activities.ProfileActivity;
import indwin.c3.shareapp.utils.AppUtils;
import indwin.c3.shareapp.utils.Constants;
import io.intercom.android.sdk.Intercom;

public class ProductsPage extends AppCompatActivity {
    private long checkDiffernece = 0;
    private TextView inc, priceChange, status, creditBalance, creditLimit, cashBack, availbal, availbalmsg, knowmore;
    private EditText hve, queryN;
    private long EMIcheck = 0;
    private int checkLongpress = 0,noOfDays,interestRate=21;
    private List<Integer> pricesFormonths;
    private GIFView loader;
    private int checkImg = 1, searchPrice, currDay, minDownpayment, firstServicecharge = 0, secondServicecharge = 0;
    private ScrollView viewDetail;
    private String userProfileStatus = "";
    private Map<String, Integer> categoryForproducts,subcategoryForproducts,brandApple,priceMonths;
    private Map<Integer,Integer>serviceChargesmapping;
    private android.content.ClipboardManager myClipboard;
    String sellerNme1 = "", productId1 = "";
    private String s = "";
    private String whichCoupon = "";
    private TextView checkout;
    private int checkCorrectdis = 1, dopay2 = 0, dummyCl = 0, globalMindown = 0;
    private String formstatus, name, fbid, rejectionReason, urlImg, email, uniqueCode, verificationdate, searchTitle, searchBrand, searchCategory, searchSubcategory, description, specification, review, infor;
    private String crcode = "", creduserid = "", truth = "", page = "";
    private Button butcheck;
    private KeyListener listen;
    private PopupWindow popup;
    private RadioButton couCode, appcBack;
    private CircleImageView profile_image;
    private android.os.Handler rep;
    private boolean mAutoIncrement = false;
    private  Boolean paymyntra=false;
    private boolean mAutoDecrement = false;
    public int mValue = 0, mValue2 = 0, dee, cb = 0, mDis = 0, checkD = 0;
    private RelativeLayout overview, det, infoLayout, desLayout, specLayout, retLayout, spiii, plusR;
    private View vow, vde;
    private int checkValidUrl = 0;
    private Double emi = 0.0;
    private int checkValidFromApis = 0;
    private int monthsnow = 0;
    private int t = 100, count = 0,considerTen=0;
    private Spinner spinner;
    private int checkLenghtedittext = 0;
    private int value = 0;
    private int maxValue = 0, minProd = 0;
    private Boolean isZeroDpApplicable=false;
    private String type = "", checkmonths = "";

    private int[] myMonths = {1, 2, 3, 6, 9, 12, 15, 18};
    private String selectedText = "", downPayment = "",sercieChargeApplicableon="sellingprice";
    private ImageView pasteiconnew;
    private String title, brand, sellerNme, searchQuery, urlforImage;
    private int sellingPrice, monthsallowed, spInc, spDec, dayToday, cuurr;
    private TextView brandName, sellingRs, pname, sellingRschange,flipcash;
    private EditText query, queryNew;
    private String userCode1k = "", userCode7k = "";
    private CustomEditText dValue;
    private int minDownpaymentfornofina = 0;
    private TextView emiAmount, titlePro, totalLoan, detInfo, detSpec, detRet, detDes;
    private ImageView seller, spinnArr, plus, productImg;
    private RelativeLayout minusR;
    private String noInterestAppon="";
    private int nointerestSp,nointerestEmiten=0;
    private int checkCashback = 0;
    private SharedPreferences st;

    //BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(broadcastReceiver, new IntentFilter("order"));
        SharedPreferences cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
        creduserid = cred.getString("phone_number", "");
        st = getSharedPreferences("token", Context.MODE_PRIVATE);
        new DeriveMapping().execute();



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

    //public void editdp(){}
    public void editdp() {
        // Toast.makeText(ProductsPage.this, "checkdp", Toast.LENGTH_SHORT).show();
        //        String s = dValue.getText().toString();
        try {
            int dp;
            if (("").equals(s))
                s = dValue.getText().toString();
            //            if(s.equals("")||(s==null))
            if (AppUtils.isEmpty(s))
                dp = 0;
            else
                dp = Integer.parseInt(s);
            Double m = sellingPrice * .2;

            if ((dp >= minDownpayment) && (dp <= sellingPrice + secondServicecharge-considerTen*200))//&& w>=mindownn
            {
                // TODO: 5/14/2016
                mValue = dp;
                cb = st.getInt("cashBack", 0);

                int cl = st.getInt("creditLimit", 0);
                int cbv = st.getInt("totalBorrowed", 0);
                int fcbv = cl - cbv;
                if (fcbv <= 0) {
                    dummyCl = 1000;
                    fcbv = 100000000;
                }

                //                if(sellingPrice-mValue>fcbv)
                //                    mValue=sellingPrice-fcbv;

            } else {
                Double downValue = sellingPrice * .2;
                //                if((sellingPrice<=1000)&&(sellingPrice>150))
                //                    downValue=0.0;
                mValue = downValue.intValue();

                cb = st.getInt("cashBack", 0);
                int cl = st.getInt("creditLimit", 0);
                int cbv = st.getInt("totalBorrowed", 0);
                int fcbv = cl - cbv;
                if (fcbv <= 0) {
                    dummyCl = 1000;
                    fcbv = 100000000;
                }

                if (sellingPrice - mValue > fcbv)
                    mValue = sellingPrice - fcbv;
                //
                mValue = minDownpayment;
                //                if(checkmonths.equals("0"))
                //                    mValue=minDownpaymentfornofina

                //     dValue.setText(String.valueOf(mValue));
            }
            s = "";
        } catch (Exception e) {
            Double downValue = sellingPrice * .2;
            //            if((sellingPrice<=1000)&&(sellingPrice>150))
            //                downValue=0.0;
            mValue = downValue.intValue();
            dValue.setText("");
            dValue.setText(String.valueOf(mValue));
            s = "";
            if (sellingPrice != searchPrice) {
                sellingRschange.setVisibility(View.VISIBLE);
                sellingRschange.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(searchPrice)));
                sellingRschange.setPaintFlags(sellingRschange.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                sellingRs.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(sellingPrice)));
            } else {
                sellingRschange.setVisibility(View.GONE);
                sellingRs.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(sellingPrice)));
            }
            //            sellingRs.setText(getApplicationContext().getString(R.string.Rs)+String.valueOf(Math.round(sellingPrice)));
        }
        dValue.setText("");
        dValue.setText(String.valueOf(mValue));
        int w = serviceCharge(searchPrice, sellingPrice - mValue, sellerNme1);
        firstServicecharge = w;
        secondServicecharge = serviceCharge(searchPrice, sellingPrice - mValue, sellerNme1);

        Double emi = calculateEmi(Double.valueOf(sellingPrice - mValue + secondServicecharge), Double.valueOf(sellingPrice), monthsnow);
        //            Toast.makeText(ProductsPage.this, String.valueOf(emi), Toast.LENGTH_SHORT).show();
        Double tot = emi * monthsnow + mValue;
        totalLoan.setText(String.valueOf(Math.round(tot)));
        //        Toast.makeText(ProductsPage.this, "lets do it", Toast.LENGTH_SHORT).show();
        EMIcheck = Math.round(emi);
        emiAmount.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(emi)) + " per month");
        //calculate emi and set emi call

    }

    public void increment() {
        mValue = Integer.parseInt(dValue.getText().toString());
        if (monthsnow != 0) {
            int inccc = 0;
            if (checkLongpress == 1)
                inccc = 10;
            else
                inccc = 1;

            if (mValue + inccc <= sellingPrice + secondServicecharge) {
                if(mValue + inccc <= sellingPrice + secondServicecharge-considerTen*200)
                {
                    
                }
                mValue += inccc;
                spInc = sellingPrice - mValue;
                int w = serviceCharge(searchPrice, sellingPrice - mValue, sellerNme1);
                firstServicecharge = w;
                secondServicecharge = serviceCharge(searchPrice, sellingPrice - mValue, sellerNme1);
                Double emi = calculateEmi(Double.valueOf(sellingPrice - mValue + secondServicecharge), Double.valueOf(sellingPrice), monthsnow);
                Double tot = emi * monthsnow + mValue;
                totalLoan.setText(String.valueOf(Math.round(tot)));
                EMIcheck = Math.round(emi);
                emiAmount.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(emi)) + " per month");
                dValue.setText(String.valueOf(Math.round(mValue)));
            }
        }
    }

    public void decrement() {

        if (monthsnow != 0) {
            mValue = Integer.parseInt(dValue.getText().toString());
            int inccc = 0;
            if (checkLongpress == 1)
                inccc = 10;
            else
                inccc = 1;
            if (mValue - inccc >= minDownpayment) {
                mValue -= inccc;
                spInc = sellingPrice - mValue;
                int w = serviceCharge(searchPrice, sellingPrice - mValue, sellerNme1);
                firstServicecharge = w;
                secondServicecharge = serviceCharge(searchPrice, sellingPrice - mValue, sellerNme1);
                Double emi = calculateEmi(Double.valueOf(sellingPrice - mValue + secondServicecharge), Double.valueOf(sellingPrice), monthsnow);

                //            Toast.makeText(ProductsPage.this, String.valueOf(emi), Toast.LENGTH_SHORT).show();
                Double tot = emi * monthsnow + mValue;
                totalLoan.setText(String.valueOf(Math.round(tot)));
                EMIcheck = Math.round(emi);
                emiAmount.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(emi)) + " per month");
                dValue.setText(String.valueOf(Math.round(mValue)));
            }
        }
    }

    public void initText() {
        couCode = (RadioButton) findViewById(R.id.radioCou);
        detInfo = (TextView) findViewById(R.id.detInfo);
        knowmore = (TextView) findViewById(R.id.knowmore);
        pasteiconnew = (ImageView) findViewById(R.id.pasteAg);

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
        try {
            listen = hve.getKeyListener();
        } catch (Exception e) {
        }
        crcode = hve.getText().toString().trim().toUpperCase();
        spinner = (Spinner) findViewById(R.id.spinnerItem);
        emiAmount = (TextView) findViewById(R.id.calMonPayRs);
        dValue = (CustomEditText) findViewById(R.id.dValue);
        dValue.setOnBackButtonListener(new CustomEditText.IOnBackButtonListener() {
            @Override
            public boolean OnEditTextBackButton() {
                editdp();
                return false;
            }
        });
        dValue.setImeOptions(EditorInfo.IME_ACTION_DONE);
        dValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dValue.requestFocusFromTouch();
            }
        });
        dValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    dValue.clearFocus();
                    if (monthsnow != 0)
                        editdp();
                    else
                        dValue.setText(String.valueOf(mValue));
                    // Toast.makeText(ProductsPage.this, "c", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(ProductsPage.this, "checkddd", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        overview = (RelativeLayout) findViewById(R.id.overView);
        det = (RelativeLayout) findViewById(R.id.details);
        vow = findViewById(R.id.vwo);
        vde = findViewById(R.id.vwd);
        plusR = (RelativeLayout) findViewById(R.id.plusR);
        minusR = (RelativeLayout) findViewById(R.id.minusR);
        brandName = (TextView) findViewById(R.id.manProduct);
        sellingRs = (TextView) findViewById(R.id.sellerMrpValue);
        sellingRschange = (TextView) findViewById(R.id.sellerMrpValueold);
        sellingRschange.setPaintFlags(sellingRschange.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        sellingRschange.setVisibility(View.GONE);
        titlePro = (TextView) findViewById(R.id.titleProduct);
        query = (EditText) findViewById(R.id.query);
        //    querylearFocus();
        plus = (ImageView) findViewById(R.id.plusImg);
        seller = (ImageView) findViewById(R.id.logo);
        spinnArr = (ImageView) findViewById(R.id.spinnArr);
        productImg = (ImageView) findViewById(R.id.productDisplay);
        if (getIntent().getExtras().getString("page").equals("pay"))
            productImg.setVisibility(View.GONE);
        else
            productImg.setVisibility(View.VISIBLE);


        totalLoan = (TextView) findViewById(R.id.calTotalPay);
        pname = (TextView) findViewById(R.id.pname);
        status = (TextView) findViewById(R.id.status);
        creditBalance = (TextView) findViewById(R.id.cbValue);
        creditLimit = (TextView) findViewById(R.id.clValue);
        cashBack = (TextView) findViewById(R.id.cbckValue);
    }

    public void setText() {
        try {
            String set = "<font color=#664A4A4A>Read more details about the product,seller and shipping on </font> <font color=#33A4D0>Product Page</font>";
            knowmore.setText(Html.fromHtml(set));
            String sta = st.getString("approvedBand", null);
            cb = st.getInt("cashBack", 0);
            int cl = st.getInt("creditLimit", 0);
            int cbv = st.getInt("totalBorrowed", 0);
            int fcbv = cl - cbv;
            if (fcbv <= 0) {
                dummyCl = 1000;
                fcbv = 100000000;
            }

            Double mind = 0.0;
            if (searchPrice >= 1000)
                mind = sellingPrice * .2;
            else if ((searchPrice <= 1000) && (searchPrice > 150))
                mind = sellingPrice * .2;

            if (sellingPrice - mind > fcbv) {
                mind = Double.valueOf(sellingPrice) - fcbv;
            }
            if (dummyCl == 1000)
                availbal.setText(getApplicationContext().getString(R.string.Rs) + "0");
            else
                availbal.setText(getApplicationContext().getString(R.string.Rs) + fcbv);
            int w = serviceCharge(searchPrice, searchPrice - mind.intValue(), sellerNme1);
            firstServicecharge = w;
            secondServicecharge = serviceCharge(searchPrice, sellingPrice - (mind.intValue() + w), sellerNme1);


            minDownpayment = (mind.intValue() + w);
            if(isZeroDpApplicable&&fcbv>=sellingPrice&&searchPrice<=1000)
            {mValue=0;
                minDownpayment=0;}
            availbalmsg.setText("Minimum Downpayment for this product: " + getApplicationContext().getString(R.string.Rs) + (minDownpayment));
            globalMindown = minDownpayment;
            if (dummyCl == 1000)
                creditBalance.setText(getApplicationContext().getString(R.string.Rs) + "0");
            else
                creditBalance.setText(getApplicationContext().getString(R.string.Rs) + fcbv);
            creditLimit.setText(getApplicationContext().getString(R.string.Rs) + cl);
            cashBack.setText(getApplicationContext().getString(R.string.Rs) + cb);
            //            CircleImageView profile=(CircleImageView)findViewById(R.id.profile_image);
            status.setText(sta);
            pname.setText(st.getString("productdpname", ""));
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
        checkout = (TextView) findViewById(R.id.checkout);
        try {
            Picasso.with(this)
                    .load(urlforImage)
                    .placeholder(R.drawable.emptyimageproducts)
                    .into(productImg);
        } catch (Exception e) {
        }
        SharedPreferences user = getSharedPreferences("token", Context.MODE_PRIVATE);
        userProfileStatus = user.getString("profileStatus", "");
        userCode1k = user.getString("status1K", "");
        userCode7k = user.getString("status7K", "");
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userProfileStatus.equals("approved") && (checkDiffernece > 30)) {
                    if (checkCorrectdis == 1) {
                        try {
                            Map userMap = new HashMap<>();
                            userMap.put("PRODUCT_CLICKED", title);
                            userMap.put("EMI_SELECTED", EMIcheck);
                            userMap.put("DOWNPAYMENT", dValue.getText().toString());
                            //                        userMap.put("phone", mPhone);
                            //                        System.out.println("Intercom data 4" + mPhone);
                            Intercom.client().updateUser(userMap);
                        } catch (Exception e) {
                            System.out.println("Intercom two" + e.toString());
                        }
                        //                        if(dValue.getText().toString();)
                        int minD = 0;
                        if (dValue.getText().toString().length() != 0)
                            minD = Integer.parseInt(dValue.getText().toString());
                        if ((minD < minDownpayment) || (dValue.getText().toString().length() == 0))
                            editdp();
                        Intent in = new Intent(ProductsPage.this, ConfirmOrder.class);
                        in.putExtra("title", title);
                        in.putExtra("prid", productId1);
                        in.putExtra("servicecharge", secondServicecharge);
                        in.putExtra("brand", brand);
                        in.putExtra("emicheck", EMIcheck);
                        in.putExtra("cashback", checkCashback);
                        if (hve.getText().toString().contains("off!"))
                            in.putExtra("whichCoupon", whichCoupon);
                        String t = hve.getText().toString();
                        if (((hve.getText().toString().equals(""))) || (hve.getText().toString().contains("Offers"))) {
                            mDis = 0;
                            checkCashback = 0;
                        }
                        in.putExtra("discount", mDis);
                        in.putExtra("monthforemi", monthsnow);
                        in.putExtra("daytoday", dayToday);
                        System.out.print("che" + dayToday + "buddy");
                        in.putExtra("daytodaycheck", cuurr);
                        in.putExtra("down", dValue.getText().toString());
                        in.putExtra("sellingprice", searchPrice);
                        in.putExtra("seller", sellerNme);
                        in.putExtra("image", urlforImage);
                        //                    in.putExtra("discount", 0);
                        in.putExtra("emi", emiAmount.getText().toString());
                        in.putExtra("months", selectedText);
                        SharedPreferences cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
                        SharedPreferences.Editor et = cred.edit();
                        et.putString("title", title);
                        et.putString("prid", productId1);
                        et.putInt("sp", searchPrice);
                        et.putInt("service", secondServicecharge);
                        et.putString("brand", brand);
                        et.putInt("checkCashback", checkCashback);
                        if (hve.getText().toString().contains("off!"))
                            et.putString("whichCoupon", whichCoupon);
                        else
                            et.putString("whichCoupon", "");
                        et.putInt("monthtenure", monthsnow);
                        et.putInt("discount", mDis);
                        et.putString("seller", sellerNme1);
                        et.commit();

                        startActivity(in);
                    } else {

                    }
                    //     finish();in.putE
                } else if (userProfileStatus.equals("waitlisted") || userProfileStatus.equals("declined") || ((checkDiffernece <= 30) && ("approved".equals(userProfileStatus))) || ("onHold".equals(userProfileStatus))) {
                    LayoutInflater inflater = (LayoutInflater) (ProductsPage.this).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    //                 View   parent = inflater.inflate(R.layout.activity_products_page, null, false);
                    View popUpView = inflater.inflate(R.layout.popupwaitlisted, null, false);

                    popup = new PopupWindow(popUpView);
                    //                        580, true);

                    popup.setContentView(popUpView);
                    popup.setWidth(ListPopupWindow.WRAP_CONTENT);
                    popup.setHeight(ListPopupWindow.WRAP_CONTENT);
                    popup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);

                    RelativeLayout cover = (RelativeLayout) findViewById(R.id.cover);
                    //                prod.setTi(Color.parseColor("#CC000000"));
                    cover.setVisibility(View.VISIBLE);
                    //    <TextView Talk to us to find out more
                    checkout.setEnabled(false);

                    TextView talk = (TextView) popUpView.findViewById(R.id.talk);
                    String set = "";
                    if ((checkDiffernece < 30) || ("onHold".equals(userProfileStatus))) {
                        TextView status = (TextView) popUpView.findViewById(R.id.status);
                        status.setVisibility(View.GONE);
                        TextView msg = (TextView) popUpView.findViewById(R.id.msg);
                        msg.setVisibility(View.GONE);
                        set = "<font color=#3380B6>As per our current policies you cannot place your next order. </font> <font color=#33A4D0>Contact us to find out more.</font>";
                    } else
                        set = "<font color=#3380B6>Talk to us </font> <font color=#33A4D0>to find out more</font>";
                    talk.setText(Html.fromHtml(set));
                    talk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intercom.client().displayMessageComposer();

                        }
                    });
                    TextView ok = (TextView) popUpView.findViewById(R.id.ok);
                    cover.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.dismiss();
                            RelativeLayout cover = (RelativeLayout) findViewById(R.id.cover);
                            cover.setVisibility(View.INVISIBLE);
                            checkout.setEnabled(true);
                        }
                    });
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.dismiss();
                            RelativeLayout cover = (RelativeLayout) findViewById(R.id.cover);
                            cover.setVisibility(View.INVISIBLE);
                            checkout.setEnabled(true);
                        }
                    });
                    //                    String set = "<font color=#664A4A4A>Checkout the ratings and reviews for this product. </font> <font color=#33A4D0>Click here</font>";
                    //                    detRet.setText(Html.fromHtml(set));

                } else if (userProfileStatus.trim().length() == 0 || userProfileStatus.equals(Constants.STATUS.APPLIED.toString()) || (dummyCl == 1000)) {
                    LayoutInflater inflater = (LayoutInflater) (ProductsPage.this).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    //                 View   parent = inflater.inflate(R.layout.activity_products_page, null, false);
                    View popUpView = inflater.inflate(R.layout.popupapplied, null, false);

                    popup = new PopupWindow(popUpView);
                    //                        580F, true);

                    popup.setContentView(popUpView);
                    popup.setWidth(ListPopupWindow.WRAP_CONTENT);
                    popup.setHeight(ListPopupWindow.WRAP_CONTENT);
                    popup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);

                    final RelativeLayout cover1 = (RelativeLayout) findViewById(R.id.cover);
                    //                prod.setTi(Color.parseColor("#CC000000"));
                    cover1.setVisibility(View.VISIBLE);
                    //    <TextView Talk to us to find out more

                    checkout.setEnabled(false);
                    TextView ok = (TextView) popUpView.findViewById(R.id.ok1);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.dismiss();
                            Intent profile = new Intent(ProductsPage.this, ProfileActivity.class);
                            startActivity(profile);
                            finish();
                            overridePendingTransition(0, 0);
                            checkout.setEnabled(true);
                            //RelativeLayout cover = (RelativeLayout) findViewById(R.id.cover);
                            cover1.setVisibility(View.INVISIBLE);
                        }
                    });
                    cover1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.dismiss();
                            checkout.setEnabled(true);
                            //RelativeLayout cover = (RelativeLayout) findViewById(R.id.cover);
                            cover1.setVisibility(View.INVISIBLE);
                        }
                    });


                }

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
                    detInfo.setText(infor);
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
                    detDes.setText(description);
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
                    detSpec.setText(specification);
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
                    String set = "<font color=#664A4A4A>Checkout the ratings and reviews for this product. </font> <font color=#33A4D0>Click here</font>";
                    detRet.setText(Html.fromHtml(set));
                    detRet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(review));
                            startActivity(browserIntent);
                        }
                    });
                }

            }
        });
        TextView knowmore = (TextView) findViewById(R.id.knowmore);
        knowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(review));
                startActivity(browserIntent);

            }
        });


        vde.setVisibility(View.GONE);
        vow.setVisibility(View.VISIBLE);
        overview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation ani1 = AnimationUtils.loadAnimation(ProductsPage.this, R.anim.fadeout);

                overview.setVisibility(View.GONE);
                overview.startAnimation(ani1);
                Animation ani = AnimationUtils.loadAnimation(ProductsPage.this, R.anim.show);
                det.setVisibility(View.VISIBLE);
                det.startAnimation(ani);
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
        //        hve.setFocusable(false);
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
        hve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editdp();

                RelativeLayout cash = (RelativeLayout) findViewById(R.id.cashback);
                if (hve.getText().toString().trim().length() == 0)
                    cash.setVisibility(View.VISIBLE);


            }
        });
        hve.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editdp();

                //int t=hve.getText().toString().length();
                //                Boolean ttt = hve.hasSelection();


                //                Toast.makeText(ProductsPage.this, "" + ttt, Toast.LENGTH_SHORT).show();
                //                hve.setFocusableInTouchMode(true);
                RelativeLayout cash = (RelativeLayout) findViewById(R.id.cashback);
                if (hve.getText().toString().trim().length() == 0)
                    cash.setVisibility(View.VISIBLE);
                //
                //
                return false;
            }
        });
        hve.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    hve.setHint("");

                } else
                    hve.setHint("Offers and Cashback");

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
                    //appcBack.setBackgroundResource(R.drawable.radiodraw);


                    hve.setBackgroundResource(R.drawable.roundedblue);
                    hve.setTextColor(Color.parseColor("#3380B6"));
                    hve.setText("");
                    ((RelativeLayout) findViewById(R.id.plusRelative)).setBackgroundColor(Color.parseColor("#3380B6"));
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
        final RelativeLayout enter = ((RelativeLayout) findViewById(R.id.plusRelative));


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    hideSoftKeyboard(ProductsPage.this);
                } catch (Exception e) {
                }
                //                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if (hve.getKeyListener() == null) {
                    hve.setBackgroundResource(R.drawable.roundedblue);
                    checkCorrectdis = 1;
                    hve.setTextColor(Color.parseColor("#3380B6"));
                    enter.setBackgroundColor(Color.parseColor("#3380B6"));
                    hve.setKeyListener(listen);
                    hve.setText("");
                    appcBack.setChecked(false);
                    couCode.setChecked(true);
                } else {


                    //                Toast.makeText(ProductsPage.this, "cccc", Toast.LENGTH_SHORT).show();
                    crcode = hve.getText().toString().trim().toUpperCase();
                    if ((crcode.length() != 0) && (couCode.isChecked())) {

                        new COUPON().execute();
                    }
                }
                ImageView pl = (ImageView) findViewById(R.id.plus);
                //                    Drawable.ConstantState d = pl.getDrawable().getConstantState();
                //                    Drawable.ConstantState d1 = getResources().getDrawable(R.drawable.cancel).getConstantState();
                //                    Drawable p = pl.getDrawable();
                //                Drawable r=DrR.drawable.cancel;
                //                Drawable i=R.drawable.cancel;
                //                if (pl.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.cancel).getConstantState())) {
                if ((checkImg == 2) || (checkImg == 3)) {
                    mDis = 0;
                    ((RelativeLayout) findViewById(R.id.cashback)).setVisibility(View.GONE);
                    sellingPrice = searchPrice;
                    System.out.println("entering here" + checkImg);
                    checkD = 0;
                    checkCashback = 0;
                    Double doPay = (searchPrice * .2);
                    //                    if((sellingPrice<=1000)&&(sellingPrice>150))
                    //                        doPay=0.0;
                    dopay2 = doPay.intValue();
                    cb = st.getInt("cashBack", 0);
                    int cl = st.getInt("creditLimit", 0);
                    int cbv = st.getInt("totalBorrowed", 0);
                    int fcbv = cl - cbv;
                    if (fcbv <= 0) {
                        dummyCl = 1000;
                        fcbv = 100000000;
                    }

                    if (searchPrice - dopay2 > fcbv) {
                        dopay2 = searchPrice - fcbv;
                    }
                    minDownpayment = globalMindown;
                    mValue = minDownpayment;
                    int mmonthss=months(searchSubcategory, searchCategory, searchBrand, sellingPrice);
                    List<String> categories = new ArrayList<String>();
                    int p=-1;
                    for (int j = 0; j < 8; j++) {
                        if ((myMonths[j] <= mmonthss)&&(mmonthss!=0)) {

                            p = j;
                            //categories.add(String.valueOf(myMonths[j]) + " months");
                        }
                    }

                    if (fcbv <= 0) {
                        dummyCl = 1000;
                        fcbv = 100000000;
                    }
                    for (int ww = p; ww >= 0; ww--) {
                        categories.add(String.valueOf(myMonths[ww]) + " months");
                    }
                    if ((searchPrice <= 150) || (dummyCl == 1000 && Constants.STATUS.APPROVED.toString().equals(userProfileStatus))) {
                        categories.clear();
                        categories.add("No Financing");
                    } else

                    {
                        categories.add("No Financing");
                    }

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ProductsPage.this, android.R.layout.simple_spinner_item, categories);

                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    dataAdapter.notifyDataSetChanged();
                    // attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter);


                    int w = serviceCharge(searchPrice, sellingPrice - mValue, sellerNme1);
                    firstServicecharge = w;
                    secondServicecharge = serviceCharge(searchPrice, sellingPrice - mValue, sellerNme1);
                    setEmi(sellingPrice);
                    appcBack.setChecked(false);

                    couCode.setChecked(true);
                    hve.setCursorVisible(true);
                    hve.setText("");

                }


            }

        });
        if (cb == 0) {
            appcBack.setChecked(false);
            appcBack.setEnabled(false);
        } else {
            appcBack.setChecked(false);
            appcBack.setEnabled(true);
        }
        Boolean flash = true;
        if (userCode1k.equals("approved") && (!(userCode7k.equals("approved"))))
            flash = false;
        if (cb == 0)
            flash = false;
        appcBack.setEnabled(flash);
        appcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb != 0) {


                    {
                        int checkD = 0;
                        if (appcBack.isChecked()) {
                            // TODO: 4/21/2016 do something with cashback
                            checkD = 1;

                            sellingPrice = spDec;
                            Double doPay = (searchPrice * .2);
                            //                            if((searchPrice<=1000)&&(searchPrice>150))
                            //                                doPay=0.0;
                            dopay2 = doPay.intValue();
                            cb = st.getInt("cashBack", 0);
                            int cl = st.getInt("creditLimit", 0);
                            int cbv = st.getInt("totalBorrowed", 0);
                            int fcbv = cl - cbv;
                            if (fcbv <= 0) {
                                dummyCl = 1000;
                                fcbv = 100000000;
                            }

                            if (searchPrice - dopay2 > fcbv) {
                                dopay2 = searchPrice - fcbv;
                            }


                            checkCashback = 1;

                            if (sellingPrice - cb < 0) {
                                checkD = 0;

                                mDis = sellingPrice;
                                sellingPrice = 0;
                                //                                setEmi(2);
                            } else {

                                checkD = 0;
                                mDis = cb;
                                sellingPrice = sellingPrice - mDis;
                                //                                setEmi(2);

                            }
                            dee = 1;
                            hve.setBackgroundResource(R.drawable.roundedyellow);
                            ((RelativeLayout) findViewById(R.id.plusRelative)).setBackgroundColor(Color.parseColor("#F28E52"));
                            //                        setEmi(sellingPrice);
                            couCode.setChecked(false);

                            Double mind = 0.0;
                            if (searchPrice >= 1000)
                                mind = sellingPrice * .2;
                            else if ((searchPrice <= 1000) && (searchPrice > 150))
                                mind = sellingPrice * .2;

                            if (sellingPrice - mind > fcbv) {
                                mind = Double.valueOf(sellingPrice) - fcbv;
                            }
                            if (dummyCl == 1000)
                                availbal.setText(getApplicationContext().getString(R.string.Rs) + "0");
                            else
                                availbal.setText(getApplicationContext().getString(R.string.Rs) + fcbv);
                            int w = serviceCharge(searchPrice, sellingPrice - mind.intValue(), sellerNme1);
                            firstServicecharge = w;
                            secondServicecharge = serviceCharge(searchPrice, sellingPrice - mind.intValue() - w, sellerNme1);
                            checkCorrectdis = 1;

                            dValue.setText(String.valueOf(Math.round(mind + w)));
                            minDownpayment = Integer.parseInt(dValue.getText().toString());
                            int mmonthss=months(searchSubcategory, searchCategory, searchBrand, sellingPrice);
                            List<String> categories = new ArrayList<String>();
                            int p=-1;
                            for (int j = 0; j < 8; j++) {
                                if ((myMonths[j] <= mmonthss)&&(mmonthss!=0)) {

                                    p = j;
                                    //categories.add(String.valueOf(myMonths[j]) + " months");
                                }
                            }

                            if (fcbv <= 0) {
                                dummyCl = 1000;
                                fcbv = 100000000;
                            }
                            for (int ww = p; ww >= 0; ww--) {
                                categories.add(String.valueOf(myMonths[ww]) + " months");
                            }
                            if ((searchPrice <= 150) || (dummyCl == 1000 && Constants.STATUS.APPROVED.toString().equals(userProfileStatus))) {
                                categories.clear();
                                categories.add("No Financing");
                            } else

                            {
                                categories.add("No Financing");
                            }

                            // Creating adapter for spinner
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ProductsPage.this, android.R.layout.simple_spinner_item, categories);

                            // Drop down layout style - list view with radio button
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            dataAdapter.notifyDataSetChanged();
                            // attaching data adapter to spinner
                            spinner.setAdapter(dataAdapter);



                            hve.setText(getApplicationContext().getString(R.string.Rs) + mDis + " Cashback applied!");

                            hve.setKeyListener(null);
                            hve.setTextColor(Color.parseColor("#F28E52"));
                            ((RelativeLayout) findViewById(R.id.plusRelative)).setVisibility(View.VISIBLE);
                            ((RelativeLayout) findViewById(R.id.cashback)).setVisibility(View.GONE);
                            setEmi(2);
                            ((ImageView) findViewById(R.id.plus)).setImageResource(R.drawable.cancel);
                            checkImg = 2;
                            //                    ((RelativeLayout) findViewById(R.id.cashback)).setVisibility(View.GONE);
                            //                    if (cb == 0)
                            //                        Toast.makeText(ProductsPage.this, "zero cashback", Toast.LENGTH_SHORT).show();
                        }


                    }
                } else
                    Toast.makeText(ProductsPage.this, "No Cashback to apply!", Toast.LENGTH_SHORT).show();
            }
        });


        Double loan = monthsallowed * emi;
        cb = st.getInt("cashBack", 0);
        int cl = st.getInt("creditLimit", 0);
        int cbv = st.getInt("totalBorrowed", 0);
        int fcbv = cl - cbv;
        Double ddd2 = sellingPrice * .2;
        //        if((searchPrice<=1000)&&(searchPrice>150))
        //            ddd2=0.0;

        loan += sellingPrice * .2;
        if (fcbv <= 0) {
            dummyCl = 1000;
            fcbv = 100000000;
        }

        if (searchPrice - ddd2.intValue() > fcbv) {
            loan += searchPrice - fcbv;
        }
        totalLoan.setText(String.valueOf(Math.round(loan)));
        Double downValue = sellingPrice * .2;
        //        if((searchPrice<=1000)&&(searchPrice>150))
        //            downValue=0.0;


        mValue = downValue.intValue();
        mValue2 = downValue.intValue();

        if (searchPrice - downValue.intValue() > fcbv) {
            mValue = searchPrice - fcbv;
            mValue2 = searchPrice - fcbv;
        }
        //        dValue.setText(String.valueOf(Math.round(downValue)));
        //        if(searchPrice-downValue.intValue()>fcbv)
        mValue = minDownpayment;

        EMIcheck = Math.round(emi);
        emiAmount.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(emi) + " per month"));
        priceChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ProductsPage.this);
                alert.setMessage("Sometimes the actual price of the " +
                        "product might vary because of the lag in placing order " +
                        "or additional delivery charges. For price changes within Rs.100," +
                        "we will go ahead and place the order with the seller of your choice." +
                        "The extra amount will be adjusted in your Flexible Payment Plan." +
                        "\n" + "\n" +
                        "If the price exceeds Rs.100, we will contact you to confirm which " +
                        "seller you want to buy product from :)");
                alert.create().show();
            }
        });
        if (searchBrand.trim().length() > 0)
            brandName.setText("Manufacturer: " + brand);
        else
            brandName.setText("");
        if (sellerNme.equals("amazon"))
            seller.setImageResource(R.drawable.amazon);
        else if (sellerNme.equals("flipkart"))
            seller.setImageResource(R.drawable.flipart);
        else if (sellerNme.equals("snapdeal"))

            seller.setImageResource(R.drawable.snapdeal);
        if (sellingPrice != searchPrice) {
            sellingRschange.setVisibility(View.VISIBLE);
            sellingRschange.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(searchPrice)));
            sellingRschange.setPaintFlags(sellingRschange.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            sellingRs.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(sellingPrice)));
        } else {
            sellingRschange.setVisibility(View.GONE);
            sellingRs.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(sellingPrice)));
        }
        titlePro.setText(searchTitle);
        query.setText(searchQuery);
    }

    private class COUPON extends
            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
            checkout.setEnabled(false);
            ((RelativeLayout) findViewById(R.id.plusRelative)).setEnabled(false);
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
                String url2 = BuildConfig.SERVER_URL + "api/promo/coupon";
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
                        try {
                            value = data1.getInt("value");
                        } catch (Exception e) {
                            value = 0;
                        }
                        try {
                            maxValue = data1.getInt("maxValue");
                        } catch (Exception e) {
                            maxValue = 0;
                        }
                        try {
                            minProd = data1.getInt("minProdValue");
                        } catch (Exception e) {
                            minProd = 0;
                        }
                        try {
                            type = data1.getString("type");
                        } catch (Exception e) {
                            type = "flat";
                        }
                        if (searchPrice < minProd)
                            return "min";
                        //// TODO: 14/6/16 flash users 1k approved and 7k not approved then only flash users
                        Boolean flash = true;
                        if (userCode1k.equals("approved") && (!(userCode7k.equals("approved"))))
                            flash = false;
                        if (!flash) {
                            return "flash";
                        }
                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {
            checkout.setEnabled(true);
            ((RelativeLayout) findViewById(R.id.plusRelative)).setEnabled(true);
            if (result.equals("win")) {
                int dis = 0;
                if (type.equals("flat"))
                    dis = value;
                else if (type.equals("percentage")) {
                    Double dd = searchPrice * value * 1.0 / 100;
                    //                    if((searchPrice<=1000)&&(searchPrice>150))
                    //                        dd=0.0;
                    int newdis = dd.intValue();
                    if (newdis <= maxValue)
                        dis = newdis;
                    else
                        dis = maxValue;
                }
                ((RelativeLayout) findViewById(R.id.cashback)).setVisibility(View.GONE);
                Double doPay = (searchPrice * .2);
                //                if((searchPrice<=1000)&&(searchPrice>150))
                //                    doPay=0.0;
                dopay2 = doPay.intValue();
                cb = st.getInt("cashBack", 0);
                int cl = st.getInt("creditLimit", 0);
                int cbv = st.getInt("totalBorrowed", 0);
                int fcbv = cl - cbv;
                //                if((searchPrice<=1000)&&(searchPrice>150))
                if (fcbv <= 0) {
                    dummyCl = 1000;
                    fcbv = 100000000;
                }

                //                    dopay2=0;
                if (searchPrice - dopay2 > fcbv) {
                    dopay2 = sellingPrice - fcbv;
                }
                whichCoupon = crcode;


                if (sellingPrice - dis < 0) {
                    checkD = 0;
                    mDis = sellingPrice;
                    sellingPrice = 0;

                } else {

                    checkD = 0;
                    mDis = dis;
                    sellingPrice = sellingPrice - mDis;
                    //                    setEmi(2);

                }
                int mmonthss=months(searchSubcategory, searchCategory, searchBrand, sellingPrice);
                List<String> categories = new ArrayList<String>();
                int p=-1;
                for (int j = 0; j < 8; j++) {
                     if ((myMonths[j] <= mmonthss)&&(mmonthss!=0)) {

                        p = j;
                        //categories.add(String.valueOf(myMonths[j]) + " months");
                    }
                }

                if (fcbv <= 0) {
                    dummyCl = 1000;
                    fcbv = 100000000;
                }
                for (int ww = p; ww >= 0; ww--) {
                    categories.add(String.valueOf(myMonths[ww]) + " months");
                }
                if ((searchPrice <= 150) || (dummyCl == 1000 && Constants.STATUS.APPROVED.toString().equals(userProfileStatus))) {
                    categories.clear();
                    categories.add("No Financing");
                } else

                {
                    categories.add("No Financing");
                }

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ProductsPage.this, android.R.layout.simple_spinner_item, categories);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                dataAdapter.notifyDataSetChanged();
                // attaching data adapter to spinner
                spinner.setAdapter(dataAdapter);


                //                hve.setFocusable(false);

                hve.setBackgroundResource(R.drawable.roundedproducts);
                hve.setTextColor(Color.parseColor("#44C2A6"));
                ((RelativeLayout) findViewById(R.id.plusRelative)).setBackgroundColor(Color.parseColor("#44C2A6"));
                ((RelativeLayout) findViewById(R.id.cashback)).setVisibility(View.GONE);
                hve.setText("Code applied " + getApplicationContext().getString(R.string.Rs) + mDis + " off!");
                checkCorrectdis = 1;
                Double mind = 0.0;
                if (searchPrice >= 1000)
                    mind = sellingPrice * .2;
                else if ((searchPrice <= 1000) && (searchPrice > 150))
                    mind = sellingPrice * .2;

                if (sellingPrice - mind > fcbv) {
                    mind = Double.valueOf(sellingPrice) - fcbv;
                }
                if (dummyCl == 1000)
                    availbal.setText(getApplicationContext().getString(R.string.Rs) + "0");
                else
                    availbal.setText(getApplicationContext().getString(R.string.Rs) + fcbv);
                int w = serviceCharge(searchPrice, sellingPrice - mind.intValue(), sellerNme1);
                firstServicecharge = w;
                secondServicecharge = serviceCharge(searchPrice, sellingPrice - mind.intValue() - w, sellerNme1);
                checkCorrectdis = 1;
                if (!checkmonths.equals("0"))
                    dValue.setText(String.valueOf(Math.round(mind + w)));
                mValue = mind.intValue() + w;
                if (sellingPrice != searchPrice) {
                    sellingRschange.setVisibility(View.VISIBLE);
                    sellingRschange.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(searchPrice)));
                    sellingRschange.setPaintFlags(sellingRschange.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    sellingRs.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(sellingPrice)));
                } else {
                    sellingRschange.setVisibility(View.GONE);
                    sellingRs.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(sellingPrice)));
                }
                //                sellingRs.setText(getApplicationContext().getString(R.string.Rs)+String.valueOf(Math.round(sellingPrice)));
                minDownpayment = mValue;


                checkImg = 2;
                setEmi(2);

                ((ImageView) findViewById(R.id.plus)).setImageResource(R.drawable.cancel);

                hve.setKeyListener(null);
                //                Toast.makeText(ProductsPage.this, value, Toa/st.LENGTH_SHORT).show();
            } else {
                checkCorrectdis = 1;
                mDis = 0;
                if (result.contains("min")) {
                    Toast.makeText(ProductsPage.this, "Minimum product value to use this Coupon is " + minProd, Toast.LENGTH_SHORT).show();
                    truth = "Invalid Code";
                }
                if (result.contains("flash")) {
                    Toast.makeText(ProductsPage.this, "This discount code is not applicable for you!.", Toast.LENGTH_SHORT).show();
                    truth = "Invalid Code";
                }
                hve.setText(truth);
                checkImg = 3;
                //hve.setFocusable(false);

                hve.setKeyListener(null);
                checkCorrectdis = 0;
                //                hve.setFocusable(false);
                hve.setBackgroundResource(R.drawable.roundedred);
                hve.setTextColor(Color.parseColor("#D48080"));
                ((RelativeLayout) findViewById(R.id.plusRelative)).setBackgroundColor(Color.parseColor("#D48080"));
                ((RelativeLayout) findViewById(R.id.cashback)).setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.plus)).setImageResource(R.drawable.retry);

            }
        }
    }

    public Double calculateEmi(Double principal, Double searchPrice, int months) {
        if (months == 0) {
            return 0.0;
        }
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
            currDay=noOfDays;
            cuurr = currDay;
        } catch (Exception e) {
            //532
        }
        Double emi = 0.0;
        Double rate = interestRate / 1200.0;
        int d = 0;
        if ((searchPrice.intValue()<=nointerestSp&&("sellingPrice".equals(noInterestAppon)))||(nointerestEmiten>=months&&("loanAmount".equals(noInterestAppon)))) {
            {
                if (currDay <= 15)
                    d = 35 - currDay;
                else
                    d = 65 - currDay;
                dayToday = d;


                        emi = (principal * 1.0 / months);


            }
        } else {
            if (currDay <= 15)
                d = 35 - currDay;
            else
                d = 65 - currDay;
            dayToday = d;
            emi = ((principal * rate * Math.pow(1 + rate, months - 1) * (1 + rate * d * 12 / 365)) / (Math.pow(1 + rate, months) - 1));
        }
        //Toast.makeText(ProductsPage.this, String.valueOf(emi), Toast.LENGTH_SHORT).show();
        if (emi < 0)
            return 0.0;
        else {
            if (emi - Math.round(emi) > 0)
                return Math.floor(emi);
            else
                return
                        Math.ceil(emi);
        }
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
        //        if((sellingPrice<=1000)&&(sellingPrice>150))
        //            v=0.0;
        cb = st.getInt("cashBack", 0);
        int cl = st.getInt("creditLimit", 0);
        int cbv = st.getInt("totalBorrowed", 0);
        int fcbv = cl - cbv;
        mValue = v.intValue();
        mValue2 = v.intValue();
        if (t.contains("No"))

            t = "0";
        considerTen=Integer.parseInt(t);
        if (fcbv <= 0) {
            dummyCl = 1000;
            fcbv = 100000000;
        }

        monthsnow = Integer.parseInt(t);
        if (sellingP == 1) {
            if (sellingPrice - mValue > fcbv)
                mValue = sellingPrice - fcbv;

            emiAmount.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(calculateEmi(sellingPrice - mValue * 1.0 - mDis, Double.valueOf(sellingPrice), monthsnow))) + " per month");
            Double tot = calculateEmi(sellingPrice * 0.8 - mDis, Double.valueOf(sellingPrice), monthsnow) * monthsnow + dopay2;
            //            Double r = (mValue - mDis);
            mValue = mValue - mDis;
            dValue.setText(String.valueOf(Math.round(mValue)));
            if (sellingPrice != searchPrice) {
                sellingRschange.setVisibility(View.VISIBLE);
                sellingRschange.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(searchPrice)));
                sellingRschange.setPaintFlags(sellingRschange.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                sellingRs.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(sellingPrice)));
            } else {
                sellingRschange.setVisibility(View.GONE);
                sellingRs.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(sellingPrice)));
            }
            //            sellingRs.setText(getApplicationContext().getString(R.string.Rs)+String.valueOf(Math.round(sellingPrice)));
            totalLoan.setText(String.valueOf(tot));
        } else {

            Double l = Math.floor(sellingPrice * .2);
            //            if((sellingPrice<=1000)&&(sellingPrice>150))
            //                l=0.0;


            int w = 0;
            mValue = minDownpayment;
            if (t.contains("0")) {
                if ((sellerNme1.equals("flipkart")) || (sellerNme1.equals("snapdeal")))


                    mValue = sellingPrice + 29;
                else {
                    int serv = 0;
                    {
                        if (searchPrice < 1000)
                            serv = 29;
                        else if (searchPrice < 5000)
                            serv = 99;
                        else if (searchPrice < 10000)
                            serv = 199;
                        else if (searchPrice < 15000)
                            serv = 299;
                        else if (searchPrice < 25000)
                            serv = 449;
                        else if (searchPrice > 25000)
                            serv = 599;
                    }
                    mValue = sellingPrice + serv;
                }
                minDownpaymentfornofina = mValue;
                checkmonths = "0";

            } else
                checkmonths = "1";
            //
            // if((sellingPrice<=1000)&&(sellingPrice>150))
            //                mValue=0;
            //            dValue.setText(String.valueOf(l.intValue()));
            if (sellingPrice - mValue > fcbv) {

                mValue = sellingPrice - fcbv;
            }
            if (t.contains("0")) {

                if ((sellerNme1.equals("flipkart")) || (sellerNme1.equals("snapdeal")))


                    mValue = sellingPrice + 29;
                else {
                    int serv = 0;
                    {
                        if (searchPrice < 1000)
                            serv = 29;
                        else if (searchPrice < 5000)
                            serv = 99;
                        else if (searchPrice < 10000)
                            serv = 199;
                        else if (searchPrice < 15000)
                            serv = 299;
                        else if (searchPrice < 25000)
                            serv = 449;
                        else if (searchPrice > 25000)
                            serv = 599;
                    }
                    mValue = sellingPrice + serv;
                }

                minDownpaymentfornofina = mValue;
                checkmonths = "0";
            } else {
                mValue = minDownpayment;
                checkmonths = "1";
            }


            int ww = serviceCharge(searchPrice, searchPrice - mValue, sellerNme1);
            firstServicecharge = ww;
            secondServicecharge = serviceCharge(searchPrice, sellingPrice - mValue, sellerNme1);
            //change vari
            if(isZeroDpApplicable&&fcbv>=sellingPrice&&searchPrice<=1000)
            {mValue=0;
                }

            dValue.setText(String.valueOf(mValue));
            if(mValue!=0)
            minDownpayment = mValue;
            availbalmsg.setText("Minimum Downpayment for this product: " + getApplicationContext().getString(R.string.Rs) + mValue);
            if (sellingPrice != searchPrice) {
                sellingRschange.setVisibility(View.VISIBLE);
                sellingRschange.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(searchPrice)));
                sellingRschange.setPaintFlags(sellingRschange.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                sellingRs.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(sellingPrice)));
            } else {
                sellingRschange.setVisibility(View.GONE);
                sellingRs.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(sellingPrice)));
            }
            //            sellingRs.setText(getApplicationContext().getString(R.string.Rs)+String.valueOf(Math.round(sellingPrice)));


            //            EMIcheck=(Math.round(calculateEmi(sellingPrice, Double.valueOf(searchPrice), monthsnow)));
            EMIcheck = Math.round(calculateEmi(sellingPrice - mValue * 1.0 + secondServicecharge, Double.valueOf(searchPrice), monthsnow));

            emiAmount.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(EMIcheck) + " per month");
            Double tot = calculateEmi(sellingPrice * 0.8, Double.valueOf(sellingPrice), monthsnow) * monthsnow + sellingPrice * .2;
            totalLoan.setText(String.valueOf(Math.round(tot)));

            //        Toast.makeText(ProductsPage.this, selectedText, Toast.LENGTH_SHORT).show();

        }
    }

    public void backpress() {
        LinearLayout back = (LinearLayout) findViewById(R.id.arrowlay);
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
            try {
                loader.setVisibility(View.VISIBLE);
                viewDetail.setVisibility(View.GONE);
            } catch (Exception e) {
            }
        }


        @Override
        public String doInBackground(String... data) {

            //  String urldisplay = data[0];
            //               HashMap<String, String> details = data[0];
            // JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: get
                HttpResponse response = null;
                String responseString = "";
                String url = BuildConfig.SERVER_URL + "api/product?productId=" + productId1 + "&seller=" + sellerNme1 + "&userid=" + creduserid;

                if (sellerNme1.equals("paytm") || (sellerNme1.equals("myntra")))
                    url = BuildConfig.SERVER_URL + "api/v1/product/details";
                //                String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjY1M2M2YTUwZTQzNzgyNjc0M2YyNjYiLCJuYW1lIjoiYnVkZHkgYXBpIGFkbWluIiwidXNlcm5hbWUiOiJidWRkeWFwaWFkbWluIiwicGFzc3dvcmQiOiJtZW1vbmdvc2gxIiwiZW1haWwiOiJjYXJlQGhlbGxvYnVkZHkuaW4iLCJpYXQiOjE0NTY3MjY1NzMsImV4cCI6MTQ1Njc2MjU3M30.98mQFcYm5Uf3Fd7ZNPD-OwMIfObu7vfoq9zNtCCLfyI";
                // payload.put("action", details.get("action"));

                paymyntra = sellerNme1.equals("paytm") || (sellerNme1.equals("myntra"));
                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);


                HttpClient client = new DefaultHttpClient(httpParameters);
                if (!paymyntra) {


                    HttpGet httppost = new HttpGet(url);
                    httppost.setHeader("x-access-token", st.getString("token_value", null));
                    httppost.setHeader("Content-Type", "application/json");


                    response = client.execute(httppost);
                    HttpEntity ent = response.getEntity();
                    responseString = EntityUtils.toString(ent, "UTF-8");
                } else {
                    JSONObject payload = new JSONObject();
                    payload.put("url",productId1);
                    payload.put("pincode", "560095");
                    payload.put("original", false);
                    HttpPost httppost = new HttpPost(url);
                    httppost.setHeader("x-access-token", st.getString("token_value", null));
                    httppost.setHeader("Content-Type", "application/json");
                    StringEntity entity = new StringEntity(payload.toString());
                    httppost.setEntity(entity);
                    response = client.execute(httppost);
                    HttpEntity ent = response.getEntity();
                    responseString = EntityUtils.toString(ent, "UTF-8");
                }
                if (response.getStatusLine().getStatusCode() != 200) {
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);
                    if (resp.getString("status").equals("success")) {
                        JSONObject data1 = new JSONObject(resp.getString("data"));
                        try{
                        searchTitle = data1.getString("title");}
                        catch (Exception e)
                        {}
                        try {
                            searchBrand = data1.getString("brand");
                        }
                        catch (Exception e)
                        {}
                        try {
                            searchCategory = data1.getString("category");
                        }
                        catch (Exception e)
                        {}
                          try {
                              searchSubcategory = data1.getString("subCategory");
                          }catch (Exception e)
                          {}
                        try {
                            searchPrice = data1.getInt("sellingPrice");
                            sellingPrice = searchPrice;
                        }catch (Exception e)
                        {}
                        if(!paymyntra) {
                            JSONObject img = new JSONObject(data1.getString("imgUrls"));
                            urlImg = img.getString("400x400");
                            urlforImage = urlImg;
                        }
                        else
                        {
                            urlImg = data1.getString("img");
                            urlforImage = urlImg;
                        }
                        try{
                            brand = searchBrand;}
                        catch (Exception e)
                        {}
                        try {
                            specification = data1.getString("specificaiton");
                        } catch (Exception e) {
                            specification = "";
                        }
                        try {
                            description = data1.getString("description");
                        } catch (Exception e) {
                            description = "";
                        }
                        try {
                            review = data1.getString("fkProductUrl");
                        } catch (Exception e) {
                            review = "";
                        }
                        if(paymyntra)
                        {
                            review=productId1;
                        }
                        infor = "The minimum downpayment is 20% of the product price and also depends on the payment band (Oxygen/Silicon/Palladium/Krypton) you lie in, which you will get to know after your college ID verification.";


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
                Intent in = new Intent(ProductsPage.this, ProductsPage.class);
                in.putExtra("page", "pay");
                in.putExtra("seller", getIntent().getExtras().getString("seller"));
                finish();
                startActivity(in);
                try {
                    loader.setVisibility(View.GONE);
                    viewDetail.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                }
                //                getIntent().getExtras().getString("seller");
            } else {
              int flipCashback=Math.round(searchPrice*2/100);
                if(("flipkart".equals(sellerNme1))||("snapdeal".equals(sellerNme1))){
                    flipcash.setVisibility(View.VISIBLE );
                flipcash.setText(getApplicationContext().getString(R.string.Rs)+flipCashback+" Cashback on this order!");}
                try {
                    loader.setVisibility(View.GONE);
                    viewDetail.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                }
                cb = st.getInt("cashBack", 0);
                int cl = st.getInt("creditLimit", 0);
                int cbv = st.getInt("totalBorrowed", 0);
                int fcbv = cl - cbv;
                Double doPay = (searchPrice * .2);
                //                if((searchPrice<=1000)&&(searchPrice>150))
                //                    doPay=0.0;

                dopay2 = doPay.intValue();
                //                if((searchPrice<=1000)&&(searchPrice>150))
                //                    dopay2=0;
                if (fcbv <= 0) {
                    dummyCl = 1000;
                    fcbv = 100000000;
                }

                if (searchPrice - dopay2 > fcbv) {
                    dopay2 = searchPrice - fcbv;
                }
                //                if(searchPrice-dopay2>fcbv)
                //                {
                //                    dopay2=searchPrice
                //                }

                show();
            }
        }
    }

    public void parse(String parseString) {
        productId1 = "";
        SharedPreferences cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = cred.edit();
        et.putString("urlprod", parseString);
        et.commit();
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
                for (int j = pos + 1; j < parseString.length(); j++) {
                    if (((parseString.charAt(j)) >= '0') && (parseString.charAt(j) <= '9'))

                        productId1 += parseString.charAt(j);
                    else break;


                }
                //                if(((parseString.charAt(j))>='0')&&(parseString.charAt(j)<='9'))
                //
                //                    productId1 += parseString.charAt(j);
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

        } else if (parseString.contains("shopclues")) {
            sellerNme1 = "shopclues";
            checkValidFromApis = 1;
        } else
            checkValidUrl = 1;

        if ((checkValidFromApis == 0) && (checkValidUrl == 0)) {
            {
                page = "api";
                //
                //                Long time = Calendar.getInstance().getTimeInMillis() / 1000;
                Intent in = new Intent(ProductsPage.this, ProductsPage.class);
                in.putExtra("seller", sellerNme1);
                in.putExtra("product", productId1);
                in.putExtra("query", parseString);
                //                query.setText("");
                finish();
                in.putExtra("page", "api");
                checkValidFromApis = 0;
                checkValidUrl = 0;
                startActivity(in);
            }


            //make api call
        }
        if ((checkValidFromApis == 1)) {
            //not monley page
            Intent in = new Intent(ProductsPage.this, ProductsPage.class);
            //query.setText("");
            in.putExtra("seller", sellerNme1);
            in.putExtra("page", "pay");
            startActivity(in);
            finish();
            page = "pay";
            //            paytmUrl();
        }
        if (checkValidUrl == 1) {
            //monkey page
            Intent in = new Intent(ProductsPage.this, ProductsPage.class);
            //            query.setText("");
            finish();
            in.putExtra("page", "monkey");
            startActivity(in);
            //finish();
            page = "monkey";
            //            wrongUrl();//
        }
        //        Toast.makeText(HomePage.this, productId, Toast.LENGTH_SHORT).show();

        sellerNme = sellerNme1;
    }

    public void wrongUrl() {

        setContentView(R.layout.wrongurl);
        queryNew = (EditText) findViewById(R.id.query);
        queryNew.setImeOptions(EditorInfo.IME_ACTION_DONE);
        queryNew.setInputType(InputType.TYPE_NULL);
        queryNew.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Intent in = new Intent(ProductsPage.this, FindProduct.class);
                    startActivity(in);
                }
                return false;
            }
        });
        //pasteiconnew=(ImageView)findViewById(R.id.pasteAg);
        TextView t = (TextView) findViewById(R.id.textattach);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intercom.client().displayMessageComposer();
                } catch (Exception e) {

                }
            }
        });
        myClipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        //        queryNew.setOnTouchListener(new View.OnTouchListener() {
        //            @Override
        //            public boolean onTouch(View v, MotionEvent event) {
        //                pasteiconnew.setVisibility(View.VISIBLE);
        //                clickpaste();
        //
        //                return false;
        //            }
        //        });
        clickUrl();
        backpress();
    }

    public void paytmUrl() {
        setContentView(R.layout.paytmjab);
        String seller = getIntent().getExtras().getString("seller");
        //        String seller = getIntent().getExtras().getString("seller");
        ImageView logo = (ImageView) findViewById(R.id.logod);
        if ("paytm".equals(seller)) {
            logo.setImageResource(R.drawable.paytm);
        }
        if ("jabong".equals(seller)) {

            logo.setImageResource(R.drawable.jabong);
        }
        if ("myntra".equals(seller)) {
            logo.setImageResource(R.drawable.myntra);
        }
        if ("shopclues".equals(seller)) {
            logo.setImageResource(R.drawable.shopclues);
        }

        if ("flipkart".equals(seller)) {
            logo.setImageResource(R.drawable.flipart);
        }

        if ("amazon".equals(seller)) {
            logo.setImageResource(R.drawable.amazon);
        }

        if ("snapdeal".equals(seller)) {
            logo.setImageResource(R.drawable.snapdeal);
        }


        queryNew = (EditText) findViewById(R.id.query);
        pasteiconnew = (ImageView) findViewById(R.id.pasteAg);
        queryNew.setImeOptions(EditorInfo.IME_ACTION_DONE);
        queryNew.setInputType(InputType.TYPE_NULL);
        queryNew.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Intent in = new Intent(ProductsPage.this, FindProduct.class);
                    startActivity(in);
                }
                return false;
            }
        });
        myClipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        //        queryNew.setOnTouchListener(new View.OnTouchListener() {
        //            @Override
        //            public boolean onTouch(View v, MotionEvent event) {
        //                pasteiconnew.setVisibility(View.VISIBLE);
        //                clickpaste();
        //
        //                return false;
        //            }
        //        });

        clickUrl();
        final EditText editQ = (EditText) findViewById(R.id.editQ);
        TextView aval = (TextView) findViewById(R.id.avlbalRs);
        TextView enter = (TextView) findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    hideSoftKeyboard(ProductsPage.this);
                } catch (Exception e) {

                }
                String minP = editQ.getText().toString();
                if ((editQ.getText().toString().length() != 0) && (Integer.parseInt(minP) >= 50)) {
                    final ScrollView removescrol = (ScrollView) findViewById(R.id.removescroll);
                    removescrol.setVisibility(View.GONE);
                    searchTitle = "";
                    searchBrand = "";
                    sellerNme = getIntent().getExtras().getString("seller");
                    sellerNme1 = sellerNme;
                    searchCategory = "";
                    searchSubcategory = "";
                    searchPrice = Integer.parseInt(editQ.getText().toString());
                    sellingPrice = searchPrice;
                    ImageView logo = (ImageView) findViewById(R.id.logo);
                    if ("paytm".equals(sellerNme1)) {
                        logo.setImageResource(R.drawable.paytm);
                    }
                    if ("jabong".equals(sellerNme1)) {

                        logo.setImageResource(R.drawable.jabong);
                    }
                    if ("myntra".equals(sellerNme1)) {
                        logo.setImageResource(R.drawable.myntra);
                    }
                    if ("shopclues".equals(sellerNme1)) {
                        logo.setImageResource(R.drawable.shopclues);
                    }
                    //                JSONObject img = new JSONObject(data1.getString("imgUrls"));
                    //                urlImg = img.getString("400x400");
                    urlforImage = "";
                    brand = searchBrand;
                    try {
                        specification = "";
                    } catch (Exception e) {
                        specification = "";
                    }
                    try {
                        description = "";
                    } catch (Exception e) {
                        description = "";
                    }
                    try {
                        review = "";
                    } catch (Exception e) {
                        review = "";
                    }
                    infor = "The minimum downpayment is 20% of the product price and also depends on the payment band (Oxygen/Silicon/Palladium/Krypton) you lie in, which you will get to know after your college ID verification.";
                    try {
                        loader = (GIFView) findViewById(R.id.loading);
                        viewDetail = (ScrollView) findViewById(R.id.viewDetail);
                        loader.setVisibility(View.GONE);
                        viewDetail.setVisibility(View.VISIBLE);
                        ImageView arrow = (ImageView) findViewById(R.id.editprice);
                        arrow.setVisibility(View.VISIBLE);
                        arrow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewDetail.setVisibility(View.GONE);
                                removescrol.setVisibility(View.VISIBLE);


                            }
                        });
                    } catch (Exception e) {
                    }
                    show();
                } else
                    editQ.setText("");
            }

        });

        ImageView img = (ImageView) findViewById(R.id.logo);
        String sta = st.getString("approvedBand", null);
        cb = st.getInt("cashBack", 0);
        int cl = st.getInt("creditLimit", 0);
        int cbv = st.getInt("totalBorrowed", 0);
        int fcbv = cl - cbv;
        if (fcbv <= 0) {
            dummyCl = 1000;
            fcbv = 100000000;
        }
        if (dummyCl == 1000)
            aval.setText(getApplicationContext().getString(R.string.Rs) + "0");
        else
            aval.setText(getApplicationContext().getString(R.string.Rs) + fcbv);
        backpress();
    }

    public void correctUrl() {


        queryNew = (EditText) findViewById(R.id.query);
        queryNew.setImeOptions(EditorInfo.IME_ACTION_DONE);
        queryNew.setInputType(InputType.TYPE_NULL);
        queryNew.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Intent in = new Intent(ProductsPage.this, FindProduct.class);
                    startActivity(in);
                }
                return false;
            }
        });
        backpress();
        myClipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        //        queryNew.setOnTouchListener(new View.OnTouchListener() {
        //            @Override
        //            public boolean onTouch(View v, MotionEvent event) {
        //                pasteiconnew.setVisibility(View.VISIBLE);
        //                clickpaste();
        //
        //                return false;
        //            }
        //        });
        clickUrl();
        inc = (TextView) findViewById(R.id.check);
        butcheck = (Button) findViewById(R.id.butcheck);
        try {
            //            monthsallowed = getIntent().getExtras().getInt("monthsallowed");
            //            title = getIntent().getExtras().getString("title");
            //            brand = getIntent().getExtras().getString("brand");
            //            emi = getIntent().getExtras().getDouble("emi");
            //            searchQuery = getIntent().getExtras().getString("query");
            //            sellerNme = getIntent().getExtras().getString("seller");
            //            sellingPrice = getIntent().getExtras().getInt("price");
            //            urlforImage = getIntent().getExtras().getString("image");

            spInc = searchPrice;
            spDec = searchPrice;
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
                Double v = (sellingPrice * .2);
                //                if((sellingPrice<=1000)&&(sellingPrice>150))
                //                    v=0.0;
                mValue = v.intValue();
                mValue2 = v.intValue();
                if (t.contains("No"))
                    t = "0";
                considerTen=Integer.parseInt(t);
                monthsnow = Integer.parseInt(t);
                cb = st.getInt("cashBack", 0);
                int cl = st.getInt("creditLimit", 0);
                int cbv = st.getInt("totalBorrowed", 0);
                int fcbv = cl - cbv;
                //                Double mind = sellingPrice * .2;
                if (fcbv <= 0) {
                    dummyCl = 1000;
                    fcbv = 100000000;
                }
                if (sellingPrice - mValue > fcbv)
                    mValue = sellingPrice - fcbv;

                int service = serviceCharge(searchPrice, sellingPrice - mValue, sellerNme1);
                if (t.contains("0")) {

                    if ((sellerNme1.equals("flipkart")) || (sellerNme1.equals("snapdeal")))


                        mValue = sellingPrice + 29;
                    else {
                        int serv = 0;
                        {
                            if (searchPrice < 1000)
                                serv = 29;
                            else if (searchPrice < 5000)
                                serv = 99;
                            else if (searchPrice < 10000)
                                serv = 199;
                            else if (searchPrice < 15000)
                                serv = 299;
                            else if (searchPrice < 25000)
                                serv = 449;
                            else if (searchPrice > 25000)
                                serv = 599;
                        }
                        mValue = sellingPrice + serv;
                    }
                    minDownpayment = mValue;
                } else {
                    if (mDis == 0)
                        minDownpayment = globalMindown;
                    mValue = minDownpayment;
                }
                if ((mDis != 0) && (!t.contains("0"))) {
                    Double mind = 0.0;
                    if (searchPrice >= 1000)
                        mind = sellingPrice * .2;
                    else if ((searchPrice <= 1000) && (searchPrice > 150))
                        mind = sellingPrice * .2;

                    if (sellingPrice - mind > fcbv) {
                        mind = Double.valueOf(sellingPrice) - fcbv;
                    }
                    if(isZeroDpApplicable&&fcbv>=sellingPrice&&searchPrice<=1000)
                    {mind=0.0;}
                    if (dummyCl == 1000)
                        availbal.setText(getApplicationContext().getString(R.string.Rs) + "0");
                    else
                        availbal.setText(getApplicationContext().getString(R.string.Rs) + fcbv);
                    int w = serviceCharge(searchPrice, sellingPrice - mind.intValue(), sellerNme1);
                    firstServicecharge = w;
                    secondServicecharge = serviceCharge(searchPrice, sellingPrice - mind.intValue() - w, sellerNme1);
                    if(isZeroDpApplicable&&fcbv>=sellingPrice&&searchPrice<=1000&&mind==0.0)
minDownpayment=0;
                    else
                    minDownpayment = mind.intValue() + w;
                    mValue = minDownpayment;
                }
                dValue.setText("");
                dValue.setText(String.valueOf(mValue));
                if (sellingPrice != searchPrice) {
                    sellingRschange.setVisibility(View.VISIBLE);
                    sellingRschange.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(searchPrice)));
                    sellingRschange.setPaintFlags(sellingRschange.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    sellingRs.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(sellingPrice)));
                } else {
                    sellingRschange.setVisibility(View.GONE);
                    sellingRs.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(sellingPrice)));
                }
                //                sellingRs.setText(getApplicationContext().getString(R.string.Rs)+String.valueOf(Math.round(sellingPrice)));
                int w = serviceCharge(searchPrice, searchPrice - mValue, sellerNme1);
                firstServicecharge = w;
                secondServicecharge = serviceCharge(searchPrice, sellingPrice - mValue, sellerNme1);
                EMIcheck = (Math.round(calculateEmi(sellingPrice - mValue * 1.0 + secondServicecharge, Double.valueOf(searchPrice), monthsnow)));
                emiAmount.setText(getApplicationContext().getString(R.string.Rs) + String.valueOf(Math.round(calculateEmi(sellingPrice - mValue * 1.0 + secondServicecharge, Double.valueOf(searchPrice), monthsnow))) + " per month");

                //                    Toast.makeText(ProductsPage.this, selectedText, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements
        int arrmont[] = new int[8];
        int p = -1;
        List<String> categories = new ArrayList<String>();
        for (int j = 0; j < 8; j++) {
            if ((myMonths[j] <= monthsallowed)&&(monthsallowed!=0)) {

                p = j;
                //categories.add(String.valueOf(myMonths[j]) + " months");
            }
        }
        int cl = st.getInt("creditLimit", 0);
        int cbv = st.getInt("totalBorrowed", 0);
        int fcbv = cl - cbv;
        if (fcbv <= 0) {
            dummyCl = 1000;
            fcbv = 100000000;
        }
        for (int w = p; w >= 0; w--) {
            categories.add(String.valueOf(myMonths[w]) + " months");
        }
        if ((searchPrice <= 150) || (dummyCl == 1000 && Constants.STATUS.APPROVED.toString().equals(userProfileStatus))) {
            categories.clear();
            categories.add("No Financing");
        } else

        {
            categories.add("No Financing");
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
                        checkLongpress = 1;
                        mAutoDecrement = true;
                        rep.post(new RptUpdater());
                        return false;
                    }
                }
        );
        minusR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkLongpress = 0;
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

                        checkLongpress = 1;
                        mAutoIncrement = true;
                        rep.post(new RptUpdater());
                        return false;
                    }
                }
        );
        plusR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkLongpress = 0;
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

    public void show() {
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
                checkDiffernece = milli - currentMilli;
                checkDiffernece = TimeUnit.DAYS.convert(checkDiffernece, TimeUnit.MILLISECONDS);
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
                    else if (diff.intValue() == 3)
                        months = 3;
                    else if (diff.intValue() == 4)
                        months = 4;
                    else if (diff.intValue() == 5)
                        months = 5;
                     else if (diff.intValue() >= 6 && diff.intValue() <= 8) {
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
        if ((monthsallowed > monthscheck) && (monthscheck != 0))
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

            emi = Math.ceil((searchPrice * 0.8 * rate * Math.pow(1 + rate, monthsallowed - 1) * (1 + rate * d * 12 / 365)) / (Math.pow(1 + rate, monthsallowed) - 1));
        }
        Intent in = new Intent(ProductsPage.this, ProductsPage.class);
        in.putExtra("title", searchTitle);
        try {
            Map userMap = new HashMap<>();
            userMap.put("PRODUCT_TITLE", searchTitle);
            //                    userMap.put("email", mEmail);
            //                    userMap.put("user_id", mPhone);
            //                    userMap.put("phone", mPhone);
            //                    System.out.println("Intercom data 4" + mPhone);
            Intercom.client().updateUser(userMap);
        } catch (Exception e) {
            System.out.println("Intercom two" + e.toString());
        }
        correctUrl();
        queryNew.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    //                        Intent in = new Intent(HomePage.this, ViewForm.class);

                    // paste = (TextView) findViewById(R.id.pasteAg);
                    queryNew.requestFocus();
                    //                    clickpaste();
                    parse(queryNew.getText().toString().trim());

                }
                return false;
            }
        });
        //                    in.putExtra("price", searchPrice);
        //                    in.putExtra("brand", searchBrand);
        //                    in.putExtra("name", name);
        //                    in.putExtra("image", urlImg);
        //                    in.putExtra("emi", emi);
        //                    in.putExtra("tot", totalLoan.getText().toString());
        //                    in.putExtra("monthsallowed", monthsallowed);
        //                    in.putExtra("seller", sellerNme1);
        //                    in.putExtra("query", queryNew.getText().toString());
        //                    in.putExtra("page", page);
        //
        //                    startActivity(in);

    }

    public int months(String subcat, String cat, String brand, int price)

    {
        if(AppUtils.isEmpty(subcat))
            subcat="";
        if(AppUtils.isEmpty(cat))
            cat="";
        if(AppUtils.isEmpty(brand))
            brand="";

        int m = 18;
        try
        {

            if(!AppUtils.isEmpty(cat)){
            int mn = categoryForproducts.get(cat);
            if (mn < m)
                m = mn;}
        }
        catch (Exception e)
        {}
        try{
        if(!AppUtils.isEmpty(subcat)) {
            int msc = subcategoryForproducts.get(subcat);
            if (msc < m)
                m = msc;

        } }
        catch (Exception e)
        {}
        try{
        if(!AppUtils.isEmpty(brand)) {
            int msc = brandApple.get(brand);
            if (msc < m)
                m = msc;

        }    }
        catch (Exception e)
        {}
        Collections.sort(pricesFormonths);
        for(int i=0;i<pricesFormonths.size();i++)
        {
            int pricenew=pricesFormonths.get(i);
            if(price<=pricenew)
            {
                int w=priceMonths.get(pricesFormonths.get(i).toString());
                if(w<m)
                    m=w;
                break;

            }

        }
        cb = st.getInt("cashBack", 0);

        int cl = st.getInt("creditLimit", 0);
        int cbv = st.getInt("totalBorrowed", 0);
        int fcbv = cl - cbv;

        int loanAmt=0;
        int newdp= (int) Math.round(.2*price);
        if (price - newdp > fcbv)

            newdp = price - fcbv;
        if(isZeroDpApplicable&&fcbv>=price&&price<=1000)
            newdp=0;
        int checkLoanamt=price-newdp;
        int dd=0;
        dd = serviceCharge(searchPrice, checkLoanamt, sellerNme1);

       Double chh=Math.floor((
               checkLoanamt-dd)/(200));
        int kk=chh.intValue();
        if(kk<m)
            m=kk;


//        int checkbalmon=200*/m;



        return m;
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("order"))
                finish();

        }
    };

    @Override
    public void onBackPressed() {
        try {
            if (popup.isShowing()) {
                popup.dismiss();
                RelativeLayout cover = (RelativeLayout) findViewById(R.id.cover);
                //                prod.setTi(Color.parseColor("#CC000000"));
                checkout.setEnabled(true);
                cover.setVisibility(View.GONE);
            } else {
                finish();
            }
        } catch (Exception e) {
            finish();
        }
    }

    public void finish() {
        super.finish();
    }

    ;

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    public void clickpaste() {
        pasteiconnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    queryNew.requestFocus();
                    queryNew.setText("");
                    ClipData abc = myClipboard.getPrimaryClip();
                    ClipData.Item item = abc.getItemAt(0);
                    String text = item.getText().toString();


                    queryNew.setText("   " + text);

                    pasteiconnew.setVisibility(View.GONE);

                } catch (Exception e) {
                    Toast.makeText(ProductsPage.this, "Please copy a URL", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    int serviceCharge(int sellingCost, int loanAmt, String seller) {
        int serv = 0;
        int checklorsp=0;
        if("loanAmount".equals(sercieChargeApplicableon))
            checklorsp=loanAmt;
        else
            checklorsp=sellingCost;
        Set<Integer> keys=serviceChargesmapping.keySet();
        ArrayList<Integer> checkKe=new ArrayList<Integer>();
        for(int l:keys)
        {
            checkKe.add(l);


        }
        Collections.sort(checkKe);
        //Collections.sort(keys);
        for(int w=0;w<checkKe.size();w++)
        {

            if(checklorsp<=checkKe.get(w))
            {
                serv=serviceChargesmapping.get(checkKe.get(w));
                break;
            }



        }
//        if("loanAmount".equals(sercieChargeApplicableon))


        return serv;

    }


    public class DeriveMapping extends
            AsyncTask<String, Void, String> {
        @Override
        public void onPreExecute() {
            categoryForproducts=new HashMap<String, Integer>();
            subcategoryForproducts=new HashMap<String, Integer>();
            brandApple=new HashMap<String, Integer>();
            priceMonths=new HashMap<String, Integer>();
            pricesFormonths=new ArrayList<Integer>();
            serviceChargesmapping=new HashMap<Integer, Integer>();


        }


        @Override
        public String doInBackground(String... data) {

            //  String urldisplay = data[0];
            //               HashMap<String, String> details = data[0];
            // JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: get
                HttpResponse response = null;
                String responseString = "";
                String url = BuildConfig.SERVER_URL + "api/productpage/variables?seller="+getIntent().getExtras().getString("seller")+"&userId=" + creduserid;

                //                String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjY1M2M2YTUwZTQzNzgyNjc0M2YyNjYiLCJuYW1lIjoiYnVkZHkgYXBpIGFkbWluIiwidXNlcm5hbWUiOiJidWRkeWFwaWFkbWluIiwicGFzc3dvcmQiOiJtZW1vbmdvc2gxIiwiZW1haWwiOiJjYXJlQGhlbGxvYnVkZHkuaW4iLCJpYXQiOjE0NTY3MjY1NzMsImV4cCI6MTQ1Njc2MjU3M30.98mQFcYm5Uf3Fd7ZNPD-OwMIfObu7vfoq9zNtCCLfyI";
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);


                HttpClient client = new DefaultHttpClient(httpParameters);



                    HttpGet httppost = new HttpGet(url);
                    httppost.setHeader("x-access-token", st.getString("token_value", null));
                    httppost.setHeader("Content-Type", "application/json");


                    response = client.execute(httppost);
                    HttpEntity ent = response.getEntity();
                    responseString = EntityUtils.toString(ent, "UTF-8");

                if (response.getStatusLine().getStatusCode() != 200) {
                    return "fail";
                } else {
                     JSONObject resp = new JSONObject(responseString);
                    if (resp.getString("status").equals("success")) {
                        String datacat=resp.getString("data");
parseJsonforcategory(datacat);
                        return "win";


                    }

                }
            } catch (Exception e) {
                String t=e.toString();
                int www=11;
            }
            return "";
        }
        protected void onPostExecute(String result) {



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
                setContentView(R.layout.activity_products_page);
                flipcash=(TextView)findViewById(R.id.flipcash);
                loader = (GIFView) findViewById(R.id.loading);
                viewDetail = (ScrollView) findViewById(R.id.viewDetail);

                try {
                    SharedPreferences user = getSharedPreferences("token", Context.MODE_PRIVATE);

                    userCode1k = user.getString("status1K", "");
                    userCode7k = user.getString("status7K", "");
                    userProfileStatus = user.getString("profileStatus", "");
                    productId1 = getIntent().getExtras().getString("product");
                    sellerNme1 = getIntent().getExtras().getString("seller");
                    sellerNme = sellerNme1;
                    try {
                        loader = (GIFView) findViewById(R.id.loading);
                        viewDetail = (ScrollView) findViewById(R.id.viewDetail);
                    } catch (Exception e) {
                    }
                    new linkSearch().execute();
                } catch (Exception e) {
                    String t = e.toString();
                }
                //            correctUrl();
                //            queryNew.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                //                @Override
                //                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                //
                ////                        Intent in = new Intent(HomePage.this, ViewForm.class);
                //
                //                        // paste = (TextView) findViewById(R.id.pasteAg);
                //                        queryNew.requestFocus();
                //                        //clickpaste();
                //                        parse(queryNew.getText().toString().trim());
                //
                //                    }
                //                    return false;
                //                }
                //            });
            }
        }}
public void parseJsonforcategory(String data)
{try{

    JSONObject dataCategories=new JSONObject(data);
    try {
        isZeroDpApplicable = dataCategories.getBoolean("isZeroDpApplicable");
    }
    catch (Exception e)
    {}
    JSONObject noint=new JSONObject(dataCategories.getString("noInterest"));
    try {
        noInterestAppon = noint.getString("applicableOn");
        nointerestSp = noint.getInt("sellingPrice");
        nointerestEmiten = noint.getInt("emiTenure");
    }
    catch (Exception e)
    {}
    interestRate=dataCategories.getInt("interestRate");
    noOfDays=dataCategories.getInt("noOfDays");

        JSONObject categoriesdata=new JSONObject(dataCategories.getString("category"));
//    JSONObject servicechargeapplicableon=new JSONObject(dataCategories.getString("affiliate"));
    try {
        sercieChargeApplicableon = dataCategories.getString("serviceChargeApplicableOn");
    }
    catch (Exception e){}
    int lenghtcategory=categoriesdata.length();
    JSONArray scharge=(dataCategories.getJSONArray("serviceCharges"));
    for(int i=0;i<scharge.length();i++)

    {
        JSONObject scc=scharge.getJSONObject(i);
        if("loanAmount".equals(sercieChargeApplicableon))
        {
            serviceChargesmapping.put(scc.getInt("loanAmount"),scc.getInt("serviceCharge"));
        }
        else
        {
            serviceChargesmapping.put(scc.getInt("sellingPrice"),scc.getInt("serviceCharge"));
        }

    }
    Iterator iterator = categoriesdata.keys();
    while(iterator.hasNext()){
        String key = (String)iterator.next();
        categoryForproducts.put(key,categoriesdata.getInt(key));
    }
    JSONObject subcategoriesdata=new JSONObject(dataCategories.getString("subCategory"));
//    int lenghtcategory=categoriesdata.length();
    Iterator iterator1 = subcategoriesdata.keys();
    while(iterator1.hasNext()){
        String key = (String)iterator1.next();
        subcategoryForproducts.put(key,subcategoriesdata.getInt(key));
    }
    JSONObject brandcat=new JSONObject(dataCategories.getString("brand"));
//    int lenghtcategory=categoriesdata.length();
    Iterator iterator2 = brandcat.keys();
    while(iterator2.hasNext()){
        String key = (String)iterator2.next();
        brandApple.put(key,brandcat.getInt(key));
    }
    JSONObject pricem=new JSONObject(dataCategories.getString("price"));
//    int lenghtcategory=categoriesdata.length();
    Iterator iteratorprice =pricem.keys();
    while(iteratorprice.hasNext()){
        String key = (String)iteratorprice.next();
pricesFormonths.add(Integer.parseInt(key));
        priceMonths.put(key,pricem.getInt(key));
    }
    Collections.sort(pricesFormonths);
}
catch (Exception e)
{}
}
}