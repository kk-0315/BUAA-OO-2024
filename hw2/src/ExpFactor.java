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

}
