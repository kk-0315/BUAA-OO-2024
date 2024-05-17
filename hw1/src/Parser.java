
import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
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

        //处理项的符号
        BigInteger num = new BigInteger("-1");
        BigInteger num1 = new BigInteger("1");
        Factor number1 = new Num(num1);
        Factor number = new Num(num);
        Term term = new Term();
        //处理项的第一个因子，包括前置符号
        processTerm(term, number, number1);
        //循环处理项的所有因子
        while (lexer.peek().equals("*")) {
            lexer.next();
            processTerm(term, number, number1);
        }
        //返回处理好的项
        return term;
    }

    public Factor parseFactor() {
        //表达式因子
        if (lexer.peek().equals("(")) {
            lexer.next();
            Factor expr = parseExpr();
            lexer.next();
            return expr;
        } else if (Character.isLetter(lexer.peek().charAt(0))) { //变量因子
            Var var = new Var(lexer.peek());
            lexer.next();
            return var;
        } else { //常数因子,example:(-01-x)^2
            String string = lexer.peek();
            BigInteger num;
            BigInteger total = BigInteger.valueOf(0);
            for (int i = 0; i < string.length(); i++) {
                char ch = lexer.peek().charAt(i);
                num = BigInteger.valueOf(ch - '0');
                total = total.multiply(BigInteger.valueOf(10)).add(num);
            }
            lexer.next();
            return new Num(total);
        }
    }

    public void processTerm(Term term, Factor number, Factor number1) {
        Factor tmp;
        if (lexer.peek().equals("-")) {
            term.addFactor(number);
            lexer.next();

        } else if (lexer.peek().equals("+")) {
            term.addFactor(number1);
            lexer.next();
        }
        //解析因子
        tmp = parseFactor();
        //处理幂
        if (lexer.peek().equals("^")) {
            lexer.next();
            //这里已经去除了^后紧跟的+号,且最大不超过8
            if (Integer.parseInt(lexer.peek()) != 0) {
                for (int i = 0; i < Integer.parseInt(lexer.peek()); i++) {
                    term.addFactor(tmp);
                }

            } else {
                term.addFactor(number1);
            }
            lexer.next();
        } else {
            term.addFactor(tmp);
        }
    }
}
