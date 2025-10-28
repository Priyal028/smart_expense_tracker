package utils;
import model.Expense;
import model.User;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
public class Predictor 
{
    public static double predictNextMonth(User user) 
    {
        Map<YearMonth, Double> monthlyTotals = new HashMap<>();
        for (Expense e : user.getExpenses()) 
        {
            YearMonth ym = YearMonth.from(e.getDate());
            monthlyTotals.put(ym, monthlyTotals.getOrDefault(ym, 0.0) + e.getAmount());
        }
        if (monthlyTotals.isEmpty()) 
        return 0.0;
        double total = monthlyTotals.values().stream().mapToDouble(Double::doubleValue).sum();
        return total / monthlyTotals.size();
    }
}