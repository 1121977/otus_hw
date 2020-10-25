package ru.otus;

public class TestedClass {

    public TestedClass(){}

    @After
    public void methodAfter1(){System.out.println("I'm methodAfter1 (this.hashCode(): " + hashCode() + ")");}

    @After
    public void methodAfter2(){System.out.println("I'm methodAfter2 (this.hashCode(): " + hashCode() + ")");}

    @Before
    public void methodBefore1(){
        System.out.println("I'm methodBefore1 (this.hashCode(): " + hashCode() + ")");
    }

    @Before
    public void methodBefore2(){
        System.out.println("I'm methodBefore2 (this.hashCode(): " + hashCode() + ")");
    }

    @Test
    public void methodTest1(){
        System.out.println("I'm methodTest1 (this.hashCode(): " + hashCode() + ")");
        throw new ArithmeticException();
    }

    @Test
    public void methodTest2()  {System.out.println("I'm methodTest2 (this.hachCode(): " + hashCode() + ")");}
}
