package system_design.class_problems;

// Week 8 practice concept answers.
public class P7ConceptAnswers {

    public record Concept(int question, String questionTitle, String answer) {
    }

    private static final Concept[] CONCEPTS = {
            new Concept(1, "Invariants in a ShoppingCart",
                    "An invariant is a condition that must hold for every valid object of a class, and for ShoppingCart "
                            + "the invariant is total_price = sum of the LineItem prices. Encapsulation enforces it by "
                            + "keeping total_price and the line items private and exposing only operations such as "
                            + "addItem and removeItem, each of which updates the line items and recomputes the total "
                            + "together. No outside code can set the total directly, so the cart can never reach the "
                            + "inconsistent state where the total disagrees with its contents."),
            new Concept(2, "Composition for optional SmartDevice capabilities",
                    "Modeling every capability combination with inheritance multiplies the class count (camera only, "
                            + "GPS only, camera + GPS, and so on) and fixes the set of capabilities at compile time, so "
                            + "adding a Timer or attaching a camera later would need new subclasses. With composition a "
                            + "SmartDevice simply holds a collection of capability objects and can gain, lose or "
                            + "recombine them at runtime, so a device with both camera and GPS and a different device "
                            + "with camera, GPS and timer are the same class with different parts. The device keeps the "
                            + "behaviour it always has and delegates the optional behaviour, so the hierarchy stays flat "
                            + "and extensible."),
            new Concept(3, "Sequence diagram for the checkout payment flow",
                    "The diagram would show four lifelines along the horizontal axis: CheckoutManager, PaymentGateway, "
                            + "BankService and the customer or order. The messages are drawn as arrows ordered top to "
                            + "bottom in time: the CheckoutManager sends initiatePayment to the PaymentGateway, the "
                            + "PaymentGateway calls the BankService, the BankService returns a result message to the "
                            + "PaymentGateway, and the PaymentGateway replies with the payment status to the "
                            + "CheckoutManager, which finally updates the order. The vertical order of the arrows shows "
                            + "message ordering, the horizontal direction shows the sender and receiver, and a dashed "
                            + "arrow with an open head marks a return message. This makes the dynamic collaboration and "
                            + "the exact order of the calls visible, which a static class diagram cannot show."),
            new Concept(4, "Abstraction in the NotificationService",
                    "The client depends only on the NotificationSender abstraction, so it holds a reference to the "
                            + "interface and calls send(message) without knowing the mechanism. Each concrete sender "
                            + "such as EmailSender, SmsSender or PushSender implements that contract in its own way, and "
                            + "the client code is identical for all of them. Because the workflow is written against "
                            + "the contract, adding a new messaging app integration means writing one new "
                            + "implementation and registering it, with no change to the client, the service or the "
                            + "existing senders. This is dependency on abstractions, and it keeps the service open for "
                            + "extension while closed for modification."),
            new Concept(5, "Aggregation versus composition in a University",
                    "Aggregation is a weak whole-part relationship where the parts can exist independently of the "
                            + "whole, while composition is a strong one where the parts are owned by the whole and "
                            + "cannot exist without it. In the scenario, a University owns its Departments, so the "
                            + "University to Department relationship is composition: if the University ceases to exist, "
                            + "its Departments cease to exist with it. A Department holds Professors, but the "
                            + "Professors survive the Department and continue their careers at other institutions, so "
                            + "that relationship is aggregation. The difference is ownership and lifecycle dependency: "
                            + "composition means the container controls creation and destruction of its parts, whereas "
                            + "aggregation only means the container refers to parts that have their own independent "
                            + "lifetime.")
    };

    public static Concept[] getConcepts() {
        return CONCEPTS.clone();
    }

    public static void main(String[] args) {
        for (Concept concept : CONCEPTS) {
            System.out.println("Q" + concept.question() + " - " + concept.questionTitle());
            System.out.println(concept.answer());
            System.out.println();
        }
    }
}
