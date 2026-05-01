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
import java.util.concurrent.*;

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


    @GetMapping(
            value="/start/curry",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String startCurry()
            throws ExecutionException, InterruptedException {
        System.out.println("Main thread START: " + Thread.currentThread().getName());
//        1. washing dished 2. cutting veggies
//                3. making curry
//                4. servicing curry

        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Future<String>> futures = new ArrayList<>();
        Future<String> washingFuture = executor.submit(WashingThread);
        Future<String> cuttingVeggies = executor.submit(CuttingVeggies);
        futures.add(washingFuture);
        futures.add(cuttingVeggies);

        System.out.println("Main continues...");

        StringBuilder result = new StringBuilder();

        int count = 1;
        for (Future<String> future : futures) {
            String res = future.get(); // waits
            result.append("Task ").append(count++).append(": ").append(res).append("\n");
        }
        System.out.println("Sneha Curry START: " + Thread.currentThread().getName());
        Thread.sleep(2000);
        System.out.println("Sneha Curry END: " + Thread.currentThread().getName());
        result.append("Sneha done with curry");

        System.out.println("Raha Serving START: " + Thread.currentThread().getName());
        Thread.sleep(4000);
        System.out.println("Raha Serving END: " + Thread.currentThread().getName());

        result.append("Raha done with Serving");

        return result.toString();
    }

    private Callable<String> WashingThread = () -> {
        long start = System.currentTimeMillis();
        System.out.println("Washing Thread START: " + Thread.currentThread().getName() + " at " + start);

        Thread.sleep(4000);

        long end = System.currentTimeMillis();
        double timeTaken = (end - start) / 1000.0;

        System.out.println("Washing Thread END: " + Thread.currentThread().getName() + " at " + end);
        System.out.println("Washing Time Taken: " + timeTaken + " seconds");

        return "Washing is done in " + timeTaken + " seconds";
    };

    private Callable<String> CuttingVeggies = () -> {
        long start = System.currentTimeMillis();
        System.out.println("Cutting Veggies Thread START: " + Thread.currentThread().getName() + " at " + start);

        Thread.sleep(1000);

        long end = System.currentTimeMillis();
        double timeTaken = (end - start) / 1000.0;

        System.out.println("Cutting Veggies Thread END: " + Thread.currentThread().getName() + " at " + end);
        System.out.println("Cutting Veggies Time Taken: " + timeTaken + " seconds");

        return "Cutting Veggies is done in " + timeTaken + " seconds";
    };

    @GetMapping(
            value="/start/curry/completable",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String startCurryCompletable()
            throws ExecutionException, InterruptedException {
        System.out.println("Main thread START: " + Thread.currentThread().getName());
//        1. washing dished 2. cutting veggies
//                3. making curry
//                4. servicing curry
        StringBuilder result = new StringBuilder();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        System.out.println("Main continues...starting future");
        CompletableFuture<String> washingFuture =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        return WashingThread.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }, executor);

        CompletableFuture<String> cuttingVeggiesFuture =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        return CuttingVeggies.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }, executor);

        CompletableFuture<String> finalFuture =
                washingFuture.thenCombine(cuttingVeggiesFuture,
                                (washingResult, veggiesResult) -> {
                                    result.append("Task 1 : ").append(washingResult).append("\n");
                                    result.append("Task 2 : ").append(veggiesResult).append("\n");
                                    return result.toString();
                                })

                        .thenCompose(previousResult ->
                                CompletableFuture.supplyAsync(() -> {
                                    try {
                                        System.out.println("Sneha Curry START: " + Thread.currentThread().getName());
                                        Thread.sleep(2000);
                                        System.out.println("Sneha Curry END: " + Thread.currentThread().getName());

                                        return previousResult + "Task 3 : Sneha done with curry\n";
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }, executor)
                        )

                        .thenCompose(previousResult ->
                                CompletableFuture.supplyAsync(() -> {
                                    try {
                                        System.out.println("Raha Serving START: " + Thread.currentThread().getName());
                                        Thread.sleep(4000);
                                        System.out.println("Raha Serving END: " + Thread.currentThread().getName());

                                        return previousResult + "Task 4 : Raha done with Serving\n";
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }, executor)
                        );
        System.out.println("Main continues... before join");
        String response = finalFuture.join();
        System.out.println("Main continues... after join");
        executor.shutdown();
        System.out.println("Main continues... after all shutdown");
        return response;
    }

    @GetMapping(
            value = "/start/curry/completable/sequence",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String startCurryCompletableSequence() {

        System.out.println("Main thread START: " + Thread.currentThread().getName());

        ExecutorService executor = Executors.newFixedThreadPool(2);
        StringBuilder result = new StringBuilder();

        CompletableFuture<String> finalFuture =
                CompletableFuture.supplyAsync(() -> {
                            try {
                                return WashingThread.call();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }, executor)

                        .thenCompose(washingResult -> {
                            result.append("Task 1 : ").append(washingResult).append("\n");

                            return CompletableFuture.supplyAsync(() -> {
                                try {
                                    return CuttingVeggies.call();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }, executor);
                        })

                        .thenCompose(veggiesResult -> {
                            result.append("Task 2 : ").append(veggiesResult).append("\n");

                            return CompletableFuture.supplyAsync(() -> {
                                try {
                                    System.out.println("Sneha Curry START: " + Thread.currentThread().getName());
                                    Thread.sleep(2000);
                                    System.out.println("Sneha Curry END: " + Thread.currentThread().getName());

                                    return "Sneha done with curry";
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }, executor);
                        })

                        .thenCompose(curryResult -> {
                            result.append("Task 3 : ").append(curryResult).append("\n");

                            return CompletableFuture.supplyAsync(() -> {
                                try {
                                    System.out.println("Raha Serving START: " + Thread.currentThread().getName());
                                    Thread.sleep(4000);
                                    System.out.println("Raha Serving END: " + Thread.currentThread().getName());

                                    return "Raha done with Serving";
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }, executor);
                        })

                        .thenApply(servingResult -> {
                            result.append("Task 4 : ").append(servingResult).append("\n");
                            return result.toString();
                        });

        String response = finalFuture.join();

        executor.shutdown();

        return response;
    }
}
