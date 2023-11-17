package mx.kenzie.clockwork.io;

import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.util.function.Consumer;

public abstract class DataTask implements Runnable {
    
    protected final Object lock = new Object();
    protected volatile boolean ready;
    protected Consumer<Throwable> errorHandler = Throwable::printStackTrace;
    
    public boolean isReady() {
        return ready;
    }
    
    @Override
    public void run() {
        try {
            this.execute();
        } catch (ThreadDeath death) {
            throw death;
        } catch (IOException | InterruptedException ex) {
            this.errorHandler.accept(ex);
        }
    }
    
    public abstract void execute() throws IOException, InterruptedException;
    
    public void await() {
        while (!ready) {
            try {
                synchronized (lock) {
                    this.lock.wait();
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
    
    @Contract(pure = true)
    public void await(long timeout) {
        if (ready) return;
        final long start = System.currentTimeMillis(), end = (start + timeout) - 5;
        do {
            try {
                synchronized (lock) {
                    this.lock.wait(timeout);
                }
                break;
            } catch (InterruptedException ignored) {
            }
        } while (!ready && System.currentTimeMillis() < end);
    }
    
    public void finish() {
        this.ready = true;
        synchronized (lock) {
            this.lock.notifyAll();
        }
    }
    
}
