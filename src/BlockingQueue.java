import java.util.LinkedList;

public class BlockingQueue<T> {
    private LinkedList<T> fifo = new LinkedList<>();
    private Integer size = 10;
    private boolean productionFinished = false;

    public synchronized T get(){
        if(fifo.isEmpty() && productionFinished) {
            notify();
            return null;
        }

        while(fifo.isEmpty() && !productionFinished)
            try{wait();} catch (InterruptedException e) {
            return null;
        };

        T first = null;
        if(!fifo.isEmpty())
            first = fifo.removeFirst();

        notify();
        return first;
    }

    public synchronized void put(T o){
        while(fifo.size() >= size)
            try{wait();} catch (InterruptedException e) {return;};
        fifo.addLast(o);
        notify();
    }

    public synchronized void setSize(Integer size){
        this.size = size;
    }

    public boolean isProductionFinished(){
        return productionFinished;
    }

    public void finishProduction(){
        productionFinished = true;
    }

    public boolean isEmpty(){
        return fifo.size() == 0;
    }
}