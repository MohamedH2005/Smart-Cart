import java.util.HashMap;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class IDandPassword {
    HashMap<String, String> LoginInfo = new HashMap<String, String>();
    HashMap<String, String> roles = new HashMap<>();
    IDandPassword() {
        // Try to load users from file first
        try {
            FileHandler.loadUsers(LoginInfo, roles);
            System.out.println("Loaded " + LoginInfo.size() + " users from file");
        } catch (Exception e) {
            System.out.println("No saved users found, using defaults");
            
            // Default users if file doesn't exist
            // Admin - Full access to all modules
            LoginInfo.put("admin", hashPassword("admin123"));
            roles.put("admin", "Admin");
            
            // Manager - Access to all modules except Admin
            LoginInfo.put("manager", hashPassword("manager123"));
            roles.put("manager", "Manager");
            
            // Marketing - Access to Sales, Reports, Marketing
            LoginInfo.put("marketing", hashPassword("marketing123"));
            roles.put("marketing", "Marketing");
            
            // User - Access to Sales, Profile only
            LoginInfo.put("user", hashPassword("user123"));
            roles.put("user", "User");
            
            // Supervisor - Access to Inventory, Sales, Customers, Reports, Profile
            LoginInfo.put("supervisor", hashPassword("supervisor123"));
            roles.put("supervisor", "Supervisor");
            
            // Cashier - Access to Sales, Profile only
            LoginInfo.put("cashier", hashPassword("cashier123"));
            roles.put("cashier", "Cashier");
            
            // Inventory Manager - Access to Inventory, Profile only
            LoginInfo.put("inventory", hashPassword("inventory123"));
            roles.put("inventory", "Inventory Manager");
            
            // HR Manager - Access to Admin (user management), Profile only
            LoginInfo.put("hr", hashPassword("hr123"));
            roles.put("hr", "HR Manager");
            
            // Accountant - Access to Reports, Profile only
            LoginInfo.put("accountant", hashPassword("accountant123"));
            roles.put("accountant", "Accountant");
            
            // Save default users to file
            FileHandler.saveUsers(LoginInfo, roles);
        }
    }
    public HashMap<String, String> getLoginInfo() { //getter method to return the login information HashMap to Login.java
        return LoginInfo;
    }
    
    public HashMap<String, String> getRoles() {//getter method to return the roles HashMap to Login.java
        return roles;
    }
    
    public String getUserRole(String username) {
        return roles.getOrDefault(username.trim().toLowerCase(), "User");
    }
    
    // Add new user and save to file
    public void addUser(String username, String password, String role) {
        String user = username.trim().toLowerCase();
        if (LoginInfo.containsKey(user)) {
            System.out.println("User already exists: " + user);
            return;
        }
        LoginInfo.put(user, hashPassword(password));
        roles.put(user, role);
        FileHandler.saveUsers(LoginInfo, roles);
    }
    
    // Update user and save to file
    public void updateUser(String username, String role) {
        roles.put(username.trim().toLowerCase(), role);
        FileHandler.saveUsers(LoginInfo, roles);
    }
    
    // Delete user and save to file
    public void deleteUser(String username) {
        LoginInfo.remove(username.trim().toLowerCase());
        roles.remove(username.trim().toLowerCase());
        FileHandler.saveUsers(LoginInfo, roles);
    }
    protected HashMap<String, String> getLoginInfo(String username, String password) {

        if (username == null || password == null ||
                username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(null,
                    "Please enter username and password.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return LoginInfo;}
        
        if (LoginInfo.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Login system is not initialized.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return LoginInfo;}       
        String key = username.trim().toLowerCase();
        String storedPassword = LoginInfo.get(key);
        if (storedPassword == null) {
            JOptionPane.showMessageDialog(null,
                    "User not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else if (storedPassword.equals(hashPassword(password))) {
            String role = roles.getOrDefault(key, "User");
            JOptionPane.showMessageDialog(null,
                    "Login successful - " + role,
                    "Welcome",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Wrong password.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);}        
        return LoginInfo;}    
    public String hashPassword(String password) {
        try {
            java.security.MessageDigest md =
                    java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));}            
            return sb.toString();}
         catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);}}}