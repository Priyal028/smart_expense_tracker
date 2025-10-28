package model;
import java.time.LocalDate;
public class RecurringExpense 
{
    private String title;
    private String category;
    private double amount;
    private LocalDate startDate;
    private String frequency;
    public RecurringExpense(String title, String category, double amount, LocalDate startDate, String frequency) 
    {
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.startDate = startDate;
        this.frequency = frequency;
    }
    public Expense generateForDate(LocalDate date) 
    {
        return new Expense(title, category, amount, date);
    }
    public boolean occursOn(LocalDate date) 
    {
        if (date.isBefore(startDate)) 
        return false;
        switch (frequency) 
        {
            case "Daily": return true;
            case "Weekly": return startDate.until(date).getDays() % 7 == 0;
            case "Monthly": return date.getDayOfMonth() == startDate.getDayOfMonth();
            default: return false;
        }
    }
    public String getTitle() 
    { 
        return title; 
    }
    public String getCategory() 
    { 
        return category; 
    }
    public double getAmount() 
    { 
        return amount; 
    }
    public LocalDate getStartDate() 
    { 
        return startDate; 
    }
    public String getFrequency() 
    { 
        return frequency; 
    }
    public String toString() 
    {
        return title + "," + category + "," + amount + "," + startDate + "," + frequency;
    }
    public static RecurringExpense fromString(String line) 
    {
        String[] parts = line.split(",");
        return new RecurringExpense(
            parts[0],
            parts[1],
            Double.parseDouble(parts[2]),
            LocalDate.parse(parts[3]),
            parts[4]
        );
    }
}