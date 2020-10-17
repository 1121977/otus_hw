package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
                processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object configClassInstance = new Object();
        try {
            configClassInstance = configClass.getConstructors()[0].newInstance();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Object finalConfigClassInstance = configClassInstance;
        Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                .forEach(method -> {
                    try {
                        boolean added = appComponents.add(method.invoke(finalConfigClassInstance, getMethodParameters(method)));
                        if(added&!method.getAnnotation(AppComponent.class).name().isEmpty()){
                            appComponentsByName.put(method.getAnnotation(AppComponent.class).name(),appComponents.get(appComponents.size()-1));
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
            // You code here...
    }

    private Object [] getMethodParameters(Method method){
        int parameterCount = method.getParameterCount();
        Object methodParameters[] = new Object[parameterCount];
        Parameter parameters[] = method.getParameters();
        for(int i=0;i<parameterCount;i++){
            for(Object component:appComponents){
                if(parameters[i].getType().isInstance(component)){
                    methodParameters[i]=component;
                    break;
                }
            }
        }
        return methodParameters;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        //C appComponent;
        for(Object appComponent:appComponents){
            if(componentClass.isInstance(appComponent)){
                return (C)appComponent;
            }
        }
        return null;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C)appComponentsByName.get(componentName);
    }
}
