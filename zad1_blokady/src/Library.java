import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Library {
    private final int N;
    private final int K;
    private final Lock lock = new ReentrantLock();
    private final Condition[] readingRoomAvailable;
    private final Condition[] bookAvailable;
    private int[] readingRoomOccupancy;
    private boolean[] bookInUse;

    public Library(int N, int K) {
        this.N = N;
        this.K = K;
        this.readingRoomAvailable = new Condition[2];
        this.bookAvailable = new Condition[N];
        this.readingRoomOccupancy = new int[2];
        this.bookInUse = new boolean[N];
        for (int i = 0; i < 2; i++) {
            this.readingRoomAvailable[i] = lock.newCondition();
            this.readingRoomOccupancy[i] = 0;
        }
        for (int i = 0; i < N; i++) {
            this.bookAvailable[i] = lock.newCondition();
            this.bookInUse[i] = false;
        }
    }

    public void requestBook(int bookId, int readerId) throws InterruptedException {
        Thread.sleep(1000); // Simulating reading time
        lock.lock();
        int roomIndex = readerId % 2;
        try {
            while (readingRoomOccupancy[roomIndex] >= K) {
                readingRoomAvailable[roomIndex].await();
            }

            while (bookInUse[bookId]) {
                bookAvailable[bookId].await();
            }

            readingRoomOccupancy[roomIndex]++;
            bookInUse[bookId] = true;

            System.out.println("Reader " + readerId + " is reading book " + bookId + " in reading room " + roomIndex);
            Thread.sleep(4000); // Simulating reading time
            System.out.println("Reader " + readerId + " returned " + bookId + " and leave room " + roomIndex);
            readingRoomOccupancy[roomIndex]--;
            bookInUse[bookId] = false;
            readingRoomAvailable[roomIndex].signalAll();
            bookAvailable[bookId].signalAll();

        } finally {

            lock.unlock();
        }
    }
}



