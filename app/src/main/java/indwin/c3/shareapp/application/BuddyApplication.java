package indwin.c3.shareapp.application;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import indwin.c3.shareapp.R;

/**
 * Created by rock on 5/18/16.
 */
public class BuddyApplication extends Application {
    private Tracker mTracker;


    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */


    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
