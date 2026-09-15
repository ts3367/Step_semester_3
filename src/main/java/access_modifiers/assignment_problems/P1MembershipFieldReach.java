package access_modifiers.assignment_problems;

// P1. Membership Field Reach Checker
// AccessChecker classifies access attempts; LibraryMember validates at construction.
// summarizeByModifier groups results per modifier instead of flat total.
public class P1MembershipFieldReach {

    private static final boolean[][] RULE_TABLE = {
        // SAME_CLASS  SAME_PACKAGE  DIFFERENT_PACKAGE
        { true,  false, false },  // private
        { true,  true,  false },  // default
        { true,  true,  false },  // protected
        { true,  true,  true  },  // public
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
            default -> -1;
        };
        if (modRow < 0 || ctxCol < 0) return "UNKNOWN";
        return RULE_TABLE[modRow][ctxCol] ? "ALLOWED" : "DENIED";
    }

    // Groups results per modifier: "private: 1 allowed / 1 denied | default: ..."
    public static String summarizeByModifier(String[][] attempts) {
        int[] allowed  = new int[4]; // private, default, protected, public
        int[] denied   = new int[4];
        String[] names = {"private", "default", "protected", "public"};

        for (String[] row : attempts) {
            int idx = switch (row[0]) {
                case "private" -> 0;
                case "default" -> 1;
                case "protected" -> 2;
                case "public" -> 3;
                default -> -1;
            };
            if (idx < 0) continue;
            if ("ALLOWED".equals(classifyAccess(row[0], row[1]))) allowed[idx]++;
            else denied[idx]++;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (i > 0) sb.append(" | ");
            sb.append(names[i]).append(": ").append(allowed[i])
              .append(" allowed / ").append(denied[i]).append(" denied");
        }
        return sb.toString();
    }

    // LibraryMember: membershipId private (unique), branchCode protected (package+subclass),
    // finesOwed private (sensitive), displayName public (info).
    // No usable no-arg constructor.
    public static class LibraryMember {
        private final String membershipId;
        protected final String branchCode;
        private final double finesOwed;
        public final String displayName;

        public LibraryMember(String membershipId, String branchCode, double finesOwed, String displayName) {
            if (membershipId == null || membershipId.trim().isEmpty() || membershipId.trim().length() < 4)
                throw new IllegalArgumentException("membershipId rejected");
            this.membershipId = membershipId.trim();
            this.branchCode = branchCode;
            this.finesOwed = finesOwed;
            this.displayName = displayName;
        }

        public String getMembershipId() { return membershipId; }
        public double getFinesOwed() { return finesOwed; }
    }

    public static void main(String[] args) {
        // Example 1
        System.out.println(classifyAccess("private", "SAME_CLASS"));  // ALLOWED
        // Example 2
        System.out.println(classifyAccess("protected", "DIFFERENT_PACKAGE")); // DENIED
        // Example 3 — per-modifier summary
        System.out.println(summarizeByModifier(new String[][] {
            {"private", "SAME_CLASS"},
            {"private", "SAME_PACKAGE"},
            {"default", "SAME_PACKAGE"},
            {"default", "DIFFERENT_PACKAGE"},
            {"protected", "SAME_PACKAGE"},
            {"protected", "SAME_CLASS"},
            {"public", "DIFFERENT_PACKAGE"}
        }));
        // Expected: private: 1 allowed / 1 denied | default: 1 allowed / 1 denied | protected: 2 allowed / 0 denied | public: 1 allowed / 0 denied
        // Example 4 — LB9 is 3 chars, rejected
        try {
            new LibraryMember("LB9", "BR1", 0, "Priya Nair");
            System.out.println("constructed");
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }
    }
}
