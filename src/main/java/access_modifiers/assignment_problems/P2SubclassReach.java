package access_modifiers.assignment_problems;

// P2. Reference Desk Subclass Reach — main package (PageTurner module)
// Extended AccessChecker with 5 contexts + describeContext.
// The premium module subclass lives in access_modifiers.assignment_problems.premium.
public class P2SubclassReach {

    private static final boolean[][] RULE_TABLE = {
        // SAME_CLASS  SAME_PACKAGE  DIFFERENT_PKG  SUB_DIFF_PKG_OWN  SUB_DIFF_PKG_PARENT
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

    public static String describeContext(String accessorContext) {
        StringBuilder sb = new StringBuilder();
        for (String part : accessorContext.split("_")) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(Character.toUpperCase(part.charAt(0)))
              .append(part.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    public static class LibraryMember {
        protected double finesOwed;

        public LibraryMember(double finesOwed) {
            this.finesOwed = finesOwed;
        }
    }

    public static void main(String[] args) {
        // Example 1
        System.out.println(classifyAccess("protected", "SUBCLASS_DIFFERENT_PACKAGE_OWNTYPE"));   // ALLOWED
        // Example 2
        System.out.println(classifyAccess("protected", "SUBCLASS_DIFFERENT_PACKAGE_PARENTTYPE")); // DENIED
        // Example 3
        System.out.println(describeContext("SUBCLASS_DIFFERENT_PACKAGE_OWNTYPE")); // Subclass Different Package Own Type
    }
}
