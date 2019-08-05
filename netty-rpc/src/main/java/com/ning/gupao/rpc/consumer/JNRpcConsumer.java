package com.ning.gupao.rpc.consumer;

import com.ning.gupao.rpc.api.IRpcHelloService;
import com.ning.gupao.rpc.consumer.proxy.JNRpcProxy;

/**
 * @Author JAY
 * @Date 2019/8/5 20:39
 * @Description TODO
 **/
public class JNRpcConsumer {

    public static void main(String[] args) {
        IRpcHelloService helloService = JNRpcProxy.create(IRpcHelloService.class);
        String sayHello = helloService.sayHello("张三");
        System.out.println(sayHello);
    }

}
