package com.ning.gupao.netty.tomcat;

import com.ning.gupao.netty.tomcat.http.JNRequest;
import com.ning.gupao.netty.tomcat.http.JNResponse;
import com.ning.gupao.netty.tomcat.http.JNServlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author JAY
 * @Date 2019/8/5 19:48
 * @Description TODO
 **/
public class JNTomcatHandler extends ChannelInboundHandlerAdapter {

    private Map<String, JNServlet> servletMapping = new HashMap<>();

    public JNTomcatHandler(Map<String, JNServlet> servletMapping) {
        this.servletMapping = servletMapping;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest){
            HttpRequest req = (HttpRequest) msg;

            // 转交给我们自己的request实现
            JNRequest request = new JNRequest(ctx,req);
            // 转交给我们自己的response实现
            JNResponse response = new JNResponse(ctx);
            // 实际业务处理
            String url = request.getUrl();

            if(servletMapping.containsKey(url)){
                servletMapping.get(url).service(request, response);
            }else{
                response.write("404 - Not Found");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }


}
