
public class ExprFactor implements Factor {
    private Factor expr;
    private Integer exp;

    public ExprFactor(Expr expr, Integer exp) {
        this.expr = expr;
        this.exp = exp;
    }

    @Override
    public Poly toPoly() {
        return this.expr.toPoly().powPoly(this.exp);
    }
}
