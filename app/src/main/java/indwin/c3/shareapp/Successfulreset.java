package indwin.c3.shareapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Successfulreset extends AppCompatActivity {
    private TextView backtologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successfullysetpass);
        backtologin=(TextView)findViewById(R.id.backtologin);
        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Successfulreset.this,MainActivity.class);
                finish();
                startActivity(in);
                overridePendingTransition(0,0);
            }
        });

    }
    @Override
    public void onBackPressed()
    {
        Intent in=new Intent(Successfulreset.this,MainActivity.class);
        finish();
        startActivity(in);
        overridePendingTransition(0,0);
    }
}
