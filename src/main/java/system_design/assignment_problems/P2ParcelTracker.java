package system_design.assignment_problems;

import java.util.ArrayList;
import java.util.List;

// SwiftShip Parcel Tracker. Parcel owns the allowed status transitions and the
// Fragile rate reuses the Standard rate by composition.
public class P2ParcelTracker {

    public enum ParcelStatus {
        BOOKED, PICKED_UP, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED;

        public boolean isCancellable() {
            return this == BOOKED;
        }
    }

    public interface ShippingType {
        String getTypeName();

        double calculateCharge(double weightKg);
    }

    public static class StandardShipping implements ShippingType {
        @Override
        public String getTypeName() {
            return "Standard";
        }

        @Override
        public double calculateCharge(double weightKg) {
            return 40 + 10 * weightKg;
        }
    }

    public static class ExpressShipping implements ShippingType {
        @Override
        public String getTypeName() {
            return "Express";
        }

        @Override
        public double calculateCharge(double weightKg) {
            return 80 + 15 * weightKg;
        }
    }

    public static class FragileShipping implements ShippingType {
        private final ShippingType baseRate = new StandardShipping();

        @Override
        public String getTypeName() {
            return "Fragile";
        }

        @Override
        public double calculateCharge(double weightKg) {
            return baseRate.calculateCharge(weightKg) + 50;
        }
    }

    public interface NotificationChannel {
        String getChannelName();

        void notify(String message);
    }

    public static class SmsChannel implements NotificationChannel {
        @Override
        public String getChannelName() {
            return "SMS";
        }

        @Override
        public void notify(String message) {
            System.out.println("[" + getChannelName() + "] " + message);
        }
    }

    public static class EmailChannel implements NotificationChannel {
        @Override
        public String getChannelName() {
            return "Email";
        }

        @Override
        public void notify(String message) {
            System.out.println("[" + getChannelName() + "] " + message);
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

    public static class Parcel {
        private final String parcelId;
        private final Customer customer;
        private final ShippingType shippingType;
        private final double weightKg;
        private final double charge;
        private final List<NotificationChannel> channels = new ArrayList<>();
        private ParcelStatus status = ParcelStatus.BOOKED;
        private boolean cancelled;

        public Parcel(String parcelId, Customer customer, ShippingType shippingType, double weightKg) {
            this.parcelId = parcelId;
            this.customer = customer;
            this.shippingType = shippingType;
            this.weightKg = weightKg;
            this.charge = shippingType.calculateCharge(weightKg);
        }

        public String getParcelId() {
            return parcelId;
        }

        public Customer getCustomer() {
            return customer;
        }

        public ShippingType getShippingType() {
            return shippingType;
        }

        public double getWeightKg() {
            return weightKg;
        }

        public double getCharge() {
            return charge;
        }

        public ParcelStatus getStatus() {
            return status;
        }

        public void subscribe(NotificationChannel channel) {
            channels.add(channel);
        }

        private boolean isValidTransition(ParcelStatus target) {
            List<ParcelStatus> order = List.of(ParcelStatus.values());
            int currentIndex = order.indexOf(status);
            int targetIndex = order.indexOf(target);
            return targetIndex == currentIndex + 1;
        }

        public void advanceTo(ParcelStatus target) {
            if (!isValidTransition(target)) {
                throw new IllegalStateException("Invalid transition: " + status + " \u2192 " + target
                        + " is not allowed.");
            }
            this.status = target;
            announceStatus();
        }

        public void cancel() {
            if (cancelled) {
                throw new IllegalStateException("Cancellation failed: " + parcelId + " is already cancelled.");
            }
            if (!status.isCancellable()) {
                throw new IllegalStateException("Cancellation failed: " + parcelId
                        + " can be cancelled only while BOOKED.");
            }
            this.cancelled = true;
            announceStatus();
        }

        public void announceStatus() {
            String message = parcelId + " is now " + (cancelled ? "CANCELLED" : status.name()) + ".";
            for (NotificationChannel channel : channels) {
                channel.notify(message);
            }
        }
    }

    public static class ParcelService {
        private final List<Parcel> parcels = new ArrayList<>();

        public List<Parcel> getParcels() {
            return parcels;
        }

        public Parcel book(Customer customer, String parcelId, ShippingType shippingType, double weightKg,
                           List<NotificationChannel> channels) {
            Parcel parcel = new Parcel(parcelId, customer, shippingType, weightKg);
            for (NotificationChannel channel : channels) {
                parcel.subscribe(channel);
            }
            parcels.add(parcel);
            System.out.println("Parcel " + parcelId + " booked (" + shippingType.getTypeName() + ", "
                    + (int) weightKg + " kg). Charge: " + money(parcel.getCharge()) + ".");
            parcel.announceStatus();
            return parcel;
        }

        public void markPickedUp(Parcel parcel) {
            parcel.advanceTo(ParcelStatus.PICKED_UP);
        }

        public void markInTransit(Parcel parcel) {
            parcel.advanceTo(ParcelStatus.IN_TRANSIT);
        }

        public void markOutForDelivery(Parcel parcel) {
            parcel.advanceTo(ParcelStatus.OUT_FOR_DELIVERY);
        }

        public void markDelivered(Parcel parcel) {
            parcel.advanceTo(ParcelStatus.DELIVERED);
        }

        static String money(double amount) {
            return "\u20B9" + String.format("%.2f", amount);
        }
    }

    public static void main(String[] args) {
        ParcelService service = new ParcelService();
        Customer customer = new Customer("Kavya");
        List<NotificationChannel> channels = List.of(new SmsChannel(), new EmailChannel());

        Parcel parcel = service.book(customer, "P101", new ExpressShipping(), 2, channels);

        service.markPickedUp(parcel);

        try {
            parcel.cancel();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        service.markInTransit(parcel);

        try {
            service.markDelivered(parcel);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        service.markOutForDelivery(parcel);
        service.markDelivered(parcel);
    }
}
