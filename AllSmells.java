import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

// =====================================================================
//  DATA CLASS  -> JavaBean: champs prives + uniquement getters/setters
// =====================================================================
class UserData {import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

// =====================================================================
//  DATA CLASS  -> JavaBean: champs prives + uniquement getters/setters
// =====================================================================
class UserData {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String city;
    private String country;
    private int age;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}

// =====================================================================
//  FEATURE ENVY  -> formatShippingLabel lit toute la donnee de Address
// =====================================================================
class Address {
    private String street;
    private String city;
    private String zipCode;
    private String country;
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getZipCode() { return zipCode; }
    public String getCountry() { return country; }
}

class Invoice {
    private Address customerAddress;
    public String formatShippingLabel() {
        String s = customerAddress.getStreet();
        String c = customerAddress.getCity();
        String z = customerAddress.getZipCode();
        String co = customerAddress.getCountry();
        return s + ", " + c + " " + z + ", " + co;
    }
}

// =====================================================================
//  LONG PARAMETER LIST  -> build(...) avec 8 parametres
// =====================================================================
class ReportBuilder {
    public String build(String title, String author, String date, int pages,
                        boolean draft, String footer, String header, String lang) {
        return title + author + date + pages + draft + footer + header + lang;
    }
}

// =====================================================================
//  SWITCH STATEMENT  -> gros switch a 7 branches
// =====================================================================
class PriceCalculator {
    public int price(int type, int qty) {
        int p = 0;
        switch (type) {
            case 1: p = qty * 10; break;
            case 2: p = qty * 20; break;
            case 3: p = qty * 30; break;
            case 4: p = qty * 40; break;
            case 5: p = qty * 50; break;
            case 6: p = qty * 60; break;
            default: p = qty; break;
        }
        return p;
    }
}

// =====================================================================
//  LONG METHOD  -> processOrder: longue, imbriquee, complexe
// =====================================================================
class OrderProcessor {
    public int processOrder(int[] items, int[] quantities, int mode) {
        int total = 0;
        int discount = 0;
        int tax = 0;
        int shipping = 0;
        for (int i = 0; i < items.length; i++) {
            int line = items[i] * quantities[i];
            if (line > 100) {
                if (mode == 1) {
                    discount = discount + line / 10;
                } else if (mode == 2) {
                    discount = discount + line / 5;
                } else {
                    discount = discount + line / 20;
                }
            }
            for (int j = 0; j < quantities[i]; j++) {
                if (j % 2 == 0) {
                    total = total + items[i];
                } else {
                    total = total + items[i] / 2;
                }
            }
            if (line > 500) {
                shipping = shipping + 50;
            } else {
                shipping = shipping + 10;
            }
        }
        if (total > 1000) {
            tax = total / 10;
        } else if (total > 500) {
            tax = total / 20;
        } else {
            tax = total / 50;
        }
        total = total - discount + tax + shipping;
        while (total < 0) {
            total = total + 100;
        }
        return total;
    }
}

// =====================================================================
//  GOD CLASS  -> SystemManager fait tout: beaucoup de methodes
//  complexes, champs varies, faible cohesion, fort couplage
// =====================================================================
class SystemManager {
    private List<String> users = new ArrayList<>();
    private Map<String, Integer> stock = new HashMap<>();
    private List<Integer> log = new ArrayList<>();
    private int counter;
    private double balance;
    private String status;
    private boolean active;

    public void addUsers(String[] names) {
        for (int i = 0; i < names.length; i++) {
            if (names[i] != null && !names[i].isEmpty()) {
                users.add(names[i]);
                counter++;
            }
        }
    }

    public int countLongNames() {
        int n = 0;
        for (String u : users) {
            if (u.length() > 3) {
                n++;
            }
        }
        return n;
    }

    public void restock(String item, int qty) {
        if (stock.containsKey(item)) {
            stock.put(item, stock.get(item) + qty);
        } else {
            stock.put(item, qty);
        }
    }

    public int totalStock() {
        int t = 0;
        for (Map.Entry<String, Integer> e : stock.entrySet()) {
            if (e.getValue() > 0) {
                t = t + e.getValue();
            }
        }
        return t;
    }

    public double applyInterest(double rate) {
        if (rate > 0) {
            balance = balance + balance * rate;
        } else {
            balance = balance - 1;
        }
        return balance;
    }

    public String computeStatus(int code) {
        switch (code) {
            case 1: status = "OK"; break;
            case 2: status = "WARN"; break;
            case 3: status = "ERROR"; break;
            case 4: status = "FATAL"; break;
            default: status = "UNKNOWN"; break;
        }
        return status;
    }

    public void recordLog(int value) {
        for (int i = 0; i < 5; i++) {
            if (value % 2 == 0) {
                log.add(value + i);
            } else {
                log.add(value - i);
            }
        }
    }

    public int sumLog() {
        int s = 0;
        for (int v : log) {
            if (v > 0) {
                s = s + v;
            }
        }
        return s;
    }

    public void toggle() {
        active = !active;
    }

    public void reset() {
        counter = 0;
        balance = 0;
        status = "";
        active = false;
    }

    public boolean validate(int code) {
        if (code < 0) {
            return false;
        } else if (code > 100) {
            return false;
        } else {
            return true;
        }
    }

    public int processBatch(int[] data, int mode) {
        int acc = 0;
        for (int i = 0; i < data.length; i++) {
            switch (mode) {
                case 1: acc = acc + data[i]; break;
                case 2: acc = acc + data[i] * 2; break;
                case 3: acc = acc - data[i]; break;
                default: acc = acc + 1; break;
            }
            if (acc > 1000) {
                acc = acc / 2;
            } else if (acc < 0) {
                acc = 0;
            }
        }
        return acc;
    }

    public void audit() {
        for (int i = 0; i < log.size(); i++) {
            if (log.get(i) > 100) {
                counter++;
            } else if (log.get(i) < 0) {
                counter--;
            }
        }
        for (String u : users) {
            if (u.startsWith("a")) {
                status = "A";
            }
        }
    }

    public double rebalance(double threshold) {
        if (balance > threshold) {
            balance = balance - threshold;
        } else if (balance < 0) {
            balance = 0;
        } else {
            balance = balance + 1;
        }
        return balance;
    }

    public String schedule(int day) {
        switch (day) {
            case 1: return "Mon";
            case 2: return "Tue";
            case 3: return "Wed";
            case 4: return "Thu";
            case 5: return "Fri";
            default: return "Off";
        }
    }

    public void cleanup() {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).isEmpty()) {
                users.remove(i);
            }
        }
        while (log.size() > 100) {
            log.remove(0);
        }
    }

    public int score(int a, int b) {
        int r = 0;
        if (a > b) {
            r = a - b;
        } else {
            r = b - a;
        }
        for (int i = 0; i < r; i++) {
            if (i % 3 == 0) {
                r = r + 1;
            }
        }
        return r;
    }

    public int analyze(int[] grid) {
        int max = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = i + 1; j < grid.length; j++) {
                if (grid[i] > grid[j]) {
                    if (grid[i] - grid[j] > max) {
                        max = grid[i] - grid[j];
                    }
                }
            }
        }
        return max;
    }

    public String dispatch(int event) {
        switch (event) {
            case 0: return "init";
            case 1: return "start";
            case 2: return "stop";
            case 3: return "pause";
            case 4: return "resume";
            default: break;
        }
        if (event > 100) {
            return "overflow";
        }
        return "idle";
    }

    public int summarize(int[] values) {
        int sum = 0;
        for (int v : values) {
            if (v > 50) {
                sum = sum + v;
            } else if (v > 10) {
                sum = sum + v / 2;
            } else if (v > 0) {
                sum = sum + 1;
            }
        }
        return sum;
    }
}

// =====================================================================
//  LONG METHOD  -> runPipeline: tres longue, profondement imbriquee,
//  forte complexite cyclomatique (placee en fin de fichier)
// =====================================================================
class BatchEngine {
    public int runPipeline(int[][] matrix, int mode, int limit) {
        int total = 0;
        int errors = 0;
        int hits = 0;
        int misses = 0;
        int weight = 0;
        int carry = 0;
        int phase = 0;
        int acc = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int v = matrix[i][j];
                if (v < 0) {
                    errors++;
                } else if (v == 0) {
                    misses++;
                } else if (v < limit) {
                    hits++;
                    acc = acc + v;
                } else {
                    weight = weight + v;
                }
                switch (mode) {
                    case 0: total = total + v; break;
                    case 1: total = total + v * 2; break;
                    case 2: total = total - v; break;
                    case 3: total = total + v / 2; break;
                    default: total = total + 1; break;
                }
                if (v % 2 == 0) {
                    carry = carry + 1;
                }
                if (v % 3 == 0) {
                    phase = phase + 1;
                }
            }
            if (acc > limit) {
                acc = acc - limit;
            }
        }
        while (carry > 0) {
            carry = carry - 1;
            total = total + 1;
        }
        if (errors > misses) {
            total = total - errors;
        } else if (misses > hits) {
            total = total + misses;
        } else if (hits > weight) {
            total = total + hits;
        } else {
            total = total + weight;
        }
        if (phase > 10) {
            total = total * 2;
        }
        for (int k = 0; k < limit; k++) {
            if (k % 5 == 0) {
                total = total + k;
            }
        }
        return total;
    }
}

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String city;
    private String country;
    private int age;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}

// =====================================================================
//  FEATURE ENVY  -> formatShippingLabel lit toute la donnee de Address
// =====================================================================
class Address {
    private String street;
    private String city;
    private String zipCode;
    private String country;
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getZipCode() { return zipCode; }
    public String getCountry() { return country; }
}

class Invoice {
    private Address customerAddress;
    public String formatShippingLabel() {
        String s = customerAddress.getStreet();
        String c = customerAddress.getCity();
        String z = customerAddress.getZipCode();
        String co = customerAddress.getCountry();
        return s + ", " + c + " " + z + ", " + co;
    }
}

// =====================================================================
//  LONG PARAMETER LIST  -> build(...) avec 8 parametres
// =====================================================================
class ReportBuilder {
    public String build(String title, String author, String date, int pages,
                        boolean draft, String footer, String header, String lang) {
        return title + author + date + pages + draft + footer + header + lang;
    }
}

// =====================================================================
//  SWITCH STATEMENT  -> gros switch a 7 branches
// =====================================================================
class PriceCalculator {
    public int price(int type, int qty) {
        int p = 0;
        switch (type) {
            case 1: p = qty * 10; break;
            case 2: p = qty * 20; break;
            case 3: p = qty * 30; break;
            case 4: p = qty * 40; break;
            case 5: p = qty * 50; break;
            case 6: p = qty * 60; break;
            default: p = qty; break;
        }
        return p;
    }
}

// =====================================================================
//  LONG METHOD  -> processOrder: longue, imbriquee, complexe
// =====================================================================
class OrderProcessor {
    public int processOrder(int[] items, int[] quantities, int mode) {
        int total = 0;
        int discount = 0;
        int tax = 0;
        int shipping = 0;
        for (int i = 0; i < items.length; i++) {
            int line = items[i] * quantities[i];
            if (line > 100) {
                if (mode == 1) {
                    discount = discount + line / 10;
                } else if (mode == 2) {
                    discount = discount + line / 5;
                } else {
                    discount = discount + line / 20;
                }
            }
            for (int j = 0; j < quantities[i]; j++) {
                if (j % 2 == 0) {
                    total = total + items[i];
                } else {
                    total = total + items[i] / 2;
                }
            }
            if (line > 500) {
                shipping = shipping + 50;
            } else {
                shipping = shipping + 10;
            }
        }
        if (total > 1000) {
            tax = total / 10;
        } else if (total > 500) {
            tax = total / 20;
        } else {
            tax = total / 50;
        }
        total = total - discount + tax + shipping;
        while (total < 0) {
            total = total + 100;
        }
        return total;
    }
}

// =====================================================================
//  GOD CLASS  -> SystemManager fait tout: beaucoup de methodes
//  complexes, champs varies, faible cohesion, fort couplage
// =====================================================================
class SystemManager {
    private List<String> users = new ArrayList<>();
    private Map<String, Integer> stock = new HashMap<>();
    private List<Integer> log = new ArrayList<>();
    private int counter;
    private double balance;
    private String status;
    private boolean active;

    public void addUsers(String[] names) {
        for (int i = 0; i < names.length; i++) {
            if (names[i] != null && !names[i].isEmpty()) {
                users.add(names[i]);
                counter++;
            }
        }
    }

    public int countLongNames() {
        int n = 0;
        for (String u : users) {
            if (u.length() > 3) {
                n++;
            }
        }
        return n;
    }

    public void restock(String item, int qty) {
        if (stock.containsKey(item)) {
            stock.put(item, stock.get(item) + qty);
        } else {
            stock.put(item, qty);
        }
    }

    public int totalStock() {
        int t = 0;
        for (Map.Entry<String, Integer> e : stock.entrySet()) {
            if (e.getValue() > 0) {
                t = t + e.getValue();
            }
        }
        return t;
    }

    public double applyInterest(double rate) {
        if (rate > 0) {
            balance = balance + balance * rate;
        } else {
            balance = balance - 1;
        }
        return balance;
    }

    public String computeStatus(int code) {
        switch (code) {
            case 1: status = "OK"; break;
            case 2: status = "WARN"; break;
            case 3: status = "ERROR"; break;
            case 4: status = "FATAL"; break;
            default: status = "UNKNOWN"; break;
        }
        return status;
    }

    public void recordLog(int value) {
        for (int i = 0; i < 5; i++) {
            if (value % 2 == 0) {
                log.add(value + i);
            } else {
                log.add(value - i);
            }
        }
    }

    public int sumLog() {
        int s = 0;
        for (int v : log) {
            if (v > 0) {
                s = s + v;
            }
        }
        return s;
    }

    public void toggle() {
        active = !active;
    }

    public void reset() {
        counter = 0;
        balance = 0;
        status = "";
        active = false;
    }

    public boolean validate(int code) {
        if (code < 0) {
            return false;
        } else if (code > 100) {
            return false;
        } else {
            return true;
        }
    }

    public int processBatch(int[] data, int mode) {
        int acc = 0;
        for (int i = 0; i < data.length; i++) {
            switch (mode) {
                case 1: acc = acc + data[i]; break;
                case 2: acc = acc + data[i] * 2; break;
                case 3: acc = acc - data[i]; break;
                default: acc = acc + 1; break;
            }
            if (acc > 1000) {
                acc = acc / 2;
            } else if (acc < 0) {
                acc = 0;
            }
        }
        return acc;
    }

    public void audit() {
        for (int i = 0; i < log.size(); i++) {
            if (log.get(i) > 100) {
                counter++;
            } else if (log.get(i) < 0) {
                counter--;
            }
        }
        for (String u : users) {
            if (u.startsWith("a")) {
                status = "A";
            }
        }
    }

    public double rebalance(double threshold) {
        if (balance > threshold) {
            balance = balance - threshold;
        } else if (balance < 0) {
            balance = 0;
        } else {
            balance = balance + 1;
        }
        return balance;
    }

    public String schedule(int day) {
        switch (day) {
            case 1: return "Mon";
            case 2: return "Tue";
            case 3: return "Wed";
            case 4: return "Thu";
            case 5: return "Fri";
            default: return "Off";
        }
    }

    public void cleanup() {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).isEmpty()) {
                users.remove(i);
            }
        }
        while (log.size() > 100) {
            log.remove(0);
        }
    }

    public int score(int a, int b) {
        int r = 0;
        if (a > b) {
            r = a - b;
        } else {
            r = b - a;
        }
        for (int i = 0; i < r; i++) {
            if (i % 3 == 0) {
                r = r + 1;
            }
        }
        return r;
    }

    public int analyze(int[] grid) {
        int max = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = i + 1; j < grid.length; j++) {
                if (grid[i] > grid[j]) {
                    if (grid[i] - grid[j] > max) {
                        max = grid[i] - grid[j];
                    }
                }
            }
        }
        return max;
    }

    public String dispatch(int event) {
        switch (event) {
            case 0: return "init";
            case 1: return "start";
            case 2: return "stop";
            case 3: return "pause";
            case 4: return "resume";
            default: break;
        }
        if (event > 100) {
            return "overflow";
        }
        return "idle";
    }

    public int summarize(int[] values) {
        int sum = 0;
        for (int v : values) {
            if (v > 50) {
                sum = sum + v;
            } else if (v > 10) {
                sum = sum + v / 2;
            } else if (v > 0) {
                sum = sum + 1;
            }
        }
        return sum;
    }
}
