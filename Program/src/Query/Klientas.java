package Query;
import java.sql.*;
import java.util.Scanner;
import Query.PrintTable;

public class Klientas {
        String AK = null;
        String Vardas = null;
        String Pavarde = null;
        String Email = null;
        Scanner sc = new Scanner(System.in);
        Statement stmt = null;
        PrintTable printTable = new PrintTable();
    public void addClient(Connection postGresConn)
    {

        if (postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }
        System.out.println("Įveskite kliento AK:");
        AK = sc.nextLine();
        if(!isNumeric(AK) || AK.length() != 11){
            System.out.println("Klaida! Netinkamas AK formatas.");
            return;
        }
        System.out.println("Įveskite kliento vardą:");
        Vardas = sc.nextLine();
        if(!isCharactersOnly(Vardas)){
            System.out.println("Klaida! Netinkamas vardo formatas.");
            return;
        }
        System.out.println("Įveskite kliento pavardę:");
        Pavarde = sc.nextLine();
        if(!isCharactersOnly(Pavarde)){
            System.out.println("Klaida! Netinkamas pavardės formatas.");
            return;
        }
        System.out.println("Įveskite kliento el. paštą:");
        Email = sc.nextLine();
        if(!isEmail(Email)){
            System.out.println("Klaida! Netinkamas el. pašto formatas.");
            return;
        }

        if (AK == null || Vardas == null || Pavarde == null || Email == null) {
            System.out.println("Klaida! Neįvesti visi kliento duomenys.");
            return;
        }

        try {
            stmt = postGresConn.createStatement();
            stmt.executeUpdate("INSERT INTO doce9051.Klientas VALUES ('" + AK + "', '" + Vardas + "', '" + Pavarde + "', '" + Email + "')");
            System.out.println("Klientas pridėtas sėkmingai.");
        } catch (SQLException e) {
            System.out.println("SQL Error!");
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

    public void deleteClient(Connection postGresConn)
    {
        if (postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }
        try {
            PrintTable.DisplayFullTable(getClients(postGresConn));
        } catch (SQLException e) {
            System.out.println("SQL Error!" + e.getMessage());
        }
        System.out.println("Įveskite kliento AK:");
        AK = sc.nextLine();
        if(!isNumeric(AK) || AK.length() != 11){
            System.out.println("Klaida! Netinkamas AK formatas.");
            return;
        }
        try {
            stmt = postGresConn.createStatement();
            stmt.executeUpdate("DELETE FROM doce9051.Klientas WHERE AK = '" + AK + "'");
            System.out.println("Klientas pašalintas sėkmingai.");
        } catch (SQLException e) {
            System.out.println("SQL Error!");
            e.printStackTrace();
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

    public void updateClient(Connection postGresCon)
    {
        if (postGresCon == null) {
            System.out.println("We should never get here.");
            return;
        }
        try {
            PrintTable.DisplayFullTable(getClients(postGresCon));
        } catch (SQLException e) {
            System.out.println("SQL Error!" + e.getMessage());
        }
        System.out.println("Įveskite kliento AK:");
        AK = sc.nextLine();
        if(!isNumeric(AK) || AK.length() != 11){
            System.out.println("Klaida! Netinkamas AK formatas.");
            return;
        }
        System.out.println("Įveskite kliento vardą:");
        Vardas = sc.nextLine();
        if(!isCharactersOnly(Vardas)){
            System.out.println("Klaida! Netinkamas vardo formatas.");
            return;
        }
        System.out.println("Įveskite kliento pavardę:");
        Pavarde = sc.nextLine();
        if(!isCharactersOnly(Pavarde)){
            System.out.println("Klaida! Netinkamas pavardės formatas.");
            return;
        }
        System.out.println("Įveskite kliento el. paštą:");
        Email = sc.nextLine();
        if(!isEmail(Email)){
            System.out.println("Klaida! Netinkamas el. pašto formatas.");
            return;
        }

        if (AK == null || Vardas == null || Pavarde == null || Email == null) {
            System.out.println("Klaida! Neįvesti visi kliento duomenys.");
            return;
        }

        try {
            stmt = postGresCon.createStatement();
            stmt.executeUpdate("UPDATE doce9051.Klientas SET Vardas = '" + Vardas + "', Pavarde = '" + Pavarde + "', Email = '" + Email + "' WHERE AK = '" + AK + "'");
            System.out.println("Klientas atnaujintas sėkmingai.");
        } catch (SQLException e) {
            System.out.println("SQL Error!");
            e.printStackTrace();
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
// *********************************************************************************************************************
    public void searchClientsByName(Connection postGresCon, String name)
    {
        if (postGresCon == null) {
            System.out.println("We should never get here.");
            return;
        }
        if (name == null || !isCharactersOnly(name)) {
            System.out.println("Klaida! Netinkamas vardo formatas.");
            return;
        }

        try {
            Statement stmt = postGresCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doce9051.Klientas WHERE Vardas = '" + name + "'");
            boolean found = false;

            if(rs.next()){
                found = true;
            }
            PrintTable.DisplaySearchTable(rs);
            if(!found){
                System.out.println("Klientas vardu: " + name + " nerastas.");
            }
        }catch (SQLException e) {
        }
    }

    public void searchClientsBySurname(Connection postGresCon, String surname)
    {
        if (postGresCon == null) {
            System.out.println("We should never get here.");
            return;
        }
        if (surname == null || !isCharactersOnly(surname)) {
            System.out.println("Klaida! Netinkamas pavardės formatas.");
            return;
        }

        try {
            Statement stmt = postGresCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doce9051.Klientas WHERE Pavarde = '" + surname + "'");
            boolean found = false;

            if(rs.next()){
                found = true;
            }
            PrintTable.DisplaySearchTable(rs);
            if(!found){
                System.out.println("Klientas pavarde " + surname + " nerastas.");
            }
        }catch (SQLException e) {
        }
    }

    public void searchClientsByEmail(Connection postGresCon, String email)
    {
        if (postGresCon == null) {
            System.out.println("We should never get here.");
            return;
        }
        if (email == null || !isEmail(email)) {
            System.out.println("Klaida! Netinkamas el. pašto formatas.");
            return;
        }

        try {
            Statement stmt = postGresCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doce9051.Klientas WHERE Email = '" + email + "'");
            boolean found = false;

            if(rs.next()){
                found = true;
            }
            PrintTable.DisplaySearchTable(rs);
            if(!found){
                System.out.println("Klientas el. paštu " + email + " nerastas.");
            }
        }catch (SQLException e) {
        }
    }

    public void searchClientsByCode(Connection postGresCon, String code)
    {
        if (postGresCon == null) {
            System.out.println("We should never get here.");
            return;
        }
        if (code.length() != 11 || !isNumeric(code)) {
            System.out.println("Klaida! Netinkamas AK formatas.");
            return;
        }

        try {
            Statement stmt = postGresCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doce9051.Klientas WHERE AK = '" + code + "'");
            boolean found = false;

            if(rs.next()){
                found = true;
            }
            PrintTable.DisplaySearchTable(rs);
            if(!found){
                System.out.println("Klientas AK " + code + " nerastas.");
            }
        }catch (SQLException e) {
        }
    }

    public static void printClients(Connection postGresConn)
    {
        if (postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }
        try {
            Statement stmt = postGresConn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doce9051.Klientas");
            PrintTable.DisplayFullTable(rs);
        }catch (SQLException e) {
        }
    }

    //*********************************************************************************************************************
    private ResultSet getClients(Connection postGresCon)
    {
        try{
            Statement stmt = postGresCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doce9051.Klientas");
            return rs;
        }catch (SQLException e) {
            System.out.println("SQL Error!" + e.getMessage());
        }finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException exp) {
                System.out.println("Unexpected SQL Error!");
                exp.printStackTrace();
            }
        }
        return null;
    }

    public void printClientRents(Connection postGresCon)
    {
        if (postGresCon == null) {
            System.out.println("We should never get here.");
            return;
        }
        try {
            Statement stmt = postGresCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT nuomosid, ak, vardas, pavarde, paimta, grazinta, miestas FROM doce9051.KlientuNuomos");
            PrintTable.DisplayFullTable(rs);
        }catch (SQLException e) {
        }
    }

    private void printClientSearch(ResultSet rs)
    {
        System.out.printf("%-20s%-20s%-20s%-20s\n", "AK", "Vardas", "Pavarde", "Email");
            try {
            while (rs.next()) {
                System.out.printf("%-20s%-20s%-20s%-20s\n", rs.getString("AK"), rs.getString("Vardas"), rs.getString("Pavarde"), rs.getString("Email"));
            }
        } catch (SQLException e) {
            System.out.println("SQL Error!" + e.getMessage());
        }
    }

    public static Boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
        } catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    private Boolean isCharactersOnly(String str) {
        return str.matches("[a-zA-Z]+");
    }
    private Boolean isEmail(String str) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return str.matches(emailRegex);
    }
}
