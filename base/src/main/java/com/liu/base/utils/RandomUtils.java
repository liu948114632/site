package com.liu.base.utils;

import java.util.Random;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/13
 */
public class RandomUtils {
    public static String randomInteger(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(new Random().nextInt(10));
        }
        return sb.toString();
    }
}
