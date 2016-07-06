package indwin.c3.shareapp.application;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import indwin.c3.shareapp.BuildConfig;
import indwin.c3.shareapp.R;

/**
 * Created by rock on 5/18/16.
 */

@ReportsCrashes(formUri = "", mailTo = "sudhanshu.jain@hellobuddy.in,moulik@hellobuddy.in,aniket@hellobuddy.in,deepak@hellobuddy.in", customReportContent = {ReportField.BUILD,
        ReportField.USER_APP_START_DATE, ReportField.USER_CRASH_DATE, ReportField.USER_EMAIL, ReportField.APP_VERSION_NAME,
        ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE,
        ReportField.LOGCAT,}, mode = ReportingInteractionMode.TOAST, resToastText = R.string.crashed_report)
public class BuddyApplication extends Application {
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.IS_ACRA_ENABLED) {
            ACRA.init(this);
        }
    }


    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
