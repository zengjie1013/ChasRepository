package com.chas.api.sms;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 聚合数据短信服务Api
 *
 * @author Chas Zeng
 * @email 894555763@qq.com
 * @date 2017/2/14
 */
public class SMSApi {

    //连接主机的超时时间（单位：毫秒）
    private static final int DEFAULT_CONN_TIMEOUT = 30000;

    //从主机读取数据的超时时间（单位：毫秒）
    private static final int DEFAULT_READ_TIMEOUT = 30000;

    //默认字符编码
    private static final String DEFAULT_ENCODING = "UTF-8";

    //接口请求地址
    private static final String REQUEST_URL = "http://v.juhe.cn/sms/send";

    //应用AppKey
    private static final String APP_KEY = "272ca705bc04f0b98a168c9cd018a503";

    /**
     * 发送短信服务
     *
     * @param mobile 手机号码
     * @param code 验证码
     * @param smsId 短信模板id
     * @param app 应用名称
     * @param method 请求方式
     * @return
     */
    public static String sendSMS(String mobile, String code, String smsId, String app, String method){
        String result = null;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mobile",mobile);
        params.put("tpl_id",smsId);
        params.put("tpl_value","#code#=" + code + "&#app#=" + app);
        params.put("key",APP_KEY);
        try {
            result = net(REQUEST_URL,params,method);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 发送短信网络请求
     *
     * @param reqUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return 网络请求字符串
     * @throws Exception
     */
    private static String net(String reqUrl, Map<String, Object> params, String method) throws Exception{
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuilder sb = new StringBuilder();
            if (method == null || "GET".equalsIgnoreCase(method)) {
                reqUrl = reqUrl + "?" + urlEncode(params);
            }
            URL url = new URL(reqUrl);
            conn = (HttpURLConnection)url.openConnection();
            if (method != null && "POST".equalsIgnoreCase(method)) {
                conn.setRequestMethod("POST");
                //设置是否从HttpURLConnection读入，默认情况下是true;
                conn.setDoOutput(true);
            }
            //Post请求不能使用缓存
            conn.setUseCaches(false);
            //如果不设置超时（timeout），在网络异常的情况下，可能会导致程序僵死而不继续往下执行。
            //设置超时连接时间
            conn.setConnectTimeout(DEFAULT_CONN_TIMEOUT);
            //设置超时读取时间
            conn.setReadTimeout(DEFAULT_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            //实际上只是建立了一个与服务器的tcp连接，并没有实际发送http请求。
            conn.connect();
            if (params != null && "POST".equalsIgnoreCase(method)) {
                /*此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，
                  所以在开发中不调用上述的connect()也可以)。*/
                OutputStream os = conn.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.writeBytes(urlEncode(params));
            }
            //将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端
            //实际发送请求的代码段就在这里
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is,DEFAULT_ENCODING));
            String readStr;
            while ((readStr = reader.readLine()) != null) {
                sb.append(readStr);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    /**
     * 对请求参数进行URL编码
     *
     * @param data 请求参数
     * @return 编码后的请求参数格式
     */
    private static String urlEncode(Map<String, Object> data){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", DEFAULT_ENCODING)).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

}
