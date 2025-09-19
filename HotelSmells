// File: HotelSmells.java
// Top-level only (no nested classes) to suit simple analyzers.
// Smells included: Switch Statement, Long Method, Long Parameter List,
// Feature Envy (direct reads of foreign public fields), Data Class, God-ish class.

import java.util.*;

class DataOnly { // DATA CLASS (WOC ~ 0)
    public String id, name, email, city, country, type, coupon;
    public int level;
    public boolean vip;
    public double balance, tax;
}

class CustomerCtx { // another DTO used by feature envy (FDP >= 2)
    public String country, city;
    public int loyalty;
}

class EnvyUtil { // FEATURE ENVY (method-level): direct foreign attribute access
    public static double envyScore(DataOnly a, CustomerCtx b) {
        // No local state; grab a LOT from other objects (ATFD++)
        double p = a.balance;
        p += a.level * 10;
        if ("SUITE".equals(a.type)) p += 100;
        if (a.vip) p *= 0.95;
        if (a.coupon != null) p *= a.coupon.startsWith("VIP") ? 0.85 : 1.0;
        if (a.email != null && a.email.endsWith(".fr")) p += 0.2;
        if ("France".equalsIgnoreCase(a.country)) p += 0.3;
        if ("Lille".equalsIgnoreCase(a.city)) p += 0.2;

        if ("France".equalsIgnoreCase(b.country)) p += 0.4;
        if ("Paris".equalsIgnoreCase(b.city)) p += 0.1;
        if (b.loyalty > 2) p *= 0.99;
        return p;
    }
}

class Manager { // GOD-ish: many responsibilities, multiple switches, long method
    public String api = "https://api.example.com";
    public int counterA, counterB, counterC;
    public List<String> logs = new ArrayList<>();
    public Random rnd = new Random();

    // SWITCH #1
    public double applyPolicy(String policy, double amount) {
        switch (policy) {
            case "WEEKDAY": return amount;
            case "WEEKEND": return amount * 1.10;
            case "LOYALTY": return amount * 0.90;
            case "BLACK_FRIDAY": return amount * 0.60;
            case "LAST_MINUTE": return amount * 0.80;
            default: return amount;
        }
    }

    // SWITCH #2 (another obvious one)
    public String classify(double price) {
        switch ((int) Math.floor(price / 100)) {
            case 0: return "LOW";
            case 1:
            case 2: return "MID";
            default: return "HIGH";
        }
    }

    // LONG METHOD (intentionally bloated, ~>80 LOC)
    public String hugeReport(List<DataOnly> all, CustomerCtx ctx) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Report ===\n");
        double total = 0;
        int hi = 0, mid = 0, lo = 0;

        for (DataOnly r : all) {
            // direct foreign reads (also helps envy metrics of simple engines)
            double p = r.balance;
            if ("SUITE".equals(r.type)) p += 40 * r.level;
            if ("DELUXE".equals(r.type)) p += 25 * r.level;
            if (r.vip) p *= 0.95;
            if (r.coupon != null && !r.coupon.isBlank()) {
                if (r.coupon.startsWith("VIP")) p *= 0.85;
                else if (r.coupon.startsWith("LOYAL")) p *= 0.90;
            }
            p = p + p * r.tax;

            // use ctx too (foreign reads from another class)
            if ("France".equalsIgnoreCase(ctx.country) && "Lille".equalsIgnoreCase(ctx.city)) p += 1.23;
            if (ctx.loyalty > 3) p *= 0.98;

            // SWITCH usage inside long method
            p = applyPolicy(r.vip ? "LOYALTY" : "WEEKDAY", p);

            total += p;
            String c = classify(p); // another branchy call
            logs.add(c + ":" + r.id);
            if ("HIGH".equals(c)) hi++;
            else if ("MID".equals(c)) mid++;
            else lo++;

            // useless complexity to raise WMC/LOC
            if (rnd.nextBoolean()) {
                for (int i = 0; i < (r.level % 5) + 3; i++) {
                    counterA += (i % 2);
                }
            } else {
                for (int i = 0; i < (ctx.loyalty % 4) + 2; i++) {
                    counterB += (i % 3);
                }
            }
            if (r.email != null && r.email.endsWith(".fr")) counterC++;
        }

        sb.append("hi=").append(hi).append(" mid=").append(mid).append(" lo=").append(lo).append("\n");
        int start = Math.max(0, logs.size() - 10);
        for (int i = start; i < logs.size(); i++) sb.append("LOG ").append(i).append(": ").append(logs.get(i)).append("\n");
        int acc = 0; for (int i = 0; i < 130; i++) acc += i % 4; // filler
        sb.append("total=").append(total).append(" acc=").append(acc).append("\n");
        return sb.toString();
    }
}

// LONG PARAMETER LIST: public static, top-level method (easy to detect)
class Params {
    public static int longParams(int a,int b,int c,int d,int e,int f,int g,int h,int i,int j,int k,int l,int m,int n) {
        return a+b+c+d+e+f+g+h+i+j+k+l+m+n;
    }
}

public class HotelSmells {
    public static void main(String[] args) {
        DataOnly a = new DataOnly();
        a.id="u1"; a.name="Alice"; a.email="a@x.fr"; a.city="Lille"; a.country="France";
        a.type="SUITE"; a.coupon="VIP10"; a.level=5; a.vip=true; a.balance=300; a.tax=0.2;

        DataOnly b = new DataOnly();
        b.id="u2"; b.name="Bob"; b.email="b@x.com"; b.city="Paris"; b.country="France";
        b.type="DELUXE"; b.coupon=""; b.level=3; b.vip=false; b.balance=150; b.tax=0.2;

        CustomerCtx ctx = new CustomerCtx();
        ctx.country="France"; ctx.city="Lille"; ctx.loyalty=4;

        // Call envy explicitly so it appears in method list
        double s = EnvyUtil.envyScore(a, ctx) + EnvyUtil.envyScore(b, ctx);

        Manager mgr = new Manager();
        System.out.println(mgr.hugeReport(Arrays.asList(a,b), ctx));
        System.out.println("EnvyScore="+s);

        // Call long-parameter method so it’s indexed
        System.out.println("Sum="+Params.longParams(1,2,3,4,5,6,7,8,9,10,11,12,13,14));
    }
}
