package com.example.recommendation.controller;


import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.*;

@RestController
@RequestMapping("api/v1/function")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Auth API", description = "Authentication APIs for User")
public class FunctionController {

    private final MeterRegistry meterRegistry;

    Consumer<String> consumer = (givenName)->{
        System.out.println("Inside Consumer Function");
        System.out.println(String.format("Hello %s",givenName));
    };

//    private String getConsumer(String name){
//        System.out.println("Inside Consumer Function");
//        System.out.println(String.format("Hello %s",givenName));
//    }

    Function<String,String> function = (givenName)->{
        System.out.println("Inside Consumer Function");
        return String.format("Hello %s",givenName);
    };

    Supplier<String> supplier = ()->{
        System.out.println("Inside Consumer Function");
        return "Hello Sneha";
    };


    Predicate<String> predicate = givenName -> givenName.equalsIgnoreCase("Sneha");

//    boolean  result = name.equalsIgnoreCase("Sneha");
//        return result;
    // consumer?name="sneha"
    @GetMapping(
            value="/consumer",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void consumerFunction(@RequestParam(name="name") String name) {
        meterRegistry.counter("function.api.calls", "type", "consumer").increment();
        consumer.accept(name);
    }

    @GetMapping(
            value="/function",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String getFunction(@RequestParam(name="name") String name){
        meterRegistry.counter("function.api.calls", "type", "function").increment();
        return function.apply(name);
    }

    @GetMapping(
            value="/supplier",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String getSupplier(){
        meterRegistry.counter("function.api.calls", "type", "supplier").increment();
        return supplier.get();
    }


    @GetMapping(
            value="/predicate",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public boolean predicateFunction(@RequestParam(name="name") String name) {
        meterRegistry.counter("function.api.calls", "type", "predicate").increment();

        int[] arr = new int[]{1,2,3,4,6};

        List<Integer> result = new ArrayList<>();

        for (int num : arr) {
            if (num % 2 == 0) {
                result.add(num);
            }
        }

        List<Integer> newResult = new ArrayList<>();
        for (int num : result) {
            if (num == 2 || num == 4) {
                newResult.add(num);
            }
        }


        IntPredicate predicateNumFunc = num -> num%2==0;
        IntPredicate predicateNumTwoOrFour = num -> num == 2 || num == 4;
        int[] resultPr = Arrays.stream(arr)
                .filter(predicateNumFunc)
                .filter(predicateNumTwoOrFour)
                .toArray();

        return predicate.test(name);
    }
}
