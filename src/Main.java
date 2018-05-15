import java.text.DecimalFormat;

public class Main {

    public static void main(String[] args) {
        Service service = new Service(1, 8, new Range<Double>(0.0, 3*Math.PI), Math.PI, Math::sin, 1e-7);
        service.start();
        System.out.println(new DecimalFormat("#0.0000000000000").format(service.join()));
    }
}

