package com.ning.gupao.netty.tomcat.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

/**
 * @Author JAY
 * @Date 2019/8/5 19:16
 * @Description TODO
 **/
public class JNRequest {

    private ChannelHandlerContext ctx;

    private HttpRequest req;

    public JNRequest(ChannelHandlerContext ctx, HttpRequest req) {
        this.ctx = ctx;
        this.req = req;
    }

    public String getMethod() {
        return req.method().name();
    }

    public String getUrl() {
        return req.uri();
    }

    public Map<String, List<String>> getParameters() {
        QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
        return decoder.parameters();
    }

}
