package com.dzd.phonebook.util;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dzd-technology01 on 2017/7/20.
 */
public class CorsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    //跨域问题配置
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        //String strBackUrl = "http://" + req.getServerName() + ":" + req.getServerPort();           //端口号
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "http://192.168.1.41");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        //复杂参数请求问题(json)
        response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
        //解决session失效问题
        response.setHeader("Access-Control-Allow-Credentials", "true");


        chain.doFilter(req, res);

    }

    @Override
    public void destroy() {
    }
}
