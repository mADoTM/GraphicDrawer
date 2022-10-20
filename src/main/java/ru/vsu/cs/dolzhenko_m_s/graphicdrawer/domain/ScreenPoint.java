package ru.vsu.cs.dolzhenko_m_s.graphicdrawer.domain;

public final class ScreenPoint {
    private final int x;
    private final int y;

    public ScreenPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
