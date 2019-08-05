package com.ning.gupao.rpc.provider;


import com.ning.gupao.rpc.api.IRpcHelloService;

public class RpcHelloServiceImpl implements IRpcHelloService {

    @Override
    public String sayHello(String name) {
        return "Hello " + name + "!";  
    }  
  
}  
