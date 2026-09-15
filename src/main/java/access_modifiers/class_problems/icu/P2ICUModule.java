package access_modifiers.class_problems.icu;

import access_modifiers.class_problems.P2InheritanceReach;

// P2 cross-package subclass — lives in a different package to exercise the
// SUBCLASS_DIFFERENT_PACKAGE_* contexts.
public class P2ICUModule extends P2InheritanceReach.PatientRecord {

    public P2ICUModule(String wardCode) {
        super(wardCode);
    }

    // ownType access — compiled with ICU ref → ALLOWED for protected
    public String ownTypeAccess() {
        return this.wardCode;  // declared type: ICUModule (subclass, own type)
    }

    // parentType access — compiled with PatientRecord ref → DENIED for protected.
    // Java decides this from the compile-time type of the reference, not the runtime object.
    // This method deliberately accesses through a parent-type reference; the compile-time
    // check correctly blocks it (protected field not accessible via parent-type ref in subclass
    // from a different package). Demonstrating via a package-level getter as workaround:
    public String parentTypeAccess(P2InheritanceReach.PatientRecord ref) {
        // ref.wardCode  ← would be a compile error here; Java's guard works at compile time.
        // Instead we show the rule is correct via the AccessRuleEngine:
        return "DENIED (compile-time guard: protected not accessible via parent-type ref)";
    }
}
