package ru.otus;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TestFW {
    private static short failedTest;
    public static void main(String ... args){
        System.out.println("Hello, Test Framework");
        testStarter(TestedClass.class);
    }
    static void testStarter(Class<?> clazz) {
        Method [] methods = clazz.getDeclaredMethods();
        Map<Class<? extends Annotation>, Set<Method>> annotationSetMethod = new HashMap<>();
        annotationSetMethod.put(Before.class, new HashSet<>());
        annotationSetMethod.put(Test.class, new HashSet<>());
        annotationSetMethod.put(After.class, new HashSet<>());
        for(Method method:methods){
            for(Annotation annotation:method.getDeclaredAnnotations()){
                Set<Method> methodSet = annotationSetMethod.get(annotation.annotationType());
                if(methodSet!=null){
                    methodSet.add(method);
                }
            }
        }
        annotationSetMethod.get(Test.class).forEach(methodTest -> {
            Object testedObject;
            try {
                testedObject = clazz.getConstructor().newInstance();
                if (testedObject == null) {
                    throw new NullPointerException();
                } else {
                    try {
                        methodsExecution(annotationSetMethod.get(Before.class), testedObject);
                        methodTest.invoke(testedObject);
                        methodsExecution(annotationSetMethod.get(After.class), testedObject);
                    } catch (Exception e) {
                        failedTest++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        System.out.println("Failed tests: " + failedTest);
        System.out.println("Passed tests: " + (methods.length-failedTest));
    }

    private static void  methodsExecution(Set<Method> methodSet, Object testedObject){
        methodSet.forEach(method -> {
            try {
                method.invoke(testedObject);
            }
            catch (IllegalAccessException | InvocationTargetException e){
                e.printStackTrace();
            }
        });
    }
}
