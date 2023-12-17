package Query;
import java.sql.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Automobilis {
    String VIN = null;
    String Marke = null;
    String Modelis = null;
    String Metai = null;
    String Numeriai = null;
    String Kaina = null;
    Scanner sc = new Scanner(System.in);
    Statement stmt = null;

    public void addCar(Connection postGresConn)
    {
        if (postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }
        System.out.println("Įveskite automobilio VIN kodą:");
        VIN = sc.nextLine();
        if(!isValidVIN(VIN) || VIN.length() != 17){
            System.out.println("Klaida! Netinkamas VIN formatas.");
            return;
        }
        System.out.println("Įveskite automobilio markę:");
        Marke = sc.nextLine();
        if(!isCharactersOnly(Marke)){
            System.out.println("Klaida! Netinkamas markės formatas.");
            return;
        }
        System.out.println("Įveskite automobilio modelį:");
        Modelis = sc.nextLine();
        if(!isCharactersAndNumbersOnly(Modelis)){
            System.out.println("Klaida! Netinkamas modelio formatas.");
            return;
        }
        System.out.println("Įveskite automobilio numerius:");
        Numeriai = sc.nextLine();
        if(!isPlateNumber(Numeriai)){
            System.out.println("Klaida! Netinkamas numerių formatas.");
            return;
        }
        System.out.println("Įveskite automobilio gamybos metus:");
        Metai = sc.nextLine();
        if(!isValidYear(Metai)){
            System.out.println("Klaida! Netinkamas metų formatas.");
            return;
        }
        System.out.println("Įveskite automobilio nuomos kainą:");
        Kaina = sc.nextLine();
        if(!isValidPrice(Kaina)){
            System.out.println("Klaida! Netinkamas kainos formatas.");
            return;
        }

        if (VIN == null || Modelis == null || Marke == null || Numeriai == null || Metai == null || Kaina == null) {
            System.out.println("Klaida! Neįvesti visi automobilio duomenys duomenys.");
            return;
        }

        try {
            stmt = postGresConn.createStatement();
            stmt.executeUpdate("INSERT INTO doce9051.automobilis VALUES ('" + VIN + "', '" + Marke + "', '" + Modelis + "', '" + Metai + "', '" + Numeriai + "', '" + Kaina + "')");
            System.out.println("Automobilis pridėtas sėkmingai.");
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException exp) {
                System.out.println("Unexpected SQL Error!");
                exp.printStackTrace();
            }
        }
    }

    public void deleteCar(Connection postGresConn)
    {
        if (postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }
        printCars(postGresConn);
        System.out.println("Įveskite automobilio valstybinius numerius kodą:");
        Numeriai = sc.nextLine();
        if(!isPlateNumber(Numeriai)){
            System.out.println("Klaida! Netinkamas numerių formatas.");
            return;
        }
        try {
            stmt = postGresConn.createStatement();
            stmt.executeUpdate("DELETE FROM doce9051.automobilis WHERE numeriai = '" + Numeriai + "'");
            System.out.println("Automobilis ištrintas sėkmingai.");
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException exp) {
                System.out.println("Unexpected SQL Error!");
                exp.printStackTrace();
            }
        }
    }

    public void updateCar(Connection postGresConn)
    {
        if (postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }
        printCars(postGresConn);
        System.out.println("Įveskite automobilio VIN kodą:");
        VIN = sc.nextLine();
        if(!isValidVIN(VIN)){
            System.out.println("Klaida! Netinkamas numerių formatas.");
            return;
        }
        System.out.println("Įveskite automobilio valstybinius numerius:");
        Numeriai = sc.nextLine();
        if(!isPlateNumber(Numeriai)){
            System.out.println("Klaida! Netinkamas numerių formatas.");
            return;
        }
        System.out.println("Įveskite automobilio markę:");
        Marke = sc.nextLine();
        if(!isCharactersOnly(Marke)){
            System.out.println("Klaida! Netinkamas markės formatas.");
            return;
        }
        System.out.println("Įveskite automobilio modelį:");
        Modelis = sc.nextLine();
        if(!isCharactersAndNumbersOnly(Modelis)){
            System.out.println("Klaida! Netinkamas modelio formatas.");
            return;
        }
        System.out.println("Įveskite automobilio gamybos metus:");
        Metai = sc.nextLine();
        if(!isValidYear(Metai)){
            System.out.println("Klaida! Netinkamas metų formatas.");
            return;
        }
        System.out.println("Įveskite automobilio nuomos kainą:");
        Kaina = sc.nextLine();
        if(!isValidPrice(Kaina)){
            System.out.println("Klaida! Netinkamas kainos formatas.");
            return;
        }

        if (VIN == null || Modelis == null || Marke == null || Numeriai == null || Metai == null || Kaina == null) {
            System.out.println("Klaida! Neįvesti visi automobilio duomenys duomenys.");
            return;
        }

        try {
            stmt = postGresConn.createStatement();
            String updateQuery = "UPDATE doce9051.automobilis SET VIN = '" + VIN + "', Marke = '" + Marke + "', Modelis = '" + Modelis + "', Metai = '" + Metai + "', Numeriai = '" + Numeriai + "', Kaina = '" + Kaina + "' WHERE VIN = '" + VIN + "'";
            stmt.executeUpdate(updateQuery);
            System.out.println("Automobilis atnaujintas sėkmingai.");
        }catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    //**********************************************************************************************************************
    public void searchCarByVIN(Connection postGresCon, String VIN) {
        if (postGresCon == null) {
            System.out.println("We should never get here.");
            return;
        }
        if (VIN.length() != 17 || !isValidVIN(VIN)) {
            System.out.println("Klaida! Netinkamas VIN formatas. " + VIN);
            return;
        }
        try {
            Statement stmt = postGresCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doce9051.automobilis WHERE VIN = '" + VIN + "'");
            boolean found = false;
            if (rs.next()) {
                found = true;
            }
            PrintTable.DisplaySearchTable(rs);
            if (!found) {
                System.out.println("Automobilis su VIN: " + VIN + " nerastas.");
            }
        } catch (SQLException e) {
        }
    }

    public void searchCarByMake(Connection postGresCon, String make) {
        if (postGresCon == null) {
            System.out.println("We should never get here.");
            return;
        }
        if (!isCharactersOnly(make)) {
            System.out.println("Klaida! Markėje gali būti tik raidės. " + make);
            return;
        }
        try {
            Statement stmt = postGresCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doce9051.automobilis WHERE UPPER(Marke) = UPPER('" + make + "')");
            boolean found = false;
            if (rs.next()) {
                found = true;
            }
            PrintTable.DisplaySearchTable(rs);
            if (!found) {
                System.out.println("Automobilis su markė: " + make + " nerastas.");
            }
        } catch (SQLException e) {
        }
    }

    public void searchCarByModel(Connection postGresConn, String model) {
        if (postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }
        if (!isCharactersAndNumbersOnly(model)) {
            System.out.println("Klaida! Netinkamas modelio formatas: " + model);
            return;
        }
        try {
            Statement stmt = postGresConn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doce9051.automobilis WHERE UPPER(Modelis) = UPPER('" + model + "')");
            boolean found = false;
            if (rs.next()) {
                found = true;
            }
            PrintTable.DisplaySearchTable(rs);
            if (!found) {
                System.out.println("Automobilis su modeliu: " + model + " nerastas.");
            }
        } catch (SQLException e) {
        }
    }

    public void searchCarByNumber(Connection postGresConn, String number) {
        if (postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }
        if (!isPlateNumber(number)) {
            System.out.println("Klaida! Netinkamas numerių formatas: " + number);
            return;
        }
        try {
            Statement stmt = postGresConn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doce9051.automobilis WHERE UPPER(Numeriai) = UPPER('" + number + "')");
            boolean found = false;
            if (rs.next()) {
                found = true;
            }
            PrintTable.DisplaySearchTable(rs);
            if (!found) {
                System.out.println("Automobilis su numeriais: " + number + " nerastas.");
            }
        } catch (SQLException e) {
        }
    }

    public void searchCarByYear(Connection postGresConn, String year)
    {
        if (postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }
        if (!isValidYear(year)) {
            System.out.println("Klaida! Netinkamas metų formatas: " + year);
            return;
        }
        try {
            Statement stmt = postGresConn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doce9051.automobilis WHERE metai = '" + year + "'");
            boolean found = false;
            if (rs.next()) {
                found = true;
            }
            PrintTable.DisplaySearchTable(rs);
            if (!found) {
                System.out.println("Automobilis kurio pagaminimo metaiL: " + year + " nerastas.");
            }
        } catch (SQLException e) {
        }
    }

    public static void printCars(Connection postGresConn) {
        if (postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }
        try {
            Statement stmt = postGresConn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doce9051.automobilis");
            PrintTable.DisplayFullTable(rs);
        } catch (SQLException e) {
        }
    }

    public static boolean isValidVIN(String vin) {
        // Regular expression for VIN validation
        String vinRegex = "^[A-HJ-NPR-Z0-9]{17}$";

        // Compile the pattern
        Pattern pattern = Pattern.compile(vinRegex);

        // Check if the VIN matches the pattern
        return pattern.matcher(vin).matches();
    }

    private Boolean isCharactersOnly(String str) {
        return str.matches("[a-zA-Z]+");
    }

    private Boolean isCharactersAndNumbersOnly(String str) {
        return str.matches("[a-zA-Z0-9]+");
    }

    public static Boolean isPlateNumber(String str) {
        return str.matches("[A-Za-z]{3}\\d{3}");
    }

    private Boolean isValidYear(String str) {
        return str.matches("[0-9]{4}");
    }
    private Boolean isValidPrice(String str) {
        try {
            double price = Double.parseDouble(str);
            return price >= 0 && price <= 1000;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
