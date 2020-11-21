package aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.Proxy;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.objectweb.asm.Opcodes.ASM9;

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
        if (!markedInterfacesList.isEmpty()) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(resultClass);
            MethodInterceptor methodInterceptor = (obj, method, args, proxy) -> {
                if (method.isAnnotationPresent(Log.class)) {
                    for (int i = 0; i < args.length; i++) {
                        System.out.println(args[i]);
                    }
                }
                return proxy.invokeSuper(obj, args);
            };
            enhancer.setCallback(methodInterceptor);
            Class<?> testedClass = enhancer.create(new Class[]{}, new Object[]{}).getClass();
            try {
                ClassReader cr = new ClassReader("aop.UsefulImpl");
                ClassWriter cw = new ClassWriter(0);
/*                ClassVisitor cv = new ClassVisitor(ASM9,cw) {
                    @Override
                    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                        super.visit(version, access, name, signature, superName, interfaces);
                    }
                };*/
                ClassPrinter cp = new ClassPrinter();
                cr.accept(cp, 0);
                System.out.println("aa");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Useful useful = (UsefulImpl) enhancer.create();
            System.out.println(useful.sayHelloTo("name"));
            System.out.println(new UsefulImpl().sayHelloTo("a"));
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
                            .filter(m -> compareMethodSignature(m, method)).count() > 0)
                    .forEach(chosenInterfacesList::add);
//            Arrays.stream(interfaces).forEach(i -> determinateCarryingInterfaces(method, i.getInterfaces(), chosenInterfacesSet));
            for (int i = 0; i < interfaces.length; i++) {
                determinateMarkedInterfaces(method, interfaces[i].getInterfaces(), chosenInterfacesList);
            }
        }
    }

    private boolean compareMethodSignature(Method method1, Method method2) {
        Class<?>[] parameterTypesM1 = method1.getParameterTypes();
        Class<?>[] parameterTypesM2 = method2.getParameterTypes();
        if (parameterTypesM1.length != parameterTypesM2.length) {
            return false;
        } else {
            for (int i = 0; i < parameterTypesM1.length; i++) {
                if (parameterTypesM1[i] != parameterTypesM2[i]) {
                    return false;
                }
            }
        }
        if (method1.getName().equals(method2.getName())) {
            return true;
        }
        return false;
    }

 /*   private Method findMethodWithSameSignatureInArray(Method method, Method [] proxyMethods){
        for(Method proxyMethod: proxyMethods){
            if(compareMethodSignature(proxyMethod, method)){
                return proxyMethod;
            }
        }
        return null;
    }*/

}