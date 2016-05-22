package indwin.c3.shareapp.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static boolean isNotValidIFSCCode(CharSequence charSequence) {
        String pattern = "[A-Z|a-z]{4}[0][\\d]{6}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(charSequence);
        if (charSequence.length() == 11 && m.matches()) {
            return false;
        }

        return true;
    }

    public static boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }
}
