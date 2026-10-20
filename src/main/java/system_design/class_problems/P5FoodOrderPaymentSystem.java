package system_design.class_problems;

import java.util.ArrayList;
import java.util.List;

// Food Order and Flexible Payment System. Payment methods are an interface
// dependency, order items are a composition owned by the Order.
public class P5FoodOrderPaymentSystem {

    public interface PaymentMethod {
        String getMethodName();

        boolean processPayment(double amount);
    }

    public static class CreditCardPayment implements PaymentMethod {
        private final String cardNumber;

        public CreditCardPayment(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        @Override
        public String getMethodName() {
            return "Credit Card";
        }

        @Override
        public boolean processPayment(double amount) {
            return true;
        }
    }

    public static class DigitalWalletPayment implements PaymentMethod {
        private final String walletId;

        public DigitalWalletPayment(String walletId) {
            this.walletId = walletId;
        }

        @Override
        public String getMethodName() {
            return "Digital Wallet";
        }

        @Override
        public boolean processPayment(double amount) {
            return false;
        }
    }

    public static class CashOnDeliveryPayment implements PaymentMethod {
        @Override
        public String getMethodName() {
            return "Cash on Delivery";
        }

        @Override
        public boolean processPayment(double amount) {
            return true;
        }
    }

    public static class Restaurant {
        private final String name;

        public Restaurant(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class FoodItem {
        private final String name;
        private final double price;

        public FoodItem(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }

    public static class LineItem {
        private final FoodItem foodItem;
        private final int quantity;

        public LineItem(FoodItem foodItem, int quantity) {
            this.foodItem = foodItem;
            this.quantity = quantity;
        }

        public FoodItem getFoodItem() {
            return foodItem;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getLineTotal() {
            return foodItem.getPrice() * quantity;
        }

        public String describe() {
            return foodItem.getName() + " (Qty " + quantity + ")";
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

    public interface NotificationSink {
        void notify(String message);
    }

    public static class CustomerNotifier implements NotificationSink {
        private final Customer customer;

        public CustomerNotifier(Customer customer) {
            this.customer = customer;
        }

        @Override
        public void notify(String message) {
            System.out.println("Notification: " + message);
        }
    }

    public enum OrderStatus {
        DRAFT("Draft"), PLACED("Placed"), PAID("Paid"), PENDING_PAYMENT("Pending Payment");

        private final String label;

        OrderStatus(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public static class Order {
        private static int orderCounter = 122;

        private final Customer customer;
        private final Restaurant restaurant;
        private final NotificationSink notificationSink;
        private final List<LineItem> lineItems = new ArrayList<>();
        private int orderId;
        private OrderStatus status = OrderStatus.DRAFT;

        public Order(Customer customer, Restaurant restaurant, NotificationSink notificationSink) {
            this.customer = customer;
            this.restaurant = restaurant;
            this.notificationSink = notificationSink;
        }

        public int getOrderId() {
            return orderId;
        }

        public Customer getCustomer() {
            return customer;
        }

        public Restaurant getRestaurant() {
            return restaurant;
        }

        public List<LineItem> getLineItems() {
            return lineItems;
        }

        public OrderStatus getStatus() {
            return status;
        }

        public double getTotal() {
            double total = 0;
            for (LineItem lineItem : lineItems) {
                total += lineItem.getLineTotal();
            }
            return total;
        }

        public void addItem(FoodItem foodItem, int quantity) {
            lineItems.add(new LineItem(foodItem, quantity));
        }

        public String describeItems() {
            StringBuilder sb = new StringBuilder();
            for (LineItem lineItem : lineItems) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(lineItem.describe());
            }
            return sb.toString();
        }

        public boolean place(PaymentMethod paymentMethod) {
            if (lineItems.isEmpty()) {
                System.out.println("Cannot place order: Order must contain at least one item.");
                return false;
            }
            orderId = ++orderCounter;
            status = OrderStatus.PLACED;
            double total = getTotal();
            if (paymentMethod.processPayment(total)) {
                status = OrderStatus.PAID;
                System.out.println("Order placed successfully.");
                System.out.println("Payment via " + paymentMethod.getMethodName() + " successful.");
                System.out.println("Order status: " + status.getLabel() + ".");
                notificationSink.notify("Order #" + orderId + " placed and paid.");
                return true;
            }
            status = OrderStatus.PENDING_PAYMENT;
            System.out.println("Order placed.");
            System.out.println("Payment via " + paymentMethod.getMethodName() + " failed.");
            System.out.println("Order status: " + status.getLabel() + ".");
            notificationSink.notify("Order #" + orderId + " placed, awaiting payment.");
            return false;
        }
    }

    public static void main(String[] args) {
        Customer customer = new Customer("Divya");
        Restaurant restaurant = new Restaurant("Spice Route");
        NotificationSink notifier = new CustomerNotifier(customer);

        FoodItem pizza = new FoodItem("Pizza", 250.0);
        FoodItem soda = new FoodItem("Soda", 40.0);
        FoodItem burger = new FoodItem("Burger", 180.0);

        Order pizzaOrder = new Order(customer, restaurant, notifier);
        System.out.println("Order created.");
        pizzaOrder.addItem(pizza, 2);
        pizzaOrder.addItem(soda, 1);
        System.out.println("Added " + pizzaOrder.describeItems() + ".");

        Order emptyOrder = new Order(customer, restaurant, notifier);
        emptyOrder.place(new CreditCardPayment("4471"));

        pizzaOrder.place(new CreditCardPayment("4471"));

        Order burgerOrder = new Order(customer, restaurant, notifier);
        burgerOrder.addItem(burger, 1);
        burgerOrder.place(new DigitalWalletPayment("wallet-9"));
    }
}
