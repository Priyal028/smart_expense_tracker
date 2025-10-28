package model;
import java.util.*;
import java.time.LocalDate;
public class User 
{
    private String username;
    private String password;
    private double budgetLimit;
    private List<Expense> expenses = new ArrayList<>();
    private List<RecurringExpense> recurringExpenses = new ArrayList<>();
    public User(String username, String password) 
    {
        this.username = username;
        this.password = password;
        this.budgetLimit = 0;
    }
    public String getUsername() 
    { 
        return username; 
    }
    public String getPassword() 
    { 
        return password; 
    }
    public double getBudgetLimit() 
    { 
        return budgetLimit; 
    }
    public void setBudgetLimit(double limit) 
    { 
        this.budgetLimit = limit; 
    }
    public void addExpense(Expense e) 
    { 
        expenses.add(e); 
    }
    public List<Expense> getExpenses() 
    { 
        return expenses; 
    }
    public void addRecurringExpense(RecurringExpense re) 
    { 
        recurringExpenses.add(re); 
    }
    public List<RecurringExpense> getRecurringExpenses() 
    { 
        return recurringExpenses; 
    }
    public List<Expense> getRecurringExpensesForMonth(LocalDate date) 
    {
        List<Expense> generated = new ArrayList<>();
        for (RecurringExpense re : recurringExpenses) 
        {
            if (re.occursOn(date)) 
            {
                generated.add(re.generateForDate(date));
            }
        }
        return generated;
    }
    public double getTotalSpent() 
    {
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }
    public boolean isOverBudget() 
    {
        return getTotalSpent() > budgetLimit;
    }
}