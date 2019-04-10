package ru.hse.java.team;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class LockFreeLazyTest {

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
    void nullSuppler() {

        for (int i = 0; i < 100_000; i++) {
            Supplier<Object> supplier = new NullSupplier();
            Lazy lazy = LazyFactory.createSingletonLazy(supplier);
            Runnable runnable = runnableForAnySupplier(lazy);
            Thread actorFirst = new Thread();
            Thread actorSecond = new Thread();

            actorFirst.run();
            actorSecond.run();
        }
    }

    @Test
    void simpleSupplier() {
        for (int i = 0; i < 100_000; i++) {
            Supplier<Object> supplier = new SimpleSupplier();
            Lazy lazy = LazyFactory.createSingletonLazy(supplier);
            Runnable runnable = runnableForAnySupplier(lazy);
            Thread actorFirst = new Thread();
            Thread actorSecond = new Thread();

            actorFirst.run();
            actorSecond.run();
        }
    }

    @Test
    void supplerWithCounter() {
        for (int i = 0; i < 100_000; i++) {
            Supplier<Object> supplier = new SupplierWithCounter();
            Lazy lazy = LazyFactory.createSingletonLazy(supplier);
            Runnable runnable = runnableForAnySupplier(lazy);
            Thread actorFirst = new Thread();
            Thread actorSecond = new Thread();

            actorFirst.run();
            actorSecond.run();
        }
    }

}