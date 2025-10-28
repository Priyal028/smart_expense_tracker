package utils;
import model.Expense;
import model.RecurringExpense;
import model.User;
import java.io.*;
import java.time.LocalDate;
public class FileManager 
{
    private static final String DATA_FOLDER = "data/";
    public static void saveUser(User user) 
    {
        try 
        {
            new File(DATA_FOLDER).mkdirs();
            BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FOLDER + user.getUsername() + ".txt"));
            bw.write(user.getPassword().trim());
            bw.newLine();
            bw.write(String.valueOf(user.getBudgetLimit()));
            bw.newLine();
            for (Expense e : user.getExpenses()) 
            {
                bw.write("E:" + e.toString());
                bw.newLine();
            }
            for (RecurringExpense re : user.getRecurringExpenses()) 
            {
                bw.write("R:" + re.toString());
                bw.newLine();
            }
            bw.close();
        } 
        catch (IOException e) 
        {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }
    public static User loadUser(String username) 
    {
        try 
        {
            File file = new File(DATA_FOLDER + username + ".txt");
            if (!file.exists()) 
            return null;
            BufferedReader br = new BufferedReader(new FileReader(file));
            String password = br.readLine().trim();
            double budget = Double.parseDouble(br.readLine());
            User user = new User(username, password);
            user.setBudgetLimit(budget);
            String line;
            while ((line = br.readLine()) != null) 
            {
                if (line.startsWith("E:")) 
                {
                    String[] parts = line.substring(2).split(",");
                    Expense e = new Expense(parts[0], parts[1], Double.parseDouble(parts[2]), LocalDate.parse(parts[3]));
                    user.addExpense(e);
                } 
                else if (line.startsWith("R:")) 
                {
                    RecurringExpense re = RecurringExpense.fromString(line.substring(2));
                    user.addRecurringExpense(re);
                }
            }
            br.close();
            return user;
        } 
        catch (IOException e) 
        {
            System.out.println("Error loading user: " + e.getMessage());
            return null;
        }
    }
}