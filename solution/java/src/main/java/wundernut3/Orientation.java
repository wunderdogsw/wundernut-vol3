package wundernut3;

public enum Orientation {
    Original, RotatedOnce, RotatedTwice;

    public Orientation next() {
        if (this == Original) return RotatedOnce;
        if (this == RotatedOnce) return RotatedTwice;
        return Original;
    }
}
