package gui;
import model.User;
import model.Expense;
import utils.FileManager;
import utils.BudgetAlert;
import utils.Predictor;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class Dashboard extends JFrame 
{
    private User user;
    private JTextArea displayArea;
    private BudgetAlert alertThread;
    public Dashboard(User user) 
    {
        this.user = user;
        setTitle("Smart Expense Tracker");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 245, 250));
        Font headingFont = new Font("Segoe UI", Font.BOLD, 20);
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 14);
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getUsername(), SwingConstants.CENTER);
        welcomeLabel.setFont(headingFont);
        welcomeLabel.setForeground(new Color(0, 122, 204));
        welcomeLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(welcomeLabel, BorderLayout.NORTH);
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        displayArea.setBackground(Color.WHITE);
        displayArea.setForeground(Color.DARK_GRAY);
        displayArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 12, 12));
        buttonPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        buttonPanel.setBackground(new Color(240, 245, 250));
        JButton addBtn = new JButton("Add Expense");
        JButton viewBtn = new JButton("View All Expenses");
        JButton filterBtn = new JButton("Filter by Category");
        JButton reportBtn = new JButton("Monthly Report");
        JButton budgetBtn = new JButton("Set Budget");
        JButton predictBtn = new JButton("Predict Next Month");
        JButton recurringBtn = new JButton("Add Recurring Expense");
        JButton logoutBtn = new JButton("Logout");
        JButton[] buttons = {addBtn, viewBtn, filterBtn, reportBtn, budgetBtn, predictBtn, recurringBtn, logoutBtn};
        for (JButton b : buttons) styleButton(b, buttonFont);
        for (JButton b : buttons) buttonPanel.add(b);
        add(buttonPanel, BorderLayout.SOUTH);
        addBtn.addActionListener(e -> new AddExpenseForm(user, this));
        viewBtn.addActionListener(e -> showAllExpenses());
        filterBtn.addActionListener(e -> filterByCategory());
        reportBtn.addActionListener(e -> showMonthlyReport());
        budgetBtn.addActionListener(e -> setBudget());
        predictBtn.addActionListener(e -> {
            double prediction = Predictor.predictNextMonth(user);
            displayArea.setText("📈 Predicted Expense for Next Month: ₹" + String.format("%.2f", prediction));
        });
        recurringBtn.addActionListener(e -> new AddRecurringExpenseForm(user, this));
        logoutBtn.addActionListener(e -> {
            alertThread.stopAlert();
            FileManager.saveUser(user);
            dispose();
            System.exit(0);
        });
        alertThread = new BudgetAlert(user);
        alertThread.start();
        setVisible(true);
    }
    private void styleButton(JButton button, Font font) 
    {
        button.setFont(font);
        button.setBackground(new Color(0, 122, 204));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    public void showAllExpenses()
    {
        StringBuilder sb = new StringBuilder("All Expenses (This Month):\n");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy");
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();
        for (Expense e : user.getExpenses()) 
        {
            if (e.getDate().getMonthValue() == currentMonth && e.getDate().getYear() == currentYear) 
            {
                sb.append(e.getTitle()).append(" | ")
                  .append(e.getCategory()).append(" | ₹")
                  .append(e.getAmount()).append(" | ")
                  .append(e.getDate().format(fmt)).append("\n");
            }
        }
        for (Expense e : user.getRecurringExpensesForMonth(today)) 
        {
            sb.append(e.getTitle()).append(" | ")
              .append(e.getCategory()).append(" | ₹")
              .append(e.getAmount()).append(" | ")
              .append(e.getDate().format(fmt)).append(" (Recurring)\n");
        }
        displayArea.setText(sb.toString());
    }
    private void filterByCategory() 
    {
        String category = JOptionPane.showInputDialog(this, "Enter category to filter:");
        if (category == null || category.isEmpty()) 
        return;
        StringBuilder sb = new StringBuilder("Expenses in category: " + category + "\n");
        for (Expense e : user.getExpenses()) 
        {
            if (e.getCategory().equalsIgnoreCase(category)) 
            {
                sb.append(e.getTitle()).append(" | ₹")
                  .append(e.getAmount()).append(" | ")
                  .append(e.getDate()).append("\n");
            }
        }
        displayArea.setText(sb.toString());
    }
    public void showMonthlyReport() 
    {
        StringBuilder sb = new StringBuilder("Monthly Report:\n");
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();
        double total = user.getExpenses().stream()
            .filter(e -> e.getDate().getMonthValue() == currentMonth && e.getDate().getYear() == currentYear)
            .mapToDouble(Expense::getAmount)
            .sum();
        for (Expense e : user.getRecurringExpensesForMonth(today)) 
        {
            total += e.getAmount();
        }
        sb.append("Total Spent This Month: ₹").append(total).append("\n");
        sb.append("Budget Limit: ₹").append(user.getBudgetLimit()).append("\n");
        sb.append("Status: ").append(total > user.getBudgetLimit() ? "Over Budget!" : "Within Budget").append("\n");
        displayArea.setText(sb.toString());
    }
    private void setBudget() 
    {
        String input = JOptionPane.showInputDialog(this, "Enter new budget limit:");
        try 
        {
            double newLimit = Double.parseDouble(input);
            user.setBudgetLimit(newLimit);
            FileManager.saveUser(user);
            JOptionPane.showMessageDialog(this, "Budget updated!");
        } 
        catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }
}