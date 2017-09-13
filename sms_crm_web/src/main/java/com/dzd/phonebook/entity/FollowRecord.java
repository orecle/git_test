package com.dzd.phonebook.entity;

import com.dzd.base.page.BasePage;

import java.sql.Timestamp;

/**
 * Created by wangran on 2017/7/17.  跟进
 */
public class FollowRecord extends BasePage {
    private Integer followid;   //跟进id
    private Integer cid; //客户id----被跟进人
    private Integer userid;  //跟进人id
    private String content;   //跟进内容
    private Timestamp createTime;  //创建时间
    private String nickName;   //业务员昵称

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getFollowid() {
        return followid;
    }

    public void setFollowid(Integer followid) {
        this.followid = followid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
