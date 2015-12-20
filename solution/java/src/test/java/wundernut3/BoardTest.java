package wundernut3;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static wundernut3.BoardPosition.*;

public class BoardTest {
    Board board = new Board();

    @Test
    public void gettingNeighborsForVertexPositionReturnsOneCorrectNeighbor() {
        List<Board.NeighborLink> neighbors = board.getNeighbors(Pos1);
        assertEquals(1, neighbors.size());
        assertEquals(EdgeDirection.Down, neighbors.get(0).direction);
        assertEquals(Pos3, neighbors.get(0).neighbor);
    }

    @Test
    public void gettingNeighborsForInsidePositionReturnsThreeCorrectNeighbors() {
        List<Board.NeighborLink> neighbors = board.getNeighbors(Pos6);
        assertEquals(3, neighbors.size());
        assertEquals(EdgeDirection.Up, neighbors.get(0).direction);
        assertEquals(EdgeDirection.DownRight, neighbors.get(1).direction);
        assertEquals(EdgeDirection.DownLeft, neighbors.get(2).direction);
        assertEquals(Pos2, neighbors.get(0).neighbor);
        assertEquals(Pos7, neighbors.get(1).neighbor);
        assertEquals(Pos5, neighbors.get(2).neighbor);
    }
}
