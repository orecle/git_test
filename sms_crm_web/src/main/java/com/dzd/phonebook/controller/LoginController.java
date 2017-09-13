package com.dzd.phonebook.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.entity.*;
import com.dzd.phonebook.service.*;
import com.dzd.phonebook.util.CommUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dzd.base.annotation.Auth;
import com.dzd.base.entity.BaseEntity;
import com.dzd.base.entity.TreeNode;

import com.dzd.base.util.MethodUtil;
import com.dzd.base.util.SessionUtils;
import com.dzd.base.util.TreeUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.page.CaptchaUtil;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;


/**
 * 登录controller
 *
 * @author chenchao
 * @date 2016-6-24 16:11:00
 */
@Controller
@RequestMapping("/")
public class LoginController {
    public static final Logger log = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private SysUserService<SysUser> sysUserService;

    @Autowired
    private SysMenuService<SysMenu> sysMenuService;

    @Autowired
    private SysRoleRelService sysRoleRelService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private FollowRecordService followRecordService;


    /**
     * @Description:登录页面访问
     * @author:oygy
     * @time:2016年12月30日 上午11:12:18
     */
    @Auth(verifyURL = false)
    @RequestMapping(value = "loginview")
    public String showPage() {
        return "login";
    }

    @RequestMapping(value = "captcha", method = RequestMethod.GET)
    @ResponseBody
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CaptchaUtil.outputCaptcha(request, response);
    }

    /**
     * 登录
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        //请求参数
        JSONObject json= CommUtil.getParams(request);

        String account = (String)json.get("account");
        String password = (String) json.get("password");
        String captcha = (String) json.get("captcha");

        SysUser sysUser = sysUserService.queryUserExist(account);
        try {
            HttpSession session = request.getSession();
            Object randomString = session.getAttribute("randomString");

            // 用户不存在
            if (sysUser == null) {
                map.put("msg", ErrorCodeTemplate.MSG_SYSUSER_EMPTY);
                map.put("retCode", ErrorCodeTemplate.CODE_FAIL);
                return map;
            } else {
                SysUser u = sysUserService.queryLogin(account, MethodUtil.MD5(password));
                if (u == null) {
                    map.put("msg", ErrorCodeTemplate.MSG_SYSUSER_PWD_IS_MISS);
                    map.put("retCode", ErrorCodeTemplate.CODE_FAIL);
                    return map;
                } else {
                   // 3. 登录
                    if (captcha != null && !"".equals(captcha) && randomString != null
                            && !"".equals(randomString.toString())
                            && captcha.trim().toUpperCase().equals(randomString.toString().trim().toUpperCase())) {
                        map = userLogin(account, password, request, session);// 调用登录方法
                    } else if (StringUtil.isEmpty(captcha)) {
                        map.put("msg", ErrorCodeTemplate.MSG_VERTIFYCODE_EMPTY);
                        map.put("retCode", ErrorCodeTemplate.CODE_FAIL);
                    } else {
                        map = userLogin(account, password, request, session);// 调用登录方法
                   }
                }
            }
        } catch (Exception e) {
            log.error(null, e);
            map.put("msg", ErrorCodeTemplate.MSG_STATE_ERROR);
            map.put("retCode", ErrorCodeTemplate.CODE_FAIL);
        }
        return map;
    }


    /**
     * 登录
     *
     * @param account
     * @param password
     * @param request
     * @param session
     */
    public Map<String, Object> userLogin(String account, String password, HttpServletRequest request,
                                         HttpSession session) {

        Map<String, Object> map = new HashMap<String, Object>();
        SysUser u = sysUserService.queryLogin(account, MethodUtil.MD5(password));
        if (u == null) {
            map.put("msg", ErrorCodeTemplate.MSG_SYSUSER_EMPTY);
            map.put("retCode", ErrorCodeTemplate.CODE_FAIL);
        } else {
            // 设置User到Session
            SessionUtils.setUser(request, u);
            SysUser user = SessionUtils.getUser(request);
            //登陆成功
            SysRoleRel sysRoleRels = sysRoleRelService.queryRoleById(user.getId());

            //登陆用户角色id
            user.setRole(sysRoleRels.getRoleId());

            try{
                SysUser su=new SysUser();
                su.setId(u.getId());
                //修改登陆了时间
                sysUserService.update(su);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            session.setAttribute("user", u);
            map.put("msg", ErrorCodeTemplate.MSG_SUCCESS_MSG);
            map.put("retCode", ErrorCodeTemplate.CODE_SUCESS);
            map.put("user",user);
        }
        return map;
    }

    /***
     * 首页接口
     * @param request
     * @return
     */
    @RequestMapping(value="homePage",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> homePage(HttpServletRequest request){
        Map<String, Object> map = new HashMap<String, Object>();
        SysUser user = SessionUtils.getUser(request);
        List<FollowRecord> followRecords = new ArrayList<FollowRecord>();
        List<CustomerView> customers = new ArrayList<CustomerView>();
        if (user.getRole() == 1) {
            //超级管理员
        } else if (user.getRole() == 2) {
            //经理
            //经理下面的所有客户最近2天的跟进记录
            followRecords = followRecordService.queryFollowRecord2Day(user.getId());
        } else {
            //业务员
            //新增沟通客户
            customers = customerService.queryCustomerStage0(user.getId());
        }
        if (followRecords.size() > 0) {
            map.put("data", followRecords);
        }
        if (customers.size() > 0) {
            map.put("data", customers);
        }
        return map;
    }

    /**
     * @Description:根据用户ID查询拥有角色
     * @author:oygy
     * @time:2016年12月31日 上午11:12:53
     */
    private List<SysRoleRel> queryRoleByUserId(Integer uid) {
        DzdPageParam dzdPageParam = new DzdPageParam();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("sysUserId", uid);
        dzdPageParam.setCondition(condition);
        List<SysRoleRel> sysRoleRels = sysRoleRelService.queryRoleByUserId(dzdPageParam);
        return sysRoleRels;
    }

    /**
     * 注销
     *
     * @param request
     * @return
     */
    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping(value = "logout")
    public String logOut(HttpServletRequest request) {
        // 清空session
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/loginview.do#exit";
    }


    /**
     * 右侧菜单退出系统
     *
     * @return
     */
    @RequestMapping("/exitMenu")
    public String exit(HttpServletRequest request) {
        // 清空session
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/welcome.do";
    }


    /**
     * 修改密码----------------接口
     *
     * @param request
     * @return
     * @throws Exception
     */
    @MethodDescription("修改密码")
    @RequestMapping(value = "updatePwd",method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse updatePwd(HttpServletRequest request) throws Exception {
        DzdResponse dzdResponse = new DzdResponse();
        //请求参数
        JSONObject json = CommUtil.getParams(request);
        Object oldPwdVal = json.get("oldPwd");
        Object newPwdVal = json.get("newPwd");
        try {
            String oldPwd = oldPwdVal.toString();
            String newPwd = newPwdVal.toString();
            SysUser user = SessionUtils.getUser(request);
            if (user == null) {
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                return dzdResponse;
            }
            SysUser bean = sysUserService.queryById(user.getId());

            //匹配旧密码
            if (!MethodUtil.ecompareMD5(oldPwd, bean.getPwd())) {
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                dzdResponse.setRetMsg(ErrorCodeTemplate.PWD_OLD_MSG);
                return dzdResponse;
            }
            bean.setPwd(MethodUtil.MD5(newPwd));
            sysUserService.updateBySelective(bean);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdResponse;
    }

    /**
     * 重置密码----------------接口
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "resetPwd",method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse resetPwd(HttpServletRequest request) throws Exception {
        DzdResponse dzdResponse = new DzdResponse();
        //请求参数
        JSONObject json = CommUtil.getParams(request);
        Object id = json.get("id");  //账户id
        SysUser sysUser=new SysUser();
        try {
            sysUser.setId(Integer.parseInt(id.toString()));
            sysUser.setPwd(MethodUtil.MD5("123456"));
            sysUserService.updateSysUserPwd(sysUser);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            log.info(null,e);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdResponse;
    }
}
