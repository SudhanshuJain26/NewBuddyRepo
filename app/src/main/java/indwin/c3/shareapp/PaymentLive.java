package indwin.c3.shareapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import indwin.c3.shareapp.utils.AppUtils;

public class PaymentLive  extends Activity {
    private static final String LOG_TAG = "PaymentTestActivity";
    private String payname,payemail,payamt,paytitle,payphone;
    public static final String EXTRA_PARAMS = "params";
    private String randomStr="";
    private static final String MERCHANT_KEY = "n46r73";// old account "zMXH8C";
    private static final String SALT = "L3gouXYu";// "YBLKG80u";
    private static final String BASE_URL = "https://secure.payu.in";
    //private static final String BASE_URL = "https://test.payu.in";
    private static final String PAYMENT_URL = BASE_URL + "/_payment";
    private String orderI="";
    public static final String PARAM_KEY = "key";
    public static final String PARAM_TRANSACTION_ID = "txnid";
    public static final String PARAM_AMOUNT = "amount";
    public static final String PARAM_FIRST_NAME = "firstname";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_PHONE = "phone";
    public static final String PARAM_PRODUCT_INFO = "productinfo";
    public static final String PARAM_SUCCESS_URL = "surl";
    public static final String PARAM_FAILURE_URL = "furl";
    public static final String PARAM_SERVICE_PROVIDER = "service_provider";
    public static final String PARAM_HASH = "hash";
    public static final String PARAM_LAST_NAME = "lastname";
    public static final String PARAM_ADDRESS1 = "address1";
    public static final String PARAM_ADDRESS2 = "address2";
    public static final String PARAM_CITY = "city";
    public static final String PARAM_STATE = "state";
    public static final String PARAM_COUNTRY = "country";
    public static final String PARAM_ZIP_CODE = "zipcode";
    public static final String PARAM_UDF1 = "udf1";
    public static final String PARAM_UDF2 = "udf2";
    public static final String PARAM_UDF3 = "udf3";
    public static final String PARAM_UDF4 = "udf4";
    public static final String PARAM_UDF5 = "udf5";
    public static final String PARAM_PG = "pg";

    private static final String KEY_PAYU_PAISA = "payu_paisa";

    private WebView mWebView;
    private Context mContext;
    private HashMap<String, String> mInputParams;
    private String mHashValue;
    private String productId;
    ProgressDialog mProgressBar;
    private static final int DIALOG2_KEY = 1;
    String transactionId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mWebView = new WebView(mContext);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
                Window.PROGRESS_VISIBILITY_ON);

        // mProgressBar = new ProgressDialog(mContext);
        showDialog(DIALOG2_KEY);
//        getActionBar().setBackgroundDrawable(
//                new ColorDrawable(Color.parseColor("#0B2C41")));
        setContentView(mWebView);

        // using below to bypass payment in test env

        // String paymentId="123"; productId=
        // getIntent().getStringExtra("productId"); Intent intent1 = new
        // Intent(PaymentLiveActivity.this, OrderSuccessActivity.class);
        // intent1.putExtra("productId",productId);
        // intent1.putExtra("paymentId",paymentId); startActivity(intent1);
        // finish(); overridePendingTransition(R.anim.animation_left_to_right,
        // R.anim.animataion_right_to_left);
        mInputParams=new HashMap<String, String>();
//        mInputParams = (HashMap<String, String>) getIntent()
//                .getSerializableExtra(EXTRA_PARAMS);

//        if (mInputParams == null || mInputParams.isEmpty()) {
//            finish();
//        }

        if (TextUtils.isEmpty("")) {
            Random random = new Random();
            int d=random.nextInt(998)+1;
            String e="";
            if(d<10)
                e="00"+d;
            else
            if(d<100)
                e="0"+d;
            else
                e=String.valueOf(d);
            System.out.println(e);
            long t=System.currentTimeMillis();
                
            randomStr = String.valueOf(t)+e;

            transactionId = hashCal("SHA-256", randomStr).substring(0,
                    20);
            mInputParams.put(PARAM_TRANSACTION_ID,randomStr);
        }

        if (TextUtils.isEmpty(mInputParams.get(PARAM_KEY))) {
            mInputParams.put(PARAM_KEY, MERCHANT_KEY);
        }
        SharedPreferences get = getSharedPreferences("cred", Context.MODE_PRIVATE);
        payamt=String.valueOf(get.getInt("downpayment", 0)+get.getInt("service", 0));
        paytitle=get.getString("title", "");
        payname=get.getString("n1", "");
//        payname="jon sno";
        payemail=get.getString("e1", "");
        payphone=get.getString("phone_number", "");
        String hashSequence = "key|txnid|amount|productinfo|firstname|email";
        String hashString = ""  ;
        String[] hashKeys = hashSequence.split("\\|");
        for (String key : hashKeys) {
            hashString = hashString.concat(getNonNullValueFromHashMap(
                    mInputParams, key));

            hashString = hashString.concat("|");
        }
//        SharedPreferences get = getSharedPreferences("cred", Context.MODE_PRIVATE);
        String t=MERCHANT_KEY+"|"+randomStr+"|"+payamt+"|"+"Mobiles"+"|"+payname+"|"+payemail+"|||||||||||";
        hashString = hashString.concat(SALT);
        t=t.concat(SALT);
        mHashValue = hashCal("SHA-512", t);
        Log.v(LOG_TAG, "HASH: " + mHashValue);

        SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
        Long time= Calendar.getInstance().getTimeInMillis()/1000;
        if(time+5<userP.getLong("expires",0))
            new sendOtp().execute();
        else
            new AuthTokc().execute();

    }

    // @Override
    // protected Dialog onCreateDialog(int id) {
    // switch (id) {
    // case DIALOG2_KEY: {
    // mProgressBar.setMessage("Hipster Loading");
    // mProgressBar.setIndeterminate(true);
    // mProgressBar.setCancelable(false);
    // return mProgressBar;
    // }
    // }
    // return null;
    // }

    @SuppressLint("SetJavaScriptEnabled")
    private void configureWebView() {
        mWebView.setVisibility(View.VISIBLE);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setCacheMode(2);
        final Activity activity = this;

        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.clearHistory();
        mWebView.clearCache(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setUseWideViewPort(false);
        mWebView.getSettings().setLoadWithOverviewMode(false);
        mWebView.setWebViewClient(new MyWebChromeClient());
        // mWebView.addJavascriptInterface(new
        // PayUJavaScriptInterface(activity), "PayUMoney");
        // mWebView.setWebViewClient(new WebViewClient() {
        //
        // @Override
        // public void onReceivedError(WebView view, int errorCode,
        // String description, String failingUrl) {
        // showToast("Oh no! " + description);
        // }
        //
        // @Override
        // public void onReceivedSslError(WebView view,
        // SslErrorHandler handler, SslError error) {
        // showToast("SslError! " + error);
        // handler.proceed();
        // }
        //
        // @Override
        // public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // view.loadUrl(url);
        // return super.shouldOverrideUrlLoading(view, url);
        // }
        //
        // @Override
        // public void onPageFinished(WebView view, String url) {
        // dismissDialog(DIALOG2_KEY);
        // }
        //
        // @Override
        // public void onPageStarted(WebView view, String url, Bitmap favicon) {
        // // TODO Auto-generated method stub
        // super.onPageStarted(view, url, favicon);
        // if
        // (url.equals("https://www.payumoney.com/mobileapp/payumoney/success.php"))
        // {
        // /*
        // * Toast.makeText(getApplicationContext(),
        // * "Payment Success", Toast.LENGTH_LONG).show();
        // */
        //
        // } else if (url
        // .equals("https://www.payumoney.com/mobileapp/payumoney/failure.php"))
        // {
        // /*
        // * Toast.makeText(getApplicationContext(), "Payment Failed",
        // * Toast.LENGTH_LONG).show();
        // */}
        // String url_new = view.getUrl();
        //
        // Log.v("", "Webview Function URL: " + url_new);
        //
        // }
        //
        // });
        SharedPreferences get = getSharedPreferences("cred", Context.MODE_PRIVATE);

        HashMap<String, String> formParams = new HashMap<String, String>();
        formParams.put(PARAM_KEY, MERCHANT_KEY);
        formParams.put(PARAM_HASH, mHashValue);
        formParams.put(PARAM_TRANSACTION_ID,
                getNonNullValueFromHashMap(mInputParams,PARAM_TRANSACTION_ID));
        formParams.put(PARAM_AMOUNT, payamt);
        formParams.put(PARAM_FIRST_NAME,
                payname);
        formParams.put(PARAM_EMAIL,
                payemail);
        formParams.put(PARAM_PHONE,
                payphone);
        formParams.put(PARAM_PRODUCT_INFO,
                "Mobiles");
        formParams.put(PARAM_SUCCESS_URL,
                getApplicationContext().getString(R.string.server)+"payment/payu/success");
        formParams.put(PARAM_FAILURE_URL,
                getApplicationContext().getString(R.string.server)+"payment/payu/failure");
        formParams.put(PARAM_LAST_NAME,
                getNonNullValueFromHashMap(mInputParams, PARAM_LAST_NAME));
        formParams.put(PARAM_ADDRESS1,
                getNonNullValueFromHashMap(mInputParams, PARAM_ADDRESS1));
        formParams.put(PARAM_ADDRESS2,
                getNonNullValueFromHashMap(mInputParams, PARAM_ADDRESS2));
        formParams.put(PARAM_CITY,
                getNonNullValueFromHashMap(mInputParams, PARAM_CITY));
        formParams.put(PARAM_STATE,
                getNonNullValueFromHashMap(mInputParams, PARAM_STATE));
        formParams.put(PARAM_COUNTRY,
                getNonNullValueFromHashMap(mInputParams, PARAM_COUNTRY));
        formParams.put(PARAM_ZIP_CODE,
                getNonNullValueFromHashMap(mInputParams, PARAM_ZIP_CODE));
        formParams.put(PARAM_UDF1,
                getNonNullValueFromHashMap(mInputParams, PARAM_UDF1));
        formParams.put(PARAM_UDF2,
                getNonNullValueFromHashMap(mInputParams, PARAM_UDF2));
        formParams.put(PARAM_UDF3,
                getNonNullValueFromHashMap(mInputParams, PARAM_UDF3));
        formParams.put(PARAM_UDF4,
                getNonNullValueFromHashMap(mInputParams, PARAM_UDF4));
        formParams.put(PARAM_UDF5,
                getNonNullValueFromHashMap(mInputParams, PARAM_UDF5));
        formParams.put(PARAM_PG,
                getNonNullValueFromHashMap(mInputParams, PARAM_PG));
        formParams.put(PARAM_SERVICE_PROVIDER, KEY_PAYU_PAISA);
        mWebView.addJavascriptInterface(new PayUJavaScriptInterface(),
                "PayUMoney");
        if (hasMandatoryParamsPresent(formParams)) {
            runPOSTFromWebView(mWebView, PAYMENT_URL, formParams.entrySet());
        } else {
            showToast("Mandatory parameter(s) missing.");
        }
    }

    private void showToast(String message) {
//        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public boolean hasMandatoryParamsPresent(HashMap<String, String> paramsHash) {
        return !(TextUtils.isEmpty(paramsHash.get(PARAM_KEY))
                || TextUtils.isEmpty(paramsHash.get(PARAM_TRANSACTION_ID))
                || TextUtils.isEmpty(paramsHash.get(PARAM_AMOUNT))
                || TextUtils.isEmpty(paramsHash.get(PARAM_PRODUCT_INFO))
                || TextUtils.isEmpty(paramsHash.get(PARAM_FIRST_NAME))
                || TextUtils.isEmpty(paramsHash.get(PARAM_EMAIL))
                || TextUtils.isEmpty(paramsHash.get(PARAM_PHONE))
                || TextUtils.isEmpty(paramsHash.get(PARAM_SERVICE_PROVIDER))
                || TextUtils.isEmpty(paramsHash.get(PARAM_FAILURE_URL)) || TextUtils
                .isEmpty(paramsHash.get(PARAM_SUCCESS_URL)));
    }

    public String getNonNullValueFromHashMap(
            HashMap<String, String> paramsHash, String key) {
        String value = paramsHash.get(key);
        return (value == null) ? "" : value;
    }

    public void runPOSTFromWebView(WebView webView, String url,
                                   Collection<Map.Entry<String, String>> postData) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head></head>");
        sb.append("<body onload='payment_form.submit()'>");
        sb.append(String
                .format("<form id='payment_form' action='%s' method='%s'>",
                        url, "post"));
        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format(
                    "<input name='%s' type='hidden' value='%s' />",
                    item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");

        Log.d(LOG_TAG, "runPOSTFromWebView called");

        webView.loadData(sb.toString(), "text/html", "utf-8");
    }

    public String hashCal(String algo, String input) {
        byte[] hashseq = input.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(algo);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();

            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1)
                    hexString.append("0");
                hexString.append(hex);
            }

        } catch (NoSuchAlgorithmException nsae) {
            Log.e(LOG_TAG, "Invalid algorithm " + algo);
        }

        return hexString.toString();
    }

    private final class PayUJavaScriptInterface {
        PayUJavaScriptInterface() {
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void success(long id, final String paymentId) {
            // confirm order call
            // new confirmorder();
//            Toast.makeText(PaymentLive.this, "Cool", Toast.LENGTH_SHORT).show();
//            productId = getIntent().getStringExtra("productId");
//            if (getIntent().getStringExtra("sender").equals("lazyadapter")) {
//                Toast.makeText(
//                        PaymentLive.this,
//                        "Your payment has been received. Please check your mail for confirmation",
//                        Toast.LENGTH_LONG).show();
////                String url = getApplicationContext().getString(R.string.server_url);
////                String myUrl = url+ApiUrlBuilder.PAY_NOW + "?orderid="
////                        + getIntent().getStringExtra("orderid")
////                        + "&payu_id=" + paymentId;
//                String myUrl="";
//                new payNow().execute(myUrl);
////                Intent intent1 = new Intent(PaymentLiveActivity.this,
////                        LandingActivity.class);
////                startActivity(intent1);
//            } else {
////                Intent intent1 = new Intent( uPaymentLiveActivity.this,
////                        OrderSuccessActivity.class);
////                intent1.putExtra("productId", productId);
////                intent1.putExtra("paymentId", paymentId);
////                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
////                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                startActivity(intent1);
//            }
//            finish();
//            overridePendingTransition(R.anim.animation_left_to_right,
//                    R.anim.animataion_right_to_left);

        }

        @JavascriptInterface
        public void failure(final String id, String error) {
            Toast.makeText(
                    getApplicationContext(),
                    "Payment Failed. Try again or contact us if the problem persists. "
                            + id + " " + error, Toast.LENGTH_LONG).show();

            finish();

        }

        @JavascriptInterface
        public void failure() {
            Toast.makeText(
                    getApplicationContext(),
                    "Payment Failed. Try again or contact us if the problem persists. ",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        @JavascriptInterface
        public void failure(final String params) {
            Toast.makeText(
                    getApplicationContext(),
                    "Payment failed. Try again or contact us if the problem persists. "
                            + params, Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private final class MyWebChromeClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            // dismissDialog(DIALOG2_KEY);
            int check=0;
            Intent in=new Intent(PaymentLive.this,Ordersuccessfailure.class);
//            Toast.makeText(PaymentLive.this, url, Toast.LENGTH_SHORT).show();
            if(url.contains("ordersuccess")) {
//                check++;
//                if(check==1)
                {
                    try{
                        String orderid=url.substring(url.indexOf('=')+1,url.indexOf('&'));
                        //ordee
                        orderI=orderid;
                        new ValidateForm().execute();

                    }
                    catch (Exception e)
                    {
                        String orderid=url.substring(url.indexOf('=')+1);
                        //
                        orderI=orderid;
                        new ValidateForm().execute();
//                        in.putExtra("orderId",orderI);
//                        startActivity(in);
//                        finish();
                    }}

            }
            else
            if(url.contains("orderfailure")) {
                check++;
//                    String orderid=url.substring(url.indexOf('=')+1,url.indexOf('&'));
                // Toast.makeText(PaymentLive.this, s, Toast.LENGTH_SHORT).show();
                if(check==1)
                {
                    in.putExtra("orderId","fail");
                    startActivity(in);
                    finish();}

            }


        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("file")) {
                return false;
            } else {
                view.loadUrl(url);
                return true;
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            showToast("Oh no! " + description);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            showToast("SslError! " + error);
            handler.proceed();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            if (url.equals("https://www.payumoney.com/mobileapp/payumoney/success.php")) {
				/*
				 * Toast.makeText(getApplicationContext(), "Payment Success",
				 * Toast.LENGTH_LONG).show();
				 */

            } else if (url
                    .equals("https://www.payumoney.com/mobileapp/payumoney/failure.php")) {
				/*
				 * Toast.makeText(getApplicationContext(), "Payment Failed",
				 * Toast.LENGTH_LONG).show();
				 */}
            String url_new = view.getUrl();

            Log.v("", "Webview Function URL: " + url_new);

        }

    }

    public class payNow extends AsyncTask<String, Integer, String> {
        String response;

        @Override
        protected String doInBackground(String... url) {
            // TODO Auto-generated method stub
            try {
                Log.d("Here", "hereeeee");
                // http client
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpEntity httpEntity = null;
                HttpResponse httpResponse = null;

                // Checking http request method type
                HttpPost httpPost = new HttpPost(url[0]);
                httpPost.setHeader("Content-Type", "application/json");
                Log.d("httpPost", httpPost.toString());
                httpResponse = httpClient.execute(httpPost);

                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

    }
    public  class AuthTokc extends
            AsyncTask<String, Void, String> {

        private String apiN="";
        //        Context context;
//        AuthTok(Context context) {
//            this.context = context;
//        }
        //    Splash obj=new Splash();
        @Override
        protected String doInBackground(String... params) {
            JSONObject payload = new JSONObject();
//            String urldisplay = params[0];
//            apiN=urldisplay;
            try {

                // userid=12&productid=23&action=add
                // TYPE: POST
                //      payload.put("userid", details.get("userid"));
                // payload.put("productid", details.get("productid"));
                // payload.put("action", details.get("action"));


                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                String urll=getApplicationContext().getString(R.string.server) + "authenticate";
                HttpPost httppost = new HttpPost(urll);
                httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");

                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");
                if (response.getStatusLine().getStatusCode() != 200) {

                    Log.e("MeshCommunication", "Server returned code "
                            + response.getStatusLine().getStatusCode());
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);

                    if (resp.getString("status").contains("fail")) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {
                        String token1="";

                        SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorP = userP.edit();
                        token1 = resp.getString("token");
                        editorP.putString("token_value",token1);
                        editorP.putLong("expires", resp.getLong("expiresAt"));
                        editorP.commit();
                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror111", e.getMessage());
                return "fail";

            }
        }
        protected void onPostExecute(String result) {
            if(result.equals("win")){

                // Toast.makeText(Inviteform.this, "yey", Toast.LENGTH_SHORT).show();
//            next.new FacebookAuth.fblogin().execute();
                // new fblogin().execute();
                new sendOtp().execute();
//            next.fblogin().execute();



            }}}
    private class sendOtp extends
            AsyncTask<String, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            spinner.setVisibility(View.VISIBLE);
//        }

        @Override
        protected String doInBackground(String... data) {

            // String urldisplay = data[0];
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                SharedPreferences cred = getSharedPreferences("cred", Context.MODE_PRIVATE);

                // payload.put("action", details.get("action"));
                payload.put("userId",cred.getString("phone_number",""));
                payload.put("orderStatus","pendingPayment");
                payload.put("fkProductId",cred.getString("prid", ""));
                payload.put("productUrl", cred.getString("urlprod",""));
                payload.put("seller",cred.getString("seller", ""));
                payload.put("sellingPrice",cred.getInt("sp", 0));
                payload.put("downPayment",payamt);
                int loanamt=cred.getInt("emi",0)*cred.getInt("monthtenure",0);
                payload.put("loanAmount",loanamt);
                if(cred.getInt("emi",0)==0)
                    payload.put("emiTenure",0);
                else
                payload.put("emiTenure",cred.getInt("monthtenure",0));
                payload.put("emi",cred.getInt("emi",0));
                payload.put("interestPayable",cred.getInt("interEst",0));
                payload.put("discount",cred.getInt("discount",0));
                payload.put("creditsUsed",0);
                payload.put("deliveryFee",0);
                payload.put("txnId",getNonNullValueFromHashMap(mInputParams,PARAM_TRANSACTION_ID));
                payload.put("couponCode",cred.getString("whichCoupon", ""));
                payload.put("deliveryAddress",cred.getString("address",""));
                payload.put("userComments",cred.getString("usercom", ""));
                payload.put("serviceCharges",cred.getInt("service",0));
                payload.put("productFrom","android");
                payload.put("interestRate",21);
                payload.put("totalPayable" ,cred.getInt("emi",0)*cred.getInt("monthtenure",0)+cred.getInt("downpayment",0)+cred.getInt("service",0));
//
                if(cred.getInt("checkCashback",0)==1)
                    payload.put("isCashbackApplied" ,true);
//  payload.put("userid","7070362045");



                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                //api/login/sendotp
                String url2 = getApplicationContext().getString(R.string.server)+"api/order/initiate";
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
//                        truth=resp.getString("status");
                        return resp.getString("status");

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {
//            Toast.makeText(PaymentLive.this, transactionId, Toast.LENGTH_SHORT).show();

            if(!result.equals("fail"))
            {
//                new ValidateForm().execute();
                configureWebView();
            }
            else
                finish();

        }}

    private class ValidateForm extends
            AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... data) {

            // String urldisplay = data[0];
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST

                // payload.put("action", details.get("action"));

                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                SharedPreferences ph = getSharedPreferences("cred", Context.MODE_PRIVATE);
                String userId = ph.getString("phone_number", "");
                String url2 = getApplicationContext().getString(R.string.server) + "api/user/form?phone=" + userId;

                HttpResponse response = AppUtils.connectToServerGet(url2, tok_sp, null);
                if (response != null) {
                    HttpEntity ent = response.getEntity();
                    String responseString = EntityUtils.toString(ent, "UTF-8");

                    if (response.getStatusLine().getStatusCode() != 200) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {
                        JSONObject resp = new JSONObject(responseString);
                        JSONObject data1 = new JSONObject(resp.getString("data"));

                        try {

                            String fbid="";
                            try {
                                fbid = data1.getString("fbConnected");
                            } catch (Exception e) {
                                fbid = "empty";
                            }
                            if (fbid.equals("") || (fbid.equals("false")))
                                fbid = "empty";
                            String formstatus="";
                            try {
                                formstatus = data1.getString("formStatus");
                            } catch (Exception e) {
                                formstatus = "empty";
                            }
                            int totalCashBack = 0;
                            try {
                                totalCashBack = data1.getInt("totalCashback");
                            } catch (Exception e) {
                                totalCashBack = 0;
                            }
                            String approvedBand="";
                            try {
                                approvedBand = data1.getString("approvedBand");
                            } catch (Exception e) {
                                approvedBand = "";
                            }
                            int creditLimit = 0;
                            try {
                                creditLimit = data1.getInt("creditLimit");
                            } catch (Exception e) {
                                creditLimit = 0;
                            }
                            int totalBorrowed = 0;
                            try {
                                totalBorrowed = data1.getInt("totalBorrowed");
                            } catch (Exception e) {
                                totalBorrowed = 0;
                            }
                            String nameadd = "";
                            String  courseend="";

                            try {
                                courseend = data1.getString("courseCompletionDate");
                            } catch (Exception e) {
                                courseend="";
                            }

                            try {
                                nameadd = data1.getString("college");
                            } catch (Exception e) {
                            }
                            String profileStatus = "";
                            try {
                                profileStatus = data1.getString("profileStatus");
                            } catch (Exception e) {
                                profileStatus = "";
                            }
                            SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorP = userP.edit();

                            editorP.putInt("creditLimit", creditLimit);

                            editorP.putInt("totalBorrowed", totalBorrowed);

                            editorP.putInt("cashBack", totalCashBack);


                            editorP.commit();


                            try {
                                String dpid = data1.getString("fbUserId");
                                SharedPreferences sf = getSharedPreferences("proid", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor2 = sf.edit();
                                editor2.putString("dpid", dpid);

                                //  editor2.putString("password", password.getText().toString());
                                editor2.commit();
                            } catch (Exception e) {
                            }

                        } catch (Exception e) {
                        }

                        if (resp.getString("msg").contains("error")) {

                            Log.e("MeshCommunication", "Server returned code "
                                    + response.getStatusLine().getStatusCode());
                            return resp.getString("msg");
                        } else {

                            return "win";

                        }

                    }
                } else {
                    return "fail";

                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {


            if (result.equals("win")) {
                Intent in=new Intent(PaymentLive.this,Ordersuccessfailure.class);
                in.putExtra("orderId",orderI);
                startActivity(in);
                finish();
            }}}

}
