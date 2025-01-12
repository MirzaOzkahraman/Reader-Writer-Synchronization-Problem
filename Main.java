import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ReadWriteLock RW = new ReadWriteLock();

        executorService.execute(new Writer(RW));
        executorService.execute(new Writer(RW));
        executorService.execute(new Writer(RW));
        executorService.execute(new Writer(RW));

        executorService.execute(new Reader(RW));
        executorService.execute(new Reader(RW));
        executorService.execute(new Reader(RW));
        executorService.execute(new Reader(RW));

        executorService.shutdown();
    }
}

class ReadWriteLock {
    private Semaphore readerSemaphore = new Semaphore(1, true);
    private Semaphore writerSemaphore = new Semaphore(1, true);
    private int readers = 0;

    public void readLock() {
        try {
            readerSemaphore.acquire();
            readers++;
            if (readers == 1) {
                writerSemaphore.acquire();
            }
            readerSemaphore.release();
            System.out.println(Thread.currentThread().getName() + " is reading. Active Readers: " + readers);
        } catch (InterruptedException e) {
            System.out.println("Reader interrupted");
            Thread.currentThread().interrupt();
        }
    }

    public void writeLock() {
        try {
            writerSemaphore.acquire();
            System.out.println(Thread.currentThread().getName() + " is writing.");
        } catch (InterruptedException e) {
            System.out.println("Writer interrupted");
            Thread.currentThread().interrupt();
        }
    }

    public void readUnLock() {
        try {
            readerSemaphore.acquire();
            readers--;
            System.out.println(Thread.currentThread().getName() + " finished reading. Active Readers: " + readers);
            if (readers == 0) {
                writerSemaphore.release();
            }
            readerSemaphore.release();
        } catch (InterruptedException e) {
            System.out.println("Reader interrupted during unlock");
            Thread.currentThread().interrupt();
        }
    }

    public void writeUnLock() {
        System.out.println(Thread.currentThread().getName() + " finished writing.");
        writerSemaphore.release();
    }
}


class Writer implements Runnable {
    private ReadWriteLock RW_lock;
    private volatile boolean running = true;

    public Writer(ReadWriteLock rw) {
        RW_lock = rw;
    }

    public void run() {
        while (running) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("interrupt occurred");
                Thread.currentThread().interrupt();
            }

            if (!running) {
                break;
            }
            System.out.println("Writer requesting permission");
            RW_lock.writeLock();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("interrupt occurred");
                Thread.currentThread().interrupt();
            }
            RW_lock.writeUnLock();
        }
    }

    public void stop() {
        running = false;
    }
}

class Reader implements Runnable {
    private ReadWriteLock RW_lock;
    private volatile boolean running = true;

    public Reader(ReadWriteLock rw) {
        RW_lock = rw;
    }

    public void run() {
        while (running) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("interrupt occurred");
                Thread.currentThread().interrupt();
            }
             if (!running) {
                break;
            }

            System.out.println("Reader asks permission");
            RW_lock.readLock();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("interrupt occurred");
                Thread.currentThread().interrupt();
            }
            RW_lock.readUnLock();
        }
    }
    public void stop() {
        running = false;
    }
}