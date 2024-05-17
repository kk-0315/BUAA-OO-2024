
import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;
    private Factor xfactor;
    private Factor yfactor;
    private Factor zfactor;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Parser(Lexer lexer, Factor x, Factor y, Factor z) {
        this.lexer = lexer;
        this.xfactor = x;
        this.yfactor = y;
        this.zfactor = z;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        //处理第一个项
        expr.addTerm(parseTerm());
        //循环处理所有项
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            expr.addTerm(parseTerm());
            /*if(Objects.equals(lexer.peek(), ")")){
                lexer.next();
            }*/
        }
        return expr;
    }

    public Term parseTerm() {
        Term term;
        //处理项的符号
        if (lexer.peek().equals("-")) {
            term = new Term(-1);
            lexer.next();

        } else if (lexer.peek().equals("+")) {
            term = new Term(1);
            lexer.next();
        } else {
            term = new Term(1);
        }
        //处理项的第一个因子，包括前置符号
        processFactor(term);
        //循环处理项的所有因子
        while (lexer.peek().equals("*")) {
            lexer.next();
            if (lexer.peek().equals("-")) {
                term.setSign();
                lexer.next();
            } else if (lexer.peek().equals("+")) {
                lexer.next();
            }
            processFactor(term);
        }
        //返回处理好的项
        return term;
    }

    public void processFactor(Term term) {
        Factor tmp;
        //解析因子
        tmp = parseFactor();
        if (lexer.peek().equals("^")) {
            lexer.next();
            if (!lexer.peek().equals("0")) {
                for (int i = 0; i < Integer.parseInt(lexer.peek()); i++) {
                    term.addFactor(tmp);
                }
            } else {
                term.addFactor(new Num(BigInteger.ONE));
            }
            lexer.next();
        } else {
            term.addFactor(tmp);
        }
    }

    public Factor parseFactor() {
        //表达式因子
        if (lexer.peek().equals("(")) {
            return parseExprFactor();
        } else if (Character.isDigit(lexer.peek().charAt(0)) ||
                lexer.peek().equals("+") || lexer.peek().equals("-")) { //常数因子待改动
            int sign = 1;
            if (lexer.peek().equals("-")) {
                sign = -1;
                lexer.next();
            } else if (lexer.peek().equals("+")) {
                lexer.next();
            }
            return parseNumFactor(sign);
        } else if (lexer.peek().equals("!")) {
            lexer.next();
            return xfactor;
        } else if (lexer.peek().equals("@")) {
            lexer.next();
            return yfactor;
        } else if (lexer.peek().equals("#")) {
            lexer.next();
            return zfactor;
        } else if ("fgh".indexOf(lexer.peek().charAt(0)) != -1) {
            return parseFuntion();
        } else if (lexer.peek().equals("exp")) {
            return parseExpFactor();
        } else { //幂函数
            return parseVarFactor();
        }
    }

    public Factor parseExprFactor() {
        lexer.next();
        Expr expr = parseExpr();
        lexer.next();
        if (lexer.peek().equals("^")) {
            lexer.next();
            int exp = Integer.parseInt(lexer.peek());
            lexer.next();
            return new ExprFactor(expr, exp);
        }
        return new ExprFactor(expr, Integer.parseInt("1"));
    }

    public Factor parseNumFactor(int sign) {
        String string = lexer.peek();
        lexer.next();
        BigInteger num = new BigInteger(string);
        num = num.multiply(BigInteger.valueOf(sign));
        return new Num(num);
    }

    public Factor parseFuntion() {

        Factor a;
        Factor b;
        Factor c;
        a = null;
        b = null;
        c = null;
        String name;
        name = lexer.peek();
        lexer.next();
        lexer.next();
        a = parseExpr();
        if (lexer.peek().equals(",")) {
            lexer.next();
            b = parseExpr();
        }
        if (lexer.peek().equals(",")) {
            lexer.next();
            c = parseExpr();
        }
        lexer.next();
        return Main.getFunction(name).toExprFactor(a, b, c);
    }

    public Factor parseExpFactor() {
        lexer.next();
        Factor factor = parseExpr();

        lexer.next();
        if (lexer.peek().equals("^")) {
            lexer.next();
            BigInteger exp = new BigInteger(lexer.peek());
            lexer.next();
            return new ExpFactor(factor, exp);
        }
        return new ExpFactor(factor, BigInteger.ONE);
    }

    public Factor parseVarFactor() {
        lexer.next();
        if (lexer.peek().equals("^")) {
            lexer.next();
            BigInteger exp = new BigInteger(lexer.peek());
            lexer.next();
            return new Var(exp);
        } else {
            return new Var(BigInteger.ONE);
        }
    }
}
