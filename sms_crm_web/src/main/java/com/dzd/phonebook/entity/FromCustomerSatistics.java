package com.dzd.phonebook.entity;

import com.dzd.base.page.BasePage;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by dzd-technology01 on 2017/7/27.
 */
public class FromCustomerSatistics extends BasePage {
    private String createTime;        //日期
    private String source;               //来源
    private Integer rpPhoneCount;        //重复电话号码数量
    private Integer sfCount;             //试发量
    private Integer sfsCount;            //试发成功量
    private Integer yxCount;             //意向数量
    private Integer cjCount;             //成交数量
    private Integer key;
    private Integer cid;
    private String fpCount;   //分配数量

    public String getFpCount() {
        return fpCount;
    }

    public void setFpCount(String fpCount) {
        this.fpCount = fpCount;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.key=cid;
        this.cid = cid;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getSfsCount() {
        return sfsCount;
    }

    public void setSfsCount(Integer sfsCount) {
        this.sfsCount = sfsCount;
    }

    public String getCreateTime() {
        return createTime;

    }

    public void setCreateTime(String createTime) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        if(!createTime.equals("") || createTime != null){
            try{
                this.createTime = sdf.format(sdf.parse(createTime));
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }else{
            this.createTime = createTime;
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getRpPhoneCount() {
        return rpPhoneCount;
    }

    public void setRpPhoneCount(Integer rpPhoneCount) {
        this.rpPhoneCount = rpPhoneCount;
    }

    public Integer getSfCount() {
        return sfCount;
    }

    public void setSfCount(Integer sfCount) {
        this.sfCount = sfCount;
    }



    public Integer getYxCount() {
        return yxCount;
    }

    public void setYxCount(Integer yxCount) {
        this.yxCount = yxCount;
    }

    public Integer getCjCount() {
        return cjCount;
    }

    public void setCjCount(Integer cjCount) {
        this.cjCount = cjCount;
    }
}
