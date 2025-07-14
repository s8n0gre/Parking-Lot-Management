// ...existing code...
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class PLM {
    private static boolean flag = false;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private BufferedReader reader;

    public PLM() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521/XEPDB1", "dev", "123"
            );
            System.out.println(" ________  ________  ________  ___  __    ___  ________   ________          ___       ________  _________       ");
            System.out.println("|\\   __  \\|\\   __  \\|\\   __  \\|\\  \\|\\  \\ |\\  \\|\\   ___  \\|\\   ____\\        |\\  \\     |\\   __  \\|\\___   ___\\     ");
            System.out.println("\\ \\  \\|\\  \\ \\  \\|\\  \\ \\  \\|\\  \\ \\  \\/  /|\\ \\  \\ \\  \\\\ \\  \\ \\  \\___|        \\ \\  \\    \\ \\  \\|\\  \\|___ \\  \\_|     ");
            System.out.println(" \\ \\   ____\\ \\   __  \\ \\   _  _\\ \\   ___  \\ \\  \\ \\  \\\\ \\  \\ \\  \\  ___       \\ \\  \\    \\ \\  \\\\\\  \\   \\ \\  \\      ");
            System.out.println("  \\ \\  \\___|\\ \\  \\ \\  \\ \\  \\\\  \\\\ \\  \\\\ \\  \\ \\  \\ \\  \\\\ \\  \\ \\  \\|\\  \\       \\ \\  \\____\\ \\  \\\\\\  \\   \\ \\  \\     ");
            System.out.println("   \\ \\__\\    \\ \\__\\ \\__\\ \\__\\\\ _\\\\ \\__\\\\ \\__\\ \\__\\ \\__\\\\ \\__\\ \\_______\\       \\ \\_______\\ \\_______\\   \\ \\__\\    ");
            System.out.println("    \\|__|     \\|__|\\|__|\\|__|\\|__|\\|__| \\|__|\\|__|\\|__| \\|__|\\|_______|        \\|_______|\\|_______|    \\|__|    ");

            System.out.println("=================================================================================================================");
            System.out.println("\t\t\t\t Connected to the Database Successfully!");
            System.out.println("=================================================================================================================\n");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void showAvailableSlots() {
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM reservation_slot_details WHERE SLOT_BOOKING_STATUS='Available'";
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            boolean found = false;
            System.out.println("=======================================");
            System.out.println("         AVAILABLE RESERVATION SLOTS    ");
            System.out.println("=======================================");
            while (rs.next()) {
                found = true;
                System.out.printf(
                    "Reservation ID: %-6d | Time: %s - %s | Status: %s%n",
                    rs.getInt("RESERVATION_ID"),
                    rs.getString("SLOT_START_TIME"),
                    rs.getString("SLOT_END_TIME"),
                    rs.getString("SLOT_BOOKING_STATUS")
                );
            }
            if (!found) {
                System.out.println("  No available slots for reservation.");
            }
            System.out.println("=======================================\n");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs, preparedStatement);
        }
    }

    public void bookSlot() {
        System.out.println("\n---------------------------------------");
        System.out.print("Enter the Reservation ID to Book: ");
        try {
            int reservationId = Integer.parseInt(reader.readLine());
            String sql = "UPDATE reservation_slot_details SET SLOT_BOOKING_STATUS='Booked' WHERE RESERVATION_ID=? AND SLOT_BOOKING_STATUS='Available'";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, reservationId);
            int updated = preparedStatement.executeUpdate();
            if (updated > 0) {
                System.out.println(" Slot booked successfully!");
            } else {
                System.out.println("❌ No such available slot ID found or it is already booked.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            close(null, preparedStatement);
        }
        System.out.println("---------------------------------------\n");
    }

    public void cancelReservation() {
        System.out.println("\n---------------------------------------");
        try {
            System.out.print("Enter the Reservation ID to Cancel: ");
            int reservationId = Integer.parseInt(reader.readLine());
            String sql = "UPDATE reservation_slot_details SET SLOT_BOOKING_STATUS='Available', RESERVATION_STATUS='Available' WHERE RESERVATION_ID=? AND SLOT_BOOKING_STATUS='Booked'";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, reservationId);
            int updated = preparedStatement.executeUpdate();
            if (updated > 0) {
                System.out.println(" Reservation canceled successfully!");
            } else {
                System.out.println("❌ No such booked reservation ID found.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            close(null, preparedStatement);
        }
        System.out.println("---------------------------------------\n");
    }

    public void bookFromAgents() {
        System.out.println("\n=======================================");
        System.out.println(" Contact Customer Support for Assistance");
        System.out.println("  Phone: (78960-xxxxx)");
        System.out.println("=======================================");
        System.out.println("Press Enter to continue...");
        waitForEnter();
    }

    public void viewReservationDetails() {
        System.out.println("\n---------------------------------------");
        System.out.print("Enter the Reservation ID to View Details: ");
        ResultSet rs = null;
        try {
            int reservationId = Integer.parseInt(reader.readLine());
            String sql = "SELECT * FROM reservation_slot_details WHERE RESERVATION_ID=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, reservationId);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                System.out.println("\n=======================================");
                System.out.println("          RESERVATION DETAILS          ");
                System.out.println("=======================================");
                System.out.println("Reservation ID : " + rs.getInt("RESERVATION_ID"));
                System.out.println("Slot ID        : " + rs.getInt("SLOT_ID"));
                System.out.println("Time           : " + rs.getString("SLOT_START_TIME") + " - " + rs.getString("SLOT_END_TIME"));
                System.out.println("Status         : " + rs.getString("SLOT_BOOKING_STATUS"));
                System.out.println("=======================================\n");
            } else {
                System.out.println("  No such reservation ID found.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            close(rs, preparedStatement);
        }
        waitForEnter();
    }

    public void contactCustomerSupport() {
        System.out.println("\n=======================================");
        System.out.println("  Contacting customer support is not implemented yet.");
        System.out.println("=======================================");
        System.out.println("Press Enter to continue...");
        waitForEnter();
    }

    public void previousMenu() {
        System.out.println("\nReturning to the previous menu...");
        System.out.println("Press Enter to continue...");
        waitForEnter();
        System.out.println("Returning to the main menu...\n");
    }

    public void exit() {
        flag = true;
        System.out.println("\n=======================================");
        System.out.println(" Exiting the application... Goodbye!");
        System.out.println("=======================================");
        System.exit(0);
    }

    public void reservationMenu() throws IOException {
        System.out.println("\n=======================================");
        System.out.println("           RESERVATION MENU            ");
        System.out.println("=======================================");
        System.out.println("1. Available Slots for Reservation");
        System.out.println("2. Book a Slot for Reservation");
        System.out.println("3. Cancel a Reservation");
        System.out.println("4. Book from Our Agents");
        System.out.println("5. View Reservation Details");
        System.out.println("6. Previous Menu");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(reader.readLine());
        switch (choice) {
            case 1 -> showAvailableSlots();
            case 2 -> bookSlot();
            case 3 -> cancelReservation();
            case 4 -> bookFromAgents();
            case 5 -> viewReservationDetails();
            case 6 -> previousMenu();
            case 7 -> exit();
            default -> System.out.println("❌ Invalid choice, please try again.");
        }
    }

    public void mainMenu() throws IOException {
        while (!flag) {
            System.out.println("\n=======================================");
            System.out.println("              MAIN MENU                ");
            System.out.println("=======================================");
            System.out.println("1. Reservation");
            System.out.println("2. Contact with Customer Support");
            System.out.println("3. Previous Menu");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(reader.readLine());
            switch (choice) {
                case 1 -> reservationMenu();
                case 2 -> contactCustomerSupport();
                case 3 -> previousMenu();
                case 4 -> exit();
                default -> System.out.println("❌ Invalid choice, please try again.");
            }
        }
    }

    private void waitForEnter() {
        try {
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close(ResultSet rs, PreparedStatement ps) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            PLM plm = new PLM();
            plm.mainMenu();
            if (plm.connection != null) plm.connection.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}

