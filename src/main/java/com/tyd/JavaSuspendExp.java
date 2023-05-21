package com.tyd;

public class JavaSuspendExp extends Thread {

    public JavaSuspendExp(String name) {
        super(name);
    }

    public void run() {
        for (int i = 1; i < 5; i++) {
            try {
                // thread to sleep for 500 milliseconds
                sleep(500);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        // creating three threads
        JavaSuspendExp t1 = new JavaSuspendExp("t1");
        JavaSuspendExp t2 = new JavaSuspendExp("t2");
        JavaSuspendExp t3 = new JavaSuspendExp("t3");
        // call run() method
        t1.start();

        Thread thread = new Thread(() -> {
            System.out.println("sdsd");
        }, "test");
        thread.run();
        t2.start();
        // suspend t2 thread
        t2.suspend();
        // call run() method
        t3.start();
        t2.resume();
    }
}
//更多请阅读：https://www.yiibai.com/java_multithreading/java-thread-suspend-method.html

class   Test1{
    public void one(){
        System.out.println("A");
        two();
    }
    public void two(){
        System.out.println("C");
    }

    public static void main(String[] args) {
        Test2 test2 = new Test2();
        test2.one();
    }
}
class Test2 extends Test1{
    public void one(){
        super.one();
        System.out.println("B");
    }
    public void two(){
        System.out.println("D");
        super.two();
    }
}
