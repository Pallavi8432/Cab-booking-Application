import java.io.*;
import java.util.*;

class User {
    String userId;
    String username;
    String password;
    String email;

    User(String userId, String username, String password, String email) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    public String toString() {
        return userId + "," + username + "," + password + "," + email;
    }

    public static User fromString(String userString) {
        String[] parts = userString.split(",");
        return new User(parts[0], parts[1], parts[2], parts[3]);
    }
}

class Booking {
    String userId;
    String pickupLocation;
    String destination;
    String cabType;

    Booking(String userId, String pickupLocation, String destination, String cabType) {
        this.userId = userId;
        this.pickupLocation = pickupLocation;
        this.destination = destination;
        this.cabType = cabType;
    }

    @Override
    public String toString() {
        return userId + "," + pickupLocation + "," + destination + "," + cabType;
    }

    public static Booking fromString(String bookingString) {
        String[] parts = bookingString.split(",");
        return new Booking(parts[0], parts[1], parts[2], parts[3]);
    }
}

public class CabBookingSystem {
    private static final String USERS_FILE = "users.txt";
    private static final String BOOKINGS_FILE = "bookings.txt";

    private static List<User> users = new ArrayList<>();
    private static List<Booking> bookings = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadUsers();
        loadBookings();
        
        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    register();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    saveUsers();
                    saveBookings();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        String userId = UUID.randomUUID().toString();

        for (User user : users) {
            if (user.username.equals(username)) {
                System.out.println("Username already exists. Please choose another username.");
                return;
            }
        }

        users.add(new User(userId, username, password, email));
        System.out.println("User registered successfully.");
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                System.out.println("Login successful.");
                userMenu(user.userId);
                return;
            }
        }

        System.out.println("Invalid username or password.");
    }

    private static void userMenu(String userId) {
        while (true) {
            System.out.println("1. Book a cab");
            System.out.println("2. View booking history");
            System.out.println("3. Update User password and email");
            System.out.println("4. Cancel booking");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    bookCab(userId);
                    break;
                case 2:
                    viewBookingHistory(userId);
                    break;
                case 3:
                    updateUserDetails(userId);
                    break;
                case 4:
                    cancelBooking(userId);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void bookCab(String userId) {
        System.out.print("Enter pickup location: ");
        String pickupLocation = scanner.nextLine();
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();
        System.out.print("Enter cab type (Standard, Premium, Luxury): ");
        String cabType = scanner.nextLine();

        bookings.add(new Booking(userId, pickupLocation, destination, cabType));
        System.out.println("Cab booked successfully.");
    }

    private static void viewBookingHistory(String userId) {
        System.out.println("Booking History for User ID " + userId + ":");
        for (Booking booking : bookings) {
            if (booking.userId.equals(userId)) {
                System.out.println("Pickup: " + booking.pickupLocation + ", Destination: " + booking.destination + ", Cab Type: " + booking.cabType);
            }
        }
    }

    private static void updateUserDetails(String userId) {
        for (User user : users) {
            if (user.userId.equals(userId)) {
                System.out.print("Enter new password: ");
                String newPassword = scanner.nextLine();
                System.out.print("Enter new email: ");
                String newEmail = scanner.nextLine();
                user.password = newPassword;
                user.email = newEmail;
                System.out.println("Details updated successfully.");
                return;
            }
        }
        System.out.println("User not found.");
    }

    private static void cancelBooking(String userId) {
        System.out.print("Enter pickup location of booking to cancel: ");
        String pickupLocation = scanner.nextLine();

        for (Iterator<Booking> iterator = bookings.iterator(); iterator.hasNext();) {
            Booking booking = iterator.next();
            if (booking.userId.equals(userId) && booking.pickupLocation.equals(pickupLocation)) {
                iterator.remove();
                System.out.println("Booking canceled successfully.");
                return;
            }
        }
        System.out.println("Booking not found.");
    }

    private static void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                users.add(User.fromString(line));
            }
        } catch (FileNotFoundException e) {
            users = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadBookings() {
        try (BufferedReader br = new BufferedReader(new FileReader(BOOKINGS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                bookings.add(Booking.fromString(line));
            }
        } catch (FileNotFoundException e) {
            bookings = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                bw.write(user.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveBookings() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Booking booking : bookings) {
                bw.write(booking.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}