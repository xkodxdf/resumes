package com.xkodxdf.webapp;

public class MainDeadlock {
    private static final Object LOCK_1 = new Object();
    private static final Object LOCK_2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> captureLocks(LOCK_1, LOCK_2));
        Thread t2 = new Thread(() -> captureLocks(LOCK_2, LOCK_1));
        t1.start();
        t2.start();
        t1.join();
        System.out.println("cant be print until t1 finish work");
    }


    private static void captureLocks(Object lock1, Object lock2) {
        synchronized (lock1) {
            System.out.println(Thread.currentThread().getName() + " captured lock:" + lock1);
            System.out.println(Thread.currentThread().getName() + " waiting for lock: " + lock2);
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + " captured lock:" + lock2);
                System.out.println(Thread.currentThread().getName() + " waiting for lock:" + lock1);
            }
        }
    }
}
