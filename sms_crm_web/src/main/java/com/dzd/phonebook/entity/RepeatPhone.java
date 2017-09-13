package com.dzd.phonebook.entity;

import java.sql.Timestamp;

/**
 * Created by dzd-technology01 on 2017/7/27.
 */
public class RepeatPhone {
    private String repeatPhone;              //重复的电话号码
    private Timestamp createTime;           //日期
    private Integer repeatNum;              //电话号码重复数量

    public String getRepeatPhone() {
        return repeatPhone;
    }

    public void setRepeatPhone(String repeatPhone) {
        this.repeatPhone = repeatPhone;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getRepeatNum() {
        return repeatNum;
    }

    public void setRepeatNum(Integer repeatNum) {
        this.repeatNum = repeatNum;
    }
}
