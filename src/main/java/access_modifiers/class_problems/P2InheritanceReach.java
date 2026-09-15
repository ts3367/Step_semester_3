package access_modifiers.class_problems;

// P2. Cross-Package Inheritance Reach — main package (MediTrack module)
// Handles 5 contexts including SUBCLASS_DIFFERENT_PACKAGE_*.
// The ICU subclass lives in access_modifiers.class_problems.icu.
public class P2InheritanceReach {

    // Extended rule table: now 5 contexts.
    // SUBCLASS_DIFFERENT_PACKAGE_OWNTYPE  — protected ALLOWED
    // SUBCLASS_DIFFERENT_PACKAGE_PARENTTYPE — protected DENIED (compile-time type is parent)
    private static final boolean[][] RULE_TABLE = {
        // SAME_CLASS  SAME_PACKAGE  DIFFERENT_PACKAGE  SUB_DIFF_PKG_OWN  SUB_DIFF_PKG_PARENT
        { true,  false, false, false, false },  // private
        { true,  true,  false, false, false },  // default
        { true,  true,  false, true,  false },  // protected
        { true,  true,  true,  true,  true  },  // public
    };

    public static String classifyAccess(String fieldModifier, String accessorContext) {
        int modRow = switch (fieldModifier) {
            case "private" -> 0;
            case "default" -> 1;
            case "protected" -> 2;
            case "public" -> 3;
            default -> -1;
        };
        int ctxCol = switch (accessorContext) {
            case "SAME_CLASS" -> 0;
            case "SAME_PACKAGE" -> 1;
            case "DIFFERENT_PACKAGE" -> 2;
            case "SUBCLASS_DIFFERENT_PACKAGE_OWNTYPE" -> 3;
            case "SUBCLASS_DIFFERENT_PACKAGE_PARENTTYPE" -> 4;
            default -> -1;
        };
        if (modRow < 0 || ctxCol < 0) return "UNKNOWN";
        return RULE_TABLE[modRow][ctxCol] ? "ALLOWED" : "DENIED";
    }

    // Human-readable context: split on "_", capitalise each word.
    public static String describeContext(String accessorContext) {
        StringBuilder sb = new StringBuilder();
        for (String part : accessorContext.split("_")) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(Character.toUpperCase(part.charAt(0)))
              .append(part.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    // PatientRecord protected wardCode is accessible by icu.ICU module subclass
    // via own-type reference (P2 example: ALLOWED), but denied via parent-type
    // reference (P2 example: DENIED). Demonstrated in P2ICUModule.
    public static class PatientRecord {
        protected String wardCode;

        public PatientRecord(String wardCode) {
            this.wardCode = wardCode;
        }
    }

    public static void main(String[] args) {
        System.out.println(classifyAccess("protected", "SUBCLASS_DIFFERENT_PACKAGE_OWNTYPE"));   // ALLOWED
        System.out.println(classifyAccess("protected", "SUBCLASS_DIFFERENT_PACKAGE_PARENTTYPE")); // DENIED
        System.out.println(describeContext("SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE"));            // Subclass Different Package Parent Type
    }
}
