package com.dzd.phonebook.dao;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.Customer;
import com.dzd.phonebook.entity.CustomerView;
import com.dzd.phonebook.entity.SysUserView;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 发送日志
 *
 * @author wangran
 * @date 2017-7-14
 */
public interface MessageLogDao<T> extends BaseDao<T> {

}
