package com.dzd.phonebook.dao;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.*;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.DzdResponse;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 客户管理接口
 *
 * @author wangran
 * @date 2017-7-14
 */
public interface CustomerDao<T> extends BaseDao<T> {


    /**
     * 客户列表接口
     * @param dzdPageParam
     * @return
     */
   public Page<CustomerView> queryCustomerPage(DzdPageParam dzdPageParam);

  /**
   * 公海客户列表接口
   * @param dzdPageParam
   * @return
   */
  public Page<CustomerView> queryGHCustomerPage(DzdPageParam dzdPageParam);


    /**
     * 导出客户信息
     * @param dzdPageParam
     * @return
     */
    Page<Customer> exportCustomer(DzdPageParam dzdPageParam);

    /***
     * 释放客户到公海---解除和跟进人关系
     * @param cid
     */
    public void updateCustomerUserId(@Param("cid") Integer cid);

    /**
     * 修改跟进人(批量修改)
     * @param map
     */
    public void updateList(Map map);

    /**
     * 导入客户信息(批量导入电话号码)
     * @param list
     */
    public void insertList(List<Customer> list);

    /**
     * 查询客户信息
     * @param cid
     * @return
     */
    public Customer queryById(@Param("cid") Integer cid);

    /**
     * 查询所有跟进人（业务员）
     * @id 登陆人id
     * @return
     */
    public List<SysUserView> queryFollwoUser(@Param("id") Integer id);

    /**
     * 查询系统中所有电话号码
     * @return
     */
    public List<String> queryTelephoneAll();

    /**
     * 查询电话号码是否已经存在
     * @param telephone
     * @param cid
     * @return
     */
    public Integer queryTelephoneIsHad(@Param("telephone") String telephone,@Param("cid") Integer cid);

    /**
     *   查询新增沟通客户（首页，业务员登陆）
     */
    public List<CustomerView> queryCustomerStage0(@Param("userid") Integer userid);

    /**
     * 修改客户是否被查看状态
     * @param cid
     */
    public void updateIsCheck1(@Param("cid") Integer cid);

    /**
     * 查看业务员是否有新的客户
     */

    public Integer queryNewCustomer(@Param("userid") Integer userid);

    /**
     * 查看否有自动分配客户功能的业务员
     * @return
     */
    public List queryUserIsCheck();


    /**
     * 查询要分配的业务员
     * @param nums
     * @return
     */
    public List<SysUserView> queryClerkUser(@Param("nums") Integer nums);

    /**
     * 修改分配客户业务员的分配时间
     * @param ids
     */
    public void updateAllocationTime(@Param("ids") List ids);

    /**
     * 修改客户发送状态
     * @param ids
     */
    public void updateIsSend1(@Param("ids") List ids);

    /**
     * 推广客户统计列表
     * @param dzdPageParam
     * @return
     */
    Page<FromCustomerSatistics> queryFromCustomerSatisticsPage(DzdPageParam dzdPageParam);

    /**
     *     查询是否有重复发送数据
     */
    public CustomerView queryRepTelephone(@Param("source") String source,@Param("telephone") String telephone);

    /**
     * 修改si_sign
     * @param cid
     */
    public void updateIsSign(@Param("cid") Integer cid);

    /**
     * 查询分配的业务员是否客户中有图文标记的客户
     */
    public Integer queryIsSign1(@Param("userid") Integer userid);

    /**
     * 查询当天重复的数据中是否包含要添加的数据
     * @param source
     * @param telephone
     * @return
     */
    public CustomerView queryRepTelephoneByToday(@Param("source") String source,@Param("telephone") String telephone);

    /**
     * 推广统计详情
     * @return
     */
    public Page<CustomerView> queryTgDetailPage(DzdPageParam dzdPageParam);

  /**
   * 统计客户stage
   * @param dzdPageParam
   * @return
   */
    public Integer queryStageTotal(DzdPageParam dzdPageParam);
    public Integer queryStageTotal0(DzdPageParam dzdPageParam);
    public Integer queryStageTotal1(DzdPageParam dzdPageParam);
    public Integer queryStageTotal2(DzdPageParam dzdPageParam);


}
