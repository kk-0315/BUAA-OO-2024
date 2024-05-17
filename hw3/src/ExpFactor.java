import java.math.BigInteger;
import java.util.ArrayList;

public class ExpFactor implements Factor {
    private Factor factor;
    private BigInteger exponent;

    public ExpFactor(Factor factor, BigInteger exponent) {
        this.factor = factor;
        this.exponent = exponent;
    }

    public Poly toPoly() {
        Num num = new Num(exponent);

        Mono mono = new Mono(BigInteger.ONE, BigInteger.ZERO,
                this.factor.toPoly().mulPoly(num.toPoly()));
        ArrayList<Mono> monos = new ArrayList<>();
        Poly poly = new Poly();
        monos.add(mono);
        poly.set(monos);
        return poly;
    }

    @Override
    public Expr derivative() {
        Term term = new Term(1);
        if (this.exponent.compareTo(BigInteger.ONE) > 0) {
            term.addFactor(new Num(this.exponent));
            term.addFactor(new ExpFactor(this.factor, this.exponent));
            term.addFactor(this.factor.derivative());
        } else if (this.exponent.compareTo(BigInteger.ONE) == 0) {
            term.addFactor(this);
            term.addFactor(this.factor.derivative());
        } else {
            term.addFactor(new Num(BigInteger.ZERO));
        }
        ArrayList<Term> terms = new ArrayList<>();
        terms.add(term);
        return new Expr(terms);
    }

}
