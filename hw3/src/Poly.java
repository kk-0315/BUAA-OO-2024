import java.math.BigInteger;
import java.util.ArrayList;

public class Poly {
    private ArrayList<Mono> monoArrayList;

    public Poly() {
        this.monoArrayList = new ArrayList<>();
    }

    public ArrayList<Mono> getMonoArrayList() {
        return this.monoArrayList;
    }

    public void set(ArrayList<Mono> monoArrayList) {
        this.monoArrayList.addAll(monoArrayList);
    }

    public Poly simPoly() {
        if (!this.monoArrayList.isEmpty()) {
            ArrayList<Mono> monos = this.getMonoArrayList();
            Poly result = new Poly();
            ArrayList<Mono> resultmono = new ArrayList<>();
            resultmono.add(monos.get(0));
            result.set(resultmono);
            for (int i = 1; i < monos.size(); i++) {
                Poly tmppoly = new Poly();
                ArrayList<Mono> tmpmono = new ArrayList<>();
                tmpmono.add(monos.get(i));
                tmppoly.set(tmpmono);
                result = result.addPoly(tmppoly);
            }
            ArrayList<Mono> monos1 = new ArrayList<>();
            Poly result1 = new Poly();
            for (Mono mono : result.getMonoArrayList()) {
                if (!mono.getCoe().equals(BigInteger.ZERO)) {
                    monos1.add(mono);
                }
            }
            result1.set(monos1);
            return result1;
        } else {
            return this;
        }
    }

    public Boolean equals(Poly poly) {
        if (this.getMonoArrayList().isEmpty() && poly.getMonoArrayList().isEmpty()) {
            return true;
        } else if (this.getMonoArrayList().isEmpty()) {
            return false;
        } else if (poly.getMonoArrayList().isEmpty()) {
            return false;
        } else {
            if (this.monoArrayList.size() != poly.getMonoArrayList().size()) {
                return false;
            } else {
                int flag = 0;
                for (int i = 0; i < this.monoArrayList.size(); i++) {
                    flag = 0;
                    Mono mono1 = this.monoArrayList.get(i);
                    for (int j = 0; j < poly.getMonoArrayList().size(); j++) {
                        Mono mono2 = poly.getMonoArrayList().get(j);
                        if (mono2.equals(mono1)) {
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 0) {
                        return false;
                    }
                }
                if (flag == 1) {
                    return true;
                }
                return false;
            }
        }
    }

    public Poly addPoly(Poly poly) {
        Poly result = new Poly();
        result.set(this.monoArrayList);
        for (Mono mono : poly.getMonoArrayList()) {
            BigInteger coe1 = mono.getCoe();
            BigInteger exp1 = mono.getExp();
            Poly inside1 = mono.getInside();
            int flag = 0;
            for (Mono mono1 : result.getMonoArrayList()) {
                BigInteger coe2 = mono1.getCoe();
                BigInteger exp2 = mono1.getExp();
                Poly inside2 = mono1.getInside();
                if (exp1.compareTo(exp2) == 0 && inside1.equals(inside2)) {
                    result.getMonoArrayList().set(result.getMonoArrayList().indexOf(mono1),
                            new Mono(coe1.add(coe2), exp1, inside1));
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                result.getMonoArrayList().add(mono);
            }
        }

        return result;
    }

    public Boolean isEmpty() {
        if (this.monoArrayList.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public Poly mulPoly(Poly poly) {
        Poly result = new Poly();

        for (Mono mono1 : poly.getMonoArrayList()) {
            BigInteger coe1 = mono1.getCoe();
            BigInteger exp1 = mono1.getExp();
            Poly inside1 = mono1.getInside();
            Poly tmpPoly = new Poly();
            ArrayList<Mono> tmpmonolist = new ArrayList<>();
            for (Mono mono2 : this.monoArrayList) {
                BigInteger coe2 = mono2.getCoe();
                BigInteger exp2 = mono2.getExp();
                Poly inside2 = mono2.getInside();
                tmpmonolist.add(new Mono(coe1.multiply(coe2),
                        exp1.add(exp2), inside1.addPoly(inside2)));

            }
            tmpPoly.set(tmpmonolist);
            result = result.addPoly(tmpPoly);
        }
        return result.simPoly();
    }

    public Poly powPoly(int exponent) {
        Poly result = new Poly();
        result.set(this.monoArrayList);
        Poly tmpPoly = new Poly();
        tmpPoly.set(this.monoArrayList);
        Poly inPoly = new Poly();
        if (exponent == 0) {
            this.monoArrayList.clear();
            this.monoArrayList.add(new Mono(BigInteger.ONE, BigInteger.ZERO, inPoly));
            return this.simPoly();
        } else {
            for (int i = 0; i < exponent - 1; i++) {
                result = result.mulPoly(tmpPoly);
            }
            return result;
        }

    }

    public Poly negate() {
        Poly inPoly = new Poly();
        Mono mono = new Mono(BigInteger.valueOf(-1), BigInteger.ZERO, inPoly);
        ArrayList<Mono> monos = new ArrayList<>();
        monos.add(mono);
        Poly poly = new Poly();
        poly.set(monos);
        return this.mulPoly(poly);
    }

    public String toString() { //暂时不考虑正的在前
        Mono mono = null;
        int flag = 0;
        for (int i = 0; i < this.monoArrayList.size(); i++) {
            if (this.monoArrayList.get(i).getCoe().compareTo(BigInteger.ZERO) > 0) {
                mono = monoArrayList.get(i);
                flag = i;
                break;
            }
        }
        if (flag == 0) {
            mono = monoArrayList.get(0);
        }
        StringBuilder sb = new StringBuilder();
        addtoSritng(sb, mono);
        for (int i = 0; i < this.monoArrayList.size(); i++) {
            if (i == flag) {
                continue;
            }
            sb.append("+");
            Mono mono1 = monoArrayList.get(i);
            addtoSritng(sb, mono1);

        }
        return sb.toString();


    }

    public void addtoSritng(StringBuilder sb, Mono mono) {
        if (mono.getCoe().equals(BigInteger.ONE)) {
            if1(mono, sb);
        } else if (mono.getCoe().equals(new BigInteger("-1"))) {
            if2(mono, sb);
        } else {
            if3(mono, sb);
        }
    }

    public void if1(Mono mono, StringBuilder sb) {
        if (mono.getExp().equals(BigInteger.ZERO)) {
            if (!mono.getInside().getMonoArrayList().isEmpty()) {
                addExp(sb, mono);
            } else {
                sb.append("1");
            }
        } else if (mono.getExp().equals(BigInteger.ONE)) {
            sb.append("x");
            if (!mono.getInside().getMonoArrayList().isEmpty()) {
                sb.append("*");
                addExp(sb, mono);
            }
        } else {
            sb.append("x");
            sb.append("^");
            sb.append(mono.getExp());
            if (!mono.getInside().getMonoArrayList().isEmpty()) {
                sb.append("*");
                addExp(sb, mono);

            }
        }
    }

    public void if2(Mono mono, StringBuilder sb) {
        if (mono.getExp().equals(BigInteger.ZERO)) {
            if (!mono.getInside().getMonoArrayList().isEmpty()) {
                BigInteger exponent = mono.getInside().getGcd();
                Poly in = mono.getInside().div(exponent);
                if (isExpr(in.toString())) {
                    sb.append("-exp((");
                    sb.append(in);
                    sb.append("))");
                    if (exponent.compareTo(BigInteger.ONE) != 0) {
                        sb.append("^");
                        sb.append(exponent);
                    }
                } else {
                    sb.append("-exp(");
                    sb.append(in);
                    sb.append(")");
                    if (exponent.compareTo(BigInteger.ONE) != 0) {
                        sb.append("^");
                        sb.append(exponent);
                    }
                }
            } else {
                sb.append("-1");
            }
        } else if (mono.getExp().equals(BigInteger.ONE)) {
            sb.append("-x");
            if (!mono.getInside().getMonoArrayList().isEmpty()) {
                sb.append("*");
                addExp(sb, mono);
            }
        } else {
            sb.append("-");
            sb.append("x");
            sb.append("^");
            sb.append(mono.getExp());
            if (!mono.getInside().getMonoArrayList().isEmpty()) {
                sb.append("*");
                addExp(sb, mono);
            }
        }
    }

    public void if3(Mono mono, StringBuilder sb) {
        if (mono.getExp().equals(BigInteger.ZERO)) {
            sb.append(mono.getCoe());
        } else if (mono.getExp().equals(BigInteger.ONE)) {
            sb.append(mono.getCoe());
            sb.append("*");
            sb.append("x");
        } else {
            sb.append(mono.getCoe());
            sb.append("*");
            sb.append("x");
            sb.append("^");
            sb.append(mono.getExp());
        }
        if (!mono.getInside().getMonoArrayList().isEmpty()) {
            sb.append("*");
            addExp(sb, mono);
        }
    }

    public BigInteger getGcd() {
        if (this.monoArrayList.size() == 1 &&
                this.monoArrayList.get(0).getExp().equals(BigInteger.ZERO) &&
                this.monoArrayList.get(0).getInside().getMonoArrayList().isEmpty()) {
            return BigInteger.ONE;
        } else {
            BigInteger result = this.monoArrayList.get(0).getCoe().abs();
            for (int i = 1; i < this.monoArrayList.size(); i++) {
                result = result.gcd(this.monoArrayList.get(i).getCoe().abs());
            }
            return result;
        }
    }

    public Poly div(BigInteger exponent) {
        ArrayList<Mono> monos = new ArrayList<>();
        for (int i = 0; i < this.monoArrayList.size(); i++) {
            Mono pastmono = this.monoArrayList.get(i);
            Mono mono = new Mono(pastmono.getCoe().divide(exponent),
                    pastmono.getExp(), pastmono.getInside());
            monos.add(mono);
        }
        Poly poly = new Poly();
        poly.set(monos);
        return poly;
    }

    private void addExp(StringBuilder sb, Mono mono) {
        BigInteger exponent = mono.getInside().getGcd();
        Poly in = mono.getInside().div(exponent);
        if (isExpr(in.toString())) {
            sb.append("exp((");
            sb.append(in);
            sb.append("))");
            if (exponent.compareTo(BigInteger.ONE) != 0) {
                sb.append("^");
                sb.append(exponent);
            }
        } else {
            sb.append("exp(");
            sb.append(in);
            sb.append(")");
            if (exponent.compareTo(BigInteger.ONE) != 0) {
                sb.append("^");
                sb.append(exponent);
            }
        }
    }

    public Boolean isExpr(String str) {
        boolean flag = false;
        int count = 0;
        if (str.charAt(0) == '-') {
            return true;
        } else {
            for (int i = 0; i < str.length(); i++) {
                if (count == 0 && ("+-*".indexOf(str.charAt(i)) != -1) && i > 0) {
                    flag = true;
                    return flag;
                } else if (str.charAt(i) == '(') {
                    count--;
                } else if (str.charAt(i) == ')') {
                    count++;
                }
            }
            return flag;
        }
    }

}