import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Service {
    private List<ProducerRunnable> producers = new ArrayList<>();
    private List<WorkerRunnable> workers = new ArrayList<>();
    private BlockingQueue<Range<Double>> fifo = new BlockingQueue<>();
    private List<Thread> threadsw = new ArrayList<>();
    private List<Thread> threadsp = new ArrayList<>();
    private Boolean on = false;

    public Service(Integer nProducers, Integer nWorkers, Range<Double> range, Double packSize, Function<Double, Double> function, Double step){
        Double diff = (range.to - range.from)/nProducers;

        for(int i = 0; i < nProducers; i++){
            producers.add(new ProducerRunnable(
                    fifo, new Range<Double>(range.from, range.from + diff < range.to ? range.from + diff : range.to), packSize));
                    range.from += diff;

        }

        for(int i = 0; i < nWorkers; i++){
            workers.add(new WorkerRunnable(fifo, function, step));
        }
    }

    public void start(){
        if(!on){
            on = true;
            Thread t;
            for (ProducerRunnable p : producers){
                threadsp.add(t = new Thread(p));
                t.start();
            }

            for(WorkerRunnable w: workers){
                threadsw.add(t = new Thread(w));
                t.start();
            }
        }
    }

    public Double join() {
        boolean finished;
        while (!fifo.isProductionFinished()){
            finished = true;
            for (ProducerRunnable p : producers) {
                if(!p.isFinished()) {
                    finished = false;
                    break;
                }
            }
            if(finished)
                fifo.finishProduction();
        }

        for(Thread wt: threadsw){
            if (fifo.isEmpty() && wt.getState() == Thread.State.WAITING)
                wt.interrupt();
            else
                try {
                    wt.join();
                } catch (Exception e) { }
        }

        Double out = 0.0;

        for(WorkerRunnable w: workers)
            out += w.getResult();

        return out;
    }
}
