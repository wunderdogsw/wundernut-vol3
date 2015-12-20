package wundernut3;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static wundernut3.Edge.*;
import static wundernut3.EdgeDirection.*;
import static wundernut3.Pieces.*;

public class TriangleTest {
    private static final Edge FIRST_EDGE = FoxHead;
    private static final Edge SECOND_EDGE = FoxTorso;
    private static final Edge THIRD_EDGE = RaccoonHead;
    private final Triangle triangle = new Triangle("test", FIRST_EDGE, SECOND_EDGE, THIRD_EDGE);

    @Test
    public void newTriangleReportsEdgesCorrectly() {
        assertEquals(FIRST_EDGE, triangle.getEdge(UpLeft));
        assertEquals(SECOND_EDGE, triangle.getEdge(UpRight));
        assertEquals(THIRD_EDGE, triangle.getEdge(Down));
    }

    @Test
    public void rotatingNewTriangleChangesOrientationCorrectly() {
        Triangle rotated = triangle.rotate();
        assertEquals(Orientation.RotatedOnce, rotated.getOrientation());
    }

    @Test
    public void onceRotatedTriangleReportsEdgesCorrectly() {
        Triangle rotated = triangle.rotate();
        assertEquals(THIRD_EDGE, rotated.getEdge(UpLeft));
        assertEquals(FIRST_EDGE, rotated.getEdge(UpRight));
        assertEquals(SECOND_EDGE, rotated.getEdge(Down));
    }

    @Test
    public void threeTimesRotatedTriangleHasEdgesLikeOnCreation() {
        Triangle rotated = triangle.rotate().rotate().rotate();
        assertEquals(FIRST_EDGE, rotated.getEdge(UpLeft));
        assertEquals(SECOND_EDGE, rotated.getEdge(UpRight));
        assertEquals(THIRD_EDGE, rotated.getEdge(Down));
    }
}
