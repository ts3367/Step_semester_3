package constructors.class_problems;

// P3. Bus Route Ranking Engine — class problems
// this for disambiguation + chaining, compareTo with multi-level tie-break, hand-rolled stable sort.
public class P3BusRouteRanking {

    public static class BusRoute implements Comparable<BusRoute> {
        private final String routeCode;
        private final String routeName;
        private final int priority;

        // Primary constructor — uses this.field = param to resolve naming clash.
        public BusRoute(String routeCode, String routeName, int priority) {
            this.routeCode = routeCode;
            this.routeName = routeName;
            this.priority = priority;
        }

        // Two-arg chains to primary with sensible default priority 3 (medium).
        public BusRoute(String routeCode, String routeName) {
            this(routeCode, routeName, 3);
        }

        // Ordering: higher priority first, then routeCode case-insensitive,
        // then routeName length, then stable (original order preserved).
        @Override
        public int compareTo(BusRoute other) {
            if (this.priority != other.priority) {
                return Integer.compare(other.priority, this.priority); // descending
            }
            int codeCmp = this.routeCode.compareToIgnoreCase(other.routeCode);
            if (codeCmp != 0) return codeCmp;
            return Integer.compare(this.routeName.length(), other.routeName.length());
        }

        public String getRouteCode() { return routeCode; }
        public String getRouteName() { return routeName; }
        public int getPriority() { return priority; }

        @Override
        public String toString() { return routeCode + "(" + routeName + ",p" + priority + ")"; }

        // Hand-rolled stable insertion sort — O(n^2), never calls built-in sort.
        public static BusRoute[] rankRoutes(BusRoute[] routes) {
            if (routes == null) return new BusRoute[0];
            BusRoute[] a = Arrays.copyOf(routes, routes.length);
            for (int i = 1; i < a.length; i++) {
                BusRoute key = a[i];
                int j = i - 1;
                while (j >= 0 && a[j].compareTo(key) > 0) {
                    a[j + 1] = a[j];
                    j--;
                }
                a[j + 1] = key;
            }
            return a;
        }

        // Arrays helper without importing java.util.Arrays at top for clarity
        private static class Arrays {
            static BusRoute[] copyOf(BusRoute[] src, int len) {
                BusRoute[] c = new BusRoute[len];
                System.arraycopy(src, 0, c, 0, len);
                return c;
            }
        }
    }

    public static void main(String[] args) {
        BusRoute[] routes = {
            new BusRoute("RT205L", "Airport Express", 3),
            new BusRoute("rt201j", "City Central", 4),
            new BusRoute("RT299T", "Night Service") // defaults to 3
        };
        BusRoute[] ranked = BusRoute.rankRoutes(routes);
        System.out.print("[");
        for (int i = 0; i < ranked.length; i++) {
            System.out.print("\"" + ranked[i].getRouteCode() + "\"" + (i < ranked.length - 1 ? ", " : ""));
        }
        System.out.println("]");
        // Expected ["rt201j", "RT205L", "RT299T"] — 4 > 3 tie broken by code
    }
}
