public class ThreadDemo extends Thread{
    Thread thread;
    String threadName;

    public ThreadDemo(String threadName) {
        this.threadName = threadName;
        super.run();
    }

    public void run() {
        try {
            for (int i = 0; i <= 4; i++) {
                System.out.println("current is running thread name " + threadName + " " + i);
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread " + threadName + " interrupted.");
        }
        System.out.println("Thread " + threadName + " exiting.");

    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (thread == null) {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }
}
