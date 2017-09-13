package com.dzd.phonebook.util;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by dzd-technology01 on 2017/7/24.
 */
public class LoginFilter implements Filter {
    public void destroy() {
        // TODO Auto-generated method stub
    }

    public void doFilter(ServletRequest arg0, ServletResponse arg1,
                         FilterChain arg2) throws IOException, ServletException {
        // TODO Auto-generated method stub
        HttpServletRequest req = (HttpServletRequest)arg0;
        HttpServletResponse resp =(HttpServletResponse) arg1;
        HttpSession session = req.getSession();

        List<String> pathList=new ArrayList<String>();
        pathList.add("/");
        pathList.add("/index.html");
        pathList.add("/login.do");
        pathList.add("/captcha.do");
        pathList.add("/customer/getData.do");
        // 获得用户请求的URI
        String path = req.getRequestURI();

        //也可以path.contains("")
        if(pathList.contains(path) || path.indexOf("/CRM")!=-1) {
            arg2.doFilter(req, resp);
            return;
        } else {//如果不是上述路径
         if(session.getAttribute("user") == null) {
                //跳转到登陆页面
             String json = "{\"state\":\"" + "Session expired"+ "\"}";
             resp.getWriter().write(json);
            } else {
                // 已经登陆,继续此次请求
                arg2.doFilter(req, resp);
            }
        }
    }
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
    }
}
