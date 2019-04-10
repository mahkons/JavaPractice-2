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


    @Test
    void nullSuppler() {
        final Supplier<Object> supplier = new SimpleSupplier();
        
    }

    @Test
    void simpleSupplier() {

    }

    @Test
    void supplerWithCounter() {

    }

}