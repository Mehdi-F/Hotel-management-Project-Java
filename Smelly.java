// File: Main.java
// Crafted for detectors that:
// - count ATFD only on direct foreign attribute access (not getters).
// - require public/static methods for Long Parameter List listing.
// Smells: Feature Envy, God Class, Data Class, Long Method, Long Parameter List, Switch Statement.

import java.util.*;

public class Smelly {

    // ---------------------- FOREIGN DATA HOLDERS ----------------------
    // public fields on purpose (to raise ATFD when accessed from elsewhere)
    static class ForeignA {
        public String id, name, email, city, country, type, coupon;
        public int level;
        public boolean vip;
        public double balance, tax;
    }
    static class ForeignB {
        public String country, city;
        public int loyalty;
    }

    // --------------------------- DATA CLASS ---------------------------
    // Pure DTO: fields + getters/setters only (low WOC).
    static class PureDTO {
        private String a,b,c,d,e,f,g,h,i,j;
        private int k,l,m;
        public String getA(){return a;} public void setA(String v){a=v;}
        public String getB(){return b;} public void setB(String v){b=v;}
        public String getC(){return c;} public void setC(String v){c=v;}
        public String getD(){return d;} public void setD(String v){d=v;}
        public String getE(){return e;} public void setE(String v){e=v;}
        public String getF(){return f;} public void setF(String v){f=v;}
        public String getG(){return g;} public void setG(String v){g=v;}
        public String getH(){return h;} public void setH(String v){h=v;}
        public String getI(){return i;} public void setI(String v){i=v;}
        public String getJ(){return j;} public void setJ(String v){j=v;}
        public int getK(){return k;} public void setK(int v){k=v;}
        public int getL(){return l;} public void setL(int v){l=v;}
        public int getM(){return m;} public void setM(int v){m=v;}
    }

    // ---------------------------- GOD CLASS ---------------------------
    // Many responsibilities + high WMC + direct foreign attribute touches (ATFD).
    static class GodBucket {
        List<String> logs = new ArrayList<>();
        Random rnd = new Random();

        // SWITCH statement (obvious)
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

        // LONG METHOD (bloated intentionally) and many foreign field reads to raise ATFD.
        public String hugeReport(List<ForeignA> as, ForeignB b) {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Report ===\n");
            double total = 0;
            int hi=0, mid=0, lo=0;

            for (ForeignA r : as) {
                // --- FEATURE ENVY style direct foreign accesses (lots of them) ---
                double p = r.balance;
                if ("SUITE".equals(r.type)) p += 40 * r.level;
                if ("DELUXE".equals(r.type)) p += 25 * r.level;
                if (r.vip) p *= 0.95;
                if (r.coupon != null && !r.coupon.isBlank()) {
                    if (r.coupon.startsWith("VIP")) p *= 0.85;
                    else if (r.coupon.startsWith("LOYAL")) p *= 0.90;
                }
                p = p + p * r.tax;

                // use many fields from ForeignB as well (FDP >= 2)
                if ("France".equalsIgnoreCase(b.country) && "Lille".equalsIgnoreCase(b.city)) p += 1.23;
                if (b.loyalty > 3) p *= 0.98;
                if ("Paris".equalsIgnoreCase(b.city)) p += 2.0;
                if ("Tunisia".equalsIgnoreCase(b.country)) p += 0.5;

                // more direct reads from ForeignA to keep ATFD high
                if (r.email != null && r.email.endsWith(".fr")) p += 0.1;
                if (r.city != null && r.city.length() > 3) p += 0.1;
                if (r.country != null && r.country.length() > 2) p += 0.1;
                if (r.name != null && r.name.length() > 1) p += 0.1;

                // pick a policy via switch
                p = applyPolicy(r.vip ? "LOYALTY" : "WEEKDAY", p);

                total += p;
                if (p > 700) { logs.add("HIGH:"+r.id); hi++; }
                else if (p > 300) { logs.add("MID:"+r.id); mid++; }
                else { logs.add("LOW:"+r.id); lo++; }

                // extra branches / loops to bloat WMC/LOC
                if (rnd.nextBoolean()) {
                    for (int i = 0; i < (r.level % 5) + 3; i++) total += (i % 2);
                } else {
                    for (int i = 0; i < (b.loyalty % 4) + 2; i++) total -= (i % 3) * 0.1;
                }
            }

            sb.append("hi=").append(hi).append(" mid=").append(mid).append(" lo=").append(lo).append("\n");
            int start = Math.max(0, logs.size() - 10);
            for (int i = start; i < logs.size(); i++) sb.append("LOG ").append(i).append(": ").append(logs.get(i)).append("\n");
            // some filler to keep it long
            int acc = 0;
            for (int i = 0; i < 120; i++) acc += i % 4;
            sb.append("total=").append(total).append(" acc=").append(acc).append("\n");
            return sb.toString();
        }
    }

    // ---------------------- FEATURE ENVY (method-level) ----------------------
    // Standalone utility that greedily reads MANY foreign attributes from TWO different objects.
    static class EnvyCalc {
        public static double envy(ForeignA a, ForeignB b) {
            double p = a.balance;
            // read a LOT of foreign attributes (ATFD++)
            p += a.level * 10;
            if ("SUITE".equals(a.type)) p += 100;
            if (a.vip) p *= 0.95;
            if (a.coupon != null) p *= a.coupon.startsWith("VIP") ? 0.85 : 1.0;
            if (a.email != null && a.email.endsWith(".fr")) p += 0.2;
            if ("France".equalsIgnoreCase(a.country)) p += 0.3;
            if ("Lille".equalsIgnoreCase(a.city)) p += 0.2;
            // and from ForeignB too (FDP >= 2)
            if ("France".equalsIgnoreCase(b.country)) p += 0.4;
            if ("Paris".equalsIgnoreCase(b.city)) p += 0.1;
            if (b.loyalty > 2) p *= 0.99;
            return p;
        }
    }

    // --------------------- LONG PARAMETER LIST (public static) ---------------------
    public static int longParams(int a,int b,int c,int d,int e,int f,int g,int h,int i,int j,int k,int l) {
        return (a+b+c+d+e+f+g+h+i+j+k+l);
    }

    // -------------------------------- DEMO --------------------------------
    public static void main(String[] args) {
        // fill foreign objects
        ForeignA fa1 = new ForeignA(); fa1.id="u1"; fa1.name="Alice"; fa1.email="a@x.fr"; fa1.city="Lille"; fa1.country="France";
        fa1.type="SUITE"; fa1.coupon="VIP10"; fa1.level=5; fa1.vip=true; fa1.balance=300; fa1.tax=0.2;

        ForeignA fa2 = new ForeignA(); fa2.id="u2"; fa2.name="Bob"; fa2.email="b@x.com"; fa2.city="Paris"; fa2.country="France";
        fa2.type="DELUXE"; fa2.coupon=""; fa2.level=3; fa2.vip=false; fa2.balance=150; fa2.tax=0.2;

        ForeignB fb = new ForeignB(); fb.country="France"; fb.city="Lille"; fb.loyalty=4;

        // call Feature Envy method (reads many foreign attributes)
        double envyScore = EnvyCalc.envy(fa1, fb) + EnvyCalc.envy(fa2, fb);

        // God class usage: long method + switch inside
        GodBucket god = new GodBucket();
        System.out.println(god.hugeReport(Arrays.asList(fa1, fa2), fb));
        System.out.println("EnvyScore="+envyScore);

        // Long Parameter List method (public static)
        System.out.println("Sum="+longParams(1,2,3,4,5,6,7,8,9,10,11,12));
    }
}
