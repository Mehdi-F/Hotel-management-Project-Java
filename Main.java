// File: Main.java
// Intentionally smelly for static analyzers: Long Method, God Class, Data Class,
// Feature Envy, Long Parameter List, Switch Statement.

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Main {

    // ========================= GOD CLASS =========================
    // Does everything: "controller", fake persistence, calculations, validation & reporting.
    public static class HotelController {
        private final Map<String, Booking> cache = new HashMap<>();
        private final List<String> logs = new ArrayList<>();
        private String dbUrl = "jdbc:mysql://localhost/hotel";
        private String httpEndpoint = "https://api.example.com";
        private String lastUserMessage = "";

        // ================== LONG PARAMETER LIST (14 params) ==================
        public Booking reserve(
                String customerName,
                String phone,
                String address,
                String country,
                String city,
                String roomType,
                int nights,
                boolean breakfast,
                boolean pickup,
                double basePrice,
                double taxRate,
                String coupon,
                int adults,
                int children
        ) {
            Customer c = new Customer();
            c.setName(customerName);
            c.setPhone(phone);
            c.setAddress(address);
            c.setCountry(country);
            c.setCity(city);
            c.setLoyaltyPoints( (adults + children) * 3 ); // silly

            Booking b = new Booking();
            b.setCustomer(c);
            b.setRoomType(roomType);
            b.setNights(nights);
            b.setBreakfast(breakfast);
            b.setAirportPickup(pickup);
            b.setBasePrice(basePrice);
            b.setTaxRate(taxRate);
            b.setCouponCode(coupon);
            b.setAdults(adults);
            b.setChildren(children);
            b.setCreatedAt(LocalDate.now());

            String key = customerName + "-" + System.nanoTime();
            cache.put(key, b);
            logs.add("reserve() -> " + key);
            return b;
        }

        // ====================== SWITCH STATEMENT ======================
        public double applyStrategy(String strategy, double amount) {
            switch (strategy) {
                case "WEEKEND": return amount * 1.12;
                case "WEEKDAY": return amount;
                case "LOYALTY": return amount * 0.90;
                case "LAST_MINUTE": return amount * 0.78;
                case "BLACK_FRIDAY": return amount * 0.60;
                default: return amount;
            }
        }

        // ======================= LONG METHOD =========================
        // Many steps, branches, loops, text building, dumb IO — on purpose.
        public String generateHugeOperationsReport(String anyLocalFile) {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Ops Report ===\n");
            sb.append("DB=").append(dbUrl).append("\n");
            sb.append("HTTP=").append(httpEndpoint).append("\n");
            sb.append("Cache=").append(cache.size()).append("\n");

            // 1) read arbitrary local file to inflate length
            int lines = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(anyLocalFile))) {
                String ln;
                while ((ln = br.readLine()) != null) {
                    lines++;
                    if (ln.contains("ERROR")) logs.add("ERR:" + ln);
                    else if (ln.contains("WARN")) logs.add("WARN:" + ln);
                    else if (ln.trim().isEmpty()) { /* noop */ }
                    // useless processing to make it longer
                    if (ln.length() % 2 == 0) lastUserMessage = ln;
                }
            } catch (IOException e) {
                logs.add("IO:" + e.getMessage());
            }
            sb.append("Scanned: ").append(anyLocalFile).append(" lines=").append(lines).append("\n");

            // 2) aggregate per room type
            Map<String, Integer> roomCounts = new HashMap<>();
            Map<String, Double> roomRevenue = new HashMap<>();
            PriceUtil util = new PriceUtil();

            for (Map.Entry<String, Booking> entry : cache.entrySet()) {
                Booking b = entry.getValue();
                double price = util.computeFinalPrice(b.getCustomer(), b);  // FEATURE ENVY uses many getters
                String rt = b.getRoomType();

                // another switch, just to be loud
                switch (rt) {
                    case "SUITE":
                    case "DELUXE":
                    case "EXECUTIVE":
                        price = applyStrategy("WEEKEND", price);
                        break;
                    case "DOUBLE":
                    case "SINGLE":
                        price = applyStrategy("WEEKDAY", price);
                        break;
                    default:
                        price = applyStrategy("LOYALTY", price);
                }

                roomCounts.put(rt, roomCounts.getOrDefault(rt, 0) + 1);
                roomRevenue.put(rt, roomRevenue.getOrDefault(rt, 0.0) + price);

                // some redundant log work
                if (price > 500) {
                    logs.add("HIGH:" + rt + ":" + price);
                } else if (price < 50) {
                    logs.add("LOW:" + rt + ":" + price);
                } else {
                    logs.add("NORM:" + rt + ":" + price);
                }
            }

            // 3) dump stats
            sb.append("-- Room Counts --\n");
            for (String rt : new TreeSet<>(roomCounts.keySet())) {
                sb.append(rt).append(" -> ").append(roomCounts.get(rt)).append("\n");
            }
            sb.append("-- Room Revenue --\n");
            for (String rt : new TreeSet<>(roomRevenue.keySet())) {
                sb.append(rt).append(" -> ").append(String.format(Locale.US, "%.2f", roomRevenue.get(rt))).append("\n");
            }

            // 4) show last N logs
            sb.append("-- Recent logs --\n");
            int start = Math.max(0, logs.size() - 10);
            for (int i = start; i < logs.size(); i++) {
                sb.append("LOG ").append(i).append(": ").append(logs.get(i)).append("\n");
            }

            // 5) do more arbitrary formatting, iterations, and branches
            List<String> warnings = new ArrayList<>();
            for (Map.Entry<String, Integer> e : roomCounts.entrySet()) {
                if (e.getValue() > 3) warnings.add("Popular: " + e.getKey());
                else if (e.getValue() == 0) warnings.add("No bookings: " + e.getKey());
                else warnings.add("OK: " + e.getKey());
            }
            Collections.sort(warnings);
            sb.append("-- Notes --\n");
            for (String w : warnings) sb.append(w).append("\n");

            // 6) fake export steps
            sb.append("Export=OK\n");
            sb.append("Done at ").append(LocalDate.now()).append("\n");
            return sb.toString();
        }
    }

    // ========================= DATA CLASSES =========================
    public static class Booking {
        private Customer customer;
        private String roomType;
        private int nights;
        private boolean breakfast;
        private boolean airportPickup;
        private double basePrice;
        private double taxRate;
        private String couponCode;
        private int adults;
        private int children;
        private LocalDate createdAt;

        public Customer getCustomer() { return customer; }
        public void setCustomer(Customer customer) { this.customer = customer; }
        public String getRoomType() { return roomType; }
        public void setRoomType(String roomType) { this.roomType = roomType; }
        public int getNights() { return nights; }
        public void setNights(int nights) { this.nights = nights; }
        public boolean isBreakfast() { return breakfast; }
        public void setBreakfast(boolean breakfast) { this.breakfast = breakfast; }
        public boolean isAirportPickup() { return airportPickup; }
        public void setAirportPickup(boolean airportPickup) { this.airportPickup = airportPickup; }
        public double getBasePrice() { return basePrice; }
        public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
        public double getTaxRate() { return taxRate; }
        public void setTaxRate(double taxRate) { this.taxRate = taxRate; }
        public String getCouponCode() { return couponCode; }
        public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
        public int getAdults() { return adults; }
        public void setAdults(int adults) { this.adults = adults; }
        public int getChildren() { return children; }
        public void setChildren(int children) { this.children = children; }
        public LocalDate getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
    }

    public static class Customer {
        private String name;
        private String phone;
        private String address;
        private String country;
        private String city;
        private int loyaltyPoints;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public int getLoyaltyPoints() { return loyaltyPoints; }
        public void setLoyaltyPoints(int loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }
    }

    // ========================= FEATURE ENVY =========================
    // Should live inside Booking/Customer but sits here and uses many getters.
    public static class PriceUtil {
        public double computeFinalPrice(Customer c, Booking b) {
            double price = b.getBasePrice();

            // room-based surcharges
            if ("SUITE".equals(b.getRoomType())) price += 40 * b.getNights();
            else if ("DELUXE".equals(b.getRoomType())) price += 25 * b.getNights();
            else if ("EXECUTIVE".equals(b.getRoomType())) price += 15 * b.getNights();

            // options
            if (b.isBreakfast()) price += 12.5 * Math.max(1, b.getAdults());
            if (b.isAirportPickup()) price += 30;

            // coupon
            String code = b.getCouponCode();
            if (code != null && !code.isBlank()) {
                if (code.startsWith("VIP")) price *= 0.85;
                else if (code.startsWith("LOYAL")) price *= 0.90;
            }

            // “loyalty discount” from Customer (cross-object data grabbing)
            if (c.getLoyaltyPoints() > 50) price *= 0.95;

            // tax
            price = price + (price * b.getTaxRate());

            // silly location tweak (pulling more from Customer)
            if ("France".equalsIgnoreCase(c.getCountry()) && "Lille".equalsIgnoreCase(c.getCity())) {
                price += 1.23;
            }

            return price;
        }
    }

    // ============================== Demo ==============================
    public static void main(String[] args) {
        HotelController ctrl = new HotelController();

        // build some data through the LONG PARAMETER LIST
        Booking b1 = ctrl.reserve("Alice","0600000001","1 Rue A","France","Lille",
                "SUITE",3,true,false,150,0.2,"VIP2025",2,0);
        Booking b2 = ctrl.reserve("Bob","0600000002","2 Rue B","France","Lille",
                "DOUBLE",1,false,true,80,0.2,"",1,1);
        Booking b3 = ctrl.reserve("Charlie","0600000003","3 Rue C","France","Paris",
                "DELUXE",2,true,true,120,0.2,"LOYAL10",2,2);

        // call LONG METHOD (pass any readable file to avoid exception)
        String report = ctrl.generateHugeOperationsReport("README.md");
        System.out.println(report);
    }
}
