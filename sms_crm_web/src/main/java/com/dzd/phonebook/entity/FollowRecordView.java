package com.dzd.phonebook.entity;

import java.text.SimpleDateFormat;

/**
 * Created by wangran on 2017/7/17.  跟进
 */
public class FollowRecordView{
    private String content;   //跟进内容
    private String createTime;  //创建时间
    private String nickName;   //业务员昵称
    private String cname; //客户名称

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
}
