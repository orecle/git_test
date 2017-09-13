package com.dzd.phonebook.controller;

import com.dzd.base.util.SessionUtils;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.entity.*;
import com.dzd.phonebook.page.MobileCheckUtil;
import com.dzd.phonebook.service.*;
import com.dzd.phonebook.util.*;
import com.dzd.phonebook.util.send.api.SendSmsUtil;
import com.github.pagehelper.Page;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.util.*;

/**
 * 客户列表
 * Created by wangran on 2017/7/14.
 */
@Controller
@RequestMapping("/customer")
public class CustomerController extends WebBaseController {
    public static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private MessageLogService messageLogService;

    @Autowired
    private SysUserService<SysUser> sysUserService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private FollowRecordService followRecordService;

    private SmsOrderExportService orderExportService = new SmsOrderExportService();


    /**
     * 查询所有跟进人
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryFollowUser", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse queryFollowUser(HttpServletRequest request) throws Exception {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            SysUser user = SessionUtils.getUser(request);
            if (user.getRole() == 1) {
                //超级管理员，查询所有业务员。其他用户根据属于自己的业务员
                user.setId(null);
            }
            // 查询所有跟进人
            List<SysUser> sysUsers = customerService.queryFollwoUser(user.getId());
            dzdResponse.setData(sysUsers);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            e.printStackTrace();
            dzdResponse = DzdResponseMessage.dzdResponseFail(dzdResponse);
        }
        return dzdResponse;
    }

    /**
     * @Description:客户管理列表--------------------------接口
     * @author:wangran
     * @time:2017年7月14日 下午17:25:34
     */
    @RequestMapping(value = "/customerList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> customerList(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        DzdPageParam dzdPageParam = new DzdPageParam();
        try {
            SysUser user = SessionUtils.getUser(request);
            //请求参数
            JSONObject json = CommUtil.getParams(request);
            Object cname =  json.get("cname");              //客户名称
            Object telephone =  json.get("telephone");       //联系电话
            Object bname =  json.get("bname");  //跟进人
            Object stage =  json.get("stage");  //0:沟通，1：意向 2：成交

            Integer pageNum = (Integer)json.get("pagenum"); //每页显示多少条数据
            Integer pageSize = (Integer)json.get("pagesize"); //每页显示多少条数据
            dzdPageParam.setStart(pageNum);
            dzdPageParam.setLimit(pageSize);

            Map<String, Object> mapData = new HashMap<String, Object>();
            Map sortMap = new HashMap();

            if (cname != null && !StringUtil.isEmpty(cname.toString())) {
                sortMap.put("cname", cname.toString());
            }
            if (telephone != null && !StringUtil.isEmpty(telephone.toString())) {
                sortMap.put("telephone", telephone.toString());
            }
            if (bname != null && !StringUtil.isEmpty(bname.toString())) {
                sortMap.put("bname", bname.toString());
            }
            if (stage != null && !StringUtil.isEmpty(stage.toString())) {
                sortMap.put("stage", stage);
            }

            if (user.getRole() == 3) {
                //业务员查询自己的跟进客户
                sortMap.put("smsUserVal", user.getId());
            } else if (user.getRole() == 2) {
                //经理查询下面所有业务员下面的跟进客户
                sortMap.put("smsUserVal", "select id from sys_user where superiorId = " + user.getId() + "");
            }

          //  sortMap.put("sortVal", "order by updateTime desc");

            dzdPageParam.setCondition(sortMap);

            //统计客户沟通，意向，成交总数
            int stageTotal=customerService.queryStageTotal(dzdPageParam);
            int stageNum0=customerService.queryStageTotal0(dzdPageParam);
            int stageNum1=customerService.queryStageTotal1(dzdPageParam);
            int stageNum2=customerService.queryStageTotal2(dzdPageParam);
            map.put("stageTotal", stageTotal);
            map.put("stage0", stageNum0);
            map.put("stage1", stageNum1);
            map.put("stage2", stageNum2);

            Page<CustomerView> dataList = customerService.queryCustomerPage(dzdPageParam);

            if (user.getRole() == 1) {
                //超级管理员，查询所有业务员。其他用户根据属于自己的业务员
                user.setId(null);
            }
            //查询业务员
            List<SysUserView> userlist = customerService.queryFollwoUser(user.getId());
            mapData.put("user", userlist);

            if (!CollectionUtils.isEmpty(dataList)) {
                mapData.put("data", dataList.getResult());
                //分页总数
                map.put("total", dataList.getTotal());
            }
            List<Header> headers=CommUtil.headersCustomer();
            mapData.put("header", headers);
            map.put("tableData", mapData);
        } catch (Exception e) {
            logger.error("====================》客户管理查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    /**
     * @Description:公海客户管理列表--------------------------接口
     * @author:wangran
     * @time:2017年7月14日 下午17:25:34
     */
    @RequestMapping(value = "/GhcustomerList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> GhcustomerList(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        DzdPageParam dzdPageParam = new DzdPageParam();
        try {
            SysUser user = SessionUtils.getUser(request);
            //请求参数
            JSONObject json = CommUtil.getParams(request);
            String cname = (String) json.get("cname");              //客户名称
            String telephone = (String) json.get("telephone");       //联系电话

            Integer pageNum = (Integer)json.get("pagenum"); //每页显示多少条数据
            Integer pageSize = (Integer)json.get("pagesize"); //每页显示多少条数据
            dzdPageParam.setStart(pageNum);
            dzdPageParam.setLimit(pageSize);

            Map<String, Object> mapData = new HashMap<String, Object>();
            Map sortMap = new HashMap();

            if (cname != null && !StringUtil.isEmpty(cname)) {
                sortMap.put("cname", cname.toString());
            }
            if (telephone != null && !StringUtil.isEmpty(telephone)) {
                sortMap.put("telephone", telephone.toString());
            }

          //  sortMap.put("sortVal", "order by updateTime desc");

            dzdPageParam.setCondition(sortMap);

            Page<CustomerView> dataList = customerService.queryGHCustomerPage(dzdPageParam);

            if (!CollectionUtils.isEmpty(dataList)) {
                mapData.put("data", dataList.getResult());
                //分页总数
                mapData.put("total",dataList.getTotal());
            }
            List<Header> headers=CommUtil.headers2();
            mapData.put("header", headers);
            map.put("tableData", mapData);
        } catch (Exception e) {
            logger.error("====================》公海客户管理查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    /**
     * @Description:根据ID查询账户信息
     * @author:wangran
     * @time:2017年5月20日 上午11:55:53
     */
    @RequestMapping(value = "/queryByCid", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> queryByCid(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        DzdResponse dzdResponse = new DzdResponse();
        try {
            //请求参数
            JSONObject json = CommUtil.getParams(request);
            Integer cid = (Integer) json.get("cid");      //客户cid

            JspResponseBean jspResponseBean = new JspResponseBean();

            Customer customer = customerService.queryById(Integer.parseInt(cid.toString()));
            jspResponseBean.setData(customer);
            dzdResponse.setData(jspResponseBean);

            dzdResponse = DzdResponseMessage.dzdResponseSuccess(dzdResponse);
        } catch (Exception e) {
            logger.error("====================》根据ID查询客户信息失败：" + e.getMessage());
            dzdResponse = DzdResponseMessage.dzdResponseFail(dzdResponse);
            e.printStackTrace();
        }
        map.put("response", dzdResponse);
        return map;
    }

    /**
     * 客户管理新增或修改-----------------接口
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/from/merge", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse merge(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        SysUser user = SessionUtils.getUser(request);
        try {
            //请求参数
            JSONObject json = CommUtil.getParams(request);
            Object cid = json.get("cid");      //客户cid
            Object cname = json.get("cname");  //客户名称
            Object telephone = json.get("telephone");   //联系电话
            Object source = json.get("source");       //客户来源
            Object userid = json.get("userid");          //跟进人id
            Object stage = json.get("stage");          //跟进人id
            Object remark = json.get("remark");        //备注

            Customer customer = new Customer();
            if (cid != null && !StringUtil.isEmpty(cid.toString())) {
                customer.setCid(Integer.parseInt(cid.toString()));
            }
            if (cname != null && !StringUtil.isEmpty(cname.toString())) {
                customer.setCname(cname.toString());
            }
            if (telephone != null && !StringUtil.isEmpty(telephone.toString())) {
                customer.setTelephone(telephone.toString());
            }
            if (stage != null && !StringUtil.isEmpty(stage.toString())) {
                customer.setStage(Integer.parseInt(stage.toString()));
            }
            if (source != null && !StringUtil.isEmpty(source.toString())) {
                customer.setSource(source.toString());
            }
            if (userid != null && !StringUtil.isEmpty(userid.toString())) {
                customer.setUserid(Integer.parseInt(userid.toString()));
            }
            if (remark != null && !StringUtil.isEmpty(remark.toString())) {
                customer.setRemark(remark.toString());
            }

            if (user.getRole() == 3) {
                //如果登陆用户是业务员，跟进人就是自己
                customer.setUserid(user.getId());
            }
            customer.setDeleted(0);
            customer.setIsFromCustomer(0);
            //查询客户表中所有客户电话号码
            Integer num = customerService.queryTelephoneIsHad(customer.getTelephone(), customer.getCid());
            if (num > 0) {
                dzdResponse = DzdResponseMessage.dzdResponseCheck(dzdResponse);
            } else {
                //新增
                if (customer.getCid() == null) {
                    log.info("-----------------》新增客户信息");
                    customerService.insert(customer);
                } else {
                    //修改
                    log.info("-----------------》修改客户信息");
                    customerService.update(customer);
                }
                dzdResponse = DzdResponseMessage.dzdResponseSuccess(dzdResponse);
            }
        } catch (Exception e) {
            logger.error("====================》客户管理新增或者修改失败：" + e.getMessage());
            dzdResponse = DzdResponseMessage.dzdResponseFail(dzdResponse);
            e.printStackTrace();
        }
        return dzdResponse;
    }


    /**
     * @Description:首页点击 立即查看 接口
     * @author:wangran
     * @time:2017年7月14日 下午17:25:34
     */
    @RequestMapping(value = "/viewImmediately", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> viewImmediately(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            SysUser user = SessionUtils.getUser(request);
            //修改业务员下面的所有客户状态
            customerService.updateIsCheck1(user.getId());
            //修改账户is_check 为0
            sysUserService.updateIsCheck0(user.getId());

            map.put("code", ErrorCodeTemplate.CODE_SUCESS);
            map.put("message", ErrorCodeTemplate.MSG_SUCCESS);
        } catch (Exception e) {
            logger.error("====================》立即查看沟通客户失败：" + e.getMessage());
            map.put("code", ErrorCodeTemplate.CODE_FAIL);
            map.put("message", ErrorCodeTemplate.MSG_FAIL);
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 获取客户来源接口
     *
     * @param from   来源
     * @param phone  电话号码
     * @param isSign 是否输入了图文验证码0：没有（试发状态） 1：有（试发成功状态）
     * @param
     */
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public DzdResponse getDate(String from, String phone, Integer isSign) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            Customer customer = new Customer();
            customer.setIsFromCustomer(1);
            customer.setTelephone(phone);
            customer.setSource(from);
            customer.setIsSign(isSign);

            List<Integer> ywIds = new ArrayList<Integer>();

            //查询数据库中是否有相同的数据
            CustomerView cv = customerService.queryRepTelephone(null, phone);

            //表示输入图文验证
            if (isSign == 1) {
                if (cv != null) { //如果已经存在
                    CustomerView csView = customerService.queryRepTelephoneByToday(from, phone);
                    if (csView != null && csView.getIsSign() == 0) {
                        //修改is_sign=1
                        customerService.updateIsSign(csView.getCid());
                        //发送短信
                        //查询业务员sys_user---->is_check=0 是否能够发送短信
                        SysUserView view = sysUserService.queryIsCheck0(cv.getUserid());
                        if (view.getIsCheck() == 0) { //如果业务员能够发短信
                            sendMessage(view, cv.getCid());
                        }
                    } else if (csView != null && csView.getIsSign() == 1
                            && csView.getSource().equals(from)) {
                        dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                        dzdResponse.setRetMsg(ErrorCodeTemplate.NO_CHANGE_SUCCESS);
                        return dzdResponse;
                    } else {
                        dzdResponse = saveData(dzdResponse, customer, ywIds,1);
                        return dzdResponse;
                    }
                } else {
                    //如果没有相同的数据,直接插入到数据库中
                    //查询要分配客户的业务员
                    dzdResponse = saveData(dzdResponse, customer, ywIds,0);
                    return dzdResponse;
                }
            } else {
                //isSing==0
                //查询要分配客户的业务员
                List<SysUserView> sysUsers = customerService.queryClerkUser(1);
                if (sysUsers.size() > 0) {
                    if (isSign == 0 && cv == null) {
                        customer.setDeleted(0);
                        customer.setUserid(sysUsers.get(0).getId());

                        //只分配给业务员，不发送短信
                        customerService.insert(customer);

                        //修改分配业务员的分配时间
                        ywIds.add(sysUsers.get(0).getId());
                        if (ywIds.size() > 0) {
                            customerService.updateAllocationTime(ywIds);
                        }
                    } else if (isSign == 0 && cv != null) {
                        CustomerView cView = customerService.queryRepTelephoneByToday(from, phone);
                        if (cView != null) {
                            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                            dzdResponse.setRetMsg(ErrorCodeTemplate.NO_CHANGE);
                            return dzdResponse;
                        } else {
                            //重复
                            customer.setDeleted(1);
                            //保存数据
                            customerService.insert(customer);
                        }
                    }
                } else {
                    dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                    dzdResponse.setRetMsg(ErrorCodeTemplate.NO_FOUND_YWY);
                    return dzdResponse;
                }
            }
        } catch (Exception ex) {
            log.error(null, ex);
            dzdResponse = DzdResponseMessage.dzdResponseFail(dzdResponse);
            return dzdResponse;
        }
        dzdResponse = DzdResponseMessage.dzdResponseSuccess(dzdResponse);
        return dzdResponse;
    }

    //保存数据
    public DzdResponse saveData(DzdResponse dzdResponse, Customer customer, List<Integer> ywIds,Integer type) {
        //如果没有相同的数据,直接插入到数据库中
        //查询要分配客户的业务员
        List<SysUserView> sysUsers = customerService.queryClerkUser(1);
        if (sysUsers.size() > 0) {
            customer.setDeleted(type);
            customer.setUserid(sysUsers.get(0).getId());  //设置分配人
            customerService.insert(customer);
            if (sysUsers.get(0).getIsCheck() == 0) {
                sendMessage(sysUsers.get(0), customer.getCid());
            }
            //修改分配业务员的分配时间
            ywIds.add(sysUsers.get(0).getId());
            if (ywIds.size() > 0) {
                customerService.updateAllocationTime(ywIds);
            }
            dzdResponse.setRetMsg(ErrorCodeTemplate.MSG_SUCCESS);
        } else {
            log.error("no found sys_user!");
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            dzdResponse.setRetMsg(ErrorCodeTemplate.NO_FOUND_YWY);
            return dzdResponse;
        }
        return dzdResponse;
    }

    //发送短信
    public void sendMessage(SysUserView view, Integer cid) {
        //短信内容
        String sendContent = "【千讯数据】您有待联系客户，请登陆管理后台查看";
        SendSmsUtil.sendMessage(view.getTelephone(), sendContent);
        //添加发送日志
        MessageLog messageLog = new MessageLog();
        messageLog.setUid(view.getId());
        messageLog.setRemark(sendContent);
        //新增发送日志
        messageLogService.insert(messageLog);

        //修改业务员is_checke=1
        sysUserService.updateIsCheck1(view.getId());

        log.info("userPhone=" + view.getTelephone() + "    " + "sendContent=" + sendContent);

        //修改插入客户的状态为已发送isSend=1
        List<Integer> newCids = new ArrayList<Integer>();
        newCids.add(cid);
        customerService.updateIsSend1(newCids);
    }

    /**
     * 获取客户来源接口   一次多个客户
     *
     * @param from   来源
     * @param phone  电话号码
     * @param isSign 是否输入了图文验证码0：没有（试发状态） 1：有（试发成功状态）
     * @param
     */
    @RequestMapping(value = "/getData2", method = RequestMethod.GET)
    @ResponseBody
    public DzdResponse getDate2(String from, String phone, String isSign) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            String[] sourceArray = from.split(",");
            String[] phoneArray = phone.split(",");
            String[] isSignsArray = isSign.split(",");
            List<Customer> customers = new ArrayList<Customer>();

            for (int i = 0; i < phoneArray.length; i++) {
                Customer customer = new Customer();
                customer.setSource(sourceArray[i]);
                customer.setTelephone(phoneArray[i]);
                customer.setIsSign(Integer.parseInt(isSignsArray[i]));  //是否被标记0：没有(试发) 1:标记(试发成功)
                customers.add(customer);
            }

            //过滤客户
            Map<String, Object> map = MobileCheckUtil.mobileAssort3(customers, customerService);

            //得到重复的客户
            List<Customer> dplist = (List<Customer>) map.get("dplist");

            //保存重复的客户
            if (dplist.size() > 0) {
                customerService.insertList(dplist);
            }

            //需要分配业务员的客户
            List<Customer> ndpist = (List<Customer>) map.get("ndplist");

            int nums = ndpist.size();
            if (nums == 0) {
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                dzdResponse.setRetMsg("没有要分配的客户");
                return dzdResponse;
            }

            //查询要分配客户的业务员
            List<SysUserView> sysUsers = customerService.queryClerkUser(nums);

            //要发送短信的业务员id
            List<Integer> userids = new ArrayList<Integer>();
            //要发送短信的业务员手机号码
            List<String> userPhones = new ArrayList<String>();
            //要分配的业务员id
            List<Integer> ywIds = new ArrayList<Integer>();

            if (sysUsers.size() > 0) {
                for (int i = 0; i < ndpist.size(); i++) {
                    SysUserView sysUser = sysUsers.get(i % sysUsers.size());
                    if (!userids.contains(sysUser.getId())) {
                        //查询能够接收短信的业务员sys_user---->is_check=0
                        SysUserView sUV = sysUserService.queryIsCheck0(sysUser.getId());
                        if (sUV.getIsCheck() == 0) { //判断是否能接收短信,不能接收短信只分配客户不发送信息
                            userids.add(sysUser.getId());      //分配业务员的id
                            userPhones.add(sysUser.getTelephone()); //分配的业务员电话号码
                        }
                    }
                    ywIds.add(sysUser.getId());
                    ndpist.get(i).setUserid(sysUser.getId()); //设置分配人
                    ndpist.get(i).setDeleted(0);
                }

                //将分配的客户保存到数据库
                if (ndpist.size() > 0) {
                    //保存到数据库
                    customerService.insertList(ndpist);

                    //sql = "update sys_user set allocation_time=now() where id in( )";
                    //修改分配业务员的分配时间
                    if (ywIds.size() > 0) {
                        customerService.updateAllocationTime(ywIds);
                    }
                }

                //to phone[userPhone] message 发送短信
                //短信内容
                String sendContent = "【千讯数据】您有待联系客户，请登陆管理后台查看";

                //遍历所有得到分配客户并且能够收短信的业务员，然后发送短信
                for (int n = 0; n < userids.size(); n++) {
                    Integer isSign1 = customerService.queryIsSign1(userids.get(n));
                    if (isSign1 > 0) {
                        //如果分配的客户中包含图文验证，那么就给业务员发送短信，否则不发送短信
                        SendSmsUtil.sendMessage(userPhones.get(n), sendContent);
                        //添加发送日志
                        MessageLog messageLog = new MessageLog();
                        messageLog.setUid(userids.get(n));
                        messageLog.setRemark(sendContent);
                        //新增发送日志
                        messageLogService.insert(messageLog);

                        //修改插入客户的状态为已发送isSend=1
                        List<Integer> newCids = new ArrayList<Integer>();
                        for (Customer cu : ndpist) {
                            newCids.add(cu.getCid());
                        }
                        customerService.updateIsSend1(newCids);

                        //update sys_user set is_check=1 wait user to check
                        //修改业务员is_checke=1
                        sysUserService.updateIsCheck1(userids.get(n));

                        log.info("userPhone=" + userPhones.get(n) + "    " + "sendContent=" + sendContent);
                    }
                }
            } else {
                log.error("no found sys_user!");
                dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
                dzdResponse.setRetMsg("没有找到可以分配的业务员");
                return dzdResponse;
            }
            dzdResponse = DzdResponseMessage.dzdResponseSuccess(dzdResponse);
        } catch (Exception ex) {
            logger.error(null, ex);
            dzdResponse = DzdResponseMessage.dzdResponseFail(dzdResponse);
        }
        return dzdResponse;
    }

    /**
     * @Description:新增跟进内容
     * @author:wangran
     * @time:2017年7月17日 上午19:56:29
     */
    @RequestMapping(value = "/addFollowRecord", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse addFollowRecord(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            SysUser user = SessionUtils.getUser(request);
            //请求参数
            JSONObject json = CommUtil.getParams(request);
            Object userid = json.get("userid");      //客户根据人id
            Object cid = json.get("cid");      //客户id
            Object content = json.get("content");  //跟进内容

            FollowRecord followRecord = new FollowRecord();

            if (userid != null && !StringUtil.isEmpty(userid.toString())) {
                followRecord.setUserid(Integer.parseInt(userid.toString()));
            } else {
                if (user.getRole() == 3) {
                    //如果是业务员新增跟进信息，则跟进人是自己，其他角色新增跟进信息跟进人必须是下属业务员
                    followRecord.setUserid(user.getId());
                }
            }

            if (cid != null && !StringUtil.isEmpty(cid.toString())) {
                followRecord.setCid(Integer.parseInt(cid.toString()));
            }

            if (content != null && !StringUtil.isEmpty(content.toString())) {
                followRecord.setContent(content.toString());
            }

            //新增客户的跟进记录
            followRecordService.insert(followRecord);

            //修改客户的更新时间
            Customer customer = new Customer();
            customer.setCid(Integer.parseInt(cid.toString()));
            customerService.update(customer);

            dzdResponse = DzdResponseMessage.dzdResponseSuccess(dzdResponse);
        } catch (Exception e) {
            logger.error("====================》新增跟进记录失败：" + e.getMessage());
            dzdResponse = DzdResponseMessage.dzdResponseFail(dzdResponse);
            e.printStackTrace();
        }
        return dzdResponse;
    }

    /**
     * @Description:释放
     * @author:wangran
     * @time:2017年7月17日 上午20:11:20
     */
    @RequestMapping(value = "/release", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse release(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();  //请求参数
        try {
            JSONObject json = CommUtil.getParams(request);
            Object cid = json.get("cid");    //被释放人id
            customerService.updateCustomerUserId(Integer.parseInt(cid.toString()));
            dzdResponse = DzdResponseMessage.dzdResponseSuccess(dzdResponse);
        } catch (Exception e) {
            logger.error("====================》释放到公海的客户失败：" + e.getMessage());
            dzdResponse = DzdResponseMessage.dzdResponseFail(dzdResponse);
            e.printStackTrace();
        }
        return dzdResponse;
    }

    /**
     * @Description:批量修改跟进人
     * @author:wangran
     * @time:2017年7月17日 上午20:11:20
     */
    @RequestMapping(value = "/updateList", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse updateLis(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            JSONObject json = CommUtil.getParams(request);
            Object userid = json.get("userid");  //跟进人id
            Object ids = json.get("ids");  //要修改的客户id
            List cids = new ArrayList();
            if (ids != null && !StringUtil.isEmpty(ids.toString())) {
                String[] str = ids.toString().split(",");
                for (String s : str) {
                    cids.add(s);
                }
            }
            Map map = new HashMap();
            map.put("userid", userid);
            map.put("ids", cids);
            map.put("stage", 1);
            customerService.updateList(map);
            dzdResponse = DzdResponseMessage.dzdResponseSuccess(dzdResponse);
        } catch (Exception e) {
            logger.error("====================》修改跟进人失败：" + e.getMessage());
            dzdResponse = DzdResponseMessage.dzdResponseFail(dzdResponse);
            e.printStackTrace();
        }
        return dzdResponse;
    }

    /**
     * @Description:加入到沟通列表
     * @author:wangran
     * @time:2017年7月17日 上午20:11:20
     */
    @RequestMapping(value = "/addStage0List", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse addStage0List(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            JSONObject json = CommUtil.getParams(request);
            SysUser user = SessionUtils.getUser(request);
            Object ids = json.get("ids");  //要修改的客户id

            List cids = new ArrayList();
            if (ids != null && !StringUtil.isEmpty(ids.toString())) {
                String[] str = ids.toString().split(",");
                for (String s : str) {
                    cids.add(s);
                }
            }
            Map map = new HashMap();
            map.put("userid", user.getId());
            map.put("ids", cids);
            map.put("stage", 0);
            customerService.updateList(map);
            dzdResponse = DzdResponseMessage.dzdResponseSuccess(dzdResponse);
        } catch (Exception e) {
            logger.error("====================》添加到沟通列表失败：" + e.getMessage());
            dzdResponse = DzdResponseMessage.dzdResponseFail(dzdResponse);
            e.printStackTrace();
        }
        return dzdResponse;
    }

    /**
     * @Description:账户删除
     * @author:wangran
     * @time:2017年5月20日 上午12:55:53
     */
    @MethodDescription("删除客户")
    @RequestMapping(value = "/apply/delete", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse apply_delete(HttpServletRequest request, @RequestParam("ids[]") List<Integer> ids, @RequestParam("sysUserIds[]") List<Integer> sysUserIds) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            sysUserService.deleteUsers(sysUserIds);
            dzdResponse = DzdResponseMessage.dzdResponseSuccess(dzdResponse);
        } catch (Exception e) {
            logger.error("====================》用户删除失败：" + e.getMessage());
            dzdResponse = DzdResponseMessage.dzdResponseFail(dzdResponse);
            e.printStackTrace();
        }
        return dzdResponse;
    }


    /**
     * @Description:根据用户ID查询拥有角色
     * @author:oygy
     * @time:2016年12月31日 上午11:12:53
     */
    public static List<SysRoleRel> queryRoleByUserId(Integer uid, SysRoleRelService sysRoleRelService) {
        DzdPageParam dzdPageParam = new DzdPageParam();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("sysUserId", uid);
        dzdPageParam.setCondition(condition);
        List<SysRoleRel> sysRoleRels = sysRoleRelService.queryRoleByUserId(dzdPageParam);

        return sysRoleRels;
    }


    /**
     * 新增客户时 上传文件
     *
     * @param uploadFile
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public DzdResponse upload(@RequestParam final MultipartFile[] uploadFile, final HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();

        try {
            //获取上传文件的名字
            String fName = uploadFile[0].getOriginalFilename();
            if (uploadFile[0].getSize() > Integer.MAX_VALUE || fName.equals("") || fName == null) {
                dzdResponse = DzdResponseMessage.dzdResponseFail(dzdResponse);
                return dzdResponse;
            }
            // 上传文件到服务器
            String fileName = uploadFile[0].getOriginalFilename();// 原来的文件名称
            String uploadFileName = fileName.substring(0, fileName.lastIndexOf("."));// 去除后缀的文件名
            String suffix = fileName.substring(fileName.lastIndexOf("."));// 后缀 .txt
            fileName = uploadFileName + "dzdqw" + RandomUtil.getRandomTenThousand() + suffix;// 拼接随机数生成的文件名

            FileUploadUtil.saveFileInfo(uploadFile[0].getInputStream(), new byte[(int) uploadFile[0].getSize()], fileName);
            dzdResponse = DzdResponseMessage.dzdResponseSuccess(dzdResponse);
        } catch (Exception ex) {
            ex.printStackTrace();
            dzdResponse = DzdResponseMessage.dzdResponseFail(dzdResponse);
            logger.error("====================》上传文件发生了异常：" + ex.getMessage());
        }
        return dzdResponse;
    }

    /**
     * 文件下载
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/download")
    public String download(HttpServletRequest request, HttpServletResponse response) {
        String fileName = "2.xlsx";
        System.out.println(fileName);
        response.setContentType("text/html;charset=utf-8"); /*设定相应类型 编码*/
        try {
            request.setCharacterEncoding("UTF-8");//设定请求字符编码
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        java.io.BufferedInputStream bis = null;//创建输入输出流
        java.io.BufferedOutputStream bos = null;

        // String ctxPath = request.getSession().getServletContext().getRealPath("/") + "upload/";//获取文件真实路径

        String path = System.getProperty("catalina.home") + "/fileUpload/" + fileName;
        String downLoadPath = path;
        System.out.println(downLoadPath);
        try {
            long fileLength = new File(downLoadPath).length();//获取文件长度
            response.setContentType("application/x-msdownload;");//下面这三行是固定形式
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(fileLength));
            bis = new BufferedInputStream(new FileInputStream(downLoadPath));//创建输入输出流实例
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];//创建字节缓冲大小
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                try {
                    bis.close();//关闭输入流
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (bos != null)
                try {
                    bos.close();//关闭输出流
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return null;
    }


    /**
     * 批量导入
     *
     * @param uploadFile
     * @param request
     * @return
     */
    @RequestMapping("/fileUpload")
    @ResponseBody
    public DzdResponse fileUpload(@RequestParam  MultipartFile[] uploadFile,HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        Object temp=request.getParameter("temp");
        try {
            // 1. 获取文件
            String fName = uploadFile[0].getOriginalFilename();
            if (uploadFile[0].getSize() > Integer.MAX_VALUE || fName.equals("") || fName == null) {
                dzdResponse = DzdResponseMessage.dzdResponseFail(dzdResponse);
                return dzdResponse;
            }

            // 2. 获取文件后缀
            String suffix = fName.substring(fName.lastIndexOf("."));
            final SysUser user = SessionUtils.getUser(request);

            // 3. 判断文件格式
            if (suffix.equals(".xlsx") || suffix.equals(".xls")) {
                // 4. 上传文件到服务器
                String fileName = FileUploadUtil.saveFile(uploadFile);

                // 5. 读取文件中的号码,进行分类得到个数
                Map<String, Object> map = FileUploadUtil.getSmsPhoneCategoryMap(uploadFile,user,customerService,sysUserService,temp);
                JSONObject json = new JSONObject();
                json.putAll(map);
                dzdResponse.setData(json);
                dzdResponse = DzdResponseMessage.dzdResponseSuccess(dzdResponse);
            } else {
                // 7. 文件格式不正确提示
                Map<String, String> map = new HashMap<String, String>();
                map.put("msg", "请选择excel文件");
                dzdResponse.setData(map);
                dzdResponse = DzdResponseMessage.dzdResponseFail(dzdResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            dzdResponse = DzdResponseMessage.dzdResponseFail(dzdResponse);
            logger.error("====================》上传文件发生了异常：" + e.getMessage());
        }
        return dzdResponse;
    }

    /**
     * 导出客户信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/exportData", method = RequestMethod.GET)
    @ResponseBody
    public void exportData(HttpServletRequest request, HttpServletResponse response) {
        DzdResponse dzdResponse = new DzdResponse();
        //请求参数
        String cname = request.getParameter("cname");
        String telephone = request.getParameter("telephone");
        String bname = request.getParameter("bname");
        String stage = request.getParameter("stage");
        String cids = request.getParameter("ids");

        DzdPageParam dzdPageParam = new DzdPageParam();
        Map<String, Object> sortMap = new HashMap<String, Object>();
        if (cname != null && !StringUtil.isEmpty(cname.toString()) && !cname.equals("undefined")) {
            sortMap.put("cname", cname);
        }
        if (telephone != null && !StringUtil.isEmpty(telephone.toString()) && !telephone.equals("undefined")) {
            sortMap.put("telephone", telephone);
        }
        if (bname != null && !StringUtil.isEmpty(bname.toString()) && !bname.equals("undefined")) {
            sortMap.put("bname", bname);
        }
        if (stage != null && !StringUtil.isEmpty(stage.toString()) && !stage.equals("undefined")) {
            sortMap.put("stage", stage);
        }

        List ids = new ArrayList();
        if (cids != null && !StringUtil.isEmpty(cids.toString())) {
            String[] str = cids.split(",");
            for (String s : str) {
                ids.add(s);
            }
        }
        sortMap.put("ids", ids);

        sortMap.put("sortVal", "order by createTime desc");
        dzdPageParam.setCondition(sortMap);

        Page<Customer> dataList = customerService.exportCustomer(dzdPageParam);

 /*       if (dataList == null || CollectionUtils.isEmpty(dataList.getResult())) {
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_PARAMETER_ERROR);
            logger.error(
                    " no hava data");
        } else {*/
        orderExportService.customerExport(request, response, dataList);
        dzdResponse = DzdResponseMessage.dzdResponseSuccess(dzdResponse);
       /* }*/
    }

}
