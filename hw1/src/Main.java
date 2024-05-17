import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String ori = scanner.nextLine();
        //String Simplify

        Lexer lexer = new Lexer(ori);
        Parser parser = new Parser(lexer);

        Expr expr1 = parser.parseExpr();
        Simplify simplify = new Simplify(expr1.toString());
        String ans = simplify.simplifyExpr(expr1.toString());

        ans = ans.replaceAll("\\+-", "-");
        ans = ans.replaceAll("-\\+", "-");

        System.out.println(ans);
    }
}