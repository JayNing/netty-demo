package com.ning.gupao.rpc.consumer.proxy;

import com.ning.gupao.rpc.api.IRpcHelloService;

import java.lang.reflect.Proxy;

/**
 * @Author JAY
 * @Date 2019/8/5 20:40
 * @Description TODO
 **/
public class JNRpcProxy {
    public static <T> T create(Class<?> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),clazz.isInterface() ? new Class[]{clazz} : clazz.getInterfaces(),
                new JNRpcInvocationHandler(clazz));
    }
}
