import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;


public class Login  { 
    IDandPassword auth;
    JFrame LoginFrame = new JFrame();
    JButton LoginButton = new JButton("Login");
    JButton ResetButton = new JButton("Reset");
    JButton SignupButton = new JButton("Signup");
    JTextField UserIDField = new JTextField();
    JPasswordField PasswordField = new JPasswordField();
    JLabel UserIDLabel = new JLabel("User ID:");
    JLabel PasswordLabel = new JLabel("Password:");
    JLabel MessageLabel = new JLabel();
    JLabel titleLabel = new JLabel("SmartCart");
    JOptionPane MessageDialog = new JOptionPane();
    
    HashMap<String, String> LoginInfo = new HashMap<String, String>();//   HashMap to store login information passed from Main.java 
    String currentUser;

    Login(HashMap<String, String> LoginInfoOriginal) {

        this.auth = new IDandPassword();
        this.LoginInfo = LoginInfoOriginal;//    this.LoginInfo = auth.getLoginInfo();
        this.currentUser = "";

        // Title Label
        titleLabel.setBounds(0, 30, 850, 60);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setVerticalAlignment(JLabel.CENTER);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(41, 128, 185, 200));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);

        // Frame
        LoginFrame.setSize(850, 600);
        LoginFrame.setLayout(null);
        LoginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LoginFrame.getContentPane().setBackground(new Color(44, 62, 80));
        LoginFrame.setTitle("SmartCart - Login");
        LoginFrame.setResizable(false);
        LoginFrame.setLocationRelativeTo(null);

        // Labels
        UserIDLabel.setBounds(275, 130, 300, 30);
        UserIDLabel.setForeground(new Color(236, 240, 241));
        UserIDLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        PasswordLabel.setBounds(275, 210, 300, 30);
        PasswordLabel.setForeground(new Color(236, 240, 241));
        PasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        // Fields
        UserIDField.setBounds(275, 160, 300, 45);
        UserIDField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        UserIDField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(52, 152, 219), 2),
                new EmptyBorder(5, 10, 5, 10)
        ));
        UserIDField.setBackground(new Color(236, 240, 241));

        PasswordField.setBounds(275, 240, 300, 45);
        PasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        PasswordField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(52, 152, 219), 2),
                new EmptyBorder(5, 10, 5, 10)
        ));
        PasswordField.setBackground(new Color(236, 240, 241));

        // Login Button
        LoginButton.setBounds(275, 330, 300, 45);
        LoginButton.setFocusable(false);
        LoginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        LoginButton.setBackground(new Color(52, 152, 219));
        LoginButton.setForeground(Color.WHITE);
        LoginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        LoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        LoginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                LoginButton.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(MouseEvent e) {
                LoginButton.setBackground(new Color(52, 152, 219));
            }
        });

        // Reset Button
        ResetButton.setBounds(420, 445, 155, 35);
        ResetButton.setFocusable(false);
        ResetButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        ResetButton.setBackground(new Color(231, 76, 60));
        ResetButton.setForeground(Color.WHITE);
        ResetButton.setBorderPainted(false);
        ResetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Signup Button
        SignupButton.setBounds(275, 445, 140, 35);
        SignupButton.setFocusable(false);
        SignupButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        SignupButton.setBackground(new Color(46, 204, 113));
        SignupButton.setForeground(Color.WHITE);
        SignupButton.setBorderPainted(false);
        SignupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Message Label
        MessageLabel.setBounds(275, 485, 300, 30);
        MessageLabel.setForeground(new Color(231, 76, 60));
        MessageLabel.setHorizontalAlignment(JLabel.CENTER);
        MessageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Add components
        LoginFrame.add(UserIDLabel);
        LoginFrame.add(PasswordLabel);
        LoginFrame.add(UserIDField);
        LoginFrame.add(PasswordField);
        LoginFrame.add(LoginButton);
        LoginFrame.add(ResetButton);
        LoginFrame.add(SignupButton);
        LoginFrame.add(MessageLabel);
        LoginFrame.add(titleLabel);

        // Actions

        LoginButton.addActionListener(new ActionListener() {
            @Override                                                          
            public void actionPerformed(ActionEvent e) {

                String user = UserIDField.getText().trim().toLowerCase();
                String pass = String.valueOf(PasswordField.getPassword());

                // Use the comprehensive getLoginInfo method for validation
                auth.getLoginInfo(user, pass);

                String storedPassword = LoginInfo.get(user);

                if (storedPassword != null && storedPassword.equals(auth.hashPassword(pass))) {
                    // Get user role
                    String userRole = auth.getUserRole(user);
                    currentUser = user;

                    JOptionPane.showMessageDialog(null,
                            "Login Success - " + userRole,
                            "Welcome",
                            JOptionPane.INFORMATION_MESSAGE);
                    LoginFrame.dispose();
                    new Dashboard(LoginInfo, user, userRole);

                } else {
                    MessageLabel.setText("Invalid username or password");
                    UserIDField.setText("");
                    PasswordField.setText("");
                }
            }
        });

        ResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserIDField.setText("");
                MessageLabel.setText("");
                UserIDField.requestFocus();
            }
        });

        SignupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField newUserField = new JTextField();
                JPasswordField newPassField = new JPasswordField();
                Object[] fields = {
                    "Username:", newUserField,
                    "Password:", newPassField
                };
                int option = JOptionPane.showConfirmDialog(null, fields, "Signup", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String newUser = newUserField.getText().trim().toLowerCase();
                    String newPass = String.valueOf(newPassField.getPassword());
                    if (newUser.isEmpty() || newPass.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (LoginInfo.containsKey(newUser)) {
                        JOptionPane.showMessageDialog(null, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        String hashedPassword = auth.hashPassword(newPass);
                        LoginInfo.put(newUser, hashedPassword);
                        auth.getRoles().put(newUser, "User");
                        FileHandler.saveUsers(LoginInfo, auth.getRoles());
                        JOptionPane.showMessageDialog(null, "Signup successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        LoginFrame.setVisible(true);
    }

    public static void main(String[] args) {
        IDandPassword idAndPassword = new IDandPassword();
        new Login(idAndPassword.getLoginInfo());
    }
}
