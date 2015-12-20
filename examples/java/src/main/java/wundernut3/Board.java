package wundernut3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static wundernut3.BoardPosition.*;
import static wundernut3.EdgeDirection.*;

public class Board {

    final class NeighborLink {
        public final EdgeDirection direction;
        public final BoardPosition neighbor;

        public NeighborLink(EdgeDirection direction, BoardPosition neighbor) {
            this.direction = direction;
            this.neighbor = neighbor;
        }
    }

    private final Map<BoardPosition, List<NeighborLink>> neighborLinks;

    public Board() {
        neighborLinks = generateNeighborLinks();
    }

    public List<NeighborLink> getNeighbors(BoardPosition position) {
        return neighborLinks.get(position);
    }

    private List<NeighborLink> neighborList(NeighborLink... links) {
        return Arrays.asList(links);
    }

    private Map<BoardPosition, List<NeighborLink>> generateNeighborLinks() {
        Map<BoardPosition, List<NeighborLink>> links = new HashMap<>();

        links.put(Pos1, neighborList(new NeighborLink(Down, Pos3)));
        links.put(Pos2, neighborList(new NeighborLink(UpRight, Pos3), new NeighborLink(Down, Pos6)));
        links.put(Pos3, neighborList(new NeighborLink(Up, Pos1), new NeighborLink(DownRight, Pos4), new NeighborLink(DownLeft, Pos2)));
        links.put(Pos4, neighborList(new NeighborLink(UpLeft, Pos3), new NeighborLink(Down, Pos8)));
        links.put(Pos5, neighborList(new NeighborLink(UpRight, Pos6)));
        links.put(Pos6, neighborList(new NeighborLink(Up, Pos2), new NeighborLink(DownRight, Pos7), new NeighborLink(DownLeft, Pos5)));
        links.put(Pos7, neighborList(new NeighborLink(UpLeft, Pos6), new NeighborLink(UpRight, Pos8)));
        links.put(Pos8, neighborList(new NeighborLink(Up, Pos4), new NeighborLink(DownRight, Pos9), new NeighborLink(DownLeft, Pos7)));
        links.put(Pos9, neighborList(new NeighborLink(UpLeft, Pos8)));

        return links;
    }
}
