import java.math.BigInteger;
import java.util.Objects;

public class Mono {
    private BigInteger coe;//系数
    private BigInteger exp;//指数
    private Poly inside;

    public Mono(BigInteger coe, BigInteger exp, Poly inside) {
        this.coe = coe;
        this.exp = exp;
        this.inside = inside;
    }

    public BigInteger getCoe() {
        return this.coe;
    }

    public BigInteger getExp() {
        return this.exp;
    }

    public Poly getInside() {
        return this.inside;
    }

    public Boolean equals(Mono mono) {
        if (Objects.equals(this.exp, mono.getExp()) && Objects.equals(this.coe, mono.getCoe())
                && this.inside.equals(mono.getInside())) {
            return true;
        }
        return false;
    }
}
