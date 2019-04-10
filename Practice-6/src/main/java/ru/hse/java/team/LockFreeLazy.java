package ru.hse.java.team;

import com.sun.istack.NotNull;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class LockFreeLazy<T> implements Lazy<T> {

    private volatile Supplier<T> supplier;
    private AtomicReference<T> value = new AtomicReference<>(null);

    public LockFreeLazy(@NotNull Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        Supplier<T> supplierCopy = supplier;
        if (supplierCopy != null) {
            supplier = null;
            value.compareAndSet(null, supplierCopy.get());
        }
        return value.get();
    }
}
