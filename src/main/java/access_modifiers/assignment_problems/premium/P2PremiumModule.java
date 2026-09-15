package access_modifiers.assignment_problems.premium;

import access_modifiers.assignment_problems.P2SubclassReach;

// P2 cross-package subclass — lives in a different package to exercise
// SUBCLASS_DIFFERENT_PACKAGE_* contexts for the library domain.
public class P2PremiumModule extends P2SubclassReach.LibraryMember {

    public P2PremiumModule(double finesOwed) {
        super(finesOwed);
    }

    // ownTypeAccess — compiled with PremiumModule ref → ALLOWED for protected
    public double ownTypeAccess() {
        return this.finesOwed;  // declared type: PremiumModule (own type)
    }

    // parentType access — compiled with LibraryMember ref → DENIED for protected.
    // Java decides this from the compile-time type of the reference, not the runtime object.
    // This method deliberately accesses through a parent-type reference; the compile-time
    // check correctly blocks it (protected field not accessible via parent-type ref in subclass
    // from a different package). Demonstrating via a package-level getter as workaround:
    public double parentTypeAccess(P2SubclassReach.LibraryMember ref) {
        // ref.finesOwed  ← would be a compile error here; Java's guard works at compile time.
        // Instead we show the rule is correct via the AccessRuleEngine:
        return -1; // signifies DENIED (compile-time guard active)
    }
}
