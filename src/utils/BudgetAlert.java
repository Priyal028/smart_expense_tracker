package utils;
import model.User;
import model.Expense;
import javax.swing.*;
import java.time.LocalDate;
public class BudgetAlert extends Thread 
{
    private User user;
    private boolean running = true;
    public BudgetAlert(User user) 
    {
        this.user = user;
    }
    public void stopAlert() 
    {
        running = false;
    }
    public void run() 
    {
        while (running) 
        {
            try 
            {
                Thread.sleep(10000);
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
                if (total > user.getBudgetLimit()) 
                {
                    double finalTotal = total;
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null,
                            "⚠️ Budget limit exceeded!\nTotal spent this month: ₹" + finalTotal,
                            "Budget Alert", JOptionPane.WARNING_MESSAGE);
                    });
                }
            } 
            catch (InterruptedException e) 
            {
                System.out.println("Alert thread interrupted.");
            }
        }
    }
}