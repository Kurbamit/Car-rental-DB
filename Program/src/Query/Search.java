package Query;

import java.sql.*;
import java.util.Scanner;

public class Search {

    Connection _connection = null;
    Statement stmt = null;
    ResultSet rs = null;
    private int choice = 0;
    private String input = null;
    Klientas _klientas = new Klientas();
    Automobilis _automobilis = new Automobilis();
    Nuoma _nuoma = new Nuoma();
    public Search(Connection connection){
        _connection = connection;
    }
    public void start()
    {
        choice = 0;
        printSearch();
        while (choice != -1) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Įveskite norimą veiksmą:");

            input = sc.nextLine();
            while(isNumeric(input)){
                System.out.println("Tokio pasirinkimo nėra.");
                System.out.println("Įveskite norimą veiksmą:");
                input = sc.nextLine();
            }
            choice = Integer.parseInt(input);

            switch (choice){
                case 1:
                    String name = null;
                    System.out.println("Įveskite kliento vardą:");
                    name = sc.nextLine();
                    _klientas.searchClientsByName(_connection, name);
                    break;
                case 2:
                    String surname = null;
                    System.out.println("Įveskite kliento pavardę:");
                    surname = sc.nextLine();
                    _klientas.searchClientsBySurname(_connection, surname);
                    break;
                case 3:
                    String email = null;
                    System.out.println("Įveskite kliento email:");
                    email = sc.nextLine();
                    _klientas.searchClientsByEmail(_connection, email);
                    break;
                case 4:
                    String ak = null;
                    System.out.println("Įveskite kliento ak:");
                    ak = sc.nextLine();
                    _klientas.searchClientsByCode(_connection, ak);
                    break;
                case 5:
                    String make = null;
                    System.out.println("Įveskite automobilio markę:");
                    make = sc.nextLine();
                    _automobilis.searchCarByMake(_connection, make);
                    break;
                case 6:
                    String model = null;
                    System.out.println("Įveskite automobilio modelį:");
                    model = sc.nextLine();
                    _automobilis.searchCarByModel(_connection, model);
                    break;
                case 7:
                    String number = null;
                    System.out.println("Įveskite automobilio numerius:");
                    number = sc.nextLine();
                    _automobilis.searchCarByNumber(_connection, number);
                    break;
                case 8:
                    String year = null;
                    System.out.println("Įveskite automobilio gamybos metus:");
                    year = sc.nextLine();
                    _automobilis.searchCarByYear(_connection, year);
                    break;
                case 9:
                    String vin = null;
                    System.out.println("Įveskite automobilio VIN:");
                    vin = sc.nextLine();
                    _automobilis.searchCarByVIN(_connection, vin);
                    break;
                case 10:
                    String id = null;
                    System.out.println("Įveskite nuomos ID:");
                    id = sc.nextLine();
                    _nuoma.searchRentByID(_connection, id);
                    break;
                case 0:
                    printSearch();
                    break;
                case -1:
                    System.out.println("Paieška baigta.");
                    break;
                default:
                    System.out.println("Tokio pasirinkimo nėra.");
                    break;
            }
        }
    }
    public void printSearch()
    {
        System.out.println("---------------------------------------------------------------------------------------------------");
        System.out.println("| 1. Paieška pagal kliento vardą.                | 2. Paieška pagal kliento pavardę.              |");
        System.out.println("| 3. Paieška pagal kliento email.                | 4. Paieška pagal kliento ak.                   |");
        System.out.println("| 5. Paieška pagal automobilio markę.            | 6. Paieška pagal automobilio modelį.           |");
        System.out.println("| 7. Paieška pagal automobilio numerius.         | 8. Paieška pagal automobilio gamybos metus.    |");
        System.out.println("| 9. Paieška pagal automobilio VIN.              | 10. Paieška pagal nuomos ID.                   |");
        System.out.println("| 0. Pagalba.                                    | -1. Grįžti atgal.                              |");
        System.out.println("---------------------------------------------------------------------------------------------------");
    }
    public boolean isNumeric(String strNum) {
        try
        {
            int i = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe)
        {
            return true;
        }
        return false;
    }
}
