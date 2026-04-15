package com.example.recommendation.controller;


import com.example.recommendation.threads.CallableThread;
import com.example.recommendation.threads.RunnableThread;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@RequestMapping("api/v1/thread")
@RequiredArgsConstructor
public class ThreadController {

    @GetMapping(
            value="/runnable",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String getThreadOperations() throws SQLException {

        // Runnable
        // Callable

        System.out.println("Main thread START: " + Thread.currentThread().getName());

        Thread thread = new Thread(new RunnableThread());

        thread.start();

        System.out.println("Main thread CONTINUES: " + Thread.currentThread().getName());
        return "Thread Check";
    }


    @GetMapping(
            value="/callable",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String getCallableThreadOperations() throws ExecutionException, InterruptedException {
        System.out.println("Main thread START: " + Thread.currentThread().getName());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        CallableThread task = new CallableThread();

        // submit task
        Future<String> future = executor.submit(task);

        System.out.println("Main continues...");
        String result = future.get();

        System.out.println("Result: " + result);

        executor.shutdown();

        return result;
    }

    @GetMapping(
            value="/callable/limited",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String getCallableThreadOperations(
            @RequestParam(name="numThreads") int numThreads,
            @RequestParam(name="numTasks") int numTasks
    )
            throws ExecutionException, InterruptedException {
        System.out.println("Main thread START: " + Thread.currentThread().getName());

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        List<Future<String>> futures = new ArrayList<>();
        for (int i = 1; i <= numTasks; i++) {
            CallableThread task = new CallableThread();
            Future<String> future = executor.submit(task);
            futures.add(future);
        }

        System.out.println("Main continues...");

        StringBuilder result = new StringBuilder();

        int count = 1;
        for (Future<String> future : futures) {
            String res = future.get(); // waits
            result.append("Task ").append(count++).append(": ").append(res).append("\n");
        }

        executor.shutdown();

        return result.toString();
    }
}
