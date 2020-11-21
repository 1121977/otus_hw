package aop;

import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.util.Arrays;

public class AOPInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.isAnnotationPresent(Log.class)) {
            Arrays.stream(args).forEach(System.out::println);
        }
        return method.invoke(proxy, new Object[]{args});
    }
}
