package aop;

import java.lang.reflect.Method;

public class App {
    static public void main(String ... args) {
        Class<? extends ClassLoader> classLoaderClass = App.class.getClassLoader().getClass();
        if( classLoaderClass != AOPClassLoader.class) {
            AOPClassLoader aopClassLoader = new AOPClassLoader();
            try {
                Class<?> loadClass = aopClassLoader.defineAppClass();
                Method method = loadClass.getMethod("main", new Class[]{String[].class});
                method.invoke(null, new Object[]{args});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                Useful useful = new UsefulImpl();
                useful.sayHelloTo("Otus");
                System.out.println("App.main is run from AOPClassloader");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
