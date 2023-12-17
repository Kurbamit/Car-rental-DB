package Query;
import java.sql.*;
import java.util.regex.Pattern;

public class Lokacija {
    public static void printLocations(Connection postGresConn)
    {
        if (postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }
        try {
            Statement stmt = postGresConn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doce9051.lokacija");
            PrintTable.DisplayFullTable(rs);
        } catch (SQLException e) {
        }
    }

    public static Boolean checkLocID(String id) {
        // Use a regular expression to match three digits
        String regex = "\\d{3}";
        return Pattern.matches(regex, id);
    }
}
