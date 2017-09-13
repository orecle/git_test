package com.dzd.phonebook.controller;

import com.dzd.base.util.SessionUtils;
import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.controller.base.WebBaseController;
import com.dzd.phonebook.entity.*;
import com.dzd.phonebook.service.*;
import com.dzd.phonebook.util.*;
import com.github.pagehelper.Page;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 客户列表
 * Created by wangran on 2017/7/27.
 */
@Controller
@RequestMapping("/fCustomerS")
public class FromCustomerSatisticsController extends WebBaseController {
    public static final Logger log = LoggerFactory.getLogger(FromCustomerSatisticsController.class);

    @Autowired
    private CustomerService customerService;


    /**
     * @Description:推广客户统计列表--------------------------接口
     * @author:wangran
     * @time:2017年7月14日 下午17:25:34
     */
    @RequestMapping(value = "/fCustomerSList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> pCustomerSList(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        DzdPageParam dzdPageParam = new DzdPageParam();
        try {
            SysUser user = SessionUtils.getUser(request);
            //请求参数
            JSONObject json = CommUtil.getParams(request);
            String startTime = (String) json.get("startTime");    //开始时间
            String endTime = (String) json.get("endTime");       //结束时间
            String source = (String) json.get("source");         //来源

            Integer pageNum = (Integer)json.get("pagenum"); //每页显示多少条数据
            Integer pageSize = (Integer)json.get("pagesize"); //每页显示多少条数据

            dzdPageParam.setStart(pageNum);
            dzdPageParam.setLimit(pageSize);

            Map<String, Object> mapData = new HashMap<String, Object>();
            Map sortMap = new HashMap();

            if (startTime != null && !StringUtil.isEmpty(startTime)) {
                sortMap.put("startTime", startTime.toString());
            }
            if (endTime != null && !StringUtil.isEmpty(endTime)) {
                sortMap.put("endTime", endTime.toString());
            }
            if (source != null && !StringUtil.isEmpty(source)) {
                sortMap.put("source", source.toString());
            }

            if (user.getRole() == 3) {
                //业务员查询自己的跟进客户
                sortMap.put("smsUserVal", user.getId());
            } else if (user.getRole() == 2) {
                //经理查询下面所有业务员下面的跟进客户
                sortMap.put("smsUserVal", "select id from sys_user where superiorId = " + user.getId() + "");
            }

            dzdPageParam.setCondition(sortMap);
            Page<FromCustomerSatistics> dataList = customerService.queryFromCustomerSatisticsPage(dzdPageParam);

            List<Header> headers = CommUtil.fromCustomerHeaders();
            mapData.put("header", headers);
            if (!CollectionUtils.isEmpty(dataList)) {
                mapData.put("data", dataList.getResult());
                mapData.put("total",dataList.getTotal());
            }
            map.put("tableData", mapData);
        } catch (Exception e) {
            logger.error("====================》推广客户统计查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }


    /**
     * @Description:推广统计详情列表--------------------------接口
     * @author:wangran
     * @time:2017年7月14日 下午17:25:34
     */
    @RequestMapping(value = "/customerTgDeatil", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> customerTgDeatil(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        DzdPageParam dzdPageParam = new DzdPageParam();
        try {
            //请求参数
            JSONObject json = CommUtil.getParams(request);
            Object date=json.get("date");              //日期
            Object source=json.get("source");          //来源
            Object cname =  json.get("cname");              //客户名称
            Object telephone =  json.get("telephone");       //联系电话

            Integer pageNum = (Integer)json.get("pagenum"); //每页显示多少条数据
            Integer pageSize = (Integer)json.get("pagesize"); //每页显示多少条数据
            dzdPageParam.setStart(pageNum);
            dzdPageParam.setLimit(pageSize);

            Map<String, Object> mapData = new HashMap<String, Object>();
            Map sortMap = new HashMap();

            if (date != null && !StringUtil.isEmpty(date.toString())) {
                sortMap.put("date", date.toString());
            }
            if (source != null && !StringUtil.isEmpty(source.toString())) {
                sortMap.put("source", source.toString());
            }
            if (cname != null && !StringUtil.isEmpty(cname.toString())) {
                sortMap.put("cname", cname.toString());
            }
            if (telephone != null && !StringUtil.isEmpty(telephone.toString())) {
                sortMap.put("telephone", telephone.toString());
            }
            dzdPageParam.setCondition(sortMap);
            Page<CustomerView> dataList = customerService.queryTgDetailPage(dzdPageParam);

            /**
             * 重复的电话号码标记为"重复"
             */
            for(int i=0;i<dataList.size();i++){
                CustomerView v =dataList.get(i);
                if(v.getDeleted() == 1){
                    dataList.get(i).setTelephone(v.getTelephone()+"（重复）");
                }
            }

            List<Header> headers = CommUtil.headersDetail();
            mapData.put("header", headers);
            if (!CollectionUtils.isEmpty(dataList)) {
                mapData.put("data", dataList.getResult());
                mapData.put("total",dataList.getTotal());
            }
            map.put("tableData", mapData);

        } catch (Exception e) {
            logger.error("====================》推广统计详情查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }
}
