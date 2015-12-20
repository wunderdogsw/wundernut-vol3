package wundernut3;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static wundernut3.Pieces.*;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution(P1, P2, P3, P4, P5, P6, P7, P8, P9);

    @Test(expected = IllegalArgumentException.class)
    public void creatingSolutionFailsWithTooFewTriangles() {
        new Solution(P1, P2, P3, P4, P5, P6, P7, P8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void creatingSolutionFailsWithTooManyTriangles() {
        new Solution(P1, P2, P3, P4, P5, P6, P7, P8, P9, P1);
    }

    @Test
    public void rotatingSolutionGivesTwoNewCorrectSolutions() {
        List<Solution> rotatedSolutions = SOLUTION.getRotatedVersions();
        assertEquals(2, rotatedSolutions.size());

        String expected1 = new Solution(P5, P7, P6, P2, P9, P8, P4, P3, P1).toString();
        String expected2 = new Solution(P9, P4, P8, P7, P1, P3, P2, P6, P5).toString();
        assertEquals(expected1, rotatedSolutions.get(0).toString());
        assertEquals(expected2, rotatedSolutions.get(1).toString());
    }
}
