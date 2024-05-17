public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = this.process(input);
        this.next();
    }

    //首先预处理
    public String process(String ori) {
        String expr = ori.replaceAll("[ \t]", "");
        /*
        StringBuilder sb = new StringBuilder();
        Boolean flag = false;

        if ("+-".indexOf(expr.charAt(0)) != -1) {
            sb.append("0");

        }
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) == '(') {
                sb.append(expr.charAt(i));
                if ("+-".indexOf(expr.charAt(i + 1)) != -1) {
                    sb.append("0");
                    sb.append(expr.charAt(i + 1));
                }
            } else {
                sb.append(expr.charAt(i));
            }
        }
        expr = sb.toString();*/
        for (int i = 0; i < 4; i++) { //+++->++ 若只有一次
            expr = expr.replaceAll("\\+\\+", "+");
            expr = expr.replaceAll("--", "+");
            expr = expr.replaceAll("\\+-", "-");
            expr = expr.replaceAll("-\\+", "-");
            expr = expr.replaceAll("\\^\\+", "^");
            expr = expr.replaceAll("\\*\\+", "*");
        }
        return expr;
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }

        return sb.toString();
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }

        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = getNumber();
        } else if ("()+-*^".indexOf(c) != -1) {
            pos += 1;
            curToken = String.valueOf(c);
        } else if (Character.isLetter(c)) {
            pos += 1;
            curToken = String.valueOf(c);
        }
    }

    public String peek() {
        return this.curToken;
    }
}
