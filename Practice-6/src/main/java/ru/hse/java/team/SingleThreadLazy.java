package ru.hse.java.team;

public class SingleThreadLazy<T> implements Lazy<T> {
    private T result;
    private Supplier<T> supplier;

    public SingleThreadLazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if (supplier == null) {
            return result;
        }
        result = supplier.get();
        supplier = null;
        return result;
    }
}
