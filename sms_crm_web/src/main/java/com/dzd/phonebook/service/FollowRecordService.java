package com.dzd.phonebook.service;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.CustomerDao;
import com.dzd.phonebook.dao.FollowRecordDao;
import com.dzd.phonebook.entity.Customer;
import com.dzd.phonebook.entity.CustomerView;
import com.dzd.phonebook.entity.FollowRecord;
import com.dzd.phonebook.entity.FollowRecordView;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 跟进记录
 *
 * @author
 * @date 2016-6-24
 */
@Service("followRecordService")
public class FollowRecordService<T> extends BaseService<T> {
	private final static Logger log = Logger.getLogger(FollowRecordService.class);

	@Autowired
	private FollowRecordDao<T> mapper;

	public FollowRecordDao<T> getDao() {
		return mapper;
	}

	/**
	 * 保存跟进记录
	 * @param followRecord
	 */
	public void insert(FollowRecord followRecord){
		mapper.insert(followRecord);
	}

	/**
	 * 查询客户最近二天的跟进记录
	 * @param id 登陆经理id
	 * @return
	 */
	public List<FollowRecordView> queryFollowRecord2Day(Integer id){
		return mapper.queryFollowRecord2Day(id);
	}

	/**
	 * 公海客户列表接口
	 * @param dzdPageParam
	 * @return
	 */
	public Page<CustomerView> queryGHCustomer(DzdPageParam dzdPageParam){
		return mapper.queryGHCustomer(dzdPageParam);
	}
	/**
	 * 修改跟进人(批量修改)
	 * @param userid 跟进人id
	 * @param cids 客户id
	 */
	public void addToCustomer(Integer userid,List cids){
		Map map=new HashMap();
		map.put("userid",userid);
		map.put("ids",cids);
		mapper.addToCustomer(map);
	}
	/**
	 * 查看客户的跟进记录
	 * @param cid
	 * @return
	 */
	public List<FollowRecordView> queryFollowRecodByCid(@Param("cid") Integer cid){
		return mapper.queryFollowRecodByCid(cid);
	}
}
