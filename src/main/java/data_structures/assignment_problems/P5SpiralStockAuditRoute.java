package data_structures.assignment_problems;

import java.util.ArrayList;
import java.util.List;

// Spiral Stock Audit Route. Four shrinking boundaries walk the grid clockwise,
// and the crossings are checked so a leftover single row or column is not
// visited twice.
// Complexity: O(m * n) time, O(1) extra space apart from the output list.
public class P5SpiralStockAuditRoute {

    public static List<Integer> auditRoute(int[][] grid) {
        List<Integer> route = new ArrayList<>();
        int top = 0;
        int bottom = grid.length - 1;
        int left = 0;
        int right = grid[0].length - 1;

        while (top <= bottom && left <= right) {
            for (int col = left; col <= right; col++) {
                route.add(grid[top][col]);
            }
            top++;

            for (int row = top; row <= bottom; row++) {
                route.add(grid[row][right]);
            }
            right--;

            if (top <= bottom) {
                for (int col = right; col >= left; col--) {
                    route.add(grid[bottom][col]);
                }
                bottom--;
            }

            if (left <= right) {
                for (int row = bottom; row >= top; row--) {
                    route.add(grid[row][left]);
                }
                left++;
            }
        }
        return route;
    }

    public static void main(String[] args) {
        int[][] grid = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}
        };
        System.out.println(auditRoute(grid));
    }
}
