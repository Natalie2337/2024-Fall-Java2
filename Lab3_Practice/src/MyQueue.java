public class MyQueue<E> {
    private E[] element;    // E[] element 泛型是用来表示一个数组中的元素类型的占位符
    private int size = 0;     //数组中目前已插入了几个数（最终有几个数）
    private int capacity;    //数组的长度（固定）
    private int DEFAULT_CAPACITY = 10;

    private int head = 0;
    private int tail = 0;


    //TODO: Constructor with default capacity
    public MyQueue(){
        this.capacity = DEFAULT_CAPACITY;
        this.element = (E[]) new Object[capacity];
    }

    //TODO: Constructor with input capacity
    public MyQueue(int o){
        this.capacity = o;
        this.element = (E[]) new Object[capacity];
    }

    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions.
     *
     * @param e the element to add
     * @return {@code true} if the element was added to this queue, else
     *         {@code false}
     */
    //TODO: implement a public method: enQueue according the above description
    public boolean enQueue(E e) {
        size = size+1 ; // size应该每加入一个元素就加1
        if (size <= capacity){
            element[size-1] = e;
            tail = size-1;
            return true;
        }else{
            size = size-1;
            //System.out.println("fail to add a new element");
            return false;
        }
    }


    /**
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    //TODO: implement a public method: deQueue according the above description
    public E deQueue(){
        E head_e = element[head];
        if (head_e != null){  //至少有1个元素可以出来
            for (int i = 0; i < size-1; i++){
                element[i] = element[i+1];
            }
            element[size-1] = null;
            tail = tail-1;
            size = size-1;
            return head_e;
        }else{
            System.out.println("fail to remove the first element");
            return null;
        }
    }



    /**
     * Print all elements from the head to tail.
     */
    public void showElements() {
        //System.out.println("size: "+size);
        for (int i = 0; i < size; i++) {
            System.out.println(element[(i+head) % element.length]);
        }
    }

    public static void main(String[] args) {
        System.out.println("**********Integer queue test**********");
        MyQueue myQueue = new MyQueue(4);
        myQueue.enQueue(6);
        myQueue.enQueue(7);
        myQueue.enQueue(8);
        System.out.println("**********After enqueue**********");
        myQueue.showElements();
        myQueue.deQueue();
        myQueue.deQueue();
        System.out.println("**********After dequeue**********");
        myQueue.showElements();
        myQueue.enQueue(9);
        myQueue.enQueue(10);
        System.out.println("**********After enqueue**********");
        myQueue.showElements();
        myQueue.deQueue();
        System.out.println("**********After dequeue**********");
        myQueue.showElements();
        myQueue.enQueue(11);
        myQueue.enQueue(12);
        myQueue.enQueue(13);
        System.out.println("**********After enqueue**********");
        myQueue.showElements();
        System.out.println();


        System.out.println("**********String queue test**********");
        MyQueue strQueue = new MyQueue(2);
        strQueue.enQueue("A");
        strQueue.enQueue("B");
        strQueue.enQueue("C");
        System.out.println("**********After enqueue**********");
        strQueue.showElements();
        strQueue.deQueue();
        System.out.println("**********After dequeue**********");
        strQueue.showElements();
        strQueue.enQueue("D");
        System.out.println("**********After enqueue**********");
        strQueue.showElements();
        strQueue.deQueue();
        strQueue.deQueue();
        System.out.println("**********After dequeue**********");
        strQueue.showElements();
        strQueue.enQueue("E");
        strQueue.enQueue("F");
        strQueue.enQueue("G");
        System.out.println("**********After enqueue**********");
        strQueue.showElements();

    }
}
