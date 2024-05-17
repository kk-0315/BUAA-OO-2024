import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Simplify {
    private final String input;

    public Simplify(String input) {
        this.input = input;
    }

    public String simplifyExpr(String input) {
        ArrayList<String> terms = new ArrayList<>();
        String[] s = input.split("\\+");
        HashMap<Integer, BigInteger> map = new HashMap<>();//key：指数，value：系数
        Integer xnum = 0;
        BigInteger sum = BigInteger.valueOf(1);

        for (String string : s) {
            xnum = 0;
            sum = BigInteger.valueOf(1);
            for (int i = 0; i < string.length(); i++) {
                char c = string.charAt(i);
                if (string.charAt(i) == '-') {
                    sum = sum.multiply(BigInteger.valueOf(-1));
                }
                if (c == 'x') {
                    xnum++;
                } else if (Character.isDigit(c)) { //目前仅限一个数字
                    BigInteger num;
                    BigInteger total = BigInteger.valueOf(0);
                    int k = 0;
                    while (Character.isDigit(string.charAt(i + k))) {
                        char ch = string.charAt(i + k);
                        num = BigInteger.valueOf(ch - '0');
                        total = total.multiply(BigInteger.valueOf(10)).add(num);
                        k++;
                        if ((i + k) >= string.length()) {
                            break;
                        }
                    }
                    sum = sum.multiply(total);
                    i += k - 1;
                }
            }
            int flag = 0;
            for (int key : map.keySet()) {
                if (xnum == key) {
                    BigInteger num = map.get(key).add(sum);
                    map.replace(key, num);
                    flag = 1;
                }
            }
            if (flag == 0) {
                map.put(xnum, sum);
            }
        }
        return oper(map);
    }

    public void Print(StringBuilder sb, int key) {
        sb.append("x");
        sb.append("^");
        sb.append(key);
    }

    public void set(StringBuilder sb, HashMap<Integer, BigInteger> map) {
        for (Integer key : map.keySet()) {
            if (map.get(key).compareTo(BigInteger.ZERO) > 0) {
                if (!map.get(key).equals(BigInteger.ONE)) {
                    sb.append(map.get(key));

                    if (key != 0 && key != 1) {
                        sb.append("*");
                        sb.append("x");
                        sb.append("^");
                        sb.append(key);
                    } else if (key == 1) {
                        sb.append("*");
                        sb.append("x");
                    }
                } else {
                    if (key != 0 && key != 1) {
                        sb.append("x");
                        sb.append("^");
                        sb.append(key);
                    } else if (key == 1) {
                        sb.append("x");
                    } else {
                        sb.append(map.get(key));
                    }
                }
                sb.append("+");
                map.replace(key, BigInteger.ZERO);
                break;
            }
        }
    }

    public String oper(HashMap<Integer, BigInteger> map) {
        StringBuilder sb = new StringBuilder();
        set(sb, map);
        for (int key : map.keySet()) {
            if (Objects.equals(map.get(key), BigInteger.valueOf(1))) {
                if (Objects.equals(key, 0)) {
                    sb.append(map.get(key));
                } else if (Objects.equals(key, 1)) {
                    sb.append("x");
                } else {
                    Print(sb, key);
                }
                sb.append("+");
            } else if (Objects.equals(map.get(key), BigInteger.valueOf(-1))) {
                if (Objects.equals(key, 0)) {
                    sb.append(map.get(key));
                } else if (Objects.equals(key, 1)) {
                    sb.append("-");
                    sb.append("x");
                } else {
                    sb.append("-");
                    Print(sb, key);
                }
                sb.append("+");
            } else if (!Objects.equals(map.get(key), BigInteger.valueOf(0))) {
                if (Objects.equals(key, 0)) {
                    sb.append(map.get(key));
                } else if (Objects.equals(key, 1)) {
                    sb.append(map.get(key));
                    sb.append("*");
                    sb.append("x");
                } else {
                    sb.append(map.get(key));
                    sb.append("*");
                    Print(sb, key);
                }
                sb.append("+");
            }

        }
        if (sb.toString().isEmpty()) {
            return "0";
        } else {
            return sb.substring(0, sb.length() - 1);
        }
    }
}
