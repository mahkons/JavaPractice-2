package ru.hse.java.team;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class SingleThreadLazyTest {


    public class NullSupplier implements Supplier<Object> {
        int counter = 0;
        @Override
        public Object get() {
            counter++;
            if (counter != 1) {
                throw new AssertionError("NullSupplier called more than once");
            }
            return null;
        }
    }

    @Test
    void SingleThreadLazyShouldBeCalledOnce() {
        Supplier<Object> nullSupplier = new NullSupplier();
        Lazy<Object> lazy = LazyFactory.createSingleThreadLazy(nullSupplier);
        assertNull(lazy.get());
        assertNull(lazy.get());
    }

}