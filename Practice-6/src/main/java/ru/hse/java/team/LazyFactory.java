package ru.hse.java.team;

import org.apache.commons.lang.NotImplementedException;

import java.util.function.Supplier;

public class LazyFactory {

    public static <T> Lazy<T> createSingleThreadLazy(Supplier<T> supplier) {
        return new SingleThreadLazy<>(supplier);
    }

    public static <T> Lazy<T> createSingletonLazy(Supplier<T> supplier) {

        return new SingletonLazy<>(supplier);
    }

    public static <T> Lazy<T> createLockFreeLazy(Supplier<T> supplier) {

        throw new NotImplementedException();
    }

}
