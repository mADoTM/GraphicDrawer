package ru.vsu.cs.dolzhenko_m_s.graphicdrawer.domain;

public final class RealPoint {
    private final double x;
    private final double y;

    public RealPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public RealPoint minus(RealPoint another) {
        return new RealPoint(x - another.x, y - another.y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
