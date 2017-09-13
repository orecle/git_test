package com.dzd.phonebook.util.send.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.dzd.phonebook.util.send.api.qwsend.ISendWs;
import com.dzd.phonebook.util.send.api.qwsend.SenderService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dzd.phonebook.entity.VertifyCode;

import javax.jws.WebParam;

/**
 * 发送短信
 *
 * @author CHENCHAO
 * @date 2017-04-11 13:49:00
 */
public class SendSmsUtil {
    public static final Logger log = LoggerFactory.getLogger(SendSmsUtil.class);

    public static void main(String[] args) {
        PostMethod postMethod = new PostMethod("http://api.dzd.com/v4/sms/send.do");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
        String timeStamp = format.format(new Date());// 时间戳
        String uid = "292";// 账户id
        String key = "3ac18cb223fa6c5a764fc85e8a519f4b";// key

        // 设置参数
        postMethod.addParameter("uid", uid);
        postMethod.addParameter("timestamp", timeStamp);
        postMethod.addParameter("sign", getSign(uid, key, timeStamp));
        postMethod.addParameter("mobile", "15712904144");
        postMethod.addParameter("text", "【千讯数据】您有待联系客户，请登陆管理后台查看");
        postMethod.addParameter("iid", "423");// 格子编码id
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setParameter(
                HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        try {
            // post提交发送短信
            httpClient.executeMethod(postMethod);
            System.out.println(postMethod.getResponseBodyAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送短信
     */
  public static void sendMessage(String phone,String content){
      PostMethod postMethod = new PostMethod("http://api.dzd.com/v4/sms/send.do");
      SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
      String timeStamp = format.format(new Date());// 时间戳
      String uid = "298";// 账户id
      String key = "367ba24ec87f1e6a4f43c7f029d11d17";// key

      // 设置参数
      postMethod.addParameter("uid", uid);
      postMethod.addParameter("timestamp", timeStamp);
      postMethod.addParameter("sign", getSign(uid, key, timeStamp));
      postMethod.addParameter("mobile",phone);
      postMethod.addParameter("text", content+":http://crm.dzd.com/");
      postMethod.addParameter("iid", "423");// 格子编码id
      HttpClient httpClient = new HttpClient();
      httpClient.getParams().setParameter(
              HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
      try {
          // post提交发送短信
          httpClient.executeMethod(postMethod);
          System.out.println(postMethod.getResponseBodyAsString());
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
    /**
     * 生成签名
     *
     * @param account
     * @param key
     * @param timeStamp
     * @return
     */
    private static String getSign(String account, String key, String timeStamp) {
        // 账户 + key + 时间戳 MD5加密
        return string2MD5(account + key + timeStamp);
    }


    /**
     * MD5加密
     *
     * @param inStr
     * @return
     */
    public static String string2MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
