package app;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ScheduledRunner implements Runnable {

    public ScheduledRunner() {}

    public void run() {
        try {
            mainLoop();
        } finally {
            queue.clear();
        }
    }

    public void interrupt() {
        needStop = true;
    }

    public void add(Callable callable, Date date) {
        queue.add(new FIFOEntry(new ScheduledCallable(callable, date)));
    }

    private void mainLoop() {

        try {
            while (!needStop) {
                FIFOEntry<ScheduledCallable> entry = queue.poll(WAIT_QUEUE_TIMEOUT, TimeUnit.MILLISECONDS);
                if (null == entry)
                    continue;
                if (entry.getEntry().getDate().getTime() > System.currentTimeMillis()) {
                    queue.add(entry);
                    // todo: sleep (next time - current) or wait for notification in "add" ?
                    continue;
                }
                entry.getEntry().getCallable().call();
            }
        } catch(InterruptedException exc) {
            // process exc
        }
        catch(Exception exc) {
            // process exc
        }
    }

    private static final long WAIT_QUEUE_TIMEOUT = 1000;

    private volatile boolean needStop = false;

    private PriorityBlockingQueue<FIFOEntry<ScheduledCallable>> queue
            = new PriorityBlockingQueue<FIFOEntry<ScheduledCallable>>();
}
