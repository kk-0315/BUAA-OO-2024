import java.math.BigInteger;
import java.util.ArrayList;

public class Var implements Factor {
    private final BigInteger exp;

    public Var(BigInteger exp) {
        this.exp = exp;
    }

    @Override
    public Poly toPoly() {
        Poly inPoly = new Poly();
        Poly poly = new Poly();
        Mono mono = new Mono(BigInteger.ONE, this.exp, inPoly);
        ArrayList<Mono> monoArrayList = new ArrayList<>();
        monoArrayList.add(mono);
        poly.set(monoArrayList);
        return poly;
    }

}