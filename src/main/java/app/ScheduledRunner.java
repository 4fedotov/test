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
            synchronized(queue) {
                queue.clear();
            }
        }
    }

    public void interrupt() {
        if (thisThread != null) {
            thisThread.interrupt();
        }
    }

    public void add(Callable callable, Date date) {
        queue.add(new FIFOEntry(new ScheduledCallable(callable, date)));
    }

    private void mainLoop() {

        thisThread = Thread.currentThread();

        while (!thisThread.isInterrupted()) {
            try {
                while (queue.isEmpty())
                    queue.wait();

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

    private volatile Thread thisThread;

    private PriorityBlockingQueue<FIFOEntry<ScheduledCallable>> queue
            = new PriorityBlockingQueue<FIFOEntry<ScheduledCallable>>();
}
