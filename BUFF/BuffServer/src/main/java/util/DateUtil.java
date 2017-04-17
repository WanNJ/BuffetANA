package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Noninstantiable utility class, used to transfer between String and LocalDate
 * Created by slow_time on 2017/3/5.
 */
public class DateUtil {

    //The date pattern that is used for conversion.
    private static final String DATE_PATTERN_LINE = "M-d-yy";
    private static final String DATE_PATTERN_SLASH = "M/d/yy";

    // The date formatter.
    private static final DateTimeFormatter DATE_FORMATTER_LINE = DateTimeFormatter.ofPattern(DATE_PATTERN_LINE);
    private static final DateTimeFormatter DATE_FORMATTER_SLASH = DateTimeFormatter.ofPattern(DATE_PATTERN_SLASH);


    //Suppress default constructor for noninstantiability
    private DateUtil() {
        throw new AssertionError();
    }

    /**
     * Returns the given date as a well formatted String. The above defined
     * {@link DateUtil#DATE_PATTERN_LINE} is used.
     * @param date the date to be returned as a string
     * @return formatted string
     */
    public static String formatLine(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER_LINE.format(date);
    }

    /**
     * Returns the given date as a well formatted String. The above defined
     * {@link DateUtil#DATE_PATTERN_SLASH} is used.
     * @param date the date to be returned as a string
     * @return formatted string
     */
    public static String formatSlash(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER_SLASH.format(date);
    }

    /**
     * Converts a String in the format of the defined {@link DateUtil#DATE_PATTERN_LINE}
     * to a {@link LocalDate} object.
     *
     * Returns null if the String could not be converted.
     *
     * @param dateString the date as String
     * @return the date object or null if it could not be converted
     */
    public static LocalDate parseLine(String dateString) {
        try {
            return DATE_FORMATTER_LINE.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Converts a String in the format of the defined {@link DateUtil#DATE_PATTERN_SLASH}
     * to a {@link LocalDate} object.
     *
     * Returns null if the String could not be converted.
     *
     * @param dateString the date as String
     * @return the date object or null if it could not be converted
     */
    public static LocalDate parseSlash(String dateString) {
        try {
            return DATE_FORMATTER_SLASH.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * 用来判断所给日期是否在某两个日期之间，与两个日期相等也算在两者之间
     * @param testDate 待判断的日期
     * @param beginDate 起始日期
     * @param endDate 结束日期
     * @return
     */
    public static boolean isBetween(LocalDate testDate, LocalDate beginDate, LocalDate endDate) {
        return !(testDate.isAfter(endDate) || testDate.isBefore(beginDate));
    }


    public static  LocalDate fromToString(String dateString){
        String[] arrs = dateString.split("-");
        int year = Integer.parseInt(arrs[0]);
        int month = Integer.parseInt(arrs[1]);
        int day = Integer.parseInt(arrs[2]);

        return LocalDate.of(year,month,day);

    }
}
