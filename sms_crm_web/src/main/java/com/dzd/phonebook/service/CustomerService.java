package com.dzd.phonebook.service;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.CustomerDao;


import com.dzd.phonebook.entity.*;
import com.dzd.phonebook.util.DzdPageParam;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

/**
 * 系统用户服务类
 *
 * @author chenchao
 * @date 2016-6-24
 */
@Service("customerService")
public class CustomerService<T> extends BaseService<T> {
	private final static Logger log = Logger.getLogger(CustomerService.class);

	@Autowired
	private CustomerDao<T> mapper;

	public CustomerDao<T> getDao() {
		return mapper;
	}


	/**
	 * 新增客户
	 * @param customer
	 */
	public void insert(Customer customer){
		mapper.add((T)customer);
	}

	/**
	 * 修改客户信息
	 * @param customer
	 */
	public void update(Customer customer){
		mapper.update((T)customer);
	}

	/***
	 * 释放客户到公海---解除和跟进人关系
	 * @param cid
	 */
	public void updateCustomerUserId(Integer cid){
		mapper.updateCustomerUserId(cid);
	}

	/**
	 * 修改跟进人(批量修改)
	 * @param map
	 */
	public void updateList(Map map){
		mapper.updateList(map);
	}

	/**
	 * 导入客户信息(批量导入电话号码)
	 * @param list
	 */
	public void insertList(List<Customer> list){
		mapper.insertList(list);
	}

	/**
	 * 查询客户信息
	 * @param cid
	 * @return
	 */
	public Customer queryById(@Param("cid") Integer cid){
		return mapper.queryById(cid);
	}

	/**
	 * 查询所有跟进人（业务员）
	 * @return
	 */
	public List<SysUserView> queryFollwoUser(Integer id){
		return mapper.queryFollwoUser(id);
	}

	/**
	 * 导出客户信息
	 * @param dzdPageParam
	 * @return
	 */
	public Page<Customer> exportCustomer(DzdPageParam dzdPageParam){
		return mapper.exportCustomer(dzdPageParam);
	}

	/**
	 * 公海客户列表接口
	 * @param dzdPageParam
	 * @return
	 */
	public Page<CustomerView> queryGHCustomerPage(DzdPageParam dzdPageParam){
		return mapper.queryGHCustomerPage(dzdPageParam);
	}


	/**
	 * 查询系统中所有的电话号码
	 * @return
	 */
	public List<String> queryTelephoneAll(){
		return mapper.queryTelephoneAll();
	}

	/**
	 * 查询电话号码是否已经存在
	 * @param telephone
	 * @param cid
	 * @return
	 */
	public Integer queryTelephoneIsHad(String telephone,Integer cid){
		return mapper.queryTelephoneIsHad(telephone,cid);
	}

	/**
	 * 客户列表接口
	 * @param dzdPageParam
	 * @return
	 */
	public Page<CustomerView> queryCustomerPage(DzdPageParam dzdPageParam){
		return mapper.queryCustomerPage(dzdPageParam);
	}

	/**
	 *   查询新增沟通客户（首页，业务员登陆）
	 */
	public List<CustomerView> queryCustomerStage0(Integer userid){
		return mapper.queryCustomerStage0(userid);
	}

	/**
	 * 修改客户是否被查看状态
	 * @param cid
	 */
	public void updateIsCheck1(Integer cid){
		mapper.updateIsCheck1(cid);
	}

	/**
	 * 查看登陆业务员是否有新的客户
	 */

	public Integer queryNewCustomer(Integer userid){
		return mapper.queryNewCustomer(userid);
	}

	/**
	 * 查看是否有自动分配客户功能的业务员
	 * @return
	 */
	public List queryUserIsCheck(){
		return mapper.queryUserIsCheck();
	}


	/**
	 * 查询要分配的业务员
	 * @param nums
	 * @return
	 */
	public List<SysUserView> queryClerkUser(Integer nums){
		return mapper.queryClerkUser(nums);
	}

	/**
	 * 修改分配客户业务员的分配时间
	 * @param ids
	 */
	public void updateAllocationTime(List ids){
		mapper.updateAllocationTime(ids);
	}

	/**
	 * 修改客户发送状态
	 * @param ids
	 */
	public void updateIsSend1(List ids){
		mapper.updateIsSend1(ids);
	}

	/**
	 * 推广客户统计列表
	 * @param dzdPageParam
	 * @return
	 */
	public Page<FromCustomerSatistics> queryFromCustomerSatisticsPage(DzdPageParam dzdPageParam){
		return mapper.queryFromCustomerSatisticsPage(dzdPageParam);
	}

	/**
	 *  查询是否有重复发送数据
	 */
	public CustomerView queryRepTelephone(@Param("source") String source,@Param("telephone") String telephone){
		return mapper.queryRepTelephone(source,telephone);
	}

	/**
	 * 修改si_sign
	 * @param cid
	 */
	public void updateIsSign(@Param("cid") Integer cid){
		mapper.updateIsSign(cid);
	}

	/**
	 * 查询分配的业务员是否客户中有图文标记的客户
	 */
	public Integer queryIsSign1(@Param("userid") Integer userid){
		return mapper.queryIsSign1(userid);
	}

	/**
	 * 查询当天重复的数据中是否包含要添加的数据
	 * @param source
	 * @param telephone
	 * @return
	 */
	public CustomerView queryRepTelephoneByToday(@Param("source") String source,@Param("telephone") String telephone){
		return mapper.queryRepTelephoneByToday(source,telephone);
	}
	/**
	 * 推广统计详情
	 * @return
	 */
	public Page<CustomerView> queryTgDetailPage(DzdPageParam dzdPageParam){
		return mapper.queryTgDetailPage(dzdPageParam);
	}

	/**
	 * 统计客户stage
	 * @param dzdPageParam
	 * @return
	 */
	public Integer queryStageTotal(DzdPageParam dzdPageParam){
		return mapper.queryStageTotal(dzdPageParam);
	}
	public Integer queryStageTotal0(DzdPageParam dzdPageParam){
		return mapper.queryStageTotal0(dzdPageParam);
	}
	public Integer queryStageTotal1(DzdPageParam dzdPageParam){
		return mapper.queryStageTotal1(dzdPageParam);
	}
	public Integer queryStageTotal2(DzdPageParam dzdPageParam){
		return mapper.queryStageTotal2(dzdPageParam);
	}
}
