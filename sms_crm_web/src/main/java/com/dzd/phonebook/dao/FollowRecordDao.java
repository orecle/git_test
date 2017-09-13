package com.dzd.phonebook.dao;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.Customer;
import com.dzd.phonebook.entity.CustomerView;
import com.dzd.phonebook.entity.FollowRecord;
import com.dzd.phonebook.entity.FollowRecordView;
import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 跟进记录接口
 *
 * @author wangran
 * @date 2017-7-17
 */
public interface FollowRecordDao<T> extends BaseDao<T> {

    /**
     * 保存跟进记录
     * @param followRecord
     */
      public void insert(FollowRecord followRecord);

    /**
     * 查询客户最近2天的跟进记录
     * @param id 登陆经理id
     * @return
     */
      public List<FollowRecordView> queryFollowRecord2Day(@Param("id") Integer id);

    /**
     * 公海客户列表
     * @param dzdPageParam
     * @return
     */
    Page<CustomerView> queryGHCustomer(DzdPageParam dzdPageParam);

    /**
     * 修改跟进人(批量修改)
     * @param map
     */
    public void addToCustomer(Map map);

    /**
     * 查看客户的跟进记录
     * @param cid
     * @return
     */
    public List<FollowRecordView> queryFollowRecodByCid(@Param("cid") Integer cid);

}
