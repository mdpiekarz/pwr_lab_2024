
    class Library {
        private final int N;
        private final int K;
        private final Object[] readingRooms;
        private final Object[] books;
        private final int[] readingRoomOccupancy;
        private final boolean[] bookInUse;

        public Library(int N, int K) {
            this.N = N;
            this.K = K;
            this.readingRooms = new Object[2];
            this.books = new Object[N];
            this.readingRoomOccupancy = new int[2];
            this.bookInUse = new boolean[N];
            for (int i = 0; i < 2; i++) {
                this.readingRooms[i] = new Object();
                this.readingRoomOccupancy[i] = 0;
            }
            for (int i = 0; i < N; i++) {
                this.books[i] = new Object();
                this.bookInUse[i] = false;
            }
        }

        public void requestBook(int bookId, int readerId) throws InterruptedException {
            int roomIndex = readerId % 2;
            synchronized (readingRooms[roomIndex]) {
                while (true) {
                    if (!isRoomFull(roomIndex)) {
                       synchronized (books[bookId]) {
                            if (!isBookInUse(bookId)) {
                                occupyRoom(roomIndex);
                                occupyBook(bookId);
                                break;
                            }
                            //books[bookId].wait();
                        }
                    }
                    readingRooms[roomIndex].wait();
                }
            }

            System.out.println("Reader " + readerId + " is reading book " + bookId + " in reading room " + roomIndex);
            Thread.sleep(2000); // Simulating reading time
            System.out.println("Reader " + readerId + " returned " + bookId + " and leave room " + roomIndex);
            synchronized (readingRooms[roomIndex]) {
                releaseRoom(roomIndex);
                releaseBook(bookId);
                readingRooms[roomIndex].notifyAll();
                //books[bookId].notifyAll();
            }
        }

        private boolean isRoomFull(int roomIndex) {
            return readingRoomOccupancy[roomIndex] >= K;
        }

        private void occupyRoom(int roomIndex) {
            readingRoomOccupancy[roomIndex]++;
        }

        private void releaseRoom(int roomIndex) {
            readingRoomOccupancy[roomIndex]--;
        }

        private boolean isBookInUse(int bookId) {
            return bookInUse[bookId];
        }

        private void occupyBook(int bookId) {
            bookInUse[bookId] = true;
        }

        private void releaseBook(int bookId) {
            bookInUse[bookId] = false;
        }
    }


