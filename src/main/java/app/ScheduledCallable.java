package app;

import java.util.Date;
import java.util.concurrent.Callable;

// immutable in terms of Date
public final class ScheduledCallable
        implements Comparable<ScheduledCallable> {

    public ScheduledCallable(Callable callable, Date date) {
        this.callable = callable;
        this.date = new Date(date.getTime());
    }

    public void call() throws Exception {
       callable.call();
    }

    public int compareTo(ScheduledCallable other){
        return Long.compare(this.date.getTime(), other.date.getTime());
    }

    public Callable getCallable() {
        return callable;
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    private final Callable callable;
    private final Date date;
}
