package com.ning.gupao.rpc.protocol;

import java.io.Serializable;

/**
 * @Author JAY
 * @Date 2019/8/5 20:22
 * @Description TODO
 **/
public class InvokerProtocol  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String className;//类名
    private String methodName;//函数名称
    private Class<?>[] parames;//形参列表
    private Object[] values;//实参列表

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParames() {
        return parames;
    }

    public void setParames(Class<?>[] parames) {
        this.parames = parames;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }
}
