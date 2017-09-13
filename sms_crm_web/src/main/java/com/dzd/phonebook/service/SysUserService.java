package com.dzd.phonebook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dzd.base.service.BaseService;
import com.dzd.phonebook.dao.SysRoleRelDao;
import com.dzd.phonebook.dao.SysUserDao;
import com.dzd.phonebook.entity.SysRoleRel;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.entity.SysUserView;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.entity.SmsUser;
import com.github.pagehelper.Page;

import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 系统用户服务类
 *
 * @author chenchao
 * @date 2016-6-24
 */
@Service("sysUserService")
public class SysUserService<T> extends BaseService<T> {
	private final static Logger log = Logger.getLogger(SysUserService.class);

	@Autowired
	private SysRoleRelService<SysRoleRel> sysRoleRelService;

	@Autowired
	private SysRoleRelDao<SysRoleRel> sysRoleRelDao;




	/**
	 *账号管理接口
	 * @param dzdPageParam
	 * @return
	 */
	public Page<SysUserView> queryUserPage(DzdPageParam dzdPageParam){
		return mapper.queryUserPage(dzdPageParam);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Object[] ids) throws Exception {
		super.delete(ids);
		for (Object id : ids) {
			sysRoleRelService.deleteByObjId((Integer) id, SysRoleRel.RelType.USER.key);
		}
	}

	/**
	 * 检查登录
	 *
	 * @param email
	 * @param pwd
	 * @return
	 */
	public T queryLogin(String email, String pwd) {
		SysUser model = new SysUser();
		model.setEmail(email);
		model.setPwd(pwd);
		SysUserDao<T> mapper = getDao();
		// mapper.queryById("");
		return (T) getDao().queryLogin(model);
	}

	/**
	 * 查询用户权限
	 *
	 * @param userId
	 * @return
	 */
	public List<SysRoleRel> getUserRole(Integer userId) {
		return sysRoleRelService.queryByObjId(userId, SysRoleRel.RelType.USER.key);
	}

	/**
	 * 添加用户权限
	 *
	 * @param userId
	 * @param roleIds
	 * @throws Exception
	 */
	public void addUserRole(Integer userId, Integer[] roleIds) throws Exception {
		if (userId == null || roleIds == null || roleIds.length < 1) {
			return;
		}
		// 清除关联关系
		sysRoleRelService.deleteByObjId(userId, SysRoleRel.RelType.USER.key);
		for (Integer roleId : roleIds) {
			SysRoleRel rel = new SysRoleRel();
			rel.setRoleId(roleId);
			rel.setObjId(userId);
			rel.setRelType(SysRoleRel.RelType.USER.key);
			sysRoleRelService.add(rel);
		}
	}

	@Autowired
	private SysUserDao<T> mapper;

	public SysUserDao<T> getDao() {
		return mapper;
	}

	/**
	 * @Description:
	 * @author:查询出所有系统管理用户（不包含代理商）
	 * @time:2017年3月24日 上午11:32:16
	 */
	public List<SysUser> querySysUserList() {
		return getDao().querySysUserList();
	}



	/**
	 * 保存用户
	 *
	 * @param sysUser
	 * @param
	 * @return
	 */
	public int saveUser(SysUser sysUser) {
		int num = 0;
		mapper.add((T) sysUser);
		SysRoleRel sysRoleRel = new SysRoleRel();
		sysRoleRel.setObjId(sysUser.getId());
		sysRoleRel.setRelType(1);
		sysRoleRel.setRoleId(sysUser.getRole());
		sysRoleRelDao.add(sysRoleRel);
		return num;
	}

	/**
	 * 修改用户
	 *
	 * @param sysUser
	 * @return
	 */
	public int updateUser(SysUser sysUser) {
		int num = 0;
		mapper.update((T) sysUser);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("objId", sysUser.getId());
		param.put("relType", 1);
		// 删除用户关联的角色
		sysRoleRelDao.deleteByObjId(param);

		SysRoleRel sysRoleRel = new SysRoleRel();
		sysRoleRel.setObjId(sysUser.getId());
		sysRoleRel.setRelType(1);
		sysRoleRel.setRoleId(sysUser.getRole());
		sysRoleRelDao.add(sysRoleRel);
		return num;
	}

	/**
	 * 删除用户
	 *
	 * @param id
	 * @return
	 */
	public int deleteUser(Integer id) {
		int num = 0;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("objId", id);
		param.put("relType", 1);

		//删除用户---角色
		sysRoleRelDao.deleteByObjId(param);
		//删除用户
		mapper.delete(id);
		return num;
	}

	/**
	 * 批量删除用户
	 *
	 * @param ids
	 * @return
	 */
	public int deleteUsers(List<Integer> ids) {
		int num = 0;
		for (int id : ids) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("objId", id);
			param.put("relType", 1);

			mapper.delete(id);
		}
		return num;
	}


	public List<SysUser> queryListUserByRoleId(Integer roleId) {
		return mapper.queryListUserByRoleId(roleId);
	}


	/**
	 * 查询账户是否存在
	 * @param email
	 * @param id
	 * @return
	 */
	public Integer querySysUserByuserEmail(Object email,Object id){
		return mapper.querySysUserByuserEmail(email, id);
	}

	public void sevaSysRoleRels(Integer objId) {
		getDao().sevaSysRoleRels(objId);
	}


	public void updateSysUserPwd(SysUser sysUser) {
		getDao().updateSysUserPwd(sysUser);
	}

	/**
	 * 
	 * @Description:根据销售经理ID查询所属业务员
	 * @author:oygy
	 * @time:2017年3月27日 下午3:45:40
	 */
	public List<SysUser> queryYwSysUserList(Integer uid) {
		return getDao().queryYwSysUserList(uid);
	}

	public Integer queryTdUser(Integer ywId) {
		return getDao().queryTdUser(ywId);
	}

	/**
	 * 查询用户是否存在
	 * 
	 * @param email
	 * @return
	 */
	public SysUser queryUserExist(String email) {
		return getDao().queryUserExist(email);
	}

	/**
	 * 查询所有经理账号
	 * @return
	 */
	public List<SysUserView> querySuperiorManager(){
		return mapper.querySuperiorManager();
	}

	/**
	 * 修改时查询用户信息
	 * @param id
	 * @return
	 */
	public SysUserView querySysUserById(Integer id){
		return mapper.querySysUserById(id);
	}

	/**
	 * 修改业务员是否要查看分配的客户
	 * @param id
	 */
	public void updateIsCheck0(Integer id){
		mapper.updateIsCheck0(id);
	}

	/**
	 * 修改业务员是否要查看分配的客户
	 * @param id
	 */
	public void updateIsCheck1(Integer id){
		mapper.updateIsCheck1(id);
	}

	/**
	 * 查看指定业务员的是否有接收短信的权限
	 * @return
	 */
	public SysUserView queryIsCheck0(Integer id){
		return mapper.queryIsCheck0(id);
	}

	/**
	 * 根据账号查询id
	 * @param email
	 * @return
	 */
	public  Integer queryIdByEmail(String email){
		return mapper.queryIdByEmail(email);
	}

	/**
	 * 查询电话号码是否已经存在
	 * @param telephone
	 * @param id
	 * @return
	 */
	public Integer queryTelephoneIsHad(Object telephone,Object id){
		return mapper.queryTelephoneIsHad(telephone,id);
	}
}
