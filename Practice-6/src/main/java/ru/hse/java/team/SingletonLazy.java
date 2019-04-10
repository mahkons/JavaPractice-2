package ru.hse.java.team;

import com.sun.istack.NotNull;

import java.util.function.Supplier;

public class SingletonLazy<T> implements Lazy<T> {

    private volatile Supplier<T> supplier;
    private volatile T value;

    public SingletonLazy(@NotNull Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        if (supplier != null) {
            synchronized (this) {
                if (supplier != null) {
                    value = supplier.get();
                    supplier = null;
                }
            }
        }
        return value;
    }
}
