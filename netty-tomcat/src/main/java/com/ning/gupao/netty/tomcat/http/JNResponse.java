package com.ning.gupao.netty.tomcat.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.internal.StringUtil;

/**
 * @Author JAY
 * @Date 2019/8/5 19:16
 * @Description TODO
 **/
public class JNResponse {

    //SocketChannel的封装
    private ChannelHandlerContext ctx;

    public JNResponse(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public void write(String content) {
        try{
            if (StringUtil.isNullOrEmpty(content)){
                return;
            }
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(content.getBytes("UTF-8")));
            response.headers().set("Content-Type","text/html");
            ctx.write(response);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ctx.flush();
            ctx.close();
        }
    }
}
