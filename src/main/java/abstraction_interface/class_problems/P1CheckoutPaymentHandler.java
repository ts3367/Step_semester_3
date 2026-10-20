package abstraction_interface.class_problems;

// P1. Checkout Payment Handler
// PaymentMethod is abstract -> new PaymentMethod() cannot compile.
// CreditCardPayment and CashPayment extend it directly with genuinely different logic.
public class P1CheckoutPaymentHandler {

    // Abstract class: shared state + shared behavior, but no instance on its own.
    // `new PaymentMethod()` is a compile-time error because calculate/payment behaviour
    // is undefined for a generic "payment" with no specific way of paying.
    public abstract static class PaymentMethod {

        // Shared static counter: it advances once per PaymentMethod object,
        // no matter which subclass actually constructed it.
        private static int transactionCounter = 1000;

        private final String transactionId;

        protected PaymentMethod() {
            transactionCounter++;
            this.transactionId = "TXN-" + transactionCounter;
        }

        // Abstract: every way of paying has its own genuinely different logic.
        public abstract String processPayment(double amount);

        // Overload -> compile-time polymorphism: same name, different parameter list.
        // It reuses the single-argument version and glues the note onto the end.
        public String processPayment(double amount, String note) {
            return processPayment(amount) + " (" + note + ")";
        }

        public String getTransactionId() {
            return transactionId;
        }
    }

    public static class CreditCardPayment extends PaymentMethod {
        private final String cardNumberLastFour;

        public CreditCardPayment(String cardNumberLastFour) {
            this.cardNumberLastFour = cardNumberLastFour;
        }

        @Override
        public String processPayment(double amount) {
            return "Charged $" + amount + " to card ending " + cardNumberLastFour
                    + " - Txn " + getTransactionId();
        }
    }

    public static class CashPayment extends PaymentMethod {

        public CashPayment() {
            // no extra state of its own
        }

        @Override
        public String processPayment(double amount) {
            return "Received $" + amount + " in cash - Txn " + getTransactionId();
        }
    }

    // Prints the confirmation without checking which subclass it actually is.
    public static void printConfirmation(PaymentMethod payment, double amount) {
        System.out.println(payment.processPayment(amount));
    }

    public static void main(String[] args) {
        CreditCardPayment cc = new CreditCardPayment("4471");
        System.out.println(cc.processPayment(250.0));
        // Expected: Charged $250.0 to card ending 4471 - Txn TXN-1001

        CashPayment cash = new CashPayment();
        System.out.println(cash.processPayment(40.0));
        // Expected: Received $40.0 in cash - Txn TXN-1002

        // Overloaded version - resolved at COMPILE time by the number of arguments.
        System.out.println(cc.processPayment(250.0, "Birthday gift"));
        // Expected: Charged $250.0 to card ending 4471 - Txn TXN-1001 (Birthday gift)

        // Upcasting: a CreditCardPayment reference stored as its parent type.
        PaymentMethod ref = cc;
        printConfirmation(ref, 250.0);
        // Expected: Charged $250.0 to card ending 4471 - Txn TXN-1001
    }
}
