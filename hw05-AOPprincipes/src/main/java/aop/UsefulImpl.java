package aop;

public class UsefulImpl implements Useful {
    public UsefulImpl(){}

    @Override
    public String sayHelloTo() {
        return "Hello!";
    }

    @Log
    @Override
    public String sayHelloTo(String name) {
        return "Hello, " + name +"!";
    }

    @Override
    public String sayHelloTo(String name, short howMany) {
        return "Hello, " + name + "! " + howMany + " times.";
    }

}
