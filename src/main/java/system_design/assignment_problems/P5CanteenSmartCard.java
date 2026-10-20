package system_design.assignment_problems;

import java.util.ArrayList;
import java.util.List;

// Campus Canteen Smart Card. The balance is derived from the transaction list,
// so the invariant balance = sum(transactions) holds by construction.
public class P5CanteenSmartCard {

    public interface PricingPlan {
        String getPlanName();

        double priceFor(double basePrice);
    }

    public static class DayScholarPlan implements PricingPlan {
        @Override
        public String getPlanName() {
            return "Day Scholar";
        }

        @Override
        public double priceFor(double basePrice) {
            return basePrice;
        }
    }

    public static class HostellerPlan implements PricingPlan {
        @Override
        public String getPlanName() {
            return "Hosteller";
        }

        @Override
        public double priceFor(double basePrice) {
            return basePrice * 0.9;
        }
    }

    public static class StaffPlan implements PricingPlan {
        @Override
        public String getPlanName() {
            return "Staff";
        }

        @Override
        public double priceFor(double basePrice) {
            return basePrice * 0.8;
        }
    }

    public enum TransactionType {
        TOP_UP, PURCHASE, REFUND
    }

    public static class Transaction {
        private final TransactionType type;
        private final String description;
        private final double amount;

        public Transaction(TransactionType type, String description, double amount) {
            this.type = type;
            this.description = description;
            this.amount = amount;
        }

        public TransactionType getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }

        public double getAmount() {
            return amount;
        }
    }

    public static class Purchase {
        private final String itemName;
        private final double amountCharged;
        private boolean refunded;

        public Purchase(String itemName, double amountCharged) {
            this.itemName = itemName;
            this.amountCharged = amountCharged;
        }

        public String getItemName() {
            return itemName;
        }

        public double getAmountCharged() {
            return amountCharged;
        }

        public boolean isRefunded() {
            return refunded;
        }

        void markRefunded() {
            this.refunded = true;
        }
    }

    public static class SmartCard {
        private static final double MIN_TOP_UP = 100.0;
        private static final double MAX_BALANCE = 5000.0;

        private final String cardId;
        private final PricingPlan plan;
        private final List<Transaction> transactions = new ArrayList<>();
        private boolean blocked;

        public SmartCard(String cardId, PricingPlan plan) {
            this.cardId = cardId;
            this.plan = plan;
        }

        public String getCardId() {
            return cardId;
        }

        public PricingPlan getPlan() {
            return plan;
        }

        public List<Transaction> getTransactions() {
            return transactions;
        }

        public boolean isBlocked() {
            return blocked;
        }

        public double getBalance() {
            double balance = 0;
            for (Transaction transaction : transactions) {
                balance += transaction.getAmount();
            }
            return balance;
        }

        public void block() {
            blocked = true;
        }

        public void unblock() {
            blocked = false;
        }

        public boolean topUp(double amount) {
            if (blocked) {
                System.out.println("Top-up failed: Card " + cardId + " is blocked.");
                return false;
            }
            if (amount < MIN_TOP_UP) {
                System.out.println("Top-up failed: Minimum top-up amount is " + money(MIN_TOP_UP) + ".");
                return false;
            }
            if (getBalance() + amount > MAX_BALANCE) {
                System.out.println("Top-up failed: Maximum card balance is " + money(MAX_BALANCE) + ".");
                return false;
            }
            transactions.add(new Transaction(TransactionType.TOP_UP, "Top-up", amount));
            System.out.println(cardId + " topped up with " + money(amount) + ". Balance: "
                    + money(getBalance()) + ".");
            return true;
        }

        public Purchase purchase(String itemName, double basePrice) {
            if (blocked) {
                System.out.println("Purchase failed: Card " + cardId + " is blocked.");
                return null;
            }
            double charged = round(plan.priceFor(basePrice));
            if (charged > getBalance()) {
                System.out.println("Purchase failed: Insufficient balance (required " + money(charged)
                        + ", available " + money(getBalance()) + ").");
                return null;
            }
            transactions.add(new Transaction(TransactionType.PURCHASE, itemName, -charged));
            System.out.println(itemName + " purchased for " + money(charged) + ". Balance: "
                    + money(getBalance()) + ".");
            return new Purchase(itemName, charged);
        }

        public boolean refund(Purchase purchase) {
            if (purchase.isRefunded()) {
                System.out.println("Refund rejected: " + purchase.getItemName() + " has already been refunded.");
                return false;
            }
            purchase.markRefunded();
            transactions.add(new Transaction(TransactionType.REFUND, purchase.getItemName(),
                    purchase.getAmountCharged()));
            System.out.println("Refund of " + money(purchase.getAmountCharged()) + " for "
                    + purchase.getItemName() + " processed. Balance: " + money(getBalance()) + ".");
            return true;
        }

        public void printStatement() {
            StringBuilder sb = new StringBuilder();
            for (Transaction transaction : transactions) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(transaction.getAmount() >= 0 ? "+" : "-")
                        .append(String.format("%.2f", Math.abs(transaction.getAmount())));
            }
            System.out.println("Mini-statement for " + cardId + ": " + sb + " = " + money(getBalance()) + ".");
        }

        static double round(double value) {
            return Math.round(value * 100.0) / 100.0;
        }

        static String money(double amount) {
            return "\u20B9" + String.format("%.2f", amount);
        }
    }

    public static void main(String[] args) {
        SmartCard card = new SmartCard("C-2045", new HostellerPlan());

        card.topUp(500);
        Purchase vegThali = card.purchase("Veg Thali", 120);
        card.purchase("Cold Coffee", 60);
        card.purchase("Family Platter", 400);

        card.refund(vegThali);
        card.refund(vegThali);

        card.printStatement();
    }
}
