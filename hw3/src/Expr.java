import java.util.ArrayList;

public class Expr implements Factor {
    private final ArrayList<Term> terms;

    public Expr() {
        this.terms = new ArrayList<>();
    }

    public Expr(ArrayList<Term> terms) {
        this.terms = terms;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public ArrayList<Term> getTerms() {
        return this.terms;
    }

    @Override
    public Poly toPoly() {
        Poly poly = new Poly();
        for (Term term : terms) {
            int sign = term.getSign();
            if (sign == -1) {
                poly = poly.addPoly(term.toPoly().negate());
            } else {
                poly = poly.addPoly(term.toPoly());
            }
        }
        return poly.simPoly();
    }

    @Override
    public Expr derivative() {
        ArrayList<Term> resultTerms = new ArrayList<>();
        for (Term term : terms) {
            if (!term.getFactors().isEmpty()) {
                Expr tmpexpr = term.derivative();
                resultTerms.addAll(tmpexpr.getTerms());
            }
        }
        return new Expr(resultTerms);
    }

}
