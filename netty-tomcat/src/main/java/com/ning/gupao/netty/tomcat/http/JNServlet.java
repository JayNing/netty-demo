package com.ning.gupao.netty.tomcat.http;

/**
 * @Author JAY
 * @Date 2019/8/5 19:15
 * @Description TODO
 **/
public abstract class JNServlet {

    public void service(JNRequest request, JNResponse response){
        if ("GET".equalsIgnoreCase(request.getMethod())){
            doGet(request,response);
        }else {
            doPost(request,response);
        }
    }

    public abstract void doGet(JNRequest request, JNResponse response);
    public abstract void doPost(JNRequest request, JNResponse response);

}
