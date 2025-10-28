import model.User;
import utils.FileManager;
import gui.Dashboard;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
public class Main 
{
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(Main::showLoginScreen);
    }
    private static void showLoginScreen() 
    {
        JFrame frame = new JFrame("Smart Expense Tracker");
        frame.setSize(420, 360);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(false);
        frame.getContentPane().setBackground(new Color(240, 245, 250));
        frame.setLayout(new BorderLayout());
        Font titleFont = new Font("Segoe UI", Font.BOLD, 20);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20),
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1)
        ));
        JLabel titleLabel = new JLabel("Login or Register", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(0, 122, 204));
        card.add(titleLabel, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setBackground(Color.WHITE);
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        usernameField.setFont(fieldFont);
        passwordField.setFont(fieldFont);
        formPanel.add(new JLabel("Username:", SwingConstants.LEFT));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:", SwingConstants.LEFT));
        formPanel.add(passwordField);
        card.add(formPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");
        styleButton(loginBtn);
        styleButton(registerBtn);
        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        card.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(card, BorderLayout.CENTER);
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            User user = FileManager.loadUser(username);
            if (user != null && user.getPassword().equals(password)) 
            {
                frame.dispose();
                new Dashboard(user);
            } 
            else 
            {
                JOptionPane.showMessageDialog(frame, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        registerBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            File file = new File("data/" + username + ".txt");
            if (file.exists()) 
            {
                JOptionPane.showMessageDialog(frame, "User already exists.", "Registration Failed", JOptionPane.WARNING_MESSAGE);
            } 
            else 
            {
                User newUser = new User(username, password);
                FileManager.saveUser(newUser);
                JOptionPane.showMessageDialog(frame, "Registration successful!", "Welcome", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        frame.setVisible(true);
    }
    private static void styleButton(JButton button) 
    {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(new Color(0, 122, 204));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}