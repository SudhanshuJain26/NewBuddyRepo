package indwin.c3.shareapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import indwin.c3.shareapp.R;

/**
 * Created by shubhang on 27/04/16.
 */
public class HelpTipDialog extends Dialog {
    private TextView header, text1, text2;
    String headerText, text1Text, text2Text, colorOkay;

    public HelpTipDialog(Context context, String headerText, String text1Text, String text2Text, String colorOkay) {
        super(context, R.style.NewDialog);
        this.headerText = headerText;
        this.text1Text = text1Text;
        this.text2Text = text2Text;
        this.colorOkay = colorOkay;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helptip_dialog);
        header = (TextView) findViewById(R.id.header);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);

//        header.setText(headerText);
        text1.setText(Html.fromHtml(text1Text));
        text1.setMovementMethod(LinkMovementMethod.getInstance());
        if ("".equals(text2Text)) {
            text2.setVisibility(View.GONE);
        }
        text2.setText(text2Text);

        Button okay = (Button) findViewById(R.id.okay);
        okay.setTextColor(Color.parseColor(colorOkay));
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
