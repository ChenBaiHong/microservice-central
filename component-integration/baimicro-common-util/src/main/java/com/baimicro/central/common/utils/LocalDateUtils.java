package com.baimicro.central.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class LocalDateUtils {
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public final static DateTimeFormatter yyyyMMddHHmmssBy_ = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * yyyyMMdd
     */
    public final static DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
    /**
     * yyyy-MM-dd
     */
    public final static DateTimeFormatter yyyyMMddBy_ = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    /**
     * yyyy-MM-dd
     */
    public final static DateTimeFormatter yyyyMMddHHmmssSBy_ = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

    /**
     * 获取当天最小时间
     * yyyy-MM-dd 00:00:00
     *
     * @return
     */
    public static String getNowDateTime000000_yyyyMMddHHmmssBy_(String date) {
        if (StringUtils.isEmpty(date)) {
            return LocalDateTime.of(LocalDate.now(), LocalTime.MIN).format(yyyyMMddHHmmssBy_);
        }
        return LocalDateTime.of(LocalDate.parse(date, yyyyMMddBy_), LocalTime.MIN).format(yyyyMMddHHmmssBy_);
    }

    /**
     * 获取当天最大时间
     * yyyy-MM-dd 23:59:59
     *
     * @return
     */
    public static String getNowDateTime235959_yyyyMMddHHmmssBy_(String date) {
        if (StringUtils.isEmpty(date)) {
            return LocalDateTime.of(LocalDate.now(), LocalTime.MAX).format(yyyyMMddHHmmssBy_);
        }
        return LocalDateTime.of(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalTime.MAX).format(yyyyMMddHHmmssBy_);
    }

    /**
     * 获取当前时间
     * yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getNowDateTime_yyyyMMddHHmmssBy_() {
        return LocalDateTime.now().format(yyyyMMddHHmmssBy_);
    }

    /**
     * 获取当前日期
     * yyyyMMdd
     *
     * @return
     */
    public static String getNowDate_yyyyMMdd() {
        return LocalDate.now().format(yyyyMMdd);
    }

    public static String getNowDate(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    public static String getNowDate(String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.now().format(formatter);
    }

    /**
     * @return long
     * @Author baihoo.chen
     * @Description TODO 获取毫秒数
     * @Date 2019/10/18 18:25
     * @Param []
     **/
    public static long getMilliSecond() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static long getMilliSecond(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * @return long
     * @Author baihoo.chen
     * @Description TODO 获取秒数
     * @Date 2019/10/18 18:25
     * @Param []
     **/
    public static long getSecond() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
    }

    public static long getSecond(LocalDateTime dateTime) {
        return dateTime.toEpochSecond(ZoneOffset.of("+8"));
    }

    public static long getSecond(LocalDate localDate) {
        return localDate.toEpochDay();
    }

    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateStr, df);
    }

    public static LocalDateTime parseLocalDateTime(String dateStr, String pattern) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateStr, df);
    }

    public static String formatLocalDate(LocalDate localDate, String pattern) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return localDate.format(df);
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(df);
    }

    /**
     * 专业处理yyyy-MM-dd HH:mm:ss.S 转成 yyyy-MM-dd HH:mm:ss
     *
     * @param dateStr
     * @return
     */
    public static String formatString_yyyyMMddHHmmssSTo_yyyMMddHHmmss(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return "";
        }
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, yyyyMMddHHmmssSBy_);
        return localDateTime.format(yyyyMMddHHmmssBy_);
    }
}
