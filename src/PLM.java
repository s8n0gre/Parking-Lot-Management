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
            System.out.println();
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
        // --- NEW: Add Owner ---
    public void addOwner() {
        System.out.println("\n---------------------------------------");
        try {
            System.out.print("Enter Owner ID: ");
            int ownerId = Integer.parseInt(reader.readLine());
            System.out.print("Enter Owner Name: ");
            String ownerName = reader.readLine();
            System.out.print("Enter Contact Number: ");
            String contact = reader.readLine();

            String sql = "INSERT INTO OWNER (OWNER_ID, OWNER_NAME, CONTACT_NUMBER) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, ownerId);
            preparedStatement.setString(2, ownerName);
            preparedStatement.setString(3, contact);

            int inserted = preparedStatement.executeUpdate();
            if (inserted > 0) {
                System.out.println(" Owner added successfully!");
            } else {
                System.out.println(" Failed to add owner.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            close(null, preparedStatement);
        }
        System.out.println("---------------------------------------\n");
    }
  public void showOwners() {
        System.out.println("\n---------------------------------------");
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM OWNER";
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            System.out.println("OWNER_ID | OWNER_NAME           | CONTACT_NUMBER");
            System.out.println("-----------------------------------------------");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-8d | %-20s | %-15s%n",
                    rs.getInt("OWNER_ID"),
                    rs.getString("OWNER_NAME"),
                    rs.getString("CONTACT_NUMBER"));
            }
            if (!found) {
                System.out.println("No owners found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs, preparedStatement);
        }
        System.out.println("---------------------------------------\n");
    }
    // --- NEW: Add Vehicle ---
    public void addVehicle() {
        System.out.println("\n---------------------------------------");
        try {
            System.out.print("Enter Vehicle ID: ");
            int vehicleId = Integer.parseInt(reader.readLine());
            System.out.print("Enter Owner ID: ");
            int ownerId = Integer.parseInt(reader.readLine());
            System.out.print("Enter Vehicle Number: ");
            String vehicleNumber = reader.readLine();
            System.out.print("Enter Vehicle Type: ");
            String vehicleType = reader.readLine();

            String sql = "INSERT INTO VEHICLE (VEHICLE_ID, OWNER_ID, VEHICLE_NUMBER, VEHICLE_TYPE) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, vehicleId);
            preparedStatement.setInt(2, ownerId);
            preparedStatement.setString(3, vehicleNumber);
            preparedStatement.setString(4, vehicleType);

            int inserted = preparedStatement.executeUpdate();
            if (inserted > 0) {
                System.out.println(" Vehicle added successfully!");
            } else {
                System.out.println(" Failed to add vehicle.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            close(null, preparedStatement);
        }
        System.out.println("---------------------------------------\n");
    }
        public void showVehicles() {
        System.out.println("\n---------------------------------------");
        ResultSet rs = null;
        try {
            String sql = "SELECT V.VEHICLE_ID, V.VEHICLE_NUMBER, V.VEHICLE_TYPE, O.OWNER_NAME FROM VEHICLE V LEFT JOIN OWNER O ON V.OWNER_ID = O.OWNER_ID";
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            System.out.println("VEHICLE_ID | VEHICLE_NUMBER    | VEHICLE_TYPE   | OWNER_NAME");
            System.out.println("------------------------------------------------------------");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-10d | %-16s | %-14s | %-20s%n",
                    rs.getInt("VEHICLE_ID"),
                    rs.getString("VEHICLE_NUMBER"),
                    rs.getString("VEHICLE_TYPE"),
                    rs.getString("OWNER_NAME"));
            }
            if (!found) {
                System.out.println("No vehicles found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs, preparedStatement);
        }
        System.out.println("---------------------------------------\n");
    }

    public void showAvailableSlots() {
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM reservation_slot_details WHERE SLOT_BOOKING_STATUS='Available' ORDER BY RESERVATION_ID asc";
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
   public void addSlot() {
        System.out.println("\n---------------------------------------");
        try {
            System.out.print("Enter the Reservation ID: ");
            int reservationId = Integer.parseInt(reader.readLine());

            System.out.print("Enter the Slot ID: ");
            int slotId = Integer.parseInt(reader.readLine());

            System.out.print("Enter Start Time (e.g., 10:00 AM): ");
            String startTime = reader.readLine();

            System.out.print("Enter End Time (e.g., 11:00 AM): ");
            String endTime = reader.readLine();

            System.out.print("Enter Booking Status (Available/Booked): ");
            String status = reader.readLine();

            System.out.print("Enter Reservation Status (Available/Booked): ");
            String resStatus = reader.readLine();

            System.out.print("Enter Vehicle ID: ");
            int vehicleId = Integer.parseInt(reader.readLine());

            String sql = "INSERT INTO reservation_slot_details (RESERVATION_ID, SLOT_ID, SLOT_START_TIME, SLOT_END_TIME, SLOT_BOOKING_STATUS, RESERVATION_STATUS, VEHICLE_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, reservationId);
            preparedStatement.setInt(2, slotId);
            preparedStatement.setString(3, startTime);
            preparedStatement.setString(4, endTime);
            preparedStatement.setString(5, status);
            preparedStatement.setString(6, resStatus);
            preparedStatement.setInt(7, vehicleId);

            int inserted = preparedStatement.executeUpdate();

            if (inserted > 0) {
                System.out.println(" Slot added successfully!");
            } else {
                System.out.println(" Failed to add the slot.");
            }

        } catch (SQLException | IOException | NumberFormatException e) {
            e.printStackTrace();
        } finally {
            close(null, preparedStatement);
        }

        System.out.println("---------------------------------------\n");
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
                System.out.println(" No such available slot ID found or it is already booked.");
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
                System.out.println(" No such booked reservation ID found.");
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
        System.out.println("3. Add a Slot into Reservation");
        System.out.println("4. Cancel a Reservation");
        System.out.println("5. Book from Our Agents");
        System.out.println("6. View Reservation Details");
        System.out.println("7. Previous Menu");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
        String input = reader.readLine();
        if (input == null || input.isBlank()) {
            exit();
            return;
        }
        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println(" Invalid choice, please try again.");
            return;
        }
        switch (choice) {
            case 1 -> showAvailableSlots();
            case 2 -> bookSlot();
            case 3 -> addSlot();
            case 4 -> cancelReservation();
            case 5 -> bookFromAgents();
            case 6 -> viewReservationDetails();
            case 7 -> previousMenu();
            case 8 -> exit();
            default -> System.out.println(" Invalid choice, please try again.");
        }
    }
        public void customerMenu() throws IOException {
        while (!flag) {
            System.out.println("\n=======================================");
            System.out.println("           CUSTOMER MENU               ");
            System.out.println("=======================================");
            System.out.println("1. View Available Slots");
            System.out.println("2. Book a Slot");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View Reservation Details");
            System.out.println("5. Contact Customer Support");
            System.out.println("6. Book from Agents");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            String input = reader.readLine();
            if (input == null || input.isBlank()) {
                exit();
                return;
            }
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(" Invalid choice, please try again.");
                continue;
            }
            switch (choice) {
                case 1 -> showAvailableSlots();
                case 2 -> bookSlot();
                case 3 -> cancelReservation();
                case 4 -> viewReservationDetails();
                case 5 -> contactCustomerSupport();
                case 6 -> bookFromAgents();
                case 7 -> exit();
                default -> System.out.println(" Invalid choice, please try again.");
            }
        }
    }

        public void agentMenu() throws IOException {
        while (!flag) {
            System.out.println("\n=======================================");
            System.out.println("             AGENT MENU                ");
            System.out.println("=======================================");
            System.out.println("1. Add Owner");
            System.out.println("2. Add Vehicle");
            System.out.println("3. Add Reservation Slot");
            System.out.println("4. Show Owners");
            System.out.println("5. Show Vehicles");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            String input = reader.readLine();
            if (input == null || input.isBlank()) {
                exit();
                return;
            }
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(" Invalid choice, please try again.");
                continue;
            }
            switch (choice) {
                case 1 -> addOwner();
                case 2 -> addVehicle();
                case 3 -> addSlot();
                case 4 -> showOwners();
                case 5 -> showVehicles();
                case 6 -> exit();
                default -> System.out.println(" Invalid choice, please try again.");
            }
        }
    }

  public void mainMenu() throws IOException {
        while (!flag) {
            System.out.println("\n=======================================");
            System.out.println("         PARKING LOT MANAGEMENT        ");
            System.out.println("=======================================");
            System.out.println("1. Customer");
            System.out.println("2. Agent");
            System.out.println("3. Exit");
            System.out.print("Select user type: ");
            String input = reader.readLine();
            if (input == null || input.isBlank()) {
                exit();
                return;
            }
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(" Invalid choice, please try again.");
                continue;
            }
            switch (choice) {
                case 1 -> customerMenu();
                case 2 -> agentMenu();
                case 3 -> exit();
                default -> System.out.println(" Invalid choice, please try again.");
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
