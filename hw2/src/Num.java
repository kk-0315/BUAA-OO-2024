import java.math.BigInteger;
import java.util.ArrayList;

public class Num implements Factor {
    private final BigInteger value;

    public Num(BigInteger num) {
        this.value = num;

    }

    public Poly toPoly() {
        Poly inPoly = new Poly();
        Mono mono = new Mono(this.value, BigInteger.ZERO, inPoly);
        ArrayList<Mono> monoArrayList = new ArrayList<>();
        monoArrayList.add(mono);
        Poly poly = new Poly();
        poly.set(monoArrayList);
        return poly;
    }

}
