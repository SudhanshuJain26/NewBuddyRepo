package indwin.c3.shareapp.utils;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by shubhang on 24/04/16.
 */
public final class ValidationUtils {

    public static boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (phoneNumber.length() == 10)
            if (!TextUtils.isEmpty(phoneNumber)) {
                return Patterns.PHONE.matcher(phoneNumber).matches();
            }
        return false;
    }

    public static boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }
}
