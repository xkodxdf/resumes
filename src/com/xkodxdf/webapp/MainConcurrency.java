package com.xkodxdf.webapp;

import java.util.ArrayList;
import java.util.List;

public class MainConcurrency {

    public static final int THREADS_NUMBER = 10000;
    private int counter;
    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        Thread thread0 = new Thread() {
            @Override
            public void run() {
                System.out.println(getName() + ", " + getState());
                throw new IllegalStateException();
            }
        };
        thread0.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getState());
            }

            private void inc() {
                synchronized (this) {
//                    counter++;
                }
            }
        }).start();
        System.out.println(thread0.getState());
        final MainConcurrency mainConcurrency = new MainConcurrency();
        List<Thread> threads = new ArrayList<>(THREADS_NUMBER);
        for (int i = 0; i < THREADS_NUMBER; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    mainConcurrency.inc();
                }
            });
            thread.start();
            threads.add(thread);
        }
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(mainConcurrency.counter);
    }

    private synchronized void inc() {
//        synchronized (this) {
//        synchronized (MainConcurrency.class) {
        counter++;
//                wait();
//                readFile
//                ...
//        }
    }

    private static class DeadLock {

        private static final Object LOCK_1 = new Object();
        private static final Object LOCK_2 = new Object();
        private static int number = 0;

        public static void main(String[] args) throws InterruptedException {

            Thread t1 = new Thread(() -> {
                for (int i = 0; i < 100; i++) {
                    addOne();
                }
            });

            Thread t2 = new Thread(() -> {
                for (int i = 0; i < 100; i++) {
                    subOne();
                }
            });

            t1.start();
            t2.start();
        }

        private static void addOne() {
            synchronized (LOCK_1) {
                System.out.println(Thread.currentThread().getName() + " in LOCK_1");
                number += 2;
            }
            synchronized (LOCK_2) {
                System.out.println(Thread.currentThread().getName() + " in LOCK_2");
                subOne();
            }
        }

        private static void subOne() {
            synchronized (LOCK_2) {
                System.out.println(Thread.currentThread().getName() + " in LOCK_2");
                number -= 1;
            }
            synchronized (LOCK_1) {
                System.out.println(Thread.currentThread().getName() + " in LOCK_1");
                addOne();
            }
        }
    }
}