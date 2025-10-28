package gui;
import model.RecurringExpense;
import model.User;
import utils.FileManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
public class AddRecurringExpenseForm extends JFrame 
{
    public AddRecurringExpenseForm(User user, Dashboard dashboard) 
    {
        setTitle("Add Recurring Expense");
        setSize(420, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 245, 250));
        setLayout(new BorderLayout());
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 18);
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20),
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1)
        ));
        JLabel titleLabel = new JLabel("Add Recurring Expense", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(0, 122, 204));
        card.add(titleLabel, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        JTextField titleField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField startDateField = new JTextField(LocalDate.now().toString());
        JComboBox<String> frequencyBox = new JComboBox<>(new String[]{"Monthly", "Weekly", "Daily"});
        titleField.setFont(fieldFont);
        categoryField.setFont(fieldFont);
        amountField.setFont(fieldFont);
        startDateField.setFont(fieldFont);
        frequencyBox.setFont(fieldFont);
        formPanel.add(new JLabel("Title:")); formPanel.add(titleField);
        formPanel.add(new JLabel("Category:")); formPanel.add(categoryField);
        formPanel.add(new JLabel("Amount (₹):")); formPanel.add(amountField);
        formPanel.add(new JLabel("Start Date (YYYY-MM-DD):")); formPanel.add(startDateField);
        formPanel.add(new JLabel("Frequency:")); formPanel.add(frequencyBox);
        card.add(formPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        styleButton(saveBtn);
        styleButton(cancelBtn, Color.GRAY);
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        card.add(buttonPanel, BorderLayout.SOUTH);
        add(card, BorderLayout.CENTER);
        saveBtn.addActionListener(e -> {
            try 
            {
                String title = titleField.getText().trim();
                String category = categoryField.getText().trim();
                double amount = Double.parseDouble(amountField.getText().trim());
                LocalDate startDate = LocalDate.parse(startDateField.getText().trim());
                String frequency = (String) frequencyBox.getSelectedItem();
                if (title.isEmpty() || category.isEmpty() || amount < 0) 
                {
                    JOptionPane.showMessageDialog(this, "Please enter valid details.");
                    return;
                }
                RecurringExpense re = new RecurringExpense(title, category, amount, startDate, frequency);
                user.addRecurringExpense(re);
                FileManager.saveUser(user);
                JOptionPane.showMessageDialog(this, "Recurring expense saved!");
                dashboard.showMonthlyReport();
                dispose();
            } 
            catch (Exception ex) 
            {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        cancelBtn.addActionListener(e -> dispose());
        setVisible(true);
    }
    private void styleButton(JButton button) 
    {
        styleButton(button, new Color(0, 122, 204));
    }
    private void styleButton(JButton button, Color bgColor) 
    {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}