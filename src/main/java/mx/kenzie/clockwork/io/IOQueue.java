package mx.kenzie.clockwork.io;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class IOQueue extends ConcurrentLinkedQueue<DataTask> {
    
    private final ExecutorService service = Executors.newSingleThreadExecutor();
    protected volatile boolean closing;
    
    public IOQueue() {
        this(50);
    }
    
    public IOQueue(int wait) {
        this.service.submit(() -> {
            while (!closing || !this.isEmpty()) {
                try {
                    final DataTask task = this.poll();
                    if (task == null) {
                        if (closing) break;
                        sleep(wait);
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
    
    public synchronized DataTask queue(DataTask task) {
        if (!this.contains(task)) this.add(task);
        return task;
    }
    
    public void shutdown(long millis) {
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
