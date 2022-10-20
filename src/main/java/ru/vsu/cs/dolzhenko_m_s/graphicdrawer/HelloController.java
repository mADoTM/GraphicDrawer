package ru.vsu.cs.dolzhenko_m_s.graphicdrawer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import ru.vsu.cs.dolzhenko_m_s.graphicdrawer.domain.*;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final ScreenConverter screenConverter = new ScreenConverter(-1, 1, 2, 2, WIDTH, HEIGHT);

    private ScreenPoint startPoint = null;

    @FXML
    private TextField functionField;

    @FXML
    private Canvas drawingCanvas = new Canvas(HEIGHT, WIDTH);

    private GraphicsContext graphicsContext;

    private final double SCALE_STEP = 0.001;

    private double DASHES_STEP = 0.1;

    private int currentScroll = 0;

    private double border = 10 * Math.pow(2, currentScroll);

    private double delta = 0;

    private Function function;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicsContext = drawingCanvas.getGraphicsContext2D();
        repaint();
    }

    @FXML
    private void onScroll(ScrollEvent e) {
        int clicks = -(int) e.getTextDeltaY() / 2;
        currentScroll += clicks;
        border *= Math.pow(2, clicks);
        double coefficient = 1 + SCALE_STEP * (clicks < 0 ? 1 : -1);
        double scale = 1;

        for (int i = Math.abs(clicks); i > 0; i--) {
            scale *= coefficient;
        }

        screenConverter.changeScale(scale);

        repaint();
    }

    private void repaint() {
        paint(graphicsContext);
    }

    @FXML
    private void onMousePressed(MouseEvent e) {
        startPoint = new ScreenPoint((int) e.getX(), (int) e.getY());
    }

    @FXML
    private void onMouseDragged(MouseEvent e) {
        ScreenPoint newPoint = new ScreenPoint((int) e.getX(), (int) e.getY());
        RealPoint p1 = screenConverter.screenPointToReal(newPoint);
        RealPoint p2 = screenConverter.screenPointToReal(startPoint);

        RealPoint deltaPoint = p2.minus(p1);
        screenConverter.moveCorner(deltaPoint);
        delta = (int) (Math.sqrt(Math.pow(screenConverter.getCenterX(), 2) + Math.pow(screenConverter.getCenterY(), 2)) * 15);

        startPoint = newPoint;
        repaint();
    }

    protected void paint(GraphicsContext graphicsContext) {
        screenConverter.setScreenHeight(HEIGHT);
        screenConverter.setScreenWidth(WIDTH);
        graphicsContext.clearRect(0, 0, WIDTH, HEIGHT);

        drawTopics(graphicsContext);
        drawDashes(graphicsContext);

        if(function != null)
            drawFunction(graphicsContext);

        drawAsixs(graphicsContext);
    }

    private void drawAsixs(GraphicsContext graphicsContext) {
        Line oX = new Line(new RealPoint(-1 - delta, 0), new RealPoint(1 + delta, 0));
        Line oY = new Line(new RealPoint(0, -1  - delta), new RealPoint(0, 1 + delta));
        drawLine(graphicsContext, oX);
        drawLine(graphicsContext, oY);
    }

    private void drawFunction(GraphicsContext graphicsContext) {
        RealPoint prev = null;
        for (double i = -1 * border * Math.pow(2, Math.abs(currentScroll)) - delta; i < Math.pow(2, Math.abs(currentScroll)) * border + delta; i += DASHES_STEP * 0.5) {
            double realY = 0;
            try {
                realY = function.compute(i);
            } catch (Exception e) {
                System.out.println("exception thrown");
                prev = null;
                continue;
            }
            var current = new RealPoint(i / border, realY / border);
            if (prev != null)
                drawLine(graphicsContext, new Line(prev, current));
            prev = current;
        }
    }

    private void drawDashes(GraphicsContext graphicsContext) {

        for (double i = -1 * border * Math.pow(2, Math.abs(currentScroll)) - delta; i < 1 * border * Math.pow(2, Math.abs(currentScroll)) + delta; i += Math.pow(2, currentScroll)) {
            RealPoint upX = new RealPoint(i / border, 0 + 0.02);
            RealPoint downX = new RealPoint(i / border, 0 - 0.02);
            drawLine(graphicsContext, new Line(upX, downX));

            RealPoint textXpoint = new RealPoint(i / border, 0 - 0.04);
            var screenPointX = screenConverter.realPointToScreen(textXpoint);
            graphicsContext.fillText("" + i, screenPointX.getX(), screenPointX.getY());


            RealPoint upY = new RealPoint(0 + 0.02, i / border);
            RealPoint downY = new RealPoint(0 - 0.02, i / border);
            drawLine(graphicsContext, new Line(upY, downY));

            if (i != 0) {
                RealPoint textYpoint = new RealPoint(0 + 0.04, i / border);
                var screenPointY = screenConverter.realPointToScreen(textYpoint);
                graphicsContext.fillText("" + i, screenPointY.getX(), screenPointY.getY());
            }
        }

    }


    private void drawArrows(GraphicsContext graphicsContext) {
        RealPoint cornerA = new RealPoint(0, screenConverter.getRealHeight());
        RealPoint leftPartA = new RealPoint(0 - 0.01, screenConverter.getRealHeight() - 0.03);
        RealPoint rightPartA = new RealPoint(0 + 0.01, screenConverter.getRealHeight() - 0.03);
        Line line1 = new Line(cornerA, leftPartA);
        Line line2 = new Line(cornerA, rightPartA);
        drawLine(graphicsContext, line1);
        drawLine(graphicsContext, line2);


        RealPoint cornerB = new RealPoint(screenConverter.getRealWidth(), 0);
        RealPoint upperPartB = new RealPoint(screenConverter.getRealWidth() - 0.03, 0 + 0.01);
        RealPoint downPartB = new RealPoint(screenConverter.getRealWidth() - 0.03, 0 - 0.01);
        Line line3 = new Line(cornerB, upperPartB);
        Line line4 = new Line(cornerB, downPartB);
        drawLine(graphicsContext, line3);
        drawLine(graphicsContext, line4);
    }

    private void drawTopics(GraphicsContext g) {
        RealPoint topicX = new RealPoint(1 - 0.05, 0 - 0.05);
        RealPoint topicY = new RealPoint(0 + 0.05, 1 - 0.05);

        var screenX = screenConverter.realPointToScreen(topicX);
        var screenY = screenConverter.realPointToScreen(topicY);

        g.fillText("x", screenX.getX(), screenX.getY());
        g.fillText("y", screenY.getX(), screenY.getY());
    }

    private void drawLine(GraphicsContext g, Line line) {
        ScreenPoint p1 = screenConverter.realPointToScreen(line.getStartPoint());
        ScreenPoint p2 = screenConverter.realPointToScreen(line.getEndPoint());

        g.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    @FXML
    private void computeFunction(ActionEvent actionEvent) {
        if(functionField.getText() != null) {
            function = new MatchParser(functionField.getText());
            repaint();
        }
    }
}