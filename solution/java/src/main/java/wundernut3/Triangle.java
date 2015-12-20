package wundernut3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static wundernut3.EdgeDirection.*;

public class Triangle {
    private final static Map<EdgeDirection, Integer> EDGE_DIRECTION_MAPPER;
    private final List<Edge> edges;
    private final String name;
    private Orientation orientation = Orientation.Original;

    static {
        EDGE_DIRECTION_MAPPER = new HashMap<>();
        EDGE_DIRECTION_MAPPER.put(UpLeft, 0);
        EDGE_DIRECTION_MAPPER.put(Up, 0);
        EDGE_DIRECTION_MAPPER.put(UpRight, 1);
        EDGE_DIRECTION_MAPPER.put(DownRight, 1);
        EDGE_DIRECTION_MAPPER.put(Down, 2);
        EDGE_DIRECTION_MAPPER.put(DownLeft, 2);
    }

    public Triangle(String name, Edge edge1, Edge edge2, Edge edge3) {
        this(name, edge1, edge2, edge3, Orientation.Original);
    }

    private Triangle(String name, Edge edge1, Edge edge2, Edge edge3, Orientation orientation) {
        this.name = name;
        edges = Arrays.asList(edge1, edge2, edge3);
        this.orientation = orientation;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public Triangle rotate() {
        return new Triangle(name, edges.get(2), edges.get(0), edges.get(1), orientation.next());
    }

    public Edge getEdge(EdgeDirection direction) {
        return edges.get(EDGE_DIRECTION_MAPPER.get(direction));
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
