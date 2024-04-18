class Reader extends Thread {
    private final int id;
    private final Library library;

    public Reader(int id, Library library) {
        this.id = id;
        this.library = library;
    }

    public void run() {
        while(true) {
            try {
                int bookId = (int) (Math.random() * 10); // Random book selection
                library.requestBook(bookId, id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
