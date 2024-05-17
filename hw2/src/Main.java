import java.util.Scanner;

public class Main {
    private static Function functionF;
    private static Function functionG;
    private static Function functionH;

    public static Function getFunction(String name) {
        if (name.equals("f")) {
            return functionF;
        } else if (name.equals("g")) {
            return functionG;
        } else {
            return functionH;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String count = scanner.nextLine();

        for (int i = 0; i < Integer.parseInt(count); i++) {
            String define = scanner.nextLine();
            if (define.charAt(0) == 'f') {
                functionF = new Function(define);
            } else if (define.charAt(0) == 'g') {
                functionG = new Function(define);
            } else {
                functionH = new Function(define);
            }

        }
        String ori = scanner.nextLine();
        //String Simplify

        Lexer lexer = new Lexer(ori);
        Parser parser = new Parser(lexer);

        Expr expr1 = parser.parseExpr();
        String ans;
        if (expr1.toPoly().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("0");
            ans = sb.toString();
        } else {
            ans = expr1.toPoly().toString();
        }

        ans = ans.replaceAll("\\+-", "-");
        ans = ans.replaceAll("-\\+", "-");

        System.out.println(ans);
    }
}