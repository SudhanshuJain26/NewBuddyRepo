package indwin.c3.shareapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

;

/**
 * Created by Aniket Verma(Digo) on 26/12/15.
 */
public class SMSreceiver extends BroadcastReceiver {

    protected static final String CODE = "code";
    protected static final String INTENT_INFORM_MESSAGE_RECIEVED = "shareapp.intent.coderecieved" ;

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null)
            {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++)
                {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String senderNum  = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();
                    String read = String.valueOf(currentMessage.getStatus());
                    String date = String.valueOf(currentMessage.getTimestampMillis());
                    Pattern pattern = Pattern.compile("(\\d{4})");
                    Matcher matcher = pattern.matcher(message);
                    SMS sms = new SMS();
                    sms.setAddress(senderNum);
                    sms.setFolderName(String.valueOf(currentMessage.getStatusOnIcc()));
                    sms.setMsg(message);
                    sms.setReadState(read);
                    sms.setTime(date);
                    sms.setId("");
                    List<SMS> lstSms = new ArrayList<SMS>();
                    lstSms.add(sms);
                    String val="";
                    if (matcher.find()) {
                        System.out.println(matcher.group(1));

                        val = matcher.group(1);
                    }
                   // new MainActivity.SendSmsToServer().execute(lstSms);

                    Log.i("SmsReceiver", "senderNum: " + senderNum
                            + "; message: " + message);




if((senderNum.contains("BUDDY"))&&(message.charAt(0)>='0')&&(message.charAt(0)<='9'))
                    {
                        System.out.println("The code recieved is" + val);
                        Intent informerIntent = new Intent(INTENT_INFORM_MESSAGE_RECIEVED);
                        informerIntent.putExtra(CODE, val);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(informerIntent) ;
}



                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
                    // Show alert
//					int duration = Toast.LENGTH_LONG;
//					Toast.makeText(context, "senderNum: "+ senderNum + ", message: " + message, duration).show() ;
                } // end for loop
            } // bundle is null
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e) ;
        }
    }

}
