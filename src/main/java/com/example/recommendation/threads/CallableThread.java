package com.example.recommendation.threads;

import java.util.concurrent.Callable;

public class CallableThread implements Callable<String> {

    @Override
    public String call() throws Exception {

        long start = System.currentTimeMillis();
        System.out.println("Callable START: " + Thread.currentThread().getName());

        Thread.sleep(5000); // simulate API/DB call

        long end = System.currentTimeMillis();
        System.out.println("Callable END: " + Thread.currentThread().getName());

        return "Time Taken: " + (end - start) + " ms by Thread :"+ Thread.currentThread().getName();
    }
}