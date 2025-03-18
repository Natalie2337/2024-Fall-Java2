import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue<E> {
    private Queue<E> queue;
    private int capacity;

    private ReentrantLock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public MyBlockingQueue(int capacity){
        //TODO: Constructor with input capacity
        this.capacity = capacity;
        this.queue = new LinkedList<>(); // new一个LinkedList类型的队列
    }

    public void put(E e) throws InterruptedException {
        //TODO: When the queue is full, wait until the consumer takes data and the queue has some empty buffer
        lock.lock();
        try{
            while(queue.size() == capacity){  //if (queue.size() == capacity) // 用while而不用if可以再次判断
                notFull.await();
            }
            queue.add(e);
            System.out.println("Thread:" + Thread.currentThread().getName()+" "+ "Produced:" + e.toString()+" "+"Queue:"+queue);
            // notFull.signalAll(); // 通知序列现在不为满
            notEmpty.signalAll();  // ?为啥不是notFull
        } finally{
            lock.unlock();
        }
        // notifyAll()?
    }

    public E take() throws InterruptedException {
        //TODO: When queue empty, wait until the producer puts new data into the queue
        E e;
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();  // signalAll()方会唤醒所有正在等待该对象的线程，使它们从等待状态进入可运行状态。
            }
            e = queue.poll();
            System.out.println("Thread:" + Thread.currentThread().getName()+" "+ "Consumed:" + e.toString()+" "+"Queue:"+queue);
            //notEmpty.signalAll();
            notFull.signalAll();
        } finally {
            lock.unlock();
        }
        return e;
    }




    public static void main(String[] args) throws InterruptedException {

        int CAPACITY = 200;
        int PRODUCER_WORK = 20;
        int PRODUCER_CNT = 10;
        int PRODUCER_OFF = 10;
        int CONSUMER_WORK = 20;
        int CONSUMER_CNT = 10;
        int CONSUMER_OFF = 10;

        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(CAPACITY);

        Runnable producer = () -> {
            for(int i=0; i<PRODUCER_WORK; i++){
                try {
                    queue.put(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(PRODUCER_OFF);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable consumer = () -> {
            for(int i=0; i<CONSUMER_WORK; i++){
                try {
                    queue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(CONSUMER_OFF);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        for (int i = 0; i < PRODUCER_CNT; i++) {
            new Thread(producer).start();
        }
        for (int i = 0; i < CONSUMER_CNT; i++) {
            new Thread(consumer).start();
        }

    }

}
