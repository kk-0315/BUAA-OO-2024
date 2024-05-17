import java.math.BigInteger;
import java.util.ArrayList;

public class Term implements Factor {
    private final ArrayList<Factor> factors;//原有的factor
    private int sign;

    public Term(int sign) {
        this.factors = new ArrayList<>();
        this.sign = sign;
    }

    public void addFactor(Factor factor) { //新的factor x
        this.factors.add(factor);
    }

    public int getSign() {
        return this.sign;
    }

    public void setSign() {
        this.sign = this.sign * (-1);
    }

    @Override
    public Poly toPoly() {
        Poly inPoly = new Poly();
        Mono mono = new Mono(BigInteger.ONE, BigInteger.ZERO, inPoly);
        ArrayList<Mono> monoArrayList = new ArrayList<>();
        monoArrayList.add(mono);
        Poly poly = new Poly();
        poly.set(monoArrayList);
        for (Factor factor : this.factors) {
            poly = poly.mulPoly(factor.toPoly());
        }
        return poly.simPoly();
    }

}
