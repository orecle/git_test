package com.dzd.phonebook.entity;


import com.dzd.base.page.BasePage;
import com.dzd.phonebook.util.StringUtils;

/**
 * Created by wangran on 2017/7/14.  客户
 */
public class CustomerView extends BasePage {

    private Integer cid;  //客户id
    private String cname; //客户名称
    private String telephone;  //联系电话
    private String phone;
    private String remark;  //备注
    private Integer stage; //阶段 0：沟通  1：意向  2：成交
    private Integer userid;  //跟进人id--user表
    private String source;  //来源
    private Integer deleted; //是否被删除0:否  1：删除
    private String createTime;  //创建时间
    private String updateTime; //修改时间
    private String bname;  //跟进人
    private String content; //跟进内容
    private Integer key;
    private String stageValue;
    private Integer isCheck;
    private Integer isFromCustomer;  //0:系统新增客户  1：推广客户
    private Integer isSign;   //是否是图文标记过0：没标记(试发) 1：标记(试发成功)
    private String isSignValue;
    private String allocatedTime;  //客户被分配时间


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        if(isSign==0){
            this.isSignValue = "试发";
        }else if(isSign==1){
            this.isSignValue="成功试发";
        }else{
            this.isSignValue="未知";
        }
        this.isSign = isSign;
    }

    public Integer getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Integer isCheck) {
        this.isCheck = isCheck;
    }

    public String getStageValue() {
        return stageValue;
    }

    public void setStageValue(String stageValue) {
      this.stageValue=stageValue;
    }


    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        if(StringUtils.isNotBlank(createTime)){
            this.createTime = createTime.substring(0,19);
        }else{
            this.createTime = createTime;
        }

    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
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
        if(stage==0){
            this.stageValue = "沟通";
        }else if(stage==1){
            this.stageValue="意向";
        }else if(stage==2){
            this.stageValue="成交";
        }else{
            this.stageValue="未知";
        }
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
        this.key=cid;
        this.cid = cid;
    }

    public String getAllocatedTime() {
        return allocatedTime;
    }

    public void setAllocatedTime(String allocatedTime) {
        if(StringUtils.isNotBlank(allocatedTime)){
            this.allocatedTime = allocatedTime.substring(0,19);
        }else{
            this.allocatedTime = allocatedTime;
        }
    }
}
