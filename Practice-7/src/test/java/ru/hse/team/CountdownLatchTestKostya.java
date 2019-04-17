package ru.hse.team;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CountdownLatchTestKostya {

    @Test
    void testSingleThreadDecrease() throws InterruptedException {
        var latch = new CountdownLatch(10);
        for (int i = 0; i < 10; i++) {
            latch.countDown();
        }
        //no locking here
        latch.await();
    }

    private void upAndDown(int upAmount, int downAmount) throws Exception {
        assert upAmount >= downAmount;

        var latch = new CountdownLatch(0);
        Thread[] uppers = new Thread[upAmount];
        Thread[] downs = new Thread[downAmount];

        final int HUNDRED = 100;

        Arrays.setAll(uppers, (i) -> new Thread(() -> {
            for (int i1 = 0; i1 < HUNDRED; i1++) {
                latch.countUp();
            }
        }));

        Arrays.setAll(downs, (i) -> new Thread(() -> {
            for (int i2 = 0; i2 < HUNDRED; i2++) {
                try {
                    latch.countDown();
                } catch (InterruptedException exception) {
                    //Interrupted, stop working
                    break;
                }
            }
        }));

        for (Thread thread : uppers) {
            thread.start();
        }
        for (Thread thread : downs) {
            thread.start();
        }
        for (Thread thread : uppers) {
            thread.join();
        }
        for (Thread thread : downs) {
            thread.join();
        }

        Field field = CountdownLatch.class.getDeclaredField("counter");
        field.setAccessible(true);
        assertEquals(HUNDRED * (upAmount - downAmount), (int)field.get(latch));
    }

    @Test
    void sameUpAndDown() throws Exception {
        upAndDown(100, 100);
    }

    @Test
    void moreUpThanDown() throws Exception {
        upAndDown(500, 100);
    }

}