package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Created by slow_time on 2017/3/5.
 */
public class DateUtil {

    //The date pattern that is used for conversion.
    private static final String DATE_PATTERN_LINE = "d-M-yy";
    private static final String DATE_PATTERN_SLASH = "d/M/yy";

    // The date formatter.
    private static final DateTimeFormatter DATE_FORMATTER_LINE =
            DateTimeFormatter.ofPattern(DATE_PATTERN_LINE);
    private static final DateTimeFormatter DATE_FORMATTER_SLASH =
            DateTimeFormatter.ofPattern(DATE_PATTERN_SLASH);

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
}
