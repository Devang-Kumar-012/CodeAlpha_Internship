import java.io.*;
import java.util.*;

class Room implements Serializable {
    int roomNumber;
    String category; // Standard, Deluxe, Suite
    double price;
    boolean isAvailable;

    public Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.isAvailable = true; // Initially available
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " [" + category + "] - Price: $" + price + " - " +
                (isAvailable ? "Available" : "Booked");
    }
}

class Booking implements Serializable {
    String customerName;
    int roomNumber;
    String category;
    double amountPaid;

    public Booking(String customerName, int roomNumber, String category, double amountPaid) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.amountPaid = amountPaid;
    }

    @Override
    public String toString() {
        return "Booking - Customer: " + customerName + ", Room: " + roomNumber + " (" + category + "), Paid: $" + amountPaid;
    }
}

public class HotelReservationSystem {
    private List<Room> rooms;
    private List<Booking> bookings;
    private final String roomsFile = "rooms.dat";
    private final String bookingsFile = "bookings.dat";

    public HotelReservationSystem() {
        rooms = loadRooms();
        bookings = loadBookings();
    }

    private List<Room> createDefaultRooms() {
        List<Room> roomList = new ArrayList<>();
        roomList.add(new Room(101, "Standard", 50));
        roomList.add(new Room(102, "Standard", 50));
        roomList.add(new Room(201, "Deluxe", 100));
        roomList.add(new Room(202, "Deluxe", 100));
        roomList.add(new Room(301, "Suite", 200));
        return roomList;
    }

    private void saveRooms() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(roomsFile))) {
            oos.writeObject(rooms);
        } catch (IOException e) {
            System.out.println("Error saving rooms: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<Room> loadRooms() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(roomsFile))) {
            return (List<Room>) ois.readObject();
        } catch (Exception e) {
            return createDefaultRooms();
        }
    }

    private void saveBookings() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(bookingsFile))) {
            oos.writeObject(bookings);
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<Booking> loadBookings() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(bookingsFile))) {
            return (List<Booking>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void searchRooms(String category) {
        System.out.println("Searching available rooms for category: " + category);
        boolean found = false;
        for (Room room : rooms) {
            if (room.category.equalsIgnoreCase(category) && room.isAvailable) {
                System.out.println(room);
                found = true;
            }
        }
        if (!found) System.out.println("No available rooms found in " + category + " category.");
    }

    public boolean bookRoom(String customerName, String category) {
        for (Room room : rooms) {
            if (room.category.equalsIgnoreCase(category) && room.isAvailable) {
                double amount = room.price;
                System.out.println("Processing payment of $" + amount + " for " + customerName + "...");
                room.isAvailable = false;
                Booking booking = new Booking(customerName, room.roomNumber, category, amount);
                bookings.add(booking);
                saveRooms();
                saveBookings();
                System.out.println("Booking successful! " + booking);
                return true;
            }
        }
        System.out.println("No available rooms found to book in " + category + " category.");
        return false;
    }

    public boolean cancelBooking(String customerName, int roomNumber) {
        Iterator<Booking> iterator = bookings.iterator();
        while (iterator.hasNext()) {
            Booking booking = iterator.next();
            if (booking.customerName.equalsIgnoreCase(customerName) && booking.roomNumber == roomNumber) {
                iterator.remove();
                for (Room room : rooms) {
                    if (room.roomNumber == roomNumber) {
                        room.isAvailable = true;
                        break;
                    }
                }
                saveRooms();
                saveBookings();
                System.out.println("Booking canceled for " + customerName + " in room " + roomNumber);
                return true;
            }
        }
        System.out.println("No booking found for " + customerName + " in room " + roomNumber);
        return false;
    }

    public void viewBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }
        System.out.println("Current Bookings:");
        for (Booking booking : bookings) {
            System.out.println(booking);
        }
    }

    // Helper method to validate room category
    private static boolean isValidCategory(String category) {
        return category != null && (category.equalsIgnoreCase("Standard") ||
                category.equalsIgnoreCase("Deluxe") ||
                category.equalsIgnoreCase("Suite"));
    }

    // Helper method to validate customer name (allow letters, spaces, apostrophes only)
    private static boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-Z\\s']+");
    }

    // Safe input method for integer with prompt and validation
    private static int getIntInput(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            try {
                int value = Integer.parseInt(input);
                if (value < 1) {
                    System.out.println("Please enter a positive number.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number input. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HotelReservationSystem system = new HotelReservationSystem();
        boolean running = true;

        System.out.println("Welcome to the Hotel Reservation System!");

        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Search Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Bookings");
            System.out.println("5. Exit");

            int choice = getIntInput(sc, "Enter your choice: ");

            switch (choice) {
                case 1:
                    System.out.print("Enter room category (Standard/Deluxe/Suite): ");
                    String searchCategory = sc.nextLine().trim();
                    if (!isValidCategory(searchCategory)) {
                        System.out.println("Invalid category. Please enter Standard, Deluxe, or Suite.");
                        break;
                    }
                    system.searchRooms(searchCategory);
                    break;

                case 2:
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine().trim();
                    if (!isValidName(name)) {
                        System.out.println("Invalid name. Use letters, spaces, and apostrophes only.");
                        break;
                    }
                    System.out.print("Enter room category to book (Standard/Deluxe/Suite): ");
                    String bookCategory = sc.nextLine().trim();
                    if (!isValidCategory(bookCategory)) {
                        System.out.println("Invalid category. Please enter Standard, Deluxe, or Suite.");
                        break;
                    }
                    system.bookRoom(name, bookCategory);
                    break;

                case 3:
                    System.out.print("Enter your name: ");
                    String cancelName = sc.nextLine().trim();
                    if (!isValidName(cancelName)) {
                        System.out.println("Invalid name. Use letters, spaces, and apostrophes only.");
                        break;
                    }
                    int roomNum = getIntInput(sc, "Enter room number to cancel: ");
                    system.cancelBooking(cancelName, roomNum);
                    break;

                case 4:
                    system.viewBookings();
                    break;

                case 5:
                    running = false;
                    System.out.println("Thank you for using the Hotel Reservation System!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        sc.close();
    }
}
