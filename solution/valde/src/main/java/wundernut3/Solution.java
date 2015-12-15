package wundernut3;

import java.util.Arrays;
import java.util.List;

public class Solution {
    private final List<Triangle> triangles;

    public Solution(Triangle... triangles) {
        if (triangles.length != 9) {
            throw new IllegalArgumentException("Incorrect number of triangles in solution");
        }

        this.triangles = Arrays.asList(triangles);
    }

    public List<Solution> getRotatedVersions() {
        Solution rotatedOnce = rotate(this);
        Solution rotatedTwice = rotate(rotatedOnce);
        return Arrays.asList(new Solution[]{rotatedOnce, rotatedTwice});
    }

    private Solution rotate(Solution original) {
        return new Solution(
                original.triangles.get(4),
                original.triangles.get(6),
                original.triangles.get(5),
                original.triangles.get(1),
                original.triangles.get(8),
                original.triangles.get(7),
                original.triangles.get(3),
                original.triangles.get(2),
                original.triangles.get(0)
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Triangle triangle : triangles) {
            sb.append(triangle);
            sb.append(", ");
        }
        sb.replace(sb.lastIndexOf(", "), sb.length() - 1, "]");
        return sb.toString();
    }
}
