package com.example.springbootshiro.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间utils
 *
 * @author wangzb
 * @version 1.0
 * @date 2021/02/05
 */
public class DateUtils {
    public static final long DAY_MILLI = 86400000L;
    public static final long HOUR_MILLI = 3600000L;
    public static final long MINUTE_MILLI = 60000L;
    public static final long SECOND_MILLI = 1000L;
    public static final String TIME_TO = " 23:59:59";
    public static final transient int BEFORE = 1;
    public static final transient int AFTER = 2;
    public static final transient int EQUAL = 3;
    public static final String TIME_PATTERN_LONG = "dd/MMM/yyyy:HH:mm:ss +0900";
    public static final String TIME_PATTERN_LONG2 = "dd/MM/yyyy:HH:mm:ss +0900";
    public static final String DB_TIME_PATTERN = "YYYY-MM-DD HH24:MI:SS";
    public static final String DB_TIME_PATTERN_1 = "YYYYMMDDHH24MISS";
    public static final String TIME_PATTERN_SHORT = "dd/MM/yy HH:mm:ss";
    public static final String TIME_PATTERN_SHORT_1 = "yyyy/MM/dd HH:mm";
    public static final String TIME_PATTERN_SHORT_2 = "yyyy年MM月dd日 HH:mm:ss";
    public static final String TIME_PATTERN_SESSION = "yyyyMMddHHmmss";
    public static final String TIME_PATTERN_MILLISECOND = "yyyyMMddHHmmssSSS";
    public static final String DATE_FMT_0 = "yyyyMMdd";
    public static final String DATE_FMT_1 = "yyyy/MM/dd";
    public static final String DATE_FMT_2 = "yyyy/MM/dd hh:mm:ss";
    public static final String DATE_FMT_3 = "yyyy-MM-dd";
    public static final String DATE_FMT_4 = "yyyy年MM月dd日";
    public static final String DATE_FMT_5 = "yyyy-MM-dd HH";
    public static final String DATE_FMT_6 = "yyyy-MM";
    public static final String DATE_FMT_7 = "MM月dd日 HH:mm";
    public static final String DATE_FMT_8 = "HH:mm:ss";
    public static final String DATE_FMT_9 = "yyyy.MM.dd";
    public static final String DATE_FMT_10 = "HH:mm";
    public static final String DATE_FMT_11 = "yyyy.MM.dd HH:mm:ss";
    public static final String DATE_FMT_12 = "MM月dd日";
    public static final String DATE_FMT_13 = "yyyy年MM月dd日HH时mm分";
    public static final String DATE_FMT_14 = "yyyyMM";
    public static final String DATE_FMT_15 = "MM-dd HH:mm:ss";
    public static final String DATE_FMT_16 = "yyyyMMddHHmm";
    public static final String DATE_FMT_17 = "HHmmss";
    public static final String DATE_FMT_18 = "yyyy";
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private DateUtils() {
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String formatLocalDateTimeToString(LocalDateTime localDateTime, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return localDateTime.format(formatter);
        } catch (DateTimeParseException var3) {
            return null;
        }
    }

    public static LocalDateTime stringToLocalDateTime(String dateStr, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.parse(dateStr, formatter);
        } catch (DateTimeParseException var3) {
            return null;
        }
    }

    public static int getActualMaximum(Date date) {
        return dateToLocalDateTime(date).getMonth().length(dateToLocalDate(date).isLeapYear());
    }

    public static int getWeekOfDate(Date date) {
        return dateToLocalDateTime(date).getDayOfWeek().getValue();
    }

    public static int getAbsDateDiffDay(LocalDate before, LocalDate after) {
        return Math.abs(Period.between(before, after).getDays());
    }

    public static int getAbsTimeDiffDay(LocalDateTime before, LocalDateTime after) {
        return Math.abs(Period.between(before.toLocalDate(), after.toLocalDate()).getDays());
    }

    public static int getAbsTimeDiffMonth(LocalDateTime before, LocalDateTime after) {
        return Math.abs(Period.between(before.toLocalDate(), after.toLocalDate()).getMonths());
    }

    public static int getAbsDateDiffMonth(String date1, String date2) {
        LocalDate localDate1 = LocalDate.parse(date1, DateTimeFormatter.ofPattern("yyyyMMdd"));
        Period periodToNextJavaRelease = Period.between(localDate1, LocalDate.parse(date2, DateTimeFormatter.ofPattern("yyyyMMdd")));
        return periodToNextJavaRelease.getMonths();
    }

    public static int getAbsTimeDiffYear(LocalDateTime before, LocalDateTime after) {
        return Math.abs(Period.between(before.toLocalDate(), after.toLocalDate()).getYears());
    }

    public static int getDayOfWeek(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(7);
    }

    public static int getLastMonth(Date date) {
        return dateToLocalDateTime(date).getMonth().getValue();
    }

    public static LocalDate newThisMonth(Date date) {
        LocalDate localDate = dateToLocalDate(date);
        return LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
    }

    public static LocalDate lastThisMonth(Date date) {
        int lastDay = getActualMaximum(date);
        LocalDate localDate = dateToLocalDate(date);
        return LocalDate.of(localDate.getYear(), localDate.getMonth(), lastDay);
    }

    public static LocalDate newThisYear(Date date) {
        LocalDate localDate = dateToLocalDate(date);
        return LocalDate.of(localDate.getYear(), 1, 1);
    }

    public static Timestamp getCurrentDateTime() {
        return new Timestamp(Instant.now().toEpochMilli());
    }

    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now(Clock.system(ZoneId.of("Asia/Shanghai")));
    }

    public static LocalDateTime reserveDateCustomTime(Date date, String customTime) {
        String dateStr = dateToLocalDate(date).toString() + " " + customTime;
        return stringToLocalDateTime(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    public static LocalDateTime reserveDateCustomTime(Timestamp date, String customTime) {
        String dateStr = timestampToLocalDate(date).toString() + " " + customTime;
        return stringToLocalDateTime(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    public static final LocalDateTime zerolizedTime(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 0, 0, 0, 0);
    }

    public static LocalDateTime zerolizedTime(Timestamp date) {
        LocalDateTime localDateTime = timestampToLocalDateTime(date);
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 0, 0, 0, 0);
    }

    public static LocalDateTime getEndTime(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 23, 59, 59, 999000000);
    }

    public static LocalDateTime getEndTime(Timestamp date) {
        LocalDateTime localDateTime = timestampToLocalDateTime(date);
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 23, 59, 59, 999000000);
    }

    public static int calculateToEndTime(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime end = getEndTime(date);
        return (int)(end.toEpochSecond(ZoneOffset.UTC) - localDateTime.toEpochSecond(ZoneOffset.UTC));
    }

    public static LocalDateTime addTime(LocalDateTime localDateTime, ChronoUnit chronoUnit, int num) {
        return localDateTime.plus((long)num, chronoUnit);
    }

    public static LocalDateTime addTime(Date date, ChronoUnit chronoUnit, int num) {
        long nanoOfSecond = date.getTime() % 1000L * 1000000L;
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(date.getTime() / 1000L, (int)nanoOfSecond, ZoneOffset.of("+8"));
        return localDateTime.plus((long)num, chronoUnit);
    }

    public static LocalDateTime addTime(Timestamp date, ChronoUnit chronoUnit, int num) {
        long nanoOfSecond = date.getTime() % 1000L * 1000000L;
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(date.getTime() / 1000L, (int)nanoOfSecond, ZoneOffset.of("+8"));
        return localDateTime.plus((long)num, chronoUnit);
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        long nanoOfSecond = date.getTime() % 1000L * 1000000L;
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(date.getTime() / 1000L, (int)nanoOfSecond, ZoneOffset.of("+8"));
        return localDateTime;
    }

    public static LocalDateTime timestampToLocalDateTime(Timestamp date) {
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(date.getTime() / 1000L, date.getNanos(), ZoneOffset.of("+8"));
        return localDateTime;
    }

    public static LocalDate dateToLocalDate(Date date) {
        return dateToLocalDateTime(date).toLocalDate();
    }

    public static LocalDate timestampToLocalDate(Timestamp date) {
        return timestampToLocalDateTime(date).toLocalDate();
    }

    public static boolean isTheSameDay(LocalDateTime begin, LocalDateTime end) {
        return begin.toLocalDate().equals(end.toLocalDate());
    }

    public static int compareTwoTime(LocalDateTime time1, LocalDateTime time2) {
        if (time1.isAfter(time2)) {
            return 1;
        } else {
            return time1.isBefore(time2) ? -1 : 0;
        }
    }

    public static long getTwoTimeDiffSecond(Timestamp time1, Timestamp time2) {
        long diff = timestampToLocalDateTime(time1).toEpochSecond(ZoneOffset.UTC) - timestampToLocalDateTime(time2).toEpochSecond(ZoneOffset.UTC);
        return diff > 0L ? diff : 0L;
    }

    public static long getTwoTimeDiffMin(Timestamp time1, Timestamp time2) {
        long diff = getTwoTimeDiffSecond(time1, time2) / 60L;
        return diff > 0L ? diff : 0L;
    }

    public static long getTwoTimeDiffHour(Timestamp time1, Timestamp time2) {
        long diff = getTwoTimeDiffSecond(time1, time2) / 3600L;
        return diff > 0L ? diff : 0L;
    }

    public static boolean isTimeInRange(Date startTime, Date endTime) throws Exception {
        LocalDateTime now = getCurrentLocalDateTime();
        LocalDateTime start = dateToLocalDateTime(startTime);
        LocalDateTime end = dateToLocalDateTime(endTime);
        return start.isBefore(now) && end.isAfter(now) || start.isEqual(now) || end.isEqual(now);
    }

    public static String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date parseDate(final String str, final String... parsePatterns) {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(str, parsePatterns);
        } catch (ParseException var3) {
            return null;
        }
    }
}
