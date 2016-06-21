package indwin.c3.shareapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import org.json.JSONObject;

public class SendOtpaddress extends AppCompatActivity {
private String token="";
    private String userid="";
    private GIFView loader;
    TextView send;
//    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otpaddress);
        ImageView back=(ImageView)findViewById(R.id.backo22);
//        ImageView back=(ImageView)findViewById(R.id.backo11);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
finish();
//                onBackPressed();
            }
        });

        send=(TextView)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send.setVisibility(View.INVISIBLE);
                new ItemsByKeyword().execute();

            }
        });
        TextView msg=(TextView)findViewById(R.id.buddyLogo);
loader=(GIFView)findViewById(R.id.loading);
        registerReceiver(broadcastReceiver, new IntentFilter("order"));
        SharedPreferences get = getSharedPreferences("cred", Context.MODE_PRIVATE);
        msg.setText("To change your address, please verify with OTP first.Your OTP will be sent to +91-"+get.getString("phone_number",""));
    }
    private class ItemsByKeyword extends
            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
//            spinner.setVisibility(View.VISIBLE);
        loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            // String urldisplay = data[0];
            // HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
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

                HttpPost httppost = new HttpPost(BuildConfig.SERVER_URL+"authenticate");
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
                        token = resp.getString("token");
                        SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userP.edit();
                        editor.putString("tok", token);

                        editor.commit();
                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror111", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {
//            spinner.setVisibility(View.GONE);

            if (result.contains("fail")) {

                // populateUserDetails();

                Toast.makeText(getApplicationContext(),
                        "Something's Wrong! Please try again!",
                        Toast.LENGTH_LONG).show();

            } else {

                new send_otp().execute("");


            }

        }
    }

    private class send_otp extends
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
                // userid=12&productid=23&action=add
                // TYPE: POST


//                payload.put("userid", userid);
//                payload.put("password", pass);
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);
                SharedPreferences cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
                 userid=cred.getString("phone_number", "");
                HttpClient client = new DefaultHttpClient(httpParameters);
               String  url_otp=BuildConfig.SERVER_URL+"api/auth/sendotp?phone="+userid;
                HttpPost httppost = new HttpPost(url_otp);
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);

                String tok_sp=toks.getString("token_value","");
                httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");
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

                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {
            send.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
if(result.contains("win"))
{
    Intent i =new Intent(SendOtpaddress.this,Otp.class);
    i.putExtra("send",3);
    i.putExtra("phone_number",userid);
    startActivity(i);
    finish();
}
        }}
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
