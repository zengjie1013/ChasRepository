package com.chas.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取IP地址工具类
 *
 * @author Chas Zeng
 * @date 2016/12/28
 */
public class IPUtils {

    public static String getIPAddress(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")){
            ip = "127.0.0.1";
        }
        return ip;
    }
}
