package com.dzd.phonebook.dao;

import java.util.List;

import com.dzd.base.dao.BaseDao;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.entity.SysUserView;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.entity.SmsUser;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;


/**
 * 用户接口
 *
 * @author
 * @date 2017-7-14
 */
public interface SysUserDao<T> extends BaseDao<T> {


    /**
     *账号管理接口
     * @param dzdPageParam
     * @return
     */
   public Page<SysUserView> queryUserPage(DzdPageParam dzdPageParam);



    /**
     * 检查登录
     *
     * @param
     * @param
     * @return
     */
    public T queryLogin(SysUser sysUser);
    public List<SysUser> querySysUserList();

    public List<SysUser> queryListUserByRoleId(Integer roleId);

    /**
     * 查询账户是否存在
     * @param email
     * @param id
     * @return
     */
    public Integer querySysUserByuserEmail(@Param("email") Object email,@Param("id") Object id);

    public void sevaSysRoleRels(Integer objId);

    public Integer queryfirmName(SmsUser smsUser);

    public void updateSysUserPwd(SysUser sysUser);

    public List<SysUser> queryYwSysUserList(Integer uid);

    public Integer queryTdUser(Integer ywId);

    /**
     * 查询用户是否存在
     *
     * @param email
     * @return
     */
    public SysUser queryUserExist(String email);

    /**
     * 查询所有经理账号
     * @return
     */
    public List<SysUserView> querySuperiorManager();

    /**
     * 修改时查询用户信息
     * @param id
     * @return
     */
    public SysUserView querySysUserById(@Param("id") Integer id);

    /**
     * 修改业务员是否要查看分配的客户
     * @param id
     */
    public void updateIsCheck0(@Param("id") Integer id);

    /**
     * 修改业务员是否要查看分配的客户
     * @param id
     */
    public void updateIsCheck1(@Param("id") Integer id);

    /**
     * 查看指定业务员的是否有接收短信的权限
     * @return
     */
    public SysUserView queryIsCheck0(@Param("id") Integer id);

    /**
     * 根据账号查询id
     * @param email
     * @return
     */
    public  Integer queryIdByEmail(@Param("email") String email);

  /**
   * 查询电话号码是否已经存在
   * @param telephone
   * @param id
   * @return
   */
  public Integer queryTelephoneIsHad(@Param("telephone") Object telephone,@Param("id") Object id);

}
