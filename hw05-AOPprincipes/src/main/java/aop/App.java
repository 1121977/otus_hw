package aop;

import java.lang.reflect.Method;

public class App {
    static public void main(String ... args) {
        var classLoaderClass = App.class.getClassLoader().getClass();
        if( !classLoaderClass.getName().equals(AOPClassLoader.class.getName())) { //иначе сравнивать классы невозможно, так как это различные объекты
            AOPClassLoader aopClassLoader = new AOPClassLoader();
            try {
                Class<?> loadClass = aopClassLoader.loadClass("aop.App", false);
                Method method = loadClass.getMethod("main", new Class[]{String[].class});
                method.invoke(null, new Object[]{args});
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else{// классы с AOP писать в этом блоке.
            try {
                new UsefulImpl().sayHelloTo("bb");
                new UsefulImpl().sayHelloTo("cc", (short) 23);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
