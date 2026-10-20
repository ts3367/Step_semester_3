package data_structures.class_problems;

import java.util.List;

// Library Catalog Lookup, restated in the sheet as Problem 4 with the same task
// as Problem 3. This variant resolves the record by lower bound instead of a
// midpoint search, which is the form used when the catalog may hold duplicate
// ISBN entries.
// Complexity: O(log n) time, O(1) space.
public class P4LibraryCatalogLookup {

    public static int lowerBound(List<P3LibraryCatalogLookup.BookRecord> catalog, String targetIsbn) {
        int low = 0;
        int high = catalog.size();
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (catalog.get(mid).isbn().compareTo(targetIsbn) < 0) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    public static String findBook(List<P3LibraryCatalogLookup.BookRecord> catalog, String targetIsbn) {
        int index = lowerBound(catalog, targetIsbn);
        if (index < catalog.size() && catalog.get(index).isbn().equals(targetIsbn)) {
            return catalog.get(index).title();
        }
        return "Not Found";
    }

    public static void main(String[] args) {
        List<P3LibraryCatalogLookup.BookRecord> catalog = List.of(
                new P3LibraryCatalogLookup.BookRecord("0001112223", "Introduction to Algebra"),
                new P3LibraryCatalogLookup.BookRecord("0002223334", "Beginning Python"),
                new P3LibraryCatalogLookup.BookRecord("0003334445", "Classic Mythology"),
                new P3LibraryCatalogLookup.BookRecord("0004445556", "Data and Society"),
                new P3LibraryCatalogLookup.BookRecord("0005556667", "European History"));

        System.out.println(findBook(catalog, "0003334445"));
        System.out.println(findBook(catalog, "0009998887"));
    }
}
