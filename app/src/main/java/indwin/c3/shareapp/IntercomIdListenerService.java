package indwin.c3.shareapp;

import android.content.Intent;
import com.google.android.gms.iid.InstanceIDListenerService;

import indwin.c3.shareapp.RegistrationIntentService;

public class IntercomIdListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}