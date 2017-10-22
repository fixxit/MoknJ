package nl.it.fixx.moknj.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class DateUtil {

    private final static DateTimeFormatter JSTF = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public final static String MONTH = "MMM";
    public final static String DAY = "EEE";

    private DateUtil() {
    }

    public static DateTime parseJavaScriptDateTime(String date) {
        try {
            return JSTF.parseDateTime(date);
        } catch (Exception e) {
            return new DateTime();
        }
    }

    public static Date parseJavaScriptDate(String date) {
        return parseJavaScriptDateTime(date).toDate();
    }

    public static DateTime parseDate(String date, String fmt) {
        DateTimeFormatter jsfmt = DateTimeFormat.forPattern(fmt);
        return jsfmt.parseDateTime(date);
    }

    public static List<String> geMonthsForYear(int year) throws Exception {
        return getDateList(year + "-01-01", year + "-12-31", MONTH);
    }

    private static List<String> getDateList(String start, String end, String df) {
        LocalDate startDate = new LocalDate(start);
        LocalDate endDate = new LocalDate(end);
        return getDateList(startDate, endDate, df);
    }

    private static List<String> getDateList(LocalDate startDate, LocalDate endDate, String df) {
        final List<String> dates = new ArrayList<>();
        while (startDate.isBefore(endDate)) {
            dates.add(startDate.toString(df));
            if (MONTH.equals(df)) {
                startDate = startDate.plus(Period.months(1));
            } else if (DAY.equals(df)) {
                startDate = startDate.plusDays(1);
            }
        }
        return dates;
    }

    public static List<String> geMonthsForDate(DateTime date) throws Exception {
        String end = date.getYear() + "-" + date.getMonthOfYear() + "-" + date.getDayOfMonth();
        return getDateList(date.getYear() + "-01-01", end, MONTH);
    }

    public static List<String> getDaysOfWeek(DateTime date) {
        LocalDate start = date.toLocalDate().dayOfWeek().withMinimumValue();
        LocalDate end = date.toLocalDate().dayOfWeek().withMaximumValue();
        return getDateList(start, end, DAY);
    }

}
