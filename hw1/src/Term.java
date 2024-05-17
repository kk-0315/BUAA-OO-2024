import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Term implements Factor {
    private final ArrayList<Factor> factors;//原有的factor -1

    public Term() {
        this.factors = new ArrayList<>();
    }

    public void addFactor(Factor factor) { //新的factor x
        ArrayList<Term> tmpterms = new ArrayList<>();
        ArrayList<Factor> tmpfactors = new ArrayList<>();
        Var add = new Var("+");
        //分四种情况 有+单  无+单  有+多 无+多 这里+号也被加入用做分割
        dealwith(factor, tmpfactors, tmpterms, add);
        //替换
        ArrayList<Factor> newfactors = new ArrayList<>();
        HashMap<Integer, BigInteger> map = new HashMap<>();
        int coe = 0;
        BigInteger num = BigInteger.ONE;
        int flag3 = 0;
        for (Factor factor1 : tmpfactors) {
            if (Objects.equals(factor1.toString(), "0")) {
                num = BigInteger.ZERO;
                flag3 = 1;
            } else if (Objects.equals(factor1.toString(), "+")) {
                if (flag3 != 1) {
                    if (map.containsKey(coe)) {
                        BigInteger past = map.get(coe);
                        map.replace(coe, past.add(num));
                    } else {
                        map.put(coe, num);
                    }
                }
                num = BigInteger.ONE;
                coe = 0;
                flag3 = 0;
            } else if (flag3 != 1) {
                if (factor1.toString().contains("*")) {
                    String[] strings = factor1.toString().split("\\*");
                    for (String s : strings) {
                        if (s.equals("x")) {
                            coe++;
                        } else if (s.equals("0")) {
                            num = BigInteger.ZERO;
                            flag3 = 1;
                        } else {
                            num = num.multiply(new BigInteger(s));
                        }
                    }
                } else {
                    if (factor1.toString().equals("x")) {
                        coe++;
                    } else if (factor1.toString().equals("0")) {
                        num = BigInteger.ZERO;
                        flag3 = 1;
                    } else {
                        num = num.multiply(new BigInteger(factor1.toString()));
                    }
                }

            }
        }
        merge(map, coe, num, flag3, newfactors, add);
        factors.clear();
        factors.addAll(newfactors);

    }

    public void merge(HashMap<Integer, BigInteger> map, int coe, BigInteger num,
                      int flag3, ArrayList<Factor> newfactors, Var add) {
        Factor factor0 = new Num(BigInteger.ZERO);
        if (flag3 != 1) {
            if (map.containsKey(coe)) {
                BigInteger past = map.get(coe);
                map.replace(coe, past.add(num));
            } else {
                map.put(coe, num);
            }
        }
        for (Integer key1 : map.keySet()) {
            newfactors.add(new Num(map.get(key1)));
            for (int i = 0; i < key1; i++) {
                newfactors.add(new Var("x"));
            }
            newfactors.add(add);
        }
        if (newfactors.isEmpty()) {
            newfactors.add(factor0);
        } else {
            newfactors.remove(newfactors.size() - 1);
        }
    }

    public void dealwith(Factor factor, ArrayList<Factor> tmpfactors,
                         ArrayList<Term> tmpterms, Var add) {
        if (factor.toString().contains("+")) {
            String[] group = factor.toString().split("\\+");
            for (String s : group) {
                Lexer lexer = new Lexer(s);
                Parser parser = new Parser(lexer);
                tmpterms.add(parser.parseTerm());
            }
            //原先不为空
            if (!factors.isEmpty()) {
                for (int i = 0; i < tmpterms.size(); i++) {
                    int flag1 = 0;
                    for (Factor factor1 : factors) {

                        if (!factor1.toString().equals("+")) {
                            if (flag1 == 0) {
                                tmpfactors.add(tmpterms.get(i));
                                flag1 = 1;//一项只能加一个新的因子否则可能会重复加!!!
                            }
                            tmpfactors.add(factor1);
                        } else {
                            tmpfactors.add(add);
                            flag1 = 0;
                        }
                    }
                    if (i < tmpterms.size() - 1) {
                        tmpfactors.add(add);
                    }
                }
            } else { //若原先为空，直接加新的因子和加号
                for (int i = 0; i < tmpterms.size(); i++) {
                    tmpfactors.add(tmpterms.get(i));
                    if (i < tmpterms.size() - 1) {
                        tmpfactors.add(add);
                    }
                }
            }
        } else {
            if (!factors.isEmpty()) {
                int flag = 0;
                for (Factor factor1 : factors) {
                    if (!factor1.toString().equals("+")) {

                        if (flag == 0) {
                            tmpfactors.add(factor);
                            flag = 1;
                        }
                        tmpfactors.add(factor1);
                    } else {
                        tmpfactors.add(add);
                        flag = 0;
                    }
                }
            } else {
                tmpfactors.add(factor);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!factors.isEmpty()) {
            int flag2 = 0;
            for (int i = 0; i < factors.size(); i++) {
                if (factors.get(i).toString().equals("+")) {
                    flag2 = 1;
                    sb.append("+");
                } else {
                    if (i != 0 && flag2 == 0) {
                        sb.append("*");
                    }
                    sb.append(factors.get(i).toString());
                    flag2 = 0;
                }
            }
        }
        return sb.toString();
    }

}
