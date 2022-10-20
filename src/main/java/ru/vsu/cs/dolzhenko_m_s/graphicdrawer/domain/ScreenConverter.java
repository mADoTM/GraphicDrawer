package ru.vsu.cs.dolzhenko_m_s.graphicdrawer.domain;

public class ScreenConverter {
    private double centerX;
    private double centerY;
    private double realWidth;
    private double realHeight;

    private int screenWidth;
    private int screenHeight;

    public ScreenConverter(double centerX, double centerY, double realWidth, double realHeight, int screenWidth, int screenHeight) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.realWidth = realWidth;
        this.realHeight = realHeight;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void changeScale(double scale) {
        realWidth *= scale;
        realHeight *= scale;
    }

    public ScreenPoint realPointToScreen(RealPoint realPoint) {
        double x = (realPoint.getX() - centerX) / realWidth * screenWidth;
        double y = (centerY - realPoint.getY()) / realHeight * screenHeight;

        return new ScreenPoint((int) x, (int) y);
    }

    public RealPoint screenPointToReal(ScreenPoint screenPoint) {
        double x = centerX + screenPoint.getX() * realWidth / screenWidth;
        double y = centerY - screenPoint.getY() * realHeight / screenHeight;

        return new RealPoint(x, y);
    }

    public void moveCorner(RealPoint delta) {
        centerY += delta.getY();
        centerX += delta.getX();
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
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }
}
