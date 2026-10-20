package data_structures.class_problems;

// Warehouse Bin Grid Scan. Single row-major pass keeping a running total and
// the maximum bin seen so far.
// Complexity: O(m * n) time, O(1) extra space.
public class P2WarehouseBinGridScan {

    public record WarehouseSummary(int total, int row, int col) {
    }

    public static WarehouseSummary warehouseSummary(int[][] grid) {
        int total = 0;
        int maxValue = Integer.MIN_VALUE;
        int maxRow = -1;
        int maxCol = -1;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                int value = grid[row][col];
                total += value;
                if (value > maxValue) {
                    maxValue = value;
                    maxRow = row;
                    maxCol = col;
                }
            }
        }
        return new WarehouseSummary(total, maxRow, maxCol);
    }

    public static void main(String[] args) {
        int[][] grid = {
                {4, 9, 2},
                {7, 1, 6},
                {3, 12, 5}
        };
        WarehouseSummary summary = warehouseSummary(grid);
        System.out.println("total = " + summary.total() + ", maxCoordinate = ("
                + summary.row() + ", " + summary.col() + ")");
    }
}
