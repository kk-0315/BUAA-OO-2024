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

    @Override
    public Expr derivative() {
        Term term = new Term(1);
        if (this.exp.compareTo(BigInteger.ONE) > 0) {
            term.addFactor(new Num(this.exp));
            term.addFactor(new Var(this.exp.subtract(BigInteger.ONE)));
        } else if (this.exp.compareTo(BigInteger.ONE) == 0) {
            term.addFactor(new Num(BigInteger.ONE));
        } else {
            term.addFactor(new Num(BigInteger.ZERO));
        }
        ArrayList<Term> terms = new ArrayList<>();
        terms.add(term);
        return new Expr(terms);
    }

}