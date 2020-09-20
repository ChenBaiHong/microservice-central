package com.baimicro.central.common.utils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过身份证获取年龄和性别
 */
public class CardUtil {


    /**
     * 通过身份证号码获取出生日期、性别
     *
     * @param idCard
     * @return 返回的出生日期格式：1990-01-01   性别格式：F-女，M-男
     */
    /**
     * 通过身份证号码获取出生日期、性别
     *
     * @param idCard
     * @return 返回的出生日期格式：1990-01-01   性别格式：F-女，M-男
     */
    public static Map<String, Object> getByIdCard(String idCard) {

        String birthday = "";
        String sexCode = "";
        Integer age = 0;
        char[] number = idCard.toCharArray();
        boolean flag = true;
        int idCardLength = idCard.length();
        if (number.length == 15) {
            for (int x = 0; x < number.length; x++) {
                if (!flag) return new HashMap();
                flag = Character.isDigit(number[x]);
            }
        } else if (number.length == 18) {
            for (int x = 0; x < number.length - 1; x++) {
                if (!flag) return new HashMap();
                flag = Character.isDigit(number[x]);
            }
        }
        if (flag && idCardLength == 15) {
            birthday = "19" + idCard.substring(6, 8) + "-"
                    + idCard.substring(8, 10) + "-"
                    + idCard.substring(10, 12);
            sexCode = Integer.parseInt(idCard.substring(idCardLength - 3, idCardLength)) % 2 == 0 ? "F" : "M";
        } else if (flag && idCardLength == 18) {
            birthday = idCard.substring(6, 10) + "-"
                    + idCard.substring(10, 12) + "-"
                    + idCard.substring(12, 14);
            sexCode = Integer.parseInt(idCard.substring(idCardLength - 4, idCardLength - 1)) % 2 == 0 ? "F" : "M";
        }
        Map<String, Object> map = new HashMap();
        LocalDate birth = LocalDateUtils.parseLocalDate(birthday, "yyyy-MM-dd");
        map.put("birth", birth);
        map.put("sex", sexCode);
        return map;
    }
}
