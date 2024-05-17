import java.math.BigInteger;

public class Num implements Factor {
    private final BigInteger value;

    public Num(BigInteger num) {
        this.value = num;

    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
