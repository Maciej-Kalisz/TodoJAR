import java.sql.*;
import java.util.Date;
import java.util.Scanner;

/*
CREATE TABLE IF NOT EXISTS tasks (
     taskID INTEGER PRIMARY KEY AUTOINCREMENT,
     name TEXT NOT NULL,
     description TEXT,
     deadline DATETIME,
     completed INTEGER NOT NULL
)
*/

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

    static Date parseDate(String s) {
        String[] parts = s.split("/");
        int[] parsed = new int[3];

        if (parts.length != 3) { System.out.println("Wrong input given!"); throw new IllegalArgumentException(); }

        for (int i = 0; i < parts.length; i++) {
             parsed[i] = Integer.parseInt(parts[i]);
        }

        return new Date(parsed[2] - 1900, parsed[1] -1, parsed[0]);
    }

    public static void main(String[] args) {
        Scanner kbInput = new Scanner(System.in);

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
                System.out.println("What should the new task be called?");
                String taskName = kbInput.nextLine();

                System.out.println("What should the task description be? ");
                String description = kbInput.nextLine();

                Date deadline;
                while (true) {
                    try {
                        System.out.println("What's the deadline for this task? ");
                        deadline = parseDate(kbInput.nextLine());
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Wrong input given!");
                    }
                }
                System.out.println(deadline);

                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:TodoJAR.sqlite")) {
                    String query = "INSERT INTO tasks(name, description, deadline, completed) VALUES (?, ?, ?, 0)";

                    PreparedStatement stm = conn.prepareStatement(query);
                    stm.setString(1, taskName);
                    stm.setString(2, description);
                    stm.setString(3, String.valueOf(deadline));
                    stm.execute();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                break;
            default:
                System.out.println("That input has not been recognised");
        }
    }
    static void br() {
        System.out.println("\n-----------------------------------\n");
    }
}