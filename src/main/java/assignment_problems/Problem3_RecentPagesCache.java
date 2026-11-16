package assignment_problems;

import java.util.HashMap;
import java.util.Scanner;

/**
 * CATEGORY A - CODING ASSIGNMENT, PROBLEM 3: Recent Pages Cache (LRU)
 *
 * TASK
 * Keep the most recently opened pages in a small cache. When full and a new page arrives, the
 * least recently used (LRU) page is evicted. Reading or updating a page makes it the most
 * recently used (MRU). get and put must both be O(1). Own doubly linked list (prev/next) +
 * HashMap from key to node; java.util.LinkedHashMap is not used.
 *
 * DESIGN
 * - HashMap<Integer, Node>  : O(1) lookup of a page's node by key.
 * - Doubly linked list      : O(1) move-to-front and O(1) removal of any node, because a node
 *                             knows BOTH its neighbours. A SINGLY linked list cannot do this in
 *                             O(1): to unlink a node you must fix its predecessor's next link,
 *                             and finding that predecessor costs O(n) from the head.
 * - Dummy head + dummy tail : insertion/removal never needs "empty list" or "edge node" special
 *                             cases. The node just before the tail is always the LRU page; the
 *                             node just after the head is always the MRU page.
 *
 * COMPLEXITY
 * - get : O(1)  (map lookup + at most 4 pointer updates to move the node to the front)
 * - put : O(1)  (map lookup/insert + constant pointer updates; eviction is O(1) too)
 * - Space: O(capacity) - at most that many real nodes plus two dummy nodes.
 *
 * INPUT  : capacity, then one operation per line: "put(key,page)" or "get(key)"
 *          (also tolerates "put key page" / "get key").
 * OUTPUT : one line per operation (page or -1 for get; the evicted key for put),
 *          then the cache from most to least recent.
 *
 * Run:   java -cp target/classes assignment_problems.Problem3_RecentPagesCache < input.txt
 */
public class Problem3_RecentPagesCache {

    /** Node of the hand-built doubly linked list (no java.util.LinkedHashMap). */
    private static class Node {
        int key;
        String page;
        Node prev;
        Node next;

        Node(int key, String page) {
            this.key = key;
            this.page = page;
        }
    }

    private final int capacity;
    private final HashMap<Integer, Node> map = new HashMap<>();
    private final Node head;    // dummy: head.next is always the MRU node
    private final Node tail;    // dummy: tail.prev is always the LRU node

    public Problem3_RecentPagesCache(int capacity) {
        this.capacity = capacity;
        head = new Node(0, null);
        tail = new Node(0, null);
        head.next = tail;
        tail.prev = head;
    }

    /** O(1): returns the page, or null when the key is absent. Makes the page MRU. */
    public String get(int key) {
        Node node = map.get(key);
        if (node == null) {
            return null;
        }
        moveToFront(node);
        return node.page;
    }

    /** O(1): insert/update a page; evicts the LRU entry when the cache is full. */
    public void put(int key, String page) {
        Node node = map.get(key);
        if (node != null) {                 // existing key: update value, refresh recency
            node.page = page;
            moveToFront(node);
            return;
        }
        if (map.size() == capacity) {       // full: evict the node just before the tail (LRU)
            Node lru = tail.prev;
            unlink(lru);
            map.remove(lru.key);
        }
        node = new Node(key, page);
        map.put(key, node);
        linkAfterHead(node);
    }

    // ---------------------------------------------------------------- list plumbing (all O(1))
    private void unlink(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.prev = node.next = null;       // fully detach
    }

    private void linkAfterHead(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void moveToFront(Node node) {
        unlink(node);
        linkAfterHead(node);
    }

    /** "Cache (most recent first): 1=Home2, 4=Mail" */
    public String describe() {
        StringBuilder sb = new StringBuilder("Cache (most recent first): ");
        boolean first = true;
        for (Node cur = head.next; cur != tail; cur = cur.next) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(cur.key).append('=').append(cur.page);
            first = false;
        }
        if (first) {
            sb.append("(empty)");
        }
        return sb.toString();
    }

    private static final String DEMO = String.join("\n",
            "2",
            "put(1,Home)",
            "put(2,Notes)",
            "get(1)",
            "put(3,Quiz)",
            "get(2)",
            "put(1,Home2)",
            "put(4,Mail)",
            "get(3)",
            "get(1)",
            "");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNext()) {                // nothing piped in -> run the example from the PDF
            sc = new Scanner(DEMO);
        }

        int capacity = Integer.parseInt(sc.next());
        Problem3_RecentPagesCache cache = new Problem3_RecentPagesCache(capacity);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.isBlank()) {
                continue;
            }
            // Normalise "put(1,Home)" / "put 1 Home" / "put(1, Home)" into op + tokens
            String[] tokens = line.replace("(", " ").replace(")", " ")
                    .replace(",", " ").trim().split("\\s+");
            String op = tokens[0];
            int key = Integer.parseInt(tokens[1]);
            if (op.equalsIgnoreCase("put")) {
                String page = tokens.length > 2 ? tokens[2] : "";
                boolean wasPresent = cache.map.containsKey(key);
                // peek at the LRU (node just before the dummy tail) to report evictions
                int evicted = (!wasPresent && cache.map.size() == cache.capacity)
                        ? cache.tail.prev.key : -1;
                cache.put(key, page);
                if (wasPresent) {
                    System.out.println("put(" + key + "," + page + ") updated");
                } else if (evicted != -1) {
                    System.out.println("put(" + key + "," + page + ") evicted " + evicted);
                } else {
                    System.out.println("put(" + key + "," + page + ")");
                }
            } else if (op.equalsIgnoreCase("get")) {
                String page = cache.get(key);
                System.out.println("get(" + key + ") -> " + (page != null ? page : "-1"));
            }
        }

        System.out.println(cache.describe());
    }
}
