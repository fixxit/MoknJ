/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author adriaan
 */
public class DateUtil {

    public static String JS_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static DateTime parseJavaScriptDateTime(String date) {
        DateTimeFormatter jsfmt = DateTimeFormat.forPattern(JS_DATE_FORMAT);
        return jsfmt.parseDateTime(date);
    }

    public static Date parseJavaScriptDate(String date) {
        return parseJavaScriptDateTime(date).toDate();
    }

    public static DateTime parseDate(String date, String fmt) {
        DateTimeFormatter jsfmt = DateTimeFormat.forPattern(fmt);
        return jsfmt.parseDateTime(date);
    }

    public static List<String> geMonthsForYear(int year) throws Exception {
        String time1 = year + "-01-01";
        String time2 = year + "-12-31";
        LocalDate date1 = new LocalDate(time1);
        LocalDate date2 = new LocalDate(time2);
        List<String> months = new ArrayList<>();
        while (date1.isBefore(date2)) {
            months.add(date1.toString("MMM"));
            date1 = date1.plus(Period.months(1));
        }

        return months;
    }

    public static List<String> geMonthsForDate(DateTime date) throws Exception {
        String time1 = date.getYear() + "-01-01";
        String time2 = date.getYear() + "-" + date.getMonthOfYear() + "-" + date.getDayOfMonth();
        LocalDate date1 = new LocalDate(time1);
        LocalDate date2 = new LocalDate(time2);
        List<String> months = new ArrayList<>();
        while (date1.isBefore(date2)) {
            months.add(date1.toString("MMM"));
            date1 = date1.plus(Period.months(1));
        }

        return months;
    }

    public static List<String> getDaysOfWeek(DateTime date) {
        // add labels
        List<String> daysInMonthLabels = new ArrayList<>();
        LocalDate firstDay = date.toLocalDate().dayOfWeek().withMinimumValue();
        LocalDate nextMonthFirstDay = date.toLocalDate().dayOfWeek().withMaximumValue();
        while (firstDay.isBefore(nextMonthFirstDay)) {
            daysInMonthLabels.add(firstDay.toString("EEE"));
            firstDay = firstDay.plusDays(1);
        }

        return daysInMonthLabels;
    }

}
