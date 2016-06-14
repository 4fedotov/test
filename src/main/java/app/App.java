package app;

import java.util.Date;
import java.util.concurrent.Callable;

public class App {

    public static void main(String[] args) {

        final Callable<Integer> c1 = new Callable() {
            public Integer call() { System.out.println("1"); return 0;}
        };
        final Callable<Integer> c2 = new Callable() {
            public Integer call() { System.out.println("2"); return 0;}
        };

        final Callable<Integer> c3 = new Callable() {
            public Integer call() { System.out.println("3"); return 0;}
        };

        ScheduledRunner runner = new ScheduledRunner();
        new Thread(runner).start();

        Date d = new Date(System.currentTimeMillis() + 2000);
        runner.add(c3, d);
        runner.add(c2, new Date(System.currentTimeMillis()));
        runner.add(c1, d);
        // expected order: 2 3 1
    }
}
