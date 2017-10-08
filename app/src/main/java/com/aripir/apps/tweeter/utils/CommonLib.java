package com.aripir.apps.tweeter.utils;

import java.math.BigInteger;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by saripirala on 10/7/17.
 */

public class CommonLib {

    public static String withSuffix(Double count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f %c",
                count / Math.pow(1000, exp),
                "KMGTPE".charAt(exp-1));
    }

}
