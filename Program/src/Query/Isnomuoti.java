package Query;
import java.sql.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Isnomuoti {
    private String AK = null;
    private String Numeriai = null;
    private String Vieta = null;
    private String Pradzia = null;
    private String Pabaiga = null;
    Scanner sc = new Scanner(System.in);
    Statement stmt = null;

    public void rentCar(Connection postGresConn) {
        if (postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }
        Klientas.printClients(postGresConn);
        System.out.println("Įveskite kliento asmens kodą:");
        AK = sc.nextLine();

        if (!Klientas.isNumeric(AK) || AK.length() != 11) {
            System.out.println("Klaida! Netinkamas AK formatas.");
            return;
        }

        Automobilis.printCars(postGresConn);
        System.out.println("Įveskite automobilio numerius:");
        Numeriai = sc.nextLine();
        if (!Automobilis.isPlateNumber(Numeriai)) {
            System.out.println("Klaida! Netinkamas numerių formatas.");
            return;
        }

        Lokacija.printLocations(postGresConn);
        System.out.println("Įveskite norimą automobilio vietą (ID):");
        Vieta = sc.nextLine();
        if (!Lokacija.checkLocID(Vieta)) {
            System.out.println("Klaida! Netinkamas vietos formatas.");
            return;
        }

        System.out.println("Įveskite nuomos pradžios datą (YYYY-MM-DD):");
        Pradzia = sc.nextLine();
        if (!isDate(Pradzia)) {
            System.out.println("Klaida! Netinkamas datos formatas.");
            return;
        }

        System.out.println("Įveskite nuomos pabaigos datą (YYYY-MM-DD):");
        Pabaiga = sc.nextLine();
        if (!isDate(Pabaiga)) {
            System.out.println("Klaida! Netinkamas datos formatas.");
            return;
        }

        try {
            postGresConn.setAutoCommit(false); // Start a transaction
            stmt = postGresConn.createStatement();

            // INSERT INTO doce9051.nuoma
            stmt.executeUpdate("INSERT INTO doce9051.nuoma (ak, paimta, grazinta, lokacijosid) VALUES ('" + AK + "', '" + Pradzia + "', '" + Pabaiga + "', '" + Vieta + "')");

            // GET VIN BY NUMERIAI
            String VIN = null;
            stmt = postGresConn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT vin FROM doce9051.automobilis WHERE numeriai = '" + Numeriai + "'");
            while (rs.next()) {
                VIN = rs.getString("vin");
            }

            // INSERT INTO doce9051.Isnomuotas
            stmt = postGresConn.createStatement();
            stmt.executeUpdate("INSERT INTO doce9051.Isnomuotas (nuomosID, VIN) VALUES ((SELECT nuomosID FROM doce9051.nuoma WHERE ak = '" + AK + "' AND paimta = '" + Pradzia + "' AND grazinta = '" + Pabaiga + "' AND lokacijosid = '" + Vieta + "'), '" + VIN + "')");

            postGresConn.commit(); // Commit the transaction
        } catch (SQLException e) {
            try {

                postGresConn.rollback(); // Rollback the transaction if an exception occurs
            } catch (SQLException rollbackException) {
                System.out.println("Failed to rollback transaction: " + rollbackException.getMessage());
            }
            System.out.println("Klaida! Nepavyko įvesti į duomenų bazę.");
            System.out.println(e.getMessage());
            return;
        } finally {
            try {
                postGresConn.setAutoCommit(true); // Reset auto-commit to true
            } catch (SQLException setAutoCommitException) {
                System.out.println("Failed to reset auto-commit: " + setAutoCommitException.getMessage());
            }
        }
        System.out.println("Nuoma sėkmingai pridėta.");
    }

    public void addCarToRent(Connection postGresConn)
    {
        if(postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }
        Nuoma.printRents(postGresConn);
        System.out.println("Įveskite nuomos ID:");
        String nuomosID = sc.nextLine();
        if(!Nuoma.checkRentID(nuomosID)) {
            System.out.println("Klaida! Netinkamas nuomos ID formatas.");
            return;
        }

        Automobilis.printCars(postGresConn);
        System.out.println("Įveskite automobilio VIN:");
        String VIN = sc.nextLine();
        if(!Automobilis.isValidVIN(VIN)) {
            System.out.println("Klaida! Netinkamas VIN formatas.");
            return;
        }

        try{
            stmt = postGresConn.createStatement();
            stmt.executeUpdate("INSERT INTO doce9051.Isnomuotas (nuomosID, VIN) VALUES ('" + nuomosID + "', '" + VIN + "')");
            System.out.println("Automobilis sėkmingai pridėtas prie nuomos: " + nuomosID);
        }catch (SQLException e){
            System.out.println("Klaida! Nepavyko įvesti į duomenų bazę.");
            System.out.println(e.getMessage());
            return;
        }
    }

    public void deleteRent(Connection postGresConn)
    {
        if(postGresConn == null) {
            System.out.println("We should never get here.");
            return;
        }
        Nuoma.printRents(postGresConn);
        System.out.println("Įveskite nuomos ID:");
        String nuomosID = sc.nextLine();
        if(!Nuoma.checkRentID(nuomosID)) {
            System.out.println("Klaida! Netinkamas nuomos ID formatas.");
            return;
        }

        try{
            stmt = postGresConn.createStatement();
            stmt.executeUpdate("DELETE FROM doce9051.Isnomuotas WHERE nuomosID = '" + nuomosID + "'");
            stmt.executeUpdate("DELETE FROM doce9051.nuoma WHERE nuomosID = '" + nuomosID + "'");
            System.out.println("Nuoma sėkmingai ištrinta.");
        }catch (SQLException e){
            System.out.println("Klaida! Nepavyko ištrinti iš duomenų bazės.");
            System.out.println(e.getMessage());
            return;
        }
    }

        private Boolean isDate(String date)
    {
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        return Pattern.matches(regex, date);
    }
}
