package mx.kenzie.clockwork.io;

import java.io.IOException;

public abstract class DataTask implements Runnable {

    protected final Object lock = new Object();
    protected volatile boolean ready;

    public boolean isReady() {
        return ready;
    }

    @Override
    public void run() {
        try {
            this.execute();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
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

    public void finish() {
        this.ready = true;
        synchronized (lock) {
            this.lock.notifyAll();
        }
    }

}
