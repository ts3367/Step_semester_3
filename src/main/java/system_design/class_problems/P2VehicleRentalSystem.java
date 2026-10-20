package system_design.class_problems;

import java.util.ArrayList;
import java.util.List;

// Vehicle Rental System. Pricing rules live on the Vehicle hierarchy, so a new
// category is added without touching the rental workflow.
public class P2VehicleRentalSystem {

    public static class Customer {
        private final String name;

        public Customer(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public abstract static class Vehicle {
        private final String name;
        private final String registrationNumber;
        private boolean available = true;
        private Rental activeRental;

        protected Vehicle(String name, String registrationNumber) {
            this.name = name;
            this.registrationNumber = registrationNumber;
        }

        public String getName() {
            return name;
        }

        public String getRegistrationNumber() {
            return registrationNumber;
        }

        public boolean isAvailable() {
            return available;
        }

        public Rental getActiveRental() {
            return activeRental;
        }

        public abstract double dailyRate();

        void markRented(Rental rental) {
            this.activeRental = rental;
            this.available = false;
        }

        void markReturned() {
            this.activeRental = null;
            this.available = true;
        }
    }

    public static class StandardCar extends Vehicle {

        public StandardCar(String name, String registrationNumber) {
            super(name, registrationNumber);
        }

        @Override
        public double dailyRate() {
            return 50.0;
        }
    }

    public static class LuxuryCar extends Vehicle {

        public LuxuryCar(String name, String registrationNumber) {
            super(name, registrationNumber);
        }

        @Override
        public double dailyRate() {
            return 100.0;
        }
    }

    public static class SuvCar extends Vehicle {

        public SuvCar(String name, String registrationNumber) {
            super(name, registrationNumber);
        }

        @Override
        public double dailyRate() {
            return 75.0;
        }
    }

    public static class Rental {
        private final Vehicle vehicle;
        private final Customer customer;
        private final int days;
        private final double totalCharge;
        private boolean returned;

        public Rental(Vehicle vehicle, Customer customer, int days, double totalCharge) {
            this.vehicle = vehicle;
            this.customer = customer;
            this.days = days;
            this.totalCharge = totalCharge;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }

        public Customer getCustomer() {
            return customer;
        }

        public int getDays() {
            return days;
        }

        public double getTotalCharge() {
            return totalCharge;
        }

        public boolean isReturned() {
            return returned;
        }

        void markReturned() {
            this.returned = true;
        }
    }

    public static class RentalService {
        private final List<Rental> rentals = new ArrayList<>();

        public List<Rental> getRentals() {
            return rentals;
        }

        public Rental rent(Vehicle vehicle, Customer customer, int days) {
            if (days <= 0) {
                throw new IllegalArgumentException("Rental duration must be positive.");
            }
            if (!vehicle.isAvailable()) {
                throw new IllegalStateException(
                        "Vehicle " + vehicle.getName() + " is already rented and cannot be double-booked.");
            }
            double totalCharge = vehicle.dailyRate() * days;
            Rental rental = new Rental(vehicle, customer, days, totalCharge);
            vehicle.markRented(rental);
            rentals.add(rental);
            System.out.println(vehicle.getName() + " rented for " + days
                    + (days == 1 ? " day" : " days") + ". Total charge: $"
                    + String.format("%.2f", totalCharge));
            return rental;
        }

        public void returnVehicle(Rental rental) {
            if (rental.isReturned()) {
                System.out.println(rental.getVehicle().getName() + " has already been returned.");
                return;
            }
            rental.markReturned();
            rental.getVehicle().markReturned();
            System.out.println(rental.getVehicle().getName() + " returned. Now available.");
        }
    }

    public static void main(String[] args) {
        RentalService service = new RentalService();
        Customer customer = new Customer("Meera");

        Vehicle luxury = new LuxuryCar("Luxury Car A", "KA-01-A");
        Vehicle standard = new StandardCar("Standard Car B", "KA-02-B");
        Vehicle suv = new SuvCar("SUV Car C", "KA-03-C");

        Rental luxuryRental = service.rent(luxury, customer, 3);
        service.rent(standard, customer, 5);
        service.rent(suv, customer, 2);

        try {
            service.rent(luxury, customer, 1);
        } catch (IllegalStateException e) {
            System.out.println("Rental failed: " + e.getMessage());
        }

        service.returnVehicle(luxuryRental);
        service.rent(luxury, customer, 1);
    }
}
