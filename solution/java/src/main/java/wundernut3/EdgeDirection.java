package wundernut3;

public enum EdgeDirection {
    UpLeft, Up, UpRight, DownRight, Down, DownLeft;

    public EdgeDirection getOpposite() {
        switch (this) {
            case UpLeft:
                return DownRight;
            case Up:
                return Down;
            case UpRight:
                return DownLeft;
            case DownRight:
                return UpLeft;
            case Down:
                return Up;
            case DownLeft:
                return UpRight;
        }

        throw new IllegalArgumentException("Getting opposite for direction that has no opposite defined");
    }
}
