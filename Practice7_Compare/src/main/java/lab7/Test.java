package lab7;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Test {

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static void testAccountLock(){
        AccountLock account = new AccountLock();
        ExecutorService service = Executors.newFixedThreadPool(100); // 创建线程池

        for(int i = 1; i <= 100; i++) {
            service.execute(new DepositThreadLock(account, 10));
        }
        service.shutdown();

        while(!service.isTerminated()) {}

        System.out.println("Balance: " + account.getBalance());
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static void testAccountSync(){
        AccountSync account = new AccountSync();
        ExecutorService service = Executors.newFixedThreadPool(100); // 创建线程池

        for(int i = 1; i <= 100; i++) {
            service.execute(new DepositThreadSync(account, 10));
        }
        service.shutdown();

        while(!service.isTerminated()) {}

        System.out.println("Balance: " + account.getBalance());
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(Test.class.getSimpleName())
                .measurementIterations(3)
                .warmupIterations(1)
                .mode(Mode.AverageTime)
                .timeUnit(TimeUnit.MILLISECONDS)
                .forks(1)   // 使用1个进程来执行基准测试
                .shouldDoGC(true)  //  在执行基准测试时进行垃圾回收
                .build();
        new Runner(options).run();
    }


}