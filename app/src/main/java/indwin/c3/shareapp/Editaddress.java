package indwin.c3.shareapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

public class Editaddress extends AppCompatActivity {
 ArrayList<Deladd> myList;
//    myList.add("Android");
//    myList.add("Android1");
int CheckSize=0;
private  ImageView loader;
    ListView listView;
//    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(broadcastReceiver, new IntentFilter("order"));
        setContentView(R.layout.activity_editaddress);

        loader=(ImageView)findViewById(R.id.loading);
        backpress();
        String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry","WebOS","Ubuntu","Windows7","Max OS X"};
      myList = new ArrayList<Deladd>();
        Deladd d1 = new Deladd();
        d1.setAdd("add" + String.valueOf(0));
        SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
        String coll=userP.getString("nameadd","");
        d1.setLine1(coll);

        myList.add(d1);

        new getAddress().execute();


    }
    public class Utility {

        public void setListViewHeightBasedOnChildren(ListView listView) {
                    ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }


    }
    private class getAddress extends
            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
//            spinner.setVisibility(View.VISIBLE);
            loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... data) {


            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST
//                SharedPreferences red = getSharedPreferences("Referral", Context.MODE_PRIVATE);
//                String referralinst=red.getString("referrer","");
                SharedPreferences cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
//                creduserid=cred.getString("phone_number","");
                payload.put("phone", cred.getString("phone_number",""));

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
                String url2 = BuildConfig.SERVER_URL+ "api/user/account/address?userid="+cred.getString("phone_number","");
                HttpGet httppost = new HttpGet(url2);
//                HttpDelete
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                httppost.setHeader("x-access-token", tok_sp);
                httppost.setHeader("Content-Type", "application/json");


                StringEntity entity = new StringEntity(payload.toString());

//                httppost.setEntity(entity);
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
//                        truth=resp.getString("msg");
                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return resp.getString("msg");

                    } else {
                        JSONObject data1 = new JSONObject(resp.getString("data"));
try{
                        JSONObject b=new JSONObject(data1.getString("deliveryAddress"));}
catch (Exception e)
{
    if(data1!=null)
    return "win";
}
                        JSONObject del=new JSONObject(data1.getString("deliveryAddress"));


                            CheckSize=del.length();

//                        d.setLine2(off.getString("line2"));
//                        d.setcity(off.getString("city"));
//                        d.setstate(off.getString("state"));

                            for(int i=1;i<=del.length();i++)
                            {

                            String add="";
//                            add=
                            JSONObject off = new JSONObject(del.getString("add"+String.valueOf(i)));
                                Boolean isActive=false;
                                try{
                                    isActive=off.getBoolean("isActive");
                                }
                                catch (Exception e)
                                {
                                    isActive=false;
                                }
                            Deladd d = new Deladd();
                                d.setAdd("add" + String.valueOf(i));
                            d.setLine1(off.getString("line1"));
                            d.setLine2(off.getString("line2"));
                                d.setcity(off.getString("city"));
                                d.setstate(off.getString("state"));
if(isActive)
                            myList.add(d);
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


            loader.setVisibility(View.GONE);
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
           {
                Deladd c=new Deladd();
                c.setLine1("Add an address");
                myList.add(c);
//                myList.add("Add an address");
                Adaptersimple adapter = new Adaptersimple(Editaddress.this, myList,CheckSize+1);
                listView = (ListView) findViewById(R.id.list1);
                listView.setDivider(null);
                LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT );
                listView.setLayoutParams(mParam);
                listView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
                TextView paydo=(TextView)findViewById(R.id.paydo);
               SharedPreferences get = getSharedPreferences("cred", Context.MODE_PRIVATE);
                     String payamt=String.valueOf(get.getInt("downpayment", 0));
               paydo.setText("Pay "+getApplicationContext().getString(R.string.Rs)+payamt);
                paydo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Editaddress.this, "Please wait while we connect you with the payment gateway", Toast.LENGTH_LONG).show();
                        Intent in=new Intent(Editaddress.this,PaymentLive.class);
                        startActivity(in);
                    }
                });
            }}}
    public void backpress()
    {
       LinearLayout back=(LinearLayout) findViewById(R.id.arrowlay);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("order"))
                finish();

        }
    };

    public void finish() {
        super.finish();
    };

}
