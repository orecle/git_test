package com.dzd.phonebook.entity;

import com.dzd.base.page.BasePage;

import java.sql.Timestamp;

/**
 * Created by wangran on 2017/7/14.  客户
 */
public class Customer extends BasePage {

    private Integer cid;  //客户id
    private String cname; //客户名称
    private String telephone;  //联系电话
    private String remark;  //备注
    private Integer stage; //阶段 0：沟通  1：意向  2：成交
    private Integer userid;  //跟进人id--user表
    private String source;  //来源
    private Integer deleted; //是否被删除0:否  1：删除
    private Timestamp createTime;  //创建时间
    private Timestamp updateTime; //修改时间
    private String bname;  //跟进人
    private String content; //跟进内容
    private Integer isCheck; //是否已经查看 0：没有查看  1：已经查看
    private Integer isSend; //是否已经给业务员发过短信 0：没有  1：已经发送过  发送后标记为1，没有标记为0
    private Integer isFromCustomer;  //0:系统新增客户  1：推广客户
    private Integer isSign;   //是否是图文标记过0：没标记(试发) 1：标记(试发成功)
    private String isSignValue;
    private Timestamp allocatedTime;  //客户被分配时间

    public Timestamp getAllocatedTime() {
        return allocatedTime;
    }

    public void setAllocatedTime(Timestamp allocatedTime) {
        this.allocatedTime = allocatedTime;
    }

    public String getIsSignValue() {
        return isSignValue;
    }

    public void setIsSignValue(String isSignValue) {
        this.isSignValue = isSignValue;
    }

    public Integer getIsFromCustomer() {
        return isFromCustomer;
    }

    public void setIsFromCustomer(Integer isFromCustomer) {
        this.isFromCustomer = isFromCustomer;
    }

    public Integer getIsSign() {
        return isSign;
    }

    public void setIsSign(Integer isSign) {
        this.isSign = isSign;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Integer isCheck) {
        this.isCheck = isCheck;
    }

    public Integer getIsSend() {
        return isSend;
    }

    public void setIsSend(Integer isSend) {
        this.isSend = isSend;
    }
}
