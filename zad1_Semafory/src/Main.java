public class Main {
    public static void main(String[] args) {
        Library library = new Library(10, 2); // 10 books, 2 reading places in room
        int numReaders = 20; // 20 readers

        for (int i = 0; i < numReaders; i++) {
            Reader reader = new Reader(i, library);
            reader.start();
        }
    }
}

