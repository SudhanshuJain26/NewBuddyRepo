package indwin.c3.shareapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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

import indwin.c3.shareapp.BuildConfig;
import indwin.c3.shareapp.GIFView;
import indwin.c3.shareapp.Otp;
import indwin.c3.shareapp.Paytmservice;
import indwin.c3.shareapp.R;
import indwin.c3.shareapp.utils.AppUtils;

public class Paytmhome extends AppCompatActivity {
private EditText amount,phhone_no;
    private RadioGroup paytm;
    private RadioButton op1,op2,op3;
    private TextView rs200,rs500,rs1500,rcnow;
    private RelativeLayout rtouch;
    private ScrollView sc;
    private  int checkSelected=0;
    private String userid;
    private GIFView loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paytmhomescreen);
        SharedPreferences cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
        userid=cred.getString("phone_number", "");
        init();
        set();

    }
    void init()
    {
        loader = (GIFView) findViewById(R.id.loading);
        amount=(EditText)findViewById(R.id.moneypay);
        phhone_no=(EditText)findViewById(R.id.newno);
        rs200=(TextView)findViewById(R.id.enter200);
        rs500=(TextView)findViewById(R.id.enter500);
        rs1500=(TextView)findViewById(R.id.enter1500);
        rcnow=(TextView)findViewById(R.id.recharge);
        paytm=(RadioGroup)findViewById(R.id.rgroup1);
        op1=(RadioButton)findViewById(R.id.prd1);
        op1.setChecked(true);
        op2=(RadioButton)findViewById(R.id.prd2);
        op3=(RadioButton)findViewById(R.id.prd3);
sc=(ScrollView)findViewById(R.id.scccs);
        rtouch=(RelativeLayout)findViewById(R.id.rltouc);



    }
    void set()
    {

        rtouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaildateAmt();
                Toast.makeText(Paytmhome.this, "dfdfd", Toast.LENGTH_SHORT).show();
            }
        });
        op2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                op1.setChecked(false);
                op3.setChecked(false);
                checkSelected=1;
                phhone_no.setVisibility(View.VISIBLE);

            }
        });
        op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                op2.setChecked(false);
                op3.setChecked(false);
                checkSelected=0;
                phhone_no.setVisibility(View.GONE);

            }
        });
        op3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                op1.setChecked(false);
                op2.setChecked(false);
                checkSelected=2;
                phhone_no.setVisibility(View.GONE);

            }
        });
        final int selected=paytm.getCheckedRadioButtonId();
        if(selected==1)
            phhone_no.setVisibility(View.VISIBLE);
        else
        phhone_no.setVisibility(View.GONE);
        op1=(RadioButton)findViewById(selected);
        String amt="";
        amt=amount.getText().toString();
rs200.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        rs200.setBackgroundResource(R.drawable.roundedbluepay);
        rs500.setBackgroundResource(R.drawable.roundedgreypay);
        rs1500.setBackgroundResource(R.drawable.roundedgreypay);
        amount.setText("200");
    }
});
        rs500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rs500.setBackgroundResource(R.drawable.roundedbluepay);
                rs200.setBackgroundResource(R.drawable.roundedgreypay);
                rs1500.setBackgroundResource(R.drawable.roundedgreypay);
                amount.setText("500");
            }
        });
        rs1500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rs1500.setBackgroundResource(R.drawable.roundedbluepay);
                rs500.setBackgroundResource(R.drawable.roundedgreypay);
                rs200.setBackgroundResource(R.drawable.roundedgreypay);
                amount.setText("1500");
            }
        });

       amount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    vaildateAmt();

                    //                        Intent in = new Intent(HomePage.this, ViewForm.class);

                    // paste = (TextView) findViewById(R.id.pasteAg);
//                    queryNew.requestFocus();
//                    //clickpaste();
//                    parse(queryNew.getText().toString().trim());

                }
                return false;
            }
        });
        rcnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaildateAmt();
                int amtNow=0;
                try {
                    amtNow = Integer.parseInt(amount.getText().toString());
                }
                catch (Exception e)
                {
                    amtNow=0;
                }
                if((checkSelected==0)&&(amtNow!=0))
                {
                    Toast.makeText(Paytmhome.this, "fiors", Toast.LENGTH_SHORT).show();
                    Intent in=new Intent(Paytmhome.this, Paytmservice.class);
                    in.putExtra("amt",amount.getText().toString());
                    in.putExtra("number",userid);
                    startActivity(in);
                    //do something
                }
                else
                    if(((checkSelected==1)&&(amtNow!=0))&&(phhone_no.getText().toString().length()==10))
                    {
                        userid=phhone_no.getText().toString();
                        new SendOtp().execute();
                        Toast.makeText(Paytmhome.this, "sec", Toast.LENGTH_SHORT).show();
                        //otp page
                    }
                else
                        if((checkSelected==2)&&(amtNow!=0))
                        {
                            Toast.makeText(Paytmhome.this, "3rd", Toast.LENGTH_SHORT).show();
                            //otp page
                        }


            }
        }); }
    void vaildateAmt()
    {if(!AppUtils.isEmpty(amount.getText().toString())){
        int t=Integer.parseInt(amount.getText().toString());
        if((t<200)||(t>1500))
          amount.setText("");
    }}
    private class SendOtp extends
            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            rcnow.setEnabled(false);
loader.setVisibility(View.VISIBLE);
        }

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

                HttpClient client = new DefaultHttpClient(httpParameters);
                String  url_otp= BuildConfig.SERVER_URL+"api/auth/sendotp?phone="+userid;
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
            rcnow.setEnabled(true);
            loader.setVisibility(View.GONE);
            if(result.contains("win"))
            {
                Intent in=new Intent(Paytmhome.this,Otp.class);

                in.putExtra("send",9);
                startActivity(in);

            }
        }}

}
