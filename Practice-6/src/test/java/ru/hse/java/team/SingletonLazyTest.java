package ru.hse.java.team;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class SingletonLazyTest {

    public class NullSupplier implements Supplier<Object> {
        int counter = 0;
        @Override
        public Object get() {
            synchronized (this) {
                counter++;
                if (counter != 1) {
                    throw new AssertionError("NullSupplier called more than once");
                }
            }
            return null;
        }
    }

    public class SimpleSupplier implements Supplier<Object> {

        @Override
        public Object get() {
            return new Object();
        }
    }

    public class SupplierWithCounter implements Supplier<Object> {

        private AtomicInteger counter = new AtomicInteger();
        @Override
        public Object get() {
            int value = counter.incrementAndGet();
            if (value != 1) {
                throw new AssertionError("SupplierWithCounter called more than once");
            }
            return new Object();
        }
    }

    private <T> Runnable runnableForAnySupplier(Lazy<Object> lazy) {
        return new Runnable() {
            @Override
            public void run() {
                Object value_first = lazy.get();
                Object value_second = lazy.get();
                if (value_first != value_second) {
                    throw new AssertionError("lazy returned different objects");
                }
            }
        };
    }


    @Test
    void nullSupplier() {

        for (int i = 0; i < 10_000; i++) {
            Supplier<Object> supplier = new NullSupplier();
            Lazy lazy = LazyFactory.createSingletonLazy(supplier);
            Runnable runnable = runnableForAnySupplier(lazy);
            Thread actorFirst = new Thread(runnable);
            Thread actorSecond = new Thread(runnable);

            actorFirst.start();
            actorSecond.start();
        }
    }

    @Test
    void simpleSupplier() {
        for (int i = 0; i < 10_000; i++) {
            Supplier<Object> supplier = new SimpleSupplier();
            Lazy lazy = LazyFactory.createSingletonLazy(supplier);
            Runnable runnable = runnableForAnySupplier(lazy);
            Thread actorFirst = new Thread(runnable);
            Thread actorSecond = new Thread(runnable);

            actorFirst.start();
            actorSecond.start();
        }
    }

    @Test
    void supplerWithCounter() {
        for (int i = 0; i < 10_000; i++) {
            Supplier<Object> supplier = new SupplierWithCounter();
            Lazy lazy = LazyFactory.createSingletonLazy(supplier);
            Runnable runnable = runnableForAnySupplier(lazy);
            Thread actorFirst = new Thread(runnable);
            Thread actorSecond = new Thread(runnable);

            actorFirst.start();
            actorSecond.start();
        }
    }

    @Test
    void bigNumberOfThreads() {
        for (int j = 0; j < 10; j++) {
            Supplier<Object> supplier = new SupplierWithCounter();
            Lazy lazy = LazyFactory.createSingletonLazy(supplier);
            Runnable runnable = runnableForAnySupplier(lazy);

            Thread[] threads = new Thread[1000];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(runnable);
            }
            for (int i = 0; i < threads.length; i++) {
                threads[i].start();
            }
        }
    }

}