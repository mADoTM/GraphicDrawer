package ru.vsu.cs.dolzhenko_m_s.graphicdrawer.domain;

public class ScreenConverter {
    private double cornerX;
    private double cornerY;
    private double realWidth;
    private double realHeight;

    private int screenWidth;
    private int screenHeight;

    public ScreenConverter(double centerX, double centerY, double realWidth, double realHeight, int screenWidth, int screenHeight) {
        this.cornerX = centerX;
        this.cornerY = centerY;
        this.realWidth = realWidth;
        this.realHeight = realHeight;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void changeScale(double scale) {
        realWidth = scale;
        realHeight = scale;
    }

    public ScreenPoint realPointToScreen(RealPoint realPoint) {
        double x = (realPoint.getX() - cornerX) / realWidth * screenWidth;
        double y = (cornerY - realPoint.getY()) / realHeight * screenHeight;

        return new ScreenPoint((int) x, (int) y);
    }

    public RealPoint screenPointToReal(ScreenPoint screenPoint) {
        double x = cornerX + screenPoint.getX() * realWidth / screenWidth;
        double y = cornerY - screenPoint.getY() * realHeight / screenHeight;

        return new RealPoint(x, y);
    }

    public void moveCorner(RealPoint delta) {
        cornerY += delta.getY();
        cornerX += delta.getX();
    }

    public double getRealXFromScreen(double x) {
        return cornerX + x * realWidth / screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public double getRealWidth() {
        return realWidth;
    }

    public double getRealHeight() {
        return realHeight;
    }

    public double getCenterX() {
        return cornerX;
    }

    public double getCenterY() {
        return cornerY;
    }
}
