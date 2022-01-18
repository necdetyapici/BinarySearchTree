package com.foreks.feed;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class RecursiveTaskExample extends RecursiveTask<Integer> {
    /**
     *
     */
    private static final long         serialVersionUID = 1L;
    private final int                 n;
    private final int                 minValue;
    private final static ForkJoinPool forkJoinPool     = new ForkJoinPool(4);

    public RecursiveTaskExample(final int n, final int minValue) {
        this.n = n;
        this.minValue = minValue;
    }

    public static void main(final String[] args) {
        singleCore();
        System.out.println("---------------");
        multiCore();
    }

    private static void singleCore() {
        final Instant before = Instant.now();
        final int sonuc = fibonacci(40);
        final Instant after = Instant.now();
        System.out.println(" in " + Duration.between(before, after).toMillis() + " miliseconds result: " + sonuc);
    }

    public static int fibonacci(final int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    private static void multiCore() {
        final RecursiveTaskExample recursiveTaskExample = new RecursiveTaskExample(40, 35);
        final Instant before = Instant.now();
        final Integer sonuc = forkJoinPool.invoke(recursiveTaskExample);
        final Instant after = Instant.now();
        System.out.println(" in " + Duration.between(before, after).toMillis() + " miliseconds result: " + sonuc);
    }

    @Override
    protected Integer compute() {
        if (this.n > this.minValue) {
            if (this.n <= 1) {
                return this.n;
            }
            final RecursiveTaskExample subTask = new RecursiveTaskExample(this.n - 1, this.minValue);
            subTask.fork();
            final RecursiveTaskExample subTask2 = new RecursiveTaskExample(this.n - 2, this.minValue);
            return subTask2.compute() + subTask.join();
        } else {
            return RecursiveTaskExample.fibonacci(this.n);
        }
    }
}
