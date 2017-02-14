package com.chas.utils;

import java.util.Random;

/**
 * 随机数工具类
 *
 * @author Chas Zeng
 * @email 894555763@qq.com
 * @date 2017/2/14
 */
public class RandomNumUtils {

    /**
     * 获取随机数（主要用于验证码）
     *
     * @param number 随机数的个数
     * @return
     */
    public static String random(int number){
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            result.append(random.nextInt(9));
        }
        return result.toString();
    }
}
