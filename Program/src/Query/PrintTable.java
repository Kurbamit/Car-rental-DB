package Query;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrintTable {
    public static void DisplaySearchTable(ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            System.out.println("We should never get here.");
            return;
        }

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Print header
        for (int i = 1; i <= columnCount; i++) {
            System.out.printf("%-20s", metaData.getColumnLabel(i));
        }
        System.out.println();

        // Print rows
        do {
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s", resultSet.getString(i));
            }
            System.out.println();
        } while (resultSet.next());
    }

    public static void DisplayFullTable(ResultSet resultSet) throws SQLException
    {
        int collumnCount = resultSet.getMetaData().getColumnCount();

        for(int i = 1; i <= collumnCount; i++)
        {
            System.out.printf("%-20s", resultSet.getMetaData().getColumnLabel(i));
        }
        System.out.println();

        while(resultSet.next())
        {
            for(int i = 1; i <= collumnCount; i++)
            {
                System.out.printf("%-20s", resultSet.getString(i));
            }
            System.out.println();
        }
    }
}
