package com.glen.smsreader.appConfig;

import android.text.TextUtils;

public class Constances {

    public static final int RC_READ_SMS = 123;

    public static final boolean isValidPhoneNumber(CharSequence target) {
        if (target == null || TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

}
