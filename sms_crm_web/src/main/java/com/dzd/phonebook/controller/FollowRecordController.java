package com.dzd.phonebook.controller;

import com.dzd.base.util.SessionUtils;

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

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 跟进记录
 * Created by wangran on 2017/7/19.
 */
@Controller
@RequestMapping("/followRecord")
public class FollowRecordController extends WebBaseController {
    public static final Logger log = LoggerFactory.getLogger(FollowRecordController.class);


    @Autowired
    private FollowRecordService followRecordService;

    @Autowired
    private CustomerService customerService;

    /**
     * @Description:查询客户的跟进记录
     * @author:wangran
     * @time:2017年7月14日 下午17:25:34
     */
    @RequestMapping(value = "/ghCustomerList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> ghCustomerList(HttpServletRequest request) throws Exception {
        Map<String,Object> map=new HashMap<String, Object>();
        try {
            //请求参数
            JSONObject json= CommUtil.getParams(request);
            Object cid = json.get("cid");
            List<FollowRecordView> dataList = followRecordService.queryFollowRecodByCid(Integer.parseInt(cid.toString()));
            map.put("tableData",dataList);
        } catch (Exception e) {
            logger.error("====================》查询客户的跟进记录失败：" + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }
}
