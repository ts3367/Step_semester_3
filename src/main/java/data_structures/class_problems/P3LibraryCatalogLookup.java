package data_structures.class_problems;

import java.util.List;

// Library Catalog Lookup. Binary search over a catalog pre-sorted by ISBN.
// Complexity: O(log n) time, O(1) space. Queries vastly outnumber updates, so
// paying the one-time O(n log n) sort is never worth repeating.
public class P3LibraryCatalogLookup {

    public record BookRecord(String isbn, String title) {
    }

    public static int findBookIndex(List<BookRecord> catalog, String targetIsbn) {
        int low = 0;
        int high = catalog.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int comparison = catalog.get(mid).isbn().compareTo(targetIsbn);
            if (comparison == 0) {
                return mid;
            }
            if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    public static String findBook(List<BookRecord> catalog, String targetIsbn) {
        int index = findBookIndex(catalog, targetIsbn);
        return index < 0 ? "Not Found" : catalog.get(index).title();
    }

    public static void main(String[] args) {
        List<BookRecord> catalog = List.of(
                new BookRecord("0001112223", "Introduction to Algebra"),
                new BookRecord("0002223334", "Beginning Python"),
                new BookRecord("0003334445", "Classic Mythology"),
                new BookRecord("0004445556", "Data and Society"),
                new BookRecord("0005556667", "European History"));

        System.out.println(findBook(catalog, "0003334445"));
        System.out.println(findBook(catalog, "0009998887"));
    }
}
