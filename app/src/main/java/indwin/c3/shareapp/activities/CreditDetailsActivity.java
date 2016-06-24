package indwin.c3.shareapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.widget.TextView;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.utils.CustomBulletSpan;

public class CreditDetailsActivity extends AppCompatActivity {
    private int startBulletMargin = 32;
    private int bulletRadius = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        TextView titleTv = (TextView) findViewById(R.id.activity_header);
        titleTv.setText("Verify Your Identity");
        CharSequence t1 = "College ID card\n";
        SpannableString s1 = new SpannableString(t1);
        s1.setSpan(new CustomBulletSpan(startBulletMargin, bulletRadius), 0, t1.length(), 0);
        CharSequence t2 = "Permanent Address Proof, Aadhaar or PAN card";
        SpannableString s2 = new SpannableString(t2);
        s2.setSpan(new CustomBulletSpan(startBulletMargin, bulletRadius), 0, t2.length(), 0);
        TextView bulletDocumentTv = (TextView) findViewById(R.id.document_bullet_tv);
        bulletDocumentTv.setText(TextUtils.concat(s1, s2));
    }
}
