public class ProducerRunnable implements Runnable {
    private Range<Double> range;
    private Double packSize;
    private BlockingQueue<Range<Double>> fifo;
    private boolean finished = false;

    public ProducerRunnable(BlockingQueue<Range<Double>> fifo, Range<Double> range, Double packSize) {
        this.range = range;
        this.packSize = packSize;
        this.fifo = fifo;
    }

    @Override
    public void run() {
        while(range.from < range.to){
            fifo.put(range.from + packSize < range.to ?
                    new Range<Double>(range.from, range.from + packSize) : new Range<Double>(range.from, range.to));
            range.from += packSize;
        }

        finished = true;
    }

    public boolean isFinished() {
        return finished;
    }
}
