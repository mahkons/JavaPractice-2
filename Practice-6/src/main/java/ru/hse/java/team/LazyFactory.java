package ru.hse.java.team;

import org.apache.commons.lang.NotImplementedException;

import java.util.function.Supplier;

public class LazyFactory {

    public static <T> Lazy<T> createSingletonLazy(Supplier<T> supplier) {

        throw new NotImplementedException();
    }

    public static <T> Lazy<T> createLockFreeLazy(Supplier<T> supplier) {

        throw new NotImplementedException();
    }
}