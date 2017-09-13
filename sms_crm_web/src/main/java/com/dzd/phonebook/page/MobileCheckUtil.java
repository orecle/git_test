package com.dzd.phonebook.page;

import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.dzd.phonebook.entity.Customer;
import com.dzd.phonebook.entity.CustomerView;
import com.dzd.phonebook.service.CustomerService;
import com.dzd.phonebook.util.Define;


public class MobileCheckUtil {
	/**
	 * 对重复号码/无效号码/有效号码分类
	 * @param
	 * @return
	 */
	public static Map<String, Object> mobileAssort(List<Customer> customers,CustomerService customerService) {
		if (null == customers || customers.size() < 1) {
			return null;
		}
		Map<String,Object> map=new HashMap<String, Object>();
		long begin = System.currentTimeMillis();
		List<String> ivlist = new LinkedList<String>();  //无效号码
		List<String> dplist = new LinkedList<String>(); //重复号码
		Map<String,String> vmap = new LinkedHashMap<String,String>();
		Map<String,String> ivmap = new LinkedHashMap<String,String>();
		Map<String,String> dpmap = new LinkedHashMap<String,String>();
		List<Customer> ivlistCustomer=new ArrayList<Customer>();  //无效号码
		List<Customer> dplistCustomer=new ArrayList<Customer>();  //重复号码
		List<Customer> dplistDB=new ArrayList<Customer>();  //和数据库中重复号码
		//查询数据库中所有的电话号码
		List<Integer> phoneAll = customerService.queryTelephoneAll();

		String regEx="[^0-9]";
		Pattern p = Pattern.compile(regEx);
		for (Customer customer : customers) {
			String mobile=customer.getTelephone().toString();
			Matcher m = p.matcher(mobile);
			mobile = m.replaceAll("").trim();

			// 无效号码
			if (!NewRegexUtil.elevenNumber(mobile)) {
				if (!ivmap.containsKey(mobile)){
					ivlist.add(mobile);
					ivlistCustomer.add(customer);
				}
			}
			else if(!vmap.containsKey(mobile)) {
				vmap.put(mobile,mobile);
			} else {
				dplist.add(mobile);
				dplistCustomer.add(customer);
			}
		}

		//移除无效号码
		customers.removeAll(ivlistCustomer);
		//移除重复的号码
	    customers.remove(dplistCustomer);

        //验证和数据库中数据重复
		for (int i = 0; i < customers.size(); i++) {
			Customer customer = customers.get(i);
			String phone=customer.getTelephone();
			if (phoneAll.contains(phone)) {
				if(!dplist.contains(phone)){
					//加入到重复数据中去
					dplist.add(phone);
				}
				dplistDB.add(customer);
			}
		}
		customers.removeAll(dplistDB); //移除和数据库中相同的对象

		if(customers.size()>0){
			// 成功号码-------批量保存文件信息到数据库,按照每5000个遍历插入
			if(customers.size()>5000){
				List<List<Customer>> ret = split(customers, 5000);
				for (int i = 0; i < ret.size(); i++) {
					// 成功号码-------批量保存文件信息到数据库
					customerService.insertList(ret.get(i));
				}
			}else{
				// 成功号码-------批量保存文件信息到数据库
				customerService.insertList(customers);
			}
		}
		map.put(Define.PHONEKEY.INVALID, ivlist); // 无效号码
		map.put(Define.PHONEKEY.DUPLICATE, dplist); // 重复号码
		map.put("successNumber", customers.size()); // 成功号码个数
		System.out.println("cost time:" + (System.currentTimeMillis()-begin));
		vmap.clear();
		ivmap.clear();
		dpmap.clear();
		return map;
	}

	/**
	 * 对重复号码/无效号码/有效号码分类
	 * @param
	 * @return
	 */
	public static Map<String, Object> mobileAssort2(List<Customer> customers,CustomerService customerService) {
		//查询客户的电话号码
		List<String> phonelist = customerService.queryTelephoneAll();

		Map<String,Object> map=new HashMap<String, Object>();
		long begin = System.currentTimeMillis();
		List<String> ivlist = new LinkedList<String>();  //无效号码
		List<String> dplist = new LinkedList<String>(); //重复号码
		List<Customer> dpCustomerlist = new LinkedList<Customer>(); //重复号码对象
		List<String> successList = new LinkedList<String>(); //成功号码
		Map<String,String> vmap = new LinkedHashMap<String,String>();
		Map<String,String> ivmap = new LinkedHashMap<String,String>();

		List<Customer> list=new ArrayList<Customer>();
		String regEx="[^0-9]";
		Pattern p = Pattern.compile(regEx);
		for (Customer customer : customers) {
			String mobile=customer.getTelephone().toString();
			Matcher m = p.matcher(mobile);
			mobile = m.replaceAll("").trim();

			// 无效号码
			if (!NewRegexUtil.elevenNumber(mobile)) {
				if (!ivmap.containsKey(mobile)){
					ivlist.add(mobile);
					list.add(customer);
				}
			}

			else if(!vmap.containsKey(mobile)) {
				vmap.put(mobile,mobile);
			} else {
				//重复
				if(!dplist.contains(mobile)){
					//如果重复集合中已经存在重复的电话号码，则不新增到集合里面
					dplist.add(mobile);
					customer.setDeleted(1);
					dpCustomerlist.add(customer);
				}
				list.add(customer);
			}
		}

		//移除导入数据中重复的号
		customers.removeAll(list);

		List<Customer> list2=new ArrayList<Customer>();
		for(Customer cu:customers){
			//判断数据库中是否存在要导入的号
			if(phonelist.contains(cu.getTelephone())){
				list2.add(cu);
				if(!dplist.contains(cu.getTelephone())){
					dplist.add(cu.getTelephone());
					cu.setDeleted(1);
					dpCustomerlist.add(cu);
				}
			}
		}
		//移除导入数据中的号和数据库中重复的号
		customers.removeAll(list2);

		//成功号码
		for(Customer c:customers){
			successList.add(c.getTelephone());
		}

		customerService.insertList(dpCustomerlist); //保存所有重复电话号码的客户

		map.put(Define.PHONEKEY.INVALID, ivlist); // 无效号码
		map.put(Define.PHONEKEY.DUPLICATE, dplist); // 重复号码
		map.put("customers",customers);
		map.put("successList",successList); // 成功号码
		System.out.println("cost time:" + (System.currentTimeMillis()-begin));
		vmap.clear();
		ivmap.clear();
		return map;
	}

	/**
	 * 对重复号码/有效号码分类
	 * @param
	 * @return
	 */
	public static Map<String, Object> mobileAssort3(List<Customer> customers,CustomerService customerService) {
		//查询客户的电话号码
		List<String> phonelist = customerService.queryTelephoneAll();

		Map<String,Object> map=new HashMap<String, Object>();
		long begin = System.currentTimeMillis();
		List<Customer> dplist = new LinkedList<Customer>(); //重复号码
		List<Customer> ndplist = new LinkedList<Customer>(); //没有重复的
        List<Customer> dpCustomerList=new LinkedList<Customer>(); //重复的对象
		for (Customer customer : customers) {
			customer.setIsFromCustomer(1); //试发客户
			customer.setStage(0);  //默认0：沟通用户
           //判断数据库中是否存在要新增的电话号码
			if(phonelist.contains(customer.getTelephone())){
				//查询当天是否有重复的数据
				CustomerView cv=customerService.queryRepTelephoneByToday(customer.getSource(), customer.getTelephone());
				if(cv!=null){
					//有重复的数据
					dpCustomerList.add(customer);
				}else{
					//没有重复的数据
					customer.setDeleted(1);
					dplist.add(customer);
				}
			}else{
				//没有重复的电话
				customer.setDeleted(0);
				ndplist.add(customer);
			}
		}

		map.put("dplist",dplist);
		map.put("ndplist",ndplist);

		System.out.println("cost time:" + (System.currentTimeMillis()-begin));
		return map;
	}
	public static void main(String []args) throws Exception{

	}

	/**
	 *  拆分集合
	 *  @param <T>
	 *  @param count 每个集合的元素个数
	 *  @return返回拆分后的各个集合
	 **/
	public static <T> List<List<T>> split(List<T> resList, int count) {
		if (resList == null || count < 1)
			return null;
		List<List<T>> ret = new ArrayList<List<T>>();
		int size = resList.size();
		if (size <= count) {
			// 数据量不足count指定的大小
			ret.add(resList);
		} else {
			int pre = size / count;
			int last = size % count;
			// 前面pre个集合，每个大小都是count个元素
			for (int i = 0; i < pre; i++) {
				List<T> itemList = new ArrayList<T>();
				for (int j = 0; j < count; j++) {
					itemList.add(resList.get(i * count + j));
				}
				ret.add(itemList);
			}
			// last的进行处理
			if (last > 0) {
				List<T> itemList = new ArrayList<T>();
				for (int i = 0; i < last; i++) {
					itemList.add(resList.get(pre * count + i));
				}
				ret.add(itemList);
			}
		}
		return ret;
	}
}
