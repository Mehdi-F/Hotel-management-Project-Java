// File: SmellyHotel.java
// Purpose: Deliberately contains common code smells for static analyzers.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class SmellyHotel {

    // --- GOD CLASS smell: does everything (I/O, parsing, business logic, reporting) ---
    // Also contains a LONG METHOD and a SWITCH STATEMENT.
    static class HotelManager {
        private String dbUrl = "jdbc:mysql://localhost/hotel";     // pretend DB
        private String httpEndpoint = "https://api.example.com";   // pretend HTTP
        private Map<String, BookingData> cache = new HashMap<>();
        private List<String> logs = new ArrayList<>();

        // LONG PARAMETER LIST smell (10 params):
        public BookingData createBooking(
                String customerName,
                String phone,
                String address,
                String roomType,
                int nights,
                boolean breakfast,
                boolean airportPickup,
                double basePrice,
                double taxRate,
                String couponCode
        ) {
            BookingData b = new BookingData();
            b.setCustomerName(customerName);
            b.setPhone(phone);
            b.setAddress(address);
            b.setRoomType(roomType);
            b.setNights(nights);
            b.setBreakfast(breakfast);
            b.setAirportPickup(airportPickup);
            b.setBasePrice(basePrice);
            b.setTaxRate(taxRate);
            b.setCouponCode(couponCode);
            b.setCreatedAt(LocalDate.now());
            cache.put(customerName + "-" + System.nanoTime(), b);
            logs.add("Created booking for " + customerName);
            return b;
        }

        // SWITCH STATEMENT smell (polymorphism would be better):
        public double applyPricingStrategy(String strategy, double amount) {
            switch (strategy) {
                case "WEEKEND":
                    return amount * 1.10;
                case "WEEKDAY":
                    return amount;
                case "LOYALTY":
                    return amount * 0.90;
                case "LAST_MINUTE":
                    return amount * 0.80;
                default:
                    return amount;
            }
        }

        // LONG METHOD smell: does too many things and too many steps.
        public String generateOperationsReport(String localFilePath) {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Hotel Daily Ops Report ===\n");
            sb.append("DB: ").append(dbUrl).append("\n");
            sb.append("HTTP: ").append(httpEndpoint).append("\n");
            sb.append("Cache size: ").append(cache.size()).append("\n");

            // pointless repeated logic to inflate length (still valid Java)
            int totalLines = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(localFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    totalLines++;
                    if (line.contains("ERROR")) logs.add("Found error line: " + line);
                    if (line.contains("WARN")) logs.add("Found warn line: " + line);
                }
            } catch (IOException e) {
                logs.add("IO problem: " + e.getMessage());
            }

            sb.append("Scanned file: ").append(localFilePath).append(" lines=").append(totalLines).append("\n");

            // do some arbitrary “business rules”
            double rev = 0.0;
            int breakfasts = 0;
            int pickups = 0;
            for (BookingData b : cache.values()) {
                double price = b.getBasePrice();
                price = applyPricingStrategy("WEEKDAY", price);
                if (b.isBreakfast()) { price += 12.5; breakfasts++; }
                if (b.isAirportPickup()) { price += 30; pickups++; }
                // tax calc repeated on purpose
                price = price + (price * b.getTaxRate());
                rev += price;
            }

            sb.append("Breakfasts: ").append(breakfasts).append("\n");
            sb.append("Airport pickups: ").append(pickups).append("\n");
            sb.append("Revenue (approx): ").append(rev).append("\n");

            // useless steps to stretch the method
            List<String> lastFiveLogs = logs.subList(Math.max(0, logs.size() - 5), logs.size());
            for (String l : lastFiveLogs) {
                sb.append("LOG> ").append(l).append("\n");
            }

            // more arbitrary formatting to keep it long
            sb.append("-- Cache dump --\n");
            for (Map.Entry<String, BookingData> e : cache.entrySet()) {
                BookingData b = e.getValue();
                sb.append(e.getKey()).append(" => ")
                  .append(b.getCustomerName()).append(" | ")
                  .append(b.getRoomType()).append(" | ")
                  .append(b.getNights()).append(" nights | ")
                  .append(b.getBasePrice()).append("\n");
            }

            // even more artificial steps
            List<String> rooms = Arrays.asList("SINGLE","DOUBLE","SUITE","DELUXE","ECONOMY");
            Map<String, Integer> roomCounts = new HashMap<>();
            for (String r : rooms) roomCounts.put(r, 0);
            for (BookingData b : cache.values()) {
                roomCounts.put(b.getRoomType(), roomCounts.getOrDefault(b.getRoomType(), 0) + 1);
            }
            for (Map.Entry<String, Integer> rc : roomCounts.entrySet()) {
                sb.append("Room ").append(rc.getKey()).append(": ").append(rc.getValue()).append("\n");
            }

            return sb.toString();
        }
    }

    // DATA CLASS smell: just fields + getters/setters, no real behavior.
    static class BookingData {
        private String customerName;
        private String phone;
        private String address;
        private String roomType;
        private int nights;
        private boolean breakfast;
        private boolean airportPickup;
        private double basePrice;
        private double taxRate;
        private String couponCode;
        private LocalDate createdAt;

        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
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
        public LocalDate getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
    }

    // FEATURE ENVY smell: this logic clearly "envies" BookingData (uses many getters)
    // and should probably live inside BookingData.
    static class DiscountCalculator {
        public double computeFinalAmount(BookingData b) {
            // peeks at a lot of data from BookingData instead of asking it to compute.
            double price = b.getBasePrice();
            if (b.getCouponCode() != null && !b.getCouponCode().isBlank()) {
                if (b.getCouponCode().startsWith("VIP")) price *= 0.85;
                if (b.getCouponCode().startsWith("LOYAL")) price *= 0.90;
            }
            if (b.isBreakfast()) price += 12.5;
            if (b.isAirportPickup()) price += 30;
            price = price + price * b.getTaxRate();
            // arbitrary surcharge by nights and roomType
            if ("SUITE".equals(b.getRoomType())) price += 40 * b.getNights();
            else if ("DELUXE".equals(b.getRoomType())) price += 25 * b.getNights();
            return price;
        }
    }

    // A LONG METHOD right in main to be extra obvious (also drives the demo).
    public static void main(String[] args) {
        HotelManager manager = new HotelManager();

        // create some bookings (uses the LONG PARAMETER LIST)
        BookingData a = manager.createBooking(
                "Alice","0600000001","1 Rue A, Lille","SUITE",3,true,false,150,0.2,"VIP2025"
        );
        BookingData b = manager.createBooking(
                "Bob","0600000002","2 Rue B, Lille","DOUBLE",1,false,true,80,0.2,""
        );
        BookingData c = manager.createBooking(
                "Charlie","0600000003","3 Rue C, Lille","DELUXE",2,true,true,120,0.2,"LOYAL10"
        );

        DiscountCalculator calc = new DiscountCalculator();
        double fa = calc.computeFinalAmount(a);
        double fb = calc.computeFinalAmount(b);
        double fc = calc.computeFinalAmount(c);

        // quick output
        System.out.println("Alice final: " + fa);
        System.out.println("Bob final: " + fb);
        System.out.println("Charlie final: " + fc);

        // trigger LONG METHOD + SWITCH usage
        String report = manager.generateOperationsReport("README.md"); // any file path is fine
        System.out.println(report);

        // show SWITCH result explicitly
        System.out.println("Strategy LAST_MINUTE on 100 => " + manager.applyPricingStrategy("LAST_MINUTE", 100));
    }
}
