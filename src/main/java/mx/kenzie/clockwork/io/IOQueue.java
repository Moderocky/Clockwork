package mx.kenzie.clockwork.io;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class IOQueue extends ConcurrentLinkedQueue<DataTask> {
    
    protected final ExecutorService service;
    protected final Object lock = new Object();
    protected volatile boolean closing;
    
    public IOQueue() {
        this(50);
    }
    
    public IOQueue(int wait) {
        this(Executors.newSingleThreadExecutor(), wait);
    }
    
    protected IOQueue(ExecutorService service, int wait) {
        this.service = service;
        this.service.submit(() -> {
            while (!closing || !this.isEmpty()) {
                try {
                    final DataTask task = this.poll();
                    if (task == null) {
                        if (closing) break;
                        this.pause(wait);
                        continue;
                    }
                    task.run();
                    task.finish();
                } catch (Throwable ex) {
                    System.err.println("Error in I/O process:");
                    ex.printStackTrace(System.err);
                }
            }
        });
    }
    
    protected static void sleep(long millis) {
        LockSupport.parkNanos(millis * 1000000L);
    }
    
    private void pause(long millis) {
        try {
            synchronized (lock) {
                this.lock.wait(millis);
            }
        } catch (InterruptedException ignored) {
        }
    }
    
    public DataTask queue(DataTask task) {
        synchronized (lock) {
            if (!this.contains(task)) this.add(task);
            this.lock.notifyAll();
        }
        return task;
    }
    
    public void shutdown(long millis) {
        synchronized (lock) {
            this.lock.notifyAll();
        }
        final List<Runnable> tasks = service.shutdownNow();
        try {
            if (!tasks.isEmpty() && !service.awaitTermination(millis, TimeUnit.MILLISECONDS))
                this.report(tasks);
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.err);
            this.report(tasks);
        }
    }
    
    private void report(List<Runnable> list) {
        if (list.isEmpty()) return;
        System.err.println(list.size() + " pending control tasks failed to finish.");
        for (Runnable runnable : list) System.err.println(runnable.getClass() + ": " + runnable);
    }
    
}
