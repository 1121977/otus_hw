package ru.otus;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationTypeMismatchException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class TestFW {
    static short failedTest;
    public static void main(String ... args){
        System.out.println("Hello, Test Framework");
        testStarter(TestedClass.class);
    }
    static void testStarter(Class<?> clazz) {
        Method methods[] = clazz.getDeclaredMethods();
        List<Method> methodsWithAfterAnnotaion = new ArrayList<>();
        List<Method> methodsWithBeforeAnnotaion = new ArrayList<>();
        List<Method> methodsWithTestAnnotation = new ArrayList<>();
        for(Method method:methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            if (annotations.length!=0)
                for (Annotation annotation:annotations)
                    switch (annotation.toString()) {
                        case ("@ru.otus.After()"):
                            methodsWithAfterAnnotaion.add(method);
                            break;
                        case ("@ru.otus.Before()"):
                            methodsWithBeforeAnnotaion.add(method);
                            break;
                        case ("@ru.otus.Test()"):
                            methodsWithTestAnnotation.add(method);
                            break;
                        default:
                            throw new AnnotationTypeMismatchException(method, "Unkown annotaion");
                    }
        }
        methodsWithTestAnnotation.forEach(_methodTest -> {
            Object testedObject;
            try {
                testedObject = clazz.getConstructor().newInstance();
                if (testedObject == null)
                    throw new NullPointerException();
                else
                methodsExecution(methodsWithBeforeAnnotaion,testedObject);
                try {
                    _methodTest.invoke(testedObject);
                }
                catch (Exception e){
                    failedTest++;
                }
                methodsExecution(methodsWithAfterAnnotaion, testedObject);
            } catch (InstantiationException| IllegalAccessException | InvocationTargetException | NoSuchMethodException | NullPointerException e) {
                e.printStackTrace();
            }

        });
        System.out.println("Failed tests: " + failedTest);
        System.out.println("Passed tests: " + (methodsWithTestAnnotation.size()-failedTest));
    }

    private static void  methodsExecution(List<Method> methodList, Object object){
        methodList.forEach(_method -> {
            try {
                _method.invoke(object);
            }
            catch (IllegalAccessException | InvocationTargetException e){
                e.printStackTrace();
            }
        });
    }
}
