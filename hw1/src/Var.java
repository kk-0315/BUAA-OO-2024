public class Var implements Factor {
    private final String var;

    public Var(String var) {
        this.var = var;
    }

    @Override
    public String toString() {
        return var;
    }
}