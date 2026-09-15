package constructors.assignment_problems;

// P3. Canteen Trust-Score Ranking Engine — assignment
// this for clash, chained constructor, compareTo with 3-level tie-break, hand-rolled stable sort.
public class P3CanteenRanking {

    public static class Canteen implements Comparable<Canteen> {
        private final String canteenCode;
        private final String canteenName;
        private final int trustScore;

        // Primary — resolves field/param clash with this.
        public Canteen(String canteenCode, String canteenName, int trustScore) {
            this.canteenCode = canteenCode;
            this.canteenName = canteenName;
            this.trustScore = trustScore;
        }

        // Chains with sensible default trustScore 3.
        public Canteen(String canteenCode, String canteenName) {
            this(canteenCode, canteenName, 3);
        }

        // Tier 1: trustScore descending. Tier 2: code case-insensitive. Tier 3: name length.
        // Stable sort ensures identical entries keep original order.
        @Override
        public int compareTo(Canteen other) {
            if (this.trustScore != other.trustScore)
                return Integer.compare(other.trustScore, this.trustScore);
            int codeCmp = this.canteenCode.compareToIgnoreCase(other.canteenCode);
            if (codeCmp != 0) return codeCmp;
            return Integer.compare(this.canteenName.length(), other.canteenName.length());
        }

        public String getCanteenCode() { return canteenCode; }
        public String getCanteenName() { return canteenName; }
        public int getTrustScore() { return trustScore; }

        // Hand-rolled insertion sort — O(n^2), stable, no built-in sort.
        public static Canteen[] rankCanteens(Canteen[] canteens) {
            if (canteens == null) return new Canteen[0];
            Canteen[] a = new Canteen[canteens.length];
            System.arraycopy(canteens, 0, a, 0, canteens.length);
            for (int i = 1; i < a.length; i++) {
                Canteen key = a[i];
                int j = i - 1;
                while (j >= 0 && a[j].compareTo(key) > 0) {
                    a[j + 1] = a[j];
                    j--;
                }
                a[j + 1] = key;
            }
            return a;
        }
    }

    public static void main(String[] args) {
        Canteen[] canteens = {
            new Canteen("HB3-C", "Spice Junction", 3),
            new Canteen("hb1-c", "Grand Mess", 5),
            new Canteen("HB2-C", "Southern Treats") // defaults to 3
        };
        Canteen[] ranked = Canteen.rankCanteens(canteens);
        System.out.print("[");
        for (int i = 0; i < ranked.length; i++) {
            System.out.print("\"" + ranked[i].getCanteenCode() + "\"" + (i < ranked.length - 1 ? ", " : ""));
        }
        System.out.println("]");
        // Expected ["hb1-c", "HB2-C", "HB3-C"] — Grand Mess first, tie broken by code
    }
}
