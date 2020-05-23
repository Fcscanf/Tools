package com.fcant.tools.utils;

/**
 * StringUtils
 * <p>
 * encoding:UTF-8
 *
 * @author Fcant 上午 10:56:44 2020/3/22/0022
 */
public class LongStringToArrayUtil {

    /**
     * 根据字符串将长字符串进行截取切片
     *
     * @param longString 长字符串
     * @param byString 根据截取的短字符串
     * @return String[]
     * @author Fcant 上午 11:02:38 2020/3/22/0022
     */
    public static String[] stringSplit(String longString, String byString) {
        String replaceString = longString.replace(byString, "");
        int allocationNum = ((longString.length() - replaceString.length()) / byString.length() + 1);
        String[] ids = new String[allocationNum];
        if (longString.contains(byString)) {
            ids = longString.split(byString);
        } else {
            ids[0] = longString;
        }
        return ids;
    }

    /**
     * 将String（原本是int类型可以强转的才可以）的数组转为Integer类型的数组
     *
     * @param ids String的数组
     * @return Integer[]
     * @author Fcant 下午 18:52:05 2020/4/18/0018
     */
    public static Integer[] arrayStringToInt(String[] ids) {
        Integer[] intIds = new Integer[ids.length];
        for (int i = 0; i < ids.length; i++) {
            intIds[i] = Integer.valueOf(ids[i]);
        }
        return intIds;
    }

}
