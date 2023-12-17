package Query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class Nuoma {
    public static void printRents(Connection postGresConn)
    {
        if (postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }
        try {
            Statement stmt = postGresConn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doce9051.nuoma");
            PrintTable.DisplayFullTable(rs);
        } catch (SQLException e) {
        }
    }

    public void searchRentByID(Connection postGresConn, String id)
    {
        if (postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }
        if (!checkRentID(id)) {
            System.out.println("Neteisingas nuomos ID formatas.");
            return;
        }
        try {
            Statement stmt = postGresConn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doce9051.nuoma WHERE nuomosid = " + id);
            PrintTable.DisplayFullTable(rs);
        } catch (SQLException e) {
        }
    }
    public static Boolean checkRentID(String id) {
        String regex = "\\d{4}";
        return Pattern.matches(regex, id);
    }
}
