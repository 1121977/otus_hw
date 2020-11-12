package aop;

public class UsefulImpl implements Useful {
    public UsefulImpl(){}

    @Override
    public void sayHelloTo() {
        System.out.println("Hello!");
        System.out.println("Constructor is " + this.getClass().getClassLoader().getName());
    }

    @Log
    @Override
    public void sayHelloTo(String name) {
        System.out.println("Hello, " + name + "!");
    }

    @Override
    public void sayHelloTo(String name, short howMany) {
        for (int i = 0; i < howMany; i++) {
            System.out.println("Hello, " + name + "!");
        }
    }

}
