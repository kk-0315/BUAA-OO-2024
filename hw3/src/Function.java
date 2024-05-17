import java.util.ArrayList;
import java.util.Objects;

public class Function {
    private String define;
    private ArrayList<String> para = new ArrayList<>();

    public Function(String define) {
        String dispose;
        Lexer lexer = new Lexer(define);
        dispose = lexer.process(define);
        String name = dispose.substring(0, dispose.indexOf('='));//参数
        String str = dispose.substring(dispose.indexOf('=') + 1);//定义式
        for (int i = 0; i < name.length(); i++) {
            if ("xyz".indexOf(name.charAt(i)) != -1) {
                para.add(String.valueOf(name.charAt(i)));//参数加入列表
            }
        }
        str = str.replaceAll("exp", "e%p");
        str = str.replaceAll("x", "!");//占位符,很好的思路
        str = str.replaceAll("y", "@");
        str = str.replaceAll("z", "#");
        str = str.replaceAll("%", "x");
        this.define = str;
    }

    public ExprFactor toExprFactor(Factor a, Factor b, Factor c) { //三个解析好的因子
        Factor x = null;
        Factor y = null;
        Factor z = null;
        for (int i = 0; i < this.para.size(); i++) { //选择正确的顺序
            if (Objects.equals(para.get(i), "x")) {
                x = chose(i, a, b, c);
            } else if (Objects.equals(para.get(i), "y")) {
                y = chose(i, a, b, c);
            } else {
                z = chose(i, a, b, c);
            }
        }
        Lexer lexer = new Lexer(this.define);
        Parser parser = new Parser(lexer, x, y, z);
        return new ExprFactor(parser.parseExpr(), 1);

    }

    public Factor chose(int i, Factor a, Factor b, Factor c) {
        if (i == 0) {
            return a;
        } else if (i == 1) {
            return b;
        } else {
            return c;
        }
    }
}
