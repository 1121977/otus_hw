package aop;

public class UsefulImpl implements Useful {
    public UsefulImpl(){}

    @Override
    public void sayHelloTo() {
//        return "Hello!";
    }

    @Override
    public void sayHelloTo(String name) {
//        return "Hello, " + name +"!";
    }

    @Log
    @Override
    public void sayHelloTo(String name, short howMany) {
//        return "Hello, " + name + "! " + howMany + " times.";
    }

}
