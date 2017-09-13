package com.dzd.phonebook.util;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dzd-technology01 on 2017/7/19.
 */
public class CommUtil {

    public static JSONObject getParams(HttpServletRequest request){
        JSONObject json=new JSONObject();
        String params = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
            StringBuffer sb = new StringBuffer("");
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
            params = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(params.equals("")){
             return json;
        }else{
            json = JSONObject.fromObject(params);
        }
        return  json;
    }
    /**
     * 账户管理
     * @return
     */
    public static List<Header> headerSysUser(){
        List<Header> headers=new ArrayList<Header>();
        Header header1=new Header();
        header1.setTitle("账号");
        header1.setDataIndex("email");
        headers.add(header1);
        Header header2=new Header();
        header2.setTitle("状态");
        header2.setDataIndex("stateValue");
        headers.add(header2);
        Header header3=new Header();
        header3.setTitle("昵称");
        header3.setDataIndex("nickName");
        headers.add(header3);
        Header header4=new Header();
        header4.setTitle("电话");
        header4.setDataIndex("telephone");
        headers.add(header4);
        Header header5=new Header();
        header5.setTitle("角色");
        header5.setDataIndex("roleName");
        headers.add(header5);
        Header header6=new Header();
        header6.setTitle("最后登录时间");
        header6.setDataIndex("lastLoginTime");
        headers.add(header6);
        Header header7=new Header();
        header7.setTitle("创建时间");
        header7.setDataIndex("createTime");
        headers.add(header7);
        return headers;
    }
    /**
     * 客户管理
     * @return
     */
    public static List<Header> headersCustomer(){
        List<Header> headers=new ArrayList<Header>();
        Header header1=new Header();
        header1.setTitle("分配时间");
        header1.setDataIndex("allocatedTime");
        header1.setKey("allocatedTime");
        headers.add(header1);
        Header header2=new Header();
        header2.setTitle("客户名称");
        header2.setDataIndex("cname");
        header2.setKey("cname");
        headers.add(header2);
        Header header3=new Header();
        header3.setTitle("联系电话");
        header3.setDataIndex("telephone");
        header3.setKey("telephone");
        headers.add(header3);
        Header header4=new Header();
        header4.setTitle("跟进记录");
        header4.setDataIndex("content");
        header4.setKey("content");
        headers.add(header4);
        Header header5=new Header();
        header5.setTitle("备注");
        header5.setDataIndex("remark");
        header5.setKey("remark");
        headers.add(header5);
        Header header6=new Header();
        header6.setTitle("阶段");
        header6.setDataIndex("stageValue");
        header6.setKey("stageValue");
        headers.add(header6);
        Header header7=new Header();
        header7.setTitle("跟进人");
        header7.setDataIndex("bname");
        header7.setKey("bname");
        headers.add(header7);
        Header header8=new Header();
        header8.setTitle("来源");
        header8.setDataIndex("source");
        header8.setKey("source");
        headers.add(header8);
        Header header9=new Header();
        header9.setTitle("详情");
        header9.setDataIndex("isSignValue");
        header9.setKey("isSignValue");
        headers.add(header9);
        return headers;
    }
    /**
     * 公海客户
     * @return
     */
    public static List<Header> headers2(){
        List<Header> headers=new ArrayList<Header>();
        Header header1=new Header();
        header1.setTitle("客户名称");
        header1.setDataIndex("cname");
        header1.setKey("cname");
        headers.add(header1);
        Header header2=new Header();
        header2.setTitle("联系电话");
        header2.setDataIndex("telephone");
        header2.setKey("telephone");
        headers.add(header2);
        Header header3=new Header();
        header3.setTitle("跟进记录");
        header3.setDataIndex("content");
        header3.setKey("content");
        headers.add(header3);
        Header header4=new Header();
        header4.setTitle("备注");
        header4.setDataIndex("remark");
        header4.setKey("remark");
        headers.add(header4);

        Header header5=new Header();
        header5.setTitle("来源");
        header5.setDataIndex("source");
        header5.setKey("source");
        headers.add(header5);
        return headers;
    }
    /**
     * 推广客户
     * @return
     */
    public static List<Header> fromCustomerHeaders(){
        List<Header> headers=new ArrayList<Header>();
        Header header1=new Header();
        header1.setTitle("日期");
        header1.setDataIndex("createTime");
        header1.setKey("createTime");
        headers.add(header1);
        Header header2=new Header();
        header2.setTitle("来源");
        header2.setDataIndex("source");
        header2.setKey("source");
        headers.add(header2);
        Header header3=new Header();
        header3.setTitle("重复数量");
        header3.setDataIndex("rpPhoneCount");
        header3.setKey("rpPhoneCount");
        headers.add(header3);
        Header header4=new Header();
        header4.setTitle("试发量");
        header4.setDataIndex("sfCount");
        header4.setKey("sfCount");
        headers.add(header4);

        Header h5=new Header();
        h5.setTitle("试发成功量");
        h5.setDataIndex("sfsCount");
        h5.setKey("sfsCount");
        headers.add(h5);

        Header h6=new Header();
        h6.setTitle("分配数量");
        h6.setDataIndex("fpCount");
        h6.setKey("fpCount");
        headers.add(h6);

        Header h7=new Header();
        h7.setTitle("意向数量");
        h7.setDataIndex("yxCount");
        h7.setKey("yxCount");
        headers.add(h7);
        Header h8=new Header();
        h8.setTitle("成交数量");
        h8.setDataIndex("cjCount");
        h8.setKey("cjCount");
        headers.add(h8);
        return headers;
    }

    /**
     * 推广客户详情
     * @return
     */
    public static List<Header> headersDetail(){
        List<Header> headers=new ArrayList<Header>();
        Header header1=new Header();
        header1.setTitle("试发日期");
        header1.setDataIndex("createTime");
        header1.setKey("createTime");
        headers.add(header1);
        Header header2=new Header();
        header2.setTitle("联系电话");
        header2.setDataIndex("telephone");
        header2.setKey("telephone");
        headers.add(header2);
        Header header3=new Header();
        header3.setTitle("跟进记录");
        header3.setDataIndex("content");
        header3.setKey("content");
        headers.add(header3);
        Header header4=new Header();
        header4.setTitle("备注");
        header4.setDataIndex("remark");
        header4.setKey("remark");
        headers.add(header4);

        Header header5=new Header();
        header5.setTitle("来源");
        header5.setDataIndex("source");
        header5.setKey("source");
        headers.add(header5);
        return headers;
    }
}
