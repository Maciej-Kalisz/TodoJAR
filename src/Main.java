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
                [1] Check all tasks
                [2] Check outstanding tasks
                [3] Create a new task
                [4] Mark as complete
                """);
        br();

        int answer = getIntInput(kbInput, "Which option would you like? ");

        switch(answer) {
            case 1:
                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:TodoJAR.sqlite")) {
                    String query = "SELECT * FROM tasks";

                    PreparedStatement stm = conn.prepareStatement(query);
                    stm.execute();
                    ResultSet rs = stm.getResultSet();

                    while(rs.next()) {
                        System.out.format("%-4s | %-15s | %-15s | %-30s | %-15s\n", "ID", "Task Name", "Description", "Deadline", "Completed?");
                        System.out.format("%-4s | %-15s | %-15s | %-30s | %-15s\n",
                                rs.getInt("taskId"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getString("deadline"),
                                rs.getInt("completed")
                        );
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                break;
            case 2:
                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:TodoJAR.sqlite")) {
                    String query = "SELECT * FROM tasks WHERE completed = 0";

                    PreparedStatement stm = conn.prepareStatement(query);
                    stm.execute();
                    ResultSet rs = stm.getResultSet();

                    while(rs.next()) {
                        System.out.format("%-4s | %-15s | %-15s | %-30s\n", "ID", "Task Name", "Description", "Deadline");
                        System.out.format("%-4s | %-15s | %-15s | %-30s\n",
                                rs.getInt("taskId"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getString("deadline")
                        );
                    }

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                break;
            case 3:
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
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                break;
            case 4:
                int toMark = getIntInput(kbInput, "Which task would you like to mark? ");

                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:TodoJAR.sqlite")) {
                    String idQuery = "SELECT taskId FROM tasks";
                    String updateQuery = "UPDATE tasks SET completed = 1 WHERE taskId = ?";

                    ResultSet rs = conn.prepareStatement(idQuery).executeQuery();

                    while (rs.next()) {
                        if (rs.getInt("taskId") == toMark) {
                            PreparedStatement updateStm = conn.prepareStatement(updateQuery);
                            updateStm.setInt(1, toMark);
                            updateStm.execute();

                            System.out.println("Task has been marked as complete!");

                            break;
                        }

                        System.out.println("No such task has been found.");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                break;
            default:
                System.out.println("That input has not been recognised");

                break;
        }
    }
    static void br() {
        System.out.println("\n-----------------------------------\n");
    }
}