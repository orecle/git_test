package com.dzd.phonebook.util;


/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/1/4
 * Time: 19:41
 */
public class Define {

    public interface PHONEKEY {
        String INVALID = "invalid";// 无效号码
        String VALID = "valid";
        String DUPLICATE = "duplicate";// 重复号码
    }

    public interface FILENAME {
        String CUSTOMER_ORDER = "customer_order";
    }
    public interface DISTINGUISHOPERATOR {
        String MOBILECHINA_NAME = "移动";
        String UNICOMCHINA_NAME = "联通";
        String TELECOMCHINA_NAME = "电信";
        String INVALIDCHINA_NAME = "未知";

        String MOBILEOPERATOR = "mobileOperator";//移动
        String UNICOMOPERATOR = "unicomOperator";//联通
        String TELECOMOPERATOR = "telecomOperator";//电信
        String INVALIDOPERATOR = "invalidOperator";//未知

        String MOBILETOTALPAGE = "mobileTotalPage";//
        String UNICOMTOTALPAGE = "unicomTotalPage";//
        String TELECOMTOTALPAGE = "telecomTotalPage";//
        String INVALIDTOTALPAGE = "invalidTotalPage";//

        String MOBILECURRENTPAGE = "mobileCurrentPage";//
        String UNICOMCURRENTPAGE = "unicomCurrentPage";//
        String TELECOMCURRENTPAGE = "telecomCurrentPage";//
        String INVALIDCURRENTPAGE = "invalidCurrentPage";//

        String TELECOMLENGTH = "telecomLength";
        String MOBILELENGTH = "mobileLength";
        String UNICOMLENGTH = "unicomLength";
        String UNKNOWNLENGTH = "unknownLength";
        String INVALIDLENGTH = "invalidLength";
    }
}
