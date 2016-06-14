package app;

import java.util.concurrent.atomic.AtomicLong;

// This class applies first-in-first-out tie-breaking to comparable elements for PriorityBlockingQueue
// https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/PriorityBlockingQueue.html
public class FIFOEntry<E extends Comparable<? super E>>
        implements Comparable<FIFOEntry<E>> {
    static final AtomicLong seq = new AtomicLong(0);
    final long seqNum;
    final E entry;
    public FIFOEntry(E entry) {
        seqNum = seq.getAndIncrement();
        this.entry = entry;
    }
    public E getEntry() { return entry; }
    public int compareTo(FIFOEntry<E> other) {
        int res = entry.compareTo(other.entry);
        if (res == 0 && other.entry != this.entry)
            res = (seqNum < other.seqNum ? -1 : 1);
        return res;
    }
}
