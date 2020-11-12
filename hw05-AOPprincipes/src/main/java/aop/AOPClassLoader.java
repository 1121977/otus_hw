package aop;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.util.*;

public class AOPClassLoader extends ClassLoader {
    private List<Method> annotatedMethods;

    Class<?> defineClass(String className) throws IOException {
        File file = new File(getFileName(className));
        byte[] bytecode = Files.readAllBytes(file.toPath());
        return super.defineClass("aop." + className, bytecode, 0, bytecode.length);
    }

    Class<?> defineAppClass() throws IOException {
        return this.defineClass("App");
    }

    String getFileName(String className) {
        return "build" + File.separator + "classes" + File.separator + "java" + File.separator
                + "main" + File.separator + "aop" + File.separator + className + ".class";
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> resultClass = super.loadClass(name, resolve);
        Method[] methods = resultClass.getMethods();
        List<Class<?>> markedInterfacesList = new ArrayList<>();
        annotatedMethods = new ArrayList<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Log.class)) {
                determinateMarkedInterfaces(method, resultClass.getInterfaces(), markedInterfacesList);
                annotatedMethods.add(method);
            }
        }
        Field [] fields = resultClass.getFields();
        methods[0].se
        if(!markedInterfacesList.isEmpty()) {
            AOPInvocationHandler aopInvocationHandler = new AOPInvocationHandler();
            Method [] proxyMethods = Proxy.newProxyInstance(this, markedInterfacesList
                    .toArray(this.getClass().getInterfaces())/*передать Class<?> []*/, aopInvocationHandler).
                    getClass().getMethods();
            for (int i =0; i< methods.length; i++){
                i.
                if(methods[i].isAnnotationPresent(Log.class)){
                    methods[i]=findMethodWithSameSignatureInArray(methods[i],proxyMethods);
                }
                System.out.println("Annotated method is " + methods[i].getName());
            }
        }
        return resultClass;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

    private void determinateMarkedInterfaces(Method method, Class<?>[] interfaces, List<Class<?>> chosenInterfacesList) {
        if (interfaces.length != 0) {
            Arrays.stream(interfaces)
                    .filter(i -> Arrays.stream(i.getDeclaredMethods())
                            .filter(m -> compareMethodSignature(m,method)).count() > 0)
                    .forEach(chosenInterfacesList::add);
//            Arrays.stream(interfaces).forEach(i -> determinateCarryingInterfaces(method, i.getInterfaces(), chosenInterfacesSet));
            for(int i =0; i< interfaces.length; i++){
                determinateMarkedInterfaces(method, interfaces[i].getInterfaces(), chosenInterfacesList);
            }
        }
    }
    private boolean compareMethodSignature(Method method1, Method method2){
        Class<?> [] parameterTypesM1 = method1.getParameterTypes();
        Class<?> [] parameterTypesM2 = method2.getParameterTypes();
        if(parameterTypesM1.length!=parameterTypesM2.length) {return false;}
        else {
            for (int i = 0; i < parameterTypesM1.length; i++) {
                if(parameterTypesM1[i]!=parameterTypesM2[i]) {
                    return false;
                }
            }
        }
        if (!method1.getName().equals(method2.getName())){
            return false;
        }
        return true;
    }

    private Method findMethodWithSameSignatureInArray(Method method, Method [] proxyMethods){
        for(Method proxyMethod: proxyMethods){
            if(compareMethodSignature(proxyMethod, method)){
                return proxyMethod;
            }
        }
        return null;
    }

}