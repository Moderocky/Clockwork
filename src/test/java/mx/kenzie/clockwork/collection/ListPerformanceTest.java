package mx.kenzie.clockwork.collection;

import mx.kenzie.clockwork.collection.primitive.ByteList;
import mx.kenzie.clockwork.collection.primitive.IntList;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;

public class ListPerformanceTest {
    public static final int SIZE_BYTES = 100_000_000;
    public static final int ITERATIONS = 10;
    private static final Thread INT_LIST_THREAD, BYTE_LIST_THREAD, ARRAY_LIST_THREAD;

    static {
        INT_LIST_THREAD = new Thread(() -> {
            final IntList list = new IntList();
            for (int i = 0; i < SIZE_BYTES / (Integer.SIZE / Byte.SIZE); i++) {
                list.add(i);
            }
        });

        INT_LIST_THREAD.setName("IntList");

        BYTE_LIST_THREAD = new Thread(() -> {
            final ByteList list = new ByteList();
            for (int i = 0; i < SIZE_BYTES; i++) {
                list.add((byte) i);
            }
        });

        BYTE_LIST_THREAD.setName("ByteList");

        ARRAY_LIST_THREAD = new Thread(() -> {
            final java.util.ArrayList<Byte> list = new java.util.ArrayList<>();
            for (int i = 0; i < SIZE_BYTES; i++) {
                list.add((byte) i);
            }
        });

        ARRAY_LIST_THREAD.setName("ArrayList");
    }

    private static void time(Thread... threads) throws InterruptedException {
        final List<Thread> groups = new ArrayList<>(threads.length);
        for (Thread thread : threads) {
            final Thread group = new Thread(() -> {
                final LongSummaryStatistics stats = new LongSummaryStatistics();
                for (int i = 0; i < ITERATIONS; i++) {
                    final long start = System.currentTimeMillis();
                    thread.run();

                    final long end = System.currentTimeMillis();
                    stats.accept(end - start);
                }
                System.out.println(thread.getName() + " took " + stats.getAverage() + "ms on average.");
            });
            group.setName(thread.getName());
            group.start();
            groups.add(group);
        }

        for (Thread group : groups) {
            group.join();
        }
    }

    @Test
    public void testLists() throws InterruptedException {
        time(INT_LIST_THREAD, BYTE_LIST_THREAD, ARRAY_LIST_THREAD);
    }
}
