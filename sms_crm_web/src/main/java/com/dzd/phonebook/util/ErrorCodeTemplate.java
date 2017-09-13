package com.dzd.phonebook.util;

/**
 * 错误代码
 *
 * @author chenchao
 * @date 2016-6-27 10:08:00
 */
public class ErrorCodeTemplate {
    /* 成功 */
    public static final String CODE_SUCESS = "000000";
    /* 失败 */
    public static final String CODE_FAIL = "000001";
    /* 参数异常 */
    public static final String CODE_PARAMETER_ERROR = "000999";

    public static final String CODE_OTHER = "000002";

    public static final String MSG_USER_ISNULL = "账户名或密码错误!";
    public static final String MSG_STATE_ERROR = "登录异常,请稍候再试!";
    public static final String MSG_SYSUSER_EMPTY = "用户不存在!";
    public static final String MSG_SYSUSER_PWD_IS_MISS = "密码错误!";
    public static final String MSG_VERTIFYCODE_EMPTY = "请输入验证码!";
    public static final String MSG_REGISTER_MSG = "用户已经存在!";
    public static final String MSG_EMAIL_NICKNAME_MSG = "名称或者账号不能为空!";
    public static final String MSG_SUCCESS_MSG = "登录成功!";
    public static final String MSG_SYSTEM_ERROR_MSG = "服务器异常!";

    public static final String MSG_SMS_PHONE_IS_EMPTY = "请输入手机号码!";
    public static final String MSG_SMS_VERIFY_IS_EMPTY = "请输入验证码!";
    public static final String MSG_VERIFYCODE_ERROR = "验证码错误或失效!";

    public static final String MSG_VERTIFYCODE_INPUT = "获取次数频繁（一天三次）,请稍候重试!";


    /**
     * 找回密码短信验证码返回信息
     */
    public static final String MSG_VERTIFYCODE_INPUT_TWO = "获取次数频繁（一天二次）,请稍候重试!";

    public static final String MSG_SYSUSER_EMPTY_1 = "账户名无效！";

    public static final String MSG_SUCCESS = "操作成功！";


    public static final String MSG_TELEPHEON = "电话号码已经被注册！";

    public static final String MSG_FAIL = "操作失败！";

    public static final String MSG_MANUAL_ADDITION = "手动添加短信数量！";

    public static final String MSG_EMAIL_PHONE_ERROR = "账号和手机号码不匹配！";

    public static final String PHONE_NO_HAVE = "手机号码不存在，请重新输入!";

    /**
     * 找回密码时手机验证码验证 次数“一天2次” 类型
     */
    public static final Integer VERTIFYCODE_TYPE = 2;

    public static final String NO_FOUND_YWY = "没有找到可以分配的业务员！";

    public static final String NO_CHANGE = "您今天已经试发过了！";

    public static final String NO_CHANGE_SUCCESS = "您今天已经试发成功了！";

    public static final String EMAIL_HAD_MSG = "账号已经存在！";

    public static final String PWD_OLD_MSG = "请检查原始密码是否正确！";
    public static final String ROLE_MSG = "请输入角色!";
    public static final String RIGHT_PHONE_MSG = "请输入有效的电话号码！";
}

