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
