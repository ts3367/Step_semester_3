package system_design.class_problems;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Hotel Booking System. Room pricing is a Strategy, so a new category is added
// without touching the booking or cancellation workflow.
public class P3HotelBookingSystem {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public interface NightlyPricing {
        String getCategoryName();

        double nightlyRate();
    }

    public static class StandardPricing implements NightlyPricing {
        @Override
        public String getCategoryName() {
            return "Standard";
        }

        @Override
        public double nightlyRate() {
            return 150.0;
        }
    }

    public static class DeluxePricing implements NightlyPricing {
        @Override
        public String getCategoryName() {
            return "Deluxe";
        }

        @Override
        public double nightlyRate() {
            return 200.0;
        }
    }

    public static class SuitePricing implements NightlyPricing {
        @Override
        public String getCategoryName() {
            return "Suite";
        }

        @Override
        public double nightlyRate() {
            return 350.0;
        }
    }

    public static class Customer {
        private final String name;

        public Customer(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum ReservationStatus {
        BOOKED, CANCELLED
    }

    public static class Reservation {
        private final Room room;
        private final Customer customer;
        private final LocalDate checkIn;
        private final LocalDate checkOut;
        private final double totalPrice;
        private final LocalDate cancellationDeadline;
        private ReservationStatus status = ReservationStatus.BOOKED;

        public Reservation(Room room, Customer customer, LocalDate checkIn, LocalDate checkOut,
                           double totalPrice, LocalDate cancellationDeadline) {
            this.room = room;
            this.customer = customer;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.totalPrice = totalPrice;
            this.cancellationDeadline = cancellationDeadline;
        }

        public Room getRoom() {
            return room;
        }

        public Customer getCustomer() {
            return customer;
        }

        public LocalDate getCheckIn() {
            return checkIn;
        }

        public LocalDate getCheckOut() {
            return checkOut;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public LocalDate getCancellationDeadline() {
            return cancellationDeadline;
        }

        public ReservationStatus getStatus() {
            return status;
        }

        public long nights() {
            return checkOut.toEpochDay() - checkIn.toEpochDay();
        }

        void cancel() {
            this.status = ReservationStatus.CANCELLED;
        }
    }

    public static class Room {
        private final String name;
        private final int number;
        private final NightlyPricing pricing;
        private final List<Reservation> reservations = new ArrayList<>();

        public Room(String name, int number, NightlyPricing pricing) {
            this.name = name;
            this.number = number;
            this.pricing = pricing;
        }

        public String getName() {
            return name;
        }

        public int getNumber() {
            return number;
        }

        public NightlyPricing getPricing() {
            return pricing;
        }

        public List<Reservation> getReservations() {
            return reservations;
        }

        public boolean isAvailableFor(LocalDate checkIn, LocalDate checkOut) {
            for (Reservation reservation : reservations) {
                if (reservation.getStatus() == ReservationStatus.CANCELLED) {
                    continue;
                }
                boolean overlaps = checkIn.isBefore(reservation.getCheckOut())
                        && reservation.getCheckIn().isBefore(checkOut);
                if (overlaps) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class BookingManager {
        private final List<Room> rooms = new ArrayList<>();
        private final List<Reservation> reservations = new ArrayList<>();
        private final int freeCancellationDaysBeforeCheckIn;

        public BookingManager(int freeCancellationDaysBeforeCheckIn) {
            this.freeCancellationDaysBeforeCheckIn = freeCancellationDaysBeforeCheckIn;
        }

        public List<Room> getRooms() {
            return rooms;
        }

        public List<Reservation> getReservations() {
            return reservations;
        }

        public void addRoom(Room room) {
            rooms.add(room);
        }

        public Room findRoomByName(String name) {
            for (Room room : rooms) {
                if (room.getName().equalsIgnoreCase(name)) {
                    return room;
                }
            }
            return null;
        }

        public Reservation book(Customer customer, Room room, LocalDate checkIn, LocalDate checkOut) {
            if (!checkIn.isBefore(checkOut)) {
                throw new IllegalArgumentException("Check-in date must be before check-out date.");
            }
            if (!room.isAvailableFor(checkIn, checkOut)) {
                System.out.println("Booking failed: " + room.getName() + " is not available for "
                        + checkIn.format(DATE_FORMAT) + " to " + checkOut.format(DATE_FORMAT) + ".");
                return null;
            }
            long nights = checkOut.toEpochDay() - checkIn.toEpochDay();
            double totalPrice = room.getPricing().nightlyRate() * nights;
            LocalDate cancellationDeadline = checkIn.minusDays(freeCancellationDaysBeforeCheckIn);
            Reservation reservation = new Reservation(room, customer, checkIn, checkOut, totalPrice,
                    cancellationDeadline);
            room.getReservations().add(reservation);
            reservations.add(reservation);
            System.out.println(room.getName() + " booked from " + checkIn.format(DATE_FORMAT) + " to "
                    + checkOut.format(DATE_FORMAT) + ". Total price: $" + String.format("%.2f", totalPrice));
            return reservation;
        }

        public boolean cancel(Reservation reservation, LocalDate onDate) {
            if (reservation.getStatus() == ReservationStatus.CANCELLED) {
                System.out.println("Cancellation failed: reservation for " + reservation.getRoom().getName()
                        + " is already cancelled.");
                return false;
            }
            if (!onDate.isBefore(reservation.getCancellationDeadline())) {
                System.out.println("Cancellation failed: reservation for " + reservation.getRoom().getName()
                        + " can only be cancelled before " + reservation.getCancellationDeadline().format(DATE_FORMAT)
                        + ".");
                return false;
            }
            reservation.cancel();
            System.out.println("Reservation for " + reservation.getRoom().getName() + " cancelled successfully.");
            return true;
        }
    }

    public static void main(String[] args) {
        BookingManager manager = new BookingManager(1);
        Room deluxe = new Room("Deluxe Room 101", 101, new DeluxePricing());
        Room standard = new Room("Standard Room 205", 205, new StandardPricing());
        manager.addRoom(deluxe);
        manager.addRoom(standard);

        Customer customer = new Customer("Meera");
        LocalDate dec1 = LocalDate.of(2024, 12, 1);
        LocalDate dec5 = LocalDate.of(2024, 12, 5);
        LocalDate dec3 = LocalDate.of(2024, 12, 3);
        LocalDate dec7 = LocalDate.of(2024, 12, 7);

        Reservation first = manager.book(customer, deluxe, dec1, dec5);
        manager.book(customer, standard, dec3, dec7);
        manager.book(customer, deluxe, dec3, dec7);

        manager.cancel(first, LocalDate.of(2024, 11, 25));

        Reservation rebooked = manager.book(customer, deluxe, dec3, dec7);
        manager.cancel(rebooked, LocalDate.of(2024, 12, 5));
    }
}
