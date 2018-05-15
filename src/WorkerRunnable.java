import java.util.function.Function;

public class WorkerRunnable implements Runnable {

    private Double result = 0.0;
    private BlockingQueue<Range<Double>> fifo;
    private Function<Double, Double> function;
    private Double step;

    public WorkerRunnable(BlockingQueue<Range<Double>> fifo, Function<Double, Double> function, Double step){
        this.function = function;
        this.step = step;
        this.fifo = fifo;
    }

    @Override
    public void run() {
        Range<Double> td = fifo.get();
        while(td != null) {
            while (td.from < td.to) {
                result += function.apply(td.from) * step;
                td.from += step;
            }
            td = fifo.get();
        }
    }

    public Double getResult(){
        return result;
    }
}
