方式一：继承runnable 

```java

public class RunnableDemo implements Runnable {
    Thread thread;
    String threadName;

    public RunnableDemo(String threadName) {
        this.threadName = threadName;
    }

    @Override
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

public class testRunnableDemo{
    public static void main(String args[]) {
        RunnableDemo R1 = new RunnableDemo( "Thread-1");
        R1.start();

        RunnableDemo R2 = new RunnableDemo( "Thread-2");
        R2.start();
    }
}

结果：
Starting Thread-1
Starting Thread-2
current is running thread name Thread-1 0
current is running thread name Thread-2 0
current is running thread name Thread-2 1
current is running thread name Thread-1 1
current is running thread name Thread-1 2
current is running thread name Thread-2 2
current is running thread name Thread-2 3
current is running thread name Thread-1 3
current is running thread name Thread-2 4
current is running thread name Thread-1 4
Thread Thread-2 exiting.
Thread Thread-1 exiting.
```

方式二：

```java
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
public class testRunnableDemo{
    public static void main(String args[]) {
//        RunnableDemo R1 = new RunnableDemo( "Thread-1");
//        R1.start();
//
//        RunnableDemo R2 = new RunnableDemo( "Thread-2");
//        R2.start();

        ThreadDemo R1 = new ThreadDemo( "Thread-1");
        R1.start();

        ThreadDemo R2 = new ThreadDemo( "Thread-2");
        R2.start();
    }
}

运行结果：
    Starting Thread-1
Starting Thread-2
current is running thread name Thread-1 0
current is running thread name Thread-2 0
current is running thread name Thread-2 1
current is running thread name Thread-1 1
current is running thread name Thread-2 2
current is running thread name Thread-1 2
current is running thread name Thread-2 3
current is running thread name Thread-1 3
current is running thread name Thread-2 4
current is running thread name Thread-1 4
Thread Thread-2 exiting.
Thread Thread-1 exiting.

```

方式三：Callable



```
import java.util.concurrent.Callable;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableThreadTest
{
    public static void main(String[] args) {
        ExecutorService threadPool2 = Executors.newFixedThreadPool(10);
        CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(threadPool2);
        //向CompletionService中提交10个任务
        for(int i=1;i<=10;i++){
            final int sequence = i;//记录任务序号
            completionService.submit(
                    new Callable<Integer>(){
                        public Integer call() throws Exception {
                            //每个任务设置随机耗时时间
                            Thread.sleep(new Random().nextInt(5000));
                            return sequence;//返回的是当前任务的序号
                        }
                    });
        }
        //获取结果
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("get result:"+completionService.take().get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}

运行结果：
get result:10
get result:9
get result:5
get result:3
get result:7
get result:8
get result:1
get result:6
get result:4
get result:2
```

方式三 变种：这种方式用的是invokeAll 而不是submit

```
package callables;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.util.concurrent.Executors.newFixedThreadPool;

class Callables {

    List<String> performMultipleTasks(int count) throws Exception {
        List<Callable<String>> tasks = prepareTasks(count);
        ExecutorService executorService = newFixedThreadPool(count);
        List<Future<String>> futures = executorService.invokeAll(tasks);
        return buildOutput(count, futures);
    }
    
    private List<String> buildOutput(int count, List<Future<String>> futures) throws Exception {
        List<String> output = new ArrayList<>(count);
        for (Future<String> future: futures) {
            output.add(future.get());
        }
        return output;
    }
    
    private List<Callable<String>> prepareTasks(int count) {
        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tasks.add(this::task);
        }
        return tasks;
    }
    
    private String task() throws InterruptedException {
        Thread.sleep(2000);
        return new Random().toString();
    }

}
```

其中方式一和方式二没有返回结果，方式三有返回结果
