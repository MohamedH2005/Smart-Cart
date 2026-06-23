import java.io.*;
import java.util.*;
public class FileHandler {
    public static void saveUsers(HashMap<String, String> loginInfo, HashMap<String, String> roles) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("users.txt"))) { // overwrite file
            for (String user : loginInfo.keySet())
                pw.println(user + "," + loginInfo.get(user) + "," + roles.getOrDefault(user, "User"));
        } catch (IOException e) {
            System.err.println("Error saving users");
        }
    }

    public static void loadUsers(HashMap<String, String> loginInfo, HashMap<String, String> roles) {
        File file = new File("users.txt");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    loginInfo.put(parts[0], parts[1]);
                    roles.put(parts[0], parts[2]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users");
        }
    }

    public static void saveProducts(ArrayList<String[]> products) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("products.txt"))) {
            for (String[] p : products)
                pw.println(String.join(",", p));
        } catch (IOException e) {
            System.err.println("Error saving products");
        }
    }

    public static ArrayList<String[]> loadProducts() {
        ArrayList<String[]> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("products.txt"))) {
            String line;
            while ((line = br.readLine()) != null)
                list.add(line.split(","));
        } catch (IOException e) {
            System.err.println("Error loading products");
        }
        return list;
    }

    public static void saveCustomers(ArrayList<String[]> customers) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("customers.txt"))) {
            for (String[] c : customers)
                pw.println(String.join(",", c));
        } catch (IOException e) {
            System.err.println("Error saving customers");
        }
    }

    public static ArrayList<String[]> loadCustomers() {
        ArrayList<String[]> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = br.readLine()) != null)
                list.add(line.split(","));
        } catch (IOException e) {
            System.err.println("Error loading customers");
        }
        return list;
    }

    public static void saveSale(String orderId, double total, String date, String status) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("sales.txt", true))) {
            pw.println(orderId + "," + total + "," + date + "," + status);
        } catch (IOException e) {
            System.err.println("Error saving sale");
        }
    }

    public static ArrayList<String[]> loadSales() {
        ArrayList<String[]> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("sales.txt"))) {
            String line;
            while ((line = br.readLine()) != null)
                list.add(line.split(","));
        } catch (IOException e) {
            System.err.println("Error loading sales");
        }
        return list;
    }

    public static void appendLog(String message) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("logs.txt", true))) {
            pw.println(java.time.LocalDateTime.now() + " - " + message);
        } catch (IOException e) {
            System.err.println("Error writing log");
        }
    }
}