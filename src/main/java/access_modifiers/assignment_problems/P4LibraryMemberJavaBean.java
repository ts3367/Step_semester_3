package access_modifiers.assignment_problems;

// P4. LibraryMember JavaBean, Chained Constructors & Security Answer
// Write-once membershipId (public setter, second call silently ignored).
// Write-only securityAnswer (setter exists, no getter anywhere).
public class P4LibraryMemberJavaBean {

    public static class LibraryMember {
        private String membershipId;
        private String name;
        private boolean premiumMember;
        private String securityAnswer;  // write-only — no getter

        // All three constructors chained via this(...) — single init path.
        public LibraryMember() {
            this(null, null);
        }

        public LibraryMember(String name) {
            this(null, name);
        }

        public LibraryMember(String membershipId, String name) {
            this.membershipId = membershipId;  // may be null (intake hasn't happened yet)
            this.name = name;
            this.premiumMember = false;
            this.securityAnswer = null;
        }

        public String getMembershipId() { return membershipId; }

        // Write-once: takes effect only the first time.
        public void setMembershipId(String id) {
            if (this.membershipId == null) {
                this.membershipId = id;
            }
            // second+ calls silently ignored
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public boolean isPremiumMember() { return premiumMember; }
        public void setPremiumMember(boolean premium) { this.premiumMember = premium; }

        // Write-only: setter exists, no matching getter anywhere.
        public void setSecurityAnswer(String answer) { this.securityAnswer = answer; }
    }

    public static void main(String[] args) {
        // Example 1: name-only → membershipId is null
        System.out.println(new LibraryMember("Priya Nair").getMembershipId()); // null
        // Example 2: id+name → returns the id
        System.out.println(new LibraryMember("LIB-8841", "Priya Nair").getMembershipId()); // LIB-8841
        // Example 3: write-once setter
        LibraryMember m = new LibraryMember();
        m.setMembershipId("LIB-8841");
        m.setMembershipId("FAKE-0000"); // silently ignored
        System.out.println(m.getMembershipId()); // LIB-8841
    }
}
