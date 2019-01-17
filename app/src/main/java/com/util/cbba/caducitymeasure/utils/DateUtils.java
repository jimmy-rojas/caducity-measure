package com.util.cbba.caducitymeasure.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static final String SIMPLE_DATE_FORMAT = "EEEE, dd MMMM yyyy";

    public static String getDateSimple(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        return format.format(date);
    }
}
