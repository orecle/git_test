package com.dzd.phonebook.service;

import com.dzd.phonebook.entity.Customer;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 发送短信服务类
 * 
 * @author CHENCHAO
 * @date 2017-03-28 09:33:00
 *
 */
public class SmsOrderExportService
{
	public static final Logger logger = LoggerFactory.getLogger(SmsOrderExportService.class);

	//客户信息管理导出
	public void customerExport(HttpServletRequest request, HttpServletResponse response,
									Page<Customer> dataList)
	{
		CustomerBusiness customerBusiness = new CustomerBusiness();
		customerBusiness.export(request, response, dataList);
	}
}
