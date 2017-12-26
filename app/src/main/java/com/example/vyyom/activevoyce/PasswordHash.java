package com.example.vyyom.activevoyce;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

/**
 * Created by Vyyom on 12/27/2017.
 *
 * This file contains methods for hashing passwords and checking passwords for hash match.
 */

class PasswordHash {

    static String hashPassword(String password) {
        final HashCode hashCode = Hashing.sha1().hashString(password, Charset.defaultCharset());
        return hashCode.toString();
    }

    static Boolean checkHashEquality(String hashValue, String valueToHash) {
        return hashValue.equals((hashPassword(valueToHash)));
    }
}
