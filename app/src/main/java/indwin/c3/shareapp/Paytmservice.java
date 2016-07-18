package indwin.c3.shareapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Paytmservice extends AppCompatActivity {
private String userId,rechargeAmt;
    private TextView rcamount,cnonumber,panow;
    private EditText cashback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentfromact();
        setContentView(R.layout.confirmrecharge);

        init();
        set();

    }
void getIntentfromact()
{
    userId=getIntent().getExtras().getString("number");
    rechargeAmt=getIntent().getExtras().getString("amt");

}
    void init()
    {
        rcamount=(TextView)findViewById(R.id.rcamount);
        cnonumber=(TextView)findViewById(R.id.cnonumber);
        panow=(TextView)findViewById(R.id.panow);
        cashback=(EditText)findViewById(R.id.hve);


    }
    void set()
    {
        cnonumber.setText(userId);
        rcamount.setText(rechargeAmt);
        int rc=Integer.parseInt(rechargeAmt);
        String svc="";
        if(rc<=500)
            svc="20";
        else
        if(rc<=1000)
            svc="35";
        else
            if(rc<=1500)
                svc="55";
        panow.setText(  "Payable Now:"+svc);
        RelativeLayout touchnow=(RelativeLayout)findViewById(R.id.touchnow);
        touchnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                cashback.remo

                Toast.makeText(Paytmservice.this, "ffff", Toast.LENGTH_SHORT).show();
            }
        });
//        cashback.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus)
//                {
//                    if(cashback.getText().toString().length()==0)
//                        cashback.clearFocus();
//                }
//            }
//        });


    }
}
