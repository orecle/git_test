package com.dzd.phonebook.util;

/**
 * Created by dzd-technology01 on 2017/7/26.
 */
public class DzdResponseMessage {

    /**
     * 返回结果
     * @return
     */
    public static DzdResponse dzdResponseSuccess(DzdResponse dzdResponse){
        dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_SUCCESS);
        return dzdResponse;
    }
    /**
     * 返回结果
     * @return
     */
    public static DzdResponse dzdResponseFail(DzdResponse dzdResponse){
        dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
        dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_FAIL);
        return dzdResponse;
    }
    /**
     * 返回结果
     * @return
     */
    public static DzdResponse dzdResponseCheck(DzdResponse dzdResponse){
        dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
        dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_TELEPHEON);
        return dzdResponse;
    }
}
