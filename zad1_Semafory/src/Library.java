import java.util.concurrent.Semaphore;
class Library {
    private final int N;
    private final int K;
    private final Semaphore[] readingRooms;
    private final Semaphore infoDesk = new Semaphore(1);
    private final Semaphore[] bookAvailable;

    public Library(int N, int K) {
        this.N = N;
        this.K = K;
        this.readingRooms = new Semaphore[2];
        this.readingRooms[0] = new Semaphore(K);
        this.readingRooms[1] = new Semaphore(K);
        this.bookAvailable = new Semaphore[N];
        for (int i = 0; i < N; i++) {
            this.bookAvailable[i] = new Semaphore(1);
        }
    }

    public void requestBook(int bookId, int readerId) throws InterruptedException {
        infoDesk.acquire();
        int roomIndex = readerId % 2;
        readingRooms[roomIndex].acquire();
        infoDesk.release();

        bookAvailable[bookId].acquire();

        System.out.println("Reader " + readerId + " is reading book " + bookId + " in reading room " + roomIndex);
        Thread.sleep(2000); // Simulating reading time
        System.out.println("Reader " + readerId + " returned " + bookId + " and leave room " + roomIndex);
        bookAvailable[bookId].release();
        readingRooms[roomIndex].release();
    }
}
