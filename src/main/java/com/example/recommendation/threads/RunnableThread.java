package com.example.recommendation.threads;

public class RunnableThread implements Runnable {

    @Override
    public void run() {

        long startTime = System.currentTimeMillis();
        System.out.println("Worker STARTED at: " + startTime +
                " | Thread: " + Thread.currentThread().getName());

        try {
            Thread.sleep(5000); // ⏸️ sleep for 2 seconds
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Worker FINISHED at: " + endTime +
                " | Thread: " + Thread.currentThread().getName());

        System.out.println("Total Time Taken: " + (endTime - startTime) + " ms");
    }
}