package app;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;

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

        while (!needStop) {
            try {

                if (queue.isEmpty()) {
                    queue.wait(WAIT_QUEUE_TIMEOUT);
                    continue;
                }

                FIFOEntry<ScheduledCallable> entry = queue.peek();
                if (entry.getEntry().getDate().getTime() > System.currentTimeMillis())
                    continue;

                entry = queue.take();
                entry.getEntry().getCallable().call();

            } catch(InterruptedException exc) {
                // process exc
            } catch(Exception exc) {
                // process exc
            }
        }
    }

    private static final long WAIT_QUEUE_TIMEOUT = 1000;

    private volatile boolean needStop = false;

    private PriorityBlockingQueue<FIFOEntry<ScheduledCallable>> queue
            = new PriorityBlockingQueue<FIFOEntry<ScheduledCallable>>();
}
