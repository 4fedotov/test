package app;

import java.util.Date;
import java.util.concurrent.Callable;

public class ScheduledCallable
        implements Comparable<ScheduledCallable> {

    public ScheduledCallable(Callable callable, Date date) {
        this.callable = callable;
        this.date = date;
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
        return date;
    }

    private Callable callable;
    private Date date;
}
