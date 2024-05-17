import java.math.BigInteger;
import java.util.ArrayList;

public class ExprFactor implements Factor {
    private Expr expr;
    private Integer exp;

    public ExprFactor(Expr expr, Integer exp) {
        this.expr = expr;
        this.exp = exp;
    }

    @Override
    public Poly toPoly() {
        return this.expr.toPoly().powPoly(this.exp);
    }

    @Override
    public Expr derivative() {
        Term term = new Term(1);
        if (this.exp > 1) {
            term.addFactor(new Num(BigInteger.valueOf(this.exp)));
            term.addFactor(new ExprFactor(this.expr, this.exp - 1));
            term.addFactor(this.expr.derivative());
        } else if (this.exp == 1) {
            term.addFactor(this.expr.derivative());
        } else {
            term.addFactor(new Num(BigInteger.ZERO));
        }
        ArrayList<Term> terms = new ArrayList<>();
        terms.add(term);
        return new Expr(terms);
    }

}
