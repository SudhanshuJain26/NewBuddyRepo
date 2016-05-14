package indwin.c3.shareapp;

import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
            int col=(Color.parseColor("#44C2A6"));
            rlo.setBackgroundColor(col);

        }
        else
        {int col=(Color.parseColor("#D48080"));
            rlo.setBackgroundColor(col);
home.setText("I'll try again later");


            ims.setImageResource(R.drawable.fail);
            orderid.setVisibility(View.GONE);
            payStatus.setText("Payment Failed");
            track.setVisibility(View.GONE);
            order.setText("Try Again");
        }

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
