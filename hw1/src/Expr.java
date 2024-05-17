import java.util.ArrayList;

public class Expr implements Factor {
    private final ArrayList<Term> terms;

    public Expr() {
        this.terms = new ArrayList<>();
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!terms.isEmpty()) {
            for (int i = 0; i < terms.size(); i++) {
                if (i != 0) {
                    sb.append("+");
                }
                sb.append(terms.get(i).toString());
            }
        }
        return sb.toString();
    }
}
