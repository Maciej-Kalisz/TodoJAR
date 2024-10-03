import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    static int getIntInput(Scanner s, String... q) {
        assert q.length <= 1;
        // bool param3 = params.length > 0 ? params[0].booleanValue() : false;
        String que = q.length > 0 ? q[0] : "";
        System.out.println(que);

        while (true) {
            try {
                return Integer.parseInt(s.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Wrong input given!");
            }
        }
    }

    public static void main(String[] args) {
        Scanner kbInput = new Scanner(System.in);

        String url = "jdbc:sqlite:/TodoJAR.sqlite";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stm = conn.createStatement();

            stm.execute("SELECT * FROM sqlite_master WHERE type='table'");

            System.out.print("Connection successful");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        br();
        System.out.println("Welcome to TodoJAR!");
        System.out.println("""
                Your options are:\s
                [1] Check outstanding tasks
                [2] Create a new task""");
        br();

        int answer = getIntInput(kbInput, "Which option would you like? ");

        switch(answer) {
            case 1:
                // TODO: Check outstanding tasks
                System.out.println("Check outstanding tasks");
                break;
            case 2:
                System.out.println("Create a new task");
                // TODO: Create a new task
                break;
            default:
                System.out.println("That input has not been recognised");
        }
    }

    public static void br() {
        System.out.println("\n-----------------------------------\n");
    }
}