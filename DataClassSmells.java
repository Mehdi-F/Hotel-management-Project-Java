// File: DataClassSmells.java

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A single file containing several classes that exhibit the "Data Class" code smell.
 * Only one public class is used (this file's class), with all smell examples as nested classes.
 *
 * Smells included:
 *  - CustomerData: Anemic JavaBean (getters/setters for many fields, no behavior)
 *  - OrderRecord: Public field struct (mutable bag of data)
 *  - AddressDTO: DTO with Builder (pure data transfer, no domain logic)
 *  - SensorReading: Immutable holder (getters only, no behavior)
 *  - InventoryItem: Value-like class (equals/hashCode/toString but no rules/behavior)
 *  - MutableFlags: Many boolean flags (primitive obsession + anemic)
 */
public class DataClassSmells {

    /**
     * SMELL: Data Class
     * - Many fields
     * - Getters/Setters for everything
     * - No domain behavior
     */
    public static class CustomerData {
        private long id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String postalCode;
        private String country;
        private int loyaltyPoints;
        private boolean active;

        public CustomerData() { /* no-op */ }

        public CustomerData(long id, String firstName, String lastName, String email, String phone,
                            String addressLine1, String addressLine2, String city, String postalCode,
                            String country, int loyaltyPoints, boolean active) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;
            this.addressLine1 = addressLine1;
            this.addressLine2 = addressLine2;
            this.city = city;
            this.postalCode = postalCode;
            this.country = country;
            this.loyaltyPoints = loyaltyPoints;
            this.active = active;
        }

        public long getId() { return id; }
        public void setId(long id) { this.id = id; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getAddressLine1() { return addressLine1; }
        public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

        public String getAddressLine2() { return addressLine2; }
        public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getPostalCode() { return postalCode; }
        public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }

        public int getLoyaltyPoints() { return loyaltyPoints; }
        public void setLoyaltyPoints(int loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }

        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CustomerData)) return false;
            CustomerData that = (CustomerData) o;
            return id == that.id;
        }

        @Override
        public int hashCode() { return Objects.hash(id); }

        @Override
        public String toString() {
            return "CustomerData{id=" + id + ", firstName='" + firstName + "', lastName='" + lastName + "'}";
        }
    }

    /**
     * SMELL: Data Class
     * - Public fields, mutable struct-like container
     * - No methods/behavior
     */
    public static class OrderRecord {
        public String orderId;
        public long customerId;
        public String status;
        public List<String> itemIds = new ArrayList<>();
        public double totalAmount;
        public LocalDateTime createdAt;

        public OrderRecord() { /* public mutable struct */ }
    }

    /**
     * SMELL: Data Class
     * - Named as DTO; pure data carrier
     * - Builder pattern but still no domain behavior/logic
     */
    public static class AddressDTO {
        private final String line1;
        private final String line2;
        private final String city;
        private final String postalCode;
        private final String country;

        private AddressDTO(Builder b) {
            this.line1 = b.line1;
            this.line2 = b.line2;
            this.city = b.city;
            this.postalCode = b.postalCode;
            this.country = b.country;
        }

        public String getLine1() { return line1; }
        public String getLine2() { return line2; }
        public String getCity() { return city; }
        public String getPostalCode() { return postalCode; }
        public String getCountry() { return country; }

        public static class Builder {
            private String line1;
            private String line2;
            private String city;
            private String postalCode;
            private String country;

            public Builder line1(String v) { this.line1 = v; return this; }
            public Builder line2(String v) { this.line2 = v; return this; }
            public Builder city(String v) { this.city = v; return this; }
            public Builder postalCode(String v) { this.postalCode = v; return this; }
            public Builder country(String v) { this.country = v; return this; }

            public AddressDTO build() { return new AddressDTO(this); }
        }
    }

    /**
     * SMELL: Data Class
     * - Immutable fields + getters only
     * - No domain behavior
     */
    public static class SensorReading {
        private final double temperatureCelsius;
        private final double humidityPercent;
        private final double pressureHpa;

        public SensorReading(double temperatureCelsius, double humidityPercent, double pressureHpa) {
            this.temperatureCelsius = temperatureCelsius;
            this.humidityPercent = humidityPercent;
            this.pressureHpa = pressureHpa;
        }

        public double getTemperatureCelsius() { return temperatureCelsius; }
        public double getHumidityPercent() { return humidityPercent; }
        public double getPressureHpa() { return pressureHpa; }
    }

    /**
     * SMELL: Data Class
     * - Immutable fields, equals/hashCode/toString
     * - No domain logic (no invariants or behaviors)
     */
    public static class InventoryItem {
        private final String sku;
        private final String name;
        private final int quantity;
        private final double unitPrice;

        public InventoryItem(String sku, String name, int quantity, double unitPrice) {
            this.sku = sku;
            this.name = name;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public String getSku() { return sku; }
        public String getName() { return name; }
        public int getQuantity() { return quantity; }
        public double getUnitPrice() { return unitPrice; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof InventoryItem)) return false;
            InventoryItem that = (InventoryItem) o;
            return Objects.equals(sku, that.sku);
        }

        @Override
        public int hashCode() { return Objects.hash(sku); }

        @Override
        public String toString() {
            return "InventoryItem{sku='" + sku + "', name='" + name + "', quantity=" + quantity + ", unitPrice=" + unitPrice + "}";
        }
    }

    /**
     * SMELL: Data Class (Primitive Obsession)
     * - Many boolean flags
     * - Getters/setters only, no behavior
     */
    public static class MutableFlags {
        private boolean enabled;
        private boolean visible;
        private boolean archived;
        private boolean pinned;
        private boolean synced;
        private boolean dirty;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public boolean isVisible() { return visible; }
        public void setVisible(boolean visible) { this.visible = visible; }

        public boolean isArchived() { return archived; }
        public void setArchived(boolean archived) { this.archived = archived; }

        public boolean isPinned() { return pinned; }
        public void setPinned(boolean pinned) { this.pinned = pinned; }

        public boolean isSynced() { return synced; }
        public void setSynced(boolean synced) { this.synced = synced; }

        public boolean isDirty() { return dirty; }
        public void setDirty(boolean dirty) { this.dirty = dirty; }
    }

    // Optional: a tiny main to ensure it compiles/runs as a standalone file if you want.
    public static void main(String[] args) {
        CustomerData c = new CustomerData();
        c.setId(1L);
        c.setFirstName("John");
        c.setLastName("Doe");

        OrderRecord o = new OrderRecord();
        o.orderId = "ORD-1";
        o.customerId = 1L;
        o.itemIds.add("SKU-123");

        AddressDTO addr = new AddressDTO.Builder()
                .line1("1 Rue de Test")
                .city("Lille")
                .postalCode("59000")
                .country("FR")
                .build();

        SensorReading r = new SensorReading(24.0, 60.0, 1012.0);

        InventoryItem item = new InventoryItem("SKU-123", "Widget", 10, 2.5);

        MutableFlags flags = new MutableFlags();
        flags.setEnabled(true);

        System.out.println("OK: " + c + ", " + o.orderId + ", " + addr.getCity()
                + ", " + r.getTemperatureCelsius() + ", " + item + ", enabled=" + flags.isEnabled());
    }
}
