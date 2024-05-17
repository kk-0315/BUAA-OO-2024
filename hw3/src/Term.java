import java.math.BigInteger;
import java.util.ArrayList;

public class Term implements Factor {
    private final ArrayList<Factor> factors;//原有的factor
    private int sign;

    public Term(int sign) {
        this.factors = new ArrayList<>();
        this.sign = sign;
    }

    public Term(int sign, ArrayList<Factor> factors) {
        this.factors = factors;
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

    public ArrayList<Factor> getFactors() {
        return this.factors;
    }

    @Override
    public Expr derivative() {

        if (this.factors.size() == 1) {
            Term term = new Term(sign);
            term.addFactor(this.factors.get(0).derivative());
            Expr expr = new Expr();
            expr.addTerm(term);
            return expr;
        } else {
            Term term1 = new Term(sign);
            Term term2 = new Term(sign);
            term1.addFactor(this.factors.get(0).derivative());

            for (int i = 1; i < this.factors.size(); i++) {
                term1.addFactor(this.factors.get(i));
            }

            term2.addFactor(this.factors.get(0));
            Term tmpterm = new Term(1);
            for (int i = 1; i < this.factors.size(); i++) {
                tmpterm.addFactor(this.factors.get(i));
            }
            term2.addFactor(tmpterm.derivative());
            ArrayList<Term> terms = new ArrayList<>();
            terms.add(term1);
            terms.add(term2);
            return new Expr(terms);
        }

    }

}
