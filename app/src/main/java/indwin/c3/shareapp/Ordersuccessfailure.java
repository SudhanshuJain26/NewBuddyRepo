package indwin.c3.shareapp;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import io.intercom.android.sdk.Intercom;

public class Ordersuccessfailure extends AppCompatActivity {
private String status="";
    private ImageView ims;
    private TextView orderid,payStatus,track,home;
    private Button order;
    private RelativeLayout rlo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordersuccessfailure);
        status=getIntent().getExtras().getString("orderId");
init();
        if(!status.equals("fail"))
        {

orderid.setText("Orderid "+status);
            try {
                Map userMap = new HashMap<>();
                userMap.put("order_placed", true);
//                userMap.put("EMI_SELECTED", emiAmount.getText().toString());
//                userMap.put("DOWNPAYMENT", dValue.getText().toString());
//                        userMap.put("phone", mPhone);
//                        System.out.println("Intercom data 4" + mPhone);
                Intercom.client().updateUser(userMap);
            } catch (Exception e) {
                System.out.println("Intercom two" + e.toString());
            }
            int col=(Color.parseColor("#44C2A6"));
            rlo.setBackgroundColor(col);
            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in=new Intent(Ordersuccessfailure.this,ViewForm.class);
                    in.putExtra("which_page", 16);
                    startActivity(in);
                }
            });

        }
        else
        {int col=(Color.parseColor("#D48080"));
            rlo.setBackgroundColor(col);
home.setText("I'll try again later");
            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   finish();
                }
            });

            ims.setImageResource(R.drawable.fail);
            orderid.setVisibility(View.GONE);
            payStatus.setText("Payment Failed");
            track.setVisibility(View.GONE);
            order.setText("Try Again");
        }
home.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i=new Intent();
        i.setAction("order");
        sendBroadcast(i);
//        overridePendingTransition(0,0);
        finish();
    }
});
    }
public void init()
{rlo=(RelativeLayout)findViewById(R.id.rlo);
    ims=(ImageView)findViewById(R.id.orderStatus);
    orderid=(TextView)findViewById(R.id.orderId);
    home=(TextView)findViewById(R.id.home);
    payStatus=(TextView)findViewById(R.id.payRec);
    track=(TextView)findViewById(R.id.track);
    order=(Button)findViewById(R.id.orderPage);
}
}
