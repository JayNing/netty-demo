package com.ning.gupao.netty.tomcat.servlet;

import com.ning.gupao.netty.tomcat.http.JNRequest;
import com.ning.gupao.netty.tomcat.http.JNResponse;
import com.ning.gupao.netty.tomcat.http.JNServlet;

/**
 * @Author JAY
 * @Date 2019/8/5 19:14
 * @Description TODO
 **/
public class SecondServlet extends JNServlet {
    @Override
    public void doGet(JNRequest request, JNResponse response) {
        this.doPost(request,response);
    }

    @Override
    public void doPost(JNRequest request, JNResponse response) {
        response.write("This is Second Sevlet");
    }
}
