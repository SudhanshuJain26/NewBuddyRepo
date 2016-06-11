package indwin.c3.shareapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.adapters.BrandList;
import io.intercom.android.sdk.Intercom;

public class SupportedWebsites extends AppCompatActivity {

    ListView list;
    ImageView backButton;
    ImageView intercom;

    private String[] brands =  {
                "Flipkart",
                "Amazon",
                "Snapdeal",
                "Paytm",
                "Shopclues",
                "Jabong",
                "Myntra",
                "Infibeam"
    } ;

    private Integer [] icons = {
            R.drawable.fk_fav1x,R.drawable.amazon_fav1x,R.drawable.sdeal_fav1x,
            R.drawable.paytm_fav1x,R.drawable.sclues_fav1x,R.drawable.jbong_fav1x,
            R.drawable.myntra_fav1x,R.drawable.ibeamfav
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supported_websites);
        list = (ListView)findViewById(R.id.list) ;

        BrandList adapter = new BrandList(this, brands, icons);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        backButton= (ImageView)findViewById(R.id.backo);
        intercom = (ImageView)findViewById(R.id.interCom);
        intercom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intercom.client().displayMessageComposer();
                } catch (Exception e) {

                }

            }
        });

        backButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                onBackPressed();
                return false;
            }
        });


    }
}
