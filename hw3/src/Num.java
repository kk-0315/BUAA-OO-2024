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

    @Override
    public Expr derivative() {
        Term term = new Term(1);
        term.addFactor(new Num(BigInteger.ZERO));
        ArrayList<Term> terms = new ArrayList<>();
        terms.add(term);
        return new Expr(terms);
    }

}
