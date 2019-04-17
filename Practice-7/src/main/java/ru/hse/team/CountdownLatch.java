package ru.hse.team;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountdownLatch {

    private int counter;
    private final Lock lock = new ReentrantLock();
    private final Condition zero = lock.newCondition();
    private final Condition nonZero = lock.newCondition();

    public CountdownLatch(int counter) {
        this.counter = counter;
    }

    public void await() throws InterruptedException {
        lock.lock();
        try {
            while (counter != 0) {
                zero.wait();
            }
        } finally {
            lock.unlock();
        }
    }

    public void countDown() throws InterruptedException {
        lock.lock();
        try {
            while (counter == 0) {
                nonZero.wait();
            }
            counter--;
            if (counter == 0) {
                zero.signalAll();
            }

        } finally {
            lock.unlock();
        }
    }

    public void countUp() throws InterruptedException {
        lock.lock();
        try {
            counter++;
            nonZero.signalAll();
        } finally {
            lock.unlock();
        }
    }

}
