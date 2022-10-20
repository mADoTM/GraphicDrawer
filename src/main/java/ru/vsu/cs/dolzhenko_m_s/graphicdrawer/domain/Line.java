package ru.vsu.cs.dolzhenko_m_s.graphicdrawer.domain;

public class Line {
    private RealPoint startPoint;
    private RealPoint endPoint;

    public Line(RealPoint startPoint, RealPoint endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public Line(double x1, double y1, double x2, double y2) {
        this(new RealPoint(x1, y1), new RealPoint(x2, y2));
    }

    public RealPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(RealPoint startPoint) {
        this.startPoint = startPoint;
    }

    public RealPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(RealPoint endPoint) {
        this.endPoint = endPoint;
    }
}
