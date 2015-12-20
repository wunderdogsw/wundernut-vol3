package wundernut3;

import java.util.*;

import static wundernut3.Edge.*;

public final class Pieces {
    public static final Triangle P1 = new Triangle("P1", FoxHead, FoxTorso, DeerHead);
    public static final Triangle P2 = new Triangle("P2", DeerHead, FoxTorso, RaccoonTorso);
    public static final Triangle P3 = new Triangle("P3", DeerHead, FoxTorso, FoxHead);
    public static final Triangle P4 = new Triangle("P4", DeerHead, DeerTorso, FoxTorso);
    public static final Triangle P5 = new Triangle("P5", DeerHead, RaccoonTorso, DeerTorso);
    public static final Triangle P6 = new Triangle("P6", RaccoonTorso, FoxTorso, RaccoonHead);
    public static final Triangle P7 = new Triangle("P7", FoxTorso, RaccoonHead, FoxHead);
    public static final Triangle P8 = new Triangle("P8", RaccoonHead, DeerHead, RaccoonTorso);
    public static final Triangle P9 = new Triangle("P9", FoxTorso, DeerTorso, DeerHead);
    public static final List<Triangle> ALL_PIECES = Arrays.asList(new Triangle[]{P1, P2, P3, P4, P5, P6, P7, P8, P9});
    private static final Map<String, Triangle> PIECE_INDEX = new HashMap<>();

    static {
        for (Triangle piece : ALL_PIECES) {
            PIECE_INDEX.put(piece.getName(), piece);
        }
    }

    public static Triangle getNormalizedMatch(Triangle triangle) {
        return PIECE_INDEX.get(triangle.getName());
    }

    private Pieces() {
    }
}
