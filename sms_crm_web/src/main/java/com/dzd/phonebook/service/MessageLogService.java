package com.dzd.phonebook.service;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.CustomerDao;
import com.dzd.phonebook.dao.MessageLogDao;
import com.dzd.phonebook.entity.Customer;
import com.dzd.phonebook.entity.CustomerView;
import com.dzd.phonebook.entity.MessageLog;
import com.dzd.phonebook.entity.SysUserView;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 发送日志
 *
 * @author
 * @date 2017-7-22
 */
@Service("messageLogService")
public class MessageLogService<T> extends BaseService<T> {
	private final static Logger log = Logger.getLogger(MessageLogService.class);

	@Autowired
	private MessageLogDao<T> mapper;

	public MessageLogDao<T> getDao() {
		return mapper;
	}

	/**
	 * 新增客户
	 * @parammessageLog
	 */
	public void insert(MessageLog messageLog){
		mapper.add((T)messageLog);
	}

}
