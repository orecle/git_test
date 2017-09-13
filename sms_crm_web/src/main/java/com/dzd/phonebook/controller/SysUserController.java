package com.dzd.phonebook.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dzd.base.util.SessionUtils;
import com.dzd.phonebook.entity.*;
import com.dzd.phonebook.page.NewRegexUtil;
import com.dzd.phonebook.service.*;
import com.dzd.phonebook.util.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dzd.base.util.MethodUtil;
import com.dzd.base.util.StringUtil;
import com.github.pagehelper.Page;

/**
 * 系统用户controller
 *
 * @author chenchao
 * @date 2016-6-29 11:29
 */
@Controller
@RequestMapping("/sysUser")
public class SysUserController extends AbstractController {

    public static final Logger log = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    private SysUserService<SysUser> sysUserService;


    @Autowired
    private SysRoleRelService sysRoleRelService;




    /**
     * @Description:账号列表接口
     * @author:wangran
     * @time:2017年7月14日 下午17:25:34
     */
    @RequestMapping(value = "/sysUserList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> sysUserList(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        DzdPageParam dzdPageParam = new DzdPageParam();

        try {
            SysUser user = SessionUtils.getUser(request);
            //请求参数
            JSONObject json = CommUtil.getParams(request);
            Object email =  json.get("email"); //账号

            Integer pageNum = (Integer)json.get("pagenum"); //每页显示多少条数据
            Integer pageSize = (Integer)json.get("pagesize"); //每页显示多少条数据
            dzdPageParam.setStart(pageNum);
            dzdPageParam.setLimit(pageSize);

            Map<String, Object> sortMap = new HashMap<String, Object>();

            if (email != null && !StringUtil.isEmpty(email.toString())) {
                sortMap.put("email", email.toString());
            }

            if (user.getSuperAdmin() != 1) {
                sortMap.put("smsUserVal", "select id from sys_user where id=" + user.getId() + " or superiorId = " + user.getId() + "");
            }
            sortMap.put("sortVal", "order by sy.createTime desc");

            dzdPageParam.setCondition(sortMap);

            Page<SysUserView> dataList = sysUserService.queryUserPage(dzdPageParam);

            Map<String, Object> mapData = new HashMap<String, Object>();
            List<Header> headers = CommUtil.headerSysUser();
            mapData.put("header", headers);
            if (!CollectionUtils.isEmpty(dataList)) {
                mapData.put("data", dataList.getResult());
                mapData.put("total",dataList.getTotal());
            }
            map.put("tableData", mapData);
        } catch (Exception e) {
            logger.error("====================》客户管理查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    /**
     * @Description:查询经理接口
     * @author:wangran
     * @time:2017年7月14日 下午17:25:34
     */
    @RequestMapping(value = "/querySuperiorManager", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse querySuperiorManager() {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            List<SysUserView> dataList = sysUserService.querySuperiorManager();
            dzdResponse.setData(dataList);
            dzdResponse = dzdResponseSuccess(dzdResponse);
        } catch (Exception e) {
            logger.error("====================》查询经理失败：" + e.getMessage());
            dzdResponse = dzdResponseFail(dzdResponse);
            e.printStackTrace();
        }
        return dzdResponse;
    }

    /**
     * 账号管理新增或者修改---------接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/from/merge", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse saveOrUpdate(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        //请求参数
        JSONObject json = CommUtil.getParams(request);
        Object id = json.get("id");              //账号id
        Object email = json.get("email");       //登陆账号
        Object nickName = json.get("nickName");  //昵称
        Object role = json.get("role");          //角色id
        Object state = json.get("state");        //状态
        Object telephone = json.get("telephone");   //联系电话
        Object superiorId = json.get("superiorId");   //上级经理
        Object isAutomatic = json.get("isAutomatic");   //是否自动分配客户



        if(nickName == null || StringUtil.isEmpty(nickName.toString()) || email == null|| StringUtil.isEmpty(email.toString()) ){
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_EMAIL_NICKNAME_MSG);
            return dzdResponse;
        }

        if(role == null || StringUtil.isEmpty(role.toString())){
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            dzdResponse.setRetMsg(ErrorCodeTemplate.ROLE_MSG);
            return dzdResponse;
        }

        if(telephone == null || StringUtil.isEmpty(telephone.toString())){
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_SMS_PHONE_IS_EMPTY);
            return dzdResponse;
        }

        // 无效号码
        if (!NewRegexUtil.elevenNumber(telephone.toString())) {
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            dzdResponse.setRetMsg(ErrorCodeTemplate.RIGHT_PHONE_MSG);
            return dzdResponse;
        }

        SysUser sysUser = new SysUser();
        if (id != null && !StringUtil.isEmpty(id.toString())) {
            sysUser.setId(Integer.parseInt(id.toString()));
        }
        if (role != null && !StringUtil.isEmpty(role.toString())) {
            sysUser.setRole(Integer.parseInt(role.toString()));
        }
        if (state != null && !StringUtil.isEmpty(state.toString())) {
            sysUser.setState(Integer.parseInt(state.toString()));
        }
        if (email != null && !StringUtil.isEmpty(email.toString())) {
            sysUser.setEmail(email.toString());
        }
        if (nickName != null && !StringUtil.isEmpty(nickName.toString())) {
            sysUser.setNickName(nickName.toString());
        }
        if (telephone != null && !StringUtil.isEmpty(telephone.toString())) {
            sysUser.setTelephone(telephone.toString());
        }
        if (superiorId != null && !StringUtil.isEmpty(superiorId.toString())) {
            sysUser.setSuperiorId(Integer.parseInt(superiorId.toString()));
        }
        if (isAutomatic != null && !StringUtil.isEmpty(isAutomatic.toString())) {
            sysUser.setIsAutomatic(Integer.parseInt(isAutomatic.toString()));
        }
        sysUser.setSuperAdmin(0);
        sysUser.setDeleted(0);
        String newPwd = "123456";
        String pwd = MethodUtil.MD5(newPwd);
        sysUser.setPwd(pwd);
        try {
            //判断账户是否已经存在
            Integer num1= sysUserService.querySysUserByuserEmail(email, id);
            if (num1> 0) {
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                dzdResponse.setRetMsg(ErrorCodeTemplate.EMAIL_HAD_MSG);
            } else {
                //判断电话号码是否被注册
                Integer num = sysUserService.queryTelephoneIsHad(telephone,id);
                if (num > 0) {
                    dzdResponse = DzdResponseMessage.dzdResponseCheck(dzdResponse);
                    return dzdResponse;
                }

                if (id == null) {
                    sysUserService.saveUser(sysUser);
                } else {
                    SysRoleRel sysRoleRel = new SysRoleRel();
                    sysRoleRel.setObjId(sysUser.getId());
                    sysRoleRel.setRoleId(sysUser.getRole());
                    sysRoleRel.setRelType(1);
                    //修改角色
                    sysRoleRelService.updateByObjId(sysRoleRel);
                    sysUserService.updateBySelective(sysUser);
                }
                dzdResponse = dzdResponseSuccess(dzdResponse);
            }
        } catch (Exception e) {
            log.error(null, e);
            dzdResponse = dzdResponseFail(dzdResponse);
            e.printStackTrace();
        }
        return dzdResponse;
    }

    /**
     * 删除用户
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse postsDelete(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            //请求参数
            JSONObject json = CommUtil.getParams(request);
            Object id = json.get("id");  //账号id
            sysUserService.deleteUser(Integer.parseInt(id.toString()));
            dzdResponse = dzdResponseSuccess(dzdResponse);
        } catch (Exception e) {
            log.error(null, e);
            dzdResponse = dzdResponseFail(dzdResponse);
            e.printStackTrace();
        }
        return dzdResponse;
    }


    /**
     * 根据编号查询用户----------------------接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryById", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse sysUserEdit(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            //请求参数
            JSONObject json = CommUtil.getParams(request);
            Object id = json.get("id");  //账号id

            if (StringUtil.isEmpty(id.toString())) {
                dzdResponse = dzdResponseFail(dzdResponse);
                return dzdResponse;
            }
            SysUserView sysUser = sysUserService.querySysUserById(Integer.parseInt(id.toString()));
            dzdResponse.setData(sysUser);
            dzdResponse = dzdResponseSuccess(dzdResponse);
        } catch (Exception e) {
            log.error(null, e);
            dzdResponse = dzdResponseFail(dzdResponse);
            e.printStackTrace();
        }
        return dzdResponse;
    }

    /**
     * 批量删除系统用户
     *
     * @param request
     * @param data
     * @return
     */
    @RequestMapping(value = "formDelete", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DzdResponse userDelete(HttpServletRequest request, @RequestBody Map<String, Object> data) {
        DzdResponse response = new DzdResponse();
        try {
            Object ids = data.get("uids");
            if (ids == null) {
                response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
                return response;
            }
            List<Integer> uids = (List<Integer>) ids;
            if (uids == null || uids.size() == 0) {
                response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
            } else {
                sysUserService.deleteUsers(uids);
                response.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
            }
        } catch (Exception e) {
            log.error(null, e);
            response.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return response;
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

    public DzdResponse dzdResponseSuccess(DzdResponse dzdResponse) {
        dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_SUCCESS);
        return dzdResponse;
    }

    public DzdResponse dzdResponseFail(DzdResponse dzdResponse) {
        dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
        dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_FAIL);
        return dzdResponse;
    }
}
