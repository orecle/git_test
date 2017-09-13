package com.dzd.phonebook.entity;

import java.sql.Timestamp;

/**
 * Created by dzd-technology01 on 2017/7/22.
 */
public class MessageLog {
    private Integer id;
    private Timestamp createTime;
    private Integer uid;   //业务员id
    private String remark;  //短信内容

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
