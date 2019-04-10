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

    private <T> void testAnySupplier(final Supplier<T> supplier) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Lazy lazy = LazyFactory.createSingletonLazy(supplier);
                Object value_first = lazy.get();
                Object value_second = lazy.get();
                if (value_first != value_second) {
                    throw new AssertionError("lazy returned different objects");
                }
            }
        };

        Thread actor_first = new Thread(runnable);
        Thread actor_second = new Thread(runnable);

        for (int i = 0; i < 100_000; i++) {
            actor_first.run();
            actor_second.run();
        }
    }


    @Test
    void nullSuppler() {
        testAnySupplier(new NullSupplier());
    }

    @Test
    void simpleSupplier() {
        testAnySupplier(new SimpleSupplier());
    }

    @Test
    void supplerWithCounter() {
        testAnySupplier(new SupplierWithCounter());
    }

}