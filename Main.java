// File: Main.java
// Designed to trigger: Feature Envy, God Class, Data Class, Long Method,
// Long Parameter List, and Switch Statement in common smell detectors.

import java.util.*;
import java.time.LocalDate;

public class Main {

    // ----------------------------- GOD CLASS -----------------------------
    // Many responsibilities, many fields, complex methods, low cohesion.
    static class MegaManager {
        // lots of state that different methods use disjointly (hurts cohesion)
        private int counterA, counterB, counterC, counterD, counterE;
        private String apiUrl = "https://api.example.com";
        private String db = "jdbc:mysql://localhost/db";
        private List<String> logs = new ArrayList<>();
        private Map<String, RecordData> cache = new HashMap<>();
        private Random rnd = new Random();

        // ------------------ LONG PARAMETER LIST (12 params) ------------------
        public void register(
                String id, String name, String phone, String email, String city, String country,
                String type, int level, boolean vip, double balance, double tax, String coupon
        ) {
            RecordData r = new RecordData();
            r.setId(id);
            r.setName(name);
            r.setPhone(phone);
            r.setEmail(email);
            r.setCity(city);
            r.setCountry(country);
            r.setType(type);
            r.setLevel(level);
            r.setVip(vip);
            r.setBalance(balance);
            r.setTax(tax);
            r.setCoupon(coupon);
            r.setCreated(LocalDate.now());
            cache.put(id, r);
            counterA++; // touches a different field than other methods
        }

        // ------------------------ SWITCH STATEMENT --------------------------
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

        // -------------------------- LONG METHOD -----------------------------
        // Inflate cyclomatic complexity (WMC) and length; touch mostly counterB.
        public String generateHugeReport() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Report ===\n");
            sb.append("API=").append(apiUrl).append(" DB=").append(db).append("\n");
            sb.append("CacheSize=").append(cache.size()).append("\n");

            // lots of decisions/loops to push WMC high
            int big = 0;
            for (Map.Entry<String, RecordData> e : cache.entrySet()) {
                RecordData r = e.getValue();
                double p = PriceUtil.featureEnvyPrice(r, new CustomerInfo("FR", "Lille", r.getLevel())); // FEATURE ENVY inside PriceUtil
                if (p > 700) { logs.add("HIGH:" + r.getId()); counterB++; }
                else if (p > 300) { logs.add("MID:" + r.getId()); counterB += 2; }
                else { logs.add("LOW:" + r.getId()); counterB += 3; }

                // more branches
                String t = r.getType();
                if ("SUITE".equals(t)) big += 5;
                else if ("DELUXE".equals(t)) big += 3;
                else if ("DOUBLE".equals(t)) big += 2;
                else big++;
                // use switch again
                p = applyPolicy(r.isVip() ? "LOYALTY" : "WEEKDAY", p);
                if (rnd.nextBoolean()) { big += (int)p % 7; } else { big -= (int)p % 5; }
            }

            // dump logs
            int start = Math.max(0, logs.size() - 15);
            for (int i = start; i < logs.size(); i++) sb.append("LOG ").append(i).append(": ").append(logs.get(i)).append("\n");

            // extra loops to keep it long
            int acc = 0;
            for (int i = 0; i < 100; i++) {
                acc += i % 4;
                if (acc % 9 == 0) sb.append('.');
            }
            sb.append("\nacc=").append(acc).append(" big=").append(big).append("\n");
            return sb.toString();
        }

        // Another unrelated method touching other fields (hurts cohesion/TCC)
        public void rotateKeys() {
            // only counterC/counterD here
            for (int i = 0; i < 10; i++) {
                counterC += i;
                if (i % 2 == 0) counterD++;
                else counterD += 2;
            }
        }

        // Another unrelated method touching different field (hurts TCC)
        public boolean ping() {
            counterE++;
            return apiUrl.startsWith("https");
        }
    }

    // ----------------------------- DATA CLASSES -----------------------------
    // Only fields + accessors. No behavior.
    static class RecordData {
        private String id, name, phone, email, city, country, type, coupon;
        private int level;
        private boolean vip;
        private double balance, tax;
        private LocalDate created;

        public String getId(){return id;} public void setId(String v){id=v;}
        public String getName(){return name;} public void setName(String v){name=v;}
        public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
        public String getEmail(){return email;} public void setEmail(String v){email=v;}
        public String getCity(){return city;} public void setCity(String v){city=v;}
        public String getCountry(){return country;} public void setCountry(String v){country=v;}
        public String getType(){return type;} public void setType(String v){type=v;}
        public String getCoupon(){return coupon;} public void setCoupon(String v){coupon=v;}
        public int getLevel(){return level;} public void setLevel(int v){level=v;}
        public boolean isVip(){return vip;} public void setVip(boolean v){vip=v;}
        public double getBalance(){return balance;} public void setBalance(double v){balance=v;}
        public double getTax(){return tax;} public void setTax(double v){tax=v;}
        public LocalDate getCreated(){return created;} public void setCreated(LocalDate v){created=v;}
    }

    static class CustomerInfo {
        private String country, city;
        private int loyaltyLevel;
        public CustomerInfo(String country, String city, int loyaltyLevel) {
            this.country = country; this.city = city; this.loyaltyLevel = loyaltyLevel;
        }
        public String getCountry(){return country;}
        public String getCity(){return city;}
        public int getLoyaltyLevel(){return loyaltyLevel;}
    }

    // ----------------------------- FEATURE ENVY -----------------------------
    // This method intentionally accesses MANY foreign getters (high ATFD)
    // from TWO different foreign classes (FDP >= 2) and almost no local fields.
    static class PriceUtil {
        public static double featureEnvyPrice(RecordData r, CustomerInfo c) {
            // no local fields; everything is taken from foreign objects:
            double p = r.getBalance();
            // access a lot of foreign attributes repeatedly to push ATFD high
            if ("SUITE".equals(r.getType())) p += 40 * r.getLevel();
            if ("DELUXE".equals(r.getType())) p += 25 * r.getLevel();
            if (r.isVip()) p *= 0.95;
            String coupon = r.getCoupon();
            if (coupon != null && !coupon.isBlank()) {
                if (coupon.startsWith("VIP")) p *= 0.85;
                else if (coupon.startsWith("LOYAL")) p *= 0.90;
            }
            p = p + p * r.getTax();
            // Touch plenty of CustomerInfo getters too:
            if ("France".equalsIgnoreCase(c.getCountry()) && "Lille".equalsIgnoreCase(c.getCity())) p += 1.23;
            if (c.getLoyaltyLevel() > 3) p *= 0.98;
            // more foreign accesses to ensure ATFD is large
            if ("Paris".equalsIgnoreCase(c.getCity())) p += 2.0;
            if ("Tunisia".equalsIgnoreCase(c.getCountry())) p += 0.5;
            if (r.getEmail() != null && r.getEmail().endsWith(".fr")) p += 0.1;
            if (r.getPhone() != null && r.getPhone().startsWith("06")) p += 0.1;
            if (r.getCity() != null && r.getCity().length() > 3) p += 0.1;
            if (r.getCountry() != null && r.getCountry().length() > 2) p += 0.1;
            return p;
        }
    }

    // -------------------------------- Demo ---------------------------------
    public static void main(String[] args) {
        MegaManager mm = new MegaManager();

        // a few entries
        mm.register("u1","Alice","0600","a@x.fr","Lille","France","SUITE",5,true,300,0.2,"VIP10");
        mm.register("u2","Bob","0601","b@x.fr","Paris","France","DELUXE",3,false,150,0.2,"");
        mm.register("u3","Chloe","0602","c@x.com","Tunis","Tunisia","DOUBLE",2,true,120,0.2,"LOYAL5");

        // increase WMC / reduce cohesion by calling unrelated methods
        mm.rotateKeys();
        mm.ping();

        // trigger LONG METHOD + SWITCH
        System.out.println(mm.generateHugeReport());
    }
}
