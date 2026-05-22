package com.example.recommendation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("api/v1/atomic")
@RequiredArgsConstructor
public class Atomicity {

    int donotSync = 1;
    int value = 1; //2,4,8,16,32
    private final Object valueLock = new Object();
    public  int multiply() throws InterruptedException {
        System.out.println("Callable START: " + Thread.currentThread().getName()+ "in MUtliply method");
        donotSync = donotSync*2;
        synchronized (valueLock){
            Thread.sleep(5000);
            System.out.println("Callable START: " + Thread.currentThread().getName()+ "in MUtliply method");
            value =  value*2;
            return value;
        }

    }

    public synchronized int multiply(int number){
        number =  number*2;
        return number;
    }

    @GetMapping(value = "/value", produces = MediaType.APPLICATION_JSON_VALUE)
    public int getValue() throws ExecutionException, InterruptedException {
        System.out.println("Main thread START: " + Thread.currentThread().getName() + " Value : "+value);
        int number = 1;

        value  = 1;

        AtomicInteger atomicInteger = new AtomicInteger(1);
        ExecutorService executors = Executors.newFixedThreadPool(5);

        Callable<Integer> t1 = () -> {
            long start = System.currentTimeMillis();
            System.out.println("Callable START: " + Thread.currentThread().getName());

            Thread.sleep(5000); // simulate API/DB call
            int updatedValue =  atomicInteger.updateAndGet(n->n*2);
            int updateNumber = multiply(number);
            long end = System.currentTimeMillis();
            System.out.println("Callable END: " + Thread.currentThread().getName()+ " Value : "+value + " number : "+number
            + " Updated Number" + updateNumber);

            System.out.println("Time Taken: " + (end - start) + " ms by Thread :"+ Thread.currentThread().getName());

            return updatedValue;

        };

        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Future<Integer> f1 = executors.submit(t1);
            futures.add(f1);
        }
        for (Future<Integer> future : futures) {
            future.get();
        }
        executors.shutdown();

        return atomicInteger.get();
    }
}
